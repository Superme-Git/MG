
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/openau/_depends_RegistGameServerRequest_.hpp"

namespace openau { 

class RegistGameServerRequest : public GNET::Protocol
{
public:
	#include "rpcgen/openau/RegistGameServerRequest.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
