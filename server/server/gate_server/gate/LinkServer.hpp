
#pragma once

#include <iostream>
#include <log4cpp/Category.hh>
#include "common/timer.h"
#include "rpcgen.hpp"
#include "rpcgen/gnet/ServerAttr.hpp"
#include "LinkSession.hpp"
#include "checktimer.hpp"
#include "speedlimit.h"
#include "ProviderMap.hpp"
using namespace std;

#define TIMER_INTERVAL        5

#define USERCOUNT_MIN         1000    
#define USERCOUNT_THRESHOLD   50

#define HALFLOGIN_MIN         100
#define HALFLOGIN_THRESHOLD   15


namespace glinkd {
	class ForbidIpValue
	{
		public:
			long forbid_end_time;

			ForbidIpValue()
			{
			}

			bool IsForbid()
			{
				return time(NULL) < forbid_end_time;
			}
	};	

	class LinkServer : public GNET::Timer::Observer,public GNET::Protocol::Manager
	{
	public:
		static log4cpp::Category& log;
	private:
		size_t			accumulate_limit;
		LinkSessions		sessions;
		int			version;
		gnet::ServerAttr	server_attr;
		int			session_state_check;//control if CheckSessionTimePolicy
		int			keepalive_open;//keepalive open
		int			keepalive_interval;//keepalive time
		int			forbid_ip_interval;//forbid ip time
		int			checkunknownprotocol;//checkunknownprotocol size and policy	
		int			listen_port_open;	
		int			log_level;//only for login and logout	
		int			stat_open;

		GNET::PassiveIO		* passiveio;
		size_t			user_count_limit;
		size_t			halflogin_limit;

		typedef std::set<Session::ID>						SessionIdSet;
		typedef std::vector<std::pair<GNET::Protocol::Type, unsigned int> >	sizepolicy_t;
		typedef SpeedLimit<int,std::less<int> >					IntLimit;
		
		SessionIdSet		halfloginset;
		sizepolicy_t		sizepolicy; // sorted by pair.first
		IntLimit	        linkbrokenlimit;
		
		//for auany
		int			plattype;
		GNET::Octets		serverid;

		const Session::State *GetInitState() const;
		bool OnCheckAccumulate(size_t size) const 
		{
			return accumulate_limit == 0 || size < accumulate_limit;
		}
		void OnAddSession(Session::ID sid);
		void OnDelSession(Session::ID sid);
		void OnSetTransport(Session::ID sid, const GNET::SockAddr& local, const GNET::SockAddr& peer);

		public:
		typedef std::map<int,Session::ID> 		UseridMap;
		typedef std::map<Session::ID, int>		TimeoutMap;
		typedef std::map<int, ForbidIpValue>		ForbidMap;

		UseridMap					useridmap;
		TimeoutMap 					alivetimemap;
		ForbidMap 					forbidipmap;

		LinkServer() : 
			accumulate_limit(0),
			version(10000),
			session_state_check(0),
			keepalive_open(0),
			keepalive_interval(30),
			forbid_ip_interval(60),
			checkunknownprotocol(0),
			listen_port_open(0),
			log_level(15),
			stat_open(0),
			passiveio(NULL),
			user_count_limit(USERCOUNT_MIN),
			halflogin_limit(HALFLOGIN_MIN),
			plattype(-1)
		{ 
		}

		static LinkServer *GetInstance()   
		{
			static LinkServer instance;
			return &instance; 
		}
		std::string Identification() const  { return "LinkServer"; }


		void OnDelSession(Session::ID sid,int state);

		void SetAccumulate(size_t size)     		{ accumulate_limit = size; }
		void SetVersion(int _version) 			{ version = _version; } 
		void SetSessionStateCheck(int _check) 		{ session_state_check = _check; }
		void SetKeepAliveOpen(int _open)		{ keepalive_open = _open; }
		void SetKeepAliveInterval(int _interval)	{ keepalive_interval = _interval; }

		void SetForbidIpInterval(int _interval)		{ forbid_ip_interval = _interval; }

		void SetCheckPolicy(int _sizepolicy) 		{ checkunknownprotocol = _sizepolicy; }
		void SetLogLevel(int _level)			{ log_level = _level; }
		void SetStatOpen(int _open)			{ stat_open = _open; }
		void SetListenPolicy(int _open) 		{ listen_port_open = _open; }
		void SetAliveTime(Session::ID sid, int time) 	{ alivetimemap[sid] = time; }

		void SetIpForbid(int loginip,ForbidIpValue & value)
		{
			//in order to not use timer,erase expire ip at this and Response
			for(ForbidMap::iterator it = forbidipmap.begin();it != forbidipmap.end();)
			{
				if((it->second).IsForbid())
					++it;
				else
					forbidipmap.erase(it++);
			}
			forbidipmap[loginip] = value;
		}

		void RemoveIpForbid(int loginip) { forbidipmap.erase(loginip); }

		ForbidIpValue * GetForbidIpValue(int loginip)
		{
			ForbidMap::iterator it = forbidipmap.find(loginip);
			if (it != forbidipmap.end())
				return &(it->second);
			return NULL;
		}

