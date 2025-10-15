
#pragma once

#include "rpcgen.hpp"
#include "MatrixPasswd.hpp"
#include "MatrixPasswd2.hpp"
#include "MatrixPasswdRep.hpp"
#include "rpcgen/gnet/MatrixPasswdArg.hpp"
#include "rpcgen/gnet/MatrixPasswdRes.hpp"
#include "Constants.hpp"
#include "openau/AuAnyLogin.hpp"
#include "openau/AuAnyLoginArg.hpp"
#include "openau/AuAnyLoginRes.hpp"
#include "AuAnyClient.hpp"
#include "DeliverServer.hpp"

namespace gnet {

class MatrixPasswdReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixPasswdReq.inc"

	virtual void Process(Manager *, Manager::Session::ID sid)
	{
		std::string account((char*)info.account.begin(),info.account.size());
		gdeliverd::DeliverServer::log.info("MatrixPasswdReq:Process:user(account=%s)",account.c_str());

		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();

		if(!dsm->GetPasswdLimit().ask(info.account))
		{
			dsm->SessionError(0,sid,localsid,GNET::ERR_PASSWD_EXCEED,"password error exceed limit");
			return;
		}

		gdeliverd::SessionInfo sinfo;

		bool sso = false;
		if(account.rfind("@sso") != std::string::npos)
		{
			sso = true;
			sinfo.logintype = SSO_LOGIN;
		}

		sinfo.loginip = info.loginip;
		sinfo.challenge.replace(info.challenge.begin(),info.challenge.size());
		sinfo.identity.replace(info.account.begin(),info.account.size());
		sinfo.response.replace(response.begin(),response.size());
		dsm->InsertSessionInfo(sid,localsid,sinfo);

		gdeliverd::UserIdentityCache::Identity iden;
		gdeliverd::UserIdentityCache* ucache = gdeliverd::UserIdentityCache::GetInstance();
		if(ucache->Find(info.account,iden))
		{
			if(iden.loginip != info.loginip)
			{
				gdeliverd::DeliverServer::log.warn("MatrixPasswdReq:Process:user(account=%s),cross login,but loginip not equal,loginip=%d,cross_loginip=%d",
												   account.c_str(),info.loginip,iden.loginip);
			}
			gdeliverd::SessionInfo* psinfo = dsm->GetSessionInfo(sid,localsid);
			if(NULL == psinfo)
				return;

			psinfo->logintype = CROSS_LOGIN;
			//	gdeliverd::UserSrc::GetInstance()->setUserSrcZoneid(iden.userid,iden.src_zoneid);
			gdeliverd::UserIdentityCache::GetInstance()->Remove(info.account);
			//don't care abroad, all use MatrixPasswd
			MatrixPasswd* rpc = (MatrixPasswd*)GNET::Rpc::Call(MatrixPasswd::PROTOCOL_TYPE,info);
			rpc->link_sid   = sid;
			rpc->client_sid = localsid;
			rpc->bl_tryresponse = false;
			rpc->bl_ssologin = false;
			rpc->bl_crosslogin = true;
			rpc->crossResponse(iden);
			gdeliverd::DeliverServer::log.info("MatrixPasswdReq:Process:user,rpc->crossResponse(iden).");
			return;
		}
		else
		{
#ifdef CROSS_SERVER

			dsm->SessionError(0,sid,localsid,GNET::ERR_COMMUNICATION,"no ticket find.");
			return;
#else

#ifdef USE_AUANY
			gdeliverd::AuAnyClient* aacm = gdeliverd::AuAnyClient::GetInstance();
			openau::AuAnyLoginArg arg(
                info.account,
                response,
                info.challenge,
                info.loginipoctets,
                info.loginip,
                //aacm->getPlatType(),
				info.logintype,
                info.gameid,
                info.gamekey,
                info.serverid,
                info.selfchannel,
                info.childchannel,
                info.deviceid);
			openau::AuAnyLogin* rpc = (openau::AuAnyLogin*)GNET::Rpc::Call(openau::AuAnyLogin::PROTOCOL_TYPE,arg);
			rpc->link_sid   = sid;
			rpc->client_sid = localsid;
			rpc->bl_tryresponse = false;
			gdeliverd::DeliverServer::log.info("MatrixPasswdReq:Process:rpc.AuAnyLogin account=%s,logintype=%d.",
														   account.c_str(),info.logintype);
			if(!aacm->SendProtocol(rpc))
			{
				rpc->tryResponse();
			}
			return;
#endif


			if(gdeliverd::AuthClient::GetInstance()->GetAbroadPolicy() == 0)
			{
				MatrixPasswd2Arg arg(info.account,info.challenge,info.loginip);
				MatrixPasswd2* rpc = (MatrixPasswd2*)GNET::Rpc::Call(MatrixPasswd2::PROTOCOL_TYPE,arg);
				rpc->link_sid   = sid;
				rpc->client_sid = localsid;
				rpc->bl_tryresponse = false;
				rpc->bl_ssologin = sso;

				if(rpc->bl_ssologin)
				{
					if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
					{
						dsm->SessionError(0,sid,localsid,GNET::ERR_COMMUNICATION,"sso login send MatrixPasswd2 failed.");
						gdeliverd::DeliverServer::log.warn("MatrixPasswdReq:Process:user(account=%s) is SSO login,send MatrixPasswd2 failed.",
														   account.c_str());
					}
					return;
				}
				if(dsm->GetAuRequestLimit().add(info.account))
				{
					if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
					{
						gdeliverd::DeliverServer::log.warn("MatrixPasswdReq:Process:user(account=%s) send MatrixPasswd2 failed.",
														   account.c_str());
						rpc->TryResponse(GNET::ERR_NOT_CONNECT_AU);
					}
				}
				else
				{
					if(!dsm->forbid_client_autologin)
					{
						gdeliverd::DeliverServer::log.warn("MatrixPasswdReq:Process:user(account=%s) MatrixPasswdReq exceed limit,using cache.",
														   account.c_str());
						rpc->TryResponse(GNET::ERR_CLIENT_AUTO_LOGIN);
					}
					else
					{
						dsm->SessionError(0,sid,localsid,GNET::ERR_LOGIN_EXCEED_LIMIT,"login exceed limit,failed.");
					}
				}
			}
			else
			{
				MatrixPasswd* rpc = (MatrixPasswd*)GNET::Rpc::Call(MatrixPasswd::PROTOCOL_TYPE,info);
				rpc->link_sid   = sid;
				rpc->client_sid = localsid;
				rpc->bl_tryresponse = false;
				rpc->bl_ssologin = sso;

				if(rpc->bl_ssologin)
				{
				    GNET::Log::log(LOG_INFO,"user rpc->bl_ssologin.");
					if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
					{
						dsm->SessionError(0,sid,localsid,GNET::ERR_COMMUNICATION,"sso login send MatrixPasswd failed.");
						gdeliverd::DeliverServer::log.warn("MatrixPasswdReq:Process:user(account=%s) is SSO login,send MatrixPasswd failed.",
														   account.c_str());
					}
					return;
				}

				if(dsm->GetAuRequestLimit().add(info.account))
				{
					if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
					{
						gdeliverd::DeliverServer::log.warn("MatrixPasswdReq:Process:user(account=%s) send MatrixPasswd failed.",
														   account.c_str());
						rpc->TryResponse(GNET::ERR_NOT_CONNECT_AU);
					}
				}
				else
				{
					if(!dsm->forbid_client_autologin)
					{
						gdeliverd::DeliverServer::log.warn("MatrixPasswdReq:Process:user(account=%s) MatrixPasswdReq exceed limit,using cache.",
														   account.c_str());
						rpc->TryResponse(GNET::ERR_CLIENT_AUTO_LOGIN);
					}
					else
					{
						dsm->SessionError(0,sid,localsid,GNET::ERR_LOGIN_EXCEED_LIMIT,"login exceed limit,failed.");
					}

				}
			}
#endif
		}
	}
};
}
