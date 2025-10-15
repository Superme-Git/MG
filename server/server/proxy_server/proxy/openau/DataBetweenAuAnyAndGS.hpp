
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/openau/_depends_DataBetweenAuAnyAndGS_.hpp"
#include "GameDBServer.hpp"
#include "AuAnyClient.hpp"

namespace openau { 

class DataBetweenAuAnyAndGS : public GNET::Protocol
{
public:
	#include "rpcgen/openau/DataBetweenAuAnyAndGS.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("DataBetweenAuAnyAndGS userid=%d,qtype=%d,flag=%d",userid,qtype,flag);
		if(flag == AuAny_TO_GS)
		{
			gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
			dbs->Send(dbs->GetSid(),this);
		}
		else if(flag == GS_TO_AuAny)
		{
			gdeliverd::AuAnyClient::GetInstance()->SendProtocol(this);
		}
	}
};

} 
