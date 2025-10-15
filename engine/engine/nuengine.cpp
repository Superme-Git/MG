#include <algorithm>
#include "../common/nulog.h"
#include "../common/nuxptimeuselog.h"
#include "../sprite/nuspritemanager.h"
#include "../sprite/nusprite.h"
#include "../sprite/nuspinesprite.h"
#include "../effect/nueffectmanager.h"
#include "../effect/nueffect.h"
#include "../world/nuworld.h"
#include "../particlesystem/nuiparticlemanager.h"
#include "nuconfigmanager.h"
#include "nuengine.h"
#include "nuport.h"
#include "../common/nuljfmutil.h"
#include "log/CoreLog.h"

#include "nucocos2d_wraper.h"

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)

#include "FileUtil.h"
#include "Utils.h"

#else

#include "utils/FileUtil.h"
#include "utils/Utils.h"

#endif

#ifdef WIN32
#include "utils/StringCover.h"
#endif


#include "nuenginetimer.h"

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

#include "utils/IOS_Utils.h"

DWORD NL_INFO::s_nProcessId = 0;
wchar_t NL_INFO::s_path[MAX_PATH] = L"info.log";
DWORD NL_ERROR::s_nProcessId = 0;
wchar_t NL_ERROR::s_path[MAX_PATH] = L"error.log";
DWORD NL_SEGMPAK::s_nProcessId = 0;
wchar_t NL_SEGMPAK::s_path[MAX_PATH] = L"segpak.log";


extern bool gIsInBackground();

namespace Nuclear
{
	IEngine* GetEngine()
	{
		return &(Engine::GetInstance());
	}

	bool Engine::SetInfoLogPath(const std::wstring &fn)
	{
		return SetLogPath<NL_INFO>(fn);
	}

	bool Engine::SetErrorLogPath(const std::wstring &fn)
	{
		return SetLogPath<NL_ERROR>(fn);
	}

	bool Engine::SetSegmpakLogPath(const std::wstring &fn)
	{
		return SetLogPath<NL_SEGMPAK>(fn);
	}

	Engine Engine::s_instance;
	int64 Engine::frame_msg = 0;

	// 操作
	Engine::Engine()
	{
		m_n3DSpriteCount = 0;
		m_nAlwaysUpdate3DSpriteCount = 0;
		m_n3DSpriteBatchUpdateCount = 0;
		m_nCurUpdateCount = 0;
		m_nPitch = 0;
		m_fGameTimeSpeedScale = 1.0f;
		m_pLJFMLog = NULL;
		m_pEnv = NULL;
		m_pApp = NULL;
		m_pWorld = NULL; 
		m_pRenderer = NULL; 
		m_pConfigMan = NULL;
		m_iOldColorMode = 32;
		m_iCurColorMode = 32;
		m_tick = Nuclear::GetMilliSeconds();
		m_LastFrameTick = Nuclear::GetMilliSeconds();
		m_uiSmoothDeltaLimit = 0;
		m_uiSmoothDeltaCount = 0;
		m_pFileIOMan = NULL;
		m_pSprMan = NULL;
		m_pAniMan = NULL;
		m_pSpineMan = NULL;
		m_pEffectMan = NULL;
		frame_stat_index = 0;
		frame_task = 0;
		frame_msg = 0;
		m_eshSeed = 0;
		m_bSizeBox = true;
		m_bHasMaximizbox = true;
		m_fBgSoundVolume = 1.0f;
		m_fEffectSoundVolume = 1.0f;
		m_SpriteHandle = INVALID_PICTURE_HANDLE;
		m_AlphaSpriteHandle = INVALID_PICTURE_HANDLE;
		m_bEnableWade = true;
		m_bAsyncRead = false;
		m_bSetInactiveTimer = false;
		m_bIsMultiSampleTypeEnable = true;
		m_windowState = XPWS_ACTIVE;
		m_cleanColor.data = 0xC090;
		m_InitState = 0;
		m_nInitStep = 0;
		m_nAppInitCount = 0;
		m_nSprStepCount = -1;
		m_pPathFinder = NULL;
		m_userTasksTimePF[XPWS_ACTIVE] = 30;
		m_userTasksTimePF[XPWS_INACTIVE] = 30;
		m_userTasksTimePF[XPWS_MINIMIZE] = 30;
		m_3dActionSoundType = 0;
		m_3dEffectSoundType = 0;
		m_n3DSpriteBatchUpdateCount = 10000;
        m_iScreenWidth = 0;
        m_iScreenHeight = 0;

		mGCMemVolume = 250.0f;
		mMustGCMemVolume = 350.0f;
		mAvailableMemVolume = 256.0f;
		mGCMemFPSValue = 20.0f;
		mGetPicNeedFPSValue = 15.0f;
		mMaxTimeIntervalPreFPS = 65;

		mFrameCount = 0;
		mFrameTimeCount = 0;
		mTimeCount = 0;
		mFps = 0.0f;
		mDeltaTime = 0;
		mCurDeltaTime = 0;
		mRealFrame = true;
		mCurMemSize = 0.0f;
		mCurAvailableMemSize = 0.0f;
		mExcCounter = 0;
		m_engineLayer = NULL;

		srand(m_tick);
	}

	Engine::~Engine()
	{
	}

	Renderer* Engine::GetRenderer( )
	{
		return m_pRenderer;
	}

	Engine::RestoreTask::~RestoreTask()
	{
	}

	void Engine::RestoreTask::Run()
	{
		Engine::GetInstance().OnRendererRestore();
	}

	void Engine::OnRendererRestore()
	{
		if( m_pWorld )
			m_pWorld->OnRendererRestore();
		m_SpriteHandle = INVALID_PICTURE_HANDLE;
		m_AlphaSpriteHandle = INVALID_PICTURE_HANDLE;
	}

