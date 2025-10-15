// PreviewerDlg.cpp : implementation file
//

#include "stdafx.h"
#include "Previewer.h"
#include "PreviewerDlg.h"
#include "LauncherUtils.h"
#include "LauncherConfig.h"
#include "TextManager.h"
#include "TextID.h"
#include <wininet.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#define WM_KICKIDLE         0x036A 
static BOOL _PumpMessage()
{
	_AFX_THREAD_STATE *pState = AfxGetThreadState();

	// 注：本函数仿照AfxPumpMessage()实现，但此处需用PeekMessage而不能使用GetMessage()
	if (!::PeekMessage(&(pState->m_msgCur), NULL, NULL, NULL, PM_REMOVE))
	{
		return FALSE;
	}

	// process this message
	if (pState->m_msgCur.message != WM_KICKIDLE && !AfxPreTranslateMessage(&(pState->m_msgCur)))
	{
		::TranslateMessage(&(pState->m_msgCur));
		::DispatchMessage(&(pState->m_msgCur));
	}

	return TRUE;
}

static void WaitThreadUntilQuit( HANDLE& rhThread )
{
	if ( rhThread != INVALID_HANDLE_VALUE )
	{
		DWORD dwRet = 0;
		DWORD dwExitCode = 0;
		BOOL bRet = TRUE;
		bRet = GetExitCodeThread( rhThread, &dwExitCode );
		if ( bRet && dwExitCode == STILL_ACTIVE )
		{
			int iTryCount = 120;
			do 
			{
				dwRet = WaitForSingleObject(rhThread,1000);
				_PumpMessage();

			} while ( dwRet == WAIT_TIMEOUT && iTryCount-- > 0 );
			
			if( dwRet == WAIT_TIMEOUT )
			{
				// 等待超时，只好强行终止线程，一般会导致内存泄露
				TerminateThread( rhThread, -1 );
			}
			else if( dwRet == WAIT_OBJECT_0 )
			{
				// 线程正常终止！
			}
			else// if ( dwRet == WAIT_FAILED )
			{
				// 其它错误
			}
		}

		CloseHandle( rhThread );
		rhThread = INVALID_HANDLE_VALUE;
	}
}


// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

// Implementation
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()

// CPreviewerDlg dialog
CPreviewerDlg::CPreviewerDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CPreviewerDlg::IDD, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);

	m_hIconLarge = NULL;
	m_hIconSmall = NULL;
	m_pThisWatcher = NULL;
	m_hBKBrush = NULL;
	m_globalStatusControlColor = 0;
	m_stepMessageControlColor = 0;


	m_pDownloadOnlineResourceThread = NULL;
}

CPreviewerDlg::~CPreviewerDlg()
{
	if( m_hBKBrush != NULL )
	{
		DeleteObject( m_hBKBrush );
		m_hBKBrush = NULL;
	}
}

void CPreviewerDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CPreviewerDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_WM_CREATE()
	ON_BN_CLICKED(IDC_DOWNLOAD_LAUNCHER_ZIP, &CPreviewerDlg::OnBnClickedDownloadLauncherZip)
	ON_BN_CLICKED(IDC_UNZIP_LOCAL_ZIP, &CPreviewerDlg::OnBnClickedUnzipLocalZip)
	ON_MESSAGE( WM_DOWNLOADWEBPAGE_END, OnDownloadWebPageEnd)
	ON_MESSAGE( WM_DEPLOYWEBPAGE_END, OnDeployWebPageEnd)
	ON_BN_CLICKED(ID_SHOW_OFFLINE_WEBPAGE, &CPreviewerDlg::OnBnClickedShowOfflineWebpage)
	ON_BN_CLICKED(ID_SHOW_ONLINE_WEBPAGE, &CPreviewerDlg::OnBnClickedShowOnlineWebpage)
	ON_BN_CLICKED(ID_SHOW_BLANK_WEBPAGE, &CPreviewerDlg::OnBnClickedShowBlankWebpage)
	ON_NOTIFY_EX( TTN_NEEDTEXT, 0, &CPreviewerDlg::OnToolTipNotify)
END_MESSAGE_MAP()


// CPreviewerDlg message handlers

void CPreviewerDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CPreviewerDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CPreviewerDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

