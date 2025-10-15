
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_InstantAddCash_.hpp"
#include "AuthClient.hpp"
#include "netplayer.h"
#include "InstantAddCashRep.hpp"

namespace gnet { 

class InstantAddCash : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/InstantAddCash.inc"
	
	int link_sid;
	int client_sid;

	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::InstantAddCashArg* arg = getArgument();
		//gnet::InstantAddCashRes* res = getResult();

	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		gnet::InstantAddCashArg* arg = getArgument();
		gnet::InstantAddCashRes* res = getResult();
		GNET::Log::log(LOG_INFO,"user(id=%d) receive InstantAddCash from au,retcode=%d",arg->userid,res->retcode);
		if(res->retcode == 0)
		{
			GNET::Log::log(LOG_INFO,"----user(id=%d) InstantAddCash success,cardnum=%.*s",arg->userid,arg->cardnum.size(),(char*)arg->cardnum.begin());
		}
		InstantAddCashRep rep;
		rep.retcode = res->retcode;
		rep.reserved = arg->reserved;
		rep.userid = arg->userid;
		rep.localsid = client_sid;
		gdeliverd::DeliverServer::GetInstance()->Send(link_sid,rep);
	}

	void OnTimeout()
	{
		// client only
		gnet::InstantAddCashArg* arg = getArgument();
		GNET::Log::log(LOG_WARNING,"user(id=%d) InstantAddCash timeout,retcode=-1",arg->userid);
		InstantAddCashRep rep;
		rep.retcode = 7;
		rep.reserved = arg->reserved;
		rep.userid = arg->userid;
		rep.localsid = client_sid;
		gdeliverd::DeliverServer::GetInstance()->Send(link_sid,rep);
	}

};

} 
