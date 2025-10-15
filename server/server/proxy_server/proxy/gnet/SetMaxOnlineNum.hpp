
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SetMaxOnlineNum_.hpp"
#include "CrossServer.hpp"
#include "CrossClient.hpp"
#include "netplayer.h"
#include "cross/BroadcastCrossStatus_Re.hpp"

namespace gnet { 

class SetMaxOnlineNum : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/SetMaxOnlineNum.inc"

	void OnServer(Manager *, Manager::Session::ID )
	{
		gnet::SetMaxOnlineNumArg* arg = getArgument();
		gnet::SetMaxOnlineNumRes* res = getResult();
		GNET::Log::log(LOG_INFO,"-----SetMaxOnlineNum----- maxnum=%d,fake_maxnum=%d",arg->maxnum,arg->fake_maxnum);
		gdeliverd::UserContainer::GetInstance()->SetPlayerLimit(arg->maxnum,arg->fake_maxnum);
		res->retcode = GNET::ERR_SUCCESS;
#ifdef CROSS_SERVER
		gdeliverd::CrossServer::GetInstance()->BroadcastCrossStatus();
#else
		gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
		cross::BroadcastCrossStatus_Re re;
		cross::ServerInfo info;
		info.zoneid = gdeliverd::DeliverServer::GetInstance()->GetZoneid();
		info.online_num = uc->Size();
		info.max_online_num = uc->GetPlayerLimit();
		re.serverinfo = info;
		gdeliverd::CrossClient::GetInstance()->SendProtocol(re);
#endif
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		//gnet::SetMaxOnlineNumArg* arg = getArgument();
		//gnet::SetMaxOnlineNumRes* res = getResult();
	}

	void OnTimeout()
	{
		// client only
		//gnet::SetMaxOnlineNumArg* arg = getArgument();
	}

};

} 
