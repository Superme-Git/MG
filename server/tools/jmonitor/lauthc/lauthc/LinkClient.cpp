
#include "LinkClient.hpp"
#include "common/timertask.h"
#include "InfoServer.hpp"
#include <arpa/inet.h>
#include "protocols.hpp"

std::string RandPort( const lauthc::GameServerInfo& sinfo);

namespace lauthc { 

	LinkClient LinkClient::instance;

	class ReconnectTask: public GNET::Thread::Runnable
	{
	public:
		LinkClient* manager;

		ReconnectTask(LinkClient* m): manager(m) {}
		void Run()
		{
			GNET::Conf *conf = GNET::Conf::GetInstance( NULL);
			manager->SetAccumulate(atoi(conf->find(manager->Identification(), "accumulate").c_str()));
			conf->put( manager->Identification(), "address", manager->m_gameserverinfo.ip);
			std::string port = RandPort( manager->m_gameserverinfo);
			conf->put( manager->Identification(), "port", port);
			GNET::Protocol::Client(manager);
		   delete this;

		   portforward::OutLog( "ReconnectTask name=%s server=%s port=%s\n", manager->GetName().c_str(), manager->m_gameserverinfo.ip.c_str(), port.c_str());
		}
	};

	class KeepAliveTask: public GNET::Thread::Runnable
	{
	public:
		LinkClient* manager;

		KeepAliveTask(LinkClient* m): manager(m) {}
		void Run()
		{
			if( manager->IsOnlined())
			{
				gnet::KeepAlive		keepalive;
				manager->SendProtocol( keepalive);
			   portforward::OutLog( "KeepAliveTask name=%s\n", manager->GetName().c_str());
			}
			GNET::Thread::HouseKeeper::AddTimerTask( this, LinkClient::KEEP_ALIVE_PERIOD);
		}
	};


	LinkClient::LinkClient( const GameServerInfo& gsi)
		: accumulate_limit(0), conn_state(false), onlined( false)
		, locker_state("LinkClient::locker_state")
		, m_gameserverinfo( gsi), backoff(BACKOFF_INIT)
	{
		GNET::Thread::HouseKeeper::AddTimerTask( new KeepAliveTask( this), LinkClient::KEEP_ALIVE_PERIOD);
	}

	void LinkClient::Reconnect()
	{
		GNET::Thread::HouseKeeper::AddTimerTask(new ReconnectTask(this), backoff);
		backoff *= 2;
		if (backoff > BACKOFF_DEADLINE)
			backoff = BACKOFF_DEADLINE;
	}

	const GNET::Protocol::Manager::Session::State* LinkClient::GetInitState() const
	{
		return &lauthc::state::Client;
	}

	void LinkClient::OnAddSession(Session::ID sid)
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

		portforward::OutLog( "LinkClient::OnAddSession linkid = %d sid = %d name=%s\n", m_forwards.GetLinkClientID(), sid, GetName().c_str());
	}

	void LinkClient::OnDelSession(Session::ID sid, int status)
	{
		GNET::Thread::Mutex::Scoped l(locker_state);
		conn_state = false;
		onlined = false;
		Reconnect();

		portforward::OutLog( "LinkClient::OnDelSession linkid = %d sid = %d status = %d(%05x) name=%s\n", m_forwards.GetLinkClientID(), sid, status, status, GetName().c_str());

		m_forwards.cmdCloseAll();
	}

	void LinkClient::OnAbortSession(const GNET::SockAddr &sa)
	{
		GNET::Thread::Mutex::Scoped l(locker_state);
		conn_state = false;
		onlined = false;
		Reconnect();

		portforward::OutLog( "LinkClient::OnAbortSession linkid = %d name=%s\n", m_forwards.GetLinkClientID(), GetName().c_str());
	}

	void LinkClient::OnCheckAddress(GNET::SockAddr & sa) const
	{
		sockaddr_in* addr = sa;

		char		buffer[16];
		sprintf( buffer, "%d", ntohs( addr->sin_port));
		std::string gameserverport = buffer;
		std::string gameserverip = inet_ntoa( addr->sin_addr);

		portforward::OutLog( "LinkClient::OnCheckAddress linkid = %d name=%s server=%s port=%s\n", m_forwards.GetLinkClientID(), GetName().c_str(), gameserverip.c_str(), gameserverport.c_str());
	}

	bool LinkClient::CheckUnknownProtocol(GNET::Protocol::Manager::Session::ID , GNET::Protocol::Type type, unsigned int size)
	{
		portforward::OutLog( "LinkClient::CheckUnknownProtocol linkid = %d name=%s type=%d size=%d\n", m_forwards.GetLinkClientID(), GetName().c_str(), type, size);
		return true;
	}

} // namespace lauthc {
