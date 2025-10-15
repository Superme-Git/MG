#include "../pfs2.h"
#include "../../xmlio/xmlio.h"
#include "../mountmanager.h"
#include <iostream>

namespace MOUNTMANAGER
{
	namespace helper {

		// GameClient.exe ����λ��install directory ��bin/binDĿ¼��
		// ��ˣ�InstallDirΪModuleDir�ĸ�Ŀ¼
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

	���Է�����

	1.	��releaseĿ¼��׼�������ļ�
		cfg/a.txt		����  a.txt in cfg/a.txt \n
		cfg/ab.txt		����  ab.txt in cfg/ab.txt \n
		cfg/f/c.txt		����  c.txt in cfg/f/c.txt \n
		
		a/cfg/b.txt		����  --- b.txt in a/cfg/b.txt
		a/cfg/ab.txt	����  --- ab.txt in a/cfg/ab.txt
		a/cfg/f/c.txt	����  --- c.txt in a/cfg/f/c.txt
		a/cfg/f/fc.txt	����  --- fc.txt in a/cfg/f/fc.txt

		��Ŀ¼a�еĴ��a.pfs����ע��,a.pfs�в�����Ŀ¼a����

	2.	wtf/mount.xml������������:
		<option group="0" name="base-filter" value="1"/>
		<mount dir="/run" device="%current-dir%" mt="2"/>
 		<mount dir="/a" device="/run/a" mt="18"/>
		<mount dir="/cfg" device="/run/cfg" filter="/a/cfg" mt="2"/>

	���Խ��ۣ�

		����filterʱ��
			open�����ļ�Ӧ�ô򿪵���aĿ¼�¶�Ӧ���ļ�

			/cfg/ab.txt			--> /a/cfg/ab.txt
			/cfg/f/c.txt		--> /a/cfg/f/c.txt
			/cfg/f/fc.txt		--> /a/cfg/f/fc.txt

			open�����ļ�Ӧ�ô򿪵���cfgĿ¼�¶�Ӧ���ļ�
			/cfg/a.txt			--> /cfg/a.txt
			/cfg/b.txt			--> /a/cfg/b.txt
			
			open�����ļ�ʧ��Ŀ¼�¶�Ӧ���ļ�
			/cfg/c.txt			--> <fail to open>

		�ر�filterʱ:

			open�����ļ�Ӧ�ô򿪵���aĿ¼�¶�Ӧ���ļ�
			/cfg/a.txt			--> /cfg/a.txt
			/cfg/ab.txt			--> /cfg/ab.txt
			/cfg/f/c.txt		--> /cfg/f/c.txt
			
			open�����ļ�ʧ��
			/cfg/b.txt			--> <fail to open>
			/cfg/c.txt			--> <fail to open>
			/cfg/f/fc.txt		--> <fail to open>

		�����Ϊ�� Test0() ����ȷ�ϡ�

		Test1() ���ڲ��� pfs ���� filter���ƶ��� filename�ļ����ԡ�
		Ŀ�����ڣ�������ͬ����filename�����£�����filter�͹ر�filter����Ӧ��һ�¡�
		�����a/cfg/�ܶ������ļ�������pathӦ�ö��ܶ�����
		�����a/cfg/���ܶ������ļ�������pathӦ�ò��ܴ�a�ж�����
		�����a/cfg/���ܶ������ļ���������/cfgĿ¼���ܶ������ļ�������path��Ϊһ�¡�
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

		// �쳣��, �μ�TestOpenFiles1()
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

		// �쳣��, �μ�TestOpenFiles1()
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
