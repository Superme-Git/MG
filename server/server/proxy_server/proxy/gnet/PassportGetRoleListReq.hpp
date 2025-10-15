
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_PassportGetRoleListReq_.hpp"

namespace gnet { 

class PassportGetRoleListReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/PassportGetRoleListReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
