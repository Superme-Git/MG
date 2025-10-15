#include "StdAfx.h"
#include "DeviceContext.h"

#ifndef ASSERT
#define  ASSERT(s) _ASSERT(s)
#endif

CDeviceContext::CDeviceContext(void)
	: m_hDC (NULL)
{
}

CDeviceContext::CDeviceContext(HDC hDC)
	: m_hDC (hDC)
{
}

CDeviceContext::~CDeviceContext(void)
{
	if (m_hDC != NULL)
		::DeleteDC(Detach());
}

// 将位图对象选进绘制及显示设备
HBITMAP CDeviceContext::SelectBitmap(HBITMAP hBitmap)
{
	_ASSERT( m_hDC != NULL );
	return (HBITMAP)::SelectObject( m_hDC, (HGDIOBJ) hBitmap );
}

// 关联逻辑设备句柄
BOOL CDeviceContext::Attach(HDC hDC)
{
	// only attach once, detach on destroy
	// only attach to an empty DC
	_ASSERT( m_hDC == NULL );

	if (hDC == NULL)
	{
		return FALSE;
	}

	m_hDC = hDC;
	return hDC != NULL;
}

// 解除关联的逻辑设备
HDC CDeviceContext::Detach(void)
{
	HDC hDC = m_hDC;
	m_hDC = NULL;
	return hDC;
}

// 删除逻辑设备
BOOL CDeviceContext::DeleteDC()
{
	if (m_hDC == NULL)
		return FALSE;

	return ::DeleteDC(Detach());
}

int CDeviceContext::SaveDC()
{
	_ASSERT(m_hDC != NULL);
	int nRetVal = 0;
	if (::SaveDC(m_hDC) != 0)
		nRetVal = -1;   // -1 is the only valid restore value for complex DCs
	return nRetVal;
}

BOOL CDeviceContext::RestoreDC(int nSavedDC)
{
	_ASSERT(m_hDC != NULL);
	BOOL bRetVal = ::RestoreDC(m_hDC, nSavedDC);
	return bRetVal;
}

HGDIOBJ CDeviceContext::SelectStockObject(int nIndex)
{
	HGDIOBJ hObject = ::GetStockObject(nIndex);
	_ASSERT(hObject != NULL);
	HGDIOBJ hOldObj = ::SelectObject( m_hDC, hObject );
	return hOldObj;
}

COLORREF CDeviceContext::SetBkColor(COLORREF crColor)
{
	_ASSERT(m_hDC != NULL);
	return ::SetBkColor(m_hDC, crColor);
}

int CDeviceContext::SetBkMode(int nBkMode)
{
	_ASSERT(m_hDC != NULL);
	return ::SetBkMode(m_hDC, nBkMode);
}

COLORREF CDeviceContext::SetTextColor(COLORREF crColor)
{
	_ASSERT(m_hDC != NULL);
	return ::SetTextColor(m_hDC, crColor);
}

int CDeviceContext::GetClipBox(LPRECT lpRect) const
{
	ASSERT(m_hDC != NULL);
	return ::GetClipBox(m_hDC, lpRect);
}

BOOL CDeviceContext::BitBlt(int x, int y, int nWidth, int nHeight, HDC hSrcDC,
			int xSrc, int ySrc, DWORD dwRop)
{
	ASSERT(m_hDC != NULL);
	return ::BitBlt( m_hDC, x, y, nWidth, nHeight, hSrcDC, xSrc, ySrc, dwRop );
}

BOOL CDeviceContext::BitBlt(int x, int y, int nWidth, int nHeight, CDeviceContext* pSrcDC,
							int xSrc, int ySrc, DWORD dwRop)
{
	ASSERT(m_hDC != NULL);
	return ::BitBlt( m_hDC, x, y, nWidth, nHeight, pSrcDC->m_hDC, xSrc, ySrc, dwRop );
}

BOOL CDeviceContext::StretchBlt(int x, int y, int nWidth, int nHeight, HDC hSrcDC,
								int xSrc, int ySrc, int nSrcWidth, int nSrcHeight, DWORD dwRop)
{
	ASSERT(m_hDC != NULL);
	return ::StretchBlt( m_hDC, x, y, nWidth, nHeight, hSrcDC,
		xSrc, ySrc, nSrcWidth, nSrcHeight, dwRop );
}

