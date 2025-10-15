///=============================================================================
/// Product   : DXTest
/// Project   : DXTest
/// Module    : SvgEditView.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	SVG 编辑器引擎测试程序
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-5-15 Created.
///=============================================================================

#include "stdafx.h"
#include "DXTest.h"
#include "SvgEditView.h"
#include "XMLHelper.h"
#include "CommonUtils.h"

//#include <d3d9types.h> // for D3DCOLOR_ARGB
#define D3DCOLOR_ARGB(a,r,g,b) \
	((D3DCOLOR)((((a)&0xff)<<24)|(((r)&0xff)<<16)|(((g)&0xff)<<8)|((b)&0xff)))

// CSvgEditView

IMPLEMENT_DYNCREATE(CSvgEditView, CScrollView)

CSvgEditView::CSvgEditView()
{
	m_pRenderer = NULL;
	m_clrBackground = D3DCOLOR_ARGB( 255, 238,242,251 );
	m_bClipToClient = FALSE;
	m_clrImageBackground = D3DCOLOR_ARGB( 255, 255, 255, 255);
	m_bFullTransparent = FALSE;

	m_hPicture = Nuclear::INVALID_PICTURE_HANDLE;
	m_bLastIsSVG = FALSE;
	m_imageBuffer = NULL;
	m_bufferSize = 0;

	m_dx = m_dy = 0;
	m_x = m_y = 0;
	m_drag_flag = false;
}

CSvgEditView::~CSvgEditView()
{
	m_pRenderer = NULL;
}

LONG CSvgEditView::Init( HWND hwnd)
{
	if ( m_pRenderer != NULL )
	{
		return 0;
	}

	LPCTSTR lpszWorkDirectory = UTIL::GetWorkDirectory();

	CString szFullFileName(lpszWorkDirectory);
	szFullFileName += (L"cfg/MH_Show.xml");

	_bstr_t bstrShowConfigFile(szFullFileName);
	MSXML2::IXMLDOMNodePtr spRootNode;
	HRESULT hr = XMLHELPER::LoadRootNodeFromXMLFile( bstrShowConfigFile, &spRootNode );
	if ( !SUCCEEDED(hr) )
	{
		ASSERT( !"Invalid Show config file!" );
		return -1;
	}

	MSXML2::IXMLDOMNodeListPtr spShowItems;
	hr = spRootNode->get_childNodes( &spShowItems );
	CHECK_HR_RETURN_VALUE( hr, -2 );

	long lCount = 0;
	hr = spShowItems->get_length(&lCount);
	CHECK_HR_RETURN_VALUE( hr, -3 );

	MSXML2::IXMLDOMNodePtr spShowNode;
	m_vShowParts.resize(lCount);

	// <Show ID="100" Svg="0/01/background.svg" Tga="0/01/background.tga" 
	//	Sex="0" Layer="0" Type="10" LinkNum="0"/>
	for( long lIndex = 0; lIndex < lCount; lIndex ++ )
	{
		hr = spShowItems->get_item(lIndex,&spShowNode);
		if ( !SUCCEEDED(hr) )
		{
			ASSERT( !"Invalid Show Item is in config file!" );
			CHECK_HR_RETURN_VALUE( hr, -4 );
		}

		XMLHELPER::CXMLNode2 xNode(spShowNode);
		ShowPart& rBack = m_vShowParts[lIndex];
		rBack.ulID      = xNode.GetAttributeInt32 ( L"ID", 0 );
		rBack.bSexMan   = xNode.GetAttributeUInt32( L"Sex", 0 );
		rBack.ulLayer   = xNode.GetAttributeUInt32( L"Layer", 0 );
		rBack.lType     = xNode.GetAttributeInt32 ( L"Type", -1 );
		rBack.ulLinkNum = xNode.GetAttributeUInt32( L"LinkNum", 0 );
		xNode.GetAttribute( L"Svg", rBack.strSvg );
		xNode.GetAttribute( L"Tga", rBack.strTga );
	}
	
	//CRect rcClient;
	//::GetClientRect( hwnd , &rcClient );
	// TODO:
	//SetScrollSizes(MM_TEXT, rcClient.Size());

	Nuclear::XDisplayMode dm(1280, 1024, 32);
	bool bRet = CreateRenderer(&m_pRenderer, hwnd,  dm, 0, Nuclear::XPRV_DEFAULT );

	return (LONG)bRet;
}

