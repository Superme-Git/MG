
#pragma once

#include "rpcgen.hpp"
#include "gmcontainer.h"
#include "netplayer.h"
#include "DeliverServer.hpp"
#include "mapusertype.h"
#include "UserInfoRep.hpp"

namespace gnet { 

class QueryUserPrivilege3_Re : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/QueryUserPrivilege3_Re.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		 /* protocol handle */
		GNET::Log::log(LOG_INFO,"user(id=%d) is GM,receive QueryUserPrivilege3_Re from au",userid);
		gdeliverd::GMContainer::GetInstance()->InsertGMInfo(userid,auth);

		gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
		gdeliverd::UserInfo* uinfo = gdeliverd::UserContainer::GetInstance()->find(userid);

		int func = 0,funcparm = 0;
		char gender = 2;
		GNET::Octets nickname;
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
			rep.blisgm = 1;
			rep.auth = auth;
			rep.algorithm = uinfo->algorithm;
			rep.gender = gender;
			if(gdeliverd::DeliverServer::GetInstance()->allow_nickname_contain_account || gdeliverd::DeliverServer::GetInstance()->GetAid() == SHENDIAO_AID || gdeliverd::DeliverServer::GetInstance()->GetAid() == PUKE_AID)
			{
				GNET::Marshal::OctetsStream os;
				os << nickname;
				os << uinfo->account;
				rep.nickname = os;
			}
			else
				rep.nickname = nickname;
			if(!dbs->Send(dbs->GetSid(),rep))
			{
				GNET::Log::log(LOG_WARNING,"user(id=%d) send UserInfoRep to gs(db) failed,retcode=-1",userid);
			}
		}
	
	/*	fprintf(stderr,"user(id=%d) GM Privilege: ",userid);
		for(size_t i=0;i<auth.size();i++)
		{
			fprintf(stderr,"%d ",auth[i]);
		}
		fprintf(stderr,"\n");
	*/
	}


};

} 
