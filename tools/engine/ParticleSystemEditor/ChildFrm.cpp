// ChildFrm.cpp : CChildFrame 类的实现
//
#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "ChildFrm.h"

#include "ParticleParameterView.h" //参数列表
#include "ParticleSysInfoView.h"   //用户关心信息显示
#include "ParticleSystemEditorView.h" //效果及时显示


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
	ON_WM_MDIACTIVATE()
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
	// TODO: Add your specialized code here and/or call the base class

	if (!m_wndSplitter.CreateStatic(this, 1, 2))
	{
		TRACE0("Failed to CreateStaticSplitter\n");
		return FALSE;
	}

	// add the first splitter pane - the default view in column 0
	if (!m_wndSplitter.CreateView(0, 0,
		RUNTIME_CLASS(CParticleParameterView), CSize(400, 0), pContext))
	{
		TRACE0("Failed to create first pane\n");
		return FALSE;
	}

	// add the second splitter pane - which is a nested splitter with 2 rows
	if (!m_wndSplitter2.CreateStatic(
		&m_wndSplitter,     // our parent window is the first splitter
		2, 1,               // the new splitter is 2 rows, 1 column
		WS_CHILD | WS_VISIBLE | WS_BORDER,  // style, WS_BORDER is needed
		m_wndSplitter.IdFromRowCol(0, 1)
		// new splitter is in the first row, 2nd column of first splitter
		))
	{
		TRACE0("Failed to create nested splitter\n");
		return FALSE;
	}
	if (!m_wndSplitter2.CreateView(0, 0, RUNTIME_CLASS(CParticleSystemEditorView), CSize(0, prect.bottom-prect.top-150), pContext))
	{
		TRACE0("Failed to create second pane\n");
		return FALSE;
	}
	if (!m_wndSplitter2.CreateView(1, 0, RUNTIME_CLASS(CParticleSysInfoView), CSize(0, 0), pContext))
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
	// TODO: Add your specialized code here and/or call the base class
	if( !CMDIChildWnd::PreCreateWindow(cs) )
		return FALSE;

	cs.style = WS_CHILD | WS_VISIBLE | WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU
		| FWS_ADDTOTITLE | WS_THICKFRAME | WS_MINIMIZEBOX | WS_MAXIMIZEBOX | WS_MAXIMIZE | WS_OVERLAPPED;

	return TRUE;
}

void CChildFrame::OnClose()
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CString strInfo = L"确定要关闭？";
	int res = MessageBox(strInfo, L"提示", MB_ICONQUESTION|MB_YESNO);
	if(res == IDNO)
		return;
	else
	{// res == IDYES 继续后面的处理...
	}

	CMDIChildWnd::OnClose();
}

// CChildFrame 消息处理程序

void CChildFrame::OnMDIActivate(BOOL bActivate, CWnd* pActivateWnd, CWnd* pDeactivateWnd)
{
	CMDIChildWnd::OnMDIActivate(bActivate, pActivateWnd, pDeactivateWnd);

	// TODO: 在此处添加消息处理程序代码
	//消灭菜单栏...
	AfxGetMainWnd()->SetMenu(NULL);

}