void CSvgEditView::Destroy()
{
	if ( m_pRenderer != NULL )
	{
		DestroyRenderer( m_pRenderer );
		m_pRenderer = NULL; 
	}
}

void CSvgEditView::OnDestroy()
{
	CScrollView::OnDestroy();
	Destroy();
}

BOOL CSvgEditView::OnEraseBkgnd(CDC* pDC)
{
	return TRUE;
}

BEGIN_MESSAGE_MAP(CSvgEditView, CScrollView)
	ON_WM_DESTROY()
	ON_WM_ERASEBKGND()
	ON_WM_SIZE()
	ON_WM_CONTEXTMENU()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_COMMAND( ID_FILE_OPEN, OnFileOpen )
	ON_COMMAND(ID_SVG_ZOOMIN, &CSvgEditView::OnSvgZoomin)
	ON_COMMAND(ID_SVG_ZOOMOUT, &CSvgEditView::OnSvgZoomout)
	ON_COMMAND(ID_SVG_ORIGINALVIEW, &CSvgEditView::OnSvgOriginalview)
	ON_COMMAND(ID_SVG_ROTATE_CW, &CSvgEditView::OnSvgRotateClockwised)
	ON_COMMAND(ID_SVG_ROTATE_CCW, &CSvgEditView::OnSvgRotateCounterClockwised)

	ON_UPDATE_COMMAND_UI(ID_SVG_ZOOMIN, &CSvgEditView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ZOOMOUT, &CSvgEditView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ORIGINALVIEW, &CSvgEditView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ROTATE_CW, &CSvgEditView::OnUpdateNeedSvg)
	ON_UPDATE_COMMAND_UI(ID_SVG_ROTATE_CCW, &CSvgEditView::OnUpdateNeedSvg)


	ON_COMMAND(ID_OPTION_BACKGROUND, &CSvgEditView::OnOptionBackground)
	ON_COMMAND(ID_OPTION_CLIPBYCLIENT, &CSvgEditView::OnOptionClipByClient)
	ON_COMMAND(ID_OPTION_IMAGEBACKGROUND, &CSvgEditView::OnOptionImagebackground)
	ON_COMMAND(ID_OPTION_FULLTRANSPARENT, &CSvgEditView::OnOptionFulltransparent)
	
	ON_UPDATE_COMMAND_UI(ID_OPTION_CLIPBYCLIENT, &CSvgEditView::OnUpdateOptionClipByClient)
	ON_UPDATE_COMMAND_UI(ID_OPTION_FULLTRANSPARENT, &CSvgEditView::OnUpdateOptionFulltransparent)
	ON_UPDATE_COMMAND_UI(ID_OPTION_IMAGEBACKGROUND, &CSvgEditView::OnUpdateOptionImagebackground)


END_MESSAGE_MAP()

