
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/_depends_CEnterWorld_.hpp"

namespace fire { 
namespace pb { 

class CEnterWorld : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/CEnterWorld.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
