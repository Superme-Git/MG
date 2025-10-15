
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class MatrixFailure : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixFailure.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		/* protocol handle */ 
			
	}

};

} 