BOOL CDeviceContext::StretchBlt(int x, int y, int nWidth, int nHeight, CDeviceContext* pSrcDC,
				int xSrc, int ySrc, int nSrcWidth, int nSrcHeight, DWORD dwRop)
{
	ASSERT(m_hDC != NULL);
	return ::StretchBlt( m_hDC, x, y, nWidth, nHeight, pSrcDC->m_hDC,
		xSrc, ySrc, nSrcWidth, nSrcHeight, dwRop );
}

BOOL CDeviceContext::Rectangle(int left, int top, int right, int bottom)
{
	ASSERT(m_hDC != NULL);
	return ::Rectangle(m_hDC, left, top, right, bottom);
}


BOOL CDeviceContext::MoveTo( int x, int y)
{
	ASSERT(m_hDC != NULL);
	return ::MoveToEx( m_hDC, x, y, NULL );
}

BOOL CDeviceContext::LineTo( int x, int y)
{
	ASSERT(m_hDC != NULL);
	return ::LineTo( m_hDC, x, y);
}

CWindowDeviceContext::CWindowDeviceContext(void)
{
}

CWindowDeviceContext::~CWindowDeviceContext(void)
{
}

CClientDeviceContext::CClientDeviceContext(void)
{
}

CClientDeviceContext::~CClientDeviceContext(void)
{
}

CScreenDeviceContext::CScreenDeviceContext(void)
{
}

CScreenDeviceContext::~CScreenDeviceContext(void)
{
}

BOOL CScreenDeviceContext::CreateDC()
{
	m_hDC = ::CreateDCW( L"DISPLAY", NULL, NULL , NULL );
	return m_hDC != NULL;
}

#define errhandler(s) 

HBITMAP CScreenDeviceContext::CaptureToBitmap( )
{
	ASSERT(m_hDC != NULL);
	if ( m_hDC == NULL )
	{
		return NULL;
	}

	// Create a normal DC and a memory DC for the entire screen. The 
	// normal DC provides a "snapshot" of the screen contents. The 
	// memory DC keeps a copy of this "snapshot" in the associated 
	// bitmap. 
	HDC hdcScreen = m_hDC;  // Should have been created!
	HDC hdcCompatible = ::CreateCompatibleDC(hdcScreen); 

	// Create a compatible bitmap for hdcScreen. 
	int nWidth = ::GetDeviceCaps(hdcScreen, HORZRES);
	int nHeight = ::GetDeviceCaps(hdcScreen, VERTRES);
	HBITMAP hbmScreen = ::CreateCompatibleBitmap(hdcScreen, nWidth, nHeight); 
	if( hbmScreen == 0 ) 
	{
		errhandler("hbmScreen"); 
		return NULL;
	}

	// Select the bitmaps into the compatible DC. 
	HGDIOBJ hOldBitmap = ::SelectObject(hdcCompatible, hbmScreen);
	if (!hOldBitmap ) 
	{
		errhandler("Compatible Bitmap Selection"); 
		return NULL;
	}

	//Copy color data for the entire display into a 
	//bitmap that is selected into a compatible DC. 
	if (!::BitBlt(hdcCompatible, 0,0, nWidth, nHeight, 
		hdcScreen, 0,0, SRCCOPY)) 
	{
		errhandler("Screen to Compat Blt Failed"); 
		return NULL;
	}

	::SelectObject( hdcCompatible, hOldBitmap );

	::DeleteDC( hdcCompatible );

	return hbmScreen;
}

BOOL CScreenDeviceContext::CaptureToDC( HDC hDC )
{
	ASSERT(m_hDC != NULL);
	if ( m_hDC == NULL ) // Should have been created!
	{
		return FALSE;
	}

	int nWidth = ::GetDeviceCaps(m_hDC, HORZRES);
	int nHeight = ::GetDeviceCaps(m_hDC, VERTRES);

	//Copy color data for the entire display into the target DC. 
	if (!::BitBlt(hDC, 0,0, nWidth, nHeight, 
		m_hDC, 0,0, SRCCOPY) ) 
	{
		errhandler("Screen to Compat Blt Failed"); 
		return FALSE;
	}

	return TRUE;
}