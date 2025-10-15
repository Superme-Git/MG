// DXTestView.cpp : implementation of the CDXTestView class
//

#include "stdafx.h"
#include "DXTest.h"

#include "DXTestDoc.h"
#include "DXTestView.h"
#include <d3d9types.h> // for D3DCOLOR_ARGB
#include "EditPartView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

BITMAPINFO* texReadPIC( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels);
BITMAPINFO* texReadP0X( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels);
BITMAPINFO* texReadB0X( LPCTSTR fileName, HBITMAP* pBitmap, char** ppPixels);
BITMAPINFO* texReadTGA( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels);
// CDXTestView

IMPLEMENT_DYNCREATE(CDXTestView, CView)

BEGIN_MESSAGE_MAP(CDXTestView, CView)
	ON_WM_CREATE()
	ON_WM_DESTROY()
	ON_WM_SIZE()
	ON_WM_ERASEBKGND()
	ON_COMMAND( ID_FILE_OPEN, OnFileOpen )
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_WM_CONTEXTMENU()
	ON_COMMAND(ID_SVG_ZOOMIN, &CDXTestView::OnSvgZoomin)
	ON_COMMAND(ID_SVG_ZOOMOUT, &CDXTestView::OnSvgZoomout)
	ON_COMMAND(ID_SVG_ORIGINALVIEW, &CDXTestView::OnSvgOriginalview)
	ON_COMMAND(ID_SVG_ROTATE_CW, &CDXTestView::OnSvgRotateClockwised)
	ON_COMMAND(ID_SVG_ROTATE_CCW, &CDXTestView::OnSvgRotateCounterClockwised)
	
	ON_UPDATE_COMMAND_UI(ID_SVG_ZOOMIN, &CDXTestView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ZOOMOUT, &CDXTestView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ORIGINALVIEW, &CDXTestView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ROTATE_CW, &CDXTestView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ROTATE_CCW, &CDXTestView::OnUpdateNeedSvg)

	ON_COMMAND(ID_OPTION_BACKGROUND, &CDXTestView::OnOptionBackground)
	ON_COMMAND(ID_OPTION_CLIPBYCLIENT, &CDXTestView::OnOptionClipByClient)
	ON_UPDATE_COMMAND_UI(ID_OPTION_CLIPBYCLIENT, &CDXTestView::OnUpdateOptionClipByClient)
	ON_COMMAND(ID_OPTION_IMAGEBACKGROUND, &CDXTestView::OnOptionImagebackground)
	ON_COMMAND(ID_OPTION_FULLTRANSPARENT, &CDXTestView::OnOptionFulltransparent)
	ON_UPDATE_COMMAND_UI(ID_OPTION_FULLTRANSPARENT, &CDXTestView::OnUpdateOptionFulltransparent)
	ON_UPDATE_COMMAND_UI(ID_OPTION_IMAGEBACKGROUND, &CDXTestView::OnUpdateOptionImagebackground)
	ON_WM_KEYDOWN()
	ON_WM_SETCURSOR()
END_MESSAGE_MAP()

// CDXTestView construction/destruction

CDXTestView::CDXTestView()
{
	//m_pRenderer = NULL;
	m_hPicture = -1;
	m_bLastIsSVG = FALSE;

	m_clrBackground = D3DCOLOR_ARGB( 255, 238,242,251 );
	m_bClipToClient = TRUE;
	m_clrImageBackground = D3DCOLOR_ARGB( 255, 255, 255, 255);
	m_bFullTransparent = FALSE;

	m_rcPicture.SetRect( 0, 0, 0, 0);

	m_buffer = NULL;
	m_buffersize = 0;

	m_bMouseDown = FALSE;
	m_pEditPartView = NULL;
	m_dcMem = NULL;
}

CDXTestView::~CDXTestView()
{
	if( m_buffer)
	{
		free( m_buffer);
		m_buffer = NULL;
	}
	if( m_dcMem)
	{
		::DeleteDC( m_dcMem);
		m_dcMem = NULL;
	}
	m_buffersize = 0;
	//m_pRenderer = NULL;
	m_hPicture = -1;
}


LONG CDXTestView::Init( HWND hwnd)
{
	//if ( m_pRenderer != NULL )
	//{
	//	return 0;
	//}

	//RECT rcClient;
	//::GetClientRect( hwnd , &rcClient );
	//Nuclear::XDisplayMode dm(1280, 1024, 32);
	//bool bRet = CreateRenderer(&m_pRenderer, hwnd,  dm, 0, Nuclear::XPRV_DEFAULT );

	//return (LONG)bRet;
	return 0;
}

void CDXTestView::Destroy()
{
	//if ( m_pRenderer != NULL )
	//{
	//	DestroyRenderer( m_pRenderer );
	//	m_pRenderer = NULL; 
	//}
}

