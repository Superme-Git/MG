#include "StdAfx.h"
#include "SaveOperator.h"
#include "../../versionmetafile.h"
#include "../../setupmetafile.h"
#include "../../src/runzip/runzipfile.h"
#include "../../src/utility/md5hash.h"
#include "../../stringhelper.h"
#include "../../filesmetafile.h"


bool CSaveOperator::EncodeFileEx( PFS::CBaseFile& src, PFS::CBaseFile& des, CZipTreeItem& item, PFSX::TSID& encoder)
{
	encoder = item.GetAttributes();
	if( 0 == encoder)
	{
		return true;
	}
	if( m_bResource && CGlobalInfo::X2OID != encoder)
	{
		// 如果是资源包并且不是xml文件就不用转换
		encoder = 0;
		return true;
	}

	des.Seek( 0, PFS::FSM_SET);
	if( PFSX::EncodeFile( &src, &des, encoder))
	{
		return true;
	}

	// 如果转换出错，直接使用源文件
	encoder = 0;

	if( m_listener )
	{
		std::wstring strWarning;
		strWarning = L"转换失败！直接使用源文件，不进行转换 - " + item.GetName();
		if( m_listener)
			m_listener->OnNotify( (WPARAM)strWarning.c_str(), COperator::PT_FILEERROR);
	}

	return true;
}

void CSaveOperator::SaveAllFile( CZipTreeItem& item)
{
	CZipTreeItem::CZipTreeItemList& map = item.GetChild();
	for( CZipTreeItem::CZipTreeItemList::iterator itor = map.begin(); itor != map.end(); ++itor)
	{
		if( COperator::m_bStop)
			break;
		if( itor->GetIsPath())
		{
			if( itor->GetIsCheck())
			{
				ZIPFILE::CFileCentral fc;
				fc.m_name = itor->GetName();
				m_writter.WriteNextCentral( fc);
			}
			SaveAllFile( *itor);
		}
		else
		{
			SaveOneFile( *itor);
		}
	}
}

bool CSaveOperator::CheckFileNameHash( const std::wstring& name)
{
	unsigned int hash1 = ZIPFILE::CHash::Hash_ELF( (const char*)name.c_str(), name.size()*sizeof( wchar_t));
	unsigned int hash2 = ZIPFILE::CHash::Hash_JS( (const char*)name.c_str(), name.size()*sizeof( wchar_t));
	HashPair pair( hash1, hash2);
	CFileNameHashMap::iterator itor = m_filenamehashs.find( pair);
	if( itor != m_filenamehashs.end())
	{
		std::wostringstream wos;
		wos << L"警告！ " << itor->second << L" 和 " << name << L"的文件名hash值相同";
		if( m_listener)
			m_listener->OnNotify( (WPARAM)wos.str().c_str(), COperator::PT_FILEERROR);
		return false;
	}
	m_filenamehashs.insert( std::make_pair( pair, name));
	return true;
}

bool CSaveOperator::IsPakFile( const std::wstring& filename)
{
	std::wstring::size_type filenamesize = filename.size();
	if( filenamesize > 4)
	{
		if( _tcsicmp( filename.c_str() + (filenamesize-4), L".pak") == 0)
		{
			return true;
		}
	}
	return false;
}

