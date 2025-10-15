#ifndef enginetester_GameApplication_h
#define enginetester_GameApplication_h

#include "nuiapp.h"
#include "NetConnection.h"

#include <nuiengine.h>
#include "common/nuexecutor.h"

#include "script_support/CCScriptSupport.h"
#include "CCLuaEngine.h"
#include "LuaTickerRegister.h"
#include "common/GameCommon.h"

#if (defined WIN32) || (defined _WIN32)
extern std::wstring gWorkPath;//当前工作路径
#endif


class LoadXmlBeansTask: public Nuclear::ExecThread::CallbackTask
{
public:
	LoadXmlBeansTask();
	virtual void Execute();
	virtual void Callback();
};


////游戏退出方式
enum eExitType
{
	eExitType_ToLogin = 0,		//退至游戏登录界面
	eExitType_ToSelectRole,		//退至角色选择色界面
	eExitType_ToWindows,			//关闭游戏
	eExitType_Max
};

enum eExitToLoginType
{	
    eExitToLoginType_Logout = 0,
	eExitToLoginType_LogoutIn,
	eExitToLoginType_ChangeLogin,
    eExitToLoginType_Special,
    eExitToLoginType_Max
};

enum eDisplayMode
{
	eDisplayMode_800600,
	eDisplayMode_1024768,
    eDisplayMode_1136640,
	eDisplayMode_1280800,
	eDisplayMode_12801024,
	eDisplayMode_1366768,
	eDisplayMode_1440900,
	eDisplayMode_16801050,
	eDisplayMode_19201080,
	eDisplayMode_Max
};

struct stDisplayModeInf  //各种分辨率的信息
{
	int idx;                //编号
	int width;              //宽
	int height;             //高
	std::wstring showText;  //在系统设置及保存在ini中的文字

	stDisplayModeInf():
	idx(0),
		width(0),
		height(0),
		showText(L"")
	{

	}
};

class UIQuestEffectNotify: public Nuclear::IEffectNotify
{
	void OnEnd(Nuclear::IEffect *pEffect);
	void OnDelete(Nuclear::IEffect *pEffect);
};

class CloseExecutorTimer : public Nuclear::INuclearTimer
{
public:
	CloseExecutorTimer()
	{
	}

	virtual void OnTimer();

	void CancelSchedule();

	bool SetSchedule(int cdtime);
};

class GameApplication : public LuaTickerRegister, public Nuclear::IApp
{
 public:
	enum eSoundType
	{
		eSoundType_SOUNDBACKGROUND		= 0,
		eSoundType_SOUNDEFFECT			= 1,
		eSoundType_BATTLESOUNDBG		= 2,
		eSoundType_BATTLESOUNDEFFECT	= 3,
		eSoundType_UISOUND             = 4,
		eSoundType_SPRITE3DEFFECT		= 5,
		eSoundType_MAGIC3DEFFECT		= 6,
	};

	enum eTitleInfoStep
	{
		eTitleInfoStep_GameName = 1,
		eTitleInfoStep_GameAndSeverName,
		eTitleInfoStep_GameAndSeverAndPlayerName,
	};

	GameApplication();
	~GameApplication();
	static GameApplication& GetInstance();

public:
	std::wstring GameName;

	int64_t m_iServerTime;//服务器时间

	Nuclear::IEngine* m_pEngine;
	Nuclear::IEnv*    m_pEnv;

	std::wstring m_AreaName;//服务器区的名称
	std::wstring m_ServerName;//服务器名称
	std::wstring m_ChargeBindHttpAdr;//充值绑定的地址
	std::wstring m_MainPageHttpAdr;//官方主页的地址

	CloseExecutorTimer*  m_pCloseExecutorTimer;

public:
	virtual std::string GetIniFileName();
	virtual void InitIni();
    virtual bool OnInit(int step);
    virtual bool OnExit();

	static std::wstring GetVersionOldCaption();
	static unsigned int GetVersionOld();
    
	static std::wstring GetVersionCaption();
	static unsigned int GetVersion();

