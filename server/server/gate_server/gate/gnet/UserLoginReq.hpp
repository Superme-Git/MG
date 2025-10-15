
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/UserLoginArg.hpp"

namespace gnet { 

class UserLoginReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/UserLoginReq.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		// handle
	}

};

} 
