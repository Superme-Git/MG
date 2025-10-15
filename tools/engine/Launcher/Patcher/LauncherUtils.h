/////////////////////////////////////////////////////////////////////////
//
// LauncherUtils.h
//
// author : yanghaibo
//

#pragma once

#define WM_UPDATE_START				(WM_USER+1011)
#define WM_UPDATE_STEP				(WM_USER+1012)
#define WM_UPDATE_END				(WM_USER+1013)

#define WM_CHECK_START				(WM_USER+1021)
#define WM_CHECK_STEP				(WM_USER+1022)
#define WM_CHECK_END				(WM_USER+1023)

// Launcher self update result ( LPARAM: 0 - no update launcher 1: launcher updated)
#define WM_LAUNCHER_UPDATE_RESULT	(WM_USER+2000)
#define WM_DOWNLOADWEBPAGE_END		(WM_USER+2001)
#define WM_DEPLOYWEBPAGE_END		(WM_USER+2002)

enum
{
	HASNEWVERSION	= 0x00000001,
	LOCALVALID		= 0x00000002,
	AUTOPATCH		= 0x00000004, // ��Ҫ�л���AUTOPATCH
	MERGECACHE		= 0x00000008, // ��Ҫ����ִ��MERGECACHE
};

// ����webҳ��Դ�߳�
class CDownloadWebPageThread : public CWinThread
{
	DECLARE_DYNCREATE(CDownloadWebPageThread)

	CDownloadWebPageThread()
	{
		// Default, do auto-deploy.
		m_bAutoDeploy = TRUE;
	}

protected:
	CString m_strResourcePath; // ������URL�����ص�WebPage Resource.zip
	BOOL m_bAutoDeploy; // �Զ�����

	BOOL DownloadOnlineResourceZip();

public:
	void SetAutoDeploy(BOOL bAutoDeploy)
	{
		m_bAutoDeploy = bAutoDeploy;
	}

	BOOL GetAutoDeploy() const
	{
		return m_bAutoDeploy;
	}

	CString GetResourcePath() const
	{ 
		return m_strResourcePath;
	}

	// Deploy web resource.zip after download successfully.
	BOOL Deploy();

	static BOOL DeployWebPageResource( CString strResourcePath );

	// thread initialization
	virtual BOOL InitInstance();

	// running and idle processing
	virtual int Run();
};

//
// ��ʽ��ϵͳAPI�����Ĵ�����Ϣ
//
void __cdecl FormatErrorMessage( DWORD dwErrorCode, CString* pMessage, ... );


//
// ���������Ϣ
//
void __cdecl ReportMessage( LPCTSTR lpszMessageFormat, ... );
