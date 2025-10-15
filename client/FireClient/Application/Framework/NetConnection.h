#ifndef __NETCONNECTION_H
#define __NETCONNECTION_H

#include  <string>
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "authc/os/windows/win32/share.hpp"
#else
#include "authc/os/ios/win32/share.hpp"
#endif

#include "include/protocol.h"

namespace Game
{
	class NetConnection : public FireNet::ILoginConnection
	{
	private:
		NetConnection(const NetConnection&);

	public:
    
		//请求服务器负载时调用的构造函数
		NetConnection(void* pData,bool bDianXin,const std::string & host, const std::string &port);
		NetConnection(const std::string & host, const std::string &port, int key, int ct_type = CONNECT_TYPE_NORMAL, const std::string& gip = "", const std::string& gport = "");
    
		NetConnection(std::string user, std::string passwd, std::string host, std::string port, bool isKick, int version,
			const std::wstring& serverName, const std::wstring& areaName, const int serverid = 0, const char* channelId = "", int type = AUTH_TYPE_AUANY,
			int ct_type = CONNECT_TYPE_NORMAL, const std::string& gip = "", const std::string& gport = "", const std::string& extparam = "");

		virtual ~NetConnection();

		//分发协议
		virtual void DispatchProtocol(aio::Manager * manager, FireNet::NetSessionID mSID, aio::Protocol * p);
		//分发LUA协议
		virtual void DispatchLuaProtocol(aio::Manager * manager, FireNet::NetSessionID mSID, aio::LuaProtocol * p);
		virtual void luasend(const FireNet::Octets & luaprotocol);
		virtual void send(const aio::Protocol & protocol);
		virtual void OnAnnounceForbidInfo(char type, int time, int createtime, const void* reason, const size_t reasonsize);
		virtual void OnAutoSuccess(unsigned int userid, int zoneid, int aid, int algorithm);
		virtual void OnAutoFailed(int netOperator, int e, const std::string & detail);

		//断开连接
		void close();
		virtual void OnGetUserCouponRep(int retcode, int userid, int remain_coupon, int today_exchange_coupon, int today_remain_exchange_coupon, int reserved1, int reserved2);
		virtual void OnCouponExchangeRep(int retcode, int userid, int remain_coupon, int today_exchange_coupon, int today_remain_exchange_coupon, int reserved1, int reserved2);

		//手机令牌，输入6位电子码
		virtual void onAuthPhoneToken();
		bool getAddiction(FireNet::Addiction & ac);

		bool IsCanEnterWorld();
		void OnNetworkConnected();

		virtual void OnRecvPing(int tickcount,int serverping);
		virtual void OnSendPing(int tickcount);
    
		void SetStatOpenFlag(bool flag);
		bool GetStatOpenFlag();
    
		int GetKey();
    
		void InstantAddCash(const std::string & cardnum, const std::string & cardpasswd);

		//充值单点登录
		void ReqForSSOTicket(int ssoid) { }
		virtual void OnSSOGetTicketRep(int, FireNet::CUserInfo, std::string, std::string, std::string);

		// 设置压缩和解压类型
		void setSecurityType(unsigned int compressType, unsigned int deCompressType);

	private:

		void onServerError(int e, const std::string & detail);

		void GetInetStatValidUnit(std::vector<int>& valid_unit);
    
		/************************************************************************/
		/* InstantAddCashRep的retcode含义如下：
		 0.成功 
		 1.卡号不存在 
		 2.用户不存在 
		 3.计费区不存在 
		 4.密码错误 
		 5.该卡已过期 
		 6.规定时间内同一张卡充值次数超限（防止试充值卡密码）
		 7.超时错误
		 8.网络通信错误
		 11.用户在该服务器已有金符石在等待划拨，请稍候再试（时间收费游戏忽略该错误码） 
		 12. 用户已有点卡充值未处理完，请稍候再试 
		 13. 用户被封禁，不能充值 
		 -1.其他错误（出现的情况有：卡号不符合规定，Usepointcard存储过程执行出错，au的InstantAddCash程序抛出异常）
		 */
		/************************************************************************/
		virtual void OnInstantAddCashRep(int retcode,int userid,int reserved) override;	//收到即时充值响应的接口

	protected:

		FireNet::IConnector * m_login;

	private:
		int64_t m_iUserID;				//账号id
		std::string m_sAccount;			//用户名

		std::string m_lastAccout;		//本次登录的账号
		void* m_pUserData;
		bool m_bDianXin;
		int  m_iKey;
		int  m_iTimeOutCount;

		//keeplive延迟统计
		struct stInetStatUnit
		{
			int code;
			int ping;
			bool loss;
        
			stInetStatUnit(int code);
		};
    
		std::wstring m_strAreaName;
		std::wstring m_strServerName;
		std::wstring m_strHost;

		static const unsigned int SEND_INTERVALS = 10;	  //十分钟发送一次
		static const unsigned int MAX_RECORD_COUNT = 60/5;//每分钟统计一次,十分钟发送一次统计结果
    
		bool m_bStatOpenFlag;						//统计打开标志
		bool m_bNewRoundStatFlag;					//新一轮开始标志
		int64_t m_iBeginStamp;						//统计开始时间戳
		std::vector<stInetStatUnit> m_vInetStat;	//统计信息单元

		static bool m_bVersionIsInvalid;			//游戏内检查版本匹配的结果
	};

}

#endif
