#include "../pfs2.h"
#include "../device.h"
#include "../src/native/rawfileio.h"

#include <conio.h>
#include <iostream>
#include <sstream>
#include <ctime>
#include <iomanip>


#define WIDEN_(s) L##s
#define WIDEN(s) WIDEN_(s)
#define __TIMEW__ WIDEN(__DATE__)


using namespace PFS;
using namespace std;

namespace DEVICESYSTEMTEST
{
	void Test();
}

extern void DumpErrorCode();
namespace DEVICESYSTEMTEST
{
	bool DeviceTest1()
	{
		CDevice device1;
		if( !device1.Mount( L"D:\\", MT_READONLY ) )
		{
			wcerr<<L"device1.Mount( \"D:\\\", MT_READONLY ) " << PFS::CEnv::GetLastError() << endl;
		}
		else
		{
			wcout<<L"device1.Mount( \"D:\\\", MT_READONLY ) OK. " << endl;
		}

		CDevice device2;
		if( !device2.Mount( L"D:\\", MT_WRITABLE ) )
		{
			wcerr<<L"device2.Mount( \"D:\\\", MT_WRITABLE ) " << PFS::CEnv::GetLastError() << endl;
		}
		else
		{
			wcout<<L"device2.Mount( \"D:\\\", MT_WRITABLE ) OK. " << endl;
		}

		CDevice device3;
		if( !device3.Mount( L"D:\\", MT_RUNTIME ) )
		{
			wcerr<<L"device3.Mount( \"D:\\\", MT_RUNTIME ) " << PFS::CEnv::GetLastError() << endl;
		}
		else
		{
			wcout<<L"device3.Mount( \"D:\\\", MT_RUNTIME ) OK. " << endl;
		}


		return true;
	}

	
	bool DeviceTest2_(std::wstring strDeviceName)
	{

		CDevice device1;
		
		if( !device1.Mount( strDeviceName, MT_READONLY ) )
		{
			wcerr<<L"device1.Mount( \""<< strDeviceName << "\", MT_READONLY ) " << PFS::CEnv::GetLastError() << endl;
		}
		else
		{
			wcout<<L"device1.Mount( \""<< strDeviceName << "\", MT_READONLY ) OK. " << endl;
		}
		
		CDevice device2;
		if( !device2.Mount( strDeviceName, MT_RUNTIME) )
		{
			wcerr<<L"device2.Mount( \""<< strDeviceName << "\", MT_RUNTIME ) " << PFS::CEnv::GetLastError() << endl;
		}
		else
		{
			wcout<<L"device2.Mount( \""<< strDeviceName << "\", MT_RUNTIME ) OK. " << endl;
		}


		// 新建PFS Device的用例
		CDevice device3;		
		if( !device3.Mount( strDeviceName, MT_WRITABLE ) )
		{
			wcerr<<L"device3.Mount( \""<< strDeviceName << "\", MT_WRITABLE ) " << PFS::CEnv::GetLastError() << endl;
		}
		else
		{
			wcout<<L"device3.Mount( \""<< strDeviceName << "\", MT_WRITABLE ) OK. " << endl;
		}

		bool isFileExisting = CFile::IsFileExisting( strDeviceName );
		bool isDirectoryExisting = CFileIO::IsDirectoryExisting( strDeviceName );
		// 
		wcout << L"CFile::IsFileExisting( \""<< strDeviceName << "\" ) " << isFileExisting << endl;
		wcout << L"CFileIO::IsDirectoryExisting( \""<< strDeviceName << "\" ) " << isDirectoryExisting << endl;
		
		device3.Unmount();

		if( isFileExisting && !CFile::RemoveFile( strDeviceName ) )
		{
			wcout << L"CFile::RemoveFile( \""<< strDeviceName << "\" ) " << CEnv::GetLastError() << endl;
		}

		if( isDirectoryExisting )
		{
			int ec = CFileIO::RemoveDirectory(  strDeviceName.c_str() ) ;
			if( ec != 0 )
				wcout << L"CFileIO::RemoveDirectory( \""<< strDeviceName << "\" ) " << ec << endl;
		}

		return true;
	}

