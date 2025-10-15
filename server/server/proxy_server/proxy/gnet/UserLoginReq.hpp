
#pragma once

#include "rpcgen.hpp"
#include "UserLogin.hpp"
#include "UserLogin2.hpp"
#include "KickoutUser.hpp"
#include "UserLogout.hpp"
#include "netplayer.h"
#include "mapusbkey.h"
#include "mapphonetoken.h"
#include "ForceLoginReq.hpp"
#include "forcelogin.h"
#include "DeliverServer.hpp"

namespace gnet { 

class UserLoginReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/UserLoginReq.inc"

	virtual void Process(Manager *, Manager::Session::ID sid)
	{
		//TODO forbid Login
		//TODO kick out
		gdeliverd::DeliverServer::log.info("UserLoginReq:Process:user(id=%d) receive UserLoginReq,localsid=%d,blkickuser=%d",
										   argument.userid, argument.localsid, argument.blkickuser);
		gdeliverd::SessionInfo* sinfo = gdeliverd::DeliverServer::GetInstance()->GetSessionInfo(sid,argument.localsid);
		if(NULL == sinfo)
			return;

		if(CheckBeForbid(argument.userid,sid,argument.localsid))
			return;
		if(CheckBeMultiLogin(sid,argument.localsid,argument.userid,argument.blkickuser))
			return;

		gdeliverd::AuthClient* acm = gdeliverd::AuthClient::GetInstance();
		if(sinfo->logintype == CROSS_LOGIN)
		{
			GNET::Octets used_elec_number;
			UserLogin2Arg arg(argument.userid,argument.localsid,argument.blkickuser,argument.freecreatime,used_elec_number,0,GNET::Octets());
			UserLogin2 *rpc = (UserLogin2*)GNET::Rpc::Call(UserLogin2::PROTOCOL_TYPE,arg);
			rpc->link_sid = sid;
			rpc->client_sid = argument.localsid;
			rpc->is_trylogin = false;
			gdeliverd::DeliverServer::log.warn("UserLoginReq:Process:user(id=%d) Cross UserLogin.",argument.userid);
			rpc->TryLogin(GNET::ERR_SUCCESS);
			return;
		}

#ifdef USE_AUANY
		if(sinfo->logintype == AUANY_LOGIN)
		{
			GNET::Octets used_elec_number;
			UserLogin2Arg arg(argument.userid,argument.localsid,argument.blkickuser,argument.freecreatime,used_elec_number,0,GNET::Octets());
			UserLogin2 *rpc = (UserLogin2*)GNET::Rpc::Call(UserLogin2::PROTOCOL_TYPE,arg);
			rpc->link_sid = sid;
			rpc->client_sid = argument.localsid;
			rpc->is_trylogin = false;
			gdeliverd::DeliverServer::log.warn("UserLoginReq:Process:user(id=%d) AuAny UserLogin2 TryLogin.",argument.userid);
			rpc->TryLogin(GNET::ERR_SUCCESS);
		}
		return;
#endif

		if(0 == gdeliverd::AuthClient::GetInstance()->GetAbroadPolicy())
		{
			GNET::Octets used_elec_number;
			//	if(sinfo->isElecLogin)
			if(sinfo->logintype == ELEC_LOGIN)
			{
				switch(sinfo->algorithm & gnet::ALGORITHM_MASK_HIGH)
				{
					case gnet::ALGORITHM_USBKEY2:
						{
							gdeliverd::UsbKeyCache::GetInstance()->GetUsedElecNumber(argument.userid,used_elec_number);
							break;
						}
					case gnet::ALGORITHM_PHONETOKEN:
						{
							gdeliverd::PhoneTokenCache::GetInstance()->GetUsedElecNumber(argument.userid,used_elec_number);
							break;
						}
					default:
						break;
				}
			}
			UserLogin2Arg arg(argument.userid,argument.localsid,argument.blkickuser,argument.freecreatime,used_elec_number,0,GNET::Octets());
			UserLogin2 *rpc = (UserLogin2*)GNET::Rpc::Call(UserLogin2::PROTOCOL_TYPE,arg);
			rpc->link_sid = sid;
			rpc->client_sid = argument.localsid;
			if(!acm->SendProtocol(rpc))
			{
				gdeliverd::DeliverServer::log.warn("UserLoginReq:Process:user(id=%d) send UserLogin2 failed.",argument.userid);
				rpc->TryLogin(GNET::ERR_NOT_CONNECT_AU);
			}
		}
		else
		{
			UserLogin *rpc = (UserLogin*)GNET::Rpc::Call(UserLogin::PROTOCOL_TYPE,argument);
			rpc->link_sid = sid;
			rpc->client_sid = argument.localsid;

			if(!acm->SendProtocol(rpc))
			{
				gdeliverd::DeliverServer::log.warn("UserLoginReq:Process:user(id=%d) send UserLogin failed.",argument.userid);
				rpc->TryLogin(GNET::ERR_NOT_CONNECT_AU);
			}
		}	
	}