	static std::wstring GetVersionBaseCaption();
	static unsigned int GetVersionBase();

	static std::wstring GetChannelCaption();
	static unsigned int GetChannel();

	const std::string getPlatformAddress();
	const std::string getUserHeadAddress();
	const std::string getBBSAdress();
	const std::string getNewswarnAddress();
	const std::string getAndroidDumpAddress();
	const std::string getIOSDumpAddress();
	const std::string getVoiceAddress();
	const std::string getDashiAddress();
	const std::string getSpaceAddress();
	const std::string getJubaoAddress();
	const std::string getChatAcessAddress();

	static std::wstring GetExtendValua(std::wstring wsKey);
	static bool GetLoadFromPak();
	static bool GetVersionDonotCheck();
    
	virtual void OnReloadAllTexture();
	void applicationDidEnterBackground();
	void applicationEnterForeground();
    
	Game::NetConnection* GetGameNetConnection();
    
    void DestroyGameNetConnection();
    
	void SetAreaServerInfo(const std::wstring& area, const std::wstring& server);
	std::wstring GetSeverNameInfo();
	const std::wstring& GetAreaName();
	const std::wstring& GetServerName();
	const std::wstring& GetChargeBindHttpAdr();
	const std::wstring& GetMainPageHttpAdr(){return m_MainPageHttpAdr;}
	const char* GetVoiceServerAddress();
	const std::string GetXunFeiServerAddress();
	const std::string GetXunFeiAppId();
	const char* GetPlatformChannelID();
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	const char* GetQuincyServerAddress();
	const char* GetCheckNetHostName();
	const char* GetHttpServerAddressPlatForm();
	void saveQuincyServerAddress(std::wstring address);
#endif
	bool AddTask(Nuclear::ExecThread::CallbackTask* pTask);
	void CloseExecutor();

	bool IsShowFPS();
	void setShowFps(bool isShow){ m_bShowFPS = isShow; }
	bool IsShowUIState();
	bool IsShowmPerformance();

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	bool IsUseSDKInWindows();
#endif

	int64_t GetEnterWorldRoleID();
	void SetEnterWorldRoleID(int64_t roleid);

	bool GetWaitToEnterWorld();
	void SetWaitToEnterWorld(bool flag);

	bool GetXmlBeanReady();
	void SetXmlBeanReady(bool flag);

	const char* getCacheLayoutConfig();
	void OnXmlBeanReady();
	void LoadBeanAsync();
	void AddAsyncLoadBeanTask();

	virtual void ExitGame(eExitType eType, int relogin = eExitToLoginType_ChangeLogin);
	virtual void StartGame();
	virtual void FinishLogin();
	virtual void CreateConnection(
		const char* account, 
		const char* key, 
		const std::wstring& host, 
		const std::wstring& sever, 
		bool bforcelogin, 
		const std::wstring &serverName, 
		const std::wstring &areaName, 
		const int serverid = 0, 
		const char* channelId = "",
		int type = AUTH_TYPE_AUANY, 
		const std::string& account_suffix = "",
		int ct_type = CONNECT_TYPE_NORMAL,
		const std::string& gip = "", 
		const std::string& gport = ""
		);
	virtual void StartLogin();
	virtual void CreateCrossConnection(const char* account, const char* key, const std::wstring& host, const std::wstring& sever);
	void DestroyConnection() { if(m_pNetConnection) delete m_pNetConnection; m_pNetConnection = NULL;}

	virtual void OnTick(unsigned int now, unsigned int delta, unsigned int realDelta);
	virtual void OnRenderUI(int now, bool realRender);
	virtual bool OnBeforeRender(int now);

