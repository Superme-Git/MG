
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/cross/_depends_CommonDataTransfer_.hpp"
#include "GameDBServer.hpp"
#include "DeliverServer.hpp"

namespace cross { 

class CommonDataTransfer : public GNET::Protocol
{
public:
	#include "rpcgen/cross/CommonDataTransfer.inc"

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
			gdeliverd::ZoneSessionManager::ZoneSession * pzonesession = gdeliverd::CrossServer::GetInstance()->getZoneSession(zoneid);
			if(pzonesession != NULL)
			{
				gdeliverd::CrossServer::GetInstance()->Send(pzonesession->getSid(),this);
			}
			else
				GNET::Log::log(LOG_WARNING,"crossserver--CommonDataTransfer pzonesession=null,zoneid=%d,dtype=%d,flag=%d",zoneid,dtype,flag);

		}
		LOG_TRACE("crossserver--CommonDataTransfer,zoneid=%d,dtype=%d,flag=%d",zoneid,dtype,flag);
#else
		if(flag == TO_CROSS)
		{
			this->zoneid  = gdeliverd::DeliverServer::GetInstance()->GetZoneid();
			gdeliverd::CrossClient::GetInstance()->SendProtocol(this);
		}

		if(flag == FROM_CROSS)
		{
			gdeliverd::GameDBServer *gsm = gdeliverd::GameDBServer::GetInstance();
			gsm->Send(gsm->GetSid(),this);
		}
		LOG_TRACE("crosssclient--CommonDataTransfer,zoneid=%d,dtype=%d,flag=%d",zoneid,dtype,flag);
#endif	
	}


};

} 
