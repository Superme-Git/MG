
#pragma once

#include "rpcgen.hpp"
#include "GMShutup_Re.hpp"
#include "GameDBServer.hpp"

namespace gnet { 

class GMShutup : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/GMShutup.inc"

	virtual void Process(Manager *, Manager::Session::ID sid) 
	{
		/* protocol handle */ 
		LOG_TRACE("user(id=%d) receive GMShutup from gs(db),gmuserid=%d",dstuserid,gmuserid);
		gdeliverd::GameDBServer * dbsm = gdeliverd::GameDBServer::GetInstance();
		GMShutup_Re shutup_re;
		shutup_re.retcode = GNET::ERR_COMMUNICATION;
		shutup_re.dstuserid = dstuserid;
		shutup_re.forbid_time = forbid_time;

		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(this))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) send GMShutup to au failed,gmuserid=%d",dstuserid,gmuserid);
			shutup_re.retcode = GNET::ERR_COMMUNICATION;
			if(!dbsm->Send(dbsm->GetSid(),shutup_re))
				GNET::Log::log(LOG_WARNING,"user(id=%d) send GMShutup_Re to gs(db) failed,gmuserid=%d",dstuserid,gmuserid);
		}
		else
		{
			shutup_re.retcode = GNET::ERR_SUCCESS;
			if(!dbsm->Send(dbsm->GetSid(),shutup_re))
				GNET::Log::log(LOG_WARNING,"user(id=%d) send GMShutup_Re to gs(db) failed,gmuserid=%d",dstuserid,gmuserid);
		}
	}


};

} 
