// MainFrm.cpp : CMainFrame 类的实现
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"

#include "ParticleSystemEditorDoc.h"

#include "MainFrm.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CMainFrame

IMPLEMENT_DYNAMIC(CMainFrame, CMDIFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CMDIFrameWnd)
	ON_WM_CREATE()
	ON_WM_CLOSE()
	ON_WM_SIZE()
	ON_WM_DROPFILES()
	ON_UPDATE_COMMAND_UI(ID_BTN_TGA2DDS, &CMainFrame::OnUpdateBtnTga2dds)
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

void CMainFrame::ShowToolBars()
{
	m_wndPSLOPToolBar.ShowWindow(SW_SHOW);
	RecalcLayout();
}

void CMainFrame::HideToolBars()
{
	m_wndPSLOPToolBar.ShowWindow(SW_HIDE);
	RecalcLayout();
}

int CMainFrame::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CMDIFrameWnd::OnCreate(lpCreateStruct) == -1)
		return -1;
	
	if (!m_wndToolBar.CreateEx(this, TBSTYLE_FLAT, WS_CHILD | WS_VISIBLE | CBRS_TOP
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC) ||
		!m_wndToolBar.LoadToolBar(IDR_MAINFRAME))
	{
		TRACE0("未能创建工具栏\n");
		return -1;      // 未能创建
	}

	//隐藏状态栏
// 	if (!m_wndStatusBar.Create(this) || 
// 		!m_wndStatusBar.SetIndicators(indicators,
// 		  sizeof(indicators)/sizeof(UINT)))
// 	{
// 		TRACE0("未能创建状态栏\n");
// 		return -1;      // 未能创建
// 	}

	// TODO: 如果不需要工具栏可停靠，则删除这三行
	m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
	EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_wndToolBar);

	ShowControlBar(&m_wndToolBar,false,false);//隐藏工具栏

										//TBSTYLE_DROPDOWN
	if (!m_wndPSLOPToolBar.CreateEx(this, TBSTYLE_FLAT, WS_CHILD | WS_VISIBLE | CBRS_TOP 
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC) ||
		!m_wndPSLOPToolBar.LoadToolBar(IDR_TOOLBAR_PARTICLESYS))
	{
		TRACE0("Failed to create toolbar\n");
		return -1;      // fail to create
	}

	CRect rect;
	m_wndPSLOPToolBar.GetWindowRect(&rect);
	rect.OffsetRect(1,0);
	this->RecalcLayout();

	m_wndPSLOPToolBar.EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_wndPSLOPToolBar, AFX_IDW_DOCKBAR_TOP, &rect);

//	DragAcceptFiles(TRUE);

	return 0;
}

BOOL CMainFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	if( !CMDIFrameWnd::PreCreateWindow(cs) )
		return FALSE;
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	//设置窗口尺寸  
	cs.cx = 1220;  
	cs.cy = 880;  

	//设置窗口位置  
	cs.x = 50;  
	cs.y = 60;   
	return TRUE;
}


// CMainFrame 诊断

#ifdef _DEBUG
void CMainFrame::AssertValid() const
{
	CMDIFrameWnd::AssertValid();
}

void CMainFrame::Dump(CDumpContext& dc) const
{
	CMDIFrameWnd::Dump(dc);
}

#endif //_DEBUG


// CMainFrame 消息处理程序


void CMainFrame::OnClose()
{
	CString strInfo = L"确定要关闭？";
	int res = MessageBox(strInfo, L"提示", MB_ICONQUESTION|MB_YESNO);
	if(res == IDNO)
		return;
	else
	{// res == IDYES 继续后面的处理...
	}

	CMDIFrameWnd::OnClose();
}

void CMainFrame::OnSize(UINT nType, int cx, int cy)
{
	CMDIFrameWnd::OnSize(nType, cx, cy);

	switch(nType)
	{
	case SIZE_MINIMIZED:
	case SIZE_MAXHIDE:
		theApp.m_bUpdateParticleSys = false;
		break;
	default:
		theApp.m_bUpdateParticleSys = true;
		break;
	}
	// TODO: 在此处添加消息处理程序代码
}

//BOOL CMainFrame::OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo)
//{
//	CDocument *pDoc = GetActiveDocument();
//	if (pDoc)
//	{
//		POSITION pos = pDoc->GetFirstViewPosition();
//		CView *pView = NULL;
//		while (pView = pDoc->GetNextView(pos))
//		{
//			if (//pView != GetActiveView() &&
//				pView->OnCmdMsg(nID, nCode, pExtra, pHandlerInfo))
//				return TRUE;
//		}
//	}
//	return CFrameWnd::OnCmdMsg(nID, nCode, pExtra, pHandlerInfo);
//}
void CMainFrame::OnDropFiles(HDROP hDropInfo)
{
	// TODO: Add your message handler code here and/or call default
// 	const int nfiles = DragQueryFile(hDropInfo, (UINT)-1, NULL, 0);
// 	XPASSERT(nfiles >= 1);
// 
// 	TCHAR fileName[MAX_PATH] = {0};
// 	
// 	for(int i=0; i<nfiles; i++)
// 	{
// 		DragQueryFile(hDropInfo, i, fileName, MAX_PATH);
// 	
// 		CFrameWnd* pchildWnd = GetActiveFrame(); 
// 		CDocument* pCurrentDoc = pchildWnd->GetActiveDocument();
// 
// 		if( (pCurrentDoc != NULL) ) //&&(lCount>5)
// 		{
// 			((CParticleSystemEditorDoc*)pCurrentDoc)->GetParticleParameterView()->OpenParticleSys(fileName);
// 		}
// 	
// 	}

	CMDIFrameWnd::OnDropFiles(hDropInfo);
}

void CMainFrame::OnUpdateBtnTga2dds(CCmdUI *pCmdUI)
{
	// TODO: 在此添加命令更新用户界面处理程序代码
	pCmdUI->Enable(TRUE);
}
