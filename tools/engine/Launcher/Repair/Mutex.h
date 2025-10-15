#pragma once
// 一些多线程保护类

namespace MUTEX
{

class CSemaphore
{
	enum { MAX_ITEMS = MAXLONG, };
	HANDLE		m_hSemaphore;

public:
	CSemaphore() : m_hSemaphore( CreateSemaphore( NULL, 0, MAX_ITEMS, NULL)) { assert( m_hSemaphore);}
	~CSemaphore() { ::CloseHandle( m_hSemaphore);}

	void AddRef() 
	{ 
		BOOL b = ReleaseSemaphore( m_hSemaphore, 1, NULL);
		assert( b);
	}
	void Wait()
	{
		WaitForSingleObject( m_hSemaphore, INFINITE);
	}
	void Clear()
	{
		while( WAIT_OBJECT_0 == WaitForSingleObject( m_hSemaphore, 0));
	}
	HANDLE GetHandle() { return m_hSemaphore; }
};

class CMutex
{
	CRITICAL_SECTION		m_cs;
public:
	class CScoped
	{
		CMutex&	m_mutex;
	public:
		CScoped( CMutex& m) : m_mutex( m) { m_mutex.Lock(); }
		~CScoped() { m_mutex.Unlock(); }
	};
public:
	CMutex() { InitializeCriticalSection( &m_cs); }
	~CMutex() { DeleteCriticalSection( &m_cs); }
public:
	void Lock() { EnterCriticalSection( &m_cs); }
	void Unlock() { LeaveCriticalSection( &m_cs); }
};

class CEvent
{
	HANDLE	m_hEvent;
public:
	CEvent( BOOL bManualReset) : m_hEvent( ::CreateEvent( NULL, bManualReset, FALSE, NULL)) { assert( m_hEvent); }
	~CEvent() { ::CloseHandle( m_hEvent); }
public:
	BOOL SetEvent() { return ::SetEvent( m_hEvent); }
	BOOL ResetEvent() { return ::ResetEvent( m_hEvent); }
	BOOL Wait() { return WAIT_OBJECT_0 == WaitForSingleObject( m_hEvent, INFINITE); }
	HANDLE GetHandle() { return m_hEvent; }
	BOOL IsBreak() { return WAIT_OBJECT_0 == WaitForSingleObject( m_hEvent, 0); }
};

};		// namespace MUTEX