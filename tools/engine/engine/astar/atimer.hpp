
#pragma once

#ifdef WIN32
#include <windows.h>  // for GetTickCount
#ifdef _MMSYSTEM_timeGetTime_
#include <mmsystem.h> // for timeGetTime
#endif
#else
#include <sys/time.h> // for gettimeofday
#endif

#include "asingleton.hpp"

namespace astar
{

#ifdef WIN32
class Timer
{
	int ticks;
public:
#ifdef _MMSYSTEM_timeGetTime_
	void reset()       { this->ticks = ::timeGetTime(); }
	int elapse() const { return ::timeGetTime() - this->ticks; }
#else
	void reset()       { this->ticks = ::GetTickCount(); }
	int elapse() const { return ::GetTickCount() - this->ticks; }
#endif

	Timer() { this->reset(); }

	// 程序启动以来的时间，毫秒
	static int tickcount() { return singleton_default<Timer>::instance().elapse(); }
};

#else

class Timer
{
	struct timeval timeofday;
public:
	void reset()
	{
		::gettimeofday(&this->timeofday, NULL);
	}

	int elapse() const
	{
		timeval now;
		::gettimeofday(&now, NULL);
		return (now.tv_sec - this->timeofday.tv_sec) * 1000 + (now.tv_usec - this->timeofday.tv_usec) / 1000;
	}

	Timer() { this->reset(); }

	// 程序启动以来的时间，毫秒
	static int tickcount() { return singleton_default<Timer>::instance().elapse(); }
};
#endif // WIN32

} // namespace astar
