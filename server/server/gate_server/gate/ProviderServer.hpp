
#pragma once

#include "rpcgen.hpp"
#include <log4cpp/Category.hh>

namespace glinkd { 

class ProviderServer : public GNET::Protocol::Manager
{
public:
	static log4cpp::Category& log;
private:
	static ProviderServer instance;
	size_t accumulate_limit;
	const Session::State *GetInitState() const;
	bool OnCheckAccumulate(size_t size) const {
		return accumulate_limit == 0 || size < accumulate_limit;
	}
	void OnAddSession(Session::ID sid);
	void OnDelSession(Session::ID sid);
	void OnDelSession(Session::ID sid,int state);

	int linkid;
public:
	static ProviderServer *GetInstance()   { return &instance; }
	std::string Identification() const     { return "ProviderServer"; }
	void SetAccumulate(size_t size)        { accumulate_limit = size; }
	ProviderServer() : accumulate_limit(0) { linkid = 0; }
	void setLinkId(int linkid)		{ assert(0 != linkid); this->linkid = linkid; }
	int getLinkId()			{ assert(0 != linkid); return linkid; }
};

} 
