#include <windows.h>
#include "../../pfs2.h"
#include "../../src/native/rawfileio.h"
#include "../../filemarshal.h"
#include "../../src/meta/metafileio.h"
#include "../../SetupMetaFile.h"
#include "InstallThread.h"
#include "../packzip/KHLogger.h"

using PFS::fsize_t;
using PFS::foff_t;

enum { BUFFERSIZE = 65536, };

CInstallThread::CInstallThread()
{
	m_buffer.Reserve( BUFFERSIZE);
}

const std::wstring SETUP_META = L"/.setup.meta";

bool CInstallThread::Init()
{
	m_listener->OnNotify( 0, INIT);

	KHMsgLog_Debug( L"begin install \n");

	if( !PFS::CFileIO::CheckCreateDirectory( m_setuppath))
	{
		m_listener->OnNotify( WPARAM( m_setuppath.c_str()), FILEERROR);
		KHMsgLog_Debug( L"CheckCreateDirectory %s(%d)\n", m_setuppath.c_str(), PFS::CEnv::GetLastError());
		return false;
	}

	KHMsgLog_Debug( L"begin mount %s\n", m_setuppath.c_str());
	if( !PFS::CEnv::Mount( GetDesName(), m_setuppath, PFS::FST_NATIVE, PFS::MT_WRITABLE))
	{
		m_listener->OnNotify( WPARAM( m_setuppath.c_str()), FILEERROR);
		KHMsgLog_Debug( L"mount error %s(%d)\n", m_setuppath.c_str(), PFS::CEnv::GetLastError());
		return false;
	}
	KHMsgLog_Debug( L"end mount %s\n", m_setuppath.c_str());
	return true;
}

bool CInstallThread::Begin()
{
	float totallen = 0.0f;
	for( std::vector<std::wstring>::const_iterator itor = m_zipfiles.begin(); itor!=m_zipfiles.end(); ++itor)
	{
		if( m_stop)
		{
			return false;
		}
		const std::wstring& zipfile = *itor;
		std::wstring mountstr = zipfile;
		mountstr.erase( mountstr.find_first_of( L'.'));

		KHMsgLog_Debug( L"begin mount %s\n", zipfile.c_str());
		if( !PFS::CEnv::Mount( mountstr, zipfile, PFS::FST_ZIP))
		{
			m_listener->OnNotify( WPARAM( zipfile.c_str()), FILEERROR);
			KHMsgLog_Debug( L"mount error %s(%d)\n", zipfile.c_str(), PFS::CEnv::GetLastError());
			return false;
		}
		KHMsgLog_Debug( L"end mount %s\n", zipfile.c_str());

		KHMsgLog_Debug( L"begin GetMetaInfos %s\n", zipfile.c_str());
		PFS::CMetaInfoMap mm;
		PFS::CEnv::GetMetaInfos( mountstr, mm);
		KHMsgLog_Debug( L"end GetMetaInfos %s\n", zipfile.c_str());

		totallen += GetFileLen( mm);
				
		if( !ReadSetupMetaFile( mountstr))
		{
			m_listener->OnNotify( WPARAM( SETUP_META.c_str()), FILEERROR);
			KHMsgLog_Debug( L"ReadSetupMetaFile error %s(%d)\n", zipfile.c_str(), PFS::CEnv::GetLastError());
			return false;
		}
	}

	WPARAM wParam = 0;
	memcpy( &wParam, &totallen, sizeof(float));	// 源文件可能大于4G，用浮点数存储
	m_listener->OnNotify( wParam, TOTALLEN);
	return true;
}

