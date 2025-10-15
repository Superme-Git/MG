// VoroTest.cpp : Defines the entry point for the application.
//

#include "stdafx.h"
#include "VoroTest.h"
#include "../include/voronoi.h"
#include <WindowsX.h>

#define MAX_LOADSTRING 100

#define WIDTH			800
#define HEIGHT			600
#define BORDER_SPACE	30 // 边白
#define CONVAS_WIDTH  (WIDTH+BORDER_SPACE+BORDER_SPACE)
#define CONVAS_HEIGHT (HEIGHT+BORDER_SPACE+BORDER_SPACE)

voronoi::Voronoi g_voronoi(100, WIDTH,HEIGHT);
POINT g_lastPos;
int g_mouseDownIndex = -1;

// 测试边计算边绘制
// #define CALC_AND_PAINT

// Global Variables:
HINSTANCE hInst;								// current instance
TCHAR szTitle[MAX_LOADSTRING];					// The title bar text
TCHAR szWindowClass[MAX_LOADSTRING];			// the main window class name

// Forward declarations of functions included in this code module:
ATOM				MyRegisterClass(HINSTANCE hInstance);
BOOL				InitInstance(HINSTANCE, int);
LRESULT CALLBACK	WndProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK	About(HWND, UINT, WPARAM, LPARAM);

int APIENTRY _tWinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPTSTR    lpCmdLine,
                     int       nCmdShow)
{
	UNREFERENCED_PARAMETER(hPrevInstance);
	UNREFERENCED_PARAMETER(lpCmdLine);

 	// TODO: Place code here.
	MSG msg;
	HACCEL hAccelTable;

	// Initialize global strings
	LoadString(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
	LoadString(hInstance, IDC_VOROTEST, szWindowClass, MAX_LOADSTRING);
	MyRegisterClass(hInstance);

	// Perform application initialization:
	if (!InitInstance (hInstance, nCmdShow))
	{
		return FALSE;
	}

	hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_VOROTEST));

	// Main message loop:
	while (GetMessage(&msg, NULL, 0, 0))
	{
		if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
		{
			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
	}

	return (int) msg.wParam;
}



//
//  FUNCTION: MyRegisterClass()
//
//  PURPOSE: Registers the window class.
//
//  COMMENTS:
//
//    This function and its usage are only necessary if you want this code
//    to be compatible with Win32 systems prior to the 'RegisterClassEx'
//    function that was added to Windows 95. It is important to call this function
//    so that the application will get 'well formed' small icons associated
//    with it.
//
ATOM MyRegisterClass(HINSTANCE hInstance)
{
	WNDCLASSEX wcex;

	wcex.cbSize = sizeof(WNDCLASSEX);

	wcex.style			= CS_HREDRAW | CS_VREDRAW | CS_DBLCLKS;
	wcex.lpfnWndProc	= WndProc;
	wcex.cbClsExtra		= 0;
	wcex.cbWndExtra		= 0;
	wcex.hInstance		= hInstance;
	wcex.hIcon			= LoadIcon(hInstance, MAKEINTRESOURCE(IDI_VOROTEST));
	wcex.hCursor		= LoadCursor(NULL, IDC_ARROW);
	wcex.hbrBackground	= (HBRUSH)(COLOR_WINDOW+1);
	wcex.lpszMenuName	= MAKEINTRESOURCE(IDC_VOROTEST);
	wcex.lpszClassName	= szWindowClass;
	wcex.hIconSm		= LoadIcon(wcex.hInstance, MAKEINTRESOURCE(IDI_SMALL));

	return RegisterClassEx(&wcex);
}

