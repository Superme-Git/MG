
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_OnlineAnnounce_.hpp"

namespace gnet { 

class OnlineAnnounce : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/OnlineAnnounce.inc"

	virtual void Process(Manager *, Manager::Session::ID );// { /* protocol handle */ }

};

} 
