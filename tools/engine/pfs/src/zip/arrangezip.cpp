#include "../common.h"
#include "arrangezip.h"
#include <time.h>
#include <sstream>
#include <iomanip>

//
// MT_ZIPUPDATE : 这种类型的MOUNT方式，用于特殊处理ZPD压缩格式的文件。
// 另外，标记这种类型的MOUNT方式，我们会将原来的zip包的签名改为自定义的PFS格式签名。
// 如果没有此标记，而仅仅有MT_WRITABLE标记的话，我们会修改PFS格式为ZIP格式。
//
// 本文件中多次出现的 bZipUpdate 不完全等价于 MT_ZIPUPDATE。首先，MT_ZIPUPDATE决定了bZipUpdate是否可能为true。
// 但，对于.files.meta文件，bZipUpdate不置为true。也就是说，bZipUpdate是根据文件动态设置的。
// 这种策略源于历史代码。尽管不是很好的处理方式，暂时保留，不做修改。
//
// yanghaibo
// 2010-11-11

namespace PFS
{

namespace helper
{
	enum 
	{ 
		BUFFERSIZE = 65536,		// 64K 
		MINFULLSIZE = 32,		// 空闲块最小值
	};

	CArrangeZip::CArrangeZip( CZipFS* zfs) 
		: m_zfs( zfs)
	{
		m_out.Reserve( BUFFERSIZE);

		// 如果是PFS包更新，则用PFS包格式，否则用ZIP包格式。
		// 在实现pfspatcher的时候发现，如果这里不正确设置zipwriter的写方式，
		// 会导致zip格式的data.pfs在打完补丁之后变成pfs格式，而这种副作用不是我们所希望的。
		// 这个问题的详细解释，参见pfspatcher相关的说明。
		// yanghaibo
		// 2010-11-11
		m_zipwriter.SetIsZip( !PFS::IsMountZipUpdate( m_zfs->GetMountType() ) );
	}

	void CArrangeZip::SetFileCentral( ZIPFILE::CFileCentral& fc, const std::wstring& filename, 
		size_t size, unsigned short usMethod, bool& bZipUpdate)
	{
		const PFSX::TSID ZpdID = PFSX::MakeTypeIDByName( 'Z', 'P', 'D');

		fc.m_name = filename;

		bZipUpdate = false;

		std::wstring::size_type len = filename.size();
		assert( len != 0);
		if( filename[len-1] == L'/')
		{
			// 是目录
			fc.m_usMethod = 0;
			fc.m_uiSizeNow = 0;
			fc.m_uiSizeOrg = 0;
			return;
		}

		// 无论是不是MT_ZIPUPDATE，.files.meta都是特殊文件，不需要根据meta处理。
		// 这源于，.files.meta文件中不记录自身的有效的meta信息。
		if( PFS::IsMountZipUpdate( m_zfs->GetMountType() ) && 
			wcscmp( filename.c_str(), L".files.meta") != 0 )
		{
			std::wstring temp;
			temp.reserve( 1 + len );
			temp.append( L"/");
			temp.append( filename);
			PFS::CMetaInfoMap& mim = m_zfs->GetMetaInfoMap();
			PFS::CMetaInfoMap::const_iterator i = mim.find( temp);
			if( i == mim.end() )
			{
				CEnv::GetOutLogInstance()->Print(L"ERROR: - exception case occurred for setting file info (%s).\n", filename.c_str());

				// 出现这种case是不正常的。
				// 但为了避免该zip文件无法被读取，这里不置bZipUpdate = true，
				// 可以在CFileWriter::WriteFileData()中计算CRC32值，以保证zip文件能被正常读取。
				// 此时，只能不信任传入的size值，而直接根据写的数据计算size。
				// yanghaibo
				// 2010-11-11
				fc.m_usMethod = 0;
				fc.m_uiSizeNow = 0;
				fc.m_uiSizeOrg = 0;
			}
			else
			{
				// 这种方式的CRC32,SizeOrg等原始数据信息从meta中读取，而直接将临时文件中的数据当做已压缩的数据块写入包中。
				bZipUpdate = true; 
				const PFS::CMetaInfo& meta = i->second;

				// 根据attribute来设置fc
				fc.m_uiCRC32 = meta.GetCRC32();
				fc.m_uiSizeOrg = (unsigned int)meta.GetFileSize();	// 源文件的大小
				PFSX::TSID attr = meta.GetTSID();
				if( ZpdID == attr )
				{
					fc.m_usMethod = 8;
					fc.m_uiSizeNow = size;
				}
				else
				{
					fc.m_usMethod = 0;
					fc.m_uiSizeNow = fc.m_uiSizeOrg;
				}
			}
		}
		else
		{
			// 普通zip的mount类型
			fc.m_usMethod = usMethod;
			fc.m_uiSizeNow = 0;
			fc.m_uiSizeOrg = 0;
		}
	}