BOOL CDXTestView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return CView::PreCreateWindow(cs);
}

// CDXTestView drawing

void CDXTestView::OnDraw(CDC* pDC)
{
	CRect rectClient;
	GetClientRect(&rectClient);

	CTime timeCurrent = CTime::GetCurrentTime();
	CString m_Stemp = timeCurrent.Format( _T("%Y-%m-%d %H:%M:%S") );
	
	if( m_bClipToClient)
	{
		::BitBlt( pDC->GetSafeHdc(), 0, 0, rectClient.right, rectClient.bottom, m_dcMem, 0, 0, SRCCOPY);
	}
	else
	{
		CDC dcTemp;
		dcTemp.CreateCompatibleDC( pDC );
		HBITMAP hBmp = ::CreateCompatibleBitmap( pDC->GetSafeHdc(), rectClient.right, rectClient.bottom);
		HBITMAP hOld = (HBITMAP)::SelectObject( dcTemp.GetSafeHdc(), hBmp);

		dcTemp.FillSolidRect( 0, 0, rectClient.right, rectClient.bottom, m_clrBackground);
		::BitBlt( dcTemp.GetSafeHdc(), 0, 0, m_rcPicture.right, m_rcPicture.bottom, m_dcMem, 0, 0, SRCCOPY);
		pDC->BitBlt( 0, 0, rectClient.right, rectClient.bottom, &dcTemp, 0, 0, SRCCOPY);

		::SelectObject( dcTemp.GetSafeHdc(), hOld);
		::DeleteDC( dcTemp);
		::DeleteObject( hBmp);
	}
	//if ( m_pRenderer != NULL )
	//{
	//	HRESULT hr;
	//	hr = m_pRenderer->Begin();
	//	m_pRenderer->Clear( m_clrBackground);//D3DCOLOR_ARGB(255,238,242,251) );

	//	if (m_Stemp.GetLength()>0)
	//	{
	//		m_rectClient.MoveToXY( 20, 20 );
	//		hr = m_pRenderer->DrawText(m_Stemp,m_Stemp.GetLength(),&m_rectClient,
	//			0xFF000000);
	//	}

	//	m_rectClient.MoveToXY( 0, 0 );
	//	if( m_hPicture != -1 )
	//	{
	//		Nuclear::FRECT fRect;
	//		if( m_bClipToClient)
	//		{
	//			fRect.Assign( (float)m_rectClient.left, (float)m_rectClient.top,
	//				(float)m_rectClient.right, (float)(m_rectClient.bottom));
	//		}
	//		else
	//		{
	//			fRect.Assign((float)m_rcPicture.left, (float)m_rcPicture.top,
	//				(float)m_rcPicture.right, (float)m_rcPicture.bottom);
	//		}
	//		m_pRenderer->DrawPicture( m_hPicture, fRect, (DWORD)0xFFFFFFFF );
	//	}

	//	hr = m_pRenderer->End();
	//}
}

// CDXTestView message handlers

int CDXTestView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CView::OnCreate(lpCreateStruct) == -1)
		return -1;

	m_dcMem = ::CreateCompatibleDC( NULL);

	return 0;
}

void CDXTestView::OnDestroy()
{
	CView::OnDestroy();

	Destroy();
}

void CDXTestView::OnInitialUpdate()
{
	CView::OnInitialUpdate();

	Init( GetSafeHwnd() );

	//CString filePath = GetDocument()->GetPathName();
	//if ( !filePath.IsEmpty() )
	//{
	//	ShowPicture( filePath );
	//}

	CRect rcClient;
	GetClientRect(&rcClient);

	//m_svg_render.init( rcClient.Width(), rcClient.Height(), agg::window_keep_aspect_ratio );
	/*m_svg_render.init( rcClient.Width(), rcClient.Height(), agg::no_window );*/
}

void CDXTestView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);

	Refresh();

	//if ( m_pRenderer != NULL )
	//{
	//	CRect rcClient;
	//	GetClientRect(&rcClient);

	//	Nuclear::CRECT rect;
	//	rect.Assign( rcClient.left, rcClient.top, rcClient.right, rcClient.bottom );
	//	m_pRenderer->SetSourceRect( &rect );
	//	m_pRenderer->SetDestRect( &rect );

	//	Refresh();
	//}
}

BOOL CDXTestView::OnEraseBkgnd(CDC* pDC)
{
	return TRUE;
}

//D3DXIFF_BMP = 0,
//D3DXIFF_JPG = 1,
//D3DXIFF_TGA = 2,
//D3DXIFF_PNG = 3,
//D3DXIFF_DDS = 4,
//D3DXIFF_PPM = 5,
//D3DXIFF_DIB = 6,
//D3DXIFF_HDR = 7,
//D3DXIFF_PFM = 8,

