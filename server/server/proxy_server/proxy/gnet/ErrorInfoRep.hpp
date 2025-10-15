
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class ErrorInfoRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/ErrorInfoRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
