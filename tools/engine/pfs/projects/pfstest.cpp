#include <conio.h>
#include <iostream>
#include <sstream>
#include <iomanip>
#include <windows.h>
#include <mmsystem.h>
#include "../pfs2.h"
#include "../filemarshal.h"
#include "../../xmlio/xmlio.h"
#include "../mountmanager.h"
#include "../filesmetafile.h"

#pragma comment( lib, "winmm.lib")
#pragma comment( lib, "../contrib/perf/perf.lib")

#include <strsafe.h>

namespace DEVICESYSTEMTEST
{
	void Test();
}

namespace MOUNTMANAGER
{
	void Test();
}


namespace NATIVETEST
{
	void Test();
}

namespace ZIPTEST
{
	void Test();
}

namespace ZIPFILETEST
{
	void Test();
}

namespace BRIEFTEST
{
	void Test();
}


namespace INETFILETEST
{
	void Test();
}

void DumpMetaInfoMap( const PFS::CMetaInfoMap& mim)
{
	for( PFS::CMetaInfoMap::const_iterator it = mim.begin(); it != mim.end(); ++ it)
	{
		const std::wstring& filename = it->first;
		const PFS::CMetaInfo& info = it->second;

		printf( "%S : %d\n", filename.c_str(), info.GetFileSize());
		//info.GetFileHash().Dump();
		printf( "%S\n\n", info.GetFileDownloadURI().c_str());

	} // for( PFS::CMetaInfoMap::iterator it = mim.begin(); it != mim.end(); ++ it)
}

void DumpErrorCode()
{
	const char*	szError = NULL;
	int nCode = PFS::CEnv::GetLastError();
	switch( nCode)
	{
	case PFS::EC_DIR_HAS_ALREADY_MOUNT:
		szError = "目录已经映射到了一个设备";
		break;
	case PFS::EC_INVALID_MOUNT_DEVICE:
		szError = "无效的Mount设备";
		break;
	case PFS::EC_FSTYPE_UNEXISTING:
		szError = "文件系统类型不存在";
		break;
	case PFS::EC_MOUNT_DIR_UNFOUND:
		szError = "无法找到该被映射的目录";
		break;
	case PFS::EC_FS_BUSY_NOW:
		szError = "文件系统正在被使用";
		break;
	case PFS::EC_FILE_NOT_MOUNT:
		szError = "没有找到相应的映射目录";
		break;
	case PFS::EC_FILE_NOT_FOUND:
		szError = "没有找到文件";
		break;
	case PFS::EC_FS_READONLY:
		szError = "文件系统目前只读";
		break;
	case PFS::EC_FILE_OPEN_MODE:
		szError = "文件打开方式不支持";
		break;
	case PFS::EC_NATIVE_DEVICE_LOST:
		szError = "无法找到原始文件设备";
		break;
	case PFS::EC_COPY_DST_EXISTING:
		szError = "文件拷贝的目标已经存在";
		break;
	case PFS::EC_INVALID_FILENAME:
		szError = "无效的文件名";
		break;
	case PFS::EC_ACCESS_DENIED:
		szError = "拒绝访问";
		break;
	case PFS::EC_SHARING_VIOLATION:
		szError = "访问违例";
		break;
	default:
		break;
	}
	if( NULL == szError)
		printf( "不明错误[%d]\n", nCode);
	else
		printf( "%s[%d]\n", szError, nCode);
}

void CheckFSMetaInfo( const std::wstring& dir)
{
	PFS::CMetaInfoMap	mim;
	if( PFS::CEnv::GetMetaInfos( dir, mim))
		DumpMetaInfoMap( mim);
	else
		DumpErrorCode();
		
	if( !PFS::CEnv::CheckMetaInfo( dir))
		DumpErrorCode();
}

void CheckOpenFile( const std::wstring& filename)
{
	PFS::CBaseFile*	MyFile = PFS::CEnv::OpenFile( filename, PFS::FM_EXCL, PFS::FA_RDONLY);
	if( NULL == MyFile)
	{
		DumpErrorCode();
		return;
	}

	char	szBuffer[1024];
	while( size_t s = MyFile->Read( szBuffer, 1023))
	{
		szBuffer[s] = 0;
		printf( szBuffer);
	}
	PFS::CEnv::CloseFile( MyFile);
}

