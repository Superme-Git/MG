// MainFrm.cpp : CMainFrame 类的实现
//

#include "stdafx.h"
#include "WinPFS.h"

#include "MainFrm.h"
#include "LeftView.h"
#include "WinPFSView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#define IDW_LIST_VIEW AFX_IDW_PANE_FIRST+1
// CMainFrame

IMPLEMENT_DYNCREATE(CMainFrame, CFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CFrameWnd)
	ON_WM_CREATE()
	ON_UPDATE_COMMAND_UI_RANGE(AFX_ID_VIEW_MINIMUM, AFX_ID_VIEW_MAXIMUM, &CMainFrame::OnUpdateViewStyles)
	ON_COMMAND_RANGE(AFX_ID_VIEW_MINIMUM, AFX_ID_VIEW_MAXIMUM, &CMainFrame::OnViewStyle)
	ON_NOTIFY( LVN_ITEMCOUNTUPDATED, IDW_LIST_VIEW, &CMainFrame::OnUpdateItemCount )
END_MESSAGE_MAP()

static UINT indicators[] =
{
	ID_SEPARATOR,           // 状态行指示器
	ID_INDICATOR_EXT,		// 文件目录
	ID_INDICATOR_ITEMCOUNT, // 项目统计
	//ID_INDICATOR_CAPS,
	//ID_INDICATOR_NUM,
	//ID_INDICATOR_SCRL,
};


// CMainFrame 构造/析构

CMainFrame::CMainFrame()
{
}

CMainFrame::~CMainFrame()
{
}

void ToolbarEnableShowButtonText( CToolBar& rToolBar, int width, int height )
{
	// Button数目，含Seperator
	int nCount = rToolBar.GetCount();
	if ( nCount == 0 )
	{
		return ;
	}

	for ( int nIndex = 0; nIndex < nCount ; nIndex ++ )
	{
		UINT nID;
		UINT nStyle;
		int iImage;
		rToolBar.GetButtonInfo(nIndex, nID, nStyle, iImage);
		if ( nID == (UINT)0 ) // 跳过Seperator
		{
			continue;
		}

		rToolBar.SetButtonStyle(nIndex, nStyle | BTNS_SHOWTEXT/* | BTNS_AUTOSIZE*/  );
		CString strText;
		strText.LoadString( nID );
		int nPos = strText.Find( _T('\n') );
		if ( nPos > 0 )
		{
			strText = strText.Mid( nPos + 1 );
		}

		rToolBar.SetButtonText(nIndex, strText);
	}

	// 重新计算Toolbar的高度
	CRect rcButton;
	rToolBar.GetItemRect( 0, &rcButton);
	SIZE szButton = { rcButton.Width(), rcButton.Height()};
	SIZE szImage = { width, height }; // Toolbar bitmap资源中每个button的大小
	rToolBar.SetSizes( szButton, szImage );
}

void LoadToolBarBitmap( HINSTANCE hinst, CImageList& il, UINT id)
{
	// 下面的方法创建的CBitmap对象为DDB对象，该类型的对象在16位色显示模式下，无法处理透明问题
	// 故而放弃，改用 LoadImage() 方法实现。
	// 2010-10-28, 2010-10-29
	// CBitmap bmp;
	// bmp.LoadBitmap( id);
	// il.Add( &bmp, (CBitmap*)NULL);
	HANDLE hImage = LoadImage( hinst, 
		MAKEINTRESOURCE( id ),
		IMAGE_BITMAP, 
		0, 0,
		LR_CREATEDIBSECTION);
	if( hImage != NULL )
	{
		AfxImageList_Add( il, (HBITMAP)hImage, NULL );
		DeleteObject( hImage );
	}
}

BOOL InitImageList( CImageList& il )
{
	HINSTANCE hinst = AfxGetApp()->m_hInstance;

	LoadToolBarBitmap( hinst, il, IDB_PROTECTED);
	LoadToolBarBitmap( hinst, il, IDB_TEST);
	LoadToolBarBitmap( hinst, il, IDB_VIEW);
	LoadToolBarBitmap( hinst, il, IDB_FIND);
	LoadToolBarBitmap( hinst, il, IDB_INFLATE);
	LoadToolBarBitmap( hinst, il, IDB_SCANVIRUS);
	LoadToolBarBitmap( hinst, il, IDB_DELETE);
	LoadToolBarBitmap( hinst, il, IDB_ADD);
	LoadToolBarBitmap( hinst, il, IDB_WIZARD);
	LoadToolBarBitmap( hinst, il, IDB_DELETE);
	LoadToolBarBitmap( hinst, il, IDB_INFORMATION);
	LoadToolBarBitmap( hinst, il, IDB_COMMENT);

	return TRUE;
}

