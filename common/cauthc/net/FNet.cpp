#include "Fnet.h"
#include "win32/share.hpp"
#include "libshare.h"
#include "rpcgen.hpp"
#include "loginbase.h"
#include "protocols.hpp"

#include "../../../dependencies/LJFilePack/LJFP_Version.h"

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

namespace FireNet
{
#define GAMEID 88
#define GAMEKEY "b18a26ffc632752987bd24a7bf0353f3"
#define SESSION "ewogICJhdmF0YXIiIDogIiIsCiAgImNoYW5uZWwiIDogImxvY29qb3kiLAogICJwbGF0Zm9ybWlkIiA6ICIyMTIwOTQ5MTYiLAogICJuaWNrIiA6ICIiLAogICJsb2dpbnRpbWUiIDogIjIwMTUtMTAtMTIgMDA6MDA6MTkiLAogICJjcGluZm8iIDogIiIsCiAgInNpZ24iIDogIjAzZjE0NGY4NGUzMWIzNGUyMWNhZTg3NzkxYTEwZmE1IiwKICAic2V4IiA6ICIiLAogICJhcHBpZCIgOiAiMTAwMTIiLAogICJuYW1lIiA6ICIiCn0="

#define CHILDCHANNEL ""
#define DEVICEID "33:44::55:66:77:88"
#define SERVERID "1101961001"

#if defined BYTE_ORDER_BIG_ENDIAN
	uint16_t BYTE16Order(BYTE16Order& x){ return x; }
	uint32_t BYTE32Order(uint32_t x){ return x; }
	uint64_t BYTE64Order(uint64_t x){ return x; }
#else

#define BYTE8Order(x) x
#define DEFINE_BYTEORDER_FUNCTION(ret,prevNum,num,type) ret BYTE##num##Order( ret x) \
			{\
	union{ ret mm; type m[2]; }input,output;\
	input.mm=x;\
	output.m[0]=BYTE##prevNum##Order(input.m[1]); \
	output.m[1]=BYTE##prevNum##Order(input.m[0]); \
	return output.mm; \
			}
	DEFINE_BYTEORDER_FUNCTION(uint16_t, 8, 16, unsigned char)
	DEFINE_BYTEORDER_FUNCTION(uint32_t, 16, 32, uint16_t)
	DEFINE_BYTEORDER_FUNCTION(uint64_t, 32, 64, uint32_t)
#endif

	INetIO* g_pNetManager = 0;

	INetIO* GetNetIO()
	{
		return g_pNetManager;
	}

	class NetConnector;
	class LoginScopedPtr;
	class NetSessionManager : public aio::Protocol::Manager
	{
		typedef std::map<FireNet::lid_t, NetConnector *> Logins;
		Logins     logins;
		FireNet::lid_t      mLid;
		FireNet::Mutex mutex;
	public:
		bool findLogin(Session::ID mSID, LoginScopedPtr & ptr);
		bool findLogin(FireNet::lid_t mLid, LoginScopedPtr & ptr);
	public:
		NetSessionManager();
		FireNet::IConnector * CreateConnector(const FireNet::CLoginParam& mParam, FireNet::ILoginConnection * callback);
		FireNet::IConnector * GetConnector(FireNet::lid_t mLid);
		void delLogin(FireNet::IConnector * login);
	protected:
		virtual void OnDelSession(Session* session) override;
		virtual void OnAddSession(Session::ID mSID, const FireNet::Connector& mAc, const std::string& hostaddr) override;
		virtual void AbortSession(const FireNet::SockAddr &, Session*, const FireNet::Connector& mConnector) override;
		virtual void Dispatch(Session::ID mSID, aio::Protocol * p) override;
		virtual void DispatchLuaProtocols(Session::ID mSID, aio::LuaProtocol * p) override;
	};

	class NetConnector : public FireNet::IConnector, public LOGINBASE::INetNotify
	{
		class NetKeepAliveTimer : public FireNet::Timer::Observer
		{
			NetConnector*		mImpl;
			time_t			mLastSendTime;
			time_t			mPeriod;
		public:
			NetKeepAliveTimer(NetConnector* impl)
				: mImpl(impl)
			{}
			virtual ~NetKeepAliveTimer() {}
		public:
			virtual void UpdateSelf()
			{
				time_t now = FireNet::Timer::GetTime();
				if (now - mLastSendTime >= mPeriod)
				{
					mImpl->OnKeepAliveTimer();
					mLastSendTime = now;
				}
			}
			void NetLoginModule_StartTime(int period)
			{
				FireNet::Timer::Detach(this);
				FireNet::Timer::Attach(this);
				mLastSendTime = FireNet::Timer::GetTime();
				mPeriod = period;
			}
			void NetLoginModule_CancelTime()
			{
				FireNet::Timer::Detach(this);
			}
		};
	public:
		FireNet::lid_t                      mLid;
		LOGINBASE::CLogin *                 mLoginBase;
		bool                                mAcready;
		gnet::AddictionControl              mAc;
		FireNet::ILoginConnection  *        mLoginUI;
		FireNet::CLoginParam                mParam;
		aio::Protocol::Manager::Session::ID mSID;
		std::string                         mHostAddress;

