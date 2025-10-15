
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_PassportGetRoleList_.hpp"
#include "AuthClient.hpp"
#include "GameDBServer.hpp"
#include "PassportGetRoleListReq.hpp"

namespace gnet { 

class PassportGetRoleList : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/PassportGetRoleList.inc"

	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::PassportGetRoleListArg* arg = getArgument();
		//gnet::PassportGetRoleListRes* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		//gnet::PassportGetRoleListArg* arg = getArgument();
		//gnet::PassportGetRoleListRes* res = getResult();
	}

	virtual void Process(Manager * manager, Manager::Session::ID ) 
	{
		/* protocol handle */ 
		gnet::PassportGetRoleListArg* arg = getArgument();
		if(xid.IsRequest())
		{
			PassportGetRoleListReq req;
			req.xid = xid.GetId();
			req.userid = arg->userid;
			GNET::Log::log(LOG_INFO,"user(id=%d) receive PassportGetRoleList from au",arg->userid);
			gdeliverd::GameDBServer* dbsm = gdeliverd::GameDBServer::GetInstance();
			gdeliverd::AuthClient* acm = gdeliverd::AuthClient::GetInstance();
			if(!dbsm->SendProtocol(req))
			{
				GNET::Log::log(LOG_WARNING,"user send PassportGetRoleListReq to gs(db) failed");
				PassportGetRoleList* rpc = (PassportGetRoleList*)GNET::Rpc::Create(PassportGetRoleList::PROTOCOL_TYPE);
				rpc->xid.SetId(xid.GetId());
				rpc->xid.ClrRequest();
				PassportGetRoleListRes* res = rpc->getResult();
				res->retcode = 2;//communication error
				if(!acm->SendProtocol(rpc))
				{
					GNET::Log::log(LOG_WARNING,"user PassportGetRoleList failed,send error to au failed");

				}
				return;
			}
		}
	}

	void OnTimeout()
	{
		// client only
		//gnet::PassportGetRoleListArg* arg = getArgument();
	}

};

} 
