// installDlg.cpp : implementation file
//

#include "stdafx.h"
#include "install.h"
#include "installDlg.h"
#include "InitState.h"
#include "CopyrightState.h"
#include "ProgressState.h"
#include "const.h"
#include "Config.h"
#include "../../pfsx/jpeg/JpgTranslator.h"
#include "LayoutManager.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CinstallDlg dialog
CInstallDlg::CInstallDlg(CWnd* pParent /*=NULL*/)
: CDialog( CInstallDlg::IDD, pParent)
, m_index( 0)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

CInstallDlg::~CInstallDlg()
{
	PFS::CEnv::Unmount( CInstallState::GetResourceStr());
	PFS::CEnv::Unmount( CInstallState::GetAppPathStr());
}

void CInstallDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CInstallDlg, CDialog)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_WM_TIMER()
	ON_WM_ERASEBKGND()
	ON_WM_LBUTTONDBLCLK()
	ON_WM_NCHITTEST()
	ON_WM_NCLBUTTONDBLCLK()
END_MESSAGE_MAP()


// CinstallDlg message handlers

BOOL CInstallDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// 注册jpeg解码器
	PFSX::JPEG::Initialize();
	GetAppPath();

	if( CConfig::GetInstance().Open( m_apppath, L"/install.xml"))
	{
		SetWindowText( CConfig::GetStr( APPNAME).c_str());
	}
	else
	{
		ShowWindow( SW_MINIMIZE);
		CGlobalError::GetInstance().SetError( ERROR_FILE_NOT_FOUND, L"install.xml");
		ChangeState( CFinishState::GetInstance());
		return TRUE;
	}

	if( !Init())
	{
		ShowWindow( SW_MINIMIZE);
		CGlobalError::GetInstance().SetError( PFS::CEnv::GetLastError(), L"resource.pfs");
		ChangeState( CFinishState::GetInstance());
		return TRUE;
	}

	CLayoutManager::GetInstance().Create( this, CInstallState::GetResourceStr(), CConfig::GetInstance().m_fullscreen );

	ChangeState( CInitState::GetInstance());

	return TRUE;  // return TRUE  unless you set the focus to a control
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CInstallDlg::OnPaint()
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
		CPaintDC dc( this);
		CLayoutManager::GetInstance().Draw( dc.GetSafeHdc(), m_index);
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CInstallDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

void CInstallDlg::GetAppPath()
{
	m_apppath.resize( MAX_PATH);
	::GetModuleFileName( NULL, const_cast<LPWSTR>( m_apppath.c_str()), MAX_PATH);
	m_apppath.erase( m_apppath.find_last_of( L"\\")+1);
}

bool CInstallDlg::Init()
{
	if( !PFS::CEnv::Mount( CInstallState::GetAppPathStr(), m_apppath, PFS::FST_NATIVE, PFS::MT_READONLY))
	{
		return false;
	}
	std::wstring resource = CInstallState::GetAppPathStr() + L"/resource.zip";
	if( !PFS::CEnv::Mount( CInstallState::GetResourceStr(), resource, PFS::FST_ZIP, PFS::MT_READONLY))
	{
		return false;
	}
	return true;
}

void CInstallDlg::OnTimer(UINT_PTR nIDEvent)
{
	switch( nIDEvent)
	{
	case CInitState::IDT_INIT:
		KillTimer( CInitState::IDT_INIT);
		ShowWindow( SW_HIDE);
		ChangeState( CCopyrightState::GetInstance());
		m_index++;
		break;
	case CProgressState::IDT_PROGRESS:
		m_index++;
		if( m_index >= CLayoutManager::GetInstance().GetFgCount())
			m_index = 1;

		// 切换背景图时不移除背景
		Invalidate( FALSE );
		break;
	}
	CDialog::OnTimer( nIDEvent);
}

void CInstallDlg::ChangeState( CInstallState& state)
{
	m_curstate = &state;
	m_curstate->SetInstallDlg( this);
	m_curstate->Show();
}

void CInstallDlg::OnOK()
{
}

void CInstallDlg::OnCancel()
{
}

BOOL CInstallDlg::OnEraseBkgnd(CDC* pDC)
{
	// 切换全屏显示时要求移除当前的背景，所以这里不再return TRUE.
	return CDialog::OnEraseBkgnd(pDC);
}

LRESULT CInstallDlg::OnNcHitTest(CPoint point)
{
	if( m_curstate != NULL && m_curstate->CanToggleFullsceen() )
	{
		if( CLayoutManager::GetInstance().CanMoveOnDesktop() )
			return HTCAPTION;
	}
	return CDialog::OnNcHitTest(point);
}

void CInstallDlg::OnLButtonDblClk(UINT nFlags, CPoint point)
{
	if( m_curstate != NULL && m_curstate->CanToggleFullsceen() )
	{
		// 用户双击窗口时可以切换全屏
		CLayoutManager::GetInstance().ToggleFullScreen();
	}
	else
	{
		CDialog::OnLButtonDblClk(nFlags, point);
	}
}

void CInstallDlg::OnNcLButtonDblClk(UINT nHitTest, CPoint point)
{
	if( m_curstate != NULL && m_curstate->CanToggleFullsceen() )
	{
		// 用户双击窗口时可以切换全屏
		CLayoutManager::GetInstance().ToggleFullScreen();
	}
	else
	{
		CDialog::OnNcLButtonDblClk(nHitTest, point);
	}
}
