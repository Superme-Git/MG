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
	// ����������ؼ�
	//
	CRect rcBrowser(0,0,1,1);
	m_htmlView = (CWebBrowserCtrl*)RUNTIME_CLASS(CWebBrowserCtrl)->CreateObject();
	if( !m_htmlView->Create( NULL, NULL, WS_CHILD | WS_VISIBLE, rcBrowser, this, NULL ) )
	{
		//ReportMessage(L"����HtmlView�ؼ�ʧ��!");
		ReportMessage( GlobalGetText( ERR_CREATE_HTMLVIEW).c_str());
		return -1;
	}

	//////////////////////////////////////////////////////////////////////////
	// ��Ϊ���ڱ���û�б�������������Ҫ�ֶ����ó�����⣬
	// ������ʾ���������С�_T("ħ�����ɸ��³���")
	//
	// Ϊ��֧�ֹ��ʻ���֧�ֶ����Ʒ���������ȴ������ļ��ж�ȡ���ڱ���.
	// ע�⣬������ֱ��뱣֤��launcher.xml�е���ͬID���ַ�����ͬ��
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
		//ReportMessage(L"����Launcher��Դ��ʧ��!");
		ReportMessage( GlobalGetText( ERR_LOAD_UIRESOURCE).c_str());
		return -1;
	}

	//
	// ���ÿؼ�����崰�ڣ�ͬʱָ��UI��Դ����Ŀ¼�Ͳ��������ļ���
	//
	if( !m_layoutManager.SetPanel( this ) )
	{
		//ReportMessage(L"����Launcher��Դ��ʧ��!");
		ReportMessage( GlobalGetText( ERR_LOAD_UIRESOURCE).c_str());
		return -1;
	}
	
	// ��ȡ�����ļ������õ�GlobalStatus��StepMessage 2����̬�ı��ؼ���ǰ���ı�ɫ
	if( !m_layoutManager.GetControlColor(_T("GlobalStatus"), m_globalStatusControlColor ) )
	{
		m_globalStatusControlColor = RGB(245,10,40); // ȱʡ��ɫֵ 
	}
	if( !m_layoutManager.GetControlColor( _T("StepMessage"), m_stepMessageControlColor ) )
	{
		m_stepMessageControlColor = RGB(10,40,245); // ȱʡ��ɫֵ 
	}
	
	// ����GlobalStatus��StepMessage 2����̬�ı��ؼ��ı���ˢ
	COLORREF clrBKBrush;
	if ( !m_layoutManager.GetControlBkColor(_T("GlobalStatus"), clrBKBrush) )
	{
		clrBKBrush = RGB(208,202,224);// ȱʡ������ɫ 
	}
	m_hBKBrush = CreateSolidBrush(clrBKBrush);

	return 0;
}

