
#pragma once
#include "rpcgen.hpp"
#include "rpcgen/gnet/MatrixPasswd2Arg.hpp"
#include "rpcgen/gnet/MatrixPasswd2Res.hpp"
#include "AuthClient.hpp"
#include "DeliverServer.hpp"
#include "GameDBServer.hpp"
#include "AnnounceForbidInfo.hpp"
#include "Constants.hpp"
#include "DeliverAnnounceToLink.hpp"
#include "GMShutup.hpp"
#include "MatrixPasswdRep.hpp"
#include "mappasswd.h"
#include "mapusbkey.h"
#include "mapphonetoken.h"
#include "cardchecker.h"
#include "mobilechecker.h"
#include "usbkey.h"
#include "phonetokenchecker.h"
#include "netplayer.h"
#include "mapforbid.h"

namespace gnet { 

   class MatrixPasswd2 : public GNET::Rpc
   {
      public:
#include "rpcgen/gnet/MatrixPasswd2.inc"

	 int 	link_sid;
	 int 	client_sid;
	 bool  	bl_tryresponse;
	 bool  	bl_ssologin;

	 void OnServer(Manager *, Manager::Session::ID )
	 {
	    //gnet::MatrixPasswd2Arg* arg = getArgument();
	    //gnet::MatrixPasswd2Res* res = getResult();
	 }

	 void OnClient(Manager *, Manager::Session::ID )
	 {
	    //TODO 
	    gnet::MatrixPasswd2Arg* arg = getArgument();
	    gnet::MatrixPasswd2Res* res = getResult();
	    GNET::Log::log(LOG_INFO,"user(account=%.*s,id=%d) receive MatrixPasswd2,algorithm=0x%0x,retcode=%d",arg->account.size(),(char*)arg->account.begin(),res->userid,res->algorithm,res->retcode);
	    gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
	    gdeliverd::SessionInfo* sinfo = dsm->GetSessionInfo(link_sid,client_sid);
	    if(NULL == sinfo)
	       return;
	    if(ProcessForbidInfo())	
	    {
	       dsm->RemoveSessionInfo(link_sid,client_sid);
	       return;
	    }

	    if(res->retcode == GNET::ERR_INVALID_ACCOUNT)
	    {
	       CheckIPForbid(arg->loginip,2);
	       dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_INVALID_ACCOUNT,"Invalid account.");
	       gdeliverd::Passwd::GetInstance()->DelPasswd(arg->account);         
	       return;
	    }

