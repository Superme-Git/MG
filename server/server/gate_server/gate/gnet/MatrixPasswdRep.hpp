
#pragma once

#include "rpcgen.hpp"
#include "LinkServer.hpp"
#include "LinkSession.hpp"
#include "KeyExchange.hpp"
#include "MatrixChallenge.hpp"

using namespace GNET;

namespace gnet { 

class MatrixPasswdRep : public GNET::Protocol
{
public:
	#include "rpcgen/gnet/MatrixPasswdRep.inc"

	virtual void Process(Manager *, Manager::Session::ID )
	{
		glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
		glinkd::LinkSession * psession = lsm->GetLinkSession(localsid);
		if(NULL == psession)
			return;
		glinkd::LinkServer::log.info("MatrixPasswdRep:Process:user(id=%d) sid=%d,retcode=%d", userid, localsid, retcode);
		//error condition pass by ErrorInfoRep
		psession->setUserId(userid);
		psession->setResponse(response);//then LinkSession will hold the real password of user from au
		if(blmatrixchallenge)//need matrix card
		{
			lsm->ChangeState(localsid,&gnet::state::VerifyMatrix); 
			lsm->Send(localsid, MatrixChallenge(algorithm,nonce)); 
		}
		else
		{
			//not need matrix card,just KeyExchange
			lsm->ChangeState(localsid,&gnet::state::KeyExchgSend); 
			KeyExchange keyexchange; 
			keyexchange.blkickuser = 0;
			keyexchange.Setup(psession->getIdentity(), psession->getResponse(), lsm, localsid); 
			glinkd::LinkServer::log.info("MatrixPasswdRep:Process:user(id=%d) sid=%d,blkickuser=%d", userid, localsid, keyexchange.blkickuser);
			lsm->Send(localsid, keyexchange); 
		}
	}
};
} 
