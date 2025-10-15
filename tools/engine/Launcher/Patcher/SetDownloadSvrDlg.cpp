#include "stdafx.h"
#include "SetDownloadSvrDlg.h"
#include "LauncherConfig.h"
#include "LauncherUtils.h"
#include "GlobalFunction.h"
#include "../../pfs/versionmetafile.h"

IMPLEMENT_DYNAMIC(SetDownloadSvrDlg, CDialog)

SetDownloadSvrDlg::SetDownloadSvrDlg(const PFSX::CVersionInfo& rVersionInfo, CWnd* pParent /*=NULL*/):CDialog(IDD_SETDOWNLOAD), 
m_rVersionInfo( rVersionInfo )
{
	m_bP2P = false;
	m_iLowerVal = _T("0");
	m_iUpperVal = _T("0");
}

SetDownloadSvrDlg::~SetDownloadSvrDlg()
{

}

void SetDownloadSvrDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(SetDownloadSvrDlg, CDialog)
	ON_WM_CREATE()
	ON_WM_PAINT()
	ON_BN_CLICKED(ID_OK, &SetDownloadSvrDlg::OnBtnClickedOk)
	ON_BN_CLICKED(ID_CANCEL, &SetDownloadSvrDlg::OnBtnClickedCancel)
	ON_BN_CLICKED(IDC_BTNCBX, &SetDownloadSvrDlg::OnBtnClickedCbx)
	ON_BN_CLICKED(IDC_SVR, &SetDownloadSvrDlg::OnBtnClickedOptSvr)
	ON_BN_CLICKED(IDC_P2P, &SetDownloadSvrDlg::OnBtnClickedOptP2P)
	ON_WM_NCHITTEST()
	ON_WM_LBUTTONUP()
	ON_WM_NCLBUTTONDOWN()
	ON_MESSAGE( WM_ITEMCHANGED, &SetDownloadSvrDlg::OnItemChanged)
	ON_NOTIFY_EX( TTN_NEEDTEXT, 0, &SetDownloadSvrDlg::OnToolTipNotify)
END_MESSAGE_MAP()

int SetDownloadSvrDlg::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CDialog::OnCreate(lpCreateStruct) == -1)
		return -1;

	EnableToolTips();

	//
	// 设置控件的面板窗口，同时指定UI资源包的目录和布局配置文件名
	//
	if( !m_layoutManager.Load( GetLauncherUIResourceDir(), 
		GetSetDownloadDlgUILayoutFile(), L"SetDownloadDlg" ) )
	{
		ReportMessage(L"Load file %s failed!", GetSetDownloadDlgUILayoutFile().GetString() );
		return -1;
	}

	//
	// 渲染面板窗口
	//
	if( !m_layoutManager.SetPanel( this ) )
	{
		ReportMessage(L"Load file %s failed!", GetSetDownloadDlgUILayoutFile().GetString() );
		return -1;
	}

	return 0;
}

void SetDownloadSvrDlg::OnPaint()
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
	m_dropList.GetText( m_dropList.GetCurSel(), str);
	if( !str.IsEmpty())
	{
		dc.DrawText( str, m_rc, DT_LEFT|DT_VCENTER|DT_SINGLELINE);
	}
}

BOOL SetDownloadSvrDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	//
	// 按钮控件初始化
	//
	m_layoutManager.ApplyButton( ID_OK, m_btnOK );
	m_layoutManager.ApplyButton( ID_CANCEL, m_btnCancel );
	m_layoutManager.ApplyButton( IDC_BTNCBX, m_btnDroplist);
	m_layoutManager.ApplyButton( IDC_SVR, m_selSvr );
	m_layoutManager.ApplyButton( IDC_P2P, m_selP2P);
	if(m_bP2P)
		OnBtnClickedOptP2P();
	else
		OnBtnClickedOptSvr();
	
	m_layoutManager.ApplyEdit( IDC_DOWNSPEED, m_editDown );
	m_layoutManager.ApplyEdit( IDC_UPSPEED, m_editUp);
	LOGFONT lf;
	CFont* ptf = m_editDown.GetFont();
	ptf->GetLogFont(&lf);
	lf.lfHeight = 18;
	font.CreateFontIndirect(&lf);
	m_editDown.SetFont(&font);
	m_editDown.SetMargins(5,5);
	m_editDown.LimitText(8);
	m_editDown.SetWindowText(m_iLowerVal);
	
	m_editUp.SetFont(&font);
	m_editUp.SetMargins(5,5);
	m_editUp.LimitText(8);
	m_editUp.SetWindowText(m_iUpperVal);
	m_layoutManager.GetControlRect( _T("ComboStatic"), m_rc);
	m_layoutManager.GetControlColor( _T("ComboStatic"), m_clrCombo);
	m_layoutManager.GetControlRect( _T("DropList"), m_rcDropList);

	bool bAnyNetworkNamed = false;

	std::set<std::wstring> networks; // 避免重复。

	// 遍历更新服务器提供的版本类型列表
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
					m_dropList.AddString( itr->second.Network.c_str() );
					bAnyNetworkNamed = true;
				}
			}
		}
	}

	// 如果没有任何命名的网络提供商，加入URL中提供的主机名或者IP地址。
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
							m_dropList.AddString( hostname.c_str() );
							bAnyNetworkNamed = true;
						}
					}
				}
			}
		}
	}

	// 如果没有任何命名的网络提供商，加入缺省的服务器。
	if( !bAnyNetworkNamed )
	{
		m_dropList.AddString( L"自动更新服务器");
		// m_lcDrop.AddString( L"电信");
	}
	m_dropList.SetTextColor( m_clrCombo);

	CRect rc( m_rc);
	ClientToScreen( &rc);
	if( !::IsWindow( m_dropList.GetSafeHwnd()))
	{
		m_dropList.Create( GetSafeHwnd(), m_rc, 1000);		
		m_dropList.SizeToContent();
	}
	if( ::IsWindow( m_dropList.GetSafeHwnd()))
	{
		if( m_dropList.IsWindowVisible())
		{
			m_dropList.ShowWindow( SW_HIDE);
		}
		else
		{
			m_dropList.SetMoveIndex( m_dropList.GetCurSel());
			m_dropList.SetWindowPos( &CWnd::wndTopMost, rc.left, rc.top, 0, 0, SWP_SHOWWINDOW|SWP_NOSIZE);
		}
	}
	m_dropList.m_textLeftInterval = 5;
	return TRUE;
}

