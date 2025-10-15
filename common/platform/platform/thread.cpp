//  thread.cpp
//  share

#include "thread.h"

namespace core
{
	unsigned int Thread::m_iFireCounter = 0;
	unsigned int Thread::m_iLimitFires = 120;

	void * Thread::thread_start(void *arg)
    {
		Thread * thread = (Thread*)arg;
        thread->Run();
        return 0;
    }

	Thread::Thread() : thread(0), running(false)
	{
	}

	Thread::Thread(const Thread&)
	{
	}

	Thread::~Thread()
	{
	#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		if (thread) 
		{
			if (thread->joinable())
				thread->join();
			delete thread;
			thread = NULL;
		}
#endif
	}

	bool Thread::IsRunningNow()
	{
		return running;
	}

	void Thread::StopRunning()
	{
		running = false;
	}

	void Thread::Start()
	{
	#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		if (thread)
			return;

		thread = new std::thread(thread_start, this);
	#else
		pthread_create(&thread, NULL, thread_start, this);
	#endif
		running = true;
	}

	void Thread::Join()
	{
	#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		thread->join();
	#else
		void*	result;
		pthread_join(thread, &result);
	#endif
	}
}
