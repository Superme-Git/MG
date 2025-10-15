// ChildFrm.cpp : CChildFrame 类的实现
//
#include "stdafx.h"
#include "LinkedObjectEditor.h"
#include "ChildFrm.h"

#include "LinkedObjectEditorView.h"
#include "LinkedObjectStatusView.h"
#include "LinkedObjectInfoView.h"

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

	// attempt to use active view of frame window
	CWnd* pView = NULL;
	CFrameWnd* pFrameWnd = GetParentFrame();
	ASSERT_VALID(pFrameWnd);
	pView = pFrameWnd->GetActiveView();

	// failing that, use the current focus
	if (pView == NULL)
		pView = GetFocus();

	return pView;
}

// CChildFrame

IMPLEMENT_DYNCREATE(CChildFrame, CMDIChildWnd)

BEGIN_MESSAGE_MAP(CChildFrame, CMDIChildWnd)
	ON_WM_CLOSE()
END_MESSAGE_MAP()


// CChildFrame 构造/析构

CChildFrame::CChildFrame()
{
	// TODO: 在此添加成员初始化代码
}

CChildFrame::~CChildFrame()
{
}

BOOL CChildFrame::OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext)
{
	CWnd *p = GetParent();
	RECT prect;
	p->GetClientRect(&prect);

	if(!m_wndSplitter1.CreateStatic(this, 1, 2))
	{
		TRACE0("Failed to CreateStaticSplitter\n");
		return FALSE;
	}

	// add the first splitter pane - the default view in column 0
	if(!m_wndSplitter1.CreateView(0, 0,
		RUNTIME_CLASS(CLinkedObjectInfoView), CSize(500, 0), pContext))
	{
		TRACE0("Failed to create first pane\n");
		return FALSE;
	}

	// add the second splitter pane - which is a nested splitter with 2 rows
	if(!m_wndSplitter2.CreateStatic(
		&m_wndSplitter1,     // our parent window is the first splitter
		2, 1,               // the new splitter is 2 rows, 1 column
		WS_CHILD | WS_VISIBLE | WS_BORDER,  // style, WS_BORDER is needed
		m_wndSplitter1.IdFromRowCol(0, 1)
		// new splitter is in the first row, 2nd column of first splitter
		))
	{
		TRACE0("Failed to create nested splitter\n");
		return FALSE;
	}
	if(!m_wndSplitter2.CreateView(0, 0, RUNTIME_CLASS(CLinkedObjectEditorView), CSize(0, prect.bottom-prect.top-20), pContext))
	{
		TRACE0("Failed to create second pane\n");
		return FALSE;
	}
	if(!m_wndSplitter2.CreateView(1, 0, RUNTIME_CLASS(CLinkedObjectStatusView), CSize(0, 0), pContext))
	{
		TRACE0("Failed to create third pane\n");
		return FALSE;
	}

	// it all worked, we now have two splitter windows which contain
	//  three different views
	return TRUE;
}

BOOL CChildFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: 在此处通过修改 CREATESTRUCT cs 来修改窗口类或样式
	if( !CMDIChildWnd::PreCreateWindow(cs) )
		return FALSE;

	cs.style = WS_CHILD | WS_VISIBLE | WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU
		| FWS_ADDTOTITLE | WS_THICKFRAME | WS_MINIMIZEBOX | WS_MAXIMIZEBOX | WS_MAXIMIZE;

	return TRUE;
}
// CChildFrame 消息处理程序

void CChildFrame::OnClose()
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CMDIChildWnd::OnClose();
}
