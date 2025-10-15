
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/move/_depends_CRoleMove_.hpp"

namespace fire { 
namespace pb { 
namespace move { 

class CRoleMove : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/move/CRoleMove.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
