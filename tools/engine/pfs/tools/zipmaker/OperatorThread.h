#pragma once
#include "SaveOperator.h"
#include "GlobalErrorString.h"

#define WM_SAVEPROGRESS		(WM_USER+100)

class CSaveThreadListener : public CThreadListener
{
	wchar_t			m_buffer[MAX_PATH];
	HWND			m_hWnd;
public:
	CSaveThreadListener() : m_hWnd( NULL) {}
	void SetHWnd( HWND hWnd) { m_hWnd = hWnd; }
	virtual void OnNotify( WPARAM wParam, LPARAM lParam)
	{
		if( COperator::PT_FILENAME == lParam)
		{
			wcscpy_s( m_buffer, MAX_PATH,  reinterpret_cast<LPCTSTR>( wParam));
			wParam = (WPARAM)m_buffer;
		}
		else if( COperator::PT_FILEERROR == lParam)
		{
			wchar_t	err[MAX_PATH] = {0};
			int nError = PFS::CEnv::GetLastError();
			if( nError == 0 ) nError = GetLastError();
			if( nError == 0 )
			{
				// 既然没有错误信息，不再显示错误号。
				wsprintf( err, L"%s", reinterpret_cast<LPCTSTR>( wParam) );
			}
			else
			{
				wsprintf( err, L"%s 错误号: %d", reinterpret_cast<LPCTSTR>( wParam), PFS::CEnv::GetLastError());
			}
			CErrorStringArray::GetInstance().Push( err);
			wParam = 0;
		}
		if( ::IsWindow( m_hWnd))
		{
			int retrytimes = 0;
			while( true)
			{
				BOOL res = ::PostMessage( m_hWnd, WM_SAVEPROGRESS, wParam, lParam);
				if( res || (COperator::PT_TOTALEND != lParam))
					break;
				if( retrytimes++ >= 3)	// 重试3次，保证 PostMessage 成功
					break;
				Sleep( 100);
			} 
		}
	}
};

class COperatorThread : public CBaseThread
{
	COperator::COperatorVector		m_operators;
public:
	COperatorThread( ) {}
	void SetOperators( COperator::COperatorVector& operators) { m_operators = operators; }

	virtual void OnRunning()
	{
		m_stop = false;
		for( COperator::COperatorVector::iterator itor = m_operators.begin(); itor != m_operators.end(); ++itor)
		{
			(*itor)->SetListener( m_listener);
			(*itor)->Do();
			if( m_stop)
				break;
		}
		if( m_listener)
			m_listener->OnNotify( 0, COperator::PT_TOTALEND);
	}
};
