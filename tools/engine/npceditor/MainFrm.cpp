// MainFrm.cpp : CMainFrame 类的实现
//

#include "stdafx.h"
#include "SoundEditor.h"

#include "MainFrm.h"
#include "SoundEditorDoc.h"
#include "SoundEditorView.h"
#include "PropView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CMainFrame

IMPLEMENT_DYNCREATE(CMainFrame, CFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CFrameWnd)
	ON_WM_CREATE()
	ON_COMMAND(ID_32786, &CMainFrame::On32786)
END_MESSAGE_MAP()

static UINT indicators[] =
{
	ID_SEPARATOR,           // 状态行指示器
	ID_INDICATOR_CAPS,
	ID_INDICATOR_NUM,
	ID_INDICATOR_SCRL,
};


// CMainFrame 构造/析构

CMainFrame::CMainFrame()
{
	// TODO: 在此添加成员初始化代码
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
		TRACE0("未能创建工具栏\n");
		return -1;      // 未能创建
	}

	if (!m_wndStatusBar.Create(this) ||
		!m_wndStatusBar.SetIndicators(indicators,
		  sizeof(indicators)/sizeof(UINT)))
	{
		TRACE0("未能创建状态栏\n");
		return -1;      // 未能创建
	}

	if (!m_wndEditorToolBar.CreateEx(this, TBSTYLE_FLAT, WS_CHILD | WS_VISIBLE | CBRS_TOP
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC) ||
		!m_wndEditorToolBar.LoadToolBar(IDR_TOOLBAR_EDITOR))
	{
		TRACE0("未能创建第二工具栏\n");
		return -1;      // 未能创建
	}

	m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
	m_wndEditorToolBar.EnableDocking(CBRS_ALIGN_ANY);
	EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_wndToolBar);
	RecalcLayout();
	CRect rect;
	m_wndToolBar.GetWindowRect(&rect);
	rect.OffsetRect(1, 0);
	DockControlBar(&m_wndEditorToolBar, AFX_IDW_DOCKBAR_TOP, &rect);

	return 0;
}

BOOL CMainFrame::OnCreateClient(LPCREATESTRUCT /*lpcs*/,
								CCreateContext* pContext)
{
	BOOL result = m_HSplitterWindow.CreateStatic(this, 1, 2, WS_CHILD|WS_VISIBLE|WS_BORDER);
	m_HSplitterWindow.CreateView(0, 0, RUNTIME_CLASS(CSoundEditorView), CSize(1024, 768), pContext);
	m_HSplitterWindow.CreateView(0, 1, RUNTIME_CLASS(CPropView), CSize(0, 0), pContext);
	return result;
}

BOOL CMainFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	if( !CFrameWnd::PreCreateWindow(cs) )
		return FALSE;
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	return TRUE;
}


// CMainFrame 诊断

#ifdef _DEBUG
void CMainFrame::AssertValid() const
{
	CFrameWnd::AssertValid();
}

void CMainFrame::Dump(CDumpContext& dc) const
{
	CFrameWnd::Dump(dc);
}

#endif //_DEBUG


// CMainFrame 消息处理程序

BOOL CMainFrame::OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo)
{
	CDocument *pDoc = GetActiveDocument();
	if (pDoc)
	{
		POSITION pos = pDoc->GetFirstViewPosition();
		CView *pView = NULL;
		while (pView = pDoc->GetNextView(pos))
		{
			if (pView != GetActiveView() &&
				pView->OnCmdMsg(nID, nCode, pExtra, pHandlerInfo))
				return TRUE;
		}
	}
	return CFrameWnd::OnCmdMsg(nID, nCode, pExtra, pHandlerInfo);
}


void CMainFrame::On32786()
{
	// TODO: Add your command handler code here
}
