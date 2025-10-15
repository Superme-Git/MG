///=============================================================================
/// Product   : MHZX
/// Project   : Launcher
/// Module    : TrayIcon.cpp
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	托盘图标
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-22 Created.
///=============================================================================
#include "stdafx.h"
#include "Launcher.h"
#include "TrayIcon.h"
#include <afxpriv.h> // for AfxLoadString
#include "../Patcher/TextID.h"
#include "../Patcher/TextManager.h"

// CTrayIcon

CTrayIcon::CTrayIcon(UINT uID)
{
	memset(&m_nid, 0 , sizeof(m_nid)); // Initialize NOTIFYICONDATA
	m_nid.cbSize = sizeof(m_nid);
	m_nid.uID = uID; // never changes after construction
}

CTrayIcon::~CTrayIcon()
{
	SetIcon(0, 0); // remove icon from system tray
}

BOOL CTrayIcon::SetNotificationWnd(HWND hNotifyWnd, UINT uCbMsg, HICON hIcon, LPCTSTR lpszDefaultTip)
{
	// Set notification window. 
	m_nid.hWnd = hNotifyWnd;
	ASSERT(uCbMsg==0 || uCbMsg>=WM_USER);
	m_nid.uCallbackMessage = uCbMsg;
	
	if( lpszDefaultTip != NULL )
	{
		_tcscpy_s( m_nid.szTip, _countof(m_nid.szTip), lpszDefaultTip );
	}
	else if (m_nid.uID) 
	{
		AfxLoadString(m_nid.uID, m_nid.szTip, _countof(m_nid.szTip));
	}

	if( hIcon != NULL )
	{
		return SetIcon(hIcon, NULL);
	}
	else if (m_nid.uID) 
	{	
		hIcon = AfxGetApp()->LoadIcon(m_nid.uID);
		return SetIcon(hIcon, NULL);
	}

	return TRUE;
}

void CTrayIcon::ShowBalloonTip( LPCTSTR lpszTip, DWORD dwInfoFlags )
{
	ASSERT( lpszTip != NULL );
	NOTIFYICONDATA IconData = {0};
	IconData.cbSize = sizeof(IconData);
	IconData.hWnd = m_nid.hWnd;
	IconData.uID = m_nid.uID;
	IconData.dwInfoFlags = dwInfoFlags;
	if( dwInfoFlags != NIIF_NONE )
	{
		IconData.uFlags = NIF_INFO;
		IconData.uTimeout = 5000; // in milliseconds
		//IconData.dwState = 0;
		//IconData.dwStateMask = NIS_HIDDEN;
		_tcscpy_s(IconData.szInfoTitle, _countof(IconData.szInfoTitle), m_nid.szTip );
		_tcscpy_s(IconData.szInfo, _countof(IconData.szInfo),  lpszTip );
		Shell_NotifyIcon(NIM_MODIFY, &IconData);
	}
	else
	{
		IconData.uFlags = NIF_INFO;
		IconData.uTimeout = 10000; // in milliseconds
		//IconData.dwState = NIS_HIDDEN;
		//IconData.dwStateMask = NIS_HIDDEN;
		_tcscpy_s(IconData.szInfoTitle, _countof(IconData.szInfoTitle), _T("") );
		_tcscpy_s(IconData.szInfo, _countof(IconData.szInfo),  _T("") );
		Shell_NotifyIcon(NIM_MODIFY, &IconData);
	}	
}

void CTrayIcon::ShowNormalTipInfo( LPCTSTR lpszTip )
{
	NOTIFYICONDATA IconData = {0};
	IconData.cbSize = sizeof(IconData);
	IconData.hWnd = m_nid.hWnd;
	IconData.uID = m_nid.uID;
	//IconData.dwInfoFlags = NIIF_INFO;
	IconData.uFlags = NIF_TIP ;
	if( lpszTip)
	{
		_tcscpy_s( IconData.szTip, _countof(IconData.szTip), lpszTip );
	}
	else
	{
		_tcscpy_s( IconData.szTip, _countof(IconData.szTip), m_nid.szTip );
	}
	Shell_NotifyIcon( NIM_MODIFY, &IconData);
}

