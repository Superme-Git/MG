
#pragma once

#include "rpcgen.hpp"

namespace gnet { 

class CertKey : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/CertKey.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
