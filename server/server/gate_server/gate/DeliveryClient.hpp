
#pragma once

#include <log4cpp/Category.hh>
#include "rpcgen.hpp"

namespace glinkd { 

class DeliveryClient : public GNET::Protocol::Manager
{
public:
	static log4cpp::Category& log;
private:
	static DeliveryClient instance;
	size_t        accumulate_limit;
	Session::ID   sid;
	bool          conn_state;
	GNET::Thread::Mutex locker_state;

	enum { BACKOFF_INIT = 2, BACKOFF_DEADLINE = 8 };
	size_t backoff;

	void Reconnect();
protected:
	virtual const Session::State *GetInitState() const;
	virtual bool OnCheckAccumulate(size_t size) const {
		return accumulate_limit == 0 || size < accumulate_limit;
	}
	virtual void OnAddSession(Session::ID sid);
	virtual void OnDelSession(Session::ID sid);
	virtual void OnDelSession(Session::ID sid,int state);
	virtual void OnAbortSession(const GNET::SockAddr &sa);
	virtual void OnCheckAddress(GNET::SockAddr &sa) const;
public:
	static DeliveryClient *GetInstance() { return &instance; }
	virtual std::string Identification() const  { return "DeliveryClient"; }
	void SetAccumulate(size_t size)     { accumulate_limit = size; }
	DeliveryClient()
		: accumulate_limit(0), conn_state(false)
		, locker_state("DeliveryClient::locker_state")
		, backoff(BACKOFF_INIT) { }

	bool SendProtocol(const GNET::Protocol & p) { return conn_state && Send(sid, p); }
	bool SendProtocol(const GNET::Protocol * p) { return conn_state && Send(sid, p); }

	bool isConnected() const { return conn_state; }
	Session::ID getSid() const { return sid; }

};


} 
