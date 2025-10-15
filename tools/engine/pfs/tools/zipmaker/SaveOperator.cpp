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
		// �������Դ�����Ҳ���xml�ļ��Ͳ���ת��
		encoder = 0;
		return true;
	}

	des.Seek( 0, PFS::FSM_SET);
	if( PFSX::EncodeFile( &src, &des, encoder))
	{
		return true;
	}

	// ���ת������ֱ��ʹ��Դ�ļ�
	encoder = 0;

	if( m_listener )
	{
		std::wstring strWarning;
		strWarning = L"ת��ʧ�ܣ�ֱ��ʹ��Դ�ļ���������ת�� - " + item.GetName();
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
		wos << L"���棡 " << itor->second << L" �� " << name << L"���ļ���hashֵ��ͬ";
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
				if( bIsPakFile)		// ���ʧ�ܵ�һ���ļ�����
				{
					std::wostringstream wos;
					wos << L"���棡 " << item.GetName() << L" �ϲ�pak�ļ�ʱʧ�ܣ�����һ���ļ�����";
					if( m_listener)
						m_listener->OnNotify( (WPARAM)wos.str().c_str(), COperator::PT_FILEERROR);
				}
				PFS::CMetaInfo meta;
				if( !SaveOneFileData( item, sizeSrc, sizeDes, meta))
					throw CSaveOperatorError();

				// ����meta��Ϣ
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
			strWarning = L"����ʧ�ܣ�- " + item.GetName();
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
//		�Ƚ�����MetaInfo�Ƿ�һ��
//		����ֻ�Ƚ��ļ���С��Hashֵ
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
	CheckFileNameHash( item.GetName());		// ����ļ���hashֵ

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
		// XML���ת��Ϊ������Ҫ��ת������ļ�����md5ֵ
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

		// ����ת�����ļ���md5ֵ
		if( encoder != 0)
		{
			tempfile.Seek( 0, PFS::FSM_SET);
			PFS::CFileMetaInfo::MakeFileMetaInfo2( tempfile, meta);
		}
	}

	PFS::CBaseFile* savefile = &file;
	// ����ת�����hashֵ
	if( encoder != 0)
	{
		savefile = &tempfile;
	}
	sizeDes = savefile->GetSize();

	// ��encoder��Ϊ��Ӧ��decoder�������ڰ�װ�͸���
	meta.SetFilePfsxAttributes( PFSX::MakeFilePfsxAttributes( encoder, crc));

	// �������ʱ���°��������ļ�û���޸ľͲ�д�ļ�����
	if( !m_lastDataPfs.empty())
	{
		std::wstring tempName = item.GetName();
		tempName.insert( 0, L"/");
		if( IsEqualMetaInfo( m_allMetaInfos[tempName], meta))
		{
			return true;
		}
	}

	// д filecentral
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

	// д�ļ�����
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
	// �����Ӱ���Ŀ¼
	ZIPFILE::CFileCentral fcRoot;
	fcRoot.m_name = name;
	m_writter.WriteNextCentral( fcRoot);

	// �ȶ��� .setup.meta
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
	paths.push_back( PFSX::SetupMetaInfo());		// ��������

	ZIPFILE::CFileReader::CFileCentralMap& map =  reader.GetFileCentralMap();

	std::wstring tempName;
	std::wstring filename;
	// ���Ӱ������еĵ� .files2.meta �ļ��õ��Ӱ������ļ���meta��Ϣ
	PFS::CMetaInfoMap tempMetaInfos;
	for( PFSX::SetupMetaVector::iterator i=paths.begin(), e=paths.end(); i!=e; i++)
	{
		tempName = i->GetName();
		tempName.erase( 0, 1);		// ȥ����һ���ַ�'/'
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
		map.erase( filename);	// �����е� .file2.meta �ļ���map��ɾ��

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

	// ���Ӱ����ļ����ݼ�meta��Ϣ�ϲ�����װ��
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

		CheckFileNameHash( fc.m_name);	// �Ӱ��ļ��� hash ֵҲҪ���

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
				// ����meta��Ϣ
				tempName = fc.m_name;
				tempName.insert( 0, L"/");

				if( !m_lastDataPfs.empty())
				{
					// �������ʱ���°��������ļ�û���޸ľͲ�д�ļ�����
					if( IsEqualMetaInfo( m_allMetaInfos[tempName], iFind->second))
					{
						continue;
					}
				}

				if( m_writter.WriteNextCentral( fc))
				{
					// ֱ�ӽ��Ӱ��е��ļ����ݰ����
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
					woss << L"�Ӱ�(" << item.GetName() << L")�� " << childName << L"�ļ�δ�ҵ�meta��Ϣ��";
					m_listener->OnNotify( (WPARAM)woss.str().c_str(), COperator::PT_FILEERROR);
				}
			}
		}
	}
	return true;
}

