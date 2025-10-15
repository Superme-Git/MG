
#pragma once

#include "rpcgen.hpp"
#include "LinkClient.hpp"
#include "infoserver/SGetServerInfo.hpp"

namespace lauthc { 

class InfoServer : public GNET::Protocol::Manager
{
	static InfoServer instance;
	size_t accumulate_limit;

	typedef std::map<int,LinkClient*>	LinkClientMap;

	LinkClientMap		m_linkclients;
	int 					m_idGenerator;
protected:
	virtual const Session::State *GetInitState() const;
	virtual bool OnCheckAccumulate(size_t size) const {
		return accumulate_limit == 0 || size < accumulate_limit;
	}
	virtual void OnAddSession(Session::ID sid);
	virtual void OnDelSession(Session::ID sid);
public:
	static InfoServer *GetInstance()   { return &instance; }
	virtual std::string Identification() const  { return "InfoServer"; }
	void SetAccumulate(size_t size)     { accumulate_limit = size; }
	InfoServer() : accumulate_limit(0), m_idGenerator( 0)
	{}

public:
	int NextID()
	{
		return m_idGenerator ++;
	}
	void InsertLinkClient( LinkClient* lc);
	LinkClient* GetLinkClient( int id);

	void MakeServerInfosList( std::list<infoserver::ServerInfos>& list) const;
};

} 