	bool Engine::Init(int step)
	{
		GLint vp[4];
		glGetIntegerv(GL_VIEWPORT, vp);

		m_iScreenWidth = vp[2];
		m_iScreenHeight = vp[3];

		switch (step)
		{
		case XPEIS_SOURCE_CONFIG_CURSOR_MAN:
			m_pSystemResourceMan = new SystemResourceManager(m_pRenderer);
			m_pSystemResourceMan->Init();
			XPLOG_INFO(L"----------显卡摘要信息------------\n");
			XPLOG_INFO(L"%s\n", m_pSystemResourceMan->GetCurDisplayDevice().c_str());
			XPLOG_INFO(L"----------显卡摘要信息------------\n");

			//获取当前设备总内存量
			cocos2d::CCImage::SetTotalPhysMemory(int(CFileUtil::GetTotalMemory() * 1024));

			m_pConfigMan = new ConfigManager(this);
			XPLOG_INFO(L"  配置管理器器创建成功\n");
			m_pConfigMan->Init();

			m_pConfigMan->SetConsoleInfo(XPCSINFO_ALL, true);
			m_pConfigMan->SetFrameStateInfo(XPFRAMESTAT_FRAME_TIME, true);
			m_pConfigMan->SetFrameStateInfo(XPFRAMESTAT_CAMERA_DIS, true);
			m_pConfigMan->SetFrameStateInfo(XPFRAMESTAT_FILE_IO, true);
			m_pConfigMan->SetFrameStateColor(XPFRAMESTAT_FRAME_TIME, 0xffffff00);
			m_pConfigMan->SetFrameStateColor(XPFRAMESTAT_CAMERA_DIS, 0xffff0000);
			m_pConfigMan->SetFrameStateColor(XPFRAMESTAT_FILE_IO, 0xff00ff00);
			m_pConfigMan->SetFrameStateColor(XPFRAMESTAT_TASK_TIME, 0xffff00ff);
			m_pConfigMan->SetFrameStateColor(XPFRAMESTAT_IAPP_MSG, 0xffffffff);

			return true;

		case XPEIS_ENV_SOUND_ANI_MAN:
            MHSD_UTILS::flurryEvent(L"Engine_AniManager_init_start");
			m_pAniMan = new AniManager(this);
			if( !m_pAniMan->Init() ) 
			{
                MHSD_UTILS::flurryEvent(L"Engine_AniManager_init_fail");
                assert(false && "animgr init fail");
				return false;
			}
			m_pSpineMan = new SpineManager(this);
XPLOG_INFO(L"  动画管理器初始化成功\n");
            MHSD_UTILS::flurryEvent(L"Engine_AniManager_success");
			return true;
		case XPEIS_SPRITE_PRE_INIT:
			m_pSprMan = new SpriteManager(this);
			if (m_pSprMan->PreInit())
			{
				m_nSprStepCount = (m_pSprMan->GetInitCount() + 63) >> 6;//64个一批
				return true;
			}
            XPLOG_INFO(L" spite preinit false \n");
            return true;
		default:
			break;
		}
		static bool sprInitResult = true;
		if (step - XPEIS_SPRITE_INITING < m_nSprStepCount)
		{
			ULONG64 result = m_pSprMan->Init((step - XPEIS_SPRITE_INITING) << 6, 64);
			if (result == 0)
				return true;
			sprInitResult = false;
			return true;
		}
		if (!sprInitResult)
		{
			sprInitResult = true;
		}
		step -= m_nSprStepCount - 1;//这个1是因为XPEIS_SPRITE_INITING也是一步啊……
		switch (step)
		{
		case XPEIS_SPRITE_POST_INIT:
            MHSD_UTILS::flurryEvent(L"Engine_SpriteMgr_postInit_start");
			if (m_pSprMan->PostInit())
			{
                MHSD_UTILS::flurryEvent(L"Engine_SpriteMgr_postInit_success");
				return true;
			}
            MHSD_UTILS::flurryEvent(L"Engine_SpriteMgr_postInit_fail");
			return false;

		case XPEIS_EFFECT_QUERY_STEPSOUND:
			m_pEffectMan = new EffectManager(this);
			m_pEffectMan->PreInit();
			return true;
		case XPEIS_INPUT_WORLD:
			m_pWorld = new World(this, m_adapter->get_logic_w(), m_adapter->get_logic_h());


			if( !m_pWorld->Init() ) 
			{
				XPLOG_INFO(L" world init fail \n");
			}

			m_nInitStep = 0;
			m_InitState = 1;
			return true;
		}

        MHSD_UTILS::flurryEvent(L"Engine_init_fail");
		return false;
	}

