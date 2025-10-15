#include "../pfs2.h"
#include "../../xmlio/xmlio.h"
#include "../mountmanager.h"
#include <iostream>

namespace MOUNTMANAGER
{
	namespace helper {

		// GameClient.exe 程序位于install directory 的bin/binD目录下
		// 因此，InstallDir为ModuleDir的父目录
		LPCWSTR GetInstallDir()
		{
			static WCHAR szPathBuffer[MAX_PATH];
			if ( szPathBuffer[0] == 0 )
			{
				DWORD dwRet = GetModuleFileName( NULL, szPathBuffer, MAX_PATH);
				LPWSTR lpSep = &szPathBuffer[dwRet-1];
				while( *lpSep != L'/' && *lpSep != L'\\' && *lpSep != L':' && *lpSep != L'\0' )
				{
					lpSep --;
				}

				if( *lpSep != L'\0' )
					lpSep --;

				while( *lpSep != L'/' && *lpSep != L'\\' && *lpSep != L':' && *lpSep != L'\0' )
				{
					lpSep --;
				}

				lpSep[1] = L'\0';
			}

			return szPathBuffer;
		}

	}

	/*

	测试方案：

	1.	在release目录下准备如下文件
		cfg/a.txt		内容  a.txt in cfg/a.txt \n
		cfg/ab.txt		内容  ab.txt in cfg/ab.txt \n
		cfg/f/c.txt		内容  c.txt in cfg/f/c.txt \n
		
		a/cfg/b.txt		内容  --- b.txt in a/cfg/b.txt
		a/cfg/ab.txt	内容  --- ab.txt in a/cfg/ab.txt
		a/cfg/f/c.txt	内容  --- c.txt in a/cfg/f/c.txt
		a/cfg/f/fc.txt	内容  --- fc.txt in a/cfg/f/fc.txt

		将目录a中的打成a.pfs包。注意,a.pfs中不含有目录a本身。

	2.	wtf/mount.xml进行如下配置:
		<option group="0" name="base-filter" value="1"/>
		<mount dir="/run" device="%current-dir%" mt="2"/>
 		<mount dir="/a" device="/run/a" mt="18"/>
		<mount dir="/cfg" device="/run/cfg" filter="/a/cfg" mt="2"/>

	测试结论：

		开启filter时：
			open如下文件应该打开的是a目录下对应的文件

			/cfg/ab.txt			--> /a/cfg/ab.txt
			/cfg/f/c.txt		--> /a/cfg/f/c.txt
			/cfg/f/fc.txt		--> /a/cfg/f/fc.txt

			open如下文件应该打开的是cfg目录下对应的文件
			/cfg/a.txt			--> /cfg/a.txt
			/cfg/b.txt			--> /a/cfg/b.txt
			
			open如下文件失败目录下对应的文件
			/cfg/c.txt			--> <fail to open>

		关闭filter时:

			open如下文件应该打开的是a目录下对应的文件
			/cfg/a.txt			--> /cfg/a.txt
			/cfg/ab.txt			--> /cfg/ab.txt
			/cfg/f/c.txt		--> /cfg/f/c.txt
			
			open如下文件失败
			/cfg/b.txt			--> <fail to open>
			/cfg/c.txt			--> <fail to open>
			/cfg/f/fc.txt		--> <fail to open>

		这个行为由 Test0() 测试确认。

		Test1() 用于测试 pfs 及其 filter机制对于 filename的兼容性。
		目的在于，做到在同样的filename机制下，启用filter和关闭filter表现应该一致。
		如果在a/cfg/能读到的文件，所有path应该都能读到。
		如果在a/cfg/不能读到的文件，所有path应该不能从a中读到。
		如果在a/cfg/不能读到的文件，但能在/cfg目录下能读到的文件，所有path行为一致。
	*/

	void DumpTextFile(PFS::CFile& file)
	{
		PFS::fsize_t total = file.GetSize();
		PFS::fsize_t totalread = 0;
		unsigned char buffer[258] = {0};
		
		PFS::fsize_t curr = file.Read( buffer, 256 );
		totalread += curr;
		bool bUnicode = false;
		if( buffer[0] == 0xFF &&
			buffer[1] == 0xFE )
		{
			std::wcout << (const wchar_t*)(buffer + 2);
			bUnicode = true;
		}
		else
		{
			std::cout << buffer;
		}

		return ;

		if( bUnicode )
		{
			while( totalread < total )
			{
				curr = file.Read( buffer, 256 );
				if( curr < 256 )
				{
					buffer[curr] = 0;
					buffer[curr+1] = 0;
				}
				totalread += curr;

				std::wcout << (const wchar_t*)(buffer);
			}
		}
		else
		{
			while( totalread < total )
			{
				curr = file.Read( buffer, 256 );
				if( curr < 256 )
				{
					buffer[curr] = 0;
				}
				totalread += curr;
				std::cout << buffer;
			}
		}
	}

