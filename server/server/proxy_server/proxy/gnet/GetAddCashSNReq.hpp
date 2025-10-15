
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class GetAddCashSNReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetAddCashSNReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
