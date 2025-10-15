
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class GMKickoutUser_Re : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GMKickoutUser_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
