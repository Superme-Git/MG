
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_InstantAddCashReq_.hpp"
#include "LinkServer.hpp"
#include "DeliveryClient.hpp"
#include "InstantAddCashRep.hpp"

namespace gnet { 

class InstantAddCashReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/InstantAddCashReq.inc"

	virtual void Process(Manager *manager, Manager::Session::ID sid) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("receive InstantAddCashReq from client,sid=%d",sid);
		glinkd::LinkSession *psession = glinkd::LinkServer::GetInstance()->GetLinkSession(sid);
		if(NULL == psession || psession->getUserId() == 0)  
			return;
		this->userid = psession->getUserId();
		this->localsid = sid;
		glinkd::DeliveryClient::GetInstance()->SendProtocol(this);
	}
};
} 
