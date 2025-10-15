#include "stdafx.h"
#include <iostream>

#include "GameApplication.h"
#include "GameUIManager.h"
#include "TaskOnOffEffectManager.h"
#include "nuiengine.h"
#include "engine/nuengine.h"
#include "sprite/nuspritemanager.h"
#include <cocos2d.h>
#include "SimpleAudioEngine.h"
#include <utils/StringUtil.h>
#include "../../../common/platform/utils/FileUtil.h"
#include "ProtocolLuaFunManager.h"
#include "EmotionManager.h"
#include "../ProtoDef/rpcgen.hpp"
#include "LoginManager.h"
#include "utils/Utils.h"
#include "LuaFireClient.h"
#include "utils/StringCover.h"
#include "GameScene.h"
#include "GameStateManager.h"
#include "LoginManager.h"
#include "RoleItemManager.h"
#include "MainCharacter.h"
#include "GameOperateState.h"
#include "MainRoleDataManager.h"
#include "BattleManager.h"
#include "BattleMagicControl.h"
#include "ResolutionAdapter.h"
#include "ini/IniFile.h"
#include "TableDataManager.h"
#include "fire/pb/CEnterWorld.hpp"
#include "platform/CCThread.h"
#include "effect/nueffectmanager.h"
#include "ArtTextManager.h"

#include "WavRecorder.h"
#include "VoiceManager.h"
#include "IconManager.h"
#include "SpaceManager.h"

#include "engine/nucocos2d_wraper.h"
#include "ConnectGetServerInfo.h"

#include "ljfmfsmanager.h"
#include "ljfileinfo.h"
#include "CallLuaUtil.h"
#include "platform/platform_types.h"

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
#include "WinWebBrowser/WinSDK.h"
#endif

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <ShlObj.h>
#endif

#include "DeviceInfo.h"

#include "GameTable/message/CMessageTip.h"
#include "MessageManager.h"
#include "Manager/NewRoleGuideManager.h"

#include "SceneNpc.h"

#include "SceneMovieManager.h"

#include "DownloadManager.h"
#include "BattleReplayManager.h"

#include "ProtoDef/rpcgen/fire/pb/game/MoneyType.hpp"
#include "ProtoDef/rpcgen/fire/pb/item/BagTypes.hpp"

#ifdef ANDROID
#include "androidcommon/AndroidLoginDialog.h"
#endif

#ifdef ANDROID
#include "../../androidcommon/ChannelPlatformUtil.h"
#include "../../common/platform/android/SDJniHelper.h"
#include <jni.h>
#endif
#include "../../common/updateengine/UpdateEngine.h"

#include "../Utils/IniManager.h"
#include "ConfigManager.h"
#include "3rdplatform/ChannelManager.h"

#include "fire/pb/attr/RoleCurrency.hpp"
#include "fire/pb/RoleInfo.hpp"

#ifdef ANDROID
#include "SDJniHelper.h"
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

#include "VideoPlayerEngine.h"

#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
#include "ShareSdkViewController.h"
#include "ScreenRecordController.h"
#endif
#include "MusicSoundVolumeMixer.h"
#include "GameCenter.h"
#include "LocationDetector.h"
#include "PhotoPicker.h"

#define  WM_TASKBAR WM_APP+450

#define USER_INFO "/AdvanSettingInfo.xml"

#define  GCAvailableMemSize 280
#define  ChatOutWindowDefautltWidth 230

bool GameApplication::m_bXmlBeanReady = false;
char GameApplication::m_VoiceServerAddress[256];
char GameApplication::m_platformChannel[64];

#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
char GameApplication::m_QuincyServerAddress[256] = {0};
char GameApplication::m_CheckNetHostName[256] = {0};
#endif

static bool s_bIsOpenGLReady = false;

static bool s_bIsGameInBackground = false;
static int  s_iBackgroundTicks = 0;

void LogLuaTraceBack()
{
#ifdef _DEBUG
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("print(debug.traceback())");
#endif
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("LogErr(debug.traceback())");
}

void gRefuseAppBrightness(bool bBackgroundMode)
{
	//进入后台或者切回端设置亮度为玩家自定义亮度
	Nuclear::EngineLayer* layer = (Nuclear::EngineLayer*)Nuclear::GetEngine()->GetEngineLayer();
	if (!layer)
		return;
	bool IsRun = true;
#if defined(_LOCOJOY_SDK_) || defined(_YJ_SDK_)
	if (layer->m_IsRunBrightNess)
		IsRun = true;
	else
		IsRun = false;
#endif
	if (IsRun&&layer->m_BrightNessVersion)
	{
		if (layer)
		{
			if (bBackgroundMode)
			{
				if (!layer->m_bDark)
					layer->m_fCurBrightness = IOS_MHSD_UTILS::GetCurBrightness();
				IOS_MHSD_UTILS::SetCurBrightness(layer->m_fCurBrightness);
				layer->m_IsTick = false;
			}
			else
			{
				layer->m_fCurBrightness = IOS_MHSD_UTILS::GetCurBrightness();
				IOS_MHSD_UTILS::SetCurBrightness(layer->m_fCurBrightness);
				layer->m_IsTick = true;
			}
			layer->m_DuraTime = 0;
			layer->m_bDark = false;
			layer->m_LastTick = Nuclear::GetMilliSeconds();
		}
	}
}

void gSetBackgroundMode(bool bBackgroundMode)
{
    if (bBackgroundMode == s_bIsGameInBackground)
        return;

	LOGI("SetBackgroundMode: %d", bBackgroundMode);
    
    s_bIsGameInBackground = bBackgroundMode;
    s_iBackgroundTicks = 0;
    if (cocos2d::CCDirector::sharedDirector()) {
        cocos2d::CCDirector::sharedDirector()->SetBackgroundMode(s_bIsGameInBackground);
    }

	if (GetBattleManager() && bBackgroundMode)
	{
		GetBattleManager()->BeginAutoOperate();
	}
#if CC_TARGET_PLATFORM != CC_PLATFORM_IOS
	gRefuseAppBrightness(bBackgroundMode);
#endif
	if (gGetStateManager() && gGetStateManager()->getGameState() == eGameStateRunning)
	{
		if (bBackgroundMode)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ShowHide.ClientLockScreen");
		}
		else
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ShowHide.UnClientLockScreen");

			cocos2d::VideoPlayer::applicationWillEnterForeground();
		}
	}
	
    //finish all touch event
    if (bBackgroundMode)
    {
        if (CEGUI::Window::getCaptureWindow()) {
            CEGUI::Window::getCaptureWindow()->releaseInput();
        }
        
    }
}

bool gIsInBackground()
{
	return s_bIsGameInBackground;
}

bool gGetReloadAllTexturesState()
{
    return s_bIsOpenGLReady;
}

void gSetReloadAllTexturesState(bool flag)
{
	s_bIsOpenGLReady = flag;
}

class CTextureFileReloader : public cocos2d::CCFileProvidor
{
	Nuclear::NuclearBuffer data;
public:
    virtual bool LoadFile(CCFileImage& f, const char* path)
    {
        data.clear();
		Nuclear::Engine* engine = (Nuclear::Engine*)Nuclear::GetEngine();
        
        bool bRet = engine->GetFileIOManager()->GetFileImage(s2ws(path), data, false);
        
        f.pData = (void*)data.constbegin();
        f.iSize = data.size();
        return bRet;
    }
};

void ReloadAllTextures()
{
    LOGD("OnReloadAllTexture: gl reset");
    
    Nuclear::GetEngine()->GetRenderer()->OnResetDevice();

	Nuclear::GetEngine()->GetRenderer()->ResetAllTextureUName();

	Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
	if (pFontMan)
	{
		pFontMan->GetFontTexturePool()->Release();
	}

	if (GetBattleManager())
	{
		GetBattleManager()->ReleaseAllSpriteNameTexture();
	}
	if (gGetScene())
		gGetScene()->ReleaseAllSpriteNameTexture();

    if (gGetGameUIManager())
    {
		CEGUI::FontManager::getSingleton().freeAllFont();
        CEGUI::ImagesetManager::getSingleton().CleanUPTextureState();

		CEGUI::FontManager::getSingleton().updateAllFont();
		
        CEGUI::System::getSingleton().signalRedraw();
    }

    if(GetBattleManager())
    {
		GetBattleManager()->ReloadAllSpriteNameTexture();
    }
    if(gGetScene())
		gGetScene()->ReloadAllSpriteNameTexture();

	Nuclear::GetEngine()->GetRenderer()->OnReloadAllTexture();
    CTextureFileReloader reloader;
    cocos2d::CCTextureCache::reloadAllTextures(&reloader);    
}


void GameApplication::OnReloadAllTexture()
{

}

void GameApplication::InitSpecialEffectList()
{
}


GameApplication GameApplication::s_instance;

LoadXmlBeansTask::LoadXmlBeansTask():Nuclear::ExecThread::CallbackTask(0)
{    
}

void LoadXmlBeansTask::Execute()
{
    if(gGetGameApplication())
        gGetGameApplication()->LoadBeanAsync();
}

void LoadXmlBeansTask::Callback()
{
    core::CMutex callbackmutex;
    {
        core::CMutex::Scoped autoMutex(callbackmutex);
        if(gGetGameApplication())
            gGetGameApplication()->OnXmlBeanReady();
    }
}

void CloseExecutorTimer::OnTimer()
{
	GameApplication::GetInstance().CloseExecutor();
}

void CloseExecutorTimer::CancelSchedule()
{
	Nuclear::GetEngine()->CancelTimer(this);
}

bool CloseExecutorTimer::SetSchedule(int cdtime)
{
	return Nuclear::GetEngine()->ScheduleTimer(this, cdtime);
}

GameApplication* gGetGameApplication()
{
	return &(GameApplication::GetInstance());
}

#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
const char* GetQuincyServerAddress()
{
	return GameApplication::GetInstance().GetQuincyServerAddress();
}
const char* GetCheckNetHostName()
{
    return GameApplication::GetInstance().GetCheckNetHostName();
}
const char* GetHttpServerAddressPlatForm()
{
	return GameApplication::GetInstance().GetHttpServerAddressPlatForm();
}
#endif

extern Nuclear::NuclearLocation gGetCursorPosition()
{
	return gGetGameApplication()->gGetCursorPosition();
}

extern eDisplayMode gGetDisPlayMode()
{
	return gGetGameApplication()->gGetDisPlayMode();
}

extern void gGetTimeDetailsCPP(const int64_t seconds, int64_t &days, int &hours, int &mins, int &secs)
{
    days = seconds/(3600*24);
    int leftHourSec = seconds - days*3600*24;
    
    hours = leftHourSec/3600;
    int leftMinSec = leftHourSec - hours*3600;
    
    mins = leftMinSec/60;
    secs = leftMinSec - mins*60;
    
    if(days < 0)
    {
        days = 0;
    }
    if(hours < 0)
    {
        hours = 0;
    }
    if(mins < 0)
    {
        mins = 0;
    }
    if(secs < 0)
    {
        secs = 0;
    }
}

extern int64_t gGetServerTime()
{
	return GameApplication::GetInstance().m_iServerTime;
}

extern void gSetMaxFps(int maxFps)
{
//#if (CC_TARGET_PLATFORM != CC_PLATFORM_WIN32)
	cocos2d::CCDirector* pDirector = cocos2d::CCDirector::sharedDirector();
	if (pDirector)
	{
		pDirector->setAnimationInterval(1.0f / float(maxFps));
	}
//#endif
}

extern Game::NetConnection* gGetNetConnection()
{
    if (gGetGameApplication()) {
        return gGetGameApplication()->GetGameNetConnection();
    }
    
    return NULL;
 }

extern void gDestroyNetConnection()
{
    if (gGetGameApplication())
        gGetGameApplication()->DestroyGameNetConnection();
}


void UIQuestEffectNotify::OnEnd(Nuclear::IEffect *pEffect)
{
	if (pEffect)
	{
		pEffect->RemoveNotify(this);
		gGetScene()->ReleaseFinishQuestEffect();
		delete this;
	}
}

void UIQuestEffectNotify::OnDelete(Nuclear::IEffect *pEffect)
{
	delete this;
}

Nuclear::TextBlockHandle hTB = Nuclear::INVALID_TEXTBLOCK_HANDLE;