	bool CheckBeForbid(int userid,Manager::Session::ID link_sid,Manager::Session::ID client_sid)
	{
		gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		gdeliverd::ForbidLogin * forbidlogin = gdeliverd::ForbidLogin::GetInstance();
		RoleForbid forbid;
		if(forbidlogin->GetForbid(userid,forbid))
		{
			gdeliverd::DeliverServer::log.info("UserLoginReq:CheckBeForbid:user(id=%d) is FORBID_LOGIN,time(second)=%d",
											   userid, forbid.time);
			dsm->Send(link_sid,AnnounceForbidInfo(userid,client_sid,forbid));
			dsm->RemoveSessionInfo(link_sid,client_sid);
			return true;
		}
		return false;	
	}

	bool CheckBeMultiLogin(Manager::Session::ID link_sid,Manager::Session::ID client_sid,int userid,int blkickuser)
	{
		gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		gdeliverd::UserContainer *uc = gdeliverd::UserContainer::GetInstance();
		gdeliverd::UserInfo* uinfo = uc->find(userid);
		if(NULL == uinfo)
			return false;
		if(!blkickuser)
		{
			gdeliverd::DeliverServer::log.warn("UserLoginReq:CheckBeMultiLogin:user(id=%d) already online,blkickuser=0,new login failed,new localsid=%d",userid,client_sid);
			dsm->SessionError(userid,link_sid,client_sid,GNET::ERR_MULTILOGIN,"multilogin");
			return true;
		}
		else
		{
			if(!dsm->getSupportForceLogin())
			{
				gdeliverd::DeliverServer::log.warn("UserLoginReq:CheckBeMultiLogin:user(id=%d) already online,blkickuser=1,kickout old login,old localsid=%d",userid,uinfo->client_sid);
				dsm->Send(uinfo->link_sid,KickoutUser(userid,uinfo->client_sid,GNET::ERR_LOGOUT_BY_BLKICK));
				dsm->RemoveSessionInfo(uinfo->link_sid,uinfo->client_sid);
				uc->erase(userid);
				return false;
			}
			else
			{
				gdeliverd::UserInfo* forcelogin_uinfo = gdeliverd::ForceLogin::GetInstance()->GetForceLogin(userid); 
				if(NULL != forcelogin_uinfo)
				{
					gdeliverd::DeliverServer::log.warn("UserLoginReq:CheckBeMultiLogin:user(id=%d) already in ForceLogin state,new login failed,new localsid=%d",userid,client_sid);
					dsm->SessionError(userid,link_sid,client_sid,GNET::ERR_MULTILOGIN,"multilogin");
					return true;

				}
				gdeliverd::ForceLogin::GetInstance()->PutForceLogin(userid,client_sid,link_sid);
				dsm->Send(uinfo->link_sid,ForceLoginReq(userid,uinfo->client_sid,0));
				gdeliverd::DeliverServer::log.warn("UserLoginReq:CheckBeMultiLogin:user(id=%d) already online,ForceLoginReq to old login,old localsid=%d",userid,uinfo->client_sid);
				return true;//just end, don't return anything for UserLoginReq
			} 	
		}
	}
};

} 
