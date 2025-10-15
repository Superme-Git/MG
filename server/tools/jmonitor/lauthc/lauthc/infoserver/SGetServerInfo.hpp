
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/infoserver/_depends_SGetServerInfo_.hpp"

namespace infoserver { 

class SGetServerInfo : public GNET::Protocol
{
public:
	#include "rpcgen/infoserver/SGetServerInfo.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
