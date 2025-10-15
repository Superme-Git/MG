// DownloaderDlg.cpp : 实现文件
//

#include "stdafx.h"
#include "downloader.h"
#include "DownloaderDlg.h"
#include "InetDownload.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// 用于应用程序“关于”菜单项的 CAboutDlg 对话框

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// 对话框数据
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

// 实现
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


// CDownloaderDlg 对话框

CDownloaderDlg::CDownloaderDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CDownloaderDlg::IDD, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	m_pDownloadThread = NULL;
}

CDownloaderDlg::~CDownloaderDlg()
{
	if( m_pDownloadThread != NULL )
	{
		(dynamic_cast<CDownloadThread*>(m_pDownloadThread))->Stop(TRUE);
		m_pDownloadThread = NULL;
	}
}


void CDownloaderDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CDownloaderDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_WM_WINDOWPOSCHANGED()
	ON_WM_GETMINMAXINFO()
END_MESSAGE_MAP()

// CDownloaderDlg 消息处理程序

BOOL CDownloaderDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// 将“关于...”菜单项添加到系统菜单中。

	// IDM_ABOUTBOX 必须在系统命令范围内。
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

	// 设置此对话框的图标。当应用程序主窗口不是对话框时，框架将自动
	//  执行此操作
	SetIcon(m_hIcon, TRUE);			// 设置大图标
	SetIcon(m_hIcon, FALSE);		// 设置小图标

	SetDlgItemText( IDC_ROOT_URL, L"http://patch2.mhzx.locojoy.com/mhzx");

	m_taskListCtrl.SubclassDlgItem( IDC_TASK_LIST, this );


	ShowWindow(SW_MAXIMIZE);

	// TODO: 在此添加额外的初始化代码

	return TRUE;  // 除非将焦点设置到控件，否则返回 TRUE
}

void CDownloaderDlg::OnSysCommand(UINT nID, LPARAM lParam)
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

// 如果向对话框添加最小化按钮，则需要下面的代码
//  来绘制该图标。对于使用文档/视图模型的 MFC 应用程序，
//  这将由框架自动完成。

void CDownloaderDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // 用于绘制的设备上下文

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// 使图标在工作矩形中居中
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// 绘制图标
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

//当用户拖动最小化窗口时系统调用此函数取得光标显示。
//
HCURSOR CDownloaderDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

void CDownloaderDlg::RecalcLayout()
{
	if( m_taskListCtrl.GetSafeHwnd() == NULL )
	{
		return;
	}

	CRect rcClient;
	GetClientRect(&rcClient);
	HWND hOKButton, hCancelButton, hUrlEdit;
	GetDlgItem( IDOK, &hOKButton );
	GetDlgItem( IDCANCEL, &hCancelButton);
	GetDlgItem( IDC_ROOT_URL, &hUrlEdit );
	RECT rcOKButton, rcCancelButton, rcUrlEdit, rcListCtrl;
	::GetWindowRect( hOKButton, &rcOKButton );
	ScreenToClient( & rcOKButton );
	::GetWindowRect( hCancelButton, &rcCancelButton );
	ScreenToClient( &rcCancelButton );

	::GetWindowRect( hUrlEdit, &rcUrlEdit );
	ScreenToClient( &rcUrlEdit );
	
	m_taskListCtrl.GetWindowRect( &rcListCtrl );
	ScreenToClient( &rcListCtrl );

	int nNumWindows = 4;

	HDWP hdwp = BeginDeferWindowPos( nNumWindows );
	if( hdwp == NULL )
	{
		ASSERT(!"BeginDeferWindowPos failed!");
		return;
	}

	// 计算坐标关系
	int nButtonDist = rcCancelButton.left - rcOKButton.left;
	int nLeftBound = rcListCtrl.left;
	int nRightBound = rcClient.Width() - nLeftBound;
	int nCancelButtonWidth = rcCancelButton.right - rcCancelButton.left;
	int nCancelButtonLeft = nRightBound - nCancelButtonWidth;
	int nCancelButtonTop = rcCancelButton.top;
	int nCancelButtonHeight = rcCancelButton.bottom - rcCancelButton.top;
	int nOKButtonWidth = rcOKButton.right - rcOKButton.left;
	int nOKButtonHeight = rcOKButton.bottom - rcOKButton.top;
	int nOKButtonLeft = nCancelButtonLeft - nButtonDist;
	int nListTop = rcListCtrl.top;
	int nListWidth = rcClient.Width() -  rcListCtrl.left - rcListCtrl.left;
	int nListHeight = rcClient.Height() - nListTop - nLeftBound;
	rcUrlEdit.right = nOKButtonLeft - 10;
	int hUrlEditWidth = rcUrlEdit.right-rcUrlEdit.left;
	int hUrlEditHeight = rcUrlEdit.bottom-rcUrlEdit.top;

	hdwp = ::DeferWindowPos( hdwp, hUrlEdit, NULL, 
		rcUrlEdit.left, rcUrlEdit.top, hUrlEditWidth, hUrlEditHeight , SWP_NOZORDER|SWP_NOACTIVATE );
	hdwp = ::DeferWindowPos( hdwp, hOKButton, NULL, 
		nOKButtonLeft, rcOKButton.top, nOKButtonWidth, nOKButtonHeight, SWP_NOZORDER|SWP_NOACTIVATE );
	hdwp = ::DeferWindowPos( hdwp, hCancelButton, NULL, 
		nCancelButtonLeft, nCancelButtonTop, nCancelButtonWidth, nCancelButtonHeight, SWP_NOZORDER|SWP_NOACTIVATE );
	hdwp = ::DeferWindowPos( hdwp, m_taskListCtrl.GetSafeHwnd(), NULL,
		rcListCtrl.left, rcListCtrl.top, nListWidth, nListHeight, SWP_NOZORDER|SWP_NOACTIVATE );

	::EndDeferWindowPos(hdwp);
}

