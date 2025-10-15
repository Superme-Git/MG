
#pragma once

#include "rpcgen.hpp"
#include "KickoutUser.hpp"
#include "GMKickoutUser_Re.hpp"
#include "DeliverServer.hpp"
#include "AuthClient.hpp"
#include "mapforbid.h"
#include "Constants.hpp"

namespace gnet { 

class GMKickoutUser : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GMKickoutUser.inc"

	virtual void Process(Manager *, Manager::Session::ID sid) 
	{
		/* protocol handle */ 
		if(forbid_time < 0)
			return;
		GNET::Log::log(LOG_INFO,"user(id=%d) receive GMKickoutUser,gmuserid=%d,forbid_time=%d",kickuserid,gmuserid,forbid_time);
		if(forbid_time > 2)
		{
		   RoleForbid forbid(gnet::FBD_FORBID_LOGIN,forbid_time,time(NULL),reason); 
		   gdeliverd::ForbidLogin::GetInstance()->PutForbid(kickuserid,forbid);
		   gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
		   gdeliverd::UserInfo* uinfo = uc->find(kickuserid);
		   if(uinfo)
		   {
		      gdeliverd::DeliverServer::GetInstance()->Send(uinfo->link_sid,KickoutUser(kickuserid,uinfo->client_sid,GNET::ERR_KICKOUT));
		      uc->erase(kickuserid);
		      UserLogout *rpc = (UserLogout*)GNET::Rpc::Call(UserLogout::PROTOCOL_TYPE,UserLogoutArg(kickuserid,uinfo->client_sid));
		      //for sure send userlogout to au always
		      gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc);
		   }
		}
		gdeliverd::GameDBServer * dbsm = gdeliverd::GameDBServer::GetInstance();
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(this))
		{
		   GNET::Log::log(LOG_WARNING,"user(id=%d) send GMKickoutUser to au failed,gmuserid=%d,forbid_time=%d",kickuserid,gmuserid,forbid_time);
		   dbsm->Send(dbsm->GetSid(),GMKickoutUser_Re(GNET::ERR_COMMUNICATION,gmuserid,localsid,kickuserid));
		}
		else
		{
		   dbsm->Send(dbsm->GetSid(),GMKickoutUser_Re(GNET::ERR_SUCCESS,gmuserid,localsid,kickuserid));
		}
	}
};

} 
