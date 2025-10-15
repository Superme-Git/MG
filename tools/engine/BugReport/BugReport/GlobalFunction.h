#pragma once

namespace helper
{

#pragma comment(lib, "version.lib")
DWORD GetFileVersion( const wchar_t* szPath, wchar_t* szVersion, size_t len)
{
	DWORD res = 0;

	DWORD dwVerHnd;
	DWORD dwVerInfoSize = GetFileVersionInfoSizeW(
		szPath, // pointer to filename string
		&dwVerHnd // pointer to variable to receive zero
		);

	if (dwVerInfoSize) 
	{
		HANDLE  hMem;
		LPVOID  lpvMem = NULL;
		unsigned int uInfoSize = 0;
		VS_FIXEDFILEINFO * pFileInfo = NULL;

		hMem = GlobalAlloc(GMEM_MOVEABLE, dwVerInfoSize);
		lpvMem = GlobalLock(hMem);
		GetFileVersionInfoW(szPath, dwVerHnd, dwVerInfoSize, lpvMem);

		::VerQueryValueW(lpvMem, L"\\", (void**)&pFileInfo, &uInfoSize);

		// Product version from the FILEVERSION of the version info resource 
		res = swprintf_s( szVersion, len, L"%d.%d.%d.%d", 
			HIWORD(pFileInfo->dwProductVersionMS),
			LOWORD(pFileInfo->dwProductVersionMS),
			HIWORD(pFileInfo->dwProductVersionLS),
			LOWORD(pFileInfo->dwProductVersionLS));

		GlobalUnlock(hMem);
		GlobalFree(hMem);
	}
	return res;
}

#pragma comment(lib,"ws2_32.lib")
DWORD GetComputerName( wchar_t* szInfo, size_t len)
{
	WORD wVersionRequested;
	WSADATA wsaData;
	int err;
	wVersionRequested = MAKEWORD( 2, 2 );
	err = WSAStartup( wVersionRequested, &wsaData );
	if( err != 0)
	{
		return 0;
	}

	const size_t COUNT = 512;
	char hostname[COUNT] = {0};
	
	if( gethostname( hostname, COUNT) == 0)
	{
		hostent* inaddrs = gethostbyname( hostname);
		if(inaddrs)
		{
			strcat_s( hostname, COUNT, "(");
			for( int i=0; inaddrs->h_addr_list[i]; i++)
			{
				in_addr ia;
				memcpy( &ia.s_addr, inaddrs->h_addr_list[i], sizeof(ia.s_addr));
				if( i > 0)
				{
					// 如果有多个ip用空格分开
					strcat_s( hostname, COUNT, " ");
				}
				strcat_s( hostname, COUNT, inet_ntoa(ia));
			}
			strcat_s( hostname, COUNT, ")");
		}
	}

	WSACleanup( );

	if( 0 == hostname[0])
	{
		return 0;
	}

	enum { CodePage = 936 };
	return MultiByteToWideChar( CodePage, 0, hostname, (int)strlen( hostname), szInfo, (int)len);
}

DWORD GetComputerUserName( wchar_t* szInfo, size_t len)
{
	DWORD	bufCharCount = (DWORD)len;

	if( !GetUserNameW( szInfo, &bufCharCount))
	{
		return 0;
	}
	return bufCharCount;
}

};