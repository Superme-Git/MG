
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/openau/_depends_QueryOrderResponse_.hpp"
#include "GameDBServer.hpp"

namespace openau { 

class QueryOrderResponse : public GNET::Protocol
{
public:
	#include "rpcgen/openau/QueryOrderResponse.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"QueryOrderReponse orderserialplat=%.*s,retcode=%d,restype=%d,userid=%d",orderserialplat.size(),orderserialplat.begin(),errorcode,restype,userid);
		gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
		dbs->Send(dbs->GetSid(),this);
	}
};

} 
