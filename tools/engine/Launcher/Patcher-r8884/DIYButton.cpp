// DIYButton.cpp : implementation file
//

#include "stdafx.h"
#include "DIYButton.h"
#include "BitmapUtils.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#define DIY_TRACE( xx ) // OutputDebugStringA( xx )

/////////////////////////////////////////////////////////////////////////////
// CDIYButton

CDIYButton::CDIYButton()
{
	m_bHover = FALSE;
}

CDIYButton::~CDIYButton()
{
}

IMPLEMENT_DYNAMIC(CDIYButton, CButton)

BEGIN_MESSAGE_MAP(CDIYButton, CButton)
	ON_WM_MOUSEMOVE()
	ON_MESSAGE(WM_MOUSELEAVE, OnMouseLeave)
	ON_WM_ERASEBKGND()
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CDIYButton message handlers


BOOL CDIYButton::LoadBitmaps(UINT nIDBitmapResource, 
	UINT nIDBitmapResourceSel,
	UINT nIDBitmapResourceHover,
	UINT nIDBitmapResourceFocus,
	UINT nIDBitmapResourceDisabled)
{
	 return LoadBitmaps(MAKEINTRESOURCE(nIDBitmapResource),
		MAKEINTRESOURCE(nIDBitmapResourceSel),
		MAKEINTRESOURCE(nIDBitmapResourceHover),
		MAKEINTRESOURCE(nIDBitmapResourceFocus),
		MAKEINTRESOURCE(nIDBitmapResourceDisabled)); 
}
	
BOOL CDIYButton::LoadBitmaps(LPCTSTR lpszBitmapResource, 
	 LPCTSTR lpszBitmapResourceSel,
	 LPCTSTR lpszBitmapResourceHover, 
	 LPCTSTR lpszBitmapResourceFocus, 
	 LPCTSTR lpszBitmapResourceDisabled)
{
	// delete old bitmaps (if present)
	m_bitmap.DeleteObject(); 
	m_bitmapSel.DeleteObject();  
	m_bitmapHover.DeleteObject(); 
	m_bitmapFocus.DeleteObject(); 
	m_bitmapDisabled.DeleteObject(); 

	if (!m_bitmap.LoadBitmap(lpszBitmapResource))
	{
		TRACE0("Failed to load bitmap for normal image.\n");
		return FALSE;   // need this one image
	}
	BOOL bAllLoaded = TRUE;
	if (lpszBitmapResourceSel != NULL)
	{
		if (!m_bitmapSel.LoadBitmap(lpszBitmapResourceSel))
		{
			TRACE0("Failed to load bitmap for selected image.\n");
			bAllLoaded = FALSE;
		}
	}
	if (lpszBitmapResourceHover != NULL)
	{
		if (!m_bitmapHover.LoadBitmap(lpszBitmapResourceHover))
			bAllLoaded = FALSE;
	}
	if (lpszBitmapResourceFocus != NULL)
	{
		if (!m_bitmapFocus.LoadBitmap(lpszBitmapResourceFocus))
			bAllLoaded = FALSE;
	}
	if (lpszBitmapResourceDisabled != NULL)
	{
		if (!m_bitmapDisabled.LoadBitmap(lpszBitmapResourceDisabled))
			bAllLoaded = FALSE;
	}
	return bAllLoaded;
}

void CDIYButton::SizeToContent()
{
	ASSERT(m_bitmap.m_hObject != NULL);
	CSize bitmapSize;
	BITMAP bmInfo;
	VERIFY(m_bitmap.GetObject(sizeof(bmInfo), &bmInfo) == sizeof(bmInfo));
	VERIFY(SetWindowPos(NULL, -1, -1, bmInfo.bmWidth, bmInfo.bmHeight,
		SWP_NOMOVE|SWP_NOZORDER|SWP_NOREDRAW|SWP_NOACTIVATE));
}

