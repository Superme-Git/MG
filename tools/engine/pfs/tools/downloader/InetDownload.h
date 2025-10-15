#pragma once
#include <wininet.h>
#include <string>

DWORD Connect(std::wstring rooturl, std::wstring& strbaseUrl, std::pair<HINTERNET, HINTERNET>& rHandles);

void Disconnect( std::pair<HINTERNET, HINTERNET>& rHandles );

bool Download( HINTERNET hConnect, std::wstring objectName, std::wstring saveTo );



// œ¬‘ÿœﬂ≥Ã
class CDownloadThread : public CWinThread
{
	DECLARE_DYNCREATE(CDownloadThread)

	CDownloadThread()
	{
	}

public:
	void SetRootUrl( LPCWSTR lpszRootUrl )
	{
		m_strRootUrl.assign( lpszRootUrl  );
	}

	LPCWSTR GetRootUrl( ) const
	{
		return m_strRootUrl.c_str();
	}

	wstringlist& GetFileNameList()
	{
		return m_strFileList;
	}

	const wstringlist& GetFileNameList( ) const
	{
		return m_strFileList;
	}

	void SetSaveTo( LPCWSTR lpszSaveTo )
	{
		m_strSaveTo.assign( lpszSaveTo );
	}

	LPCWSTR GetSaveTo( ) const
	{
		return m_strSaveTo.c_str();
	}

	// thread initialization
	virtual BOOL InitInstance();

	// running and idle processing
	virtual int Run();

	void Stop( BOOL bWait = FALSE );

public:
	std::wstring m_strRootUrl;
	std::wstring m_strBaseUrl, m_strSaveTo;

	wstringlist m_strFileList;

	volatile BOOL m_bStop;
};
