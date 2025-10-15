
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetMaxOnlineNum_.hpp"

namespace gnet { 

class GetMaxOnlineNum : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/GetMaxOnlineNum.inc"

	void OnServer(Manager *, Manager::Session::ID )
	{
	//	gnet::GetMaxOnlineNumArg* arg = getArgument();
		gnet::GetMaxOnlineNumRes* res = getResult();
		
		gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
		res->retcode = GNET::ERR_SUCCESS;
		res->online_num = uc->Size();
		res->maxnum = uc->GetPlayerLimit();
		res->fake_maxnum = uc->GetFakePlayerLimit(); 
		GNET::Log::log(LOG_INFO,"-----GetMaxOnlineNum-----,maxnum=%d,fake_num=%d,online_num=%d",res->maxnum,res->fake_maxnum,res->online_num);
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		//gnet::GetMaxOnlineNumArg* arg = getArgument();
		//gnet::GetMaxOnlineNumRes* res = getResult();
	}

	void OnTimeout()
	{
		// client only
		//gnet::GetMaxOnlineNumArg* arg = getArgument();
	}

};

} 