	bool Engine::Run(const EngineParameter &ep) 
	{
#ifdef XP_PERFORMANCE
		double beginMemSize = CFileUtil::GetUsedMemory();
#endif
		MHSD_UTILS::flurryEvent(L"Engine_run");
		XPLOG_INFO_CHECK();
		XPLOG_ERROR_CHECK();
		XPLOG_SEGMPAK_CHECK();
		XPLOG_FRAME_STATE_CHECK();
		XPLOG_BIG_FRAME_STATE_CHECK();
		XPLOG_FULL_FRAME_STATE_CHECK();
		XPLOG_INFO(L"引擎开始启动...\n");
		m_renderFlag = ep.dwRenderFlags;
		m_bEnableMipMap = ep.bEnableMipMap;
		if( ep.pApp == NULL )
			return false;
        
		m_pApp = ep.pApp;
		m_nAppInitCount = ep.nAppInitStepCount;
		m_InitState = 0;
		m_nInitStep = 0;
		m_nSprStepCount = -1;

		m_pPathFinder = new astar::PathFinder();

		m_pFileIOMan = new NuclearFileIOManager();
        MHSD_UTILS::flurryEvent(L"Engine_FileIOManager_init_start");
		if( !m_pFileIOMan->Init(false, ep.bApplictionInBuild))
		{
            MHSD_UTILS::flurryEvent(L"Engine_FileIOManager_init_fail");
#ifdef ANDROID
           //GetChannelPlatformInterface()->GameResLoadError();//shield by yyy
#else
			IOS_MHSD_UTILS::GameResLoadError();
#endif
			return false;
		}
        MHSD_UTILS::flurryEvent(L"Engine_FileIOManager_init_success");
        LOGD("fileio init finish!");

#ifndef ANDROID
		//设置Log的路径
		{
			std::wstring tmp = m_pFileIOMan->GetCurrentDirectory() + L"\\" + NL_INFO::s_path;
			SetLogPath<NL_INFO>(tmp);
			tmp = m_pFileIOMan->GetCurrentDirectory() + L"\\" + NL_ERROR::s_path;
			SetLogPath<NL_ERROR>(tmp);
			tmp = m_pFileIOMan->GetCurrentDirectory() + L"\\" + NL_SEGMPAK::s_path;
			SetLogPath<NL_SEGMPAK>(tmp);
			XPLOG_INFO_CHECK();
			XPLOG_ERROR_CHECK();
			XPLOG_SEGMPAK_CHECK();
		}
#endif
		m_bAsyncRead = ep.bAsyncRead;
        static EngineApp app(this);
#if (defined WIN7_32) || (CC_TARGET_PLATFORM == CC_PLATFORM_WP8) 
        //cocos2d::CCApplication::sharedApplication().run();
#else
        cocos2d::CCApplication::sharedApplication().run();
#endif
		LOGD("引擎LJFM初始化成功");

		m_bSizeBox = ep.bSizeBox;
		m_bHasMaximizbox = ep.bHasMaximizbox;
        
        GLint vp[4];
        glGetIntegerv(GL_VIEWPORT, vp);
		m_iWidth = vp[2];//mode.width;
		m_iHeight= vp[3];//mode.height;
        
        m_adapter->init();
        MHSD_UTILS::flurryEvent(L"Engine_XPCreateRender_init");
		XPCREATE_RENDERER_RESULT renderResult = CreateRenderer(&m_pRenderer, ep.dmode, m_pFileIOMan, ep.dwRenderFlags, XPRV_COCOS2D, ep.multiSampleType);
		switch( renderResult )
		{
		case XPCRR_SHADER_VERSION:
			MHSD_UTILS::flurryEvent(L"Engine_XPCreateRender_init_error_shaderVersion");
            return false;
			break;
		case XPCRR_OK:
            MHSD_UTILS::flurryEvent(L"Engine_XPCreateRender_init_success");
			break;
		default:
            MHSD_UTILS::flurryEvent(L"Engine_XPCreateRender_init_error_default");
			return false;
			break;
		}
		m_pRenderer->SetAutoTestDevice(false);
		m_pRenderer->SetRestoreTask(&m_restoreTask);

		LOGD("渲染器创建成功");

#ifdef XP_PERFORMANCE
		double endMemSize = CFileUtil::GetUsedMemory();
		float deltaMemSize = endMemSize - beginMemSize;
#ifdef WIN32
		std::string strOut = "[Mem Dosage] Engine::Run: ";
		strOut += StringCover::floatToString(deltaMemSize);
		strOut += " Total MemUsage:";
		strOut += StringCover::floatToString(endMemSize);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
#endif

#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8) 
		cocos2d::CCApplication::sharedApplication()->run();
#elif (defined WIN7_32)
		cocos2d::CCApplication::sharedApplication().run();
#endif     

#ifndef WIN7_32
		return true;
#endif

		return Exit();
	}	

	bool Engine::Exit()
	{ 
		if (!m_bUserWnd)
		{
			m_userTasks.clear();
			m_userTasksList.clear();
#ifdef WIN7_32
			m_pApp->OnExit();
#endif
			if (m_SpriteHandle != INVALID_PICTURE_HANDLE)
				m_pRenderer->FreeRenderTarget(m_SpriteHandle);
			m_SpriteHandle = INVALID_PICTURE_HANDLE;

			if (m_AlphaSpriteHandle != INVALID_PICTURE_HANDLE)
				m_pRenderer->FreeRenderTarget(m_AlphaSpriteHandle);
			m_AlphaSpriteHandle = INVALID_PICTURE_HANDLE;

			EngineSpriteMap::iterator sprit = m_engineSprites.begin(), sprie = m_engineSprites.end();
			for (; sprit != sprie; ++sprit)
			{
				delete sprit->second;
			}
			m_engineSprites.clear();

			m_willBeDeleteEffects.clear();
			EffectSet::iterator it = m_effects.begin(), ie = m_effects.end();
			for (; it != ie; ++it)
			{
				delete *it;
			}
			m_effects.clear();

			std::map<std::wstring, Effect*>::iterator hIt = m_holdEffects.begin(), hIe = m_holdEffects.end();
			for (; hIt != hIe; ++hIt)
			{
				delete hIt->second;
			}
			m_holdEffects.clear();

			XPSAFE_DELETE(m_pWorld);
			XPSAFE_DELETE(m_pEffectMan);
			XPSAFE_DELETE(m_pSprMan);				// yeqing 2015-10-19 去掉注释
			XPSAFE_DELETE(m_pAniMan);
			XPSAFE_DELETE(m_pSpineMan);

			XPSAFE_DELETE(m_pEnv);
			XPSAFE_DELETE(m_pConfigMan);
			XPSAFE_DELETE(m_pSystemResourceMan);	// yeqing 2015-10-19 去掉注释

			DestroyRenderer(m_pRenderer);
			m_pRenderer = NULL;
			m_pFileIOMan->Destroy();				// yeqing 2015-10-19 去掉注释
			XPSAFE_DELETE(m_pFileIOMan);			// yeqing 2015-10-19 去掉注释

			LJFM::LJFMOpen::SetOutLogInstance(NULL);
			XPSAFE_DELETE(m_pLJFMLog);
			XPSAFE_DELETE(m_pPathFinder);

			LOGD("引擎退出");
		}
		return true;
	}

