
#pragma once
#include "rpcgen.hpp"
#include "MatrixPasswdRep.hpp"
#include "mappasswd.h"
#include "cardchecker.h"
#include "mobilechecker.h"
#include "DeliverServer.hpp"
#include "GameDBServer.hpp"
#include "AuthClient.hpp"
#include "rpcgen/gnet/MatrixPasswdArg.hpp"
#include "rpcgen/gnet/MatrixPasswdRes.hpp"
#include "DeliverAnnounceToLink.hpp"
#include "netplayer.h"
#include "mapforbid.h"
#include "AnnounceForbidInfo.hpp"
#include "Constants.hpp"

namespace gnet {

   class MatrixPasswd : public GNET::Rpc
   {
      public:
#include "rpcgen/gnet/MatrixPasswd.inc"

	 int link_sid;
	 int client_sid;
	 bool bl_tryresponse;
	 bool bl_ssologin;
	 bool bl_crosslogin;

	 void CalcResponse( )
	 {
	    gnet::MatrixPasswdArg* arg = getArgument();
	    gnet::MatrixPasswdRes* res = getResult();

	    GNET::HMAC_MD5Hash hash;
	    hash.SetParameter(res->response);
	    hash.Update(arg->challenge);
	    hash.Final(res->response);
	 }

	 void makePasswd(GNET::Octets& account,GNET::Octets& ticket,GNET::Octets& response)
	 {
		 GNET::MD5Hash md5;
		 md5.Update(account);
		 md5.Update(ticket);
		 md5.Final(response);
	 }

	 //when one ip occur several times of errcode=2or3,Announce link to forbid this ip for seconds
	 void CheckIPForbid(int ip,int type)
	 {
		 //1 add one time, 2 add two times
		 if(type != 1 && type != 2)
			 return;
		 gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		 bool ret = dsm->GetLockIpLimit().add(ip);
		 if(!ret)
		 {
			 DeliverAnnounceToLink ann(ip);
			 dsm->Send(link_sid,ann);
			 return;
		 }
		 else if(type == 2 && !dsm->GetLockIpLimit().add(ip))
		 {
			 DeliverAnnounceToLink ann(ip);
			 dsm->Send(link_sid,ann);
		 }
	 }

	 void OnServer(Manager *, Manager::Session::ID )
	 {
		 //gnet::MatrixPasswdArg* arg = getArgument();
		 //gnet::MatrixPasswdRes* res = getResult();
	 }

	 void OnClient(Manager *, Manager::Session::ID )
	 {
		 //TODO
		 gnet::MatrixPasswdArg* arg = getArgument();
		 gnet::MatrixPasswdRes* res = getResult();
		 GNET::Log::log(LOG_INFO,"user(account=%.*s,id=%d) receive MatrixPasswd,algorithm=0x%0x,retcode=%d",arg->account.size(),(char*)arg->account.begin(),res->userid,res->algorithm,res->retcode);
		 gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		 gdeliverd::SessionInfo* sinfo = dsm->GetSessionInfo(link_sid,client_sid);
		 if(NULL == sinfo)
			 return;

		 if(res->retcode == GNET::ERR_INVALID_ACCOUNT)
		 {
			 CheckIPForbid(arg->loginip,2);
			 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_INVALID_ACCOUNT,"Invalid account.");
			 gdeliverd::Passwd::GetInstance()->DelPasswd(arg->account);
			 return;
		 }

		 if(res->retcode == GNET::ERR_SUCCESS)
		 {
			 //cache passwd when algorithm contains cache signal
			 if((res->algorithm & gnet::ALGORITHM_OPT_NOCACHE) == 0)
				 gdeliverd::Passwd::GetInstance()->SetPasswd( arg->account, res->userid, res->algorithm, res->response, res->matrix);
			 else if((res->algorithm & gnet::ALGORITHM_OPT_TICKETS) == 0)
				 gdeliverd::Passwd::GetInstance()->DelPasswd(arg->account);
		 }
		 else
		 {
			 dsm->SessionError(res->userid,link_sid,client_sid,res->retcode,"MatrixPasswdRes.retcode != 0");
			 return;
		 }

		 bool blisgm = false;
#ifndef ALL_GM
		 if((res->algorithm & gnet::ALGORITHM_GM_ACCOUNT) == gnet::ALGORITHM_GM_ACCOUNT)
			 blisgm = true;
#else
		 blisgm = true;
#endif
		 //GM can enter game even if online_num > max_player_num
		 gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
		 if(!blisgm && uc->Size() >= uc->GetPlayerLimit())
		 {
			 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_SERVEROVERLOAD,"online user exceed MAX_PLAYER_NUM");
			 return;
		 }

