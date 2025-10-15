//  thread.h
//  share

//import from cauthc 
#ifndef share_thread_h
#define share_thread_h
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <thread>
#else
#include <pthread.h>
#endif

namespace core
{
    class Thread
    {
    public:
		Thread();
        
		virtual ~Thread();
        
		virtual bool IsRunningNow();

		virtual void StopRunning();
        
		virtual void Start();
        
		virtual void Join();
        
        virtual void Run() = 0;

		static unsigned int m_iFireCounter;//当前线程唤醒了的次数
		static unsigned int m_iLimitFires;//每秒限制线程唤醒的次数

	private:
		static void * thread_start(void *arg);
		Thread(const Thread&);
		void operator=(const Thread&) {}

	private:
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		std::thread* thread;
#else
		pthread_t thread;
#endif
		volatile bool running;
	};

}//namespace core

#endif