		NetKeepAliveTimer					mKeepalivetimer;
		bool								mSaready;
		gnet::ServerAttr					mServerAttr;
		int                                 mServerPing;

		int									mSecureLevel;

		FireNet::ProtocolType mCompressType;	// 压缩类型
		FireNet::ProtocolType mDeCompressType;	// 解压类型

		NetConnector(const FireNet::CLoginParam & _param, FireNet::ILoginConnection * login)
			: mParam(_param), mKeepalivetimer(this)
		{
			mAcready = false;
			mSaready = false;
			mServerPing = 0;
			mSecureLevel = 0;
			mSID = 0;
			this->mLoginUI = login;
			mLoginBase = new LOGINBASE::CLogin(*this);

			mCompressType = enumSECURITY_NULL;		// 初始化压缩类型
			mDeCompressType = enumSECURITY_NULL;	// 初始化解压类型
		}

		FireNet::ILoginConnection * GetLoginConnector() { return mLoginUI; }

		bool NetLoginModule_CheckVersion(int Version)
		{
			if (!mParam.mIsCheckVersion)
			{
				return true;
			}
			std::wstring wsVersionLoc1 = LJFP_Version::Version2VersionCaption(mParam.mVersion, 1);
			std::wstring wsVersionLoc2 = LJFP_Version::Version2VersionCaption(mParam.mVersion, 2);
			std::wstring wsVersionSrv1 = LJFP_Version::Version2VersionCaption(Version, 1);
			std::wstring wsVersionSrv2 = LJFP_Version::Version2VersionCaption(Version, 2);
			if (wsVersionLoc1 == wsVersionSrv1 && wsVersionLoc2 == wsVersionSrv2)
			{
				return true;
			}
			return false;
		}
		virtual ~NetConnector();

		/////////////////////////////////////
		// KeepAlive
		void OnKeepAliveTimer();
		virtual void NetLoginModule_SetupKeepAlive(int period) override
		{
			if (period <= 0)
			{
				mKeepalivetimer.NetLoginModule_CancelTime();
				return;
			}

			if (period < 1000)
				period = 1000;
			mKeepalivetimer.NetLoginModule_StartTime(period);
		}

		virtual aio::Protocol::Manager::Session::ID GetSessionID() override { return mSID; }
		///////////////////////////////////////////////////////////////////////////
		// FireNet::IConnector
		virtual FireNet::lid_t GetID() override;
		virtual void Start() override;
		virtual void Close() override;
		virtual void send(const aio::Protocol & protocol) override;
		virtual void send(const FireNet::Octets & luaprotocol) override;
		virtual bool NetLoginModule_GetAddiction(FireNet::Addiction &) override;
		virtual bool NetLoginModule_GetServerInfo(FireNet::ServerInfo &) override;

		virtual int  NetLoginModule_GetServerVersion() override      { return mParam.mVersion; }
		virtual int NetLoginModule_GetServerPing() override          { return this->mServerPing; }
		virtual int NetLoginModule_GetServerLevel() override		 { return this->mSecureLevel; }
		virtual std::string getHostAddress() override { return mHostAddress; }

		virtual bool NetLoginModule_CanEnterWorld() override;

		virtual void NetLoginModule_InstantAddCash(const std::string & cardnum, const std::string & cardpasswd) override;
		virtual void NetLoginModule_GetUserCoupon() override;
		virtual void NetLoginModule_SSOGetTicket(FireNet::CTickerInfo &) override;
		virtual void NetLoginModule_CouponExchange(int coupon_number, int cash_number, int reserved1, int reserved2) override;
		////////////////////////////////////////////////////////////////////////////
		// LOGINBASE::CNotify
		virtual void NetLoginModule_OnGetUserNameAndPassword(std::string& UserName, std::string& password) override;
		virtual void NetLoginModule_OnChallenge(const FireNet::Octets& responce) override;
		virtual void NetLoginModule_OnSetOutSecurity(const FireNet::Octets& key) override;
		virtual void NetLoginModule_OnSetInSecurity(const FireNet::Octets& key) override;
		virtual void NetLoginModule_OnSendKeyExchange(const FireNet::Octets& nonce) override;

