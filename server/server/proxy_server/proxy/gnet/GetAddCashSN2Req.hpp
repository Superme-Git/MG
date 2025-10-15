
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetAddCashSN2Req_.hpp"

namespace gnet { 

class GetAddCashSN2Req : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetAddCashSN2Req.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
