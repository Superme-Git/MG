
#include <netdb.h>
#include "AuthClient.hpp"
#include "common/timertask.h"
#include "common/hardware.h"
#include "DeliverServer.hpp"
#include "gnet/AnnounceZoneid3.hpp"
#include "gnet/CertRequest.hpp"
#include "mapusbkey.h"
#include "mapphonetoken.h"

namespace gdeliverd {


	int UsbKeyCache::time_diff;
	int PhoneTokenCache::time_diff;

	void AuthClient::Reconnect()
	{
		GNET::Thread::HouseKeeper::AddTimerTask(new GNET::ReconnectTask(this, 1), backoff);
		backoff *= 2;
		if (backoff > BACKOFF_DEADLINE) backoff = BACKOFF_DEADLINE;
	}

	const GNET::Protocol::Manager::Session::State* AuthClient::GetInitState() const
	{
		return &gnet::state::AuthClient;
	}

	void AuthClient::OnAddSession(Session::ID sid)
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

		char buffer[64];
		int len = snprintf(buffer,sizeof(buffer) - 1,"aid=%d,zoneid=%d,sid=%d",DeliverServer::GetInstance()->GetAid(),DeliverServer::GetInstance()->GetZoneid(),sid);
		buffer[len] = 0;

		GNET::Log::log(LOG_INFO,"connect to au success,%s",buffer);
		if(GetCertPolicy() == 1)
		{
			if(SendProtocol(gnet::CertRequest(0)))
			{
				GNET::Log::log(LOG_INFO,"=====begin CertVerify:send CertRequest to au ok,%s",buffer);
			}
			else
				GNET::Log::log(LOG_WARNING,"=====begin CertVerify:send CertRequest to au failed,%s",buffer);
		}
		else
		{
			gnet::AnnounceZoneid3 ann;
			ann.zoneid  = DeliverServer::GetInstance()->GetZoneid();
			ann.aid     = DeliverServer::GetInstance()->GetAid();
			ann.blreset = blreset;

			int ip1 = 0, ip2 = 0, ip3 = 0;
			std::vector<int>	ips;
			const GNET::IFConfig *ifconfig = GNET::IFConfig::GetInstance();
			std::map< const char *, struct in_addr > map = ifconfig->if2ipv4();
			for ( std::map<const char *, struct in_addr>::iterator it = map.begin(), ie = map.end(); it != ie; ++it )
				ips.push_back( (int)(*it).second.s_addr );
			if( ips.size() >= 1 )	ip1 = ips[0];
			if( ips.size() >= 2 )	ip2 = ips[1];
			if( ips.size() >= 3 )	ip3 = ips[2];
			ann.ip1 = ip1;
			ann.ip2 = ip2;
			ann.ip3 = ip3;

			ann.version = 1;
			if(SendProtocol(ann))
			{
				this->bl_keepalive = true;
				GNET::Log::log(LOG_INFO,"send AnnounceZoneid3 to au ok,%s",buffer);
			}
			else
				GNET::Log::log(LOG_WARNING,"send AnnounceZoneid3 to au failed,%s",buffer);
		}
		if (blreset) blreset=false; //only reset authserver once
	}

	void AuthClient::OnDelSession(Session::ID sid)
	{
		GNET::Thread::Mutex::Scoped l(locker_state);
		conn_state = false;
		this->bl_keepalive = false;
		if(bl_zoneid_valid)
		{
			Reconnect();
		}
		//TODO
		GNET::Log::log(LOG_WARNING,"-----disconnect from au-----,aid=%d,zoneid=%d,sid=%d",DeliverServer::GetInstance()->GetAid(),DeliverServer::GetInstance()->GetZoneid(),sid);
	}

	void AuthClient::OnDelSession(Session::ID sid,int state)
	{
		GNET::Thread::Mutex::Scoped l(locker_state);
		conn_state = false;
		this->bl_keepalive = false;
		if(bl_zoneid_valid)
		{
			Reconnect();
		}
		//TODO
		GNET::Log::log(LOG_WARNING,"-----disconnect from au-----,state=0x%x,aid=%d,zoneid=%d,sid=%d",state,DeliverServer::GetInstance()->GetAid(),DeliverServer::GetInstance()->GetZoneid(),sid);
	}

	void AuthClient::OnAbortSession(const GNET::SockAddr &sa)
	{
		GNET::Thread::Mutex::Scoped l(locker_state);
		conn_state = false;
		this->bl_keepalive = false;
		if(bl_zoneid_valid)
		{
			Reconnect();
		}
		// TODO
		GNET::Log::log(LOG_WARNING,"-----connect to au failed-----,aid=%d,zoneid=%d,sid=%d",DeliverServer::GetInstance()->GetAid(),DeliverServer::GetInstance()->GetZoneid(),sid);
	}

	void AuthClient::OnCheckAddress(GNET::SockAddr & sa) const
	{
		// check or setup a new address
		struct addrinfo hints,*res;
		memset(&hints, 0, sizeof(hints));
		hints.ai_family = AF_UNSPEC;
		hints.ai_socktype = SOCK_STREAM;
		GNET::Conf *conf = GNET::Conf::GetInstance();
		std::string host = conf->find(this->Identification(),"address");
		std::string port = conf->find(this->Identification(),"port");
		if (0 != getaddrinfo(host.c_str(), port.c_str(), &hints, &res))
		{
			GNET::Log::log(LOG_ERR,"AuthClient::OnCheckAddress getaddrinfo failed,address=%s.\n",host.c_str());
			return;
		}
		for (struct addrinfo * ai = res; ai != NULL; ai = ai->ai_next)
		{
			struct sockaddr_in *addr = sa;
			memset(addr, 0, sizeof(*addr));
			addr->sin_family = AF_INET;
			addr->sin_addr.s_addr = ((struct sockaddr_in*)ai->ai_addr)->sin_addr.s_addr;
			addr->sin_port = ((struct sockaddr_in*)ai->ai_addr)->sin_port;
			break;
		}
		GNET::Log::log(LOG_INFO,"AuthClient::OnCheckAddress getaddrinfo ok,address=%s,ip=%s.",host.c_str(),inet_ntoa(((const struct sockaddr_in *)sa)->sin_addr));
	}
}
