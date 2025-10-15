// ShowView.cpp : implementation file
//

#include "stdafx.h"
#include "DXTest.h"
#include "DXTestDoc.h"
#include "ShowView.h"


// CShowView

IMPLEMENT_DYNCREATE(CShowView, CView)

#define D3DCOLOR_ARGB(a,r,g,b) \
	((D3DCOLOR)((((a)&0xff)<<24)|(((r)&0xff)<<16)|(((g)&0xff)<<8)|((b)&0xff)))

CShowView::CShowView()
{
	m_pRenderer = NULL;
	m_hPicture = Nuclear::INVALID_PICTURE_HANDLE;
	m_bLastIsSVG = FALSE;
	m_svg_render = NULL;
	m_dx = 0;
	m_dy = 0;
	m_x = 0;
	m_y = 0;
	m_drag_flag = FALSE;
	m_clrBackground = D3DCOLOR_ARGB( 255, 238,242,251 );
	m_bClipToClient = FALSE;
	m_clrImageBackground = D3DCOLOR_ARGB( 255, 255, 255, 255);
	m_bFullTransparent = FALSE;

	m_imageBuffer = NULL;
	m_bufferSize = 0;
}

CShowView::~CShowView()
{
	m_pRenderer = NULL;
	m_hPicture = Nuclear::INVALID_PICTURE_HANDLE;

	if (m_imageBuffer != NULL )
	{
		free( m_imageBuffer );
		m_imageBuffer  = NULL;
	}
	m_bufferSize = 0;

	if ( m_svg_render != NULL )
	{
		m_svg_render->Destroy();
		m_svg_render = NULL;
	}
}

BITMAPINFO* texReadPIC( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels);
BITMAPINFO* texReadP0X( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels);
BITMAPINFO* texReadB0X( LPCTSTR fileName, HBITMAP* pBitmap, char** ppPixels);
BITMAPINFO* texReadTGA( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels);

BEGIN_MESSAGE_MAP(CShowView, CView)
	ON_WM_DESTROY()
	ON_WM_SIZE()
	ON_WM_ERASEBKGND()
	ON_COMMAND( ID_FILE_OPEN, OnFileOpen )
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_WM_CONTEXTMENU()
	ON_COMMAND(ID_SVG_ZOOMIN, &CShowView::OnSvgZoomin)
	ON_COMMAND(ID_SVG_ZOOMOUT, &CShowView::OnSvgZoomout)
	ON_COMMAND(ID_SVG_ORIGINALVIEW, &CShowView::OnSvgOriginalview)
	ON_COMMAND(ID_SVG_ROTATE_CW, &CShowView::OnSvgRotateClockwised)
	ON_COMMAND(ID_SVG_ROTATE_CCW, &CShowView::OnSvgRotateCounterClockwised)

	ON_UPDATE_COMMAND_UI(ID_SVG_ZOOMIN, &CShowView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ZOOMOUT, &CShowView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ORIGINALVIEW, &CShowView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ROTATE_CW, &CShowView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ROTATE_CCW, &CShowView::OnUpdateNeedSvg)
	ON_COMMAND(ID_OPTION_BACKGROUND, &CShowView::OnOptionBackground)
	ON_COMMAND(ID_OPTION_CLIPBYCLIENT, &CShowView::OnOptionClipByClient)
	ON_UPDATE_COMMAND_UI(ID_OPTION_CLIPBYCLIENT, &CShowView::OnUpdateOptionClipByClient)
	ON_COMMAND(ID_OPTION_IMAGEBACKGROUND, &CShowView::OnOptionImagebackground)
	ON_COMMAND(ID_OPTION_FULLTRANSPARENT, &CShowView::OnOptionFulltransparent)
	ON_UPDATE_COMMAND_UI(ID_OPTION_FULLTRANSPARENT, &CShowView::OnUpdateOptionFulltransparent)
	ON_UPDATE_COMMAND_UI(ID_OPTION_IMAGEBACKGROUND, &CShowView::OnUpdateOptionImagebackground)
END_MESSAGE_MAP()


