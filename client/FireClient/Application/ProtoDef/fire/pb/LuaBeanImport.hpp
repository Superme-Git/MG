
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/_depends_LuaBeanImport_.hpp"

namespace fire { 
namespace pb { 

class LuaBeanImport : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/LuaBeanImport.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
