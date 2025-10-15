
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/move/_depends_CSendAutoMovePathID_.hpp"

namespace fire { 
namespace pb { 
namespace move { 

class CSendAutoMovePathID : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/move/CSendAutoMovePathID.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
} 
