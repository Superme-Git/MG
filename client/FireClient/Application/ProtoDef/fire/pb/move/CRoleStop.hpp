
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/move/_depends_CRoleStop_.hpp"

namespace fire { 
namespace pb { 
namespace move { 

class CRoleStop : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/move/CRoleStop.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
