
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_Challenge_.hpp"

namespace gnet { 

class Challenge : public aio::Protocol
{
public:
	#include "rpcgen/gnet/Challenge.inc"

	virtual void Process(Manager *, Manager::Session::ID ); // see FireNet.cpp

};

} 