bool CInstallThread::ReadSetupMetaFile( const std::wstring& src)
{
	std::wstring setupmeta = src + SETUP_META;
	PFS::CFile file;
	if( !file.Open( setupmeta, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}
	PFSX::SetupMetaVector packs;
	if( !PFSX::CSetupMetaFile::DoRead( file, packs))
	{
		return false;
	}
	m_packs.insert( m_packs.end(), packs.begin(), packs.end());
	return true;
}

float CInstallThread::GetFileLen( PFS::CMetaInfoMap& mm)
{
	float totallen = 0.0f;
	for( PFS::CMetaInfoMap::iterator itor = mm.begin(); itor!=mm.end(); ++itor)
	{
		totallen += itor->second.GetFileSize();
	}
	return totallen;
}

bool CInstallThread::WriteSetupMetaFile()
{
	std::wstring setupmeta = GetDesName() + SETUP_META;
	PFS::CFile file;
	if( !file.Open( setupmeta, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
	{	
		return false;
	}
	return PFSX::CSetupMetaFile::DoWrite( file, m_packs);
}

bool CInstallThread::WriteAllNativeFile( )
{
	PFS::CMetaInfoMap mmNative;
	for( std::vector<std::wstring>::const_iterator itor = m_zipfiles.begin(); itor!=m_zipfiles.end(); ++itor)
	{
		const std::wstring& zipfile = *itor;
		std::wstring mountstr = zipfile;
		mountstr.erase( mountstr.find_first_of( L'.'));

		PFS::CMetaInfoMap mm;
		PFS::CEnv::GetMetaInfos( mountstr, mm);
		for( PFS::CMetaInfoMap::iterator itor = mm.begin(); itor != mm.end(); ++itor)
		{
			if( m_stop)
			{
				return false;
			}
			if( IsZipFile( itor->first))
				continue;
			m_listener->OnNotify( WPARAM( itor->first.c_str()), FILENAME);
			
			WPARAM wParam = 0;
			float temp = (float)itor->second.GetFileSize();
			memcpy( &wParam, &temp, sizeof(float));
			m_listener->OnNotify( wParam, FILELEN);

			const std::wstring& srcname = itor->first;
			if( !WriteNativeFile( mountstr + srcname, srcname, itor->second))
			{
				m_listener->OnNotify( WPARAM( srcname.c_str()), FILEERROR);
				KHMsgLog_Debug( L"WriteNativeFile error %s(%d)\n", srcname.c_str(), PFS::CEnv::GetLastError());
				throw CInstallError();
			}
			mmNative.insert( *itor);
			PFS::CEnv::AddFileMetaInfo( GetDesName() + srcname, itor->second);

			m_listener->OnNotify( wParam, FILEEND);
		}
	}
	WriteNativeMetaFile( mmNative);
	
	// setup.meta 单独处理
	if( !WriteSetupMetaFile())
	{
		m_listener->OnNotify( WPARAM( SETUP_META.c_str()), FILEERROR);
		return false;
	}
	return true;
}

bool CInstallThread::WriteAllZipFile( )
{
	try
	{
		for( PFSX::SetupMetaVector::const_iterator itor = m_packs.begin(); itor!=m_packs.end(); ++itor)
		{
			const std::wstring& packname = (*itor).GetName();
			std::wstring zipname = packname + L".pfs";
			std::wstring zipfilename = GetDesName() + zipname;
			m_listener->OnNotify( WPARAM( zipname.c_str()), FILENAME);

			PFS::CFile fileio;
			if( !fileio.Open( zipfilename, PFS::FM_CREAT, PFS::FA_WRONLY))
			{
				m_listener->OnNotify( WPARAM( zipname.c_str()), FILEERROR);
				KHMsgLog_Debug( L"Open file error %s(%d)\n", zipname.c_str(), PFS::CEnv::GetLastError());
				throw CInstallError();
			}
			PFS::CPFSWriter writter;
			writter.SetFileIO( fileio);

			PFS::CMetaInfoMap mm;
			for( std::vector<std::wstring>::const_iterator itor = m_zipfiles.begin(); itor!=m_zipfiles.end(); ++itor)
			{
				const std::wstring& zipfile = *itor;
				std::wstring mountstr = zipfile;
				mountstr.erase( mountstr.find_first_of( L'.'));

				if( !PFS::CEnv::Mount( packname, mountstr + packname, PFS::FST_NATIVE, PFS::MT_READONLY))
				{
					m_listener->OnNotify( WPARAM( zipname.c_str()), FILEERROR);
					KHMsgLog_Debug( L"mount error %s(%d)\n", packname.c_str(), PFS::CEnv::GetLastError());
					throw CInstallError();
				}
				
				PFS::CMetaInfoMap mmChild;
				PFS::CEnv::GetMetaInfos( packname, mmChild);
				size_t len = (size_t)GetFileLen( mmChild);	
				if( len > 0)
				{
					WPARAM wParam = 0;
					float temp = (float)len;
					memcpy( &wParam, &temp, sizeof(float));
					m_listener->OnNotify( wParam, FILELEN);

					for( PFS::CMetaInfoMap::iterator itorMap = mmChild.begin(); itorMap != mmChild.end(); ++itorMap)
					{
						if( m_stop)
						{
							// 调 Unmount 避免内存泄露
							PFS::CEnv::Unmount( packname);
							return false;
						}
						const std::wstring& childname = itorMap->first;
						if( !WriteZipFile( packname + childname, childname, itorMap->second, writter))
						{
							m_listener->OnNotify( WPARAM( zipname.c_str()), FILEERROR);
							
							// 调 Unmount 避免内存泄露
							PFS::CEnv::Unmount( packname);
							KHMsgLog_Debug( L"WriteZipFile %s(%d)\n", childname.c_str(), PFS::CEnv::GetLastError());
							throw CInstallError();
						}
						//m_listener->OnNotify( itorMap->second.GetFileSize(), FILEPROGRESS);

						temp = (float)itorMap->second.GetFileSize();
						memcpy( &wParam, &temp, sizeof(float));
						m_listener->OnNotify( wParam, FILEEND);
					}
					mm.insert( mmChild.begin(), mmChild.end());
				}
				PFS::CEnv::Unmount( packname);
			}
			WriteZipMetaFile( writter, mm);
			writter.CloseFile();
		}
	}
	catch( ZIPFILE::CZipWriteError&)
	{
		throw CInstallError();
	}
	return true;
}

bool CInstallThread::WriteNativeMetaFile( PFS::CMetaInfoMap& mm)
{
	PFS::CFile metafile;
	metafile.Open( GetDesName() + PFS::CFileMetaInfo::GetMetaFileName(), PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY);
	PFS::CMetaFileWrite::DoWrite( metafile, mm);
	return true;
}

//const wchar_t UNICODE_WINDOWS_FILE_PREFIX = 0xFEFF;
bool CInstallThread::WriteZipMetaFile( ZIPFILE::CFileWriter& writter, PFS::CMetaInfoMap& mm)
{
	writter.SetZipLevel( 0);

	ZIPFILE::CFileCentral central;
	central.m_name = PFS::CFileMetaInfo::GetMetaFileName();
	central.m_name.erase( 0, central.m_name.find_first_not_of( L'/'));
	central.m_usMethod = 0;
	if( !writter.WriteNextCentral( central))
		return false;
	PFS::CDataBlock data;
	PFS::CMetaDataWrite::DoWrite( data, mm);
	//data.insert( data.begin(), &UNICODE_WINDOWS_FILE_PREFIX, sizeof(wchar_t));
	writter.WriteFileData( data, true);
	return true;
}

void CInstallThread::End()
{
	m_packs.clear();
	for( std::vector<std::wstring>::const_iterator itor = m_zipfiles.begin(); itor!=m_zipfiles.end(); ++itor)
	{
		const std::wstring& zipfile = *itor;
		std::wstring mountstr = zipfile;
		mountstr.erase( mountstr.find_first_of( L'.'));
		PFS::CEnv::Unmount( mountstr);
	}
	PFS::CEnv::Unmount( GetDesName());
}

bool CInstallThread::WriteNativeFile( const std::wstring& srcname, const std::wstring& desname, PFS::CMetaInfo& meta)
{
	PFS::CFile srcfile;
	if( !srcfile.Open( srcname, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}

	std::wstring name = GetDesName() + desname;
	PFS::CFile desfile;
	if( !desfile.Open( name, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
	{
		return false;
	}

	if( !PFSX::DecodeFile( &srcfile, &desfile, 
		PFSX::GetEncoderTypeFromFilePfsxAttributes( meta.GetFilePfsxAttributes())))
	{
		return false;
	}
	
	//while( size_t s = file.Read( m_buffer.GetData(), BUFFERSIZE))
	//{
	//	if( PFS::FILE_SIZE_ERROR == s)
	//		return false;
	//	m_buffer.Resize( s);
	//	s = desfile.Write( m_buffer.GetData(), s);
	//	if( PFS::FILE_SIZE_ERROR == s)
	//		return false;
	//	m_listener->OnNotify( s, FILEPROGRESS);
	//}
	//PFS::CImageData& srcdata = file.GetImage();
	//desfile.Write( srcdata.GetData(), srcdata.GetSize());

	desfile.SetEnd();
	return true;
}

class CZipWriteAdapter : public PFS::CBaseFile
{
	ZIPFILE::CFileWriter& m_writter;

public:
	CZipWriteAdapter( ZIPFILE::CFileWriter& writter) : m_writter( writter) {}

	virtual size_t Read( void* data, size_t size) { return PFS::FILE_SIZE_ERROR; }
	virtual size_t Write( const void* data, size_t size)
	{
		return m_writter.WriteDeflateData( data, size, false);
	}
	virtual fsize_t GetSize() { return PFS::FILE_SIZE_ERROR; }
	virtual fsize_t Seek( foff_t pos, PFS::FILE_SEEK_METHOD fsm) { return PFS::FILE_SIZE_ERROR; }
	virtual fsize_t GetPos() { return PFS::FILE_SIZE_ERROR; }
	virtual bool SetEnd() { return true; }
	virtual bool ReadAll( void* data, fsize_t size ){ return false; }
	virtual PFS::CImageData GetImage() { return PFS::CImageData(); }
};

bool CInstallThread::WriteZipFile( const std::wstring& srcname, const std::wstring& desname, 
								  PFS::CMetaInfo& meta, ZIPFILE::CFileWriter& writter)
{
	PFS::CFile srcfile;
	if( !srcfile.Open( srcname, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}
	PFS::CImageFile srctemp = PFS::CImageFile( srcfile.GetImage());

	const PFSX::TSID ZipID = PFSX::MakeTypeIDByName( 'Z', 'I', 'P');
	const PFSX::TSID X2OID = PFSX::MakeTypeIDByName( 'X', '2', 'O');
	const PFSX::TSID ZpdID = PFSX::MakeTypeIDByName( 'Z', 'P', 'D');

	ZIPFILE::CFileCentral central;
	central.m_name = desname;
	central.m_name.erase( 0, central.m_name.find_first_not_of( L'/'));
	central.m_uiSizeOrg = (unsigned int)meta.GetFileSize();	// 源文件的大小
	central.m_uiCRC32 = PFSX::GetCRC32FromFilePfsxAttributes( meta.GetFilePfsxAttributes());

	PFSX::TSID attr = PFSX::GetEncoderTypeFromFilePfsxAttributes( meta.GetFilePfsxAttributes());
	if( ZpdID == attr )
	{
		central.m_usMethod = 8;
		central.m_uiSizeNow = (unsigned int)srcfile.GetSize();
	}
	else
	{
		central.m_usMethod = 0;
		central.m_uiSizeNow = central.m_uiSizeOrg;
	}

	if( !writter.WriteNextCentral( central))
	{
		return false;
	}
	CZipWriteAdapter destemp( writter);
	if( !PFSX::DecodeFile( &srctemp, &destemp, attr))
	{
		return false;
	}
	//while( size_t s = file.Read( m_buffer.GetData(), BUFFERSIZE))
	//{
	//	if( PFS::FILE_SIZE_ERROR == s)
	//	{
	//		return false;
	//	}
	//	m_buffer.Resize( s);
	//	if( PFS::FILE_SIZE_ERROR == writter.WriteDeflateData( m_buffer, false))
	//		return false;
	//}
	//PFS::CImageData& srcdata = file.GetImage();
	//writter.WriteFileData( srcdata.GetData(), srcdata.GetSize(), true);
	return true;
}

bool CInstallThread::IsZipFile( const std::wstring& filename)
{
	for( PFSX::SetupMetaVector::iterator itor = m_packs.begin(); itor!=m_packs.end(); ++itor)
	{
		const std::wstring& dir = (*itor).GetName();
		if( dir.size() > filename.size())
			continue;
		if( 0 != wcsncmp( dir.c_str(), filename.c_str(), dir.size()))
			continue;
		if( L'/' != filename[dir.size()])
			continue;
		return true;
	}
	return false;
}