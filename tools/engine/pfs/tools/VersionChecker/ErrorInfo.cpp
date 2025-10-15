//////////////////////////////////////////////////////////////////////////
//
//
//
#include "stdafx.h"
#include "ErrorInfo.h"
#include "KHLogger.h"
#include <algorithm>

#ifndef  INTERNET_ERROR_BASE
#include <wininet.h>
#endif

namespace upd
{

void SetLastError( DWORD dwErrorCode )
{
	ErrorInfo::Instance().ErrorCode = dwErrorCode;
	if ( (long)dwErrorCode > 0 )
	{
		std::wstring temp;
		FormatErrorMessage(dwErrorCode, &temp);
		if ( !temp.empty() )
		{
			std::wstring::iterator iBegin = temp.begin();
			std::wstring::iterator iLast = temp.end();
			iLast = std::remove( iBegin, iLast, L'\n' );
			iLast = std::remove( iBegin, iLast, L'\r' );
			//iLast = std::remove( iBegin, iLast, L'.' );
			temp.resize( iLast - iBegin );
		}

		ErrorInfo::Instance().ErrorDetail = temp;
	}
}

void __cdecl FormatErrorMessage( DWORD dwErrorCode, std::wstring* pMessage, ... )
{
	va_list vl;
	va_start(vl, pMessage);

	LPWSTR lpBuffer = NULL;

	LANGID langId = GetUserDefaultUILanguage();
	// 12000 - 12174 internet error code
	if ( dwErrorCode >= INTERNET_ERROR_BASE && dwErrorCode <= INTERNET_ERROR_LAST )
	{
		BOOL bNeedFree = FALSE;
		HMODULE hInet = GetModuleHandleW(L"wininet.dll");
		if ( hInet == NULL )
		{
			hInet = LoadLibraryExW( L"wininet.dll", NULL, LOAD_LIBRARY_AS_DATAFILE );
			bNeedFree = TRUE;
		}

		if ( hInet != NULL )
		{
			DWORD dwRet = ::FormatMessageW(
				FORMAT_MESSAGE_FROM_HMODULE | 
				FORMAT_MESSAGE_ALLOCATE_BUFFER | 
				FORMAT_MESSAGE_IGNORE_INSERTS
				, hInet, 
				dwErrorCode,
				langId, 
				(LPWSTR)&lpBuffer, 0, &vl);
			if ( dwRet == 0 && ::GetLastError() == ERROR_RESOURCE_LANG_NOT_FOUND )
			{
				::FormatMessageW(
					FORMAT_MESSAGE_FROM_HMODULE | 
					FORMAT_MESSAGE_ALLOCATE_BUFFER | 
					FORMAT_MESSAGE_IGNORE_INSERTS
					, hInet, 
					dwErrorCode,
					0, 
					(LPWSTR)&lpBuffer, 0, &vl);
			}

			if ( bNeedFree )
			{
				FreeLibrary( hInet );
			}
		}
	}
	else
	{			
		DWORD dwRet = ::FormatMessageW(
			FORMAT_MESSAGE_FROM_SYSTEM | 
			FORMAT_MESSAGE_ALLOCATE_BUFFER | 
			FORMAT_MESSAGE_IGNORE_INSERTS
			, NULL, 
			dwErrorCode, langId, (LPWSTR)&lpBuffer, 0, &vl);
		if ( dwRet == 0 && ::GetLastError() == ERROR_RESOURCE_LANG_NOT_FOUND )
		{
			::FormatMessageW(
				FORMAT_MESSAGE_FROM_SYSTEM | 
				FORMAT_MESSAGE_ALLOCATE_BUFFER | 
				FORMAT_MESSAGE_IGNORE_INSERTS
				, NULL, 
				dwErrorCode, 0, (LPWSTR)&lpBuffer, 0, &vl);
		}
	}

	if( lpBuffer != NULL )
	{
		*pMessage = lpBuffer;
		LocalFree(lpBuffer);
	}

	va_end(vl);
}

void SetLastError( DWORD dwErrorCode, const std::wstring& strDetail )
{
	ErrorInfo::Instance().ErrorCode = dwErrorCode;
	ErrorInfo::Instance().ErrorDetail = strDetail;

	KHMsgLog_InfoW( L"%s£¬´íÎóºÅ(%d)\n", strDetail.c_str(), dwErrorCode);
}

}// namespace upd