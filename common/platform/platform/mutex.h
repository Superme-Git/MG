//  mutex.h
//  share

#ifndef share_mutex_h
#define share_mutex_h
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <mutex>
#else
#include <pthread.h>
#endif
namespace core
{
    
	class CMutex
	{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		std::mutex mutex;
#else
		pthread_mutex_t mutex;
#endif
        
		CMutex(const CMutex&);
		void operator=(const CMutex&);
	public:
		CMutex()
		{
#if !((defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP))
            pthread_mutex_init(&mutex, NULL);
#endif
		}
		~CMutex()
		{
#if !((defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP))
			pthread_mutex_destroy(&mutex);
#endif
		}
        
		void Lock()
		{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
			mutex.lock();
#else
			pthread_mutex_lock(&mutex);
#endif
		}
		void UNLock()
		{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
			mutex.unlock();
#else
			pthread_mutex_unlock(&mutex);
#endif
		}
        
		class Scoped
		{
			CMutex & mm;
			Scoped(const Scoped&);
			void operator=(const Scoped&);
		public:
			explicit Scoped(CMutex& m) : mm(m) { mm.Lock(); }
			explicit Scoped(CMutex* m) : mm(*m) { mm.Lock(); }
			~Scoped() { mm.UNLock(); }
		};
	};
} // namespace core

#endif