void CheckMountFileSystem()
{
	if( PFS::CEnv::Mount( L"/test", L"/run/psp/mp_root", PFS::FST_NATIVE, PFS::MT_WRITABLE))
	{
		CheckFSMetaInfo( L"/test");
		CheckOpenFile( L"/test/100mnv01/mp.txt");
	}
	else
	{
		DumpErrorCode();
	}
}

void CheckRemoveFile()
{
	PFS::CMetaInfoMap	mim;
	if( PFS::CEnv::GetMetaInfos( L"/run", mim))
		printf( "/run count = %d\n", mim.size());
	else
		DumpErrorCode();

	if( PFS::CEnv::GetMetaInfos( L"/test", mim))
		printf( "/test count = %d\n", mim.size());
	else
		DumpErrorCode();

	if( !PFS::CEnv::RemoveFile( L"/test/100mnv01/dscf0145.bmp"))
		DumpErrorCode();

	if( PFS::CEnv::GetMetaInfos( L"/run", mim))
		printf( "/run count = %d\n", mim.size());
	else
		DumpErrorCode();

	if( PFS::CEnv::GetMetaInfos( L"/test", mim))
		printf( "/test count = %d\n", mim.size());
	else
		DumpErrorCode();
}

void CheckRemoveFileIfOpened()
{
	bool b = PFS::CEnv::Mount( L"/run", L"D:/temp", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		printf( "Mount fs failed!\n ");
		return;
	}

	PFS::CBaseFile* fp = PFS::CEnv::OpenFile( L"/run/ab.txt", PFS::FM_CREAT, PFS::FA_RDWR );
	if( !fp )
	{
		DumpErrorCode();
		return;
	}

	wchar_t xxx[] = L"\xFF\xFE abcxxxxxx";
	fp->Write( xxx, sizeof(xxx) );

	if( !PFS::CEnv::RemoveFile( L"/run/ab.txt" ) )
		DumpErrorCode();

	PFS::CEnv::CloseFile( fp );

	//if( !PFS::CEnv::RemoveFile( L"/run/ab.txt" )
	//	DumpErrorCode();

	PFS::CEnv::Unmount( L"/run");	
}

void CheckRemoveFileIfOpened2()
{
	bool b = PFS::CEnv::Mount( L"/run", L"D:/temp/ab.pfs", PFS::FST_ZIP, PFS::MT_WRITABLE);
	if( !b)
	{
		printf( "Mount fs failed!\n ");
		return;
	}

	PFS::CBaseFile* fp = PFS::CEnv::OpenFile( L"/run/ab.txt", PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_RDWR );
	if( !fp )
	{
		DumpErrorCode();
		return;
	}

	char xxx[] = "\xFE\xFF";
	fp->Write( xxx, sizeof(xxx) );

	wchar_t xxx2[] = L"abcxxxxxx";
	fp->Write( xxx2, sizeof(xxx2) );

	//if( !PFS::CEnv::RemoveFile( L"/run/ab.txt" ) )
	//	DumpErrorCode();

	PFS::CEnv::CloseFile( fp );

	if( !PFS::CEnv::RemoveFile( L"/run/ab.txt" ) )
		DumpErrorCode();
	
	fp = PFS::CEnv::OpenFile( L"/run/ab.txt", PFS::FM_EXCL, PFS::FA_RDWR );
	if( !fp )
	{
		DumpErrorCode();
	}

	PFS::CEnv::Unmount( L"/run");	
}


void CheckCopyFile()
{
	if( !PFS::CEnv::FileCopy( L"/test/play.java", L"/run/play.java"))
	{
		DumpErrorCode();
		if( !PFS::CEnv::FileCopy( L"/test/play.java", L"/run/play.java", false))
			DumpErrorCode();
	}
}

