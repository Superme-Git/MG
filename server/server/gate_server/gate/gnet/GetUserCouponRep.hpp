
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetUserCouponRep_.hpp"

namespace gnet { 

class GetUserCouponRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetUserCouponRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */
		LOG_TRACE("user(id=%d) receive GetUserCouponRep,retcode=%d",userid,retcode); 
		glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
		Manager::Session::ID localsid = lsm->GetSidByUserid(userid);
		if(localsid > 0)
		   lsm->Send(localsid,this);
	}
};
} 
