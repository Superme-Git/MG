
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SysSendMail3_.hpp"

namespace gnet { 

class SysSendMail3 : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SysSendMail3.inc"

	virtual void Process(Manager * manager, Manager::Session::ID sid) 
	{
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"SysSendMail3 orderid=%ld,userid=%d,roleid=%ld",orderid,userid,roleid); 
		if(!gdeliverd::GameDBServer::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_INFO,"send SysSendMail3 to gs(db) failed,orderid=%ld,userid=%d,roleid=%ld",orderid,userid,roleid); 
		}

	}

};

} 
