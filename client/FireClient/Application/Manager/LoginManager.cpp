#include "stdafx.h"
#include "LoginManager.h"
#include "../ProtoDef/fire/pb/CRoleList.hpp"
#include "../ProtoDef/fire/pb/CEnterWorld.hpp"
#include "GameStateManager.h"
#include "GameUIManager.h"
#include "GameTable/message/CMessageTip.h"
#include "GameApplication.h"
#include "UISprite.h"
#include <engine/nucocos2d_wraper.h>
#include "SimpleAudioEngine.h"
#include "IniManager.h"
#include "ConfigManager.h"
#include "CCDirector.h"
#include "MessageManager.h"
#include "ConnectGetServerInfo.h"
#include "SystemInfo.h"
#include "MainRoleDataManager.h"

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
#include "WinWebBrowser/WinSDK.h"
#endif

#include "../../../cocos2d-2.0-rc2-x-2.0.1/extensions/libSpine/spine-c/src/spine/Json.h"

#include "CallLuaUtil.h"


#ifdef _LOCOJOY_SDK_
#include "GameSdk.h"
#include "ChannelManager.h"
#endif

#ifdef _YJ_SDK_
#include "ChannelManager.h"
#endif

#ifdef ANDROID
#include "../../androidcommon/ChannelPlatformUtil.h"
#include "../../common/platform/android/SDJniHelper.h"
#include <jni.h>
#endif

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

#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "ICSharpCallback.h"
#endif

const int c_SinPeriod = 12000;
const float c_fFrontBackWidth = 5120.0f;

static std::wstring g_msdeviceid;
static std::wstring g_ip;

LoginManager::LoginManager()
: m_eLoginState(eLoginState_Null)
, m_RoleNum(0)
, m_dwPreLoginID(0)
, m_CurtainPictureHandle(Nuclear::INVALID_PICTURE_HANDLE)
, m_iBackPictureOffset(0.0f)
, m_iMiddlePictureOffset(0.0f)
, m_iFrontPictureOffset(0.0f)
, m_iBackPictureSpeed(30.0f)
, m_iMiddlePictureSpeed(0.008f)
, m_iFrontPictureSpeed(0.012f)
, m_iEaglePictureSpeed(0.04f)
, m_iEagleXPosition(350.0f)
, m_iAnimationIndex(0)
, m_iTimeCount(EACH_PLAY_TIME)
, m_pEagleEffect(NULL)
, m_pParticleLizi1(NULL)
, m_pParticleLizi2(NULL)
, m_pParticleShuye(NULL)
, m_iLoginBackMusicHandle(Nuclear::INVALID_SOUND_HANDLE)
, m_isAutoLogin(false)
,m_bNeedToReload(false)
, mShortcutItemLaunchedBy(eShortcutItem_None)
, mbShortcutItemHandled(false)
{
	// 不知道为什么需要重新设置一下音量才好用 add by yangjiafan
#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	std::string strDocuments=CFileUtil::GetDocDir();
	std::wstring iniW = SHARE_String2Wstring(strDocuments + "/" + LASTACCOUNT_SERVER_INI);
	std::wstring oriW = SHARE_String2Wstring(strDocuments + "/" + LASTACCOUNT_SERVER_INI_ORI);
	Platform::String^ ini = ref new Platform::String(iniW.c_str());
	Platform::String^ ori = ref new Platform::String(oriW.c_str());
	PhoneDirect3DXamlAppComponent::CheckInifileClass::callCSharp(ini, ori);
#endif

    //初始化默认帐户信息
    std::string strIniPath(LASTACCOUNT_SERVER_INI);
	IniManager IniMgr(strIniPath);
    
    std::string     strLastAccount;
    std::string     strLastPassword;
	std::string     strLastChannelId;
    std::string     host;
    std::string     port;
    std::string     servername;
    std::string     area;
    IniMgr.GetValueByName("Account", "LastAccount", strLastAccount);
    IniMgr.GetValueByName("Password", "LastPassword", strLastPassword);
	IniMgr.GetValueByName("ChannelId", "LastChannelId", strLastChannelId);
	 
    IniMgr.GetValueByName("Host", "host", host);
    IniMgr.GetValueByName("Port", "port", port);
    IniMgr.GetValueByName("Server", "server", servername);
    IniMgr.GetValueByName("ServerArea", "area", area);
    
	//将保存的账号填入账号列表
	for(int i = 0; i < 10; i++)
	{
        stHistoryAccountInfo accountInfo;
        
        std::wstring account;
		if(IniMgr.GetValueByName(L"AccountList", L"user" + StringCover::intTowstring(i), account))
        {
            accountInfo.m_sName = account;
            std::wstring password;
            if(IniMgr.GetValueByName(L"AccountList", L"password" + StringCover::intTowstring(i), password))
            {
                accountInfo.m_sPassword =password;
                m_lHistoryAccount.push_back(accountInfo);
            }
        }
        else
            break;
    }    
    
    if (servername != "" && area != "" && host != "" && port != "")    //没有默认服务器信息,选中列表中的第一项作为服务器
	{
		SetSelectServerInfo(StringCover::to_wstring(area), StringCover::to_wstring(servername), StringCover::to_wstring(host), StringCover::to_wstring(port), 0);
    }

    SetAccountInfo(StringCover::to_wstring(strLastAccount));
    SetPassword(StringCover::to_wstring(strLastPassword));
	SetChannelId(StringCover::to_wstring(strLastChannelId));
}

