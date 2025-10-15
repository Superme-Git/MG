
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/openau/_depends_ServerIDResponse_.hpp"
#include "LinkServer.hpp"

namespace openau { 

class ServerIDResponse : public GNET::Protocol
{
public:
	#include "rpcgen/openau/ServerIDResponse.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */
		log.info("ServerIDResponse:Process:palttype=%d,serverid=%.*s", plattype, serverid.size(), serverid.begin());
		glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
		lsm->setPlatType(plattype);
		lsm->setServerId(serverid);
	}
	static log4cpp::Category& log;
};
log4cpp::Category& ServerIDResponse::log = log4cpp::Category::getInstance(std::string("glinkd"));
} 
