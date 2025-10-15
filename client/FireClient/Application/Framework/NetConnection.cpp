#include "stdafx.h"
#include "NetConnection.h"
#include "GameApplication.h"
#include "MessageTask.h"
#include "LuaMessageTask.h"
#include "nuiengine.h"
#include "LoginManager.h"
#include "GameUIManager.h"
#include "MessageManager.h"
#include "IniManager.h"
#include "../fire/pb/CPingStat.hpp"
#include "../rpcgen/fire/pb/PingStatEntry.hpp"
#include <numeric>
#include "LoginManager.h"
#include "GameStateManager.h"
#include "DeviceData.h"
#include "GameTable/message/CMessageTip.h"
#if (defined WIN32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
//#include "fire/pb/move/SRoleEnterScene.hpp"
#endif
#include "net/FNet.h"

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

namespace Game
{

#if (1)
#define TEMP_WARN(__FORMAT__, ...) \
	do \
			{ \
		std::wstring wstrTemp; \
		StringCover::FormatString(wstrTemp, __FORMAT__, ##__VA_ARGS__); \
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("OnAuthError2", ws2s(wstrTemp).c_str()); \
			} while (0)
#else
#define TEMP_WARN(__FORMAT__, ...)
#endif

	bool NetConnection::m_bVersionIsInvalid = false;

//请求服务器负载时调用的构造函数
NetConnection::NetConnection(void* pData,bool bDianXin,const std::string & host, const std::string &port)
{
    m_pUserData = pData;
    m_bDianXin = bDianXin;
    m_iKey = 0;
    m_iTimeOutCount = 0;
    
    m_iUserID = 0;
    
    m_iBeginStamp = 0;
    m_bNewRoundStatFlag = false;
    m_bStatOpenFlag = false;
    
    m_strServerName=L"";
    m_strAreaName=L"";
    m_strHost=L"";
	m_login = NULL;

	FireNet::CLoginParam param;
    param.mHost = host;
    param.mPort = port;
    param.mUserName = "";
    param.mPassword = "";
    param.mIsKickUser = true;
	param.mChannelId = ws2s(gGetLoginManager()->GetChannelId());
	param.mStrChildChannel = "";

    m_login = FireNet::GetNetSystem()->CreateConnector(param, this);
    m_login->Start();
}
    
NetConnection::NetConnection(const std::string & host, const std::string &port, int key, int ct_type, const std::string& gip, const std::string& gport)
{
    m_pUserData = NULL;
    m_bDianXin = false;
    m_iKey = key;
    m_iTimeOutCount = 0;
    
    m_iUserID = 0;
    
    m_iBeginStamp = 0;
    m_bNewRoundStatFlag = false;
    m_bStatOpenFlag = false;
    
    m_strServerName=L"";
    m_strAreaName=L"";
    m_strHost=L"";
	m_login = NULL;

	FireNet::CLoginParam param;
	param.mHost = host;
	param.mPort = port;
	param.mUserName = "";
	param.mPassword = "";
	param.mIsKickUser = true;
	param.mConnectType = ct_type;
	param.mGIP = gip;
	param.mGPort = gport;
	param.mChannelId = ws2s(gGetLoginManager()->GetChannelId());
	param.mStrChildChannel = "";

	m_login = FireNet::GetNetSystem()->CreateConnector(param, this);
    m_login->Start();
}

NetConnection::NetConnection(std::string user, std::string passwd, std::string host, std::string port, bool isKick, int version,
	const std::wstring& serverName, const std::wstring& areaName, const int serverid, const char* channelId, int type,
	int ct_type, const std::string& gip, const std::string& gport, const std::string& extparam )
{
	m_pUserData = NULL;
	m_bDianXin = false;
	m_iKey = 0;
	m_iTimeOutCount = 0;

	m_iUserID = 0;
	m_sAccount = user;

	m_iBeginStamp = 0;
	m_bNewRoundStatFlag = false;
	m_bStatOpenFlag = false;

	m_strServerName = serverName;
	m_strAreaName = areaName;
	m_strHost = StringCover::to_wstring(host);
	m_lastAccout = user;
	m_login = NULL;

	FireNet::CLoginParam param;
	param.mHost = host;
	param.mPort = port;
	param.mUserName = user;
	param.mPassword = passwd;
	param.mIsKickUser = isKick;
	param.mLoginType = type;
	param.mConnectType = ct_type;
	param.mGIP = gip;
	param.mGPort = gport;
	param.mServerId = serverid;
	param.mChannelId = channelId;
	param.mStrChildChannel = extparam;

#ifdef ANDROID
	LOGE("NetConnection::NetConnection, param.mUserName=%s, param.mPassword=%s, param.mStrChildChannel=%s", param.mUserName.c_str(), param.mPassword.c_str(), param.mStrChildChannel.c_str());
#endif

	if (version != 0)
	{
		param.mIsCheckVersion = true;
		param.mVersion = version;
	}

	m_login = FireNet::GetNetSystem()->CreateConnector(param, this);
	m_login->Start();
}

NetConnection::NetConnection(const NetConnection&)
{

}

NetConnection::~NetConnection()
{
	if (m_login)
	{
		m_login->Close();
	}
}

void NetConnection::OnAutoSuccess(unsigned int userid,int zoneid,int aid,int algorithm )
{
	if (gGetGameApplication()->isReconnecting())
	{
		//connect during running game
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.reconnectdlg\".DestroyDialog()");
		gGetMessageManager()->CloseConfirmBox(eConfirmNormal, false);
	}
	else if (CEGUI::WindowManager::getSingleton().isWindowPresent("CharacterCreatedDlg"))
	{
		//connect during creating role
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.createroledialog\".OnConnected()");
		return;
	}

