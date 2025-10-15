
#pragma once

#include <time.h>
#include "rpcgen.hpp"
#include "rpcgen/gnet/UserLoginArg.hpp"
#include "rpcgen/gnet/UserLoginRes.hpp"
#include "netplayer.h"
#include "UserLoginRep.hpp"
#include "OnlineAnnounce.hpp"
#include "QueryUserPrivilege3.hpp"
#include "QueryUserPrivilege3_Re.hpp"
#include "DeliverServer.hpp"
#include "AuthClient.hpp"
#include "gmcontainer.h"
#include "CertVerify.hpp"
#include "KickoutUser.hpp"
#include "mapusertype.h"
#include "UserInfoRep.hpp"
#include "Constants.hpp"
#include "CrossServer.hpp"

namespace gnet { 

class UserLogin : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/UserLogin.inc"
	Manager::Session::ID client_sid;
	Manager::Session::ID link_sid;
	bool is_trylogin;

	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::UserLoginArg* arg = getArgument();
		//gnet::UserLoginRes* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		gnet::UserLoginArg* arg = getArgument();
		gnet::UserLoginRes* res = getResult();
		GNET::Log::log(LOG_INFO,"user(id=%d) receive UserLogin,blkickuser=%d,func=%d,funcparm=%d,blIsGM=%d",arg->userid,arg->blkickuser,res->func,res->funcparm,res->blisgm);
		gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		gdeliverd::SessionInfo* sinfo = dsm->GetSessionInfo(link_sid,client_sid);
		if(NULL == sinfo)
			return;
		if(res->retcode != GNET::ERR_SUCCESS)
		{
			dsm->SessionError(arg->userid,link_sid,client_sid,res->retcode,"UserLoginRes.retcode != 0.");
			return;
		}
		gdeliverd::UserType::GetInstance()->SetUserType(arg->userid,res->func,res->funcparm,2,GNET::Octets());
		
		GNET::Octets account = sinfo->getAccountRemoveSSO();
		if(gdeliverd::UserContainer::GetInstance()->addUser(arg->userid,client_sid,link_sid,sinfo->loginip,sinfo->algorithm,account))
		{
			dsm->Send(link_sid,OnlineAnnounce(arg->userid,client_sid,res->remain_playtime,dsm->GetZoneid(),dsm->GetAid(),sinfo->algorithm));
			//online_num changed,so to broadcast to link 
			dsm->BroadcastStatus();
		}
		else
		{
			dsm->SessionError(arg->userid,link_sid,client_sid,GNET::ERR_MULTILOGIN,"already in usermap!");
			return;	
		}
	//	SendCertVerify();
		//send func,funcparm,gmauth to gs(db)
		UserInfoRep rep;
		rep.userid = arg->userid;
		rep.retcode = 0;
		rep.func = res->func;
		rep.funcparm = res->funcparm;
		rep.loginip = sinfo->loginip;
		rep.algorithm = sinfo->algorithm;
		rep.blisgm = res->blisgm;
		rep.gender = 2;
		if(dsm->allow_nickname_contain_account || dsm->GetAid() == SHENDIAO_AID || dsm->GetAid() == PUKE_AID)
		{
		   GNET::Marshal::OctetsStream os;
		   os << GNET::Octets();
		   os << account;
		   rep.nickname = os;
		}

		gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
#ifndef ALL_GM
		if(res->blisgm != 0)
		{
			GNET::Log::log(LOG_INFO,"user(id=%d) is GM,receive UserLogin from au---",arg->userid);			
			QueryUserPrivilege3 qup3;
			qup3.userid = arg->userid;
			qup3.zoneid = dsm->GetZoneid();
			if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(qup3))
			{
				GNET::Log::log(LOG_WARNING,"user(id=%d) is GM,send QueryUserPrivilege3 to au failed",arg->userid);
			}
		}
		else
		{
			if(!dbs->Send(dbs->GetSid(),rep))
			{
				GNET::Log::log(LOG_WARNING,"user(id=%d) send UserInfoRep to gs(db) failed",arg->userid);
			}		
			gdeliverd::GMContainer::GetInstance()->RemoveGMInfo(arg->userid);
		}
