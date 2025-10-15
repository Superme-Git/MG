
#pragma once

#include <log4cpp/Category.hh>
#include "rpcgen.hpp"
#include "gnet/matrixchecker.h"
#include "gnet/MatrixFailure.hpp"
#include "AuthClient.hpp"
#include "mappasswd.h"
#include "speedlimit.h"
#include "serverattr.h"
#include "gnet/Constants.hpp"

namespace gdeliverd { 
	struct SessionInfo
	{
		GNET::Octets 		challenge;
		GNET::Octets		identity;
		GNET::Octets 		response;
		int			userid;	
		int			loginip;
		int			algorithm;
		int			logintype;//0:normal login; 1:usbkey2 use Elec number login; 2:cross login; 3:auany login; 4:sso login
		gnet::MatrixChecker * 	checker;

		SessionInfo() : userid(0),loginip(0),algorithm(0),logintype(gnet::NORMAL_LOGIN),checker(NULL) 
		{
		}
		~SessionInfo()
		{
			if(checker)
			{
				delete checker;
				checker = NULL;
			}
		}

		GNET::Octets getAccountRemoveSSO() const
		{
			std::string username((char*)identity.begin(),identity.size());
			std::basic_string<char>::size_type idx = -1;
			std::basic_string<char>::size_type npos = -1;
			if((idx = username.rfind("@sso")) != npos)
			{
				username = username.substr(0,idx);
				return GNET::Octets(username.c_str(),username.length());
			}
			else
			   return identity;
		}
	};


	class DeliverServer : public GNET::Protocol::Manager
	{
	public:
		static log4cpp::Category& log;
	private:
		size_t accumulate_limit;
		
		typedef std::map<Session::ID,SessionInfo>	SessionInfoMap;	
		typedef std::map<Session::ID,SessionInfoMap>	SessionMap;
		typedef std::set<Session::ID> 			Sidset;
		typedef SpeedLimit<GNET::Octets,OctetsCompare>	OctetsLimit;	
		typedef SpeedLimit<int,std::less<int> >		IntLimit;	

		typedef SessionInfoMap::iterator		SIterator;
		typedef SessionMap::iterator			Iterator;
		typedef SessionMap::const_iterator		CIterator;

		SessionMap	sessions;//client session maps
		Sidset 		linksidset;//linksid set
		OctetsLimit	passwdlimit;//control passwd error exceed limit
		OctetsLimit	aurequestlimit;
		IntLimit	matrixlimit;//control matrixcard error exceed limit
		IntLimit	phonetokenlimit;//control phonetoken error exceed limit
		IntLimit	lockiplimit;//errcode=2&&errocde=3 exceed limit
		ServerAttr 	serverAttr;
		
		IntLimit	couponlimit;//control GetUserCoupon exceed limit
		IntLimit	coupon_exchange_limit;//control CouponExchange exceed limit
		int    		zoneid;
		char   		aid;
		int64_t		boottime;
		bool		support_forcelogin;
		int		forcelogin_timeout;
		protected:

		virtual const Session::State *GetInitState() const;
		virtual bool OnCheckAccumulate(size_t size) const 
		{
			return (accumulate_limit == 0) || (size < accumulate_limit);
		}
		virtual void OnAddSession(Session::ID sid);
		virtual void OnDelSession(Session::ID sid);
		virtual void OnDelSession(Session::ID sid,int state);

		public:
		std::set<int> 	AuQueryIdSet;
		std::set<int> 	TradeQueryIdSet;
		bool		forbid_client_autologin;
		bool		allow_nickname_contain_account;	
		DeliverServer() : accumulate_limit(0),support_forcelogin(false),forbid_client_autologin(false),allow_nickname_contain_account(false) { }
		static DeliverServer *GetInstance()   
		{
			static DeliverServer instance; 
			return &instance; 
		}
		virtual std::string Identification() const  	{ return "DeliverServer"; }
		void SetAccumulate(size_t size)     		{ accumulate_limit = size; }
	
		int GetZoneid() const 			{ return zoneid; }
		char GetAid() const 			{ return aid; }
		ServerAttr& GetServerAttr() 		{ return serverAttr; }
		OctetsLimit & GetPasswdLimit() 		{ return passwdlimit; }
		OctetsLimit & GetAuRequestLimit() 	{ return aurequestlimit; }
		IntLimit & GetMatrixLimit() 		{ return matrixlimit; }
		IntLimit & GetPhoneTokenLimit() 	{ return phonetokenlimit; }
		IntLimit & GetLockIpLimit() 		{ return lockiplimit; }
		IntLimit & GetCouponLimit() 		{ return couponlimit; }
		IntLimit & GetCouponExchangeLimit() 	{ return coupon_exchange_limit; }
		
		void SetZoneid(int _zoneid) 		{ zoneid = _zoneid; } 
		void SetAid(char _aid) 			{ aid = _aid; }

		int64_t getBootTime() const 		{ return boottime; }
		void setBootTime(int64_t _time) 	{ boottime = _time; }

