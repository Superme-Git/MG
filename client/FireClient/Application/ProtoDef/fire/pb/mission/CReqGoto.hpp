
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/mission/_depends_CReqGoto_.hpp"

namespace fire { 
namespace pb { 
namespace mission { 

class CReqGoto : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/mission/CReqGoto.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
