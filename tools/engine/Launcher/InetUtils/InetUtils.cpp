//////////////////////////////////////////////////////////////////////////
//
// InetUtils.cpp : Defines the entry point for the DLL application.
//
//  [10/24/2008 yanghaibo]
//

#include "stdafx.h"
#include "InetUtils.h"

enum { CodePage = 936 }; // 明确指定URL中的文件名编码为936，参见zipfile.h

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
		DisableThreadLibraryCalls( hModule );
	case DLL_THREAD_ATTACH:
	case DLL_THREAD_DETACH:
	case DLL_PROCESS_DETACH:
		break;
	}
    return TRUE;
}

static void _LogPrint( IN LPCTSTR lpszFormat, ... )
{
	TCHAR szBuffer[ 1024 ] = {0};
	va_list ap;
	va_start( ap, lpszFormat );
	StringCchVPrintf( szBuffer, _countof(szBuffer), lpszFormat, ap);
	va_end( ap );

	// 暂时只能输出到控制台窗口了
	OutputDebugString( szBuffer );
}

BOOL InetCheckConnection( LPCTSTR lpszUrl )
{
	if( !PathIsURL(lpszUrl))
	{
		SetLastError(ERROR_INVALID_PARAMETER);
		return FALSE;
	}

	int tryCount = 3;
	BOOL bRet = FALSE;
	DWORD dwError = 0;
	DWORD dwTimeStart = GetTickCount();
	DWORD dwTimeElapse = dwTimeStart;
	do{
		bRet = InternetCheckConnection( lpszUrl, FLAG_ICC_FORCE_CONNECTION, 0 );
		if ( !bRet )
		{
			dwError = GetLastError();
			dwTimeElapse = GetTickCount() - dwTimeStart;
		}
	}while ( !bRet && 
		(dwError == ERROR_INTERNET_CANNOT_CONNECT || dwError == ERROR_INTERNET_TIMEOUT) && 
		dwTimeElapse < 20000 && 
		(Sleep(1000), --tryCount) );

	return bRet;
}

// 检查IE代理是否设置
// HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings
// ProxyEnable : REG_DWORD	
BOOL IsProxyEnabled()
{
	HKEY hkResult = NULL;
	LPTSTR lpszSubKey = TEXT("Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings");
	LONG lRet = RegOpenKeyEx( HKEY_CURRENT_USER, lpszSubKey, 0, KEY_QUERY_VALUE, &hkResult);
	if( lRet != ERROR_SUCCESS )
	{
		_LogPrint(TEXT("RegOpenKeyEx(\"%s\") failed: %d.\n"), lpszSubKey, lRet );
		return FALSE;
	}

	DWORD dwType = REG_DWORD;
	DWORD dwProxyEnable = 0;
	DWORD dwLength = sizeof(DWORD);
	lRet = RegQueryValueEx( hkResult, TEXT("ProxyEnable"), NULL, &dwType, (LPBYTE)&dwProxyEnable, &dwLength );
	RegCloseKey( hkResult );
	if( lRet != ERROR_SUCCESS )
	{
		_LogPrint(TEXT("RegQueryValueExW(\"%s\":ProxyEnable) failed: %d.\n"), lpszSubKey, lRet );
		return FALSE;
	}

	return !!dwProxyEnable;
}

const DWORD INET_OPEN_URL_FLAGS = 
	INTERNET_FLAG_HYPERLINK |
	INTERNET_FLAG_NEED_FILE |
	INTERNET_FLAG_NO_CACHE_WRITE  |
	INTERNET_FLAG_NO_COOKIES |
	INTERNET_FLAG_NO_UI | 
	INTERNET_FLAG_PRAGMA_NOCACHE | // Forces the request to be resolved by the origin server, even if a cached copy exists on the proxy. 
	INTERNET_FLAG_RELOAD |
	INTERNET_FLAG_RESYNCHRONIZE;

static TCHAR g_szNoCacheHeaders[] = TEXT("Pragma: no-cache\r\nCache-Control: no-cache\r\n");

