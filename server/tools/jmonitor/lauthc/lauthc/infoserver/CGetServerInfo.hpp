
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/infoserver/_depends_CGetServerInfo_.hpp"

namespace infoserver { 

class CGetServerInfo : public GNET::Protocol
{
public:
	#include "rpcgen/infoserver/CGetServerInfo.inc"

	virtual void Process(Manager *, Manager::Session::ID );// { /* protocol handle */ }

};

} 