		virtual void NetLoginModule_SetCompressType(FireNet::ProtocolType type) override { mCompressType = type; }		// 设置压缩类型
		virtual void NetLoginModule_SetDeCompressType(FireNet::ProtocolType type) override { mDeCompressType = type; }	// 设置解压类型
	};

	class AuthError : public aio::Protocol
	{
		FireNet::lid_t         loginid;
		int           mNetOperator;
		int           error;
		std::string   detail;
	public:
		AuthError(FireNet::lid_t loginid, int netOperator, int error, const std::string & _detail)
			: aio::Protocol(0), detail(_detail)
		{
			this->loginid = loginid;
			this->mNetOperator = netOperator;
			this->error = error;
		}
		virtual Protocol * Clone() const override { return new AuthError(*this); }
		virtual FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream &) const override { throw std::runtime_error("FakeProtocol"); }
		virtual const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream &) override { throw std::runtime_error("FakeProtocol"); }
		virtual void Process(Manager *, Manager::Session::ID) override;
	};

	class LoginScopedPtr
	{
		NetConnector    * ptr;
		//FireNet::Mutex   * pm;

		LoginScopedPtr(const LoginScopedPtr&);
		void operator=(const LoginScopedPtr&);
		friend class NetSessionManager;
		bool initAndLock(NetConnector * __p, FireNet::Mutex & __m)
		{
			ptr = __p;
			//pm = &__m;
			return true;
		}
	public:
		LoginScopedPtr()
		{
			ptr = NULL;
			//pm = NULL;
		}
		~LoginScopedPtr()
		{
			ptr = NULL;
			//if (pm)
			//	pm->UNLock();
		}
		NetConnector * operator->()
		{
			return ptr;
		}
	};

	NetConnector::~NetConnector()
	{
		if (FireNet::GetNetIO())
		{
			((NetSessionManager*)FireNet::GetNetIO())->Close(mSID, "close");
			((NetSessionManager*)FireNet::GetNetIO())->delLogin(this);
		}
		mKeepalivetimer.NetLoginModule_CancelTime();
		delete mLoginBase;
	}

	///////////////////////////////////////////////////////////////////////////
	// FireNet::IConnector
	void NetConnector::Close()
	{
		delete this;
	}

	FireNet::lid_t NetConnector::GetID()
	{
		return this->mLid;
	}

	void NetConnector::Start()
	{
		FireNet::Connector c;
		if (mParam.mConnectType == CONNECT_TYPE_NORMAL) {
			c.host = mParam.mHost;
			c.port = mParam.mPort;
		}
		else if (mParam.mConnectType == CONNECT_TYPE_TGW) {
			c.host = mParam.mGIP;
			c.port = mParam.mGPort;
		}
		c.mHostAddress = mParam.mAddress;
		c.udata = mLid;
		aio::Engine::getInstance().Connect(c);
	}

	void NetConnector::OnKeepAliveTimer()
	{
		//		gnet::KeepAlive keepalive;
		//		keepalive.code = (int)FireNet::Timer::GetTime();
		//		aio::Protocol::Manager * manager = FireNet::GetNetIO();
		//		manager->Send( this->sid, keepalive );

		gnet::KeepAlive keepalive;
		keepalive.code = clock() * 1000 / CLOCKS_PER_SEC;
		FireNet::GetNetIO()->Send(this->mSID, keepalive);

		mLoginUI->OnSendPing(keepalive.code);
	}

	void NetConnector::send(const aio::Protocol & protocol)
	{
#if (defined WIN32) && (defined _DEBUG)
		char buff[32] = {};
		std::sprintf(buff, "[Send Protocol] %d\n", protocol.getType());
		OutputDebugStringA(buff);
//#elif defined ANDROID //add temporary log for testin. by liugeng
		LOGE("[Send Protocol] %d\n", protocol.getType());
#endif
		FireNet::GetNetIO()->Send(this->mSID, protocol);
	}

	void NetConnector::send(const FireNet::Octets & luaprotocol)
	{
		FireNet::GetNetIO()->Send(this->mSID, luaprotocol);
	}

	bool NetConnector::NetLoginModule_GetAddiction(FireNet::Addiction & ad)
	{
		if (!mAcready)
			return false;

		ad.rate = mAc.rate;
		ad.msg = mAc.msg;
		for (std::vector<gnet::GPair>::iterator it = mAc.data.begin(), ie = mAc.data.end(); it != ie; ++it)
		{
			switch (it->key)
			{
			case 1: ad.TimePlay = it->value; break;
			case 2: ad.TimeHaly = it->value; break;
			case 3: ad.Time0 = it->value; break;
			}
		}
		return true;
	}

	bool NetConnector::NetLoginModule_GetServerInfo(FireNet::ServerInfo & si)
	{
		if (!mSaready)
			return false;
		si.flags = mServerAttr.flags;
		si.load = mServerAttr.load;
		si.extra = mServerAttr.extra;
		return true;
	}

	bool NetConnector::NetLoginModule_CanEnterWorld()
	{
		return true;
	}

	void NetConnector::NetLoginModule_InstantAddCash(const std::string & cardnum, const std::string & cardpasswd)
	{
		gnet::InstantAddCashReq req;
		req.userid = 0;
		req.localsid = 0;
		req.cardnum.replace(cardnum.c_str(), cardnum.length());
		req.cardpasswd.replace(cardpasswd.c_str(), cardpasswd.length());
		req.reserved = 0;
		this->send(req);
	}

	void NetConnector::NetLoginModule_GetUserCoupon()
	{
		gnet::GetUserCouponReq req;
		req.userid = 0;
		this->send(req);
	}

	void NetConnector::NetLoginModule_SSOGetTicket(FireNet::CTickerInfo & info)
	{
		gnet::SSOGetTicketReq req;
		FireNet::Octets account(info.mUserInfo.mAccount.c_str(), info.mUserInfo.mAccount.size());
		FireNet::Octets agentname(info.mUserInfo.mAgentName.c_str(), info.mUserInfo.mAgentName.size());
		FireNet::Octets agentaccount(info.mUserInfo.mAgentAccount.c_str(), info.mUserInfo.mAgentAccount.size());
		FireNet::Octets _info(info.mInformation.c_str(), info.mInformation.size());
		FireNet::Octets local_context(info.mContext.c_str(), info.mContext.size());
		req.user.isagent = info.mUserInfo.mIsagent;
		req.user.account = account;
		req.user.agentname = agentname;
		req.user.agentaccount = agentaccount;
		req.toaid = info.mToAID;
		req.tozoneid = info.mToZoneID;
		req.info = _info;
		req.local_context = local_context;
		this->send(req);
	}

	void NetConnector::NetLoginModule_CouponExchange(int coupon_number, int cash_number, int reserved1, int reserved2)
	{
		gnet::CouponExchangeReq req;
		req.coupon_number = coupon_number;
		req.cash_number = cash_number;
		req.userid = 0;
		req.reserved1 = reserved1;
		req.reserved2 = reserved2;
		this->send(req);
	}
	////////////////////////////////////////////////////////////////////////////
	// LOGINBASE::CNotify
	void NetConnector::NetLoginModule_OnGetUserNameAndPassword(std::string& UserName, std::string& password)
	{
		UserName = mParam.mUserName;
		password = mParam.mPassword;
	}

	void NetConnector::NetLoginModule_OnChallenge(const FireNet::Octets& responce)
	{

#if defined(_LOCOJOY_SDK_)
		gnet::Response		res;
		std::string strUsername = "";
		std::string strdeviceid = "";
		size_t pos = mParam.mUserName.find_first_of(",");
		if (pos != std::wstring::npos)
		{
			strUsername = mParam.mUserName.substr(0, pos);
		}
		if (pos != std::wstring::npos)
		{
			strdeviceid = mParam.mUserName.substr(pos + 1, mParam.mUserName.length());
		}

		res.identity.replace(strUsername.c_str(), strUsername.length());
		//res.response = responce;
		std::string strresponse = mParam.mPassword.c_str();
		res.response.replace(strresponse.c_str(), strresponse.length());
		//res.logintype = mParam.logintype;
		res.logintype = 1;
		res.gameid = GAMEID;
		std::string strGameKey = GAMEKEY;
		res.gamekey.replace(strGameKey.c_str(), strGameKey.length());

		std::string strselfchannel = mParam.mChannelId.c_str();

		res.selfchannel.replace(strselfchannel.c_str(), strselfchannel.length());
		std::string strchildchannel = CHILDCHANNEL;
		res.childchannel.replace(strchildchannel.c_str(), strchildchannel.length());
		res.deviceid.replace(strdeviceid.c_str(), strdeviceid.length());
		char aserverid[128];
		sprintf(aserverid, "%d", mParam.mServerId);
		std::string strserverid = aserverid;
		res.serverid.replace(strserverid.c_str(), strserverid.length());
		res.mid.replace(mParam.mID.c_str(), mParam.mID.length());
		res.reserved1 = mParam.mReserved1;
		res.reserved2.replace(mParam.mReserved2.c_str(), mParam.mReserved2.length());
		this->send(res);
#elif defined(_YJ_SDK_)
		gnet::Response		res;
		std::string strUsername = "";
		std::string strdeviceid = "";
		size_t pos = mParam.mUserName.find_first_of(",");
		if (pos != std::wstring::npos)
		{
			strUsername = mParam.mUserName.substr(0, pos);
		}
		if (pos != std::wstring::npos)
		{
			strdeviceid = mParam.mUserName.substr(pos + 1, mParam.mUserName.length());
		}

		res.identity.replace(strUsername.c_str(), strUsername.length());
		std::string strresponse = mParam.mPassword.c_str();
		res.response.replace(strresponse.c_str(), strresponse.length());
		res.logintype = 2;
		res.gameid = GAMEID;
		std::string strGameKey = GAMEKEY;
		res.gamekey.replace(strGameKey.c_str(), strGameKey.length());

		std::string strselfchannel = mParam.mChannelId.c_str();

		res.selfchannel.replace(strselfchannel.c_str(), strselfchannel.length());

		std::string strchildchannel = mParam.mStrChildChannel.c_str();;
		res.childchannel.replace(strchildchannel.c_str(), strchildchannel.length());
		res.deviceid.replace(strdeviceid.c_str(), strdeviceid.length());

		char aserverid[128];
		sprintf(aserverid, "%d", mParam.mServerId);
		std::string strserverid = aserverid;
		res.serverid.replace(strserverid.c_str(), strserverid.length());
		res.mid.replace(mParam.mID.c_str(), mParam.mID.length());
		res.reserved1 = mParam.mReserved1;
		res.reserved2.replace(mParam.mReserved2.c_str(), mParam.mReserved2.length());
		this->send(res);
#ifdef ANDROID
		LOGE("NetConnector::NetLoginModule_OnChallenge mParam.mUserName=%s,strselfchannel=%s,strdeviceid=%s", mParam.mUserName.c_str(), strselfchannel.c_str(), strdeviceid.c_str());
#endif
#else
		gnet::Response		res;
		std::string strdeviceid = "";
		std::string strUsername = mParam.mUserName.c_str();
		res.identity.replace(strUsername.c_str(), strUsername.length());
		//res.response = responce;
		std::string strresponse = mParam.mPassword.c_str();
		res.response.replace(strresponse.c_str(), strresponse.length());
		//res.logintype = mParam.logintype;
		res.logintype = 1;
		res.gameid = GAMEID;
		std::string strGameKey = GAMEKEY;
		res.gamekey.replace(strGameKey.c_str(), strGameKey.length());
		std::string strselfchannel = mParam.mChannelId.c_str();
		res.selfchannel.replace(strselfchannel.c_str(), strselfchannel.length());
		std::string strchildchannel = CHILDCHANNEL;
		res.childchannel.replace(strchildchannel.c_str(), strchildchannel.length());
		size_t pos = mParam.mUserName.find_first_of(",");
		if (pos != std::wstring::npos)
		{
			strdeviceid = mParam.mUserName.substr(pos + 1, mParam.mUserName.length());
		}
		res.deviceid.replace(strdeviceid.c_str(), strdeviceid.length());
		char aserverid[128];
		sprintf(aserverid, "%d", mParam.mServerId);
		std::string strserverid = aserverid;
		res.serverid.replace(strserverid.c_str(), strserverid.length());
		res.mid.replace(mParam.mID.c_str(), mParam.mID.length());
		res.reserved1 = mParam.mReserved1;
		res.reserved2.replace(mParam.mReserved2.c_str(), mParam.mReserved2.length());
		this->send(res);
#endif

	}

	void NetConnector::NetLoginModule_OnSetOutSecurity(const FireNet::Octets& key)
	{
		FireNet::GetNetIO()->SetOSecurity(this->mSID, mCompressType, key);
	}

	void NetConnector::NetLoginModule_OnSetInSecurity(const FireNet::Octets& key)
	{
		FireNet::GetNetIO()->SetISecurity(this->mSID, mDeCompressType, key);
	}

	void NetConnector::NetLoginModule_OnSendKeyExchange(const FireNet::Octets& nonce)
	{
		gnet::KeyExchange ke;
		ke.blkickuser = mParam.mIsKickUser ? 1 : 0;
		ke.nonce = nonce;
		this->send(ke);

		// password. security. memset and clear.
		mParam.mUserName.assign(mParam.mUserName.size(), '*');
		mParam.mUserName.clear();
		mParam.mPassword.assign(mParam.mPassword.size(), '*');
		mParam.mPassword.clear();
	}

	//////////////////////////////////////////////////////////////////
	void AuthError::Process(Manager * manager, Manager::Session::ID)
	{
		if (mNetOperator != NETOP_SERVERATTR && detail != "server attr")
		{
			//SDLOG_ERR(L"[AuthError::Process] action %d, error %d, detail %s", action, error, detail.c_str());
		}

		LoginScopedPtr login;
		if (((NetSessionManager *)manager)->findLogin(loginid, login))
			login->GetLoginConnector()->OnAutoFailed(mNetOperator, error, detail);
	}

	static class ProtocolQueue
	{
		struct Item
		{
			aio::Manager * manager;
			FireNet::NetSessionID mSID;
			aio::Protocol* p;

			inline Item(aio::Manager * manager, FireNet::NetSessionID mSID, aio::Protocol * p)
			{
				this->manager = manager;
				this->mSID = mSID;
				this->p = p;
			}

			inline void Process()
			{
				p->Process(manager, mSID);
			}
		};
		typedef std::list<Item> ItemList;

		ItemList		mList;
		FireNet::Mutex		mMutex;
	public:
		ProtocolQueue() {}
		~ProtocolQueue() {}
	public:
		inline void Push(aio::Manager * manager, FireNet::NetSessionID mSID, aio::Protocol * p)
		{
			mMutex.Lock();
			mList.push_back(Item(manager, mSID, p));
			mMutex.UNLock();
		}
		inline void Process()
		{
			ItemList temp;
			{
				mMutex.Lock();
				mList.swap(temp);
				mMutex.UNLock();
			}
			std::for_each(temp.begin(), temp.end(), std::mem_fun_ref(&Item::Process));
		}

	private:

		struct ProcessProtocolFunc
		{
			FireNet::IProcessProtocol& mIPP;
		public:
			ProcessProtocolFunc(FireNet::IProcessProtocol& ipp)
				: mIPP(ipp)
			{}
			void operator()(const Item& item)
			{
				mIPP.ProcessProtocol(item.manager, item.mSID, item.p);
			}
		};
	public:
		inline void Process(FireNet::IProcessProtocol& ipp)
		{
			ItemList temp;
			{
				mMutex.Lock();
				mList.swap(temp);
				mMutex.UNLock();
			}
			std::for_each(temp.begin(), temp.end(), ProcessProtocolFunc(ipp));
		}
	} g_protocolqueue;

	void PushProtocol(aio::Manager * manager, FireNet::NetSessionID mSID, aio::Protocol * p)
	{
		g_protocolqueue.Push(manager, mSID, p);
	}

	void ProcessProtocol()
	{
		g_protocolqueue.Process();
	}

	void ProcessProtocol(FireNet::IProcessProtocol& ipp)
	{
		g_protocolqueue.Process(ipp);
	}
	
	class CNetSystem :public INetSystem
	{
	public:
		virtual void Startup();
		virtual void Cleanup();
		virtual FireNet::IConnector * CreateConnector(const FireNet::CLoginParam & mParam, FireNet::ILoginConnection * callback);
		virtual FireNet::IConnector * GetConnector(lid_t mLid);
		virtual void PushProtocol(aio::Manager * manager, NetSessionID mSID, aio::Protocol * p);
		virtual void ProcessProtocol();
		virtual void ProcessProtocol(IProcessProtocol& ipp);
	};
	void CNetSystem::Startup()
	{
		g_pNetManager = new NetSessionManager();
		authc::RegisterProtocols();
		aio::Engine::getInstance().Startup();
	}

	void CNetSystem::Cleanup()
	{
		aio::Engine::getInstance().Cleanup();
		authc::UnregisterProtocols();
		delete g_pNetManager;
		g_pNetManager = NULL;
	}
	
	FireNet::IConnector* CNetSystem::CreateConnector(const FireNet::CLoginParam & mParam, FireNet::ILoginConnection * callback)
	{
		return ((NetSessionManager*)g_pNetManager)->CreateConnector(mParam, callback);
	}

	FireNet::IConnector* CNetSystem::GetConnector(lid_t mLid)
	{
		return ((NetSessionManager*)g_pNetManager)->GetConnector(mLid);
	}

	void CNetSystem::PushProtocol(aio::Manager * manager, FireNet::NetSessionID mSID, aio::Protocol * p)
	{
		g_protocolqueue.Push(manager, mSID, p);
	}

	void CNetSystem::ProcessProtocol()
	{
		g_protocolqueue.Process();
	}

	void CNetSystem::ProcessProtocol(FireNet::IProcessProtocol& ipp)
	{
		g_protocolqueue.Process(ipp);
	}

	INetSystem* GetNetSystem()
	{
		static CNetSystem s_NetSys;
		return &s_NetSys;
	}

	bool NetSessionManager::findLogin(Session::ID mSID, LoginScopedPtr & ptr)
	{
		mutex.Lock();
		for (Logins::iterator it = logins.begin(), ie = logins.end(); it != ie; ++it)
		{
			if (it->second->mSID == mSID)
			{
				mutex.UNLock();
				return ptr.initAndLock(it->second, mutex);
			}
		}
		mutex.UNLock();
		return false;
	}

	bool NetSessionManager::findLogin(FireNet::lid_t mLid, LoginScopedPtr & ptr)
	{
		mutex.Lock();
		Logins::iterator it = logins.find(mLid);
		if (it != logins.end())
		{
			mutex.UNLock();
			return ptr.initAndLock(it->second, mutex);
		}
		mutex.UNLock();
		return false;
	}

	NetSessionManager::NetSessionManager()
	{
		mLid = 0;
	}

	FireNet::IConnector * NetSessionManager::CreateConnector(const FireNet::CLoginParam& mParam, FireNet::ILoginConnection * callback)
	{
		mutex.Lock();
		NetConnector * login = new NetConnector(mParam, callback);
		while (!logins.insert(std::make_pair(++mLid, login)).second) {}
		login->mLid = mLid;
		mutex.UNLock();
		return login;
	}

	FireNet::IConnector * NetSessionManager::GetConnector(FireNet::lid_t mLid)
	{
		mutex.Lock();
		Logins::iterator it = logins.find(mLid);
		if (it != logins.end())
		{
			mutex.UNLock();
			return it->second;
		}
		mutex.UNLock();
		return NULL;
	}

	void NetSessionManager::delLogin(FireNet::IConnector * login)
	{
		mutex.Lock();
		logins.erase(login->GetID());
		mutex.UNLock();
	}

	void NetSessionManager::OnDelSession(Session* session)
	{
		const Session::ID mSID = session->getSID();
		LoginScopedPtr login;
		if (findLogin(mSID, login))
			login->GetLoginConnector()->DispatchProtocol(this, mSID, new AuthError(login->mLid, NETOP_FIRENET, 0, session->getCloseInfo()));
	}

	void NetSessionManager::OnAddSession(Session::ID mSID, const FireNet::Connector& mAc, const std::string& hostaddr)
	{
		LoginScopedPtr login;
		if (findLogin(mAc.udata, login))
		{
			login->mSID = mSID;
			login->mHostAddress = hostaddr;
			if (login.ptr->mParam.mConnectType == CONNECT_TYPE_TGW) 
			{
				FireNet::Octets tgwHeader;
				std::string head = "tgw_l7_forward\r\nHost:" + login.ptr->mParam.mHost + ":" + login.ptr->mParam.mPort + "\r\n\r\n";
				tgwHeader.replace(head.c_str(), head.length());
				FireNet::GetNetIO()->Send(mSID, tgwHeader);
			}
			return;
		}
		Close(mSID, "OnAddSession: IConnector has closed");
	}

	void NetSessionManager::AbortSession(const FireNet::SockAddr &, Session*, const FireNet::Connector& mConnector)
	{
		LoginScopedPtr login;
		if (findLogin(mConnector.udata, login))
			login->GetLoginConnector()->DispatchProtocol(this, 0, new AuthError(login->mLid, NETOP_CONNECT, mConnector.syserror, mConnector.error));
	}

	void NetSessionManager::Dispatch(Session::ID mSID, aio::Protocol * p)
	{
		{
			LoginScopedPtr login;
			if (findLogin(mSID, login))
			return login->GetLoginConnector()->DispatchProtocol(this, mSID, p);
		}
		delete p;
		Close(mSID, "Dispatch: IConnector has closed");
	}

	void NetSessionManager::DispatchLuaProtocols(Session::ID mSID, aio::LuaProtocol * p)
	{
		{
			LoginScopedPtr login;
			if (findLogin(mSID, login))
			return login->GetLoginConnector()->DispatchLuaProtocol(this, mSID, p);
		}
		delete p;
		Close(mSID, "Dispatch: IConnector has closed");
	}

}//namespace FireNet

