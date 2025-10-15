
#pragma once

#include "rpcgen.hpp"
#include "GetAddCashSN.hpp"
#include "GameDBServer.hpp"
#include "AuthClient.hpp"

namespace gnet { 

class GetAddCashSNRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetAddCashSNRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"user(id=%d,zoneid=%d) receive GetAddCashSNRep from gs(db),sn=%d,retcode=%d",userid,zoneid,sn,retcode);
		GetAddCashSN* rpc = (GetAddCashSN*)GNET::Rpc::Create(GetAddCashSN::PROTOCOL_TYPE);
		rpc->xid.SetId(xid);
		rpc->xid.ClrRequest(); 
		GetAddCashSNRes* res = rpc->getResult();
		res->retcode = retcode; 
		res->userid = userid;
		res->zoneid = zoneid;
		res->sn = sn;

		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d,zoneid=%d) send GetAddCashSN to au failed",userid,zoneid);

		}
	}
};

} 
