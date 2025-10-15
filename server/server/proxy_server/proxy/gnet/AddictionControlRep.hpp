
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_AddictionControlRep_.hpp"

namespace gnet { 

class AddictionControlRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AddictionControlRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