	void Engine::SetAniPicGCTime(int t)
	{
        if(m_pAniMan)
        {
			m_pAniMan->SetAniPicGCTime(t);
        }
	}

	int Engine::GetAniPicGCTime() const
	{
        if(m_pAniMan)
        {
			return m_pAniMan->GetAniPicGCTime();
        }
        return 0;
	}

	void Engine::SetAniXapGCTime(int t)
	{
        if(m_pAniMan)
        {
			m_pAniMan->SetAniXapGCTime(t);
        }
	}

	int Engine::GetAniXapGCTime() const
	{
        if(m_pAniMan)
        {
			return m_pAniMan->GetAniXapGCTime();
        }
        return 0;
	}

	XPTimeUseLog g_timeuserlog;

	void Engine::SetSmoothDeltaLimit(unsigned int uiSmoothDeltaLimit)
	{
		if (m_uiSmoothDeltaLimit > 0 && uiSmoothDeltaLimit == 0)
		{
			SetSmoothDeltaCount(0);
		}
		m_uiSmoothDeltaLimit = uiSmoothDeltaLimit;
	}

	unsigned int Engine::GetSmoothDeltaLimit()
	{
		return m_uiSmoothDeltaLimit;
	}
	void Engine::SetSmoothDeltaCount(unsigned int uiSmoothDeltaCount)
	{
		m_uiSmoothDeltaCount = uiSmoothDeltaCount;
	}
	unsigned int Engine::GetSmoothDeltaCount()
	{
		return m_uiSmoothDeltaCount;
	}

	void Engine::MainLoop()
	{
#ifdef XP_PERFORMANCE
		static int64_t accumulateTime = 0;
		int64_t startTick = Nuclear::GetMilliSeconds();
		static bool bStatisticsMem = false;
		if (!bStatisticsMem)
		{
			float dMemSize = CFileUtil::GetUsedMemory();
	#ifdef WIN32
			std::string strOut = "[Mem Dosage] MainLoop Start: ";
			strOut += StringCover::floatToString(dMemSize);
			strOut += '\n';
			::OutputDebugStringA(strOut.c_str());
	#endif
			bStatisticsMem = true;
		}
#endif
		m_timerManager.Tick();

		unsigned int tick = Nuclear::GetMilliSeconds();
		int realDelta = tick - m_LastFrameTick;
		if (realDelta <= 0) realDelta = 1;
		mCurDeltaTime = realDelta;
		m_LastFrameTick = tick;
		mTimeCount += mCurDeltaTime;
		mDeltaTime = mCurDeltaTime;

		if (mRealFrame)
		{
			if (mDeltaTime >= m_uiSmoothDeltaLimit)
			{
				//World* world = static_cast<World*>(GetEngine()->GetWorld());
				//eNuclearWorldMode worldMode = world->GetWorldMode();
				//bool bIsInBattle = worldMode == XPWM_SIMPLE_BATTLE || worldMode == XPWM_WORLD_BATTLE;
				//if (!bIsInBattle)
					mDeltaTime = m_uiSmoothDeltaLimit;
			}
			mDeltaTime *= m_fGameTimeSpeedScale;
		}

		m_tick += mDeltaTime;

		RunUserTask();

		m_pFileIOMan->OnUpdate(((__int64)m_pSystemResourceMan->GetCPUFrequency()) * 1000);

		if (mRealFrame)
		{
			m_pEffectMan->OnTick();
			UpdateSpriteAction(mDeltaTime);
			m_pApp->OnTick(m_tick, mDeltaTime, realDelta);
			m_pWorld->OnTick(m_tick, mDeltaTime, realDelta);

			EffectSet::iterator it = m_willBeDeleteEffects.begin(), ie = m_willBeDeleteEffects.end();
			if (it != ie)
			{
				for (; it != ie; ++it)
				{
					delete *it;
					m_effects.erase(*it);
				}
				m_willBeDeleteEffects.clear();
			}

			it = m_effects.begin(), ie = m_effects.end();
			for (; it != ie;)
			{
				(*(it++))->Update(mDeltaTime);
			}

			UpdateBeforeRender(mDeltaTime);
		}
		else
		{
			UpdateSpriteAction(mDeltaTime);
			EffectSet::iterator it = m_effects.begin(), ie = m_effects.end();
			for (; it != ie;)
			{
				(*(it++))->Update(mDeltaTime);
			}
		}

		if (m_pAniMan)
		{
			m_pAniMan->Run(mDeltaTime);

			++frame_stat_index;
#ifdef ANDROID
			if (mFps > mGCMemFPSValue && (frame_stat_index & 0xF) == 0)
#else
			if (mFps > mGCMemFPSValue && mCurMemSize > mGCMemVolume && (frame_stat_index & 0xF) == 0)
#endif
			{
				m_pAniMan->GarbageCollection(m_tick);//每15帧 GC一次
			}

			if (mRealFrame)
			{
				m_pAniMan->Update();
			}
		}
#ifdef XP_PERFORMANCE
		static int64_t mainLoopTimePerSecond = 0;
		int64_t deltaTick = Nuclear::GetMilliSeconds() - startTick;
		accumulateTime += realDelta;
		mainLoopTimePerSecond += deltaTick;
		if (accumulateTime >= 1000)
		{
			Nuclear::Renderer *pRenderer = Nuclear::GetEngine()->GetRenderer();
			Nuclear::RenderPerformance& rp = pRenderer->GetPerformance();
			rp.m_iUpdateTimePerSecond = mainLoopTimePerSecond;
			accumulateTime = 0;
			mainLoopTimePerSecond = 0;
		}
#endif
	}

