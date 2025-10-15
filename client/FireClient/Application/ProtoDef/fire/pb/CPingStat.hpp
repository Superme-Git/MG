
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/_depends_CPingStat_.hpp"

namespace fire { 
namespace pb { 

class CPingStat : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/CPingStat.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