		 GNET::Octets response(res->response.begin(),res->response.size());
		 sinfo->userid = res->userid;
		 sinfo->algorithm = res->algorithm;
		 //check passwd
		 CalcResponse();
		 if(res->response != sinfo->response)
		 {
			 CheckIPForbid(arg->loginip,2);
			 //check if password error exceed limit
			 if(dsm->GetPasswdLimit().add(sinfo->identity))
				 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_INVALID_PASSWORD,"password error.");
			 else
				 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_PASSWD_EXCEED,"password error exceed limit.");
			 return;
		 }

		 switch(res->algorithm & gnet::ALGORITHM_MASK_HIGH)
		 {
			 case gnet::ALGORITHM_CARD:
				 {
					 if(!dsm->GetMatrixLimit().ask(res->userid))
					 {
						 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_MATRIX_EXCEED,"mibao card error exceed limit.");
						 return;
					 }

					 MatrixChecker * checker = new CardChecker(
							 res->userid,
							 GNET::byteorder_32(sinfo->loginip),
							 (unsigned char*)res->matrix.begin()
							 );
					 SendMatrixPasswdRep(response,checker);
					 break;
				 }
			 case gnet::ALGORITHM_HANDSET:
				 {
					 MatrixChecker * checker = MobileCheckerFactory::Instance().Create(
							 res->userid,
							 GNET::byteorder_32(sinfo->loginip),
							 res->algorithm, res->matrix
							 );
					 SendMatrixPasswdRep(response,checker);
					 break;
				 }
			 default:
				 {
					 SendMatrixPasswdRep(response,NULL);
					 break;
				 }
		 }
	 }

	 bool CheckBeForbid()
	 {
		 gnet::MatrixPasswdRes* res = getResult();
		 gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		 gdeliverd::ForbidLogin * forbidlogin = gdeliverd::ForbidLogin::GetInstance();
		 RoleForbid forbid;
		 if(forbidlogin->GetForbid(res->userid,forbid))
		 {
			 GNET::Log::log(LOG_INFO,"user(id=%d) is FORBID_LOGIN,time(second)=%d",res->userid,forbid.time);
			 dsm->Send(link_sid,AnnounceForbidInfo(res->userid,client_sid,forbid));
			 dsm->RemoveSessionInfo(link_sid,client_sid);
			 return true;
		 }
		 return false;
	 }

	 void TryResponse(int reason)
	 {
		 if(bl_tryresponse || bl_ssologin)
			 return;
		 bl_tryresponse = true;

		 gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		 gdeliverd::SessionInfo* sinfo = dsm->GetSessionInfo(link_sid,client_sid);
		 if(NULL == sinfo)
			 return;
		 gnet::MatrixPasswdArg* arg = getArgument();
		 gnet::MatrixPasswdRes* res = getResult();
		 GNET::Log::log(LOG_WARNING,"user(account=%.*s) MatrixPasswd tryResponse.",arg->account.size(),arg->account.begin());
		 GNET::Log::log(LOG_INFO,"user(account=%.*s) MatrixPasswd logintype=%d",arg->account.size(),arg->account.begin(),arg->logintype);

		 //find account's passwd in cache
		 if(!gdeliverd::Passwd::GetInstance()->GetPasswd(arg->account,res->userid,res->algorithm,res->response,res->matrix))
		 {
			 GNET::Log::log(LOG_WARNING,"user(account=%.*s) MatrixPasswd tryResponse failed,password cache not found.",arg->account.size(),(char*)arg->account.begin());
			 //check if ip should be forbidden,add one times
			 CheckIPForbid(arg->loginip,1);
			 switch(reason)
			 {
				 case GNET::ERR_AU_DELIVER_TIMEOUT:
					 {
						 dsm->SessionError(0,link_sid,client_sid,reason,"MatrixPasswd timeout and password cache not found.");
						 break;
					 }
				 case GNET::ERR_NOT_CONNECT_AU:
					 {

						 dsm->SessionError(0,link_sid,client_sid,reason,"cert verify not finish or gdeliverd not connect AU.");
						 break;
					 }
				 case GNET::ERR_CLIENT_AUTO_LOGIN:
					 {
						 dsm->SessionError(0,link_sid,client_sid,reason,"client auto login and password cache not found.");

						 break;
					 }
			 }
			 return;
		 }

		 GNET::Log::log(LOG_WARNING,"TryResponse (userid=%d) ",res->userid);

		 if(CheckBeForbid())
			 return;

		 bool blisgm = false;
#ifndef ALL_GM
		 if((res->algorithm & gnet::ALGORITHM_GM_ACCOUNT) == gnet::ALGORITHM_GM_ACCOUNT)
			 blisgm = true;
#else
		 blisgm = true;
#endif
		 gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
		 if(!blisgm && uc->Size() >= uc->GetPlayerLimit())
		 {
			 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_SERVEROVERLOAD,"online user exceed MAX_PLAYER_NUM");
			 return;
		 }
		 GNET::Octets response(res->response.begin(),res->response.size());
		 sinfo->userid = res->userid;
		 sinfo->algorithm = res->algorithm;

		 CalcResponse();
