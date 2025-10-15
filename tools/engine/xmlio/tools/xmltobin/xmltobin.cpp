// xmltobin.cpp : Defines the entry point for the console application.

#include "../../../pfs/pfs2.h"
#include "../../xmlio.h"

void OnFoundFile( const std::wstring& path, const std::wstring& name, WIN32_FIND_DATAW& wfd, bool br, size_t size, bool bUnicode);
void OnFoundPath( const std::wstring& path, const std::wstring& name, WIN32_FIND_DATAW& wfd, bool br, size_t size, bool bUnicode);

int usage( wchar_t* exename)
{
	wprintf( L"%s [filename] [-r] [-s minsize] [-u]\n", exename);
	return -1;
}

void TryFindFile( const std::wstring& path, const std::wstring& name, bool br, size_t size, bool bUnicode)
{
	if( br)
	{
		std::wstring	find = path + L"\\*.*";
		WIN32_FIND_DATAW		wfd;
		HANDLE hfind = FindFirstFileW( find.c_str(), &wfd);
		if( INVALID_HANDLE_VALUE == hfind)
			return;
		OnFoundPath( path, name, wfd, br, size, bUnicode);
		while( FindNextFileW( hfind, &wfd))
			OnFoundPath( path, name, wfd, br, size, bUnicode);
		FindClose( hfind);
	}

	{
		std::wstring	find = path + L"\\" + name;
		WIN32_FIND_DATAW		wfd;
		HANDLE hfind = FindFirstFileW( find.c_str(), &wfd);
		if( INVALID_HANDLE_VALUE == hfind)
			return;
		OnFoundFile( path, name, wfd, br, size, bUnicode);
		while( FindNextFileW( hfind, &wfd))
			OnFoundFile( path, name, wfd, br, size, bUnicode);
		FindClose( hfind);
	}
}

void OnFoundPath( const std::wstring& path, const std::wstring& name, WIN32_FIND_DATAW& wfd, bool br, size_t size, bool bUnicode)
{
	if( (wfd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) != FILE_ATTRIBUTE_DIRECTORY)
		return;
	if( L'.' == wfd.cFileName[0])
		return;
	assert( br);

	std::wstring newpath = path + L"\\" + wfd.cFileName;
	TryFindFile( newpath, name, br, size, bUnicode);
}

void OnFoundFile( const std::wstring& path, const std::wstring& name, WIN32_FIND_DATAW& wfd, bool br, size_t size, bool bUnicode)
{
	if( (wfd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) == FILE_ATTRIBUTE_DIRECTORY)
		return;

	std::wstring filename = path + L"\\" + wfd.cFileName;

	{
		wprintf( filename.c_str() );

		PFS::fsize_t filesize = 0;
		if ( size > 0 )
		{
			PFSX::CNativeFile		nf;
			if( !nf.Open( filename, PFS::FM_EXCL, PFS::FA_RDONLY))
			{
				wprintf( L" - read failed : code %d!\n", PFS::CEnv::GetLastError());
				return;
			}
			filesize = nf.GetSize();
			if(  filesize < size )
			{
				wprintf( L" - ignored, file size %I64d < %d!\n", filesize, size);
				return;
			}
		}

		XMLIO::CFileReader		fr;
		int error = fr.OpenNativeFile( filename);
		if( error)
		{
			wprintf( L" - read failed : code %d!\n", error);
			return;
		}

		if( bUnicode)
		{
			std::wstring	strxml;
			fr.SaveAsString( strxml);
			if( strxml.empty())
			{
				wprintf( L" - SaveAsString failed! Empty string!\n");
				return;
			}

			PFSX::CNativeFile		file;
			if( !file.Open( filename, PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY))
			{
				wprintf( L" - write failed : code %d!\n", PFS::CEnv::GetLastError());
				return;
			}
			wchar_t UNICODE_WINDOWS_FILE_PREFIX = 0xFEFF;
			file.Write( &UNICODE_WINDOWS_FILE_PREFIX, sizeof( UNICODE_WINDOWS_FILE_PREFIX));
			file.Write( strxml.c_str(), strxml.size() * sizeof( wchar_t));
			file.Close();
		}
		else
		{
			PFS::CDataBlock	data;
			if( filesize > size )
			{
				if( filesize >= 5*1024*1024 )
				{
					data.Reserve( min( (size_t)filesize, 4*1024*1024 ) );
				}
				else
				{
					data.Reserve( min( (size_t)filesize, 2*1024*1024 ) );
				}
			}
			fr.SaveAsOctets(data);

			PFSX::CNativeFile		file;

			if( !file.Open( filename, PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY))
			{
				wprintf( L" - write failed : code %d!\n", PFS::CEnv::GetLastError());
				return;
			}

			file.Write( data.GetData(), data.GetSize());
			file.Close();
		}

		wprintf( L" - succeed!\n" );
	}
}

int wmain(int argc, wchar_t* argv[])
{
	bool	br = false;
	bool	bunicode = false;
	size_t	size = 0;
	wchar_t* filename = NULL;

	for( int i = 1; i < argc; i ++)
	{
		wchar_t* wd = argv[i];
		wchar_t wc = wd[0];

		if( L'-' == wc || L'/' == wc)
		{
			wd ++;
			if( 0 == _wcsicmp( wd, L"r"))
				br = true;
			else if( 0 == _wcsicmp( wd, L"s"))
			{
				i ++;
				if( i >= argc)
					return usage( argv[0]);
				size = _wtol( argv[i]);
			}
			else if( 0 == _wcsicmp( wd, L"u"))
			{
				bunicode = true;
			}
			else
				return usage( argv[0]);
		}
		else
		{
			if( NULL == filename)
				filename = wd;
			else
				return usage( argv[0]);
		}
	}
	std::wstring		path, file;

	if( NULL == filename)
	{
		path = L".";
		file = L"*.*";
	}
	else
	{
		wchar_t* pathend = wcsrchr( filename, L'\\');
		if( NULL == pathend)
			pathend = wcsrchr( filename, L'/');
		if( pathend)
		{
			*pathend = 0;
			path = filename;
			file = (pathend + 1);
		}
		else
		{
			path = L".";
			file = filename;
		}
	}

	setlocale(LC_ALL, "chs");
	TryFindFile( path, file, br, size, bunicode);
	return 0;
}

