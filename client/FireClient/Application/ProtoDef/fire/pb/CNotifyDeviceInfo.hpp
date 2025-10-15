
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/_depends_CNotifyDeviceInfo_.hpp"

namespace fire { 
namespace pb { 

class CNotifyDeviceInfo : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/CNotifyDeviceInfo.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
