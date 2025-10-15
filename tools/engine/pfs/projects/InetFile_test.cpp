#include "../pfs2.h"
#include "..\src\common.h"
#include "..\src\brief\brief.h"
#include <wininet.h>

#include <conio.h>
#include <iostream>
#include <sstream>
#include <ctime >
using namespace PFS;
using namespace PFSX;

namespace INETFILETEST
{
	void Test();
}

namespace INETFILETEST
{
	// 05dbb3f8 00000003 005079fb gameclient!PFS::InetOpenUrl(void * hInternet = <Memory access error>, 
	// wchar_t * lpszUrl = 0x0d20eee0 "http://mhzx.locojoy.com/launcher/left.xml")+0x25

	static std::wstring url[] =
	{
		L"http://172.16.32.30/mhzx/.files.meta",
		L"http://172.16.32.30/mhzx/patcher/repair.exe",
	};

	static LPCTSTR _GetAgentName()
	{
		static TCHAR AgentName[MAX_PATH];
		if ( AgentName[0] == NULL )
		{
			GetModuleFileName( NULL, AgentName, MAX_PATH);
			PathStripPath( AgentName );//Leaves the file name only
		}
		return AgentName;
	}
		
	DWORD WINAPI SubTest(LPVOID i)
	{
		const std::wstring& download = url[((DWORD_PTR)i)%2];
		std::wcout<< GetCurrentThreadId() << L" download file: " << download << std::endl;

		std::wstring local = download.substr( download.rfind( L'/'));

		PFSX::CNativeFile tempfile;
		std::wostringstream woss;
		woss << L"d:\\test_inet\\" << i << local;

		if( !tempfile.Open( woss.str(), PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
		{
			std::wcout << L"open local file: " << woss.str() << L" failed! " << PFS::CEnv::GetLastError() << std::endl;
			return 0;
		}

		CInetFile file;
		bool bRet = file.Open( download);
		if( bRet)
		{
			// 优先使用注册表配置
			HINTERNET hInternet = InternetOpen(_GetAgentName(), 
				INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
			if ( hInternet == NULL )
			{
				std::wcout << L"InternetOpen failed GLE: " << GetLastError() << std::endl;
				return 0;
			}

			std::wstring::size_type pos = download.find( L"//");
			std::wstring::size_type pos2 = download.find( L'/', pos+2);
			std::wstring ip = download.substr( pos+2, pos2-pos-2);
			std::wstring fileurl = download.substr( pos2);

			HINTERNET hConnect = InternetConnect(
				hInternet,
				ip.c_str(),
				INTERNET_DEFAULT_HTTP_PORT, // INTERNET_INVALID_PORT_NUMBER, // INTERNET_DEFAULT_HTTP_PORT,
				TEXT(""), // LPCTSTR lpszUsername,
				TEXT(""), // LPCTSTR lpszPassword,
				INTERNET_SERVICE_HTTP,
				0,
				0);

			if( hConnect == NULL)
			{
				std::wcout << L"InternetConnect failed GLE: " << GetLastError() << std::endl;
				return 0;
			}

			const size_t BUFFERLEN = 4096;
			CDataBlock buffer;
			

			size_t len = (size_t)file.GetSize();
			size_t lBegin = 0;
			size_t lEnd = len;
			while( true)
			{
				if( lBegin+BUFFERLEN < len)
				{
					lEnd = lBegin+BUFFERLEN;
					buffer.Resize( BUFFERLEN);
				}
				else
				{
					lEnd = -1;
					buffer.Resize( len-lBegin);
				}
				bRet =  file.Open( hConnect, fileurl, lBegin, lEnd );
				//bool bRet =  file.Open( download );
				if( bRet  )
				{
					if( file.Read( buffer.GetData(), buffer.GetSize()) == buffer.GetSize())
					{
						//std::wstring strXML((wchar_t*)imageData.GetData()+1, (unsigned)(imageData.GetSize()-2)/2 );
						//std::wcout<< strXML << std::endl;
						tempfile.Write( buffer.GetData(), buffer.GetSize());
						if( lEnd == -1)
						{
							tempfile.SetEnd();
							break;
						}
						lBegin += BUFFERLEN;
					}
					else
					{
						std::wcout<< L"Open url failed! FILE_SIZE_ERROR " << std::endl;
						return 0;
					}
				}
				else
				{
					int error = PFS::CEnv::GetLastError();
					std::wcout<< L"Open url from " << lBegin << L" to " << lEnd << L" failed! "<< error << std::endl;
					return 0;
				}
			}
		}
		else
		{
			std::wcout<< L"Open url failed! "<< PFS::CEnv::GetLastError() << std::endl;
			return 0;
		}

		std::wcout<< L"download url " << download << L" successed!" << std::endl;

		return 0;
	}

	void Test()
	{
		std::wcout.imbue( std::locale( "chs") );
		const int COUNT = 2;
		int i = COUNT;
		srand( (unsigned) std::time(NULL ) );
		HANDLE hThread[COUNT] = {0};
		do 
		{
			hThread[COUNT-i] = CreateThread( NULL, 0, &SubTest, (LPVOID)i, 0, 0 );
			std::wcout<< L"Thread created Done!" << (DWORD_PTR)hThread[COUNT-i] << std::endl;
			Sleep( rand() % 2000 ) ;
		} while( --i );

		WaitForMultipleObjects( COUNT, hThread, TRUE, INFINITE );

		std::wcout<< L"Done!";
	}
}