int CMainFrame::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CFrameWnd::OnCreate(lpCreateStruct) == -1)
		return -1;
	
	if (!m_wndToolBar.CreateEx(this, 
		//TBSTYLE_FLAT | TBSTYLE_TRANSPARENT
		TBSTYLE_FLAT|TBSTYLE_AUTOSIZE, WS_CHILD | WS_VISIBLE | CBRS_TOP
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC
		) ||
		!m_wndToolBar.LoadToolBar(IDR_MAINFRAME))
	{
		TRACE0("未能创建工具栏\n");
		return -1;      // 未能创建
	}

	//if (!m_wndDlgBar.Create(this, IDR_MAINFRAME, 
	//	CBRS_ALIGN_TOP, AFX_IDW_DIALOGBAR))
	//{
	//	TRACE0("未能创建对话栏\n");
	//	return -1;		// 未能创建
	//}

	//if (!m_wndReBar.Create(this) ||
	//	!m_wndReBar.AddBar(&m_wndToolBar) ||
	//	!m_wndReBar.AddBar(&m_wndDlgBar))
	//{
	//	TRACE0("未能创建 Rebar\n");
	//	return -1;      // 未能创建
	//}

	if (!m_wndStatusBar.Create(this) ||
		!m_wndStatusBar.SetIndicators(indicators,
		  sizeof(indicators)/sizeof(UINT)))
	{
		TRACE0("未能创建状态栏\n");
		return -1;      // 未能创建
	}

	m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
	EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_wndToolBar);
	
	CBitmap bmp;
	bmp.LoadBitmap( IDB_PROTECTED);
	BITMAP bp;
	bmp.GetBitmap( &bp); // 根据资源获取按钮尺寸
	if ( !m_ilToolBar.Create( bp.bmWidth, bp.bmHeight, ILC_COLOR32, 4, 4) )
	{
		return -1;
	}
	
	// 装载工具条图片
	InitImageList( m_ilToolBar );

	m_wndToolBar.GetToolBarCtrl().SetImageList( &m_ilToolBar );

	ToolbarEnableShowButtonText( m_wndToolBar, bp.bmWidth, bp.bmHeight );
	
	// 初始化状态栏第二栏的尺寸和内容（显示工作目录）。
	UINT nStyle = m_wndStatusBar.GetPaneStyle(1);
	m_wndStatusBar.SetPaneInfo(1, ID_INDICATOR_EXT, nStyle, 480 );
	WCHAR szDirectoryBuffer1[] = L"工作目录：";
	WCHAR szDirectoryBuffer[MAX_PATH + _countof(szDirectoryBuffer1)] = L"工作目录：";
	::GetCurrentDirectoryW(MAX_PATH, szDirectoryBuffer + wcslen(szDirectoryBuffer) );
	m_wndStatusBar.SetPaneText(1, szDirectoryBuffer, 1 );

	return 0;
}

BOOL CMainFrame::OnCreateClient(LPCREATESTRUCT /*lpcs*/,
	CCreateContext* pContext)
{
	// 创建拆分窗口
	if (!m_wndSplitter.CreateStatic(this, 1, 2))
		return FALSE;

	if (!m_wndSplitter.CreateView(0, 0, RUNTIME_CLASS(CLeftView), CSize(200, 200), pContext) ||
		!m_wndSplitter.CreateView(0, 1, RUNTIME_CLASS(CWinPFSView), CSize(200, 300), pContext))
	{
		m_wndSplitter.DestroyWindow();
		return FALSE;
	}
	CLeftView* leftView = (CLeftView*)m_wndSplitter.GetPane( 0, 0);
	CWinPFSView* pfsView = (CWinPFSView*)m_wndSplitter.GetPane( 0, 1);
	leftView->m_pPFSView = pfsView;
	pfsView->m_pLeftView = leftView;

	return TRUE;
}

BOOL CMainFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	if( !CFrameWnd::PreCreateWindow(cs) )
		return FALSE;
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	cs.style = WS_OVERLAPPED | WS_CAPTION | FWS_ADDTOTITLE
		 | WS_THICKFRAME | WS_MINIMIZEBOX | WS_MAXIMIZEBOX | WS_MAXIMIZE | WS_SYSMENU;

	return TRUE;
}

// CMainFrame 消息处理程序


