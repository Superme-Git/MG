#ifndef __TIMER_H
#define __TIMER_H

#include <vector>
#include <functional>
#include <algorithm>
#include <time.h>

namespace GNET
{

class Timer
{
public:
	class Observer
	{
	public:
		virtual ~Observer() { }
		virtual void Update() = 0;
	};
private:
	static time_t now;
	typedef std::vector<Observer*> Observers;
	static Observers& observers() { static Observers tmp; return tmp; }
	time_t t;
public:
	Timer() : t(now) { if(!now) now = t = time(NULL); }

	static void Attach(Observer *o) { observers().push_back(o); }
	static void Detach(Observer *o) { observers().erase( std::remove(observers().begin(), observers().end(), o), observers().end()); }
	static void Update() 
	{
		time_t tmp = time(NULL);
		if (tmp > now)
		{
			std::for_each(observers().begin(), observers().end(), std::mem_fun(&Observer::Update));	
			now = tmp;
		}
	}
	static time_t GetTime() { return now; }
	int Elapse() const { return (int)(now - t); }
	void Reset() { t = now; }
};

};

#endif