LONG CShowView::Init( HWND hwnd)
{
	if ( m_pRenderer != NULL )
	{
		return 0;
	}

	RECT rcClient;
	::GetClientRect( hwnd , &rcClient );
//rcClient.right - rcClient.left, rcClient.bottom - rcClient.top
	Nuclear::XDisplayMode dm(1280, 1024, 32);
	bool bRet = CreateRenderer(&m_pRenderer, hwnd,  dm, 0, Nuclear::XPRV_DEFAULT );
	
	return (LONG)bRet;
}

void CShowView::Destroy()
{
	if ( m_pRenderer != NULL )
	{
		DestroyRenderer( m_pRenderer );
		m_pRenderer = NULL; 
	}
}
// CShowView drawing

void CShowView::OnDraw(CDC* pDC)
{
	CRect m_rectClient;
	GetClientRect(&m_rectClient);

	CTime timeCurrent = CTime::GetCurrentTime();
	CString m_Stemp = timeCurrent.Format( _T("%Y-%m-%d %H:%M:%S") );
	if ( m_pRenderer != NULL )
	{
		bool bRet = m_pRenderer->Begin();
		m_pRenderer->Clear( m_clrBackground );
		//m_pRenderer->Clear( D3DCOLOR_ARGB(255,238,242,251) );
		//m_pRenderer->Clear( D3DCOLOR_ARGB(255,7,46,44) );
		//m_pRenderer->Clear( D3DCOLOR_ARGB(255,238,242,251) );
		//m_pRenderer->Clear( D3DCOLOR_ARGB(255,0,0,255) );

		if (m_Stemp.GetLength()>0)
		{
			m_rectClient.MoveToXY( 20, 20 );
			bRet = m_pRenderer->DrawText(m_Stemp,m_Stemp.GetLength(),&m_rectClient,
				0xFF000000);
		}

		if( m_hPicture != Nuclear::INVALID_PICTURE_HANDLE)
		{
			Nuclear::FRECT fRect;
			fRect.Assign((float)m_rcPicture.left, (float)m_rcPicture.top,
				(float)m_rcPicture.right, (float)m_rcPicture.bottom);
			m_pRenderer->DrawPicture(m_hPicture, fRect, (DWORD)0xFFFFFFFF );
		}

		bRet = m_pRenderer->End();
	}
}

// CShowView message handlers

void CShowView::OnDestroy()
{
	CView::OnDestroy();

	Destroy();
}

void CShowView::OnInitialUpdate()
{
	CView::OnInitialUpdate();

	Init( GetSafeHwnd() );

	CString filePath = GetDocument()->GetPathName();
	if ( !filePath.IsEmpty() )
	{
		ShowPicture( filePath );
	}

	CRect rcClient;
	GetClientRect(&rcClient);

}

void CShowView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);

	if ( m_pRenderer != NULL )
	{
		CRect rcClient;
		GetClientRect(&rcClient);

		Nuclear::CRECT rect;
		rect.Assign( rcClient.left, rcClient.top, rcClient.right, rcClient.bottom );
		m_pRenderer->SetSourceRect( &rect );
		m_pRenderer->SetDestRect( &rect );
		
		Refresh();
	}
}

BOOL CShowView::OnEraseBkgnd(CDC* pDC)
{
	return TRUE;
}

