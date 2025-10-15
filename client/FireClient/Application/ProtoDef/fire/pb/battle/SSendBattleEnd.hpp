
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/_depends_SSendBattleEnd_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 

class SSendBattleEnd : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/SSendBattleEnd.inc"

	virtual void Process(Manager *, Manager::Session::ID ) ; // see Module battle

};

} 
} 
} 
