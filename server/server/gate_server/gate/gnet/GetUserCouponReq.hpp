
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetUserCouponReq_.hpp"

namespace gnet { 

class GetUserCouponReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetUserCouponReq.inc"

	virtual void Process(Manager *, Manager::Session::ID sid)
	{ 
		/* protocol handle */ 
		glinkd::LinkSession *psession = glinkd::LinkServer::GetInstance()->GetLinkSession(sid);
		if(NULL == psession || psession->getUserId() == 0)  
			return;
		this->userid = psession->getUserId();
		glinkd::DeliveryClient::GetInstance()->SendProtocol(this);
		LOG_TRACE("user(id=%d) receive GetUserCouponReq,sid=%d",psession->getUserId(),sid);
	}
};

} 