namespace gnet
{
	void Challenge::Process(Manager * manager, Manager::Session::ID mSID)
	{
		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
		{
			if (login->mParam.mUserName.empty())
			{
				login->mServerAttr = this->mserverattr;
				login->mSaready = true;
				login->mParam.mVersion = this->version;
				manager->Close(mSID, "server attr");
				login->GetLoginConnector()->OnAutoFailed(NETOP_SERVERATTR, 0, "server attr");
				return;
			}

			if (!login->NetLoginModule_CheckVersion(this->version))
			{
				manager->Close(mSID, "version not match");
				login->GetLoginConnector()->OnAutoFailed(NETOP_VERSION, this->version, "version not match");
				return;
			}
			if (login->mParam.mLoginType == AUTH_TYPE_AUANY)
			{
				//login->mParam.reserved2 =
				FireNet::Octets response(login->mParam.mPassword.c_str(), login->mParam.mPassword.length());
				login->NetLoginModule_OnChallenge(response);
				return;
			}
			if (!login->mLoginBase->ProcessChallenge(this->nonce))
			{
				manager->Close(mSID, "ProcessChallenge");
				login->GetLoginConnector()->OnAutoFailed(NETOP_PROTOCOL, this->type, "ProcessChallenge");
				return;
			}
		}
	}

