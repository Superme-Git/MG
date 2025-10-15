
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_AU2Game_.hpp"
#include "GameDBServer.hpp"

namespace gnet { 

class AU2Game : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/AU2Game.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("user(id=%d) AU2Game qtype=%d,retcode=%d",userid,qtype,retcode);
		if(qtype == 2)
		{
			GNET::Marshal::OctetsStream os(info);
			std::vector<int> friends;
			os >> GNET::MarshalContainer(friends);	
			GNET::Log::log(LOG_INFO,"AU2Game::MatchFriends,friends.size=%d",friends.size());
		}
		gdeliverd::GameDBServer* dbsm = gdeliverd::GameDBServer::GetInstance();
		if(!dbsm->SendProtocol(this))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) send AU2Game to gs(db) failed,qtype=%d,retcode=%d",userid,qtype,retcode); 
		}
	}

};

} 