int CPreviewerDlg::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CDialog::OnCreate(lpCreateStruct) == -1)
		return -1;

	EnableToolTips();

	//////////////////////////////////////////////////////////////////////////
	//
	// 创建浏览器控件
	//
	CRect rcBrowser(0,0,1,1);
	m_htmlView = (CWebBrowserCtrl*)RUNTIME_CLASS(CWebBrowserCtrl)->CreateObject();
	
	// 检查版本线程在检查完版本信息之后，通知新的版本信息
	//PFSX::CVersionInfo remoteVersion;
	//PFSX::String2Version(L"1.0.0", remoteVersion.m_Version );
	//remoteVersion.m_bValid = true;
	//
	//tm when = {0};
	//_localtime32_s( &when, &remoteVersion.m_ReleaseDate );
	//m_htmlView->SetServerVersionInfo( remoteVersion );


	if( !m_htmlView->Create( NULL, NULL, WS_CHILD | WS_VISIBLE, rcBrowser, this, NULL ) )
	{
		ReportMessage( GlobalGetText( ERR_CREATE_HTMLVIEW).c_str());
		return -1;
	}

	if( !m_layoutManager.Load( GetLauncherUIResourceDir(), GetLauncherUILayoutFile(), L"Launcher" ) )
	{
		CString strMessage;
		strMessage = L"Usage:\n\n"
			L"Put this program on the same directory as the inetutils.dll.\n\n"
			L"In this directory, it should contain an launcher sub-folder.\n\n"
			L"In the launcher folder, there should contain a file named as config.xml,\n"
			L"and other folder like \"ui\" and \"text\".";

		ReportMessage( strMessage );
		return -1;
	}

	return 0;
}

BOOL CPreviewerDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// 
	// WebBrowser定位并初始化
	//
	CRect rcBrowser;
	if( !m_layoutManager.GetControlRect( _T("WebControl"), rcBrowser ) )
	{
		HWND hStatic = ::GetDlgItem( GetSafeHwnd(), IDC_WEBBROWSER2 );
		::GetWindowRect( hStatic , &rcBrowser );
		ScreenToClient( &rcBrowser );
	}
	
	HWND hWebControl = ::GetDlgItem(*this, IDC_WEB_CONTROL);
	RECT rcWebControl;
	::GetWindowRect(hWebControl, &rcWebControl);
	::DestroyWindow( hWebControl );
	
	ScreenToClient( &rcWebControl );
	rcBrowser.MoveToXY( rcWebControl.left, rcWebControl.top );
	
	m_htmlView->MoveWindow( &rcBrowser, TRUE );
	
	m_htmlView->Navigate2( GetLauncherOfflineUrl() );
	CheckRadioButton(ID_SHOW_OFFLINE_WEBPAGE, ID_SHOW_BLANK_WEBPAGE, ID_SHOW_OFFLINE_WEBPAGE);
	
	
	SetDlgItemText( IDC_LAUNCHER_ZIP_URL, GetLauncherOnlineResourceUrl() );
	SetDlgItemText( IDC_LAUNCHER_OFFLINE_URL, GetLauncherOfflineUrl() );
	SetDlgItemText( IDC_LAUNCHER_ONLINE_URL, GetLauncherOnlineUrl() );

	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CPreviewerDlg::OnBnClickedDownloadLauncherZip()
{
	HWND hButton = ::GetDlgItem(*this,IDC_UNZIP_LOCAL_ZIP);
	::EnableWindow(hButton,FALSE); 

	HWND hButton2 = ::GetDlgItem(*this,IDC_DOWNLOAD_LAUNCHER_ZIP);
	::EnableWindow(hButton2,FALSE); 

	if( m_pDownloadOnlineResourceThread != NULL )
	{
		WaitThreadUntilQuit( m_pDownloadOnlineResourceThread->m_hThread );
		delete m_pDownloadOnlineResourceThread;
		m_pDownloadOnlineResourceThread = NULL;
	}

	CWinThread *pThread = AfxBeginThread( RUNTIME_CLASS(CDownloadWebPageThread),
		THREAD_PRIORITY_NORMAL, 0, CREATE_SUSPENDED, NULL );
	if ( pThread != NULL )
	{
		((CDownloadWebPageThread*)pThread)->SetAutoDeploy(FALSE);
		m_pDownloadOnlineResourceThread = pThread;
		pThread->m_pMainWnd = this; // the message receiver.
		pThread->ResumeThread();
	}
	else
	{
		::EnableWindow(hButton,TRUE); 
		::EnableWindow(hButton2,TRUE); 
		// 出错的话不用处理，还显示旧的页面
	}
}

