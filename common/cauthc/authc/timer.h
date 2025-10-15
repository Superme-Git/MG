#ifndef __FIRENET_TIMER_H__
#define __FIRENET_TIMER_H__

#include <vector>
#include <functional>
#include <algorithm>
#include <time.h>
#ifdef _WIN32
#include "os/windows/mutex.hpp"
#elif defined OS_IOS
#include "os/linux/mutex.hpp"
#endif

namespace FireNet
{
	class Timer
	{
	public:
		class Observer
		{
		public:
			virtual ~Observer() { }
			virtual void UpdateSelf() = 0;
		};
	private:
		static int64_t now;
		typedef std::list<Observer*> Observers;
		static Observers observers;
		static FireNet::Mutex	locker;
	private:
		Timer()
		{
		}
	public:
		static void Attach(Observer *o)
		{
			FireNet::Mutex::Scoped scoped( locker);
			observers.push_back(o);
		}
		static void Detach(Observer *o)
		{
			FireNet::Mutex::Scoped scoped( locker);
			observers.erase( std::remove(observers.begin(), observers.end(), o), observers.end());
		}
		static void UpdateSelf()
		{
			int64_t tmp = GetSystemTick();
			if (tmp > now)
			{
                FireNet::Mutex::Scoped scoped( locker);
				std::for_each(observers.begin(), observers.end(), std::mem_fun(&Observer::UpdateSelf));
				now = tmp;
			}
		}
		static time_t GetTime()
		{
			return now;
		}

		static int64_t GetSystemTick();
	};
};

#endif
