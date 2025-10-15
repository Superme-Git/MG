
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetAddCashSN2Rep_.hpp"

namespace gnet { 

class GetAddCashSN2Rep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GetAddCashSN2Rep.inc"
	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"user(id=%d,zoneid=%d) receive GetAddCashSN2Rep from gs(db),sn=%d,retcode=%d",userid,zoneid,sn,retcode);
		GetAddCashSN2* rpc = (GetAddCashSN2*)GNET::Rpc::Create(GetAddCashSN2::PROTOCOL_TYPE);
		rpc->xid.SetId(xid);
		rpc->xid.ClrRequest(); 
		GetAddCashSN2Res* res = rpc->getResult();
		res->retcode = retcode; 
		res->userid = userid;
		res->zoneid = zoneid;
		res->sn = sn;

		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d,zoneid=%d) send GetAddCashSN2 to au failed",userid,zoneid);
		}	 
	}
};

} 
