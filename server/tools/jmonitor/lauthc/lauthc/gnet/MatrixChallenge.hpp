
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_MatrixChallenge_.hpp"

namespace gnet { 

class MatrixChallenge : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixChallenge.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
