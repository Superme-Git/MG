// SelectServerDialog.cpp : implementation file
//

#include "stdafx.h"
#include "SelectServerDialog.h"
#include "LauncherConfig.h"
#include "LauncherUtils.h"
#include "GlobalFunction.h"
#include "../../pfs/versionmetafile.h"

// CSelectServerDialog dialog

IMPLEMENT_DYNAMIC(CSelectServerDialog, CDialog)

CSelectServerDialog::CSelectServerDialog(
	const PFSX::CVersionInfo& rVersionInfo, CWnd* pParent /*=NULL*/)
	: CDialog(CSelectServerDialog::IDD, pParent)
	, m_rVersionInfo( rVersionInfo )
{
}

CSelectServerDialog::~CSelectServerDialog()
{
}

void CSelectServerDialog::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CSelectServerDialog, CDialog)
	ON_WM_CREATE()
	ON_WM_PAINT()
	ON_BN_CLICKED(IDC_OK, &CSelectServerDialog::OnBnClickedOk)
	ON_BN_CLICKED(IDC_CANCEL, &CSelectServerDialog::OnBnClickedCancel)
	ON_BN_CLICKED(IDC_COMBOBUTTON, &CSelectServerDialog::OnBnClickedCombobutton)
	ON_BN_CLICKED(IDC_OPEN_WEBPAGE, &CSelectServerDialog::OnBnClickedOpenWebpage)
	ON_WM_NCHITTEST()
	ON_WM_LBUTTONUP()
	ON_WM_NCLBUTTONDOWN()
	ON_MESSAGE( WM_ITEMCHANGED, &CSelectServerDialog::OnItemChanged)
	ON_NOTIFY_EX( TTN_NEEDTEXT, 0, &CSelectServerDialog::OnToolTipNotify)
END_MESSAGE_MAP()

// CSelectServerDialog message handlers

int CSelectServerDialog::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CDialog::OnCreate(lpCreateStruct) == -1)
		return -1;

	EnableToolTips();

	//
	// ���ÿؼ�����崰�ڣ�ͬʱָ��UI��Դ����Ŀ¼�Ͳ��������ļ���
	//
	if( !m_layoutManager.Load( GetLauncherUIResourceDir(), 
		GetSelectServerDlgUILayoutFile(), L"SelectServerDlg" ) )
	{
		ReportMessage(L"Load file %s failed!", GetSelectServerDlgUILayoutFile().GetString() );
		return -1;
	}

	//
	// ��Ⱦ��崰��
	//
	if( !m_layoutManager.SetPanel( this ) )
	{
		ReportMessage(L"Load file %s failed!", GetSelectServerDlgUILayoutFile().GetString() );
		return -1;
	}

	return 0;
}

BOOL CSelectServerDialog::OnInitDialog()
{
	CDialog::OnInitDialog();

	//
	// ��ť�ؼ���ʼ��
	//
	m_layoutManager.ApplyButton( IDC_OPEN_WEBPAGE, m_btnOpenWebpage );
	m_layoutManager.ApplyButton( IDC_OK, m_btnOk );
	m_layoutManager.ApplyButton( IDC_CANCEL, m_btnCancel );
	m_layoutManager.ApplyButton( IDC_COMBOBUTTON, m_btnDroplist);
	m_layoutManager.GetControlRect( _T("ComboStatic"), m_rcCombo);
	m_layoutManager.GetControlColor( _T("ComboStatic"), m_clrCombo);
	m_layoutManager.GetControlRect( _T("DropList"), m_rcDropList);

	bool bAnyNetworkNamed = false;
	
	std::set<std::wstring> networks; // �����ظ���

	// �������·������ṩ�İ汾�����б�
	const PFSX::CVersionInfo::VersionUpdateInfoMap & rMap = m_rVersionInfo.GetUpdateInfoMap();
	for( PFSX::CVersionInfo::VersionUpdateInfoMap::const_iterator itr = rMap.begin();
		itr != rMap.end(); ++ itr)
	{
		if( ( itr->second.Type % PFSX::VERSION_COUNT ) == PFSX::VERSION_DEFAULT )
		{
			if( !itr->second.Network.empty() )
			{
				if( networks.find(itr->second.Network) == networks.end() )
				{
					networks.insert(itr->second.Network);
					m_lcDrop.AddString( itr->second.Network.c_str() );
					bAnyNetworkNamed = true;
				}
			}
		}
	}

	// ���û���κ������������ṩ�̣�����URL���ṩ������������IP��ַ��
	if( !bAnyNetworkNamed )
	{
		for( PFSX::CVersionInfo::VersionUpdateInfoMap::const_iterator itr = rMap.begin();
			itr != rMap.end(); ++ itr)
		{
			if( ( itr->second.Type % PFSX::VERSION_COUNT ) == PFSX::VERSION_DEFAULT )
			{
				if( !itr->second.Url.empty() )
				{
					std::wstring hostname;
					if( GetHostNameFromUrl( itr->second.Url.c_str(), hostname ) )
					{
						if( networks.find(hostname) == networks.end() )
						{
							networks.insert(hostname);
							m_lcDrop.AddString( hostname.c_str() );
							bAnyNetworkNamed = true;
						}
					}
				}
			}
		}
	}

	// ���û���κ������������ṩ�̣�����ȱʡ�ķ�������
	if( !bAnyNetworkNamed )
	{
		m_lcDrop.AddString( L"�Զ����·�����");
		// m_lcDrop.AddString( L"����");
	}

	m_lcDrop.SetTextColor( m_clrCombo);
	m_lcDrop.m_textLeftInterval = m_rcCombo.left - m_rcDropList.left;
	if( m_lcDrop.m_textLeftInterval < 5 )
		m_lcDrop.m_textLeftInterval = 5;

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CSelectServerDialog::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: Add your message handler code here
	// Do not call CDialog::OnPaint() for painting messages
	if (!m_layoutManager.OnPanelPaint(&dc) )
	{
		CDialog::OnPaint();
	}
	dc.SetBkMode( TRANSPARENT);
	dc.SetTextColor( m_clrCombo);
	dc.SelectStockObject( DEFAULT_GUI_FONT);
	
	CString str;
	m_lcDrop.GetText( m_lcDrop.GetCurSel(), str);
	if( !str.IsEmpty())
	{
		dc.DrawText( str, m_rcCombo, DT_LEFT|DT_VCENTER|DT_SINGLELINE);
	}
}

