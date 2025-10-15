
#include "LogClientTcpManager.hpp"
#include "common/timertask.h"

namespace logclient { 


LogClientTcpManager LogClientTcpManager::instance;

void LogClientTcpManager::Reconnect()
{
	GNET::Thread::HouseKeeper::AddTimerTask(new GNET::ReconnectTask(this, 1), backoff);
	backoff *= 2;
	if (backoff > BACKOFF_DEADLINE) backoff = BACKOFF_DEADLINE;
}

const GNET::Protocol::Manager::Session::State* LogClientTcpManager::GetInitState() const
{
	return &gnet::state::LogNormal;
}

void LogClientTcpManager::OnAddSession(Session::ID sid)
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
	GNET::Log::log( LOG_INFO, "connect to LogServiceTcp success,sid=%d",sid );
}

void LogClientTcpManager::OnDelSession(Session::ID sid)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	conn_state = false;
	Reconnect();
	//TODO
	GNET::Log::log( LOG_INFO, "disconnect from LogServiceTcp,sid=%d",sid );
}

void LogClientTcpManager::OnAbortSession(const GNET::SockAddr &sa)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	conn_state = false;
	Reconnect();
	// TODO
	GNET::Log::log( LOG_INFO, "connect to LogServiceTcp failed,sid=%d",sid );
}

void LogClientTcpManager::OnCheckAddress(GNET::SockAddr & sa) const
{
	// check or setup a new address
}


} 