	m_iUserID = userid;

	gGetLoginManager()->LoginIn();
	gGetGameApplication()->SetAreaServerInfo(m_strServerName, m_strAreaName);
}

//分发协议
void NetConnection::DispatchProtocol(aio::Protocol::Manager * manager, aio::Protocol::Manager::Session::ID sid, aio::Protocol * p)
{
	m_iTimeOutCount = 0; // clear keep alive counter to avoid reconnecting
	Nuclear::GetEngine()->PutTask(new MessageTask(p, manager, sid));
}

//分发LUA协议
void NetConnection::DispatchLuaProtocol(aio::Manager * manager, FireNet::NetSessionID sid, aio::LuaProtocol * p)
{
	m_iTimeOutCount = 0; // clear keep alive counter to avoid reconnecting
	Nuclear::GetEngine()->PutTask(new LuaMessageTask(p));
}
    
void NetConnection::onServerError(int e, const std::string & detail){
    if (3 == e)
    {
        //gGetGameUIManager()->AddMessageTip(GameTable::message::GetCMessageTipTableInstance().getRecorder(140471).msg);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(GameTable::message::GetCMessageTipTableInstance().getRecorder(140471).msg).c_str());
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CTipsManager.AddMessageTip_", 1);
    }
    else if (20 == e) //144808	该账号已在另一台设备登陆，你被迫下线，请注意账号安全。
    {
        //gGetGameUIManager()->AddMessageTip(GameTable::message::GetCMessageTipTableInstance().getRecorder(144808).msg);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(GameTable::message::GetCMessageTipTableInstance().getRecorder(144808).msg).c_str());
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CTipsManager.AddMessageTip_", 1);
    }
}

void NetConnection::luasend(const FireNet::Octets & luaprotocol)
{
	m_login->send(luaprotocol);
}

