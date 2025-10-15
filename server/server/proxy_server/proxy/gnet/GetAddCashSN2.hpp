
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetAddCashSN2_.hpp"
#include "GetAddCashSN2Req.hpp"

namespace gnet { 

class GetAddCashSN2 : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/GetAddCashSN2.inc"

	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::GetAddCashSN2Arg* arg = getArgument();
		//gnet::GetAddCashSN2Res* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		//gnet::GetAddCashSN2Arg* arg = getArgument();
		//gnet::GetAddCashSN2Res* res = getResult();
	}

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */ 
		gnet::GetAddCashSN2Arg* arg = getArgument();
		if(!xid.IsRequest())
		   return;
		GetAddCashSN2Req req;
		req.xid = xid.GetId();
		req.userid = arg->userid;
		req.zoneid = arg->zoneid;
		req.force = arg->force;
		GNET::Log::log(LOG_INFO,"user(id=%d,zoneid=%d) receive GetAddCashSN2 from au,force=%d",arg->userid,arg->zoneid,arg->force);
		gdeliverd::GameDBServer* dbsm = gdeliverd::GameDBServer::GetInstance();
		if(!dbsm->SendProtocol(req))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d,zoneid=%d) send GetAddCashSN2Req to gs(db) failed,force=%d",arg->userid,arg->zoneid,arg->force);
			SendResToAu(GNET::ERR_DELIVER_SEND,arg->userid,arg->zoneid);	
		}
	}

	void SendResToAu(int retcode,int userid,int zoneid)
	{
		GetAddCashSN2* rpc = (GetAddCashSN2*)GNET::Rpc::Create(GetAddCashSN2::PROTOCOL_TYPE);
		rpc->xid.SetId(xid.GetId());
		rpc->xid.ClrRequest();
		GetAddCashSN2Res* res = rpc->getResult();
		res->retcode = retcode;
		res->userid = userid;
		res->zoneid = zoneid;
		res->sn = 0;
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d,zoneid=%d) GetAddCashSN2 failed,send GetAddCashSN2 to au failed",userid,zoneid);

		}	 

	}


	void OnTimeout()
	{
		// client only
		gnet::GetAddCashSN2Arg* arg = getArgument();
		SendResToAu(GNET::ERR_DELIVER_TIMEOUT,arg->userid,arg->zoneid);	
	}

};

} 