	bool CArrangeZip::WriteFileData( PFS::CBaseFile* basefile, bool bZipUpdate)
	{
		basefile->Seek( 0, FSM_SET);
		fsize_t len = basefile->GetSize();
		fsize_t read = 0;
		size_t current = 0;
		while( true)
		{
			current = basefile->Read( m_out.GetData(), BUFFERSIZE);
			if( FILE_SIZE_ERROR == current)
			{
				return false;
			}
			m_out.Resize( current);

			read += current;
			bool bFinish = current ? ( read >= len) : true;
			if( bZipUpdate )
			{
				m_zipwriter.WriteDeflateData( m_out, false);
			}
			else
			{
				// 这里出错会抛异常，不用判断返回值
				m_zipwriter.WriteFileData( m_out, bFinish);
			}
			if( bFinish)
			{
				break;
			}
		}
		return true;
	}

	void CArrangeZipCompact::Arrange()
	{
		CEnv::GetOutLogInstance()->Print(L"INFO: Compact BEGIN\n" );
		if( Create() )
		{
			Init();
			WriteNoModify();
			if( !m_vtModify.empty() )
				WriteModify(m_vtModify);
			if( !m_vtLastModify.empty() )
				WriteModify(m_vtLastModify);
			End();
		}
		else
		{
			assert( false);
		}

		CEnv::GetOutLogInstance()->Print(L"INFO: Compact END\n" );
	}

	static std::wstring GetTempExtNameByTime()
	{
		static time_t nNow = 0;
		if( 0 == nNow)
			time( &nNow);
		else 
			nNow++;
		std::wostringstream ss;
		ss << std::setbase( 16);
		ss << L"." << nNow;
		return ss.str();
	}

	// m_bUseTempFile = true 先写临时文件再改名的方式，需要双倍磁盘空间
	//				  = false 直接在源文件上整理，不需多余磁盘空间，如果非法中断则源文件会被损坏，下次无法打开
	bool CArrangeZipCompact::Create()
	{
		if( m_bUseTempFile)
		{
			m_realfilename = m_zfs->GetNativeDevicePath();
			m_tempfilename = m_realfilename + GetTempExtNameByTime();
			if( !m_tempfile.Open( m_tempfilename, PFS::FM_CREAT|PFS::FM_EXCL, PFS::FA_WRONLY))
			{
				CEnv::GetOutLogInstance()->Print(L"ERROR: - create tempfile %s failed(%d).\n", m_tempfilename.c_str(), CEnv::GetLastError() );

				assert( false);
				throw CFileIOException( CEnv::GetLastError(), m_tempfilename );
			}

			CEnv::GetOutLogInstance()->Print(L"ERROR: - create tempfile %s for %s done.\n", m_tempfilename.c_str(), m_realfilename.c_str() );

			m_zipwriter.SetFileIO( m_tempfile);
		}
		else
		{
			m_zipwriter.SetFileIO( *m_zfs->m_basefile);
		}

		return true;
	}

