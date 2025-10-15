
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/skill/_depends_CUseSceneSkill_.hpp"

namespace fire { 
namespace pb { 
namespace skill { 

class CUseSceneSkill : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/skill/CUseSceneSkill.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
