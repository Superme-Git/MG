
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_UserInfoReq_.hpp"
#include "GameDBServer.hpp"
#include "mapusertype.h"
#include "UserInfoRep.hpp"
#include "gmcontainer.h"
#include "netplayer.h"

namespace gnet { 

class UserInfoReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/UserInfoReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{ 
		/* protocol handle */ 
		LOG_TRACE("user(id=%d) receive UserInfoReq from gs(db)",userid);
		int func = 0,funcparm = 0;
		char gender = 2;
		GNET::Octets nickname;
		gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
		gdeliverd::UserInfo* uinfo = gdeliverd::UserContainer::GetInstance()->find(userid);

		UserInfoRep rep;
		rep.userid = userid;
		if(uinfo == NULL || (!gdeliverd::UserType::GetInstance()->GetUserType(userid,func,funcparm,gender,nickname)))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) uinfo=null or usertype cache not exist",userid);
			rep.retcode = -1;
			if(!dbs->Send(dbs->GetSid(),rep))
			{
				GNET::Log::log(LOG_WARNING,"user(id=%d) send UserInfoRep to gs(db) failed,retcode=-1",userid);
			}
			return;		
		} 
		else
		{
			rep.retcode = 0;
			rep.func = func;
			rep.funcparm = funcparm;
			rep.loginip = uinfo->loginip;
			rep.blisgm = 0;
			rep.algorithm = uinfo->algorithm;
			if(gdeliverd::DeliverServer::GetInstance()->allow_nickname_contain_account || gdeliverd::DeliverServer::GetInstance()->GetAid() == SHENDIAO_AID || gdeliverd::DeliverServer::GetInstance()->GetAid() == PUKE_AID)
			{
				GNET::Marshal::OctetsStream os;
				os << nickname;
				os << uinfo->account;
				rep.nickname = os;
			}
			else
				rep.nickname = nickname;

		}
		std::vector<int> * pauth = gdeliverd::GMContainer::GetInstance()->GetGMInfo(userid);
		if(pauth)
		{
			rep.blisgm = 1;
			rep.auth = *pauth;
		}
		if(!dbs->Send(dbs->GetSid(),rep))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) send UserInfoRep to gs(db) failed,retcode=-1",userid);
		}
	}
};

} 
