
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/circletask/_depends_CReqGotoPatrol_.hpp"

namespace fire { 
namespace pb { 
namespace circletask { 

class CReqGotoPatrol : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/circletask/CReqGotoPatrol.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
