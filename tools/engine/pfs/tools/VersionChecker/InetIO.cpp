//////////////////////////////////////////////////////////////////////////
//
// InetIO.cpp
//
//  [10/27/2008 yanghaibo]
//
//////////////////////////////////////////////////////////////////////////
#include "stdafx.h"
#include "InetIO.h"

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

	HINTERNET InetOpenUrl( HINTERNET hInternet, LPCTSTR lpszUrl )
	{
		HINTERNET hFile = NULL;

		DWORD dwError = 0;
		hFile = InternetOpenUrl( hInternet, lpszUrl, NULL, 0, 
			INTERNET_FLAG_RELOAD |
			INTERNET_FLAG_NO_UI | 
			INTERNET_FLAG_RAW_DATA |
			INTERNET_FLAG_DONT_CACHE  |
			INTERNET_FLAG_NEED_FILE 
			, 0 );
		if ( !hFile )
		{
			dwError = GetLastError();
		}

		return hFile;
	}

	HINTERNET InetOpenFile( HINTERNET hConnect, LPCTSTR lpszObjectName )
	{
		HINTERNET hFile = NULL;
		DWORD dwError = 0;
		
		int tryCount = 3;
		do 
		{
			hFile = HttpOpenRequest( hConnect, TEXT("GET"),
				lpszObjectName, HTTP_VERSION, NULL, NULL, 
				INTERNET_FLAG_KEEP_CONNECTION |
				INTERNET_FLAG_NO_COOKIES |
				INTERNET_FLAG_RELOAD |
				INTERNET_FLAG_NO_UI | 
				INTERNET_FLAG_RAW_DATA |
				INTERNET_FLAG_DONT_CACHE ,
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

		unsigned char * pUTF8 = new unsigned char[ nBufSize ];

		int iLength = WideCharToMultiByte( CodePage, 0, lpwszURL, -1,
			(LPSTR)pUTF8, nBufSize, 0, FALSE);

		iLength --;

		DWORD lBufferLen = *pdwResult;
		LPWSTR lpszDestPtr = lpszDest;
		DWORD lResultLen = 0;
		BOOL bRet = TRUE;
		for (int i = 0; i < iLength; i ++ )
		{
			if (pUTF8[i] <= 0x7f)
			{
				if ( lResultLen < lBufferLen )
				{
					*lpszDestPtr ++ = (WCHAR)pUTF8[i];
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
					StringCchPrintf(lpszDestPtr, 4,  L"%%%02X", pUTF8[i] );
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

		delete[] pUTF8;

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

			OutputDebugString(szBufferFull);
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

	BOOL CInetIO::Open( LPCTSTR _lpszUrl )
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

		HINTERNET hInternet = InternetOpen(_GetAgentName(), 
			INTERNET_OPEN_TYPE_PRECONFIG, NULL, NULL, 0);
		if ( hInternet == NULL )
		{
			return FALSE;
		}

		DWORD dwTimeout = 10000;
		InternetSetOptionW( hInternet, INTERNET_OPTION_CONNECT_TIMEOUT, &dwTimeout, sizeof(DWORD) );

		DWORD dwError = ERROR_SUCCESS;
		DWORD dwStatusCode = 0;

		WCHAR FromBuffer[MAX_RAW_HEADER_BUFFER_LENGTH + 1] = {0};
		DWORD dwCbLength = MAX_RAW_HEADER_BUFFER_LENGTH * 2 + 2;

		HINTERNET hFileFrom = NULL;
		do 
		{
			hFileFrom = InetOpenUrl( hInternet, lpszUrl );
			if ( hFileFrom == NULL )
			{
				dwError = _TraceInetLastError( TEXT("InetOpenUrl"), lpszUrl );
				
				break;
			}
			else
			{
				dwError = ::GetLastError();				
			}

			// Get the http StatusCode
			DWORD dwStatusCodeSize = sizeof(dwStatusCode);
			BOOL bRet = HttpQueryInfo( hFileFrom, 
				HTTP_QUERY_STATUS_CODE|HTTP_QUERY_FLAG_NUMBER, 
				&dwStatusCode, &dwStatusCodeSize, 0 );
			if ( !bRet || dwStatusCode != HTTP_STATUS_OK && dwStatusCode != HTTP_STATUS_CONTINUE )
			{
				if( dwError == 0 )
				{
					dwError = ::GetLastError();
					if( dwError == 0 )
						dwError = ERROR_INTERNET_EXTENDED_ERROR;
				}

				BOOL bRet2 = 0;
				bRet2 = HttpQueryInfo( hFileFrom, 
					HTTP_QUERY_RAW_HEADERS_CRLF,
					FromBuffer, &dwCbLength, 0 );
				//if ( bRet2 && dwCbLength != 0 )
				//{
				//	OutputDebugStringW( FromBuffer );
				//}

				//if( bRet ) // Get the StatusCode OK!
				//{
				//	//// Map the StatusCode to system error code
				//	//if( dwStatusCode == HTTP_STATUS_NOT_FOUND )
				//	//{
				//	//	dwError = ERROR_INTERNET_ITEM_NOT_FOUND;
				//	//}
				//	//else if( dwStatusCode == HTTP_STATUS_DENIED )
				//	//{
				//	//	dwError = ERROR_INTERNET_NO_DIRECT_ACCESS;
				//	//}
				//	//else if ( dwStatusCode == HTTP_STATUS_URI_TOO_LONG )
				//	//{
				//	//	dwError = ERROR_INTERNET_INVALID_URL;
				//	//}
				//	//else
				//	//{
				//	//	dwError = dwStatusCode; // ERROR_HTTP_INVALID_SERVER_RESPONSE;
				//	//}
				//}

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
		if ( dwError == ERROR_INTERNET_EXTENDED_ERROR )
		{
			if( FromBuffer[0] != NULL && dwCbLength != 0 )
			{
				throw CInetIOException(dwError, dwStatusCode, FromBuffer );
			}
			else
			{
				throw CInetIOException(dwError, dwStatusCode, NULL );
			}
		}

		return FALSE;
	}

	BOOL CInetIO::Open( HINTERNET hConnect, LPCTSTR _lpszObjectName )
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
			hFileFrom = InetOpenFile( hConnect, lpszUrl );
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
					else
					{
						dwError = dwStatusCode;// ERROR_HTTP_INVALID_SERVER_RESPONSE;
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