#pragma once

#include "rpcgen.hpp"
#include "CrossServer.hpp"

namespace gdeliverd
{
	class CrossBroadcast : public GNET::Thread::Runnable
	{
		int update_time;
		CrossBroadcast(int _time,int _prior=1) : Runnable(_prior), update_time(_time) { }
	public:
		~CrossBroadcast() { }
		static CrossBroadcast* GetInstance(int _time=60,int _prior=1) { 
			static CrossBroadcast instance(_time,_prior); 
			return &instance; 
		}
		void Run()
		{
			CrossServer::GetInstance()->BroadcastCrossStatus();
			GNET::Thread::HouseKeeper::AddTimerTask(this,update_time);
		}
	};
};