	virtual void OnRenderInit(int now, int step, int totalstep);
	// windows 消息
	// return true 表示希望引擎继续处理此消息，反之引擎忽略此消息
	virtual Nuclear::NuclearWinMSGFuncResult OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam, void* pAdditionalParam = NULL);

	// 加载地图的回调
	virtual bool LoadingMapNotify(int pro);

	virtual void OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type);

	Nuclear::NuclearLocation gGetCursorPosition();

	//设置游戏时辰和当前服务器的时间
	int GetTime();
	void SetTime(const int64_t& servertime);

	bool IsZerosHours();//是否是零点
	void OnWeeHours();//零点时，客户端做的一些处理

	//从进入游戏到现在的时间
	DWORD GetOnLineTime();

	int GetLoginWindowWidth(); //获得登陆得的窗口大小，不是1024*768就是800*600

	eDisplayMode GetEnterWorldWindowMode();

	//是否全屏 
	Nuclear::NuclearScreenMode GetIsWindow();

	//垂直同步
	int GetRenderFlag();

	Nuclear::NuclearMultiSampleType GetAntialiaseLevel(int& level); //获得抗锯齿的级别
	bool isSpriteUseMipmap(); //精灵贴图是否用mipmap
	
	// ycl SDK 初始化，或者显示快速登录界面
	void doSDKOrShowQuickLogin();
	void playLoginBGM();  // ycl 播放登录背景音乐

	void InitDisplayMode();//进入游戏后，初始化分辨率

	eDisplayMode	gGetDisPlayMode();

	BOOL SetGameMainWindowTitle(const UINT& StepInfo, const std::wstring& AddInfo = L"");
	BOOL SetGameMainWindowTitle(const std::wstring& TitleName);
	BOOL SetGameMainWindowTitle(LPCWSTR TitleName);
	//void FlashGameMainWindow();//窗口闪烁

	//进入游戏load游戏图
	//void DrawPicture();
	void DrawLoginBar(float pro);
	void BeginDrawServantIntro();
	void EndDrawServantIntro();
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	void notifyStartChangeMap();
#endif
  
	void SetWaitForEnterWorldState(bool bWait);

	bool isShowProgressBar();

	bool IsInLittleGame();

	void setCollectFrequency(unsigned int frequency);

	void InitSpecialEffectList();

	//自适应同屏人数相关
	void OnFpsGatherTimer(int delta);
	int GetScreenMaxRoleByIniFile(); //从ini中获得玩家设置的同屏人数的类别
	int GetScreenNumByFps(int fps);  //根据fps获得同屏人数
	void BeginGatherFpsInf();
	void EndGatherFpsInf();
	void BeginAdjustScreenRoleNum();
	void EndAdjustScreenRoleNum();

	void SetScreenMaxRoleType(int type);
	int GetCurScreenMaxRoleType();
	void OnGameStartAdjustScreenRolNum(); //进入游戏时开始根据ini文件的配置开启自适应帧率
	void EnableAutoAdjustScreenNum(bool bEnable);

	void EnableAutoHideModel(bool bHide);
	bool isAutoHideModel();

	int GetCurShowMaxRoleNum();
	void SetMaxShowRoleNum(int num);

	void SetGACDTips(const std::wstring& tips);
	std::wstring GetGACDTips();
	bool isLuaDebugPrint();

	//void ResetRendOnceTask();

	//进入战斗录像观看观看模式
	void EnterBattleDemo();
	//退出游戏战斗录像观看模式
	void ExitBattleDemo();

	//进入战斗动画编辑模式
	void EnterBattleAnimEdit();
	//退出游戏动画编辑模式
	void ExitBattleAnimEdit();

	//进入资源检查模式
	void EnterResourceCheck();
	//退出资源检查模式
	void ExitResourceCheck();

	void AddToHWndList(const HWND& hWnd);

	//void SetUpFCMTick();
	//void CancelFCMTick();

	bool isFirstEnterGame();
	void SetFirstEnterGame(bool b);
	int isFirstTimeEnterGame(); //0新建账号第一次进入游戏， 1 进入新手剧情 ， 2 进入新手战斗， 3 新手战斗后剧情 ， 10 新手剧情新手战斗over
	void setFirstTimeEnterGameValue(int flag);

	std::wstring GetDisplayDesc(int w,int h);
	const std::vector<stDisplayModeInf>&  GetAllDisplayInf();
	void GetDisplayInf(eDisplayMode mode, stDisplayModeInf& inf);

	void InitFont();
	void EnableHotApplySysSetting();
	bool isHotApplySysSetting();

	//性能测试相关
	void CollectingGCMemory();

	void BeginShake(int shakeID);		//开始震屏
	void ShakeRun(int now,int delta);	//震屏run

	void ToggleDrawUI();
	void ToggleShowName();

	bool GetShieldAllAppFunc();
	void SetShieldAllAppFunc(bool flag) { m_bShieldAllAppFunc = flag; }

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	void setLoginInfo(std::wstring token, std::wstring uid);
#endif

	void setDisconnect(bool b);
	void setTimeoutCount(int count);
	int getTimeoutCount();
	void setReconnecting(bool b);
	bool isReconnecting();

	void setDetachingIME(bool b);

	std::wstring getCaptureDir();

	//是否是标准版本（true:标准版本,false:低清版本）
	bool IsNormalVer();
	int  GetFirstTimeEnterGame() { return m_nFirstTimeEG; }

	// 分享到社交平台
	void shareToPlatform(eShareSDK eShareSDK_, eShareType eShareType_, const char* title, const char* text, const char* imgPath, const char* webUrl);
	//显示MQview
	void showMQView();
	//IOS应用评论
	void CallEvaluate();
    //yangbin----ios录像功能
    void SetRecordEnable(bool enable);
    bool GetRecordEnable();
    
    void StartRecordForIOS();
    void StopRecordForIOS();
    
    void SetRecordState(int state);
    int GetRecordState();

	void SendLocalPushType(int type);

	//调用ChartBoostSdk的统计
	void gCallChartBoost();

	//启动外挂分析器
	void gStartWGAnalysis();

	// 游戏是否初始化成功
	bool isGameInitialized();

	void ShareResultCallBack(int nReslt);
    
