// PatcherMainDialog.cpp : implementation file
//

#include "stdafx.h"
#include "Previewer.h"
#include "PatcherMainDialog.h"
#include "LauncherUtils.h" // for ReportMessage()
#include "LauncherConfig.h"
#include "TextManager.h"
#include "TextID.h"

// CPatcherMainDialog dialog

IMPLEMENT_DYNAMIC(CPatcherMainDialog, CDialog)

CPatcherMainDialog::CPatcherMainDialog(CWnd* pParent /*=NULL*/)
	: CDialog(CPatcherMainDialog::IDD, pParent)
{
	m_hIcon = NULL;
	m_hIconLarge = NULL;
	m_hIconSmall = NULL;
	m_hBKBrush = NULL;
	m_globalStatusControlColor = 0;
	m_stepMessageControlColor = 0;
}

CPatcherMainDialog::~CPatcherMainDialog()
{
	if( m_hBKBrush != NULL )
	{
		DeleteObject( m_hBKBrush );
		m_hBKBrush = NULL;
	}
}

void CPatcherMainDialog::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}


BEGIN_MESSAGE_MAP(CPatcherMainDialog, CDialog)
	ON_COMMAND(IDC_CHECK_UPDATE, &CPatcherMainDialog::OnBnClickedCheckUpdate)
	ON_COMMAND(IDC_LAUNCH_GAME, &CPatcherMainDialog::OnBnClickedLaunchGame)
	ON_COMMAND(IDC_MINIMIZE, &CPatcherMainDialog::OnBnClickedMinimize)
	ON_COMMAND(IDC_MANUAL_UPDATE, &CPatcherMainDialog::OnManualUpdate)
	ON_COMMAND(IDC_REPAIR_SETUP, &CPatcherMainDialog::OnRepairSetup)
	ON_NOTIFY_EX(TTN_NEEDTEXT, 0, &CPatcherMainDialog::OnToolTipNotify)
	ON_WM_PAINT()
	ON_WM_NCHITTEST()
	ON_WM_CREATE()
	ON_WM_ERASEBKGND()		
	ON_WM_DESTROY()
	ON_WM_CLOSE()
	ON_WM_CTLCOLOR()
END_MESSAGE_MAP()

// CPatcherMainDialog message handlers
int CPatcherMainDialog::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CDialog::OnCreate(lpCreateStruct) == -1)
		return -1;

	EnableToolTips();

	//////////////////////////////////////////////////////////////////////////
	//
	// 创建浏览器控件
	//
	CRect rcBrowser(0,0,1,1);
	m_htmlView = (CWebBrowserCtrl*)RUNTIME_CLASS(CWebBrowserCtrl)->CreateObject();
	if( !m_htmlView->Create( NULL, NULL, WS_CHILD | WS_VISIBLE, rcBrowser, this, NULL ) )
	{
		//ReportMessage(L"创建HtmlView控件失败!");
		ReportMessage( GlobalGetText( ERR_CREATE_HTMLVIEW).c_str());
		return -1;
	}

	//////////////////////////////////////////////////////////////////////////
	// 因为窗口本身没有标题栏，所以需要手动设置程序标题，
	// 用以显示在任务栏中。_T("魔幻修仙更新程序")
	//
	// 为了支持国际化和支持多个产品，这里优先从配置文件中读取窗口标题.
	// 注意，这个名字必须保证和launcher.xml中的相同ID的字符串相同。
	const std::wstring& strCaptionPatcher = GlobalGetText(TOK_PRODUCT_PATCHER);
	if( !strCaptionPatcher.empty() )
	{
		SetWindowText(strCaptionPatcher.c_str());
	}
	else
	{
		CString strCaption(MAKEINTRESOURCE(IDR_MAINFRAME));
		SetWindowText(strCaption);
	}

	if( !m_layoutManager.Load( GetLauncherUIResourceDir(), GetLauncherUILayoutFile(), L"Launcher" ) )
	{
		//ReportMessage(L"加载Launcher资源包失败!");
		ReportMessage( GlobalGetText( ERR_LOAD_UIRESOURCE).c_str());
		return -1;
	}

	//
	// 设置控件的面板窗口，同时指定UI资源包的目录和布局配置文件名
	//
	if( !m_layoutManager.SetPanel( this ) )
	{
		//ReportMessage(L"加载Launcher资源包失败!");
		ReportMessage( GlobalGetText( ERR_LOAD_UIRESOURCE).c_str());
		return -1;
	}
	
	// 获取配置文件中配置的GlobalStatus和StepMessage 2个静态文本控件的前景文本色
	if( !m_layoutManager.GetControlColor(_T("GlobalStatus"), m_globalStatusControlColor ) )
	{
		m_globalStatusControlColor = RGB(245,10,40); // 缺省颜色值 
	}
	if( !m_layoutManager.GetControlColor( _T("StepMessage"), m_stepMessageControlColor ) )
	{
		m_stepMessageControlColor = RGB(10,40,245); // 缺省颜色值 
	}
	
	// 创建GlobalStatus和StepMessage 2个静态文本控件的背景刷
	COLORREF clrBKBrush;
	if ( !m_layoutManager.GetControlBkColor(_T("GlobalStatus"), clrBKBrush) )
	{
		clrBKBrush = RGB(208,202,224);// 缺省背景颜色 
	}
	m_hBKBrush = CreateSolidBrush(clrBKBrush);

	return 0;
}

