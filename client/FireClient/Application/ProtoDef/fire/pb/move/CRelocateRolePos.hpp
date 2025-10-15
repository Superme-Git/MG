
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/move/_depends_CRelocateRolePos_.hpp"

namespace fire { 
namespace pb { 
namespace move { 

class CRelocateRolePos : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/move/CRelocateRolePos.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
