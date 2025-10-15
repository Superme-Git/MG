
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/gnet/_depends_DataBetweenAuAnyAndClient_.hpp"

namespace gnet { 

class DataBetweenAuAnyAndClient : public aio::Protocol
{
public:
	#include "rpcgen/gnet/DataBetweenAuAnyAndClient.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
