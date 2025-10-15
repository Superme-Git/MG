

#include "stdafx.h"
#include "EffectEditor.h"

#include "MainFrm.h"
#include "EffectEditorDoc.h"
#include "TimeBarView.h"
#include "ToolView.h"
#include "EffectEditorView.h"
#include "PropertyView.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif




IMPLEMENT_DYNCREATE(CMainFrame, CFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CFrameWnd)
	ON_WM_CREATE()
END_MESSAGE_MAP()

static UINT indicators[] =
{
	ID_SEPARATOR,           
	ID_SEPARATOR,			
	ID_SEPARATOR,			
	ID_SEPARATOR,			
	ID_SEPARATOR,			
	ID_INDICATOR_CAPS,
	ID_INDICATOR_NUM,
	ID_INDICATOR_SCRL,
};




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
	m_HSplitterWindow.CreateStatic(this, 2, 1);
	BOOL result = m_VSplitterWindow.CreateStatic(&m_HSplitterWindow, 1, 3, WS_CHILD|WS_VISIBLE|WS_BORDER, m_HSplitterWindow.IdFromRowCol(0, 0));

	m_HSplitterWindow.CreateView(1, 0, RUNTIME_CLASS(CTimeBarView), CSize(0, 0), pContext);
	m_VSplitterWindow.CreateView(0, 0, RUNTIME_CLASS(CToolView), CSize(0, 0), pContext);
	m_VSplitterWindow.CreateView(0, 1, RUNTIME_CLASS(CEffectEditorView), CSize(0, 0), pContext);
	m_VSplitterWindow.CreateView(0, 2, RUNTIME_CLASS(CPropertyView), CSize(0, 0), pContext);
	CRect rect;
	GetClientRect(&rect);
	m_HSplitterWindow.SetRowInfo(0, rect.Height() - 200, 0);
	m_VSplitterWindow.SetColumnInfo(0, 50, 50);
	m_VSplitterWindow.SetColumnInfo(1, rect.Width() - 235, 235);
	m_VSplitterWindow.SetColumnInfo(2, 204 - 50, 204 - 50);

	return result;
}

void CMainFrame::SetStateBarSelEffectPosition(int x, int y)
{
	CString temp;
	temp.Format(L"选中 x:%d, y:%d", x, y);
	m_wndStatusBar.SetPaneText(4, temp);
}

void CMainFrame::SetStateBarEffectPosition(int x, int y)
{
	CString temp;
	temp.Format(L"特效 x:%d, y:%d", x, y);
	m_wndStatusBar.SetPaneText(3, temp);
}

void CMainFrame::SetStateBarBgPosition(int x, int y)
{
	CString temp;
	temp.Format(L"背景 x:%d, y:%d", x, y);
	m_wndStatusBar.SetPaneText(2, temp);
}

void CMainFrame::SetStateBarMousePosition(int x, int y)
{
	CString temp;
	temp.Format(L"鼠标 x:%d, y:%d", x, y);
	m_wndStatusBar.SetPaneText(1, temp);
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

	if (!m_wndStatusBar.Create(this) ||
		!m_wndStatusBar.SetIndicators(indicators,
		sizeof(indicators)/sizeof(UINT)))
	{
		TRACE0("未能创建状态栏\n");
		return -1;      
	}

	m_wndStatusBar.SetPaneInfo(0, ID_SEPARATOR, SBPS_NOBORDERS | SBPS_STRETCH, 0);
	m_wndStatusBar.SetPaneInfo(1, ID_SEPARATOR, SBPS_NORMAL, 140);
	m_wndStatusBar.SetPaneInfo(2, ID_SEPARATOR, SBPS_NORMAL, 140);
	m_wndStatusBar.SetPaneInfo(3, ID_SEPARATOR, SBPS_NORMAL, 140);
	m_wndStatusBar.SetPaneInfo(4, ID_SEPARATOR, SBPS_NORMAL, 140);


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