GameApplication::GameApplication()
: m_pEngine(NULL)
, m_pNetConnection(NULL)
, m_pEnv(NULL)
, m_time(0)
, m_iEnterTime(0)
, m_iServerTime(0)
, m_bDrawServantIntro(-1)
, m_bInLittleGame(false)
, m_bFirstLogin(false)
, m_iMouseOverCheckTimer(1000)
, m_WaitPictureHandle(Nuclear::INVALID_PICTURE_HANDLE)
, m_bFirstEnterGame(false)
, m_bHotApplySysSetting(false)
, m_bDrawName(true)
, m_bDrawUI(true)
, m_iMainCharacterShapeID(0)
, m_iLastScreenRoleNum(100)
, m_dwGatherElapse(0)
, m_bBeginGatherFpsInf(false)
, m_iGatherTime(60*1000)
, m_TotalFps(0.0f)
, m_iFpsTick(0)
, m_bAutoAdjustScreenNum(false)
, m_curScreenMaxRoleType(0)
, m_bEnableAutoHideModel(true)
, m_ptBattleOldViewport(0,0)
, m_pCloseExecutorTimer(NULL)
, m_bWaitForEnterWorldMessage(false)
, m_bLogoLoad(false)
, m_iLoadLogoTime(0)
, m_bWaitToEnterWorld(false)
, m_iEnterWorldRoleID(0)
, m_bShieldAllAppFunc(true)
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
, m_bNeedLogin(false)
, m_bStartChangeMap(false)
#endif
, m_bDisconnect(false)
, m_iTimeoutCount(30)
, m_bReconnecting(false)
, m_bShowFPS(false)
, m_bShowUIState(false)
, m_bShowPerformance(false)
, mCollectFrequency(5000)
, mDeltaCollectTime(0)
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
, m_bUseSDKInWindows(false)
#endif
, mLuaPrint(true)
, m_bIsDetachingIME(false)
, m_willDispatchTouchEvent(true)
, m_nFirstTimeEG(10)
, m_RecordEnable(false)
, m_CurRecordState(0)
, m_LocalPushType(0)
, m_bGameInitialized(false)
{
#if (defined ANDROID) || (defined WIN32 || defined _WIN32)
	m_bIsInterrupt = false;
#endif
	m_ListHoldSchoolEffect.clear();
	m_ListHoldModelEffect.clear();
}

GameApplication::~GameApplication()
{
	RemoveDownloadManager();
	TipsManager::RemoveInstance();
	BattleReplayManager::RemoveInstance();
	GameStateManager::RemoveInstance();
    GameConfigManager::RemoveInstance();
	MT3::ChannelManager::ExitChannelManager();
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	WinSDK::destroyInstance();
#endif
}

bool GameApplication::LoadingMapNotify(int pro)
{
	int progress = pro;

    if (gGetScene() && gGetScene()->IsFirstEnterMap())
    {
		progress = pro/3;
        if (progress<24) progress=24;
	}
    
    //SDLOG_INFO(L"load map pro:%d \n",pro);
    DrawLoginBar(progress);
 	return true;
}

void GameApplication::EnterBattleDemo()
{
	gGetStateManager()->setGameState(eGameStateBattleDemo);
}

void GameApplication::ExitBattleDemo()
{
	gGetStateManager()->setGameState(eGameStateNull);
}

void GameApplication::EnterBattleAnimEdit() 
{
	gGetStateManager()->setGameState(eGameStateEditBattleAni);
}

void GameApplication::ExitBattleAnimEdit()
{
	gGetStateManager()->setGameState(eGameStateNull);
}

void GameApplication::EnterResourceCheck() 
{
}

void GameApplication::ExitResourceCheck()
{
	gGetStateManager()->setGameState(eGameStateNull);
}

void GameApplication::StartLogin()
{
    if (gGetGameOperateState())
        gGetGameOperateState()->ResetCursor();
    if(gGetStateManager())
        gGetStateManager()->setGameState(eGameStateNull);
	LoginManager::NewInstance();
	MessageManager::NewInstance();
	gGetLoginManager()->Init();

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeScriptFile(L"main.lua");

	playLoginBGM();  // ycl 从游戏中返回登录界面时再次播放登录背景音乐
}

void GameApplication::FinishLogin()
{
	gGetStateManager()->setGameState(eGameStateRunning);
    if(gGetLoginManager())
    {
        gGetLoginManager()->Clear();
    }

	//发送ios本地推送是否打开的设置
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(m_LocalPushType);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("SystemsettingTuiSongDlg.LocalNotificationType", 1);
}

void GameApplication::BeginDrawServantIntro()
{
	m_bFirstLogin = true;
}

void GameApplication::EndDrawServantIntro()
{
	m_bFirstLogin = false;
	if (!m_bReconnecting)
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LoginImageAndBar.unLoad");
	}

	if (GetBattleManager() && GetBattleManager()->IsInBattle()==false)
	{
		GetBattleManager()->ResetBattleGround(true);
	}
	if (GetMainCharacter())
	{
		GetMainCharacter()->CheckGoto();
	}

	if (m_bReconnecting)
	{
		//reconnect successed
		m_bReconnecting = false;
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("OnInternetReconnected");
	}
}

bool GameApplication::OnBeforeRender(int now)
{
	if (gGetScene())
	{
		gGetScene()->OnBeforeRender(now);
	}
	if (GetBattleManager() && GetBattleManager()->IsInBattle())
	{
		GetBattleManager()->OnBeforeRender(now);
	}
	return true;
}

void GameApplication::SetTime(const int64_t &servertime)
{
	m_iServerTime = servertime;
	time_t et = m_iServerTime/1000;
	struct tm times;
	localtime_s(&times,&et);
	
	int allseconds = times.tm_min * 60 + times.tm_sec;
	m_time = allseconds/150 % 12;

	int64_t time_set = m_iServerTime + ((23 - times.tm_hour) * 3600 + (59 - times.tm_min) * 60 + (60 - times.tm_sec)) * 1000;
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushInt64ToLuaStack(time_set);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("TaskManager_CToLua_SetTime", 1);

	if(m_time == 4)
	{
		if(gGetScene() && gGetScene()->isOnDreamScene())
		{
			return;
		}
	}
	else if(m_time == 10)
	{
		if(gGetScene() && gGetScene()->isOnDreamScene())
		{
			return;
		}
	}
}

void GameApplication::OnWeeHours()
{
	time_t et = m_iServerTime/1000;
	struct tm times;
	localtime_s(&times,&et);
}

bool GameApplication::IsZerosHours()
{
	time_t et = m_iServerTime/1000;
	struct tm times;
	localtime_s(&times,&et);

	if(times.tm_hour == 23 && times.tm_min == 59 && times.tm_sec >= 55)
		return true;

	return false;
}

//版本信息
unsigned int GameApplication::GetVersionOld()
{
    return UpdateEngine::g_uiVersionOld;
}
std::wstring GameApplication::GetVersionOldCaption()
{
    return UpdateEngine::g_wsVersionOldCaption;
}
unsigned int GameApplication::GetVersion()
{
	return UpdateEngine::g_uiVersion;
}
std::wstring GameApplication::GetVersionCaption()
{
	return UpdateEngine::g_wsVersionCaption;
}
unsigned int GameApplication::GetVersionBase()
{
	return UpdateEngine::g_uiVersionBase;
}
std::wstring GameApplication::GetVersionBaseCaption()
{
	return UpdateEngine::g_wsVersionBaseCaption;
}
unsigned int GameApplication::GetChannel()
{
	return UpdateEngine::g_uiChannel;
}
std::wstring GameApplication::GetChannelCaption()
{
	return UpdateEngine::g_wsChannelCaption;
}
std::wstring GameApplication::GetExtendValua(std::wstring wsKey)
{
	std::map<std::wstring, std::wstring>::iterator it = UpdateEngine::g_ExtendMap.find(wsKey);
	if (it != UpdateEngine::g_ExtendMap.end())
	{
		return it->second;
	}
	return L"";
}
bool GameApplication::GetLoadFromPak()
{
	return UpdateEngine::g_uiNoPack != 1;
}
bool GameApplication::GetVersionDonotCheck()
{
	return UpdateEngine::g_uiVersionDonotCheck == 1;
}

//预解释技能中的特效及战斗系统所使用到的表格
void GameApplication::preLoadBattleEffect()
{
	unsigned i = 0;
	std::vector<int> stageIds;
	Nuclear::Engine* pEngine = static_cast<Nuclear::Engine*>(Nuclear::GetEngine());
	Nuclear::EffectManager* pEffectMan = pEngine->GetEffectManager();
	GameTable::battle::GetCStageInfoTableInstance().getAllID(stageIds);
	for (i = 0; i < stageIds.size(); ++i)
	{
		int stageID = stageIds[i];
		const GameTable::battle::CStageInfo& stageconfig = GameTable::battle::GetCStageInfoTableInstance().getRecorder(stageID);
		if (stageconfig.youfangxiang == 1)
		{
			pEffectMan->CreateEffect(stageconfig.effectname + L"1", false, true);
			pEffectMan->CreateEffect(stageconfig.effectname + L"2", false, true);
		}
		else
		{
			pEffectMan->CreateEffect(stageconfig.effectname, false, true);
		}
	}

	std::vector<int> stage2Ids;
	GameTable::battle::GetCStageInfo2TableInstance().getAllID(stage2Ids);
	for (i = 0; i < stageIds.size(); ++i)
	{
		int stageID = stageIds[i];
		const GameTable::battle::CStageInfo2& stage2config = GameTable::battle::GetCStageInfo2TableInstance().getRecorder(stageID);
		std::string::size_type nPos = stage2config.effectname.find(L"_.");
		if (stage2config.youfangxiang)
		{
			pEffectMan->CreateEffect(stage2config.effectname + L"1", false, true);
			pEffectMan->CreateEffect(stage2config.effectname + L"2", false, true);
		}
		else
		{
			pEffectMan->CreateEffect(stage2config.effectname, false, true);
		}
	}

	GameTable::battle::GetCStageInfoTableInstance();
	GameTable::battle::GetCStageInfo2TableInstance();
	GameTable::battle::GetCBattleAIConfigTableInstance();
	GameTable::gm::GetCbattlelistTableInstance();
	GameTable::npc::GetCMonsterConfigTableInstance();

	MainCharacter::InitMouseBtnDownEffect();//初始化点击地面特??
	CArtText::InitArtText();//初始化冒数字特效
}

void GameApplication::StartGame()
{
#ifdef XP_PERFORMANCE
	float beginMemSize = CFileUtil::GetUsedMemory();
#ifdef WIN32
	std::string strOut = "[Mem Dosage] StartGame Begin: ";
	strOut += StringCover::floatToString(beginMemSize);
	strOut += '\n';
	::OutputDebugStringA(strOut.c_str());
#endif
#endif
    
    GameOperateState::NewInstance();
    
	EmotionManager::NewInstance();
	SceneMovieManager::NewInstance();
    
	FinishLogin();

	GameScene::NewInstance();
	SceneNpcManager::NewInstance();
	RoleItemManager::NewInstance();

	BattleMagicControl::NewInstance();
	BattleManager::NewInstance();

	NewRoleGuideManager::NewInstance();
	
    TaskOnOffEffectManager::NewInstance();

	MusicSoundVolumeMixer::NewInstance();
	GameCenter::NewInstance();
    LocationDetector::NewInstance();

	preLoadBattleEffect();//预解释技能中的特效及战斗系统所使用到的表格

	m_nFirstTimeEG = 10;
	m_iEnterTime = Nuclear::GetMilliSeconds();
#ifdef XP_PERFORMANCE
	float endMemSize = CFileUtil::GetUsedMemory();
	float deltaMemSize = endMemSize - beginMemSize;
#ifdef WIN32
	strOut = "[Mem Dosage] StartGame End: ";
	strOut += StringCover::floatToString(deltaMemSize);
	strOut += " Total MemUsage:";
	strOut += StringCover::floatToString(endMemSize);
	strOut += '\n';
	::OutputDebugStringA(strOut.c_str());
#endif
#endif
}

bool GameApplication::OnExit() 
{
    CleanupNetModule();

#ifdef USE_PP
	VMProtectBeginVirtualization
	ExitGame(eExitType_ToWindows);
	Protector::StopProtector();
	VMProtectEnd	
#else
	ExitGame(eExitType_ToWindows);
#endif

	return true;
}

void GameApplication::ExitGame(eExitType eType, int relogin)
{
	cocos2d::extension::CCHttpClient::getInstance()->clear();

	gGetWavRecorder()->release();
	gGetVoiceManager()->release();
	gGetSpaceManager()->Release();

	CocosDenshion::SimpleAudioEngine::sharedEngine()->stopAllEffects();

	if (m_bReconnecting)
	{
		m_bReconnecting = false;
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.reconnectdlg\".DestroyDialog()");
	}
	else
		CocosDenshion::SimpleAudioEngine::sharedEngine()->stopBackgroundMusic(true);
    
	CollectingGCMemory();

	gGetGameApplication()->SetWaitForEnterWorldState(false);
	gGetGameApplication()->SetWaitToEnterWorld(false);
    gGetGameApplication()->SetEnterWorldRoleID(0);
    
	m_SpecialEffectList.clear();
	if (gGetNetConnection())
	{
		gGetNetConnection()->SetStatOpenFlag(true);
	}

	if (m_bFirstLogin)
	{
		EndDrawServantIntro();
	}

	EndAdjustScreenRoleNum();

	MessageManager::RemoveInstance();
    LoginManager::RemoveInstance();
	BattleManager::RemoveInstance();
	BattleMagicControl::RemoveInstance();
	SceneNpcManager::RemoveInstance();
	GameScene::RemoveInstance();
	NewRoleGuideManager::RemoveInstance();

	if (gGetGameUIManager())
	{
		//gGetGameUIManager()->clearMessages();
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CTipsManager.clearMessages_");
	}
	if (GetMainCharacter())
	{
		GetMainCharacter()->RefreshRoleInfoOfThisServer();
	}

	if (cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()) {
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("MT3HeroManager_PurgeData");
	}

	switch (eType)
	{
	case eExitType_ToLogin:
		{	
			delete m_pNetConnection;
			m_pNetConnection = NULL;

			static_cast<Nuclear::Engine*>(Nuclear::GetEngine())->StopRunUserTask();

			if (gGetGameUIManager()) gGetGameUIManager()->OnExitGameToLogin(relogin);

            StartLogin();
		}
		break;
	case eExitType_ToSelectRole:
		{	
			if (gGetGameUIManager()) gGetGameUIManager()->OnExitGameToSelectRole();
		}
		break;
	case eExitType_ToWindows:
		{
			// yeqing 2015-10-20
			delete m_pNetConnection;
			m_pNetConnection = NULL;

			// yeqing 2015-10-19 // 加入，若有问题，以后再Fix
			if (gGetGameUIManager()) gGetGameUIManager()->OnExitGameApp();

			SceneMovieManager::RemoveInstance();
			GameOperateState::RemoveInstance();	// yeqing 2015-10-21 加入

#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
			exit(0);
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
			MT3::JniMethodInfo t;
			if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "ExitGame", "()V"))
			{
				t.env->CallStaticVoidMethod(t.classID, t.methodID);
				t.env->DeleteLocalRef(t.classID);
			}
			else
			{
				exit(0);
			}
			return;
