// MainFrm.cpp : CMainFrame ���ʵ��
//

#include "stdafx.h"
#include "PictureViewer.h"

#include "MainFrm.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CMainFrame

IMPLEMENT_DYNAMIC(CMainFrame, CFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CFrameWnd)
	ON_WM_CREATE()
	ON_WM_SETFOCUS()
	ON_WM_DROPFILES()
END_MESSAGE_MAP()


//static UINT indicators[] =
//{
//	ID_SEPARATOR,           // ״̬��ָʾ��
//	ID_INDICATOR_CAPS,
//	ID_INDICATOR_NUM,
//	ID_INDICATOR_SCRL,
//};


// CMainFrame ����/����

CMainFrame::CMainFrame()
	: m_hIcon(NULL)
	, m_hImageListCold(NULL)
	, m_hImageListHot(NULL)
{	
}

CMainFrame::~CMainFrame()
{
	if ( m_hIcon != NULL )
	{
		DestroyIcon(m_hIcon);
		m_hIcon = NULL;
	}

	if( m_hImageListHot != NULL )
	{
		ImageList_Destroy( m_hImageListHot );
	}

	if ( m_hImageListCold != NULL )
	{
		ImageList_Destroy( m_hImageListHot );
	}
}

int CMainFrame::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CFrameWnd::OnCreate(lpCreateStruct) == -1)
		return -1;
	// ����һ����ͼ��ռ�ÿ�ܵĹ�����
	if (!m_wndView.Create(NULL, NULL, AFX_WS_DEFAULT_VIEW,
		CRect(0, 0, 0, 0), this, AFX_IDW_PANE_FIRST, NULL))
	{
		TRACE0("δ�ܴ�����ͼ����\n");
		return -1;
	}
	
	if (!m_wndToolBar.CreateEx(this, TBSTYLE_FLAT, WS_CHILD | WS_VISIBLE | CBRS_ALIGN_BOTTOM
		| CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC) ||
		!m_wndToolBar.LoadToolBar(IDB_TOOLBAR_COLD))
	{
		TRACE0("δ�ܴ���������\n");
		return -1;      // δ�ܴ���
	}

	// Set up cold bar image lists.
	HINSTANCE hi = AfxGetInstanceHandle();
	m_hImageListCold = ImageList_LoadImage( hi,
		MAKEINTRESOURCE( IDB_TOOLBAR_COLD ), 16,27,
		0, IMAGE_BITMAP, LR_CREATEDIBSECTION );
	ASSERT( m_hImageListCold != NULL );
	m_wndToolBar.SendMessage(TB_SETIMAGELIST, 0, (LPARAM)m_hImageListCold);
	
	// Set up hot bar image lists.
	m_hImageListHot = ImageList_LoadImage( hi,
		MAKEINTRESOURCE( IDB_TOOLBAR_HOT ), 16,27,
		0, IMAGE_BITMAP, LR_CREATEDIBSECTION );
	ASSERT( m_hImageListHot != NULL );
	m_wndToolBar.SendMessage(TB_SETHOTIMAGELIST, 0, (LPARAM)m_hImageListHot);
	
	UINT nID, nStyle;
	int iImage;
	m_wndToolBar.GetButtonInfo(6, nID, nStyle, iImage );
	m_wndToolBar.SetButtonInfo(6, nID, nStyle, 2 );
	m_wndToolBar.GetButtonInfo(7, nID, nStyle, iImage );
	m_wndToolBar.SetButtonInfo(7, nID, nStyle, 3 );

	m_wndToolBar.GetButtonInfo(3, nID, nStyle, iImage );
	m_wndToolBar.SetButtonInfo(3, nID, nStyle, 5 );
	m_wndToolBar.GetButtonInfo(4, nID, nStyle, iImage );
	m_wndToolBar.SetButtonInfo(4, nID, nStyle, 6 );

	m_wndToolBar.GetButtonInfo(9, nID, nStyle, iImage );
	m_wndToolBar.SetButtonInfo(9, nID, nStyle, 24 );

	//if (!m_wndStatusBar.Create(this)
	//	//||
	//	//!m_wndStatusBar.SetIndicators(indicators,
	//	//  sizeof(indicators)/sizeof(UINT))
	//	  )
	//{
	//	TRACE0("δ�ܴ���״̬��\n");
	//	return -1;      // δ�ܴ���
	//}

	// TODO: �������Ҫ��������ͣ������ɾ��������
	//m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
	//EnableDocking(CBRS_ALIGN_ANY);
	//DockControlBar(&m_wndToolBar);

	CCommandLineInfo cmdInfo;
	AfxGetApp()->ParseCommandLine( cmdInfo );
	if( cmdInfo.m_nShellCommand == CCommandLineInfo::FileOpen )
	{
		m_wndView.ShowPicture( cmdInfo.m_strFileName );
	}

	DragAcceptFiles( TRUE );

	// ����Ӧ�ó���ͼ��
	HICON hIcon = LoadIcon( AfxGetInstanceHandle(), MAKEINTRESOURCE( IDR_MAINFRAME )  );
	if ( hIcon != NULL )
	{
		SetIcon( hIcon, TRUE ); // ��ͼ��
		SetIcon( hIcon, FALSE );// Сͼ��
		m_hIcon = hIcon;
	}

	return 0;
}

BOOL CMainFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	if( !CFrameWnd::PreCreateWindow(cs) )
		return FALSE;
	// TODO: �ڴ˴�ͨ���޸�
	//  CREATESTRUCT cs ���޸Ĵ��������ʽ

	cs.dwExStyle &= ~WS_EX_CLIENTEDGE;
	cs.lpszClass = AfxRegisterWndClass(0);
	return TRUE;
}


// CMainFrame ��Ϣ�������

void CMainFrame::OnSetFocus(CWnd* /*pOldWnd*/)
{
	// ������ǰ�Ƶ���ͼ����
	m_wndView.SetFocus();
}

BOOL CMainFrame::OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo)
{
	// ����ͼ��һ�γ��Ը�����
	if (m_wndView.OnCmdMsg(nID, nCode, pExtra, pHandlerInfo))
		return TRUE;

	// ����ִ��Ĭ�ϴ���
	return CFrameWnd::OnCmdMsg(nID, nCode, pExtra, pHandlerInfo);
}



void CMainFrame::OnDropFiles(HDROP hDropInfo)
{
	// TODO: Add your message handler code here and/or call default
	SetActiveWindow();      // activate us first !

	UINT nFiles = ::DragQueryFile(hDropInfo, (UINT)-1, NULL, 0);
	if ( nFiles < 1 )
	{
		::DragFinish(hDropInfo);
		return;
	}

	TCHAR szFileName[MAX_PATH+1] = {0};
	DragQueryFile( hDropInfo, 0, szFileName, MAX_PATH );
	m_wndView.ShowPicture( szFileName );

	::DragFinish(hDropInfo);
}
