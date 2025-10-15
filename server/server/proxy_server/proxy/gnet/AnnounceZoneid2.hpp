
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_AnnounceZoneid2_.hpp"

namespace gnet { 

class AnnounceZoneid2 : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AnnounceZoneid2.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
