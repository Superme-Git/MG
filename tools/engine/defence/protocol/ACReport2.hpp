
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class ACReport2 : public aio::Protocol
{
public:
	#include "ACReport2.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
