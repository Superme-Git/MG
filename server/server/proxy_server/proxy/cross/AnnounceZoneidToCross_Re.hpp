
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/cross/_depends_AnnounceZoneidToCross_Re_.hpp"
#include "CrossClient.hpp"

namespace cross { 

class AnnounceZoneidToCross_Re : public GNET::Protocol
{
public:
	#include "rpcgen/cross/AnnounceZoneidToCross_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */
		gdeliverd::CrossClient * ccm = gdeliverd::CrossClient::GetInstance();
		if(!ccm->getBlCrossClient())
			return;
		if(retcode == RET_OK)	
		{
			ccm->setCrossZoneid(cross_zoneid);
			gdeliverd::CrossClient::GetInstance()->setBlHandshake(true);
			GNET::Log::log(LOG_INFO,"AnnounceZoneidToCross_Re ok,retcode=%d,cross_zoneid=%d,boottime=%d",retcode,cross_zoneid,boottime);
		}
		else if(retcode == RET_CONFLICT_ZONE)
		{
			gdeliverd::CrossClient::GetInstance()->setBlHandshake(false);
			GNET::Log::log(LOG_ERR,"====================AnnounceZoneidToCross_Re ERROR,the same zoneid already connect to CrossServe,retcode=%d,cross_zoneid=%d,boottime=%d",retcode,cross_zoneid,boottime);
		}
	}

};

} 
