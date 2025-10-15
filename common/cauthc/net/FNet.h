#ifndef __NET_H__
#define __NET_H__
#include <string>
#include <map>
#include <stdint.h>
#include "../include/octets.h"
#include "../include/security.h"

struct addrinfo;

namespace aio
{
	class Protocol;
	class LuaProtocol;
	class SockAddr;
	class Security;
	class Manager;
}

namespace FireNet
{
	class Connector;
	class Octets;
	class Security;
	class SockAddr;
}

namespace authc
{
	class IConnector;
}

namespace FireNet
{
	typedef uint32_t ProtocolType;
	typedef uint32_t NetSessionID;
	typedef int64_t lid_t;

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

	// 速率
	#define NETRATE_ALL 0
	#define NETRATE_HALF 1
	#define NETRATE 2

	// 加密方法
	#define SECURITY_FLAGE_NONE 0x0
	#define SECURITY_FLAGE_CARD 0x10000
	#define SECURITY_FLAGE_HANDSET 0x20000
	#define SECURITY_FLAGE_USBKEY 0x30000
	#define SECURITY_FLAGE_HPONE 0x40100
	#define SECURITY_FLAGE_USBKEY2 0x50000
	#define SECURITY_FLAGE_PHONETOKEN 0x60000
	#define SECURITY_FLAGE_MODIFYPASSWD 0x200
	#define SECURITY_FLAGE_MODIFYMATRIX 0x400
	#define SECURITY_FLAGE_MASKHIGH 0xFFFF0000
	#define SECURITY_FLAGE_MASKOPT 0xFF00
	#define SECURITY_FLAGE_OPTNOCACHE 0x100

	// 服务器附带信息
	#define SERVERFLAGE_2XEXP 1
	#define SERVERFLAGE_MONEY 2
	#define SERVERFLAGE_SERVER 4
	#define SERVERFLAGE_2XSP 8

	//
	#define SIZE_CHALLENGE 16
	#define SIZE_MD5 16

	// 字节序
	uint16_t BYTE16Order(uint16_t x);
	uint32_t BYTE32Order(uint32_t x);
	uint64_t BYTE64Order(uint64_t x);

	// 用户信息
	class CUserInfo
	{
	public:
		unsigned char mIsagent; //代理
		std::string mAccount;//账号.
		std::string mPassword;//密码.
		std::string mAgentAccount; //代理账号
		std::string mAgentName; //代理名字.
		int64_t mUserId;//用户ID.
	};

	// 登录参数
	class CLoginParam
	{
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
		std::string mChannelId;
		std::string mStrChildChannel;
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
	};

	// PING信息
	class CTickerInfo
	{
	public:
		FireNet::CUserInfo mUserInfo;//用户信息
		int mLoginIP;//登陆IP
		int mToAID;
		int mToZoneID;//大区ID
		std::string mInformation;//信息
		std::string mContext;//上下文
		std::string mContextByServer;//服务器上下文
	};

	class Addiction
	{
	public:
		int rate;
		int msg;
		int TimePlay;
		time_t TimeHaly;
		time_t Time0;
	public:
		Addiction() { rate = 0; msg = 0; TimePlay = 0; TimeHaly = 0; Time0 = 0; }
	};

	class ServerInfo
	{
	public:
		int flags;
		char load;
		std::map<int, int> extra;
	public:
		ServerInfo() { flags = 0; load = 0; }
	};

	class PollIO;
	class SockAddr;
	class Connector;

	class NetSession
	{
		friend class NetIO;
		friend class StreamIO;

		enum { DEFAULTIOBUF = 8192 };
		PollIO  *assoc_io;
		bool	sending;
		bool    closing;
		bool    close_discard;
		Octets	ibuffer;
		Octets	obuffer;
		Octets  isecbuf;
		Octets  osecbuf;
		Security *isec;
		Security *osec;

		std::string closeinfo;
#if USE_PROTOCOLINFO_LIST
		std::list<std::string> protocolinfo_list;
#endif

		Octets& GetOBuffer();
		Octets& GetIBuffer();
		void SendFinish();

	protected:
		bool urgency_support;
		Mutex* locker;
		virtual ~NetSession();
		NetSession(const NetSession &rhs);

		bool Output(const Octets &data);
		Octets& Input();
		void SendReady();
		void SetISecurity(Security::Type type, const Octets &key);
		void SetOSecurity(Security::Type type, const Octets &key);
	public:
		NetSession();

		void Close(const char* info, bool locked = false);

		virtual bool NoMoreData() const = 0;
		virtual void OnRecv() = 0;
		virtual void OnSend() = 0;
		virtual void OnOpen(const SockAddr& local, const SockAddr& peer, const Connector& connector) = 0;
		virtual void OnOpen(const Connector& connector) = 0;
		virtual void OnClose() { }
		virtual void OnAbort(const SockAddr& sa, const Connector& connector) = 0;
		virtual	NetSession* Clone() const = 0;

		virtual void Destroy();
		const std::string getCloseInfo() const;

#if USE_PROTOCOLINFO_LIST
		void AddLastProtocolInfo(const char* info);
		const std::string GetLastProtocolInfo() const;
#endif
	private:
		void _Close();
	};

