
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_InstantAddCashRep_.hpp"

namespace gnet { 

class InstantAddCashRep : public aio::Protocol
{
public:
	#include "rpcgen/gnet/InstantAddCashRep.inc"

	virtual void Process(Manager *, Manager::Session::ID );// { /* protocol handle */ }

};

} 
