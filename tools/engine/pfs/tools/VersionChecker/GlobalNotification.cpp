///=============================================================================
/// Product   : MHZX
/// Project   : Launcher
/// Module    : GlobalNotification.cpp
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	全局通告器
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-12 Created.
///=============================================================================
#include "StdAfx.h"
#include "GlobalNotification.h"
#include <afxmt.h>
#include "KHLogger.h"

// 注：为了简单这里只将多线程保护用在了全局函数中，而没有对类的成员函数实施保护
// 因此，这里只定义了一个全局的临界区对象
static CCriticalSection g_csMessageContainer;
#define AUTO_LOCK()	CSingleLock __autoLock(&g_csMessageContainer, TRUE)

static CCriticalSection g_csDataContainer;
#define AUTO_LOCK_DATA() CSingleLock __autoLock(&g_csDataContainer, TRUE)

CGlobalNotification::CGlobalNotification(void)
{
	m_watcher = NULL;
	m_nextMid = 0;
	m_nextDataid = 0;
}

CGlobalNotification::~CGlobalNotification(void)
{
	if ( m_watcher != NULL )
	{
		m_watcher->Destroy();
		m_watcher = NULL;
	}
}

bool CGlobalNotification::RegisterWatcher( IWatcher* watcher )
{
	if ( watcher == NULL )
	{
		return false;
	}

	if( m_watcher != watcher )
	{
		if ( m_watcher != NULL )
		{
			UnRegisterWatcher( m_watcher );
		}

		m_watcher = watcher;
	}

	return true;
}

void CGlobalNotification::UnRegisterWatcher( IWatcher* watcher )
{
	if( m_watcher == watcher )
	{
		m_watcher = NULL;
	}
}

void CGlobalNotification::Notify( const Message& rMessage )
{
	if( rMessage.dwStatusCode == 0 )
	{
		KHMsgLog_InfoW( L"%s\n", rMessage.strDetail.c_str());
	}
	else
	{
		KHMsgLog_InfoW( L"%s(%d)\n", rMessage.strDetail.c_str(), rMessage.dwStatusCode);
	}

	m_messageContainer.insert( std::make_pair( m_nextMid, rMessage ) );

	if( m_watcher != NULL )
	{
		m_watcher->OnNotify( m_nextMid );
	}

	m_nextMid ++;
}


void CGlobalNotification::NotifyDataFetch( Data& rData )
{
	Data& rNewData = m_dataContainer[m_nextDataid];
	rNewData.swap( rData );
	if ( m_watcher != NULL )
	{
		m_watcher->OnNotifyDataFetch( m_nextDataid );
	}

	m_nextDataid ++;
}

void CGlobalNotification::NotifyDataReady( Data& rData )
{
	Data& rNewData = m_dataContainer[m_nextDataid];
	rNewData.swap( rData );
	if ( m_watcher != NULL )
	{
		m_watcher->OnNotifyDataReady( m_nextDataid );
	}

	m_nextDataid ++;
}

void CGlobalNotification::NotifyDataLost( Data& rData )
{
	Data& rNewData = m_dataContainer[m_nextDataid];
	rNewData.swap( rData );
	if ( m_watcher != NULL )
	{
		m_watcher->OnNotifyDataLost( m_nextDataid );
	}

	m_nextDataid ++;
}


BOOL CGlobalNotification::GetMessage(MessageId mid, Message* pMessage )
{
	std::map<MessageId,Message>::iterator itr = m_messageContainer.find( mid );
	if( itr != m_messageContainer.end() )
	{		
		*pMessage = itr->second;
		m_messageContainer.erase(itr);
		return TRUE;
	}
	return FALSE;
}

BOOL CGlobalNotification::GetData(DataId did, Data& rData )
{
	std::map<DataId,Data>::iterator itr = m_dataContainer.find(did);
	if ( itr != m_dataContainer.end() )
	{
		rData.swap( itr->second );		
		m_dataContainer.erase( itr );
		return TRUE;
	}

	return FALSE;
}

void CGlobalNotification::NotifyStart( int nRange )
{
	if( m_watcher != NULL )
	{
		m_watcher->OnProgressStart(nRange);
	}
}

