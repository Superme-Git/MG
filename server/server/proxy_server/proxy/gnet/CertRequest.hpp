
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class CertRequest : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/CertRequest.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
