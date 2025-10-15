
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_Challenge_.hpp"

namespace gnet { 

class Challenge : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/Challenge.inc"

	virtual void Process(Manager *, Manager::Session::ID );// { /* protocol handle */ }

};

} 