void CGlobalNotification::NotifyStep(const Message& rMessage )
{
	if ( rMessage.dwStatusCode != 0 )
	{
		// 只记录错误状态的信息
		KHMsgLog_InfoW( L"%s(%d)\n", rMessage.strDetail.c_str(), rMessage.dwStatusCode);
	}

	m_messageContainer.insert( std::make_pair( m_nextMid, rMessage ) );

	if( m_watcher != NULL )
	{
		m_watcher->OnProgressStep(m_nextMid);
	}

	m_nextMid ++;
}

void CGlobalNotification::NotifyEnd( )
{
	if( m_watcher != NULL )
	{
		m_watcher->OnProgressEnd();
	}
}

void GlobalNotify( LPCWSTR lpszFormat, ... )
{
	Message message;
	message.dwStatusCode = 0;

	va_list vl;
	va_start(vl, lpszFormat);

	LPTSTR lpBuffer = NULL;
	::FormatMessage(
		FORMAT_MESSAGE_FROM_STRING | FORMAT_MESSAGE_ALLOCATE_BUFFER, lpszFormat, 
		0, 0, (LPTSTR)&lpBuffer, 0, &vl);
	if( lpBuffer != NULL )
	{	
		message.strDetail = lpBuffer;
		LocalFree(lpBuffer);
	}

	va_end(vl);

	AUTO_LOCK();
	CGlobalNotification::Instance().Notify( message );
}

void GlobalNotify(	DWORD dwStatusCode, LPCWSTR lpszFormat, ... )
{
	Message message;
	message.dwStatusCode = dwStatusCode;

	va_list vl;
	va_start(vl, lpszFormat);

	LPTSTR lpBuffer = NULL;
	::FormatMessage(
		FORMAT_MESSAGE_FROM_STRING | FORMAT_MESSAGE_ALLOCATE_BUFFER, lpszFormat, 
		0, 0, (LPTSTR)&lpBuffer, 0, &vl);
	if( lpBuffer != NULL )
	{	
		message.strDetail = lpBuffer;
		LocalFree(lpBuffer);
	}

	va_end(vl);
	
	AUTO_LOCK();
	CGlobalNotification::Instance().Notify( message );
}

void GlobalNotifyErrorInfo( const upd::ErrorInfo& rErrorInfo, LPCWSTR lpszContext /*= NULL*/ )
{
	Message message;
	message.dwStatusCode = rErrorInfo.ErrorCode;
	if( lpszContext != NULL ) message.strDetail = lpszContext;
	message.strDetail += rErrorInfo.ErrorDetail;
	AUTO_LOCK();
	CGlobalNotification::Instance().Notify( message );
}

bool GlobalRegister( IWatcher* watcher )
{
	AUTO_LOCK();
	return CGlobalNotification::Instance().RegisterWatcher( watcher );
}

void GlobalUnRegister( IWatcher* watcher )
{
	AUTO_LOCK();
	CGlobalNotification::Instance().UnRegisterWatcher( watcher );
}

BOOL GlobalGetMessage( MessageId mid, Message* pMessage )
{
	ASSERT(pMessage != NULL);
	AUTO_LOCK();
	return CGlobalNotification::Instance().GetMessage( mid, pMessage );
}

void GlobalNotifyStart( int nRange )
{
	// 暂时不需要多线程保护
	return CGlobalNotification::Instance().NotifyStart( nRange );
}

void GlobalNotifyStep( const std::wstring& rstrItem, DWORD dwStatusCode )
{
	Message message;
	message.dwStatusCode = dwStatusCode;
	message.strDetail = rstrItem;

	AUTO_LOCK();
	return CGlobalNotification::Instance().NotifyStep(message);
}

void GlobalNotifyEnd( )
{
	// 暂时不需要多线程保护
	return CGlobalNotification::Instance().NotifyEnd();
}

void __cdecl GlobalNotifyDataFetch( Data& rData )
{
	AUTO_LOCK_DATA();
	CGlobalNotification::Instance().NotifyDataFetch(  rData );
}

void __cdecl GlobalNotifyDataReady( Data& rData )
{
	AUTO_LOCK_DATA();
	CGlobalNotification::Instance().NotifyDataReady(  rData );
}

void __cdecl GlobalNotifyDataLost( Data& rData )
{
	AUTO_LOCK_DATA();
	CGlobalNotification::Instance().NotifyDataLost(  rData );
}

BOOL GlobalGetData( DataId did, Data& rData )
{
	AUTO_LOCK_DATA();
	return CGlobalNotification::Instance().GetData( did, rData );
}