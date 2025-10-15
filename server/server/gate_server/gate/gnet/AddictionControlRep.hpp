
#pragma once

#include "rpcgen.hpp"
#include "AddictionControl.hpp"
#include "LinkServer.hpp"

namespace gnet { 

class AddictionControlRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AddictionControlRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */
		LOG_TRACE("user(id=%d) receive AddictionControlRep,zoneid=%d,rate=%d,msg=%d",userid,zoneid,rate,msg);
		if(!glinkd::LinkServer::ValidUser(localsid,userid))
			return;
		glinkd::LinkServer::GetInstance()->Send(localsid,AddictionControl(zoneid,userid,rate,msg,data));
	}
};

} 
