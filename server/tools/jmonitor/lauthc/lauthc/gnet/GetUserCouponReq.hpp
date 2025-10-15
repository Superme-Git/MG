
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetUserCouponReq_.hpp"

namespace gnet { 

class GetUserCouponReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetUserCouponReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
