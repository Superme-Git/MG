#pragma once

#include <log4cpp/Category.hh>
#include "rpcgen.hpp"
#include "AuthClient.hpp"
#include "DeliverServer.hpp"
#include "AuAnyClient.hpp"
#include "gnet/AuGameKeepAlive.hpp"

namespace gdeliverd
{
	class KeepAlive : public GNET::Thread::Runnable
	{
		static log4cpp::Category& log;
		int update_time;
		KeepAlive(int _time,int _prior=1) : Runnable(_prior), update_time(_time) { }
	public:
		~KeepAlive() { }
		static KeepAlive* GetInstance(int _time=60,int _prior=1) { 
			static KeepAlive instance(_time,_prior); 
			return &instance; 
		}
		void Run()
		{
			struct timeval now;
			gettimeofday(&now,NULL);
			int64_t code = now.tv_sec * 1000 + now.tv_usec / 1000;

			if(AuthClient::GetInstance()->bl_keepalive && AuthClient::GetInstance()->GetAbroadPolicy() == 0)
			{
				AuthClient::GetInstance()->SendProtocol(gnet::AuGameKeepAlive(code));
				log.debug("KeepAlive:Run:send AuGameKeepAlive to au ok,zoneid=%d,aid=%d",
						  DeliverServer::GetInstance()->GetZoneid(), DeliverServer::GetInstance()->GetAid());
			}
			
			if(AuAnyClient::GetInstance()->isConnected())
			{
				AuAnyClient::GetInstance()->SendProtocol(gnet::AuGameKeepAlive(code));
				log.debug("KeepAlive:Run:send AuGameKeepAlive to auany ok,serverid=%.*s",
						  AuAnyClient::GetInstance()->getServerId().size(), AuAnyClient::GetInstance()->getServerId().begin());
			}	

			GNET::Thread::HouseKeeper::AddTimerTask(this,update_time);
		}
	};
	log4cpp::Category& KeepAlive::log = log4cpp::Category::getInstance(std::string("gdeliverd"));
};
