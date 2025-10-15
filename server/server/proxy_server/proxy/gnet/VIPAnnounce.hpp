
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_VIPAnnounce_.hpp"
#include "netplayer.h"

namespace gnet { 

class VIPAnnounce : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/VIPAnnounce.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("user(id=%d) receive VIPAnnounce from au",userid);
		gdeliverd::GameDBServer* dbsm = gdeliverd::GameDBServer::GetInstance();
		if(!dbsm->SendProtocol(this))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) send VIPAnnounce to gs(db) failed",userid); 
		}
	}

};

} 