BOOL CDIYButton::DrawBitmap(HDC hDC, CBitmap *pBitmap)
{
	BITMAP bmpInfo;
    if (pBitmap->GetBitmap(&bmpInfo) == 0)
		return FALSE;

	CDC dc;
	CDC dcMem;

	if (!dc.Attach(hDC))
		return FALSE;

	if (!dcMem.CreateCompatibleDC(&dc))
	{
		dc.Detach();
		return FALSE;
	}

	CBitmap* pOldBitmap = dcMem.SelectObject(pBitmap);
	ASSERT(pOldBitmap != NULL);
	if (pOldBitmap == NULL)
	{
		dc.Detach();
		return FALSE;
	}

	if (!dc.BitBlt(0, 0, bmpInfo.bmWidth, bmpInfo.bmHeight, &dcMem, 0, 0, SRCCOPY))
	{
		dc.Detach();
		return FALSE;
	}
	
	dcMem.SelectObject(pOldBitmap);
	
	dc.Detach();

	return true;
}

BOOL CDIYButton::InitializeFromBitmap(
	const CBitmap& rDIBitmap, const CRect& rFirstRect,
	BOOL bHasDisabledStatusBitmap )
{
	CRect rRectNormal;
	CRect rRectHover;
	CRect rRectSelected;
	CRect rRectDisabled;

	CRect rcRect = rFirstRect;
	INT yOffset = rcRect.Height();
	
	{ // Normal 状态
		rRectNormal = rcRect;
	}

	{ // Hover 状态
		rcRect.OffsetRect( 0, yOffset );
		rRectHover = rcRect;
	}

	{ // Selected 状态
		rcRect.OffsetRect( 0, yOffset );
		rRectSelected = rcRect;
	}

	{ // Disabled 状态
		if( bHasDisabledStatusBitmap )
		{
			rcRect.OffsetRect( 0, yOffset );
			rRectDisabled = rcRect;
		}
		else // 没有Disable状态时使用Normal态的图标
		{
			rRectDisabled = rFirstRect;
		}
	}

	return InitializeFromBitmap2(rDIBitmap, rRectNormal, rRectHover, rRectSelected, rRectDisabled);
}

