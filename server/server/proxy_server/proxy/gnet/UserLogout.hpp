
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/UserLogoutArg.hpp"
#include "rpcgen/gnet/UserLogoutRes.hpp"


namespace gnet { 

class UserLogout : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/UserLogout.inc"
//	int client_sid;
//	int link_sid;

	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::UserLogoutArg* arg = getArgument();
		//gnet::UserLogoutRes* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		//gnet::UserLogoutArg* arg = getArgument();
		//gnet::UserLogoutRes* res = getResult();
		
	}

	void OnTimeout()
	{
		// client only
		//gnet::UserLogoutArg* arg = getArgument();
	}

};

} 
