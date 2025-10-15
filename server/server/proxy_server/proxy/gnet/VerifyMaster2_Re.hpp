
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_VerifyMaster2_Re_.hpp"

namespace gnet { 

class VerifyMaster2_Re : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/VerifyMaster2_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_WARNING,"send VerifyMaster2_Re to au failed,rolename=%.*s,retcode=%d",rolename.size(),(char*)rolename.begin(),ret);
		}
	}

};

} 
