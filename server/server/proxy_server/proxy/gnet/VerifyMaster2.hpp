
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_VerifyMaster2_.hpp"

namespace gnet { 

class VerifyMaster2 : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/VerifyMaster2.inc"

	virtual void Process(Manager * manager, Manager::Session::ID sid) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("VerifyMaster2 zoneid=%d,roleid=%ld",zoneid,roleid);
		if(!gdeliverd::GameDBServer::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_WARNING,"send VerifyMaster2 to gs(db) failed,zoneid=%d,roleid=%ld",zoneid,roleid);
		}
	}

};

} 