class CSaveOperatorError {};
void CSaveOperator::SaveOneFile( CZipTreeItem& item)
{
	if( !item.GetIsCheck())
		return;
	if( m_listener)
		m_listener->OnNotify( (WPARAM)item.GetName().c_str(), COperator::PT_FILENAME);

	PFSX::TSID encoder = 0;
	PFSX::TSID decoder = 0;

	DWORD dwTime = GetTickCount();

	try
	{
		fsize_t sizeSrc = 0;
		fsize_t sizeDes = 0;
		if( item.GetIsChildPackage())
		{
			if( !MergeChildPackage( item, sizeDes))
				throw CSaveOperatorError();
			sizeSrc = sizeDes;
		}
		else
		{
			bool bIsPakFile = IsPakFile( item.GetName());
			if( bIsPakFile && MergePakFile( item, sizeSrc, sizeDes))
			{

			}
			else
			{
				if( bIsPakFile)		// 如果失败当一个文件处理
				{
					std::wostringstream wos;
					wos << L"警告！ " << item.GetName() << L" 合并pak文件时失败，当成一个文件处理！";
					if( m_listener)
						m_listener->OnNotify( (WPARAM)wos.str().c_str(), COperator::PT_FILEERROR);
				}
				PFS::CMetaInfo meta;
				if( !SaveOneFileData( item, sizeSrc, sizeDes, meta))
					throw CSaveOperatorError();

				// 保存meta信息
				//PFS::CEnv::SetFileMetaInfo( item.GetMount(), meta);
				std::wstring temp = item.GetName();
				temp.insert( 0, L"/");
				m_allMetaInfos[temp] = meta;
			}
		}
		m_analyse.AddAnalyseInfo( item.GetName(), sizeSrc, sizeDes, GetTickCount()-dwTime, false);
	}
	catch( CSaveOperatorError&)
	{
		if( m_listener )
		{
			std::wstring strWarning;
			strWarning = L"保存失败！- " + item.GetName();
			m_listener->OnNotify( (WPARAM)strWarning.c_str(), COperator::PT_FILEERROR);
		}
		m_analyse.AddAnalyseInfo( item.GetName(), 0, 0, GetTickCount()-dwTime, true);
	}
	if( m_listener)
		m_listener->OnNotify( 1, COperator::PT_TOTALPROGRESS);
}

unsigned int MakeFileCRC32( PFS::CBaseFile& file)
{
	ZIPFILE::helper::CCRC32 crc32;
	file.Seek( 0, PFS::FSM_SET);
	static char buffer[BUFFERSIZE] = {0};
	while( size_t s = file.Read( buffer, BUFFERSIZE))
	{
		if( PFS::FILE_SIZE_ERROR == s)
		{
			break;
		}
		crc32.Update( buffer, s);
	}
	return crc32.GetValue();
}

//
// summary:
//		比较两个MetaInfo是否不一样
//		这里只比较文件大小和Hash值
//
bool IsEqualMetaInfo(const PFS::CMetaInfo& rLeft,const PFS::CMetaInfo& rRight)
{
	return rLeft.GetFileSize() == rRight.GetFileSize() &&
		rLeft.GetFileHash() == rRight.GetFileHash() && 
		rLeft.GetFilePfsxAttributes() == rRight.GetFilePfsxAttributes() &&
		rLeft.GetMethod() == rRight.GetMethod();
}

bool CSaveOperator::SaveOneFileData( CZipTreeItem& item, fsize_t& sizeSrc, fsize_t& sizeDes, PFS::CMetaInfo& meta)
{
	CheckFileNameHash( item.GetName());		// 检测文件名hash值

	PFSX::TSID encoder = 0;
	PFS::CFile file;
	if( !file.Open( item.GetMount(), PFS::FM_EXCL, PFS::FA_RDONLY))
		return false;
	sizeSrc = file.GetSize();

	//PFS::CMetaInfo meta;
	meta.SetFileDownloadURI( item.GetUrl());
	meta.SetMethod( item.GetZipLevel());

	if( !EncodeFileEx( file, m_tempfile, item, encoder))
		return false;

	PFS::CImageFile tempfile( m_tempfile.GetImage());
	unsigned int crc = 0;
	if( encoder == CGlobalInfo::X2OID)
	{
		// XML如果转换为二进制要用转化后的文件生成md5值
		tempfile.Seek( 0, PFS::FSM_SET);
		PFS::CFileMetaInfo::MakeFileMetaInfo( tempfile, meta);
		crc = MakeFileCRC32( tempfile);
	}
	else
	{
		PFS::FILE_DATA_HASH md5Value;
		if( !MakeBaseFileHash( file, md5Value))
		{
			return false;
		}
		meta.SetFileHash( md5Value);
		meta.SetFileSize( file.GetSize());

		crc = MakeFileCRC32( file);

		// 生成转换后文件的md5值
		if( encoder != 0)
		{
			tempfile.Seek( 0, PFS::FSM_SET);
			PFS::CFileMetaInfo::MakeFileMetaInfo2( tempfile, meta);
		}
	}

	PFS::CBaseFile* savefile = &file;
	// 计算转换后的hash值
	if( encoder != 0)
	{
		savefile = &tempfile;
	}
	sizeDes = savefile->GetSize();

	// 将encoder作为对应的decoder保存用于安装和更新
	meta.SetFilePfsxAttributes( PFSX::MakeFilePfsxAttributes( encoder, crc));

	// 如果是临时更新包，并且文件没有修改就不写文件数据
	if( !m_lastDataPfs.empty())
	{
		std::wstring tempName = item.GetName();
		tempName.insert( 0, L"/");
		if( IsEqualMetaInfo( m_allMetaInfos[tempName], meta))
		{
			return true;
		}
	}

	// 写 filecentral
	ZIPFILE::CFileCentral fc;
	fc.m_name = item.GetName();
	if( m_bResource)
	{
		if( item.GetZipLevel() == 0)
			fc.m_usMethod = 0;
		m_writter.SetZipLevel( item.GetZipLevel());
	}
	else
	{
		fc.m_usMethod = 0;
		m_writter.SetZipLevel( 0);
	}
	if( !m_writter.WriteNextCentral( fc))
	{
		return false;
	}

	// 写文件数据
	SaveToZip( *savefile);

	return true;
}

