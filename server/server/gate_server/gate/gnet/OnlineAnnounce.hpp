
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"
#include "openau/ServerIDResponse.hpp"

namespace gnet { 

class OnlineAnnounce : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/OnlineAnnounce.inc"

	virtual void Process(Manager *, Manager::Session::ID sid )
	{
		glinkd::LinkServer *lsm = glinkd::LinkServer::GetInstance();
		glinkd::LinkSession *psession = lsm->GetLinkSession(localsid);
		if(NULL == psession)
			return;

		psession->getForwards()->authority().setupAllowInOnlineAnnounce(algorithm, aid, zoneid, psession->getIdentity());

		glinkd::LinkServer::log.info("OnlineAnnounce:Process:user(id=%d) algorithm=0x%x",userid, algorithm);
		lsm->ChangeState(localsid,&gnet::state::Forward); 
		lsm->Send(localsid,this);
		
		lsm->GetHalfLoginSet().erase(localsid);
		lsm->TriggerListen();
		if (lsm->ExceedUserLimit(lsm->GetUseridMap().size()) && lsm->IsListening())
		{
			glinkd::LinkServer::log.warn("OnlineAnnounce:Process:online > limit,online=%d", lsm->GetUseridMap().size());
			lsm->StopListen();
		}
		lsm->GetUseridMap().insert(std::make_pair(userid,localsid));
		glinkd::LinkServer::log.info("OnlineAnnounce:Process:accountid=%d,account=%.*s,peer=%s,mid=%.*s login!", 
									 psession->getUserId(), 
									 psession->getIdentity().size(), (char*)psession->getIdentity().begin(), 
									 inet_ntoa(psession->getPeerAddr()), 
									 psession->getMid().size(), (char*)psession->getMid().begin());

		if(lsm->getPlatType() != -1)	
		{
			openau::ServerIDResponse response;
			response.plattype = lsm->getPlatType();
			response.serverid = lsm->getServerId();
			lsm->Send(localsid,response);
		}
	}

};

} 
