#pragma once

#include "rpcgen.hpp"

namespace glinkd
{
	/* Check alivemap periodically, and remove timeout items
	*/  
	class CheckTimer : public GNET::Thread::Runnable
	{
		int update_time;
		public:
		CheckTimer(int _time,int _proir=1) : Runnable(_proir),update_time(_time) { }
		void CheckConnection();
		static CheckTimer* GetInstance(int _time=60,int _proir=1) {
			static CheckTimer instance(_time,_proir);
			return &instance;
		}	
		void Run()
		{
			CheckConnection();
			GNET::Thread::HouseKeeper::AddTimerTask(this,update_time);
		}
	};
}
