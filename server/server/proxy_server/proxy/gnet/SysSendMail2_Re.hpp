
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SysSendMail2_Re_.hpp"

namespace gnet { 

class SysSendMail2_Re : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SysSendMail2_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */ 
		GNET::Log::log(LOG_INFO,"SysSendMail2_Re retcode=%d,tid=%d",retcode,tid);
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_WARNING,"send SysSendMail2_Re to au failed,retcode=%d,tid=%d",retcode,tid);
		}
	}

};

} 
