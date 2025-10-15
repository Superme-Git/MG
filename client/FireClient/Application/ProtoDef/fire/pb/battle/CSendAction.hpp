
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/_depends_CSendAction_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 

class CSendAction : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/CSendAction.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
