
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/newhand/_depends_CReqNewHandBattle_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 
namespace newhand { 

class CReqNewHandBattle : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/newhand/CReqNewHandBattle.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
} 
