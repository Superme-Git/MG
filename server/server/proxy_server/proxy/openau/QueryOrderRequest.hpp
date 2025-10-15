
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/openau/_depends_QueryOrderRequest_.hpp"
#include "AuAnyClient.hpp"

namespace openau { 

class QueryOrderRequest : public GNET::Protocol
{
public:
	#include "rpcgen/openau/QueryOrderRequest.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"receive QueryOrderRequest");
		gdeliverd::AuAnyClient::GetInstance()->SendProtocol(this);
	}

};

} 