#endif
	}
		break;
	default:
		break;
	}

	Character::ClearPicPool();
	RoleItemManager::RemoveInstance();
	TaskOnOffEffectManager::RemoveInstance();

	if (eType == eExitType_ToWindows)
	{
		//最后清除UI资源
		gGetGameUIManager()->UnInitGameUI();
		GameUImanager::RemoveInstance();	// yeqing 2015-10-20
		CleanupLuaScriptModule();
		CleanupMT3Log();
	}

	MusicSoundVolumeMixer::RemoveInstance();
	GameCenter::RemoveInstance();
    LocationDetector::RemoveInstance();
}

void GameApplication::applicationEnterForeground()
{    
#if (defined ANDROID) || (defined WIN32 || defined _WIN32)
    m_bIsInterrupt = true;

    gSetBackgroundMode(false);
    
    LOGD("OnReloadAllTexture: applicationEnterForeground");

#endif
}

void GameApplication::applicationDidEnterBackground()
{
#if (defined ANDROID) || (defined WIN32 || defined _WIN32)
    
#if !((defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP))
    gSetReloadAllTexturesState(false);
#endif
    gSetBackgroundMode(true);
#endif
}

void GameApplication::AddAsyncLoadBeanTask()
{
    LoadXmlBeansTask* newtask = new LoadXmlBeansTask();
    AddTask(newtask);
}

void GameApplication::LoadBeanAsync()
{
}

#if defined(WIN7_32) && defined(_DEBUG)
int lua_print(lua_State * luastate)
{
	if (!gGetGameApplication()->isLuaDebugPrint())
		return 0;

	int nargs = lua_gettop(luastate);

	std::string t;
	for (int i = 1; i <= nargs; i++)
	{
		if (lua_istable(luastate, i))
			t += "table";
		else if (lua_isnone(luastate, i))
			t += "none";
		else if (lua_isnil(luastate, i))
			t += "nil";
		else if (lua_isboolean(luastate, i))
		{
			if (lua_toboolean(luastate, i) != 0)
				t += "true";
			else
				t += "false";
		}
		else if (lua_isfunction(luastate, i))
			t += "function";
		else if (lua_islightuserdata(luastate, i))
			t += "lightuserdata";
		else if (lua_isthread(luastate, i))
			t += "thread";
		else
		{
			const char * str = lua_tostring(luastate, i);
			if (str)
				t += lua_tostring(luastate, i);
			else
				t += lua_typename(luastate, lua_type(luastate, i));
		}
		if (i != nargs)
			t += "\t";
	}
	CCLOG("[Lua Print] %s", t.c_str());

	return 0;
}

int lua_getusectime(lua_State * luastate)
{
	timeval val;
	gettimeofday(&val, NULL);
	int64_t ret = ((int64_t)val.tv_sec) * 1000000 + val.tv_usec;
	lua_pushnumber(luastate, ret);
	return 1;
}
#endif

bool GameApplication::InitLuaScriptModule()
{
    Game::ProtocolLuaFunManager::NewInstance();
    USING_NS_CC;
    // register lua engine
	cocos2d::CCScriptEngineProtocol* pEngine = cocos2d::CCLuaEngine::engine();
	if (pEngine)
	{
		cocos2d::CCScriptEngineManager::sharedManager()->setScriptEngine(pEngine);

#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		tolua_engineWp8_open(pEngine->getLuaState());
		tolua_FireClientWp8_open(pEngine->getLuaState());
#elif defined WIN7_32
		tolua_engineWin32_open(pEngine->getLuaState());
		tolua_FireClientWin32_open(pEngine->getLuaState());
	#ifdef _DEBUG
		//将lua的print输出到vs的输出里 by lg
		const luaL_reg global_functions [] = {
			{ "print", lua_print },
			{ "getusectime", lua_getusectime },
			{ NULL, NULL }
		};
		luaL_register(pEngine->getLuaState(), "_G", global_functions);
	#endif
#else
		tolua_engine_open(pEngine->getLuaState());
		tolua_FireClient_open(pEngine->getLuaState());
#endif

		std::string path = "/script/";
    
		pEngine->addSearchPath(path.c_str());

		pEngine->executeScriptFile(L"dofile_main.lua");
	}
    return true;
}

bool GameApplication::CleanupLuaScriptModule()
{
    Game::ProtocolLuaFunManager::RemoveInstance();
    USING_NS_CC;

	// cleanup lua engine
    CCScriptEngineManager::sharedManager()->purgeSharedManager();
    
	return true;
}

void GameApplication::CopyLog()
{
    std::fstream fin;
	fin.open(CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "mt3_ct.log").c_str(), std::ios::in);
    if (fin.is_open()) {
        std::fstream *fout = new std::fstream();
		fout->open(CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "mt3_history.log").c_str(), std::ios::out | std::ios::app);
        if (fout->is_open()) {
            fout->seekg(0, std::ios::end);
            if (fout->tellg() > 5*1024*1024) {
                fout->close();
                fout->clear();
				fout->open(CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "mt3_history.log").c_str(), std::ios::out);
            }
        }
        if (fout->is_open()) {
            std::string stemp;
            while(std::getline(fin, stemp))
            {
                *fout << stemp << std::endl;
            }
            fout->close();
            delete fout;
        }
        fin.close();
    }

	fin.open(CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "CEGUI_ct.log").c_str(), std::ios::in);
	if (fin.is_open()) {
		std::fstream *fout = new std::fstream();
		fout->open(CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "CEGUI_history.log").c_str(), std::ios::out | std::ios::app);
		if (fout->is_open()) {
			fout->seekg(0, std::ios::end);
			if (fout->tellg() > 5 * 1024 * 1024) {
				fout->close();
				fout->clear();
				fout->open(CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "CEGUI_history.log").c_str(), std::ios::out);
			}
		}
		if (fout->is_open()) {
			std::string stemp;
			while (std::getline(fin, stemp))
			{
				*fout << stemp << std::endl;
			}
			fout->close();
			delete fout;
		}
		fin.close();
	}

	std::rename(CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "mt3_ct.log").c_str(), CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "mt3.log").c_str());
	std::rename(CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "CEGUI_ct.log").c_str(), CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "CEGUI.log").c_str());
}

bool GameApplication::InitMT3Log()
{
    CopyLog();
    core::Logger::NewInstance();
#if defined(_DEBUG) || defined(DEBUG)
	core::Logger::GetInstance()->setLoggingLevel(core::Insane);
#else
	core::Logger::GetInstance()->setLoggingLevel(core::Warnings);
#endif
	core::Logger::GetInstance()->setLogFilename(CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), "mt3_ct.log"), false);
    return true;
}

bool GameApplication::CleanupMT3Log()
{
    core::Logger::RemoveInstance();
    return true;
}
std::string GameApplication::GetIniFileName()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS || CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	std::string rootDir = CFileUtil::GetRootDir() + "/res";
	std::string catchDir = CFileUtil::GetCacheDir() + "/res";
#else
	#ifdef NoPack
	std::string rootDir = CFileUtil::GetRootDir() + "../../res";
	std::string catchDir = CFileUtil::GetCacheDir() + "/res";
	#else
	std::string rootDir = CFileUtil::GetRootDir() + "../../res1";
	std::string catchDir = CFileUtil::GetCacheDir() + "/res1";
	#endif
#endif
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	std::string clientIniName = "cfg/clientsetting_win.ini";
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	std::string clientIniName = "cfg/clientsetting_ios.ini";
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	std::string clientIniName = "cfg/clientsetting_android.ini";
#else
	std::string clientIniName = "cfg/clientsetting_wp.ini";
#endif
	std::string clientIniPath;
	if (GetLoadFromPak())
	{
		unsigned int FNCRC32 = LJCRC32Func(0, (unsigned char*)(clientIniName.c_str()), clientIniName.size() * sizeof(char));
		std::wstring clientIniNameWS = L"";
		clientIniNameWS = StringCover::NumTowstring<unsigned int>(FNCRC32);
		clientIniName = ws2s(clientIniNameWS);
		std::string clientIniPathRoot = rootDir + "/" + clientIniName;
		clientIniPath = catchDir + "/" + clientIniName;
		if (MHSD_UTILS::ExistFile(StringCover::to_wstring(clientIniPath)) == false)
		{
			CFileUtil::CopyFileByFS(StringCover::to_wstring(clientIniPathRoot), StringCover::to_wstring(clientIniPath));
		}
	}
	else
	{
		clientIniPath = rootDir + "/" + clientIniName;
	}

	return clientIniPath;
}
void GameApplication::InitIni()
{
	std::string clientIniPath = GetIniFileName();

	IniFile::read_profile_string("ClientSetting", "PlatformChannel", m_platformChannel, 64, "", clientIniPath.c_str());

	Nuclear::Engine* pEngine = static_cast<Nuclear::Engine*>(Nuclear::GetEngine());

	int showFPS = IniFile::read_profile_int("ClientSetting", "bShowFPS", 1, clientIniPath.c_str());
	m_bShowFPS = showFPS > 0 ? true : false;

	int showUIState = IniFile::read_profile_int("ClientSetting", "bShowUIState", 1, clientIniPath.c_str());
	m_bShowUIState = showUIState > 0 ? true : false;

	int showPerformance = IniFile::read_profile_int("ClientSetting", "bShowPerformance", 1, clientIniPath.c_str());
	m_bShowPerformance = showPerformance > 0 ? true : false;

	int luaPrint = IniFile::read_profile_int("ClientSetting", "bLuaPrint", 0, clientIniPath.c_str());
	mLuaPrint = luaPrint > 0 ? true : false;

	float fGCMemVolume = IniFile::read_profile_float("ClientSetting", "GCMemVolume", 220.0f, clientIniPath.c_str());
	pEngine->SetGCMemVolume(fGCMemVolume);

	float fMustGCMemVolume = IniFile::read_profile_float("ClientSetting", "MustGCMemVolume", 300.0f, clientIniPath.c_str());
	pEngine->SetMustGCMemVolume(fMustGCMemVolume);

	float fAvailableMemVolume = IniFile::read_profile_float("ClientSetting", "AvailableMemVolume", 256.0f, clientIniPath.c_str());
	pEngine->SetAvailableMemVolume(fAvailableMemVolume);

	mCollectFrequency = (unsigned int)IniFile::read_profile_int("ClientSetting", "GCCoolTime", 1, clientIniPath.c_str());

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	int useSDKInWindows = IniFile::read_profile_int("ClientSetting", "bUseSDKInWindows", 0, clientIniPath.c_str());
	m_bUseSDKInWindows = useSDKInWindows > 0 ? true : false;
	char HttpLoginUrl[256];
	IniFile::read_profile_string("ClientSetting", "HttpLoginUrl", HttpLoginUrl, 256, "", clientIniPath.c_str());
	WinSDK::getInstance()->m_strLoginUrl = HttpLoginUrl;
	char HttpChargeUrl[256];
	IniFile::read_profile_string("ClientSetting", "HttpChargeUrl", HttpChargeUrl, 256, "", clientIniPath.c_str());
	WinSDK::getInstance()->m_strChargeUrl = HttpChargeUrl;
	char gameName[128];
	char gamePointName[128];
	IniFile::read_profile_string("ClientSetting", "GameName", gameName, 128, "", clientIniPath.c_str());
	IniFile::read_profile_string("ClientSetting", "GamePointName", gamePointName, 128, "", clientIniPath.c_str());
	int isPointServer = IniFile::read_profile_int("ClientSetting", "bIsPointVersion", 0, clientIniPath.c_str());
	if (isPointServer > 0)
	{
		//点卡版
		SetGameMainWindowTitle(s2ws(gamePointName));
	}
	else
	{
		//非点卡版
		SetGameMainWindowTitle(s2ws(gameName));
	}
#endif

	int TmpTotalPhysMemoryLimit = IniFile::read_profile_int("ClientSetting", "TotalPhysMemoryLimit", 800 * 1000, clientIniPath.c_str());
	cocos2d::CCImage::SetTotalPhysMemoryLimit(TmpTotalPhysMemoryLimit);

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	if (useSDKInWindows)
	{
		IniFile::read_profile_string("ClientSetting", "HttpServerAddressPlatForm", HttpServerAddressPlatForm, 256, "", clientIniPath.c_str());
	}
	else
	{
		std::string strplatform = "http://192.168.32.2:8080/6688/";
		memcpy(HttpServerAddressPlatForm, &strplatform[0], strplatform.length());
	}
#else
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressPlatForm", HttpServerAddressPlatForm, 256, "", clientIniPath.c_str());
#endif
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressUserHead", HttpServerAddressUserHead, 256, "", clientIniPath.c_str());
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressBBS", HttpServerAddressBBS, 256, "", clientIniPath.c_str());
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressNewWarn", HttpServerAddressNewWarn, 256, "", clientIniPath.c_str());
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressIOSDump", HttpServerAddressIOSDump, 256, "", clientIniPath.c_str());
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressAndroidDump", HttpServerAddressAndroidDump, 256, "", clientIniPath.c_str());
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressVoice", HttpServerAddressVoice, 256, "", clientIniPath.c_str());
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressDaShi", HttpServerAddressDaShi, 256, "", clientIniPath.c_str());
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressSpace", HttpServerAddressSpace, 256, "", clientIniPath.c_str());
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressJubao", HttpServerAddressJubao, 256, "", clientIniPath.c_str());
	IniFile::read_profile_string("ClientSetting", "HttpServerAddressChatAcess", HttpServerAddressChatAcess, 256, "", clientIniPath.c_str());
}

