// CAttributeView message handlers
// AttributeView.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "AttributeView.h"
#include "GlobalInfo.h"

BEGIN_MESSAGE_MAP( CKillFocusEdit, CEdit)
	ON_WM_KILLFOCUS()
END_MESSAGE_MAP()

void CKillFocusEdit::OnKillFocus( CWnd* pNewWnd)
{
	CEdit::OnKillFocus( pNewWnd);
	GetParent()->SendMessage( WM_EDITKILLFOCUS, 0, 0);
}

// CAttributeView
IMPLEMENT_DYNCREATE(CAttributeView, CFormView)

CAttributeView::CAttributeView()
: CFormView( CAttributeView::IDD)
, m_bIsResource( false)
{

}

CAttributeView::~CAttributeView()
{
}

void CAttributeView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_COMBO1, m_cbZipLevel);
	DDX_Control(pDX, IDC_EDIT1, m_url);
}

BEGIN_MESSAGE_MAP(CAttributeView, CFormView)
	ON_CBN_SELCHANGE(IDC_COMBO1, &CAttributeView::OnCbnSelchangeCombo1)
	ON_MESSAGE( WM_EDITKILLFOCUS, &CAttributeView::OnEditKillFocus)
END_MESSAGE_MAP()


// CAttributeView diagnostics

#ifdef _DEBUG
void CAttributeView::AssertValid() const
{
	CFormView::AssertValid();
}

#ifndef _WIN32_WCE
void CAttributeView::Dump(CDumpContext& dc) const
{
	CFormView::Dump(dc);
}
#endif
#endif //_DEBUG


// CAttributeView message handlers


void CAttributeView::OnInitialUpdate()
{
	__super::OnInitialUpdate();

	// TODO: Add your specialized code here and/or call the base class
	SetIsResource( false);
}

// 设置文件数据
void CAttributeView::SetZipTreeItem( CZipTreeItem* item)
{
	if( !item)
		return;
	CAttributeDesc::CAttributeDescMap& map = m_bIsResource ? CGlobalInfo::GetInstance().GetZipDesc().GetMap()
		: CGlobalInfo::GetInstance().GetSetupDesc().GetMap();
	int value = m_bIsResource ? item->GetZipLevel() : item->GetAttributes();

	m_cbZipLevel.SetCurSel( std::distance( map.begin(), map.find( value)));
	m_url.SetWindowText( item->GetUrl().c_str());
}

void CAttributeView::OnCbnSelchangeCombo1()
{
	// TODO: Add your control notification handler code here
	m_leftview->OnUpdateLevel( m_cbZipLevel.GetCurSel());
}

void CAttributeView::SetIsResource( bool bIsResource)
{
	m_bIsResource = bIsResource;
	m_cbZipLevel.ResetContent();
	CAttributeDesc::CAttributeDescMap& map = m_bIsResource ? CGlobalInfo::GetInstance().GetZipDesc().GetMap()
		: CGlobalInfo::GetInstance().GetSetupDesc().GetMap();
	for( CAttributeDesc::CAttributeDescMap::iterator itor = map.begin(); itor != map.end(); ++itor)
	{
		m_cbZipLevel.AddString( itor->second.sDescript.c_str());
	}
}

HRESULT CAttributeView::OnEditKillFocus( WPARAM wParam, LPARAM lParam)
{
	CString str;
	m_url.GetWindowText( str);
	m_leftview->OnUpdateUrl( str.GetBuffer());

	return 0;
}
