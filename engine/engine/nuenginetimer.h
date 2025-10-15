//
//  timer.h
//  engine

#ifndef __Nuclear_timer_H__
#define __Nuclear_timer_H__

#include <map>
#include <list>
#include <vector>

typedef long long tick_t; //change from unsinged int to int64_t. by liugeng
typedef unsigned int TimerHandle;
typedef void (*TIMER_CALLBACK)(void*);
const TimerHandle INVALIDATE_TIMER = 0;
typedef unsigned int ThreadHandle;
ThreadHandle GetCurThread();

class TimerManager
{
    struct sTimer
	{
		TimerHandle	m_handle;
		TIMER_CALLBACK	m_pCallBack;
		tick_t			m_Elapse;
		tick_t			m_NextTick;
        void*           m_pArg;
		char			m_bCycle;
	};
    
#if defined DEBUG || defined _DEBUG
	ThreadHandle m_hThread;
#endif
    
	typedef std::list<sTimer*> TimerList;
	typedef std::map<tick_t, TimerList> TimeQueue;
	typedef std::map<TimerHandle, sTimer*> TimerMap;
public:
	TimerManager();
	~TimerManager();
    
	void		Tick();
	
	void		ResetTimer(TimerHandle aHTimer);
	TimerHandle SetTimer(unsigned int aElapse, TIMER_CALLBACK aCallBack, void* aArg, bool aBCycle = true);
	bool		KillTimer(TimerHandle aHTimer);

private:
	void		callTimerList(TimerList& aTimerList);

protected:
	void		destroy();

private:
	TimeQueue	m_TimeQueue;
	TimerMap	m_timers;
	tick_t		m_lastTick;
	unsigned int m_timerID;
};

#endif
