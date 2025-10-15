// ODDropListCtrl.cpp : implementation file
//

#include "stdafx.h"
#include "ODDropListCtrl.h"
#include "LauncherConfig.h"
#include "LauncherUtils.h"
#include "RenderHelper.h"
#include "BitmapUtils.h"

// CODDropListCtrl

IMPLEMENT_DYNAMIC(CODDropListCtrl, CWnd)

CODDropListCtrl::CODDropListCtrl()
: m_curSel( 0)
, m_nInterval( 0)
, m_bHideByParent( false)
, m_clrText( ::GetSysColor( COLOR_CAPTIONTEXT))
, m_textLeftInterval( 10)
, m_bMouseDown( false)
{

}

CODDropListCtrl::~CODDropListCtrl()
{
}


BEGIN_MESSAGE_MAP(CODDropListCtrl, CWnd)
	ON_WM_PAINT()
	ON_WM_KILLFOCUS()
	ON_WM_MOUSEMOVE()
	ON_WM_LBUTTONUP()
	ON_WM_CREATE()
	ON_WM_ERASEBKGND()
	ON_MESSAGE( WM_MOUSELEAVE, OnMouseLeave)
	ON_WM_LBUTTONDOWN()
	ON_WM_KEYDOWN()
END_MESSAGE_MAP()

BOOL CODDropListCtrl::Create(HWND hParent, const RECT& rect, UINT nID)
{
	BOOL bRet = CWnd::CreateEx(WS_EX_TOOLWINDOW|WS_EX_LAYERED, AfxRegisterWndClass( CS_VREDRAW), L"", WS_POPUP,
		rect.left, rect.top, rect.right-rect.left, rect.bottom-rect.top, hParent, 0, 0);

	if( bRet)
	{
		// Make this window 80% alpha
		SetLayeredWindowAttributes( 0, (255 * 80) / 100, LWA_ALPHA);
	}
	return bRet;
}

// CODDropListCtrl message handlers



void CODDropListCtrl::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: Add your message handler code here
	// Do not call CWnd::OnPaint() for painting messages
	
	CDC dcMem;
	dcMem.CreateCompatibleDC( &dc);

	CRect rc;
	GetClientRect( &rc);
	if( m_bmpFR.GetSafeHandle())
	{
		dcMem.SelectObject( &m_bmpFR);
		dc.BitBlt( 0, 0, rc.Width(), rc.Height(), &dcMem, 0, 0, SRCCOPY);
	}
	else
	{
		dc.FillSolidRect( &rc, RGB(60,24,3));
	}
	
	dc.SetBkMode( TRANSPARENT);
	dc.SelectStockObject( DEFAULT_GUI_FONT);

	rc.CopyRect( m_rcItem);
	int left = ( m_rcFR.Width() - rc.Width()) / 2;
	if( left < 0)
	{
		left = 0;
	}
	rc.OffsetRect( left, m_rcBorder.top);
	dc.SetTextColor( m_clrText);
	dcMem.SelectObject( m_bmpNormal);

	for( INT_PTR i = 0; i<m_items.GetCount(); i++)
	{
		if( i == m_moveIndex)
		{	
			if( m_bMouseDown)
			{
				dc.StretchBlt( rc.left, rc.top, rc.Width(), rc.Height(), &dcMem, 
					m_rcDown.left, m_rcDown.top, m_rcDown.Width(), m_rcDown.Height(), SRCCOPY);		
			}
			else
			{
				dc.StretchBlt( rc.left, rc.top, rc.Width(), rc.Height(), &dcMem, 
					m_rcNormal.left, m_rcNormal.top, m_rcNormal.Width(), m_rcNormal.Height(), SRCCOPY);
			}
		}

		CRect rcTemp( rc);
		rcTemp.OffsetRect( m_textLeftInterval, 0);
		dc.DrawText( m_items[i], &rcTemp, DT_LEFT|DT_VCENTER|DT_SINGLELINE);
		rc.OffsetRect( 0, rc.Height() + m_nInterval);
	}

}

bool IsAncestorWnd( CWnd* pAncestor, CWnd* pCur)
{
	while( pCur)
	{
		if( pCur == pAncestor)
		{
			return true;
		}
		pCur = pCur->GetParent();
	}
	return false;
}

