#include "../pfs2.h"
#include "..\src\common.h"
#include "..\src\brief\brief.h"
//#include "..\InetFile.h"

#include <conio.h>
#include <iostream>
#include <sstream>
using namespace PFS;
using namespace PFSX;

namespace BRIEFTEST
{
	void Test();
}

namespace BRIEFTEST
{

	void MountBriefFSTest()
	{
		if( !CEnv::Mount( L"/b", L"http://172.16.32.30/mhzx", PFS::FST_BRIEF, MT_READONLY | MT_RUNTIME ) )
		{
			return;
		}

		CFile file;
		if( ! file.Open( L"/b/bin/pp_data0", FM_EXCL, FA_RDONLY ) )
		{
			CEnv::Unmount( L"/b" );
			return;
		}

		fsize_t nSize = 0;
		nSize = file.GetSize();

		void * buffer = new char[(70<<20)];
		size_t nToReadOneTime = (64<<20)+(32<<10)+(8<<10);
		size_t nTotalRead = 0;
		size_t nRead = 0;
		do 
		{
			nRead = file.Read( buffer, nToReadOneTime);
			nTotalRead += nRead;
		} while ( nRead > 0 );		

		delete[] buffer;

		assert( nTotalRead == nSize );

		CEnv::Unmount( L"/b" );

	}

	// for verifing bug29
	void NativeRemoveFileTest()
	{
		CEnv::Mount( L"/run", L"d:/temp", FST_NATIVE, MT_WRITABLE );
		{
			CFile file;
			file.Open( L"/run/NativeTestFile.txt", FM_CREAT, FA_RDWR );
		}

		CEnv::RemoveFile( L"/run/NativeTestFile.txt" );

		CMetaInfo minfile;
		if( CEnv::GetFileMetaInfo( L"/run/nativetestfile.txt", minfile ) )
		{
			assert( 0 );
		}

		CEnv::Unmount( L"/run" );
	}

	void CInetFileDownload()
	{
		CInetFile file;
		if( !file.Open( L"http://172.16.32.30/mhzx/data.pfs" ) )
		{
			return;
		}

		fsize_t nSize0 = file.GetSize();

		size_t nSize = 0;

		void * buffer = new char[(70<<20)];
		size_t nToReadOneTime = (64<<20)+(32<<10)+(8<<10);
		fsize_t nTotalRead = 0;
		size_t nRead = 0;
		do 
		{
			nRead = file.Read( buffer, nToReadOneTime);
			nTotalRead += nRead;
		} while ( nRead > 0 );		

		delete[] buffer;

		assert( nTotalRead == nSize );

	}

	void CInetFileTest()
	{	
		CInetFile file;
		if( !file.Open( L"http://172.16.32.30/mhzx/bin/gameclient.exe" ) )
		{
			return;
		}

		fsize_t nRet2 = file.GetSize();
		size_t nRet = 0;

		nRet = file.Write( "abc", 3 );
		assert( nRet != PFS::FILE_SIZE_ERROR );
		nRet2 = file.Seek( 0, PFS::FSM_SET );
		assert( nRet == 0 );

		CImageData data = file.GetImage();
		nRet2 = file.GetPos();
		assert( nRet2 == file.GetSize() );

		nRet2 = file.Seek( 0, PFS::FSM_SET );
		assert( nRet == 0 );
		nRet2 = file.GetPos();
		assert( nRet2 == 0 );
		
		data = file.GetImage();
		nRet2 = file.GetPos();
		assert( nRet2 == file.GetSize() );
	}

	void Test()
	{
		MountBriefFSTest();
		//CInetFileTest();
		//CInetFileDownload();
		//NativeRemoveFileTest();
	}
}