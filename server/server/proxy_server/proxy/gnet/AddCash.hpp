
#pragma once

#include "rpcgen.hpp"
#include "GameDBServer.hpp"

namespace gnet { 

class AddCash : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AddCash.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */
		GNET::Log::log(LOG_INFO,"user(id=%d,zoneid=%d) receive AddCash,cash=%d,sn=%d",userid,zoneid,cash,sn); 
		if(!gdeliverd::GameDBServer::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d,zoneid=%d) send AddCash to gs(db) failed,cash=%d,sn=%d",userid,zoneid,cash,sn); 
		}
	}
};

} 
