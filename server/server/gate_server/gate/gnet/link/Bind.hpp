
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"

namespace gnet { 
namespace link { 

class Bind : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/link/Bind.inc"

	virtual void Process(Manager *, Manager::Session::ID sid)
	{
		log.info("Bind:Process:from gs,pvid=%d,sid=%d", pvid, sid);
		glinkd::LinkServer::GetInstance()->GetSessions().bind(pvid, linksids, sid);
	}
	static log4cpp::Category& log;
};
log4cpp::Category& Bind::log = log4cpp::Category::getInstance(std::string("glinkd"));
} 
} 
