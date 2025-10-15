
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"
#include "rpcgen/gnet/_depends_AnnounceForbidInfo_.hpp"

namespace gnet { 

class AnnounceForbidInfo : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AnnounceForbidInfo.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */ 
		LOG_TRACE("user(id=%d) receive AnnounceForbidInfo",userid);
		if(!glinkd::LinkServer::ValidSid(localsid))
			return;
		glinkd::LinkServer::GetInstance()->Send(localsid,this);
	}

};

} 
