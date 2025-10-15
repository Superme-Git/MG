
#pragma once

#include "rpcgen.hpp"
#include "GameDBServer.hpp"
#include "gmcontainer.h"
#include "QueryUserPrivilege3_Re.hpp"

namespace gnet { 

class QueryUserPrivilege3 : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/QueryUserPrivilege3.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		/* protocol handle */ 
		std::vector<int> * pauth = gdeliverd::GMContainer::GetInstance()->GetGMInfo(userid);
		if(pauth)
		{
			LOG_TRACE("user(id=%d,zoneid=%d) receive QueryUserPrivilege3 from gs(db)",userid,zoneid);
			gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
			QueryUserPrivilege3_Re qup_re;
			qup_re.userid = userid;
			qup_re.auth = *pauth;
			if(!dbs->Send(dbs->GetSid(),qup_re)) 
			{
				GNET::Log::log(LOG_WARNING,"user(id=%d,zoneid=%d) is GM,send QueryUserPrivilege3_Re to gs(db) failed",userid,zoneid);
			}
		}

	}

};

} 
