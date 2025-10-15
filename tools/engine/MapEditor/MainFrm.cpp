

#include "stdafx.h"
#include "MapEditor.h"

#include "MainFrm.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif




IMPLEMENT_DYNAMIC(CMainFrame, CMDIFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CMDIFrameWnd)
	ON_WM_CREATE()
END_MESSAGE_MAP()

static UINT indicators[] =
{
	ID_SEPARATOR,           
	ID_SEPARATOR,
	ID_INDICATOR_CAPS,
	ID_INDICATOR_NUM,
	ID_INDICATOR_SCRL,
};




#ifdef _DEBUG
void CMainFrame::AssertValid() const
{
	CMDIFrameWnd::AssertValid();
}

void CMainFrame::Dump(CDumpContext& dc) const
{
	CMDIFrameWnd::Dump(dc);
}

#endif 
BOOL CMainFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	if( !CMDIFrameWnd::PreCreateWindow(cs) )
		return FALSE;





	return TRUE;
}

int CMainFrame::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CMDIFrameWnd::OnCreate(lpCreateStruct) == -1)
		return -1;

	if (!m_ToolBar.CreateEx(this, TBSTYLE_FLAT, WS_CHILD | WS_VISIBLE | CBRS_TOP
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC) ||
		!m_ToolBar.LoadToolBar(IDR_MAINFRAME))
	{
		TRACE0("Failed to create toolbar\n");
		return -1;      
	}

	if (!m_StatusBar.Create(this) ||
		!m_StatusBar.SetIndicators(indicators,
		sizeof(indicators)/sizeof(UINT)))
	{
		TRACE0("Failed to create status bar\n");
		return -1;      
	}
	m_StatusBar.SetPaneInfo(0, ID_SEPARATOR, SBPS_NOBORDERS | SBPS_STRETCH, 0);
	m_StatusBar.SetPaneInfo(1, ID_SEPARATOR, SBPS_NORMAL, 100);

	CRect rect;


	m_ToolBar.EnableDocking(CBRS_ALIGN_ANY);
	EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_ToolBar);

	if (!m_LayerEditToolBar.CreateEx(this, TBSTYLE_DROPDOWN, WS_CHILD | CBRS_TOP 
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC) ||
		!m_LayerEditToolBar.LoadToolBar(IDR_TOOLBAR_EDIT_LAYER))
	{
		TRACE0("Failed to create toolbar\n");
		return -1;      
	}
	m_LayerEditToolBar.GetWindowRect(&rect);
	rect.OffsetRect(1,0);
	this->RecalcLayout();

	m_LayerEditToolBar.EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_LayerEditToolBar, AFX_IDW_DOCKBAR_TOP, &rect);

	return 0;
}

void CMainFrame::SetStateBarMousePosition(int x, int y)
{

	CString temp;
	temp.Format(_T("x:%d, y:%d"), x, y);
	m_StatusBar.SetPaneText(1, temp);
}

void CMainFrame::HideToolBars()
{
	if (--m_nToolBarCount == 0)
	{
		m_LayerEditToolBar.ShowWindow(SW_HIDE);
		RecalcLayout();
	}
}

void CMainFrame::ShowToolBars()
{
	m_nToolBarCount++;
	m_LayerEditToolBar.ShowWindow(SW_SHOW);
	RecalcLayout();
}

CMainFrame::~CMainFrame()
{
}

CMainFrame::CMainFrame() : m_nToolBarCount(0)
{

}

