
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class ErrorInfo : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/ErrorInfo.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		// handle
	}

};

} 
