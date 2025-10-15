
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/cross/_depends_BroadcastData_.hpp"
#include "CrossServer.hpp"
#include "GameDBServer.hpp"
#include "CrossClient.hpp"

namespace cross { 

class BroadcastData : public GNET::Protocol
{
public:
	#include "rpcgen/cross/BroadcastData.inc"

	virtual void Process(Manager * manager, Manager::Session::ID sid) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("receive BroadcastData");
#ifdef CROSS_SERVER
		if(manager == gdeliverd::GameDBServer::GetInstance())
			gdeliverd::CrossServer::GetInstance()->BroadcastCross(this);
		if(manager == gdeliverd::CrossServer::GetInstance())
		{
			gdeliverd::GameDBServer *gsm = gdeliverd::GameDBServer::GetInstance();
			gsm->Send(gsm->GetSid(),this);
		}
#else
		if(manager == gdeliverd::GameDBServer::GetInstance())
			gdeliverd::CrossClient::GetInstance()->SendProtocol(this);
		if(manager == gdeliverd::CrossClient::GetInstance())
		{
			gdeliverd::GameDBServer *gsm = gdeliverd::GameDBServer::GetInstance();
			gsm->Send(gsm->GetSid(),this);
		}
#endif
	}

};

} 
