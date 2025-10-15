
#include "LogClientManager.hpp"
#include "common/timertask.h"
#include "common/log.h"


namespace logclient { 


LogClientManager LogClientManager::instance;

void LogClientManager::Reconnect()
{
	GNET::Thread::HouseKeeper::AddTimerTask(new GNET::ReconnectTask(this, 1), backoff);
	backoff *= 2;
	if (backoff > BACKOFF_DEADLINE) backoff = BACKOFF_DEADLINE;
}

const GNET::Protocol::Manager::Session::State* LogClientManager::GetInitState() const
{
	return &gnet::state::LogNormal;
}

void LogClientManager::OnAddSession(Session::ID sid)
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
	GNET::Log::log( LOG_INFO, "connect to LogService success." );
}

void LogClientManager::OnDelSession(Session::ID sid)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	conn_state = false;
	Reconnect();
	//TODO
	GNET::Log::log( LOG_INFO, "disconnect from LogService." );
}

void LogClientManager::OnAbortSession(const GNET::SockAddr &sa)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	conn_state = false;
	Reconnect();
	// TODO
	GNET::Log::log( LOG_INFO, "connect to LogService failed." );
}

void LogClientManager::OnCheckAddress(GNET::SockAddr & sa) const
{
	// check or setup a new address
}


} 
