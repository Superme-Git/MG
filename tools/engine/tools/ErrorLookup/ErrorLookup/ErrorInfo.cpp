//////////////////////////////////////////////////////////////////////////
//
//
//
#include "stdafx.h"
#include "ErrorInfo.h"

#ifndef  INTERNET_ERROR_BASE
#include <wininet.h>
#endif

namespace EC
{
	static DWORD _FormatErrorMessage( LANGID langId, DWORD dwErrorCode,
		LPWSTR lpBuffer, DWORD nSize, DWORD dwFlags )
	{
		DWORD dwRet = 0;
		if ( dwErrorCode >= INTERNET_ERROR_BASE && dwErrorCode <= INTERNET_ERROR_LAST )
		{
			// 12000 - 12174 internet error code	
			BOOL bNeedFree = FALSE;
			HMODULE hInet = GetModuleHandleW(L"wininet.dll");
			if ( hInet == NULL )
			{
				hInet = LoadLibraryExW( L"./wininet-chs.dll", NULL, LOAD_LIBRARY_AS_DATAFILE );
				if( hInet == NULL )
					hInet = LoadLibraryExW( L"./wininet.dll", NULL, LOAD_LIBRARY_AS_DATAFILE );
				if( hInet == NULL )
					hInet = LoadLibraryExW( L"wininet.dll", NULL, LOAD_LIBRARY_AS_DATAFILE );
				bNeedFree = TRUE;
			}

			if ( hInet != NULL )
			{
				dwRet = ::FormatMessageW(
					FORMAT_MESSAGE_FROM_HMODULE | 
					FORMAT_MESSAGE_IGNORE_INSERTS | dwFlags
					, hInet, 
					dwErrorCode,
					langId, 
					(LPWSTR)lpBuffer, nSize,
					NULL);
				if ( dwRet == 0 && ::GetLastError() == ERROR_RESOURCE_LANG_NOT_FOUND )
				{
					dwRet = ::FormatMessageW(
						FORMAT_MESSAGE_FROM_HMODULE | 
						FORMAT_MESSAGE_IGNORE_INSERTS | dwFlags
						, hInet, 
						dwErrorCode,
						0, //MAKELANG(LANG_NEUTRAL, SUBLANG_NEUTRAL) 
						(LPWSTR)lpBuffer, nSize,
						NULL);
				}

				if ( bNeedFree )
				{
					FreeLibrary( hInet );
				}
			}
		}
		else
		{			
			dwRet = ::FormatMessageW(
				FORMAT_MESSAGE_FROM_SYSTEM | 
				FORMAT_MESSAGE_IGNORE_INSERTS | dwFlags
				, NULL, 
				dwErrorCode, langId, 
				(LPWSTR)lpBuffer, nSize,
				NULL);
			if ( dwRet == 0 && ::GetLastError() == ERROR_RESOURCE_LANG_NOT_FOUND )
			{
				dwRet = ::FormatMessageW(
					FORMAT_MESSAGE_FROM_SYSTEM | 
					FORMAT_MESSAGE_IGNORE_INSERTS | dwFlags
					, NULL, 
					dwErrorCode, 
					0, //MAKELANG(LANG_NEUTRAL, SUBLANG_NEUTRAL)
					(LPWSTR)lpBuffer, nSize,
					NULL);
			}
		}

		return dwRet;
	}

	int GetErrorDescription( LANGID langId, DWORD dwErrorCode,
		LPWSTR lpBuffer, DWORD nSize )
	{
		DWORD dwRet = _FormatErrorMessage( langId, dwErrorCode,
			lpBuffer, nSize, 0 );
		if( dwRet > 0 )
		{
			return (int)dwRet;
		}
		else
		{
			// 内存不足，利用FORMAT_MESSAGE_ALLOCATE_BUFFER计算需要的内存大小
			LPWSTR lpBuffer2 = NULL; 
			dwRet = _FormatErrorMessage( langId, dwErrorCode,
				(LPWSTR)&lpBuffer2, nSize, FORMAT_MESSAGE_ALLOCATE_BUFFER );
			if( dwRet > 0 )
			{
				::LocalFree( lpBuffer2 );
				return -(int)dwRet;
			}

			return 0;
		}
	}

}// namespace EC