void CODDropListCtrl::OnKillFocus(CWnd* pNewWnd)
{
	CWnd::OnKillFocus(pNewWnd);

	// TODO: Add your message handler code here

	TRACE( L"CODDropListCtrl::OnKillFocus m_bHideByParent = %d \n", m_bHideByParent);

	CWnd* parent = GetParent();
	m_bHideByParent = IsAncestorWnd( parent, pNewWnd);
	
	ShowWindow( SW_HIDE);
}

void CODDropListCtrl::GetText( int nIndex, CString& rString ) const
{
	if( nIndex >= 0 && nIndex < m_items.GetCount())
	{
		rString = m_items[nIndex];
	}
}

int CODDropListCtrl::AddString( LPCTSTR lpszItem )
{
	return (int)m_items.Add( lpszItem);
}

void CODDropListCtrl::SizeToContent()
{
	int orgHeight = m_rcFR.Height() - m_rcBorder.top - m_rcBorder.bottom;
	int height = orgHeight;
	if( !m_items.IsEmpty())
	{
		int count = (int)m_items.GetCount();
		height = count * m_rcItem.Height() + ( count-1) * m_nInterval;
		if( height < orgHeight)
		{
			height = orgHeight;
		}
	}
	HBITMAP hBmp = MHZX::CRenderHelper::CreateDIBitmap( NULL, m_rcFR.Width(), 
		m_rcBorder.top+m_rcBorder.bottom+height, NULL);
	if( hBmp)
	{
		m_bmpFR.DeleteObject();
		m_bmpFR.Attach( hBmp);

		CDC& src = m_layoutManager.GetFRMemDC();
		CDC dcMem;
		dcMem.CreateCompatibleDC( NULL);
		CBitmap* pOld = dcMem.SelectObject( &m_bmpFR);
		// top
		dcMem.BitBlt( 0, 0, m_rcFR.Width(), m_rcBorder.top, &src, m_rcFR.left, m_rcFR.top, SRCCOPY);
		// center
		dcMem.StretchBlt( 0, m_rcBorder.top, m_rcFR.Width(), height, &src, 
			m_rcFR.left, m_rcFR.top+m_rcBorder.top, m_rcFR.Width(), orgHeight, SRCCOPY);
		// bottom
		dcMem.BitBlt( 0, m_rcBorder.top+height, m_rcFR.Width(), m_rcBorder.bottom, &src, 
			m_rcFR.left, m_rcFR.bottom-m_rcBorder.bottom, SRCCOPY);

		dcMem.SelectObject( pOld);

		MoveWindow( 0, 0, m_rcFR.Width(), m_rcBorder.top+m_rcBorder.bottom+height);
		CRgn wndRgn;
		CreateRgnByAlphaBits( m_bmpFR, wndRgn);
		SetWindowRgn( wndRgn, FALSE );
	}
}


void CODDropListCtrl::OnMouseMove(UINT nFlags, CPoint point)
{
	// TODO: Add your message handler code here and/or call default

	CWnd::OnMouseMove(nFlags, point);

	int moveIndex = HitTest( point);
	if( moveIndex < 0)
	{
		return;
	}
	if( moveIndex != m_moveIndex)
	{
		m_moveIndex = moveIndex;
		Invalidate();
	}

	if( moveIndex >= 0)
	{
		TRACKMOUSEEVENT tme;
		tme.cbSize = sizeof(tme);
		tme.hwndTrack = m_hWnd;
		tme.dwFlags = TME_LEAVE;
		tme.dwHoverTime = 1;
		_TrackMouseEvent(&tme);
	}
}

void CODDropListCtrl::OnLButtonUp(UINT nFlags, CPoint point)
{
	// TODO: Add your message handler code here and/or call default

	CWnd::OnLButtonUp(nFlags, point);
	int curSel = HitTest( point);
	if( curSel < 0)
	{
		return;
	}

	if( curSel != m_curSel)
	{
		m_curSel = curSel;
		
		CWnd* parent = GetParent();
		if( parent)
		{
			parent->PostMessage( WM_ITEMCHANGED, m_curSel, 0);
		}
	}
	ShowWindow( SW_HIDE);
	m_bHideByParent = false;
	m_bMouseDown = false;
}