	void DeviceTest2()
	{
		std::time_t _tmv = std::time(NULL);
		std::wstringstream wss;
		wss<<_tmv;
		wss.str();

		std::wstring strDeviceName(L"D:\\test\\__dummy__");
		strDeviceName += wss.str();
		strDeviceName += L".pfs";
		DeviceTest2_(strDeviceName);

		std::wstring strDeviceName2(L"D:\\test\\__dummy__folder__" + wss.str() );
		DeviceTest2_(strDeviceName2);
	}

	bool DeviceTest_DeviceFile( std::wstring strDeviceName, MOUNT_TYPE mt )
	{
		CDevice device1;
		if( !device1.Mount( strDeviceName, mt ) )
		{
			wcerr<<L"device1.Mount( \""<< strDeviceName << "\", " << std::setw( 8 ) << std::hex << mt << ") " << std::dec << PFS::CEnv::GetLastError() << endl;

			// 无法继续测试
			return false;
		}
		else
		{
			wcout<<L"device1.Mount( \""<< strDeviceName << "\", " << std::setw( 8 ) << std::hex << mt << " ) OK. " << endl;
			wcout<< std::dec;
		}

		if( 1 )
		{
			CDeviceFile file1, file2, file3, file4, file5;

			bool bRet = file1.Open( &device1, L"a1.xml", FM_CREAT, FA_RDWR );
			wcout<< L"CDeviceFile file1; file1.Open( &device1, \"a1.xml\", FM_CREATE, FA_RDWR ) returns " << bRet << ". EC:" << CEnv::GetLastError() << endl;

			bRet = file2.Open( &device1, L"a2.xml", FM_EXCL, FA_RDWR );
			wcout<< L"CDeviceFile file2; file2.Open( &device1, \"a2.xml\", FM_EXCL, FA_RDWR ) returns " << bRet << ". EC:" << CEnv::GetLastError() << endl;


			bRet = file3.Open( &device1, L"a3.xml", FM_CREAT | FM_TRUNC, FA_RDWR );
			wcout<< L"CDeviceFile file3; file3.Open( &device1, \"a3.xml\", FM_CREAT | FM_TRUNC, FA_RDWR ) returns " << bRet << ". EC:" << CEnv::GetLastError() << endl;


			bRet = file4.Open( &device1, L"a4.xml", FM_EXCL | FM_TRUNC, FA_RDWR );
			wcout<< L"CDeviceFile file4; file4.Open( &device1, \"a4.xml\", FM_EXCL | FM_TRUNC, FA_RDWR ) returns " << bRet << ". EC:" << CEnv::GetLastError() << endl;


			bRet = file5.Open( &device1, L"a5.xml", FM_EXCL, FA_RDONLY );
			wcout<< L"CDeviceFile file5; file5.Open( &device1, \"a5.xml\", FM_EXCL, FA_RDONLY ) returns " << bRet << ". EC:" << CEnv::GetLastError() << endl;
		}
		else
		{
			CDeviceFile file1, file2, file3, file4, file5;

			bool bRet = file1.Open( &device1, L"a10.xml", FM_CREAT, FA_RDWR );
			wcout<< L"CDeviceFile file1; file1.Open( &device1, \"a10.xml\", FM_CREATE, FA_RDWR ) returns " << bRet << ". EC:" << CEnv::GetLastError() << endl;

			bRet = file2.Open( &device1, L"a20.xml", FM_EXCL, FA_RDWR );
			wcout<< L"CDeviceFile file2; file2.Open( &device1, \"a20.xml\", FM_EXCL, FA_RDWR ) returns " << bRet << ". EC:" << CEnv::GetLastError() << endl;


			bRet = file3.Open( &device1, L"a30.xml", FM_CREAT | FM_TRUNC, FA_RDWR );
			wcout<< L"CDeviceFile file3; file3.Open( &device1, \"a30.xml\", FM_CREAT | FM_TRUNC, FA_RDWR ) returns " << bRet << ". EC:" << CEnv::GetLastError() << endl;


			bRet = file4.Open( &device1, L"a40.xml", FM_EXCL | FM_TRUNC, FA_RDWR );
			wcout<< L"CDeviceFile file4; file4.Open( &device1, \"a40.xml\", FM_EXCL | FM_TRUNC, FA_RDWR ) returns " << bRet << ". EC:" << CEnv::GetLastError() << endl;


			bRet = file5.Open( &device1, L"a50.xml", FM_EXCL, FA_RDONLY );
			wcout<< L"CDeviceFile file5; file5.Open( &device1, \"a50.xml\", FM_EXCL, FA_RDONLY ) returns " << bRet << ". EC:" << CEnv::GetLastError() << endl;

		}

		return true;
	}