#else
		int gmpri[]  = {0,1,3,4,5,6,7,8,100,101,104,105,206,215};
		std::vector<int> auth(gmpri,gmpri+14);
		rep.blisgm = 1;
		rep.auth = auth;
		//cache gm auth
		gdeliverd::GMContainer::GetInstance()->InsertGMInfo(arg->userid,auth);
		if(!dbs->Send(dbs->GetSid(),rep))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) send UserInfoRep to gs(db) failed",arg->userid);
		}	
#endif
		dsm->RemoveSessionInfo(link_sid,client_sid);	
	}

	void TryLogin(int reason)
	{
	   	if(is_trylogin)
		   return;
                is_trylogin = true;
		
		gnet::UserLoginArg* arg = getArgument();
		GNET::Log::log(LOG_WARNING,"user(id=%d) UserLogin tryLogin.",arg->userid);
		gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		gdeliverd::SessionInfo* sinfo = dsm->GetSessionInfo(link_sid,client_sid);
		if(NULL == sinfo)
			return;
		int func = 0,funcparm = 0;
		char gender = 2;
		GNET::Octets nickname;
		if(!gdeliverd::UserType::GetInstance()->GetUserType(arg->userid,func,funcparm,gender,nickname))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) UserLogin tryLogin failed,userinfo cache not found.",arg->userid);
			if(reason == GNET::ERR_AU_DELIVER_TIMEOUT)
				dsm->SessionError(arg->userid,link_sid,client_sid,GNET::ERR_AU_DELIVER_TIMEOUT,"UserLogin timeout and userinfo cache not found.");
			else if(reason == GNET::ERR_NOT_CONNECT_AU)
				dsm->SessionError(arg->userid,link_sid,client_sid,GNET::ERR_NOT_CONNECT_AU,"cert verify not finish or gdeliverd not connect AU.");
			return;
		}
		GNET::Octets account = sinfo->getAccountRemoveSSO();
		if(gdeliverd::UserContainer::GetInstance()->addUser(arg->userid,client_sid,link_sid,sinfo->loginip,sinfo->algorithm,account))
		{
			dsm->Send(link_sid,OnlineAnnounce(arg->userid,arg->localsid,0,dsm->GetZoneid(),dsm->GetAid(),sinfo->algorithm));
			dsm->BroadcastStatus();
		}
		else
		{	
			dsm->SessionError(arg->userid,link_sid,client_sid,GNET::ERR_MULTILOGIN,"already in usermap!");
			return;	
		}
		//	SendCertVerify();

		gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
		UserInfoRep rep;
		rep.userid = arg->userid;
		rep.retcode = 0;
		rep.func = func;
		rep.funcparm = funcparm;
		rep.loginip = sinfo->loginip;
		rep.algorithm = sinfo->algorithm;
		rep.blisgm = 0;
		rep.gender = gender;
		std::vector<int> * pauth = gdeliverd::GMContainer::GetInstance()->GetGMInfo(arg->userid);
		if(pauth)
		{
			rep.blisgm = 1;
			rep.auth = *pauth;
			GNET::Log::log(LOG_WARNING,"user(id=%d) is GM,trylogin,auth.size=%d",arg->userid,rep.auth.size());
		}
		if(dsm->allow_nickname_contain_account || dsm->GetAid() == SHENDIAO_AID || dsm->GetAid() == PUKE_AID)
		{
		   GNET::Marshal::OctetsStream os;
		   os << GNET::Octets();
		   os << account;
		   rep.nickname = os;
		}

		if(!dbs->Send(dbs->GetSid(),rep))
			GNET::Log::log(LOG_WARNING,"user(id=%d) send UserInfoRep to gs(db) failed.",arg->userid);
		dsm->RemoveSessionInfo(link_sid,client_sid);
	}

	void SendCertVerify()
	{
		gnet::UserLoginArg* arg = getArgument();
		CertVerify cv;
		gdeliverd::AuthClient *acm = gdeliverd::AuthClient::GetInstance();
		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		cv.localsid = client_sid;
		cv.authd_cert.replace(acm->authd_cert.begin(),acm->authd_cert.size());
		if(!dsm->Send(link_sid,cv))
			GNET::Log::log(LOG_WARNING,"user(id=%d) send CertVerify to link failed",arg->userid);
	}

	void OnTimeout()
	{
		// client only
		gnet::UserLoginArg* arg = getArgument();
		GNET::Log::log(LOG_WARNING,"user(id=%d) UserLogin timeout",arg->userid);
		TryLogin(GNET::ERR_AU_DELIVER_TIMEOUT);
	}

};

} 
