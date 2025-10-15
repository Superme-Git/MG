
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetUserCouponRep_.hpp"

namespace gnet { 
/*
 * 0.success
 * 1.userid not exist		
 * 2.user be forbid
 * 3.connmunication error
 * 4.rpc timeout	
 */
class GetUserCouponRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetUserCouponRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
