
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_KeyExchange_.hpp"

namespace gnet { 

class KeyExchange : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/KeyExchange.inc"

	virtual void Process(Manager *, Manager::Session::ID );// { /* protocol handle */ }

};

} 
