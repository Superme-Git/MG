
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_Response_.hpp"

namespace gnet { 

class Response : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/Response.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