void CShowView::ShowPicture( LPCTSTR lpszFilePath )
{
	if ( lpszFilePath == NULL )
	{
		return ;
	}

	if ( m_pRenderer == NULL )
	{
		return ;
	}

	if ( m_hPicture != Nuclear::INVALID_PICTURE_HANDLE )
	{
		m_pRenderer->FreePicture(m_hPicture);
		m_hPicture = Nuclear::INVALID_PICTURE_HANDLE;
	}

	m_bLastIsSVG = FALSE;
	m_strFilePath = lpszFilePath;
	CString strExt = m_strFilePath.Right(4);
	CString strSimpleExt = strExt.Mid(0,3);
	if ( strExt.CompareNoCase(_T(".svg") ) == 0 )
	{
		CRect rcClient;
		GetClientRect(&rcClient);
		if ( m_svg_render == NULL )
		{
			m_svg_render = CreateShowRenderer( ST_DEFAULT );
			if( m_svg_render == NULL )
			{
				return;
			}
		}

		// reset the position;
		m_x = 0;
		m_y = 0;

		m_svg_render->SetDeviceSize(rcClient.Width(),rcClient.Height());
		m_svg_render->ParseFile( m_strFilePath ); 

		// 设置背景色
		//m_svg_render->SetBgColor( 0x00FFFFFF );
		//m_svg_render->SetBgColor( 0x1FFFFFFF );
		if ( m_bFullTransparent  )
		{
			m_svg_render->SetBgColor( 0 );
		}
		else
		{
			m_svg_render->SetBgColor( m_clrImageBackground );
		}
		//m_svg_render->SetBgColor( 0xFFFFFFFF );

		m_bLastIsSVG = TRUE;
	}
	else if ( strExt.CompareNoCase(_T(".pic") ) == 0 ||
		strSimpleExt.CompareNoCase(_T(".p0") ) == 0 || 
		strSimpleExt.CompareNoCase(_T(".b0") ) == 0 )
	{
		char*			qt_pPixels = NULL;
		BITMAPINFO*		qt_pBitmapInfo = NULL;
		HBITMAP			qt_hBITMAP = NULL;

		if ( strExt.CompareNoCase(_T(".pic") ) == 0 )
		{
			qt_pBitmapInfo =  texReadPIC (m_strFilePath, &qt_hBITMAP, &qt_pPixels);
		}
		else if ( strSimpleExt.CompareNoCase(_T(".p0") ) == 0 )
		{
			qt_pBitmapInfo =  texReadP0X (m_strFilePath, &qt_hBITMAP, &qt_pPixels);
		}
		else // .b0x
		{
			qt_pBitmapInfo =  texReadB0X (m_strFilePath, &qt_hBITMAP, &qt_pPixels);
		}

		if (!qt_pBitmapInfo)
		{
			::MessageBox( GetSafeHwnd(), _T("Could not read file") , m_strFilePath, MB_OK | MB_ICONERROR );
			return;
		}

		m_BitmapInfo = *qt_pBitmapInfo;

		BYTE *pBuffer = (BYTE *)qt_pPixels;
		int BufSize = m_BitmapInfo.bmiHeader.biSizeImage;

		// 注:这里的qt_pPixels原本就是DIB图像数据的Buffer指针，然而，实际上，
		// 我们也可以使用GetDIBits函数从hBITMAP中获取DIB图像数据
		// 唯一的区别就是，GetDIBits函数需要指定一个输出的缓冲区，它负责填充这个缓冲区。
		// 因此，这两种模式在不同的场合下，效率上会有些不同。

#if 0
		// 由HBITMAP获取DIB图像数据

		HDC hScreenDC = ::GetDC(NULL);
		BYTE *pBuffer = new BYTE[ BufSize ];
		if ( pBuffer != NULL )
		{
			int iRet = GetDIBits( hScreenDC, qt_hBITMAP, 0, m_BitmapInfo.bmiHeader.biHeight, 
				pBuffer, &m_BitmapInfo, DIB_RGB_COLORS );
			if ( iRet == m_BitmapInfo.bmiHeader.biHeight )
			{
				// 目前只支持32 argb
				int hPicture = m_pRenderer->NewPicture32(
					m_BitmapInfo.bmiHeader.biWidth , m_BitmapInfo.bmiHeader.biHeight );
				if ( hPicture != -1 )
				{	
					m_pRenderer->SetPictureData32(hPicture, pBuffer /*qt_pPixels*/, BufSize);
					m_hPicture = hPicture;
				}
			}

			delete[] pBuffer;
		}

		if (hScreenDC)
		{
			::ReleaseDC(NULL, hScreenDC);
		}
#else
		// 直接使用texReadPIC返回的内存指针

		// 目前只支持32 argb
		int hPicture = m_pRenderer->NewPicture(
			m_BitmapInfo.bmiHeader.biWidth , m_BitmapInfo.bmiHeader.biHeight );
		if ( hPicture != Nuclear::INVALID_PICTURE_HANDLE )
		{	
			m_pRenderer->SetPictureData(hPicture, pBuffer, BufSize);
			m_hPicture = hPicture;
		}
#endif

		if (qt_hBITMAP) DeleteObject (qt_hBITMAP);
		if (qt_pBitmapInfo) HeapFree (GetProcessHeap(), 0 , qt_pBitmapInfo);

	}
	else if ( strExt.CompareNoCase(_T(".jpg") ) == 0 ||
		strExt.CompareNoCase(_T(".png") ) == 0 ||
		strExt.CompareNoCase(_T(".bmp") ) == 0 ||
		strExt.CompareNoCase(_T(".tga") ) == 0 ||
		strExt.CompareNoCase(_T(".dds") ) == 0 ||
		strExt.CompareNoCase(_T(".dib") ) == 0 ||
		strExt.CompareNoCase(_T(".hdr") ) == 0 ||
		strExt.CompareNoCase(_T(".pfm") ) == 0 ||
		strExt.CompareNoCase(_T(".ppm") ) == 0)
	{
		if ( m_pRenderer != NULL )
			m_hPicture = m_pRenderer->LoadPicture( m_strFilePath );
		if ( m_hPicture != Nuclear::INVALID_PICTURE_HANDLE )
		{
			Nuclear::PictureInfo picInfo;
			m_pRenderer->GetPictureInfo( m_hPicture, picInfo );

			m_BitmapInfo.bmiHeader.biWidth = picInfo.m_nPicWidth ;
			m_BitmapInfo.bmiHeader.biHeight = picInfo.m_nPicHeight;
		}
	}

	Refresh();	
}

