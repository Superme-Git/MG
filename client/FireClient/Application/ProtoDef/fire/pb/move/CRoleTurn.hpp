
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/move/_depends_CRoleTurn_.hpp"

namespace fire { 
namespace pb { 
namespace move { 

class CRoleTurn : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/move/CRoleTurn.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
