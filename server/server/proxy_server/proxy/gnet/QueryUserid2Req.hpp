
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_QueryUserid2Req_.hpp"

namespace gnet { 

class QueryUserid2Req : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/QueryUserid2Req.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
