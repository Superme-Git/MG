
#pragma once

#include "rpcgen.hpp"

namespace gnet { 
namespace link { 

class SetLogin : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/link/SetLogin.inc"

	virtual void Process(Manager *, Manager::Session::ID ); // LinkServer.cpp

};

} 
} 