#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
const char* GameApplication::GetQuincyServerAddress()
{
	std::string strIniFileName("SystemSetting.ini");
	IniManager IniMgr(strIniFileName);

	std::wstring strKey = L"HttpServerAddressIOSDump";
	std::wstring strValue;
    IniMgr.GetValueByName(L"ClientSetting", strKey, strValue);

	if (strValue.length() != 0)
	{
		return ws2s(strValue).c_str();
	}
	else
	{
		if (!strlen(m_QuincyServerAddress))
		{
			std::string clientIniPath = GetIniFileName();
			IniFile::read_profile_string("ClientSetting", "QuincyServerAddress", m_QuincyServerAddress, 256, "", clientIniPath.c_str());
		}
		return m_QuincyServerAddress;
	}
}
const char* GameApplication::GetCheckNetHostName()
{
    if (!strlen(m_CheckNetHostName))
    {
		std::string clientIniPath = GetIniFileName();
        IniFile::read_profile_string("ClientSetting", "CheckNetHostName", m_CheckNetHostName, 256, "www.baidu.com", clientIniPath.c_str());
    }
    return m_CheckNetHostName;
}
const char* GameApplication::GetHttpServerAddressPlatForm()
{
	if (!strlen(m_CheckNetHostName))
	{
		std::string clientIniPath = GetIniFileName();
		IniFile::read_profile_string("ClientSetting", "HttpServerAddressPlatForm", HttpServerAddressPlatForm, 256, "", clientIniPath.c_str());
	}
	return m_CheckNetHostName;
}

void GameApplication::saveQuincyServerAddress(std::wstring address)
{
	IniManager IniMgr("SystemSetting.ini");
	IniMgr.WriteValueByName(L"ClientSetting", L"HttpServerAddressIOSDump", address);
}
#endif

const char* GameApplication::GetVoiceServerAddress()
{ 
	std::string strVoiceIp = GetServerInfo()->getHttpAdressByEnum(eHttpChatUrl);
	sprintf(m_VoiceServerAddress,"%s", strVoiceIp.c_str());
	return m_VoiceServerAddress;
}

const std::string GameApplication::GetXunFeiServerAddress()
{
	std::string strXunFeiIp = GetServerInfo()->getHttpAdressByEnum(eHttpXunFeiServerUrl);
	if (strXunFeiIp.size())
	{
		return strXunFeiIp;
	}
	else
	{
		return "http://openapi.openspeech.cn"; // 讯飞永久域名.
	}
}

const std::string GameApplication::GetXunFeiAppId()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	return "57232a7f";
#endif
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	return "572326c0";
#endif
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	return "57232a51";
#endif
	return "57232a7f";
}