	bool DeviceTest3_WithMountType( std::wstring strDeviceName, MOUNT_TYPE mt )
	{
		if( PFS::IsMountReadOnly( mt ) )
		{
			bool isFileExisting = CFile::IsFileExisting( strDeviceName );
			wcout << L"CFile::IsFileExisting( \""<< strDeviceName << "\" ) " << isFileExisting << endl;
			
			if( !isFileExisting )
			{
				CFile file;
				if( !file.Open( strDeviceName, FM_CREAT, FA_RDWR ) )
				{
					wcout << L"CFile::Open( \""<< strDeviceName << "\", FM_CREAT, FA_RDWR ) " << CEnv::GetLastError() << endl;
				}
			}
		}
	
		return DeviceTest_DeviceFile( strDeviceName, mt );
	}

	bool DeviceTest3_READONLY_PFS(  )
	{
		std::wstring strDeviceName(L"D:\\test\\__readonly__.pfs");
		return DeviceTest3_WithMountType( strDeviceName, MT_READONLY );
	}

	bool DeviceTest3_RUNTIME_PFS()
	{
		std::wstring strDeviceName(L"D:\\test\\__runtime__.pfs");
		return DeviceTest3_WithMountType( strDeviceName, MT_RUNTIME );
	}

	bool DeviceTest3_WRITABLE_PFS()
	{
		std::wstring strDeviceName(L"D:\\test\\__writable__.pfs");
		return DeviceTest3_WithMountType( strDeviceName, MT_WRITABLE );
	}

	bool DeviceTest3_TRUNCATE_PFS()
	{
		std::wstring strDeviceName(L"D:\\test\\__truncate__.pfs");
		return DeviceTest3_WithMountType( strDeviceName, MT_TRUNCATE );
	}

	bool DeviceTest3_PREFERDIR_PFS()
	{
		std::wstring strDeviceName(L"D:\\test\\__preferdir__.pfs");
		return DeviceTest3_WithMountType( strDeviceName, MT_PREFERDIR );
	}

	void DeviceTest3()
	{
		DeviceTest3_READONLY_PFS();
		DeviceTest3_RUNTIME_PFS();
		DeviceTest3_WRITABLE_PFS();
		DeviceTest3_TRUNCATE_PFS();
		DeviceTest3_PREFERDIR_PFS();
	}

	bool DeviceTest4_WithMountType( std::wstring strDeviceName, MOUNT_TYPE mt )
	{
		if( IsMountReadOnly(mt) )
		{
			bool isNativePath = strDeviceName[0] == L'/';
			if( !isNativePath )
			{
				std::wstring nativeDir = strDeviceName;
				isNativePath = PFS::CEnv::PFSFileNameToNativeFileName( nativeDir, strDeviceName );
			}
			
			bool isDirectoryExisting = isNativePath && CFileIO::IsDirectoryExisting( strDeviceName );
			wcout << L"CFileIO::IsDirectoryExisting( \""<< strDeviceName << "\" ) " << isDirectoryExisting << endl;

			if( !isDirectoryExisting )
			{
				int ec = CFileIO::CheckCreateDirectory( strDeviceName.c_str() );
				if( ec != 0 )
				{
					wcout << L"CFileIO::CheckCreateDirectory( \""<< strDeviceName << "\" ) " << ec << endl;
				}
			}
		}

		return DeviceTest_DeviceFile( strDeviceName, mt );
	}
	
