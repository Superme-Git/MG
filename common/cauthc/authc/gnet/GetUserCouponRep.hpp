
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetUserCouponRep_.hpp"

namespace gnet { 

class GetUserCouponRep : public aio::Protocol
{
public:
	#include "rpcgen/gnet/GetUserCouponRep.inc"

	virtual void Process(Manager *, Manager::Session::ID );

};

} 