	bool TestOpenFile( PFS::CFile& file, std::wstring filename )
	{
		if( file.Open( filename, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<< L"open "<< filename << L" ok!" << std::endl;

			DumpTextFile( file );
			
			file.Close();
		}
		else
		{
			std::wcout<< L"open "<< filename << L" failed!" << std::endl;
		}

		return true;
	}

	bool TestOpenFilesFormalPath( )
	{
		if( PFS::CEnv::IsBaseFilterEnabled() )
		{
			std::wcout<< L"\n------base filter is enabled------\n"<<std::endl;
		}
		else
		{
			std::wcout<< L"\n------base filter is disabled------\n"<<std::endl;
		}
		
		std::wcout<< L"\n------=======------\n"<<std::endl;		

		PFS::CFile file;
		TestOpenFile( file, L"/cfg/a.txt" );
		TestOpenFile( file, L"/cfg/b.txt" );
		TestOpenFile( file, L"/cfg/c.txt" );
		TestOpenFile( file, L"/cfg/ab.txt" );
		TestOpenFile( file, L"/cfg/f/c.txt" );
		TestOpenFile( file, L"/cfg/f/fc.txt" );

		return true;
	}
	
	bool TestOpenFiles1( )
	{
		if( PFS::CEnv::IsBaseFilterEnabled() )
		{
			std::wcout<< L"\n------base filter is enabled------\n" <<std::endl;
		}
		else
		{
			std::wcout<< L"\n------base filter is disabled------\n" <<std::endl;
		}

		PFS::CFile file;
		
		// Test cases for file: /cfg/f/c.txt.
		// Passed ones in both native and zip file system:
		//		TestOpenFile( file, L"/cfg/f/c.txt" );
		//		TestOpenFile( file, L"/cfg/f/./c.txt" );
		//		TestOpenFile( file, L"/cfg/f/././c.txt" );
		//		TestOpenFile( file, L"/cfg/f/.\\../f/c.txt" );
		// The following ones cannot be found in pfs package but can be found in windows native file system.
		// it's bugs of PathCanonicalizeW(). We don't try to fix this behaviors.
		// After canonicalized, the paths would be "/cfg/f/f/c.txt".
		//		TestOpenFile( file, L"/cfg/f/.//../f/c.txt" ); 
		//		TestOpenFile( file, L"/cfg/f/.\\\\../f/c.txt" );
		//		TestOpenFile( file, L"/cfg/f/.//../f\\c.txt" );
		//		TestOpenFile( file, L"/cfg/f/.\\/../f\\c.txt" );
		//
		std::wcout<< L"\n------/cfg/f/c.txt------\n" << std::endl;

		// passed ones:
		TestOpenFile( file, L"/cfg/f/c.txt" );
		TestOpenFile( file, L"/cfg/f/./c.txt" );
		TestOpenFile( file, L"/cfg/f/././c.txt" );
		TestOpenFile( file, L"/cfg/f/.\\../f/c.txt" );
		//
		// Failed ones in zip file system:
		//
		// When we use our customized _PathCannonicalize(), they can go through...
		//
		TestOpenFile( file, L"/cfg/f////c.txt" );
		TestOpenFile( file, L"/cfg/f/.//../f/c.txt" );
		TestOpenFile( file, L"/cfg/f/.\\\\../f/c.txt" );
		TestOpenFile( file, L"/cfg/f/.//../f\\c.txt" );
		TestOpenFile( file, L"/cfg/f/.\\/../f\\c.txt" );
		TestOpenFile( file, L"/cfg/./././f/.\\/../f\\c.txt" );
		TestOpenFile( file, L"/cfg/./././f/.\\\\../f\\c.txt" );
		TestOpenFile( file, L"/cfg/./././f/.//../f\\c.txt" );

		return true;
	}


	bool TestOpenFiles2( )
	{
		if( PFS::CEnv::IsBaseFilterEnabled() )
		{
			std::wcout<< L"\n------base filter is enabled------\n" <<std::endl;
		}
		else
		{
			std::wcout<< L"\n------base filter is disabled------\n" <<std::endl;
		}

		PFS::CFile file;
		
		std::wcout<< L"\n------/cfg/f/c.txt------\n "<<std::endl;

		TestOpenFile( file, L"/cfg/./././f/./c.txt" );
		TestOpenFile( file, L"/cfg/./././f/./../f/c.txt" );
		TestOpenFile( file, L"/cfg/./././f\\./c.txt" );

		// 异常组, 参见TestOpenFiles1()
		//TestOpenFile( file, L"/cfg/./././f/.\\/../f\\c.txt" );
		//TestOpenFile( file, L"/cfg/./././f/.\\\\../f\\c.txt" );
		//TestOpenFile( file, L"/cfg/./././f/.//../f\\c.txt" );

		TestOpenFile( file, L"/cfg/./././f/.\\./../f\\c.txt" );
		TestOpenFile( file, L"/cfg/./././f/.\\.\\../f\\c.txt" );
		TestOpenFile( file, L"/cfg/./././f/././../f\\c.txt" );

		return true;
	}

	bool TestOpenFiles3( )
	{
		if( PFS::CEnv::IsBaseFilterEnabled() )
		{
			std::wcout<< L"\n------base filter is enabled------\n "<<std::endl;
		}
		else
		{
			std::wcout<< L"\n------base filter is disabled------\n "<<std::endl;
		}

		PFS::CFile file;

		std::wcout<< L"\n------/cfg/ab.txt------\n "<<std::endl;
		TestOpenFile( file, L"/cfg/./././ab.txt" );
		TestOpenFile( file, L"/cfg//ab.txt" );
		TestOpenFile( file, L"/cfg/\\ab.txt" );
		TestOpenFile( file, L"/cfg/.\\ab.txt" );
		TestOpenFile( file, L"/cfg///ab.txt" );
		TestOpenFile( file, L"/cfg/.//ab.txt" );
		TestOpenFile( file, L"/cfg/.\\/ab.txt" );
		TestOpenFile( file, L"/cfg/.\\.\\/ab.txt" );
		TestOpenFile( file, L"/cfg/./././f/../ab.txt" );
		TestOpenFile( file, L"/cfg/./././f/.././ab.txt" );
		TestOpenFile( file, L"/cfg/./././f/..\\ab.txt" );
		TestOpenFile( file, L"/cfg/./././f/..\\./ab.txt" );

		std::wcout<< L"\n------/cfg/f/c.txt------\n "<<std::endl;
		TestOpenFile( file, L"/cfg/./././f/./c.txt" );
		TestOpenFile( file, L"/cfg/./././f/./../f/c.txt" );
		TestOpenFile( file, L"/cfg/./././f\\./c.txt" );

		// 异常组, 参见TestOpenFiles1()
		//TestOpenFile( file, L"/cfg/./././f/.\\/../f\\c.txt" );
		//TestOpenFile( file, L"/cfg/./././f/.\\\\../f\\c.txt" );
		//TestOpenFile( file, L"/cfg/./././f/.//../f\\c.txt" );

		TestOpenFile( file, L"/cfg/./././f/.\\./../f\\c.txt" );
		TestOpenFile( file, L"/cfg/./././f/.\\.\\../f\\c.txt" );
		TestOpenFile( file, L"/cfg/./././f/././../f\\c.txt" );

		std::wcout<< L"\n------/cfg/f/fc.txt------\n "<<std::endl;
		TestOpenFile( file, L"/cfg/./././f/.././f/fc.txt" );
		TestOpenFile( file, L"/cfg/./././f/.././f/./fc.txt" );
		TestOpenFile( file, L"/cfg/./././f/..\\./f\\fc.txt" );
		TestOpenFile( file, L"/cfg/./././f/..\\/.\\f/./fc.txt" );

		return true;
	}


	// test _StripBasePath 
	bool TestOpenFiles4( )
	{
		if( PFS::CEnv::IsBaseFilterEnabled() )
		{
			std::wcout<< L"\n------base filter is enabled------\n "<<std::endl;
		}
		else
		{
			std::wcout<< L"\n------base filter is disabled------\n "<<std::endl;
		}

		PFS::CFile file;

		std::wcout<< L"\n------/cfg/a------\n "<<std::endl;

		TestOpenFile( file, L"/cfg/a.txt" );
		TestOpenFile( file, L"/cfg//a.txt" );
		TestOpenFile( file, L"/cfg/\\a.txt" );
		TestOpenFile( file, L"/cfg\\\\a.txt" );
		TestOpenFile( file, L"/cfg/\\\\a.txt" );
		TestOpenFile( file, L"/cfg//\\\\a.txt" );
		TestOpenFile( file, L"/cfg//\\//a.txt" );
		TestOpenFile( file, L"/cfg/\\.\\a.txt" );
		TestOpenFile( file, L"/cfg/.\\\\a.txt" );

		std::wcout<< L"\n------/cfg/f/c------\n "<<std::endl;

		TestOpenFile( file, L"/cfg/f/c.txt" );
		TestOpenFile( file, L"/cfg//f/c.txt" );
		TestOpenFile( file, L"/cfg/\\f/c.txt" );
		TestOpenFile( file, L"/cfg\\\\f/c.txt" );
		TestOpenFile( file, L"/cfg/\\\\f/c.txt" );
		TestOpenFile( file, L"/cfg//\\\\f/c.txt" );
		TestOpenFile( file, L"/cfg//\\//f/c.txt" );
		TestOpenFile( file, L"/cfg/\\.\\f/c.txt" );
		TestOpenFile( file, L"/cfg/.\\\\f/c.txt" );

		std::wcout<< L"\n------/cfg/b------\n "<<std::endl;

		TestOpenFile( file, L"/cfg/b.txt" );
		TestOpenFile( file, L"/cfg//b.txt" );
		TestOpenFile( file, L"/cfg/\\b.txt" );
		TestOpenFile( file, L"/cfg\\\\b.txt" );
		TestOpenFile( file, L"/cfg/\\\\b.txt" );
		TestOpenFile( file, L"/cfg//\\\\b.txt" );
		TestOpenFile( file, L"/cfg//\\//b.txt" );
		TestOpenFile( file, L"/cfg/\\.\\b.txt" );
		TestOpenFile( file, L"/cfg/.\\\\b.txt" );

		std::wcout<< L"\n------/cfg/f/fc------\n "<<std::endl;		

		TestOpenFile( file, L"/cfg/f/fc.txt" );
		TestOpenFile( file, L"/cfg//f/fc.txt" );
		TestOpenFile( file, L"/cfg/\\f/fc.txt" );
		TestOpenFile( file, L"/cfg\\\\f/fc.txt" );
		TestOpenFile( file, L"/cfg/\\\\f/fc.txt" );
		TestOpenFile( file, L"/cfg//\\\\f/fc.txt" );
		TestOpenFile( file, L"/cfg//\\//f/fc.txt" );
		TestOpenFile( file, L"/cfg/\\.\\f/fc.txt" );
		TestOpenFile( file, L"/cfg/.\\\\f/fc.txt" );
	
		return true;
	}


	bool Test0()
	{
		PFS::CEnv::EnableBaseFilter(true);
		{
			TestOpenFilesFormalPath();
		}

		PFS::CEnv::EnableBaseFilter(false);
		{
			TestOpenFilesFormalPath();
		}

		return true;
	}

	bool Test1()
	{
		PFS::CEnv::EnableBaseFilter(true);
		{
			TestOpenFiles1();
		}

		PFS::CEnv::EnableBaseFilter(false);
		{
			TestOpenFiles1();
		}

		PFS::CEnv::EnableBaseFilter(true);
		{
			TestOpenFiles2();
		}

		PFS::CEnv::EnableBaseFilter(false);
		{
			TestOpenFiles2();
		}

		
		PFS::CEnv::EnableBaseFilter(true);
		{
			TestOpenFiles3();
		}

		PFS::CEnv::EnableBaseFilter(false);
		{
			TestOpenFiles3();
		}

		// test _StripBasePath 
		PFS::CEnv::EnableBaseFilter(true);
		{
			TestOpenFiles4();
		}

		PFS::CEnv::EnableBaseFilter(false);
		{
			TestOpenFiles4();
		}

		return true;
	}

	void TestNPCConfig0()
	{
		if( PFS::CEnv::IsBaseFilterEnabled() )
		{
			std::wcout<< L"\n------base filter is enabled------\n "<<std::endl;
		}
		else
		{
			std::wcout<< L"\n------base filter is disabled------\n "<<std::endl;
		}

		PFS::CFile file;

		std::wcout<< L"\n------/cfg/npcconfig.xml------\n "<<std::endl;
		TestOpenFile( file, L"/cfg/npcconfig.xml" );
	}

	void TestNPCConfig()
	{
		PFS::CEnv::EnableBaseFilter(true);
		{
			TestNPCConfig0();
		}

		PFS::CEnv::EnableBaseFilter(false);
		{
			TestNPCConfig0();
		}
	}

	void Test()
	{
		LPCWSTR szPath = helper::GetInstallDir();
		SetCurrentDirectoryW( szPath );

		std::wstring currentdir = szPath;
		currentdir += L"\\wtf\\mount.xml";

		PFSX::CMountManager::MountFromXML( currentdir );

		//Test0();
		TestNPCConfig();

		PFSX::CMountManager::UnmountPFS();
		
	}
}
