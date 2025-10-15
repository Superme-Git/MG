#pragma once

#include "rpcgen.hpp"
#include "cross/UserInfo.hpp"
#include "mappasswd.h"

namespace gdeliverd {

class ZoneSessionManager
{
	public:
		class ZoneSession
		{
			private:
				int 		zoneid;
				GNET::Protocol::Manager::Session::ID 	sid;
				GNET::SockAddr	peer;
				time_t 		boottime;
				time_t 		timestamp;
				int 		online_num;
				int		max_online_num;

				ZoneSession(const ZoneSession&);
				ZoneSession& operator=(const ZoneSession&);
			public:
				ZoneSession(int _zoneid, GNET::Protocol::Manager::Session::ID _sid, const GNET::SockAddr& _peer, time_t _boottime) 
					: zoneid(_zoneid),sid(_sid),peer(_peer),boottime(_boottime),timestamp(time(NULL))
					{ }

				const GNET::Protocol::Manager::Session::ID getSid() const 	{ return sid; }
				GNET::SockAddr getPeer() const			 		{ return peer; }
				const struct in_addr& getPeerAddr() const			{ return ((const struct sockaddr_in*)peer)->sin_addr; }
				const int getIntPeer() 	
				{
					return ((struct sockaddr_in*)peer)->sin_addr.s_addr;
				}

				const char* getStringPeer() const
				{
					return inet_ntoa(getPeerAddr());
				}

				const time_t getBoottime() const	{ return boottime; }
				const time_t getTimestamp() const	{ return timestamp; }

				void setPeer(const GNET::SockAddr &_peer)         { peer = _peer; }
				void setTimestamp(time_t _timestamp)	{ timestamp = _timestamp; }
				void setOnlineNum(int _num)		{ online_num = _num; }
				void setMaxOnlineNum(int _num)		{ max_online_num = _num; }	
		};


	private:
		typedef std::map<int/*zoneid*/,ZoneSession*>	ZoneMap; 
		ZoneMap					zone2sessionmap;	

		ZoneSessionManager(const ZoneSessionManager&);
		ZoneSessionManager& operator=(const ZoneSessionManager&);

	public:
		ZoneSessionManager()	{ }
		~ZoneSessionManager()	{ }

		bool insert(int zoneid,GNET::Protocol::Manager::Session::ID sid,GNET::SockAddr& peer,time_t bootime)
		{
			std::pair<ZoneMap::iterator, bool> ii = zone2sessionmap.insert(std::make_pair(zoneid, (ZoneSession*)NULL));
			if (ii.second)
			{
				ii.first->second = new ZoneSession(zoneid,sid,peer,bootime);
				return true;
			}
			return false;
		}


		void erase(int zoneid)
		{
			ZoneMap::iterator it = zone2sessionmap.find(zoneid);
			if (it != zone2sessionmap.end())
			{
				delete it->second;
				zone2sessionmap.erase(it);
			}
		}

		ZoneSession * find(int zoneid)
		{
			ZoneMap::iterator it = zone2sessionmap.find(zoneid);
			if (it != zone2sessionmap.end())
				return it->second;
			return NULL;
		}

		ZoneSession * find(int zoneid,GNET::Protocol::Manager::Session::ID sid)
		{
			ZoneMap::iterator it = zone2sessionmap.find(zoneid);
			if (it != zone2sessionmap.end() && (it->second->getSid() == sid))
				return it->second;
			return NULL;
		}

		int size() const { return zone2sessionmap.size(); }

};

class UserIdentityCache : public GNET::Timer::Observer
{
	public:
		class Identity
		{
			public:
			int	src_zoneid;
			int	userid;
			int	algorithm;
			GNET::Octets password;/*md5(account,ticket)*/
			int	loginip;
			int	addtime;
			public:
			Identity() { addtime = time(NULL);}
			Identity(int s, int u, int algo, const GNET::Octets& p, int l) : src_zoneid(s),userid(u),algorithm(algo),password(p),loginip(l)  
			{
				addtime = time(NULL);
			}
			Identity(const Identity& r) : src_zoneid(r.src_zoneid),userid(r.userid),algorithm(r.algorithm),password(r.password),loginip(r.loginip)  
			{
				addtime = time(NULL);
			}

			void GetIdentity(int& szoneid,int& uid, int& algo, GNET::Octets& p, int& ip)
			{
				szoneid = src_zoneid;
				uid  = userid;
				algo = algorithm;
				p = password;
				ip = loginip;
			}

		};

	//	typedef std::map<int/*userid*/, Identity> IdentityMap;
		typedef std::map<GNET::Octets/*account*/, Identity,OctetsCompare> IdentityMap;
	//	typedef	GNET::simplelru<GNET::Octets,TicketEntry,OctetsCompare> LRUMap;
	private:

