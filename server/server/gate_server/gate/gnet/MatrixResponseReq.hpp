
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class MatrixResponseReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixResponseReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
