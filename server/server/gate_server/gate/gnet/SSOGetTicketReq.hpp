
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SSOGetTicketReq_.hpp"

namespace gnet { 

class SSOGetTicketReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SSOGetTicketReq.inc"

	virtual void Process(Manager *, Manager::Session::ID sid) 
	{ 
		/* protocol handle */ 
		glinkd::LinkSession *psession = glinkd::LinkServer::GetInstance()->GetLinkSession(sid);
                if(NULL == psession || psession->getUserId() == 0)
                        return;
		this->user.userid = psession->getUserId();
		glinkd::DeliveryClient::GetInstance()->SendProtocol(this);
	   	LOG_TRACE("user(id=%d) receive SSOGetTicketReq,sid=%d",psession->getUserId(),sid);
	}
};
} 
