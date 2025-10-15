
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/openau/_depends_ServerIDResponse_.hpp"

namespace openau { 

class ServerIDResponse : public GNET::Protocol
{
public:
	#include "rpcgen/openau/ServerIDResponse.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
