///=============================================================================
/// Product   : MHZX
/// Project   : Launcher
/// Module    : GlobalNotification.h
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	全局通告器
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-12 Created.
///=============================================================================
#pragma once

#include "ErrorInfo.h"

#include <map>

typedef LONG MessageId;

#define WM_GLOBALSTATUS		WM_USER + 500
#define WM_PROGRESS_START WM_USER+1001 
#define WM_PROGRESS_STEP WM_USER+1002
#define WM_PROGRESS_END WM_USER+1003
struct Message
{
	DWORD dwStatusCode;
	std::wstring strDetail;
};

class IWatcher
{
public:
	virtual void OnNotify(MessageId mid) = 0;
	virtual void OnProgressStart(int nRange) = 0;

	// stepValue	 =0	进度前进一步
	//				!=0	进度前进到setpValue指定的地方
	virtual void OnProgressStep(MessageId mid, int stepValue) = 0;
	virtual void OnProgressEnd() = 0;

	virtual void Destroy() = 0;
};

class CWndWatcher : public IWatcher
{
public:
	void SetHwnd( HWND hWnd )
	{
		m_hWnd = hWnd;
	}

	virtual void OnProgressStart(int nRange)
	{
		PostMessage( m_hWnd, WM_PROGRESS_START, 0, nRange );
	}

	virtual void OnProgressStep(MessageId mid, int stepValue)
	{
		PostMessage( m_hWnd, WM_PROGRESS_STEP, stepValue, mid );
	}

	virtual void OnProgressEnd()
	{
		PostMessage( m_hWnd, WM_PROGRESS_END, 0, 0 );
	}

	virtual void OnNotify(MessageId mid)
	{
		PostMessage( m_hWnd, WM_GLOBALSTATUS, 0, mid);
	}

	virtual void Destroy()
	{
		delete this;
	}

protected:
	HWND m_hWnd;
};

class CGlobalNotification
{
public:
	~CGlobalNotification(void);
	bool RegisterWatcher( IWatcher* watcher );
	void UnRegisterWatcher( IWatcher* watcher );
	void Notify( const Message& rMessage );

	// 进度信息
	void NotifyStart( int nRange );
	void NotifyStep( const Message& rMessage, int stepValue );
	void NotifyEnd( );

	BOOL GetMessage(MessageId mid, Message* pMessage );

	static CGlobalNotification& Instance()
	{
		static CGlobalNotification _Instance;
		return _Instance;
	}
	
protected:
	CGlobalNotification(void);
	IWatcher* m_watcher; // 这里只允许有一个Watcher
	std::map<MessageId,Message> m_messageContainer;
	MessageId m_nextMid;
};

extern bool GlobalRegister( IWatcher* watcher );
extern void GlobalUnRegister( IWatcher* watcher );

extern void GlobalNotifyErrorInfo( const upd::ErrorInfo& rErrorInfo, LPCWSTR lpszContext = NULL );
extern void __cdecl GlobalNotify( LPCWSTR lpszFormat, ... );
extern void __cdecl GlobalNotify( DWORD dwStatusCode, LPCWSTR lpszFormat, ... );
extern BOOL GlobalGetMessage( MessageId mid, Message* pMessage);
extern void GlobalNotifyStart( int nRange );
extern void GlobalNotifyStep( const std::wstring& rstrItem, DWORD dwStatusCode = 0, int stepValue = 0);
extern void GlobalNotifyEnd( );