LoginManager::~LoginManager()
{
    Clear();

	destroyServerInfo();
}

void LoginManager::Clear()
{
    GameConfigManager::sSetPlayBackMusicBootState(true);

    CocosDenshion::SimpleAudioEngine::sharedEngine()->stopBackgroundMusic(true);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("loginBg.DestroyDialog");
	m_eLoginState = eLoginState_Null;
	m_RoleNum = 0;
    
    m_bNeedToReload = false;
    
    ClearConnections();
    m_mServerInfoMap.clear();
}

void LoginManager::Exit()
{
	m_eLoginState = eLoginState_Null;
}

void LoginManager::Init()
{
    m_bNeedToReload = true;
	m_RoleNum = 0;
	m_RoleList.clear();
	m_eLoginState = eLoginState_Enter;
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("loginBg.getInstanceAndShow");
}

bool LoginManager::isFirstEnter()
{
	stHistoryAccountInfo lastAccount;
	lastAccount.m_sName = GetAccount();
	lastAccount.m_sPassword = GetPassword();
	for (std::list<stHistoryAccountInfo>::iterator itor = m_lHistoryAccount.begin(); itor != m_lHistoryAccount.end(); itor++)
	{
		if ((*itor).m_sName == GetAccount())
		{
			return false;
		}
	}
	return true;
}


static std::wstring m_channelid;
void LoginManager::SetChannelId(const std::wstring& channelid)
{
	m_channelid = channelid;
}
const std::wstring& LoginManager::GetChannelId()
{
	return m_channelid;
}

void LoginManager::SetCurChannelId(const std::wstring& channelId)
{
	std::string strIniPath(LASTACCOUNT_SERVER_INI);
	IniManager IniMgr(strIniPath);

	std::wstring accountnum(L"");
	bool bExistLast = IniMgr.GetValueByName(L"AccountInfo", L"num", accountnum);
	bool exist_user_already = false;
	if (bExistLast)
	{
		int unum = StringCover::WStrToNum<int>(accountnum);
		unum = (unum < 10) ? unum : 10;
		for (int user_i = 1; user_i <= unum; user_i++)
		{
			std::wstring username(L"");
			IniMgr.GetValueByName(L"Account" + StringCover::intTowstring(user_i), L"username", username);
			if (username == gGetLoginManager()->GetAccount())
			{
				IniMgr.WriteValueByName(L"Account" + StringCover::intTowstring(user_i),
					L"channelid", channelId);
				exist_user_already = true;
			}
		}
	}
	if ( exist_user_already == false )
    {
		IniMgr.WriteValueByName("ChannelId", "LastChannelId", StringCover::to_string(channelId));
    }
    
}

