
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class CertVerify : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/CertVerify.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */ 
	}

};

} 
