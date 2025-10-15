
#pragma once

#include <log4cpp/Category.hh>
#include "rpcgen.hpp"
#include "DeliveryClient.hpp"
#include "LinkServer.hpp"
#include "MatrixPasswdReq.hpp"

namespace gnet {
  
class Response : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/Response.inc"

	virtual void Process(Manager *, Manager::Session::ID sid)
	{
		log.info("Response:Process:user(account=%.*s),logintype=%d.", identity.size(),(char*)identity.begin(),logintype);
		glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
		glinkd::LinkSession * psession = lsm->GetLinkSession(sid);
		if(NULL == psession)
		{
			lsm->Close(sid);
			return;
		}
		int client_ip = ((struct sockaddr_in*)(psession->getPeer()))->sin_addr.s_addr;

		char* client_ip_char = inet_ntoa(((struct sockaddr_in*)(psession->getPeer()))->sin_addr);
		GNET::Octets client_ip_oct(client_ip_char,strlen(client_ip_char));
		/*
		log.debug("user(account=%.*s) ip=%.*s", identity.size(),(char*)identity.begin(),
            client_ip_oct.size(),(char*)client_ip_oct.begin());
        */
		glinkd::ForbidIpValue *pforbid = lsm->GetForbidIpValue(client_ip);
		if(NULL != pforbid)
		{
			if(pforbid->IsForbid())
			{
				lsm->SendErr(sid,GNET::ERR_IP_FORBID,"ip forbid");
				return;
			}
			else
				lsm->RemoveIpForbid(client_ip);//if expire,erase this ip
		}

		psession->setResponse(response);//keyexchange will use this
		psession->setIdentity(identity);//keyexchange will use this
		psession->setLogintype(logintype);
		psession->setMid(mid);

		lsm->GetHalfLoginSet().insert(sid);
		if (lsm->ExceedHalfloginLimit(lsm->GetHalfLoginSet().size()) && lsm->IsListening())
		{
			log.warn("Response:Process:halflogin > limit,halflogin=%d", lsm->GetHalfLoginSet().size());
			lsm->StopListen();
		}

		if(glinkd::DeliveryClient::GetInstance()->SendProtocol(
            MatrixPasswdReq(
                MatrixPasswdArg(identity,
                                psession->getChallenge(),
                                client_ip,
                                client_ip_oct,
                                logintype,
                                gameid,
                                gamekey,
                                serverid,
                                selfchannel,
                                childchannel,
                                deviceid,
                                mid,
                                reserved1,
                                reserved2),sid,response)))
			lsm->ChangeState(sid,&state::GResponseReceive);
		else
			lsm->SendErr(sid,GNET::ERR_LINK_NOT_CONNECT_DELIVER,"link not connect deliver");
	}
	
	static log4cpp::Category& log;
};
log4cpp::Category& Response::log = log4cpp::Category::getInstance(std::string("glinkd"));
}