	bool DeviceTest4_READONLY_DIR(  )
	{
		std::wstring strDeviceName(L"D:\\test\\__readonly__dir__");
		return DeviceTest4_WithMountType( strDeviceName, MT_READONLY );
	}

	bool DeviceTest4_RUNTIME_DIR()
	{
		std::wstring strDeviceName(L"D:\\test\\__runtime__dir__");
		return DeviceTest4_WithMountType( strDeviceName, MT_RUNTIME );
	}

	bool DeviceTest4_WRITABLE_DIR()
	{
		std::wstring strDeviceName(L"D:\\test\\__writable__dir__");
		return DeviceTest4_WithMountType( strDeviceName, MT_WRITABLE );
	}

	bool DeviceTest4_TRUNCATE_DIR()
	{
		std::wstring strDeviceName(L"D:\\test\\__truncate__dir__");
		return DeviceTest4_WithMountType( strDeviceName, MT_TRUNCATE );
	}

	bool DeviceTest4_PREFERDIR_DIR()
	{
		std::wstring strDeviceName(L"D:\\test\\__prefer__dir__");
		return DeviceTest4_WithMountType( strDeviceName, MT_PREFERDIR );
	}

	void DeviceTest4()
	{
		DeviceTest4_READONLY_DIR();
		DeviceTest4_RUNTIME_DIR();
		DeviceTest4_WRITABLE_DIR();
		DeviceTest4_TRUNCATE_DIR();
		DeviceTest4_PREFERDIR_DIR();
	}

	void Test3_4()
	{
		DeviceTest3();
		DeviceTest4();
	}

	void PackageDirectoryDeviceTest_root( std::wstring root, bool dir, MOUNT_TYPE mt, MOUNT_TYPE mt2 )
	{
		if( dir )
		{
			std::wstringstream wss;
			wss.fill(L'0');
			wss << root << L"/__" << std::setw( 8 ) << std::hex << mt << L"__dir";
			std::wstring device = wss.str();

			std::wstringstream wssdir;
			wssdir.fill(L'0');
			wssdir<< L"/__" << std::setw( 8 ) << std::hex << mt << L"__dir";

			CEnv::Mount( wssdir.str(), device, FST_NATIVE, mt );

			std::wstringstream wsssubdir;
			wsssubdir.fill(L'0');
			wsssubdir << wssdir.str() << L"/__"<<  std::setw( 8 ) << std::hex << mt2 << L"__subdir";
			std::wstring device2 = wsssubdir.str();

			DeviceTest4_WithMountType( device2, mt2 );

			CEnv::Unmount( wssdir.str() );
		}
		else
		{
			std::wstringstream wss;
			wss.fill(L'0');
			wss << root << L"/__" << std::setw( 8 ) << std::hex << mt << L"__.pfs";
			std::wstring device = wss.str();
			
			std::wstringstream wssdir;
			wssdir.fill(L'0');
			wssdir<< L"/__" << std::setw( 8 ) << std::hex << mt << L"__pfs";
			CEnv::Mount(wssdir.str(), device, FST_ZIP, mt );
					
			std::wstringstream wss2;
			wss2.fill(L'0');
			wss2 << wssdir.str() << L"/__" << std::setw( 8 ) << std::hex << mt2 << L"__.pfs";
			std::wstring device2 = wss2.str();
			
			DeviceTest3_WithMountType( device2, mt2 );

			CEnv::Unmount( wssdir.str() );
		}
	}

