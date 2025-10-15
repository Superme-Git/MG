
#pragma once

#include "rpcgen.hpp"

namespace gnet { 
namespace link { 

class Dispatch : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/link/Dispatch.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		// handle
	}

};

} 
} 
