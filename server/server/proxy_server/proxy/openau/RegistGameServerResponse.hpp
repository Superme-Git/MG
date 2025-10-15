
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/openau/_depends_RegistGameServerResponse_.hpp"
#include "AuAnyClient.hpp"
#include "DeliverServer.hpp"
#include "ServerIDResponse.hpp"
#include "GameDBServer.hpp"

namespace openau { 

class RegistGameServerResponse : public GNET::Protocol
{
public:
	#include "rpcgen/openau/RegistGameServerResponse.inc"

	virtual void Process(Manager * manager, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		gdeliverd::AuAnyClient::GetInstance()->setPlatType(plattype);
		gdeliverd::AuAnyClient::log.info("RegistGameServerResponse:Process:plattype=%d",plattype);
		if(plattype > 0)
		{
			ServerIDResponse response;
			response.plattype = gdeliverd::AuAnyClient::GetInstance()->getPlatType();
			response.serverid = gdeliverd::AuAnyClient::GetInstance()->getServerId();
			gdeliverd::DeliverServer::GetInstance()->BroadcastLink(response);
		
			gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
			dbs->Send(dbs->GetSid(),response);
		}
	}

};

} 
