// viewxmlView.cpp : implementation of the CviewxmlView class
//

#include "stdafx.h"
#include "viewxml.h"
#include "viewxmlDoc.h"
#include "viewxmlView.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CviewxmlView

IMPLEMENT_DYNCREATE(CviewxmlView, CTreeView)

BEGIN_MESSAGE_MAP(CviewxmlView, CTreeView)
	ON_WM_CREATE()
	ON_WM_MOUSEMOVE()
	ON_WM_CTLCOLOR()
	ON_WM_TIMER()
	ON_WM_MOUSEWHEEL()
END_MESSAGE_MAP()

// CviewxmlView construction/destruction
#define IDT_SHOW_TIP_EDIT		1000

CviewxmlView::CviewxmlView()
: m_curItem( NULL)
, m_fontHeight( 16)
{
	// TODO: add construction code here

}

CviewxmlView::~CviewxmlView()
{
}

BOOL CviewxmlView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return CTreeView::PreCreateWindow(cs);
}

void CviewxmlView::OnInitialUpdate()
{
	CTreeView::OnInitialUpdate();
	GetTreeCtrl().DeleteAllItems();

	if( GetDocument()->m_bOpen)
	{
		XMLIO::CFileReader& reader = GetDocument()->m_reader;
		
		XMLIO::CINode root;
		if( reader.GetRootNode( root))
		{
			InsertToTree( root, TVI_ROOT);
		}
	}
}

// CviewxmlView message handlers
void CviewxmlView::InsertToTree( XMLIO::CINode& node, HTREEITEM hParent)
{
	m_name = node.GetName( );

	// name
	m_wos.str( L"");
	m_wos << L"<" << m_name << L" ";

	// attribute
	XMLIO::CAttributesNames atts;
	node.GetAttributeNames( atts);
	for( XMLIO::CAttributesNames::const_iterator i=atts.begin(), e=atts.end(); i != e; ++i)
	{
		const std::wstring& attName = *i;
		node.GetAttribute( attName, m_value);
		m_wos << attName << L"=" << L"\"" << m_value << L"\" ";
	}

	m_value = node.GetText();
	size_t count = node.GetChildrenCount();
	if( m_value.empty() && 0 == count)
	{
		m_wos << L"/>";
		GetTreeCtrl().InsertItem( m_wos.str().c_str(), hParent);
	}
	else
	{
		m_wos << L">" ;
		HTREEITEM hTemp = GetTreeCtrl().InsertItem( m_wos.str().c_str(), hParent);

		m_wos.str( L"");
		m_wos << m_value << L"</" << m_name << L">";
		GetTreeCtrl().InsertItem( m_wos.str().c_str(), hParent);

		// children
		XMLIO::CINode child;
		for( size_t i=0; i<count; i++)
		{
			if( node.GetChildAt( i, child))
			{
				InsertToTree( child, hTemp);
			}
		}

		//
		GetTreeCtrl().Expand( hTemp, TVE_EXPAND);
	}
}

int CviewxmlView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CTreeView::OnCreate(lpCreateStruct) == -1)
		return -1;

	// TODO:  Add your specialized creation code here
	GetTreeCtrl().ModifyStyle( 0, TVS_HASBUTTONS|TVS_HASLINES|TVS_LINESATROOT);
	m_editTip.Create( ES_MULTILINE|ES_READONLY, CRect(0,0,100,100), this, 0);
	m_editTip.SetFont( GetFont());

	TEXTMETRIC tm;
	CDC* pDC = m_editTip.GetDC();
	CFont* pOld = pDC->SelectObject( m_editTip.GetFont());
	pDC->GetTextMetrics(&tm);
	pDC->SelectObject( pOld);
	m_editTip.ReleaseDC( pDC);

	m_fontHeight = tm.tmHeight;

	return 0;
}

void CviewxmlView::OnMouseMove(UINT nFlags, CPoint point)
{
	// TODO: Add your message handler code here and/or call default

	HTREEITEM hOld = m_curItem;
	UINT uFlags;
	m_curItem = GetTreeCtrl().HitTest( point, &uFlags);
	if( m_curItem )
	{
		if( TVHT_ONITEM & uFlags)
		{
			CString str = GetTreeCtrl().GetItemText( m_curItem);
			if( str.GetLength() > 260)	// 树控件不能显示大于260个字符的串，这种情况出一个edit显示
			{
				if( m_curItem != hOld)
				{
					SetTimer( IDT_SHOW_TIP_EDIT, 500, NULL);
				}
				return;
			}
		}
		else
		{
			m_curItem = NULL;
		}
	}
	//KillTimer( IDT_SHOW_TIP_EDIT);
	m_editTip.ShowWindow( SW_HIDE);
	//CTreeView::OnMouseMove(nFlags, point);
}


HBRUSH CviewxmlView::OnCtlColor(CDC* pDC, CWnd* pWnd, UINT nCtlColor)
{
	HBRUSH hbr = CTreeView::OnCtlColor(pDC, pWnd, nCtlColor);

	// TODO:  Change any attributes of the DC here
	if( pWnd == &m_editTip)
	{
		// 改变编辑框背景色
		pDC->SetBkColor( RGB(255,255,225));
	}

	// TODO:  Return a different brush if the default is not desired
	return hbr;
}

void CviewxmlView::OnTimer(UINT_PTR nIDEvent)
{
	// TODO: Add your message handler code here and/or call default
	if( IDT_SHOW_TIP_EDIT == nIDEvent)
	{
		KillTimer( IDT_SHOW_TIP_EDIT);

		if( m_curItem)
		{
			CString str = GetTreeCtrl().GetItemText( m_curItem);
			m_editTip.SetWindowText( str);

			CRect rc;
			GetTreeCtrl().GetItemRect( m_curItem, &rc, TRUE);
			CRect rcClient;
			GetTreeCtrl().GetClientRect( &rcClient);
			if( rc.left < rcClient.left)
			{
				rc.left = rcClient.left;
			}
			if( rc.right > rcClient.right)
			{
				rc.right = rcClient.right;
			}
			m_editTip.MoveWindow( &rc);

			rc.bottom = rc.top + m_fontHeight*m_editTip.GetLineCount();

			m_editTip.SetWindowPos( NULL, 0, 0, rc.Width(), rc.Height(), 
				SWP_SHOWWINDOW|SWP_NOMOVE);
			//Invalidate( );
		}
		else
		{
			m_editTip.ShowWindow( SW_HIDE);
		}
	}
	CTreeView::OnTimer(nIDEvent);
}

BOOL CviewxmlView::OnMouseWheel(UINT nFlags, short zDelta, CPoint pt)
{
	// TODO: Add your message handler code here and/or call default
	m_editTip.ShowWindow( SW_HIDE);

	return CTreeView::OnMouseWheel(nFlags, zDelta, pt);
}
