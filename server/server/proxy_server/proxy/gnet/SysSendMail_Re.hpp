
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SysSendMail_Re_.hpp"
#include "AuthClient.hpp"

namespace gnet { 

class SysSendMail_Re : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SysSendMail_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */ 
		LOG_TRACE("SysSendMail_Re retcode=%d,tid=%d",retcode,tid);
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_WARNING,"send SysSendMail_Re to au failed,retcode=%d,tid=%d",retcode,tid);
		}
	}

};

} 