void CSvgEditView::OnFileOpen()
{
	CFileDialog OpenDlg( TRUE, NULL, NULL, OPEN_EXISTING, 
		_T("Svg Files(*.svg)|*.svg|")
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

void CSvgEditView::OnSize(UINT nType, int cx, int cy)
{
	CScrollView::OnSize(nType, cx, cy);

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

// CSvgEditView drawing

void CSvgEditView::OnInitialUpdate()
{
	CScrollView::OnInitialUpdate();

	CSize sizeTotal;
	// TODO: calculate the total size of this view
	sizeTotal.cx = sizeTotal.cy = 100;
	SetScrollSizes(MM_TEXT, sizeTotal);

	Init( GetSafeHwnd() );

	CString filePath = GetDocument()->GetPathName();
	if ( !filePath.IsEmpty() )
	{
		ShowPicture( filePath );
	}
}

void CSvgEditView::OnDraw(CDC* pDC)
{
	CRect m_rectClient;
	GetClientRect(&m_rectClient);

	CTime timeCurrent = CTime::GetCurrentTime();
	CString m_Stamp = timeCurrent.Format( _T("%Y-%m-%d %H:%M:%S") );
	if ( m_pRenderer != NULL )
	{
		bool bRet = m_pRenderer->Begin();
		m_pRenderer->Clear( m_clrBackground );

		if (m_Stamp.GetLength()>0)
		{
			m_rectClient.MoveToXY( 20, 20 );
			bRet = m_pRenderer->DrawText(m_Stamp,m_Stamp.GetLength(),&m_rectClient,
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

void CSvgEditView::ShowPicture( LPCTSTR lpszFilePath )
{
	if ( lpszFilePath == NULL )
	{
		return ;
	}

	if ( m_pRenderer == NULL )
	{
		return ;
	}

	if ( m_hPicture != Nuclear::INVALID_PICTURE_HANDLE)
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
		m_svg_render.set_device_dimension(0,0);

		// 先清除原有的svg(测试只显示一个svg的情况)
		m_svg_render.clear_svg();
		if( !m_svg_render.add_svg( m_strFilePath, 0 ) )
		{
			return;
		}

		m_bLastIsSVG = TRUE;
	}
	else
	{
		// 不支持
		return ;
	}

	Refresh();	
}

void CSvgEditView::Refresh()
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
		if ( m_hPicture != -1)
		{
			m_pRenderer->FreePicture(m_hPicture);
			m_hPicture = -1;
		}


		unsigned iDeviceWidth = rcClient.Width();
		unsigned iDeviceHeight = rcClient.Height()+40;
		if( !m_bClipToClient )
		{
			unsigned iImageWidth = m_svg_render.get_image_view_width();
			unsigned iImageHeight = m_svg_render.get_image_view_height();

			if( iDeviceWidth > iImageWidth )
			{
				iDeviceWidth = iImageWidth;
			}

			if( iDeviceHeight > iImageHeight )
			{
				iDeviceHeight = iImageHeight;
			}
		}

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

		m_svg_render.set_device_dimension(iDeviceWidth, iDeviceHeight);

		// Redraw
		m_svg_render.on_draw();
		const agg::rendering_buffer& buffer = m_svg_render.rbuf_window();		

		m_rcPicture.left = 0;
		m_rcPicture.top = 0;
		m_rcPicture.right = buffer.width();
		m_rcPicture.bottom = buffer.height();

		// 目前只支持32 argb
		int hPicture = m_pRenderer->NewPicture(
			buffer.width(), buffer.height() );
		if ( hPicture != -1 )
		{
			int size = buffer.height() * buffer.stride_abs();

			m_pRenderer->SetPictureData(hPicture, buffer.buf(), size);

			m_hPicture = hPicture;
		}

		// the bounding box of the picture is the client box
	}
	
	InvalidateRect( NULL );
	UpdateWindow();
}

// CSvgEditView message handlers

void CSvgEditView::OnUpdateNeedSvg(CCmdUI *pCmdUI)
{
	pCmdUI->Enable(m_bLastIsSVG);
}

void CSvgEditView::OnContextMenu(CWnd* /*pWnd*/, CPoint point)
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

void CSvgEditView::OnSvgZoomin()
{
	//DWORD dwPos = GetMessagePos();

	//POINT point;
	//point.x = LOWORD( dwPos );
	//point.y = HIWORD( dwPos );

	//ScreenToClient(&point);

	//if( point.x > 0 && point.y > 0 )
	//{
	//	if( m_svg_render.zoom_in( point.x, point.y ) )
	//	{
	//		Refresh();
	//	}
	//}
	//else 
	if( m_svg_render.zoom_in() )
	{
		Refresh();
	}
}

void CSvgEditView::OnSvgZoomout()
{
	//DWORD dwPos = GetMessagePos();

	//POINT point;
	//point.x = LOWORD( dwPos );
	//point.y = HIWORD( dwPos );

	//ScreenToClient(&point);

	//if( point.x > 0 && point.y > 0 )
	//{
	//	if( m_svg_render.zoom_out( point.x, point.y ) )
	//	{
	//		Refresh();
	//	}
	//}
	//else 
	if( m_svg_render.zoom_out() )
	{
		Refresh();
	}
}

void CSvgEditView::OnSvgRotateClockwised()
{
	if( m_svg_render.rotate_svg(0, 5, false ) )
	{
		Refresh();
	}
}

void CSvgEditView::OnSvgRotateCounterClockwised()
{
	if( m_svg_render.rotate_svg(0, -5, false ) )
	{
		Refresh();
	}
}

void CSvgEditView::OnSvgOriginalview()
{
	m_svg_render.original_view();
	if( m_svg_render.original_view_svg(0) )
	{
		Refresh();
	}
}

void CSvgEditView::OnLButtonDown(UINT nFlags, CPoint point)
{
	if( m_bLastIsSVG )
	{		
		::SetCapture(GetSafeHwnd());

		m_x = point.x;
		m_y = point.y;
		m_dx = 0;
		m_dy = 0;
		m_drag_flag = true;

		if( m_svg_render.select_svg(0, true) )
		{
			Refresh();
		}
	}

	CView::OnLButtonDown(nFlags, point);
}

void CSvgEditView::OnLButtonUp(UINT nFlags, CPoint point)
{
	if( m_bLastIsSVG )
	{
		::ReleaseCapture();
		m_drag_flag = false;
		m_dx = m_x = 0;
		m_dy = m_y = 0;
	}

	CView::OnLButtonUp(nFlags, point);
}

//------------------------------------------------------------------------
//static unsigned get_key_flags(int wflags)
//{
//	unsigned flags = 0;
//	if(wflags & MK_LBUTTON) flags |= agg::mouse_left;
//	if(wflags & MK_RBUTTON) flags |= agg::mouse_right;
//	if(wflags & MK_SHIFT)   flags |= agg::kbd_shift;
//	if(wflags & MK_CONTROL) flags |= agg::kbd_ctrl;
//	return flags;
//}

void CSvgEditView::OnMouseMove(UINT nFlags, CPoint point)
{	
	if( m_bLastIsSVG )
	{
		if( m_drag_flag )
		{
			m_dx = point.x - m_x;
			m_dy = point.y - m_y;
			m_x = point.x; 
			m_y = point.y; 

			if( m_svg_render.translate_svg(0, m_dx, m_dy, false ) )
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

void CSvgEditView::OnOptionBackground()
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

void CSvgEditView::OnOptionClipByClient()
{
	m_bClipToClient = !m_bClipToClient;
	Refresh();
}

void CSvgEditView::OnUpdateOptionClipByClient(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_bClipToClient );
	pCmdUI->Enable( m_bLastIsSVG );
}

void CSvgEditView::OnOptionImagebackground()
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

void CSvgEditView::OnOptionFulltransparent()
{
	m_bFullTransparent = !m_bFullTransparent;
	Refresh();
}

void CSvgEditView::OnUpdateOptionFulltransparent(CCmdUI *pCmdUI)
{
	pCmdUI->SetRadio( m_bFullTransparent );
	pCmdUI->Enable( m_bLastIsSVG );
}

void CSvgEditView::OnUpdateOptionImagebackground(CCmdUI *pCmdUI)
{
	pCmdUI->SetRadio( !m_bFullTransparent );
	pCmdUI->Enable( m_bLastIsSVG );
}

