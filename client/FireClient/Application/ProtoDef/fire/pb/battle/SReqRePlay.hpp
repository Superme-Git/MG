
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/_depends_SReqRePlay_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 

class SReqRePlay : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/SReqRePlay.inc"

	virtual void Process(Manager *, Manager::Session::ID ) ; // see Module battle

};

} 
} 
} 