LRESULT CPreviewerDlg::OnDownloadWebPageEnd( WPARAM wParam, LPARAM lParam)
{
	HWND hButton2 = ::GetDlgItem(*this,IDC_DOWNLOAD_LAUNCHER_ZIP);
	::EnableWindow(hButton2, TRUE);
	
	HWND hButton = ::GetDlgItem(*this,IDC_UNZIP_LOCAL_ZIP);
	::EnableWindow(hButton,TRUE); 

	if( !wParam )
	{
		CString strUrl = GetLauncherOnlineResourceUrl();
		::MessageBoxW(*this, strUrl, L"Download Online zip failed!", MB_OK|MB_ICONERROR );
	}

	return 0;
}

LRESULT CPreviewerDlg::OnDeployWebPageEnd( WPARAM wParam, LPARAM lParam)
{
	HWND hButton = ::GetDlgItem(*this,IDC_UNZIP_LOCAL_ZIP);
	::EnableWindow(hButton,TRUE); 
	
	return 0;
}

CString TryGetLocalPathByUrl( LPCTSTR lpszUrlFrom, LPCTSTR lpszLocalDirTo )
{
	TCHAR FilePathName[INTERNET_MAX_PATH_LENGTH] = {0};
	TCHAR DataFileName[MAX_PATH] = {0};
	DWORD dwSize = sizeof(FilePathName);

	{	// try to determinate the file name from the url path 
		URL_COMPONENTS UrlComponents = {0};
		UrlComponents.dwStructSize = sizeof(URL_COMPONENTS);
		UrlComponents.dwUrlPathLength = 1;
		UrlComponents.dwHostNameLength = 1;
		UrlComponents.dwSchemeLength = 1;
		UrlComponents.dwExtraInfoLength = 1;
		BOOL bRet = InternetCrackUrl( lpszUrlFrom, 0, 0, &UrlComponents );
		if( !bRet )
		{
			return L"";
		}

		if( UrlComponents.dwUrlPathLength == 0 )
		{
			return L"";
		}
		
		StringCchCopyN(DataFileName, MAX_PATH, UrlComponents.lpszUrlPath, UrlComponents.dwUrlPathLength );

		// Leaves the file name only
		PathStripPath(DataFileName);
	}

	// 合并文件名和目录组成全路径
	PathCombine(FilePathName, lpszLocalDirTo, DataFileName);

	return CString(FilePathName);
}

void CPreviewerDlg::OnBnClickedUnzipLocalZip()
{
	CString strResourcePath;
	if( m_pDownloadOnlineResourceThread != NULL )
	{
		strResourcePath = ((CDownloadWebPageThread*)m_pDownloadOnlineResourceThread)->GetResourcePath();
		if( !strResourcePath.IsEmpty() )
		{
			HWND hButton = ::GetDlgItem(*this,IDC_UNZIP_LOCAL_ZIP);
			::EnableWindow(hButton,FALSE); 

			HWND hButton2 = ::GetDlgItem(*this,IDC_DOWNLOAD_LAUNCHER_ZIP);
			::EnableWindow(hButton2,FALSE); 

			WaitThreadUntilQuit( m_pDownloadOnlineResourceThread->m_hThread );
			
			if( !((CDownloadWebPageThread*)m_pDownloadOnlineResourceThread)->Deploy() )
			{
				::MessageBoxW(*this, strResourcePath, L"Unzip file failed!", MB_OK|MB_ICONERROR );
			}
			else
			{
				::MessageBoxW(*this, strResourcePath, L"Unzip file successfully:", MB_OK );
			}

			delete m_pDownloadOnlineResourceThread;
			m_pDownloadOnlineResourceThread = NULL;

			::EnableWindow(hButton,TRUE); 
			::EnableWindow(hButton2,TRUE); 

			// Show the offline page automaticaly.
			OnBnClickedShowOfflineWebpage();
			return;
		}
	}

	if( strResourcePath.IsEmpty() )
	{
		CString strUrl = GetLauncherOnlineResourceUrl();
		CString strUIDirectory = GetLauncherUIResourceDir();
		strResourcePath = TryGetLocalPathByUrl(strUrl,strUIDirectory);
	}

	if( !::PathFileExistsW(strResourcePath )) 
	{
		CString strUrl = GetLauncherOnlineResourceUrl();
		CString strMessage;
		strMessage = L"Please download file first : \n";
		strMessage += strUrl;
		strMessage += L"\n\nOr put a file under the following path: \n";
		strMessage += strResourcePath;

		::MessageBoxW(*this, strMessage, L"Unzip file failed!", MB_OK|MB_ICONERROR );
	}
	else
	{
		if( CDownloadWebPageThread::DeployWebPageResource(strResourcePath) )
		{
			// Show the offline page automaticaly.
			OnBnClickedShowOfflineWebpage();
			::MessageBoxW(*this, strResourcePath, L"Unzip file successfully:", MB_OK );
		}
		else
		{
			CString strMessage;
			strMessage += L"Can not unzip the file of the following path: \n";
			strMessage += strResourcePath;
			strMessage += L"\n\nMaybe it's built through a higher version of zip program.";

			::MessageBoxW(*this, strMessage, L"Unzip file failed!", MB_OK|MB_ICONERROR );
		}
	}
}

