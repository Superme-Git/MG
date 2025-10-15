
#pragma once

#include "rpcgen.hpp"
#include "crossdata.hpp"

namespace gdeliverd { 

//#define ZONE_SESSION_ALIVE_TIME	180
static const int ZONE_SESSION_ALIVE_TIME = 180;

class CrossServer : public GNET::Protocol::Manager
{
	static CrossServer instance;
	size_t accumulate_limit;

	bool	bl_cross_server;

	typedef std::map<Session::ID, GNET::SockAddr>	SidMap;
	typedef std::map<Session::ID, int/*zoneid*/>	SidZoneMap;
	public:
	SidMap						sid2peermap;
	SidZoneMap					sid2zonemap;
	ZoneSessionManager				zonesessionmanager;		


	protected:
	virtual const Session::State *GetInitState() const;
	virtual bool OnCheckAccumulate(size_t size) const {
		return accumulate_limit == 0 || size < accumulate_limit;
	}
	virtual void OnAddSession(Session::ID sid);
	virtual void OnDelSession(Session::ID sid);
	virtual void OnDelSession(Session::ID sid,int state);
	virtual void OnSetTransport(Session::ID sid, const GNET::SockAddr& local, const GNET::SockAddr& peer);
	public:
	static CrossServer *GetInstance()   { return &instance; }
	virtual std::string Identification() const  { return "CrossServer"; }
	void SetAccumulate(size_t size)     { accumulate_limit = size; }
	CrossServer() 
		: accumulate_limit(0)
		  , bl_cross_server(false) { }

	bool getBlCrossServer()		{ return bl_cross_server; }
	void setBlCrossServer(bool _bl)	{ bl_cross_server = _bl; }

	ZoneSessionManager & getZoneSessionManager()	{ return zonesessionmanager; }
	ZoneSessionManager::ZoneSession * getZoneSession(int zoneid)	
	{ 
		return getZoneSessionManager().find(zoneid);
	}

	SidMap getSidMap() 	{ return sid2peermap; }
	SidZoneMap getSidZoneMap() 	{ return sid2zonemap; }
	
	void BroadcastCross(GNET::Protocol*);
	void BroadcastCross(const GNET::Protocol*);
	void BroadcastCross(GNET::Protocol& p) 		{ BroadcastCross(&p); }
	void BroadcastCross(const GNET::Protocol& p) 	{ BroadcastCross(&p); }

	void BroadcastCrossStatus();
	void SendCrossStatus(Session::ID sid);

	void insertSidZoneMap(Session::ID sid, int zoneid)
	{
		sid2zonemap[sid] = zoneid;
	}

	int getZoneidBySid(Session::ID sid)
	{
		SidZoneMap::iterator it = sid2zonemap.find(sid);
		if(it != sid2zonemap.end())
			return it->second;	
		return 0;
	}
};

} 
