
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_DiscountAnnounce_.hpp"

namespace gnet { 

class DiscountAnnounce : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/DiscountAnnounce.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		GNET::Log::log(LOG_INFO,"========= receive DiscountAnnounce from au ========== size=%d",discount.size());
		gdeliverd::GameDBServer* dbsm = gdeliverd::GameDBServer::GetInstance();
		dbsm->discount = discount;
		if(!dbsm->SendProtocol(this))
		{
			GNET::Log::log(LOG_WARNING,"==== send DiscountAnnounce to gs(db) failed ===="); 
		}
	}
};

} 
