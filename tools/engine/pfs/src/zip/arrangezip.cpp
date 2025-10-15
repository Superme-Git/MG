#include "../common.h"
#include "arrangezip.h"
#include <time.h>
#include <sstream>
#include <iomanip>

//
// MT_ZIPUPDATE : �������͵�MOUNT��ʽ���������⴦��ZPDѹ����ʽ���ļ���
// ���⣬����������͵�MOUNT��ʽ�����ǻὫԭ����zip����ǩ����Ϊ�Զ����PFS��ʽǩ����
// ���û�д˱�ǣ���������MT_WRITABLE��ǵĻ������ǻ��޸�PFS��ʽΪZIP��ʽ��
//
// ���ļ��ж�γ��ֵ� bZipUpdate ����ȫ�ȼ��� MT_ZIPUPDATE�����ȣ�MT_ZIPUPDATE������bZipUpdate�Ƿ����Ϊtrue��
// ��������.files.meta�ļ���bZipUpdate����Ϊtrue��Ҳ����˵��bZipUpdate�Ǹ����ļ���̬���õġ�
// ���ֲ���Դ����ʷ���롣���ܲ��ǺܺõĴ���ʽ����ʱ�����������޸ġ�
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
		MINFULLSIZE = 32,		// ���п���Сֵ
	};

	CArrangeZip::CArrangeZip( CZipFS* zfs) 
		: m_zfs( zfs)
	{
		m_out.Reserve( BUFFERSIZE);

		// �����PFS�����£�����PFS����ʽ��������ZIP����ʽ��
		// ��ʵ��pfspatcher��ʱ���֣�������ﲻ��ȷ����zipwriter��д��ʽ��
		// �ᵼ��zip��ʽ��data.pfs�ڴ��겹��֮����pfs��ʽ�������ָ����ò���������ϣ���ġ�
		// ����������ϸ���ͣ��μ�pfspatcher��ص�˵����
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
			// ��Ŀ¼
			fc.m_usMethod = 0;
			fc.m_uiSizeNow = 0;
			fc.m_uiSizeOrg = 0;
			return;
		}

		// �����ǲ���MT_ZIPUPDATE��.files.meta���������ļ�������Ҫ����meta����
		// ��Դ�ڣ�.files.meta�ļ��в���¼�������Ч��meta��Ϣ��
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

				// ��������case�ǲ������ġ�
				// ��Ϊ�˱����zip�ļ��޷�����ȡ�����ﲻ��bZipUpdate = true��
				// ������CFileWriter::WriteFileData()�м���CRC32ֵ���Ա�֤zip�ļ��ܱ�������ȡ��
				// ��ʱ��ֻ�ܲ����δ����sizeֵ����ֱ�Ӹ���д�����ݼ���size��
				// yanghaibo
				// 2010-11-11
				fc.m_usMethod = 0;
				fc.m_uiSizeNow = 0;
				fc.m_uiSizeOrg = 0;
			}
			else
			{
				// ���ַ�ʽ��CRC32,SizeOrg��ԭʼ������Ϣ��meta�ж�ȡ����ֱ�ӽ���ʱ�ļ��е����ݵ�����ѹ�������ݿ�д����С�
				bZipUpdate = true; 
				const PFS::CMetaInfo& meta = i->second;

				// ����attribute������fc
				fc.m_uiCRC32 = meta.GetCRC32();
				fc.m_uiSizeOrg = (unsigned int)meta.GetFileSize();	// Դ�ļ��Ĵ�С
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
			// ��ͨzip��mount����
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
				// �����������쳣�������жϷ���ֵ
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

	// m_bUseTempFile = true ��д��ʱ�ļ��ٸ����ķ�ʽ����Ҫ˫�����̿ռ�
	//				  = false ֱ����Դ�ļ����������������̿ռ䣬����Ƿ��ж���Դ�ļ��ᱻ�𻵣��´��޷���
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
		m_zfs->CloseFile();	// ����closefile��Ϊ������ _wrename �ɹ�
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

		// �ļ���δ�䵫���ļ��������µģ�����Ҫ���´�һ��
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
			// m_vtNoModify�Ѿ���offset�����ˣ����ļ���ͷ����һ���޸ĵ��ļ�֮���δ�޸��ļ��Ϳ���ֱ������
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
			// дδ�޸ĵ��ļ�
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

				// �����µ�central offset
				if( curcentral != NULL )
				{
					// ����CArrangeZipCompact::End()�������ZipFS����ReOpen()�����ԣ����������Ƿ��޸�offset��
					// ���޹ؽ�Ҫ�� ���Լ�¼��־��û�������ˡ�
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

			// ����CArrangeZipCompact::End()�������ZipFS����ReOpen()�����ԣ����������Ƿ��޸�offset��
			// ���޹ؽ�Ҫ�� ���Լ�¼��־��û�������ˡ�
			// ��������central���ֶ��Ƿ���£�Ҳ����ν�ˡ�
			// ������Ϊ���߼������������Ծɸ�����ص��ֶΡ�
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

			// ����޸ı��
			central->ClearModifiedFlag();

			// ��ʱ�ļ����Թر��ˡ�
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
		// �������ļ������еĿ��п�
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
		
		// ��m_size��С��������
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
			m_zipwriter.SetZipLevel( 1); // ���ѹ��

		if( !WriteFileData( tempfile, bZipUpdate))
		{
			CEnv::GetOutLogInstance()->Print(L"ERROR: - write data for file %s failed. zipupdate=%d.\n", 
				filename.c_str(), bZipUpdate );
			return false;
		}

		const ZIPFILE::CFileCentral* curcentral = m_zipwriter.GetCurCentral();
		if( curcentral )
		{
			// ʵ��д��ĳ���
			writelen = curcentral->GetDataOffset() - curcentral->m_uiOffset + curcentral->GetCompressedSize();

			// ����CArrangeZipUnCompactģʽ����Щ�ֶο϶���Ҫ������
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

		// ����޸ı��
		central->ClearModifiedFlag();
		
		// ��ʱ�ļ����Թر��ˡ�
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
			// �Ѿ�����
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
			// �ݹ�д�븸Ŀ¼�����ﲻ�ܽ���m_zipwritter������offset�й�
			if( pos != std::wstring::npos)
			{
				std::wstring slast = filename.substr( 0, pos+1);
				if( NULL == m_zipwriter.GetFileCentral( slast))
				{
					CEmptyBlockVector::iterator iParentFind;
					size_t writelen = 0;
					bool bTemp = false;	// ����϶�����false
					if( WriteModifyCentral( slast, 0, 0, iParentFind, writelen, bTemp))
					{
						RefreshEmptyBlock( iParentFind, writelen);
					}
				}
			}
		}

		ZIPFILE::CFileCentral fc;
		SetFileCentral( fc, filename, filesize, usMethod, bZipUpdate);

		// ��Ϊfc��m_offsetΪ0������ֱ���� GetDataOffset ���Եõ� CFileHeader �ĳ���
		estimatesize = fc.GetDataOffset() + GetDeflateBound( filesize, usMethod, bZipUpdate);

		CEmptyBlock temp;
		temp.m_size = estimatesize;
		iFind = std::lower_bound( m_vtEmptyBlock.begin(), m_vtEmptyBlock.end(), temp);
		size_t offset = 0;
		if( iFind == m_vtEmptyBlock.end())
		{
			// �Ҳ����㹻��Ŀ��п飬ֻ��д�����
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

	// ����ѹ�����С
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

	// ˢ�¿��п�
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
				// ������п��С������¾�ɾ��
				m_vtEmptyBlock.erase( iFind);
			}
			else
			{
				// ��m_vtEmptyBlockVector����Ϊ����
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

		// ͨ���ж�m_realfilenameȷ���Ƿ��ǰ��а�
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
			// ���δ�ı��ļ��Ĵ�С�����ļ��ܴ�С��һ������� Compact ��ʽ����
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