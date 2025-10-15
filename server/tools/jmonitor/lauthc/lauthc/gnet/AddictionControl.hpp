
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_AddictionControl_.hpp"

namespace gnet { 

class AddictionControl : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AddictionControl.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
