
#pragma once

#include <time.h>
#include "rpcgen.hpp"
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
#include "rpcgen/gnet/UserLogin2Arg.hpp"
#include "rpcgen/gnet/UserLogin2Res.hpp"
#include "mapusertype.h"
#include "UserInfoRep.hpp"
#include "Constants.hpp"
#include "CrossServer.hpp"

namespace gnet {

class UserLogin2 : public GNET::Rpc
{
public:
	#include "rpcgen/gnet/UserLogin2.inc"
	Manager::Session::ID client_sid;
	Manager::Session::ID link_sid;
	bool is_trylogin;

	void OnServer(Manager *, Manager::Session::ID )
	{
		//gnet::UserLogin2Arg* arg = getArgument();
		//gnet::UserLogin2Res* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID )
	{
		gnet::UserLogin2Arg* arg = getArgument();
		gnet::UserLogin2Res* res = getResult();
		gdeliverd::DeliverServer::log.info("UserLogin2:OnClient:user(id=%d) receive UserLogin2,blkickuser=%d,func=%d,funcparm=%d,blIsGM=%d,auth.size=%d",arg->userid,arg->blkickuser,res->func,res->funcparm,res->blisgm,res->auth.size());
		gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		gdeliverd::SessionInfo* sinfo = dsm->GetSessionInfo(link_sid,client_sid);
		if(NULL == sinfo)
			return;
		if(res->retcode != GNET::ERR_SUCCESS)
		{
			dsm->SessionError(arg->userid,link_sid,client_sid,res->retcode,"UserLogin2Res.retcode != 0.");
			return;
		}
		gdeliverd::UserType::GetInstance()->SetUserType(arg->userid,res->func,res->funcparm,res->gender,GNET::Octets(res->nickname.begin(),res->nickname.size()));

		GNET::Octets account = sinfo->getAccountRemoveSSO();
		if(gdeliverd::UserContainer::GetInstance()->addUser(arg->userid,client_sid,link_sid,sinfo->loginip,sinfo->algorithm,account))
		{
			dsm->Send(link_sid,OnlineAnnounce(arg->userid,client_sid,res->remain_playtime,dsm->GetZoneid(),dsm->GetAid(),sinfo->algorithm));
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
		rep.gender = res->gender;
		if(dsm->allow_nickname_contain_account || dsm->GetAid() == SHENDIAO_AID || dsm->GetAid() == PUKE_AID)
		{
		   GNET::Marshal::OctetsStream os;
		   os << res->nickname;
		   os << account;
		   rep.nickname = os;
		}
		else
			rep.nickname = res->nickname;
		if(res->blisgm == 1 && res->auth.size() > 0)
		{
			int len = res->auth.size();
			for(int i=0;i< len;i++)
			{
				int pri = res->auth[i];
				if(pri >= 0)
					rep.auth.push_back(pri);
				else
					rep.auth.push_back(pri + 256);
			}
			gdeliverd::GMContainer::GetInstance()->InsertGMInfo(arg->userid,rep.auth);
		}
		else
			gdeliverd::GMContainer::GetInstance()->RemoveGMInfo(arg->userid);
#ifdef ALL_GM
		int gmpri[]  = {0,1,3,4,5,6,7,8,100,101,104,105,206,215};
		std::vector<int> auth(gmpri,gmpri+14);
		rep.blisgm = 1;
		rep.auth = auth;
		//cache gm auth
		gdeliverd::GMContainer::GetInstance()->InsertGMInfo(arg->userid,auth);
#endif
		gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
		if(!dbs->Send(dbs->GetSid(),rep))
		{
			gdeliverd::DeliverServer::log.warn("UserLogin2:OnClient:user(id=%d) send UserInfoRep to gs(db) failed",arg->userid);
		}
		dsm->RemoveSessionInfo(link_sid,client_sid);
	//	SendGame2AU();
	}

	void TryLogin(int reason)
	{
		if(is_trylogin)
		   return;
                is_trylogin = true;

		gnet::UserLogin2Arg* arg = getArgument();
		gdeliverd::DeliverServer::log.info("UserLogin2:TryLogin:user(id=%d) UserLogin2 tryLogin.",arg->userid);
		gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		gdeliverd::SessionInfo* sinfo = dsm->GetSessionInfo(link_sid,client_sid);
		if(NULL == sinfo)
			return;
		int func = 0,funcparm = 0;
		char gender = 2;
		GNET::Octets nickname;
		if(!gdeliverd::UserType::GetInstance()->GetUserType(arg->userid,func,funcparm,gender,nickname))
		{
			gdeliverd::DeliverServer::log.warn("UserLogin2:TryLogin:user(id=%d) UserLogin2 tryLogin failed,userinfo cache not found.",arg->userid);
			if(reason == GNET::ERR_AU_DELIVER_TIMEOUT)
				dsm->SessionError(arg->userid,link_sid,client_sid,GNET::ERR_AU_DELIVER_TIMEOUT,"UserLogin2 timeout and userinfo cache not found.");
			else if(reason == GNET::ERR_NOT_CONNECT_AU)
				dsm->SessionError(arg->userid,link_sid,client_sid,GNET::ERR_NOT_CONNECT_AU,"cert verify not finish or gdeliverd not connect AU.");
			return;
		}
		gdeliverd::DeliverServer::log.warn("UserLogin2:TryLogin:user(id=%d) UserInfoRep.UserType.nickname=%.*s",arg->userid,nickname.size(),nickname.begin());
		GNET::Octets account = sinfo->getAccountRemoveSSO();
		gdeliverd::DeliverServer::log.warn("UserLogin2:TryLogin:user(id=%d) SessionInfo.account=%.*s",arg->userid,account.size(),account.begin());
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
		if(dsm->allow_nickname_contain_account || dsm->GetAid() == SHENDIAO_AID || dsm->GetAid() == PUKE_AID)
		{
		   GNET::Marshal::OctetsStream os;
		   os << nickname;
		   os << account;
		   rep.nickname = os;
		}
		else
			rep.nickname = nickname;

		std::vector<int> * pauth = gdeliverd::GMContainer::GetInstance()->GetGMInfo(arg->userid);
		if(pauth)
		{
			rep.blisgm = 1;
			rep.auth = *pauth;
			gdeliverd::DeliverServer::log.warn("UserLogin2:TryLogin:user(id=%d) is GM,trylogin,auth.size=%d",arg->userid,rep.auth.size());
		}
		gdeliverd::DeliverServer::log.warn("UserLogin2:TryLogin:user(id=%d) UserInfoRep.rep.nickname=%.*s",arg->userid,rep.nickname.size(),rep.nickname.begin());
		if(!dbs->Send(dbs->GetSid(),rep))
			gdeliverd::DeliverServer::log.warn("UserLogin2:TryLogin:user(id=%d) send UserInfoRep to gs(db) failed.",arg->userid);
		dsm->RemoveSessionInfo(link_sid,client_sid);
	}

	void SendCertVerify()
	{
	//	gnet::UserLogin2Arg* arg = getArgument();
		CertVerify cv;
		gdeliverd::AuthClient *acm = gdeliverd::AuthClient::GetInstance();
		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		cv.localsid = client_sid;
		cv.authd_cert.replace(acm->authd_cert.begin(),acm->authd_cert.size());
		dsm->Send(link_sid,cv);
	}

	/*
	void SendGame2AU()
	{
		gnet::UserLogin2Arg* arg = getArgument();
		Game2AU ga;
		ga.userid = arg->userid;
		ga.qtype = 4;
		GNET::Marshal::OctetsStream os;
		int cash = 700;
		int paytype = 1;
		os << cash;
		os << paytype;
		ga.info = os;
		ga.reserved = 0;
		gdeliverd::AuthClient *acm = gdeliverd::AuthClient::GetInstance();
		if(acm->SendProtocol(ga))
			GNET::Log::log(LOG_INFO,"--------user(id=%d) send Game2AU OK",arg->userid);
	}
	*/

	void OnTimeout()
	{
		// client only
		if(is_trylogin)
			return;
		gnet::UserLogin2Arg* arg = getArgument();
		gdeliverd::DeliverServer::log.warn("UserLogin2:OnTimeout:user(id=%d) UserLogin2 timeout.",arg->userid);
		TryLogin(GNET::ERR_AU_DELIVER_TIMEOUT);
	}

};

}