void NetConnection::OnAutoFailed(int netOperator, int e, const std::string & detail)
{
	//如果检查到版本不匹配，不再做其他处理，只能退出游戏
	if (m_bVersionIsInvalid) return;

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LoginWaitingDialog.DestroyDialog");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("OnAuthError");

	TEMP_WARN(L"[OnAutoFailed] : %s (a=%d e=%d)", detail.c_str(), netOperator, e);

	std::basic_ostringstream<wchar_t> message;
	if (netOperator == NETOP_CONNECT)
	{
		if (m_sAccount.empty())
		{
			//SDLOG_WARN(L"[OnAutoFailed] eConnect: %s (Account is empty)", detail.c_str());
			TEMP_WARN(L"[OnAutoFailed] eConnect: %s (Account is empty)", detail.c_str());
		}
		else
		{
			if (gGetGameApplication()->isReconnecting())
			{
				//SDLOG_WARN(L"[OnAutoFailed] eConnect: %s (e=%d) (Reconnecting Account: %s)", detail.c_str(), e, m_sAccount.c_str());
				TEMP_WARN(L"[OnAutoFailed] eConnect: %s (e=%d) (Reconnecting Account: %s)", detail.c_str(), e, m_sAccount.c_str());
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.reconnectdlg\".onConnectFailed()");
			}
			else
			{
				//SDLOG_WARN(L"[OnAutoFailed] eConnect: %s (e=%d) (eExitToLogin)", detail.c_str(), e);
				TEMP_WARN(L"[OnAutoFailed] eConnect: %s (e=%d) (eExitToLogin)", detail.c_str(), e);

				if (!CEGUI::WindowManager::getSingleton().isWindowPresent("CharacterCreatedDlg")) //add by lg
				{
					GameApplication::GetInstance().ExitGame(eExitType_ToLogin);

					gGetMessageManager()->AddConfirmBox(eConfirmOK, MHSD_UTILS::GETSTRING(1135).c_str(), CEGUI::Event::Subscriber(&MessageManager::HandleDefaultCancelEvent),
						CEGUI::Event::Subscriber(&MessageManager::HandleDefaultCancelEvent));
				}
			}
		}
	}
	else if (netOperator == NETOP_SERVER)
    {
        //请求服务器状态失败
        if(m_sAccount.empty())
        {
			//SDLOG_WARN(L"[OnAutoFailed] eServer: %s (Account is empty)", detail.c_str());
			TEMP_WARN(L"[OnAutoFailed] eServer: %s (Account is empty)", detail.c_str());
        }
        else
        {
			//SDLOG_WARN(L"[OnAutoFailed] eServer: %s (e=%d) (eExitToLogin)", detail.c_str(), e);
			TEMP_WARN(L"[OnAutoFailed] eServer: %s (e=%d) (eExitToLogin)", detail.c_str(), e);
            GameApplication::GetInstance().ExitGame(eExitType_ToLogin);
            onServerError(e,detail);
        }
    }
	else if (netOperator == NETOP_FIRENET)
    {
        if(m_sAccount.empty())
        {
			TEMP_WARN(L"[OnAutoFailed] eNet: %s (Account is empty)", detail.c_str());
        }
        else if(m_lastAccout.empty() == false)
        {
			int nGameState = gGetStateManager() ? gGetStateManager()->getGameState() : -1;

			//avoid server close when client login. by lg
			if ((nGameState == eGameStateLogin || nGameState == eGameStateNull) && !CEGUI::WindowManager::getSingleton().isWindowPresent("CharacterCreatedDlg"))
			{
				//SDLOG_WARN(L"[OnAutoFailed] eNet: %s (e=%d) (eExitToLogin)", detail.c_str(), e);
				TEMP_WARN(L"[OnAutoFailed] eNet: %s (e=%d) (eExitToLogin)", detail.c_str(), e);
				GameApplication::GetInstance().ExitGame(eExitType_ToLogin);

				gGetMessageManager()->AddConfirmBox(eConfirmOK, MHSD_UTILS::GETSTRING(1136).c_str(), CEGUI::Event::Subscriber(&MessageManager::HandleDefaultCancelEvent),
					CEGUI::Event::Subscriber(&MessageManager::HandleDefaultCancelEvent));
			}
			else
			{
				//SDLOG_WARN(L"[OnAutoFailed] eNet: %s (e=%d) (setDisconnect=true GameState:%d)", detail.c_str(), e, nGameState);
				TEMP_WARN(L"[OnAutoFailed] eNet: %s (e=%d) (setDisconnect=true GameState:%d)", detail.c_str(), e, nGameState);
				assert(gGetGameApplication());
				gGetGameApplication()->setDisconnect(true);
				m_bStatOpenFlag = false;
			}
				
        }
    }
	else if (netOperator == NETOP_VERSION)
    {
		//SDLOG_WARN(L"[OnAutoFailed] eVersion: %s (e=%d) (eExitToLogin)", detail.c_str(), e);
		TEMP_WARN(L"[OnAutoFailed] eVersion: %s (e=%d) (eExitToLogin)", detail.c_str(), e);

		m_bVersionIsInvalid = true;
		aio::Engine::getInstance().Cleanup();

		//检测到游戏内容有更新，请退出后重新打开游戏
		gGetMessageManager()->AddConfirmBox(eConfirmOK, MHSD_UTILS::GETSTRING(11397).c_str(),
			CEGUI::Event::Subscriber(&MessageManager::HandleExitGameConfirmed, gGetMessageManager()),
			CEGUI::Event::Subscriber(&MessageManager::HandleDefaultCancelEvent));

    }
	else if (netOperator == NETOP_PROTOCOL)
    {
		//SDLOG_WARN(L"[OnAutoFailed] eProtocol: %s (e=%d) (eExitToLogin)", detail.c_str(), e);
		TEMP_WARN(L"[OnAutoFailed] eProtocol: %s (e=%d) (eExitToLogin)", detail.c_str(), e);

		GameApplication::GetInstance().ExitGame(eExitType_ToLogin);
    }
	else if (netOperator == NETOP_SERVERATTR)
    {
		//不是错误。只是为了接收ServerAttr
		FireNet::ServerInfo info;
		m_login->NetLoginModule_GetServerInfo(info);

		if (gGetGameApplication()->isReconnecting())
		{
			TEMP_WARN(L"[OnAutoFailed] eServerAttr: %s (e=%d) (Reconnecting load:%d)", detail.c_str(), e, info.load);
			if (info.load == 1 || info.load == 2 || info.load == 3) //1.2畅通 3.爆满
			{
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.reconnectdlg\".onServerLoaded()");
			}
			else
			{
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.reconnectdlg\".onServerClosed()");
			}
		}
		else
		{
			TEMP_WARN(L"[OnAutoFailed] eServerAttr: %s (SetServerLoad key:%d)", detail.c_str(), m_iKey);
			gGetLoginManager()->SetServerLoad(m_iKey, info);
		}

    }
	else if (netOperator == NETOP_TIMEOUT)
    {
        if(m_lastAccout.empty())
        {
			//SDLOG_WARN(L"[OnAutoFailed] eTimeout: %s (Account is empty)", detail.c_str());
			TEMP_WARN(L"[OnAutoFailed] eTimeout: %s (Account is empty)", detail.c_str());
		}
        else
        {
			//SDLOG_WARN(L"[OnAutoFailed] eTimeout: %s (e=%d) (eExitToLogin)", detail.c_str(), e);
			TEMP_WARN(L"[OnAutoFailed] eTimeout: %s (e=%d) (eExitToLogin)", detail.c_str(), e);

            GameApplication::GetInstance().ExitGame(eExitType_ToLogin);
        }
    }
	else
    {
		//SDLOG_WARN(L"[OnAutoFailed] default: %s (e=%d) (eExitToLogin)", detail.c_str(), e);
		TEMP_WARN(L"[OnAutoFailed] default: %s (e=%d) (eExitToLogin)", detail.c_str(), e);

        GameApplication::GetInstance().ExitGame(eExitType_ToLogin);
        message<<MHSD_UTILS::GETSTRING(1139).c_str();

		gGetMessageManager()->AddConfirmBox(eConfirmOK, MHSD_UTILS::GETSTRING(1140).c_str() + StringCover::to_wstring(detail), CEGUI::Event::Subscriber(&MessageManager::HandleDefaultCancelEvent), CEGUI::Event::Subscriber(&MessageManager::HandleDefaultCancelEvent));
    }
  
}

