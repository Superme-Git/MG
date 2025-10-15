
#pragma once

#include <log4cpp/Category.hh>
#include "rpcgen.hpp"
#include <map>
#include <set>

namespace glinkd
{

typedef int ProviderId;
typedef GNET::Protocol::Manager::Session::ID SessionId;

class ProviderMap
{
	static log4cpp::Category& log;
	typedef std::map<ProviderId, SessionId> map_t;
	map_t map;

public:
	typedef map_t::iterator iterator;
	typedef map_t::const_iterator const_iterator;

	void unbind(ProviderId pvid, SessionId sid)
	{
		// check owner: psid
		map.erase(pvid);
	}

	void bind(ProviderId pvid, SessionId psid)
	{
		std::pair<iterator, bool> r = map.insert(std::make_pair(pvid, psid));
		if (false == r.second)
		{
			log.info("pvid=%d bind link session replace,sid: %d->%d", pvid, r.first->second, psid);
			r.first->second = psid;
		}
		log.info("glinkd bind provider ok,pvid=%d",pvid);
	}

	iterator begin() { return map.begin(); }
	iterator end()   { return map.end(); }

	void erase(iterator it)        { map.erase(it); }
	iterator find(ProviderId pvid) { return map.find(pvid); }

	const_iterator find(ProviderId pvid) const { return map.find(pvid); }

	const_iterator begin() const { return map.begin(); }
	const_iterator end()   const { return map.end(); }

	void collectSessionId(std::set<SessionId> & out) const
	{
		for (const_iterator it = begin(), e = end(); it != e; ++it)
			out.insert(it->second);
	}
};
}