BOOL CPatcherMainDialog::OnInitDialog()
{
	CDialog::OnInitDialog();

	// 首先通过配置文件确定名字。
	const std::wstring& strCaptionPatcher = GlobalGetText(TOK_PRODUCT_PATCHER);
	if( !strCaptionPatcher.empty() )
	{
		m_hIconLarge = m_layoutManager.LoadIcon(strCaptionPatcher.c_str(), TRUE);
		m_hIconSmall = m_layoutManager.LoadIcon(strCaptionPatcher.c_str(), FALSE);
	}
	
	if ( m_hIconLarge == NULL || m_hIconSmall == NULL )
	{
		CString strAppName(MAKEINTRESOURCE(IDR_MAINFRAME));
		m_hIconLarge = m_layoutManager.LoadIcon(strAppName, TRUE);
		m_hIconSmall = m_layoutManager.LoadIcon(strAppName, FALSE);
	}

	// 设置此对话框的图标。当应用程序主窗口不是对话框时，框架将自动
	//  执行此操作
	if ( m_hIconLarge != NULL && m_hIconSmall != NULL )
	{
		SetIcon(m_hIconLarge, TRUE);			// 设置大图标
		SetIcon(m_hIconSmall, FALSE);		// 设置小图标
		m_hIcon = m_hIconLarge;
	}
	else
	{
		// Use the default one, which need not to be destroyed
		m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);

		if( m_hIconLarge != NULL )
			SetIcon(m_hIconLarge, TRUE);
		else
			SetIcon(m_hIcon, TRUE);

		if ( m_hIconSmall != NULL )
			SetIcon(m_hIconSmall, FALSE);
		else
			SetIcon(m_hIcon, FALSE);
	}
	
	// 
	// WebBrowser定位并初始化
	//
	CRect rcBrowser;
	if( !m_layoutManager.GetControlRect( _T("WebControl"), rcBrowser ) )
	{
		// 历史原因，这个名字不需要翻译，废弃“网页框”这个中文，改用“WebControl”。
		// 但是对于尚未修改的资源，尝试一次“网页框”。
		if( !m_layoutManager.GetControlRect( _T("网页框"), rcBrowser ) )
		{
			HWND hStatic = ::GetDlgItem( GetSafeHwnd(), IDC_WEBBROWSER );
			::GetWindowRect( hStatic , &rcBrowser );
			ScreenToClient( &rcBrowser );
		}
	}
	
	m_htmlView->MoveWindow( &rcBrowser, TRUE );
	m_htmlView->Navigate2( GetLauncherWebPageUrl() );

	// ForbidWebBrowseScrollBar();
	//_TryStartDownloadWebPageThread();

	m_globalStatus.SubclassDlgItem(IDC_GLOBAL_STATUS, this );
	m_stepMessage.SubclassDlgItem(IDC_STEP_MESSAGE, this );

	//
	// 为GLOBAL_STATUS静态框定位
	//
	CRect rcGlobalStatus, rcStepMessage;
	if( m_layoutManager.GetControlRect(_T("GlobalStatus"), rcGlobalStatus) )
	{
		m_globalStatus.MoveWindow(rcGlobalStatus, FALSE);
	}

	//
	// 为STEP_MESSAGE静态框定位
	//
	if( m_layoutManager.GetControlRect(_T("StepMessage"), rcStepMessage) )
	{
		m_stepMessage.MoveWindow(rcStepMessage, FALSE);
	}

	//
	// 进度条控件初始化
	//

	// 显示百分比的特征首先初始化，这样，ApplyProgressCtrl才有可能通过配置修改这个特征。
	m_updatePgrsCtrl.SetShowPercent( TRUE ); 	
	VERIFY( m_layoutManager.ApplyProgressCtrl( IDC_DOWNLOAD_PROGRESS, m_downloadPgrsCtrl ) );
	VERIFY( m_layoutManager.ApplyProgressCtrl( IDC_UPDATE_PROGRESS, m_updatePgrsCtrl ) );

	m_downloadPgrsCtrl.SetRange( 0, 100 );
	m_downloadPgrsCtrl.SetStep( 1 );
	m_updatePgrsCtrl.SetRange( 0, 100 );
	m_updatePgrsCtrl.SetStep( 20 );

	//
	// 按钮控件初始化
	//
	VERIFY( m_layoutManager.ApplyButton(IDC_CHECK_UPDATE, m_btnCheckUpdate ) );
	VERIFY( m_layoutManager.ApplyButton(IDC_MANUAL_UPDATE, m_btnManualUpdate ) );
	VERIFY( m_layoutManager.ApplyButton(IDC_REPAIR_SETUP, m_btnRepairSetup ) );
	VERIFY( m_layoutManager.ApplyButton(IDC_LAUNCH_GAME, m_btnLaunchGame ) );
	VERIFY( m_layoutManager.ApplyButton(IDC_MINIMIZE, m_btnMinimize ) );
	VERIFY( m_layoutManager.ApplyButton(IDCANCEL, m_btnClose ) );

	// Button 初始状态
	m_btnLaunchGame.SetFocus();
	
	return FALSE;  // 除非将焦点设置到控件，否则返回 TRUE
}

