
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/move/_depends_CCheckMove_.hpp"

namespace fire { 
namespace pb { 
namespace move { 

class CCheckMove : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/move/CCheckMove.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
