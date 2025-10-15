
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/_depends_SRemoveWatcher_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 

class SRemoveWatcher : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/SRemoveWatcher.inc"

	virtual void Process(Manager *, Manager::Session::ID ) ; // see Module battle

};

} 
} 
} 