		bool getSupportForceLogin() const	{ return support_forcelogin; }
		void setSupportForceLogin(bool bl)	{ support_forcelogin = bl; }
		int getForceLoginTimeout() const	{ return forcelogin_timeout; }
		void setForceLoginTimeout(int _timeout) { forcelogin_timeout = _timeout; }

		void BroadcastLink(GNET::Protocol*);
		void BroadcastLink(const GNET::Protocol*);
		void BroadcastLink(GNET::Protocol& p) 		{ BroadcastLink(&p); }
		void BroadcastLink(const GNET::Protocol& p) 	{ BroadcastLink(&p); }

		void BroadcastStatus();
		void GetServerLoad();
		void SendStatusToAllLink();
		void SessionError(int userid,Session::ID link_sid,Session::ID client_sid, int errcode, const char *info);
		void SendErrorInfo(Session::ID sid, Session::ID client_sid,int errcode, const char *info);
		void LimitConfig(int window=60,int high=3)
		{
			passwdlimit.config(window,high);
			matrixlimit.config(window,high);
			phonetokenlimit.config(window,high);
			log.info("DeliverServer:PasswdErrLimitConfig:window=%d,high=%d",window,high);
		}
	
		void LockIpLimitConfig(int window=60,int high=10)
		{
			lockiplimit.config(window,high);
			log.info("DeliverServer:LockIpLimitConfig:window=%d,high=%d",window,high);
		}
		
		void CouponLimitConfig(int window=300,int high=1)
		{
			couponlimit.config(window,high);
			log.info("DeliverServer:CouponLimitConfig:window=%d,high=%d",window,high);
		}

		void CouponExchangeLimitConfig(int window=120,int high=1)
		{
			coupon_exchange_limit.config(window,high);
			log.info("DeliverServer:CouponExchangeLimitConfig:window=%d,high=%d",window,high);
		}
		void AuRequsetLimitConfig(int window=600,int high=10)
		{
		   	aurequestlimit.config(window,high);
			log.info("DeliverServer:AuRequestConfig:window=%d,high=%d",window,high);
		}

		SessionInfo * GetSessionInfo(Session::ID link_sid,Session::ID client_sid)
		{
			Iterator it = sessions.find(link_sid);
			if (it != sessions.end())
			{
				SessionInfoMap::iterator itor = (it->second).find(client_sid);
				if(itor != (it->second).end())
					return &(itor->second);
			}	
			return NULL;
		}

		void InsertSessionInfo(Session::ID link_sid,Session::ID client_sid,SessionInfo& info)
		{
			Iterator it = sessions.find(link_sid);
			if (it == sessions.end())
			{
				SessionInfoMap map;
				map.insert(SessionInfoMap::value_type(client_sid,info));
				sessions.insert(SessionMap::value_type(link_sid,map));
			}
			else
			{
				//if the key-value exists,replace the value
				(it->second)[client_sid] = info;
			}	
		}
		
		void RemoveSessionInfoByLinkId(Session::ID link_sid)
		{
			Iterator it = sessions.find(link_sid);
			if(it != sessions.end())
			{
			   for(SIterator sit = it->second.begin();sit != it->second.end();)
			   {
				if(NULL != sit->second.checker)
				{
					delete sit->second.checker;
					sit->second.checker = NULL;	
				}
				it->second.erase(sit++);
			   }
			   sessions.erase(it);
			}
		}

		void RemoveSessionInfo(Session::ID link_sid,Session::ID client_sid)
		{
			Iterator it = sessions.find(link_sid);
			if(it != sessions.end())
			{
				SessionInfoMap::iterator itor = (it->second).find(client_sid);
				if(itor != (it->second).end())
				{
					if(NULL != itor->second.checker)
					{
						gnet::MatrixFailure mf;
						mf.userid = itor->second.checker->GetUid();
						mf.loginip = itor->second.checker->GetIp();
						mf.weight = 1;
						if(!gdeliverd::AuthClient::GetInstance()->SendProtocol(mf))
						{
							log.warn("DeliverServer:RemoveSessionInfo:user(id=%d) send MatrixFailure to au failed!", mf.userid);
						}
						delete itor->second.checker;
						itor->second.checker = NULL;

					}
					(it->second).erase(client_sid);
					if(it->second.size() == 0)
					{
						sessions.erase(link_sid);
					}
				}
			}
		}	

		void AddChecker(Session::ID link_sid,Session::ID client_sid, gnet::MatrixChecker * checker)
		{      
			if(NULL == checker)
				return; 
			Iterator it = sessions.find(link_sid);
			if (it != sessions.end())
			{
				SessionInfoMap::iterator itor = (it->second).find(client_sid);
				if(itor != (it->second).end())
				{
					SessionInfo& info = itor->second;
					if (info.checker) 
					{
						delete info.checker;
					} 
					info.checker = checker;
				}
				else
					delete checker;
			}
			else
				delete checker;	       
		}
	};
} 
