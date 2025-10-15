
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class GMShutup_Re : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GMShutup_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
