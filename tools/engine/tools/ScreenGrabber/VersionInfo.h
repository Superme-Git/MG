#pragma once
#include <strsafe.h>
#pragma comment(lib, "version.lib")

typedef union
{
	//unsigned __int64 u64Value;
	WORD wVersion[4];
} VERSION_TYPE;

class CVersionInfo
{
public:
	CVersionInfo()
		: m_hMem (NULL)
		, m_pVersionInfoBlock( NULL )
	{

	}

	~CVersionInfo()
	{
		if( m_hMem != NULL )
		{
			GlobalUnlock(m_hMem);
			GlobalFree(m_hMem);
		}
		m_pVersionInfoBlock = NULL;
	}

	BOOL Init(LPCWSTR szPath)
	{
		DWORD dwVerHandle = 0;
		DWORD dwVerInfoSize = GetFileVersionInfoSizeW( szPath, &dwVerHandle );
		if( dwVerInfoSize == 0 ) 
		{
			return FALSE;
		}

		// Read the list of languages and code pages.
		m_hMem = GlobalAlloc(GMEM_MOVEABLE, dwVerInfoSize);
		m_pVersionInfoBlock = GlobalLock(m_hMem);

		BOOL bRet = GetFileVersionInfoW(szPath, dwVerHandle, dwVerInfoSize, m_pVersionInfoBlock );
		if( !bRet )
		{
			GlobalUnlock(m_hMem);
			GlobalFree(m_hMem);
			m_hMem = NULL;
			m_pVersionInfoBlock = NULL;
		}

		return bRet;
	}

	BOOL GetModuleVersions( VERSION_TYPE* pFileVersion, VERSION_TYPE* pProductVersion )
	{
		if( m_pVersionInfoBlock == NULL )
		{
			return FALSE;
		}

		BOOL bRet = FALSE;

		unsigned int uInfoSize = 0;
		VS_FIXEDFILEINFO * pFileInfo = NULL;
		bRet = ::VerQueryValueW(m_pVersionInfoBlock, L"\\", (void**)&pFileInfo, &uInfoSize);
		if( bRet )
		{
			{
				// File version from the FILEVERSION of the version info resource 
				VERSION_TYPE& uVersion = *pFileVersion;
				uVersion.wVersion[0] = HIWORD(pFileInfo->dwFileVersionMS); 
				uVersion.wVersion[1] = LOWORD(pFileInfo->dwFileVersionMS);
				uVersion.wVersion[2] = HIWORD(pFileInfo->dwFileVersionLS);
				uVersion.wVersion[3] = LOWORD(pFileInfo->dwFileVersionLS); 
			}
			{
				// Product version from the FILEVERSION of the version info resource 
				VERSION_TYPE& uVersion = *pProductVersion;
				uVersion.wVersion[0] = HIWORD(pFileInfo->dwProductVersionMS); 
				uVersion.wVersion[1] = LOWORD(pFileInfo->dwProductVersionMS);
				uVersion.wVersion[2] = HIWORD(pFileInfo->dwProductVersionLS);
				uVersion.wVersion[3] = LOWORD(pFileInfo->dwProductVersionLS); 
			}
		}

		return bRet;
	}

	static LPVOID GetFileDescription( LPVOID pVersionInfoBlock, DWORD* pdwChars )
	{
		// Structure used to store enumerated languages and code pages.
		struct LANGANDCODEPAGE
		{
			WORD wLanguage;
			WORD wCodePage;
		} *lpTranslate = NULL;

		UINT cbTranslate = 0;

		if( !VerQueryValueW(pVersionInfoBlock, L"\\VarFileInfo\\Translation",
			(LPVOID*)&lpTranslate, &cbTranslate) )
		{
			return NULL;
		}

		WCHAR SubBlock[50] = {0};
		LPVOID lpBuffer = NULL;
		UINT dwChars = 0;

		// Read the file description for each language and code page.
		for( UINT i = 0; i < (cbTranslate/sizeof(struct LANGANDCODEPAGE)); i++ )
		{
			HRESULT hr = StringCchPrintfW(SubBlock, 50,
				L"\\StringFileInfo\\%04x%04x\\FileDescription",
				lpTranslate[i].wLanguage,
				lpTranslate[i].wCodePage);
			if( hr != 0 )
			{
				// TODO: write error handler.
				continue;
			}

			// Retrieve file description for language and code page "i". 
			if( !VerQueryValueW(pVersionInfoBlock, 
				SubBlock, 
				&lpBuffer, 
				&dwChars) )
			{
				continue;
			}

			break;
		}

		if( pdwChars != NULL )
			*pdwChars = dwChars;

		return lpBuffer;
	}


	BOOL GetFileDescription( LPWSTR lpszDescriptionBuffer, DWORD dwBufferSize )
	{
		BOOL bRet = FALSE;
		DWORD dwChars = 0;
		LPVOID pDescription = GetFileDescription( m_pVersionInfoBlock, &dwChars );
		if( pDescription != NULL && dwChars > 0 )
		{
			HRESULT hr = StringCchCopyNW( lpszDescriptionBuffer, dwBufferSize,
				(STRSAFE_LPCWSTR)pDescription, dwChars );
			bRet = SUCCEEDED(hr);
		}
		return bRet;
	}

protected:
	HANDLE m_hMem;
	LPVOID m_pVersionInfoBlock;
};