void CSaveOperator::SaveToZip( PFS::CBaseFile& file)
{
	file.Seek( 0, PFS::FSM_SET);
	fsize_t sizeDes =  file.GetSize();
	fsize_t read = 0;
	while( true)
	{
		if( COperator::m_bStop)
			throw CSaveOperatorError();
		size_t current = file.Read( m_buffer.GetData(), BUFFERSIZE);
		if( PFS::FILE_SIZE_ERROR == current)
			throw CSaveOperatorError();
		m_buffer.Resize( current);

		read += current;
		bool bFinish = current ? (read >= sizeDes) : true;
		m_writter.WriteFileData( m_buffer, bFinish);
		if( bFinish)
		{
			break;
		}
	}
}

const wchar_t UNICODE_WINDOWS_FILE_PREFIX = 0xFEFF;
const std::wstring VERSION_META = L".version.meta";
const std::wstring FILES_META = L".files.meta";
const std::wstring FILES2_META = L".files2.meta";
const std::wstring SETUP_META = L".setup.meta";
bool CSaveOperator::MergeChildPackage( CZipTreeItem& item, fsize_t& size)
{
	PFS::CFile file;
	if( !file.Open( item.GetMount(), PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}
	size = file.GetSize();
	ZIPFILE::CFileReader reader;
	try
	{
		reader.SetFileIO( file);
	}
	catch( ZIPFILE::CZipFileBadFormat&)
	{
		return false;
	}
	
	std::wstring name = item.GetName();
	name.erase( name.find_last_of( L'.'));
	name += L"/";
	// 插入子包根目录
	ZIPFILE::CFileCentral fcRoot;
	fcRoot.m_name = name;
	m_writter.WriteNextCentral( fcRoot);

	// 先读出 .setup.meta
	if( !ReadDataFromZip( reader, SETUP_META, m_buffer))
	{
		return false;
	}
	PFS::CImageFile imageFile( m_buffer);
	PFSX::SetupMetaVector paths;
	if( !PFSX::CSetupMetaFile::DoRead( imageFile, paths))
	{
		return false;
	}
	paths.push_back( PFSX::SetupMetaInfo());		// 插入根结点

	ZIPFILE::CFileReader::CFileCentralMap& map =  reader.GetFileCentralMap();

	std::wstring tempName;
	std::wstring filename;
	// 读子包中所有的的 .files2.meta 文件得到子包所有文件的meta信息
	PFS::CMetaInfoMap tempMetaInfos;
	for( PFSX::SetupMetaVector::iterator i=paths.begin(), e=paths.end(); i!=e; i++)
	{
		tempName = i->GetName();
		tempName.erase( 0, 1);		// 去掉第一个字符'/'
		filename = tempName;
		if( tempName.empty())
		{
			filename += FILES2_META;
		}
		else
		{
			filename += PFSX::FILES2_META_NAME;
		}
		if( !ReadDataFromZip( reader, filename, m_buffer))
		{
			return false;
		}
		map.erase( filename);	// 将所有的 .file2.meta 文件从map中删除

		PFS::CMetaInfoMap pakMap;
		if( !PFS::CMetaDataRead::DoRead( m_buffer, pakMap))
		{
			return false;
		}
		if( tempName.empty())
		{
			pakMap.erase( PFSX::VERSION_META_NAME);
			pakMap.erase( PFSX::SETUP_META_NAME);
		}
		else
		{
			tempName.insert( 0, L"/");
		}
		for( PFS::CMetaInfoMap::iterator i=pakMap.begin(), e=pakMap.end(); i!=e; i++)
		{
			tempMetaInfos.insert( std::make_pair( tempName+i->first, i->second));
		}
	}

	// 将子包中文件内容及meta信息合并到安装包
	for( ZIPFILE::CFileReader::CFileCentralMap::iterator itor = map.begin(); itor != map.end(); ++itor)
	{
		if( COperator::m_bStop)
		{
			return false;
		}
		const std::wstring& childName = itor->first;
		if( childName == VERSION_META || childName == SETUP_META 
			|| childName == FILES_META || childName == FILES2_META)
		{
			continue;
		}
				
		ZIPFILE::CFileCentral fc = itor->second;
		fc.m_name = name + childName;

		if( m_listener)
		{
			std::wstring temp = item.GetName() + L"/" + childName;
			m_listener->OnNotify( (WPARAM)temp.c_str(), COperator::PT_FILENAME);
		}

		CheckFileNameHash( fc.m_name);	// 子包文件名 hash 值也要检测

		if( fc.IsDirectory())
		{
			m_writter.WriteNextCentral( fc);
		}
		else
		{
			tempName = childName;
			tempName.insert( 0, L"/");

			PFS::CMetaInfoMap::const_iterator iFind = tempMetaInfos.find( tempName);
			if( iFind != tempMetaInfos.end())
			{
				// 保存meta信息
				tempName = fc.m_name;
				tempName.insert( 0, L"/");

				if( !m_lastDataPfs.empty())
				{
					// 如果是临时更新包，并且文件没有修改就不写文件数据
					if( IsEqualMetaInfo( m_allMetaInfos[tempName], iFind->second))
					{
						continue;
					}
				}

				if( m_writter.WriteNextCentral( fc))
				{
					// 直接将子包中的文件数据搬过来
					ZIPFILE::CFileCentralRead centralread( reader, itor->second);
					
					m_buffer.Clear();
					while( centralread.ReadOrg( m_buffer, BUFFERSIZE) > 0)
					{
						m_writter.WriteDeflateData( m_buffer, false);
					}

					m_allMetaInfos[tempName] = iFind->second;
				}
			}
			else
			{
				if( m_listener)
				{
					std::wostringstream woss;
					woss << L"子包(" << item.GetName() << L")中 " << childName << L"文件未找到meta信息！";
					m_listener->OnNotify( (WPARAM)woss.str().c_str(), COperator::PT_FILEERROR);
				}
			}
		}
	}
	return true;
}

// *.pak文件是一个zip包但是没有.file.meta信息，要和子包区分开，特殊处理
bool CSaveOperator::MergePakFile( CZipTreeItem& item, fsize_t& sizeSrc, fsize_t& sizeDes)
{
	const std::wstring& pakmountstrOriginal = item.GetMount();
	std::wstring pakmountstr;
	pakmountstr.resize( pakmountstrOriginal.size() );
	std::transform( pakmountstrOriginal.begin(), pakmountstrOriginal.end(), pakmountstr.begin(), ::towlower);
	// 首先算出所有子文件的metainfo信息
	if( !PFS::CEnv::Mount( pakmountstr, item.GetMount(), PFS::FST_ZIP, PFS::MT_READONLY))
	{
		return false;
	}
	if( !PFS::CEnv::CheckMetaInfo( pakmountstr))
	{
		PFS::CEnv::Unmount( pakmountstr);
		return false;
	}
	PFS::CMetaInfoMap mim;
	if( !PFS::CEnv::GetMetaInfos( pakmountstr, mim))
	{
		PFS::CEnv::Unmount( pakmountstr);
		return false;
	}
	fsize_t childSrc = 0, childDes = 0;
	bool bRes = true;
	std::wstring tempName;
	for( PFS::CMetaInfoMap::iterator itor = mim.begin(); itor!=mim.end(); ++itor)
	{
		const std::wstring& childname = itor->first;
		CZipTreeItem itemChild;
		PFS::CMetaInfo meta;
		itemChild.SetMount( pakmountstr + childname);
		itemChild.SetName( item.GetName() + childname);
		itemChild.SetZipLevel( m_project.GetAttribute().GetZipLevel( childname));
		itemChild.SetAttributes( m_project.GetAttribute().GetSetupLevel( childname));
		if( !SaveOneFileData( itemChild, childSrc, childDes, itor->second))
		{
			bRes = false;
			break;
		}
		sizeSrc += childSrc;
		sizeDes += childDes;

		// 保存meta信息
		tempName = itemChild.GetName();
		tempName.insert( 0, L"/");
		m_allMetaInfos[tempName] = itor->second;
	}
	PFS::CEnv::Unmount( pakmountstr);

	return bRes;
}

void CSaveOperator::SaveFileMetaInfo()
{
	m_writter.SetZipLevel( CGlobalInfo::NO);
	if( m_listener)
		m_listener->OnNotify( (WPARAM)FILES_META.c_str(), COperator::PT_FILENAME);

	// 插入 version.meta 和 setup.meta
	// PFS::CMetaInfo meta;
	//m_allMetaInfos.insert( std::make_pair( L"/" + VERSION_META, meta));
	//m_allMetaInfos.insert( std::make_pair( L"/" + SETUP_META, m_setupmeta));
	//m_bakAllMetaInfos.insert( std::make_pair( L"/" + VERSION_META, meta));
	//m_bakAllMetaInfos.insert( std::make_pair( L"/" + SETUP_META, m_setupmeta));

	// .file.meta写1.0版本
	m_buffer.Clear();
	PFS::CMetaDataWrite::DoWrite( m_buffer, m_allMetaInfos);
	SaveDataToZip( FILES_META, m_buffer);

	//// .file.meta写1.1版本
	//m_buffer.Clear();
	//PFS::CMetaDataWrite::DoWriteByVersion( m_buffer, m_bakAllMetaInfos, 0x0101);
	//SaveDataToZip( FILES2_META, m_buffer);

	//if( m_listener)
	//	m_listener->OnNotify( (WPARAM)VERSION_META.c_str(), COperator::PT_FILENAME);

	// 写.version.meta
	//PFS::FILE_NAME_HASH hashData;
	//PFS::helper::CFileNameHash hash;
	//hash.Final( hashData );
	//std::wstring hashstr = PFSX::CStringHelper::Hash2String( hashData );
	//PFSX::CVersionInfo& versionmetafile = m_project.GetVersionMeta();
	//versionmetafile.m_Hash = hashstr;

	//if( m_lastDataPfs.empty())
	//{
	//	CAnalyseFile::CAnalyseInfo allInfo = m_analyse.GetAllInfos();
	//	versionmetafile.m_size = allInfo.m_size/( 1024.0*1024.0);	// 计算包大小
	//}
	////SaveUpdateLog( versionmetafile.m_UpdateLog);

	//PFS::CDataBlock temp;
	//versionmetafile.WriteTo( temp);

	//m_buffer.Clear();
	//m_buffer.Reserve( sizeof( wchar_t) + temp.GetSize());
	//m_buffer.Append( &UNICODE_WINDOWS_FILE_PREFIX, sizeof(wchar_t));
	//m_buffer.Append( temp.GetData(), temp.GetSize());
	//
	// SaveDataToZip( VERSION_META, m_buffer);
}

static BSTR TryReadUniCodeStringFromMemory( PFS::CDataBlock& buffer) 
{
	BSTR	bstrText = (BSTR)buffer.GetData();
	if( UNICODE_WINDOWS_FILE_PREFIX == bstrText[0])
	{
		bstrText ++;
		return bstrText;
	}
	else
	{
		char*	szSrc = (char*)buffer.GetData();
		int		nResult = MultiByteToWideChar( CP_ACP, 0, szSrc, -1, NULL, NULL);
		if( 0 == nResult)
			return NULL;

		PFS::CDataBlock	temp;
		temp.Resize( sizeof(wchar_t) * ( nResult + 1) );
		wchar_t* wsBuffer= (wchar_t*)temp.GetData();
		nResult = MultiByteToWideChar( CP_ACP, 0, szSrc, -1, wsBuffer, nResult);
		if( 0 == nResult)
			return NULL;
		wsBuffer[nResult] = 0;

		buffer.Swap( temp);
		return (BSTR)buffer.GetData();
	}
}

const std::wstring UPDATELOGFILE = L"更新日志.txt";
void CSaveOperator::SaveUpdateLog( std::wstring& updatelog)
{
	CZipTreeItem::CZipTreeItemList& childs = m_parent.GetChild();
	for( CZipTreeItem::CZipTreeItemList::iterator itor = childs.begin(); itor != childs.end(); ++itor)
	{
		if( itor->GetName() == UPDATELOGFILE)
		{
			PFS::CFile file;
			if( !file.Open( itor->GetMount(), PFS::FM_EXCL, PFS::FA_RDONLY))
				return;
			fsize_t filelen = file.GetSize();
			PFS::CDataBlock temp;
			temp.Reserve( (size_t)(filelen + sizeof( wchar_t)));
			size_t readlen = file.Read( temp.GetData(), (size_t)filelen);
			if( readlen == PFS::FILE_SIZE_ERROR)
				return;
			temp.Resize( readlen);
			wchar_t end = 0;
			temp.Append( &end, sizeof( wchar_t));
			updatelog = TryReadUniCodeStringFromMemory( temp);
		}
	}
}

void CSaveOperator::MakeSetupMeta( CZipTreeItem& item)
{
	CZipTreeItem::CZipTreeItemList& map = item.GetChild();
	for( CZipTreeItem::CZipTreeItemList::iterator itor = map.begin(); itor != map.end(); ++itor)
	{
		if( itor->GetIsCheck() )
		{
			//bool bChildPackage = itor->GetIsChildPackage();
			//if( bChildPackage || itor->GetIsPack() )
			if( itor->GetIsPack() )
			{
				std::wstring temp = itor->GetName();
				if ( itor->GetIsChildPackage() )
				{
					temp.erase( temp.find( L".pfs") );
				}

				temp.erase( temp.find_last_not_of( L'/')+1);
				temp.insert( 0, L"/");

				PFSX::SetupMetaInfo setupmeta;
				setupmeta.SetName(temp);

				// 生成打包目录meta文件hash信息
				if( !MakePakMetaFileHash( setupmeta))
				{
					if( m_listener )
					{
						std::wstring strWarning;
						strWarning = L"生成打包目录meta文件hash信息失败！ - " + temp;
						m_listener->OnNotify( (WPARAM)strWarning.c_str(), COperator::PT_FILEERROR);
					}
				}

				m_setuppath.push_back( setupmeta );
			}
			else if( itor->GetIsPath())
			{
				//MakeSetupMeta( *itor);
			}
		}
		else if( itor->GetIsPath())
		{
			//MakeSetupMeta( *itor);
		}
	}
}

// 将上一版本setupMeta信息和当前setupMeta信息 Merge
bool CSaveOperator::MergeLastSetupMetaInfo()
{
	PFS::CFile setupFile;
	if( !setupFile.Open( m_lastDataPfs + L"/" + SETUP_META, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}
	PFSX::SetupMetaVector curSetups;
	if( !PFSX::CSetupMetaFile::DoRead( setupFile, curSetups))
	{
		return false;
	}
	for( PFSX::SetupMetaVector::iterator i=curSetups.begin(), e=curSetups.end(); i!=e; ++i)
	{
		const PFSX::SetupMetaInfo& path = *i;
		if( std::find( m_setuppath.begin(), m_setuppath.end(), path) == m_setuppath.end())
		{
			m_setuppath.push_back( path);
		}
	}
	return true;
}

void CSaveOperator::SaveSetupMetaInfo()
{
	// 首先将所有文件meta信息备份到另一个map
	m_bakAllMetaInfos = m_allMetaInfos;

	m_writter.SetZipLevel( CGlobalInfo::NO);
	
	//MakeSetupMeta( m_parent);

	if( !m_lastDataPfs.empty())
	{
		MergeLastSetupMetaInfo();
	}

	m_setuppath.SetOriginal(true);
	PFS::CDataBlock temp;
	PFSX::CSetupMetaFile::DoWrite( temp, m_setuppath);

	m_buffer.Clear();
	m_buffer.Reserve( sizeof(wchar_t) + temp.GetSize());
	m_buffer.Append( &UNICODE_WINDOWS_FILE_PREFIX, sizeof(wchar_t));
	m_buffer.Append( temp.GetData(), temp.GetSize());

	SaveDataToZip( SETUP_META, m_buffer);

	PFS::CImageFile metaFile( m_buffer);
	PFS::CFileMetaInfo::MakeFileMetaInfo( metaFile, m_setupmeta);
	
}

int CSaveOperator::GetItemFileCount( CZipTreeItem& item)
{
	int count = 0;
	if( item.GetIsPath())
	{	
		CZipTreeItem::CZipTreeItemList& map = item.GetChild();
		for( CZipTreeItem::CZipTreeItemList::iterator itor = map.begin(); itor != map.end(); ++itor)
			count += GetItemFileCount( *itor);
		return count;
	}
	return item.GetIsCheck();
}

// 生成md5值
bool CSaveOperator::MakeMD5()
{
	if( m_listener)
	{
		m_listener->OnNotify( (WPARAM)L"生成安装包md5值", COperator::PT_FILENAME);
	}

	PFS::CDataBlock result;
	if( !MakeBaseFileHash( m_fileio, result))
	{
		if( m_listener)
		{
			m_listener->OnNotify( (WPARAM)L"生成安装包md5值失败！", COperator::PT_FILEERROR);
		}
		return false;
	}

	m_md5Value = PFSX::CStringHelper::Data2String( result);

	std::wstring md5file = m_file;
	md5file += L".md5";
	PFSX::CNativeFile nf;
	if( !nf.Open( md5file, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
	{
		if( m_listener)
		{
			m_listener->OnNotify( (WPARAM)L"保存安装包md5值文件失败！", COperator::PT_FILEERROR);
		}
		return false;
	}
	//nf.Write( m_buffer.GetData(), m_buffer.GetSize());
	std::string md5Str;
	Data2AnsiString( result, md5Str);
	nf.Write( md5Str.c_str(), md5Str.size());
	return true;
}

// 得到上一版本安装包的 MetaInfoMap
bool CSaveOperator::GetLastDataPfsMetaInfoMap()
{
	if( m_lastDataPfs.empty())
	{
		return false;
	}
	return PFS::CEnv::GetMetaInfos( m_lastDataPfs, m_allMetaInfos);
}


// 生成打包目录的hash信息
bool CSaveOperator::MakePakMetaFileHash( PFSX::SetupMetaInfo& setupmeta)
{
	std::wstring& pakname = setupmeta.GetName();
	std::wstring beginStr = pakname;
	if( beginStr.empty())
	{
		return false;
	}
	beginStr += L"/";

	PFS::CMetaInfoMap pakmap;
	std::wstring filename;
	PFS::CMetaInfoMap::iterator rBegin = m_bakAllMetaInfos.lower_bound( beginStr);
	for( ; rBegin != m_bakAllMetaInfos.end(); )
	{
		const std::wstring& childname = rBegin->first;
		int res = wcsncmp( childname.c_str(), beginStr.c_str(), beginStr.size());
		if( res == 0)
		{
			filename = childname.c_str() + pakname.size();
			pakmap.insert( std::make_pair( filename, rBegin->second));

			rBegin = m_bakAllMetaInfos.erase( rBegin);
		}
		else
		{
			if( res < 0)
			{
				// 这个case应该不可能到
				assert( false);
				++rBegin;
			}
			else
			{
				break;
			}
		}
	}

	// 将 pakmap 写到 m_buffer 内存块
	m_buffer.Clear();
	if( !PFS::CMetaDataWrite::DoWriteByVersion( m_buffer, pakmap, 0x0101))
	{
		return false;
	}

	filename = pakname.c_str() + 1;
	filename += L"/";
	filename += FILES2_META;
	if( !SaveDataToZip( filename, m_buffer))
	{
		return false;
	}

	PFS::FILE_DATA_HASH md5 = 0;
	// 生成 meta 文件 1.1 版本的的 hash 值
	PFS::CImageFile tempfile2( m_buffer);
	PFS::helper::CMD5FileHashMethod md5Method2;
	if( !md5Method2.MakeFileHash( tempfile2, md5))
	{
		return false;
	}
	setupmeta.SetHash2( md5);

	// 生成 meta 文件的 hash 值
	// 这里写 1.0 版本的 .files.meta 文件，因为客户端的 .files.meta 文件都是1.0版本的
	m_buffer.Clear();
	if( !PFS::CMetaDataWrite::DoWrite( m_buffer, pakmap))
	{
		return false;
	}
	PFS::CImageFile tempfile( m_buffer);
	PFS::helper::CMD5FileHashMethod md5Method;
	if( !md5Method.MakeFileHash( tempfile, md5))
	{
		return false;
	}
	setupmeta.SetHash( md5);

	

	return true;
}

// 保存内存块到zip包
bool CSaveOperator::SaveDataToZip( const std::wstring& filename, PFS::CDataBlock& data)
{
	ZIPFILE::CFileCentral fc;
	fc.m_name = filename;
	fc.m_usMethod = 0;
	if( m_writter.WriteNextCentral( fc))
	{
		return m_writter.WriteFileData( data, true);
	}

	if( m_listener )
	{
		std::wstring strWarning;
		strWarning = L"保存失败！ - " + filename;
		m_listener->OnNotify( (WPARAM)strWarning.c_str(), COperator::PT_FILEERROR);
	}
	return false;
}

// 从zip包读内存块
bool CSaveOperator::ReadDataFromZip( ZIPFILE::CFileReader& fileReader,
									const std::wstring& filename, PFS::CDataBlock& data)
{
	ZIPFILE::CFileReader::CFileCentralMap& map =  fileReader.GetFileCentralMap();
	ZIPFILE::CFileReader::CFileCentralMap::iterator itor = map.find( filename);
	if( itor == map.end())
	{
		return false;
	}
	ZIPFILE::CFileCentralRead centralRead( fileReader, itor->second);
	unsigned int total = itor->second.GetOrginalSize();
	if( total > 0 )
	{
		data.Clear();
		if( total == centralRead.Read( data, total) )
		{
			return true;
		}
	}
	return false;
}

// 生成 basefile 的 md5
bool CSaveOperator::MakeBaseFileHash( PFS::CBaseFile& basefile, PFS::FILE_DATA_HASH& md5)
{
	basefile.Seek( 0, PFS::FSM_SET);

	// ZERO the buffer.
	m_buffer.Resize(BUFFERSIZE);

	PFS::helper::CMD5Hash md5Method;
	while( size_t read = basefile.Read( m_buffer.GetData(), BUFFERSIZE))
	{
		if( PFS::FILE_SIZE_ERROR == read )
		{
			return false;
		}

		md5Method.Update( m_buffer.GetData(), read);
	}

	md5Method.Final( md5);
	return true;
}

bool CSaveOperator::MakeBaseFileHash( PFS::CBaseFile& basefile, PFS::CDataBlock& md5)
{
	basefile.Seek( 0, PFS::FSM_SET);

	m_buffer.Resize(BUFFERSIZE);	
	
	PFS::helper::CMD5Hash md5Method;
	while( size_t read = basefile.Read( m_buffer.GetData(), BUFFERSIZE))
	{
		if( PFS::FILE_SIZE_ERROR == read )
		{
			return false;
		}

		md5Method.Update( m_buffer.GetData(), read);
	}

	md5Method.Final( md5);
	return true;
}