
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/battle/_depends_SSendCameraUrl_.hpp"

namespace fire { 
namespace pb { 
namespace battle { 

class SSendCameraUrl : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/battle/SSendCameraUrl.inc"

	virtual void Process(Manager *, Manager::Session::ID ) ; // see Module battle

};

} 
} 
} 
