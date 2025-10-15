
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_KeepAlive_.hpp"

namespace gnet { 

class KeepAlive : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/KeepAlive.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
