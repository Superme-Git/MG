
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_ForceLoginReq_.hpp"

namespace gnet { 

class ForceLoginReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/ForceLoginReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
