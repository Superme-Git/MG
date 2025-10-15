
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/UserLoginRes.hpp"

namespace gnet { 

class UserLoginRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/UserLoginRep.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		// handle
	}

};

} 
