
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/_depends_SSendRoundStart_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 

class SSendRoundStart : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/SSendRoundStart.inc"

	virtual void Process(Manager *, Manager::Session::ID ) ; // see Module battle

};

} 
} 
} 