// *.pak�ļ���һ��zip������û��.file.meta��Ϣ��Ҫ���Ӱ����ֿ������⴦��
bool CSaveOperator::MergePakFile( CZipTreeItem& item, fsize_t& sizeSrc, fsize_t& sizeDes)
{
	const std::wstring& pakmountstrOriginal = item.GetMount();
	std::wstring pakmountstr;
	pakmountstr.resize( pakmountstrOriginal.size() );
	std::transform( pakmountstrOriginal.begin(), pakmountstrOriginal.end(), pakmountstr.begin(), ::towlower);
	// ��������������ļ���metainfo��Ϣ
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

		// ����meta��Ϣ
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

	// ���� version.meta �� setup.meta
	// PFS::CMetaInfo meta;
	//m_allMetaInfos.insert( std::make_pair( L"/" + VERSION_META, meta));
	//m_allMetaInfos.insert( std::make_pair( L"/" + SETUP_META, m_setupmeta));
	//m_bakAllMetaInfos.insert( std::make_pair( L"/" + VERSION_META, meta));
	//m_bakAllMetaInfos.insert( std::make_pair( L"/" + SETUP_META, m_setupmeta));

	// .file.metaд1.0�汾
	m_buffer.Clear();
	PFS::CMetaDataWrite::DoWrite( m_buffer, m_allMetaInfos);
	SaveDataToZip( FILES_META, m_buffer);

	//// .file.metaд1.1�汾
	//m_buffer.Clear();
	//PFS::CMetaDataWrite::DoWriteByVersion( m_buffer, m_bakAllMetaInfos, 0x0101);
	//SaveDataToZip( FILES2_META, m_buffer);

	//if( m_listener)
	//	m_listener->OnNotify( (WPARAM)VERSION_META.c_str(), COperator::PT_FILENAME);

	// д.version.meta
	//PFS::FILE_NAME_HASH hashData;
	//PFS::helper::CFileNameHash hash;
	//hash.Final( hashData );
	//std::wstring hashstr = PFSX::CStringHelper::Hash2String( hashData );
	//PFSX::CVersionInfo& versionmetafile = m_project.GetVersionMeta();
	//versionmetafile.m_Hash = hashstr;

	//if( m_lastDataPfs.empty())
	//{
	//	CAnalyseFile::CAnalyseInfo allInfo = m_analyse.GetAllInfos();
	//	versionmetafile.m_size = allInfo.m_size/( 1024.0*1024.0);	// �������С
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

const std::wstring UPDATELOGFILE = L"������־.txt";
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

				// ���ɴ��Ŀ¼meta�ļ�hash��Ϣ
				if( !MakePakMetaFileHash( setupmeta))
				{
					if( m_listener )
					{
						std::wstring strWarning;
						strWarning = L"���ɴ��Ŀ¼meta�ļ�hash��Ϣʧ�ܣ� - " + temp;
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

// ����һ�汾setupMeta��Ϣ�͵�ǰsetupMeta��Ϣ Merge
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
	// ���Ƚ������ļ�meta��Ϣ���ݵ���һ��map
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

// ����md5ֵ
bool CSaveOperator::MakeMD5()
{
	if( m_listener)
	{
		m_listener->OnNotify( (WPARAM)L"���ɰ�װ��md5ֵ", COperator::PT_FILENAME);
	}

	PFS::CDataBlock result;
	if( !MakeBaseFileHash( m_fileio, result))
	{
		if( m_listener)
		{
			m_listener->OnNotify( (WPARAM)L"���ɰ�װ��md5ֵʧ�ܣ�", COperator::PT_FILEERROR);
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
			m_listener->OnNotify( (WPARAM)L"���氲װ��md5ֵ�ļ�ʧ�ܣ�", COperator::PT_FILEERROR);
		}
		return false;
	}
	//nf.Write( m_buffer.GetData(), m_buffer.GetSize());
	std::string md5Str;
	Data2AnsiString( result, md5Str);
	nf.Write( md5Str.c_str(), md5Str.size());
	return true;
}

// �õ���һ�汾��װ���� MetaInfoMap
bool CSaveOperator::GetLastDataPfsMetaInfoMap()
{
	if( m_lastDataPfs.empty())
	{
		return false;
	}
	return PFS::CEnv::GetMetaInfos( m_lastDataPfs, m_allMetaInfos);
}


// ���ɴ��Ŀ¼��hash��Ϣ
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
				// ���caseӦ�ò����ܵ�
				assert( false);
				++rBegin;
			}
			else
			{
				break;
			}
		}
	}

	// �� pakmap д�� m_buffer �ڴ��
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
	// ���� meta �ļ� 1.1 �汾�ĵ� hash ֵ
	PFS::CImageFile tempfile2( m_buffer);
	PFS::helper::CMD5FileHashMethod md5Method2;
	if( !md5Method2.MakeFileHash( tempfile2, md5))
	{
		return false;
	}
	setupmeta.SetHash2( md5);

	// ���� meta �ļ��� hash ֵ
	// ����д 1.0 �汾�� .files.meta �ļ�����Ϊ�ͻ��˵� .files.meta �ļ�����1.0�汾��
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

// �����ڴ�鵽zip��
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
		strWarning = L"����ʧ�ܣ� - " + filename;
		m_listener->OnNotify( (WPARAM)strWarning.c_str(), COperator::PT_FILEERROR);
	}
	return false;
}

// ��zip�����ڴ��
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

// ���� basefile �� md5
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