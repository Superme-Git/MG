// MainFrm.cpp : implementation of the CMainFrame class
//

#include "stdafx.h"
#include "viewpfs.h"

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
	ID_INDICATOR_PANE1,
	ID_INDICATOR_PANE2,
	ID_INDICATOR_PANE3,
	// ID_INDICATOR_CAPS,
	//ID_INDICATOR_NUM,
	//ID_INDICATOR_SCRL,
};


// CMainFrame construction/destruction

CMainFrame::CMainFrame()
{
	// TODO: add member initialization code here
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


	// 初始化状态栏第二栏的尺寸和内容（显示工作目录）。
	UINT nStyle = m_wndStatusBar.GetPaneStyle(1);
	m_wndStatusBar.SetPaneInfo(1, ID_INDICATOR_PANE1, nStyle, 400 );
	WCHAR szDirectoryBuffer1[] = L"工作目录：";
	WCHAR szDirectoryBuffer[MAX_PATH + _countof(szDirectoryBuffer1)] = L"工作目录：";
	::GetCurrentDirectoryW(MAX_PATH, szDirectoryBuffer + wcslen(szDirectoryBuffer) );
	m_wndStatusBar.SetPaneText(1, szDirectoryBuffer, 1 );

	m_wndStatusBar.SetPaneInfo(2, ID_INDICATOR_PANE2, nStyle, 60 );
	m_wndStatusBar.SetPaneInfo(3, ID_INDICATOR_PANE3, nStyle, 100 );

	m_wndStatusBar.SetPaneText(2, L"", 1 );
	m_wndStatusBar.SetPaneText(3, L"", 1 );

	return 0;
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

void CMainFrame::OnUpdateFrameTitle(BOOL bAddToTitle)
{
	CDocument* pDocument = GetActiveDocument();
	if (pDocument != NULL)
	{
		// 设置状态栏第二栏的尺寸和内容（显示工作目录或者当前文档路径名）。
		CString strFilePath = pDocument->GetPathName();
		if( strFilePath.GetLength() == 0 )
		{
			WCHAR szDirectoryBuffer1[] = L"工作目录：";
			WCHAR szDirectoryBuffer[MAX_PATH + _countof(szDirectoryBuffer1)] = L"工作目录：";
			::GetCurrentDirectoryW(MAX_PATH, szDirectoryBuffer + wcslen(szDirectoryBuffer) );
			m_wndStatusBar.SetPaneText(1, szDirectoryBuffer, 1 );

			m_wndStatusBar.SetPaneText(2, L"", 1 );
			m_wndStatusBar.SetPaneText(3, L"", 1 );
		}
		else
		{
			m_wndStatusBar.SetPaneText(1, strFilePath, 1 );
		}
	}

	CFrameWnd::OnUpdateFrameTitle(bAddToTitle);
}

void CMainFrame::UpdateStatusBar( int count, int memorysize)
{
	CString str;
	str.Format( L"共%d项", count);
	m_wndStatusBar.SetPaneText( 2, str );

	if( memorysize >= 1024*1024*1024 )
	{
		str.Format( L"占用内存%.3fG", memorysize/(1024*1024*1024*1.0f));
	}
	else if( memorysize >= 1024*1024 )
	{
		str.Format( L"占用内存%.3fM", memorysize/(1024.0*1024*1.0f));
	}
	else if( memorysize >= 1024 )
	{
		str.Format( L"占用内存%.3fK", memorysize/1024.0f);
	}
	else
	{
		str.Format( L"占用内存%uB", memorysize);
	}

	m_wndStatusBar.SetPaneText( 3, str );
}