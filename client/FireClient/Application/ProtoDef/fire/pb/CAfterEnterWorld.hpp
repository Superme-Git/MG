
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/_depends_CAfterEnterWorld_.hpp"

namespace fire { 
namespace pb { 

class CAfterEnterWorld : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/CAfterEnterWorld.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