	void CArrangeZipCompact::End()
	{
		m_zipwriter.CloseFile();
		if( !m_bUseTempFile)
			return;
		m_zfs->CloseFile();	// 这里closefile是为了下面 _wrename 成功
		m_tempfile.Close();
		std::wstring temp = m_realfilename + GetTempExtNameByTime();

		if( 0 == _wrename( m_realfilename.c_str(), temp.c_str()))
		{
			if( 0 == _wrename( m_tempfilename.c_str(), m_realfilename.c_str()))
			{
				_wremove( temp.c_str());
			}
			else
			{
				_wrename( temp.c_str(), m_realfilename.c_str());
				_wremove( m_tempfilename.c_str());
			}
		}
		else
		{
			_wremove( m_tempfilename.c_str());
		}

		// 文件名未变但是文件内容是新的，这里要重新打开一次
		m_zfs->ReOpenFS();
	}

	static bool SortByOffset( const ZIPFILE::CFileCentral* left, const ZIPFILE::CFileCentral* right)
	{
		return left->m_uiOffset < right->m_uiOffset;
	}

	void CArrangeZipCompact::Init()
	{
		ZIPFILE::CFileReader::CFileCentralMap& centralmap = m_zfs->m_zipfile.GetFileCentralMap();
		m_vtModify.reserve( centralmap.size() / 2);
		m_vtNoModify.reserve( centralmap.size() / 2);
		ZIPFILE::CFileReader::CFileCentralMap::iterator iEnd = centralmap.end();
		for( ZIPFILE::CFileReader::CFileCentralMap::iterator itor = centralmap.begin(); itor != iEnd; ++itor)
		{
			ZIPFILE::CFileCentral& fc = itor->second;
			if( fc.IsFileDeleted())
				continue;
			if( fc.IsFileModified())
			{
				if( itor->first[0] == L'.' )
				{
					m_vtLastModify.push_back( &fc);
				}
				else
				{
					m_vtModify.push_back( &fc);
				}
			}
			else
			{
				m_vtNoModify.push_back( &fc);
			}
		}
		if( !m_bUseTempFile)
		{
			std::sort( m_vtNoModify.begin(), m_vtNoModify.end(), SortByOffset);
		}
	}

	void CArrangeZipCompact::WriteNoModify()
	{
		CFileCentralVector::iterator itor = m_vtNoModify.begin();
		const ZIPFILE::CFileCentral* central = NULL;
		if( !m_bUseTempFile)
		{
			// m_vtNoModify已经按offset排序了，从文件开头到第一个修改的文件之间的未修改文件就可以直接跳过
			unsigned int uiOffset = 0;
			while( itor != m_vtNoModify.end())
			{
				central = *itor;
				if( uiOffset != central->m_uiOffset)
					break;
				uiOffset = (unsigned int)central->GetDataOffset() + central->GetCompressedSize();
				m_zipwriter.AddFileCentral( *central);
				++itor;
			}
			m_zipwriter.SetOffset( uiOffset);
		}
		while( itor != m_vtNoModify.end())
		{
			// 写未修改的文件
			central = *itor;

			m_zipwriter.WriteNextCentral( *central);
			if( central->GetCompressedSize() != 0)
			{
				ZIPFILE::CFileCentralRead reader( m_zfs->m_zipfile, *central);
				while( (int)reader.ReadOrg( m_out, BUFFERSIZE ) > 0)
				{
					m_zipwriter.WriteDeflateData( m_out, true);
				}

				const ZIPFILE::CFileCentral* curcentral = m_zipwriter.GetCurCentral();

				// 调整新的central offset
				if( curcentral != NULL )
				{
					// 由于CArrangeZipCompact::End()函数会对ZipFS进行ReOpen()，所以，这里无论是否修改offset，
					// 都无关紧要。 所以记录日志更没有意义了。
					(*itor)->m_uiOffset = curcentral->m_uiOffset;
				}
				else
				{
					CEnv::GetOutLogInstance()->Print(L"ERROR: no central for file %s.\n", 
						central->GetFileName().c_str() );
				}
			}

			++itor;
		}
	}

