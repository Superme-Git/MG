
#include "AuAnyClient.hpp"
#include "common/timertask.h"
#include "openau/RegistGameServerRequest.hpp"
#include "common/hardware.h"

namespace gdeliverd { 


AuAnyClient AuAnyClient::instance;

log4cpp::Category& AuAnyClient::log = log4cpp::Category::getInstance(std::string("gdeliverd"));

void AuAnyClient::Reconnect()
{
	GNET::Thread::HouseKeeper::AddTimerTask(new GNET::ReconnectTask(this, 1), backoff);
	backoff *= 2;
	if (backoff > BACKOFF_DEADLINE) backoff = BACKOFF_DEADLINE;
}

const GNET::Protocol::Manager::Session::State* AuAnyClient::GetInitState() const
{
	return &gnet::state::AuAnyClient;
}

void AuAnyClient::OnAddSession(Session::ID sid)
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
	openau::RegistGameServerRequest request;
	request.plattype = getPlatType();
	request.serverid = getServerId();
	
	int ip1 = 0, ip2 = 0, ip3 = 0;
	std::vector<int>	ips;
	const GNET::IFConfig *ifconfig = GNET::IFConfig::GetInstance();
	std::map< const char *, struct in_addr > map = ifconfig->if2ipv4();
	for ( std::map<const char *, struct in_addr>::iterator it = map.begin(), ie = map.end(); it != ie; ++it )
		ips.push_back( (int)(*it).second.s_addr ); 
	if( ips.size() >= 1 )	ip1 = ips[0];
	if( ips.size() >= 2 )	ip2 = ips[1];
	if( ips.size() >= 3 )	ip3 = ips[2];
	request.ip1 = ip1;
	request.ip2 = ip2;
	request.ip3 = ip3;
	SendProtocol(request);
	log.info("AuAnyClient:OnAddSession:connect to auany success,palttype=%d,serverid=%.*s",
			 request.plattype, getServerId().size(), getServerId().begin());
}

void AuAnyClient::OnDelSession(Session::ID sid)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	conn_state = false;
	Reconnect();
}

void AuAnyClient::OnDelSession(Session::ID sid,int state)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	conn_state = false;
	Reconnect();
	log.warn("AuAnyClient:OnDelSession:disconnect from auany,state=0x%x,palttype=%d,serverid=%.*s",
			 state, getPlatType(), getServerId().size(), getServerId().begin());
}


void AuAnyClient::OnAbortSession(const GNET::SockAddr &sa)
{
	GNET::Thread::Mutex::Scoped l(locker_state);
	conn_state = false;
	Reconnect();
	log.warn("AuAnyClient:OnAbortSession:connect to auany failed,palttype=%d,serverid=%.*s",
			 getPlatType(), getServerId().size(), getServerId().begin());
}

void AuAnyClient::OnCheckAddress(GNET::SockAddr & sa) const
{
	// check or setup a new address
}


} 
