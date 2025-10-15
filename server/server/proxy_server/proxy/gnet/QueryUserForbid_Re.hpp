
#pragma once

#include "rpcgen.hpp"
#include "mapforbid.h"
#include "GameDBServer.hpp"
#include "GMShutup.hpp"
#include "Constants.hpp"

namespace gnet { 

class QueryUserForbid_Re : public GNET::Protocol
{
	public:
	#include "rpcgen/gnet/QueryUserForbid_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		// handle
		LOG_TRACE("userid(id=%d) receive QueryUserForbid_Re from au,forbid.size()=%d",userid,forbid.size());
		int len = forbid.size();
		gdeliverd::ForbidLogin * forbidlogin = gdeliverd::ForbidLogin::GetInstance();
		if(len == 0)
			forbidlogin->RmvForbid(userid);	
		else
		{
			gdeliverd::GameDBServer * dbsm = gdeliverd::GameDBServer::GetInstance();
			for (int i=0;i<len;++i)
			{
				int time_left = forbid[i].time;
				switch(forbid[i].ltype)
				{
					case gnet::FBD_FORBID_TALK:
					{		
						if(time_left > 1)
						{
							LOG_TRACE("user(id=%d) is FORBID_TALK,time(second)=%d",userid,time_left);
							if(!dbsm->Send(dbsm->GetSid(),GMShutup(-1,-1,userid,time_left,forbid[i].reason)))
							{
								GNET::Log::log(LOG_WARNING,"user(id=%d) send GMShutup to gs(db) failed",userid);
							}
						}
						break;
					}
					case gnet::FBD_FORBID_LOGIN:
					{	
						if ( time_left <= 1 ) 
						{
							forbidlogin->RmvForbid(userid);
							break;
						}
						LOG_TRACE("userid(id=%d) is FORBID_LOGIN,time(second)=%d",userid,time_left);
						forbidlogin->PutForbid(userid,forbid[i]);
						break;
					}
					case gnet::FBD_FORBID_SELLPOINT:
						break;
					case gnet::FBD_FORBID_TRADE:
						break;						
					case gnet::FBD_FORBID_SELL:
						break;
					default:
						break;
				}
			}
		}
	}
};
} 
