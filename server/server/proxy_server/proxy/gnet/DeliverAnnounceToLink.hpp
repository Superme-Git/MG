
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_DeliverAnnounceToLink_.hpp"

namespace gnet { 

class DeliverAnnounceToLink : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/DeliverAnnounceToLink.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
