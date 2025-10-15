
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/_depends_CEndPlayCG_.hpp"

namespace fire { 
namespace pb { 

class CEndPlayCG : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/CEndPlayCG.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
