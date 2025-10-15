
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_GetUserNickName_.hpp"

namespace gnet { 

class GetUserNickName : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/GetUserNickName.inc"

	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::GetUserNickNameArg* arg = getArgument();
		//gnet::GetUserNickNameRes* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		//gnet::GetUserNickNameArg* arg = getArgument();
		//gnet::GetUserNickNameRes* res = getResult();
	}

	void OnTimeout()
	{
		// client only
		//gnet::GetUserNickNameArg* arg = getArgument();
	}

};

} 
