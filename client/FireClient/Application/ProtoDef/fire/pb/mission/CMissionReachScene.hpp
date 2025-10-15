
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/mission/_depends_CMissionReachScene_.hpp"

namespace fire { 
namespace pb { 
namespace mission { 

class CMissionReachScene : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/mission/CMissionReachScene.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
