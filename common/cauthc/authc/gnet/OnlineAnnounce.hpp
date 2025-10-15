
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_OnlineAnnounce_.hpp"

namespace gnet { 

class OnlineAnnounce : public aio::Protocol
{
public:
	#include "rpcgen/gnet/OnlineAnnounce.inc"

	virtual void Process(Manager *, Manager::Session::ID ); // see authc.cpp

};

} 