	void CArrangeZipCompact::WriteModify( CFileCentralVector& vFileCentrals )
	{
		ZIPFILE::CFileCentral fc;
		ZIPFILE::CFileCentral* central = NULL;
		CFileCentralVector::iterator iEnd = vFileCentrals.end();
		bool bZipUpdate = false;

		CEnv::GetOutLogInstance()->Print(L"INFO: %u modified files is committing.\n", vFileCentrals.size() );

		for( CFileCentralVector::iterator itor = vFileCentrals.begin(); itor != iEnd; ++itor)
		{
			central = *itor;
			const std::wstring& filename = central->GetFileName();
			CZipTempFile* tempfile = m_zfs->m_tempfilemanager.GetTempFile( filename );
			if( NULL == tempfile)
			{
				CEnv::GetOutLogInstance()->Print(L"WARN: - no tempfile found for file %s.\n", filename.c_str() );
				continue;
			}
			
			SetFileCentral( fc, filename, 
				(size_t)tempfile->GetSize(), 
				central->m_usMethod, bZipUpdate);

			if( !m_zipwriter.WriteNextCentral( fc))
			{
				CEnv::GetOutLogInstance()->Print(L"WARN: - write file info failed for %s.\n", filename.c_str() );
				continue;
			}

			if( fc.m_usMethod == 0)
			{
				m_zipwriter.SetZipLevel( 0);
			}
			else
			{
				m_zipwriter.SetZipLevel( 1);
			}
			
			if( !WriteFileData( tempfile, bZipUpdate))
			{
				CEnv::GetOutLogInstance()->Print(L"WARN: - write file data failed for %s. zipupdate: %d\n", 
					filename.c_str(), bZipUpdate );
				continue;
			}

			const ZIPFILE::CFileCentral* curcentral = m_zipwriter.GetCurCentral();

			// 由于CArrangeZipCompact::End()函数会对ZipFS进行ReOpen()，所以，这里无论是否修改offset，
			// 都无关紧要。 所以记录日志更没有意义了。
			// 因此下面的central的字段是否更新，也无所谓了。
			// 不过，为了逻辑完整，这里仍旧更新相关的字段。
			// CEnv::GetOutLogInstance()->Print(L"INFO: - offset change: %hu --> %hu - %s.\n", 
			//	central->m_uiOffset, curcentral->m_uiOffset, filename.c_str() );
			central->m_uiOffset = curcentral->m_uiOffset; 
			central->m_uiSizeNow = curcentral->m_uiSizeNow;
			central->m_uiSizeOrg = curcentral->m_uiSizeOrg;
			central->m_uiCRC32 = curcentral->m_uiCRC32;
			central->m_usMethod = curcentral->m_usMethod;
			central->m_usModifyTime = curcentral->m_usModifyTime;
			central->m_usModifyDate = curcentral->m_usModifyDate;
			central->m_usFlag = curcentral->m_usFlag;
			central->m_usDiskNumberStart = curcentral->m_usDiskNumberStart;

			// 清除修改标记
			central->ClearModifiedFlag();

			// 临时文件可以关闭了。
			tempfile->Close();
		}
	}

	void CArrangeZipUnCompact::Arrange()
	{
		CEnv::GetOutLogInstance()->Print(L"INFO: UnCompact BEGIN\n" );

		if( Create() )
		{
			Init();
			WriteModify();
			End();
		}
		else
		{
			assert( false);
		}

		CEnv::GetOutLogInstance()->Print(L"INFO: UnCompact END\n" );
	}

