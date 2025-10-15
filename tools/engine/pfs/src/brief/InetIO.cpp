//////////////////////////////////////////////////////////////////////////
//
// InetIO.cpp
//
//  [10/27/2008 yanghaibo]
//
//////////////////////////////////////////////////////////////////////////
#include "InetIO.h"

#include "../../pfs2.h" // 为了使用CEnv::GetOutLogInstance()

#include <Shlwapi.h> // for PathIsUrl() etc.
#include <strsafe.h> // StringCchCopyN() and StringCchCopy()
#pragma comment( lib, "wininet.lib")
#pragma comment( lib, "Shlwapi.lib" )

#ifndef _countof
#define _countof(_Array) (sizeof(_Array) / sizeof(_Array[0]))
#endif

namespace PFS
{
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
			::PFS::CEnv::GetOutLogInstance()->Print(L"RegOpenKeyEx(\"%s\") failed: %d.\n", lpszSubKey, lRet );
			return FALSE;
		}

		DWORD dwType = REG_DWORD;
		DWORD dwProxyEnable = 0;
		DWORD dwLength = sizeof(DWORD);
		lRet = RegQueryValueEx( hkResult, TEXT("ProxyEnable"), NULL, &dwType, (LPBYTE)&dwProxyEnable, &dwLength );
		RegCloseKey( hkResult );
		if( lRet != ERROR_SUCCESS )
		{
			::PFS::CEnv::GetOutLogInstance()->Print(L"RegQueryValueExW(\"%s\":ProxyEnable) failed: %d.\n", lpszSubKey, lRet );
			return FALSE;
		}

		return !!dwProxyEnable;
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

	//
	// 格式化http request的range header
	// 返回header的长度
	//
	template<int size>
	int FormatRangeHeader(TCHAR (&szHeadersRange)[size], LONG lRangeBegin, LONG lRangeEnd )
	{
		int lengthOf = 0;
		if( lRangeBegin != 0L )
		{
			if( lRangeEnd != -1L )
			{
				if( lRangeBegin <= lRangeEnd )
				{
					// ensure lRangeBegin <= lRangeEnd
					lengthOf = swprintf_s( szHeadersRange, TEXT("Range:bytes=%ld-%ld\r\n"), lRangeBegin, lRangeEnd );
				}
				else
				{
					// ensure lRangeBegin <= lRangeEnd
					lengthOf = swprintf_s( szHeadersRange, TEXT("Range:bytes=%ld-%ld\r\n"), lRangeEnd, lRangeBegin );
				}
			}
			else
			{
				lengthOf = swprintf_s( szHeadersRange, TEXT("Range:bytes=%ld-\r\n"), lRangeBegin);
			}
		}
		else
		{
			if( lRangeEnd != -1L )
			{
				lengthOf = swprintf_s( szHeadersRange, TEXT("Range:bytes=0-%ld\r\n"), lRangeEnd);
			}
		}

		return lengthOf;
	}

	HINTERNET InetOpenUrl( HINTERNET hInternet, LPCTSTR lpszUrl, LONG lRangeBegin, LONG lRangeEnd = -1L )
	{
		HINTERNET hFile = NULL;

		TCHAR szHeadersBuffer[512] = TEXT("Pragma: no-cache\r\nCache-Control: no-cache\r\n");
		TCHAR szHeadersRange[256] = {0};
		if( lRangeBegin != 0L || lRangeEnd != -1L )
		{
			// 添加range header
			FormatRangeHeader( szHeadersRange, lRangeBegin, lRangeEnd );

			// combine the headers
			wcscat_s(szHeadersBuffer, szHeadersRange);
		}

		int tryCountConnect = 2; // 无法连接情况，重试2次
		int tryCountTimeout = 1; // 超时情况，不再重试，因为连接超时已经在调用前设置好（我们设置的为5秒超时）
		DWORD dwError = 0;
		do 
		{
			hFile = InternetOpenUrl(
				hInternet, lpszUrl,
				szHeadersBuffer, lstrlen(szHeadersBuffer), 
				INTERNET_FLAG_HYPERLINK |
				INTERNET_FLAG_NEED_FILE |
				INTERNET_FLAG_NO_CACHE_WRITE  |
				INTERNET_FLAG_NO_COOKIES |
				INTERNET_FLAG_NO_UI | 
				INTERNET_FLAG_PRAGMA_NOCACHE | // Forces the request to be resolved by the origin server, even if a cached copy exists on the proxy. 
				INTERNET_FLAG_RELOAD |
				INTERNET_FLAG_RESYNCHRONIZE
				, 0 );
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

	HINTERNET InetOpenFile( HINTERNET hConnect, LPCTSTR lpszObjectName, LONG lRangeBegin = 0L, LONG lRangeEnd = -1L )
	{
		HINTERNET hFile = NULL;
		DWORD dwError = 0;
		
		int tryCount = 3;
		do 
		{
			hFile = HttpOpenRequest( hConnect, TEXT("GET"),
				lpszObjectName, HTTP_VERSION, NULL, NULL, 
				INTERNET_FLAG_KEEP_CONNECTION | // Uses keep-alive semantics, if available, for the connection. 
				INTERNET_FLAG_HYPERLINK | // Forces a reload if there was no Expires time and no LastModified time returned from the server when determining whether to reload the item from the network.
				INTERNET_FLAG_NEED_FILE | // Causes a temporary file to be created if the file cannot be cached.
				INTERNET_FLAG_NO_CACHE_WRITE | // Does not add the returned entity to the cache.
				INTERNET_FLAG_NO_COOKIES | // Does not automatically add cookie headers to requests, and does not automatically add returned cookies to the cookie database.
				INTERNET_FLAG_NO_UI |  // Disables the cookie dialog box.
				INTERNET_FLAG_PRAGMA_NOCACHE | // Forces the request to be resolved by the origin server, even if a cached copy exists on the proxy. 
				INTERNET_FLAG_RELOAD | // Forces a download of the requested file, object, or directory listing from the origin server, not from the cache.
				INTERNET_FLAG_RESYNCHRONIZE, // Reloads HTTP resources if the resource has been modified since the last time it was downloaded
				0);
			if ( !hFile )
			{
				dwError = GetLastError();
			}
		} while ( !hFile &&
			(dwError == ERROR_INTERNET_CANNOT_CONNECT || dwError == ERROR_INTERNET_TIMEOUT) &&
			(Sleep(1000), --tryCount) );
		
		tryCount = 3;
		if ( hFile )
		{
			// 强制实行 INTERNET_FLAG_KEEP_CONNECTION 行为
			LPCTSTR lpszHeadersConnectionKeepAlive = TEXT("Connection: Keep-Alive\r\n");
			BOOL bAdded = HttpAddRequestHeaders(hFile,
				lpszHeadersConnectionKeepAlive, lstrlen(lpszHeadersConnectionKeepAlive), 
				HTTP_ADDREQ_FLAG_ADD|HTTP_ADDREQ_FLAG_REPLACE );
			if ( !bAdded )
			{
				dwError = GetLastError();
				::PFS::CEnv::GetOutLogInstance()->Print( 
					L"Warning: HttpAddRequestHeaders(\"Connection: Keep-Alive\\r\\n\") failed(%d)!\n", dwError );
			}

			// 有关cache-control的相关参考资料:
			//
			// http://www.diybl.com/course/4_webprogram/php/phpjs/20090308/159957.html
			//
			// no-cache: 数据内容绝不被储存起来.proxy服务器和浏览器读到此标签,将不会将数据内容存入cache中.
			// no-store: 数据内容除了不能存入cache中之外,亦不能存入暂时的磁盘中,这个标签防止敏感性的数据被复制.
			// must-revalidate:用户在每次读取数据时,会再次和原来的服务器确定是否为最新数据,而不再通过中间的proxy服务器.
			//
			// http://www.i18nguy.com/markup/metatags.html
			//
			// http://blog.csdn.net/pojianbing/archive/2009/08/18/4460333.aspx
			//		Cache-Control: no-cache,Cache-Control: no-store,Pragma: no-cache区别 
			// Cache-Control: no-cache：这个很容易让人产生误解，使人误以为是响应不被缓存。
			//		实际上Cache-Control: no-cache是会被缓存的，只不过每次在向客户端（浏览器）提供响应数据时，
			//		缓存都要向服务器评估缓存响应的有效性。
			// Cache-Control: no-store：
			//		这个才是响应不被缓存的意思。
			// Pragma: no-cache：
			//		跟Cache-Control: no-cache相同，Pragma: no-cache兼容http 1.0，
			//		Cache-Control: no-cache是http 1.1提供的。
			//		因此，Pragma: no-cache可以应用到http 1.0 和http 1.1,
			//		而Cache-Control: no-cache只能应用于http 1.1.
			//

			//
			// 下面两个Request Header用来控制缓存 - 我们不要任何缓存!
			// 因为下面的HTTP_ADDREQ_FLAG_REPLACE flag,所以,这2个Header不能一起添加
			//
			LPCTSTR lpszHeaders1 = TEXT("Pragma: no-cache\r\n");
			bAdded = HttpAddRequestHeaders(hFile,
				lpszHeaders1, lstrlen(lpszHeaders1), 
				HTTP_ADDREQ_FLAG_ADD|HTTP_ADDREQ_FLAG_REPLACE );
			if ( !bAdded )
			{
				dwError = GetLastError();
				::PFS::CEnv::GetOutLogInstance()->Print( 
					L"Warning: HttpAddRequestHeaders(\"Pragma: no-cache\\r\\n\") failed(%d)!\n", dwError );
			}

			LPCTSTR lpszHeaders2 = TEXT("Cache-Control: no-cache\r\n");
			bAdded = HttpAddRequestHeaders(hFile,
				lpszHeaders2, lstrlen(lpszHeaders2), 
				HTTP_ADDREQ_FLAG_ADD|HTTP_ADDREQ_FLAG_REPLACE );
			if ( !bAdded )
			{
				dwError = GetLastError();
				::PFS::CEnv::GetOutLogInstance()->Print( 
					L"Warning: HttpAddRequestHeaders(\"Cache-Control: no-cache\\r\\n\") failed(%d)!\n", dwError );
			}

			if( lRangeBegin != 0L || lRangeEnd != -1L )
			{
				// 添加range header
				TCHAR szHeadersRange[256] = {0};
				FormatRangeHeader( szHeadersRange, lRangeBegin, lRangeEnd );

				bAdded = HttpAddRequestHeaders(hFile,
					szHeadersRange, lstrlen(szHeadersRange), 
					HTTP_ADDREQ_FLAG_ADD|HTTP_ADDREQ_FLAG_REPLACE );
				if ( !bAdded )
				{
					dwError = GetLastError();

					// 这里的 from-to 仅仅反映函数传入的参数，不完全等价于header的内容
					::PFS::CEnv::GetOutLogInstance()->Print( 
						L"Warning: HttpAddRequestHeaders(\"range: bytes=%ld-%ld\\r\\n\") failed(%d)!\n",
						lRangeBegin, lRangeEnd, dwError );
				}
			}

			HINTERNET hFileTemp = hFile;
			hFile = NULL;
			do
			{
				if( !::HttpSendRequest(hFileTemp, NULL, 0, 0, 0) )
				{
					dwError = GetLastError();
				}
				else
				{
					hFile = hFileTemp;
					break;
				}
			} while ( (dwError == ERROR_INTERNET_CANNOT_CONNECT || dwError == ERROR_INTERNET_TIMEOUT) &&
				(Sleep(1000), --tryCount) );
		}

		return hFile;
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

	enum { CodePage = 936 }; // 明确指定URL中的文件名编码为936，参见zipfile.h

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

	// 调试输出网络API错误信息
	static DWORD _TraceInetLastError( LPCTSTR pszContext, LPCTSTR pszUrl)
	{
		DWORD dwError = GetLastError();

		if ( dwError != ERROR_SUCCESS  )
		{
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
					TEXT("%s - url: %s, Http Error: %s(%d)\n"), pszContext, pszUrl, szBuffer, dwHttpError );					
			}
			else
			{
				StringCchPrintf( szBufferFull, _countof(szBufferFull), 
					TEXT("%s - url: %s, System Error: %d\n"), pszContext, pszUrl, dwError );
			}

			::PFS::CEnv::GetOutLogInstance()->Print(szBufferFull);
		}

		return dwError;
	}

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
		return Open( lpszUrl, 0L,  -1L );
	}

	BOOL CInetIO::Open( LPCTSTR _lpszUrl, LONG lRangeBegin, LONG lRangeEnd )
	{
		if ( _lpszUrl == NULL )
		{
			SetLastError( ERROR_INVALID_PARAMETER );
			return FALSE;
		}

		LPCTSTR lpszUrl = _lpszUrl;
		TCHAR szCanonicalizedUrl[INTERNET_MAX_URL_LENGTH];
		DWORD dwUrlSize = INTERNET_MAX_URL_LENGTH;
		if( InternetCanonicalizeUrl( _lpszUrl, szCanonicalizedUrl, &dwUrlSize, 0 ) )
		{
			lpszUrl = szCanonicalizedUrl;
		}

		TCHAR szEncodedUrl[INTERNET_MAX_URL_LENGTH];
		dwUrlSize = INTERNET_MAX_URL_LENGTH;
		if ( URLEncode( lpszUrl, szEncodedUrl, &dwUrlSize ) )
		{
			lpszUrl = szEncodedUrl;
		}

		//
		// 在使用代理的情况下, InetCheckConnection() 会导致无法连接那些仅能通过代理连接的服务器
		// 所以,这里不再进行 InetCheckConnection().
		//

		//if( !InetCheckConnection(lpszUrl) )
		//{
		//	OutputDebugString( TEXT( "Cannot connect to " ) );
		//	OutputDebugString( lpszUrl );
		//	OutputDebugString( TEXT("\n") );
		//	return FALSE;
		//}

		//
		// INTERNET_OPEN_TYPE_DIRECT - 直接连接,不通过代理
		// INTERNET_OPEN_TYPE_PRECONFIG - 使用IE或其他工具对注册表项配置的代理进行连接,如果没有代理配置信息,则直接连接
		//
		HINTERNET hInternet = InternetOpen(_GetAgentName(), 
			INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
		if ( hInternet == NULL )
		{
			_TraceInetLastError( TEXT("InternetOpen(PRECONFIG)"), lpszUrl );
			return FALSE;
		}

		DWORD dwTimeout = 5000; // 5s
		BOOL bRet = InternetSetOptionW( hInternet, INTERNET_OPTION_CONNECT_TIMEOUT, &dwTimeout, sizeof(DWORD) );
		if( !bRet )
		{
			_TraceInetLastError( TEXT("PRECONFIG:InternetSetOptionW(INTERNET_OPTION_CONNECT_TIMEOUT)"), lpszUrl );
		}

		DWORD dwError = ERROR_SUCCESS;

		HINTERNET hFileFrom = NULL;
		do 
		{
			hFileFrom = InetOpenUrl( hInternet, lpszUrl, lRangeBegin, lRangeEnd );
			if ( hFileFrom == NULL )
			{
				dwError = _TraceInetLastError( TEXT("InetOpenUrl"), lpszUrl );
				
				// 关闭PRECONFIG模式的hInternet句柄
				InternetCloseHandle( hInternet );

				// 检查代理设置是否启用，如果没有启用，放弃直连模式的重试
				// 因为这种情况下，INTERNET_OPEN_TYPE_PRECONFIG就是直连。
				if( !IsProxyEnabled( ) )
				{
					break;
				}

				::PFS::CEnv::GetOutLogInstance()->Print(L"Proxy enabled, but can't connect through proxy server. Try again bypassing proxy settings.\n");

				// 再次尝试直连模式
				hInternet = InternetOpen(_GetAgentName(), 
					INTERNET_OPEN_TYPE_DIRECT, NULL, NULL, 0);
				if ( hInternet == NULL )
				{
					_TraceInetLastError( TEXT("InternetOpen(DIRECT)"), lpszUrl );
					return FALSE;
				}

				BOOL bRet = InternetSetOptionW( hInternet, INTERNET_OPTION_CONNECT_TIMEOUT, &dwTimeout, sizeof(DWORD) );
				if( !bRet )
				{
					_TraceInetLastError( TEXT("DIRECT:InternetSetOptionW(INTERNET_OPTION_CONNECT_TIMEOUT)"), lpszUrl );
				}

				hFileFrom = InetOpenUrl( hInternet, lpszUrl, lRangeBegin, lRangeEnd );
				if ( hFileFrom == NULL )
				{
					// 如果直连模式仍旧失败,只好放弃
					dwError = _TraceInetLastError( TEXT("InetOpenUrl"), lpszUrl );
					break;
				}
			}

			// Get the http StatusCode
			DWORD dwStatusCode = 0;
			DWORD dwStatusCodeSize = sizeof(dwStatusCode);
			BOOL bRet = HttpQueryInfo( hFileFrom, 
				HTTP_QUERY_STATUS_CODE|HTTP_QUERY_FLAG_NUMBER, 
				&dwStatusCode, &dwStatusCodeSize, 0 );
			if ( !bRet || dwStatusCode != HTTP_STATUS_OK && dwStatusCode != HTTP_STATUS_CONTINUE &&
				dwStatusCode != HTTP_STATUS_PARTIAL_CONTENT)
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
						dwError = ERROR_HTTP_INVALID_SERVER_RESPONSE;
					}
				}

				break;
			}

			TCHAR FilePathName[INTERNET_MAX_PATH_LENGTH] = {0};
			TCHAR DataFileName[MAX_PATH] = {0};
			DWORD dwSize = sizeof(DataFileName);

			// try to get the file name from the option

			do
			{
				// try to determinate the file name from the url path 
				URL_COMPONENTS UrlComponents = {0};
				UrlComponents.dwStructSize = sizeof(URL_COMPONENTS);
				UrlComponents.dwUrlPathLength = 1;
				UrlComponents.dwHostNameLength = 1;
				UrlComponents.dwSchemeLength = 1;
				UrlComponents.dwExtraInfoLength = 1;
				bRet = InternetCrackUrl( _lpszUrl,0, 0, &UrlComponents );
				if ( bRet && UrlComponents.dwUrlPathLength > 0 )
				{

					StringCchCopyN(FilePathName, MAX_PATH, UrlComponents.lpszUrlPath, UrlComponents.dwUrlPathLength );
					URLDecode(FilePathName, DataFileName, &dwSize);
				}
				else
				{
					if( bRet )
					{
						dwError = ERROR_INTERNET_INVALID_URL;
						bRet = FALSE;
					}
					break;// break to the outer loop, to try another solution
				}

			}while(0);

			if ( !bRet )
			{
				bRet = InternetQueryOption( hFileFrom, INTERNET_OPTION_DATAFILE_NAME, FilePathName, &dwSize );
				if( bRet && dwSize > 0 )
				{
					PathStripPath( FilePathName );
					PathUndecorate( FilePathName );
					// 因为URLEncode的原因，这里的文件名已经被Encode,所以这里需要执行Decode
					// 但是，这种方法无法取得DataFile的完整路径.
					// 比如打开www.sina.com.cn,crack url方式无法得到url中含有的文件名，
					// 这里需要使用IE缓存中生成的编码后的文件名.
					URLDecode(FilePathName, DataFileName, &dwSize);
				}
				else
				{
					if( bRet )
					{
						dwError = ERROR_INTERNET_INVALID_URL;
						bRet = FALSE;
					}
					break;
				}
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

	BOOL CInetIO::Open( HINTERNET hConnect, LPCTSTR lpszObjectName )
	{
		return Open( hConnect, lpszObjectName, 0L, -1L );
	}

	BOOL CInetIO::Open( HINTERNET hConnect, LPCTSTR _lpszObjectName, LONG lRangeBegin, LONG lRangeEnd /*= -1L*/ )
	{
		if ( _lpszObjectName == NULL )
		{
			SetLastError( ERROR_INVALID_PARAMETER );
			return FALSE;
		}

		LPCTSTR lpszUrl = _lpszObjectName;
		TCHAR szCanonicalizedUrl[INTERNET_MAX_URL_LENGTH];
		DWORD dwUrlSize = INTERNET_MAX_URL_LENGTH;
		if( InternetCanonicalizeUrl( _lpszObjectName, szCanonicalizedUrl, &dwUrlSize, 0 ) )
		{
			lpszUrl = szCanonicalizedUrl;
		}

		TCHAR szEncodedUrl[INTERNET_MAX_URL_LENGTH];
		dwUrlSize = INTERNET_MAX_URL_LENGTH;
		if ( URLEncode( lpszUrl, szEncodedUrl, &dwUrlSize ) )
		{
			lpszUrl = szEncodedUrl;
		}

		DWORD dwError = ERROR_SUCCESS;

		HINTERNET hFileFrom = NULL;
		do 
		{
			hFileFrom = InetOpenFile( hConnect, lpszUrl, lRangeBegin, lRangeEnd );
			if ( hFileFrom == NULL )
			{
				dwError = _TraceInetLastError( TEXT("InetOpenFile"), lpszUrl );
				break;
			}

			// Get the http StatusCode
			DWORD dwStatusCode = 0;
			DWORD dwStatusCodeSize = sizeof(dwStatusCode);
			BOOL bRet = HttpQueryInfo( hFileFrom, 
				HTTP_QUERY_STATUS_CODE|HTTP_QUERY_FLAG_NUMBER, 
				&dwStatusCode, &dwStatusCodeSize, 0 );
			if ( !bRet || dwStatusCode != HTTP_STATUS_OK && dwStatusCode != HTTP_STATUS_CONTINUE &&
				dwStatusCode != HTTP_STATUS_PARTIAL_CONTENT)
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
					else
					{
						dwError = _TraceInetLastError( TEXT("HttpQueryInfo"), lpszUrl );
						PFS::CEnv::GetOutLogInstance()->Print( TEXT("HttpQueryInfo(%s): StatusCode(%d)\n"), 
							lpszUrl, dwStatusCode );
						if( dwError == ERROR_SUCCESS )
						{
							dwError = ERROR_HTTP_INVALID_SERVER_RESPONSE;
						}
					}
				}

				break;
			}

			// try to get the file name from the option
			if( _lpszObjectName == NULL || _lpszObjectName[0] == 0 )
			{
				TCHAR FilePathName[INTERNET_MAX_PATH_LENGTH] = {0};
				TCHAR DataFileName[MAX_PATH] = {0};
				DWORD dwSize = sizeof(DataFileName);

				bRet = InternetQueryOption( hFileFrom, INTERNET_OPTION_DATAFILE_NAME, FilePathName, &dwSize );
				if( bRet && dwSize > 0 )
				{
					PathStripPath( FilePathName );
					PathUndecorate( FilePathName );
					// 因为URLEncode的原因，这里的文件名已经被Encode,所以这里需要执行Decode
					// 但是，这种方法无法取得DataFile的完整路径.
					// 比如打开www.sina.com.cn,crack url方式无法得到url中含有的文件名，
					// 这里需要使用IE缓存中生成的编码后的文件名.
					URLDecode(FilePathName, DataFileName, &dwSize);

					StringCchCopy( m_szDataFileName, MAX_PATH, DataFileName );
				}
				else
				{
					// TODO:
				}
			}
			else
			{
				StringCchCopy( m_szDataFileName, MAX_PATH, _lpszObjectName );
			}

			// Save the successful results
			m_hFile = hFileFrom;
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
			throw CInetIOException(ERROR_INVALID_HANDLE);
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
		
		throw CInetIOException(GetLastError());
	}

	DWORD CInetIO::GetPos()
	{
		return Seek( 0, FILE_CURRENT );
	}

	DWORD CInetIO::Seek( LONG lDistanceToMove, DWORD dwMoveMethod )
	{
		if( m_hFile == NULL )
		{
			throw CInetIOException(ERROR_INVALID_HANDLE);
		}

		// FROM MSDN: This function cannot be used once the end of the file has been reached by InternetReadFile.
		// 
		DWORD dwRet = InternetSetFilePointer( m_hFile, lDistanceToMove, NULL, dwMoveMethod, 0 );
		if ( dwRet == (DWORD)(-1) )
		{
			DWORD dwError = _TraceInetLastError( TEXT("InternetSetFilePointer"), m_szDataFileName );
			if( dwError != NO_ERROR )
				throw CInetIOException(dwError);
		}

		return dwRet;
	}

	DWORD CInetIO::Read( void* lpBuffer, DWORD dwSize )
	{
		if( m_hFile == NULL )
		{
			throw CInetIOException(ERROR_INVALID_HANDLE);
		}

		// 说明:
		// 下面分段下载网络文件的方案源于已发现的InternetReadFile()的一个问题.
		// 当dwSize大约为64M+40K大小时,将导致InternetReadFile()返回FALSE,
		// 调用GetLastError()返回8(存储空间不足，无法处理此命令。)
		// 尚未找到此错误的真实原因.但这种分段下载数据的方案肯定能够解决问题.
		// 现在采用的段大小为1M.
		//  [4/16/2009 yanghaibo]
		DWORD dwNumberBytesToRead = min(1<<20, dwSize); // 每次请求的数据量
		DWORD dwNumberBytesTotalRead = 0;	// 总的读取量
		DWORD dwNumberBytesRead = 0;		// 每次实际读取的数据量
		int loops = (dwSize >> 20) + 1;		// 需要循环的次数
		BYTE * pBuffer = (BYTE *)lpBuffer;	// 每次循环需要写入的缓冲区首地址.
		BOOL bRet = FALSE;					// 返回值
		do 
		{
			if( loops == 1 )
			{
				// 最后需要读取的一段数据(last segment)
				dwNumberBytesToRead = dwSize - dwNumberBytesTotalRead;
			}

			bRet = InternetReadFile( m_hFile, pBuffer, dwNumberBytesToRead, &dwNumberBytesRead );
			if ( bRet && dwNumberBytesRead > 0 )
			{
				dwNumberBytesTotalRead += dwNumberBytesRead;
				pBuffer += dwNumberBytesRead;
			}
			else
			{
				break; // failed or no data read.
			}
		} while ( --loops > 0  );

		if( !bRet )
		{
			DWORD dwError = _TraceInetLastError( TEXT("InternetReadFile"), m_szDataFileName );
			if( dwError != NO_ERROR )
			{
				throw CInetIOException(dwError);
			}
		}
		
		// Note: dwNumberBytesTotalRead may be less than dwSize.

		return dwNumberBytesTotalRead;
	}

	DWORD CInetIO::Write( const void* lpBuffer, DWORD dwSize )
	{
		// 不支持写操作，即不支持网络上传
		throw CInetIOException(ERROR_INVALID_HANDLE);

		//if( m_hFile == NULL )
		//{
		//	throw CInetIOException(ERROR_INVALID_HANDLE);
		//}

		//DWORD dwNumberBytesWritten = 0;
		//if( InternetWriteFile ( m_hFile, lpBuffer, dwSize, &dwNumberBytesWritten ) )
		//{
		//	return dwNumberBytesWritten;
		//}
		//throw CInetIOException(GetLastError());
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

}; // namespace PFS