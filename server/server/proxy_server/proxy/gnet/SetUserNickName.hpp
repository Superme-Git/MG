
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SetUserNickName_.hpp"
#include "SetUserNickNameRep.hpp"

namespace gnet { 

class SetUserNickName : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/SetUserNickName.inc"

	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::SetUserNickNameArg* arg = getArgument();
		//gnet::SetUserNickNameRes* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		gnet::SetUserNickNameArg* arg = getArgument();
		gnet::SetUserNickNameRes* res = getResult();
		LOG_TRACE("user(id=%d) receive SetUserNickName from au,retcode=%d",arg->userid,res->retcode);

		SetUserNickNameRep rep;
		rep.userid = arg->userid;
		rep.retcode = res->retcode;
		rep.nickname = arg->nickname;
		gdeliverd::GameDBServer* dbsm = gdeliverd::GameDBServer::GetInstance();
		if(!dbsm->SendProtocol(rep))
		{
		   GNET::Log::log(LOG_WARNING,"user(id=%d) send SetUserNickName to gs(db) failed .",arg->userid);
		}

	}

	void OnTimeout()
	{
	   // client only
	   //gnet::SetUserNickNameArg* arg = getArgument();
	}

};

} 