	// 如果是用户自己创建的窗口，那么需要在窗口函数中调用此函数
	bool Engine::OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam, void* pAdditionalParam/* = NULL*/)
	{ 
		if (GetApp())
		{
			NuclearWinMSGFuncResult result = 0;
			result = GetApp()->OnWindowsMessage(hWnd, msg, wParam, lParam, pAdditionalParam);
			if (result & XPWINMSG_INTERCEPT)
				return true;
		}
		return false; 
	}

	void Engine::InitNotify()
	{
		static int lasttick = 0;
		m_tick = Nuclear::GetMilliSeconds();
		int step = m_nInitStep;
		int count = (m_nSprStepCount<0)?-1:(m_nSprStepCount + XPEIS_INPUT_WORLD + m_nAppInitCount);
		if (m_InitState == 1)
		{
			step += m_nSprStepCount + XPEIS_INPUT_WORLD;
		}

#ifdef XP_PERFORMANCE
		static bool bStatistics = false;
		float beginMemSize = CFileUtil::GetUsedMemory();
		if (!bStatistics)
		{
#ifdef WIN32
			std::string strOut = "[Mem Dosage] OnRenderInit Start: ";
			strOut += StringCover::floatToString(beginMemSize);
			strOut += '\n';
			::OutputDebugStringA(strOut.c_str());
#endif
		}
#endif
		m_pApp->OnRenderInit(m_tick, step, count);
#ifdef XP_PERFORMANCE
		if (!bStatistics)
		{
			float endMemSize = CFileUtil::GetUsedMemory();
			float deltaMemSize = endMemSize - beginMemSize;
#ifdef WIN32
			std::string strOut = "[Mem Dosage] OnRenderInit End: ";
			strOut += StringCover::floatToString(deltaMemSize);
			strOut += " step:";
			strOut += StringCover::intToString(step);
			strOut += " Total MemUsage:";
			strOut += StringCover::floatToString(endMemSize);
			strOut += '\n';
			::OutputDebugStringA(strOut.c_str());
#endif
			bStatistics = true;
		}
#endif
		lasttick = m_tick;
	}

	// 如果是用户自己创建的窗口，那么需要在程序空闲(OnIdle)时中调用此函数
	void Engine::OnIdle() 
	{ 
		if (m_InitState < 2 && gIsInBackground())
		{
			return;
		}

#ifdef XP_PERFORMANCE
		static bool bStatisticsMem = false;
		if (!bStatisticsMem)
		{
			float dMemSize = CFileUtil::GetUsedMemory();
#ifdef WIN32
			std::string strOut = "[Mem Dosage] Engine::OnIdle Start: ";
			strOut += StringCover::floatToString(dMemSize);
			strOut += '\n';
			::OutputDebugStringA(strOut.c_str());
#endif
			bStatisticsMem = true;
		}
#endif
		switch (m_InitState)
		{
            case 0:
                {
                    char buffer[50];
                    sprintf(buffer,"Engine_OnIdle_InitState_%d_InitStep_%d",m_InitState,m_nInitStep);
                    if (!Init(m_nInitStep++))
                    {
                        sprintf(buffer,"Engine_OnIdle_InitState_%d_InitStep_%d_fail",m_InitState,m_nInitStep);
                    }
                   std::wstring wsw = s2ws(buffer);
                    MHSD_UTILS::flurryEvent(wsw);
                }
			break;
		case 1:
			if (m_nInitStep < m_nAppInitCount)
			{
                char buffer[50];
                sprintf(buffer,"Engine_OnIdle_InitState_%d_InitStep_%d",m_InitState,m_nInitStep);
				LOGD("enter engine app init. Step:%d Total:%d", m_nInitStep, m_nAppInitCount);
				if (!m_pApp->OnInit(m_nInitStep++))
				{
                    sprintf(buffer, "Engine_OnIdle_InitState_%d_InitStep_%d_fail",m_InitState,m_nInitStep);
                    assert(false);
				}
                std::wstring wsw = s2ws(buffer);
                MHSD_UTILS::flurryEvent(wsw);
				break;
			}
			m_InitState = 2;
			//不用break了
		case 2:
			MainLoop();
			break;
		}
	}
    
    const wchar_t* g_dirStr[XPDIR_COUNT] = {
        L"top",
        L"top right",
        L"right", 
        L"btm right",
        L"bottom",
        L"btm left",
        L"left",
        L"top left",
    };
    NuclearDirection GetDirFromString(const wchar_t* aStrDir);
    NuclearDirection GetDirFromString(const wchar_t* aStrDir)
    {
        for(int i = 0; i < XPDIR_COUNT; i++)
        {
            if (wcscmp(g_dirStr[i], aStrDir) == 0) {
                return NuclearDirection(i);
            }
        }
        
        return XPDIR_TOPLEFT;
    }
    
	void Engine::UpdateBeforeRender(int delta)
	{
		m_pWorld->UpdateBeforeRender(delta);
		g_timeuserlog.Push(XPTimeUseLog::TUL_WORLDBEFORERENDER);
		m_pWorld->SortMid();
		g_timeuserlog.Push( XPTimeUseLog::TUL_SORTMID);
	}

	void Engine::UpdateSpriteAction(DWORD delta)
	{
		m_pWorld->UpdateSpriteAction(delta);
		EngineSpriteMap::iterator it = m_engineSprites.begin(), ie = m_engineSprites.end();
		for (;it!=ie;++it)
		{
			it->second->Update(delta);
			it->second->UpdateAction(delta);
		}
	}

	void Engine::Render(bool controlFPS)
	{
		if (!m_pRenderer) return;
#ifdef XP_PERFORMANCE
		static int64_t accumulateTime = 0;
		int64_t startTick = Nuclear::GetMilliSeconds();
#endif

		//统计帧率
		mFrameCount++;
		static unsigned int nCouter = 0;
		if (mTimeCount >= 1000)
		{
			mFps = mFrameCount * 1000.0f / mTimeCount;  // ycl make FPS correct
			mTimeCount = 0;
			mFrameCount = 0;
			if (nCouter % 3 == 0)//每3秒统计一次
			{
				GetCurMemInfo();
			}

			nCouter++;

			mExcCounter = core::Thread::m_iFireCounter;
			core::Thread::m_iFireCounter = 0;
		}
		//统计帧率结束

		if( m_pWorld != NULL )
		{
			CCLOGERROR("engine=%x world=%x\n",this,m_pWorld);
			m_pWorld->Render(m_tick, true, true/*mRealFrame*/);
		}

		m_pApp->OnRenderUI(m_tick, true/*mRealFrame*/);
        
		// ycl 公司 logo 图片最后绘制，以保证显示在前面
        if (m_InitState < 2)
        {
            InitNotify();
        }

#ifdef XP_PERFORMANCE
		static int64_t renderTimePerSecond = 0;
		int64_t deltaTick = Nuclear::GetMilliSeconds() - startTick;
		accumulateTime += mCurDeltaTime;
		renderTimePerSecond += deltaTick;
		if (accumulateTime >= 1000)
		{
			Nuclear::Renderer *pRenderer = Nuclear::GetEngine()->GetRenderer();
			Nuclear::RenderPerformance& rp = pRenderer->GetPerformance();
			rp.m_iRenderTimePerSecond = renderTimePerSecond;
			accumulateTime = 0;
			renderTimePerSecond = 0;
		}
#endif
	}

	void Engine::OnTimer(int iPeriod)
	{		
		if( m_pSystemResourceMan != NULL )
			m_pSystemResourceMan->OnTimer(iPeriod);

		if( m_pConfigMan != NULL )
			m_pConfigMan->OnTimer(iPeriod);

		if( m_pWorld )
			m_pWorld->OnTimer(iPeriod);

		if( m_pFileIOMan )
			m_pFileIOMan->OnTimer(iPeriod);
	}


	void Engine::RunUserTask()
	{
		RannableVect::iterator vIt, vIe;
		m_lckTask.Lock();
		vIt = m_userTasks.begin(), vIe = m_userTasks.end();
		for (;vIt!=vIe;++vIt)
		{
			m_userTasksList.push_back(*vIt);
		}
		m_userTasks.clear();
		m_lckTask.Unlock();

        const int MAX_TASK_CNT_PER_FRAME = 5;
        int task_cnt = 0;
		while (!m_userTasksList.empty()  && task_cnt <= MAX_TASK_CNT_PER_FRAME)
		{
			NuclearHardRef<INuclearRunnable> runable = m_userTasksList.front();
			m_userTasksList.pop_front();
			runable->Run();
            task_cnt++;
		}
	}

	void Engine::StopRunUserTask()
	{
		m_lckTask.Lock();
		m_userTasks.clear();
		m_lckTask.Unlock();
		m_userTasksList.clear();
	}

	void Engine::PutTask(INuclearRunnable *task)
	{
		if( task == NULL ) return;
		m_lckTask.Lock();
		m_userTasks.push_back(NuclearHardRef<INuclearRunnable>(task));
		m_lckTask.Unlock();
	}

    void TimerCallBack(void* idEvent)
    {
        ((INuclearTimer*)idEvent)->OnTimer();
    }

	bool Engine::ScheduleTimer(INuclearTimer * timer, int period/* ms */)
	{
		if( timer == NULL || period < 50 || period > 3600*1000) 
			return false;
        
        if( timer->m_iTimerID != 0)
            CancelTimer(timer);
        timer->m_iTimerID = m_timerManager.SetTimer(period, TimerCallBack, timer);
        return timer->m_iTimerID != 0;
	}

	bool Engine::CancelTimer(INuclearTimer * timer)
	{
		if( timer == NULL ) return false;
        return m_timerManager.KillTimer(timer->m_iTimerID);
	}

	void Engine::EnableParticle(bool flag)
	{
		if (GetRenderer() && GetRenderer()->GetParticleManager())
		{
			GetRenderer()->GetParticleManager()->SetEnabled(flag);
		}
	}

	bool Engine::GetWaterDepth(const NuclearLocation& pt, unsigned int &nDepth) const
	{
		if ((!m_pWorld) || (!m_bEnableWade))
		{
			nDepth = 0;
			return true;
		}
		return m_pWorld->GetWaterDepth(pt, nDepth);
	}

	void Engine::GetSpriteShadowParam(float &shearX, float &scalingY) const
	{
		if (!m_pWorld)
			return;
		m_pWorld->GetSpriteShadowParam(shearX, scalingY);
	}

	inline const Sprite* Engine::CheckEngineSpriteIncludeAttached(EngineSpriteHandle handle) const
	{
		EngineSpriteMap::const_iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
			return iter->second;
		iter = m_attachedSprites.find(handle);
		if (iter != m_attachedSprites.end())
			return iter->second;
		return NULL;
	}

	inline const Sprite* Engine::CheckEngineSprite(EngineSpriteHandle handle) const
	{
		EngineSpriteMap::const_iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
			return iter->second;
		return NULL;
	}

	inline Sprite* Engine::CheckEngineSpriteIncludeAttached(EngineSpriteHandle handle)
	{
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
			return iter->second;
		iter = m_attachedSprites.find(handle);
		if (iter != m_attachedSprites.end())
			return iter->second;
		return NULL;
	}

	inline Sprite* Engine::CheckEngineSprite(EngineSpriteHandle handle)
	{
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
			return iter->second;
		return NULL;
	}

	Sprite* Engine::FindAndAddEngineSprite(EngineSpriteHandle handle)
	{
		return CheckEngineSpriteIncludeAttached(handle);
	}

	EngineSpriteHandle Engine::CreateEngineSprite(const std::wstring &modelname, bool async, bool isUISprite)
	{
		Sprite *pSprite = NULL;
		int nType = m_pSprMan->GetModelTypeByModelName(modelname);
		if (nType == 1)//Spine精灵
		{
			pSprite = new SpineSprite(this, modelname);
			if (isUISprite)
				((SpineSprite*)pSprite)->SetIsUISprite(isUISprite);
		}
		else//系列帧精灵
		{
			pSprite = new Sprite(this, modelname);
		}

		if (pSprite)
		{
			pSprite->SetModel(modelname, async);
			pSprite->SetScreenCoord(true);
			pSprite->EnableShadow(false);
			m_engineSprites[++m_eshSeed] = pSprite;
			return m_eshSeed;
		}
		return INVALID_ENGINE_SPRITE_HANDLE;
	}

	bool Engine::SetEngineSpriteModel(EngineSpriteHandle handle, const std::wstring &modelname, bool async)
	{
		Sprite *pSprite = CheckEngineSpriteIncludeAttached(handle);
		if (pSprite)
		{
			return pSprite->SetModel(modelname, async);
		}
		return false;
	}

	void Engine::ReleaseEngineSprite(EngineSpriteHandle handle)
	{
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
		{
			delete iter->second;
			m_engineSprites.erase(iter);
		}
	}

	void Engine::RendererEngineSprite(EngineSpriteHandle handle)
	{
		Sprite *pSprite = CheckEngineSprite(handle);
		if (pSprite)
		{
			pSprite->Render(NULL, true);
		}
	}

	void Engine::SetEngineSpriteLoc(EngineSpriteHandle handle, const NuclearLocation& loc)
	{
		Sprite *pSprite = CheckEngineSprite(handle);
		if (pSprite)
		{
			pSprite->SetLocation(loc);
		}
	}

	void Engine::SetEngineSpriteDirection(EngineSpriteHandle handle, NuclearDirection dir)
	{
		Sprite *pSprite = CheckEngineSprite(handle);
		if (pSprite)
		{
			pSprite->SetDirection(dir);
		}
	}

	void Engine::SetEngineSpriteDirection(EngineSpriteHandle handle, const NuclearPoint &target)
	{
		Sprite *pSprite = CheckEngineSprite(handle);
		if (pSprite)
		{
			pSprite->SetDirection(target.x, target.y);
		}
	}

	void Engine::SetEngineSpriteDefaultAction(EngineSpriteHandle handle, const std::wstring &actname)
	{
		Sprite *pSprite = CheckEngineSpriteIncludeAttached(handle);
		if (pSprite)
		{
			pSprite->ReleaseAction(pSprite->GetDefaultAction());
			pSprite->HoldAction(actname);
			pSprite->SetDefaultAction(actname, XPSALT_SYNC, 1.0f, false);
		}
	}

	void Engine::SetEngineSpriteAction(EngineSpriteHandle handle, const std::wstring &actname)
	{
		Sprite *pSprite = CheckEngineSpriteIncludeAttached(handle);
		if (pSprite)
		{
			pSprite->PlayAction(actname, XPSALT_SYNC, 1.0f);
		}
	}

	void Engine::SetEngineSpriteComponent(EngineSpriteHandle handle, int scid, const std::wstring& resource, Nuclear::NuclearColor color)
	{
		Sprite *pSprite = CheckEngineSpriteIncludeAttached(handle);
		if (pSprite)
		{
			pSprite->SetComponent(scid, resource, color);
		}
	}

	void Engine::EnableEngineSpriteShadow(EngineSpriteHandle handle, bool b)
	{
		Sprite *pSprite = CheckEngineSprite(handle);
		if (pSprite)
		{
			pSprite->EnableShadow(b);
		}
	}

	void Engine::SetEngineSpriteScale(EngineSpriteHandle handle, float scale)
	{
		Sprite *pSprite = CheckEngineSpriteIncludeAttached(handle);
		if (pSprite)
		{
			pSprite->SetScale(scale);
		}
	}

	void Engine::SetEngineSpriteAlpha(EngineSpriteHandle handle, unsigned char alpha)
	{
		Sprite *pSprite = CheckEngineSpriteIncludeAttached(handle);
		if (pSprite)
		{
			pSprite->SetAlpha(alpha);
		}
	}

	// yeqing 2015-11-07
	bool Engine::GetEngineSpriteActionTimeByName(EngineSpriteHandle handle, const std::wstring& action_name, float &time)
	{
		Sprite *pSprite = CheckEngineSprite(handle);
		if (pSprite)
		{
			return pSprite->GetActionTimeByName(action_name, time);
		}
		return false;
	}

	// 设置和取消持续效果，需要手工取消，个数无上限
	IEffect* Engine::SetEngineSpriteDurativeEffect(EngineSpriteHandle handle, const std::wstring &effect_name, const NuclearPoint& relpos, unsigned int flag)
	{
		Sprite *pSprite = CheckEngineSpriteIncludeAttached(handle);
		if (pSprite)
		{
			return pSprite->SetDurativeEffect(effect_name, relpos, flag);
		}
		return NULL;
	}
	void Engine::RemoveEngineSpriteDurativeEffect(EngineSpriteHandle handle, IEffect* pEffect)
	{
		Sprite *pSprite = CheckEngineSpriteIncludeAttached(handle);
		if (pSprite)
		{
			pSprite->RemoveDurativeEffect(pEffect);
		}
	}
	// 临时效果，播放完毕自动取消，个数无上限, times是播放次数, 如果有声音，soundtype就是其声音类型
	IEffect* Engine::PlayEngineSpriteEffect(EngineSpriteHandle handle, const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag, unsigned char soundtype)
	{
		Sprite *pSprite = CheckEngineSpriteIncludeAttached(handle);
		if (pSprite)
		{
			pSprite->PlayEffect(effect_name, dx, dy, times, flag, soundtype);
		}
		return NULL;
	}

	int Engine::GetEngineSpriteBubbleItemOffset(EngineSpriteHandle handle) const
	{
		const Sprite *pSprite = CheckEngineSpriteIncludeAttached(handle);
		if (pSprite)
		{
			return pSprite->GetBubbleItemOffset();
		}
		return 0;
	}

	//界面特效相关
	IEffect* Engine::CreateEffect(const std::wstring &effectname, bool async)
	{
		Effect *tpEffect = GetEffectManager()->CreateEffect(effectname, true);
		if( tpEffect )
		{
			m_effects.insert(tpEffect);
			tpEffect->AssureResource(async);
			tpEffect->Play();
			tpEffect->SetDirection(XPDIR_TOP);//和资源无关
			tpEffect->SetScreenCrood(true);
			IParticleEffect *pPEffect = tpEffect->TryConvertToParticle();
			if (pPEffect)
			{
				pPEffect->SetCycleMode(XPPCM_PULSE_EMISSION);
			}
		}
		return tpEffect;
	}

	void Engine::DrawEffect(IEffect* pEffect)
	{
		Effect* tpEffect = dynamic_cast<Effect*>(pEffect);
		XPASSERT(m_effects.find(tpEffect) != m_effects.end() && L"这个界面特效已经被释放了！");
		tpEffect->Render(NULL, true);
	}

	void Engine::ReleaseEffect(IEffect* pEffect)
	{
		Effect* tpEffect = dynamic_cast<Effect*>(pEffect);
		XPASSERT(m_effects.find(tpEffect) != m_effects.end() && L"这个界面特效已经被释放了！");
		tpEffect->Stop();
		m_willBeDeleteEffects.insert(tpEffect);
	}

	void Engine::HoldEffect(const std::wstring &name)
	{
		if (m_holdEffects.find(name) != m_holdEffects.end())
			return;

		Effect *tpEffect = GetEffectManager()->CreateEffect(name, true, false);
		if( tpEffect )
		{
			m_holdEffects[name] = tpEffect;
			tpEffect->AssureResource(true);
			tpEffect->HoldRes();
		}
	}

	void Engine::ReleaseEffect(const std::wstring &name)
	{
		std::map<std::wstring, Effect*>::iterator it = m_holdEffects.find(name);
		if (it != m_holdEffects.end())
		{
			delete it->second;
			m_holdEffects.erase(it);
		}
	}

	int Engine::GetAllSpriteCount() const
	{
		int result = (int)m_engineSprites.size();
		if (m_pWorld)
			result += m_pWorld->GetAllSpriteCount();
		return result;
	}

	void Engine::GetAllSpriteDetail(std::map<std::wstring, int> &detail) const
	{
		detail.clear();
		EngineSpriteMap::const_iterator it = m_engineSprites.begin(), ie = m_engineSprites.end();
		for (;it!=ie;++it)
		{
			detail[it->second->GetModelName()] += 1;
		}
		if (m_pWorld)
			m_pWorld->GetAllSpriteDetail(detail);
	}

	PictureHandle Engine::CaptureWorld()
	{
		if( m_pRenderer == NULL ) return INVALID_PICTURE_HANDLE;
		if( m_pWorld == NULL ) return INVALID_PICTURE_HANDLE;
		if( !m_pWorld->IsMapFull() )  return INVALID_PICTURE_HANDLE;
		NuclearDisplayMode mode = m_pRenderer->GetDisplayMode();
		PictureHandle result = m_pRenderer->CreateRenderTarget(mode.width, mode.height, mode.bitperpix==32?XPTEXFMT_A8R8G8B8:XPTEXFMT_A4R4G4B4);
		if (result == INVALID_PICTURE_HANDLE) return result;
		m_pRenderer->PushRenderTarget(result);
        m_pWorld->RenderCaptureWorld();
		m_pRenderer->PopRenderTarget();
		return result;
	}
    
    void Engine::GCNow()
    {
        if(m_pAniMan)
        {
			m_pAniMan->GarbageCollectionAll(true);
        }
    }
    
    void Engine::setGCCooldown(int time)
    {
        if(m_pAniMan)
        {
			m_pAniMan->setGcCooldown(time);
        }
    }

	int Engine::GetLogicWidth()
	{
		if (m_adapter) {
			return m_adapter->get_logic_w();
		}
		return GetScreenWidth();
	}
	int Engine::GetLogicHeight()
	{
		if (m_adapter) {
			return m_adapter->get_logic_h();
		}
		return GetScreenHeight();
	}

	void Engine::ResetFPSCounter()
	{
		mFrameTimeCount = 0;
		mFrameCount = 0;
	}
	float Engine::GetFPS() const
	{
		return mFps;
	}
	float Engine::GetLatestFPS() const
	{
		return 1000.0f / mCurDeltaTime;
	}

	void Engine::GetCurMemInfo()
	{
#ifndef ANDROID
		mCurMemSize = (float)CFileUtil::GetUsedMemory();
		mCurAvailableMemSize = (float)CFileUtil::GetAvailableMemory();
#endif // !ANDROID
	}

	void Engine::SetStepLoadTexture(bool b)
	{
		if (m_pAniMan)
		{
			m_pAniMan->SetStepLoadTexture(b);
		}
	}

	bool Engine::GetStepLoadTexture() const
	{
		if (m_pAniMan)
		{
			return m_pAniMan->GetStepLoadTexture();
		}
		return false;
	}

	void Engine::SetLimitFireThreadSecond(int limitFireThreadCount)
	{
		core::Thread::m_iLimitFires = limitFireThreadCount;
	}

	int Engine::GetLimitFireThreadSecond()
	{
		return core::Thread::m_iLimitFires;
	}
}
