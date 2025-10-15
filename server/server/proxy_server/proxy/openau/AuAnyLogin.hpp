
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/openau/_depends_AuAnyLogin_.hpp"
#include "gnet/Constants.hpp"
#include "gnet/MatrixPasswdRep.hpp"
#include "AuAnyClient.hpp"
#include "DeliverServer.hpp"

namespace openau {

class AuAnyLogin : public GNET::Rpc
{
public:
#include "rpcgen/openau/AuAnyLogin.inc"

	int link_sid;
	int client_sid;
	bool bl_tryresponse;

	void makePasswd(GNET::Octets& account,GNET::Octets& ticket,GNET::Octets& response)
	{
		GNET::MD5Hash md5;
		md5.Update(account);
		md5.Update(ticket);
		md5.Final(response);
	}


	void OnServer(Manager *, Manager::Session::ID)
	{
		//openau::AuAnyLoginArg* arg = getArgument();
		//openau::AuAnyLoginRes* res = getResult();
	}

	void OnClient(Manager *, Manager::Session::ID)
	{
		openau::AuAnyLoginArg* arg = getArgument();
		openau::AuAnyLoginRes* res = getResult();
		gdeliverd::AuAnyClient::log.info("AuAnyLogin:OnClient:user(account=%.*s,realaccount=%.*s,id=%d) retcode=%d", 
										 arg->account.size(), (char*)arg->account.begin(), res->realaccount.size(), (char*)res->realaccount.begin(), res->userid, res->retcode);
		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		gdeliverd::SessionInfo* sinfo = dsm->GetSessionInfo(link_sid,client_sid);
		if(NULL == sinfo)
			return;

		if(res->retcode != ErrorCodes::error_succeed)
		{
			if(res->retcode == ErrorCodes::error_invalid)
			{
				if(res->realaccount.size() > 0)
				{
					if(dsm->GetPasswdLimit().add(res->realaccount))
						dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_INVALID_PASSWORD,"AuAnyLogin password error.");
					else
						dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_PASSWD_EXCEED,"AuAnyLogin password error exceed limit.");
				}
				else
				{
					if(dsm->GetPasswdLimit().add(sinfo->identity))
						dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_INVALID_PASSWORD,"AuAnyLogin password error.");
					else
						dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_PASSWD_EXCEED,"AuAnyLogin password error exceed limit.");
				}
			}
			else
				dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_AUANY_LOGIN_EXCEPTION,"AuAnyLogin args or platform exception.");
			return;
		}
		bool blisgm = false;
#ifdef ALL_GM
		blisgm = true;
#endif
		//GM can enter game even if online_num > max_player_num
		gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
		if(!blisgm && uc->Size() >= uc->GetPlayerLimit())
		{
			dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_SERVEROVERLOAD,"online user exceed MAX_PLAYER_NUM");
			return;
		}
		GNET::Octets response;
		makePasswd(arg->account,sinfo->response,response);
		SendMatrixPasswdRep(response);
		sinfo->logintype = gnet::AUANY_LOGIN;
		//	gdeliverd::UserType::GetInstance()->SetUserType(res->userid,0,0,2,GNET::Octets());
		gdeliverd::UserType::GetInstance()->SetUserType(res->userid,0,0,2,res->platinfo);

		if(res->realaccount.size() > 0)
		{
			sinfo->identity.replace(res->realaccount.begin(),res->realaccount.size()); //add xys 2014-04-29
			gdeliverd::AuAnyClient::log.warn("AuAnyLogin:OnClient:replace with realaccount=%.*s",
											 res->realaccount.size(), res->realaccount.begin());
		}

	}

	void SendMatrixPasswdRep(GNET::Octets & response)
	{
		openau::AuAnyLoginRes* res = getResult();
		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		gnet::MatrixPasswdRep rep(client_sid,0,res->userid,response,0,0,0);
		dsm->Send(link_sid,rep);
	}

	void tryResponse()
	{
		if(bl_tryresponse)
			return;
		bl_tryresponse = true;
		gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		gdeliverd::SessionInfo* sinfo = dsm->GetSessionInfo(link_sid,client_sid);
		if(NULL == sinfo)
			return;
		dsm->SessionError(0,link_sid,client_sid,GNET::ERR_AUANY_LOGIN_NET_ERROR,"gdeliverd not connect to AuAnyServer or timeout.");
	}

	void OnTimeout()
	{
		if(bl_tryresponse)
			return;
		openau::AuAnyLoginArg* arg = getArgument();
		gdeliverd::AuAnyClient::log.warn("AuAnyLogin:OnTimeout:user(account=%.*s) AuAnyLogin timeout.",
										 arg->account.size(), (char*)arg->account.begin());
		tryResponse();
	}

};

}