void CDXTestView::ShowPicture( LPCTSTR lpszFilePath )
{
//	if ( lpszFilePath == NULL )
//	{
//		return ;
//	}
//
//	if ( m_pRenderer == NULL )
//	{
//		return ;
//	}
//
//	if ( m_hPicture != -1)
//	{
//		m_pRenderer->FreePicture(m_hPicture);
//		m_hPicture = -1;
//	}
//
//	m_bLastIsSVG = FALSE;
//	m_strFilePath = lpszFilePath;
//	CString strExt = m_strFilePath.Right(4);
//	CString strSimpleExt = strExt.Mid(0,3);
//
//	if ( strExt.CompareNoCase(_T(".svg") ) == 0 )
//	{
//
//		m_bLastIsSVG = TRUE;
//	}
//	else if ( strExt.CompareNoCase(_T(".pic") ) == 0 ||
//		strSimpleExt.CompareNoCase(_T(".p0") ) == 0 || 
//		strSimpleExt.CompareNoCase(_T(".b0") ) == 0 )
//	{
//		char*			qt_pPixels = NULL;
//		BITMAPINFO*		qt_pBitmapInfo = NULL;
//		HBITMAP			qt_hBITMAP = NULL;
//
//		if ( strExt.CompareNoCase(_T(".pic") ) == 0 )
//		{
//			qt_pBitmapInfo =  texReadPIC (m_strFilePath, &qt_hBITMAP, &qt_pPixels);
//		}
//		else if ( strSimpleExt.CompareNoCase(_T(".p0") ) == 0 )
//		{
//			qt_pBitmapInfo =  texReadP0X (m_strFilePath, &qt_hBITMAP, &qt_pPixels);
//		}
//		else // .b0x
//		{
//			qt_pBitmapInfo =  texReadB0X (m_strFilePath, &qt_hBITMAP, &qt_pPixels);
//		}
//
//		if (!qt_pBitmapInfo)
//		{
//			::MessageBox( GetSafeHwnd(), _T("Could not read file") , m_strFilePath, MB_OK | MB_ICONERROR );
//			return;
//		}
//
//		m_BitmapInfo = *qt_pBitmapInfo;
//		
//		BYTE *pBuffer = (BYTE *)qt_pPixels;
//		int BufSize = m_BitmapInfo.bmiHeader.biSizeImage;
//
//		// 注:这里的qt_pPixels原本就是DIB图像数据的Buffer指针，然而，实际上，
//		// 我们也可以使用GetDIBits函数从hBITMAP中获取DIB图像数据
//		// 唯一的区别就是，GetDIBits函数需要指定一个输出的缓冲区，它负责填充这个缓冲区。
//		// 因此，这两种模式在不同的场合下，效率上会有些不同。
//
//#if 0
//		// 由HBITMAP获取DIB图像数据
//
//		HDC hScreenDC = ::GetDC(NULL);
//		BYTE *pBuffer = new BYTE[ BufSize ];
//		if ( pBuffer != NULL )
//		{
//			int iRet = GetDIBits( hScreenDC, qt_hBITMAP, 0, m_BitmapInfo.bmiHeader.biHeight, 
//				pBuffer, &m_BitmapInfo, DIB_RGB_COLORS );
//			if ( iRet == m_BitmapInfo.bmiHeader.biHeight )
//			{
//				// 目前只支持32 argb
//				int hPicture = m_pRenderer->NewPicture32(
//					m_BitmapInfo.bmiHeader.biWidth , m_BitmapInfo.bmiHeader.biHeight );
//				if ( hPicture != -1 )
//				{	
//					m_pRenderer->SetPictureData32(hPicture, pBuffer /*qt_pPixels*/, BufSize);
//					m_hPicture = hPicture;
//				}
//			}
//
//			delete[] pBuffer;
//		}
//
//		if (hScreenDC)
//		{
//			::ReleaseDC(NULL, hScreenDC);
//		}
//#else
//		// 直接使用texReadPIC返回的内存指针
//
//		// 目前只支持32 argb
//		int hPicture = m_pRenderer->NewPicture(
//			m_BitmapInfo.bmiHeader.biWidth , m_BitmapInfo.bmiHeader.biHeight );
//		if ( hPicture != -1 )
//		{	
//			m_pRenderer->SetPictureData(hPicture, pBuffer, BufSize);
//			m_hPicture = hPicture;
//		}
//#endif
//
//		if (qt_hBITMAP) DeleteObject (qt_hBITMAP);
//		if (qt_pBitmapInfo) HeapFree (GetProcessHeap(), 0 , qt_pBitmapInfo);
//
//	}
//	else if ( strExt.CompareNoCase(_T(".jpg") ) == 0 ||
//		strExt.CompareNoCase(_T(".png") ) == 0 ||
//		strExt.CompareNoCase(_T(".bmp") ) == 0 ||
//		strExt.CompareNoCase(_T(".tga") ) == 0 ||
//		strExt.CompareNoCase(_T(".dds") ) == 0 ||
//		strExt.CompareNoCase(_T(".dib") ) == 0 ||
//		strExt.CompareNoCase(_T(".hdr") ) == 0 ||
//		strExt.CompareNoCase(_T(".pfm") ) == 0 ||
//		strExt.CompareNoCase(_T(".ppm") ) == 0)
//	{
//		if ( m_pRenderer != NULL )
//			m_hPicture = m_pRenderer->LoadPicture( m_strFilePath );
//		if ( m_hPicture != -1 )
//		{
//			Nuclear::PictureInfo picInfo;
//			m_pRenderer->GetPictureInfo( m_hPicture, picInfo );
//
//			m_BitmapInfo.bmiHeader.biWidth = picInfo.m_nPicWidth ;
//			m_BitmapInfo.bmiHeader.biHeight = picInfo.m_nPicHeight;
//		}
//	}
//
//	Refresh();	
}