void NetConnection::send(const aio::Protocol & protocol)
{
	m_login->send(protocol);
}

int NetConnection::GetKey()
{
	return m_iKey;
}

void NetConnection::OnAnnounceForbidInfo(char type,int time,int createtime, const void* reason, const size_t reasonsize)
{
}

void NetConnection::close()
{
	if (m_login){ m_login->Close(); }
}

void NetConnection::onAuthPhoneToken()
{
}

bool NetConnection::GetStatOpenFlag()
{
	return m_bStatOpenFlag;
}

void NetConnection::OnSendPing(int tickcount)
{
	OnNetworkConnected();

    if (!m_bStatOpenFlag)
	{
		return;
	}
	stInetStatUnit unit(tickcount);
	m_vInetStat.push_back(unit);
    
	if (m_bNewRoundStatFlag)
	{
		m_iBeginStamp = gGetServerTime();
		m_bNewRoundStatFlag = false;
	}
    m_iTimeOutCount ++;
    int maxcount = gGetGameApplication()->getTimeoutCount();
    if (maxcount > 0 && m_iTimeOutCount > maxcount)
    {
        if (gGetGameApplication())
		{
			//SDLOG_WARN(L"[OnSendPing] : (setDisconnect=true) (curTimeOutCount=%d > maxTimeOutCount=%d)", m_iTimeOutCount, maxcount);
			if (m_login)
			{
				FireNet::GetNetIO()->Close(m_login->GetSessionID(), "[OnSendPing] : m_iTimeOutCount > maxcount");
			}
            m_bStatOpenFlag = false;
        }
    }
}

