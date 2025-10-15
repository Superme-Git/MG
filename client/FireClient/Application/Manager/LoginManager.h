#ifndef __LOGINMANAGER_H
#define __LOGINMANAGER_H

#include "Singleton.hpp"
#include "GameCommon.h"
#include "NetConnection.h"
#include "nuieffect.h"

#include "network/HttpClient.h"
#include "network/HttpRequest.h"
#include "network/HttpResponse.h"
#include "fire/pb/RoleInfo.hpp"
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	#define LASTACCOUNT_SERVER_INI ".Last_Account.ini"
	#define LASTACCOUNT_SERVER_INI_ORI "Last_Account.ini"
#else
	#define LASTACCOUNT_SERVER_INI "Last_Account.ini"
#endif

#define MAX_ROLE_NUM 3
#define ANIMATION_NUM 15
#define EACH_PLAY_TIME 80
#define CHECK_SERVER_TIME 60

// SDK相关状态
enum eEnterMainStatus {
	eEnterMainStatus_None = 0,							// 游戏初始化
	eEnterMainStatus_LoginSuccess = 1,					// SDK登陆成功
	eEnterMainStatus_GamePlaying = 2,					// 游戏进行中
	eEnterMainStatus_SwitchByClickBtn = 3,				// 选服界面点击切换账号
	eEnterMainStatus_SwitchBySdk = 4,					// SDK界面切换账号回调
	eEnterMainStatus_ActiveCodeBySdkSwitch = 5,			// SDK界面切换账号后启动激活码验证
	eEnterMainStatus_NeedDisableBtnInUI = 6,			// SDK界面切换账号后需要屏蔽选服界面的按钮
	eEnterMainStatus_Waiting = 7,						// SDK界面切换账号处理完成等待最后处理
};

struct stGameRole
{
	DWORD			dwID;
	std::wstring	strName;
	eSexType		eSex;
	DWORD			dwLevel;
	eSchoolType		eSchool;
};

enum eLoginState
{
	eLoginState_Null,
	eLoginState_Enter,
	eLoginState_RequestLogin,
	eLoginState_RoleSelect,
	eLoginState_RoleCreate,
	eLoginState_ServersChoose,
};

class UISprite;
typedef std::vector<fire::pb::RoleInfo> RoleList;

enum eShortcutItem
{
	eShortcutItem_None,  // 不是快捷菜单启动
	eShortcutItem_ViewStall,  // 查看摆摊
	eShortcutItem_FriendChat,  // 好友聊天（好友列表）
	eShortcutItem_ActivityCalendar,  // 活动日历
};

class LoginManager : public CSingleton<LoginManager>
{
public:
	LoginManager();
	~LoginManager();
public:
    void SetSelectServerInfo(const std::wstring& area,const std::wstring& server,const std::wstring& ip, const std::wstring& port,int hosttype);
	const std::wstring& GetSelectArea();
	const std::wstring& GetSelectServer();
	const std::wstring& GetHost();
	const std::wstring& GetPort();
	const std::wstring& GetAccount();
	const std::wstring& GetPassword();
	const std::wstring& GetChannelId();
	const std::wstring& GetCurChannelId();
	int GetHostType();
	void SetAccountInfo(const std::wstring& account);
	void SetPassword(const std::wstring& password);
	void SetSession(const std::wstring& session);
	void SetDeviceid(const std::wstring& session);
	void SetChannelId(const std::wstring& channelId);
	void SetIp(const std::wstring& ip);
	std::wstring& GetIp();
	std::wstring& GetDeviceid();
    void LoginAgain();
    void SaveAccount();
	bool isAutoLogin();
    void Clear();
	void doGetRoleHeadInfo();
	int m_serverID;
	void setServerKey(int id);
	int getServerID();

	bool isFirstEnter();

	/*
		应用宝登陆需要的参数
	*/
	const std::wstring& GetOpenId();
	const std::wstring& GetOpenKey();
	const std::wstring& GetPayToken();
	const std::wstring& GetPf();
	const std::wstring& GetPfKey();
	const std::wstring& GetZoneId();
	const std::wstring& GetPlatformName();
	void SetOpenId(const std::wstring& openid);
	void SetOpenKey(const std::wstring& openkey);
	void SetPayToken(const std::wstring& paytoken);
	void SetPf(const std::wstring& pf);
	void SetPfKey(const std::wstring& pfkey);
	void SetZoneId(const std::wstring& zoneid);
	void SetPlatformName(const std::wstring& platformname);
	void SetCurChannelId(const std::wstring& channelId);

	/**
	* 用于检查渠道是否支持logout、switchAccount、enterPlatform、showToolBar、hideToolBar
	* @funcName 函数名称
	* @return 1-支持 0-不支持
	*/
	int isSDKFuncSupported(const char *funcName);
	//是否是应用宝
	void setIsYingYongBao(bool isyingyongbao);
	bool getIsYingYongBao();
	
