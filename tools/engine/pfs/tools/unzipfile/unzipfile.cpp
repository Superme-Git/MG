//-------------------------------------------------------------
//
// unzipfile.cpp : Defines the entry point for the console application.
//
// unzipfile - 
//	Usage:
//		unzipfile <source zip file path>
// or
//		unzipfile <source zip file path> <unzipped file path>
//
//	Unzip a zipped single file.
//
//-------------------------------------------------------------
// Revision history:
//
// 0.9.0 - initial version - yanghaibo - 2010-11-24 
//
//-------------------------------------------------------------
#include "stdafx.h"

#include "../../contrib/zlib-1.2.3/zlib.h"
#include "../../pfs/pfs2.h"
#include <assert.h>
#include <iostream>

#define WIDEN2(x) L ## x
#define WIDEN(x) WIDEN2(x)
#define __TIMESTAMPW__ WIDEN(__DATE__) L" " WIDEN(__TIME__)
#define VERSION_STRING L"Version 0.9.0 - Compiled at " __TIMESTAMPW__

void usage( _TCHAR* arg0 )
{
	std::wcerr<<  L"Usage: " << std::endl;
	std::wstring exepath = arg0;
	std::wstring::size_type dot = exepath.rfind(L'\\');
	if( dot == std::wstring::npos )
	{
		dot = exepath.rfind(L'/');
	}
	if( dot != std::wstring::npos )
	{
		exepath.assign( &arg0[dot + 1] );
	}

	std::wcerr <<  L"   " << exepath << L" <source zip file path>" << std::endl;
	std::wcerr <<  L"or " << std::endl;
	std::wcerr <<  L"   " << exepath << L" <source zip file path> <unzipped file path>" << std::endl;
	std::wcerr <<  L"Unzip a zipped single file." << std::endl;
	std::wcerr << std::endl;
	std::wcerr <<  L"   " << VERSION_STRING << std::endl;
}

using PFS::CFile;

int UnzipFile(CFile& source, CFile& destination )
{
	ULONGLONG total = source.GetSize();
	ULONGLONG remaining = total;
	UINT BLOCK = 1024*100;
	UINT BLOCK_UNZIPPED = BLOCK*8;

	char* buffer = (char*)malloc( BLOCK );
	char* buffer_unzipped = (char*)malloc( BLOCK_UNZIPPED);

	z_stream m_strm;
	m_strm.zalloc = Z_NULL;
	m_strm.zfree = Z_NULL; 
	m_strm.opaque = Z_NULL;
	m_strm.avail_in = 0;
	m_strm.next_in = Z_NULL;

	int ret = inflateInit2( &m_strm, -MAX_WBITS);
	if( ret != Z_OK )
	{
		return ret;
	}

	ULONGLONG current = 0;
	while ( remaining > 0 )
	{
		current = source.Read(buffer, BLOCK );
		if ( current == 0 )
		{
			assert( current != 0 );
			break;
		}
		remaining -= current;
		bool bFinish = remaining==0;

		m_strm.avail_in = (uInt)current;
		m_strm.next_in = (Bytef*)buffer;

		// normally this loop will do only once
		// Note: we should check the avail_in ==0 but not the avail_out !=0 as the quit condition.
		// because in some cases, the avail_out == 0 and also the avail_in == 0,
		// in this case, inflate will failed with -5.
		while( m_strm.avail_in ) 
		{
			m_strm.avail_out = BLOCK_UNZIPPED;
			m_strm.next_out = (Bytef*)buffer_unzipped;

			ret = inflate( &m_strm, Z_SYNC_FLUSH);
			if( ret < Z_OK)
				break;

			UINT have = BLOCK_UNZIPPED - m_strm.avail_out;
			destination.Write( buffer_unzipped, have );

			// Note: in some cases, when ret == Z_STREAM_END, 
			// but avail_in is still not 0, and if we don't break here,
			// it will cause a dead-loop.
			if ( ret == Z_STREAM_END )
				break;
		} 
	}

	free( buffer );
	free( buffer_unzipped );

	inflateEnd( &m_strm);

	return ret;
}

bool is_arg_like_ask_usage( const _TCHAR* arg )
{
	std::wstring strArg = arg;
	if( strArg.compare( L"-h" ) == 0 ||
		strArg.compare( L"/h" ) == 0 ||
		strArg.compare( L"-?" ) == 0 ||
		strArg.compare( L"/?" ) == 0 ||
		strArg.compare( L"?" ) == 0 ||
		strArg.compare( L"--help" ) == 0 ||
		strArg.compare( L"-help" ) == 0 ||
		strArg.compare( L"/help" ) == 0 )
	{
		return true;
	}
	else
	{
		return false;
	}
}

int _tmain(int argc, _TCHAR* argv[])
{
	if( argc < 2 )
	{	
		usage( argv[0] );
		return -1;
	}

	std::wstring unzippedpath;
	if( argc > 2 )
	{
		unzippedpath = argv[2];
	}
	else
	{
		if( is_arg_like_ask_usage(argv[1]) )
		{
			usage( argv[0] );
			return 0;
		}

		unzippedpath = argv[1];

		std::wstring::size_type dot = unzippedpath.rfind(L'.');
		if( dot != std::wstring::npos )
		{
			unzippedpath.insert( dot, L".unzipped" );
		}
		else
		{
			unzippedpath += L".unzipped";
		}
	}

	CFile file1;
	CFile file2;

	if( !file1.Open( argv[1], PFS::FM_EXCL, PFS::FA_RDONLY ) )
	{
		std::wcerr << argv[1] << L" --> "  << unzippedpath << L" FAILED with ErrorCode: " << PFS::CEnv::GetLastError() << std::endl;
		return PFS::CEnv::GetLastError();
	}

	if( !file2.Open( unzippedpath, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY) )
	{
		std::wcerr << argv[1] << L" --> "  << unzippedpath << L" FAILED with ErrorCode: " << PFS::CEnv::GetLastError() << std::endl;
		return PFS::CEnv::GetLastError();
	}

	int iRet = UnzipFile( file1, file2 );
	if( iRet >= Z_OK && iRet <= Z_STREAM_END )
	{
		std::wcout << argv[1] << L" --> "  << unzippedpath << L" OK!" << std::endl;
		iRet = 0;
	}
	else
	{
		std::wcerr << argv[1] << L" --> "  << unzippedpath << L" FAILED with ErrorCode: " << iRet << std::endl;
	}

	return iRet;
}