// 如果向对话框添加最小化按钮，则需要下面的代码
//  来绘制该图标。对于使用文档/视图模型的 MFC 应用程序，
//  这将由框架自动完成。
void CPatcherMainDialog::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // 用于绘制的设备上下文

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// 使图标在工作矩形中居中
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// 绘制图标
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CPaintDC dc(this);
		if (!m_layoutManager.OnPanelPaint(&dc) )
		{
			CDialog::OnPaint();
		}
	}
}

//当用户拖动最小化窗口时系统调用此函数取得光标显示。
//
HCURSOR CPatcherMainDialog::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

void CPatcherMainDialog::OnBnClickedMinimize()
{
	// 最小化对话框窗口到托盘区
	// ShowWindow(SW_HIDE);
}

void CPatcherMainDialog::OnManualUpdate()
{
}

void CPatcherMainDialog::OnRepairSetup()
{
}
void CPatcherMainDialog::OnClose()
{
}
void CPatcherMainDialog::OnBnClickedLaunchGame()
{
}
void CPatcherMainDialog::OnBnClickedCheckUpdate()
{
}

LRESULT CPatcherMainDialog::OnNcHitTest(CPoint point)
{
	LRESULT nRet = CDialog::OnNcHitTest(point);
	if(nRet == HTCLIENT)
	{
		// 允许鼠标点中窗体客户区域移动窗口
		nRet = HTCAPTION;
	}
	return nRet;
}

