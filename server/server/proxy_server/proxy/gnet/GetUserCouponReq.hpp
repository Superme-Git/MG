
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetUserCouponReq_.hpp"
#include "GetUserCouponRep.hpp"
#include "GetUserCoupon.hpp"

namespace gnet { 

class GetUserCouponReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetUserCouponReq.inc"

	virtual void Process(Manager *, Manager::Session::ID sid) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("user(id=%d) receive GetUserCouponReq",userid);
		int isabroad = gdeliverd::AuthClient::GetInstance()->GetAbroadPolicy();
		if(isabroad == 1)
			return;

		gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
		gdeliverd::UserInfo* uinfo = uc->find(userid);
		if(NULL == uinfo)
			return;		

		if(!gdeliverd::DeliverServer::GetInstance()->GetCouponLimit().add(userid))
		{
			//don't send GetUserCouponRp to client
			GNET::Log::log(LOG_WARNING,"user(id=%d) GetUserCouponReq exceed limit!",userid);
			return;
		}

		GetUserCoupon * rpc = (GetUserCoupon*)GNET::Rpc::Call(GetUserCoupon::PROTOCOL_TYPE,GetUserCouponArg(userid));
		rpc->link_sid = sid;
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) send GetUserCoupon to au failed!",userid);
			gdeliverd::DeliverServer::GetInstance()->Send(sid,GetUserCouponRep(3,userid,0,0,0,0,0));
		}

	}
};

} 
