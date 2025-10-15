
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_AuthdVersion_.hpp"

namespace gnet { 

class AuthdVersion : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AuthdVersion.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"AuthVersion,version=%d,retcode=%d",version,retcode);
		if(retcode != RET_OK)
		{
			GNET::Log::log(LOG_WARNING,"=====Active close AuSession,AuthVersion,zoneid invalid or conflict,version=%d,retcode=%d",version,retcode);
			gdeliverd::AuthClient::GetInstance()->bl_zoneid_valid = false;
			gdeliverd::AuthClient::GetInstance()->CloseAuSession();
			return;
		}
		if(version == 1)
		{
			gdeliverd::AuthClient::GetInstance()->SetAbroadPolicy(0);
		}
		else
			gdeliverd::AuthClient::GetInstance()->SetAbroadPolicy(1);
	//	gdeliverd::AuthClient::GetInstance()->bl_keepalive = true;
	}

};

} 