//
//   FUNCTION: InitInstance(HINSTANCE, int)
//
//   PURPOSE: Saves instance handle and creates main window
//
//   COMMENTS:
//
//        In this function, we save the instance handle in a global variable and
//        create and display the main program window.
//
BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
   HWND hWnd;

   hInst = hInstance; // Store instance handle in our global variable

   hWnd = CreateWindow(szWindowClass, szTitle, WS_OVERLAPPEDWINDOW,
      CW_USEDEFAULT, 0, CW_USEDEFAULT, 0, NULL, NULL, hInstance, NULL);

   if (!hWnd)
   {
      return FALSE;
   }

   ShowWindow(hWnd, nCmdShow);
   UpdateWindow(hWnd);

   return TRUE;
}

class CDCGraphics : public Graphics
{
	int m_iSavedDC;
	HDC m_hWndDC;
	HPEN m_hPen;
	HPEN m_hPenDash;
	HBRUSH m_hBrush;
	HDC m_hDC;
	RECT m_rcClient;
	HBITMAP m_hBitmap;
	int m_xOffset, m_yOffset; // 对所有的绝对坐标进行偏移
	BOOL m_bDoubleBuffered; // 是否启用双缓冲

public:
	CDCGraphics( HDC hDC, RECT rcClient, int xOffset, int yOffset, BOOL bDoubleBuffered )
	{
		m_xOffset = xOffset;
		m_yOffset = yOffset;
		m_bDoubleBuffered = bDoubleBuffered;
		m_hPen = NULL;
		m_hPenDash = NULL;
		m_hBrush = NULL;
		m_rcClient = rcClient;
		m_rcClient.right += m_xOffset*2;
		m_rcClient.bottom+= m_yOffset*2;

		if( m_bDoubleBuffered )
		{
			m_hWndDC = hDC;
			m_hDC = ::CreateCompatibleDC( hDC );
			HBITMAP hBitmap = ::CreateCompatibleBitmap( hDC, m_rcClient.right-m_rcClient.left, m_rcClient.bottom-m_rcClient.top);
			::SelectObject( m_hDC, hBitmap );
			m_hBitmap = hBitmap;
		}
		else
		{
			m_hDC = hDC;
		}

		::Rectangle( m_hDC, 0, 0, m_rcClient.right, m_rcClient.bottom );
		m_iSavedDC = ::SaveDC( m_hDC );
		SetBkMode( m_hDC, TRANSPARENT );
	}

	~CDCGraphics()
	{
		if( m_bDoubleBuffered )
		{
			BitBlt( m_hWndDC, m_rcClient.left, m_rcClient.top, m_rcClient.right-m_rcClient.left, m_rcClient.bottom-m_rcClient.top, m_hDC, 0, 0, SRCCOPY);
			::DeleteObject( m_hBitmap );
			::DeleteDC( m_hDC );
		}
		else
		{
			::RestoreDC( m_hDC, m_iSavedDC );
		}

		if( m_hPen != NULL )
		{
			::DeleteObject(m_hPen);
		}

		if ( m_hPenDash != NULL )
		{
			::DeleteObject(m_hPenDash);
		}
		if( m_hBrush != NULL )
		{
			::DeleteObject( m_hBrush );
		}
	}

	virtual void setColor( Color color )
	{
		if( m_hPen != NULL )
		{
			::DeleteObject(m_hPen);
		}
		if( m_hBrush != NULL )
		{
			::DeleteObject( m_hBrush );
		}
		if ( m_hPenDash != NULL )
		{
			::DeleteObject(m_hPenDash);
		}

		m_hPen = ::CreatePen( PS_SOLID, 1, color);
		m_hPenDash = ::CreatePen( PS_DOT, 1, color);

		::SelectObject( m_hDC, m_hPen );
		m_hBrush = ::CreateSolidBrush( color );
		::SelectObject( m_hDC, m_hBrush );
		
		SetTextColor( m_hDC, color );
	}

	virtual void fillRect( int x, int y, int w, int h )
	{
		x+=m_xOffset;
		y+=m_yOffset;
		::Rectangle( m_hDC, x, y, x+w, y+h );
	}

	virtual void fillOval( int x, int y, int w, int h )
	{
		x+=m_xOffset;
		y+=m_yOffset;
		::Ellipse( m_hDC, x, y, x+w, y+h );
	}