static HINTERNET InetOpenUrl( HINTERNET hInternet, LPCTSTR lpszUrl )
{
	HINTERNET hFile = NULL;

	int tryCountConnect = 2; // 无法连接情况，重试2次
	int tryCountTimeout = 1; // 超时情况，不再重试，因为连接超时已经在调用前设置好（我们设置的为5秒超时）
	DWORD dwError = 0;
	do 
	{
		hFile = InternetOpenUrl(
			hInternet, lpszUrl,
			g_szNoCacheHeaders, lstrlen(g_szNoCacheHeaders), 
			INET_OPEN_URL_FLAGS, 0 );
		if ( !hFile )
		{
			dwError = GetLastError();

			if(dwError == ERROR_INTERNET_CANNOT_CONNECT )
			{
				--tryCountConnect;
				tryCountTimeout = 1;
			}
			else if( dwError == ERROR_INTERNET_TIMEOUT )
			{
				--tryCountTimeout;
				tryCountConnect = 2;
			}
			else
			{
				// other error, need not try again
				break;
			}
		}
	} while ( !hFile && tryCountConnect && tryCountTimeout );

	return hFile;
}

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

// 参考 http://www.blooberry.com/indexdot/html/topics/urlencoding.htm
// 对于 Reserved Characters 和 Unsafe Characters 交由 InternetCanonicalizeUrl 处理
//bool IsReservedChar( char c )
//{
//	const static char reserved[] = { 36, 38, 43, 44, 47, 58, 59, 61, 63, 64 };
//	return std::binary_search( &reserved[0], &reserved[_countof(reserved)], c );
//}

//bool IsUnsafeChar( char c )
//{
//	const static char unsafe[] = { 32, 34, 60, 62, 35, 37, 91, 92, 93, 94, 96, 123, 124, 125, 126 };
//	return std::binary_search( &unsafe[0], &unsafe[_countof(unsafe)], c );
//}

static WCHAR HexToInt( WCHAR hexChar )
{
	if( hexChar >= L'0' && hexChar <= L'9' )
	{
		return (WCHAR)(hexChar - L'0');
	}
	else if( hexChar >= L'A' && hexChar <= L'F' )
	{
		return (WCHAR)(hexChar - L'A' + 10 );
	}
	else
	{
		return 0;
	}
}

static BOOL URLDecode(LPCWSTR lpwszURL, LPWSTR lpszDest, DWORD* pdwResult )
{
	int iLength = lstrlenW(lpwszURL);

	DWORD lBufferLen = *pdwResult;
	LPWSTR lpszDestPtr = lpszDest;
	DWORD lResultLen = 0;
	BOOL bRet = TRUE;
	
	CHAR AnsiBuffer[MAX_PATH] = {0};

	for (int i = 0; ; )
	{
		while ( i < iLength && lpwszURL[i] != L'%')
		{
			if ( lResultLen < lBufferLen )
			{
				*lpszDestPtr ++ = (WCHAR)lpwszURL[i];
			}
			else
			{
				bRet = FALSE;
			}

			lResultLen ++;

			i ++;
		}
		if ( i + 2 < iLength )
		{
			int j = 0;
			for ( ; i + 2 < iLength && lpwszURL[i] == L'%'; j ++, i += 3 )
			{
				AnsiBuffer[j] = (CHAR)(((DWORD)HexToInt(lpwszURL[i+1]) << 4) + HexToInt(lpwszURL[i+2]));
			}

			j = MultiByteToWideChar( CodePage, 0, (LPCSTR)AnsiBuffer, j, lpszDestPtr, j);
			lpszDestPtr += j;
			lResultLen += j;
		}
		else if ( i < iLength )
		{
			*lpszDestPtr ++ = (WCHAR)lpwszURL[i];
			lResultLen ++;
			i++;
		}
		else
		{
			break;
		}
	}

	*lpszDestPtr = L'\0';

	*pdwResult = lResultLen;

	return bRet;
}