HBITMAP CreateDIBitmap(LPVOID pBuffer, unsigned nWidth, unsigned nHeight, BITMAPINFO** ppBitmapInfo/* = NULL*/)
{
	HBITMAP hBitmap = NULL;
	LPVOID pDibBits = NULL;
	DWORD biSizeImage = nWidth*nHeight*4;
	if( ppBitmapInfo == NULL )
	{
		BITMAPINFO tBitmapInfo = {{0},{0}};
		tBitmapInfo.bmiHeader.biSize = sizeof (BITMAPINFOHEADER);
		tBitmapInfo.bmiHeader.biPlanes = 1;
		tBitmapInfo.bmiHeader.biBitCount = 32;
		tBitmapInfo.bmiHeader.biCompression = BI_RGB;
		tBitmapInfo.bmiHeader.biXPelsPerMeter = 0;
		tBitmapInfo.bmiHeader.biYPelsPerMeter = 0;
		tBitmapInfo.bmiHeader.biClrUsed = 0;
		tBitmapInfo.bmiHeader.biClrImportant = 0;
		tBitmapInfo.bmiHeader.biWidth = nWidth;
		tBitmapInfo.bmiHeader.biHeight = -(LONG)nHeight;
		tBitmapInfo.bmiHeader.biSizeImage = biSizeImage;
		hBitmap = ::CreateDIBSection( NULL,
			&tBitmapInfo, DIB_RGB_COLORS, &pDibBits, NULL, 0 );
		if( hBitmap == NULL || pDibBits == NULL )
		{
			return NULL;
		}
	}
	else
	{
		*ppBitmapInfo = (BITMAPINFO*)::HeapAlloc( ::GetProcessHeap(), 0, sizeof(BITMAPINFO) );
		if( *ppBitmapInfo == NULL )
		{
			return NULL;
		}

		BITMAPINFO& tBitmapInfo = **ppBitmapInfo;

		tBitmapInfo.bmiHeader.biSize = sizeof (BITMAPINFOHEADER);
		tBitmapInfo.bmiHeader.biPlanes = 1;
		tBitmapInfo.bmiHeader.biBitCount = 32;
		tBitmapInfo.bmiHeader.biCompression = BI_RGB;
		tBitmapInfo.bmiHeader.biXPelsPerMeter = 0;
		tBitmapInfo.bmiHeader.biYPelsPerMeter = 0;
		tBitmapInfo.bmiHeader.biClrUsed = 0;
		tBitmapInfo.bmiHeader.biClrImportant = 0;
		tBitmapInfo.bmiHeader.biWidth = nWidth;
		tBitmapInfo.bmiHeader.biHeight = -(LONG)nHeight;
		tBitmapInfo.bmiHeader.biSizeImage = biSizeImage;

		hBitmap = ::CreateDIBSection( NULL,
			&tBitmapInfo, DIB_RGB_COLORS, &pDibBits, NULL, 0 );
		if( hBitmap == NULL || pDibBits == NULL )
		{
			::HeapFree( ::GetProcessHeap(), 0, *ppBitmapInfo );
			return NULL;
		}
	}

	memcpy( pDibBits, pBuffer, biSizeImage );
	return hBitmap;
}

