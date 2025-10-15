
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class MatrixResponseRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixResponseRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
