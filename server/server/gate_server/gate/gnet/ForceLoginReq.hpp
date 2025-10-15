
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_ForceLoginReq_.hpp"

namespace gnet { 

class ForceLoginReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/ForceLoginReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"user(id=%d) ForceLoginReq,localsid=%d.",userid,localsid);
		glinkd::LinkServer *lsm = glinkd::LinkServer::GetInstance();
		glinkd::LinkSession *psession = lsm->GetLinkSession(localsid);
		if(NULL == psession)
			return;
		lsm->Send(localsid,this);
	}

};

} 
