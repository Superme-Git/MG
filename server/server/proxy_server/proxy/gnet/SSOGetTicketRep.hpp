
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SSOGetTicketRep_.hpp"

namespace gnet { 

class SSOGetTicketRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SSOGetTicketRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ /* protocol handle */ 
	 	LOG_TRACE("user(id=%d) receive SSOGetTicketRep from au",user.userid);
		gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
          	gdeliverd::UserInfo* uinfo = uc->find(user.userid);
                if(NULL == uinfo)
                        return;
		gdeliverd::DeliverServer::GetInstance()->Send(uinfo->link_sid,this);
	}
};

} 
