
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/_depends_BeanImport_.hpp"

namespace fire { 
namespace pb { 

class BeanImport : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/BeanImport.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
