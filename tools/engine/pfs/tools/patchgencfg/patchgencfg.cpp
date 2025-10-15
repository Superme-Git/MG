// patchgencfg.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <string>
#include "../../pfs2.h"
#include "../../src/utility/md5hash.h"
#include <iostream>
#include <iomanip>

class CFileHelperBase
{
public:
	void TryFindFile( const std::wstring& path, const std::wstring& name, bool br )
	{
		if( br)
		{
			std::wstring	find = path + L"\\*.*";
			WIN32_FIND_DATAW		wfd;
			HANDLE hfind = FindFirstFileW( find.c_str(), &wfd);
			if( INVALID_HANDLE_VALUE == hfind)
				return;
			OnFoundPath( path, name, wfd, br );
			while( FindNextFileW( hfind, &wfd))
				OnFoundPath( path, name, wfd, br);
			FindClose( hfind);
		}

		{
			std::wstring	find = path + L"\\" + name;
			WIN32_FIND_DATAW		wfd;
			HANDLE hfind = FindFirstFileW( find.c_str(), &wfd);
			if( INVALID_HANDLE_VALUE == hfind)
				return;
			OnFoundFile( path, name, wfd );
			while( FindNextFileW( hfind, &wfd ) )
				OnFoundFile( path, name, wfd );
			FindClose( hfind);
		}
	}

	void OnFoundPath( const std::wstring& path, const std::wstring& name, WIN32_FIND_DATAW& wfd, bool br )
	{
		if( (wfd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) != FILE_ATTRIBUTE_DIRECTORY)
			return;
		if( L'.' == wfd.cFileName[0])
			return;
		_ASSERTE(br);

		std::wstring newpath = path + L"\\" + wfd.cFileName;
		TryFindFile( newpath, name, br );
	}

	void OnFoundFile( const std::wstring& path, const std::wstring& name, WIN32_FIND_DATAW& wfd )
	{
		if( (wfd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) == FILE_ATTRIBUTE_DIRECTORY)
			return;

		std::wstring filename = path + L"\\" + wfd.cFileName;

		OnFindFile( filename );
	}

protected:
	virtual void OnFindFile( const std::wstring& filename ){}
};

const wchar_t CFG[] = L".cfg";	// 保存文件下载进度的配置文件后缀名

class CGenCFG : public CFileHelperBase
{

	PFS::helper::CMD5FileHashMethod hashMethod;

protected:	
	bool WriteCfgFile( PFS::CFile& cfgFile, size_t offset, PFS::fsize_t total, const PFS::FILE_DATA_HASH& hash  )
	{
		cfgFile.Seek( 0, PFS::FSM_SET);
		if( sizeof( size_t) != cfgFile.Write( &offset, sizeof( size_t)))
		{
			return false;
		}

		if ( total != (PFS::fsize_t)-1 )
		{
			if( sizeof( PFS::fsize_t) != cfgFile.Write( &total, sizeof( PFS::fsize_t)))
			{
				return false;
			}
		}

		if ( hash != 0 )
		{
			if( sizeof( PFS::FILE_DATA_HASH) != cfgFile.Write( &hash,sizeof( PFS::FILE_DATA_HASH) ) )
			{
				return false;
			}
		}

		return true;
	}

	virtual void OnFindFile( const std::wstring& filename )
	{
		PFS::CFile file;
		if( !file.Open(filename, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout << L"Open file failed " << filename << PFS::CEnv::GetLastError() << std::endl;
			return;
		}

		PFS::fsize_t total = file.GetSize();
		PFS::FILE_DATA_HASH hash;
		hashMethod.MakeFileHash(file, hash);
		std::wstring cfg = filename + CFG;			
		PFS::CFile cfgFile;
		if( !cfgFile.Open( cfg, PFS::FM_CREAT, PFS::FA_RDWR ) )
		{
			std::wcerr << L"Open file failed " << cfg << PFS::CEnv::GetLastError() << std::endl;
			return;
		}

		if( !WriteCfgFile(cfgFile, (size_t)total, total, hash ) )
		{
			std::wcerr << L"WriteCfgFile failed " << cfg << PFS::CEnv::GetLastError() << std::endl;
			return;
		}

		std::wcout << L"WriteCfgFile done : " << cfg  << std::endl;
		std::wcout << L"size: " << std::setbase(10) << total << L" " << std::setbase(16) << std::setfill(L'0') << std::setw(8) << total << std::endl;
		std::wcout << L"hash: " << std::setbase(10) << hash << L" " << std::setbase(16) << std::setfill(L'0') << std::setw(16) << hash << std::endl;
	}
};

int _tmain(int argc, _TCHAR* argv[])
{
	if( argc < 2 )
	{
		std::wstring exepath = argv[0];
		std::wstring::size_type dot = exepath.rfind(L'\\');
		if( dot == std::wstring::npos )
		{
			dot = exepath.rfind(L'/');
		}

		if( dot != std::wstring::npos )
		{
			exepath.assign( &(argv[0][dot + 1]) );
		}

		std::wcerr << L"Usage: " << exepath << L" <filenames-or-wildcards> ..." << std::endl;
		return 1;
	}

	CGenCFG x;	
	for( int a = 1; a < argc; a ++ )
	{
		x.TryFindFile( L".", argv[a], false );
	}

	return 0;
}
