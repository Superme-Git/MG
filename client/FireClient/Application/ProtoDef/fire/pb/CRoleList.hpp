
#pragma once

//#include "rpcgen.hpp" // uncomment this line if need.
#include "rpcgen/fire/pb/_depends_CRoleList_.hpp"

namespace fire { 
namespace pb { 

class CRoleList : public aio::Protocol
{
public:
	#include "rpcgen/fire/pb/CRoleList.inc"

	virtual void Process(Manager *, Manager::Session::ID ) { /* protocol handle */ }

};

} 
} 
