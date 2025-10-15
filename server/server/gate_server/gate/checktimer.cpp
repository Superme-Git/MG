#include "checktimer.hpp"
#include "LinkServer.hpp"
#include "gnet/link/SetLogin.hpp"


namespace glinkd
{
	//check client's alive according to KeepAlive Protocol
	void CheckTimer::CheckConnection()
	{
		LinkServer* lsm = LinkServer::GetInstance();
		LinkServer::log.debug("CheckTimer:CheckConnection:===========");
		for (LinkServer::TimeoutMap::iterator it = lsm->alivetimemap.begin();it != lsm->alivetimemap.end();)
		{
			LinkSession * psession = lsm->GetLinkSession(it->first);
			if(psession && gnet::link::SetLogin::eLogin == psession->getLogin())
			{
				(*it).second -= update_time;
				LinkServer::log.debug("CheckTimer:CheckConnection:user(id=%d,sid=%d) alivetime:%d.", psession->getUserId(), it->first, (*it).second);
				if ((*it).second <= 0)
				{
					LinkServer::log.info("CheckTimer:CheckConnection:user(id=%d,sid=%d) KeepAlive expired,session closing.",psession->getUserId(),it->first);
					lsm->Close(it->first,GNET::CLOSE_EXPIRE);
					lsm->alivetimemap.erase(it++);
				}
				else
					++it;
			}
			else
				++it;
		}
	}
}
