#include "nutimer.h"
#include "nulog.h"

#include <algorithm>

namespace Nuclear
{
	static std::vector<NuclearTimer*> s_obs;
	
	void NuclearTimer::attachTimer()
	{
		s_obs.push_back(this);
	}
	
	void NuclearTimer::detachTimer()
	{
		s_obs.erase(std::remove(s_obs.begin(), s_obs.end(), this), s_obs.end());
	}	
	
	void NuclearTimer::OnSourceTimer()
	{
		for(std::vector<NuclearTimer*>::iterator it = s_obs.begin(), ie = s_obs.end(); it != ie ; ++it)
		{
			(*it)->OnTimer();
		}
	}
}