#ifndef __Nuclear_TIMER_H
#define __Nuclear_TIMER_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{

class TimerOB
{
public:
	virtual ~TimerOB() { }
	virtual void OnTimer() = 0;

	static void OnSourceTimer();

protected:
	void AttachTimer();
	void DetachTimer();
};

};

#endif