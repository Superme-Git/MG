
#pragma once

#include "rpcgen.hpp"
#include "ProviderServer.hpp"
#include "LinkServer.hpp"

namespace gnet { 
namespace link { 

class LinkServerControl : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/link/LinkServerControl.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */ 
		glinkd::ProviderServer::log.info("LinkServerControl::Process:from gs,ptype=%d", ptype);
		if(ptype == E_STOP_LISTEN )
		{
			glinkd::ProviderServer::log.debug("LinkServerControl::Process:from gs,ptype=E_STOP_LISTEN");
			glinkd::LinkServer::GetInstance()->StopListen();	
		}
		if(ptype == E_START_LISTEN)
		{
			glinkd::ProviderServer::log.debug("LinkServerControl::Process:from gs,ptype=E_START_LISTEN");
			glinkd::LinkServer::GetInstance()->StartListen();
		}
	}

};

} 
} 
