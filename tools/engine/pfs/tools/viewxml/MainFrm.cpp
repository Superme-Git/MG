// MainFrm.cpp : implementation of the CMainFrame class
//

#include "stdafx.h"
#include "viewxml.h"
#include "MainFrm.h"

#include "../../pfs2.h"
#include "../../../xmlio/xmlio.h"

#include "viewxmlView.h"
#include "ShowXmlView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

// CMainFrame

IMPLEMENT_DYNCREATE(CMainFrame, CFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CFrameWnd)
	ON_WM_CREATE()
	ON_COMMAND(ID_VIEW_DICTIONARYVIEW, &CMainFrame::OnViewDictionaryview)
	ON_COMMAND(ID_VIEW_XMLVIEW, &CMainFrame::OnViewXmlview)
	ON_COMMAND(ID_VIEW_TREEVIEW, &CMainFrame::OnViewTreeview)
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

	DragAcceptFiles();

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

BOOL CMainFrame::OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext)
{
	return CFrameWnd::OnCreateClient(lpcs, pContext);
}

void CMainFrame::OnViewDictionaryview()
{
	// TODO: 针对XML文档，显示其字符串字典视图。研究基于XML字典的翻译方案。
	// 
	MessageBox(L"TODO: 针对XML文档，显示其字符串字典视图。研究基于XML字典的翻译方案。", L"尚未实现", MB_OK);
}

void CMainFrame::OnViewTreeview()
{
	CDocument* pDoc = GetActiveDocument();
	if( pDoc != NULL )
	{
		POSITION pos = pDoc->GetFirstViewPosition();;
		CView *pOldView = pDoc->GetNextView(pos);

		CCreateContext cc;
		cc.m_pNewViewClass = RUNTIME_CLASS(CviewxmlView);
		cc.m_pCurrentFrame = this;
		cc.m_pCurrentDoc = pDoc;
		cc.m_pLastView = pOldView;
		CWnd * pWnd = CreateView( &cc, AFX_IDW_PANE_FIRST );
		if( pWnd != NULL )
		{
			// The new view has been attached to the current document as the argument of CCreateContext::m_pCurrentDoc.
			// The old view has been dettached from the current document until it being destoryed.
			// pDoc->RemoveView(pOldView);

			pOldView->DestroyWindow();

			InitialUpdateFrame(pDoc, TRUE);

			// SetActiveView((CView*)pWnd,TRUE);
			// pDoc->UpdateAllViews(NULL);
		}

		RecalcLayout();
	}
}

void CMainFrame::OnViewXmlview()
{
	CDocument* pDoc = GetActiveDocument();
	if( pDoc != NULL )
	{

		POSITION pos = pDoc->GetFirstViewPosition();;
		CView *pOldView = pDoc->GetNextView(pos);

		CCreateContext cc;
		cc.m_pNewViewClass = RUNTIME_CLASS(CShowXmlView);
		cc.m_pCurrentFrame = this;
		cc.m_pCurrentDoc = pDoc;
		cc.m_pLastView = pOldView;
		CWnd * pWnd = CreateView( &cc, AFX_IDW_PANE_FIRST );
		if( pWnd != NULL )
		{
			// The new view has been attached to the current document as the argument of CCreateContext::m_pCurrentDoc.
			// The old view has been dettached from the current document until it being destoryed.
			// pDoc->RemoveView(pOldView);
			pOldView->DestroyWindow();

			InitialUpdateFrame(pDoc, TRUE);
		}

		RecalcLayout();
	}
}
