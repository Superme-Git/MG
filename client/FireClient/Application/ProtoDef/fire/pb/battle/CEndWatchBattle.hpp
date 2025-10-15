
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/_depends_CEndWatchBattle_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 

class CEndWatchBattle : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/CEndWatchBattle.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