		enum 
		{
			DEFAULT_CACHE_MAXTIME = 180,
		};

		IdentityMap identity_map;

		int cache_max_time;

		UserIdentityCache()
		{
			cache_max_time = DEFAULT_CACHE_MAXTIME;
		}

	public:
		bool Initialize() { return true; }

		static UserIdentityCache * GetInstance() 
		{ 
			static UserIdentityCache instance; 
			return &instance; 
		}
		
		int getCacheMaxTime() const { return cache_max_time;}
		void setCacheMaxTime(int _time) { cache_max_time = _time; }
		bool Exist(const GNET::Octets& account)
		{
			IdentityMap::const_iterator it = identity_map.find(account);
			return (it != identity_map.end());
		}

		bool Find(const GNET::Octets& account, Identity & iden)
		{
			IdentityMap::const_iterator it = identity_map.find(account);
			if (it == identity_map.end())
				return false;
			iden = Identity(it->second);
			return true;
		}

		void Insert(GNET::Octets& account, const Identity & iden)
		{
			if(Exist(account))
			{
				Remove(account);
				identity_map.insert(std::make_pair(account,iden));
				GNET::Log::log(LOG_INFO,"------UserIdentityCache replace old,account=%.*s",account.size(),(char*)account.begin());
			}
			else
			{
				identity_map.insert(std::make_pair(account,iden));
				GNET::Log::log(LOG_INFO,"------UserIdentityCache insert,account=%.*s",account.size(),(char*)account.begin());
			}
		}

		void Remove(GNET::Octets& account)
		{
			identity_map.erase(account);
			GNET::Log::log(LOG_INFO,"------UserIdentityCache remove,account=%.*s",account.size(),(char*)account.begin());
		}

		virtual void Update()
		{
			int now = time(NULL);
			IdentityMap::iterator it, ite = identity_map.end();
			for (it = identity_map.begin(); it != ite;)
			{
				if (now - it->second.addtime > cache_max_time)
				{
					GNET::Log::log(LOG_INFO,"------UserIdentityCache remove expire,account=%.*s",it->first.size(),(char*)it->first.begin());
					identity_map.erase(it++);
				}
				else
					++it;
			}
		}	
};

/*
   class TicketPasswd : public GNET::Timer::Observer
   {
   public:
   class TicketEntry
   {
   int    userid;
   int    algorithm;
   GNET::Octets password;
   int addtime;
   public:
   TicketEntry() { }
   TicketEntry(int u, int algo, const GNET::Octets& p) : userid(u),algorithm(algo),password(p)  {}
   TicketEntry(const TicketEntry& r) : userid(r.userid),algorithm(r.algorithm),password(r.password)  {}

   void GetTicket(int& uid, int& algo, GNET::Octets& p)
   {
   uid  = userid;
   algo = algorithm;
   p = password;
   }
   };


   typedef	GNET::simplelru<GNET::Octets,TicketEntry,OctetsCompare> LRUMap;
   LRUMap lrumap;
   public:
   TicketPasswd():lrumap(100000) {  }
   ~TicketPasswd() { }
   static TicketPasswd * GetInstance() 
   { 
   static TicketPasswd instance; 
   return &instance; 
   }

//	size_t Size() { return lrumap.size();}
size_t GetCacheLimit() { return lrumap.getMaxSize();}
void SetMaxSize(size_t size) 
{
if(size <= 100000)
lrumap.setMaxSize(100000);
else 
lrumap.setMaxSize(size); 
}
bool GetPasswd( GNET::Octets username, int& userid, int& algorithm, GNET::Octets& passwd )
{
TicketEntry te;
if(lrumap.find( username,te ))
{
te.GetTicket(userid, algorithm, passwd);
return true;
}
return false;
}
void SetPasswd( GNET::Octets username, int userid, int algorithm, GNET::Octets& passwd )
{
lrumap.put(username,TicketEntry(userid,algorithm,passwd));
}

void DelPasswd( GNET::Octets username )
{
lrumap.del(username);
}

};
 */

class UserSrc
{
	public:
		std::map<int/*userid*/,int/*src_zoneid*/> uid2zoneidmap;
		UserSrc() {}

		static UserSrc * GetInstance() 
		{
			static UserSrc instance;
			return &instance;
		}

		void setUserSrcZoneid(int userid, int src_zoneid)
		{
			uid2zoneidmap[userid] = src_zoneid;
		}

		int getSrcZoneidByUserid(int userid)
		{
			return uid2zoneidmap[userid];
		}
};

}
