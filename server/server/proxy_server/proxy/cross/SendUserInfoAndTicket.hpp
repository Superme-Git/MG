
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/cross/_depends_SendUserInfoAndTicket_.hpp"
#include "GameDBServer.hpp"
#include "netplayer.h"
#include "CrossClient.hpp"
#include "cross/Constants.hpp"
#include "SendUserInfoAndTicket_Re.hpp"
#include "mapusertype.h"
#include "gmcontainer.h"

namespace cross { 

static const int TICKET_LEN = 32;

class SendUserInfoAndTicket : public GNET::Protocol
{
public:
	#include "rpcgen/cross/SendUserInfoAndTicket.inc"
	void makePasswd(GNET::Octets& account,GNET::Octets& ticket,GNET::Octets& passwd)
	{
		GNET::MD5Hash md5;
		md5.Update(account);
		md5.Update(ticket);
		md5.Final(passwd);
	}

	virtual void Process(Manager *manager, Manager::Session::ID sid) 
	{ 
		/* protocol handle */ 
		SendUserInfoAndTicket_Re re;
		re.retcode = SendUserInfoAndTicket_Re::RET_OK;
		re.userid = this->userinfo.userid;
		re.flag = this->flag;
		char ip[32];
		struct in_addr addr;
		addr.s_addr = userinfo.loginip;
		strcpy(ip,inet_ntoa(addr));
#ifdef CROSS_SERVER
		if(flag == TO_CROSS)
		{
			gdeliverd::UserInfo* uinfo = gdeliverd::UserContainer::GetInstance()->find(userinfo.userid);
			if(uinfo != NULL)
			{
				re.retcode = SendUserInfoAndTicket_Re::RET_MULTI_LOGIN;
			}
			else
			{
				gdeliverd::UserIdentityCache * pcache = gdeliverd::UserIdentityCache::GetInstance();
			/*	if(pcache->Exist(account))
				{
					re.retcode = SendUserInfoAndTicket_Re::RET_MULTI_LOGIN;
				}
				else
			*/	{
					GNET::Octets passwd;
					makePasswd(account,ticket,passwd);
					gdeliverd::CrossServer * csm = gdeliverd::CrossServer::GetInstance();
					int src_zoneid = csm->getZoneidBySid(sid);
					gdeliverd::UserSrc::GetInstance()->setUserSrcZoneid(userinfo.userid,src_zoneid);
#ifdef USE_AUANY	
					gdeliverd::UserIdentityCache::Identity iden(src_zoneid,userinfo.userid,userinfo.algorithm,ticket,userinfo.loginip);
#else					
					gdeliverd::UserIdentityCache::Identity iden(src_zoneid,userinfo.userid,userinfo.algorithm,passwd,userinfo.loginip);
#endif
					pcache->Insert(account,iden);
					gdeliverd::UserType::GetInstance()->SetUserType(userinfo.userid,userinfo.func,userinfo.funcparm,userinfo.gender,userinfo.nickname);
					if(userinfo.blisgm > 0)
						gdeliverd::GMContainer::GetInstance()->InsertGMInfo(userinfo.userid,userinfo.auth);
				}
			}

			if(re.retcode != SendUserInfoAndTicket_Re::RET_OK)
				manager->Send(sid,re);
			else
			{
				gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
				dbs->Send(dbs->GetSid(),this);
			}
		}	
		else if(flag == FROM_CROSS)
		{
			gdeliverd::UserInfo* uinfo = gdeliverd::UserContainer::GetInstance()->find(userinfo.userid);
			if(uinfo == NULL)
				return;
			this->account = uinfo->account;

			gdeliverd::CrossServer * csm = gdeliverd::CrossServer::GetInstance();
			int src_zoneid = gdeliverd::UserSrc::GetInstance()->getSrcZoneidByUserid(userinfo.userid);
			gdeliverd::ZoneSessionManager::ZoneSession * pzonesession = csm->getZoneSession(src_zoneid);
			if(pzonesession == NULL)
			{
				re.retcode = SendUserInfoAndTicket_Re::RET_NETWORK_ERROR;
				manager->Send(sid,re);
			}
			else
			{
				csm->Send(pzonesession->getSid(),this);
			}
		}
		GNET::Log::log(LOG_INFO,"CrossDeliver--user(id=%d) receive SendUserInfoAndTicket flag=%d,loginip=%s,retcode=%d",userinfo.userid,flag,ip,re.retcode);

#else
		if(flag == TO_CROSS)
		{
			gdeliverd::UserInfo* uinfo = gdeliverd::UserContainer::GetInstance()->find(userinfo.userid);
			if(uinfo == NULL)
				return;
			this->account = uinfo->account;//assign account

			gdeliverd::CrossClient * ccm = gdeliverd::CrossClient::GetInstance();
			if(ccm->getBlHandshake())
			{
				if(ccm->BlCrossOverload())
				{
					re.retcode = SendUserInfoAndTicket_Re::RET_SERVER_OVERLOAD;
				}
				else
				{
					gdeliverd::CrossClient::GetInstance()->SendProtocol(this);
					GNET::Log::log(LOG_INFO,"user(id=%d) SenduserInfoAndTicket to CrossDeliver,loginip=%s",userinfo.userid,ip);
				}
			}
			else
			{
				re.retcode = SendUserInfoAndTicket_Re::RET_NETWORK_ERROR;
			}

			if(re.retcode != SendUserInfoAndTicket_Re::RET_OK)
			{
				gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
				dbs->Send(dbs->GetSid(),re);
			}
		}	
		else if(flag == FROM_CROSS)
		{
			gdeliverd::UserInfo* uinfo = gdeliverd::UserContainer::GetInstance()->find(userinfo.userid);
			if(uinfo != NULL)
			{
				re.retcode = SendUserInfoAndTicket_Re::RET_MULTI_LOGIN;
			}
			else
			{
				gdeliverd::UserIdentityCache * pcache = gdeliverd::UserIdentityCache::GetInstance();
			/*	if(pcache->Exist(account))
				{
					re.retcode = SendUserInfoAndTicket_Re::RET_MULTI_LOGIN;
				}
				else
			*/	{
					GNET::Octets passwd;
					makePasswd(account,ticket,passwd);
					gdeliverd::CrossClient * ccm = gdeliverd::CrossClient::GetInstance();
#ifdef USE_AUANY
					gdeliverd::UserIdentityCache::Identity iden(ccm->getCrossZoneid(),userinfo.userid,userinfo.algorithm,ticket,userinfo.loginip);
#else
					gdeliverd::UserIdentityCache::Identity iden(ccm->getCrossZoneid(),userinfo.userid,userinfo.algorithm,passwd,userinfo.loginip);

#endif	
					pcache->Insert(account,iden);

					gdeliverd::UserType::GetInstance()->SetUserType(userinfo.userid,userinfo.func,userinfo.funcparm,userinfo.gender,userinfo.nickname);
					if(userinfo.blisgm > 0)
						gdeliverd::GMContainer::GetInstance()->InsertGMInfo(userinfo.userid,userinfo.auth);
				}
			}

			if(re.retcode != SendUserInfoAndTicket_Re::RET_OK)
				manager->Send(sid,re);
			else
			{
				gdeliverd::GameDBServer * dbs = gdeliverd::GameDBServer::GetInstance();
				dbs->Send(dbs->GetSid(),this);
			}
		}
		GNET::Log::log(LOG_INFO,"CrossClient--user(id=%d) receive SendUserInfoAndTicket flag=%d,loginip=%s,retcode=%d",userinfo.userid,flag,ip,re.retcode);
#endif
	}

};

} 
