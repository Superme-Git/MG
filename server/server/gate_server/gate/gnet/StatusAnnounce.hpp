
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class StatusAnnounce : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/StatusAnnounce.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
