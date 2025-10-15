
#include "logserviceserver.hpp"
#include "state.hxx"

#include "log.h"

namespace GNET
{

LogserviceServer LogserviceServer::instance;

const Protocol::Manager::Session::State* LogserviceServer::GetInitState() const
{
	return &state_LogNormal;
}

void LogserviceServer::OnAddSession(Session::ID sid)
{
	//TODO
	Log::log( LOG_INFO, "LogserviceServer: OnAddSession,sid=%d",sid );
}

void LogserviceServer::OnDelSession(Session::ID sid)
{
	//TODO
	Log::log( LOG_INFO, "LogserviceServer: OnDelSession,sid=%d",sid );
}

};
