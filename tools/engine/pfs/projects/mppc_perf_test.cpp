///=============================================================================
/// Product   : PFS
/// Project   : pfs
/// Module    : mppc_perf_test.cpp
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
#include <assert.h>
#include <conio.h>
#include <iostream>
#include <sstream>
#include <strsafe.h>
#include "../../iolib/io/mppc.h"
#include <MMSystem.h>
#pragma comment( lib, "winmm.lib")

using namespace GNET;
using namespace PFS;
namespace MPPC_TEST
{
	void Test();
}

namespace MPPC_TEST
{
	void Encode( CBaseFile& source, CBaseFile& destination )
	{
		size_t remaining = source.GetSize();
		const int BLOCK = 4096;
		const int BLOCK_ZIPPED = 8192;
		int destlen = 8192;
		unsigned char buffer[BLOCK];
		unsigned char buffer_zipped[BLOCK_ZIPPED];
	
		size_t current = 0;
		while ( remaining > 0 )
		{
			current = source.Read(buffer, BLOCK  );
			assert( current != 0 );

			remaining -= current;

			destlen = 8192;
			int ret = mppc::compress2( buffer_zipped,  &destlen, buffer, current );
			if ( ret < 0 || destlen <= 0 )
			{
				break;
			}

			destination.Write( buffer_zipped, destlen );
		}
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// 该函数尚未通过测试。
	// 
	void Decode( CBaseFile& source, CBaseFile& destination )
	{
		size_t remaining = source.GetSize();
		const int BLOCK = 4096;
		const int BLOCK_ZIPPED = BLOCK*4;
		unsigned char* buffer = NULL;
		unsigned char* buffer_zipped = NULL;

		buffer = (unsigned char*)malloc( BLOCK );
		int destlen = BLOCK_ZIPPED;
		
		buffer_zipped = (unsigned char*)malloc( destlen );

		size_t current = 0;
		while ( remaining > 0 )
		{
			current = source.Read(buffer, BLOCK  );
			assert( current != 0 );

			remaining -= current;

			destlen = 8192;
			int ret = mppc::uncompress2( buffer_zipped,  &destlen, buffer, current );
			if ( ret < 0 || destlen <= 0 )
			{
				break;
			}

			destination.Write( buffer_zipped, destlen );
		}

		free( buffer );
		free( buffer_zipped );
	}

	void EncodeTest()
	{
		TCHAR buffer[256] = {0};
		DWORD timeElapse = timeGetTime();
		CBaseFile* pFileIn = CEnv::OpenFile( L"/run/gameclient.ncb", FM_EXCL, FA_RDONLY );
		CBaseFile* pFileOut = CEnv::OpenFile( L"/run/gameclient_1.mppc", FM_CREAT|FM_TRUNC, FA_WRONLY );
		Encode(*pFileIn, *pFileOut);
		timeElapse = timeGetTime() - timeElapse;
		StringCchPrintf( buffer, _countof(buffer), L"encode gameclient.ncb->gameclient_1.mppc -- %lu ms\n", timeElapse );

		CEnv::CloseFile( pFileIn );
		CEnv::CloseFile( pFileOut );
		std::wcout<< buffer;
	}

	void DecodeTest( std::wstring filename)
	{
		TCHAR buffer[256] = {0};
		DWORD timeElapse = timeGetTime();
		CBaseFile* pFileIn = CEnv::OpenFile( L"/run/gameclient_1.mppc", FM_EXCL, FA_RDONLY );
		CBaseFile* pFileOut = CEnv::OpenFile( L"/run/" + filename, FM_CREAT|FM_TRUNC, FA_WRONLY );

		//CDataFile dataFile;
		Decode(*pFileIn, *pFileOut);
		timeElapse = timeGetTime() - timeElapse;
		size_t inSize = pFileIn->GetSize();
		size_t outSize = pFileOut->GetSize();
		StringCchPrintf( buffer, _countof(buffer), L"Decode gameclient_1.mppc -- %lu ms\n" , timeElapse );
		std::wcout<< buffer;
		StringCchPrintf( buffer, _countof(buffer), L"IN: %lu bytes/s, OUT: %lu bytes/s\n", inSize/timeElapse,outSize/timeElapse );
		std::wcout<< buffer;

		CEnv::CloseFile( pFileIn );
		CEnv::CloseFile( pFileOut );
	}


	void Test()
	{
		CEnv::Mount( L"/run", L"D:\\zt", PFS::FST_NATIVE, PFS::MT_WRITABLE );

		//EncodeTest();
		DecodeTest( L"1.mppc.ncb" );
		DecodeTest( L"2.mppc.ncb" );
		DecodeTest( L"3.mppc.ncb" );
		DecodeTest( L"4.mppc.ncb" );
		DecodeTest( L"5.mppc.ncb" );

		CEnv::Unmount(L"/run");
	}
}