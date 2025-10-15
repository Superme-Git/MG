
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SysSendMail3_Re_.hpp"

namespace gnet { 

class SysSendMail3_Re : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SysSendMail3_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"SysSendMail3_Re retcode=%d,orderid=%ld,roleid=%ld",retcode,orderid,roleid);
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_INFO,"send SysSendMail3_Re to au failed,retcode=%d,orderid=%ld,roleid=%ld",retcode,orderid,roleid);
		}
	}

};

} 
