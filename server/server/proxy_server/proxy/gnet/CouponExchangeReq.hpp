
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_CouponExchangeReq_.hpp"
#include "CouponExchange.hpp"
#include "CouponExchangeRep.hpp"

namespace gnet { 

class CouponExchangeReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/CouponExchangeReq.inc"

	virtual void Process(Manager *, Manager::Session::ID sid) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("user(id=%d) receive CouponExchangeReq from link",userid);
		int isabroad = gdeliverd::AuthClient::GetInstance()->GetAbroadPolicy();
		if(isabroad == 1)
			return;
		
		gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
		gdeliverd::UserInfo* uinfo = uc->find(userid);
		if(NULL == uinfo)
			return;		
		if(!gdeliverd::DeliverServer::GetInstance()->GetCouponExchangeLimit().add(userid))
		{
			gdeliverd::DeliverServer::GetInstance()->Send(sid,CouponExchangeRep (15,userid,0,0,0,0,0));
			GNET::Log::log(LOG_WARNING,"user(id=%d) CouponExchangeReq exceed limit!",userid);
			return;
		}

		int64_t timestamp = 1000 * time(NULL);
		CouponExchangeArg arg(userid,coupon_number,cash_number,timestamp,reserved1,reserved2);
		CouponExchange * rpc = (CouponExchange*)GNET::Rpc::Call(CouponExchange::PROTOCOL_TYPE,arg);
		rpc->link_sid = sid;
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) send CouponExchange to au failed!",userid);
			gdeliverd::DeliverServer::GetInstance()->Send(sid,CouponExchangeRep(8,userid,0,0,0,0,0));
		}

	}
};

} 
