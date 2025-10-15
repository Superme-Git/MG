
#pragma once

#include "rpcgen.hpp"
#include "pf.hpp"

namespace lauthc { 


struct GameServerInfo
{
	std::string			m_name;
	int 					m_listenport1;
	int 					m_listenport2;
	std::string			m_gsip;
	std::string 		m_gsjmxport1;
	std::string 		m_gsjmxport2;

	std::string		host;
	std::string		ip;
	int 				port;
	int 				eport;
};


class LinkClient : public GNET::Protocol::Manager
{
	friend class ReconnectTask;
	friend class KeepAliveTask;

	static LinkClient instance;
	size_t        accumulate_limit;
	Session::ID   sid;
	bool          conn_state;
	bool 			onlined;
	GNET::Thread::Mutex locker_state;

	portforward::Forwards				m_forwards;

	GameServerInfo				m_gameserverinfo;

	enum
	{
		BACKOFF_INIT = 30,
		BACKOFF_DEADLINE = 600,
		BACKOFF_ERRINFO = 300,
		BACKOFF_NEWLOCAL_FAILED = 60,

		KEEP_ALIVE_PERIOD = 30,
	};

	size_t backoff;


	void Reconnect();
protected:
	virtual const Session::State *GetInitState() const;
	virtual bool OnCheckAccumulate(size_t size) const {
		return accumulate_limit == 0 || size < accumulate_limit;
	}
	virtual void OnAddSession(Session::ID sid);
	virtual void OnDelSession(Session::ID sid, int status);
	virtual void OnDelSession(Session::ID) {}
	virtual void OnAbortSession(const GNET::SockAddr &sa);
	virtual void OnCheckAddress(GNET::SockAddr &sa) const;
public:
	LinkClient( const GameServerInfo& gsi);

	static LinkClient *GetInstance() { return &instance; }
	virtual std::string Identification() const  { return "LinkClient"; }
	void SetAccumulate(size_t size)     { accumulate_limit = size; }
	LinkClient()
		: accumulate_limit(0), conn_state(false)
		, locker_state("LinkClient::locker_state")
		, backoff(BACKOFF_INIT) { }

	bool SendProtocol(const GNET::Protocol & p) { return conn_state && Send(sid, p); }
	bool SendProtocol(const GNET::Protocol * p) { return conn_state && Send(sid, p); }

	bool isConnected() const { return conn_state; }
	Session::ID getSid() const { return sid; }

	void CloseSession()
	{
		if( conn_state)
			Close(sid);
	}

	const portforward::Forwards& GetForwards() const
	{
		return m_forwards;
	}
	portforward::Forwards& GetForwards()
	{
		return m_forwards;
	}
	const std::string& GetName() const
	{
		return m_gameserverinfo.m_name;
	}

	const GNET::Octets GetNameAsOctets() const
	{
		return GNET::Octets( m_gameserverinfo.m_name.c_str(), m_gameserverinfo.m_name.length());
	}

	int GetListenPort1() const
	{
		return m_gameserverinfo.m_listenport1;
	}
	int GetListenPort2() const
	{
		return m_gameserverinfo.m_listenport2;
	}

	const std::string& GetGsIP() const
	{
		return m_gameserverinfo.m_gsip;
	}
	const std::string& GetGsJMXPort1() const
	{
		return m_gameserverinfo.m_gsjmxport1;
	}
	const std::string& GetGsJMXPort2() const
	{
		return m_gameserverinfo.m_gsjmxport2;
	}

	void BackoffForErrorInfo()
	{
		if( backoff < BACKOFF_ERRINFO)
			backoff = BACKOFF_ERRINFO;
	}
	void BackoffForNewLocalFailed()
	{
		if( backoff < BACKOFF_NEWLOCAL_FAILED)
			backoff = BACKOFF_NEWLOCAL_FAILED;
	}

	void SetOnlined()
	{
		onlined = true;
	}
	bool IsOnlined() const
	{
		return onlined;
	}

	virtual bool CheckUnknownProtocol(GNET::Protocol::Manager::Session::ID , GNET::Protocol::Type , unsigned int );
	virtual void DispatchUnknownProtocol(GNET::Protocol::Manager::Session::ID , GNET::Protocol::Type , const GNET::Marshal::OctetsStream&) {}

};


} 
