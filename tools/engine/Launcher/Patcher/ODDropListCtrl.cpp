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
	, m_textLeftInterval(10)
	, m_bMouseDown( false)
{

}

CODDropListCtrl::~CODDropListCtrl()
{
	m_bmpItemButton.Detach();
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

int CODDropListCtrl::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CWnd::OnCreate(lpCreateStruct) == -1)
		return -1;

	if( !m_layoutManager.Load( GetLauncherUIResourceDir(), 
		GetSelectServerListUILayoutFile(), L"DropListCtrl" ) )
	{
		ReportMessage(L"加载 SelectServerList 资源包失败!");
		return -1;
	}

	if( !m_layoutManager.SetPanel( this, FALSE ) )
	{
		ReportMessage(L"初始化 SelectServerList 资源包失败!");
		return -1;
	}

	m_layoutManager.GetFRImageRect( m_rcFR, m_rcBorder );

	//Image1 - rcNormal, may be empty rectangle.
	//Image2 - rcHover
	//Image3 - rcSelected
	//Image0 - rcDisabled, may be empty rectangle.

	HBITMAP hBmp = NULL;

	m_rcNormal.SetRectEmpty();
	m_layoutManager.GetBitmap( L"ItemButton", L"Image1", hBmp, TRUE, m_rcNormal);

	hBmp = NULL;
	m_layoutManager.GetBitmap( L"ItemButton", L"Image2", hBmp, TRUE, m_rcHover);
	if( !hBmp )
	{
		m_layoutManager.GetBitmap( L"ItemNormal", L"Image1", hBmp, TRUE, m_rcHover);
	}

	if( hBmp)
	{
		m_bmpItemButton.Attach( hBmp);
	}

	hBmp = NULL;
	m_layoutManager.GetBitmap( L"ItemButton", L"Image3", hBmp, TRUE, m_rcDown);
	if( !hBmp )
	{
		m_layoutManager.GetBitmap( L"ItemDown", L"Image1", hBmp, TRUE, m_rcDown);
	}

	m_rcItem = m_rcHover;
	m_rcItem.MoveToXY(0,0);

	// 调试的时候，测试多个条目的显示效果：
	//m_items.Add(L"xxxx1");
	//m_items.Add(L"xxxx2");
	//m_items.Add(L"xxxx3");
	//m_items.Add(L"xxxx4");

	return 0;
}

