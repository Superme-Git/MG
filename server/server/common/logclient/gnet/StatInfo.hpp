
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_StatInfo_.hpp"

namespace gnet { 

class StatInfo : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/StatInfo.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
