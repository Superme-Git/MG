
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_StatInfoVital_.hpp"

namespace gnet { 

class StatInfoVital : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/StatInfoVital.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
