
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_UserInfoRep_.hpp"

namespace gnet { 

class UserInfoRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/UserInfoRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
