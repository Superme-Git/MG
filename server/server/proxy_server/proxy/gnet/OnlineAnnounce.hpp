
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class OnlineAnnounce : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/OnlineAnnounce.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		// handle
	}

};

} 