BOOL CPatcherMainDialog::OnToolTipNotify( UINT id, NMHDR * pNMHDR, LRESULT * pResult )
{
	TOOLTIPTEXT *pTTT = (TOOLTIPTEXT *)pNMHDR;
	UINT_PTR nID = pNMHDR->idFrom;

	if ((pTTT->uFlags & TTF_IDISHWND) == 0 )
	{
		return FALSE;
	}

	// idFrom is actually the HWND of the tool
	int iID = ::GetDlgCtrlID((HWND)nID);
	if( iID == 0 )
	{
		return FALSE;
	}

	// 特殊处理CDIYButton类的提示信息，它们的悬浮提示来自于XML定义。
	CWnd *pChild = CWnd::FromHandlePermanent((HWND)nID);
	if( pChild != NULL )
	{
		if( pChild->IsKindOf(RUNTIME_CLASS(CDIYButton)))
		{
			// 自绘按钮
			LPCTSTR lpszTipInfo = ((CDIYButton*)pChild)->GetTipInfo();
			if( lpszTipInfo != NULL && lpszTipInfo[0] != _T('\0'))
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), lpszTipInfo  );
				return TRUE;
			}
		}
		else if( pChild->IsKindOf(RUNTIME_CLASS(CODProgressCtrl) ) )
		{
			// 自绘进度条
			LPCTSTR lpszTipInfo = ((CODProgressCtrl*)pChild)->GetTipInfo();
			if( lpszTipInfo != NULL && lpszTipInfo[0] != _T('\0'))
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), lpszTipInfo  );
				return TRUE;
			}
		}
	}

	CString strName;
	GetDlgItemText(iID, strName );
	if ( !strName.IsEmpty() )
	{
		_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strName );
		return TRUE;
	}

	// 提示信息
	if( strName.LoadString( iID ) )
	{
		if( !strName.IsEmpty() )
		{
			int iPos = strName.Find( _T("\n") );
			if ( iPos > 0 )
			{
				strName = strName.Mid( iPos + 1 );
			}

			CStringW strTipInfo;
			if( m_layoutManager.GetControlTipInfo( strName, strTipInfo ) )
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strTipInfo);
			}
			else
			{
				if ( iPos > 0 )
				{
					_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strName );
				}
				else
				{
					pTTT->lpszText = MAKEINTRESOURCE(iID);
					pTTT->hinst = AfxGetResourceHandle();
				}
			}
		}
		else
		{
			pTTT->lpszText = MAKEINTRESOURCE(iID);
			pTTT->hinst = AfxGetResourceHandle();
		}
	}

	return(TRUE);
}

BOOL CPatcherMainDialog::OnEraseBkgnd(CDC* pDC)
{
	return TRUE; 
}

void CPatcherMainDialog::OnDestroy()
{
	CDialog::OnDestroy();

	if ( m_hIconLarge != NULL )
	{
		DestroyIcon( m_hIconLarge );
		m_hIconLarge = NULL;
	}

	if ( m_hIconSmall != NULL )
	{
		DestroyIcon( m_hIconSmall );
		m_hIconSmall = NULL;
	}
}

HBRUSH CPatcherMainDialog::OnCtlColor(CDC* pDC, CWnd* pWnd, UINT nCtlColor)
{
	if ( nCtlColor == CTLCOLOR_STATIC  )
	{
		if( pWnd == &m_globalStatus ) 
		{
			pDC->SetBkMode( TRANSPARENT );
			pDC->SetTextColor( m_globalStatusControlColor ); 
			return m_hBKBrush;
		}
		else if( pWnd == &m_stepMessage ) 
		{
			pDC->SetBkMode( TRANSPARENT );
			pDC->SetTextColor( m_stepMessageControlColor ); 
			return m_hBKBrush;
		}
	}

	HBRUSH hbr = CDialog::OnCtlColor(pDC, pWnd, nCtlColor);
	// You can change any attributes of the DC here
	// Return a different brush if the default is not desired
	return hbr;
}