bool GameApplication::OnInit(int step)
{
#ifdef XP_PERFORMANCE
	double beginMemSize = CFileUtil::GetUsedMemory();
	unsigned int begin = Nuclear::GetMilliSeconds();
	unsigned int end = 0;
	double endMemSize = 0.0;
#endif

	switch (step)
	{
	case 0:
	{
		core::Logger::flurryEvent(L"GameApplication_OnInit_InitMT3Log_start");
		InitMT3Log();
		core::Logger::flurryEvent(L"GameApplication_OnInit_InitMT3Log_end");
#ifdef XP_PERFORMANCE
		end = Nuclear::GetMilliSeconds();
		endMemSize = CFileUtil::GetUsedMemory();
		float deltaMemSize = endMemSize - beginMemSize;
		LOGD("初始化时间为:%d", end - begin);
	#ifdef WIN32
		std::string strOut = "[Mem Dosage] step0: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += " Total MemUsage:";
		strOut += StringCover::floatToString(endMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
	#endif
#endif
		return true;
	}
	case 1:
	{
		core::Logger::flurryEvent(L"GameApplication_OnInit_InitNetModule_start");
		InitNetModule();
		core::Logger::flurryEvent(L"GameApplication_OnInit_InitNetModule_end");
		m_pNetConnection = NULL;
#ifdef XP_PERFORMANCE
		endMemSize = CFileUtil::GetUsedMemory();
		float deltaMemSize = endMemSize - beginMemSize;
	#ifdef WIN32
		std::string strOut = "[Mem Dosage] step1 InitNetModule: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
	#endif
#endif

		core::Logger::flurryEvent(L"GameApplication_OnInit_InitLuaScriptModule_start");
		InitLuaScriptModule();

		core::Logger::flurryEvent(L"GameApplication_OnInit_InitLuaScriptModule_end");
#ifdef XP_PERFORMANCE
		end = Nuclear::GetMilliSeconds();
		endMemSize = CFileUtil::GetUsedMemory();
		deltaMemSize = endMemSize - beginMemSize;
		LOGD("init net lua use time:%d", end - begin);
	#ifdef WIN32
		strOut = "[Mem Dosage] step1: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += " Total MemUsage:";
		strOut += StringCover::floatToString(endMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
	#endif
#endif
		return true;
	}
	case 2:
	{
#ifdef ANDROID
		std::wstring strResDir = s2ws(CFileUtil::GetRootDir()) + L"/res/";
#elif WIN7_32
	#ifdef NoPack
		std::wstring strResDir = s2ws(CFileUtil::GetRootDir()) + L"../../res/";
	#else
		std::wstring strResDir = s2ws(CFileUtil::GetRootDir()) + L"../../res1/";
	#endif
#else
		std::wstring strResDir = s2ws(CFileUtil::GetCacheDir()) + L"/res/";
#endif
		std::wstring strConfigDir = strResDir;
		strConfigDir += L"table/bintable/";
		std::wstring strErrorFileName;
		if (!TableDataManager::instance().loadAllTable(strErrorFileName, strConfigDir))
		{
			return false;
		}
		SetXmlBeanReady(true);
#ifdef XP_PERFORMANCE
		end = Nuclear::GetMilliSeconds();
		endMemSize = CFileUtil::GetUsedMemory();
		float deltaMemSize = endMemSize - beginMemSize;
		LOGD("init xmltable use time %d \n", end - begin);
	#ifdef WIN32
		std::string strOut = "[Mem Dosage] step2: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += " Total MemUsage:";
		strOut += StringCover::floatToString(endMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
	#endif
#endif
		return true;
	}
	case 3:
	{
		core::Logger::flurryEvent(L"GameApplication_OnInit_GetEngine_start");
		m_pEngine = Nuclear::GetEngine();
		core::Logger::flurryEvent(L"GameApplication_OnInit_GetEngine_end");
#ifdef XP_PERFORMANCE
		end = Nuclear::GetMilliSeconds();
		endMemSize = CFileUtil::GetUsedMemory();
		float deltaMemSize = endMemSize - beginMemSize;
		LOGD("init font use time %d \n", end - begin);
	#ifdef WIN32
		std::string strOut = "[Mem Dosage] step3: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += " Total MemUsage:";
		strOut += StringCover::floatToString(endMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
	#endif
#endif
		return true;
	}
	case 4:
	{
		core::Logger::flurryEvent(L"GameApplication_OnInit_GameUIManager_start");
		GameUImanager::NewInstance();
		gGetGameUIManager()->InitGameUI();
		InitFont();
#ifdef XP_PERFORMANCE
		endMemSize = CFileUtil::GetUsedMemory();
		float deltaMemSize = endMemSize - beginMemSize;
	#ifdef WIN32
		std::string strOut = "[Mem Dosage] step4 InitGameUI: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
	#endif
#endif
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("MT3HeroManager_Initialize");
#ifdef XP_PERFORMANCE
		deltaMemSize = CFileUtil::GetUsedMemory() - endMemSize;
		endMemSize = CFileUtil::GetUsedMemory();
	#ifdef WIN32
		strOut = "[Mem Dosage] step4 InitHeroManager: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
	#endif
#endif

    gGetWavRecorder()->initialize(CFileUtil::GetTempDir().c_str());
	gGetSpaceManager()->Initialize(CFileUtil::GetTempDir().c_str());

    gGetVoiceManager()->initialize();

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	#ifdef XP_PERFORMANCE
		deltaMemSize = CFileUtil::GetUsedMemory() - endMemSize;
		endMemSize = CFileUtil::GetUsedMemory();
		#ifdef WIN32
		strOut = "[Mem Dosage] step4 InitVoiceManager: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
		#endif
	#endif
#endif
		core::Logger::flurryEvent(L"GameApplication_OnInit_GameUIManager_end");
#ifdef XP_PERFORMANCE
		end = Nuclear::GetMilliSeconds();
		LOGD("init gameui use time %d \n", end - begin);
#endif

		GameConfigManager::NewInstance();

#ifdef XP_PERFORMANCE
		deltaMemSize = CFileUtil::GetUsedMemory() - endMemSize;
		endMemSize = CFileUtil::GetUsedMemory();
	#ifdef WIN32
		strOut = "[Mem Dosage] step4 new GameConfigManager: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
	#endif
#endif

		LoginManager::NewInstance();
		gGetLoginManager()->Init();
#ifdef XP_PERFORMANCE
		begin = Nuclear::GetMilliSeconds();
		deltaMemSize = CFileUtil::GetUsedMemory() - endMemSize;
		endMemSize = CFileUtil::GetUsedMemory();
	#ifdef WIN32
		strOut = "[Mem Dosage] step4 InitLoginManager: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
	#endif
#endif
		MessageManager::NewInstance();

#ifdef XP_PERFORMANCE
		end = Nuclear::GetMilliSeconds();
		endMemSize = CFileUtil::GetUsedMemory();
		deltaMemSize = endMemSize - beginMemSize;
		LOGD("init login use time %d \n", end - begin);
	#ifdef WIN32
		strOut = "[Mem Dosage] step4: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += " Total MemUsage:";
		strOut += StringCover::floatToString(endMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
	#endif
#endif
		return true;
	}
	case 5:
	{
		return true;
	}
	case 6:
	{
		return true;
	}

	default:
		break;
	}

	bool bNormalVer = cocos2d::CCImage::GetTotalPhysMemoryLimit() < cocos2d::CCImage::GetTotalPhysMemory() ? true : false;
	cocos2d::CCImage::SetIsNormal(bNormalVer);

	IconManager::NewInstance();

	m_bGameInitialized = true;

	return true;
}

// ycl 开场动画播放完毕后，过多少帧再�?SDK 界面
static int sFramesBeforeShowSDK = -1;

void GameApplication::OnTick(unsigned int now, unsigned int delta, unsigned realDelta)
{
#ifdef XP_PERFORMANCE
	static int64_t accumulateTime = 0;
	int64_t startTick = Nuclear::GetMilliSeconds();
#endif
	if (realDelta > 1000)
	{
		realDelta = delta;
	}

	m_iServerTime += realDelta;
    
    //调用DIALOG的TICK.
	Dialog::OnTickAllDlg(delta);

	if (gGetLoginManager())
	{
		gGetLoginManager()->Run(now,delta);
	}

	if (gGetGameUIManager())
    {
        gGetGameUIManager()->Run(now,delta);
    }

	if (gGetVoiceManager() && gGetStateManager() && gGetStateManager()->isGameState(eGameStateRunning))
    {
        gGetVoiceManager()->tick(delta);
    }

	if (GetDownloadManager() && gGetStateManager() && gGetStateManager()->isGameState(eGameStateRunning))
	{
		GetDownloadManager()->Run();
	}

	if (gGetScene()&& gGetStateManager() && gGetStateManager()->isGameState(eGameStateRunning))
	{        
#ifdef XP_PERFORMANCE
		static int64_t luaAccumulateTime = 0;
		int64_t luaStartTick = Nuclear::GetMilliSeconds();
#endif
		LuaTick(delta);
#ifdef XP_PERFORMANCE
		static int64_t luaTickTimePerSecond = 0;
		int64_t luaDeltaTick = Nuclear::GetMilliSeconds() - luaStartTick;
		luaTickTimePerSecond += luaDeltaTick;
		if (accumulateTime + realDelta >= 1000)
		{
			Nuclear::Renderer *pRenderer = Nuclear::GetEngine()->GetRenderer();
			Nuclear::RenderPerformance& rp = pRenderer->GetPerformance();
			rp.m_iLuaTickTimePerSecond = luaTickTimePerSecond;
			luaTickTimePerSecond = 0;
		}
#endif
        
		if (GetBattleManager() && GetBattleManager()->IsInBattle())
		{
			GetBattleManager()->Run(now,delta);
		}

		gGetScene()->Run(now,delta);

		if (gGetMessageManager())
		{
			gGetMessageManager()->Run(now,delta);
		}
	}

	// ycl 开场动画播放完毕后，过多少秒再??SDK 界面
	if (sFramesBeforeShowSDK > 0)
	{
		--sFramesBeforeShowSDK;

		if (sFramesBeforeShowSDK <= 0)
		{
#ifdef _LOCOJOY_SDK_
#ifdef ANDROID
			gGetGameUIManager()->sdkLogin();
#else
			gGetGameUIManager()->sdkInit();
#endif
#endif // _LOCOJOY_SDK_


#ifdef _YJ_SDK_
#ifdef ANDROID
			LOGE("GameApplication::OnTick begin");
			gGetGameUIManager()->JYSdkLogin();
			LOGE("GameApplication::OnTick end");
#else

#endif
#endif // _YJ_SDK_

		}
	}

	if (MusicSoundVolumeMixer::GetInstance())
	{
		MusicSoundVolumeMixer::GetInstance()->update(delta);
	}

	std::wstring strChannelId;
	if (LoginManager::GetInstance())
	{
		strChannelId = LoginManager::GetInstance()->GetChannelId();
	}
	if (strChannelId == L"108800000")  // 只在 appstore 版里使用 GameCenter
	{
		GameCenter* pGameCenter = GameCenter::GetInstance();
		if (pGameCenter)
		{
			pGameCenter->update(delta);
		}
	}
#ifdef XP_PERFORMANCE
	static int64_t gameTickTimePerSecond = 0;
	int64_t deltaTick = Nuclear::GetMilliSeconds() - startTick;
	accumulateTime += realDelta;
	gameTickTimePerSecond += deltaTick;
	if (accumulateTime >= 1000)
	{
		Nuclear::Renderer *pRenderer = Nuclear::GetEngine()->GetRenderer();
		Nuclear::RenderPerformance& rp = pRenderer->GetPerformance();
		rp.m_iGameTickTimePerSecond = gameTickTimePerSecond;
		accumulateTime = 0;
		gameTickTimePerSecond = 0;
	}
#endif

	cocos2d::PhotoPicker::shared()->update(delta * 0.001f);

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	if (gGetGameApplication()->IsUseSDKInWindows()) {
		// Windows登陆框的update
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.winlogindlg\".Update()");
	}
	// Windows公告栏的update
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.newswarndlg\".Update_Cpp()");
#endif
}

static cocos2d::VideoPlayer* spVideoPlayer = NULL;
static bool sbDoSDKOrShowQuickLoginAfterPlayingCG = false;

// ycl 开场动画播放事件回�?
void onVideoPlayerEvent(cocos2d::VideoPlayer::EventType event)
{
	switch (event)
	{
	case cocos2d::VideoPlayer::STOPPED:  // 开场动画被跳过
	case cocos2d::VideoPlayer::COMPLETED:  // 开场动画播放完??
	case cocos2d::VideoPlayer::TERMINATION:  // 其它错误
        {
            if(spVideoPlayer)
            {
                spVideoPlayer->addEventListener(NULL);

                spVideoPlayer->stop();
                spVideoPlayer->setVisible(false);
                spVideoPlayer->autorelease();  // 延迟释放 spVideoPlayer 对象
                spVideoPlayer = NULL;
            }

			// 播放登录背景音乐
			gGetGameApplication()->playLoginBGM();

			// SDK
			if (sbDoSDKOrShowQuickLoginAfterPlayingCG)
			{
				gGetGameApplication()->doSDKOrShowQuickLogin();
			}
        }
		break;
	}
}

void GameApplication::OnRenderInit(int now, int step, int totalstep)
{
	int timeBegin = Nuclear::GetEngine()->GetTimeCount();
    if (!m_bLogoLoad)
	{
        m_bLogoLoad = true;
        Nuclear::GetEngine()->GetRenderer()->FreePicture(m_WaitPictureHandle);
        m_WaitPictureHandle = Nuclear::GetEngine()->GetRenderer()->LoadPicture(L"/image/loading/waiting.jpg");
    }

	Nuclear::NuclearDisplayMode mode = Nuclear::GetEngine()->GetRenderer()->GetDisplayMode();
    float screenwith = mode.width;
    float screenheight = mode.height;
    float imagewidth = 1280;
    float imageheight = 720;
    Nuclear::NuclearFRectt rect(0.f, 0.f, screenwith, screenheight);
    if (((imagewidth*screenheight)/(imageheight*screenwith)) > 1)
	{
        // 上下贴边
        float w = ((imagewidth*screenheight)/imageheight - screenwith)/2; // 图片左右各裁减w
        rect.Assign(-w, 0.f, screenwith+w, screenheight);
    }
	else
	{
        // 左右贴边
        float h = ((screenwith*imageheight)/imagewidth - screenheight)/2; // 图片上下各裁减h
        rect.Assign(0.f, -h, screenwith, screenheight+h);
    }

	if (step == totalstep)
	{
		Nuclear::GetEngine()->GetRenderer()->FreePicture(m_WaitPictureHandle);
		m_WaitPictureHandle = Nuclear::INVALID_PICTURE_HANDLE;

        core::Logger::flurryEvent(L"show_sdk_login",true);
#if defined(XP_PERFORMANCE) && defined(WIN32)
		float beginMemSize = CFileUtil::GetUsedMemory();
		std::string strOut = "[Mem Dosage] InitGameUIPostInit Start: ";
		strOut += StringCover::floatToString(beginMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
        gGetGameUIManager()->InitGameUIPostInit();

        // ycl 播放开场动画。播放完毕再??SDK 初始化或显示快速登录界??
		cocos2d::CCUserDefault* pUserDefault = cocos2d::CCUserDefault::sharedUserDefault();
		if(!pUserDefault->getBoolForKey("StartCGPlayed", false))  // ycl 开场动画是否播过，如果没播过，则播放
		{
			pUserDefault->setBoolForKey("StartCGPlayed", true);
			pUserDefault->flush();

			if (!spVideoPlayer)
			{
				spVideoPlayer = new cocos2d::VideoPlayer;
				if (spVideoPlayer)
				{
#if defined(WIN32)
					std::wstring wStr = gGetGameUIManager()->GetFullPathFileName(L"/cfg/video/MT3.wmv");
#else
					std::wstring wStr = gGetGameUIManager()->GetFullPathFileName(L"/cfg/video/MT3.mp4");
#endif
					std::string strFilename = StringCover::to_string(wStr);
					spVideoPlayer->setFileName(strFilename, "", "");

					spVideoPlayer->setVideoRect(0, 0, 0, 0);

					spVideoPlayer->addEventListener(onVideoPlayerEvent);

					spVideoPlayer->play();
					spVideoPlayer->setVisible(true);
				}
			}
		}

		if (spVideoPlayer)  // ycl 如果当前正在播开场动画，则不在此处进??SDK 或显示快速登??
		{
			sbDoSDKOrShowQuickLoginAfterPlayingCG = true;
		}
		else
		{
            // 播放登录背景音乐
            playLoginBGM();
			doSDKOrShowQuickLogin();
		}
		
#if defined(XP_PERFORMANCE) && defined(WIN32)
		float endMemSize = CFileUtil::GetUsedMemory();
		float deltaMemSize = endMemSize - beginMemSize;
		strOut = "[Mem Dosage] InitGameUIPostInit End: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += " Total MemUsage:";
		strOut += StringCover::floatToString(endMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
	}

	if (!spVideoPlayer && m_WaitPictureHandle != Nuclear::INVALID_PICTURE_HANDLE)
	{
		Nuclear::GetEngine()->GetRenderer()->DrawPicture(m_WaitPictureHandle, rect, 0xFFFFFFFF);
	}

	int timeCount = Nuclear::GetEngine()->GetTimeCount() - timeBegin;
}

void GameApplication::doSDKOrShowQuickLogin()
{
#if defined(_LOCOJOY_SDK_) || defined(_YJ_SDK_)

// 启动时进入快速登录界面（除应用宝和易接之外的安卓SDK登陆改版）
#if defined(_LOCOJOY_SDK_) && defined(ANDROID)
	if (MT3::ChannelManager::getIsYingYongBao()) {
		sFramesBeforeShowSDK = 2;  // ycl 2 帧后再弹 SDK 界面
	} else {
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("Config.setCur3rdPlatform", "app");
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LoginQuickDialog.getInstanceAndShow");
	}
#else
	sFramesBeforeShowSDK = 2;  // ycl 2 帧后再弹 SDK 界面
#endif

#else

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	if (gGetGameApplication()->IsUseSDKInWindows()) {
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("Config.setCur3rdPlatform", "winapp");
	}
#endif
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("LoginQuickDialog.getInstanceAndShow");

#endif
}

void GameApplication::playLoginBGM()
{
	if (gGetGameConfigManager() && gGetGameConfigManager()->isPlayBackMusic())
	{
		std::wstring ws = L"/sound/scene/dengluqu.ogg";
		std::string newSoundRes;
		std::wstring newws;
		newws = LJFM::LJFMF::GetFullPathFileName(ws);
		if (!newws.empty())
		{
			newSoundRes = StringCover::to_string(newws);
			CocosDenshion::SimpleAudioEngine::sharedEngine()->playBackgroundMusic(newSoundRes.c_str(), true);
			int BackMusicValue = gGetGameConfigManager()->GetConfigValue(L"soundvalue");
			CocosDenshion::SimpleAudioEngine::sharedEngine()->setBackgroundMusicVolume((float)(BackMusicValue / 255.0f));
		}
	}
}

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
void GameApplication::setLoginInfo(std::wstring token ,std::wstring uid)
{
	m_sToken = token;
	m_sUid = uid;
	m_bNeedLogin = true;
}
#endif

void GameApplication::OnRenderUI(int now, bool realRender)
{
	if (gGetGameUIManager())
	{
		CEGUI::System::getSingleton().getRenderer()->ResetRenderTextures();  // 在渲染开始前清除所有CEGUI贴图被渲染过的标记，以便在渲染结束时判断出哪些贴图无用了
	}

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	if (m_bNeedLogin)
	{
		if (gGetLoginManager())
		{
			gGetLoginManager()->SetPassword(m_sToken);
			gGetLoginManager()->SetAccountInfo(m_sUid);
			gGetLoginManager()->ToServerChoose(gGetLoginManager()->GetSelectArea(), gGetLoginManager()->GetSelectServer());
		}
		m_bNeedLogin = false;
	}
#endif
    
	if (m_bDisconnect)
	{
		m_bDisconnect = false;
		if (gGetStateManager() && gGetStateManager()->getGameState() != eGameStateLogin && gGetStateManager()->getGameState() != eGameStateNull)
		{
			//重连 by lg
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.reconnectdlg\".getInstanceAndShow()");
			gGetGameApplication()->setReconnecting(true);
		}
		else if (gGetStateManager() && gGetStateManager()->getGameState() == eGameStateLogin)
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeString("require \"logic.createroledialog\".OnDisconnect()");
		}

		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("TaskManager_CToLua_ResetCurMainTaskNpcState");

		if (GetMainCharacter() && GetMainCharacter()->GetMoveState() == eMove_Fly)
		{
			GetMainCharacter()->StopAutoMove();
		}
	}

	Nuclear::World * world = static_cast<Nuclear::World*>(Nuclear::GetEngine()->GetWorld());
	if (!realRender && !world->IsMapLoaded())//没有加载地图的情况下，没有必要限??
	{
		return;
	}

	if (gGetScene() && m_bDrawName)
	{
		gGetScene()->Draw(now);
	}
	if (GetBattleManager() && GetBattleManager()->IsInBattle())
	{
		GetBattleManager()->DrawUnderUI(now);
	}

	if (gGetGameUIManager() && m_bDrawUI)
	{
		gGetGameUIManager()->Draw();
	}
	if (GetMainCharacter())
	{
		GetMainCharacter()->DrawMsgEffect(now);
	}

	if (GetBattleManager() && GetBattleManager()->IsInBattle())
	{
		GetBattleManager()->Draw(now);
	}

	if (cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()) {
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("RoleSkillManager_DrawEffect");
	}

	if (m_bWaitForEnterWorldMessage && !m_bReconnecting)
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LoginImageAndBar.draw", 20);
	}
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	if (m_bStartChangeMap && !m_bReconnecting)
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LoginImageAndBar.draw", 20);
	}
#endif

	if (gGetGameUIManager())
	{
		CEGUI::ImagesetManager::getSingleton().UpdateTextureState();
	}
}

void GameApplication::DrawLoginBar(float pro)
{
	if (!m_bReconnecting)
	{
		cocos2d::CCDirector* pDirector = cocos2d::CCDirector::sharedDirector();
		if (pDirector != NULL)
		{
			pDirector->BeginDraw(m_pEngine->m_adapter->get_logic_w(), m_pEngine->m_adapter->get_logic_h(),
				m_pEngine->m_adapter->get_screen_w(), m_pEngine->m_adapter->get_screen_h());
			cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColor);
		}

		if (gGetSceneMovieManager() && gGetSceneMovieManager()->isOnSceneMovie() && !gGetSceneMovieManager()->CanDrawLoadingBar())
		{
			if (gGetScene()) {
				gGetScene()->DrawMovieCapturePic(pro);
			}
		}
		else
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("LoginImageAndBar.draw", pro);

		if (pDirector != NULL)
		{
			cocos2d::CCShaderCache::sharedShaderCache()->popShader();
			pDirector->EndDraw();
		}

	}
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	m_bStartChangeMap = false;
#endif
}

