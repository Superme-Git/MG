
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_QueryUserid2_.hpp"
#include "QueryUserid2Req.hpp"
#include "AuthClient.hpp"
#include "GameDBServer.hpp"

namespace gnet { 

class QueryUserid2 : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/QueryUserid2.inc"

	enum
	{
		QUERY_FROM_AU = 1,
		QUERY_FROM_TRADESERVER = 2,	
	};
	
	int query_from;

	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::QueryUserid2Arg* arg = getArgument();
		//gnet::QueryUserid2Res* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		//gnet::QueryUserid2Arg* arg = getArgument();
		//gnet::QueryUserid2Res* res = getResult();
	}

	virtual void Process(Manager * manager, Manager::Session::ID ) 
	{
		/* protocol handle */ 
		gnet::QueryUserid2Arg* arg = getArgument();
		if(xid.IsRequest())
		{
			QueryUserid2Req req;
			req.xid = xid.GetId();
			req.rolename.replace(arg->rolename.begin(),arg->rolename.size());
			GNET::Log::log(LOG_INFO,"user receive QueryUserid2");
			gdeliverd::GameDBServer* dbsm = gdeliverd::GameDBServer::GetInstance();
			gdeliverd::AuthClient* acm = gdeliverd::AuthClient::GetInstance();
			if(!dbsm->SendProtocol(req))
			{
				GNET::Log::log(LOG_WARNING,"user send QueryUserid2Req to gs(db) failed");
				QueryUserid2* rpc = (QueryUserid2*)GNET::Rpc::Create(QueryUserid2::PROTOCOL_TYPE);
				rpc->xid.SetId(xid.GetId());
				rpc->xid.ClrRequest();
				QueryUserid2Res* res = rpc->getResult();
				res->result = 3;//communication error
				res->userid = -1;
				res->roleid = -1;
				res->level = -1;
				if(manager == acm)
				{
					if(!acm->SendProtocol(rpc))
					{
						GNET::Log::log(LOG_WARNING,"user QueryUserid2 failed,send error to au failed");

					}
				}
				return;
			}

			if(manager == acm)
			{
				GNET::Log::log(LOG_INFO,"user receive QueryUserid2 from au");
				query_from = QUERY_FROM_AU;
				gdeliverd::DeliverServer::GetInstance()->AuQueryIdSet.insert(xid.GetId());
			}
		}
	}

	void OnTimeout()
	{
		// client only
		//gnet::QueryUserid2Arg* arg = getArgument();
		if(query_from == QUERY_FROM_AU)
		{
			gdeliverd::DeliverServer::GetInstance()->AuQueryIdSet.erase(xid.GetId());
		}
		else if(query_from == QUERY_FROM_TRADESERVER)
		{
			gdeliverd::DeliverServer::GetInstance()->TradeQueryIdSet.erase(xid.GetId());
		}	
	}

};

} 