const std::wstring& LoginManager::GetCurChannelId()
{
	std::string strIniPath(LASTACCOUNT_SERVER_INI);
	IniManager IniMgr(strIniPath);

	std::wstring accountnum(L"");
	bool bExistLast = IniMgr.GetValueByName(L"AccountInfo", L"num", accountnum);
	//
	if (bExistLast)
	{
		int unum = StringCover::WStrToNum<int>(accountnum);
		unum = (unum < 10) ? unum : 10;
		bool exist_user_already = false;
		for (int user_i = 1; user_i <= unum; user_i++)
		{
			std::wstring username(L"");
			IniMgr.GetValueByName(L"Account" + StringCover::intTowstring(user_i), L"username", username);
			if (username == gGetLoginManager()->GetAccount())
			{
				std::wstring channelId(L"");
				IniMgr.GetValueByName(L"Account" + StringCover::intTowstring(user_i), L"channelid", channelId);
				m_channelid = channelId;
				return m_channelid;
			}
		}
	}
	return m_channelid;
}



static unsigned char EnterMainStatus = eEnterMainStatus_None;
void LoginManager::setEnterMainStatus(unsigned char status)
{
	EnterMainStatus = status;
}
unsigned char LoginManager::getEnterMainStatus()
{
	return EnterMainStatus;
}


void LoginManager::SetNeedToReloadPicture(bool flag)
{
	m_bNeedToReload = flag;
}

void LoginManager::ToServerChoose(const std::wstring& area, const std::wstring& server)
{
	m_eLoginState = eLoginState_ServersChoose;
	gGetStateManager()->setGameState(eGameStateNull);

    m_bNeedToReload = false;

    gGetGameUIManager()->InitGameUIPostInit();
}

bool LoginManager::NeedToReloadPicture()
{
	return m_bNeedToReload;
}

RoleList::size_type LoginManager::GetRoleListNum()
{
	return m_RoleList.size();
}

void LoginManager::LoginIn()
{
	fire::pb::CRoleList LoginInCmd;
	gGetNetConnection()->send(LoginInCmd);

#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID || CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	MT3SystemInfo::sSendSystemInfo();
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	MT3SystemInfo::sSendSystemInfo();
#endif
}


void LoginManager::Run(int now, int delta)
{
    for (std::map<int, stCheckServer>::iterator itor = m_mCheckLoadTimeMap.begin(); itor != m_mCheckLoadTimeMap.end(); itor++)
    {
        itor->second.time -= delta;
        if (itor->second.time < 0 )
        {
            if (!itor->second.hasReturn)
            {
                FireNet::ServerInfo info;
                info.load = -1;
                SetServerLoad(itor->first, info);
            }
			CheckLoad(itor->second.host, itor->second.port, itor->second.key, CONNECT_TYPE_NORMAL, "", "", itor->second.checkTime);
            break;
        }
    }
    
	m_iTimeCount -= delta;
	if (m_iTimeCount < 0)
	{
		m_iTimeCount = EACH_PLAY_TIME;
		m_iAnimationIndex++;
		if (m_iAnimationIndex >= ANIMATION_NUM)
		{
			m_iAnimationIndex = 0;
		}
	}

	m_iBackPictureOffset += PixelAligned(m_iBackPictureSpeed*delta/1000.0f);

    float picWidth=(float)m_Picinfo.m_nPicWidth;

	if (m_iBackPictureOffset >picWidth)
	{
		m_iBackPictureOffset = 0.0f;
	}
    EnterLuaMain();
}

void LoginManager::UpdateRoleList()
{    
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LoginQuickDialog.DestroyDialog");

    //帐号只有一个角色
    if (m_RoleList.size() == 1)
    {
        gGetGameApplication()->BeginDrawServantIntro();
        if(gGetGameApplication()->GetXmlBeanReady())
        {
            gGetGameApplication()->DrawLoginBar(20);
            m_eLoginState = eLoginState_Null;
            int numMaxShowNum = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("SystemSettingNewDlg.GetMaxDisplayPlayerNum");
            fire::pb::CEnterWorld EnterWorldCmd(gGetLoginManager()->GetPreLoginRoleID(), numMaxShowNum);
            gGetNetConnection()->send(EnterWorldCmd);
        }
        else
        {
            gGetGameApplication()->DrawLoginBar(10);
            m_eLoginState = eLoginState_Null;
            gGetGameApplication()->SetWaitToEnterWorld(true);
            gGetGameApplication()->SetEnterWorldRoleID(gGetLoginManager()->GetPreLoginRoleID());
        }
        
        gGetGameApplication()->SetWaitForEnterWorldState(true);
    }
	else if (m_RoleList.size() > 1)
	{
		ShowSelectRoleDialog();
	}
	else
	{
		if (gGetGameApplication()->isReconnecting())
		{
			// 重连时,如果服务器删库,则退到登陆界面.
			GameApplication::GetInstance().ExitGame(eExitType_ToLogin);
		}
		else
		{
			ShowCreateRoleDialog();
		}
	}
}

