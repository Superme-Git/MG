
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_ForceLoginRep_.hpp"
#include "forcelogin.h"

namespace gnet { 

class ForceLoginRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/ForceLoginRep.inc"

	virtual void Process(Manager *, Manager::Session::ID sid) 
	{ 
		/* protocol handle */
	       	GNET::Log::log(LOG_INFO,"user(id=%d) ForceLoginRep,localsid=%d,deny_flag=%d",userid,localsid,deny_flag);
		gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
		gdeliverd::UserInfo * uinfo = uc->find(userid); 
		if(NULL == uinfo)
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) ForceLoginRep,uinfo not exist.",userid);
		//	return;
		}
		gdeliverd::ForceLogin * pflogin = gdeliverd::ForceLogin::GetInstance();
		gdeliverd::UserInfo * puinfo = pflogin->GetForceLogin(userid);
		if(NULL == puinfo)
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) ForceLoginRep,ForceLogin info not exist.",userid);
			return;
		}
		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		if(deny_flag == 1) //deny be kick,kick new
		{
			dsm->SessionError(userid,puinfo->link_sid,puinfo->client_sid,GNET::ERR_MULTILOGIN,"multilogin,deny ForceLogin.");
			pflogin->RmvForceLogin(userid);
			return;
		}	
		else if(deny_flag == 0)//kick self
		{
			pflogin->ProcessForceLogin(userid,*puinfo);	
		}
	}

};

} 