	void KeyExchange::Process(Manager * manager, Manager::Session::ID mSID)
	{
		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
		{
			if (!login->mLoginBase->ProcessKeyExchange(this->nonce, mSID))
			{
				manager->Close(mSID, "ProcessKeyExchange");
				login->GetLoginConnector()->OnAutoFailed(NETOP_PROTOCOL, this->type, "ProcessKeyExchange");
			}
		}
	}

	void OnlineAnnounce::Process(Manager * manager, Manager::Session::ID mSID)
	{
		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
		{
			login->mSecureLevel = this->algorithm;
			login->NetLoginModule_SetupKeepAlive(3000);//change from 5000 to 3000 by lg
			login->GetLoginConnector()->OnAutoSuccess(userid, zoneid, aid, algorithm);
		}
	}

	void KeepAlive::Process(Manager * manager, Manager::Session::ID mSID)
	{
		//		authc::LoginScopedPtr login;
		//		if (((authc::NetSessionManager*)manager)->findLogin(sid, login))
		//		{
		//			int now = FireNet::Timer::GetSystemTick();
		//			login->serverping = now - this->code;
		//		}

		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
		{
			login->mServerPing = clock() * 1000 / CLOCKS_PER_SEC - this->code;
			login->GetLoginConnector()->OnRecvPing(this->code, login->mServerPing);
		}
	}