private:
    //yangbin--录像相关变量－－begin
    bool m_RecordEnable;
    int m_CurRecordState;
	int m_LocalPushType;
    //yangbin--录像相关变量－－end
    
    void CleanupNetModule();
	void InitNetModule();

	// init lua 系统 ， 不需要cleanup
	bool InitLuaScriptModule();
	bool CleanupLuaScriptModule();

	// logger
	bool InitMT3Log();
	bool CleanupMT3Log();
	void CopyLog();

	std::vector<HWND> hwndlist;
	void SendRegisterHotKey();
	void SendShowMessage();
	void SendHideMessage();

	void preLoadBattleEffect();

	char HttpServerAddressPlatForm[256];
	char HttpServerAddressUserHead[256];
	char HttpServerAddressBBS[256];
	char HttpServerAddressNewWarn[256];
	char HttpServerAddressIOSDump[256];
	char HttpServerAddressAndroidDump[256];
	char HttpServerAddressVoice[256];
	char HttpServerAddressDaShi[256];
	char HttpServerAddressSpace[256];
	char HttpServerAddressJubao[256];
	char HttpServerAddressChatAcess[256];

private:
	//定时播放的特效 相关
	struct stSpecialEffect 
	{
		int effectid;
		int64_t begintime;
		int64_t endtime;
		int	cdtime;
		bool bintime;
		int	 playnum;
		stSpecialEffect()
			:cdtime(0),effectid(0),bintime(false),playnum(-1)
		{ }
	};

	BOOL bBossKeyRegister;

	std::list<stSpecialEffect> m_SpecialEffectList;

	bool m_bHotApplySysSetting; //是否是在热启动系统设置的设置

	bool m_bDrawName;
	bool m_bDrawUI;
	bool m_bDrawChatOutWnd;

	int m_iMouseOverCheckTimer;

	std::list<int>	m_ListHoldModelEffect;
	int	 m_iMainCharacterShapeID;		//主角自己的造型，不释放其攻击施法特效，除非退出游戏

	std::vector<stDisplayModeInf> m_vecDisplayModeInf;

	std::list<int>	m_ListHoldSchoolEffect;
	int	 m_iMainCharacterSchoolID;

	bool m_bAutoAdjustScreenNum;
	int m_iLastScreenRoleNum;
	int m_dwGatherElapse;
	bool m_bBeginGatherFpsInf;  //是否是在收集fps信息中

	int m_iGatherTime; //每一次统计几毫秒

	float m_TotalFps;
	int m_iFpsTick;

	int m_curScreenMaxRoleType; //当前同屏为数的类别
	bool m_bEnableAutoHideModel; //是否开启当人数过多时只画名字不画模型

	//引擎接口的震屏幕
	Nuclear::NuclearLocation m_ptBattleOldViewport;
	std::wstring m_gacd_tips;

    int m_iLoadLogoTime;
    bool m_bLogoLoad;
    
	int m_bDrawServantIntro;
	bool m_bFirstLogin;

	bool m_bInLittleGame;

	bool m_bWaitForEnterWorldMessage;
	
	bool m_bShieldAllAppFunc;
    
    int m_iNumMaxShowNum;

	int m_time; //时辰

	DWORD m_iEnterTime;//登录游戏时的时间(毫秒)

	eDisplayMode	m_eDisplayMode;

	static GameApplication s_instance;

	Nuclear::PictureHandle	m_WaitPictureHandle;		//登录背景

	Game::NetConnection* m_pNetConnection;

	static bool m_bXmlBeanReady; //xml beans 线程是否加载完

	static char m_VoiceServerAddress[256];
	static char m_platformChannel[64];
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	static char m_QuincyServerAddress[256];
	static char m_CheckNetHostName[256];
