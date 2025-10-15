#include "win32/share.hpp"

namespace FireNet
{
	int64_t Timer::GetSystemTick()
	{
#ifdef OS_WINDOWS
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		return ::GetTickCount64();
#else
		return ::GetTickCount();
#endif
#elif defined OS_LINUX
		struct timeval now_tv;
		gettimeofday( &now_tv,NULL );
		return now_tv.tv_sec * 1000 + now_tv.tv_usec / 1000;
#elif defined OS_ANDROID
        struct timeval now_tv;
		gettimeofday( &now_tv,NULL );
		return now_tv.tv_sec * 1000 + now_tv.tv_usec / 1000;
#elif defined OS_IOS
        struct timeval now_tv;
		gettimeofday( &now_tv,NULL );
		return now_tv.tv_sec * 1000 + now_tv.tv_usec / 1000;
#endif
	}


	int64_t Timer::now = Timer::GetSystemTick();
	Timer::Observers Timer::observers;
	FireNet::Mutex	Timer::locker;
};
