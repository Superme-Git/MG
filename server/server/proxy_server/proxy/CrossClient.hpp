
#pragma once

#include "rpcgen.hpp"

namespace gdeliverd { 

class CrossClient : public GNET::Protocol::Manager
{
	static CrossClient instance;
	size_t        accumulate_limit;
	Session::ID   sid;
	bool          conn_state;
	GNET::Thread::Mutex locker_state;

	enum { BACKOFF_INIT = 2, BACKOFF_DEADLINE = 8 };
	size_t backoff;
	bool	bl_cross_client;
	bool	bl_handshake_ok;
	int	crossserver_online_num;
	int	crossserver_max_online_num;
	int	cross_zoneid;

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
	virtual void OnSetTransport(Session::ID sid, const GNET::SockAddr& local, const GNET::SockAddr& peer);
public:
	static CrossClient *GetInstance() { return &instance; }
	virtual std::string Identification() const  { return "CrossClient"; }
	void SetAccumulate(size_t size)     { accumulate_limit = size; }
	CrossClient()
		: accumulate_limit(0), conn_state(false)
		, locker_state("CrossClient::locker_state")
		, backoff(BACKOFF_INIT)
		, bl_cross_client(false)
		, bl_handshake_ok(false)
       		, crossserver_online_num(0)
		, crossserver_max_online_num(0)	
		, cross_zoneid(0)	{ }

	bool SendProtocol(const GNET::Protocol & p) { return conn_state && Send(sid, p); }
	bool SendProtocol(const GNET::Protocol * p) { return conn_state && Send(sid, p); }

	bool isConnected() const 	{ return conn_state; }
	Session::ID getSid() const 	{ return sid; }

	bool getBlCrossClient()		{ return bl_cross_client; }
	void setBlCrossClient(bool _bl)	{ bl_cross_client = _bl; }

	bool getBlHandshake() const	{ return bl_handshake_ok; }
	void setBlHandshake(bool _bl)	{ bl_handshake_ok = _bl; }

	int getCrossOnlineNum() const 	{ return crossserver_online_num; }
	int getCrossMaxOnlineNum() const { return crossserver_max_online_num; }

	void setCrossOnlineNum(int _num) { crossserver_online_num = _num;}
	void setCrossMaxOnlineNum(int _num) { crossserver_max_online_num = _num;}
	
	bool BlCrossOverload() 		{ return crossserver_online_num >= crossserver_max_online_num; }

	int getCrossZoneid() const		{ return cross_zoneid; }
	void setCrossZoneid(int _zoneid)	{  cross_zoneid = _zoneid; }
};


} 
