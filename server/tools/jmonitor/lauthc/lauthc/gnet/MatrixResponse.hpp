
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_MatrixResponse_.hpp"

namespace gnet { 

class MatrixResponse : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixResponse.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
