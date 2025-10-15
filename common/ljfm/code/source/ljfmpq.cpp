#ifdef _MSC_VER
#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
#include "../include/common.h"
#include "../include/ljfmpq.h"

namespace LJFMQ
{
	namespace helper
	{

		bool CHandle::Wait()
		{
			DWORD dwResult = ::WaitForSingleObject( GetHandle(), INFINITE);
			return WAIT_OBJECT_0 == dwResult;
		}

		unsigned int CThread::Create()
		{
			DWORD dwThreadID = 0;
			HANDLE hThread = ::CreateThread( NULL, 0, ThreadProc, this, 0, &dwThreadID);
			if( NULL == hThread)
			{
				return ::GetLastError();
			}

			Attach( hThread);
			return 0;
		}

		unsigned long __stdcall CThread::ThreadProc( void* data)
		{
			((CThread*)data)->OnRunning();
			return -1;
		}

		unsigned int CEvent::Create( bool signaled)
		{
			HANDLE hEvent = ::CreateEvent( NULL, FALSE, signaled ? TRUE : FALSE, NULL);
			if( NULL == hEvent)
			{
				return ::GetLastError();
			}

			Attach( hEvent);
			return 0;
		}

		bool CEvent::TriggerEvent()
		{
			return TRUE == ::SetEvent( GetHandle());
		}

		unsigned int CSemaphore::Create( int maxcount)
		{
			HANDLE hSemaphore = ::CreateSemaphore( NULL, 0, maxcount, NULL);
			if( NULL == hSemaphore)
			{
				return ::GetLastError();
			}

			Attach( hSemaphore);
			return 0;
		}

		bool CSemaphore::Increase()
		{	
			return TRUE == ::ReleaseSemaphore( GetHandle(), 1, NULL);	
		}
	}
}
#endif
#endif