void SetDownloadSvrDlg::OnBtnClickedOk()
{
	m_editDown.GetWindowText(m_iLowerVal);
	m_editUp.GetWindowText(m_iUpperVal);
	CDialog::OnOK();
}

void SetDownloadSvrDlg::OnBtnClickedCancel()
{
	CDialog::OnCancel();
}

void SetDownloadSvrDlg::OnBtnClickedCbx()
{
	if( !m_dropList.m_bHideByParent)
	{
		CRect rc( m_rcDropList);
		ClientToScreen( &rc);

		if( !::IsWindow( m_dropList.GetSafeHwnd()))
		{
			m_dropList.Create( GetSafeHwnd(), m_rcDropList, 1000);	
			m_dropList.SizeToContent();
		}

		if( ::IsWindow( m_dropList.GetSafeHwnd()))
		{
			if( m_dropList.IsWindowVisible())
			{
				m_dropList.ShowWindow( SW_HIDE);
			}
			else
			{
				m_dropList.SetMoveIndex( m_dropList.GetCurSel());
				m_dropList.SetWindowPos( &CWnd::wndTopMost, rc.left, rc.top, 0, 0, SWP_SHOWWINDOW|SWP_NOSIZE);
			}
		}
	}
	m_dropList.m_bHideByParent = false;
}

void SetDownloadSvrDlg::OnBtnClickedOptSvr()
{
	m_selSvr.SetCheck(TRUE);
	m_selP2P.SetCheck(FALSE);
	m_selP2P.RedrawWindow();
}

void SetDownloadSvrDlg::OnBtnClickedOptP2P()
{
	m_selSvr.SetCheck(FALSE);
	m_selP2P.SetCheck(TRUE);
	m_selSvr.RedrawWindow();
}

BOOL SetDownloadSvrDlg::OnToolTipNotify( UINT id, NMHDR * pNMHDR, LRESULT * pResult )
{
	TOOLTIPTEXT *pTTT = (TOOLTIPTEXT *)pNMHDR;
	UINT_PTR nID = pNMHDR->idFrom;

	if (pTTT->uFlags & TTF_IDISHWND)
	{
		// idFrom is actually the HWND of the tool
		int iID = ::GetDlgCtrlID((HWND)nID);
		if(iID)
		{
			// 特殊处理CDIYButton类的提示信息，它们的悬浮提示来自于XML定义。
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

			// 按钮提示信息
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

LRESULT SetDownloadSvrDlg::OnNcHitTest(CPoint point)
{
	LRESULT nRet = CDialog::OnNcHitTest(point);
	if(nRet == HTCLIENT)
	{
		// 允许鼠标点中窗体客户区域移动窗口
		ScreenToClient( &point);
		if( !m_rc.PtInRect( point))
		{
			nRet = HTCAPTION;	
		}
	}
	return nRet;
}

LRESULT SetDownloadSvrDlg::OnItemChanged( WPARAM wParam, LPARAM lParam)
{
	InvalidateRect( &m_rc);
	return 0;
}

void SetDownloadSvrDlg::OnLButtonUp(UINT nFlags, CPoint point)
{
	CDialog::OnLButtonUp(nFlags, point);

	TRACE( L"OnLButtonUp m_bHideByParent = %d \n", m_dropList.m_bHideByParent);

	if( m_rc.PtInRect( point) )
	{
		OnBtnClickedCbx();
	}
	m_dropList.m_bHideByParent = false;
}


void SetDownloadSvrDlg::OnNcLButtonDown(UINT nHitTest, CPoint point)
{
	CDialog::OnNcLButtonDown(nHitTest, point);
	m_dropList.m_bHideByParent = false;
}