void CODDropListCtrl::SizeToContent()
{
	CRect rcWindow;
	GetWindowRect(&rcWindow);

	int srcClientHeight = m_rcFR.Height() - m_rcBorder.top - m_rcBorder.bottom;
	int srcClientWidth = m_rcFR.Width() - m_rcBorder.left - m_rcBorder.right;
	
	int destClientHeight = rcWindow.Height() - m_rcBorder.top - m_rcBorder.bottom;
	int destClientWidth = rcWindow.Width() - m_rcBorder.left - m_rcBorder.right;

	int clientWidth = destClientWidth;
	int clientHeight = destClientHeight;
	if( !m_items.IsEmpty() )
	{
		int count = (int)m_items.GetCount();
		clientHeight = count * m_rcItem.Height() + ( count-1) * m_nInterval;
	}
	
	int winWidth = rcWindow.Width();
	int winHeight = m_rcBorder.top + m_rcBorder.bottom + clientHeight;

	m_rcItem.MoveToY(m_rcBorder.top);
	m_rcItem.left = m_rcBorder.left;
	m_rcItem.right = m_rcBorder.left + clientWidth;

	HBITMAP hBmp = MHZX::CRenderHelper::CreateDIBitmap( NULL, winWidth, winHeight, NULL);
	if( !hBmp )
	{
		return;
	}

	m_bmpFR.DeleteObject();
	m_bmpFR.Attach( hBmp );

	CDC& src = m_layoutManager.GetFRMemDC();

	CDC dcMem;
	dcMem.CreateCompatibleDC( NULL);
	CBitmap* pOld = dcMem.SelectObject( &m_bmpFR);

	// 9-宫格绘制方案示意图：
	//
	//   0 |    1    |  2
	// ----+---------+----
	//   3 |    4    |  5
	// ----+---------+----
	//   6 |    7    |  8
	//
	// 其中1、7横向拉伸,3、5纵向拉伸，4双向拉伸。0,2,6,8不拉伸。
	// 
	// #0 区域
	dcMem.BitBlt( 0, 0, m_rcBorder.left, m_rcBorder.top, &src, 0, 0, SRCCOPY);

	// #1 横向拉伸区域
	dcMem.StretchBlt( m_rcBorder.left, 0, clientWidth, m_rcBorder.top, 
				&src, m_rcBorder.left, 0, srcClientWidth, m_rcBorder.top, SRCCOPY);

	// #2 区域
	dcMem.BitBlt( m_rcBorder.left + clientWidth, 0, m_rcBorder.right, m_rcBorder.top, 
				&src, m_rcBorder.left + srcClientWidth, 0, SRCCOPY);

	// #3 纵向拉伸区域
	dcMem.StretchBlt( 0, m_rcBorder.top, m_rcBorder.left, clientHeight, 
		&src, 0, m_rcBorder.top, m_rcBorder.left, srcClientHeight, SRCCOPY);

	// #4 双向拉伸区域
	dcMem.StretchBlt( m_rcBorder.left, m_rcBorder.top, clientWidth, clientHeight, &src, 
		m_rcBorder.left, m_rcBorder.top, srcClientWidth, srcClientHeight, SRCCOPY);

	// #5 纵向拉伸区域
	dcMem.StretchBlt( m_rcBorder.left + clientWidth, m_rcBorder.top, m_rcBorder.right, clientHeight, 
		&src, m_rcBorder.left + srcClientWidth, m_rcBorder.top, m_rcBorder.right, srcClientHeight, SRCCOPY);

	// #6 区域
	dcMem.BitBlt( 0, m_rcBorder.top + clientHeight, m_rcBorder.left, clientHeight, &src, 
		0, m_rcBorder.top + srcClientHeight, SRCCOPY);

	// #7 横向拉伸区域
	dcMem.StretchBlt( m_rcBorder.left, m_rcBorder.top + clientHeight, clientWidth, m_rcBorder.bottom, 
		&src, m_rcBorder.left, m_rcBorder.top + srcClientHeight, srcClientWidth, m_rcBorder.bottom, SRCCOPY);

	// #8 区域
	dcMem.BitBlt( m_rcBorder.left + clientWidth, m_rcBorder.top + clientHeight, m_rcBorder.right, m_rcBorder.bottom,
		&src, m_rcBorder.left + srcClientWidth, m_rcBorder.top + srcClientHeight, SRCCOPY);

	dcMem.SelectObject( pOld );

	// 重置窗口尺寸
	SetWindowPos(NULL, 0, 0, winWidth, winHeight,
		SWP_NOMOVE | SWP_NOZORDER | SWP_NOACTIVATE | SWP_NOOWNERZORDER );

	// 剪裁不规则窗口
	CRgn wndRgn;
	CreateRgnByAlphaBits( m_bmpFR, wndRgn);
	SetWindowRgn( wndRgn, FALSE );
}