void CShowView::Refresh()
{
	CRect rcClient;
	GetClientRect( &rcClient );
	m_rcPicture = rcClient;
	rcClient.DeflateRect(0,0,0,40);
	rcClient.NormalizeRect();	

	if ( rcClient.IsRectEmpty() || rcClient.IsRectNull()  )
	{
		return;
	}

	if ( m_bLastIsSVG )
	{
		if ( m_hPicture != Nuclear::INVALID_PICTURE_HANDLE)
		{
			m_pRenderer->FreePicture(m_hPicture);
			m_hPicture = Nuclear::INVALID_PICTURE_HANDLE;
		}

		// Redraw

		// 测试 parse之后重新设置device dimension
		int iDeviceWidth = rcClient.Width();
		int iDeviceHeight = rcClient.Height()+40;
		if( !m_bClipToClient )
		{
			int iShowWidth = (int)m_svg_render->GetShowWidth();
			int iShowHeight = (int)m_svg_render->GetShowHeight();

			if( iShowWidth < iDeviceWidth )
			{
				iDeviceWidth = iShowWidth;
			}

			if( iShowHeight < iDeviceHeight )
			{
				iDeviceHeight = iShowHeight;
			}
		}

		if ( m_bFullTransparent  )
		{
			m_svg_render->SetBgColor( 0 );
		}
		else
		{
			m_svg_render->SetBgColor( m_clrImageBackground );
		}

		m_svg_render->SetDeviceSize(iDeviceWidth, iDeviceHeight);

		// 方案1，使用renderer engine自己的buffer渲染
		//m_svg_render->Draw();
		//const void* buffer = m_svg_render->GetBitmapData();
		
		// 方案2，使用外部提供的buffer渲染
		unsigned int needSize = iDeviceWidth * iDeviceHeight * 4;
		if( m_bufferSize < needSize ) // 计算所需的内存大小
		{
			m_bufferSize = needSize;
			if ( m_imageBuffer == NULL )
			{
				m_imageBuffer = malloc(m_bufferSize );
			}
			else
			{
				m_imageBuffer = realloc(m_imageBuffer, m_bufferSize );
			}
		}
		const void* buffer = m_imageBuffer;
		m_svg_render->DrawOnBuffer(m_imageBuffer, iDeviceWidth, iDeviceHeight, true, false );
		
		m_rcPicture.left = 0;
		m_rcPicture.top = 0;
		m_rcPicture.right = m_svg_render->GetDeviceWidth();
		m_rcPicture.bottom = m_svg_render->GetDeviceHeight();

		// 目前只支持32 argb
		HPICTURE hPicture = m_pRenderer->NewPicture(
			m_rcPicture.right, m_rcPicture.bottom );
		if ( hPicture != -1 )
		{
			size_t size = m_svg_render->GetBitmapSize();

			m_pRenderer->SetPictureData(hPicture, buffer, (int)size);

			m_hPicture = hPicture;
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
		m_rcPicture.left = (szClient.cx-newWidth)/2;
		m_rcPicture.top = (szClient.cy-newHeight)/2;
		m_rcPicture.right = m_rcPicture.left + newWidth;
		m_rcPicture.bottom = m_rcPicture.top + newHeight;

		m_rcPicture.OffsetRect(0, 30);
	}

	InvalidateRect( NULL );
	UpdateWindow();
}

void CShowView::OnFileOpen()
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


void CShowView::OnLButtonDown(UINT nFlags, CPoint point)
{
	if( m_bLastIsSVG )
	{		
		::SetCapture(GetSafeHwnd());
		m_dx = point.x - m_x;
		m_dy = point.y - m_y;
		m_drag_flag = true;
	}

	CView::OnLButtonDown(nFlags, point);
}

void CShowView::OnLButtonUp(UINT nFlags, CPoint point)
{
	if( m_bLastIsSVG )
	{
		::ReleaseCapture();
		//m_svg_render.on_mouse_button_up(point.x,point.y,nFlags);
		m_drag_flag = false;
	}

	CView::OnLButtonUp(nFlags, point);
}

void CShowView::OnMouseMove(UINT nFlags, CPoint point)
{	
	if( m_bLastIsSVG )
	{
		if( m_drag_flag )
		{
			m_x = point.x - m_dx;
			m_y = point.y - m_dy;
			
			if( m_svg_render->Translate( m_x, m_y, true ) )
			{
				Refresh();
			}
		}
	}
	else
	{
		CView::OnMouseMove(nFlags, point);
	}	
}

void CShowView::OnUpdateNeedSvg(CCmdUI *pCmdUI)
{
	pCmdUI->Enable(m_bLastIsSVG);
}


void CShowView::OnSvgZoomin()
{
	if( m_svg_render->Scale( 2.0, false ) )
	{
		Refresh();
	}	
}

void CShowView::OnSvgZoomout()
{
	if( m_svg_render->Scale( 0.5, false ) )
	{
		Refresh();
	}
}

void CShowView::OnSvgRotateClockwised()
{
	if( m_svg_render->Rotate( 5 * 3.14159 / 180.0, false ) )
	{
		Refresh();
	}
}

void CShowView::OnSvgRotateCounterClockwised()
{
	if(  m_svg_render->Rotate( -5 * 3.14159 / 180.0, false ))
	{
		Refresh();
	}
}


void CShowView::OnSvgOriginalview()
{
	if( m_svg_render->OriginalView() )
	{
		m_x = 0;
		m_y = 0;

		Refresh();
	}
}

void CShowView::OnContextMenu(CWnd* /*pWnd*/, CPoint point)
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
void CShowView::OnOptionBackground()
{
	// ARGB --> BGR
	COLORREF r_ = (m_clrBackground & 0xFF0000)>>16;
	COLORREF g_ = (m_clrBackground & 0xFF00)>>8;
	COLORREF b_ = (m_clrBackground & 0xFF);
	COLORREF clrRGB = RGB( r_, g_, b_ );

	CColorDialog dlg( clrRGB, CC_FULLOPEN, this );
	if( dlg.DoModal() == IDOK)
	{
		clrRGB = dlg.GetColor();
		D3DCOLOR r = GetRValue(clrRGB);
		D3DCOLOR g = GetGValue(clrRGB);
		D3DCOLOR b = GetBValue(clrRGB);
		m_clrBackground = D3DCOLOR_ARGB( 255, r, g, b ); 
		Invalidate();
	}
}

void CShowView::OnOptionClipByClient()
{
	m_bClipToClient = !m_bClipToClient;
	Refresh();
}

void CShowView::OnUpdateOptionClipByClient(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_bClipToClient );
	pCmdUI->Enable( m_bLastIsSVG );
}

void CShowView::OnOptionImagebackground()
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

void CShowView::OnOptionFulltransparent()
{
	m_bFullTransparent = !m_bFullTransparent;
	Refresh();
}

void CShowView::OnUpdateOptionFulltransparent(CCmdUI *pCmdUI)
{
	pCmdUI->SetRadio( m_bFullTransparent );
	pCmdUI->Enable( m_bLastIsSVG );
}

void CShowView::OnUpdateOptionImagebackground(CCmdUI *pCmdUI)
{
	pCmdUI->SetRadio( !m_bFullTransparent );
	pCmdUI->Enable( m_bLastIsSVG );
}

