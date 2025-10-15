
#include "CrossServer.hpp"
#include "cross/BroadcastCrossStatus.hpp"
#include "cross/ServerInfo.hpp"
#include "netplayer.h"

namespace gdeliverd { 


CrossServer CrossServer::instance;

const GNET::Protocol::Manager::Session::State* CrossServer::GetInitState() const
{
	return &gnet::state::CrossServer;
}

void CrossServer::OnAddSession(Session::ID sid)
{
	//TODO
//	BroadcastCrossStatus();
	SendCrossStatus(sid);
	GNET::Log::log(LOG_INFO,"CrossServer::OnAddSession,add a deliver,sid=%d",sid);
}

void CrossServer::OnDelSession(Session::ID sid)
{
	//TODO
	GNET::Log::log(LOG_INFO,"CrossServer::OnDelSession,delete a deliver,sid=%d",sid);
	sid2peermap.erase(sid);
}

void CrossServer::OnDelSession(Session::ID sid,int state)
{
	//TODO
	int zoneid = getZoneidBySid(sid);
	GNET::Log::log(LOG_INFO,"CrossServer::OnDelSession,delete a deliver,sid=%d,zoneid=%d,state=0x%X",sid,zoneid,state);
	sid2peermap.erase(sid);
	sid2zonemap.erase(sid);
	getZoneSessionManager().erase(zoneid);
}

void CrossServer::OnSetTransport(Session::ID sid,const GNET::SockAddr& local,const GNET::SockAddr& peer)
{
//	sid2peermap[sid] = ((struct sockaddr_in*)peer)->sin_addr.s_addr;
	sid2peermap[sid] = peer;
}

void CrossServer::BroadcastCross(GNET::Protocol* p)
{
	for(SidMap::const_iterator it= sid2peermap.begin(),ite=sid2peermap.end();it!=ite;++it)
	{
		Send(it->first,p);
	}
}

void CrossServer::BroadcastCross(const GNET::Protocol* p)
{
	for(SidMap::const_iterator it= sid2peermap.begin(),ite=sid2peermap.end();it!=ite;++it)
	{
		Send(it->first,p);
	}
}

void CrossServer::SendCrossStatus(Session::ID sid) 
{
	gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
	cross::BroadcastCrossStatus bc;
	cross::ServerInfo info;
	info.zoneid = gdeliverd::DeliverServer::GetInstance()->GetZoneid();
	info.online_num = uc->Size();
	info.max_online_num = uc->GetPlayerLimit();
	bc.serverinfo = info;
	Send(sid,bc);

}
void CrossServer::BroadcastCrossStatus()
{
	static time_t timer = 0;
	time_t now = time(NULL);

	if(now - timer < 10)
		return;
	timer = now;

	gdeliverd::UserContainer * uc = gdeliverd::UserContainer::GetInstance();
	cross::BroadcastCrossStatus bc;
	cross::ServerInfo info;
	info.zoneid = gdeliverd::DeliverServer::GetInstance()->GetZoneid();
	info.online_num = uc->Size();
	info.max_online_num = uc->GetPlayerLimit();
	bc.serverinfo = info;
	BroadcastCross(bc);
	GNET::Log::log(LOG_INFO,"CrossServer::BroadcastCrossStatus,zoneid=%d,online=%d,max=%d",info.zoneid,info.online_num,info.max_online_num);
}

} 
