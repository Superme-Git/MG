
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_ForceLoginRep_.hpp"
#include "DeliveryClient.hpp"

namespace gnet { 

class ForceLoginRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/ForceLoginRep.inc"

	virtual void Process(Manager *, Manager::Session::ID sid) 
	{ 
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"user(id=%d) ForceLoginRep,localsid=%d.",userid,sid);
		glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
		glinkd::LinkSession * psession = lsm->GetLinkSession(sid);
		if(NULL == psession) 
		{
			LOG_TRACE("receive ForceLoginRep,psession=null,sid=%d" ,sid);
			return;
		}
		this->localsid = sid;
		this->userid = psession->getUserId();
		glinkd::DeliveryClient::GetInstance()->SendProtocol(this);
	}

};

} 
