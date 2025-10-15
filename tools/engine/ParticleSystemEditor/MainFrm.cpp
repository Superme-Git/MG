// MainFrm.cpp : CMainFrame ���ʵ��
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
	ID_SEPARATOR,           // ״̬��ָʾ��
	ID_INDICATOR_CAPS,
	ID_INDICATOR_NUM,
	ID_INDICATOR_SCRL,
};

// CMainFrame ����/����

CMainFrame::CMainFrame()
{
	// TODO: �ڴ���ӳ�Ա��ʼ������
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
		TRACE0("δ�ܴ���������\n");
		return -1;      // δ�ܴ���
	}

	//����״̬��
// 	if (!m_wndStatusBar.Create(this) || 
// 		!m_wndStatusBar.SetIndicators(indicators,
// 		  sizeof(indicators)/sizeof(UINT)))
// 	{
// 		TRACE0("δ�ܴ���״̬��\n");
// 		return -1;      // δ�ܴ���
// 	}

	// TODO: �������Ҫ��������ͣ������ɾ��������
	m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
	EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_wndToolBar);

	ShowControlBar(&m_wndToolBar,false,false);//���ع�����

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
	// TODO: �ڴ˴�ͨ���޸�
	//  CREATESTRUCT cs ���޸Ĵ��������ʽ

	//���ô��ڳߴ�  
	cs.cx = 1220;  
	cs.cy = 880;  

	//���ô���λ��  
	cs.x = 50;  
	cs.y = 60;   
	return TRUE;
}


// CMainFrame ���

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


// CMainFrame ��Ϣ�������


void CMainFrame::OnClose()
{
	CString strInfo = L"ȷ��Ҫ�رգ�";
	int res = MessageBox(strInfo, L"��ʾ", MB_ICONQUESTION|MB_YESNO);
	if(res == IDNO)
		return;
	else
	{// res == IDYES ��������Ĵ���...
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
	// TODO: �ڴ˴������Ϣ����������
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
	// TODO: �ڴ������������û����洦��������
	pCmdUI->Enable(TRUE);
}
