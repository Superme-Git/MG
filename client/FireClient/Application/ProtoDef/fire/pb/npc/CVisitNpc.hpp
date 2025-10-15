
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/npc/_depends_CVisitNpc_.hpp"

namespace fire { 
namespace pb { 
namespace npc { 

class CVisitNpc : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/npc/CVisitNpc.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
