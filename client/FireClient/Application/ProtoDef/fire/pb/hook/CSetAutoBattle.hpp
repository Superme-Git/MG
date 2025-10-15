
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/hook/_depends_CSetAutoBattle_.hpp"

namespace fire { 
namespace pb { 
namespace hook { 

class CSetAutoBattle : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/hook/CSetAutoBattle.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
