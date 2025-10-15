
#pragma once

#include "rpcgen.hpp"
#include "MatrixResponseRep.hpp"
#include "mapphonetoken.h"

namespace gnet { 

   class MatrixResponseReq : public GNET::Protocol
   {
      public:
#include "rpcgen/gnet/MatrixResponseReq.inc"

	 virtual void Process(Manager *manager, Manager::Session::ID link_sid)
	 {
		 /* protocol handle */ 
		 gdeliverd::DeliverServer *dsm = gdeliverd::DeliverServer::GetInstance();
		 gdeliverd::SessionInfo *sinfo = dsm->GetSessionInfo(link_sid,localsid);

		 if (sinfo && sinfo->checker)
		 {
			 switch(sinfo->algorithm & gnet::ALGORITHM_MASK_HIGH)
			 {
				 case gnet::ALGORITHM_CARD:
					 {
						 LOG_TRACE("user(id=%d) is mibao card,receive MatrixResponse",sinfo->userid);
						 if(!sinfo->checker->Verify(response))
						 {
							 GNET::Log::log(LOG_WARNING,"user(id=%d) matrix card error",sinfo->userid);
							 if(dsm->GetMatrixLimit().add(sinfo->userid))
								 dsm->SessionError(sinfo->userid,link_sid,localsid,GNET::ERR_MATRIXFAILURE, "mibao card error.");
							 else
								 dsm->SessionError(sinfo->userid,link_sid,localsid,GNET::ERR_MATRIX_EXCEED,"mibao card error exceed limit.");
							 return;
						 }
						 break;
					 }

				 case gnet::ALGORITHM_PHONETOKEN:
					 {
						 LOG_TRACE("user(id=%d) is phone token,receive MatrixResponse",sinfo->userid);

						 char num[6];
						 int i = 0,j = 100000;
						 int temp_response = response;
						 while(i < 6)
						 {
							 num[i] = (temp_response / j) + '0';
							 temp_response = (temp_response % j);
							 i++;
							 j /= 10;
						 }
					//	 snprintf(num,6,"%d",response);
					/*	 int len = snprintf(num,6,"%d",response);
						 if(len != 6 && len != 5)
						 {
							 GNET::Log::log(LOG_WARNING,"PhoneToken elec number.size invalid.");
							 dsm->SessionError(sinfo->userid,link_sid,localsid,GNET::ERR_PHONETOKEN_FAILURE, "phone token error.");
							 return;
						 }
					*/	 /*	if(len == 5)//elec number may begin with 0
							len = snprintf(num,7,"0%d",response);

							for(int i = 0;i < 6;++i)
							num[i] -= '0';
							*/
						 if(!sinfo->checker->Verify(response))
						 {
							 if(dsm->GetMatrixLimit().add(sinfo->userid))
								 dsm->SessionError(sinfo->userid,link_sid,localsid,GNET::ERR_PHONETOKEN_FAILURE, "phone token error.");
							 else
								 dsm->SessionError(sinfo->userid,link_sid,localsid,GNET::ERR_PHONETOKEN_EXCEED,"phone token error exceed limit.");
							 return;
						 }
						 else
						 {
							 GNET::Octets last_used_elec_number; 
							 GNET::Octets using_elec_number(num,sizeof(num));
							 gdeliverd::PhoneTokenCache* ptc = gdeliverd::PhoneTokenCache::GetInstance();
							 ptc->GetUsedElecNumber(sinfo->userid,last_used_elec_number);
							 if(last_used_elec_number == using_elec_number)
								 dsm->SessionError(sinfo->userid,link_sid,localsid,GNET::ERR_ELEC_NUMBER_USED,"this elec number be used.");
							 else
							 {
							//	 sinfo->isElecLogin = true;
								 sinfo->logintype = ELEC_LOGIN;
								 ptc->SetUsedElecNumber(sinfo->userid,using_elec_number);
							 }
						 }
						 break;
					 }
				 default:
					 return;
			 }
			 LOG_TRACE("user(id=%d) MatrixResponse verify success.",sinfo->userid);
			 dsm->Send(link_sid,MatrixResponseRep(1,localsid));
			 delete sinfo->checker;
			 sinfo->checker = NULL;
		 }
	 }
   };
}