void CDXTestView::Refresh()
{
	CRect rcClient;
	GetClientRect( &rcClient );

	if ( rcClient.IsRectEmpty() || rcClient.IsRectNull()  )
	{
		return;
	}

	if ( m_bLastIsSVG )
	{
		//if ( m_hPicture != -1)
		//{
		//	m_pRenderer->FreePicture(m_hPicture);
		//	m_hPicture = -1;
		//}

		// 目前只支持32 argb
		int iDeviceWidth = rcClient.Width();
		int iDeviceHeight = rcClient.Height();
		if( !m_bClipToClient )
		{
			int iShowWidth = (int)m_rcPicture.right;
			int iShowHeight = (int)m_rcPicture.bottom;

			if( iShowWidth < iDeviceWidth )
			{
				iDeviceWidth = iShowWidth;
			}

			if( iShowHeight < iDeviceHeight )
			{
				iDeviceHeight = iShowHeight;
			}
		}

		//int hPicture = m_pRenderer->NewPicture(	iDeviceWidth, iDeviceHeight);
		
		int count = iDeviceWidth * iDeviceHeight * 4;
		if( NULL == m_buffer)
		{
			m_buffer = (char*)malloc( count);
			m_buffersize = count;
		}
		else
		{
			if( m_buffersize < count)
			{
				m_buffer = (char*)realloc( m_buffer, count);
				m_buffersize = count;
			}
		}
		agg::rendering_buffer renderbuffer;
		renderbuffer.attach( (agg::int8u*)m_buffer, iDeviceWidth, iDeviceHeight, iDeviceWidth*4);
		
		if ( m_bFullTransparent  )
		{
			agg::rgba8 c = agg::argb8_packed( 0 );
			m_svg_render.set_bg_color( c );
		}
		else
		{
			agg::rgba8 c = agg::argb8_packed( m_clrImageBackground );
			m_svg_render.set_bg_color( c );
		}

		m_svg_render.draw_on_buffer( renderbuffer, true, false);
				
		//if ( hPicture != -1 )
		//{
		//	//int size = m_rcPicture.bottom * buffer.stride_abs();

		//	m_pRenderer->SetPictureData( hPicture, m_buffer, count);

		//	m_hPicture = hPicture;
		//}

		HBITMAP hBmp = CreateDIBitmap( m_buffer, iDeviceWidth, iDeviceHeight, NULL);
		if( hBmp)
		{
			HBITMAP hOld = (HBITMAP)::SelectObject( m_dcMem, hBmp);
			if( hOld)
			{
				::DeleteObject( hOld);
			}
		}

		// the bounding box of the picture is the client box
	}
	else
	{
		// Save the bounding box of the picture
		int newWidth  = m_BitmapInfo.bmiHeader.biWidth;
		int newHeight = m_BitmapInfo.bmiHeader.biHeight;

		CSize szClient = rcClient.Size();
		// scale based on which is higher			
		if (newWidth > szClient.cx)
		{
			newHeight = (newHeight*szClient.cx)/newWidth;
			newWidth  = szClient.cx;
		}
		if (newHeight > szClient.cy)
		{
			newWidth  = (newWidth*szClient.cy)/newHeight;
			newHeight = szClient.cy;
		}

		// Save the bounding box of the picture
		//m_rcPicture.left = (szClient.cx-newWidth)/2;
		//m_rcPicture.top = (szClient.cy-newHeight)/2;
		//m_rcPicture.right = m_rcPicture.left + newWidth;
		//m_rcPicture.bottom = m_rcPicture.top + newHeight;

		//m_rcPicture.OffsetRect(0, 30);
	}

	InvalidateRect( NULL );
	UpdateWindow();
}

void CDXTestView::OnFileOpen()
{
	CFileDialog OpenDlg( TRUE, NULL, NULL, OPEN_EXISTING, 
		_T("Svg Files(*.svg)|*.svg|")
		_T("Common files(*.jpg;*.png;*.ppm;*.bmp)|*.jpg;*.png;*.ppm;*.bmp|") 
		_T("p0 b0 Files(*.p0?;b0?)|*.p0?;*.b0?|")
		_T("Other files(*.dds;*.dib;*.hdr;*.pfm)|*.dds;*.dib;*.hdr;*.pfm|") 
		_T("Jpg Files(*.jpg)|*.jpg|")
		_T("Png Files(*.png)|*.png|")
		_T("PPM Files(*.ppm)|*.ppm|")
		_T("bmp Files(*.bmp)|*.bmp|")
		_T("Tga Files(*.tga)|*.tga|")
		_T("Pic Files(*.pic)|*.pic|")
		_T("All Files(*.*)|*.*||"),
		this, 0 );

	if( IDOK != OpenDlg.DoModal() )
	{
		return ;
	}

	CString fileName = OpenDlg.GetPathName();

	AfxGetApp()->AddToRecentFileList(fileName);

	ShowPicture( fileName );
}