Nuclear::NuclearWinMSGFuncResult GameApplication::OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam, void* pAdditionalParam/* = NULL*/)
{
#ifndef WIN32
	if (msg == Nuclear::WM_LBUTTONDOWN && gGetGameUIManager())
#else
	if (msg == WM_LBUTTONDOWN && gGetGameUIManager())
#endif 
	{
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
		gGetGameUIManager()->DetachIME();
#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
		bool bSuc = gGetGameUIManager()->DetachIME();
		if (bSuc)
		{
			return Nuclear::XPWINMSG_HANDLED;
		}
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
        m_willDispatchTouchEvent = !m_bIsDetachingIME;
        if (m_bIsDetachingIME) {
            return Nuclear::XPWINMSG_HANDLED;
        }
#endif
    }
    
    
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    if (!m_willDispatchTouchEvent)
    {
        return Nuclear::XPWINMSG_HANDLED;
    }
#endif

	// 新手引导过程中处理无效操作
	if (gGetGameUIManager() && gGetGameUIManager()->checkClickWhenGuide(hWnd, msg, wParam, lParam)) {
		return Nuclear::XPWINMSG_HANDLED;
	}
    
    if (isFirstEnterGame())
    {
        return Nuclear::XPWINMSG_HANDLED;
    }
    
    if (gGetSceneMovieManager()&&gGetSceneMovieManager()->isOnSceneMovie())
	{
		gGetSceneMovieManager()->OnWindowsMessage(hWnd,msg,wParam,lParam);
		
		return Nuclear::XPWINMSG_HANDLED;
	}
    
    if (gGetGameUIManager())
    {
        if (gGetGameUIManager()->OnWindowsMessage(hWnd, msg, wParam, lParam))
        {
            return Nuclear::XPWINMSG_INTERCEPT;
        }
    }
    
	if (gGetStateManager()&& gGetStateManager()->isGameState(eGameStateRunning) &&
        gGetScene() && GetMainCharacter()
		&&!m_bFirstLogin)
	{
		if (GetMainCharacter()->GetMoveState() == eMove_Fly)
		{
#ifndef WIN32
			if (Nuclear::WM_LBUTTONDOWN == msg)
#else
			if (WM_LBUTTONDOWN == msg)
#endif
			{
				GetTipsManager()->AddMsgTips(160470, 0, sEmptyParameters, true);
			}
			return Nuclear::XPWINMSG_HANDLED;
		}

		Nuclear::World* world = static_cast<Nuclear::World*>(Nuclear::GetEngine()->GetWorld());
		wParam *= 1 / world->GetScale();
		lParam *= 1 / world->GetScale();

        if (GetBattleManager()->IsInBattle())
		{
			if (GetBattleManager()->OnWindowsMessage( hWnd, msg, wParam, lParam))
				return  Nuclear::XPWINMSG_HANDLED;
		}
		else
		{
            
			if (GetMainCharacter()->OnWindowsMessage( hWnd, msg, wParam, lParam))
				return  Nuclear::XPWINMSG_HANDLED;
		}

#ifndef WIN32
		if (Nuclear::WM_LBUTTONDOWN == msg
#else
		if (WM_LBUTTONDOWN == msg
#endif
            && gGetScene()
            && gGetScene()->isLoadMaping() == false
            && gGetScene()->IsMapJumping()==false)
		{
			gGetScene()->UpdateMouseOverObjects(Nuclear::NuclearLocation(wParam, lParam));
		}
 	}

    return Nuclear::XPWINMSG_OK;
}

void GameApplication::OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type)
{
	if (GetBattleManager() && GetBattleManager()->IsInBattle())
		GetBattleManager()->OnUpdateSpriteAction(sprite,type);
	if (gGetScene())
		gGetScene()->OnUpdateSpriteAction(sprite,type);
}

void GameApplication::InitDisplayMode()
{

}

void GameApplication::BeginShake(int shakeID)
{

}

void GameApplication::ShakeRun(int now,int delta)
{

}


BOOL GameApplication::SetGameMainWindowTitle(LPCWSTR TitleName)
{
    return true;
}

BOOL GameApplication::SetGameMainWindowTitle(const std::wstring& TitleName)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
	cocos2d::CCEGLView *eglView = cocos2d::CCEGLView::sharedOpenGLView();
	WCHAR wszBuf[50] = { 0 };
	MultiByteToWideChar(CP_UTF8, 0, ws2s(TitleName).c_str(), -1, wszBuf, sizeof(wszBuf));
	SetWindowText(eglView->getHWnd(), wszBuf);
#endif
    return true;
}

BOOL GameApplication::SetGameMainWindowTitle(const UINT& StepInfo,const std::wstring& AddInfo)
{
	switch (StepInfo)
	{
	case eTitleInfoStep_GameName:
		{
			return SetGameMainWindowTitle(GameName) ;
		}
	case eTitleInfoStep_GameAndSeverName:
		{
			std::wstring strTitleName;
			strTitleName = GameName + L"--"  + GetSeverNameInfo();
			return SetGameMainWindowTitle(strTitleName);
		}
	case eTitleInfoStep_GameAndSeverAndPlayerName:
		{
			std::wstring strTitleName;
			strTitleName = GameName + L"--" + GetSeverNameInfo() + L"--" + AddInfo;
			return SetGameMainWindowTitle(strTitleName);
		}
	}
	return FALSE;
}

void GameApplication::SetAreaServerInfo(const std::wstring& area, const std::wstring& server)
{
	m_AreaName = area;
	m_ServerName = server;

	std::string servername("server");
	std::stringstream stream;
    
    std::string strIniPath(LASTACCOUNT_SERVER_INI);
  
	IniManager IniMgr(strIniPath);
    
    std::string strServerName=StringCover::to_string(server);

	for(int i = 1; i<30;i++)
	{
		stream.str("");
		stream<<strServerName<<i;

        std::string account;
		bool bExistLast=IniMgr.GetValueByName("ServerList", stream.str(), account);

		if(bExistLast)
		{
			std::wstring nowserver=StringCover::to_wstring(account);
			if(m_ServerName == nowserver)
				break;
		}
		else
		{
            std::string strCurServerName=StringCover::to_string(m_ServerName);
            IniMgr.WriteValueByName("ServerList", stream.str(), strCurServerName);
			
			stream.str("");
			stream<<"area"<<i;
			 std::string strCurAreaName=StringCover::to_string(m_AreaName);
            IniMgr.WriteValueByName("AreaList",stream.str().c_str(), strCurAreaName);
			break;
		}
	}
}

std::wstring GameApplication::GetSeverNameInfo()
{
	std::wstring SeverNameInfo;
	SeverNameInfo = m_AreaName +L"--" + m_ServerName;
	return SeverNameInfo;
}

Nuclear::NuclearLocation GameApplication::gGetCursorPosition()
{
	Nuclear::NuclearLocation pt(0, 0);
	m_pEngine->GetEnv()->GetCursorPosition(pt);
	return pt;
}

int GameApplication::GetLoginWindowWidth()
{
   return 1024;
}

eDisplayMode GameApplication::GetEnterWorldWindowMode()
{
	return eDisplayMode_1024768;
}

Nuclear::NuclearScreenMode GameApplication::GetIsWindow()
{
	return Nuclear::XPSM_WINDOW; //default setting
}

int GameApplication::GetRenderFlag()
{
	return 1; //default setting
}

Nuclear::NuclearMultiSampleType GameApplication::GetAntialiaseLevel(int& level )
{
	return Nuclear::XPMULTISAMPLE_4_SAMPLES;
}

bool GameApplication::isSpriteUseMipmap()
{
	return false;
}

std::wstring GameApplication::GetDisplayDesc(int w,int h)
{
	std::vector<stDisplayModeInf>::iterator it=m_vecDisplayModeInf.begin();
	for (;it!=m_vecDisplayModeInf.end();++it)
	{
		if ((*it).width==w&&(*it).height==h)
		{
			return (*it).showText;
		}
	}

	return L"";
}

void GameApplication::GetDisplayInf(eDisplayMode mode,stDisplayModeInf& inf)
{
	size_t idx=(size_t)mode;
	if (idx<m_vecDisplayModeInf.size())
	{
		inf= m_vecDisplayModeInf[idx];
	}
}


const std::vector<stDisplayModeInf>& GameApplication::GetAllDisplayInf()
{
	return m_vecDisplayModeInf;
}

void GameApplication::InitFont()
{
	Nuclear::IFontManager *pFontMan = m_pEngine->GetRenderer()->GetFontManager();
	{
		std::wstring szFile;
		std::vector<std::wstring> szSystemFont;
		std::vector<std::wstring> szName;
		int type = 1;
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
		szName.push_back(L"/system/fonts/DroidSansFallback.ttf");
		szSystemFont.push_back(L"simhei");
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
		szName.push_back(L"/System/Library/Fonts/STHeiti Medium.ttc");
		szName.push_back(L"/System/Library/Fonts/Cache/STHeiti-Medium.ttc");
		szName.push_back(L"/System/Library/Fonts/Core/STHeiti-Medium.ttc");
		szName.push_back(L"/System/Library/Fonts/STHeiti Light.ttc");
		szName.push_back(L"/System/Library/Fonts/Cache/STHeiti Light.ttc");
		szName.push_back(L"/System/Library/Fonts/Core/STHeiti-Light.ttc");
		szSystemFont.push_back(L"Heiti SC");
		for (int i = 0; i < szName.size(); i++)
		{
			std::ifstream* datafile = new std::ifstream(StringCover::to_string(szName[i]).c_str(), std::ios::binary | std::ios::ate);
			if (!datafile->fail())
			{
				datafile->close();
				delete datafile;
				szFile = szName[i];
				break;
			}
			datafile->close();
			delete datafile;
		}
		if (szFile.length() == 0)
		{
			type = 2;
			szFile = szSystemFont[0];
		}
#else
		std::wstring szFileName[] = { L"/Deng.ttf", L"/simkai.ttf" };
		szSystemFont.push_back(L"simhei");
		wchar_t szPath[256];
		SHGetSpecialFolderPathW(NULL, szPath, CSIDL_FONTS, 0);
		std::wstring strPath(szPath);
		for (int i = 0; i < 2; i++)
			szName.push_back(szPath + szFileName[i]);
#endif
#if CC_TARGET_PLATFORM != CC_PLATFORM_IOS
		for (size_t i = 0; i < szName.size(); i++)
		{
			std::ifstream* datafile = new std::ifstream(StringCover::to_string(szName[i]).c_str(), std::ios::binary | std::ios::ate);
			if (!datafile->fail())
			{
				datafile->close();
				delete datafile;
				szFile = szName[i];
				break;
			}
			datafile->close();
			delete datafile;
		}
		if (szFile.length() == 0)
		{
			type = 2;
			szFile = szSystemFont[0];
		}
#endif
		pFontMan->AddFontType(0, L"/ui/fonts/DFYuanW7-GB2312.ttf", 22, szFile.c_str(), 22, type);
		pFontMan->AddFontType(1, L"/ui/fonts/DFYuanW7-GB2312.ttf", 15, szFile.c_str(), 15, type);
		pFontMan->AddFontType(2, L"/ui/fonts/DFYuanW7-GB2312.ttf", 25, szFile.c_str(), 25, type);
	}
}

void GameApplication::AddToHWndList(const HWND& hWnd)
{
	hwndlist.push_back(hWnd);
}

int GameApplication::GetScreenMaxRoleByIniFile()
{
    return 0;
}

void GameApplication::SetScreenMaxRoleType(int type)
{
	if (m_curScreenMaxRoleType!=type)
	{
		m_curScreenMaxRoleType=type;
		if (type==0)
		{
			EnableAutoAdjustScreenNum(true);
		}else
		{
			EnableAutoAdjustScreenNum(false);
		}
	}
}

void GameApplication::OnGameStartAdjustScreenRolNum()
{
	m_curScreenMaxRoleType=gGetGameApplication()->GetScreenMaxRoleByIniFile();
	bool bAutoAdust=m_curScreenMaxRoleType==0?true:false;
	EnableAutoAdjustScreenNum(bAutoAdust);
}


void GameApplication::EnableAutoAdjustScreenNum(bool bEnable) 
{ 
	if (m_bAutoAdjustScreenNum!=bEnable)
	{
		if (bEnable)
		{
			BeginAdjustScreenRoleNum();
		}else
		{
			EndAdjustScreenRoleNum();

		}		
	}
}

void GameApplication::BeginAdjustScreenRoleNum()
{
    if (!m_bAutoAdjustScreenNum)
    {
		m_bAutoAdjustScreenNum=true;

		XPLOG_INFO(L"begin adjust screenrolenum\n");
		if (!m_bBeginGatherFpsInf)
		{
			BeginGatherFpsInf();
		}
    }
}

void GameApplication::EndAdjustScreenRoleNum()
{
	m_bAutoAdjustScreenNum=false;
	EndGatherFpsInf();

	XPLOG_INFO(L"end adjust screenrolenum\n");
}
void GameApplication::BeginGatherFpsInf()
{
	m_bBeginGatherFpsInf=true;
	m_dwGatherElapse=0;

	m_TotalFps=0.0f;
	m_iFpsTick=0;

	XPLOG_INFO(L"start gather fps info\n");
}
void GameApplication::EndGatherFpsInf()
{
	m_bBeginGatherFpsInf=false;
	m_dwGatherElapse=0;
	m_TotalFps=0.0f;
	m_iFpsTick=0;
}