	bool CArrangeZipUnCompact::Create()
	{
		m_zipwriter.SetFileIO( *m_zfs->m_basefile);
		return true;
	}

	void CArrangeZipUnCompact::Init()
	{
		ZIPFILE::CFileReader::CFileCentralMap& centralmap = m_zfs->m_zipfile.GetFileCentralMap();
		m_vtModify.reserve( centralmap.size() / 2);
		CFileCentralVector vtNoModify;
		vtNoModify.reserve( centralmap.size() / 2);

		ZIPFILE::CFileReader::CFileCentralMap::iterator iEnd = centralmap.end();
		for( ZIPFILE::CFileReader::CFileCentralMap::iterator itor = centralmap.begin(); itor != iEnd; ++itor)
		{
			ZIPFILE::CFileCentral& fc = itor->second;
			if( fc.IsFileDeleted())
				continue;
			
			if( fc.IsFileModified())
			{
				m_vtModify.push_back( &fc);
			}
			else
			{
				if( NULL == m_zipwriter.AddFileCentral( fc) )
				{
					CEnv::GetOutLogInstance()->Print(L"ERROR: - duplicated file %s found.\n", fc.GetFileName().c_str() );
					continue;
				}

				vtNoModify.push_back( &fc);
			}
		}
		// 下面求文件中所有的空闲块
		std::sort( vtNoModify.begin(), vtNoModify.end(), SortByOffset);
		m_vtEmptyBlock.reserve( vtNoModify.size());

		CEmptyBlock fullsize;
		const ZIPFILE::CFileCentral* central = NULL;
		unsigned int uiOffset = 0;
		for( CFileCentralVector::const_iterator itor = vtNoModify.begin(); itor != vtNoModify.end(); ++itor)
		{
			central = *itor;
			if( uiOffset != central->m_uiOffset)
			{
				fullsize.m_size = central->m_uiOffset - uiOffset;
				if( fullsize.m_size > MINFULLSIZE)
				{
					fullsize.m_offset = uiOffset;
					m_vtEmptyBlock.push_back( fullsize);
				}
			}
			uiOffset = central->GetDataOffset() + central->GetCompressedSize();
		}
		m_lastOffset = uiOffset;
		
		// 按m_size从小到大排序
		std::sort( m_vtEmptyBlock.begin(), m_vtEmptyBlock.end());
	}

	void CArrangeZipUnCompact::WriteModify()
	{
		CEnv::GetOutLogInstance()->Print(L"INFO: %u modified files is committing.\n", m_vtModify.size() );
		
		CFileCentralVector::iterator iEnd = m_vtModify.end();
		for( CFileCentralVector::iterator itor = m_vtModify.begin(); itor != iEnd; ++itor)
		{
			WriteOneModify( *itor);
		}
	}

	void CArrangeZipUnCompact::End()
	{
		m_zipwriter.SetOffset( m_lastOffset);
		m_zipwriter.CloseFile();
	}

