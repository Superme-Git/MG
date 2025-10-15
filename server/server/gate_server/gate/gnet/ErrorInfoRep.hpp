
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"
#include "ErrorInfo.hpp"

namespace gnet { 

	class ErrorInfoRep : public GNET::Protocol
	{
		public:
		#include "rpcgen/gnet/ErrorInfoRep.inc"

		virtual void Process(Manager *, Manager::Session::ID )
		{
			/* protocol handle */
			LOG_TRACE("receive ErrorInfoRep from deliver,errcode=%d,sid=%d",errcode,localsid);
			if(!glinkd::LinkServer::ValidSid(localsid))
				return;
			glinkd::LinkServer::GetInstance()->SendErr(localsid,errcode,info);
		}
	};

} 
