
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_CouponExchangeRep_.hpp"

namespace gnet { 

class CouponExchangeRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/CouponExchangeRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */
		LOG_TRACE("user(id=%d) receive CouponExchangeRep,retcode=%d",userid,retcode); 
		glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
		Manager::Session::ID localsid = lsm->GetSidByUserid(userid);
		if(localsid > 0)
		   lsm->Send(localsid,this);
	}
};

} 
