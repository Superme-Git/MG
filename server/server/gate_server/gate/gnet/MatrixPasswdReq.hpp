
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/MatrixPasswdArg.hpp"

namespace gnet { 

class MatrixPasswdReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixPasswdReq.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		// handle
	}

};

} 
