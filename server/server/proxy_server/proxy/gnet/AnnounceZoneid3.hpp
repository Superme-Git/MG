
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_AnnounceZoneid3_.hpp"

namespace gnet { 

class AnnounceZoneid3 : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AnnounceZoneid3.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
