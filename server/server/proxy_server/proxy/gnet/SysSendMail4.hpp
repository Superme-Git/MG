
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SysSendMail4_.hpp"

namespace gnet { 

class SysSendMail4 : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SysSendMail4.inc"

	virtual void Process(Manager * manager, Manager::Session::ID sid) 
	{
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"SysSendMail4:paytype=%d,orderid=%ld,userid=%d,roleid=%ld",paytype,orderid,userid,roleid); 
		if(!gdeliverd::GameDBServer::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_INFO,"send SysSendMail4 to gs(db) failed,paytype=%d,orderid=%ld,userid=%d,roleid=%ld",paytype,orderid,userid,roleid); 
		}

	}

};

} 
