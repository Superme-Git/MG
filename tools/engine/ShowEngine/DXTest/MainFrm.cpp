// MainFrm.cpp : implementation of the CMainFrame class
//

#include "stdafx.h"
#include "DXTest.h"

#include "MainFrm.h"
#include "DXTestDoc.h"
#include "DXTestView.h"
//#include "ShowPartView.h"
#include "EditPartView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CMainFrame

IMPLEMENT_DYNCREATE(CMainFrame, CFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CFrameWnd)
	ON_WM_CREATE()
	ON_WM_CLOSE()
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
	CBitmap bmp, bmpDisabled;
	bmp.LoadBitmap( IDB_BITMAP1);
	BITMAP bp;
	bmp.GetBitmap( &bp);
	m_ilToolBar.Create( bp.bmHeight, bp.bmHeight, ILC_COLOR32, 4, 4);
	m_ilToolBar.Add( &bmp, (CBitmap*)NULL);

	bmpDisabled.LoadBitmap( IDB_BITMAP1D );
	m_ilToolBarDisabled.Create( bp.bmHeight, bp.bmHeight, ILC_COLOR32, 4, 4);
	m_ilToolBarDisabled.Add( &bmpDisabled, (CBitmap*)NULL);

	m_wndToolBar.GetToolBarCtrl().SetImageList( &m_ilToolBar);
	m_wndToolBar.GetToolBarCtrl().SetDisabledImageList(&m_ilToolBarDisabled);

	if (!m_wndToolBarShow.CreateEx(this, TBSTYLE_FLAT, WS_CHILD | WS_VISIBLE | CBRS_TOP
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC) ||
		!m_wndToolBarShow.LoadToolBar(IDR_TOOLBAR_SHOW))
	{
		TRACE0("Failed to create toolbar\n");
		return -1;      // fail to create
	}
	CBitmap bmpShow, bmpShowDisabled;
	bmpShow.LoadBitmap( IDB_BITMAP2);
	bmpShow.GetBitmap( &bp);
	m_ilToolBarShow.Create( bp.bmHeight, bp.bmHeight, ILC_COLOR32, 4, 4);
	m_ilToolBarShow.Add( &bmpShow, (CBitmap*)NULL);

	bmpShowDisabled.LoadBitmap( IDB_BITMAP2D );
	m_ilToolBarShowDisabled.Create( bp.bmHeight, bp.bmHeight, ILC_COLOR32, 4, 4);
	m_ilToolBarShowDisabled.Add( &bmpShowDisabled, (CBitmap*)NULL);

	m_wndToolBarShow.GetToolBarCtrl().SetImageList( &m_ilToolBarShow);
	m_wndToolBarShow.GetToolBarCtrl().SetDisabledImageList( &m_ilToolBarShowDisabled);

	if (!m_wndStatusBar.Create(this) ||
		!m_wndStatusBar.SetIndicators(indicators,
		  sizeof(indicators)/sizeof(UINT)))
	{
		TRACE0("Failed to create status bar\n");
		return -1;      // fail to create
	}

	// TODO: Delete these three lines if you don't want the toolbar to be dockable
	m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
	m_wndToolBarShow.EnableDocking(CBRS_ALIGN_ANY);
	EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_wndToolBar);
	DockControlBar(&m_wndToolBarShow);

	//DragAcceptFiles(TRUE);

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


// CMainFrame diagnostics

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


// CMainFrame message handlers




BOOL CMainFrame::OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext)
{
	// TODO: Add your specialized code here and/or call the base class
	m_wndSplitter.CreateStatic(this, 1, 2);
	m_wndSplitter.CreateView(0, 0, RUNTIME_CLASS(CDXTestView), CSize(280,0), pContext);
	m_wndSplitter.CreateView(0, 1, RUNTIME_CLASS(CEditPartView), CSize(400,0), pContext);
	//m_wndSplitter.CreateView(0, 1, RUNTIME_CLASS(CShowPartView), CSize(400,0), pContext);

	CDXTestView* testview = (CDXTestView*)m_wndSplitter.GetPane( 0, 0);
	CEditPartView* editpartview = (CEditPartView*)m_wndSplitter.GetPane( 0, 1);
	editpartview->m_pDXTestView = testview;
	testview->m_pEditPartView = editpartview;
	//CShowPartView* editpartview = (CShowPartView*)m_wndSplitter.GetPane( 0, 1);
	//editpartview->m_pDXTestView = testview;
	return CFrameWnd::OnCreateClient(lpcs, pContext);
}

BOOL CMainFrame::OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo)
{
	// 定制Command消息路由
	CDXTestView* testview = (CDXTestView*)m_wndSplitter.GetPane( 0, 0);
	if( testview != NULL && testview->OnCmdMsg(nID, nCode, pExtra, pHandlerInfo) )
	{
		return TRUE;
	}
	else
	{
		CEditPartView* editpartview = (CEditPartView*)m_wndSplitter.GetPane( 0, 1);
		if( editpartview != NULL && editpartview->OnCmdMsg(nID, nCode, pExtra, pHandlerInfo) )
		{
			return TRUE;
		}
	}
	
	return CFrameWnd::OnCmdMsg(nID, nCode, pExtra, pHandlerInfo);
}

void CMainFrame::OnClose()
{
	// TODO: Add your message handler code here and/or call default
	CEditPartView* editpartview = (CEditPartView*)m_wndSplitter.GetPane( 0, 1);
	if( editpartview)
	{
		if( editpartview->IsChanged())
		{
			int res = AfxMessageBox( L"配置文件已改变，是否保存？", MB_YESNOCANCEL|MB_DEFBUTTON1);
			if( IDCANCEL == res )
			{
				return;
			}
			if( IDYES == res)
			{
				editpartview->OnSaveConfig();
			}
		}
	}
	CFrameWnd::OnClose();
}