	class IConnector
	{
	public:
		virtual lid_t GetID() = 0;
		virtual void Start() = 0;
		virtual void Close() = 0;
		virtual void NetLoginModule_InstantAddCash(const std::string & cardnum, const std::string & cardpasswd) = 0;
		virtual void NetLoginModule_GetUserCoupon() = 0;
		virtual void NetLoginModule_CouponExchange(int coupon_number, int cash_number, int reserved1, int reserved2) = 0;
		virtual void send(const aio::Protocol & protocol) = 0;
		virtual void send(const FireNet::Octets & luaprotocol) = 0;
		virtual void NetLoginModule_SSOGetTicket(CTickerInfo &) = 0;
		virtual bool NetLoginModule_GetAddiction(Addiction &) = 0;
		virtual bool NetLoginModule_GetServerInfo(ServerInfo &) = 0;
		virtual int  NetLoginModule_GetServerVersion() = 0;
		virtual int NetLoginModule_GetServerLevel() = 0;
		virtual int NetLoginModule_GetServerPing() = 0;
		virtual void NetLoginModule_SetupKeepAlive(int period) = 0;
		virtual std::string getHostAddress() = 0;
		virtual bool NetLoginModule_CanEnterWorld() = 0;
		virtual NetSessionID GetSessionID() = 0;
		virtual void NetLoginModule_SetCompressType(FireNet::ProtocolType type) = 0;
		virtual void NetLoginModule_SetDeCompressType(FireNet::ProtocolType type) = 0;
	protected:
		virtual ~IConnector() { }
	};

	// 登录链接接口.
	class ILoginConnection
	{
	public:
		virtual void DispatchProtocol(aio::Manager * manager, NetSessionID mSID, aio::Protocol * p) = 0;
		virtual void DispatchLuaProtocol(aio::Manager * manager, NetSessionID mSID, aio::LuaProtocol * p) = 0;
		virtual void OnAutoSuccess(unsigned int userId, int zoneID, int aID, int algorithm) = 0;
		virtual void OnAutoFailed(int netOperator, int e, const std::string & detail) = 0;
		virtual void OnSendPing(int TickCount) = 0;
		virtual void OnRecvPing(int TickCount, int mServerPing) = 0;
		virtual void OnAnnounceForbidInfo(char type, int time, int createTime, const void* reason, const size_t reasonSize) = 0;
		virtual void OnInstantAddCashRep(int retcode, int userid, int reserved) = 0;
		virtual void OnGetUserCouponRep(int retcode, int userid, int remain_coupon, int ToDayExchangeCoupon, int ToDayRemainExchangeCoupon, int reserved1, int reserved2) = 0;
		virtual void OnCouponExchangeRep(int retcode, int userid, int remain_coupon, int ToDayExchangeCoupon, int ToDayRemainExchangeCoupon, int reserved1, int reserved2) = 0;
		virtual void OnSSOGetTicketRep(int retcode, FireNet::CUserInfo info, std::string Ticket, std::string local_context, std::string reserved) = 0;
	protected:
		virtual ~ILoginConnection(){}
	};

	class INetIO
	{
	public:
		virtual void Close(NetSessionID mSID, const char * info = 0) = 0;
		virtual void Send(NetSessionID mSID, const FireNet::Octets & protocol) = 0;
		virtual void Send(NetSessionID mSID, const aio::Protocol & protocol) = 0;
		virtual void Send(NetSessionID mSID, const aio::Protocol * protocol) = 0;
		virtual void SetISecurity(NetSessionID mSID, FireNet::ProtocolType type, const FireNet::Octets &key) = 0;
		virtual void SetOSecurity(NetSessionID mSID, FireNet::ProtocolType type, const FireNet::Octets &key) = 0;
		virtual void Dispatch(NetSessionID mSID, aio::Protocol * p) = 0;
		virtual void DispatchLuaProtocols(NetSessionID mSID, aio::LuaProtocol * p) = 0;
		virtual void CloseAll() = 0;
		virtual void ConnectTo(const FireNet::SockAddr& addr, const FireNet::Connector& mConnector) = 0;
#ifdef OS_IOS
		virtual void ConnectTo2(const FireNet::SockAddr& addr, const FireNet::Connector& mConnector, addrinfo* pAI) = 0;
#endif
        
	public:
		virtual ~INetIO(){}
	};

	INetIO *GetNetIO();

	class IProcessProtocol
	{
	public:
		virtual void ProcessProtocol(aio::Manager*, NetSessionID, aio::Protocol*) = 0;
	protected:
		virtual ~IProcessProtocol(){}
	};

	class INetSystem
	{
	public:
		virtual FireNet::IConnector * CreateConnector(const FireNet::CLoginParam & mParam, FireNet::ILoginConnection * callback)=0;
		virtual FireNet::IConnector * GetConnector(lid_t mLid)=0;
		virtual void PushProtocol(aio::Manager * manager, NetSessionID mSID, aio::Protocol * p)=0;
		virtual void ProcessProtocol()=0;
		virtual void ProcessProtocol(IProcessProtocol& ipp) = 0;
		virtual void Startup()=0;
		virtual void Cleanup()=0;
	protected:
		virtual ~INetSystem(){}
	};

	INetSystem* GetNetSystem();

}//namespace FireNet.

#endif//__NET_H__