static BOOL URLEncode(LPCWSTR lpwszURL, LPWSTR lpszDest, DWORD* pdwResult )
{
	int nBufSize = WideCharToMultiByte( CodePage, 0, lpwszURL, -1, NULL, 0, 0, FALSE);
	if( 0 == nBufSize)
	{
		*pdwResult = 0;
		return FALSE;
	}

	unsigned char * p936Buffer = new unsigned char[ nBufSize ];

	int iLength = WideCharToMultiByte( CodePage, 0, lpwszURL, -1,
		(LPSTR)p936Buffer, nBufSize, 0, FALSE);

	iLength --;

	DWORD lBufferLen = *pdwResult;
	LPWSTR lpszDestPtr = lpszDest;
	DWORD lResultLen = 0;
	BOOL bRet = TRUE;
	for (int i = 0; i < iLength; i ++ )
	{
		if (p936Buffer[i] <= 0x7f)
		{
			if ( lResultLen < lBufferLen )
			{
				*lpszDestPtr ++ = (WCHAR)p936Buffer[i];
			}
			else
			{
				bRet = FALSE;
			}

			lResultLen ++;
		}
		else
		{
			if ( lResultLen + 3 < lBufferLen )
			{
				StringCchPrintf(lpszDestPtr, 4,  L"%%%02X", p936Buffer[i] );
				lpszDestPtr += 3;
			}
			else
			{
				bRet = FALSE;
			}

			lResultLen += 3;
		}
	}

	*lpszDestPtr = L'\0';

	*pdwResult = lResultLen;

	delete[] p936Buffer;

	return bRet;
}


static DWORD TraceInetError( IN LPCTSTR lpszUrl = NULL )
{
	DWORD dwError = GetLastError();
	TCHAR szBufferFull[ 1024 ] = {0};
	if ( dwError == ERROR_INTERNET_EXTENDED_ERROR )
	{
		DWORD dwHttpError = 0;
		TCHAR szBuffer[ 1024 ] = {0};
		DWORD dwBufferLength = 1024;
		InternetGetLastResponseInfo(
			&dwHttpError,
			szBuffer,
			&dwBufferLength);

		StringCchPrintf( szBufferFull, _countof(szBufferFull), 
			TEXT("Http Error: %s(%d)\n"), szBuffer, dwHttpError );					
	}
	else if( lpszUrl != NULL )
	{
		StringCchPrintf( szBufferFull, _countof(szBufferFull), 
			TEXT("Open url failed! url: %s, ErrorCode:%d\n"), lpszUrl, dwError );
	}
	else
	{
		StringCchPrintf( szBufferFull, _countof(szBufferFull), 
			TEXT("Inet API Error! ErrorCode:%d\n"), dwError );
	}

	_LogPrint(szBufferFull);

	return dwError;
}

