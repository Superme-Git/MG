///=============================================================================
/// Product   : PFS
/// Project   : pfs
/// Module    : NativeFS_test.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-2-13 Created.
///=============================================================================
#include "../pfs2.h"
#include <conio.h>
#include <iostream>
#include <sstream>

using namespace PFS;
using namespace std;
namespace NATIVETEST
{
	void Test();
}

extern void DumpErrorCode();
namespace NATIVETEST
{
	void TestMountNative( const std::wstring& device, FS_TYPE fst, MOUNT_TYPE mt )
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

	void TestNativeDirectoryMount()
	{
		TestMountNative( L"d:\\temp", FST_NATIVE, MT_READONLY  );
		TestMountNative( L"d:\\temp.invalid", FST_NATIVE, MT_READONLY );

		TestMountNative( L"d:\\temp", FST_NATIVE, MT_RUNTIME );
		TestMountNative( L"d:\\temp.invalid", FST_NATIVE, MT_RUNTIME );

		TestMountNative( L"d:\\temp", FST_NATIVE, MT_WRITABLE);
		TestMountNative( L"d:\\temp.invalid", FST_NATIVE, MT_WRITABLE);
	}


	void TestMountNativeMountCase( const std::wstring& base, const std::wstring& device, FS_TYPE fst, MOUNT_TYPE mt )
	{
		if( CEnv::Mount( base, device, fst, mt ) )
		{
			wcout<< L" mount (" << base << L"," << device << L", fst:" << fst << L" mount type:" << mt << L" OK!\n";
		}
		else
		{
			wcout<< L" mount (" << base << L"," << device << L", fst:" << fst << L" mount type:" << mt << L" - ";
			DumpErrorCode();
		}
	}

	void TestNativeMountDirectoryMount()
	{
		if( !CEnv::Mount( L"/run",  L"d:\\temp", FST_NATIVE, MT_WRITABLE) )
		{
			wcout<< L" mount ";
			DumpErrorCode();
			return;
		}

		//bool bOK = CEnv::IsFileExisting( L"/run/1.jpg" );
		//wcout<<  L"/run/1.jpg is " << (bOK ? L" existing" : L" not existing")  << endl;

		//TestMountNativeMountCase( L"/r", L"/run/r", FST_NATIVE, MT_READONLY  );
		//TestMountNativeMountCase( L"/r", L"/run/r.invalid", FST_NATIVE, MT_READONLY );

		//TestMountNativeMountCase( L"/r", L"/run/r",  FST_NATIVE, MT_RUNTIME );
		//TestMountNativeMountCase( L"/r", L"/run/r.invalid", FST_NATIVE, MT_RUNTIME );

		//TestMountNativeMountCase( L"/r", L"/run/r", FST_NATIVE, MT_WRITABLE);
		//TestMountNativeMountCase( L"/r2", L"/run/r.invalid/r.invalid2",  FST_NATIVE, MT_WRITABLE);

		//TestMountNativeMountCase( L"/r2", L"/run/r.invalid",  FST_NATIVE, MT_READONLY);

		TestMountNativeMountCase( L"/r2", L"/run/r.invalid",  FST_NATIVE, MT_WRITABLE);
		TestMountNativeMountCase( L"/r3", L"/r2/a4.zip",  FST_ZIP, MT_WRITABLE);

		CEnv::Unmount( L"/r3" );
		CEnv::Unmount( L"/r2" );
		CEnv::Unmount( L"/run" );
	}

	void Test()
	{
		TestNativeMountDirectoryMount();
	}
}