
#pragma once

#include "rpcgen.hpp"
#include "GetAddCashSNReq.hpp"
#include "GameDBServer.hpp"
#include "AuthClient.hpp"
#include "rpcgen/gnet/GetAddCashSNArg.hpp"
#include "rpcgen/gnet/GetAddCashSNRes.hpp"

namespace gnet { 

class GetAddCashSN : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/GetAddCashSN.inc"

	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::GetAddCashSNArg* arg = getArgument();
		//gnet::GetAddCashSNRes* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		//gnet::GetAddCashSNArg* arg = getArgument();
		//gnet::GetAddCashSNRes* res = getResult();
	}

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */ 
		gnet::GetAddCashSNArg* arg = getArgument();
		if(!xid.IsRequest())
		   return;
		GetAddCashSNReq req;
		req.xid = xid.GetId();
		req.userid = arg->userid;
		req.zoneid = arg->zoneid;
		GNET::Log::log(LOG_INFO,"user(id=%d,zoneid=%d) receive GetAddCashSN from au",arg->userid,arg->zoneid);
		gdeliverd::GameDBServer* dbsm = gdeliverd::GameDBServer::GetInstance();
		if(!dbsm->SendProtocol(req))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d,zoneid=%d) send GetAddCashSNReq to gs(db) failed",arg->userid,arg->zoneid);
			SendResToAu(GNET::ERR_DELIVER_SEND,arg->userid,arg->zoneid);	
		}
	}

	void SendResToAu(int retcode,int userid,int zoneid)
	{
		GetAddCashSN* rpc = (GetAddCashSN*)GNET::Rpc::Create(GetAddCashSN::PROTOCOL_TYPE);
		rpc->xid.SetId(xid.GetId());
		rpc->xid.ClrRequest();
		GetAddCashSNRes* res = rpc->getResult();
		res->retcode = retcode;
		res->userid = userid;
		res->zoneid = zoneid;
		res->sn = 0;
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d,zoneid=%d) GetAddCashSN failed,send GetAddCashSN to au failed",userid,zoneid);

		}	 

	}

	void OnTimeout()
	{
		// client only
		gnet::GetAddCashSNArg* arg = getArgument();
		SendResToAu(GNET::ERR_DELIVER_TIMEOUT,arg->userid,arg->zoneid);	
	}

};

} 
