
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetUserNickNameReq_.hpp"

namespace gnet { 

class GetUserNickNameReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetUserNickNameReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