void CDXTestView::OnLButtonDown(UINT nFlags, CPoint point)
{
	SetCursor( ::LoadCursor( NULL, IDC_HAND));
	m_bMouseDown = TRUE;

	m_svg_render.change_select_status_all( false);
	m_select = m_svg_render.try_select_svg( point.x, point.y);
	Refresh();
	if( m_select != NULL )
	{
		if( m_select->get_image_id() < CHANGED_PART_MIN_ID)
		{
			// 裸模部件不让拖动
			m_bMouseDown = FALSE;
			return;
		}
		if( m_pEditPartView)
		{
			m_pEditPartView->ChangeTabByPartID( m_select->get_image_id());
		}
		m_select->show_bounding_box( true );
	}

	::SetCapture(GetSafeHwnd());
	m_ptLast = point;
	
	CView::OnLButtonDown(nFlags, point);
}

void CDXTestView::OnLButtonUp(UINT nFlags, CPoint point)
{
	if( m_bMouseDown )
	{
		::ReleaseCapture();
		m_bMouseDown = FALSE;
	}

	CView::OnLButtonUp(nFlags, point);
}

//------------------------------------------------------------------------
static unsigned get_key_flags(int wflags)
{
	unsigned flags = 0;
	//if(wflags & MK_LBUTTON) flags |= agg::mouse_left;
	//if(wflags & MK_RBUTTON) flags |= agg::mouse_right;
	//if(wflags & MK_SHIFT)   flags |= agg::kbd_shift;
	//if(wflags & MK_CONTROL) flags |= agg::kbd_ctrl;
	return flags;
}

void CDXTestView::OnMouseMove(UINT nFlags, CPoint point)
{	
	if( m_bMouseDown )
	{
		UINT input_flags = get_key_flags(nFlags);
		TranslateSelectPart( point.x-m_ptLast.x, point.y-m_ptLast.y);

		m_ptLast = point;

		SetCursor( ::LoadCursor( NULL, IDC_HAND));
	}
	else
	{
		CView::OnMouseMove(nFlags, point);
	}	
}

LRESULT CDXTestView::WindowProc(UINT message, WPARAM wParam, LPARAM lParam)
{
	//if( 0 == window_proc(&m_svg_render, GetSafeHwnd(), message, wParam, lParam) )
	//{
	//	return 0;
	//}

	return CView::WindowProc(message, wParam, lParam);
}

void CDXTestView::OnUpdateNeedSvg(CCmdUI *pCmdUI)
{
	pCmdUI->Enable(m_bLastIsSVG);
}

void CDXTestView::OnSvgZoomin()
{
	//DWORD dwPos = GetMessagePos();

	//POINT point;
	//point.x = LOWORD( dwPos );
	//point.y = HIWORD( dwPos );

	//ScreenToClient(&point);

	//if( point.x > 0 && point.y > 0)
	//{
	//	if( m_svg_render.zoom_in( point.x, point.y))
	//	{
	//		Refresh();
	//	}
	//}
	//else
	{
		if( m_svg_render.zoom_in())
		{
			Refresh();
		}
	}
}

void CDXTestView::OnSvgZoomout()
{
	//DWORD dwPos = GetMessagePos();

	//POINT point;
	//point.x = LOWORD( dwPos );
	//point.y = HIWORD( dwPos );

	//ScreenToClient(&point);

	//if( point.x > 0 && point.y > 0)
	//{
	//	if( m_svg_render.zoom_out( point.x, point.y))
	//	{
	//		Refresh();
	//	}
	//}
	//else
	{
		if( m_svg_render.zoom_out())
		{
			Refresh();
		}
	}
}


void CDXTestView::OnSvgRotateClockwised()
{
	if( m_select != NULL)
	{
		if( m_select->get_image_id() < CHANGED_PART_MIN_ID)
		{
			// 裸模部件不让旋转
			return;
		}
		m_select->rotate( 5 );
		Refresh();
	}
	
	//DWORD dwPos = GetMessagePos();

	//POINT point;
	//point.x = LOWORD( dwPos );
	//point.y = HIWORD( dwPos );

	//ScreenToClient(&point);

	//if( point.x > 0 && point.y > 0)
	//{
	//	if( m_svg_render.rotate( point.x, point.y, 10, false))
	//	{
	//		Refresh();
	//	}
	//}
	//else
	//{
	//	if( m_svg_render.rotate( 0, 0, 10, false))
	//	{
	//		Refresh();
	//	}
	//}
}

void CDXTestView::OnSvgRotateCounterClockwised()
{
	if( m_select != NULL)
	{
		if( m_select->get_image_id() < CHANGED_PART_MIN_ID)
		{
			// 裸模部件不让旋转
			return;
		}
		m_select->rotate( -5 );
		Refresh();
	}
	
	//DWORD dwPos = GetMessagePos();

	//POINT point;
	//point.x = LOWORD( dwPos );
	//point.y = HIWORD( dwPos );

	//ScreenToClient(&point);

	//if( point.x > 0 && point.y > 0)
	//{
	//	if( m_svg_render.rotate( point.x, point.y, -10, false))
	//	{
	//		Refresh();
	//	}
	//}
	//else
	//{
	//	if( m_svg_render.rotate( 0, 0, -10, false))
	//	{
	//		Refresh();
	//	}
	//}
}