int GameApplication::GetScreenNumByFps(int fps)
{
	return 100;
}

void GameApplication::OnFpsGatherTimer(int delta)
{
	if (m_bAutoAdjustScreenNum)
	{
		if (m_bBeginGatherFpsInf)
		{
			m_dwGatherElapse+=delta;
			if (m_dwGatherElapse>=m_iGatherTime)
			{
				if (m_iFpsTick>200)
				{
					float fFps=m_TotalFps/(float)m_iFpsTick;
					int iFps=(int)fFps;

					int screenRoleNum=GetScreenNumByFps(iFps);

					if (screenRoleNum>0&&screenRoleNum<=150)
					{
						m_iLastScreenRoleNum=screenRoleNum;

						std::wstring strMsg(L"");
						StringCover::FormatString(strMsg,L"average fps:%d,change screenrolenum to:%d\n",iFps,screenRoleNum);
						XPLOG_INFO(strMsg.c_str());
					}
				}
				
				BeginGatherFpsInf();
			}
			else
			{
				if (Nuclear::GetEngine()->GetWindowState()==Nuclear::XPWS_ACTIVE)
				{
					float curFps=Nuclear::GetEngine()->GetLatestFPS();
					m_TotalFps+=curFps;
					m_iFpsTick++;
				}
			}
		}
	}
}

bool GameApplication::AddTask(Nuclear::ExecThread::CallbackTask* pTask)
{
	if (Nuclear::GetEngine())
    {
		Nuclear::Engine* engine = (Nuclear::Engine*)Nuclear::GetEngine();
        return engine->GetFileIOManager()->AddTask(pTask);
    }
	return false;
}

void GameApplication::CloseExecutor()
{

}

//进行垃圾回收
void GameApplication::CollectingGCMemory()
{
	if (gGetStateManager() && gGetStateManager()->getGameState() == eGameStateRunning)
	{
		cocos2d::CCScriptEngineManager* pScriptEngine = cocos2d::CCScriptEngineManager::sharedManager();
		if (pScriptEngine && pScriptEngine->getScriptEngine())
		{
			cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->collectMemory();
		}
		if (Nuclear::GetEngine())
		{
			Nuclear::GetEngine()->GCNow();
		}
	}
}


///////////////////////////////////////////////////////////////////////////////////////////

ResolutionAdapter g_adapter;
bool gRunGameApplication()
{
    core::Logger::flurryEvent(L"GameApplication_runGameApplication");

    Nuclear::IApp* pApp = gGetGameApplication();
    
    Nuclear::EngineParameter ep;

    ep.szWindowTitle = (wchar_t*)L"AppTest";
    ep.szClassName = (wchar_t*)L"FireEngineWindow";

	ep.bAsyncRead = true;

	ep.bHasMaximizbox = false;
    ep.bSizeBox = false;
    ep.dwRenderFlags = 0;

	ep.nAppInitStepCount = 8;
    ep.pApp = pApp;

	Nuclear::Engine* pEngine = static_cast<Nuclear::Engine*>(Nuclear::GetEngine());
    pEngine->m_adapter = (Nuclear::iadapter*)&g_adapter;
	
#ifdef WIN7_32
	UpdateEngine::Run();
#endif
	pApp->InitIni();

	pEngine->SetSmoothDeltaLimit(65);//设置时间片最大限??用于调整卡顿之后画面突变的现??
	pEngine->SetLimitFpsTime(40);//设置限每帧时??

#ifdef XP_PERFORMANCE
	float endMemSize = CFileUtil::GetUsedMemory();
#ifdef WIN32
	std::string strOut = "[Mem Dosage] initGL, Start MemUsage: ";
	strOut += " Start MemUsage:";
	strOut += StringCover::floatToString(endMemSize);
	strOut += '\n';
	::OutputDebugStringA(strOut.c_str());
#endif
#endif
	
	LJFM::LJFMOpen::SetLoadFromPak(UpdateEngine::g_uiNoPack != 1);
	LJFM::LJFMOpen::SetVersionDonotCheck(UpdateEngine::g_uiVersionDonotCheck == 1);
	int iResult = LJFM::LJFMOpen::InitFileList();
	if (iResult != 0)
	{
		return false;
	}
	return pEngine->Run(ep);
}

void GameApplication::InitNetModule()
{
	FireNet::GetNetSystem()->Startup();
    ProtoDef::RegisterProtocols();
}
void GameApplication::CleanupNetModule()
{
    ProtoDef::UnregisterProtocols();
	FireNet::GetNetSystem()->Cleanup();
}

void GameApplication::CreateConnection( const char* account, const char* key,const std::wstring& host,const std::wstring& sever,bool bforcelogin,
	const std::wstring &serverName, const std::wstring &areaName, const int serverid, const char* channelId, int type, const std::string& account_suffix,
                                        int ct_type, const std::string& gip, const std::string& gport)
{
	std::string user(account);
	if(account_suffix != "")
	{
		user = user+ "$"+account_suffix;
	}
    
	std::string password(key);

    
	if(m_pNetConnection != NULL)
	{
		delete m_pNetConnection;
		m_pNetConnection = NULL;
	}
    

	int version = 0;
#ifndef WIN32
    if (GetExtendValua(L"VersionDonotCheck") == L"0")
        version = GetVersion();
#endif
	
#ifdef _YJ_SDK_
	std::string  yj_uin = account;
	std::string  yj_pwd = ws2s(gGetLoginManager()->GetYJToken());
	std::string  yj_channelId1 = ws2s(gGetLoginManager()->GetYJChannelId());
	std::string  yj_productCode = ws2s(gGetLoginManager()->GetYJAppId());
	std::string  extparam = yj_channelId1 + "|" + yj_productCode;
#ifdef ANDROID
	LOGE("yj_uin=%s,yj_pwd=%s,yj_channelId1=%s,yj_productCode=%s",yj_uin.c_str(),yj_pwd.c_str(),yj_channelId1.c_str(),yj_productCode.c_str());
#endif

	m_pNetConnection = new  Game::NetConnection(yj_uin, yj_pwd, StringCover::to_string(host), StringCover::to_string(sever), bforcelogin, version, serverName, areaName, serverid, channelId, type, ct_type, gip, gport, extparam);
#else
	m_pNetConnection = new  Game::NetConnection(user, password, StringCover::to_string(host), StringCover::to_string(sever), bforcelogin, version, serverName, areaName, serverid, channelId, type, ct_type, gip, gport,"");
#endif // _YJ_SDK_
	
}

void GameApplication::CreateCrossConnection(const char *account, const char *key, const std::wstring &host, const std::wstring &sever)
{
    if(m_pNetConnection != NULL)
	{
		delete m_pNetConnection;
		m_pNetConnection = NULL;
	}
    
	m_pNetConnection = new  Game::NetConnection(account,key,StringCover::to_string(host),StringCover::to_string(sever),true,0, L"", L"");
}

#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
#include <thread>
#include <mutex>
#include <condition_variable>
#else
#include <pthread.h>
#include <semaphore.h>
#endif

#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
static std::thread* s_voiceThread;
#else
static pthread_t s_voiceThread;
#endif

void GameApplication::DestroyGameNetConnection()
{
    if (m_pNetConnection != NULL) {
        delete m_pNetConnection;
    }
    m_pNetConnection = NULL;
}

int GameApplication::isFirstTimeEnterGame()
{
	std::string filepath = CFileUtil::GetDocDir();
	filepath += USER_INFO;

	char* fileBuf = NULL;
	std::streamsize ss = 0;
	LJXML::LJXML_Doc<LJXML::Char> doc;
	
	if (LoadFromFile(filepath.c_str(), fileBuf, ss))
	{
		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
		{
			delete[] fileBuf;
			return 0;
		}
		LJXML::Char* charData = doc.LoadFromString(fileData);
		delete[] fileBuf;
	}
	else
	{
		return 0;
	}
	if (!doc.first_node())
	{
		return 0;
	}
	LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
	LJXML::LJXML_NodeList typenl;
	root->GetSibNodeList(typenl);
	for (size_t i = 0; i < typenl.size(); i++)
	{
		int64_t uid = _wtoi64(typenl[i]->first_attribute(L"id")->value());
		if (uid == GetMainRoleDataNumValue<int64_t>("roleid"))
		{
			m_nFirstTimeEG = _wtoi(typenl[i]->first_attribute(L"entered")->value());
			if (m_nFirstTimeEG >= 3)
				Nuclear::GetEngine()->SetStepLoadTexture(GameConfigManager::GetInstance()->GetStepLoadTextureAlways());

			return m_nFirstTimeEG;
		}
	}
	return 0;
}

void GameApplication::setFirstTimeEnterGameValue(int flag)
{
	m_nFirstTimeEG = flag;

	std::string filepath = CFileUtil::GetDocDir();
	filepath += USER_INFO;

	char* fileBuf = NULL;
	std::streamsize ss = 0;
	LJXML::LJXML_Doc<LJXML::Char> doc;
	if (LoadFromFile(filepath.c_str(), fileBuf, ss))
	{
		std::wstring fileData;
		LJXMLStringHelper::EncodeLJ codeType;
		if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
		{
			delete[] fileBuf;
			return;
		}
		LJXML::Char* charData = doc.LoadFromString(fileData);

		delete[] fileBuf;
	}
	bool bFind = false;
	LJXML::LJXML_Attr<LJXML::Char> Attr;
	std::wstring enterValue;
	if (doc.first_node())
	{
		LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
		LJXML::LJXML_NodeList typenl;
		root->GetSibNodeList(typenl);
		for (size_t i = 0; i < typenl.size(); i++)
		{
			int64_t uid = _wtoi64(typenl[i]->first_attribute(L"id")->value());
			if (uid == GetMainRoleDataNumValue<int64_t>("roleid"))
			{
				typenl[i]->remove_attribute(typenl[i]->first_attribute(L"entered"));

				Attr.name(L"entered");
				enterValue = StringCover::NumTowstring<int>(m_nFirstTimeEG);
				Attr.value(enterValue.c_str(), enterValue.length());
				typenl[i]->append_attribute(&Attr);

				bFind = true;
				break;
			}
		}
	}
	LJXML::LJXML_Tool<LJXML::Char>::list_type listMemory;
	std::wstring strUser = L"User";
	if (!bFind)
	{
		int64_t uid = GetMainRoleDataNumValue<int64_t>("roleid");
		
		listMemory.push_back(L"id");
		listMemory.push_back(StringCover::NumTowstring(uid));

		listMemory.push_back(L"entered");
		listMemory.push_back(StringCover::NumTowstring(m_nFirstTimeEG));

		auto itBegin = listMemory.begin();
		auto itEnd = listMemory.end();
		LJXML::LJXML_Tool<LJXML::Char>::CreateElement(&doc, strUser, L"", itBegin, itEnd);
	}
	doc.SaveToFile(filepath);
}

void GameApplication::OnXmlBeanReady()
{
    SetXmlBeanReady(true);
    if(GetWaitToEnterWorld())
    {
		int maxNumber = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("SystemSettingNewDlg.GetMaxDisplayPlayerNum");

		fire::pb::CEnterWorld EnterWorldCmd(GetEnterWorldRoleID(), maxNumber);
        gGetNetConnection()->send(EnterWorldCmd);
        
        SetWaitToEnterWorld(false);
        SetEnterWorldRoleID(0);
    }
}

Game::NetConnection* GameApplication::GetGameNetConnection()
{
	return m_pNetConnection;
}

const std::wstring& GameApplication::GetAreaName()
{
	return m_AreaName;
}

const std::wstring& GameApplication::GetServerName()
{
	return m_ServerName;
}

const std::wstring& GameApplication::GetChargeBindHttpAdr()
{
	return m_ChargeBindHttpAdr;
}

const char* GameApplication::GetPlatformChannelID()
{
	return m_platformChannel;
}

bool GameApplication::IsShowFPS()
{
	return m_bShowFPS;
}

bool GameApplication::IsShowUIState()
{
	return m_bShowUIState;
}

bool GameApplication::IsShowmPerformance()
{
	return m_bShowPerformance;
}
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
bool GameApplication::IsUseSDKInWindows()
{
	return m_bUseSDKInWindows;
}
#endif
int64_t GameApplication::GetEnterWorldRoleID()
{
	return m_iEnterWorldRoleID;
}

void GameApplication::SetEnterWorldRoleID(int64_t roleid)
{
	m_iEnterWorldRoleID = roleid;
}

bool GameApplication::GetWaitToEnterWorld()
{
	return m_bWaitToEnterWorld;
}

void GameApplication::SetWaitToEnterWorld(bool flag)
{
	m_bWaitToEnterWorld = flag;
}

bool GameApplication::GetXmlBeanReady()
{
	return m_bXmlBeanReady;
}

void GameApplication::SetXmlBeanReady(bool flag)
{
	m_bXmlBeanReady = flag;
}

const char* GameApplication::getCacheLayoutConfig()
{
	return m_cacheLayout.c_str();
}

GameApplication& GameApplication::GetInstance()
{
	return s_instance;
}

int GameApplication::GetTime()
{
	return m_time;
}