	void PackageDirectoryDeviceTest_( bool dir, std::wstring root )
	{
		MOUNT_TYPE mts[] ={ MT_READONLY, MT_RUNTIME, MT_WRITABLE, MT_TRUNCATE, MT_PREFERDIR };

		for( int i = 0; i < _countof(mts); i ++ )
		{
			for( int j = 0; j < _countof(mts); j ++ )
			{
				PackageDirectoryDeviceTest_root( root, dir, mts[i], mts[j] );
			}
		}

		for( int i = 0; i < _countof(mts); i ++ )
		{
			for( int j = 0; j < _countof(mts); j ++ )
			{
				MOUNT_TYPE mt2 = MakeMountTypeWithFSType( mts[j], FST_NATIVE );
				PackageDirectoryDeviceTest_root( root, dir, mts[i], mt2 );
			}
		}

		
		for( int i = 0; i < _countof(mts); i ++ )
		{
			for( int j = 0; j < _countof(mts); j ++ )
			{
				MOUNT_TYPE mt2 = MakeMountTypeWithFSType( mts[j], FST_ZIP );
				PackageDirectoryDeviceTest_root( root, dir, mts[i], mt2 );
			}
		}

	
		for( int i = 0; i < _countof(mts); i ++ )
		{
			for( int j = 0; j < _countof(mts); j ++ )
			{
				MOUNT_TYPE mt2 = MakeMountTypeWithFSType( mts[j], FST_RUNZIP );
				PackageDirectoryDeviceTest_root( root, dir, mts[i], mt2 );
			}
		}

	}

	void PackageDirectoryDeviceTest()
	{
		wcout.fill(L'_');
		wcerr.fill(L'_');
		CEnv::Mount( L"/test", L"D:\\test", FST_NATIVE, MT_RUNTIME );

		bool bRetM = CEnv::Mount( L"/ui", L"/test/ui.pfs", FST_RUNZIP, MT_RUNTIME );

		if( !bRetM )
		{
			int nError = CEnv::GetLastError();

			CEnv::Unmount( L"/test" );
			return;
		}

		CDevice device;
		bool bRet = device.Mount(L"/ui");
		if( !bRet )
		{
			int nError = CEnv::GetLastError();

			CEnv::Unmount( L"/ui" );
			CEnv::Unmount( L"/test" );
			return;
		}

		bRetM = CEnv::Mount( L"/abc", L"/test/abc", FST_NATIVE, MT_RUNTIME );

		if( !bRetM )
		{
			int nError = CEnv::GetLastError();

			CEnv::Unmount( L"/ui" );
			CEnv::Unmount( L"/test" );

			return;
		}

		CDevice device2;
		bRet = device2.Mount(L"/abc");
		if( !bRet )
		{
			int nError = CEnv::GetLastError();

			CEnv::Unmount( L"/abc" );
			CEnv::Unmount( L"/ui" );
			CEnv::Unmount( L"/test" );
			return;
		}

		//PackageDirectoryDeviceTest_( true, L"/test" );
		//PackageDirectoryDeviceTest_( false, L"/test" );
		CEnv::Unmount( L"/ui" );
			
		CEnv::Unmount( L"/test" );
	}

	void MountFileSystemDeviceTest()
	{
		
		wcout.fill(L'_');
		wcerr.fill(L'_');

		CEnv::Mount( L"/test", L"D:\\test", FST_NATIVE, MT_WRITABLE );

		bool bRetM = CEnv::Mount( L"/pak", L"/test/pak", FST_NATIVE, MT_WRITABLE );

		if( !bRetM )
		{
			int nError = CEnv::GetLastError();

			CEnv::Unmount( L"/test" );
			return;
		}

		CDevice device;
		bool bRet = device.Mount(L"/pak/a.pfs", MT_WRITABLE);
		if( !bRet )
		{
			int nError = CEnv::GetLastError();

			CEnv::Unmount( L"/pak" );
			CEnv::Unmount( L"/test" );
			return;
		}

		CEnv::Unmount( L"/pak" );
			
		CEnv::Unmount( L"/test" );
	}
	void Test()
	{
		MountFileSystemDeviceTest();
		// PackageDirectoryDeviceTest();
	}
}