BOOL CPatcherMainDialog::OnInitDialog()
{
	CDialog::OnInitDialog();

	// ����ͨ�������ļ�ȷ�����֡�
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

	// ���ô˶Ի����ͼ�ꡣ��Ӧ�ó��������ڲ��ǶԻ���ʱ����ܽ��Զ�
	//  ִ�д˲���
	if ( m_hIconLarge != NULL && m_hIconSmall != NULL )
	{
		SetIcon(m_hIconLarge, TRUE);			// ���ô�ͼ��
		SetIcon(m_hIconSmall, FALSE);		// ����Сͼ��
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
	// WebBrowser��λ����ʼ��
	//
	CRect rcBrowser;
	if( !m_layoutManager.GetControlRect( _T("WebControl"), rcBrowser ) )
	{
		// ��ʷԭ��������ֲ���Ҫ���룬��������ҳ��������ģ����á�WebControl����
		// ���Ƕ�����δ�޸ĵ���Դ������һ�Ρ���ҳ�򡱡�
		if( !m_layoutManager.GetControlRect( _T("��ҳ��"), rcBrowser ) )
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
	// ΪGLOBAL_STATUS��̬��λ
	//
	CRect rcGlobalStatus, rcStepMessage;
	if( m_layoutManager.GetControlRect(_T("GlobalStatus"), rcGlobalStatus) )
	{
		m_globalStatus.MoveWindow(rcGlobalStatus, FALSE);
	}

	//
	// ΪSTEP_MESSAGE��̬��λ
	//
	if( m_layoutManager.GetControlRect(_T("StepMessage"), rcStepMessage) )
	{
		m_stepMessage.MoveWindow(rcStepMessage, FALSE);
	}

	//
	// �������ؼ���ʼ��
	//

	// ��ʾ�ٷֱȵ��������ȳ�ʼ����������ApplyProgressCtrl���п���ͨ�������޸����������
	m_updatePgrsCtrl.SetShowPercent( TRUE ); 	
	VERIFY( m_layoutManager.ApplyProgressCtrl( IDC_DOWNLOAD_PROGRESS, m_downloadPgrsCtrl ) );
	VERIFY( m_layoutManager.ApplyProgressCtrl( IDC_UPDATE_PROGRESS, m_updatePgrsCtrl ) );

	m_downloadPgrsCtrl.SetRange( 0, 100 );
	m_downloadPgrsCtrl.SetStep( 1 );
	m_updatePgrsCtrl.SetRange( 0, 100 );
	m_updatePgrsCtrl.SetStep( 20 );

	//
	// ��ť�ؼ���ʼ��
	//
	VERIFY( m_layoutManager.ApplyButton(IDC_CHECK_UPDATE, m_btnCheckUpdate ) );
	VERIFY( m_layoutManager.ApplyButton(IDC_MANUAL_UPDATE, m_btnManualUpdate ) );
	VERIFY( m_layoutManager.ApplyButton(IDC_REPAIR_SETUP, m_btnRepairSetup ) );
	VERIFY( m_layoutManager.ApplyButton(IDC_LAUNCH_GAME, m_btnLaunchGame ) );
	VERIFY( m_layoutManager.ApplyButton(IDC_MINIMIZE, m_btnMinimize ) );
	VERIFY( m_layoutManager.ApplyButton(IDCANCEL, m_btnClose ) );

	// Button ��ʼ״̬
	m_btnLaunchGame.SetFocus();
	
	return FALSE;  // ���ǽ��������õ��ؼ������򷵻� TRUE
}

// �����Ի��������С����ť������Ҫ����Ĵ���
//  �����Ƹ�ͼ�ꡣ����ʹ���ĵ�/��ͼģ�͵� MFC Ӧ�ó���
//  �⽫�ɿ���Զ���ɡ�
void CPatcherMainDialog::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // ���ڻ��Ƶ��豸������

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// ʹͼ���ڹ��������о���
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// ����ͼ��
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

//���û��϶���С������ʱϵͳ���ô˺���ȡ�ù����ʾ��
//
HCURSOR CPatcherMainDialog::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

void CPatcherMainDialog::OnBnClickedMinimize()
{
	// ��С���Ի��򴰿ڵ�������
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
		// ���������д���ͻ������ƶ�����
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

	// ���⴦��CDIYButton�����ʾ��Ϣ�����ǵ�������ʾ������XML���塣
	CWnd *pChild = CWnd::FromHandlePermanent((HWND)nID);
	if( pChild != NULL )
	{
		if( pChild->IsKindOf(RUNTIME_CLASS(CDIYButton)))
		{
			// �Ի水ť
			LPCTSTR lpszTipInfo = ((CDIYButton*)pChild)->GetTipInfo();
			if( lpszTipInfo != NULL && lpszTipInfo[0] != _T('\0'))
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), lpszTipInfo  );
				return TRUE;
			}
		}
		else if( pChild->IsKindOf(RUNTIME_CLASS(CODProgressCtrl) ) )
		{
			// �Ի������
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

	// ��ʾ��Ϣ
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
