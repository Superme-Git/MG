
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SysSendMail2_.hpp"
#include "GameDBServer.hpp"
#include "SysSendMail2_Re.hpp"

namespace gnet { 

class SysSendMail2 : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SysSendMail2.inc"

	virtual void Process(Manager * manager, Manager::Session::ID sid) 
	{
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"SysSendMail2 tid=%d,sysid=%d,sys_type=%d,receiver=%ld,attach_money=%d",tid,sysid,sys_type,receiver,attach_money); 
		if(!gdeliverd::GameDBServer::GetInstance()->SendProtocol(this))
		{
			if(!manager->Send(sid,gnet::SysSendMail2_Re(GNET::ERR_COMMUNICATION,tid)))
			{
				GNET::Log::log(LOG_WARNING,"send SysSendMail2_Re to au failed,tid=%d,sysid=%d,sys_type=%d,receiver=%ld,attach_money=%d",tid,sysid,sys_type,receiver,attach_money);
			} 
			GNET::Log::log(LOG_WARNING,"send SysSendMail2 to gs(db) failed,tid=%d,sysid=%d,sys_type=%d,receiver=%ld,attach_money=%d",tid,sysid,sys_type,receiver,attach_money); 
		}

	}

};

} 
