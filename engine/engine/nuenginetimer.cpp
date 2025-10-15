//
//  timer.cpp
//  engine
//

#include "nuenginetimer.h"
#include "../common/nuutil.h"

ThreadHandle GetCurThread()
{
	return 0;
}

TimerManager::TimerManager()
	: m_lastTick(0)
	, m_timerID(1)
{
	destroy();

#if defined DEBUG || defined _DEBUG
	m_hThread = GetCurThread();
#endif
}

TimerManager::~TimerManager()
{
	destroy();
}

void TimerManager::destroy()
{
	TimerMap::iterator i = m_timers.begin();
	for (; i != m_timers.end(); ++i)
	{
		delete i->second;
	}
	m_timers.clear();
	m_TimeQueue.clear();
}

void TimerManager::ResetTimer(TimerHandle aHTimer)
{
	TimerMap::iterator iTimer = m_timers.find(aHTimer);
	if (iTimer == m_timers.end())
		return;

	sTimer* pTimer = iTimer->second;
	tick_t now = Nuclear::GetMilliSeconds();
	tick_t tick = 0;
	if (0xFFFFFFFF - now < pTimer->m_Elapse)
	{
		tick = pTimer->m_Elapse - (0xFFFFFFFF - now);
	}
	else
	{
		tick = now + pTimer->m_Elapse;
	}

	pTimer->m_NextTick = tick;
	m_TimeQueue[tick].push_back(pTimer);
}

TimerHandle TimerManager::SetTimer(unsigned int aElapse, TIMER_CALLBACK aCallBack, void* aArg, bool aBCycle /*= true*/)
{
	assert(aCallBack);
    
	sTimer* pTimer = new sTimer;
	tick_t now = Nuclear::GetMilliSeconds();
	tick_t tick = 0;
	if (0xFFFFFFFF - now < aElapse)
	{
		tick = aElapse - (0xFFFFFFFF - now);
	}
	else
	{
		tick = now + aElapse;
	}
    
	pTimer->m_handle = m_timerID++;
	pTimer->m_pCallBack = aCallBack;
	pTimer->m_Elapse = aElapse;
	pTimer->m_NextTick = tick;
	pTimer->m_bCycle = aBCycle?1:0;
    pTimer->m_pArg = aArg;
    
	m_TimeQueue[tick].push_back(pTimer);
	m_timers[pTimer->m_handle] = pTimer;
    
	return pTimer->m_handle;
}

bool TimerManager::KillTimer(TimerHandle aHTimer)
{
	bool bRet = false;
	TimerMap::iterator iTimer = m_timers.find(aHTimer);
    
    if (iTimer == m_timers.end()) return false;
    
	sTimer* pTimer = iTimer->second;
	m_timers.erase(aHTimer);
    
	TimeQueue::iterator iTick = m_TimeQueue.find(pTimer->m_NextTick);
	assert(iTick != m_TimeQueue.end());
    
	TimerList &tl = iTick->second;
	if (!tl.empty())
	{
		TimerList::iterator i = tl.begin();
		while( i != tl.end() )
		{
			if ((*i) != NULL && (*i)->m_handle == aHTimer)
			{
				*i = NULL;
				bRet = true;
				break;
			}
            ++i;
		}
	}
	delete pTimer;
	return true;
}

void TimerManager::callTimerList(TimerList& aTimerList)
{
	TimerList::iterator i = aTimerList.begin();
	while(i != aTimerList.end())
	{
		if (*i)
		{
			sTimer timer = *(*i);
			timer.m_pCallBack(timer.m_pArg);
            
			if (timer.m_bCycle != 0)
			{
				ResetTimer(timer.m_handle);
			}
			else
			{
				KillTimer(timer.m_handle);
			}
		}
		
		++i;
	}
}

void TimerManager::Tick()
{
	tick_t now = Nuclear::GetMilliSeconds();
	TimeQueue::iterator itLast = m_TimeQueue.lower_bound(m_lastTick);
	
	if (now < m_lastTick)//∑¢…˙“Á≥ˆ
	{
		while (itLast != m_TimeQueue.end())
		{
			callTimerList(itLast->second);
			//itLast = m_TimeQueue.erase(itLast);
            m_TimeQueue.erase(itLast++);
		}
        
		TimeQueue::iterator cur = m_TimeQueue.begin();
		while(cur->first <= now && cur != m_TimeQueue.end())
		{
			callTimerList(cur->second);
			//cur = m_TimeQueue.erase(cur);
            m_TimeQueue.erase(cur++);
		}
	}
	else
	{
		//assert(itLast == m_TimeQueue.begin());
		while(itLast != m_TimeQueue.end() && itLast->first <= now)
		{
			callTimerList(itLast->second);
			//itLast = m_TimeQueue.erase(itLast);
            m_TimeQueue.erase(itLast++);
		}
	}
    
	m_lastTick = now;
}