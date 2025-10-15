
#include "ProviderServer.hpp"
#include "gnet/link/AnnounceLinkId.hpp"
#include "LinkServer.hpp"
#include "LinkSession.hpp"

namespace glinkd { 


ProviderServer ProviderServer::instance;

log4cpp::Category& ProviderServer::log = log4cpp::Category::getInstance(std::string("glinkd"));

const GNET::Protocol::Manager::Session::State* ProviderServer::GetInitState() const
{
	return &gnet::state::LinkProvider;
}

void ProviderServer::OnAddSession(Session::ID sid)
{
	log.info("ProviderServer:OnAddSession:add a gs,sid=%d", sid);
	this->Send(sid, gnet::link::AnnounceLinkId(this->linkid));
}

void ProviderServer::OnDelSession(Session::ID sid,int state)
{
	log.info("ProviderServer:OnDelSession:delete a gs and kick all client,state=%d,sid=%d", state, sid);
	glinkd::LinkServer * lsm = glinkd::LinkServer::GetInstance();
	glinkd::LinkSessions::links_map::iterator itor;
	for(itor = lsm->GetSessions().begin();itor != lsm->GetSessions().end();)
	{
		lsm->Close((*itor++).first);	
	}			
}

void ProviderServer::OnDelSession(Session::ID sid)
{
}


} 
