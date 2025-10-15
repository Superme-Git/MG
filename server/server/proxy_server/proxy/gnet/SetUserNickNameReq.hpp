
#pragma once

#include "rpcgen.hpp"
#include "rpcgen/gnet/_depends_SetUserNickNameReq_.hpp"

namespace gnet { 

class SetUserNickNameReq : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/SetUserNickNameReq.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		LOG_TRACE("user(id=%d) receive SetUserNickNameReq from gs(db).",userid); 
		SetUserNickName *rpc = (SetUserNickName*)GNET::Rpc::Call(SetUserNickName::PROTOCOL_TYPE,SetUserNickNameArg(userid,nickname,0));

		if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(rpc))
		{
			GNET::Log::log(LOG_WARNING,"user(id=%d) send SetUserNickName to au failed",userid);
		}	 	
	}

};

} 