DWORD GameApplication::GetOnLineTime()
{
	return Nuclear::GetMilliSeconds() - m_iEnterTime;
}

eDisplayMode GameApplication::gGetDisPlayMode()
{
	GLint vp[4];
	glGetIntegerv(GL_VIEWPORT, vp);

	if (vp[2] == 1024)
	{
		return eDisplayMode_1024768;
	}
	else if (vp[2] == 1136)
	{
		return eDisplayMode_1136640;
	}
	else
	{
		return eDisplayMode_800600;
	}
}
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
void GameApplication::notifyStartChangeMap()
{
	m_bStartChangeMap = true;
}
#endif
void GameApplication::SetWaitForEnterWorldState(bool bWait)
{
	m_bWaitForEnterWorldMessage = bWait;
}

bool GameApplication::isShowProgressBar()
{
	return m_bFirstLogin;
}

bool GameApplication::IsInLittleGame()
{
	return m_bInLittleGame;
}

void GameApplication::setCollectFrequency(unsigned int frequency)
{
	mCollectFrequency = frequency;
}

bool GameApplication::isFirstEnterGame()
{
	return m_bFirstEnterGame;
}

void GameApplication::SetFirstEnterGame(bool b)
{
	m_bFirstEnterGame = b;
}

void GameApplication::EnableHotApplySysSetting()
{
	m_bHotApplySysSetting = true;
}

bool GameApplication::isHotApplySysSetting()
{
	return m_bHotApplySysSetting;
}

void GameApplication::ToggleDrawUI()
{
	m_bDrawUI = !m_bDrawUI;
}

void GameApplication::ToggleShowName()
{
	m_bDrawName = !m_bDrawName;
}

int GameApplication::GetCurScreenMaxRoleType()
{
	return m_curScreenMaxRoleType;
}

void GameApplication::EnableAutoHideModel(bool bHide)
{
	m_bEnableAutoHideModel = bHide;
}

bool GameApplication::isAutoHideModel()
{
	return m_bEnableAutoHideModel;
}

int GameApplication::GetCurShowMaxRoleNum()
{
	return m_iLastScreenRoleNum;
}

void GameApplication::SetMaxShowRoleNum(int num)
{
	m_iLastScreenRoleNum = num;
}

void GameApplication::SetGACDTips(const std::wstring& tips)
{
	m_gacd_tips = tips;
}

std::wstring GameApplication::GetGACDTips()
{
	return m_gacd_tips;
}

bool GameApplication::isLuaDebugPrint()
{
	return mLuaPrint;
}

bool GameApplication::GetShieldAllAppFunc()
{
	return m_bShieldAllAppFunc;
}

void GameApplication::setDisconnect(bool b)
{
	m_bDisconnect = b;
}

void GameApplication::setTimeoutCount(int count)
{
	m_iTimeoutCount = count;
}

int GameApplication::getTimeoutCount()
{
	return m_iTimeoutCount;
}

void GameApplication::setReconnecting(bool b)
{
	m_bReconnecting = b;
}

bool GameApplication::isReconnecting()
{
	return m_bReconnecting;
}

void GameApplication::setDetachingIME(bool b)
{
	m_bIsDetachingIME = b;
}

std::wstring GameApplication::getCaptureDir() {
	return	MHSD_UTILS::GetDocDir() + L"/capture.jpg";
}

bool GameApplication::IsNormalVer()
{
	return cocos2d::CCImage::IsNormal();
}

// 分享到社交平台
void GameApplication::shareToPlatform(eShareSDK eShareSDK_, eShareType eShareType_, const char* title, const char* text, const char* imgPath, const char* webUrl)
{
    std::string sharePicPath = imgPath;
	std::wstring sharePicNewPath = s2ws(imgPath);
	if (sharePicNewPath.find(L"http") == std::wstring::npos)
	{
		std::wstring newpath = gGetGameUIManager()->GetFullPathFileName(s2ws(imgPath));
		sharePicPath = ws2s(newpath);
	}
#ifdef ANDROID    
	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
		"com/locojoy/sdk/SDKShare",
		"doOnekeyShare",
		"(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
	{
		jstring strTitle = methodInfo.env->NewStringUTF(title);
		jstring strTxt = methodInfo.env->NewStringUTF(text);
		jstring strSharePicPath = methodInfo.env->NewStringUTF(sharePicPath.c_str());
		jstring strWebUrl = methodInfo.env->NewStringUTF(webUrl);
		methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID, (int)eShareSDK_, (int)eShareType_, strTitle, strTxt, strSharePicPath, strWebUrl);
		methodInfo.env->DeleteLocalRef(methodInfo.classID);
		methodInfo.env->DeleteLocalRef(strTitle);
		methodInfo.env->DeleteLocalRef(strTxt);
		methodInfo.env->DeleteLocalRef(strSharePicPath);
		methodInfo.env->DeleteLocalRef(strWebUrl);
	}
#endif

#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    oneKeyShareToPlatform((int)eShareSDK_, (int)eShareType_, title, text, sharePicPath.c_str(), webUrl);
    //const char* imgpath = (CFileUtil::GetDocDir() + "/capture.jpg").c_str();
    //oneKeyShareToPlatform(19, 0, "title", "text", imgpath , "www.baidu.com");
#endif
    
}

//显示MQview
void GameApplication::showMQView()
{
#ifdef _MEIQIA_SDK_
#ifdef ANDROID
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "AndroidShowMQView", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);

		t.env->DeleteLocalRef(t.classID);
	}
#endif
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	IosShowMQView();
#endif
#endif
}

void gGCNow(int level)
{
	gGetGameApplication()->CollectingGCMemory();
}

void gReceiveMemoryWarning()
{
	gGetGameApplication()->CollectingGCMemory();
}

void gSetGCCooldown(int time)
{
	if (Nuclear::GetEngine()) {
		Nuclear::GetEngine()->setGCCooldown(time);
    }
}

long gGetNumberValueByStrKey(const char *key)
{
	return cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("GetNumberValueForStrKey", key);
}


int64_t GetTableNumberFromLua(const char* tableName, int tableId, const char* key)
{
	CallLuaUtil util;
	util.addArg(tableName);
	util.addArg(tableId);
	util.addArg(key);
	if (!util.callLua("GetTableValueFromLua"))
		return -1;
	return util.getRetInt(-1);
}
float GetTableFloatFromLua(const char* tableName, int tableId, const char* key)
{
	CallLuaUtil util;
	util.addArg(tableName);
	util.addArg(tableId);
	util.addArg(key);
	if (!util.callLua("GetTableValueFromLua"))
		return -1;
	return util.getRetFloat(-1);
}
std::string GetTableStringFromLua(const char* tableName, int tableId, const char* key)
{
	CallLuaUtil util;
	util.addArg(tableName);
	util.addArg(tableId);
	util.addArg(key);
	if (util.callLua("GetTableValueFromLua"))
	{
		const char* ret = util.getRetCString("");
		if (ret) {
			return ret;
		}
	}
	return "";
}

std::wstring GetTableWStringFromLua(const char* tableName, int tableId, const char* key)
{
	return s2ws(GetTableStringFromLua(tableName, tableId, key));
}



#ifdef ANDROID
extern "C" {
	jint Java_com_locojoy_mini_mt3_GameApp_nativeGetNotifyEnable(JNIEnv* env, jobject thiz, jint id)
	{
        if (cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine())
        {
            cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(id);
            int ret = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("SystemsettingTuiSongDlg.LoadConfigById", 1);
            return ret;
        }
        return 0;
	}
}
#endif

#ifdef ANDROID
extern "C" {
	jint Java_com_locojoy_mini_mt3_GameApp_nativeSetChannelName(JNIEnv* env, jobject thiz, jstring channelName)
	{
		std::string cName = std::string(MT3::JniHelper::jstring2string(channelName).c_str());
		gSetChannelName(cName);
	}
}
#endif

#ifdef ANDROID
extern "C" {
	jint Java_com_locojoy_mini_mt3_GameApp_nativeSetIsYingYongbao(JNIEnv* env)
	{
		MT3::ChannelManager::setIsYingYongBao(true);
	}
}
#endif

#ifdef ANDROID
extern "C" {
	jint Java_com_locojoy_mini_mt3_GameApp_nativeExitGame(JNIEnv* env)
	{
		MT3::ChannelManager::yj_sdkExit();
	}
}
#endif

#ifdef ANDROID
extern "C" {
	jint Java_com_locojoy_mini_mt3_GameApp_nativeDestroy(JNIEnv* env)
	{
		MT3::ChannelManager::yj_onDestroy();
	}
}
#endif

#ifdef ANDROID
extern "C" {
	void Java_com_locojoy_mini_mt3_GameApp_nativeSendWGInfo(JNIEnv* env)
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("AlertKick");
	}
}
#endif

#ifdef ANDROID
extern "C" {
	void Java_com_locojoy_mini_mt3_GameApp_shareSucceed(JNIEnv* env)
	{
		gGetGameApplication()->ShareResultCallBack(1);
	}
}
#endif

std::string s_bChannelName = "";

void gSetChannelName(std::string channelName)
{
	s_bChannelName = channelName;
}

std::string gGetChannelName()
{
	return s_bChannelName;
}

void GameApplication::SetRecordEnable(bool enable)
{
    m_RecordEnable = enable;
}
            
bool GameApplication::GetRecordEnable()
{
    return m_RecordEnable;
}
            
void GameApplication::SetRecordState(int state)
{
    m_CurRecordState = state;
}
            
int GameApplication::GetRecordState()
{
    return m_CurRecordState;
}
        
void GameApplication::StartRecordForIOS()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    StartRecordToC_Plus();
#endif
}

void GameApplication::StopRecordForIOS()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    StopRecordToC_Plus();
#endif
}

const std::string GameApplication::getPlatformAddress()
{
	std::string chrValue = HttpServerAddressPlatForm;
	if (chrValue == "")
	{
		return "http://mt3.cn.serverlist.locojoy.com:50000/88/";
	}
	else
	{
		return chrValue;
	}
}

const std::string GameApplication::getUserHeadAddress()
{
	std::string chrValue = HttpServerAddressUserHead;
	if (chrValue == "")
	{
		return "124.251.28.28:8800";
	}
	else
	{
		return chrValue;
	}
}

const std::string GameApplication::getBBSAdress()
{
	std::string chrValue = HttpServerAddressBBS;
	if (chrValue == "")
	{
		return "http://bbs.locojoy.com/gameloginatserver.php?";
	}
	else
	{
		return chrValue;
	}
}


const std::string GameApplication::getNewswarnAddress()
{
	std::string chrValue = HttpServerAddressNewWarn;
	if (chrValue == "")
	{
		return "http://42.62.47.211:130/mt3/ios_notice.html";
	}
	else
	{
		return chrValue;
	}
}

const std::string GameApplication::getAndroidDumpAddress()
{
	std::string chrValue = HttpServerAddressAndroidDump;
	if (chrValue == "")
	{
		return "120.26.89.140";
	}
	else
	{
		return chrValue;
	}
}

const std::string GameApplication::getIOSDumpAddress()
{
	std::string chrValue = HttpServerAddressIOSDump;
	if (chrValue == "")
	{
		return "http://120.26.89.140/server/crash_v300.php";
	}
	else
	{
		return chrValue;
	}
}

const std::string GameApplication::getVoiceAddress()
{
	std::string chrValue = HttpServerAddressVoice;
	if (chrValue == "")
	{
		return "http://124.251.28.137/";
	}
	else
	{
		return chrValue;
	}
}

const std::string GameApplication::getDashiAddress()
{
	std::string chrValue = HttpServerAddressDaShi;
	if (chrValue == "")
	{
		return "http://mt3.kfjl.locojoy.com";
	}
	else
	{
		return chrValue;
	}
}

const std::string GameApplication::getSpaceAddress()
{
	std::string chrValue = HttpServerAddressSpace;
	if (chrValue == "")
	{
		return "http://59.151.112.45:8821";
	}
	else
	{
		return chrValue;
	}
}

const std::string GameApplication::getJubaoAddress()
{
	std::string chrValue = HttpServerAddressJubao;
	if (chrValue == "")
	{
		return "http://testot.locojoy.com/api/ChatMonitor/TipOff.ashx";
	}
	else
	{
		return chrValue;
	}
}

const std::string GameApplication::getChatAcessAddress()
{
	std::string chrValue = HttpServerAddressChatAcess;
	if (chrValue == "")
	{
		return "http://testot.locojoy.com/api/ChatMonitor/TipContent.ashx";
	}
	else
	{
		return chrValue;
	}
}

//调用ChartBoostSDK的统计
void GameApplication::gCallChartBoost()
{
#ifdef ANDROID
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "AndroidCallChartBoost", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);

		t.env->DeleteLocalRef(t.classID);
}
#endif
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	IosCallChartBoost();
#endif
}

void GameApplication::gStartWGAnalysis()
{
#ifdef ANDROID
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, "com/locojoy/mini/mt3/GameApp", "StartWGListener", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);

		t.env->DeleteLocalRef(t.classID);
	}
#endif
}


bool GameApplication::isGameInitialized()
{
	return m_bGameInitialized;
}

void GameApplication::SendLocalPushType(int type)
{
	this->m_LocalPushType = type;
}

void GameApplication::CallEvaluate()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
	IosCallEvaluate();
#endif
}

void GameApplication::ShareResultCallBack(int nReslt)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("g_shareResultCallBack", nReslt);

	
}
