
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/_depends_CStopRePlay_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 

class CStopRePlay : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/CStopRePlay.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
