#ifndef __FIRENET_THREAD_HPP__
#define __FIRENET_THREAD_HPP__

#pragma once

#include <set>
#include <process.h>
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <thread>
#endif
namespace aio
{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	class Thread
	{
	private:
		std::thread* m_pThread;
		volatile bool running;

		static unsigned __stdcall __threadmain(void * argList)
		{
			Thread * thread = (Thread*)argList;
			thread->Run();
			return 0;
		}

		Thread(const Thread&) {}
		void operator=(const Thread&) {}

	public:
		Thread()
		 : running(false)
		 , m_pThread(NULL)
		{
		}

		virtual ~Thread()
		{
			try
			{
				if (m_pThread)
				{
					if (m_pThread->joinable())
						m_pThread->join();
					delete m_pThread;
					m_pThread = NULL;
				}
			}
			catch (const FireNet::Marshal::Exception& e)
			{
				printf("%s\n", e.what.c_str());
				throw e;
			}
		}

		virtual bool IsRunningNow() { return running; }
		virtual void StopRunning() { running = false; }

		virtual void Start()
		{
			running = true;
			m_pThread = new std::thread(Thread::__threadmain, this);
		}

		virtual void Join()
		{
			if (m_pThread && m_pThread->joinable())
				m_pThread->join();
		}

		virtual void Run() = 0;
	};
#else
	class Thread
	{
	private:
		HANDLE thread;  // Ïß³Ì¾ä±ú

		volatile bool running;

		static unsigned __stdcall __threadmain(void * argList)
		{
			Thread * thread = (Thread*)argList;
			thread->Run();
			return 0;
		}

		Thread(const Thread&) {}
		void operator=(const Thread&) {}

	public:
		Thread() : running( false)
		{
			thread = (HANDLE)_beginthreadex(NULL, 0, __threadmain, this, CREATE_SUSPENDED, NULL);
		}

		virtual ~Thread() { CloseHandle(thread); }

		virtual bool IsRunningNow() { return running; }

		virtual void StopRunning() { running = false; }

		virtual void Start() { running = true; ResumeThread(thread); }

		virtual void Join() { WaitForSingleObject(thread, INFINITE); }

		virtual void Run() = 0;
	};
#endif

} // namespace aio

#endif