void CODDropListCtrl::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// Do not call CWnd::OnPaint() for painting messages
	
	CDC dcMem;
	dcMem.CreateCompatibleDC( &dc);

	CGdiObject *pOldBitmap = NULL;
	CRect rc;
	GetClientRect( &rc);
	if( m_bmpFR.GetSafeHandle())
	{
		pOldBitmap = dcMem.SelectObject( &m_bmpFR);
		dc.BitBlt( 0, 0, rc.Width(), rc.Height(), &dcMem, 0, 0, SRCCOPY);
	}
	else
	{
		dc.FillSolidRect( &rc, RGB(60,24,3));
	}
	
	if( m_items.IsEmpty() )
	{
		if( pOldBitmap != NULL )
			dcMem.SelectObject( pOldBitmap );

		return;
	}

	dc.SetBkMode( TRANSPARENT);
	CGdiObject *pOldFont = dc.SelectStockObject( DEFAULT_GUI_FONT);

	rc.CopyRect( m_rcItem);
	rc.MoveToY(m_rcBorder.top);
	dc.SetTextColor( m_clrText);

	// 下面的ItemButton绘制方式，基于一个事实：Normal、Hover和Down 3种状态都用了同一张底图，仅仅坐标不一样。
	// 所以，使用了同一个dcMem。
	if( pOldBitmap == NULL )
		pOldBitmap = dcMem.SelectObject( &m_bmpItemButton );
	else
		dcMem.SelectObject( &m_bmpItemButton );

	CRect rcItem;
	for( int i = 0; i < m_items.GetCount(); i ++ )
	{
		GetItemRect(i, rcItem);

		if( i == m_moveIndex )
		{
			if( m_bMouseDown )
			{
				dc.StretchBlt( rcItem.left, rcItem.top, rcItem.Width(), rcItem.Height(), &dcMem, 
					m_rcDown.left, m_rcDown.top, m_rcDown.Width(), m_rcDown.Height(), SRCCOPY);		
			}
			else
			{
				dc.StretchBlt( rcItem.left, rcItem.top, rcItem.Width(), rcItem.Height(), &dcMem, 
					m_rcHover.left, m_rcHover.top, m_rcHover.Width(), m_rcHover.Height(), SRCCOPY);
			}
		}
		else
		{
			// Normal态如果没有提供，不绘制底图。
			if( !m_rcNormal.IsRectEmpty() )
				dc.StretchBlt( rcItem.left, rcItem.top, rcItem.Width(), rcItem.Height(), &dcMem, 
					m_rcNormal.left, m_rcNormal.top, m_rcNormal.Width(), m_rcNormal.Height(), SRCCOPY);
		}

		rcItem.OffsetRect( m_textLeftInterval, 0);
		dc.DrawText( m_items[i], &rcItem, DT_LEFT|DT_VCENTER|DT_SINGLELINE);
	}

	dcMem.SelectObject( pOldBitmap );
	dc.SelectObject(pOldFont);
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


void CODDropListCtrl::OnMouseMove(UINT nFlags, CPoint point)
{
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

	if( moveIndex >= 0 )
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

int CODDropListCtrl::HitTest( CPoint point)
{
	CRect rc;
	for( int i = 0; i<m_items.GetCount(); i++)
	{
		if( !GetItemRect( i, rc ) )
		{
			continue;
		}

		if( rc.PtInRect( point))
		{
			return (int)i;
		}
	}
	return -1;
}

BOOL CODDropListCtrl::OnEraseBkgnd(CDC* pDC)
{
	return TRUE;
}

LRESULT CODDropListCtrl::OnMouseLeave( WPARAM wParam, LPARAM lParam)
{
	if( m_bMouseDown)
	{
		m_bMouseDown = false;
		CRect rc;
		if( GetItemRect( m_moveIndex, rc) )
			InvalidateRect( &rc, FALSE);
	}

	return 0;
}

void CODDropListCtrl::OnLButtonDown(UINT nFlags, CPoint point)
{
	CWnd::OnLButtonDown(nFlags, point);
	
	int curSel = HitTest( point);
	if( curSel < 0)
	{
		return;
	}

	m_bMouseDown = true;

	CRect rc;
	if( GetItemRect( curSel, rc) )
		InvalidateRect( &rc, FALSE);
}

BOOL CODDropListCtrl::GetItemRect( int index, CRect& rcItem)
{
	rcItem.SetRectEmpty();

	if( index < 0 || index >= m_items.GetCount() )
	{
		return FALSE;
	}
	
	rcItem.CopyRect( m_rcItem );

	rcItem.MoveToY( m_rcBorder.top + index*(m_rcItem.Height() + m_nInterval));

	return TRUE;
}

void CODDropListCtrl::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
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
