
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_CouponExchangeReq_.hpp"

namespace gnet { 

class CouponExchangeReq : public aio::Protocol
{
public:
	#include "rpcgen/gnet/CouponExchangeReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