void CDXTestView::OnSvgOriginalview()
{
	if( m_select != NULL)
	{
		m_select->reset();
	}
	else
	{
		m_svg_render.original_view();
	}
	Refresh();
}

void CDXTestView::OnContextMenu(CWnd* /*pWnd*/, CPoint point)
{
	if ( !m_bLastIsSVG )
	{
		return;
	}

	CMenu popupMenu;
	if( !popupMenu.LoadMenu( IDR_SVG_CONTEXT ) )
	{
		return;
	}

	CMenu *pSubMenu = popupMenu.GetSubMenu(0);
	if ( pSubMenu != NULL )
	{
		pSubMenu->TrackPopupMenu( 0, point.x, point.y, this );
	}

}

void CDXTestView::OnOptionBackground()
{
	// ARGB --> BGR
	//COLORREF r_ = (m_clrBackground & 0xFF0000)>>16;
	//COLORREF g_ = (m_clrBackground & 0xFF00)>>8;
	//COLORREF b_ = (m_clrBackground & 0xFF);
	//COLORREF clrRGB = RGB( r_, g_, b_ );

	//CColorDialog dlg( clrRGB, CC_FULLOPEN, this );
	//if( dlg.DoModal() == IDOK)
	//{
	//	clrRGB = dlg.GetColor();
	//	D3DCOLOR r = GetRValue(clrRGB);
	//	D3DCOLOR g = GetGValue(clrRGB);
	//	D3DCOLOR b = GetBValue(clrRGB);
	//	m_clrBackground = D3DCOLOR_ARGB( 255, r, g, b ); 
	//	Invalidate();
	//}

	CColorDialog dlg( m_clrBackground, CC_FULLOPEN, this );
	if( dlg.DoModal() == IDOK)
	{
		m_clrBackground = dlg.GetColor();
		Invalidate();
	}
}

void CDXTestView::OnOptionClipByClient()
{
	m_bClipToClient = !m_bClipToClient;
	Refresh();
}

void CDXTestView::OnUpdateOptionClipByClient(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_bClipToClient );
	pCmdUI->Enable( m_bLastIsSVG );
}

void CDXTestView::OnOptionImagebackground()
{
	// ARGB --> BGR
	COLORREF r_ = (m_clrImageBackground & 0xFF0000)>>16;
	COLORREF g_ = (m_clrImageBackground & 0xFF00)>>8;
	COLORREF b_ = (m_clrImageBackground & 0xFF);
	COLORREF clrRGB = RGB( r_, g_, b_ );

	CColorDialog dlg( clrRGB, CC_FULLOPEN, this );
	if( dlg.DoModal() == IDOK)
	{
		clrRGB = dlg.GetColor();
		D3DCOLOR r = GetRValue(clrRGB);
		D3DCOLOR g = GetGValue(clrRGB);
		D3DCOLOR b = GetBValue(clrRGB);
		m_clrImageBackground = D3DCOLOR_ARGB( 255, r, g, b ); 
		m_bFullTransparent = FALSE;
		Refresh();
	}
}

void CDXTestView::OnOptionFulltransparent()
{
	m_bFullTransparent = !m_bFullTransparent;
	Refresh();
}

void CDXTestView::OnUpdateOptionFulltransparent(CCmdUI *pCmdUI)
{
	pCmdUI->SetRadio( m_bFullTransparent );
	pCmdUI->Enable( m_bLastIsSVG );
}

void CDXTestView::OnUpdateOptionImagebackground(CCmdUI *pCmdUI)
{
	pCmdUI->SetRadio( !m_bFullTransparent );
	pCmdUI->Enable( m_bLastIsSVG );
}

