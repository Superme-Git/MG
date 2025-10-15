
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/gnet/_depends_SSOGetTicketReq_.hpp"

namespace gnet { 

class SSOGetTicketReq : public aio::Protocol
{
public:
	#include "rpcgen/gnet/SSOGetTicketReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
