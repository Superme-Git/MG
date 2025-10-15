
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class ACAnswer2 : public aio::Protocol
{
public:
	#include "ACAnswer2.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
