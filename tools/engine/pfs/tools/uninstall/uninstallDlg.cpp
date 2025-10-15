// uninstallDlg.cpp : implementation file
//

#include "stdafx.h"
#include "uninstall.h"
#include "uninstallDlg.h"
#include "DeleteSelf.h"
#include "const.h"
#include "../install/Config.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


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

// CuninstallDlg dialog
CuninstallDlg::CuninstallDlg(CWnd* pParent /*=NULL*/)
: CDialog(CuninstallDlg::IDD, pParent)
, m_success( false)
, m_bDeleteAll(FALSE)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CuninstallDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_PROGRESS1, m_progress);
	DDX_Control(pDX, IDC_STATIC_PROMPT, m_prompt);
	DDX_Check(pDX, IDC_CHECK1, m_bDeleteAll);
}

BEGIN_MESSAGE_MAP(CuninstallDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
	ON_WM_DESTROY()
	ON_MESSAGE( WM_UNINSTALLPROGRESS, OnUninstallProgress)
END_MESSAGE_MAP()


// CuninstallDlg message handlers

BOOL CuninstallDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// TODO: Add extra initialization here
	CGlobalFunction::ParseAppString( m_path, m_appname);
	CConfig::GetInstance().Open( m_path, L"uninstall.xml");
	
	SetWindowText( CConfig::GetStr( APPNAME).c_str());
	GetDlgItem( IDOK)->SetWindowText( CConfig::GetStr( START).c_str());
	GetDlgItem( IDCANCEL)->SetWindowText( CConfig::GetStr( CLOSE).c_str());

	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CuninstallDlg::OnSysCommand(UINT nID, LPARAM lParam)
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

void CuninstallDlg::OnPaint()
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
HCURSOR CuninstallDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

void CuninstallDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	if( IDNO == AfxMessageBox( CConfig::GetStr( AFFIRM).c_str(), MB_YESNO|MB_DEFBUTTON2))
	{
		return;
	}
	if( CheckPatcherProgram() && CheckClientProgram())
	{
		UpdateData( );
		m_listener.SetHWnd( GetSafeHwnd());
		m_thread.SetListener( &m_listener);
		m_thread.StartUninstall( m_path, m_appname, !!m_bDeleteAll);
		GetDlgItem( IDOK)->EnableWindow( FALSE);
		//CDialog::OnOK();
	}
}

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

bool CuninstallDlg::CheckRunProgram( LPCTSTR pszClassName, LPCTSTR pszProgramName)
{
	// Determine if a window with the class name exists...
	HWND hWndPrev = ::FindWindow( pszClassName, pszProgramName);
	if ( hWndPrev != NULL)
	{
		CString str;
		str.Format( L"\"%s\" 程序正在运行，您确认要退出么？", pszProgramName);
		if( IDNO == AfxMessageBox( str, MB_YESNO|MB_DEFBUTTON2))
		{
			return false;
		}
		str.Format( L"正在退出 \"%s\" 程序，请稍候...", pszProgramName);
		m_prompt.SetWindowText( str);

		::PostMessage( hWndPrev, WM_CLOSE, 0, 0);

		DWORD dwStart = ::GetTickCount();
		DWORD dwExitStart = dwStart;
		while( true )		
		{
			DWORD dwCur = ::GetTickCount();
			if( dwCur - dwExitStart > 3000)		// 退出后等待3秒
			{
				break;
			}
			if( dwCur - dwStart > 30000)		// 全部退出时间不能超过30秒
			{
				break;
			}
			// 等待程序退出
			HWND hTemp = ::FindWindow( pszClassName, pszProgramName);
			if( hTemp != NULL)
			{
				if( hTemp != hWndPrev)
				{
					hWndPrev = hTemp;
					::PostMessage( hWndPrev, WM_CLOSE, 0, 0);
				}
				dwExitStart = ::GetTickCount();
			}
			else
			{
				hWndPrev = NULL;
			}
			_PumpMessage();
		}
		
		if ( hWndPrev != NULL)
		{
			str.Format( L"\"%s\" 程序不能自动退出，请手动退出后再重试卸载操作。", pszProgramName);
			AfxMessageBox( str);
			return false;
		}
	}
	return true;
}

// 检查patcher是否在运行
bool CuninstallDlg::CheckPatcherProgram()
{
	// 注：IDS_LAUNCHERNAME 所指示的字符串必须与Patcher中的IDR_MAINFRAME相同
	TCHAR strAppName[MAX_PATH] = {0};
	::LoadString( GetModuleHandle(NULL), IDS_LAUNCHERNAME, strAppName, MAX_PATH);

	return CheckRunProgram( _T("#32770"), strAppName);
}


