#pragma once
#include <assert.h>

namespace THREAD
{

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

		CMutex() { InitializeCriticalSection( &m_cs); }
		~CMutex() { DeleteCriticalSection( &m_cs); }

	public:
		void Lock() { EnterCriticalSection( &m_cs); }
		void Unlock() { LeaveCriticalSection( &m_cs); }
	};

	

	class CSemaphore
	{
		enum { MAX_ITEMS = MAXLONG, };
		HANDLE		m_hSemaphore;

	public:
		CSemaphore() : m_hSemaphore( CreateSemaphore( NULL, 0, MAX_ITEMS, NULL)) { }
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


	class CEvent
	{
		HANDLE	m_hEvent;
	public:
		CEvent( BOOL bManualReset) : m_hEvent( ::CreateEvent( NULL, bManualReset, FALSE, NULL)) { assert( m_hEvent); }
		~CEvent() { ::CloseHandle( m_hEvent); }
	public:
		void SetEvent() { ::SetEvent( m_hEvent); }
		bool Wait() { return WAIT_OBJECT_0 == WaitForSingleObject( m_hEvent, INFINITE); }
		HANDLE GetHandle() { return m_hEvent; }
		bool IsBreak() { return WAIT_OBJECT_0 == WaitForSingleObject( m_hEvent, 0); }
	};

};	// namespace THREAD