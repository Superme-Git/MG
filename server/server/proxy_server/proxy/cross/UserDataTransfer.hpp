
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/cross/_depends_UserDataTransfer_.hpp"

namespace cross { 

class UserDataTransfer : public GNET::Protocol
{
public:
	#include "rpcgen/cross/UserDataTransfer.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		/* protocol handle */ 
#ifdef CROSS_SERVER
		if(flag == TO_CROSS)
		{
			gdeliverd::GameDBServer *gsm = gdeliverd::GameDBServer::GetInstance();
			gsm->Send(gsm->GetSid(),this);
		}

		if(flag == FROM_CROSS)
		{
			int src_zoneid = gdeliverd::UserSrc::GetInstance()->getSrcZoneidByUserid(userid);
			gdeliverd::ZoneSessionManager::ZoneSession * pzonesession = gdeliverd::CrossServer::GetInstance()->getZoneSession(src_zoneid);
			if(pzonesession != NULL)
			{
				gdeliverd::CrossServer::GetInstance()->Send(pzonesession->getSid(),this);
			}
			else
				GNET::Log::log(LOG_WARNING,"crossserver--UserDataTransfer pzonesession=null,userid=%d,dtype=%d,flag=%d",userid,dtype,flag);
		}
		LOG_TRACE("crossserver--UserDataTransfer,userid=%d,dtype=%d,flag=%d",userid,dtype,flag);
#else
		if(flag == TO_CROSS)
		{
			gdeliverd::CrossClient::GetInstance()->SendProtocol(this);
		}

		if(flag == FROM_CROSS)
		{
			gdeliverd::GameDBServer *gsm = gdeliverd::GameDBServer::GetInstance();
			gsm->Send(gsm->GetSid(),this);
		}
		LOG_TRACE("crosssclient--UserDataTransfer,userid=%d,dtype=%d,flag=%d",userid,dtype,flag);
#endif	
	}

};

} 