BOOL CDIYButton::InitializeFromBitmap2(
	const CBitmap& rDIBitmap,
	const CRect& rRectNormal,
	const CRect& rRectHover,
	const CRect& rRectSelected,
	const CRect& rRectDisabled)
{
	m_bitmap.DeleteObject();  
	m_bitmapSel.DeleteObject();  
	m_bitmapHover.DeleteObject(); 
	m_bitmapFocus.DeleteObject(); 
	m_bitmapDisabled.DeleteObject(); 

	CRect rcRect = rRectNormal;

	BITMAP bmp;
	const_cast<CBitmap&>(rDIBitmap).GetBitmap( &bmp );

	CDC btnMemDC;
	btnMemDC.CreateCompatibleDC(NULL);

	CDC srcMemDC;
	srcMemDC.CreateCompatibleDC(NULL);
	srcMemDC.SelectObject( const_cast<CBitmap*>(&rDIBitmap) );

	CDC* pDC = &srcMemDC;

	CBitmap * pBitmap;
	HGDIOBJ hOldObj;

	m_bitmap.CreateCompatibleBitmap( pDC, rcRect.Width(), rcRect.Height() );  
	m_bitmapSel.CreateCompatibleBitmap( pDC, rcRect.Width(), rcRect.Height() );  
	m_bitmapHover.CreateCompatibleBitmap( pDC, rcRect.Width(), rcRect.Height() );  
	m_bitmapFocus.CreateCompatibleBitmap( pDC, rcRect.Width(), rcRect.Height() );  
	m_bitmapDisabled.CreateCompatibleBitmap( pDC, rcRect.Width(), rcRect.Height() );  
	
	{ // Normal 状态
		pBitmap = &m_bitmap;
		hOldObj = btnMemDC.SelectObject(pBitmap);
		btnMemDC.BitBlt(0,0,rcRect.Width(),
			rcRect.Height(), &srcMemDC, 
			rcRect.left, rcRect.top, SRCCOPY );
		
		pBitmap = &m_bitmapFocus;
		hOldObj = btnMemDC.SelectObject(pBitmap);
		btnMemDC.BitBlt(0,0,rcRect.Width(),
			rcRect.Height(), &srcMemDC, 
			rcRect.left, rcRect.top, SRCCOPY );
	}

	{ // Hover 状态
		rcRect = rRectHover;
		
		pBitmap = &m_bitmapHover;
		hOldObj = btnMemDC.SelectObject(pBitmap);
		btnMemDC.BitBlt(0,0,rcRect.Width(),
			rcRect.Height(), &srcMemDC, 
			rcRect.left, rcRect.top, SRCCOPY );
	}

	{ // Selected 状态
		rcRect = rRectSelected;
		pBitmap = &m_bitmapSel;
		btnMemDC.SelectObject(pBitmap);
		btnMemDC.BitBlt(0,0,rcRect.Width(),
			rcRect.Height(), &srcMemDC, 
			rcRect.left, rcRect.top, SRCCOPY );
	}

	{ // Disabled 状态
		if( !rRectDisabled.IsRectEmpty() )
			rcRect = rRectDisabled;
		else // 没有Disable状态时使用Normal态的图标
			rcRect = rRectNormal;
		
		pBitmap = &m_bitmapDisabled;
		hOldObj = btnMemDC.SelectObject(pBitmap);
		btnMemDC.BitBlt(0,0,rcRect.Width(),
			rcRect.Height(), &srcMemDC, 
			rcRect.left, rcRect.top, SRCCOPY );

		btnMemDC.SelectObject(hOldObj);
	}

	VERIFY(SetWindowPos(NULL, -1, -1, rcRect.Width(), rcRect.Height(),
		SWP_NOMOVE|SWP_NOZORDER|SWP_NOREDRAW|SWP_NOACTIVATE));

	// 注意，这里使用DIBBitmap生成窗口Range
	CreateRgnByAlphaBits( m_bitmap, m_wndClipRgn);
	
	if( m_wndClipRgn.GetSafeHandle() )
	{
		SetWindowRgn( m_wndClipRgn, TRUE );
	}

	return TRUE;
}

BOOL CDIYButton::AutoLoad(UINT nID, CWnd *pParent)
{
	// first attach the CBitmapButton to the dialog control
	if (!SubclassDlgItem(nID, pParent))
		return FALSE;

	CString buttonName;
	GetWindowText(buttonName);
	ASSERT(!buttonName.IsEmpty());      // must provide a title

	LoadBitmaps(buttonName + _T("U"), buttonName + _T("D"),
		buttonName + _T("H")
		//, NULL,NULL
		//buttonName + _T("F"), buttonName + _T("X")
		);

	// we need at least the primary
	if (m_bitmap.m_hObject == NULL)
		return FALSE;

	// size to content
	SizeToContent();
	return TRUE;
}

