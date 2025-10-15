
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SSOGetTicketReq_.hpp"

namespace gnet { 

class SSOGetTicketReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SSOGetTicketReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ /* protocol handle */ 
	   	LOG_TRACE("user(id=%d) receive SSOGetTicketReq from link",user.userid);
		gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
                gdeliverd::UserInfo* uinfo = uc->find(user.userid);
                if(NULL == uinfo)
                        return;
		this->loginip = uinfo->loginip;
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(this))
		{
		   	GNET::Log::log(LOG_WARNING,"user(id=%d) send SSOGetTicketReq to au failed!",user.userid);
		}
	}
};

} 
