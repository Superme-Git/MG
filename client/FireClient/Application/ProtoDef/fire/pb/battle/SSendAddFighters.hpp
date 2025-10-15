
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/_depends_SSendAddFighters_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 

class SSendAddFighters : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/SSendAddFighters.inc"

	virtual void Process(Manager *, Manager::Session::ID ) ; // see Module battle

};

} 
} 
} 