	bool CArrangeZipUnCompact::WriteOneModify( ZIPFILE::CFileCentral* central)
	{
		const std::wstring& filename = central->GetFileName();
		CZipTempFile* tempfile = m_zfs->m_tempfilemanager.GetTempFile( filename );
		if( NULL == tempfile)
		{
			CEnv::GetOutLogInstance()->Print(L"ERROR: - No TempFile for file %s found.\n", filename.c_str() );
			return false;
		}
		
		CEmptyBlockVector::iterator iFind;
		size_t writelen = 0;
		bool bZipUpdate = false;
		if( !WriteModifyCentral( filename, central->m_usMethod, (size_t)tempfile->GetSize(),
			iFind, writelen, bZipUpdate))
			return false;
		
		if( central->m_usMethod == 0)
			m_zipwriter.SetZipLevel( 0);
		else
			m_zipwriter.SetZipLevel( 1); // 最快压缩

		if( !WriteFileData( tempfile, bZipUpdate))
		{
			CEnv::GetOutLogInstance()->Print(L"ERROR: - write data for file %s failed. zipupdate=%d.\n", 
				filename.c_str(), bZipUpdate );
			return false;
		}

		const ZIPFILE::CFileCentral* curcentral = m_zipwriter.GetCurCentral();
		if( curcentral )
		{
			// 实际写入的长度
			writelen = curcentral->GetDataOffset() - curcentral->m_uiOffset + curcentral->GetCompressedSize();

			// 对于CArrangeZipUnCompact模式，这些字段肯定需要调整。
			central->m_uiOffset = curcentral->m_uiOffset; 
			central->m_uiOffset = curcentral->m_uiOffset; 
			central->m_uiSizeNow = curcentral->m_uiSizeNow;
			central->m_uiSizeOrg = curcentral->m_uiSizeOrg;
			central->m_uiCRC32 = curcentral->m_uiCRC32;
			central->m_usMethod = curcentral->m_usMethod;
			central->m_usModifyTime = curcentral->m_usModifyTime;
			central->m_usModifyDate = curcentral->m_usModifyDate;
			central->m_usFlag = curcentral->m_usFlag;
			central->m_usDiskNumberStart = curcentral->m_usDiskNumberStart;
		}
		else
		{
			CEnv::GetOutLogInstance()->Print(L"ERROR: - no central found for %s failed. zipupdate=%d.\n", 
				filename.c_str(), bZipUpdate );
		}

		RefreshEmptyBlock( iFind, writelen);

		// 清除修改标记
		central->ClearModifiedFlag();
		
		// 临时文件可以关闭了。
		tempfile->Close();

		return true;
	}

	inline bool CheckCentralStringFormat( const std::wstring& dir)
	{
		if( dir.empty())
			return false;
		if( 0 != dir.find_first_not_of(L'/'))
			return false;
		return true;
	}

	bool CArrangeZipUnCompact::WriteModifyCentral( const std::wstring& filename, unsigned short usMethod, size_t filesize,
		CEmptyBlockVector::iterator& iFind, size_t& estimatesize, bool& bZipUpdate)
	{
		assert( CheckCentralStringFormat( filename));

		if( NULL != m_zipwriter.GetFileCentral( filename))
		{
			// 已经存在
			CEnv::GetOutLogInstance()->Print(L"ERROR: - File info %s already exists.\n", filename.c_str() );
			return false;
		}

		std::wstring::size_type pos = filename.find_last_of( L'/');
		if( pos != std::wstring::npos)
		{
			if( pos == filename.size()-1 )
			{
				pos = filename.find_last_of( L'/', filename.size()-2);
			}
			// 递归写入父目录，这里不能交给m_zipwritter做，跟offset有关
			if( pos != std::wstring::npos)
			{
				std::wstring slast = filename.substr( 0, pos+1);
				if( NULL == m_zipwriter.GetFileCentral( slast))
				{
					CEmptyBlockVector::iterator iParentFind;
					size_t writelen = 0;
					bool bTemp = false;	// 这里肯定返回false
					if( WriteModifyCentral( slast, 0, 0, iParentFind, writelen, bTemp))
					{
						RefreshEmptyBlock( iParentFind, writelen);
					}
				}
			}
		}

		ZIPFILE::CFileCentral fc;
		SetFileCentral( fc, filename, filesize, usMethod, bZipUpdate);

		// 因为fc的m_offset为0，这里直接用 GetDataOffset 可以得到 CFileHeader 的长度
		estimatesize = fc.GetDataOffset() + GetDeflateBound( filesize, usMethod, bZipUpdate);

		CEmptyBlock temp;
		temp.m_size = estimatesize;
		iFind = std::lower_bound( m_vtEmptyBlock.begin(), m_vtEmptyBlock.end(), temp);
		size_t offset = 0;
		if( iFind == m_vtEmptyBlock.end())
		{
			// 找不到足够大的空闲块，只能写在最后
			offset = m_lastOffset;
		}
		else
		{
			offset = iFind->m_offset;
		}

		m_zipwriter.SetOffset( offset);
		if( !m_zipwriter.WriteNextCentral( fc))
		{
			CEnv::GetOutLogInstance()->Print(L"ERROR: - Write file info (%s) failed.\n", filename.c_str() );
			return false;
		}

		return true;
	}

