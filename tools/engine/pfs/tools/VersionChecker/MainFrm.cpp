// MainFrm.cpp : implementation of the CMainFrame class
//

#include "stdafx.h"
#include "VersionChecker.h"

#include "MainFrm.h"
#include "VersionCheckerDoc.h"
#include "VersionCheckerView.h"
#include "CheckReportView.h"

#include "DownloadFileList.h"
#include "DownloadThread.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CMainFrame

IMPLEMENT_DYNCREATE(CMainFrame, CFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CFrameWnd)
	ON_WM_CREATE()
END_MESSAGE_MAP()

static UINT indicators[] =
{
	ID_SEPARATOR,           // status line indicator
	ID_INDICATOR_CAPS,
	ID_INDICATOR_NUM,
	ID_INDICATOR_SCRL,
};


// CMainFrame construction/destruction

CMainFrame::CMainFrame()
{
	// TODO: add member initialization code here
}

CMainFrame::~CMainFrame()
{
	GlobalUnRegister( &m_wndWatcher );
	DOWNLOAD::GetInstance<std::wstring>().Push( std::wstring() );
}

int CMainFrame::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CFrameWnd::OnCreate(lpCreateStruct) == -1)
		return -1;
	
	if (!m_wndToolBar.CreateEx(this, TBSTYLE_FLAT, WS_CHILD | WS_VISIBLE | CBRS_TOP
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC) ||
		!m_wndToolBar.LoadToolBar(IDR_MAINFRAME))
	{
		TRACE0("Failed to create toolbar\n");
		return -1;      // fail to create
	}

	if (!m_wndStatusBar.Create(this) ||
		!m_wndStatusBar.SetIndicators(indicators,
		  sizeof(indicators)/sizeof(UINT)))
	{
		TRACE0("Failed to create status bar\n");
		return -1;      // fail to create
	}

	// TODO: Delete these three lines if you don't want the toolbar to be dockable
	m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
	EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_wndToolBar);


	CWinThread *pThread = AfxBeginThread( RUNTIME_CLASS(CDownloadThread) );
	if ( pThread == NULL )
	{
		// TODO:
		TRACE0("Failed to create CDownloadThread\n");
		return -1;
	}

	return 0;
}

BOOL CMainFrame::OnCreateClient(LPCREATESTRUCT lpcs,
	CCreateContext* pContext)
{
	BOOL bRet = m_wndSplitter.CreateStatic(this, 2, 1 );
	if( !bRet )
	{
		return FALSE;
	}

	CRect rcClient;
	GetClientRect( &rcClient );

	bRet = m_wndSplitter.CreateView(0,0, RUNTIME_CLASS(CVersionCheckerView),  
		CSize(rcClient.Width(), rcClient.Height()*4/5), pContext);
	if( !bRet )
	{
		return FALSE;
	}

	bRet = m_wndSplitter.CreateView(1,0, RUNTIME_CLASS(CCheckReportView), 
		CSize(rcClient.Width(), rcClient.Height()/5), pContext);
	if( !bRet )
	{
		return FALSE;
	}

	CWnd* pWnd00 = m_wndSplitter.GetPane(0,0);
	CWnd* pWnd10 = m_wndSplitter.GetPane(1,0);
	
	m_wndWatcher.SetDataHwnd( pWnd00->GetSafeHwnd() );
	m_wndWatcher.SetHwnd( pWnd10->GetSafeHwnd() );

	GlobalRegister( &m_wndWatcher );
	
	return TRUE;
}

BOOL CMainFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	if( !CFrameWnd::PreCreateWindow(cs) )
		return FALSE;
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return TRUE;
}

// CMainFrame message handlers



