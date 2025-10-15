
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"
#include "LinkSession.hpp"
#include "gnet/KeyExchange.hpp"

namespace gnet { 

class MatrixResponseRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixResponseRep.inc"

	virtual void Process(Manager *, Manager::Session::ID ) 
	{
		/* protocol handle */
		LOG_TRACE("receive MatrixResponseRep,localsid=%d",localsid); 
		glinkd::LinkServer *lsm = glinkd::LinkServer::GetInstance();
		glinkd::LinkSession *psession = lsm->GetLinkSession(localsid);
		if(NULL == psession)
			return;
		if(blkeyexchange)
		{
			lsm->ChangeState(localsid,&gnet::state::KeyExchgSend); 
			//send keyexchange protocol 
			KeyExchange keyexchange; 
			keyexchange.Setup(psession->getIdentity(), psession->getResponse(), lsm, localsid); 
			lsm->Send(localsid, keyexchange); 
		}
	}
};
} 
