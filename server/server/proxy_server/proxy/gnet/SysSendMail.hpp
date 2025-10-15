
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SysSendMail_.hpp"
#include "GameDBServer.hpp"
#include "SysSendMail_Re.hpp"

namespace gnet { 

class SysSendMail : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SysSendMail.inc"

	virtual void Process(Manager * manager, Manager::Session::ID sid) 
	{
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"SysSendMail tid=%d,sysid=%d,sys_type=%d,receiver=%d,attach_money=%d",tid,sysid,sys_type,receiver,attach_money); 
		if(!gdeliverd::GameDBServer::GetInstance()->SendProtocol(this))
		{
			if(!manager->Send(sid,gnet::SysSendMail_Re(GNET::ERR_COMMUNICATION,tid)))
			{
				GNET::Log::log(LOG_WARNING,"send SysSendMail_Re to au failed,tid=%d,sysid=%d,sys_type=%d,receiver=%d,attach_money=%d",tid,sysid,sys_type,receiver,attach_money);
			} 
			GNET::Log::log(LOG_WARNING,"send SysSendMail to gs(db) failed,tid=%d,sysid=%d,sys_type=%d,receiver=%d,attach_money=%d",tid,sysid,sys_type,receiver,attach_money); 
		}

	}

};

} 
