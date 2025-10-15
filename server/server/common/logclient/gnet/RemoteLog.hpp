
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_RemoteLog_.hpp"

namespace gnet { 

class RemoteLog : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/RemoteLog.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
