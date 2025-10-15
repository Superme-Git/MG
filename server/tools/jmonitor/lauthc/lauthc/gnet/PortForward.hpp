
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_PortForward_.hpp"

namespace gnet { 

class PortForward : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/PortForward.inc"

	virtual void Process(Manager *, Manager::Session::ID ); // { /* protocol handle */ }

};

} 
