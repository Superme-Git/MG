#include "StdAfx.h"
#include "ScreenshotWnd.h"
#include "DeviceContext.h"

CScreenshotWnd::CScreenshotWnd(void)
	: m_hBitmap( NULL)
{
}

CScreenshotWnd::~CScreenshotWnd(void)
{
}

int CScreenshotWnd::OnCreate(LPCREATESTRUCT lpCreateStruct )
{
	CaptureScreen();
	return __super::OnCreate(lpCreateStruct);
}

BOOL CScreenshotWnd::CaptureScreen()
{
	CScreenDeviceContext dc;
	if( dc.CreateDC() )
	{
		m_hBitmap = dc.CaptureToBitmap( );
	}
	return m_hBitmap != NULL;
}

void CScreenshotWnd::OnDraw( HDC hDC )
{
	if( m_hBitmap != NULL )
	{
		CDeviceContext dc(hDC);

		CRect rcClipBox;
		dc.GetClipBox( &rcClipBox );
		CDeviceContext dcCompatible;
		dcCompatible.CreateCompatibleDC( &dc );
		HBITMAP hOldBmp = dcCompatible.SelectBitmap( m_hBitmap );
		dc.BitBlt( 0, 0, rcClipBox.Width(), rcClipBox.Height(), &dcCompatible, 0, 0, SRCCOPY );
		dcCompatible.SelectBitmap( hOldBmp );
	}
}
