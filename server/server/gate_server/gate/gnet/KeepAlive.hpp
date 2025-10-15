
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"

namespace gnet { 
   class KeepAlive : public GNET::Protocol
   {
      public:
#include "rpcgen/gnet/KeepAlive.inc"

	 virtual void Process(Manager *manager, Manager::Session::ID sid)
	 {
	    // handle
	    glinkd::LinkServer	* lsm = glinkd::LinkServer::GetInstance();
	    glinkd::LinkSession * psession = lsm->GetLinkSession(sid);
	    if(NULL == psession)
	    {
	     	lsm->Close(sid);
		return;  
	    }
	    glinkd::LinkServer::log.debug("KeepAlive:Process:user(id=%d) code=%d,sid=%d", psession->getUserId(), code, sid);
	    if(!lsm->Send(sid,this))
	    {
		   glinkd::LinkServer::log.warn("KeepAlive:Process:user(id=%d) send KeepAlive to client failed,sid=%d", psession->getUserId(), sid);
	       lsm->Close(sid);
	       return;
	    }
	    lsm->SetAliveTime(sid, lsm->GetKeepAliveInterval());
	 }
   };
} 
