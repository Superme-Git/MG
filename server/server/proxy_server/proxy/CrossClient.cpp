
#include "CrossClient.hpp"
#include "common/timertask.h"
#include "common/hardware.h"
#include "DeliverServer.hpp"
#include "cross/AnnounceZoneidToCross.hpp"

namespace gdeliverd { 


CrossClient CrossClient::instance;

void CrossClient::Reconnect()
{
	GNET::Thread::HouseKeeper::AddTimerTask(new GNET::ReconnectTask(this, 1), backoff);
	backoff *= 2;
	if (backoff > BACKOFF_DEADLINE) backoff = BACKOFF_DEADLINE;
}

const GNET::Protocol::Manager::Session::State* CrossClient::GetInitState() const
{
	return &gnet::state::CrossClient;
}

void CrossClient::OnAddSession(Session::ID sid)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	if (conn_state)
	{
		Close(sid);
		return;
	}
	conn_state = true;
	this->sid = sid;
	backoff = BACKOFF_INIT;
	
	//TODO
	char buffer[64];
	int len = snprintf(buffer,sizeof(buffer) - 1,"aid=%d,zoneid=%d,sid=%d",DeliverServer::GetInstance()->GetAid(),DeliverServer::GetInstance()->GetZoneid(),sid);
	buffer[len] = 0;

	GNET::Log::log(LOG_INFO,"connect to CrossServer success,%s",buffer);
	cross::AnnounceZoneidToCross ann;
	ann.zoneid  = DeliverServer::GetInstance()->GetZoneid();
	ann.boottime = time(NULL);
	if(SendProtocol(ann))
		GNET::Log::log(LOG_INFO,"send AnnounceZoneidToCross ok,%s",buffer);
	else
		GNET::Log::log(LOG_WARNING,"send AnnounceZoneidToCross failed,%s",buffer);
}

void CrossClient::OnDelSession(Session::ID sid)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	conn_state = false;
	Reconnect();
	//TODO
	GNET::Log::log(LOG_WARNING,"-----disconnect from CrossServer-----,aid=%d,zoneid=%d,sid=%d",DeliverServer::GetInstance()->GetAid(),DeliverServer::GetInstance()->GetZoneid(),sid);
}

void CrossClient::OnDelSession(Session::ID sid, int state)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	conn_state = false;
	Reconnect();
	//TODO
	GNET::Log::log(LOG_WARNING,"-----disconnect from CrossServer-----,state=0x%x,aid=%d,zoneid=%d,sid=%d",state,DeliverServer::GetInstance()->GetAid(),DeliverServer::GetInstance()->GetZoneid(),sid);
}

void CrossClient::OnAbortSession(const GNET::SockAddr &sa)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	conn_state = false;
	Reconnect();
	// TODO
	GNET::Log::log(LOG_WARNING,"-----connect to CrossServer failed-----,aid=%d,zoneid=%d,sid=%d",DeliverServer::GetInstance()->GetAid(),DeliverServer::GetInstance()->GetZoneid(),sid);
}

void CrossClient::OnCheckAddress(GNET::SockAddr & sa) const
{
	// check or setup a new address
}

void CrossClient::OnSetTransport(Session::ID sid,const GNET::SockAddr& local,const GNET::SockAddr& peer)
{
}


} 
