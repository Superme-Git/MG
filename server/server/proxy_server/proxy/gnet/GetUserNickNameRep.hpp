
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetUserNickNameRep_.hpp"

namespace gnet { 

class GetUserNickNameRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetUserNickNameRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
