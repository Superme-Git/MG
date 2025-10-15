
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_AddictionControl_.hpp"

namespace gnet { 

class AddictionControl : public aio::Protocol
{
public:
	#include "rpcgen/gnet/AddictionControl.inc"

	virtual void Process(Manager *, Manager::Session::ID); // see FireNet.cpp

};

} 
