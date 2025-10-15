
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_InstantAddCashReq_.hpp"

namespace gnet { 

class InstantAddCashReq : public aio::Protocol
{
public:
	#include "rpcgen/gnet/InstantAddCashReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
