
#pragma once

#include "rpcgen.hpp"

namespace gnet { 
namespace link { 

class AnnounceLinkId : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/link/AnnounceLinkId.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
