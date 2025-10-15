
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"

namespace gnet { 
namespace link { 

class Kick : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/link/Kick.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		glinkd::LinkServer::log.info("Kick:Process:from gs,linksid=%d", this->linksid);

		switch (this->action)
		{
		case Kick::A_DELAY_CLOSE:
			glinkd::LinkServer::GetInstance()->ChangeState(this->linksid, &gnet::state::Null);
			break;
		//GM�0�0�0�8�0�6�0�9�0�4��iweb�0�0�0�8�0�6�0�9
		case Kick::A_QUICK_CLOSE:
			glinkd::LinkServer::GetInstance()->SendErr(this->linksid,this->error,"Kick by gs,Kick::A_QUICK_CLOSE");
			break;
		
		case Kick::A_ACKICKOUT:
			{
				glinkd::LinkServer::GetInstance()->SendErr(this->linksid,GNET::ERR_ACKICKOUT,"Kick by gs,Kick::A_ACKICKOUT");
				break;
			}
		default:
			glinkd::LinkServer::GetInstance()->Close(this->linksid);
			break;
		}
	}

};

} 
} 
