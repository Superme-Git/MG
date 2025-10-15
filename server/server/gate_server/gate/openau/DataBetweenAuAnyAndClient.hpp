
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/openau/_depends_DataBetweenAuAnyAndClient_.hpp"
#include "LinkServer.hpp"
#include "DeliveryClient.hpp"

namespace openau { 

class DataBetweenAuAnyAndClient : public GNET::Protocol
{
public:
	#include "rpcgen/openau/DataBetweenAuAnyAndClient.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("DataBetweenAuAnyAndClient userid=%d,qtype=%d,flag=%d",userid,qtype,flag);
		if(flag == AuAny_TO_CLIENT)
		{
			glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
			Manager::Session::ID localsid = lsm->GetSidByUserid(userid);
			if(localsid > 0)
				lsm->Send(localsid,this);
		}
		else if(flag == CLIENT_TO_AuAny)
		{
			glinkd::DeliveryClient::GetInstance()->SendProtocol(this);
		}
	}		

};

} 
