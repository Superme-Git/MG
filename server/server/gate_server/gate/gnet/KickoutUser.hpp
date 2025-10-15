
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"

namespace gnet { 

class KickoutUser : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/KickoutUser.inc"

	virtual void Process(Manager *, Manager::Session::ID sid) 
	{
		glinkd::LinkServer::log.info("KickoutUser:Process:user(id=%d),sid=%d,cause=%d",userid,localsid,cause);
		 /* protocol handle */ 
		if(!glinkd::LinkServer::ValidUser(localsid,userid))
			return;
		glinkd::LinkServer* lsm = glinkd::LinkServer::GetInstance();
		if(cause == 0)
		{
			lsm->Close(localsid);
			glinkd::LinkServer::log.info("KickoutUser:Process:user(id=%d) be Kickout by AC,sid=%d",userid,localsid);
			return;	
		}
		lsm->SendErr(localsid,cause,"Be kicked out");
	}
};

} 