	virtual void drawLine( int x1, int y1, int x2, int y2 )
	{
		x1+=m_xOffset;
		y1+=m_yOffset;
		x2+=m_xOffset;
		y2+=m_yOffset;
		::MoveToEx( m_hDC, x1, y1, NULL );
		::LineTo( m_hDC, x2, y2 );
	}

	virtual void drawDashLine( int x1, int y1, int x2, int y2 )
	{
		HGDIOBJ hOld = ::SelectObject( m_hDC, m_hPenDash);
		x1+=m_xOffset;
		y1+=m_yOffset;
		x2+=m_xOffset;
		y2+=m_yOffset;
		::MoveToEx( m_hDC, x1, y1, NULL );
		::LineTo( m_hDC, x2, y2 );
		::SelectObject( m_hDC, hOld );
	}

	virtual void drawString( const std::wstring& text, int x, int y )
	{
		x+=m_xOffset;
		y+=m_yOffset;
		RECT rcText;
		rcText.left = x;
		rcText.top = y;
		rcText.right = x + WIDTH;
		rcText.bottom = y + 30;
		::DrawTextW(m_hDC, text.c_str(), (int)text.size(), &rcText, DT_LEFT );
	}
};

//
//  FUNCTION: WndProc(HWND, UINT, WPARAM, LPARAM)
//
//  PURPOSE:  Processes messages for the main window.
//
//  WM_COMMAND	- process the application menu
//  WM_PAINT	- Paint the main window
//  WM_DESTROY	- post a quit message and return
//
//
LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	int wmId, wmEvent;
	PAINTSTRUCT ps;
	HDC hdc;

	switch (message)
	{
	case WM_COMMAND:
		wmId    = LOWORD(wParam);
		wmEvent = HIWORD(wParam);
		// Parse the menu selections:
		switch (wmId)
		{
		case IDM_ABOUT:
			DialogBox(hInst, MAKEINTRESOURCE(IDD_ABOUTBOX), hWnd, About);
			break;
		case IDM_EXIT:
			DestroyWindow(hWnd);
			break;
		default:
			return DefWindowProc(hWnd, message, wParam, lParam);
		}
		break;
	case WM_PAINT:
		hdc = BeginPaint(hWnd, &ps);
		{
			RECT rcClient;
			::GetClientRect( hWnd, &rcClient );
			rcClient.right = WIDTH;
			rcClient.bottom = HEIGHT;
			CDCGraphics dc( hdc, rcClient, BORDER_SPACE, BORDER_SPACE, TRUE );

#ifndef CALC_AND_PAINT
			g_voronoi.paintTessellations( dc );
#else
			g_voronoi.calcAndPaintTessellations(dc);
#endif

		}
		EndPaint(hWnd, &ps);
		break;
	case WM_LBUTTONDOWN:
		{
			int x = GET_X_LPARAM( lParam );
			int y = GET_Y_LPARAM( lParam );
			x-=BORDER_SPACE;
			y-=BORDER_SPACE;
			if( wParam & MK_SHIFT)
			{
				g_mouseDownIndex = g_voronoi.hitTest( x, y );
				if( g_mouseDownIndex != -1)
				{
					g_lastPos.x = x;
					g_lastPos.y = y;
					::SetCapture( hWnd);
				}
			}
			else
			{
				if( g_voronoi.addPoint( x, y ) )
				{
#ifndef CALC_AND_PAINT
					g_voronoi.calcTessellations();
#endif
					InvalidateRect( hWnd, NULL, TRUE );
				}
			}
			return 1;
		}
	case WM_MOUSEMOVE:
		{
			if( g_mouseDownIndex != -1)
			{
				int x = GET_X_LPARAM( lParam );
				int y = GET_Y_LPARAM( lParam );
				x-=BORDER_SPACE;
				y-=BORDER_SPACE;

				if( g_voronoi.movePoint( g_mouseDownIndex, x-g_lastPos.x, y-g_lastPos.y, false))
				{
#ifndef CALC_AND_PAINT
					g_voronoi.calcTessellations();
#endif
					InvalidateRect( hWnd, NULL, TRUE );
				}
				g_lastPos.x = x;
				g_lastPos.y = y;
			}
			return 1;
		}
	case WM_LBUTTONUP:
		{
			if( g_mouseDownIndex != -1)
			{
				g_mouseDownIndex = -1;
				::ReleaseCapture( );
			}
			return 1;
		}
	case WM_MBUTTONUP:
		{
			int x = GET_X_LPARAM( lParam );
			int y = GET_Y_LPARAM( lParam );
			x-=BORDER_SPACE;
			y-=BORDER_SPACE;
			if ( x <= 0 || y <= 0 )
			{
				break;
			}

			bool bRefresh = false;
			int index = g_voronoi.hitTest( x, y );
			if ( index != -1 )
			{
				bRefresh = g_voronoi.erasePoint( index );
			}
		
			if( bRefresh )
			{
				g_voronoi.calcTessellations();
				RECT rcConvas;
				rcConvas.left = 0;
				rcConvas.top = 0;
				rcConvas.right = CONVAS_WIDTH;
				rcConvas.bottom = CONVAS_HEIGHT;
				InvalidateRect( hWnd, &rcConvas, FALSE );
			}
			
			return 1;
		}
		break;
	case WM_LBUTTONDBLCLK:
		{
			int x = GET_X_LPARAM( lParam );
			int y = GET_Y_LPARAM( lParam );
			x-=BORDER_SPACE;
			y-=BORDER_SPACE;

			if ( x <= 0 || y <= 0 )
			{
				break;
			}

			bool bRefresh = false;
			bRefresh = g_voronoi.randomPoints( 100, x, y );
			if( bRefresh )
			{
				g_voronoi.calcTessellations();
				RECT rcConvas;
				rcConvas.left = 0;
				rcConvas.top = 0;
				rcConvas.right = CONVAS_WIDTH;
				rcConvas.bottom = CONVAS_HEIGHT;
				InvalidateRect( hWnd, &rcConvas, FALSE );
			}

			return 1;
		}
	case WM_RBUTTONDOWN:
		{
			g_voronoi.reset();
			InvalidateRect( hWnd, NULL, TRUE );
			return 1;
		}
	case WM_ERASEBKGND:
		{
			RECT rcClient;
			::GetClientRect( hWnd, &rcClient );

			HRGN hRgn = ::CreateRectRgn( rcClient.left, rcClient.top, rcClient.right, rcClient.bottom );
			HRGN hRgnConvas = ::CreateRectRgn( 0, 0, CONVAS_WIDTH, CONVAS_HEIGHT);
			int nRet = ::CombineRgn( hRgn, hRgn, hRgnConvas, RGN_XOR );
			if( nRet != ERROR)
			{
				HDC hDC = ::GetDC( hWnd );
				HBRUSH hBrush = GetSysColorBrush( COLOR_WINDOW );
				::FillRgn( hDC, hRgn, hBrush );
				::ReleaseDC( hWnd, hDC );
				::DeleteObject( hRgn );
				return TRUE;
			}
			
			return FALSE;
		}
	case WM_DESTROY:
		PostQuitMessage(0);
		break;
	default:
		return DefWindowProc(hWnd, message, wParam, lParam);
	}
	return 0;
}

// Message handler for about box.
INT_PTR CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	UNREFERENCED_PARAMETER(lParam);
	switch (message)
	{
	case WM_INITDIALOG:
		return (INT_PTR)TRUE;

	case WM_COMMAND:
		if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL)
		{
			EndDialog(hDlg, LOWORD(wParam));
			return (INT_PTR)TRUE;
		}
		break;
	}
	return (INT_PTR)FALSE;
}
