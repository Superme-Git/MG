
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SSOGetTicketRep_.hpp"

namespace gnet { 

class SSOGetTicketRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SSOGetTicketRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		/* protocol handle */ 
	   	LOG_TRACE("user(id=%d) receive SSOGetTicketRep,retcode=%d",user.userid,retcode);
                glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
		Manager::Session::ID localsid = lsm->GetSidByUserid(this->user.userid);
                if(localsid > 0)
                        lsm->Send(localsid,this);
	}
};
} 