	//易接SDK所需参数
	const std::wstring& GetYJAppId();
	const std::wstring& GetYJChannelId();
	const std::wstring& GetYJUserId();
	const std::wstring& GetYJToken();
	void SetYJAppId(const std::wstring& m_yjappid);
	void SetYJChannelId(const std::wstring& m_yjchannelid);
	void SetYJUserId(const std::wstring& m_yjuserid);
	void SetYJToken(const std::wstring& m_yjtoken);

	void CloseWinWebView();
	void WinWebViewUpdate();

public:
	struct stHistoryAccountInfo
	{
		std::wstring m_sName;
		std::wstring m_sPassword;
	};

	void Init();
	void Exit();

	void EnterLuaMain();
	void LoginIn();

    void setEnterMainStatus(unsigned char status);
	unsigned char getEnterMainStatus();

	bool NeedToReloadPicture();
	void SetNeedToReloadPicture(bool flag);

	void Run(int now,int delta);

	void ToServerChoose(const std::wstring& area, const std::wstring& server);

	RoleList& GetRoleList();
	RoleList::size_type GetRoleListNum();
	void UpdateRoleList();

	const fire::pb::RoleInfo* GetRoleInfoByID(int64_t roleid);

	void ShowCreateRoleDialog();
	void ShowSelectRoleDialog();

	int64_t GetPreLoginRoleID();
	void SetPreLoginRoleID(int64_t dwID);

	std::list<stHistoryAccountInfo> m_lHistoryAccount;
	std::string GetHistoryPassword(int num);
	std::string GetHistoryAccount(int num);

	void SetLoginState(eLoginState state);
	eLoginState GetLoginState();

	void ClearConnections();
	void CheckLoad(const std::string host, const std::string port, int serverKey, int ct_type = CONNECT_TYPE_NORMAL, const std::string& gip = "", const std::string& gport = "", int checkTime = CHECK_SERVER_TIME);
    void SetServerLoad(int key, FireNet::ServerInfo info);
	void ClearConnectionByKey(int key);

	// 是否从 ios9 的快捷菜单启动
	bool isShortcutLaunched() const { return mShortcutItemLaunchedBy != eShortcutItem_None; }
	eShortcutItem getShortcutItemLaunchedBy() const { return mShortcutItemLaunchedBy; }
	void setShortcutItemLaunchedBy(eShortcutItem item);
	bool isShortcutItemHandled() const { return mbShortcutItemHandled; }
	void setShortcutItemHandled(bool val) { mbShortcutItemHandled = val; }

private:
	struct stCheckServer
	{
		std::string     host;
		std::string     port;
		int             key;
		int             time;
		int				checkTime;
		Game::NetConnection *  netConnection;
		bool            hasReturn;
	};

    std::map<int, FireNet::ServerInfo> m_mServerInfoMap;
    std::map<int, stCheckServer> m_mCheckLoadTimeMap;

private:
	bool m_bNeedToReload;

private:
	size_t			m_RoleNum;
	RoleList		m_RoleList;
	int64_t			m_dwPreLoginID;
	eLoginState	m_eLoginState;

private:
	int						m_iAnimationIndex;
	int						m_iTimeCount;
	Nuclear::PictureHandle	m_BackAnimationHandle[ANIMATION_NUM];
	Nuclear::PictureHandle	m_FrontAnimationHandle[ANIMATION_NUM];

	Nuclear::PictureHandle	m_MiddlePictureHandle[2];
	Nuclear::PictureHandle	m_FrontPictureHandle[4];
	Nuclear::PictureHandle	m_CurtainPictureHandle;

	Nuclear::IEffect* m_pEagleEffect;
	Nuclear::IEffect* m_pParticleShuye;
	Nuclear::IEffect* m_pParticleLizi2;
	Nuclear::IEffect* m_pParticleLizi1;

	Nuclear::SoundHandle m_iLoginBackMusicHandle;

	float	m_iFrontPictureSpeed;
	float	m_iMiddlePictureSpeed;
	float	m_iBackPictureSpeed;

	float	m_iEaglePictureSpeed;
	float	m_iEagleXPosition;

	float	m_iFrontPictureOffset;
	float	m_iMiddlePictureOffset;
	float	m_iBackPictureOffset;

	std::wstring m_password;
	std::wstring m_account;
	std::wstring m_host;
	std::wstring m_port;
	std::wstring m_SelectArea;
	std::wstring m_SelectServer;

	std::wstring m_Key;

	bool m_isAutoLogin;

	int m_iHostType;

	Nuclear::NuclearPictureInfo m_Picinfo;

	// ios9 快捷菜单启动
	eShortcutItem mShortcutItemLaunchedBy;
	bool mbShortcutItemHandled;
};

inline LoginManager* gGetLoginManager()
{
	return LoginManager::GetInstance();
}

#endif
