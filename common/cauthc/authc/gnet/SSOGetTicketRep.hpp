
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/gnet/_depends_SSOGetTicketRep_.hpp"

namespace gnet { 

class SSOGetTicketRep : public aio::Protocol
{
public:
	#include "rpcgen/gnet/SSOGetTicketRep.inc"

	virtual void Process(Manager *, Manager::Session::ID );

};

} 
