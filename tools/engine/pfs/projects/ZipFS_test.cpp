///=============================================================================
/// Product   : PFS
/// Project   : pfs
/// Module    : ZipFS_test.cpp
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

using namespace PFS;
using namespace std;
namespace ZIPTEST
{
	void Test();
}

extern void DumpErrorCode();


namespace ZIPTEST
{

	void TestMountZip( const std::wstring& device, FS_TYPE fst, MOUNT_TYPE mt )
	{
		if( CEnv::Mount( L"/run", device,fst, mt ) )
		{
			wcout<< L" mount " << device << L" to /run with fst:" << fst << L" mount type:" << mt << L" OK!\n";
			CEnv::Unmount( L"/run" );
		}
		else
		{
			wcout<< L" mount " << device << L" to /run with fst:" << fst << L" mount type:" << mt << L" - ";
			DumpErrorCode();
		}
	}

	void TestZipMount()
	{
		TestMountZip( L"d:\\temp\\zippfs.zip", FST_ZIP, MT_READONLY  );
		TestMountZip( L"d:\\temp\\zippfs.invalid.zip", FST_ZIP, MT_READONLY );

		TestMountZip( L"d:\\temp\\zippfs.zip", FST_ZIP, MT_RUNTIME );
		TestMountZip( L"d:\\temp\\zippfs.invalid.zip", FST_ZIP, MT_RUNTIME );

		TestMountZip( L"d:\\temp\\zippfs.zip", FST_ZIP, MT_WRITABLE);
		TestMountZip( L"d:\\temp\\zippfs.invalid.zip", FST_ZIP, MT_WRITABLE);
	}

	bool TestMountOnMountCase( const std::wstring& base, const std::wstring& device, FS_TYPE fst, MOUNT_TYPE mt )
	{
		if( CEnv::Mount( base, device, fst, mt ) )
		{
			wcout<< L" mount (" << base << L"," << device << L", fst:" << fst << L" mount type:" << mt << L" OK!\n";
			return true;
		}
		else
		{
			wcout<< L" mount (" << base << L"," << device << L", fst:" << fst << L" mount type:" << mt << L" - ";
			DumpErrorCode();

			return false;
		}
	}

	void TestNativeOnZipMount()
	{
		if( !CEnv::Mount( L"/run",  L"d:\\temp\\zippfs.zip", FST_ZIP, MT_WRITABLE) )
		{
			wcout<< L" mount ";
			DumpErrorCode();
			return;
		}

		//bool bOK = CEnv::IsFileExisting( L"/run/1.jpg" );
		//wcout<<  L"/run/1.jpg is " << (bOK ? L" existing" : L" not existing")  << endl;

		//TestMountOnMountCase( L"/r", L"/run/r", FST_NATIVE, MT_READONLY  );
		//TestMountOnMountCase( L"/r", L"/run/r.invalid", FST_NATIVE, MT_READONLY );

		//TestMountOnMountCase( L"/r", L"/run/r",  FST_NATIVE, MT_RUNTIME );
		//TestMountOnMountCase( L"/r", L"/run/r.invalid", FST_NATIVE, MT_RUNTIME );

		if( TestMountOnMountCase( L"/r", L"/run/r", FST_NATIVE, MT_WRITABLE) )
		{
			{
				CFile fileNew;
				if( fileNew.Open( L"/r/newfile.txt", FM_CREAT|FM_TRUNC, FA_RDWR ) )
				{
					fileNew.Write( "/r/newfile.txt", strlen("/r/newfile.txt") );
				}
				else
				{
					DumpErrorCode();
				}
			}

			if( TestMountOnMountCase( L"/r2", L"/r/r.invalid/r.invalid2",  FST_NATIVE, MT_WRITABLE) )
			{
				{
					CFile fileNew;
					fileNew.Open( L"/r2/newfile.txt", FM_CREAT|FM_TRUNC, FA_RDWR );
					fileNew.Write( "/r2/newfile.txt", strlen("/r2/newfile.txt") );
				}

				CEnv::Unmount( L"/r2" );
			}

			CEnv::Unmount( L"/r" );
		}


		//TestMountOnMountCase( L"/r2", L"/run/r.invalid",  FST_NATIVE, MT_READONLY);

		//TestMountZipNativeMountCase( L"/r2", L"/run/r.invalid",  FST_NATIVE, MT_WRITABLE);

		//TestMountZipNativeMountCase( L"/r3", L"/r2/a4.zip",  FST_ZIP, MT_WRITABLE);


		CEnv::Unmount( L"/run" );
	}

