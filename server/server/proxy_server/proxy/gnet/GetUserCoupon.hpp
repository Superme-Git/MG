
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetUserCoupon_.hpp"
#include "GetUserCouponRep.hpp"

namespace gnet { 

class GetUserCoupon : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/GetUserCoupon.inc"

	int link_sid;
	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::GetUserCouponArg* arg = getArgument();
		//gnet::GetUserCouponRes* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		gnet::GetUserCouponArg* arg = getArgument();
		gnet::GetUserCouponRes* res = getResult();
		GNET::Log::log(LOG_INFO,"user(id=%d) GetUserCoupon,coupon=(%d,%d,%d),retcode=%d",arg->userid,res->remain_coupon,res->today_exchange_coupon,res->today_remain_exchange_coupon,res->retcode);
		gdeliverd::DeliverServer::GetInstance()->Send(link_sid,GetUserCouponRep(res->retcode,arg->userid,res->remain_coupon,res->today_exchange_coupon,res->today_remain_exchange_coupon,res->reserved1,res->reserved2));
	}

	void OnTimeout()
	{
		// client only
		gnet::GetUserCouponArg* arg = getArgument();
		GNET::Log::log(LOG_WARNING,"user(id=%d) GetUserCoupon timeout,retcode=4",arg->userid);
		gdeliverd::DeliverServer::GetInstance()->Send(link_sid,GetUserCouponRep(4,arg->userid,0,0,0,0,0));
	}
};

} 