bool CDXTestView::ShowPart( const TShowPartPtrContainer& tspc)
{
	std::wstring fullpath;
	CShowPartLoader::GetSvgFullPath( fullpath);
	
	for( TShowPartPtrContainer::const_iterator i = tspc.begin(), e = tspc.end(); i != e; ++i)
	{
		const ShowPartItem* part = *i;

		if( MH_SHOW_TROUSERS_INNER == part->ulLayer )	// “鞋里面裤子”和“鞋外面裤子”要互斥
		{
			m_svg_render.remove_svg( MH_SHOW_TROUSERS_OUTER);
		}
		else if( MH_SHOW_TROUSERS_OUTER == part->ulLayer)
		{
			m_svg_render.remove_svg( MH_SHOW_TROUSERS_INNER);
		}
		ClearConflictParts( part->conflict, false);

		svg::svg_image_ptr tempimage = m_svg_render.get_svg( part->ulLayer);
		if( tempimage == NULL || tempimage->get_image_id() != part->ulID)
		{
			CString temp( fullpath.c_str());
			temp += part->strSvg.c_str();

			tempimage = m_svg_render.add_svg( temp, part->ulLayer);
		}
		
		if( tempimage != NULL)
		{
			m_rcPicture.left = 0;
			m_rcPicture.top = 0;
			m_rcPicture.right = m_svg_render.get_image_view_width();
			m_rcPicture.bottom = m_svg_render.get_image_view_height();

			tempimage->show( true);
			tempimage->set_image_id( part->ulID);
			tempimage->set_trans_affine( part->transform);

			m_bLastIsSVG = TRUE;
		}
		else
		{
			// 装载失败
			//m_svg_render.remove_svg( part->ulLayer);
		}
	}
	Refresh();
	return true;
}

bool CDXTestView::HidePart( const TShowPartPtrContainer& tspc)
{
	for( TShowPartPtrContainer::const_iterator i = tspc.begin(), e = tspc.end(); i != e; ++i)
	{
		const ShowPartItem* part = *i;

		svg::svg_image_ptr tempimage = m_svg_render.get_svg( part->ulLayer);
		if( tempimage == m_select)
		{
			m_select = NULL;
		}
		m_svg_render.show_svg( part->ulLayer, false);
	}
	Refresh();
	return true;
}

bool CDXTestView::ChangeLayer( const TShowPartPtrContainer& tspc, unsigned long oldLayer)
{
	for( TShowPartPtrContainer::const_iterator i = tspc.begin(), e = tspc.end(); i != e; ++i)
	{
		const ShowPartItem* part = *i;

		if( oldLayer >= 0)
		{
			svg::svg_image_ptr tempimage = m_svg_render.get_svg( oldLayer);
			if( tempimage != NULL && tempimage->get_image_id() >= CHANGED_PART_MIN_ID)
			{
				if( tempimage == m_select)
				{
					m_select = NULL;
				}
				m_svg_render.remove_svg( oldLayer);	
			}
		}
	}
	ShowPart( tspc);
	return true;
}

bool CDXTestView::SelectPart( const TShowPartPtrContainer& tspc)
{
	if( m_select != NULL)
	{
		m_select->show_bounding_box( false );
	}
	for( TShowPartPtrContainer::const_iterator i = tspc.begin(), e = tspc.end(); i != e; ++i)
	{
		const ShowPartItem* part = *i;

		m_select = m_svg_render.get_svg( part->ulLayer);
		if( m_select != NULL)
		{
			m_select->show_bounding_box( true );
		}
	}
	Refresh();
	return true;
}

void CDXTestView::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	// TODO: Add your message handler code here and/or call default
	int x = 0, y = 0;
	switch( nChar)
	{
	case VK_UP:
		y -= 1;
		break;
	case VK_DOWN:
		y += 1;
		break;
	case VK_LEFT:
		x -= 1;
		break;
	case VK_RIGHT:
		x += 1;
		break;
	default:
		return;
	}
	TranslateSelectPart( x, y);

	CView::OnKeyDown(nChar, nRepCnt, nFlags);
}

// 移动选中部件
void CDXTestView::TranslateSelectPart( int x, int y)
{
	if( m_select != NULL)
	{
		m_svg_render.translate_svg( m_select, x, y);
	}
	else
	{
		m_svg_render.translate( x, y);
	}
	Refresh();
}

// 清除所有部件
void CDXTestView::ClearAllParts()
{
	m_svg_render.clear_svg();
}

bool CDXTestView::RemovePart( unsigned long ulID, unsigned long ulLayer)
{
	if( m_select != NULL && m_select->get_image_id() == ulID)
	{
		m_select->show_bounding_box( false);
		m_select = NULL;
	}
	m_svg_render.remove_svg( ulLayer);
	Refresh();
	return true;
}

// 得到当前所有显示部件
void CDXTestView::GetAllShowParts( SvgImageMap& showParts)
{
	showParts.clear();

	for( int i=0; i<MH_SHOW_LAYER_MAX; i++)
	{
		svg::svg_image_ptr image = m_svg_render.get_svg( i);
		if( image == NULL)
		{
			continue;
		}
		if( image->get_image_id() >= CHANGED_PART_MIN_ID && image->visible())
		{
			showParts[i] = image;
		}
	}
}

// 清除conflict部件
void CDXTestView::ClearConflictParts( __int64 conflict, bool bRefresh)
{
	for( int i=0; i<MH_SHOW_LAYER_MAX; i++)
	{
		if( conflict & (1i64 << i))
		{
			m_svg_render.remove_svg( i);
		}
	}
	if( bRefresh)
	{
		Refresh();
	}
}