void CTrayIcon::ShowBalloonTipError( LPCTSTR lpszTip )
{
	ShowBalloonTip( lpszTip, NIIF_ERROR );
}

void CTrayIcon::ShowBalloonTipInfo( LPCTSTR lpszTip )
{
	ShowBalloonTip( lpszTip, NIIF_INFO );
}

BOOL CTrayIcon::SetIcon(HICON hicon, LPCTSTR lpTip)
{
	// Common SetIcon for all overloads.
	UINT msg;
	m_nid.uFlags = 0;

	if (hicon)
	{
		// Set the icon
		msg = m_nid.hIcon ? NIM_MODIFY : NIM_ADD;
		m_nid.hIcon = hicon; // Add or replace icon in system tray
		m_nid.uFlags |= NIF_ICON;
	}
	else 
	{
		if (m_nid.hIcon==NULL) // remove icon from tray
			return TRUE; // already deleted
		msg = NIM_DELETE;
	}

	if (lpTip) // Use the tip, if any
		_tcscpy_s(m_nid.szTip, _countof(m_nid.szTip), lpTip);

	if (m_nid.szTip[0])
		m_nid.uFlags |= NIF_TIP;
	if (m_nid.uCallbackMessage && m_nid.hWnd) // Use callback if any
		m_nid.uFlags |= NIF_MESSAGE;

	BOOL bRet = Shell_NotifyIcon(msg, &m_nid); // Do it

	if (msg==NIM_DELETE || !bRet)
		m_nid.hIcon = NULL; // failed

	return bRet;
}

LRESULT CTrayIcon::OnTrayNotification(WPARAM wID, LPARAM lEvent)
{
	if (wID != m_nid.uID || 
		(lEvent != WM_CONTEXTMENU && 
		lEvent != WM_RBUTTONUP && 
		lEvent != WM_LBUTTONUP && 
		lEvent != WM_LBUTTONDBLCLK) )
		return 0;

	CMenu menu;//装载上下文菜单；
	CMenu* pSubMenu = NULL;
	const std::wstring& strMenuItem = GlobalGetText(TOK_BUTTON_SHOW_S);
	const std::wstring& strMenuItem2 = GlobalGetText(TOK_BUTTON_EXIT_X);
	if( !strMenuItem.empty() && !strMenuItem2.empty() )
	{
		if( !menu.CreatePopupMenu() )
		{
			ASSERT(FALSE);
			return 0;
		}

		pSubMenu = &menu;

		// 显示窗口(&S)
		pSubMenu->AppendMenu(MF_STRING, ID_SHOW_LAUNCHER, strMenuItem.c_str());
		// 退出(&X)
		pSubMenu->AppendMenu(MF_STRING, ID_QUIT_APP, strMenuItem2.c_str());		
	}
	else
	{
		if (!menu.LoadMenu(m_nid.uID))
		{
			ASSERT(FALSE);
			return 0;
		}
		else
		{
			pSubMenu = menu.GetSubMenu(0);
			if( pSubMenu == NULL )
			{
				ASSERT(pSubMenu);
				return 0;
			}
		}
	}
	
	if ( lEvent == WM_RBUTTONUP || lEvent == WM_CONTEXTMENU )
	{
		// 移除Tooltip
		ShowBalloonTip( _T(""), NIIF_NONE );

		//将当前菜单作为上下文菜单；
		CPoint mouse;
		GetCursorPos(&mouse);
		::SetForegroundWindow(m_nid.hWnd);

		//设置第一个菜单项为默认菜单项目
		::SetMenuDefaultItem(pSubMenu->m_hMenu, 0, TRUE);

		::TrackPopupMenu(pSubMenu->m_hMenu, 0, mouse.x, mouse.y, 0,m_nid.hWnd, NULL);
	}
	else
	{
		// left-button-up or double click: execute first menu item
		//::SetFocus( m_nid.hWnd );
		::SendMessage(m_nid.hWnd, WM_COMMAND, pSubMenu->GetMenuItemID(0), 0);
	}

	return 1;
}