const fire::pb::RoleInfo* LoginManager::GetRoleInfoByID(int64_t roleid)
{
	RoleList::iterator roleiter = m_RoleList.begin();
	for(; roleiter != m_RoleList.end(); roleiter++)
	{
		if((*roleiter).roleid == roleid)
		{
			return &(*roleiter);
		}
	}

	return NULL;
}

RoleList& LoginManager::GetRoleList()
{
	return m_RoleList;
}

int64_t LoginManager::GetPreLoginRoleID()
{
	return m_dwPreLoginID;
}

void LoginManager::SetPreLoginRoleID(int64_t dwID)
{
	m_dwPreLoginID = dwID;
}

void LoginManager::SetSelectServerInfo(const std::wstring& area, const std::wstring& server, const std::wstring& ip, const std::wstring& port, int hosttype)
{
	m_SelectArea = area;
	m_SelectServer = server;
	m_host = ip;
	m_port = port;
	m_iHostType = hosttype;
}

const std::wstring& LoginManager::GetSelectArea()
{
	return m_SelectArea;
}

const std::wstring& LoginManager::GetSelectServer()
{
	return m_SelectServer;
}

const std::wstring& LoginManager::GetHost()
{
	return m_host;
}

const std::wstring& LoginManager::GetPort()
{
	return m_port;
}

const std::wstring& LoginManager::GetAccount()
{
	return m_account;
}

const std::wstring& LoginManager::GetPassword()
{
	return m_password;
}

int LoginManager::GetHostType()
{
	return m_iHostType;
}

void LoginManager::SetAccountInfo(const std::wstring& account)
{
	m_account = account;
}

void LoginManager::SetPassword(const std::wstring& password)
{
	m_password = password;
}

void LoginManager::SetSession(const std::wstring& session)
{
	m_Key = session;
}

void LoginManager::SetDeviceid(const std::wstring& session)
{
	g_msdeviceid = session;
}

void LoginManager::SetIp(const std::wstring& ip)
{
	g_ip = ip;
}

std::wstring& LoginManager::GetIp()
{
	return g_ip;
}

std::wstring& LoginManager::GetDeviceid()
{
	if (g_msdeviceid.length() == 0)
	{
		int64_t nRoleId = GetMainRoleDataNumValue<int64_t>("roleid");
		g_msdeviceid = s2ws(StringCover::int64_tToString(nRoleId));
	}

	return g_msdeviceid;
}

void LoginManager::ShowSelectRoleDialog()
{
	m_eLoginState = eLoginState_RoleSelect;
}

void LoginManager::SetLoginState(eLoginState state)
{
	m_eLoginState = state;
}

eLoginState LoginManager::GetLoginState()
{
	return m_eLoginState;
}

void LoginManager::ShowCreateRoleDialog()
{
	m_eLoginState = eLoginState_RoleCreate;
    cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.createroledialog\":getInstance()");
}

//易接SDK所需参数
static std::wstring m_yjappid;
void LoginManager::SetYJAppId(const std::wstring& yjappid)
{
	m_yjappid = yjappid;
}
const std::wstring&  LoginManager::GetYJAppId()
{
	return m_yjappid;
}
static std::wstring m_yjchannelid;
void LoginManager::SetYJChannelId(const std::wstring& yjchannelid)
{
	m_yjchannelid = yjchannelid;
}
const std::wstring&  LoginManager::GetYJChannelId()
{
	return m_yjchannelid;
}
static std::wstring m_yjuserid;
void LoginManager::SetYJUserId(const std::wstring& yjuserid)
{
	m_yjuserid = yjuserid;
}
const std::wstring&  LoginManager::GetYJUserId()
{
	return m_yjuserid;
}
static std::wstring m_yjtoken;
void LoginManager::SetYJToken(const std::wstring& yjtoken)
{
	m_yjtoken = yjtoken;
}

const std::wstring&  LoginManager::GetYJToken()
{
	return m_yjtoken;
}

