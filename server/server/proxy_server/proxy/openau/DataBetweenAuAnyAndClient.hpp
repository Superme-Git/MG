
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/openau/_depends_DataBetweenAuAnyAndClient_.hpp"
#include "DeliverServer.hpp"
#include "AuAnyClient.hpp"
#include "netplayer.h"

namespace openau { 

class DataBetweenAuAnyAndClient : public GNET::Protocol
{
public:
	#include "rpcgen/openau/DataBetweenAuAnyAndClient.inc"

	virtual void Process(Manager *, Manager::Session::ID) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("DataBetweenAuAnyAndClient userid=%d,qtype=%d,flag=%d",userid,qtype,flag);
		if(flag == AuAny_TO_CLIENT)
		{
			gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
			gdeliverd::UserInfo* uinfo = uc->find(userid);
			if(NULL == uinfo)
				return;
			gdeliverd::DeliverServer::GetInstance()->Send(uinfo->link_sid,this);
		}
		else if(flag == CLIENT_TO_AuAny)
		{
			gdeliverd::AuAnyClient::GetInstance()->SendProtocol(this);
		}
	}

};

} 
