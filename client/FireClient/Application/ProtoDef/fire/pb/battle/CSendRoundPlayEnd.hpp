
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/_depends_CSendRoundPlayEnd_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 

class CSendRoundPlayEnd : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/CSendRoundPlayEnd.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
