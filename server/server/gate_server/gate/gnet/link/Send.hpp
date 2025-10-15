
#pragma once

#include "rpcgen.hpp"

namespace gnet { 
namespace link { 

class Send : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/link/Send.inc"

	virtual void Process(Manager *, Manager::Session::ID );

};

} 
} 
