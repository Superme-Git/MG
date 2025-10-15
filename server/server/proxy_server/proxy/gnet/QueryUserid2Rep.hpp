
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_QueryUserid2Rep_.hpp"
#include "QueryUserid2.hpp"

namespace gnet { 

class QueryUserid2Rep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/QueryUserid2Rep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"user(id=%d,roleid=%ld) receive QueryUserid2Rep from gs(db),result=%d",userid,roleid,result);
		QueryUserid2* rpc = (QueryUserid2*)GNET::Rpc::Create(QueryUserid2::PROTOCOL_TYPE);
		rpc->xid.SetId(xid);
		rpc->xid.ClrRequest(); 
		QueryUserid2Res* res = rpc->getResult();
		res->result = result; 
		res->userid = userid;
		res->roleid = roleid;
		res->level = level;
	
		gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		if(dsm->AuQueryIdSet.find(xid) != dsm->AuQueryIdSet.end())
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d,roleid=%ld) send QueryUserid2 to au,result=%d",userid,roleid,result);
			if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
			{
				GNET::Log::log(LOG_WARNING,"user(id=%d,roleid=%ld) send QueryUserid2 to au failed,result=%d",userid,roleid,result);
			}
			dsm->AuQueryIdSet.erase(xid);	
		}
		
		if(dsm->TradeQueryIdSet.find(xid) != dsm->TradeQueryIdSet.end())
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d,roleid=%ld) send QueryUserid2 to tradeServer,result=%d",userid,roleid,result);
			dsm->TradeQueryIdSet.erase(xid);			
		}
	}

};

} 