void NetConnection::GetInetStatValidUnit(std::vector<int>& valid_unit)
{
    for (size_t i=0; i<m_vInetStat.size(); i++)
	{
		if ( !m_vInetStat[i].loss )
		{
			valid_unit.push_back(m_vInetStat[i].ping);
		}
	}
}

bool NetConnection::IsCanEnterWorld()
{
	return m_login->NetLoginModule_CanEnterWorld();
}

void NetConnection::OnRecvPing(int tickcount,int serverping)
{
    if (!m_bStatOpenFlag)
	{
		return;
	}
    
	for (size_t i=0; i<m_vInetStat.size(); i++)
	{
		if (m_vInetStat[i].code == tickcount)
		{
			m_vInetStat[i].loss = false;
			m_vInetStat[i].ping = serverping;
		}
	}
    
	std::vector<int> valid_unit;
	GetInetStatValidUnit(valid_unit);
    
	if(valid_unit.size() >= MAX_RECORD_COUNT*SEND_INTERVALS)
	{
		int MaxInTenMin = *std::max_element(valid_unit.begin(), valid_unit.end());
		int MinInTenMin = *std::min_element(valid_unit.begin(), valid_unit.end());
		int AVGInTenMin = (int)(std::accumulate(valid_unit.begin(), valid_unit.end(),0)/(double)(MAX_RECORD_COUNT*SEND_INTERVALS));
        
        
		fire::pb::CPingStat stat;
		stat.beginstamp = m_iBeginStamp;
		stat.pingstats._max = (short) (MaxInTenMin > SHRT_MAX ? SHRT_MAX:MaxInTenMin);
		stat.pingstats._min = (short) (MinInTenMin > SHRT_MAX ? SHRT_MAX:MinInTenMin);
		stat.pingstats._avg = (short) (AVGInTenMin > SHRT_MAX ? SHRT_MAX:AVGInTenMin);
		stat.losspercent = (short)((m_vInetStat.size()-valid_unit.size())*100/(double)m_vInetStat.size());
		gGetNetConnection()->send(stat);
        
		m_vInetStat.clear();
		m_bNewRoundStatFlag = true;
	}
}

bool NetConnection::getAddiction(FireNet::Addiction & ac)
{
	if (m_login)
	{
		return m_login->NetLoginModule_GetAddiction(ac);
	}
	return false;
}

void NetConnection::OnNetworkConnected()
{
	if (m_iTimeOutCount >= 1) // 6s 没有响应
	{
		if (DeviceData::sIsNetworkConnected() == false || m_iTimeOutCount >= 4) // 网络层断开或15秒没有响应
		{
			//SDLOG_WARN(L"[OnNetworkConnected] : (IsNetworkConnected() == false) (curTimeOutCount=%d)", m_iTimeOutCount);
#ifndef ANDROID
			if (m_login)
			{
				FireNet::GetNetIO()->Close(m_login->GetSessionID(), "IsNetworkConnected() == false");
			}
#endif
		}
	}
}

void NetConnection::OnSSOGetTicketRep(int retcode, FireNet::CUserInfo info,
									   std::string ticket,std::string local_context,std::string reserved)
{ 
}

void NetConnection::SetStatOpenFlag(bool flag)
{
	m_bStatOpenFlag = flag;
}

void NetConnection::OnInstantAddCashRep(int retcode,int userid,int reserved)
{
}

void NetConnection::InstantAddCash(const std::string & cardnum, const std::string & cardpasswd)
{
	if (m_login)
	{
		m_login->NetLoginModule_InstantAddCash(cardnum, cardpasswd);
	}
}

void NetConnection::OnGetUserCouponRep(int retcode, int userid, int remain_coupon, int today_exchange_coupon, int today_remain_exchange_coupon, int reserved1, int reserved2)
{

}

void NetConnection::OnCouponExchangeRep(int retcode, int userid, int remain_coupon, int today_exchange_coupon, int today_remain_exchange_coupon, int reserved1, int reserved2)
{

}

void NetConnection::setSecurityType(unsigned int compressType, unsigned int deCompressType)
{
	if (m_login) {
		m_login->NetLoginModule_SetCompressType(FireNet::ProtocolType(compressType));
		m_login->NetLoginModule_SetDeCompressType(FireNet::ProtocolType(deCompressType));
	}
}

NetConnection::stInetStatUnit::stInetStatUnit(int code)
{
	this->code = code;
	ping = 0;
	loss = true;
}

}