int CODDropListCtrl::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CWnd::OnCreate(lpCreateStruct) == -1)
		return -1;

	// TODO:  Add your specialized creation code here
	if( !m_layoutManager.Load( GetLauncherUIResourceDir(), 
		GetSelectServerListUILayoutFile(), L"DropListCtrl" ) )
	{
		ReportMessage(L"加载 SelectServerList 资源包失败!");
		return -1;
	}
	
	if( !m_layoutManager.SetPanel( this ) )
	{
		ReportMessage(L"初始化 SelectServerList 资源包失败!");
		return -1;
	}

	m_layoutManager.GetFRImageRect( m_rcFR, m_rcBorder);

	HBITMAP hBmp = NULL;
	m_layoutManager.GetBitmap( L"ItemNormal", hBmp, TRUE, m_rcNormal);
	if( hBmp)
	{
		m_bmpNormal.Attach( hBmp);
	}
	m_layoutManager.GetBitmap( L"ItemDown", hBmp, TRUE, m_rcDown);

	m_rcItem = m_rcNormal;
	m_rcItem.OffsetRect( -m_rcItem.left, -m_rcItem.top);
	return 0;
}

int CODDropListCtrl::HitTest( CPoint point)
{
	CRect rc;
	rc.CopyRect( m_rcItem);
	rc.OffsetRect( m_rcBorder.left, m_rcBorder.top);
	for( INT_PTR i = 0; i<m_items.GetCount(); i++)
	{
		if( rc.PtInRect( point))
		{
			return (int)i;
		}
		rc.OffsetRect( 0, rc.Height() + m_nInterval);
	}
	return -1;
}

BOOL CODDropListCtrl::OnEraseBkgnd(CDC* pDC)
{
	// TODO: Add your message handler code here and/or call default
	return TRUE;
	//return CWnd::OnEraseBkgnd(pDC);
}

LRESULT CODDropListCtrl::OnMouseLeave( WPARAM wParam, LPARAM lParam)
{
	if( m_bMouseDown)
	{
		m_bMouseDown = false;
		CRect rc = GetItemRect( m_moveIndex);
		InvalidateRect( &rc, FALSE);
	}
	return 0;
}

void CODDropListCtrl::OnLButtonDown(UINT nFlags, CPoint point)
{
	// TODO: Add your message handler code here and/or call default

	CWnd::OnLButtonDown(nFlags, point);
	
	int curSel = HitTest( point);
	if( curSel < 0)
	{
		return;
	}

	m_bMouseDown = true;
	CRect rc = GetItemRect( curSel);
	InvalidateRect( &rc, FALSE);
}

CRect CODDropListCtrl::GetItemRect( int index)
{
	CRect rc;
	rc.SetRectEmpty();
	if( index < 0)
	{
		return rc;
	}
	rc.CopyRect( m_rcItem);
	int left = ( m_rcFR.Width() - rc.Width()) / 2;
	if( left < 0)
	{
		left = 0;
	}
	rc.OffsetRect( left, m_rcBorder.top + index*m_rcItem.Height());
	return rc;
}

void CODDropListCtrl::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	// TODO: Add your message handler code here and/or call default

	CWnd::OnKeyDown(nChar, nRepCnt, nFlags);
	switch( nChar)
	{
	case VK_RETURN:
	case VK_SPACE:
		if( m_moveIndex >= 0 && m_moveIndex < m_items.GetCount())
		{
			m_curSel = m_moveIndex;
			CWnd* parent = GetParent();
			if( parent)
			{
				parent->PostMessage( WM_ITEMCHANGED, m_curSel, 0);
			}
		}
	case VK_ESCAPE:
		ShowWindow( SW_HIDE);
		m_bHideByParent = false;
		break;
	case VK_UP:
		m_moveIndex--;
		if( m_moveIndex < 0)
		{
			m_moveIndex = 0;
		}
		Invalidate();
		break;
	case VK_DOWN:
		m_moveIndex++;
		if( m_moveIndex >= (int)m_items.GetCount() )
		{
			m_moveIndex = (int)m_items.GetCount()-1;
		}
		Invalidate();
		break;
	}
}
