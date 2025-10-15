
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_AuGameKeepAlive_.hpp"
#include "AuAnyClient.hpp"

namespace gnet { 

class AuGameKeepAlive : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AuGameKeepAlive.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		struct timeval now;
		gettimeofday(&now,NULL);
		int64_t new_code = now.tv_sec * 1000 + now.tv_usec / 1000;
		long ping = new_code - code;
		gdeliverd::AuAnyClient::log.debug("AuGameKeepAlive:Process:PING between AU and deliver is %ld ms.", ping);
	}

};

} 
