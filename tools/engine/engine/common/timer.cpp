#include "stdafx.h"
#include "timer.h"
#include "log.h"

#include <algorithm>

namespace Nuclear
{

// todo
static std::vector<TimerOB*> s_obs;

void TimerOB::AttachTimer()
{
	s_obs.push_back(this);
}

void TimerOB::DetachTimer()
{
	s_obs.erase(std::remove(s_obs.begin(), s_obs.end(), this), s_obs.end());
}


void TimerOB::OnSourceTimer()
{
	for(std::vector<TimerOB*>::iterator it = s_obs.begin(), ie = s_obs.end(); it != ie ; ++it)
	{
		(*it)->OnTimer();
	}
}

}