void CheckFileName()
{
	bool b = PFS::CEnv::Mount( L"/dc", L"/run/datacore", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		printf( "Mount /dc /run/datacore failed!\n ");
		return;
	}
	const std::wstring oldfilename = L"D:\\temp\\datacore\\wdb\\beans\\Appearance.hpp";
	std::wstring newname;
	b = PFS::CEnv::NativeFileNameToPFSFileName( oldfilename, newname);
	printf( "%d old %S new %S\n", b, oldfilename.c_str(), newname.c_str());

	std::wstring newname2;
	b = PFS::CEnv::PFSFileNameToNativeFileName( newname, newname2);
	printf( "%d old %S new %S\n", b, newname.c_str(), newname2.c_str());

	PFS::CEnv::Unmount( L"/dc");
}

namespace TRANTEST
{
	void Test();
};

void PfsCopyTest()
{
	using namespace PFS;
	std::wstring device = L"D:\\2DGame\\launcher\\launcher.zip";
	bool bRet = CEnv::Mount( L"/res", device, FST_ZIP, MT_READONLY );
	if ( !bRet )
	{
		return;
	}

	bRet = CEnv::CheckMetaInfo( L"/res");
	if( !bRet )
	{
		return;
	}

	CMetaInfoMap paths ;
	if( !CEnv::GetMetaInfos(L"/res", paths ) )
	{
		return;
	}

	std::wstring device_2 = L"D:\\2DGame\\launcher\\unzip\\pe\\d2";
	bRet = CEnv::Mount( L"/unzipped", device_2, FST_NATIVE, MT_WRITABLE );
	if ( !bRet )
	{
		return;
	}

	CMetaInfoMap::const_iterator itr = paths.begin();
	for( ; itr != paths.end(); ++ itr )
	{
		const std::wstring& path =  (*itr).first;
		CEnv::FileCopy( L"/res" + path , L"/unzipped" + path, false);
		std::wcout<< path << L" was copied to " <<  device_2 + path << std::endl;
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// 必须执行Unmount，否则会导致内存泄露
	//
	CEnv::Unmount(L"/unzipped");
	CEnv::Unmount(L"/res");
}

void PfsCopyTest2()
{
	using namespace PFS;
	std::wstring device = L"D:\\zippfs";
	std::wstring device_2 = L"D:\\zippfs.zip";

	bool bRet = CEnv::Mount( L"/unzipped", device, FST_NATIVE, MT_READONLY);
	if ( !bRet )
	{
		return;
	}

	bRet = CEnv::Mount( L"/res", device_2, FST_ZIP, MT_WRITABLE );
	if ( !bRet )
	{
		return;
	}

	bRet = CEnv::CheckMetaInfo( L"/unzipped");
	if( !bRet )
	{
		return;
	}

	CMetaInfoMap paths ;
	if( !CEnv::GetMetaInfos(L"/unzipped", paths ) )
	{
		return;
	}

	CMetaInfoMap::const_iterator itr = paths.begin();
	for( ; itr != paths.end(); ++ itr )
	{
		const std::wstring& path =  (*itr).first;
		CEnv::FileCopy( L"/unzipped" + path , L"/res" + path, false);
		std::wcout<< path << L" was copied to " <<  device_2 + path << std::endl;
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// 必须执行Unmount，否则会导致内存泄露，并且zip文件不保存写入的文件。
	//
	CEnv::Unmount(L"/unzipped");
	CEnv::Unmount(L"/res");
}

class MyNotify : public  PFSX::CAyncReadNotify
{
public:
	MyNotify() {}
	virtual ~MyNotify() {}
protected:
	virtual void OnReady( int nError, const std::wstring& filename, const PFS::CImageData& data) 
	{
		wprintf( L"OnReady %s %d\n", filename.c_str(), nError);
	}
};

class PieceNotify : public  PFSX::CAyncReadNotify
{
public:
	PieceNotify() {}
	virtual ~PieceNotify() {}
protected:
	virtual void OnReady( int nError, const std::wstring& filename, const PFS::CImageData& data) 
	{
		wprintf( L"Error %s %d\n", filename.c_str(), nError);
	}
	virtual bool DoAfterRead( int nError, const std::wstring& filename, const PFS::CImageData& data, size_t left) 
	{
		std::string		str;
		if( data.GetSize())
			str.assign( (const char*)data.GetData(), (size_t)data.GetSize());
		wprintf( L"DoAfterRead %d %s %d %S\n", nError, filename.c_str(), left, str.c_str());
		return 0 == left; 
	} 
};

void DoCheckImage()
{
	bool b = PFS::CEnv::Mount( L"/run", L"D:\\temp", PFS::FST_NATIVE, PFS::MT_READONLY);

	PFS::CFile		file;

	b = file.Open( L"/run/out.txt", PFS::FM_EXCL, PFS::FA_RDONLY);

	PFS::CImageData data = file.GetImage();

	printf( "%s", data.GetData());
}

void DoCheckMarshal()
{
	bool b = PFS::CEnv::Mount( L"/run", L"D:\\temp", PFS::FST_NATIVE, PFS::MT_WRITABLE);

	{
		PFS::CFile		file;

		bool b = file.Open( L"/run/marshal.bin", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY);

		PFS::CFileStream		fs( file);

		char	c = 12;
		short	s = 23456;
		long	l = 1234566778;
		fs << c << s << l;
	}

	{
		PFS::CFile		file;

		bool b = file.Open( L"/run/marshal.bin", PFS::FM_EXCL, PFS::FA_RDONLY);

		PFS::CFileStream		fs( file);

		char	c = 0 ;
		short	s = 0;
		long	l = 0;
		fs >> c >> s >> l;
		
		printf( " %02x %04x %08x\n", c, s, l);
	}
}

namespace UPDATETEST
{
	void Test();
};

void DoTestGetMetaInfosEx()
{
	PFS::CEnv::Mount( L"/run/run", L"D:\\temp", PFS::FST_NATIVE);
//	PFS::CEnv::CheckMetaInfo( L"/run/run");
	PFS::CEnv::Mount( L"/run/run/test/swfr", L"/run/run/swfr", PFS::FST_NATIVE);

	PFS::CMetaInfoMap		mim;
	PFS::CEnv::GetMetaInfosEx( L"/run/run", mim);
	PFS::CEnv::GetMetaInfosEx( L"/run", mim);
	PFS::CEnv::GetMetaInfosEx( L"/run/run/test", mim);
}


int xxxx( )
{
	bool bRes = PFS::CEnv::Mount( L"/run", L"D:\\temp", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	PFS::CEnv::Mount( L"/run/bmp", L"/run/bmp", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( bRes)
	{
		PFS::CFile file;
		file.Open( L"/run/bmp/1.txt", PFS::FM_EXCL, PFS::FA_RDONLY);
		PFS::CEnv::Unmount( L"/run");
	}
	
/*
	bool b = PFS::CEnv::Mount( L"/run", L"D:\\temp\\url", PFS::FST_BRIEF, PFS::MT_READONLY);
//	bool b = PFS::CEnv::Mount( L"/run", L"D:\\temp\\temp.zip", PFS::FST_ZIP, PFS::MT_WRITABLE);
//	b = PFS::CEnv::Mount(L"/outzip", L"/run/aaa.zip", PFS::FST_ZIP, PFS::MT_WRITABLE);
	PFS::CEnv::Mount( L"/des", L"D:\\temp\\url", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( b)
	{

		PFS::CEnv::FileCopy( L"/run/file.rar", L"/des/file.rar", false);

		PFS::CBaseFile* file = PFS::CEnv::OpenFile( L"/run/file.rar", PFS::FM_EXCL, PFS::FA_RDONLY);
		if( NULL == file)
			DumpErrorCode();
		else
		{
			PFS::CMetaInfoMap mm;
			PFS::CMetaFileRead::DoRead( *file, mm);

			char temp[1024] = {0};
			file->Read( temp, 1024);
			file->Write( "222222", 6);
			PFS::CEnv::CloseFile( file);
		}
		PFS::CEnv::Unmount( L"/outzip");

		//CheckFileName();

//		CheckFSMetaInfo( L"/run");
//		CheckOpenFile( L"/run/psp/readme");
//		CheckMountFileSystem();
//		CheckRemoveFile();
//		CheckCopyFile();
		if( !PFS::CEnv::CheckMetaInfo( L"/run"))
			DumpErrorCode();
		PFS::CEnv::Unmount( L"/run");
	}

	PFS::CEnv::Mount( L"/system", L"c:", PFS::FST_NATIVE);
*/

//	DoCheckMarshal();
//	return 0 ;

	bool b = PFS::CEnv::Mount( L"/run", L"D:\\temp", PFS::FST_NATIVE, PFS::MT_READONLY);
	PFSX::AsyncReadInitialize();

	MyNotify		mn;
	PieceNotify		pn;

	DWORD		dwTimeTest;

	if( 0)
	{
		PFS::CFile		file;
		PFS::CDataBlock	buffer;

		dwTimeTest = timeGetTime();
		file.Open( L"/run/data.txt", PFS::FM_EXCL, PFS::FA_RDONLY);
		size_t size = (size_t)file.GetSize();
		buffer.Reserve( size);
		size = file.Read( buffer.GetData(), size);
		buffer.Resize( size);
		file.Close();


		dwTimeTest = timeGetTime() - dwTimeTest;
		printf( "CFile open time = %d\n", dwTimeTest);
		file.Close();
	}



	PFSX::CSyncFile		sf;

	dwTimeTest = timeGetTime();
	sf.Open( L"/run/data.txt");
	dwTimeTest = timeGetTime() - dwTimeTest;
	printf( "CSyncFile open time = %d\n", dwTimeTest);

	{
		const size_t	sizebuffer = 64;
		char			Buffer[sizebuffer];
		
		while( size_t s = sf.Read( Buffer, sizebuffer - 1))
		{
			Buffer[s] = 0;
			printf( "%s", Buffer);
		}
	}

	PFSX::AsyncRead( L"/run/test.txt", pn);
	PFSX::AsyncRead( L"/run/2ui14.bmp", mn);
	PFSX::AsyncRead( L"/run/2ui04.bmp", mn);
	PFSX::AsyncRead( L"/run/mainui.bmp", mn);

	while( true)
	{
		PFSX::AsyncReadUpdate();
		PFSX::CSyncFile		sf;
		sf.Open( L"/run/data.zip");
	}
	_getch();
	return 0;
}

namespace URLTEST
{
	void Test();
}

void MountManagerTest()
{
	//::SetCurrentDirectory(L"D:/projects/release2005");
	//assert( PFSX::CMountManager::MountFromXML( L"D:/projects/release2005/wtf/mount.xml" ) == 0 );
	//PFSX::CMountManager::UnmountPFS();

	//::SetCurrentDirectory(L"D:/projects/release");
	//assert( PFSX::CMountManager::MountFromXML( L"D:/projects/release/wtf/mount.xml" ) == 0 );
	//PFSX::CMountManager::UnmountPFS();

	::SetCurrentDirectory(L"E:/2DGame");
	assert( PFSX::CMountManager::MountFromXML( L"E:/2DGame/wtf/mount.xml" ) == 0 );
	PFSX::CMountManager::UnmountPFS();
}

namespace MPPC_TEST
{
	void Test();
}

namespace STR_TEST
{
	void Test();
}


// 注册PFS的log
class CPFSTestLog : public  PFS::COutLog
{
public:
	virtual void Print( const wchar_t* lpszFormat, ...)
	{
		WCHAR szBuffer[16*1024] = {0};
		va_list args;
		va_start(args, lpszFormat);
		HRESULT hr = StringCchVPrintfW(szBuffer, _countof(szBuffer), lpszFormat, args);
		va_end(args);
		if(SUCCEEDED(hr))
		{
			OutputDebugStringW(szBuffer);
			wprintf( L"%s", szBuffer );
		}
		else
		{
			assert(FALSE);
		}
	}
	virtual void VPrint( const wchar_t* info, va_list vl)
	{
	}
} g_PFSTestLog;




BOOL UnzipResource( LPCTSTR lpszInputZipFile, LPCTSTR lpszOutputDir )
{
	assert( lpszOutputDir != NULL && lpszInputZipFile != NULL );
	
	const std::wstring WEB_SRC = L"/web_src";
	const std::wstring WEB_DES = L"/web_des";
	
	bool res = false;
	do
	{
		res = PFS::CEnv::Mount( WEB_SRC, lpszInputZipFile, PFS::FST_ZIP, PFS::MT_READONLY );
		if( !res ) { break; }

		res = PFS::CEnv::CheckMetaInfo( WEB_SRC );
		if( !res ) { break; }

		PFS::CMetaInfoMap paths ;
		res = PFS::CEnv::GetMetaInfos( WEB_SRC, paths );
		if( !res ) { break; }

		res = PFS::CEnv::Mount( WEB_DES, lpszOutputDir, PFS::FST_NATIVE, PFS::MT_WRITABLE );
		if( !res ) { break; }

		for( PFS::CMetaInfoMap::const_iterator i=paths.begin(), e=paths.end(); i != e; ++i )
		{
			const std::wstring& path = i->first;
			if( std::find_if( path.begin(), path.end(), ::iswupper) != path.end())
			{
				if( _wcsicmp( path.substr(path.size()-std::wstring(L"thumbs.db").size()).c_str(), 
					L"thumbs.db" ) == 0 )
				{
					continue;
				}
			}
			res = PFS::CEnv::FileCopy( WEB_SRC + path , WEB_DES + path, false);
			if( !res ) { break; }
		}
	}
	while(0);

	int nLastError = 0;
	if( !res )
		nLastError = PFS::CEnv::GetLastError();

	PFS::CEnv::Unmount( WEB_SRC);
	PFS::CEnv::Unmount( WEB_DES);

	if( !res )
		PFS::CEnv::SetLastError(nLastError);

	return res;
}

bool TestFilesMetaVersion()
{
	std::wstring valueVersion_1_1 = L"1.1", valueVersion_1_0 = L"1.0";
	std::wstring valueVersion11, valueVersion10, valueVersiondefault;

	bool res = false;
	do
	{
		res = PFS::CEnv::Mount( L"/r", L"D:\\temp", PFS::FST_NATIVE, PFS::MT_WRITABLE );
		if( !res ) { break; }
	
		res = PFS::CEnv::Mount( L"/v11", L"D:\\temp\\v11", PFS::FST_NATIVE, PFS::MT_WRITABLE );
		if( !res ) { break; }
	
		res = PFS::CEnv::Mount( L"/v10", L"D:\\temp\\v10", PFS::FST_NATIVE, PFS::MT_WRITABLE );
		if( !res ) { break; }
	
		res =PFS::CEnv::SetOption(PFS::PFS_OPTION_BASE_FILESMETAVERSION, L"/v11", valueVersion_1_1 );
		if( !res ) { break; }
		res =PFS::CEnv::SetOption(PFS::PFS_OPTION_BASE_FILESMETAVERSION, L"/v10", valueVersion_1_0 );
		if( !res ) { break; }
		res =PFS::CEnv::GetOption(PFS::PFS_OPTION_BASE_FILESMETAVERSION, L"/v11", valueVersion11 );
		if( !res ) { break; }
		res =PFS::CEnv::GetOption(PFS::PFS_OPTION_BASE_FILESMETAVERSION, L"/v10", valueVersion10 );
		if( !res ) { break; }

		res =PFS::CEnv::GetOption(PFS::PFS_OPTION_BASE_FILESMETAVERSION, L"/r", valueVersiondefault );
		if( !res ) { break; }

		{
			PFS::CFile		file;
			bool b = file.Open( L"/r/marshal.bin", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_RDWR);
			PFS::CFileStream fs( file);
			char	c = 12;
			short	s = 23456;
			long	l = 1234566778;
			fs << c << s << l;

			PFS::CMetaInfo mi;
			PFS::CFileMetaInfo::MakeFileMetaInfoBoth(file, mi);
			PFS::CEnv::SetFileMetaInfo(L"/r/marshal.bin", mi );
		}

		{
			PFS::CFile		file;
			bool b = file.Open( L"/v11/marshal.bin", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_RDWR);
			PFS::CFileStream fs( file);
			char	c = 12;
			short	s = 23456;
			long	l = 1234566778;
			fs << c << s << l;

			
			PFS::CMetaInfo mi;
			PFS::CFileMetaInfo::MakeFileMetaInfoBoth(file, mi);
			PFS::CEnv::SetFileMetaInfo(L"/v11/marshal.bin", mi );
		}

		{
			PFS::CFile		file;
			bool b = file.Open( L"/v10/marshal.bin", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_RDWR);
			PFS::CFileStream fs( file);
			char	c = 12;
			short	s = 23456;
			long	l = 1234566778;
			fs << c << s << l;

			PFS::CMetaInfo mi;
			PFS::CFileMetaInfo::MakeFileMetaInfoBoth(file, mi);
			PFS::CEnv::SetFileMetaInfo(L"/v10/marshal.bin", mi );
		}


	}while(0);

	PFS::CEnv::Unmount(L"/v10");
	PFS::CEnv::Unmount(L"/v11");
	PFS::CEnv::Unmount(L"/r");

	if( valueVersion_1_1 != valueVersion11 )
	{
		return false;
	}
		
	if( valueVersion_1_0 != valueVersion10 )
	{
		return false;
	}
	
	if( valueVersion_1_0 != valueVersiondefault )
	{
		return false;
	}

	return true;
}

int main( int argc, char* argv[])
{

	ZIPTEST::Test();

	std::cout.imbue( std::locale( ".936" ) );
	std::wcout.imbue( std::locale( ".936" ) );
	std::cerr.imbue( std::locale( ".936" ) );
	std::wcerr.imbue( std::locale( ".936" ) );

	::setlocale( LC_ALL, ".936");
	PFS::CEnv::SetOutLogInstance(&g_PFSTestLog);

	PFS::CEnv::Mount( L"/aaa", L"D:\\temp\\mapcopy0509.pfs", PFS::FST_RUNZIP, PFS::MT_READONLY | PFS::MT_RUNTIME);

	{
		PFS::CBaseFile* file = PFS::CEnv::OpenFile( L"/aaa/model/静态模型/东浮/张修真东浮工业区2/texture/张修真工业区1.dds", PFS::FM_EXCL, PFS::FA_RDONLY);
		if( NULL != file)
			PFS::CEnv::CloseFile( file);
	}

	{
		PFS::CBaseFile* file = PFS::CEnv::OpenFile( L"/aaa/model/静态模型/东浮/张修真东浮工业区2/texture/张修真工业区10.dds", PFS::FM_EXCL, PFS::FA_RDONLY);
		if( NULL != file)
			PFS::CEnv::CloseFile( file);
	}

	PFS::CEnv::DumpInfos( g_PFSTestLog);

//	TestFilesMetaVersion();

	//UnzipResource(L"D:\\test\\launcher.zip.", L"D:\\test\\webpage");

	//STR_TEST::Test();
	//NATIVETEST::Test();
	
	//ZIPFILETEST::Test();
	//MPPC_TEST::Test();

	//MountManagerTest();
	//xxxx();
	//CheckRemoveFileIfOpened();
	//CheckRemoveFileIfOpened2();
	//PfsCopyTest();
	//PfsCopyTest2();
	//TRANTEST::Test();
	//UPDATETEST::Test();
	//URLTEST::Test();
	//DoCheckImage();
	//DoTestGetMetaInfosEx();
	//StringTests();

	//BRIEFTEST::Test();
	//INETFILETEST::Test();


	//MOUNTMANAGER::Test();


	//DEVICESYSTEMTEST::Test();
	return 0;
}
