// ScreenGrabber.cpp : Defines the entry point for the application.
//

#include "stdafx.h"
#include "ScreenGrabber.h"
#include "DeviceContext.h"
#include "ScreenshotWnd.h"
#include "TaskExplorerFrameWnd.h"
#include "XWProcessTreeControl.h"
#include "XWProcessListControl.h"
#pragma comment(lib, "comctl32.lib" )

#define MAX_LOADSTRING 100

// Global Variables:
HINSTANCE hInst;								// current instance
TCHAR szTitle[MAX_LOADSTRING];					// The title bar text
TCHAR szWindowClass[MAX_LOADSTRING];			// the main window class name

// Forward declarations of functions included in this code module:
ATOM				MyRegisterClass(HINSTANCE hInstance);
BOOL				InitInstance(HINSTANCE, int);
LRESULT CALLBACK	WndProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK	About(HWND, UINT, WPARAM, LPARAM);

class CMainWnd : public CXWFrameWnd
{
public:
	virtual BOOL OnWndMsg(UINT message, WPARAM wParam, LPARAM lParam, LRESULT* pResult );
	virtual BOOL OnCommand(WPARAM wParam, LPARAM lParam );
	virtual int OnCreate(LPCREATESTRUCT lpCreateStruct );
	virtual void OnSize(UINT nType, int cx, int cy);

	CScreenshotWnd m_screenShotWnd;
	CTaskExplorerFrameWnd m_taskExplorerFrameWnd;
	CXWProcessListControl m_listCtrl;
};

static CMainWnd g_mainWnd;

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
	LoadString(hInstance, IDC_SCREENGRABBER, szWindowClass, MAX_LOADSTRING);
	XWRegisterClass(hInstance);
	
	hInst = hInstance; // Store instance handle in our global variable

	// Perform application initialization:
	HMENU hMenu = ::LoadMenu( hInstance,MAKEINTRESOURCE(IDC_SCREENGRABBER));
	BOOL bRet = g_mainWnd.Create( NULL, szTitle, WS_OVERLAPPEDWINDOW,
		CW_USEDEFAULT, 0, CW_USEDEFAULT, 0, NULL, hMenu, NULL );
	if( !bRet  )
	{
		return FALSE;
	}

	g_mainWnd.ShowWindow(nCmdShow);
	g_mainWnd.UpdateWindow();

	hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_SCREENGRABBER));
	
	INITCOMMONCONTROLSEX InitCtrls;
	InitCtrls.dwSize = sizeof(INITCOMMONCONTROLSEX);
	InitCtrls.dwICC = ICC_LISTVIEW_CLASSES | ICC_TREEVIEW_CLASSES;
	InitCommonControlsEx(  &InitCtrls );
	//InitCommonControls();

	// Main message loop:
	while (GetMessage(&msg, NULL, 0, 0))
	{
		if( XWPreTranslateMessage( &msg ) )
		{
			continue;
		}

		if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
		{
			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
	}

	return (int) msg.wParam;
}

int CMainWnd::OnCreate(LPCREATESTRUCT lpCreateStruct )
{
	CRect rcClient;
	GetClientRect( &rcClient );
	if( !m_listCtrl.Create( L"Process List",
		0, 0, rcClient.Width(), rcClient.Height(), 
		GetSafeHwnd(), 253 ) )
	{
		return -1;
	}
	
	return 0;
}

BOOL CMainWnd::OnCommand( WPARAM wParam, LPARAM lParam )
{
	HWND hWnd = GetSafeHwnd();

	int wmId, wmEvent;
	wmId    = LOWORD(wParam);
	wmEvent = HIWORD(wParam);

	// Parse the menu selections:
	switch (wmId)
	{
	case IDM_ABOUT:
		DialogBox(hInst, MAKEINTRESOURCE(IDD_ABOUTBOX), hWnd, About);
		break;
	case IDM_EXIT:
		DestroyWindow();
		break;
	case ID_GRABSCREEN:
		if( !m_screenShotWnd.GetSafeHwnd() )
		{
			::ShowWindow( hWnd, SW_MINIMIZE );
			::ShowWindow( hWnd, SW_HIDE );

			if( m_screenShotWnd.Create(NULL, L"Screenshot", 
				WS_OVERLAPPEDWINDOW, 100, 100, 600,400, hWnd, 0, 0) )
			{
				m_screenShotWnd.ShowWindow(SW_SHOWMAXIMIZED );
				m_screenShotWnd.UpdateWindow();
			}

			::ShowWindow( hWnd, SW_SHOW);
			::ShowWindow( hWnd, SW_RESTORE );
		}
		else
		{
			m_screenShotWnd.ShowWindow( SW_SHOW );
		}
		break;
	case ID_TASKEXPLORER:
		{
			if( !m_taskExplorerFrameWnd.GetSafeHwnd() )
			{
				BOOL bRet = m_taskExplorerFrameWnd.Create( 
					NULL, L"Task Explorer", 
					WS_OVERLAPPEDWINDOW, 100, 100, 600,400, hWnd, 0, 0);
				if( bRet )
				{
					m_taskExplorerFrameWnd.ShowWindow( SW_SHOW );
					m_taskExplorerFrameWnd.UpdateWindow();
				}
			}
			else
			{
				m_taskExplorerFrameWnd.ShowWindow( SW_SHOW );
			}
		}
		break;
	default:
		return FALSE;
	}

	return FALSE;
}

BOOL CMainWnd::OnWndMsg(UINT message, WPARAM wParam, LPARAM lParam, LRESULT* pResult )
{
	switch (message)
	{	
	case WM_DESTROY:
		PostQuitMessage(0);
		return TRUE;
	default:
		break;
	}

	return __super::OnWndMsg(message, wParam, lParam, pResult);
}

void CMainWnd::OnSize(UINT nType, int cx, int cy)
{
	if( nType != SIZE_MINIMIZED &&
		nType != SIZE_MAXSHOW && 
		nType != SIZE_MAXHIDE )
	{
		m_listCtrl.MoveWindow( 0, 0, cx, cy);
	}

	return __super::OnSize( nType, cx , cy );
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
