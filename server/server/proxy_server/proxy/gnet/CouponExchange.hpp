
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_CouponExchange_.hpp"
#include "CouponExchangeRep.hpp"

namespace gnet { 

class CouponExchange : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/CouponExchange.inc"

	int link_sid;
	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::CouponExchangeArg* arg = getArgument();
		//gnet::CouponExchangeRes* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		gnet::CouponExchangeArg* arg = getArgument();
		gnet::CouponExchangeRes* res = getResult();
		GNET::Log::log(LOG_INFO,"user(id=%d) CouponExchange,coupon=(%d,%d,%d),retcode=%d",arg->userid,res->remain_coupon,res->today_exchange_coupon,res->today_remain_exchange_coupon,res->retcode);
		gdeliverd::DeliverServer::GetInstance()->Send(link_sid,CouponExchangeRep(res->retcode,arg->userid,res->remain_coupon,res->today_exchange_coupon,res->today_remain_exchange_coupon,res->reserved1,res->reserved2));
	}

	void OnTimeout()
	{
		// client only
		gnet::CouponExchangeArg* arg = getArgument();
		GNET::Log::log(LOG_WARNING,"user(id=%d) CouponExchange timeout,retcode=9",arg->userid);
		gdeliverd::DeliverServer::GetInstance()->Send(link_sid,CouponExchangeRep(9,arg->userid,0,0,0,0,0));
	}
};

} 
