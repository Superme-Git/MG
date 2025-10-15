
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/cross/_depends_AnnounceZoneidToCross_.hpp"
#include "CrossServer.hpp"
#include "DeliverServer.hpp"
#include "commonhelper.hpp"
#include "AnnounceZoneidToCross_Re.hpp"

namespace cross { 

class AnnounceZoneidToCross : public GNET::Protocol
{
public:
	#include "rpcgen/cross/AnnounceZoneidToCross.inc"

	virtual void Process(Manager *manager, Manager::Session::ID sid) 
	{
		/* protocol handle */ 
		gdeliverd::CrossServer * csm = gdeliverd::CrossServer::GetInstance();
		gdeliverd::ZoneSessionManager::ZoneSession * zs = csm->getZoneSessionManager().find(zoneid);
		AnnounceZoneidToCross_Re re;
		re.retcode = AnnounceZoneidToCross_Re::RET_OK;
		re.cross_zoneid = gdeliverd::DeliverServer::GetInstance()->GetZoneid();
		re.boottime = gdeliverd::DeliverServer::GetInstance()->getBootTime();

		const char* newpeer = gdeliverd::CommonHelper::getStringPeer(csm->sid2peermap[sid]);
		if(NULL != zs)
		{
			time_t now = time(NULL);
			const char* oldpeer = gdeliverd::CommonHelper::getStringPeer(zs->getPeer());
			int oldsid = zs->getSid();
			if(zs->getTimestamp() - now > gdeliverd::ZONE_SESSION_ALIVE_TIME)
			{
				csm->ChangeState(oldsid,&gnet::state::Null);
				csm->insertSidZoneMap(sid,zoneid);
				csm->getZoneSessionManager().erase(zoneid);
				csm->getZoneSessionManager().insert(zoneid,sid,csm->sid2peermap[sid],boottime);
				GNET::Log::log(LOG_WARNING,"AnnounceZoneidToCross Warning,new session will replace old! zoneid=%d,new(%d,%s),old(%d,%s)",zoneid,sid,newpeer,oldsid,oldpeer);
			}
			else
			{
				re.retcode = AnnounceZoneidToCross_Re::RET_CONFLICT_ZONE; 
				GNET::Log::log(LOG_ERR,"AnnounceZoneidToCross Error,zone already exist! zoneid=%d,new(%d,%s),old(%d,%s)",zoneid,sid,newpeer,oldsid,oldpeer);
				csm->Close(sid);
			}
		}	
		else
		{
			csm->insertSidZoneMap(sid,zoneid);
			csm->getZoneSessionManager().insert(zoneid,sid,csm->sid2peermap[sid],boottime);
			GNET::Log::log(LOG_INFO,"AnnounceZoneidToCross ok,zoneid=%d,(%d,%s)",zoneid,sid,newpeer);
		}
		csm->Send(sid,re);
	}
};

} 
