
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GameDataTransferByAU_.hpp"

namespace gnet { 

class GameDataTransferByAU : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GameDataTransferByAU.inc"

	virtual void Process(Manager * manager, Manager::Session::ID ) 
	{ 
		/* protocol handle */
		LOG_TRACE("receive GameDataTransferByAU fromzoneid=%d,tozoneid=%d",fromzoneid,tozoneid);
		if(manager == gdeliverd::GameDBServer::GetInstance())
		{
			gdeliverd::AuthClient::GetInstance()->SendProtocol(this);
		}
		else if(manager == gdeliverd::AuthClient::GetInstance())
		{
			gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
			dbs->Send(dbs->GetSid(),this);
		}
	}

};

} 
