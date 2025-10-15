#pragma once

class CMyThreadListener : public CThreadListener
{
	HWND			m_hWnd;
	UINT			m_messageid;

	virtual void OnNotify( WPARAM wParam, LPARAM lParam)
	{
		if( ::IsWindow( m_hWnd))
		{
			::PostMessage( m_hWnd, m_messageid, wParam, lParam);
		}
	}

public:
	CMyThreadListener() : m_hWnd( NULL), m_messageid( 0) {}

	void SetHWnd( HWND hWnd) 
	{ 
		m_hWnd = hWnd; 
	}

	void SetMessageId( UINT messageid) 
	{
		m_messageid = messageid;
	}
};