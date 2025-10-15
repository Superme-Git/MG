
#pragma once

#include "rpcgen.hpp"

namespace gnet { 
namespace link { 

class LinkBroken : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/link/LinkBroken.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		// handle
	}

};

} 
} 
