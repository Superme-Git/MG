
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_CouponExchangeRep_.hpp"

namespace gnet { 

class CouponExchangeRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/CouponExchangeRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
