#pragma once
#include "../packzip/basethread.h"

#define	WM_PROGRESS		(WM_USER+2002)

enum
{
	INIT = 0,
	TOTALLEN,
	TOTALEND,
	FILENAME,
	FILELEN,
	FILEPROGRESS,
	FILEEND,
	FILEERROR,
};

class CProgressThreadListener : public CThreadListener
{
	wchar_t			m_buffer[MAX_PATH];
	HWND			m_hWnd;
	DWORD			m_error;

public:
	CProgressThreadListener() : m_hWnd( NULL), m_error( 0) {}
	void SetHWnd( HWND hWnd) { m_hWnd = hWnd; }
	DWORD GetError() { return m_error; }
	virtual void OnNotify( WPARAM wParam, LPARAM lParam)
	{
		if( FILENAME == lParam || FILEERROR == lParam)
		{
			m_error = PFS::CEnv::GetLastError();
			wcscpy_s( m_buffer, MAX_PATH,  reinterpret_cast<LPCTSTR>( wParam));
			wParam = (WPARAM)m_buffer;
		}
		if( ::IsWindow( m_hWnd))
			::PostMessage( m_hWnd, WM_PROGRESS, wParam, lParam);
	}
};