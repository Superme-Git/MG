#pragma once
#include <process.h>
#include <windows.h>
#include "common.h"

class CThreadListener
{
public:
	virtual void OnNotify( WPARAM wParam, LPARAM lParam) = 0;
};

static HANDLE hEvent = ::CreateEvent(NULL, false, true, NULL/*L"PackEvent"*/);

class CBaseThread
{
	HANDLE				m_hThread;
	
	enum { EXIT_DELAY = 3000, };
	bool				m_bNormalEnd;
protected:
	CThreadListener*	m_listener;
	bool				m_stop;

public:
	CBaseThread() : m_hThread( NULL), m_listener( NULL), m_stop( false),m_bNormalEnd(true) {}
	virtual ~CBaseThread() {}
private:
	static unsigned int __stdcall ThreadStartAddress(void* p)
	{
		CoInitialize( NULL);
		try
		{
			((CBaseThread*)p)->OnRunning();
		}
		catch(std::exception ex)
		{
			ERRLOG(ex.what());
		}
		if(hEvent)
			::SetEvent(hEvent);
		CoUninitialize();
		return 0;
	}
protected:
	virtual void OnRunning() = 0;
	void SetThreadResult(bool res) { m_bNormalEnd = res; }
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

	bool GetThreadResult() { return m_bNormalEnd; }

	bool Suspend() { return ::SuspendThread( m_hThread) != 0xffffffff; }

	bool Resume() { return ::ResumeThread( m_hThread) != 0xffffffff; }
};
