
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_DeliverAnnounceToLink_.hpp"
#include "LinkServer.hpp"

namespace gnet { 

class DeliverAnnounceToLink : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/DeliverAnnounceToLink.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */ 
		LOG_TRACE("receive DeliverAnnounceToLink,forbid_ip=%d",forbid_ip);
		glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
		glinkd::ForbidIpValue value;
		value.forbid_end_time = time(NULL) + lsm->GetForbidIpInterval();
		lsm->SetIpForbid(forbid_ip,value);
	}
};
} 
