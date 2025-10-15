
#pragma once

#include <set>
#include "rpcgen.hpp"
#include "ProviderMap.hpp"
#include "pf.hpp"

namespace glinkd
{


class LinkSession
{
	int userid;
	ProviderMap providers;
	GNET::Octets challenge;
	GNET::Octets response;
	GNET::Octets identity;
	GNET::SockAddr peer;
	int login;//as gnet::link::SetLogin 0:logout,1:login
	time_t logintime;
	int	logintype;
	GNET::Octets	mid;

	portforward::Forwards forwards;

	LinkSession(const LinkSession&);
	void operator=(const LinkSession&);
public:

	LinkSession()
	{
		userid = 0;
		login  = 0;
		logintime = time(NULL);
		logintype = 0;
	}

	portforward::Forwards * getForwards() { return &forwards; }

	const int getUserId() const     { return userid; }
	ProviderMap * getProviders()    { return & providers; }

	void setUserId(int _userid)                       { userid = _userid; }
	void setChallenge(const GNET::Octets &_challenge) { challenge = _challenge; }
	void setIdentity(const GNET::Octets &_identity)   { identity = _identity; }
	void setResponse (const GNET::Octets &_response)  { response = _response; }
	void setPeer(const GNET::SockAddr &_peer)         { peer = _peer; }
	void setLogin(int _login)                         { login = _login; }
	void setLogintime(time_t _logintime)              { logintime = _logintime; }
	void setLogintype(time_t _type)		          { logintype = _type; }
	void setMid (const GNET::Octets &_mid)  	  { mid = _mid; }

	const struct in_addr& getPeerAddr() const     {  return ((const struct sockaddr_in*)peer)->sin_addr; }
	GNET::SockAddr getPeer()			{ return peer; }
	const GNET::Octets& getChallenge() const	{ return challenge; }
/*	const*/ GNET::Octets& getResponse()/* const*/ 	{ return response; }
/*	const*/ GNET::Octets& getIdentity()/* const*/ 	{ return identity; }
	const int  getLogin() const                   	{ return login; }
	const time_t  getLogintime() const		{ return logintime; }
	const int  getLogintype() const			{ return logintype; }
	GNET::Octets& getMid() 				{ return mid; }
};

class LinkSessions
{
public:
	static log4cpp::Category& log;
	typedef std::map<SessionId, LinkSession*> links_map;

private:
	links_map links;
	ProviderMap defaultProviders;

	LinkSessions(const LinkSessions&);
	void operator=(const LinkSessions&);
public:
	LinkSessions()
	{
	}

	~LinkSessions()
	{
		// LinkSessions 定义在单件的 LinkServer 中，只有在程序退出时才会销毁。
		// 按 share 程序传统，这里的析构操作可以不做。
		/*
		links_map tmp;
		tmp.swap(this->links);
		for (links_map::iterator it = tmp.begin(), ie = tmp.end(); it != ie; ++it)
			delete it->second;
		*/
	}

	void unbind(ProviderId pvid, const std::set<int> & linkids, SessionId psid)
	{
		if (linkids.empty())
		{
			getDefaultProviders()->unbind(pvid, psid);
			return;
		}

		for (std::set<int>::const_iterator it = linkids.begin(), end = linkids.end(); it != end; ++it)
		{
			links_map::iterator linkit = links.find(*it);
			if (linkit != links.end())
				linkit->second->getProviders()->unbind(pvid, psid);
		}
	}

	void bind(ProviderId pvid, const std::set<int> & linkids, SessionId psid)
	{
		log.info("LinkSessions:bind:linkids.size = %d", linkids.size());
		if (linkids.empty())
		{
			getDefaultProviders()->bind(pvid, psid);
			return;
		}

		for (std::set<int>::const_iterator it = linkids.begin(), end = linkids.end(); it != end; ++it)
		{
			links_map::iterator linkit = links.find(*it);
			if (linkit != links.end())
				linkit->second->getProviders()->bind(pvid, psid);
			else
				log.warn("LinkSessions:bind:linksid not found,pvid=%d, psid=%d linksid=%d", pvid, psid, *it);
		}
	}

	bool insert(SessionId sid)
	{
		std::pair<links_map::iterator, bool> ii = links.insert(std::make_pair(sid, (LinkSession*)NULL));
		if (ii.second)
		{
			ii.first->second = new LinkSession();
			return true;
		}
		return false;
	}


	void erase(SessionId sid)
	{
		links_map::iterator it = links.find(sid);
		if (it != links.end())
		{
			delete it->second;
			links.erase(it);
		}
	}

	LinkSession * find(SessionId sid)
	{
		links_map::iterator it = links.find(sid);
		if (it != links.end())
			return it->second;
		return NULL;
	}

	LinkSession * find(SessionId sid,int userid)
	{
		links_map::iterator it = links.find(sid);
		if (it != links.end() && it->second->getUserId() == userid)
			return it->second;
		return NULL;
	}


	ProviderMap * getDefaultProviders()
	{
		return & defaultProviders;
	}

	links_map::iterator begin()                    { return links.begin(); }
	links_map::iterator end()                      { return links.end(); }
	links_map::iterator lower_bound(SessionId sid) { return links.lower_bound(sid); }
	links_map::iterator upper_bound(SessionId sid) { return links.upper_bound(sid); }

	int size() const { return links.size(); }
};

}

