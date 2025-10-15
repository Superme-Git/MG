
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_Game2AU_.hpp"

namespace gnet { 

class Game2AU : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/Game2AU.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		/* protocol handle */ 
		LOG_TRACE("Game2AU userid=%d,qtype=%d",userid,qtype);
		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(this))
		{
		/*	AU2Game ag;
			ag.userid = userid;
			ag.qtype = qtype;
			ag.retcode = 8;
			gdeliverd::GameDBServer::GetInstance()->SendProtocol(ag);
		*/
			GNET::Log::log(LOG_WARNING,"send Game2AU to au failed,userid=%d,qtype=%d",userid,qtype);
		}
	}
};

} 
