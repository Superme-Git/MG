
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_PassportGetRoleListRep_.hpp"
#include "GameDBServer.hpp"
#include "AuthClient.hpp"
#include "PassportGetRoleList.hpp"

namespace gnet { 

class PassportGetRoleListRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/PassportGetRoleListRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"receive PassportGetRoleListRep from gs(db),retcode=%d",retcode);
		PassportGetRoleList* rpc = (PassportGetRoleList*)GNET::Rpc::Create(PassportGetRoleList::PROTOCOL_TYPE);
		rpc->xid.SetId(xid);
		rpc->xid.ClrRequest(); 
		PassportGetRoleListRes* res = rpc->getResult();
		res->retcode = retcode; 
		res->rolelist = rolelist;

		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
		{
			GNET::Log::log(LOG_WARNING,"send PassportGetRoleList to au failed");

		}
		else
			GNET::Log::log(LOG_INFO,"user send PassportGetRoleList to au ok");
			 

	}

};

} 
