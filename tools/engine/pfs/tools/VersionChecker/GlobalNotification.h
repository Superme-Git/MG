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
#include <vector>

typedef LONG MessageId;
typedef LONG DataId;

#define WM_GLOBALSTATUS		WM_USER + 500

#define WM_DATAFETCH		WM_USER + 501
#define WM_DATAREADY		WM_USER + 502
#define WM_DATALOST			WM_USER + 503

#define WM_PROGRESS_START	WM_USER + 1001 
#define WM_PROGRESS_STEP	WM_USER + 1002
#define WM_PROGRESS_END		WM_USER + 1003

struct Message
{
	DWORD dwStatusCode;
	std::wstring strDetail;
};

struct Data
{
	int iItemId;
	std::wstring strItemName;
	std::vector<unsigned char> vData;

	void swap( Data& _rRight )
	{
		iItemId = _rRight.iItemId;
		strItemName.swap( _rRight.strItemName );
		vData.swap( _rRight.vData );
	}
};

class IWatcher
{
public:
	virtual void OnNotify(MessageId mid) = 0;
	virtual void OnNotifyDataFetch(DataId did) = 0;
	virtual void OnNotifyDataReady(DataId did) = 0;
	virtual void OnNotifyDataLost(DataId did) = 0;

	virtual void OnProgressStart(int nRange) = 0;
	virtual void OnProgressStep(MessageId mid) = 0;
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

	void SetDataHwnd( HWND hWnd )
	{
		m_hDataWnd = hWnd;
	}

	virtual void OnProgressStart(int nRange)
	{
		PostMessage( m_hWnd, WM_PROGRESS_START, 0, nRange );
	}

	virtual void OnProgressStep(MessageId mid)
	{
		PostMessage( m_hWnd, WM_PROGRESS_STEP, 0, mid );
	}

	virtual void OnProgressEnd()
	{
		PostMessage( m_hWnd, WM_PROGRESS_END, 0, 0 );
	}

	virtual void OnNotify(MessageId mid)
	{
		PostMessage( m_hWnd, WM_GLOBALSTATUS, 0, mid);
	}

	virtual void OnNotifyDataFetch(DataId did)
	{
		PostMessage( m_hDataWnd, WM_DATAFETCH, 0, did);
	}

	virtual void OnNotifyDataReady(DataId did)
	{
		PostMessage( m_hDataWnd, WM_DATAREADY, 0, did);
	}

	virtual void OnNotifyDataLost(DataId did)
	{
		PostMessage( m_hDataWnd, WM_DATALOST, 0, did);
	}

	virtual void Destroy()
	{
		delete this;
	}

protected:
	HWND m_hWnd, m_hDataWnd;
};

class CGlobalNotification
{
public:
	~CGlobalNotification(void);
	bool RegisterWatcher( IWatcher* watcher );
	void UnRegisterWatcher( IWatcher* watcher );

	// 状态消息
	void Notify( const Message& rMessage );

	// 数据下载
	void NotifyDataFetch( Data& rData );
	void NotifyDataReady( Data& rData );
	void NotifyDataLost( Data& rData );

	// 进度信息
	void NotifyStart( int nRange );
	void NotifyStep( const Message& rMessage );
	void NotifyEnd( );

	BOOL GetMessage(MessageId mid, Message* pMessage );
	BOOL GetData(DataId did, Data& rData );

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

	std::map<DataId, Data> m_dataContainer;
	DataId m_nextDataid;
};

extern bool GlobalRegister( IWatcher* watcher );
extern void GlobalUnRegister( IWatcher* watcher );

extern void GlobalNotifyErrorInfo( const upd::ErrorInfo& rErrorInfo, LPCWSTR lpszContext = NULL );
extern void __cdecl GlobalNotify( LPCWSTR lpszFormat, ... );
extern void __cdecl GlobalNotify( DWORD dwStatusCode, LPCWSTR lpszFormat, ... );
extern BOOL GlobalGetMessage( MessageId mid, Message* pMessage);

extern void GlobalNotifyStart( int nRange );
extern void GlobalNotifyStep( const std::wstring& rstrItem, DWORD dwStatusCode = 0 );
extern void GlobalNotifyEnd( );

extern void __cdecl GlobalNotifyDataFetch( Data& rData );
extern void __cdecl GlobalNotifyDataReady( Data& rData );
extern void __cdecl GlobalNotifyDataLost( Data& rData );

extern BOOL GlobalGetData( DataId did, Data& rData );