		int GetSessionStateCheck() const 	{ return session_state_check;}
		int GetKeepAliveOpen() const	 	{ return keepalive_open;}
		int GetKeepAliveInterval() const 	{ return keepalive_interval;}
		int GetForbidIpInterval() const 	{ return forbid_ip_interval;}
		int GetCheckPolicy() const 		{ return checkunknownprotocol;}
		int GetLogLevel() const			{ return log_level; }
		int GetStatOpen() const			{ return stat_open; }
		int GetListenPolicy() const 		{ return listen_port_open;}
		LinkSessions & GetSessions() 		{ return sessions; }

		SessionIdSet & GetHalfLoginSet() 	{ return halfloginset; }
		UseridMap & GetUseridMap() 		{ return useridmap; }

		IntLimit & GetLinkBrokenLimit() 	{ return linkbrokenlimit;}

		void LimitConfig(int window=60,int high=3)
		{
			linkbrokenlimit.config(window,high);
			log.info("LinkServer:LimitConfig:window=%d,high=%d", window, high);
		}

		void StartListen() 
		{ 
			if (NULL == passiveio) 
			{
				passiveio = GNET::Protocol::Server(this); 
				log.info("LinkServer:StartListen");
				if(NULL == passiveio)
				{
					log.error("LinkServer:StartListen:failed,reason:socket create or port bind failed!");
					exit(-1);
				}
			}
		}
		void StopListen()
		{ 
			if (NULL != passiveio) 
			{
				passiveio->Close(); 
				passiveio = NULL;
				log.info("LinkServer:StopListen");
			}
		}
		void TriggerListen();
		bool IsListening() { return passiveio != NULL; }
		void SetUserCountLimit(size_t size) 
		{ 
			user_count_limit = (size > USERCOUNT_MIN ? size : USERCOUNT_MIN); 
		}
		void SetHalfLoginLimit(size_t size) 
		{ 
			halflogin_limit = (size > HALFLOGIN_MIN ? size : HALFLOGIN_MIN); 
		}
		int GetUserCountLimit () const 	{ return user_count_limit;}
		int GetHalfLoginLimit () const  { return halflogin_limit; }

		GNET::Octets& getServerId() 	{ return serverid; }
		void setServerId(const GNET::Octets& _id) { serverid.replace(_id.begin(),_id.size()); }
		int getPlatType() const		{ return plattype; }
		void setPlatType(int _p)	{ plattype = _p; }

		bool ExceedUserLimit(size_t size) 
		{
			return size >= (user_count_limit + USERCOUNT_THRESHOLD); 
		}
		bool UnderUserLimit(size_t size) 
		{ 
			return size <= (user_count_limit - USERCOUNT_THRESHOLD);
		}

		bool ExceedHalfloginLimit(size_t size) 
		{ 
			return size >= (halflogin_limit + HALFLOGIN_THRESHOLD); 
		}
		bool UnderHalfloginLimit(size_t size) 
		{
			return size <= (halflogin_limit - HALFLOGIN_THRESHOLD); 
		}

		void SendErr(Session::ID sid, int errorcode, const char *info);
		void SendErr(Session::ID sid, int errorcode, const GNET::Octets & info);

		void ActiveCloseSession( Session::ID sid );
		void Update();//timer execute funtion
		void Broadcast(int ptype, const GNET::Octets & pdata, int time);
		void ReloadSizePolicy();
		//server attribute
		void SetServerAttr(const gnet::ServerAttr& _server_attr)
		{
			server_attr = _server_attr;
		}

		gnet::ServerAttr & GetServerAttr() { return server_attr; } 

		static bool ValidSid(Session::ID sid) 
		{
			return NULL != GetInstance()->GetLinkSession(sid);
		}
		static bool ValidUser(Session::ID sid, int userid) 
		{
			return NULL != GetInstance()->GetSessions().find(sid,userid);
		}
		LinkSession * GetLinkSession(Session::ID sid)
		{
			return GetSessions().find(sid);
		}
		LinkSession * GetLinkSessionByUserid(int userid) // add by lich 2011/6/10
		{
			UseridMap::iterator it = this->GetUseridMap().find(userid);
			if (it == this->GetUseridMap().end())
				return NULL;
			return this->GetLinkSession(it->second);
		}
		Session::ID GetSidByUserid(int userid)
		{
			UseridMap::iterator it = this->GetUseridMap().find(userid);
			if (it == this->GetUseridMap().end())
				return 0;
			return it->second;
		}

		private:
		virtual bool CheckUnknownProtocol(Session::ID sid, GNET::Protocol::Type type, unsigned int psize);
		virtual void DispatchUnknownProtocol(Session::ID sid, GNET::Protocol::Type type, const GNET::Marshal::OctetsStream & os);

		struct SizepolicyComparator
		{
			bool operator()(const sizepolicy_t::value_type & p1, const sizepolicy_t::value_type & p2) const
			{
				return p1.first < p2.first;
			}
		};

		class Broadcaster
		{
			int ptype;
			GNET::Octets pdata;
			int remain_time;

			int sid_lower;
			int remain_hint;
			GNET::Timer timer;

			// nocopyable
			Broadcaster(const Broadcaster &);
			Broadcaster& operator=(const Broadcaster&);

			public:
			Broadcaster(int _ptype, const GNET::Octets & _pdata, int time, int sid, int onlines)
				: ptype(_ptype), pdata(_pdata), remain_time(time), sid_lower(sid), remain_hint(onlines)
			{
			}

			void update(LinkServer * ls);
		};

		std::set<Broadcaster*> broadcasters;
	};
} 
