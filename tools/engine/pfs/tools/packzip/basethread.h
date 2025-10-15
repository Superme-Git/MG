#pragma once
#include <process.h>

class CThreadListener
{
public:
	virtual void OnNotify( WPARAM wParam, LPARAM lParam) = 0;
};

class CBaseThread
{
	HANDLE				m_hThread;
	
	enum { EXIT_DELAY = 3000, };
protected:
	CThreadListener*	m_listener;
	bool				m_stop;

public:
	CBaseThread() : m_hThread( NULL), m_listener( NULL), m_stop( false) {}
	virtual ~CBaseThread() {}
private:
	static unsigned int __stdcall ThreadStartAddress(void* p)
	{
		CoInitialize( NULL);
		((CBaseThread*)p)->OnRunning();
		CoUninitialize();
		return 0;
	}
protected:
	virtual void OnRunning() = 0;
public:
	int Start()
	{
		assert( NULL == m_hThread);

		m_stop = false;
		m_hThread = (HANDLE)_beginthreadex( NULL, 0, ThreadStartAddress, this, 0, NULL);
		if( NULL == m_hThread)
			return errno;
		else
			return 0;
	}
	void Stop( DWORD time = EXIT_DELAY)
	{
		m_stop = true;
		if( WAIT_TIMEOUT == WaitForSingleObject( m_hThread, time))
			TerminateThread( m_hThread, -1);
		CloseHandle( m_hThread);
		m_hThread = NULL;
	}
	void SetListener( CThreadListener* listener) { m_listener = listener; }
	bool IsRunning() 
	{ 
		DWORD res = WaitForSingleObject( m_hThread, 0); 
		return ( (WAIT_FAILED != res) && ( WAIT_OBJECT_0 != res));
	}
	bool Suspend() { return ::SuspendThread( m_hThread) != 0xffffffff; }
	bool Resume() { return ::ResumeThread( m_hThread) != 0xffffffff; }
};
