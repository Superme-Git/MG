
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/cross/_depends_SendUserInfoAndTicket_Re_.hpp"
#include "cross/Constants.hpp"

namespace cross { 

	class SendUserInfoAndTicket_Re : public GNET::Protocol
	{
		public:
#include "rpcgen/cross/SendUserInfoAndTicket_Re.inc"

			virtual void Process(Manager *, Manager::Session::ID ) 
			{ 
				/* protocol handle */
				GNET::Log::log(LOG_INFO,"user(id=%d) SendUserInfoAndTicket_Re retcode=%d",userid,retcode); 
#ifdef CROSS_SERVER
				if(flag == TO_CROSS)
				{
					int src_zoneid = gdeliverd::UserSrc::GetInstance()->getSrcZoneidByUserid(userid);
					gdeliverd::ZoneSessionManager::ZoneSession * pzonesession = gdeliverd::CrossServer::GetInstance()->getZoneSession(src_zoneid);
					if(pzonesession != NULL)
					{
						gdeliverd::CrossServer::GetInstance()->Send(pzonesession->getSid(),this);
					}
				}

				if(flag == FROM_CROSS)
				{
					gdeliverd::GameDBServer *gsm = gdeliverd::GameDBServer::GetInstance();
					gsm->Send(gsm->GetSid(),this);
				}
#else
				if(flag == TO_CROSS)
				{
					gdeliverd::GameDBServer *gsm = gdeliverd::GameDBServer::GetInstance();
					gsm->Send(gsm->GetSid(),this);
				}

				if(flag == FROM_CROSS)
				{
					gdeliverd::CrossClient::GetInstance()->SendProtocol(this);
				}
#endif
			}
	};
} 