DWORD InetHttpDownload(IN LPCTSTR lpszUrlFrom, IN LPCTSTR lpszLocalDirTo, 
					   OUT LPTSTR lpszDataFileName, IN DWORD dwDataFileNameSize )
{
	if ( lpszUrlFrom == NULL || lpszLocalDirTo == NULL )
	{
		SetLastError(ERROR_INVALID_PARAMETER);
		return ERROR_INVALID_PARAMETER;
	}

	if( !PathIsDirectory( lpszLocalDirTo ) &&
		!CreateDirectory( lpszLocalDirTo, NULL ) )
	{
		SetLastError(ERROR_INVALID_PARAMETER);
		return ERROR_INVALID_PARAMETER;
	}

	LPCTSTR lpszUrl = lpszUrlFrom;
	TCHAR szCanonicalizedUrl[INTERNET_MAX_URL_LENGTH];
	DWORD dwUrlSize = INTERNET_MAX_URL_LENGTH;
	if( InternetCanonicalizeUrl( lpszUrlFrom, szCanonicalizedUrl, &dwUrlSize, 0 ) )
	{
		lpszUrl = szCanonicalizedUrl;
	}
	else
	{
		return TraceInetError(lpszUrlFrom);
	}

	TCHAR szEncodedUrl[INTERNET_MAX_URL_LENGTH];
	dwUrlSize = INTERNET_MAX_URL_LENGTH;
	if ( URLEncode( lpszUrl, szEncodedUrl, &dwUrlSize ) )
	{
		lpszUrl = szEncodedUrl;
	}

	DWORD dwError = ERROR_SUCCESS;

	// 首先使用注册表配置方式打开Internet句柄
	HINTERNET hInternet = InternetOpen(_GetAgentName(), INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
	if ( hInternet == NULL )
	{
		dwError = GetLastError();
		_LogPrint( TEXT("InternetOpen(INTERNET_OPEN_TYPE_PRECONFIG) failed: %d.\n"), dwError );
		return dwError;
	}

	DWORD dwTimeout = 5000; // 5s
	BOOL bRet = InternetSetOptionW( hInternet, INTERNET_OPTION_CONNECT_TIMEOUT, &dwTimeout, sizeof(DWORD) );
	if( !bRet )
	{
		dwError = GetLastError();
		_LogPrint( TEXT("PRECONFIG:InternetSetOptionW(INTERNET_OPTION_CONNECT_TIMEOUT, %ums) failed: %d.\n"), dwTimeout, dwError );
	}

	HINTERNET hFileFrom = NULL;
	do 
	{
		BOOL bRet = TRUE;

		hFileFrom = InetOpenUrl( hInternet, lpszUrl );
		if ( hFileFrom == NULL )
		{
			dwError = TraceInetError(lpszUrl);
			InternetCloseHandle( hInternet );

			// 检查代理设置是否启用，如果没有启用，放弃直连模式的重试
			// 因为这种情况下，INTERNET_OPEN_TYPE_PRECONFIG就是直连。
			if( !IsProxyEnabled( ) )
			{
				break;
			}

			_LogPrint(TEXT("Proxy enabled, but can't connect through proxy server. Try again bypassing proxy settings.\n"));

			// 使用直连模式打开
			hInternet = InternetOpen(_GetAgentName(), INTERNET_OPEN_TYPE_DIRECT , NULL, NULL, 0 );
			if ( hInternet == NULL )
			{
				dwError = GetLastError();
				_LogPrint( TEXT("InternetOpen(INTERNET_OPEN_TYPE_DIRECT) failed: %d.\n"), dwError );
				return dwError;
			}

			// Try again
			hFileFrom = InetOpenUrl( hInternet, lpszUrl );
			if ( hFileFrom == NULL )
			{
				break;
			}
		}

		// Get the http StatusCode
		DWORD dwStatusCode = 0;
		DWORD dwStatusCodeSize = sizeof(dwStatusCode);
		bRet = HttpQueryInfo( hFileFrom, 
			HTTP_QUERY_STATUS_CODE|HTTP_QUERY_FLAG_NUMBER, 
			&dwStatusCode, &dwStatusCodeSize, 0 );
		if ( !bRet || dwStatusCode != HTTP_STATUS_OK && dwStatusCode != HTTP_STATUS_CONTINUE )
		{
			if( bRet ) // Get the StatusCode OK!
			{
				// Map the StatusCode to system error code
				if( dwStatusCode == HTTP_STATUS_NOT_FOUND )
				{
					dwError = ERROR_INTERNET_ITEM_NOT_FOUND;
				}
				else if( dwStatusCode == HTTP_STATUS_DENIED )
				{
					dwError = ERROR_INTERNET_NO_DIRECT_ACCESS;
				}
				else if ( dwStatusCode == HTTP_STATUS_URI_TOO_LONG )
				{
					dwError = ERROR_INTERNET_INVALID_URL;
				}
				else if ( dwStatusCode == HTTP_STATUS_GATEWAY_TIMEOUT )
				{
					dwError = ERROR_INTERNET_CANNOT_CONNECT;
				}
				else
				{
					dwError = TraceInetError(lpszUrl);
					if( dwError == ERROR_SUCCESS )
					{
						dwError = ERROR_HTTP_INVALID_SERVER_RESPONSE;
					}
				}
			}
			else
			{
				dwError = TraceInetError(lpszUrl);
			}

			break;
		}

		TCHAR FilePathName[INTERNET_MAX_PATH_LENGTH] = {0};
		TCHAR DataFileName[MAX_PATH] = {0};
		DWORD dwSize = sizeof(FilePathName);

		// try to get the file name from the option
		//
		bRet = InternetQueryOption( hFileFrom, INTERNET_OPTION_DATAFILE_NAME, FilePathName, &dwSize );
		if (!bRet)
		{
			dwError = TraceInetError(lpszUrl);
		}
		if( bRet && dwSize > 0 )
		{
			// 因为URLEncode的原因，我们已经不能简单地使用下面的方式获取文件名
			// 这里需要对URL进行Decode处理
			PathStripPath( FilePathName );
			PathUndecorate( FilePathName );
			URLDecode( FilePathName, DataFileName, &dwSize );
		}
		else
		{	// try to determinate the file name from the url path 
			URL_COMPONENTS UrlComponents = {0};
			UrlComponents.dwStructSize = sizeof(URL_COMPONENTS);
			UrlComponents.dwUrlPathLength = 1;
			UrlComponents.dwHostNameLength = 1;
			UrlComponents.dwSchemeLength = 1;
			UrlComponents.dwExtraInfoLength = 1;
			bRet = InternetCrackUrl( lpszUrlFrom, 0, 0, &UrlComponents );
			if( !bRet )
			{
				dwError = TraceInetError(lpszUrl);
				break;
			}

			if( UrlComponents.dwUrlPathLength == 0 )
			{
				dwError = ERROR_INTERNET_INVALID_URL;
				break;
			}
			
			StringCchCopyN(DataFileName, MAX_PATH, UrlComponents.lpszUrlPath, UrlComponents.dwUrlPathLength );

			// Leaves the file name only
			PathStripPath(DataFileName);
		}

		// 合并文件名和目录组成全路径
		PathCombine(FilePathName, lpszLocalDirTo, DataFileName);

		// 创建目标文件，以保存要下载的数据
		HANDLE hFileTo = CreateFile( FilePathName, GENERIC_WRITE, 
			FILE_SHARE_READ, NULL, CREATE_ALWAYS,  0, NULL );
		if ( hFileTo == INVALID_HANDLE_VALUE )
		{
			dwError = GetLastError();
			break;
		}

		// Put the file path name to the out parameter if needed
		if( lpszDataFileName != NULL && dwDataFileNameSize > 0 )
		{
			StringCchCopy( lpszDataFileName, dwDataFileNameSize, FilePathName );
		}

		DWORD dwNumberBytesWritten = 0;
		DWORD dwNumberBytesRead = 0;
		BYTE Buffer[ 1024 ];

		// download the file and save it to the destination
		do 
		{
			bRet = InternetReadFile( hFileFrom, Buffer, sizeof(Buffer), &dwNumberBytesRead );
			if ( !bRet )
			{
				dwError = TraceInetError();
				break;
			}

			if( dwNumberBytesRead > 0 &&
				!WriteFile( hFileTo, Buffer, dwNumberBytesRead, &dwNumberBytesWritten, NULL ) )
			{
				dwError = GetLastError();
				break;
			}

		} while ( dwNumberBytesRead );		

		CloseHandle( hFileTo );

	} while(0);

	if ( dwError != ERROR_SUCCESS )
	{
		SetLastError(dwError);
	}

	if ( hFileFrom != NULL )
	{
		InternetCloseHandle( hFileFrom );
		hFileFrom = NULL;
	}

	if ( hInternet != NULL )
	{
		InternetCloseHandle( hInternet );
		hInternet = NULL;
	}

	return dwError;
}

DWORD InetFtpDownload( LPCTSTR lpszUrlFrom, LPCTSTR lpszLocalDirTo )
{
	return InetHttpDownload( lpszUrlFrom, lpszLocalDirTo, NULL, 0 );
}

// Just a test case 
INETUTILS_API BOOL InetHttpDownload_Test(  )
{
	//LPCTSTR lpszUrlFrom = TEXT("http://172.16.2.34/file.rar");
	//LPCTSTR lpszUrlFrom = TEXT("http://172.16.2.126/file.rar");
	//LPCTSTR lpszUrlFrom = TEXT("http://www.sina.com.cn/index.html");
	//LPCTSTR lpszUrlFrom = TEXT("http://www.sina.com.cn");
	//LPCTSTR lpszUrlFrom = TEXT("http://172.16.2.126/mhzx/gameclient/程序更新日志.txt");
	LPCTSTR lpszUrlFrom = TEXT("http://172.16.2.126/2/中文/人民.txt");
	//LPCTSTR lpszUrlFrom = TEXT("http://172.16.2.126/2/中文/2.txt");
	
	LPCTSTR lpszLocalDirTo = TEXT("D:\\download");
	TCHAR FileName[MAX_PATH] = {0};
	TCHAR Info[1024] = {0};

	if( !InetCheckConnection(lpszUrlFrom) )
	{
		StringCchPrintf(Info, 1024, TEXT("%s can not be reached!"),lpszUrlFrom);
		::MessageBox( NULL, Info, TEXT("Error"), MB_OK | MB_ICONERROR );
		return false;
	}

	DWORD dwError = InetHttpDownload( lpszUrlFrom, lpszLocalDirTo, FileName, MAX_PATH );
	if( 0 == dwError )
	{
		StringCchPrintf(Info, 1024, TEXT("%s was downloaded to %s successfully!"), lpszUrlFrom, lpszLocalDirTo);
		::MessageBox( NULL, Info, TEXT("Info"), MB_OK );
		return TRUE;
	}
	else
	{
		StringCchPrintf(Info, 1024, TEXT("Failed to download %s!(%d)"), lpszUrlFrom, dwError);
		::MessageBox( NULL, Info, TEXT("Error"), MB_OK | MB_ICONERROR );
		return FALSE;
	}
}


namespace InetUtil
{
	CInetIO::CInetIO()
	{
		m_hInternet = NULL;
		m_hFile = NULL;		
		ZeroMemory( m_szDataFileName, sizeof(m_szDataFileName) );
	}

	CInetIO::~CInetIO()
	{
		Close();
	}

	BOOL CInetIO::Open( LPCTSTR lpszUrl )
	{
		if ( lpszUrl == NULL )
		{
			SetLastError( ERROR_INVALID_PARAMETER );
			return FALSE;
		}
	
		DWORD dwError = ERROR_SUCCESS;

		HINTERNET hInternet = InternetOpen(_GetAgentName(), 
			INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
		if ( hInternet == NULL )
		{
			dwError = GetLastError();
			_LogPrint( TEXT("InternetOpen(INTERNET_OPEN_TYPE_PRECONFIG) failed: %d.\n"), dwError );
			return FALSE;
		}
	
		DWORD dwTimeout = 5000; // 5s
		BOOL bRet = InternetSetOptionW( hInternet, INTERNET_OPTION_CONNECT_TIMEOUT, &dwTimeout, sizeof(DWORD) );
		if( !bRet )
		{
			dwError = GetLastError();
			_LogPrint( TEXT("PRECONFIG:InternetSetOptionW(INTERNET_OPTION_CONNECT_TIMEOUT, %ums) failed: %d.\n"), dwTimeout, dwError );
		}

		HINTERNET hFileFrom = NULL;
		do 
		{
			TCHAR szCalonicalizedUrl[INTERNET_MAX_URL_LENGTH];
			DWORD dwUrlSize = INTERNET_MAX_URL_LENGTH;
			if( InternetCanonicalizeUrl( lpszUrl, szCalonicalizedUrl, &dwUrlSize, 0 ) )
			{
				hFileFrom = InternetOpenUrl( hInternet, szCalonicalizedUrl, 
					g_szNoCacheHeaders, lstrlen(g_szNoCacheHeaders),
					INET_OPEN_URL_FLAGS, 0 );
			}
			else
			{
				hFileFrom = InternetOpenUrl( hInternet, lpszUrl,
					g_szNoCacheHeaders, lstrlen(g_szNoCacheHeaders), 
					INET_OPEN_URL_FLAGS, 0 );
			}

			if ( hFileFrom == NULL )
			{
				dwError = GetLastError();
				_LogPrint( TEXT("InternetOpenUrl(%s) failed: %d.\n"), lpszUrl, dwError );
				break;
			}

			TCHAR FilePathName[INTERNET_MAX_PATH_LENGTH] = {0};

			TCHAR DataFileName[MAX_PATH] = {0};
			DWORD dwSize = sizeof(DataFileName);

			// try to get the file name from the option
			BOOL bRet = InternetQueryOption( hFileFrom, INTERNET_OPTION_DATAFILE_NAME, DataFileName, &dwSize );
			if( bRet && dwSize > 0 )
			{
				PathStripPath( DataFileName );
				PathUndecorate( DataFileName );
			}
			else
			{
				// try to determinate the file name from the url path 
				URL_COMPONENTS UrlComponents = {0};
				UrlComponents.dwStructSize = sizeof(URL_COMPONENTS);
				UrlComponents.dwUrlPathLength = 1;
				UrlComponents.dwHostNameLength = 1;
				UrlComponents.dwSchemeLength = 1;
				UrlComponents.dwExtraInfoLength = 1;
				bRet = InternetCrackUrl( lpszUrl,0, 0, &UrlComponents );
				if( !bRet )
				{
					break;
				}

				if( UrlComponents.dwUrlPathLength == 0 )
				{
					dwError = ERROR_INTERNET_INVALID_URL;
					break;
				}

				StringCchCopyN(DataFileName, MAX_PATH, UrlComponents.lpszUrlPath, UrlComponents.dwUrlPathLength );
			}

			// Save the successful results
			m_hFile = hFileFrom;
			m_hInternet = hInternet;

			StringCchCopy( m_szDataFileName, MAX_PATH, DataFileName );

			return TRUE;

		} while( 0 );
		
		if ( dwError == ERROR_SUCCESS )
		{
			dwError = GetLastError();
		}

		// Failed :
		if( hFileFrom != NULL )
		{
			InternetCloseHandle( hFileFrom );
		}

		if( hInternet != NULL )
		{
			InternetCloseHandle( hInternet );
		}

		SetLastError(dwError);

		return FALSE;
	}
	
	LPCTSTR CInetIO::GetDataFileName()
	{
		return m_szDataFileName;
	}

	DWORD CInetIO::GetSize()
	{
		if( m_hFile == NULL )
		{
			throw ERROR_INVALID_HANDLE;
		}

		DWORD dwSize = 0;
		DWORD dwBufferLength = sizeof(dwSize);
		DWORD dwIndex = 0;
		if( HttpQueryInfo(
			m_hFile,
			HTTP_QUERY_CONTENT_LENGTH | HTTP_QUERY_FLAG_NUMBER ,
			&dwSize,
			&dwBufferLength,
			&dwIndex ) )
		{
			return dwSize;
		}

		throw GetLastError();
	}

	DWORD CInetIO::GetPos()
	{
		return Seek( 0, FILE_CURRENT );
	}

	DWORD CInetIO::Seek( LONG lDistanceToMove, DWORD dwMoveMethod )
	{
		if( m_hFile == NULL )
		{
			throw ERROR_INVALID_HANDLE;
		}

		// FROM MSDN: This function cannot be used once the end of the file has been reached by InternetReadFile.
		// 
		DWORD dwRet = InternetSetFilePointer( m_hFile, lDistanceToMove, NULL, dwMoveMethod, 0 );
		if ( dwRet == (DWORD)(-1) )
		{
			DWORD dwError = ::GetLastError();
			if( dwError != NO_ERROR )
				throw dwError;
		}

		return dwRet;
	}

	DWORD CInetIO::Read( void* lpBuffer, DWORD dwSize )
	{
		if( m_hFile == NULL )
		{
			throw ERROR_INVALID_HANDLE;
		}

		DWORD dwNumberBytesRead = 0;
		if( InternetReadFile( m_hFile, lpBuffer, dwSize, &dwNumberBytesRead ) )
		{
			return dwNumberBytesRead;
		}
		
		throw GetLastError();
	}

	DWORD CInetIO::Write( const void* lpBuffer, DWORD dwSize )
	{
		// 不支持写操作，即不支持网络上传
		throw ERROR_INVALID_TARGET_HANDLE;
		
		//if( m_hFile == NULL )
		//{
		//	throw ERROR_INVALID_HANDLE;
		//}

		//DWORD dwNumberBytesWritten = 0;
		//if( InternetWriteFile ( m_hFile, lpBuffer, dwSize, &dwNumberBytesWritten ) )
		//{
		//	return dwNumberBytesWritten;
		//}

		//throw GetLastError();
	}

	void CInetIO::Close()
	{
		if( m_hFile != NULL )
		{
			InternetCloseHandle( m_hFile );
			m_hFile = NULL;
		}

		if ( m_hInternet != NULL )
		{
			InternetCloseHandle( m_hInternet );
			m_hInternet = NULL;
		}

		ZeroMemory( m_szDataFileName, sizeof(m_szDataFileName) );
	}
};