#endif

	bool m_bWaitToEnterWorld;
	int64_t m_iEnterWorldRoleID;
	bool m_bShowFPS;
	bool m_bShowUIState;
	bool m_bShowPerformance;
	std::string m_cacheLayout;

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	std::wstring m_sToken;
	std::wstring m_sUid;
	bool m_bNeedLogin;
#endif
    
private:
    bool m_bDisconnect;
    int  m_iTimeoutCount;
	bool m_bReconnecting;

	//垃圾回收
	unsigned int mCollectFrequency;
	unsigned int mDeltaCollectTime;

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	//Windows版本是否带SDK: 0-不带SDK 1-带SDK
	bool m_bUseSDKInWindows;
#endif

	bool mLuaPrint;

	bool m_bIsDetachingIME;         //keyboard is hiding. by lg
    bool m_willDispatchTouchEvent;  //if no, won't continue handle OnWindowsMessage until mouse button up. by lg

	bool m_bFirstEnterGame; //是否是第一次登陆游戏，即刚建的角色第一次进场景

	int  m_nFirstTimeEG;//进到游戏的第几步

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	bool m_bStartChangeMap;
#endif

	bool m_bGameInitialized;
};

bool gRunGameApplication();

#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
const char* GetQuincyServerAddress();
const char* GetCheckNetHostName();
const char* GetHttpServerAddressPlatForm();
#endif

Game::NetConnection* gGetNetConnection();
void gDestroyNetConnection();
extern int64_t gGetServerTime();
extern void gSetMaxFps(int maxFps);
extern void gGetTimeDetailsCPP(const int64_t seconds, int64_t &days, int &hours, int &mins, int &secs);
void gSetBackgroundMode(bool bBackgroundMode);
bool gIsInBackground();
void gRefuseAppBrightness(bool bBackgroundMode);
void gSetReloadAllTexturesState(bool flag);
bool gGetReloadAllTexturesState();

GameApplication* gGetGameApplication();
eDisplayMode gGetDisPlayMode();

void gGCNow(int level);
void gReceiveMemoryWarning();
void gSetGCCooldown(int time);

long gGetNumberValueByStrKey(const char *key); //get number value from lua. by lg

void gSetChannelName(std::string channelName);
std::string gGetChannelName();

extern int64_t GetTableNumberFromLua(const char* tableName, int tableId, const char* key);

extern float GetTableFloatFromLua(const char* tableName, int tableId, const char* key);

extern std::string GetTableStringFromLua(const char* tableName, int tableId, const char* key);

extern std::wstring GetTableWStringFromLua(const char* tableName, int tableId, const char* key);


//just for debug. by lg
extern void LogLuaTraceBack();

#endif
