
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/cross/_depends_BroadcastCrossStatus_.hpp"
#include "CrossClient.hpp"
#include "netplayer.h"
#include "BroadcastCrossStatus_Re.hpp"

namespace cross { 

class BroadcastCrossStatus : public GNET::Protocol
{
public:
	#include "rpcgen/cross/BroadcastCrossStatus.inc"

	virtual void Process(Manager *manager, Manager::Session::ID sid) 
	{ 
		/* protocol handle */
#ifdef CROSS_SERVER
		return;
#endif
		gdeliverd::CrossClient * ccm = gdeliverd::CrossClient::GetInstance();
		ccm->setCrossOnlineNum(serverinfo.online_num);
		ccm->setCrossMaxOnlineNum(serverinfo.max_online_num);

		gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
		
		BroadcastCrossStatus_Re re;
		ServerInfo info;
		info.zoneid = gdeliverd::DeliverServer::GetInstance()->GetZoneid();
		info.online_num = uc->Size();
		info.max_online_num = uc->GetPlayerLimit();
		re.serverinfo = info;
		manager->Send(sid,re);

		GNET::Log::log(LOG_INFO,"CrossClient::receive BroadcastCrossStatus,crossserver(zoneid=%d,online=%d,max=%d),local(zoneid=%d,online=%d,max=%d)",serverinfo.zoneid,serverinfo.online_num,serverinfo.max_online_num,info.zoneid,info.online_num,info.max_online_num);
	}

};

} 
