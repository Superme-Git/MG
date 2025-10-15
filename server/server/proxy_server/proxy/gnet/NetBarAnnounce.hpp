
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_NetBarAnnounce_.hpp"

namespace gnet { 

class NetBarAnnounce : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/NetBarAnnounce.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