	// 估算压缩后大小
	size_t CArrangeZipUnCompact::GetDeflateBound( size_t filesize, unsigned short usMethod, bool bZipUpdate)
	{
		if( bZipUpdate)
		{
			return filesize;
		}
		if( usMethod == 0)
		{
			return filesize;
		}
		return m_zipwriter.DeflateBound( filesize);
	}

	// 刷新空闲块
	void CArrangeZipUnCompact::RefreshEmptyBlock( CEmptyBlockVector::iterator& iFind, size_t writesize)
	{
		if( iFind == m_vtEmptyBlock.end())
		{
			m_lastOffset += writesize;
		}
		else
		{
			iFind->m_offset += writesize;
			iFind->m_size -= writesize;
			if( iFind->m_size <= MINFULLSIZE)
			{
				// 如果空闲块很小的情况下就删掉
				m_vtEmptyBlock.erase( iFind);
			}
			else
			{
				// 将m_vtEmptyBlockVector整理为排序
				CEmptyBlock temp = *iFind;
				bool bOrder = true;
				if( iFind != m_vtEmptyBlock.begin())
				{
					CEmptyBlockVector::iterator iPrev = iFind;
					iPrev--;
					bOrder = (*iPrev < temp);
				}
				if( !bOrder)
				{
					CEmptyBlockVector::iterator iPrev = std::lower_bound( m_vtEmptyBlock.begin(), iFind, temp);
					assert( iPrev != iFind);
					m_vtEmptyBlock.erase( iFind);
					m_vtEmptyBlock.insert( iPrev, temp);
				}
			}
		}
	}

	CArrangeZipManager::CArrangeZipManager( CZipFS* zfs)
	{
		assert( zfs);
		std::wstring realfilename = zfs->GetNativeDevicePath();
		std::replace( realfilename.begin(), realfilename.end(), L'/', L'\\' );

		CEnv::GetOutLogInstance()->Print(L"INFO: Committing pfs - %s BEGIN.\n", realfilename.c_str() );

		// 通过判断m_realfilename确定是否是包中包
		bool bCompact = false; 
		if ( ::PathFileExists( realfilename.c_str() ) )
		{
			fsize_t noModifyLen = 0;
			ZIPFILE::CFileReader::CFileCentralMap& centralmap = zfs->m_zipfile.GetFileCentralMap();
			ZIPFILE::CFileReader::CFileCentralMap::iterator iEnd = centralmap.end();
			for( ZIPFILE::CFileReader::CFileCentralMap::iterator itor = centralmap.begin(); itor != iEnd; ++itor)
			{
				const ZIPFILE::CFileCentral& fc = itor->second;
				if( fc.IsFileDeleted() || fc.IsFileModified())
					continue;
				noModifyLen += fc.GetCompressedSize();
			}
			fsize_t filelen = zfs->m_zipfile.GetFileIO().GetSize();
			// 如果未改变文件的大小不到文件总大小的一半则采用 Compact 方式整理
			bCompact = ( noModifyLen < filelen/2);
		}
		if( bCompact)
		{
			CArrangeZipCompact arranger( zfs);
			arranger.Arrange();
		}
		else
		{
			CArrangeZipUnCompact arranger( zfs);
			arranger.Arrange();
		}

		CEnv::GetOutLogInstance()->Print(L"INFO: Committing pfs - %s END.\n", realfilename.c_str() );
	}

}	// namespace helper

}	// namespace PFS