	void TestMountAndCheck()
	{
		if( !CEnv::Mount( L"/r0",  L"d:\\temp", FST_NATIVE, MT_WRITABLE) )
		{
			wcout<< L" mount ";
			DumpErrorCode();
			return;
		}

		if( !CEnv::Mount( L"/r1",  L"/r0/t2", FST_NATIVE, MT_WRITABLE) )
		{
			wcout<< L" mount ";
			DumpErrorCode();
			return;
		}

		//if( !CEnv::Mount( L"/r",  L"/r1/resource.zip", FST_ZIP, MT_RUNTIME|MT_CACHEDATA) )
		//{
		//	wcout<< L" mount ";
		//	DumpErrorCode();
		//	return;
		//}

		//if( !CEnv::Mount( L"/r3",  L"D:\\temp\\T2\\launcher.zip", FST_ZIP, MT_WRITABLE) )
		//{
		//	wcout<< L" mount ";
		//	DumpErrorCode();
		//	return;
		//}

		//{
		//	CFile fileNew;
		//	if( !fileNew.Open( L"/r/copyright.txt", FM_EXCL, FA_RDONLY) )
		//	{
		//		DumpErrorCode();
		//	}

		//	CImageData data = fileNew.GetImage();

		//	CFile filenative;
		//	filenative.Open(L"/r1/copyright.txt", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY );
		//	filenative.Write(data.GetData(), (size_t)data.GetSize());


		//	CDataBlock dataBlock;
		//	dataBlock.Resize((size_t)fileNew.GetSize());

		//	fileNew.ReadAll( dataBlock.GetData(), dataBlock.GetSize() );
		//	CFile filenativedatablock;
		//	filenativedatablock.Open(L"/r1/copyright-datablock.txt", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY );
		//	filenativedatablock.Write(dataBlock.GetData(), dataBlock.GetSize());
		//}

		{
			//if( !CEnv::Mount( L"/r",  L"/r1/webpage.pfs", FST_ZIP, MT_RUNTIME) )
			//if( !CEnv::Mount( L"/r",  L"/r1/webpage.pfs", FST_RUNZIP, MT_RUNTIME) )
			if( !CEnv::Mount( L"/r",  L"/r1/webpage.pfs", FST_ZIP, MT_WRITABLE ) )
			{
				wcout<< L" mount ";
				DumpErrorCode();
				return;
			}

			{
				CFile fileNew;
				if( !fileNew.Open( L"/r/index.htm", FM_EXCL, FA_RDONLY) )
				{
					DumpErrorCode();
				}

				CImageData data = fileNew.GetImage();

				CFile filenative;
				filenative.Open(L"/r1/index-fromimage.htm", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY );
				filenative.Write(data.GetData(), (size_t)data.GetSize());


				CDataBlock dataBlock;
				dataBlock.Resize((size_t)fileNew.GetSize());

				fileNew.ReadAll( dataBlock.GetData(), dataBlock.GetSize() );
				CFile filenativedatablock;
				filenativedatablock.Open(L"/r1/index-datablock.htm", PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY );
				filenativedatablock.Write(dataBlock.GetData(), dataBlock.GetSize());
			}
		}

		CEnv::Unmount( L"/r" );
		CEnv::Unmount( L"/r3" );
		CEnv::Unmount( L"/r1" );
		CEnv::Unmount( L"/r0" );
	}



	void TestMountZipUnderZip()
	{
		if( !CEnv::Mount( L"/r0",  L"d:\\temp", FST_NATIVE, MT_WRITABLE) )
		{
			wcout<< L" mount ";
			DumpErrorCode();
			return;
		}

		if( !CEnv::Mount( L"/r1",  L"/r0/t2", FST_NATIVE, MT_WRITABLE) )
		{
			wcout<< L" mount ";
			DumpErrorCode();
			return;
		}

		if( !CEnv::Mount( L"/r2",  L"/r1/aaatest.zip", FST_ZIP, MT_WRITABLE) )
		{
			wcout<< L" mount ";
			DumpErrorCode();
			return;
		}

		if( !CEnv::Mount( L"/r3",  L"/r2/btest.zip", FST_ZIP, MT_WRITABLE) )
		{
			wcout<< L" mount ";
			DumpErrorCode();
			return;
		}

		{
			CFile fileNew;
			if( !fileNew.Open( L"/r3/testfile.txt", FM_CREAT, FA_WRONLY) )
			{
				DumpErrorCode();
			}
			else
			{
				fileNew.Write( "abcdefg", 7 );
			}
		}

		CEnv::Unmount( L"/r3" );
		CEnv::Unmount( L"/r2" );
		CEnv::Unmount( L"/r1" );
		CEnv::Unmount( L"/r0" );
	}

	void TestRunZipCacheDataForMultiProcess()
	{
		


		if( !CEnv::Mount( L"/runzip",  L"d:\\temp\\t2\\aaatest.zip", FST_RUNZIP, MT_RUNTIME|MT_CACHEDATA) )
		{
			wcout<< L" mount /runzip failed";
			DumpErrorCode();
			return;
		}

		{
			CFile fileNew;
			if( !fileNew.Open( L"/runzip/testfile.txt", FM_EXCL, FA_RDONLY) )
			{
				DumpErrorCode();
			}
			else
			{
				wcout<< L" open file ok!\n";
			}
		}
		{
			CFile fileNew;
			if( !fileNew.Open( L"/runzip/testfile.txt", FM_EXCL, FA_RDONLY) )
			{
				DumpErrorCode();
			}
			else
			{
				wcout<< L" open file ok!\n";
			}
		}

		CEnv::Unmount( L"/runzip" );
	}

	void TestPfs()
	{
		if(! PFS::CEnv::Mount( L"/image", L"e:/image.pfs",FST_RUNZIP, MT_RUNTIME))
		{
			wcout<< L" mount /runzip failed";
			DumpErrorCode();
			return;
		}
		CFile fileNew;
			if( !fileNew.Open( L"/image/loading/waiting.jpg",FM_EXCL, FA_RDONLY))
			{
				DumpErrorCode();
			}
			else
			{
				wcout<< L" open file ok!\n";
			}

			CImageData* pImageData = &fileNew.GetImage();
			
			const void* pData = pImageData->GetData();
			const unsigned short* pata = (const unsigned short*)pData;

			int nPic = * pata;

	}

	void Test()
	{
		//TestZipMount();
		//TestNativeOnZipMount();
		//TestMountAndCheck();
		//TestMountZipUnderZip();
		//stRunZipCacheDataForMultiProcess();

		TestPfs();

	}

} // ZIPTEST