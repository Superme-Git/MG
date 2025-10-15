#ifndef __FIRENET_AUTHC_HPP__
#define __FIRENET_AUTHC_HPP__

namespace authc
{
	// 网络相关的操作.
	#define NETOP_CONNECT 1
	#define NETOP_SERVER 2
	#define NETOP_FIRENET 3
	#define NETOP_TIMEOUT 4	
	#define NETOP_VERSION 5
	#define NETOP_PROTOCOL 6
	#define NETOP_SERVERATTR 7
	#define NETOP_SERVERFULL 39

	// 登陆类型
	#define AUTH_TYPE_NORMAL 0
	#define AUTH_TYPE_AUANY  1

	// 连接方式
	#define CONNECT_TYPE_NORMAL 0
	#define CONNECT_TYPE_TGW    1

	typedef aio::lid_t lid_t;

	// 启动网络模块
	void Startup();
	// 关闭网络模块
	void Cleanup();

	// 用户信息
	class CUserInfo
	{
	public:
		int64_t mUserId; //用户ID.
		std::string mAccount; //账号.
		std::string mPassword;//密码.
		std::string mAgentAccount; //代理账号
		std::string mAgentName; //代理名字.
		unsigned char mIsagent; //
	};

	// 登录链接接口.
	class ILoginConnection
	{
	public:
		ILoginConnection(){}
		virtual ~ILoginConnection() { }
	public:
		//分发协议
		virtual void DispatchProtocol(aio::Manager * manager, aio::SessionIDType mSID, aio::Protocol * p) = 0;
		//分发LUA协议
		virtual void DispatchLuaProtocol(aio::Manager * manager, aio::SessionIDType mSID, aio::LuaProtocol * p) = 0;
		//登陆成功
		virtual void OnAutoSuccess(unsigned int userId, int zoneID, int aID, int algorithm) = 0;
		//登陆失败
		virtual void OnAutoFailed(int netOperator, int e, const std::string & detail) = 0;
		//发送PING
		virtual void OnSendPing(int TickCount) = 0;
		//接手PING
		virtual void OnRecvPing(int TickCount, int mServerPing) = 0;
		//
		virtual void OnAnnounceForbidInfo(char type, int time, int createTime, const void* reason, const size_t reasonSize) = 0;
		virtual void OnInstantAddCashRep(int retcode, int userid, int reserved) = 0;
		virtual void OnGetUserCouponRep(int retcode, int userid, int remain_coupon, int ToDayExchangeCoupon, int ToDayRemainExchangeCoupon, int reserved1, int reserved2) = 0;
		virtual void OnCouponExchangeRep(int retcode, int userid, int remain_coupon, int ToDayExchangeCoupon, int ToDayRemainExchangeCoupon, int reserved1, int reserved2) = 0;
		virtual void OnSSOGetTicketRep(int retcode, CUserInfo info, std::string Ticket, std::string local_context, std::string reserved) = 0;
	};

	// PING信息
	class CTickerInfo
	{
	public:
		CUserInfo mUserInfo;//用户信息
		int mLoginIP;//登陆IP
		int mToAID;
		int mToZoneID;//大区ID
		std::string mInformation;//信息
		std::string mContext;//上下文
		std::string mContextByServer;//服务器上下文
	};

	// 登录参数
	class CLoginParam
	{
	public:
		CLoginParam() :
			mVersion(0),
			mIsCheckVersion(false),
			mIsKickUser(false),
			mLoginType(AUTH_TYPE_NORMAL),
			mID("locojoy"),
			mReserved1(0),
			mReserved2(""),
			mConnectType(CONNECT_TYPE_NORMAL)
		{
		}
		virtual ~CLoginParam(){}
	public:
		std::string mID;
		std::string mDeviceId;
		int         mServerId;
		std::string mHost;
		std::string mAddress;
		std::string mPort;
		std::string mGIP;
		std::string mGPort;
		int         mVersion;
		bool        mIsCheckVersion;
	public:
		int         mConnectType;
		int         mLoginType;
		std::string mUserName;
		int         mUserType;
		std::string mPassword;
	public:
		int         mReserved1;
		std::string mReserved2;
	public:
		bool        mIsKickUser;
	};

	class Login
	{
	public:
		virtual lid_t GetID() = 0;
		virtual void Start() = 0;
		virtual void Close() = 0;
		virtual void sendHandSetResponse( int num ) = 0;
		virtual void NetLoginModule_SendPhoneToKeyResponse( int num ) = 0;

		virtual void NetLoginModule_InstantAddCash(const std::string & cardnum,const std::string & cardpasswd) = 0;

		virtual void NetLoginModule_GetUserCoupon( ) = 0;
		virtual void NetLoginModule_CouponExchange(int coupon_number,int cash_number,int reserved1,int reserved2) = 0;
		virtual void send(const aio::Protocol & protocol) = 0;
        virtual void send(const FireNet::Octets & luaprotocol) = 0 ;
		virtual void NetLoginModule_SSOGetTicket(CTickerInfo &) = 0;

		struct Addiction
		{
			enum {
				enumRateAll = 0,
				enumRateHalf = 1,
				enumRate = 2
			};
			int rate;  
			int msg;  
			int TimePlay;
			time_t TimeHaly;
			time_t Time0;

			Addiction() { rate = 0; msg = 0; TimePlay = 0; TimeHaly = 0; Time0 = 0; }
		};
		virtual bool NetLoginModule_GetAddiction(Addiction & ) = 0;

		struct ServerInfo 
		{
			enum
			{
				enum2XExp = 1,
				enum2XMoney = 2,
				enumServer = 4,
				enum2XSp = 8,
			};
			int flags;
			char load;
			std::map<int,int> extra;

			ServerInfo() { flags = 0; load = 0;}
		};

		virtual bool NetLoginModule_GetServerInfo(ServerInfo &) = 0; 
		virtual int  NetLoginModule_GetServerVersion() = 0;

		enum
		{
			enumSFNONE        =  0x0,
			enumSFCARD        =  0x00010000,
			enumSFHANDSET     =  0x00020000,
			enumSFUSBKEY      =  0x00030000,
			enumSFHPONE		  =  0x00040100,
			enumSFUSBKEY2	  =  0x00050000,
			enumSFPHONETOKEN  =  0x00060000,

			enumSFNEEDMODIFYPASSWD = 0x00000200,
			enumSFNEEDMODIFYMATRIX = 0x00000400,

		};
		virtual int NetLoginModule_GetServerLevel() = 0;

		virtual int NetLoginModule_GetServerPing() = 0;

		virtual void NetLoginModule_SetupKeepAlive(int period) = 0;

		virtual std::string getHostAddress() = 0;
		virtual bool NetLoginModule_CanEnterWorld() = 0;
		virtual aio::SessionIDType GetSessionID() = 0;

	protected:
		virtual ~Login() { }
	};

	Login * newLogin(const CLoginParam & mParam, ILoginConnection * callback);
	aio::Manager * getLoginManager();
	Login * getLogin(lid_t mLid);

	void PushProtocol( aio::Manager * manager, aio::SessionIDType mSID, aio::Protocol * p);
	void ProcessProtocol();

	class IProcessProtocol
	{
	public:
		IProcessProtocol() {}
		virtual ~IProcessProtocol() {}
	public:
		virtual void ProcessProtocol( aio::Manager*, aio::SessionIDType, aio::Protocol*) = 0;
	};

	void ProcessProtocol( IProcessProtocol& ipp);

} // namespace authc

#endif  // #ifndef __FIRENET_AUTHC_HPP__

