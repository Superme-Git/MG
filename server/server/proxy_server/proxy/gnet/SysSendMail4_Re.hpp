
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SysSendMail4_Re_.hpp"

namespace gnet { 

class SysSendMail4_Re : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SysSendMail4_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"SysSendMail4_Re retcode=%d,paytype=%d,orderid=%ld,roleid=%ld",retcode,paytype,orderid,roleid);
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_INFO,"send SysSendMail4_Re to au failed,retcode=%d,paytype=%d,orderid=%ld,roleid=%ld",retcode,paytype,orderid,roleid);
		}
	}

};

} 
