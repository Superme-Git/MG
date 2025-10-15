

#include "stdafx.h"
#include "SpriteEditor.h"
#include "SpriteEditorView.h"

#include "MainFrm.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

IMPLEMENT_DYNAMIC(CViewExSplitWnd, CSplitterWnd)

CViewExSplitWnd::CViewExSplitWnd()
{
}

CViewExSplitWnd::~CViewExSplitWnd()
{
}

CWnd* CViewExSplitWnd::GetActivePane(int* pRow, int* pCol)
{
	ASSERT_VALID(this);

	
	CWnd* pView = NULL;
	CFrameWnd* pFrameWnd = GetParentFrame();
	ASSERT_VALID(pFrameWnd);
	pView = pFrameWnd->GetActiveView();

	
	if (pView == NULL)
		pView = GetFocus();

	return pView;
}




IMPLEMENT_DYNCREATE(CMainFrame, CFrameWnd)

void CMainFrame::OnClose()
{
	if(((CSpriteEditorApp*)AfxGetApp())->OnFrameClose())
		CFrameWnd::OnClose();
}

#ifdef _DEBUG
void CMainFrame::AssertValid() const
{
	CFrameWnd::AssertValid();
}

void CMainFrame::Dump(CDumpContext& dc) const
{
	CFrameWnd::Dump(dc);
}

#endif 

BOOL CMainFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	if( !CFrameWnd::PreCreateWindow(cs) )
		return FALSE;
	return TRUE;
}

BOOL CMainFrame::OnCreateClient(LPCREATESTRUCT /*lpcs*/,
								CCreateContext* pContext)
{

	if (!m_wndSplitter.CreateStatic(this, 1, 3))
	{
		TRACE0("Failed to CreateStaticSplitter\n");
		return FALSE;
	}

	if (!m_wndSplitter.CreateView(0, 2,
		RUNTIME_CLASS(CActionControlView), CSize(160, 0), pContext))
	{
		TRACE0("Failed to create last pane\n");
		return FALSE;
	}


	if (!m_wndSplitter.CreateView(0, 0,
		RUNTIME_CLASS(CAnimationOpView), CSize(200, 0), pContext))
	{
		TRACE0("Failed to create first pane\n");
		return FALSE;
	}






	if (!m_wndSplitter2.CreateStatic(
		&m_wndSplitter,     
		2, 1,               
		WS_CHILD | WS_VISIBLE | WS_BORDER,  
		m_wndSplitter.IdFromRowCol(0, 1)

		))
	{
		TRACE0("Failed to create nested splitter\n");
		return FALSE;
	}
	if (!m_wndSplitter2.CreateView(0, 0, RUNTIME_CLASS(CSpriteEditorView), CSize(400, GetSystemMetrics(SM_CYSCREEN) - 250), pContext))
	{
		TRACE0("Failed to create second pane\n");
		return FALSE;
	}
	if (!m_wndSplitter2.CreateView(1, 0, RUNTIME_CLASS(CEquipSelectView), CSize(400, 0), pContext))
	{
		TRACE0("Failed to create third pane\n");
		return FALSE;
	}


	m_wndSplitter.SetColumnInfo(1,800,0);
	m_wndSplitter2.SetRowInfo(0,600,0);



	return TRUE;
}

int CMainFrame::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CFrameWnd::OnCreate(lpCreateStruct) == -1)
		return -1;

	if (!m_wndToolBar.CreateEx(this, TBSTYLE_FLAT, WS_CHILD | WS_VISIBLE | CBRS_TOP
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC) ||
		!m_wndToolBar.LoadToolBar(IDR_MAINFRAME))
	{
		TRACE0("未能创建工具栏\n");
		return -1;      
	}

	UINT indicators[] =
	{
		ID_SEPARATOR,           
		ID_INDICATOR_CAPS,
		ID_INDICATOR_NUM,
		ID_INDICATOR_SCRL,
	};

	if (!m_wndStatusBar.Create(this) ||
		!m_wndStatusBar.SetIndicators(indicators,
		sizeof(indicators)/sizeof(UINT)))
	{
		TRACE0("未能创建状态栏\n");
		return -1;      
	}

	m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
	EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_wndToolBar);

	return 0;
}

CMainFrame::~CMainFrame()
{
}

CMainFrame::CMainFrame()
{
}

BEGIN_MESSAGE_MAP(CMainFrame, CFrameWnd)
	ON_WM_CREATE()
	ON_WM_CLOSE()
END_MESSAGE_MAP()

