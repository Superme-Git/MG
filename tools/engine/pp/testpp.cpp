#include <windows.h>
#include <tchar.h>

#include "perfectprotector.h"

LRESULT WINAPI MsgProc( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam )
{

    switch( msg )
    {
    case WM_DESTROY:
		PostQuitMessage(0);
		return 0;
    }

    return DefWindowProc( hWnd, msg, wParam, lParam );
}

INT WINAPI WinMain( HINSTANCE hInst, HINSTANCE, LPSTR, INT )
{
	int ppretcode = Protector::StartProtector();
	HCURSOR hCursor = LoadCursor( NULL, IDC_ARROW );
	HBRUSH hbrBackground = (HBRUSH)GetStockObject( WHITE_BRUSH );
	WNDCLASSEX wc = { sizeof(WNDCLASSEX), CS_CLASSDC, MsgProc, 0L, 0L,
                      GetModuleHandle(NULL), NULL, hCursor, hbrBackground, NULL, 
                      _T("TestPP"), NULL };
    RegisterClassEx( &wc );

    
    HWND hWnd = CreateWindow( _T("TestPP"), _T("TestPP"),
                              WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX, 100, 100, 800, 600,
                              GetDesktopWindow(), NULL, wc.hInstance, NULL );

	
	if( hWnd )
	{
		
		ShowWindow( hWnd, SW_SHOWDEFAULT );
		UpdateWindow( hWnd );

		MSG msg;
		ZeroMemory( &msg, sizeof(msg) );
		while( msg.message!=WM_QUIT )
		{
			if( PeekMessage( &msg, NULL, 0U, 0U, PM_REMOVE ) )
			{
				TranslateMessage( &msg );
				DispatchMessage( &msg );
			}
			else
				;
//				Render();
		}
	}

    UnregisterClass( _T("TestPP"), wc.hInstance );
    
	Protector::StopProtector();
	
	return 0;
}