	    if(res->retcode == GNET::ERR_SUCCESS)
	    {
	       if((res->algorithm & gnet::ALGORITHM_OPT_NOCACHE) == 0)
	       {
		  gdeliverd::Passwd::GetInstance()->SetPasswd(arg->account,res->userid,res->algorithm,res->response,res->matrix);
		  //cache usbkey2 info separately,if unbind,not clean this info,only clean when process restart					
		  switch(res->algorithm & gnet::ALGORITHM_MASK_HIGH)
		  {
		     case gnet::ALGORITHM_USBKEY2:
			{
			   gdeliverd::UsbKeyCache::GetInstance()->SetUsbKeyInfo(res->userid,res->seed,res->pin,res->rtime,res->currenttime);
			   break;
			}
		     case gnet::ALGORITHM_PHONETOKEN:
			{
			   gdeliverd::PhoneTokenCache::GetInstance()->SetPhoneTokenInfo(res->userid,res->seed,res->rtime,res->currenttime);
			   break;
			}
		  }
	       }
	       else if((res->algorithm & gnet::ALGORITHM_OPT_TICKETS) == 0)
		  gdeliverd::Passwd::GetInstance()->DelPasswd(arg->account);         
	    }
	    else
	    {
	       dsm->SessionError(res->userid,link_sid,client_sid,res->retcode,"MatrixPasswd2Res.retcode != 0");
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
	       if((res->algorithm & gnet::ALGORITHM_MASK_HIGH) == gnet::ALGORITHM_USBKEY2)
	       {
		  GNET::Octets temp;
		  GNET::Octets used_elec_number; 
		  if(CheckUsbPasswd(sinfo->response,temp,used_elec_number))
		  {
		     if(used_elec_number == res->last_used_elec_number)
			dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_ELEC_NUMBER_USED,"this elec number be used.");
		     else
		     {
		//	sinfo->isElecLogin = true;
			sinfo->logintype = ELEC_LOGIN;
			gdeliverd::UsbKeyCache::GetInstance()->SetUsedElecNumber(res->userid,used_elec_number);	
			SendMatrixPasswdRep(temp,NULL);
		     }
		     return;
		  }
	       }
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
	       case gnet::ALGORITHM_PHONETOKEN:
		  {
		     if(!dsm->GetPhoneTokenLimit().ask(res->userid))
		     {
			dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_PHONETOKEN_EXCEED,"phonetoken elec number error exceed limit.");
			return;
		     }
		     gdeliverd::PhoneTokenCache::GetInstance()->SetUsedElecNumber(res->userid,res->last_used_elec_number);
		     MatrixChecker * checker = new PhoneTokenChecker(
			   res->userid, 
			   GNET::byteorder_32(sinfo->loginip),
			   res->seed,
			   (res->currenttime - res->rtime) / 32
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
	    gnet::MatrixPasswd2Res* res = getResult();
	    RoleForbid forbid;
	    if(gdeliverd::ForbidLogin::GetInstance()->GetForbid(res->userid,forbid))
	    {
	       GNET::Log::log(LOG_INFO,"user(id=%d) is FORBID_LOGIN,time(second)=%d",res->userid,forbid.time);
	       gdeliverd::DeliverServer* dsm = gdeliverd::DeliverServer::GetInstance();
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
	    gnet::MatrixPasswd2Arg* arg = getArgument();
	    gnet::MatrixPasswd2Res* res = getResult();

	    GNET::Log::log(LOG_WARNING,"user(account=%.*s) MatrixPasswd2 tryResponse.",arg->account.size(),arg->account.begin());
	    //find account's passwd in cache
	    if(!gdeliverd::Passwd::GetInstance()->GetPasswd(arg->account,res->userid,res->algorithm,res->response,res->matrix ) )
	    {
	       GNET::Log::log(LOG_WARNING,"user(account=%.*s) MatrixPasswd2 tryResponse failed,password cache not found.",arg->account.size(),(char*)arg->account.begin());
	       //check if ip should be forbidden,add one times
	       CheckIPForbid(arg->loginip,1);
	       switch(reason)
	       {
		  case GNET::ERR_AU_DELIVER_TIMEOUT:
		     {
			dsm->SessionError(0,link_sid,client_sid,reason,"MatrixPasswd2 timeout and password cache not found.");
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
	    if(res->response != sinfo->response)
	    {
	       if((res->algorithm & gnet::ALGORITHM_MASK_HIGH) == gnet::ALGORITHM_USBKEY2)
	       {
		  gdeliverd::UsbKeyCache* ukc = gdeliverd::UsbKeyCache::GetInstance();
		  if(ukc->GetUsbKeyInfo(res->userid,res->seed,res->pin,res->rtime,res->currenttime))
		  {
		     GNET::Octets temp;
		     GNET::Octets using_elec_number; 
		     GNET::Octets last_used_elec_number; 
		     if(CheckUsbPasswd(sinfo->response,temp,using_elec_number))
		     {
			if(ukc->GetUsedElecNumber(res->userid,last_used_elec_number) && (using_elec_number == last_used_elec_number))
			   dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_ELEC_NUMBER_USED,"this elec number be used.");
			else
			{
			   //sinfo->isElecLogin = true;
			   sinfo->logintype = ELEC_LOGIN;
			   ukc->SetUsedElecNumber(res->userid,using_elec_number);	
			   SendMatrixPasswdRep(temp,NULL);
			}
			return;
		     }
		     else
			dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_INVALID_PASSWORD,"password error.");
		  }
		  else
		  {

		     dsm->SessionError(0,link_sid,client_sid,GNET::ERR_COMMUNICATION,"usbkey2 user tryResponse and keyinfo cache not found.");
		     return;
		  }
	       }
	       CheckIPForbid(arg->loginip,1);
	       //check if password error exceed limit
	       if(dsm->GetPasswdLimit().add(sinfo->identity))
		  dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_INVALID_PASSWORD,"password error.");
	       else
		  dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_PASSWD_EXCEED,"password error exceed limit.");
	       return;
	    }

	    res->retcode = GNET::ERR_SUCCESS;
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
	       case gnet::ALGORITHM_PHONETOKEN:
		  {
		     if(!dsm->GetPhoneTokenLimit().ask(res->userid))
		     {
			dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_PHONETOKEN_EXCEED,"phonetoken elec number error exceed limit.");
			return;
		     }
		  
		     gdeliverd::PhoneTokenCache* ptc = gdeliverd::PhoneTokenCache::GetInstance();
		     if(ptc->GetPhoneTokenInfo(res->userid,res->seed,res->rtime,res->currenttime))
		     {
			MatrixChecker * checker = new PhoneTokenChecker(
			      res->userid, 
			      GNET::byteorder_32(sinfo->loginip),
			      res->seed,
			      (res->currenttime - res->rtime) / 32
			      );
			SendMatrixPasswdRep(response,checker);
		     }
		     else
		     {
			dsm->SessionError(res->userid,link_sid,client_sid,GNET::ERR_COMMUNICATION,"phonetoken cache not found.");
			return;	
		     }
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

	 void SendMatrixPasswdRep(GNET::Octets & response,MatrixChecker* checker)
	 {
	    gnet::MatrixPasswd2Res* res = getResult();
	    gdeliverd::DeliverServer * dsm = gdeliverd::DeliverServer::GetInstance();
	    MatrixPasswdRep rep(client_sid,0,res->userid,response,0,0,0);
	    if(checker)
	    {
	       rep.blmatrixchallenge = 1;
	       rep.algorithm = res->algorithm;
	       rep.nonce = checker->Challenge();
	       dsm->AddChecker(link_sid,client_sid,checker);
	       LOG_TRACE("user(id=%d) is matrix card or phonetoken user,algorithm=0x%x",res->userid,res->algorithm);
	    }
	    dsm->Send(link_sid,rep);
	 }

	 void CalcResponse( )
	 {
	    gnet::MatrixPasswd2Arg* arg = getArgument();
	    gnet::MatrixPasswd2Res* res = getResult();

	    GNET::HMAC_MD5Hash hash;
	    hash.SetParameter(res->response);
	    hash.Update(arg->challenge);
	    hash.Final(res->response);
	 }

	 /*
	  * use pin and elec number to validate password
	  * if success,the key return to link for generate isecurity and osecurity key 
	  * is generated by pin,elec number and account,not the response return by au
	  */ 
	 bool CheckUsbPasswd(GNET::Octets& sinfo_response,GNET::Octets& passwdmd5,GNET::Octets& used_elec_number)
	 {
	    gnet::MatrixPasswd2Arg* arg = getArgument();
	    gnet::MatrixPasswd2Res* res = getResult();
	    int _rtime = (res->currenttime - res->rtime) / 32;
	    gdeliverd::UsbKey usb(res->seed,res->pin,_rtime);
	    GNET::Octets usb_response;
	    usb.CalcResponse(arg->account,arg->challenge,passwdmd5,usb_response,used_elec_number);
	    if(usb_response.size() == 0)
	       return false;
	    if(usb_response != sinfo_response)
	    {
	       gdeliverd::UsbKey usb2(res->seed,res->pin,_rtime-1);
	       usb2.CalcResponse(arg->account,arg->challenge,passwdmd5,usb_response,used_elec_number);
	       if(usb_response != sinfo_response)
	       {
		  gdeliverd::UsbKey usb3(res->seed,res->pin,_rtime+1);
		  usb3.CalcResponse(arg->account,arg->challenge,passwdmd5,usb_response,used_elec_number);
		  if(usb_response != sinfo_response)
		     return false;
	       }
	    }
	    return true;
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

	 bool ProcessForbidInfo()
	 {
	    gnet::MatrixPasswd2Res* res = getResult();
	    bool ret = false;	
	    int len = res->forbid.size();
	    gdeliverd::ForbidLogin * forbidlogin = gdeliverd::ForbidLogin::GetInstance();
	    if(len == 0)
	       forbidlogin->RmvForbid(res->userid);	
	    else
	    {
	       gdeliverd::GameDBServer * dbsm = gdeliverd::GameDBServer::GetInstance();
	       for (int i=0;i<len;++i)
	       {
		  int time_left = res->forbid[i].time;
		  switch(res->forbid[i].ltype)
		  {
		     case gnet::FBD_FORBID_TALK:
			{		
			   if(time_left > 1)
			   {
			      LOG_TRACE("user(id=%d) is FORBID_TALK,time(second)=%d",res->userid,time_left);
			      if(!dbsm->Send(dbsm->GetSid(),GMShutup(-1,-1,res->userid,time_left,res->forbid[i].reason)))
			      {
				 GNET::Log::log(LOG_WARNING,"user(id=%d) send GMShutup to gs(db) failed",res->userid);
			      }
			   }
			   break;
			}
		     case gnet::FBD_FORBID_LOGIN:
			{	
			   if ( time_left <= 1 ) 
			   {
			      forbidlogin->RmvForbid(res->userid);
			      break;
			   }
			   LOG_TRACE("userid(id=%d) is FORBID_LOGIN,time(second)=%d",res->userid,time_left);
			   forbidlogin->PutForbid(res->userid,res->forbid[i]);
			   gdeliverd::DeliverServer::GetInstance()->Send(link_sid,AnnounceForbidInfo(res->userid,client_sid,res->forbid[i]));
			   ret = true;
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
	    return ret;
	 }

	 void OnTimeout()
	 {
	    //TODO
	    gnet::MatrixPasswd2Arg* arg = getArgument();
	    GNET::Log::log(LOG_WARNING,"user(account=%.*s) MatrixPasswd2 timeout.",arg->account.size(),(char*)arg->account.begin());
	    TryResponse(GNET::ERR_AU_DELIVER_TIMEOUT);
	 }
   };
} 
