
#pragma once

#include "rpcgen.hpp"
#include "netplayer.h"
#include "AddictionControlRep.hpp"
#include "DeliverServer.hpp"
#include "rpcgen/gnet/_depends_AddictionControl_.hpp"

namespace gnet { 

	class AddictionControl : public GNET::Protocol
	{
		public:
			#include "rpcgen/gnet/AddictionControl.inc"

			virtual void Process(Manager *, Manager::Session::ID ) 
			{
				GNET::Log::log(LOG_INFO,"user(id=%d) age < 18,receive AddictionControl.",userid);
				gdeliverd::UserInfo* uinfo = gdeliverd::UserContainer::GetInstance()->find(userid);
				if(uinfo)
					gdeliverd::DeliverServer::GetInstance()->Send(uinfo->link_sid,AddictionControlRep(uinfo->client_sid,zoneid,userid,rate,msg,data));
			}
	};
} 