void CSelectServerDialog::OnBnClickedOk()
{
	OnOK();
}

void CSelectServerDialog::OnBnClickedCancel()
{
	OnCancel();
}

void CSelectServerDialog::OnBnClickedCombobutton()
{
	if( !m_lcDrop.m_bHideByParent)
	{
		CRect rc( m_rcDropList);
		ClientToScreen( &rc);

		if( !::IsWindow( m_lcDrop.GetSafeHwnd()))
		{
			m_lcDrop.Create( GetSafeHwnd(), m_rcDropList, 1000);		
			m_lcDrop.SizeToContent();
		}

		if( ::IsWindow( m_lcDrop.GetSafeHwnd()))
		{
			if( m_lcDrop.IsWindowVisible())
			{
				m_lcDrop.ShowWindow( SW_HIDE);
			}
			else
			{
				m_lcDrop.SetMoveIndex( m_lcDrop.GetCurSel());
				m_lcDrop.SetWindowPos( &CWnd::wndTopMost, rc.left, rc.top, 0, 0, SWP_SHOWWINDOW|SWP_NOSIZE);
			}
		}
	}
	m_lcDrop.m_bHideByParent = false;
}

LRESULT CSelectServerDialog::OnNcHitTest(CPoint point)
{
	LRESULT nRet = CDialog::OnNcHitTest(point);
	if(nRet == HTCLIENT)
	{
		// ���������д���ͻ������ƶ�����
		ScreenToClient( &point);
		if( !m_rcCombo.PtInRect( point))
		{
			nRet = HTCAPTION;	
		}
	}
	return nRet;
}

LRESULT CSelectServerDialog::OnItemChanged( WPARAM wParam, LPARAM lParam)
{
	InvalidateRect( &m_rcCombo);
	return 0;
}

void CSelectServerDialog::OnLButtonUp(UINT nFlags, CPoint point)
{
	CDialog::OnLButtonUp(nFlags, point);
	
	TRACE( L"OnLButtonUp m_bHideByParent = %d \n", m_lcDrop.m_bHideByParent);

	if( m_rcCombo.PtInRect( point) )
	{
		OnBnClickedCombobutton();
	}
	m_lcDrop.m_bHideByParent = false;
}


void CSelectServerDialog::OnNcLButtonDown(UINT nHitTest, CPoint point)
{
	CDialog::OnNcLButtonDown(nHitTest, point);
	m_lcDrop.m_bHideByParent = false;
}

void CSelectServerDialog::OnBnClickedOpenWebpage()
{
	SHELLEXECUTEINFO seInfo = {0};
	seInfo.cbSize = sizeof(SHELLEXECUTEINFO);
	seInfo.lpVerb = _T("open");
	seInfo.lpFile = GetOnlineDownloadPageUrl().GetString();
	if( ShellExecuteExW(&seInfo) )
	{
		EndDialog(IDCANCEL);
	}
}

BOOL CSelectServerDialog::OnToolTipNotify( UINT id, NMHDR * pNMHDR, LRESULT * pResult )
{
	TOOLTIPTEXT *pTTT = (TOOLTIPTEXT *)pNMHDR;
	UINT_PTR nID = pNMHDR->idFrom;

	if (pTTT->uFlags & TTF_IDISHWND)
	{
		// idFrom is actually the HWND of the tool
		int iID = ::GetDlgCtrlID((HWND)nID);
		if(iID)
		{
			// ���⴦��CDIYButton�����ʾ��Ϣ�����ǵ�������ʾ������XML���塣
			CWnd *pChild = CWnd::FromHandlePermanent((HWND)nID);
			if( pChild != NULL && pChild->IsKindOf(RUNTIME_CLASS(CDIYButton)))
			{
				LPCTSTR lpszTipInfo = ((CDIYButton*)pChild)->GetTipInfo();
				if( lpszTipInfo != NULL && lpszTipInfo[0] != _T('\0'))
				{
					_tcscpy_s(pTTT->szText, _countof(pTTT->szText), lpszTipInfo  );
					return TRUE;
				}
			}

			// ��ť��ʾ��Ϣ
			CString strName;
			if( strName.LoadString( iID ) )
			{
				int iPos = strName.Find( _T("\n") );
				if ( iPos > 0 )
				{
					strName = strName.Mid( iPos + 1 );
					_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strName );
				}
				else
				{
					pTTT->lpszText = MAKEINTRESOURCE(iID);
					pTTT->hinst = AfxGetResourceHandle();
				}
			}
			else
			{
				GetDlgItemText(iID, strName );
				if ( !strName.IsEmpty() )
				{
					_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strName );
				}
			}

			return(TRUE);
		}
	}

	return(FALSE);
}
