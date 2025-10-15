
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_CouponExchangeReq_.hpp"
#include "DeliveryClient.hpp"

namespace gnet { 

class CouponExchangeReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/CouponExchangeReq.inc"

	virtual void Process(Manager *, Manager::Session::ID sid)
	{ 
		/* protocol handle */ 
		LOG_TRACE("receive CouponExchangeReq from client,sid=%d",sid);
		glinkd::LinkSession * psession = glinkd::LinkServer::GetInstance()->GetLinkSession(sid);
		if(NULL == psession || psession->getUserId() == 0)  
			return;
		this->userid = psession->getUserId();
		glinkd::DeliveryClient::GetInstance()->SendProtocol(this);
	}
};

} 