	void ErrorInfo::Process(Manager * manager, Manager::Session::ID mSID)
	{
		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
		{
			std::string info((const char *)this->info.begin(), this->info.size());
			login->GetLoginConnector()->OnAutoFailed(NETOP_SERVER, this->errcode, info);
		}
	}

	void AddictionControl::Process(Manager * manager, Manager::Session::ID mSID)
	{
		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
		{
			login->mAc = *this;
			login->mAcready = true;
		}
	}

	void AnnounceForbidInfo::Process(Manager *manager, Manager::Session::ID mSID)
	{
		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
			login->GetLoginConnector()->OnAnnounceForbidInfo(this->forbid.ltype, this->forbid.time, this->forbid.createtime, this->forbid.reason.begin(), this->forbid.reason.size());
	}

	void InstantAddCashRep::Process(Manager * manager, Manager::Session::ID mSID)
	{
		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
		{
			login->GetLoginConnector()->OnInstantAddCashRep(retcode, userid, reserved);
		}
	}

	void GetUserCouponRep::Process(Manager * manager, Manager::Session::ID mSID)
	{
		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
		{
			login->GetLoginConnector()->OnGetUserCouponRep(retcode, userid, remain_coupon, today_exchange_coupon, today_remain_exchange_coupon, reserved1, reserved2);
		}
	}