void LoginManager::SaveAccount()
{
	IniManager iniMgr = IniManager(LASTACCOUNT_SERVER_INI);
    
    iniMgr.WriteValueByName("Account", "LastAccount" , StringCover::to_string(GetAccount()));
    iniMgr.WriteValueByName("Password", "LastPassword" , StringCover::to_string(GetPassword()));
	iniMgr.WriteValueByName("ChannelId", "LastChannelId", StringCover::to_string(GetChannelId()));
	iniMgr.WriteValueByName("Host", "host", StringCover::to_string(GetHost()));
	iniMgr.WriteValueByName("Port", "port", StringCover::to_string(GetPort()));
    iniMgr.WriteValueByName("Server", "server" , StringCover::to_string(GetSelectServer()));
    iniMgr.WriteValueByName("ServerArea", "area" , StringCover::to_string(GetSelectArea()));
    
    stHistoryAccountInfo lastAccount;
    lastAccount.m_sName = GetAccount();
    lastAccount.m_sPassword = GetPassword();
    for (std::list<stHistoryAccountInfo>::iterator itor = m_lHistoryAccount.begin(); itor != m_lHistoryAccount.end(); itor++)
    {
        if ((*itor).m_sName == GetAccount())
        {
            m_lHistoryAccount.erase(itor);
            break;
        }
    }
    
    m_lHistoryAccount.push_front(lastAccount);
	
	//最多保存10个 by liugeng
	while (m_lHistoryAccount.size() > 10)
	{
		m_lHistoryAccount.pop_back();
	}
    
    std::list<stHistoryAccountInfo>::iterator itor = m_lHistoryAccount.begin();
    for (int i = 0;i < 10 && itor != m_lHistoryAccount.end(); i++, itor++)
    {
        iniMgr.WriteValueByName(L"AccountList",  L"user" + StringCover::intTowstring(i) , (*itor).m_sName);
        iniMgr.WriteValueByName(L"AccountList",  L"password" + StringCover::intTowstring(i) , (*itor).m_sPassword);
    }    
}

bool LoginManager::isAutoLogin()
{
	return m_isAutoLogin;
}

std::string LoginManager::GetHistoryAccount(int num)
{
    if ((size_t)num >= m_lHistoryAccount.size())
    {
        return "";
    }
    
    std::list<stHistoryAccountInfo>::iterator itor = m_lHistoryAccount.begin();
    for (int i = 0; i < num; i++,itor++);
    return StringCover::to_string((*itor).m_sName);
}

std::string LoginManager::GetHistoryPassword(int num)
{
	if ((size_t)num >= m_lHistoryAccount.size())
    {
        return "";
    }
    
    std::list<stHistoryAccountInfo>::iterator itor = m_lHistoryAccount.begin();
    for (int i = 0; i < num; i++,itor++);
    return StringCover::to_string((*itor).m_sPassword);
}

void LoginManager::CheckLoad(const std::string host, const std::string port, int serverKey, int ct_type, const std::string& gip, const std::string& gport, int checkTime)
{
    if (m_mCheckLoadTimeMap.end() != m_mCheckLoadTimeMap.find(serverKey))
    {
        delete m_mCheckLoadTimeMap.find(serverKey)->second.netConnection;
        m_mCheckLoadTimeMap.erase(m_mCheckLoadTimeMap.find(serverKey));
    }
    
	std::map<int, stCheckServer> ::iterator it = m_mCheckLoadTimeMap.find(serverKey);
	if (it != m_mCheckLoadTimeMap.end())
	{
		return;
	}

	doGetRoleHeadInfo();

    Game::NetConnection* pNetConnetcor = new Game::NetConnection(host, port, serverKey, ct_type, gip, gport);
    struct stCheckServer check;
    check.host = host;
    check.port = port;
    check.key = serverKey;
	check.time = checkTime * 1000;
	check.checkTime = checkTime;
    check.netConnection = pNetConnetcor;
    check.hasReturn = false;
    m_mCheckLoadTimeMap[serverKey] = check;
}

void LoginManager::ClearConnections()
{
    for (std::map<int, stCheckServer>::iterator itor = m_mCheckLoadTimeMap.begin(); m_mCheckLoadTimeMap.end() != itor; itor++)
    {
        delete itor->second.netConnection;
        itor->second.netConnection = NULL;
    }
    m_mCheckLoadTimeMap.clear();
}

