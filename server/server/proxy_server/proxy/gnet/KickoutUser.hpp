
#pragma once

#include "rpcgen.hpp"
#include "netplayer.h"
#include "UserLogout.hpp"
#include "DeliverServer.hpp"

namespace gnet { 

class KickoutUser : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/KickoutUser.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		/* protocol handle */
		GNET::Log::log(LOG_INFO,"user(id=%d) receive KickoutUser from au",userid);
		gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
		gdeliverd::UserInfo* uinfo = uc->find(userid);
		if(NULL == uinfo)
			return;

		this->localsid = uinfo->client_sid;
		gdeliverd::DeliverServer::GetInstance()->Send(uinfo->link_sid,this);
		uc->erase(userid);
		UserLogout *rpc = (UserLogout*)GNET::Rpc::Call(UserLogout::PROTOCOL_TYPE,UserLogoutArg(userid,localsid));
		//for sure send userlogout to au always
		gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc);
	}
};
} 
