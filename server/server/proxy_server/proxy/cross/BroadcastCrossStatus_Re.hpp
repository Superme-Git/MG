
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/cross/_depends_BroadcastCrossStatus_Re_.hpp"
#include "netplayer.h"
#include "DeliverServer.hpp"
#include "CrossServer.hpp"

namespace cross { 

class BroadcastCrossStatus_Re : public GNET::Protocol
{
public:
	#include "rpcgen/cross/BroadcastCrossStatus_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		gdeliverd::CrossServer * csm = gdeliverd::CrossServer::GetInstance();
		if(!csm->getBlCrossServer())
			return;

		gdeliverd::ZoneSessionManager::ZoneSession * zs = csm->getZoneSessionManager().find(serverinfo.zoneid);
		if(NULL == zs)
		{
			GNET::Log::log(LOG_ERR,"BroadcastCrossStatus_Re zone session not exist,zoneid=%d",serverinfo.zoneid);
			return;
		}
		zs->setOnlineNum(serverinfo.online_num);
		zs->setMaxOnlineNum(serverinfo.max_online_num);

		gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		GNET::Log::log(LOG_INFO,"CrossServer::BroadcastCrossStatus_Re,crossclient(zoneid=%d,online=%d,max=%d),local(zoneid=%d,online=%d,max=%d)",serverinfo.zoneid,serverinfo.online_num,serverinfo.max_online_num,dsm->GetZoneid(),uc->Size(),uc->GetPlayerLimit());
	}

};

} 