/*		 if(res->response != sinfo->response)
		 {
			 CheckIPForbid(arg->loginip,1);
			 //check if password error exceed limit
			 if(dsm->GetPasswdLimit().add(sinfo->identity))
				 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_INVALID_PASSWORD,"password error.");
			 else
				 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_PASSWD_EXCEED,"password error exceed limit.");
			 return;
		 }*/

		 res->retcode = GNET::ERR_SUCCESS;
		 switch(res->algorithm & gnet::ALGORITHM_MASK_HIGH)
		 {
			 case gnet::ALGORITHM_CARD:
				 {
					 if(!dsm->GetMatrixLimit().ask(res->userid))
					 {
						 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_MATRIX_EXCEED,"mibao card error exceed limit");
						 return;
					 }

					 MatrixChecker * checker = new CardChecker(
							 res->userid,
							 GNET::byteorder_32(sinfo->loginip),
							 (unsigned char*)res->matrix.begin()
							 );
					 SendMatrixPasswdRep(response,checker);
					 break;
				 }
			 case gnet::ALGORITHM_HANDSET:
				 {
					 MatrixChecker * checker = MobileCheckerFactory::Instance().Create(
							 res->userid,
							 GNET::byteorder_32(sinfo->loginip),
							 res->algorithm, res->matrix
							 );
					 SendMatrixPasswdRep(response,checker);
					 break;
				 }
			 default:
				 {
				     GNET::Log::log(LOG_INFO,"user(account=%.*s) MatrixPasswd default algorithm.",arg->account.size(),arg->account.begin());
					 SendMatrixPasswdRep(response,NULL);
					 break;
				 }
		 }
	 }

	 void crossResponse(const gdeliverd::UserIdentityCache::Identity& iden)
	 {
		 gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
		 gdeliverd::SessionInfo* sinfo = dsm->GetSessionInfo(link_sid,client_sid);
		 if(NULL == sinfo)
			 return;
		 gnet::MatrixPasswdArg* arg = getArgument();
		 gnet::MatrixPasswdRes* res = getResult();
		 res->retcode = GNET::ERR_SUCCESS;
		 res->userid = iden.userid;
		 res->algorithm = iden.algorithm;
		 res->response = iden.password;
		 GNET::Log::log(LOG_INFO,"------user(account=%.*s) MatrixPasswd crossResponse,cross login.",arg->account.size(),arg->account.begin());

		 if(CheckBeForbid())
			 return;

		 bool blisgm = false;
#ifndef ALL_GM
		 if((res->algorithm & gnet::ALGORITHM_GM_ACCOUNT) == gnet::ALGORITHM_GM_ACCOUNT)
			 blisgm = true;
#else
		 blisgm = true;
#endif
		 gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
		 if(!blisgm && uc->Size() >= uc->GetPlayerLimit())
		 {
			 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_SERVEROVERLOAD,"online user exceed MAX_PLAYER_NUM");
			 return;
		 }
#ifdef USE_AUANY
		 GNET::Octets response;
		 makePasswd(arg->account,res->response,response);
#else
		 GNET::Octets response(res->response.begin(),res->response.size());
		 sinfo->userid = res->userid;
		 sinfo->algorithm = res->algorithm;

		 CalcResponse();
#endif
		 if(res->response != sinfo->response)
		 {
			 dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_INVALID_PASSWORD,"password error.");
			 return;
		 }

		 switch(res->algorithm & gnet::ALGORITHM_MASK_HIGH)
		 {
			 case gnet::ALGORITHM_CARD:
			 case gnet::ALGORITHM_HANDSET:
			 default:
				 {
					 SendMatrixPasswdRep(response,NULL);
					 break;
				 }
		 }
	 }

	 void SendMatrixPasswdRep(GNET::Octets & response,MatrixChecker* checker)
	 {
		 gnet::MatrixPasswdRes* res = getResult();
		 gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
		 MatrixPasswdRep rep(client_sid,0,res->userid,response,0,0,0);
		 if(checker)
		 {
			 rep.blmatrixchallenge = 1;
			 rep.algorithm = res->algorithm;
			 rep.nonce = checker->Challenge();
			 dsm->AddChecker(link_sid,client_sid,checker);
			 LOG_TRACE("user(id=%d) is matrix card user",res->userid);
		 }
		 GNET::Log::log(LOG_INFO,"user SendMatrixPasswdRep().");
		 dsm->Send(link_sid,rep);
	 }

	 void OnTimeout()
	 {
		 //TODO
		 if(bl_crosslogin)
			 return;
		 gnet::MatrixPasswdArg* arg = getArgument();
		 GNET::Log::log(LOG_WARNING,"user(account=%.*s) MatrixPasswd timeout.",arg->account.size(),(char*)arg->account.begin());
		 TryResponse(GNET::ERR_AU_DELIVER_TIMEOUT);
		 GNET::Log::log(LOG_INFO,"user(account=%.*s) MatrixPasswd OnTimeout(),TryResponse again.",arg->account.size(),arg->account.begin());
	 }

   };

}
