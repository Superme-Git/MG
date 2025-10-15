
#pragma once

#include "rpcgen.hpp"
#include "DeliveryClient.hpp"
#include "MatrixResponseReq.hpp"

namespace gnet { 

class MatrixResponse : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixResponse.inc"

	virtual void Process(Manager *, Manager::Session::ID sid) 
	{
		 /* protocol handle */ 
		LOG_TRACE( "receive MatrixResponse from client,localsid=%d",sid);
		glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
		if(!glinkd::LinkServer::ValidSid(sid))
		{
			lsm->Close(sid);
			return;
		}	
		if(!glinkd::DeliveryClient::GetInstance()->SendProtocol(MatrixResponseReq(response,sid))) 
		{
			GNET::Log::log(LOG_WARNING,"send MatrixResponseReq to deliver failed,localsid=%d", sid);
			lsm->SendErr(sid,GNET::ERR_LINK_NOT_CONNECT_DELIVER,"link not connect deliver!");
		}
	}
};
} 
