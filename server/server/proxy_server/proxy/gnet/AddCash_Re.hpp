
#pragma once

#include "rpcgen.hpp"
#include "AuthClient.hpp"

namespace gnet { 

class AddCash_Re : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AddCash_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */
		GNET::Log::log(LOG_INFO,"user(id=%d,zoneid=%d) receive AddCash_Re,sn=%d,retcode=%d",userid,zoneid,sn,retcode); 
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d,zoneid=%d) send AddCash_Re to au failed,sn=%d,retcode=%d",userid,zoneid,sn,retcode); 
		}		
	}
};

} 