CWinPFSView* CMainFrame::GetRightPane()
{
	CWnd* pWnd = m_wndSplitter.GetPane(0, 1);
	CWinPFSView* pView = DYNAMIC_DOWNCAST(CWinPFSView, pWnd);
	return pView;
}

void CMainFrame::OnUpdateViewStyles(CCmdUI* pCmdUI)
{
	if (!pCmdUI)
		return;

	// TODO: 自定义或扩展此代码以处理“视图”菜单中的选项

	CWinPFSView* pView = GetRightPane(); 

	// 如果右窗格尚未创建或者不是视图，
	// 则在范围内禁用命令

	if (pView == NULL)
		pCmdUI->Enable(FALSE);
	else
	{
		DWORD dwStyle = pView->GetStyle() & LVS_TYPEMASK;

		// 如果命令是 ID_VIEW_LINEUP，则只有在处于
		// LVS_ICON 或 LVS_SMALLICON 模式时才启用命令

		if (pCmdUI->m_nID == ID_VIEW_LINEUP)
		{
			if (dwStyle == LVS_ICON || dwStyle == LVS_SMALLICON)
				pCmdUI->Enable();
			else
				pCmdUI->Enable(FALSE);
		}
		else
		{
			// 否则，使用点线来反映视图的样式
			pCmdUI->Enable();
			BOOL bChecked = FALSE;

			switch (pCmdUI->m_nID)
			{
			case ID_VIEW_DETAILS:
				bChecked = (dwStyle == LVS_REPORT);
				break;

			case ID_VIEW_SMALLICON:
				bChecked = (dwStyle == LVS_SMALLICON);
				break;

			case ID_VIEW_LARGEICON:
				bChecked = (dwStyle == LVS_ICON);
				break;

			case ID_VIEW_LIST:
				bChecked = (dwStyle == LVS_LIST);
				break;

			default:
				bChecked = FALSE;
				break;
			}

			pCmdUI->SetRadio(bChecked ? 1 : 0);
		}
	}
}


void CMainFrame::OnViewStyle(UINT nCommandID)
{
	// TODO: 自定义或扩展此代码以处理“视图”菜单中的选项
	CWinPFSView* pView = GetRightPane();

	// 如果右窗格已创建而且是 CWinPFSView，
	// 则处理菜单命令...
	if (pView != NULL)
	{
		DWORD dwStyle = -1;

		switch (nCommandID)
		{
		case ID_VIEW_LINEUP:
			{
				// 要求列表控件与网格对齐
				CListCtrl& refListCtrl = pView->GetListCtrl();
				refListCtrl.Arrange(LVA_SNAPTOGRID);
			}
			break;

		// 其他命令更改列表控件上的样式
		case ID_VIEW_DETAILS:
			dwStyle = LVS_REPORT;
			break;

		case ID_VIEW_SMALLICON:
			dwStyle = LVS_SMALLICON;
			break;

		case ID_VIEW_LARGEICON:
			dwStyle = LVS_ICON;
			break;

		case ID_VIEW_LIST:
			dwStyle = LVS_LIST;
			break;
		}

		// 更改样式；窗口将自动重新绘制
		if (dwStyle != -1)
			pView->ModifyStyle(LVS_TYPEMASK, dwStyle);
	}
}

void CMainFrame::OnUpdateItemCount( NMHDR* nmhdr, LRESULT* pResult )
{
	NMLISTITEMCOUNT* pHdr = (NMLISTITEMCOUNT*)nmhdr;
	CString str;
	str.Format( L"共%d项", pHdr->count );
	m_wndStatusBar.SetPaneText( 2, str );
	*pResult = 1;
}

void CMainFrame::OnUpdateFrameTitle(BOOL bAddToTitle)
{
	CDocument* pDocument = GetActiveDocument();
	if (pDocument != NULL)
	{
		// 设置状态栏第二栏的尺寸和内容（显示工作目录或者当前文档路径名）。
		CString strFilePath = pDocument->GetPathName();
		if( strFilePath.GetLength() == 0 )
		{
			WCHAR szDirectoryBuffer1[] = L"工作目录：";
			WCHAR szDirectoryBuffer[MAX_PATH + _countof(szDirectoryBuffer1)] = L"工作目录：";
			::GetCurrentDirectoryW(MAX_PATH, szDirectoryBuffer + wcslen(szDirectoryBuffer) );
			m_wndStatusBar.SetPaneText(1, szDirectoryBuffer, 1 );
		}
		else
		{
			m_wndStatusBar.SetPaneText(1, strFilePath, 1 );
		}
	}

	CFrameWnd::OnUpdateFrameTitle(bAddToTitle);
}
