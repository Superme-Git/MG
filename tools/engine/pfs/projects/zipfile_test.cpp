///=============================================================================
/// Product   : PFS
/// Project   : pfs
/// Module    : zipfile_test.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-2-16 Created.
///=============================================================================
#include "../pfs2.h"
#include <conio.h>
#include <iostream>
#include <sstream>
//#include "..\src\zipcommon.h"
#include <strsafe.h>
#include "../contrib/zlib-1.2.3/zlib.h"
#include <MMSystem.h>
#pragma comment( lib, "winmm.lib")

using namespace PFS;
using namespace std;
namespace ZIPFILETEST
{
	void Test();
}

namespace ZIPFILETEST
{

	static int _PerformEncode( CBaseFile& source, CBaseFile& destination )
	{
		size_t total = source.GetSize();
		size_t remaining = total;
		const size_t BLOCK = 4096;
		const size_t BLOCK_ZIPPED = 8192;
		char buffer[BLOCK];
		char buffer_zipped[BLOCK_ZIPPED];

		z_stream m_strm;
		m_strm.zalloc = Z_NULL;    
		m_strm.zfree = Z_NULL;    
		m_strm.opaque = Z_NULL;
		m_strm.avail_in = 0;    
		m_strm.next_in = Z_NULL;

		int ret = deflateInit2( &m_strm, Z_BEST_COMPRESSION, Z_DEFLATED, -MAX_WBITS, 8, Z_DEFAULT_STRATEGY);
		if( ret != Z_OK )
		{
			return ret;
		}

		size_t current = 0;
		while ( remaining > 0 )
		{
			current = source.Read(buffer, sizeof(buffer) );
			assert( current != 0 );

			remaining -= current;

			m_strm.avail_in = (uInt)current;
			m_strm.next_in = (Bytef*)buffer;

			//bool bFinish = remaining == 0;

			// normally this loop will do only once
			// Note: we should check the avail_in ==0 but not the avail_out !=0 as the quit condition.
			// because in some cases, the avail_out == 0 and also the avail_in == 0,
			// in this case, inflate will failed with -5.
			while( m_strm.avail_in )
			{
				m_strm.avail_out = sizeof(buffer_zipped);
				m_strm.next_out = (Bytef*)buffer_zipped;

				//if (bFinish)
				//{
				//	ret = deflate(&m_strm, Z_FINISH);
				//	deflateReset(&m_strm);
				//}
				//else
				{
					ret = deflate(&m_strm, Z_SYNC_FLUSH);
				}
				if( ret < Z_OK)
					break;

				size_t have = sizeof(buffer_zipped) - m_strm.avail_out;
				destination.Write( buffer_zipped, have );

				// Note: in some cases, when ret == Z_STREAM_END, 
				// but avail_in is still not 0, and if we don't break here,
				// it will cause a dead-loop.
				if ( ret == Z_STREAM_END )
					break;
			}
		}

		m_strm.avail_in = 0;
		m_strm.next_in = NULL;
		m_strm.avail_out = sizeof(buffer_zipped);
		m_strm.next_out = (Bytef*)buffer_zipped;
		ret = deflate( &m_strm, Z_FINISH);
		if( ret < Z_OK)
			return ret;
		size_t have = sizeof(buffer_zipped) - m_strm.avail_out;
		destination.Write( buffer_zipped, have );
		deflateEnd( &m_strm);

		return ret;
	}


	static int _PerformDecode( CBaseFile& source, CBaseFile& destination, bool bOnceDecode )
	{
		size_t total = source.GetSize();
		size_t remaining = total;
		size_t BLOCK = 1024*100;
		size_t BLOCK_UNZIPPED = BLOCK*8;

		if( bOnceDecode )
		{
			BLOCK = source.GetSize();
			BLOCK_UNZIPPED = BLOCK*4;
		}

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

		size_t current = 0;
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

				size_t have = BLOCK_UNZIPPED - m_strm.avail_out;
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

	void ZipPerformanceTest()
	{
		TCHAR buffer[256] = {0};
		DWORD timeElapse = timeGetTime();
		CBaseFile* pFileIn = CEnv::OpenFile( L"/run/gameclient.ncb", FM_EXCL, FA_RDONLY );
		CBaseFile* pFileOut = CEnv::OpenFile( L"/run/gameclient_1.zip", FM_CREAT|FM_TRUNC, FA_WRONLY );
		_PerformEncode(*pFileIn, *pFileOut);
		timeElapse = timeGetTime() - timeElapse;
		StringCchPrintf( buffer, _countof(buffer), L"encode gameclient.ncb->gameclient_1.zip -- %lu ms\n", timeElapse );

		CEnv::CloseFile( pFileIn );
		CEnv::CloseFile( pFileOut );
		std::wcout<< buffer;
	}

	void UnzipPerformanceTest(std::wstring destfilename, bool bOnceDecode )
	{
		TCHAR buffer[256] = {0};
		
		
		CBaseFile* pFileIn = CEnv::OpenFile( L"/run/gameclient_1.zip",  FM_EXCL, FA_RDONLY);
		//CBaseFile* pFileOut = CEnv::OpenFile( L"/run/" + destfilename, FM_CREAT|FM_TRUNC, FA_WRONLY   );

		CDataFile dataFile;
		
		DWORD timeElapse = timeGetTime();
		_PerformDecode(*pFileIn, dataFile, bOnceDecode);
		size_t inSize = pFileIn->GetSize();
		size_t outSize = dataFile.GetSize();
		timeElapse = timeGetTime() - timeElapse;
		StringCchPrintf( buffer, _countof(buffer), L"Decode gameclient_1.zip->%s -- %lu ms\n", destfilename.c_str(), timeElapse );
		std::wcout<< buffer;
		StringCchPrintf( buffer, _countof(buffer), L"IN: %lu bytes/s, OUT: %lu bytes/s\n", inSize/timeElapse,outSize/timeElapse );
		std::wcout<< buffer;

		CEnv::CloseFile( pFileIn );
		//CEnv::CloseFile( pFileOut );
	}

	void Test()
	{
		CEnv::Mount( L"/run", L"D:\\zt", PFS::FST_NATIVE, PFS::MT_WRITABLE );

		//ZipPerformanceTest();

		UnzipPerformanceTest( L"1.ncb", false );

		UnzipPerformanceTest( L"2.ncb", false );
		
		UnzipPerformanceTest( L"3.ncb", true );

		UnzipPerformanceTest( L"4.ncb", true );

		CEnv::Unmount(L"/run");
	}
}
