
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SetUserNickNameRep_.hpp"

namespace gnet { 

class SetUserNickNameRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SetUserNickNameRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