void LoginManager::ClearConnectionByKey(int key)
{
	std::map<int, stCheckServer>::iterator it = m_mCheckLoadTimeMap.begin();
	while (it != m_mCheckLoadTimeMap.end())
	{
		if (it->second.key == key)
		{
			delete it->second.netConnection;
			it->second.netConnection = NULL;
			m_mCheckLoadTimeMap.erase(it);
			break;
		}
		it++;
	}
}

void LoginManager::setShortcutItemLaunchedBy(eShortcutItem item)
{
	mShortcutItemLaunchedBy = item;
	mbShortcutItemHandled = false;
}

void LoginManager::SetServerLoad(int key, FireNet::ServerInfo info)
{
    m_mCheckLoadTimeMap[key].hasReturn = true;
    m_mServerInfoMap[key] = info;
    cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("SelectServersDialog.SetServerLoad", key, info.load);
}

void LoginManager::doGetRoleHeadInfo()
{
	GetServerInfo()->connetGetUserInfo();
}

void LoginManager::setServerKey(int id)
{
	m_serverID = id;
}

int LoginManager::getServerID()
{
	return m_serverID;
}

/*
应用宝登陆需要的参数
*/
static std::wstring m_openid = L"";
static std::wstring m_openkey = L"";
static std::wstring m_paytoken = L"";
static std::wstring m_pf = L"";
static std::wstring m_pfkey = L"";
static std::wstring m_zoneid = L"";
static std::wstring m_platformname = L"";

const std::wstring& LoginManager::GetOpenId()
{
	return m_openid;
}

const std::wstring& LoginManager::GetOpenKey()
{
	return m_openkey;
}

const std::wstring& LoginManager::GetPayToken()
{
	return m_paytoken;
}

const std::wstring& LoginManager::GetPf()
{
	return m_pf;
}

const std::wstring& LoginManager::GetPfKey()
{
	return m_pfkey;
}

const std::wstring& LoginManager::GetZoneId()
{
	return m_zoneid;
}

const std::wstring& LoginManager::GetPlatformName()
{
	return m_platformname;
}

void LoginManager::SetOpenId(const std::wstring& openid)
{
	m_openid = openid;
}

void LoginManager::SetOpenKey(const std::wstring& openkey)
{
	m_openkey = openkey;
}

void LoginManager::SetPayToken(const std::wstring& paytoken)
{
	m_paytoken = paytoken;
}

void LoginManager::SetPf(const std::wstring& pf)
{
	m_pf = pf;
}

void LoginManager::SetPfKey(const std::wstring& pfkey)
{
	m_pfkey = pfkey;
}

void LoginManager::SetZoneId(const std::wstring& zoneid)
{
	m_zoneid = zoneid;
}

void LoginManager::SetPlatformName(const std::wstring& platformname)
{
	m_platformname = platformname;
}

static bool m_isYingyongbao = false;
void LoginManager::setIsYingYongBao(bool isyingyongbao)
{
	m_isYingyongbao = isyingyongbao;
}

bool LoginManager::getIsYingYongBao()
{
	return m_isYingyongbao;
}

int LoginManager::isSDKFuncSupported(const char *funcName)
{
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
	return isFunctionSupported(funcName);
#else
	return 1;
#endif
#endif

#ifdef _YJ_SDK_
	return 1;
#endif

	return 1;
}

void LoginManager::CloseWinWebView()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	WinSDK::getInstance()->closeWinWebView();
#endif
}

void LoginManager::WinWebViewUpdate()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	WinSDK::getInstance()->update();
#endif
}