void CPreviewerDlg::OnBnClickedShowOfflineWebpage()
{
	// 离线页面
	m_htmlView->Navigate2( GetLauncherOfflineUrl() );
	CheckRadioButton(ID_SHOW_OFFLINE_WEBPAGE, ID_SHOW_BLANK_WEBPAGE, ID_SHOW_OFFLINE_WEBPAGE);
}

void CPreviewerDlg::OnBnClickedShowOnlineWebpage()
{
	// 在线页面
	m_htmlView->Navigate2( GetLauncherOnlineUrl() );
	CheckRadioButton(ID_SHOW_OFFLINE_WEBPAGE, ID_SHOW_BLANK_WEBPAGE, ID_SHOW_ONLINE_WEBPAGE);
}

void CPreviewerDlg::OnBnClickedShowBlankWebpage()
{
	m_htmlView->Navigate2( L"about:blank" );
	CheckRadioButton(ID_SHOW_OFFLINE_WEBPAGE, ID_SHOW_BLANK_WEBPAGE, ID_SHOW_BLANK_WEBPAGE);
}

BOOL CPreviewerDlg::OnToolTipNotify( UINT id, NMHDR * pNMHDR, LRESULT * pResult )
{
	TOOLTIPTEXT *pTTT = (TOOLTIPTEXT *)pNMHDR;
	UINT_PTR nID = pNMHDR->idFrom;

	if ((pTTT->uFlags & TTF_IDISHWND) == 0 )
	{
		return FALSE;
	}

	// idFrom is actually the HWND of the tool
	int iID = ::GetDlgCtrlID((HWND)nID);
	if( iID == 0 )
	{
		return FALSE;
	}

	// 特殊处理CDIYButton类的提示信息，它们的悬浮提示来自于XML定义。
	CWnd *pChild = CWnd::FromHandlePermanent((HWND)nID);
	if( pChild != NULL )
	{
		if( pChild->IsKindOf(RUNTIME_CLASS(CDIYButton)))
		{
			// 自绘按钮
			LPCTSTR lpszTipInfo = ((CDIYButton*)pChild)->GetTipInfo();
			if( lpszTipInfo != NULL && lpszTipInfo[0] != _T('\0'))
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), lpszTipInfo  );
				return TRUE;
			}
		}
		else if( pChild->IsKindOf(RUNTIME_CLASS(CODProgressCtrl) ) )
		{
			// 自绘进度条
			LPCTSTR lpszTipInfo = ((CODProgressCtrl*)pChild)->GetTipInfo();
			if( lpszTipInfo != NULL && lpszTipInfo[0] != _T('\0'))
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), lpszTipInfo  );
				return TRUE;
			}
		}
	}

	CString strName;

	// 提示信息
	if( strName.LoadString( iID ) )
	{
		if( !strName.IsEmpty() )
		{
			int iPos = strName.Find( _T("\n") );
			if ( iPos > 0 )
			{
				strName = strName.Mid( iPos + 1 );
			}

			CStringW strTipInfo;
			if( m_layoutManager.GetControlTipInfo( strName, strTipInfo ) )
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strTipInfo);
			}
			else
			{
				if ( iPos > 0 )
				{
					_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strName );
				}
				else
				{
					pTTT->lpszText = MAKEINTRESOURCE(iID);
					pTTT->hinst = AfxGetResourceHandle();
				}
			}
		}
		else
		{
			pTTT->lpszText = MAKEINTRESOURCE(iID);
			pTTT->hinst = AfxGetResourceHandle();
		}
	}
	else
	{
		GetDlgItemText(iID, strName );
		if ( !strName.IsEmpty() )
		{
			_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strName );
			return TRUE;
		}

	}

	return(TRUE);
}
