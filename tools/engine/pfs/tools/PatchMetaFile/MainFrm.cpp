// MainFrm.cpp : implementation of the CMainFrame class
//

#include "stdafx.h"
#include "PatchMetaFile.h"

#include "MainFrm.h"

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
	ID_INDICATOR_EXT,		// current directory... or current document file path.
//	ID_INDICATOR_CAPS,
//	ID_INDICATOR_NUM,
//	ID_INDICATOR_SCRL,
};

// CMainFrame construction/destruction

CMainFrame::CMainFrame()
{
}

CMainFrame::~CMainFrame()
{
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

	UINT nStyle = m_wndStatusBar.GetPaneStyle(1);
	m_wndStatusBar.SetPaneInfo(1, ID_INDICATOR_EXT, nStyle, 480 );
	WCHAR szDirectoryBuffer1[] = L"工作目录：";
	WCHAR szDirectoryBuffer[MAX_PATH + _countof(szDirectoryBuffer1)] = L"工作目录：";
	::GetCurrentDirectoryW(MAX_PATH, szDirectoryBuffer + wcslen(szDirectoryBuffer) );
	m_wndStatusBar.SetPaneText(1, szDirectoryBuffer, 1 );

	return 0;
}

BOOL CMainFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	if( !CFrameWnd::PreCreateWindow(cs) )
		return FALSE;
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs
	cs.style &= ~(WS_SIZEBOX|WS_MAXIMIZEBOX) ;

	return TRUE;
}

// CMainFrame message handlers

void CMainFrame::OnUpdateFrameTitle(BOOL bAddToTitle)
{
	CDocument* pDocument = GetActiveDocument();
	if (pDocument != NULL)
	{
		CString strFilePath = pDocument->GetPathName();
		if( strFilePath.GetLength() == 0 )
		{
			WCHAR szDirectoryBuffer1[] = L"工作目录：";
			WCHAR szDirectoryBuffer[MAX_PATH + _countof(szDirectoryBuffer1)] = L"工作目录：";
			::GetCurrentDirectoryW(MAX_PATH, szDirectoryBuffer + wcslen(szDirectoryBuffer) );
			m_wndStatusBar.SetPaneText(1, szDirectoryBuffer, 1 );
		}
		else
		{
			m_wndStatusBar.SetPaneText(1, strFilePath, 1 );
		}
	}

	CFrameWnd::OnUpdateFrameTitle(bAddToTitle);
}
