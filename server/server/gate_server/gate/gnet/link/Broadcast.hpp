
#pragma once

#include "rpcgen.hpp"

namespace gnet { 
namespace link { 

class Broadcast : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/link/Broadcast.inc"

	virtual void Process(Manager *, Manager::Session::ID ); // see LinkServer.cpp

};

} 
} 
