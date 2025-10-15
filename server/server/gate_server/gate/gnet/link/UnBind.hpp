
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"

namespace gnet { 
namespace link { 

class UnBind : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/link/UnBind.inc"

	virtual void Process(Manager *, Manager::Session::ID sid)
	{
		LOG_TRACE("receive UnBind from gs,pvid=%d,sid=%d",pvid,sid);
		glinkd::LinkServer::GetInstance()->GetSessions().unbind(pvid, linksids, sid);
	}

};

} 
} 
