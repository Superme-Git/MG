#pragma once
#include <assert.h>
#include "../packzip/basethread.h"
#include "DeleteOperator.h"

#define WM_UNINSTALLPROGRESS		(WM_USER+100)

class CUninstallThreadListener : public CThreadListener
{
	HWND			m_hWnd;
	wchar_t			m_buffer[MAX_PATH];

public:
	CUninstallThreadListener() : m_hWnd( NULL) {}
	void SetHWnd( HWND hWnd) { m_hWnd = hWnd; }
	virtual void OnNotify( WPARAM wParam, LPARAM lParam)
	{
		if( CDeleteOperator::FILENAME == lParam || CDeleteOperator::PATHNAME == lParam)
		{
			wcscpy_s( m_buffer, MAX_PATH,  reinterpret_cast<LPCTSTR>( wParam));
			wParam = (WPARAM)m_buffer;
		}
		if( ::IsWindow( m_hWnd))
			::PostMessage( m_hWnd, WM_UNINSTALLPROGRESS, wParam, lParam);
	}
};

class CUninstallThread : public CBaseThread
{
	CDeleteOperator*		m_ope;

public:
	CUninstallThread() : m_ope( NULL) {}
	~CUninstallThread() { if( m_ope) delete m_ope; }

	void StartUninstall( const std::wstring& path, const std::wstring& appname, bool bDeleteAll)
	{
		if( bDeleteAll)
			m_ope = new CDeleteAllOperator;
		else
			m_ope = new CDeleteSystemOperator;
		m_ope->SetAppPath( path, appname);
		Start();
	}
	virtual void OnRunning()
	{
		if( m_ope)
		{
			m_ope->SetListener( m_listener);
			m_ope->Do();
		}
	}
};