// 检查游戏客户端是否在运行
bool CuninstallDlg::CheckClientProgram()
{
	std::wstring strAppName;
	if( !GetClientProgramName( strAppName))
	{
		return false;
	}

	// 注：IDS_APP_TITLE 和 IDC_GAMECLIENT 所指示的字符串必须与GameClient中的 IDC_GAMECLIENT 相同
	TCHAR strClassName[MAX_PATH] = {0};
	::LoadString( GetModuleHandle(NULL), IDC_GAMECLIENT, strClassName, MAX_PATH);

	return CheckRunProgram( strClassName, strAppName.c_str());
}

// 得到游戏客户端名称
bool CuninstallDlg::GetClientProgramName( std::wstring& programe)
{
	// 注：IDS_APP_TITLE 所指示的字符串必须与GameClient中的 IDS_APP_TITLE 相同
	TCHAR strAppName[MAX_PATH] = {0};
	::LoadString( GetModuleHandle(NULL), IDS_APP_TITLE, strAppName, MAX_PATH);

	std::wostringstream wos;
	wos << strAppName;

	std::wstring apppath( m_path);
	apppath += L"bin\\gameclient.exe";
	DWORD dwVerHnd;
	DWORD dwVerInfoSize = GetFileVersionInfoSizeW(
		apppath.c_str(),	// pointer to filename string
		&dwVerHnd			// pointer to variable to receive zero
		);

	if (dwVerInfoSize) 
	{
		HANDLE  hMem;
		LPVOID  lpvMem = NULL;
		unsigned int uInfoSize = 0;
		VS_FIXEDFILEINFO * pFileInfo = NULL;

		hMem = GlobalAlloc(GMEM_MOVEABLE, dwVerInfoSize);
		lpvMem = GlobalLock(hMem);
		GetFileVersionInfoW( apppath.c_str(), dwVerHnd, dwVerInfoSize, lpvMem);

		::VerQueryValueW(lpvMem, L"\\", (void**)&pFileInfo, &uInfoSize);

		// Product version from the FILEVERSION of the version info resource 
		wos << L" " << HIWORD(pFileInfo->dwProductVersionMS) << L"." << LOWORD(pFileInfo->dwProductVersionMS)
			<< L"." << HIWORD(pFileInfo->dwProductVersionLS) << L"." << LOWORD(pFileInfo->dwProductVersionLS);

		GlobalUnlock(hMem);
		GlobalFree(hMem);
	}
	programe = wos.str();
	return true;
}

void CuninstallDlg::OnDestroy()
{
	CDialog::OnDestroy();
	// TODO: Add your message handler code here
	if( m_success)
	{
		CDeleteSelf deleter;
		deleter.Do();
	}
}

//Show error message of GetLastError()
std::wstring GetSystemError( DWORD dwError)
{
	LPTSTR lpMsgBuf = NULL;
	FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
		NULL, dwError, MAKELANGID( LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR)&lpMsgBuf, 0, NULL);
	std::wstring res;
	if( lpMsgBuf)
	{
		res = lpMsgBuf;
		LocalFree( lpMsgBuf);
	}
	return res;
}

HRESULT CuninstallDlg::OnUninstallProgress( WPARAM wParam, LPARAM lParam)
{
	switch( lParam)
	{
	case CDeleteOperator::TOTALCOUNT:
		m_progress.SetRange( 0, (short)wParam);
		break;
	case CDeleteOperator::PATHNAME:
		{
			wchar_t buffer[MAX_PATH] = {0};
			wsprintf( buffer, CConfig::GetStr( DELETEPATH).c_str(), (LPCTSTR)wParam);
			m_prompt.SetWindowText( buffer);
			m_progress.SetPos( m_progress.GetPos() + 1);
		}
		break;
	case CDeleteOperator::FILENAME:
		{
			wchar_t buffer[MAX_PATH] = {0};
			wsprintf( buffer, CConfig::GetStr( DELETEFILE).c_str(), (LPCTSTR)wParam);
			m_prompt.SetWindowText( buffer);
			m_progress.SetPos( m_progress.GetPos() + 1);
		}
		break;
	case CDeleteOperator::DELETELINK:
		m_prompt.SetWindowText( CConfig::GetStr( DELETELINK).c_str());
		break;
	case CDeleteOperator::TOTALEND:
		{
			if( CDeleteOperator::m_error != 0)
			{
				wchar_t buffer[MAX_PATH] = {0};
				wsprintf( buffer, CConfig::GetStr( FAILED).c_str(), GetSystemError( CDeleteOperator::m_error).c_str());
				AfxMessageBox( buffer);
			}
			else
			{
				m_success = true;
				AfxMessageBox( CConfig::GetStr( SUCCESS).c_str());
			}
		}
		PostQuitMessage( 0);
		break;
	}
	return 0;
}