void CDIYButton::DrawItem(LPDRAWITEMSTRUCT lpDrawItemStruct) 
{
	//detect button state and action
	UINT uAction = lpDrawItemStruct->itemAction;
	UINT uState = lpDrawItemStruct->itemState;

	DIY_TRACE("DrawItem\n");

	CBitmap* pBitmap = NULL;
	if (uState & ODS_DISABLED)
	{
		pBitmap = &m_bitmapDisabled;
		DIY_TRACE("the btn is disable\n");
	}
	else if (uState & ODS_SELECTED)
	{
		pBitmap = &m_bitmapSel;
		DIY_TRACE("the btn is down\n");
	}
	else if (m_bHover)
	{
		pBitmap = &m_bitmapHover;
		DIY_TRACE("the btn is hover\n");
	}	
#if(WINVER >= 0x0500)
	else if (uState & ODS_HOTLIGHT	)
	{
		pBitmap = &m_bitmapHover;
		DIY_TRACE("the btn is hotlight\n");
	}
#endif
	else if (uState & ODS_FOCUS)
	{
		pBitmap = &m_bitmapFocus;
		DIY_TRACE("the btn is focused\n");
	}
	else
	{
		pBitmap = &m_bitmap;
		DIY_TRACE("the btn is normal\n");
	}

	// draw the whole button
	CDC* pDC = CDC::FromHandle(lpDrawItemStruct->hDC);
	CDC memDC;
	memDC.CreateCompatibleDC(NULL);
	CBitmap* pOld = memDC.SelectObject(pBitmap);
	if (pOld == NULL)
		return;     // destructors will clean up

	CRect rect;
	rect.CopyRect(&lpDrawItemStruct->rcItem);
	
	// 如果窗口被剪裁，那么此时设置窗口剪裁区作为DC的剪裁区
	//if(m_wndClipRgn.GetSafeHandle())
	//	pDC->SelectClipRgn( &m_wndClipRgn );

	// Alpha混合渲染，这样可以透出背景
	BLENDFUNCTION blend = {0};
	blend.SourceConstantAlpha = 255;
	blend.AlphaFormat = AC_SRC_ALPHA;
	blend.BlendOp = AC_SRC_OVER;
	pDC->AlphaBlend(rect.left, rect.top, rect.Width(), rect.Height(),
		&memDC, 0, 0, rect.Width(), rect.Height(), blend );

	memDC.SelectObject(pOld);
	
	//LONG lStyle = GetWindowLong( GetSafeHwnd(), GWL_STYLE );
	//if ( (lStyle & WS_TABSTOP) && (uState & ODS_FOCUS) )
	//{
	//	rect.DeflateRect( 3,3 );
	//	pDC->DrawFocusRect(rect);
	//}

	//if ( m_bHover )
	//{
	//	pDC->SetROP2(R2_MASKPEN);
	//	pDC->SelectStockObject(NULL_PEN);
	//	CBrush brush;
	//	brush.CreateSolidBrush( RGB(255,255,0) );// yellow - green brush
	//	pDC->SelectObject(&brush);
	//	pDC->Rectangle(rect);
	//}
	//else if ( uState & ODS_FOCUS )
	//{
	//	//rect.DeflateRect( 3,3 );
	//	//pDC->DrawFocusRect(rect);

	//	// 黄绿色蒙板作为焦点效果
	//	pDC->SetROP2(R2_MASKPEN);
	//	pDC->SelectStockObject(NULL_PEN);
	//	CBrush brush;
	//	brush.CreateSolidBrush( RGB(205,255,0) );// yellow - green brush
	//	pDC->SelectObject(&brush);
	//	pDC->Rectangle(rect);
	//}
}

void CDIYButton::OnMouseMove(UINT nFlags, CPoint point) 
{
	if (!m_bHover)
	{
		TRACKMOUSEEVENT tme;
		tme.cbSize = sizeof(tme);
		tme.hwndTrack = m_hWnd;
		tme.dwFlags = TME_LEAVE;
		tme.dwHoverTime = 1;
		_TrackMouseEvent(&tme);
		m_bHover = TRUE;
		InvalidateRect(NULL, FALSE);
	}
	CButton::OnMouseMove(nFlags, point);
}

LRESULT CDIYButton::OnMouseLeave(WPARAM wParam, LPARAM lParam)
{
	m_bHover = FALSE;
	InvalidateRect(NULL, FALSE);
	return 0;
}

void CDIYButton::PreSubclassWindow() 
{
	ModifyStyle(0, BS_OWNERDRAW);
	CButton::PreSubclassWindow();
}

BOOL CDIYButton::OnEraseBkgnd(CDC* pDC)
{
	// 不填充背景，这样可以透出父窗口的前景色
	return TRUE;
}
