
#pragma once

#include "rpcgen.hpp"
#include "UserLogout.hpp"
#include "CrossServer.hpp"
#include "forcelogin.h"
#include "GameDBServer.hpp"

namespace gnet { 

class StatusAnnounce : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/StatusAnnounce.inc"

	virtual void Process(Manager *, Manager::Session::ID sid)
	{
		if(status != 0)
			return;
		gdeliverd::GameDBServer::log.info("StatusAnnounce:Process:user(id=%d) from link", userid);
		//for ForceLogin
		gdeliverd::SessionInfo* sinfo = gdeliverd::DeliverServer::GetInstance()->GetSessionInfo(sid,localsid);
		if(NULL != sinfo)
		{
			gdeliverd::ForceLogin::GetInstance()->RmvForceLogin(sinfo->userid);
		}
		//end

		gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
		gdeliverd::UserInfo * uinfo = uc->find(userid);
		if(NULL == uinfo)
			return;
		gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		dsm->RemoveSessionInfo(sid,localsid);

		Manager::Session::ID linkid = uinfo->link_sid;
		if(localsid == uinfo->client_sid && sid == linkid)
		{	
			uc->erase(userid);
			dsm->GetCouponLimit().clear(userid);
			dsm->GetCouponExchangeLimit().clear(userid);
			gdeliverd::GameDBServer::log.info("StatusAnnounce:Process:user(id=%d) logout,erase user,sid=%d",userid,localsid);
		}
		dsm->BroadcastStatus();
	}

};

} 
