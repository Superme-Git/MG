#include "stdafx.h"
#include "DIYEditCtrl.h"
#include "BitmapUtils.h"


IMPLEMENT_DYNAMIC(CDIYEditCtrl, CEdit)

CDIYEditCtrl::CDIYEditCtrl()
{
	
}

CDIYEditCtrl::~CDIYEditCtrl()
{
	m_brush.DeleteObject();
	m_bitmap.DeleteObject();
	m_wndClipRgn.DeleteObject();
}

BEGIN_MESSAGE_MAP(CDIYEditCtrl, CEdit)
	ON_WM_CREATE()
	ON_WM_CHAR()
//	ON_CONTROL_REFLECT(EN_CHANGE, OnChange)
	ON_WM_CTLCOLOR_REFLECT()
	ON_WM_ERASEBKGND()
END_MESSAGE_MAP()

int CDIYEditCtrl::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CWnd::OnCreate(lpCreateStruct) == -1)
		return -1;
}

BOOL CDIYEditCtrl::LoadBitmaps(LPCTSTR lpszBitmapResource)
{
	// delete old bitmaps (if present)
	m_bitmap.DeleteObject(); 
	if (!m_bitmap.LoadBitmap(lpszBitmapResource))
	{
		TRACE0("Failed to load bitmap for normal image.\n");
		return FALSE;   // need this one image
	}
	BOOL bAllLoaded = TRUE;
	return bAllLoaded;
}

void CDIYEditCtrl::DrawItem(LPDRAWITEMSTRUCT lpDrawItemStruct) 
{
	return;
	//detect button state and action
	UINT uAction = lpDrawItemStruct->itemAction;
	UINT uState = lpDrawItemStruct->itemState;

	CBitmap* pBitmap = NULL;
	
	pBitmap = &m_bitmap;

	// draw the whole button
	CDC* pDC = CDC::FromHandle(lpDrawItemStruct->hDC);
	CDC memDC;
	memDC.CreateCompatibleDC(NULL);
	CBitmap* pOld = memDC.SelectObject(pBitmap);
	if (pOld == NULL)
		return;

	CRect rect;
	rect.CopyRect(&lpDrawItemStruct->rcItem);

	BLENDFUNCTION blend = {0};
	blend.SourceConstantAlpha = 255;
	blend.AlphaFormat = AC_SRC_ALPHA;
	blend.BlendOp = AC_SRC_OVER;
	pDC->AlphaBlend(rect.left, rect.top, rect.Width(), rect.Height(),
		&memDC, 0, 0, rect.Width(), rect.Height(), blend );

	memDC.SelectObject(pOld);

	/*CRect rc;
	GetClientRect(&rc);
	pDC = GetDC();
	TEXTMETRIC tm;
	pDC->GetTextMetrics(&tm);
	int h = tm.tmHeight + tm.tmExternalLeading;
	int margin = (rc.Height() - h)/2;
	rc.DeflateRect(0, margin);
	SetRectNP(&rc);*/
}

BOOL CDIYEditCtrl::InitializeFromBitmap(const CBitmap& rDIBitmap, const CRect& rRectNormal, BOOL bHasDisabledStatusBitmap)
{
	m_bitmap.DeleteObject();  

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

	m_bitmap.CreateCompatibleBitmap( pDC, rcRect.Width(), rcRect.Height() );  
	
	pBitmap = &m_bitmap;
	btnMemDC.SelectObject(pBitmap);
	btnMemDC.BitBlt(0, 0, rcRect.Width(),
		rcRect.Height(), &srcMemDC, 
		rcRect.left, rcRect.top, SRCCOPY );
	
	m_brush.CreatePatternBrush(&m_bitmap);

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

void CDIYEditCtrl::PreSubclassWindow() 
{
	ModifyStyle(0, BS_OWNERDRAW);
	CEdit::PreSubclassWindow();
}

BOOL CDIYEditCtrl::OnEraseBkgnd(CDC* pDC)
{
	// 不填充背景，这样可以透出父窗口的前景色
	return TRUE;
}

HBRUSH CDIYEditCtrl::CtlColor(CDC *pDC, UINT nCtlColor)
{
	pDC->SetBkMode(TRANSPARENT);
	pDC->SetTextColor(RGB(255, 255, 255));
	return (HBRUSH)m_brush;
}

void CDIYEditCtrl::OnChange()
{
	Invalidate();
}

void CDIYEditCtrl::OnChar(UINT nChar, UINT nRep, UINT nFlags)
{
	Invalidate();
	CEdit::OnChar(nChar, nRep, nFlags);
}