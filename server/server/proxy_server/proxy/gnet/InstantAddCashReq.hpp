
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_InstantAddCashReq_.hpp"
#include "AuthClient.hpp"
#include "DeliverServer.hpp"
#include "GameDBServer.hpp"
#include "netplayer.h"
#include "InstantAddCashRep.hpp"
#include "InstantAddCash.hpp"

namespace gnet { 

class InstantAddCashReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/InstantAddCashReq.inc"
/* InstantAddCashRep retcode mean
 * 0 success
 * 1 card not exist
 * 2 userid not exist
 * 3 zoneid not exist
 * 4 card passwd error
 * 5 card expire
 * 6 card used exceed limit
 * 11 this userid being exce usecash
 * 12 this userid being addcash
 * 13 this userid be forbidden,forbid addcash
 * -1 other error
 */
	virtual void Process(Manager *, Manager::Session::ID sid) 
	{ 
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"user(id=%d) receive InstantAddCashReq,localsid=%d",userid,localsid);
		if(gdeliverd::AuthClient::GetInstance()->GetAbroadPolicy() == 1)
		{
			SendResToLink(3,userid,reserved,sid,localsid);
			return;
		}		

		if(!gdeliverd::GameDBServer::GetInstance()->GetAddcashLimit().add(cardnum))
		{
			SendResToLink(6,userid,reserved,sid,localsid);
			return;
		}

		gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
		gdeliverd::UserInfo* uinfo = uc->find(userid);
		if(NULL == uinfo)
		{
			SendResToLink(-1,userid,reserved,sid,localsid);
			return;		
		}
		InstantAddCashArg arg;
		arg.userid = userid;	
		arg.loginip = uinfo->loginip;
		arg.reserved = reserved;
		arg.cardnum.replace(cardnum.begin(),cardnum.size());
		arg.cardpasswd.replace(cardpasswd.begin(),cardpasswd.size());
		InstantAddCash * rpc = (InstantAddCash*)GNET::Rpc::Call(InstantAddCash::PROTOCOL_TYPE,arg);
		rpc->link_sid = sid;
		rpc->client_sid = localsid;
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) send InstantAddCash to au failed!",userid);
			SendResToLink(-1,userid,reserved,sid,localsid);
		}

	}

	void SendResToLink(int retcode,int userid,int reserved,int link_sid,int client_sid)
	{
		InstantAddCashRep rep;
		rep.retcode = retcode;
		rep.userid = userid;
		rep.reserved = reserved;
		rep.localsid = client_sid;
		gdeliverd::DeliverServer::GetInstance()->Send(link_sid,rep);
	}

};

} 
