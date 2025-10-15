///=============================================================================
/// Product   : MHZX
/// Project   : Launcher
/// Module    : TrayIcon.h
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	Õ–≈ÃÕº±Í
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-22 Created.
///=============================================================================
#pragma once

// CTrayIcon command target

class CTrayIcon
{
public:
	CTrayIcon(UINT uID);
	virtual ~CTrayIcon();

protected:
	NOTIFYICONDATA m_nid; // struct for Shell_NotifyIcon
	BOOL SetIcon(HICON hicon, LPCTSTR lpTip);

public:
	BOOL SetNotificationWnd(HWND hNotifyWnd, UINT uCbMsg, HICON hIcon, LPCTSTR lpszDefaultTip);
	void ShowBalloonTip( LPCTSTR lpszTip, DWORD dwInfoFlags);
	void ShowBalloonTipError( LPCTSTR lpszTip );
	void ShowBalloonTipInfo( LPCTSTR lpszTip );
	LRESULT OnTrayNotification(WPARAM uID, LPARAM lEvent);
	void ShowNormalTipInfo( LPCTSTR lpszTip );
};
