
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_ErrorInfo_.hpp"

namespace gnet { 

class ErrorInfo : public aio::Protocol
{
public:
	#include "rpcgen/gnet/ErrorInfo.inc"

	virtual void Process(Manager *, Manager::Session::ID );  // see authc.cpp

};

} 