	void CouponExchangeRep::Process(Manager * manager, Manager::Session::ID mSID)
	{
		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
		{
			login->GetLoginConnector()->OnCouponExchangeRep(retcode, userid, remain_coupon, today_exchange_coupon, today_remain_exchange_coupon, reserved1, reserved2);
		}
	}

	void SSOGetTicketRep::Process(Manager * manager, Manager::Session::ID mSID)
	{
		FireNet::LoginScopedPtr login;
		if (((FireNet::NetSessionManager*)manager)->findLogin(mSID, login))
		{
			FireNet::CUserInfo info;
			info.mIsagent = user.isagent;
			info.mUserId = user.userid;
			std::string _account((char *)user.account.begin(), user.account.size());
			info.mAccount = _account;
			std::string _agentname((char *)user.agentname.begin(), user.agentname.size());
			info.mAgentName = _agentname;
			std::string _agentaccount((char *)user.agentaccount.begin(), user.agentaccount.size());
			info.mAgentAccount = _agentaccount;
			std::string _ticket((char *)ticket.begin(), ticket.size());
			std::string _local_context((char *)local_context.begin(), local_context.size());
			std::string _reserved((char *)reserved.begin(), reserved.size());
			login->GetLoginConnector()->OnSSOGetTicketRep(retcode, info, _ticket, _local_context, _reserved);
		}
	}
}