void CDownloaderDlg::OnWindowPosChanged(WINDOWPOS* lpwndpos)
{
	CDialog::OnWindowPosChanged(lpwndpos);
	RecalcLayout();
}

void CDownloaderDlg::OnGetMinMaxInfo(MINMAXINFO* lpMMI)
{
	lpMMI->ptMinTrackSize.x = 350;
	lpMMI->ptMinTrackSize.y = 300;
	CDialog::OnGetMinMaxInfo(lpMMI);
}

void CDownloaderDlg::OnOK()
{
	CString strRootUrl;
	GetDlgItemText(IDC_ROOT_URL, strRootUrl );
	if ( strRootUrl.IsEmpty() )
	{
		AfxMessageBox(_T("BaseURL为空!"));
		return;
	}

	if( m_pDownloadThread != NULL )
	{
		(dynamic_cast<CDownloadThread*>(m_pDownloadThread))->Stop();
		m_pDownloadThread = NULL;
	}

	CWinThread* pThread = AfxBeginThread( 
		RUNTIME_CLASS( CDownloadThread ), THREAD_PRIORITY_NORMAL, 0, CREATE_SUSPENDED, 0  );
	if ( pThread == NULL )
	{
		return;
	}

	if( CDownloadThread* pDownloadThread = dynamic_cast<CDownloadThread*>(pThread) )
	{	
		wstringlist strlist;
		m_taskListCtrl.GetFileNameList(strlist);
		if( !strlist.empty() )
		{
			pDownloadThread->SetSaveTo( L"E:\\downtest" );
			pDownloadThread->SetRootUrl( strRootUrl.GetString() );
			pDownloadThread->GetFileNameList().swap(strlist);
			pDownloadThread->m_bAutoDelete = FALSE;
			
			pDownloadThread->Dump(afxDump);
			TRACE( traceDumpContext, 0, _T("CDownloadThread ThreadId: 0x%03x\n"), pDownloadThread->m_nThreadID );

			pDownloadThread->ResumeThread();
		
			m_pDownloadThread = pDownloadThread;
		}
		else
		{
			AfxMessageBox(_T("没有指定下载的文件!"));
			pThread->Delete();
		}
	}
}

BOOL CDownloaderDlg::PreTranslateMessage(MSG* pMsg)
{
	// TODO: Add your specialized code here and/or call the base class
	return CDialog::PreTranslateMessage(pMsg);
}