void LoginManager::LoginAgain(){
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	if (gGetGameApplication()->IsUseSDKInWindows()) {
		EnterMainStatus = eEnterMainStatus_SwitchByClickBtn; // 选服界面点击切换账号
		WinSDK::getInstance()->openLoginUrl();
		return;
	}
#endif

#ifdef _LOCOJOY_SDK_
	EnterMainStatus = eEnterMainStatus_SwitchByClickBtn; // 选服界面点击切换账号
#ifdef ANDROID
	if (getIsYingYongBao())
	{
		if (isFunctionSupported("logout"))
		{
			logout();
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("SelectServerEntry_YingYongBaoShow");
		}
	}
	else
	{
		//if (isFunctionSupported("switchAccount"))
		//{
		//	switchAccount();
		//}
		//else
		//{
			if (isFunctionSupported("logout"))
			{
				LOGE("LoginManager::LoginAgain()");
				logout();
				bool is_RongHe = false;
				cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
				if (pScriptEngine)
				{
					CallLuaUtil util;
					if (util.callLua(pScriptEngine->getLuaState(), "Config_IsRongHe"))
					{
						is_RongHe =  util.getRetBool();
					}
				}
				if (!is_RongHe) 
				{
					login();
				}
			}
		//}
	}
#else
    SDK::GameSdk::logout();
    SDK::GameSdk::login(false);
#endif

	//关闭暗灯逻辑
	Nuclear::EngineLayer* layer = (Nuclear::EngineLayer*)Nuclear::GetEngine()->GetEngineLayer();
	if (layer)
	{
		layer->m_IsRunBrightNess = false;
		layer->m_bDark = false;
		layer->m_DuraTime = 0;
		layer->m_LastTick = Nuclear::GetMilliSeconds();
	}
#endif

#ifdef _YJ_SDK_
#ifdef ANDROID
	EnterMainStatus = eEnterMainStatus_SwitchByClickBtn; // 选服界面点击切换账号
	LOGE("LoginManager::LoginAgain begin");
	MT3::ChannelManager::yj_sdkLogout();
	LOGE("LoginManager::LoginAgain end");
#else

#endif

	//关闭暗灯逻辑
	Nuclear::EngineLayer* layer = (Nuclear::EngineLayer*)Nuclear::GetEngine()->GetEngineLayer();
	if (layer)
	{
		layer->m_IsRunBrightNess = false;
		layer->m_bDark = false;
		layer->m_DuraTime = 0;
		layer->m_LastTick = Nuclear::GetMilliSeconds();
	}
#endif

}

void LoginManager::EnterLuaMain(){
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
	if (MT3::ChannelManager::getIsYingYongBao()) {
		if (canDoYYBLoginCallback()) {
			onYYBLoginCb();
		}
	}
#endif
#endif

	if (EnterMainStatus == eEnterMainStatus_LoginSuccess){
		EnterMainStatus = eEnterMainStatus_GamePlaying;

#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
		bool is_LocoJoy = false;
		cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
		if (pScriptEngine)
		{
			CallLuaUtil util;
			if (util.callLua(pScriptEngine->getLuaState(), "Config_IsLocojoy"))
			{
				is_LocoJoy =  util.getRetBool();
			}
		}
		if (!is_LocoJoy) 
		{
			if (isFunctionSupported("showToolBar"))
			{
				showToolBar(TopLeft);
			}
		}
#else
		//showToolBar(TopRight);
#endif
#endif

		//开始启动暗灯逻辑
		Nuclear::EngineLayer* layer = (Nuclear::EngineLayer*)Nuclear::GetEngine()->GetEngineLayer();
		if (layer)
		{
			layer->m_IsRunBrightNess = true;
			layer->m_LastTick = Nuclear::GetMilliSeconds();
		}
        cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeScriptFile(L"main.lua");
    }
	else if (EnterMainStatus == eEnterMainStatus_SwitchBySdk) {
		EnterMainStatus = eEnterMainStatus_Waiting;
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("Logout_CalledBySdk"); // 触发调用游戏内的“切换账号”
	}
	else if (EnterMainStatus == eEnterMainStatus_ActiveCodeBySdkSwitch) {
		EnterMainStatus = eEnterMainStatus_NeedDisableBtnInUI;
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("Logout_CalledBySdk"); // 触发调用游戏内的“切换账号”
	}
	else if (EnterMainStatus == eEnterMainStatus_NeedDisableBtnInUI) {
		bool disableServerEntryClick = false;
		cocos2d::CCScriptEngineProtocol* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine();
		if (pScriptEngine) {
			CallLuaUtil util;
			util.addArg(false);
			if (util.callLua(pScriptEngine->getLuaState(), "SelectServerEntry_EnableClick")) {
				disableServerEntryClick = util.getRetBool();
			}
		}
		if (disableServerEntryClick) {
			EnterMainStatus = eEnterMainStatus_Waiting;
		}
	}
}
