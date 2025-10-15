
#pragma once

#include "rpcgen.hpp"

namespace gdeliverd { 

	class AuthClient : public GNET::Protocol::Manager
	{
		size_t        		accumulate_limit;
		Session::ID  		sid;
		bool          		conn_state;
		GNET::Thread::Mutex 	locker_state;

		enum { BACKOFF_INIT = 2, BACKOFF_DEADLINE = 8 };

		size_t 		backoff;
		bool		blreset;//whether clear all onlineuser of this zoneid on Au
		int		use_cert;//1-use,default 0
		int		isabroad;//1-true,default 1

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

		GNET::Octets 	osec_key;
		GNET::Octets 	authd_cert;
		bool 		bl_keepalive;//set true after Cert Verify or send AnnounceZoneid
		bool		bl_zoneid_valid;//for AuthdVersion

		static AuthClient *GetInstance() 		
		{
			static AuthClient instance; 
			return &instance; 
		}
		virtual std::string Identification() const 	{ return "AuthClient"; }
		void SetAccumulate(size_t size)    		{ accumulate_limit = size; }

		AuthClient()
			: accumulate_limit(0), conn_state(false)
			, locker_state("AuthClient::locker_state")
			, backoff(BACKOFF_INIT)
			, blreset(true)
			, use_cert(0)
			, isabroad(1) 
			, bl_keepalive(false)
			, bl_zoneid_valid(true) { }

		bool SendProtocol(const GNET::Protocol & p) 
		{
			/*
			 * CertRequest.type	545
			 * CertKey.type		547	   
			 * AnnounceZoneid3	527
			 */ 
			if(bl_keepalive || (p.GetType() == 545 || p.GetType() == 547 || p.GetType() == 527))
			{
				return conn_state && Send(sid, p);
			}
			return false;
		}

		bool SendProtocol(const GNET::Protocol * p) 
		{ 
			if(bl_keepalive || (p->GetType() == 545 || p->GetType() == 547 || p->GetType() == 527))
			{
				return conn_state && Send(sid, p);
			}
			return false;
		}

		bool isConnected() const		{ return conn_state; }
		Session::ID getSid() const 		{ return sid; }
		bool GetBlreset() 			{ return blreset; }

		void SetCertPolicy(int _use)		{ use_cert = _use; }
		int  GetCertPolicy() const 		{ return use_cert; }

		void SetAbroadPolicy(int _use) 		{ isabroad = _use; }
		int  GetAbroadPolicy() const 		{ return isabroad; }

		void CloseAuSession() 			{ Close(sid); }
	};


} 
