//  semaphore.h
//  share

#ifndef share_semaphore_h
#define share_semaphore_h
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <mutex>
#include <condition_variable>
#else
#include <semaphore.h>
#endif

namespace core
{
    class CSemaphore
    {
        const char* m_pSemName;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		volatile int val;
		std::mutex mutex;
		std::condition_variable condition;
#else
        sem_t* m_sem;
#endif
    public:
        CSemaphore(const char* aName);
        ~CSemaphore();
        
        int     wait();
        void    fire();
    };
}

#endif
