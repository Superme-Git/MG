#include "stdafx.h"
#include <algorithm>
#include <atlimage.h>

#include "..\common\log.h"
#include "..\common\pfsutil.h"
#include "..\common\fileiomanager.h"
#include "..\common\xptimeuselog.h"
#include "..\sprite\spritemanager.h"
#include "..\sprite\componentsprite.h"
#include "..\effect\effectmanager.h"
#include "..\effect\effect.h"
#include "..\sound\directsoundmanager.h"
#include "..\soundarea\stepsoundtype.h"
#include "..\world\world.h"
#include "..\particlesystem\iparticlemanager.h"
#include "cursormanager.h"
#include "configmanager.h"
#include "engine.h"

#include "..\effect\fonteffect.h"

DWORD XL_INFO::s_nProcessId = 0;
DWORD XL_ERROR::s_nProcessId = 0;

namespace Nuclear
{	

	IEngine* GetEngine()
	{
		return &(Engine::GetInstance());
	}

	Engine Engine::s_instance;
	__int64 Engine::frame_msg = 0;

	// 操作
	Engine::Engine() {
		m_pPfslog = NULL;
		m_pETM = NULL;
		m_pEnv = NULL;
		m_pCursorMan = NULL;
		m_pQuery = NULL;
		m_pApp = NULL;
		m_pWorld = NULL; 
		m_pRenderer = NULL; 
		m_pConfigMan = NULL;
		m_pSoundManager = NULL;
		m_hWnd = NULL;
		m_keyboardHook = NULL;
		m_pStepSoundType = NULL;
		m_iOldColorMode = 32;
		m_iCurColorMode = 32;
		m_tick = Nuclear::GetMilliSeconds();
		m_pFileIOMan = NULL;
		m_pSprMan = NULL;
		m_pAniMan = NULL;
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
		m_bgsoundMode = XPBGSM_AUTO;
		m_windowState = XPWS_ACTIVE;
		m_cleanColor.data = 0xC090;
		m_InitState = 0;
		m_nInitStep = 0;
		m_nAppInitCount = 0;
		m_nSprStepCount = -1;
		m_pPathFinder = NULL;
		m_userTaskListECPF[XPWS_ACTIVE] = -1;
		m_userTaskListECPF[XPWS_INACTIVE] = -1;
		m_userTaskListECPF[XPWS_MINIMIZE] = -1;
		m_userTaskListRCPF[XPWS_ACTIVE] = -1;
		m_userTaskListRCPF[XPWS_INACTIVE] = -1;
		m_userTaskListRCPF[XPWS_MINIMIZE] = -1;
		m_userTasksTimePF[XPWS_ACTIVE] = 30;
		m_userTasksTimePF[XPWS_INACTIVE] = 30;
		m_userTasksTimePF[XPWS_MINIMIZE] = 30;
		XL_INFO::s_nProcessId = GetCurrentProcessId();
		XL_ERROR::s_nProcessId = XL_INFO::s_nProcessId;
	}

	Engine::~Engine()
	{
	}


	Renderer* Engine::GetRenderer( HWND hwnd )
	{
		if( hwnd == NULL )
			return m_pRenderer;
		return NULL;
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
		CheckSpritePictureHandle();
	}

	void Engine::OnSetCursor()
	{
		if (m_pCursorMan)
			m_pCursorMan->OnSetCursor();
	}

	void Engine::OnActive(bool active)
	{
		if (active)
		{
			SetWindowState(XPWS_ACTIVE);
			return;
		}
		if (!m_bSetInactiveTimer)
		{
			m_bSetInactiveTimer = true;
			if (m_hWnd)
			{
				SetTimer(m_hWnd, 4, 200, NULL);
			} else {
				SetWindowState(XPWS_INACTIVE);
			}
		}
	}

	//这三态只能这样子转化，别的转化都是非法的：
	//XPWS_ACTIVE > XPWS_INACTIVE or XPWS_MINIMIZE
	//XPWS_INACTIVE > XPWS_ACTIVE or XPWS_MINIMIZE
	//XPWS_MINIMIZE > XPWS_ACTIVE
	void Engine::SetWindowState(XPWINDOW_STATE state)
	{
		if (m_bSetInactiveTimer)
		{
			m_bSetInactiveTimer = false;
			KillTimer(m_hWnd, 4);
		}
		if (state == XPWS_INACTIVE && m_windowState == XPWS_MINIMIZE)
			return;//如果从最小化变为后台，那么就忽略
		m_windowState = state;
		if (m_pSoundManager)
		{
			m_pSoundManager->OnWindowStateChange(state == XPWS_ACTIVE);
		}
	}

	void Engine::CenterWindow()
	{
		int screenWidth = GetSystemMetrics(SM_CXSCREEN);
		int screenHeight = GetSystemMetrics(SM_CYSCREEN);
		XDisplayMode mode = m_pRenderer->GetDisplayMode();
		switch (mode.screenMode)
		{
		case XPSM_WINDOW:
			{
				int iCaptionHeight = GetSystemMetrics(SM_CYCAPTION);
				int iFixedFrameSizeY = m_bSizeBox?GetSystemMetrics(SM_CYSIZEFRAME):GetSystemMetrics(SM_CYFIXEDFRAME);
				int iFixedFrameSizeX = m_bSizeBox?GetSystemMetrics(SM_CXSIZEFRAME):GetSystemMetrics(SM_CXFIXEDFRAME);
				int width = m_iWidth+2*iFixedFrameSizeX;
				int height = m_iHeight+2*iFixedFrameSizeY+iCaptionHeight;
				SetWindowPos(m_hWnd, HWND_NOTOPMOST, (screenWidth - width) / 2, 
					(screenHeight - m_iHeight) / 2 - iCaptionHeight - iFixedFrameSizeY, width, height, 0);
			}
			break;
		case XPSM_FADE_FULL_SCREEN:
			SetWindowPos(m_hWnd, HWND_TOPMOST, 0, 0, screenWidth, screenHeight, 0);
			break;
		case XPSM_REAL_FULL_SCREEN:
			SetWindowPos(m_hWnd, HWND_NOTOPMOST, 0, 0, screenWidth, screenHeight, 0);
			break;
		}
	}

	DWORD Engine::GetWindowStyleFromMode(XPScreenMode mode)
	{
		switch (mode)
		{
		case XPSM_WINDOW:
			return WS_POPUP | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX | (m_bHasMaximizbox?WS_MAXIMIZEBOX:0) | (m_bSizeBox?WS_SIZEBOX:0);
			break;
		case XPSM_FADE_FULL_SCREEN:
		case XPSM_REAL_FULL_SCREEN:
			return WS_POPUP;
			break;
		}
		return 0;
	}

	bool Engine::Init(int step)
	{
		switch (step)
		{
		case XPEIS_SOURCE_CONFIG_CURSOR_MAN:
			m_pSystemResourceMan = new SystemResourceManager(this);
			XPLOG_INFO(L"  系统资源管理器器创建成功\n");
			m_pSystemResourceMan->Init();
			XPLOG_INFO(L"----------显卡摘要信息------------\n");
			XPLOG_INFO(L"%s\n", m_pSystemResourceMan->GetCurDisplayDevice().c_str());
			XPLOG_INFO(L"----------显卡摘要信息------------\n");


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

			m_pCursorMan = new CursorManager(this);
			return true;

		case XPEIS_ENV_SOUND_ANI_MAN:
			m_pEnv = new Env(this);
			m_pSystemResourceMan->SetSoundInited(true);
			m_pSoundManager = new DirectSoundManager(this);
			if( !m_pSoundManager->Init(m_hWnd, DSSCL_PRIORITY) ||
				! m_pSoundManager->SetPrimaryBufferFormat( 2, 22050, 16 ) )
			{
				//XPALARM(L"DirectSound 初始化失败");
				XPLOG_ERROR(L"DirectSound 初始化失败");
				m_pSystemResourceMan->SetSoundInited(false);
			}
			XPLOG_INFO(L"  声音管理器初始化成功\n");

			m_pAniMan = new AniManager(this);
			if( !m_pAniMan->Init() ) 
			{
				XPALARM(L"动画管理器初始化失败");
				return false;
			}
			XPLOG_INFO(L"  动画管理器初始化成功\n");
			return true;
		case XPEIS_SPRITE_PRE_INIT:
			m_pSprMan = new SpriteManager(this);
			if (m_pSprMan->PreInit())
			{
				m_nSprStepCount = (m_pSprMan->GetInitCount() + 63) >> 6;//64个一批
				return true;
			}
			XPALARM(L"精灵管理器整体初始化失败！");
			XPLOG_INFO(L"精灵管理器整体初始化失败！");
			return false;
		default:
			break;
		}
		bool sprInitResult = true;
		if (step - XPEIS_SPRITE_INITING < m_nSprStepCount)
		{
			unsigned __int64 result = m_pSprMan->Init((step - XPEIS_SPRITE_INITING) << 6, 64);
			if (result == 0)
				return true;
			sprInitResult = false;
			XPLOG_ERROR(L"精灵管理器第 %d 批模型初始化失败！错误信息：%I64d\n", step - XPEIS_SPRITE_INITING, result);
			return true;
		}
		if (!sprInitResult)
		{
			XPALARM(L"精灵管理器部分初始化失败！\n");
			sprInitResult = true;
		}
		step -= m_nSprStepCount - 1;//这个1是因为XPEIS_SPRITE_INITING也是一步啊……
		switch (step)
		{
		case XPEIS_SPRITE_POST_INIT:
			if (m_pSprMan->PostInit())
			{
				XPLOG_INFO(L"  精灵管理器初始化成功\n");
				return true;
			}
			XPLOG_INFO(L"精灵管理器初始化失败\n");
			return false;

		case XPEIS_EFFECT_QUERY_STEPSOUND:
			m_pEffectMan = new EffectManager(this);
			m_pQuery = new Query(this);
			m_pStepSoundType = new StepSoundType();
			m_pStepSoundType->Init(m_pFileIOMan);			
			return true;
		case XPEIS_INPUT_WORLD:
			m_pWorld = new World(this, m_iWidth, m_iHeight);


			if( !m_pWorld->Init() ) 
			{
				XPALARM(L"加载世界失败");
				return false;
			}
			//最后一步，把当前窗口的是否Active设进SoundMan里面
			if (GetForegroundWindow() != m_hWnd)
			{
				HWND wnd = GetForegroundWindow();
				SetForegroundWindow(m_hWnd);
				SetForegroundWindow(wnd);
				m_pSoundManager->OnWindowStateChange(false);
			}

			XPLOG_INFO(L"  游戏世界初始化成功\n");
			m_nInitStep = 0;
			m_InitState = 1;
			return true;
		}
		return false;
	}

	bool Engine::Run(const EngineParameter &ep) 
	{
		XPLOG_INFO_CHECK();
		XPLOG_ERROR_CHECK();
		XPLOG_FRAME_STATE_CHECK();
		XPLOG_BIG_FRAME_STATE_CHECK();
		XPLOG_FULL_FRAME_STATE_CHECK();
		XPLOG_INFO(L"引擎开始启动...\n");
		if( ep.pApp == NULL )
			return false;

		m_pPathFinder = new astar::PathFinder();

		m_pPfslog = new PFSLog();
		PFS::CEnv::SetOutLogInstance(m_pPfslog);
		m_pFileIOMan = new CFileIOManager();
		if( !m_pFileIOMan->Init(false, ep.bApplictionInBuild))
		{
			XPALARM(L"PFS路径初始化失败\n");
			return false;
		}
		m_bAsyncRead = ep.bAsyncRead;

		XPLOG_INFO(L"  引擎PFS初始化成功\n");

		m_bSizeBox = ep.bSizeBox;
		m_bHasMaximizbox = ep.bHasMaximizbox;
		int iCaptionHeight = GetSystemMetrics(SM_CYCAPTION);
		int iFixedFrameSizeY = m_bSizeBox?GetSystemMetrics(SM_CYSIZEFRAME):GetSystemMetrics(SM_CYFIXEDFRAME);
		int iFixedFrameSizeX = m_bSizeBox?GetSystemMetrics(SM_CXSIZEFRAME):GetSystemMetrics(SM_CXFIXEDFRAME);

		WNDCLASSEX wc = { sizeof(WNDCLASSEX) }; // 类结构
		m_bUserWnd = ( ep.hWnd != NULL );
		if( m_bUserWnd )
		{
			m_hWnd = ep.hWnd;
		}
		else
		{
			// 注册窗口类
			wc.style = CS_CLASSDC;
			wc.lpfnWndProc = MsgProc;
			wc.hInstance = GetModuleHandle(NULL);
			wc.lpszClassName =  ep.szClassName;

			RegisterClassEx( &wc );

			// 初始化主窗口
			m_hWnd = CreateWindow( ep.szClassName, ep.szWindowTitle,
				GetWindowStyleFromMode(ep.dmode.screenMode),
				100, 100, ep.dmode.width+2*iFixedFrameSizeX, ep.dmode.height+2*iFixedFrameSizeY+iCaptionHeight,
				GetDesktopWindow(), NULL, wc.hInstance, NULL );
			//先不装键盘钩子，以后弄一套热键注册方法
			//那个13是WH_KEYBOARD_LL，不定义_WIN32_WINNT了。
			//m_keyboardHook = SetWindowsHookEx(13, LowLevelKeyboardHookEx, wc.hInstance, 0);
		}
		::SetTimer(m_hWnd,1,1000,NULL);
		::SetTimer(m_hWnd,2,250,NULL);
		

		DEVMODE olddevmode;
		EnumDisplaySettings(NULL, ENUM_CURRENT_SETTINGS, &olddevmode);
		m_iOldColorMode = olddevmode.dmBitsPerPel;
		m_iCurColorMode = olddevmode.dmBitsPerPel;

		ChangeDisplayColor(ep.dmode.bitperpix);

		if( !CreateRenderer(&m_pRenderer, m_hWnd, ep.dmode, m_pFileIOMan, ep.dwRenderFlags) )
		{
			XPALARM(L"渲染器创建失败");
			return false;
		}
		m_pRenderer->SetAutoTestDevice(false);
		m_pRenderer->SetRestoreTask(&m_restoreTask);

		XDisplayMode mode = m_pRenderer->GetDisplayMode();
		m_iWidth = mode.width;
		m_iHeight= mode.height;

		if( !m_bUserWnd)
		{
			CenterWindow();			
		}

		XPLOG_INFO(L"  渲染器创建成功\n");

		m_pApp = ep.pApp;
		m_nAppInitCount = ep.nAppInitStepCount;
		m_InitState = 0;
		m_nInitStep = 0;
		m_nSprStepCount = -1;

		//从这里开始就可以进入消息循环了
		if( !m_bUserWnd)
		{
			ShowWindow( m_hWnd, SW_SHOWDEFAULT );
			UpdateWindow( m_hWnd );

			MSG msg;
			ZeroMemory( &msg, sizeof(msg) );

			//如果拖住了标题栏，或者在标题栏按右键，
			//那么就要set一个timer来处理渲染和逻辑线程，这个就会设为true
			bool bTimerSeted = false;
			while( msg.message!=WM_QUIT )
			{
				if( PeekMessage( &msg, NULL, 0U, 0U, PM_REMOVE ) )
				{
					TranslateMessage( &msg );
					switch ( msg.message)
					{
					case WM_NCLBUTTONDOWN:
					case WM_NCRBUTTONDOWN:
						::SetTimer( m_hWnd, 3, 30, NULL);
						bTimerSeted = true;
						break;
					default:
						break;
					}
					DispatchMessage( &msg );	
					if( bTimerSeted)
					{
						::KillTimer( m_hWnd, 3);
						bTimerSeted = false;
					}
				}
				else
					OnIdle();
			}

			m_pApp->OnExit();

			if (m_SpriteHandle != INVALID_PICTURE_HANDLE)
				m_pRenderer->FreeRenderTarget(m_SpriteHandle);
			m_SpriteHandle = INVALID_PICTURE_HANDLE;
			if (m_AlphaSpriteHandle != INVALID_PICTURE_HANDLE)
				m_pRenderer->FreeRenderTarget(m_AlphaSpriteHandle);
			m_AlphaSpriteHandle = INVALID_PICTURE_HANDLE;

			EngineSpriteMap::iterator sprit = m_engineSprites.begin(), sprie = m_engineSprites.end();
			for (;sprit!=sprie;++sprit)
			{
				delete sprit->second;
			}
			m_engineSprites.clear();

			m_willBeDeleteEffects.clear();
			EffectSet::iterator it = m_effects.begin(), ie = m_effects.end();
			for (;it!=ie;++it)
			{
				delete *it;
			}
			m_effects.clear();

			std::map<std::wstring, Effect*>::iterator hIt = m_holdEffects.begin(), hIe = m_holdEffects.end();
			for (;hIt!=hIe;++hIt)
			{
				delete hIt->second;
			}
			m_holdEffects.clear();

			delete m_pWorld;
			m_pWorld = NULL;

			delete m_pStepSoundType;
			m_pStepSoundType = NULL;
			delete m_pSoundManager;
			delete m_pQuery;
			delete m_pEffectMan;
			delete m_pSprMan;
			delete m_pAniMan;
			delete m_pEnv;
			delete m_pCursorMan;
			delete m_pConfigMan;
			delete m_pSystemResourceMan;
			
			DestroyRenderer(m_pRenderer); 
			ChangeDisplayColor(m_iOldColorMode);
			m_pFileIOMan->Destroy();
			delete m_pFileIOMan;

			if (m_keyboardHook)
			{
				UnhookWindowsHookEx(m_keyboardHook);
				m_keyboardHook = NULL;
			}

			UnregisterClass( wc.lpszClassName, wc.hInstance );
			PFS::CEnv::SetOutLogInstance(NULL);
			delete m_pPfslog;

			delete m_pPathFinder;

			XPLOG_INFO(L"引擎退出\n");
		}
		return true;

	}	

	bool Engine::Exit()
	{ 
		if( m_bUserWnd )
		{
			if( m_pApp ) { m_pApp->OnExit(); }
			EngineSpriteMap::iterator sprit = m_engineSprites.begin(), sprie = m_engineSprites.end();
			for (;sprit!=sprie;++sprit)
			{
				delete sprit->second;
			}
			m_engineSprites.clear();
			if (m_SpriteHandle != INVALID_PICTURE_HANDLE)
				m_pRenderer->FreeRenderTarget(m_SpriteHandle);
			m_SpriteHandle = INVALID_PICTURE_HANDLE;
			if (m_AlphaSpriteHandle != INVALID_PICTURE_HANDLE)
				m_pRenderer->FreeRenderTarget(m_AlphaSpriteHandle);
			m_AlphaSpriteHandle = INVALID_PICTURE_HANDLE;
			EffectSet::iterator it = m_effects.begin(), ie = m_effects.end();
			m_willBeDeleteEffects.clear();
			for (;it!=ie;++it)
			{
				delete *it;
			}
			m_effects.clear();

			std::map<std::wstring, Effect*>::iterator hIt = m_holdEffects.begin(), hIe = m_holdEffects.end();
			for (;hIt!=hIe;++hIt)
			{
				delete hIt->second;
			}
			m_holdEffects.clear();

			delete m_pWorld;
			delete m_pStepSoundType;
			delete m_pSoundManager;
			delete m_pQuery;
			delete m_pEffectMan;
			delete m_pSprMan;
			delete m_pAniMan;
			delete m_pEnv;
			delete m_pCursorMan;
			delete m_pConfigMan;
			delete m_pSystemResourceMan;
			DestroyRenderer(m_pRenderer);
			ChangeDisplayColor(m_iOldColorMode);
			m_pFileIOMan->Destroy();
			delete m_pFileIOMan;

			PFS::CEnv::SetOutLogInstance(NULL);
			delete m_pPfslog;
			delete m_pPathFinder;
			XPLOG_INFO(L"引擎退出\n");
		}
		else
		{
			SendMessage(m_hWnd, WM_CLOSE, 0,0);
		}
		
		return true;

	}

	void Engine::ChangeDisplayColor(int color)
	{
		if( color != m_iCurColorMode )
		{
			DEVMODE devmode;
			memset(&devmode, 0, sizeof(DEVMODE));
			devmode.dmSize = sizeof(DEVMODE);
			devmode.dmBitsPerPel = color;
			devmode.dmFields = DM_BITSPERPEL;
			if( DISP_CHANGE_SUCCESSFUL == ChangeDisplaySettings(&devmode,0) )
				m_iCurColorMode = color;
		}
	}

	void Engine::SetAniPicGCTime(int t)
	{
		XPASSERT(m_pAniMan);
		m_pAniMan->SetAniPicGCTime(t);
	}

	int Engine::GetAniPicGCTime() const
	{
		XPASSERT(m_pAniMan);
		return m_pAniMan->GetAniPicGCTime();
	}

	void Engine::SetAniXapGCTime(int t)
	{
		XPASSERT(m_pAniMan);
		m_pAniMan->SetAniXapGCTime(t);
	}

	int Engine::GetAniXapGCTime() const
	{
		XPASSERT(m_pAniMan);
		return m_pAniMan->GetAniXapGCTime();
	}

#define XPSLEEP_TIME 1
	static void testsleep(int n/*ms*/,float v)
	{
		__int64 now = rdtsc();
		__int64 sleep64 = (__int64)(n * v);
		__int64 sleepend = now + sleep64;
		//v *= SLEEP_TIME;
		while( true )
		{
			__int64 d = rdtsc() - sleepend;
			if( d > 0 ) break;
			if( -d > v )
				Sleep(XPSLEEP_TIME);
			else
				break;
				//Sleep(0);
		}
	}

	XPTimeUseLog g_timeuserlog;

	void Engine::ControlFPS()
	{
		static __int64 tc = rdtsc();
		static CRECT cp = GetViewport();
		CRECT cp_now = GetViewport();
		__int64 now = rdtsc();
		__int64 delta64 = now - tc;

		float v = m_pSystemResourceMan->GetCPUFrequency() * 1000.f;
		if( g_timeuserlog.Log( delta64, v))
		{
			// 以下为了去掉记日志花去的时间
			__int64 newnow = rdtsc(); 
			tc +=( newnow - now);
			now = newnow;
		}
		if((m_windowState != XPWS_ACTIVE || GetConfigManager()->IsControlFPS()))
		{
			static int nfps;
			nfps = GetConfigManager()->GetControlFPS(m_windowState) * 2;

			int iPiece = m_pSystemResourceMan->GetSystemTimerPeriod();
			while( iPiece * nfps  < 10000000 )//iPicec * nfps的单位是 10 000 000分之一帧
				iPiece += m_pSystemResourceMan->GetSystemTimerPeriod();
			iPiece /= 10000;
			int mst = (int)(delta64/v);
			if( mst < iPiece )
			{
				testsleep(iPiece - mst, v);
				now = rdtsc();
				delta64 = now - tc;
			}
		}
		frame_stat[frame_stat_index%XPFRAMESTAT_COUNT] = delta64/v;
		frame_stat_task[frame_stat_index%XPFRAMESTAT_COUNT] = frame_task/v;
		frame_stat_msg[frame_stat_index%XPFRAMESTAT_COUNT] = frame_msg/v;
		frame_task = 0;
		frame_msg = 0;
		camera_dis[frame_stat_index%XPFRAMESTAT_COUNT] = sqrtf((float)cp.GetPos().Dis2(cp_now.GetPos()));
		fileio_task_count[frame_stat_index%XPFRAMESTAT_COUNT] = m_pFileIOMan->GetFileIOTaskBytes()/1024/64.f;
		++frame_stat_index;
		tc = now;
		cp = cp_now;
		g_timeuserlog.Push( XPTimeUseLog::TUL_BEGIN);
	}

	void Engine::MainLoop()
	{
		g_timeuserlog.Push( XPTimeUseLog::TUL_PROCESSMESSAGE);
		RunUserTask();
		g_timeuserlog.Push( XPTimeUseLog::TUL_MAINLOOPRUNUSERTASK);
		m_pFileIOMan->OnUpdate(((__int64)m_pSystemResourceMan->GetCPUFrequency()) * 1000);
		g_timeuserlog.Push( XPTimeUseLog::TUL_FILEIOUPDATE);
		int tick = Nuclear::GetMilliSeconds();
		int delta = tick - m_tick;
		m_tick = tick;
		if( delta > 0 )
		{
			m_pSoundManager->OnTick();
			g_timeuserlog.Push( XPTimeUseLog::TUL_SOUNDMANAGER);
			m_pEffectMan->OnTick();
			g_timeuserlog.Push( XPTimeUseLog::TUL_EFFECTMANAGER);
			m_pApp->OnTick(m_tick, delta);
			g_timeuserlog.Push( XPTimeUseLog::TUL_APPTICK);
			m_pWorld->OnTick(m_tick, delta);
			g_timeuserlog.Push( XPTimeUseLog::TUL_WORLDTICK);
			EffectSet::iterator it = m_willBeDeleteEffects.begin(), ie = m_willBeDeleteEffects.end();
			if (it != ie)
			{
				for (;it!=ie;++it)
				{
					delete *it;
					m_effects.erase(*it);
				}
				m_willBeDeleteEffects.clear();
			}
			g_timeuserlog.Push( XPTimeUseLog::TUL_DELETEEFFECT);
			it = m_effects.begin(), ie = m_effects.end();
			for (;it!=ie;)
			{
				(*(it++))->Update(delta);
			}
			g_timeuserlog.Push( XPTimeUseLog::TUL_ENGINE_EFFECTUPDATE);
		}

		bool b = m_pApp->OnBeforeRender(m_tick);
		g_timeuserlog.Push( XPTimeUseLog::TUL_APPBEFORERENDER);
		UpdateBeforeRender(delta);
		if( m_windowState != XPWS_MINIMIZE && m_pRenderer->TestDevice() == XPDS_OK && b)
		{
			Render(true);
		} else {
			ControlFPS();
		}
		
		if ((frame_stat_index & 0xF) == 0)//每15帧 GC一次
		{
			m_pAniMan->GarbageCollection(m_tick);
			g_timeuserlog.Push( XPTimeUseLog::TUL_ANIMANAGERGC);
			m_pSoundManager->GarbageCollection(m_tick);
			g_timeuserlog.Push( XPTimeUseLog::TUL_SOUNDMANAGERGC);
		}
	}

	// 如果是用户自己创建的窗口，那么需要在窗口函数中调用此函数
	bool Engine::OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam ) 
	{ 
		if (GetApp())
		{
			XPWinMSGFuncResult result = 0;
			result = GetApp()->OnWindowsMessage(hWnd, msg, wParam, lParam );
			if (result & XPWINMSG_INTERCEPT)
				return true;
		}
		switch( msg )
		{
		//case XPMESSAGE_TASKWAKEUP:
		//	g_timeuserlog.Push(XPTimeUseLog::TUL_MSG_BEGIN_TASK);
		//	RunUserTask();
		//	g_timeuserlog.Push(XPTimeUseLog::TUL_MSG_END_TASK);
		//	break;
		case WM_TIMER:
			if( wParam == 1 )
				TimerOB::OnSourceTimer();
			else if( wParam == 2 )
				OnTimer(250);
			break;
		case WM_SIZING:
		case WM_PAINT:
			Render(false);
			break;
		case WM_SIZE:
			{
				switch( wParam )
				{
				case SIZE_MINIMIZED:
				case SIZE_MAXHIDE:
					SetWindowState(XPWS_MINIMIZE);
					break;
				default:
					SetWindowState(XPWS_ACTIVE);
				}
			}
			break;
		case WM_SETCURSOR:
			OnSetCursor();
			return true; // prevent Windows from setting cursor to window class cursor
		case WM_ACTIVATE:
			switch (wParam)
			{
			case WA_ACTIVE:
			case WA_CLICKACTIVE:
				SetWindowState(XPWS_ACTIVE);
				break;
			case WA_INACTIVE:
				SetWindowState(XPWS_INACTIVE);
				break;
			}
			break;
		case WM_SHOWWINDOW:
			if (wParam)
			{
				//显示窗口
				SetWindowState(XPWS_ACTIVE);
			} else {
				//隐藏窗口
				SetWindowState(XPWS_MINIMIZE);
			}
			break;
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
		if ((m_tick - lasttick > 50) || (step == count) || (step == 1))
		{
			m_pRenderer->Begin(0x00000000);
			m_pApp->OnRenderInit(m_tick, step, count);
			m_pRenderer->End();
			lasttick = m_tick;
		}
	}

	// 如果是用户自己创建的窗口，那么需要在程序空闲(OnIdle)时中调用此函数
	void Engine::OnIdle() 
	{ 
		switch (m_InitState)
		{
		case 0:
			if (!Init(m_nInitStep++))
			{
				PostQuitMessage(0);
			}
			InitNotify();
			break;
		case 1:
			if (m_nInitStep < m_nAppInitCount)
			{
				if (!m_pApp->OnInit(m_nInitStep++))
				{
					PostQuitMessage(0);
				}
				InitNotify();
				break;
			}
			m_InitState = 2;
			//不用break了
		case 2:
			MainLoop();
			break;
		}
	}

	LRESULT CALLBACK Engine::LowLevelKeyboardHookEx(INT nCode, WPARAM wParam, LPARAM lParam)
	{
		/*if (nCode == HC_ACTION)
		{
			KBDLLHOOKSTRUCT *pkbdllhook = (KBDLLHOOKSTRUCT *)lParam;
			switch (wParam)
			{
			case WM_KEYUP:
			case WM_SYSKEYUP:
				switch (pkbdllhook->vkCode)
				{
				case VK_F10:
				case VK_LMENU:
				case VK_RMENU:
					if (::GetForegroundWindow() == GetInstance().m_hWnd)
						return true;
					break;
				}
				break;
			}
		}*/
		return CallNextHookEx(GetInstance().m_keyboardHook, nCode, wParam, lParam);
	}

	LRESULT CALLBACK Engine::MsgProc( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam )
	{
		if (GetInstance().m_InitState < 2)
		{
			if (msg == WM_DESTROY) 
				PostQuitMessage( 0 );
			return DefWindowProc( hWnd, msg, wParam, lParam );
		}
		switch( msg)
		{
		case WM_LBUTTONDOWN:
		case WM_LBUTTONUP:
		case WM_MOUSEMOVE:
		case WM_RBUTTONDOWN:
		case WM_RBUTTONUP:
		case 0x020A: // WM_MOUSEWHEEL
			{
				CPOINT point;
				point.x = LOWORD( lParam);
				point.y = HIWORD( lParam);
				Engine::GetInstance().WindowToClient( point);
				lParam = ( point.y << 16) | point.x;
			}
			break;
		default:
			break;
		}

		XPWinMSGFuncResult result = 0;
		//调用App的OnWindowsMessage之前先把XPMESSAGE_TASKWAKEUP、WM_ACTIVATE、WM_KILLFOCUS、WM_SETFOCUS、WM_TIMER、WM_SIZING、WM_PAINT、WM_SIZE、WM_DESTROY还有(WM_SYSCOMMAND里面wParam==SC_KEYMENU的消息)拦截掉
		if( Engine::GetInstance().GetApp() != NULL)
		{
			switch( msg )
			{
			//case XPMESSAGE_TASKWAKEUP:
			//	g_timeuserlog.Push(XPTimeUseLog::TUL_MSG_BEGIN_TASK);
			//	GetInstance().RunUserTask();
			//	g_timeuserlog.Push(XPTimeUseLog::TUL_MSG_END_TASK);
				break;
			case WM_TIMER:
				switch (wParam)
				{
				case 1:
					TimerOB::OnSourceTimer();
					break;
				case 2:
					g_timeuserlog.Push(XPTimeUseLog::TUL_MSG_BEGIN_TIMER250);
					GetInstance().OnTimer(250);
					g_timeuserlog.Push(XPTimeUseLog::TUL_MSG_END_TIMER250);
					break;
				case 3:
					GetInstance().OnIdle();
					break;
				case 4:
					if (GetActiveWindow() == NULL)
					{
						GetInstance().SetWindowState(XPWS_INACTIVE);
					} else {
						if (GetInstance().m_bSetInactiveTimer)
						{
							GetInstance().m_bSetInactiveTimer = false;
							KillTimer(GetInstance().m_hWnd, 4);
						}
					}
					break;
				default:
					break;
				}
				break;
			case WM_ACTIVATE:
				switch (wParam)
				{
				case WA_ACTIVE:
				case WA_CLICKACTIVE:
					GetInstance().SetWindowState(XPWS_ACTIVE);
					break;
				case WA_INACTIVE:
					GetInstance().OnActive(false);
					break;
				}
				break;
			case WM_SHOWWINDOW:
				if (wParam)
				{
					//显示窗口
					GetInstance().SetWindowState(XPWS_ACTIVE);
				} else {
					//隐藏窗口
					GetInstance().SetWindowState(XPWS_MINIMIZE);
				}
				break;
			case WM_KILLFOCUS:
			case WM_SETFOCUS:
				break;
			case WM_SIZING:
			case WM_PAINT:
				GetInstance().Render(false);
				break;
			case WM_DESTROY:
				PostQuitMessage( 0 );
				return 0;
			case WM_SYSCOMMAND:	//屏蔽因为按一下Alt、F10、Alt+空格造成的线程阻塞
				if( wParam == SC_KEYMENU )
					return 0;
				//这里不break，别的WM_SYSCOMMAND消息还是要处理的
				if ( wParam == SC_RESTORE)
				{
					if (GetInstance().m_pRenderer->GetDisplayMode().screenMode != XPSM_WINDOW)
					{
						GetInstance().CenterWindow();
					}
				}
			default:
				__int64 tc = rdtsc();
				result = Engine::GetInstance().GetApp()->OnWindowsMessage(hWnd, msg, wParam, lParam );
				frame_msg += rdtsc() - tc;
				break;
			}
		}
		if (result & XPWINMSG_INTERCEPT)
			return TRUE;
		switch (msg)
		{
		case WM_SIZE:
			{
				//XPLOG_INFO("wParam = %d\n", wParam);
				switch( wParam )
				{
				case SIZE_MINIMIZED:
				case SIZE_MAXHIDE:
					GetInstance().SetWindowState(XPWS_MINIMIZE);
					break;
				default:
					break;
				}
			}
			break;
		case WM_SETCURSOR:
			GetInstance().GetEnv()->ChangeCursor( INVALID_CURSOR_HANDLE);
			switch ( LOWORD( lParam))
			{
			case HTTOP: case HTBOTTOM:          SetCursor( LoadCursor( NULL, IDC_SIZENS));   break;
			case HTRIGHT: case HTLEFT:          SetCursor( LoadCursor( NULL, IDC_SIZEWE));   break;
			case HTTOPLEFT: case HTBOTTOMRIGHT: SetCursor( LoadCursor( NULL, IDC_SIZENWSE)); break;
			case HTTOPRIGHT: case HTBOTTOMLEFT: SetCursor( LoadCursor( NULL, IDC_SIZENESW)); break;
			default:
				GetInstance().OnSetCursor();
			}
			return TRUE; // prevent Windows from setting cursor to window class cursor
		default:
			break;
		}

		return DefWindowProc( hWnd, msg, wParam, lParam );
	}

	void Engine::WindowToClient( Nuclear::CPOINT& point)
	{
		XDisplayMode xdm = Engine::GetInstance().GetEnv()->GetDisplayMode();
		RECT client;
		::GetClientRect( Engine::GetInstance().GetWindow(), &client);
		

		point.x = ( int)( ( float)xdm.width / ( client.right - client.left) * point.x);
		point.y = ( int)( ( float)xdm.height / ( client.bottom - client.top) * point.y);
	}

	bool Engine::SetDisplayMode(const XDisplayMode &dmode)
	{
		XDisplayMode cmode = m_pRenderer->GetDisplayMode();
		if( dmode == cmode ) return true;
		std::vector<XDisplayMode> v= m_pRenderer->EnumDisplayMode();
		if( std::find(v.begin(), v.end(), dmode) == v.end()) return false;

		m_pWorld->SetViewSize(dmode.width, dmode.height);

		ChangeDisplayColor(dmode.bitperpix);
		if( m_pRenderer->SetDisplayMode(dmode))
		{
			
			m_iWidth = dmode.width;
			m_iHeight = dmode.height;
			DWORD newflag = GetWindowStyleFromMode(dmode.screenMode);
			if (GetWindowStyleFromMode(cmode.screenMode) != newflag)
			{
				SetWindowLongPtr(m_hWnd, GWL_STYLE, newflag | WS_VISIBLE);
			}
			CenterWindow();
			return true;
		}
		else
		{
			ChangeDisplayColor(cmode.bitperpix);
			m_pWorld->SetViewSize(m_iWidth, m_iHeight);
		}
		return false;
	}

	void Engine::UpdateBeforeRender(int delta)
	{
		m_pWorld->UpdateBeforeRender(delta);
		EngineSpriteMap::iterator it = m_engineSprites.begin(), ie = m_engineSprites.end();
		for (;it!=ie;++it)
		{
			it->second->Tick(delta);
		}
	}

	void Engine::Render(bool controlFPS)
	{
		if( m_pRenderer == NULL ) return;
		CheckSpritePictureHandle();
		m_pRenderer->Begin(m_cleanColor);
		if ( GetConfigManager()->IsRenderAlphaSprite() && m_AlphaSpriteHandle != INVALID_PICTURE_HANDLE)
		{
			m_pRenderer->PushRenderTarget(m_AlphaSpriteHandle);
			m_pRenderer->Clear(0x00000000);
			m_pRenderer->PopRenderTarget();
		}
		if( m_pWorld != NULL )
			m_pWorld->Render(m_tick, true);
		m_pRenderer->JustEnd();
		m_pRenderer->JustBegin();
		if( m_pApp != NULL )
		{
			m_pApp->OnRenderUI(m_tick);
		}

		g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERUI);
		if( GetConfigManager()->TestConsoleInfo(XPCSINFO_FRAMESTAT )) DrawFrameStat();
		if( GetConfigManager()->TestConsoleInfo(XPCSINFO_WORLDINFO )) m_pWorld->DrawInfo();
		if( GetConfigManager()->TestConsoleInfo(XPCSINFO_FPS       )) DrawFPS();
		if( GetConfigManager()->TestConsoleInfo(XPCSINFO_COORDINATE)) DrawCameraPos();
		if( GetConfigManager()->TestConsoleInfo(XPCSINFO_SYSINFO   )) DrawSystemInfo();
		if( GetConfigManager()->TestConsoleInfo(XPCSINFO_RENDERINFO)) DrawPerformance();
		if (controlFPS)
		{
			ControlFPS();
		}
		g_timeuserlog.Push(XPTimeUseLog::TUL_RENDERDEBUGINFO);
		m_pRenderer->End();
		g_timeuserlog.Push(XPTimeUseLog::TUL_RENDEREND);
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

	void Engine::DrawSystemInfo()
	{
		RECT trect;
		trect.left = 0;
		trect.top = 100;
		trect.right =  500;
		trect.bottom = 120;

		SystemResourceManager::WindowsVersionInfo WindowsVersionInfo;
		GetSystemResourceManager()->GetWindowsVersionInfo(WindowsVersionInfo);
		m_pRenderer->DrawText(WindowsVersionInfo.m_strInfo.c_str(), WindowsVersionInfo.m_strInfo.size(), &trect, 0xff00ffff);

		wchar_t buf[128];
		int c = (int)(GetSystemResourceManager()->GetCPUFrequency() / (1024/10.f));
		
		wsprintf(buf, L"CPU: %d.%d gHz", c/10, c%10);
		std::wstring cpuct = buf;
		trect.top +=20;
		trect.bottom +=20;
		m_pRenderer->DrawText(cpuct.c_str(), cpuct.size(), &trect, 0xff00ffff);

		SystemResourceManager::MemoryStatus MemoryStatus;
		GetSystemResourceManager()->GetMemoryStatus(MemoryStatus);
		
		wsprintf(buf,L"Memory(use/ava/tot): %d/%d/%dM %d%%"
			, MemoryStatus.m_iTotalVirtual/1024 - MemoryStatus.m_iAvailVirtual/1024
			, MemoryStatus.m_iAvailPhys/1024, MemoryStatus.m_iTotalPhys/1024, MemoryStatus.m_iMemoryLoad);
		std::wstring meminfo = buf;
		trect.top +=20;
		trect.bottom +=20;
		m_pRenderer->DrawText(meminfo.c_str(), meminfo.size(), &trect, 0xff00ffff);

		std::wstring strAdapterDes = GetRenderer()->GetAdapterDescription();
		trect.top +=20;
		trect.bottom +=20;
		m_pRenderer->DrawText(strAdapterDes.c_str(), strAdapterDes.size(), &trect, 0xff00ffff);
		
		wsprintf(buf, L"AvailableTextureMemory: %d M", GetSystemResourceManager()->GetAvailTextureMemory());
		trect.top += 20;
		trect.bottom += 20;
		m_pRenderer->DrawText(buf, _tcslen(buf), &trect, 0xff00ffff);
	}

	void Engine::DrawFrameStat()
	{
		FRECT lines[XPFRAMESTAT_TYPE_COUNT][XPFRAMESTAT_COUNT];
		for(int i=0; i<XPFRAMESTAT_COUNT; ++i)
		{
			int nowIndex = (frame_stat_index +i)%XPFRAMESTAT_COUNT;
			//每帧总时间
			lines[XPFRAMESTAT_FRAME_TIME][i].bottom = (float)m_iHeight;
			lines[XPFRAMESTAT_FRAME_TIME][i].top = m_iHeight - frame_stat[nowIndex] /500.f* m_iHeight;
			lines[XPFRAMESTAT_FRAME_TIME][i].left = (m_iWidth - XPFRAMESTAT_COUNT)/2 + i *1.f;
			lines[XPFRAMESTAT_FRAME_TIME][i].right = lines[XPFRAMESTAT_FRAME_TIME][i].left;

			//camera距离
			lines[XPFRAMESTAT_CAMERA_DIS][i] = lines[XPFRAMESTAT_FRAME_TIME][i];
			lines[XPFRAMESTAT_CAMERA_DIS][i].top = m_iHeight - camera_dis[nowIndex] /500.f* m_iHeight;

			//io
			lines[XPFRAMESTAT_FILE_IO][i] = lines[XPFRAMESTAT_FRAME_TIME][i];
			lines[XPFRAMESTAT_FILE_IO][i].top = m_iHeight - fileio_task_count[nowIndex] /500.f* m_iHeight;

			//task
			lines[XPFRAMESTAT_TASK_TIME][i] = lines[XPFRAMESTAT_FRAME_TIME][i];
			lines[XPFRAMESTAT_TASK_TIME][i].top = m_iHeight - frame_stat_task[nowIndex] /500.f* m_iHeight;
			
			//iapp的msg
			lines[XPFRAMESTAT_IAPP_MSG][i] = lines[XPFRAMESTAT_FRAME_TIME][i];
			lines[XPFRAMESTAT_IAPP_MSG][i].top = m_iHeight - frame_stat_msg[nowIndex] /500.f* m_iHeight;

		}
		ConfigManager* pCfm = GetConfigManager();
		if (pCfm->TestFrameStateInfo(XPFRAMESTAT_FRAME_TIME))
		{
			m_pRenderer->DrawLine(lines[XPFRAMESTAT_FRAME_TIME], XPFRAMESTAT_COUNT, pCfm->GetFrameStateColor(XPFRAMESTAT_FRAME_TIME));
			CRECT trect(CPOINT((m_iWidth+XPFRAMESTAT_COUNT)/2+30, (int)(m_iHeight - 30 /500.f* m_iHeight)), 80,30);
			m_pRenderer->DrawLine(FRECT(lines[XPFRAMESTAT_FRAME_TIME][XPFRAMESTAT_COUNT-1].right, (float)trect.top, (float)trect.right, (float)trect.top), 0xffffff00);
			std::wstring str = L"30 ms";
			m_pRenderer->DrawText(str.c_str(), str.size(), &trect,0xffffff00);
		}
		if (pCfm->TestFrameStateInfo(XPFRAMESTAT_CAMERA_DIS))
		{
			m_pRenderer->DrawLine(lines[XPFRAMESTAT_CAMERA_DIS], XPFRAMESTAT_COUNT, pCfm->GetFrameStateColor(XPFRAMESTAT_CAMERA_DIS));
		}
		if (pCfm->TestFrameStateInfo(XPFRAMESTAT_FILE_IO))
		{
			m_pRenderer->DrawLine(lines[XPFRAMESTAT_FILE_IO], XPFRAMESTAT_COUNT, pCfm->GetFrameStateColor(XPFRAMESTAT_FILE_IO));
		}
		if (pCfm->TestFrameStateInfo(XPFRAMESTAT_TASK_TIME))
		{
			m_pRenderer->DrawLine(lines[XPFRAMESTAT_TASK_TIME], XPFRAMESTAT_COUNT, pCfm->GetFrameStateColor(XPFRAMESTAT_TASK_TIME));
		}
		if (pCfm->TestFrameStateInfo(XPFRAMESTAT_IAPP_MSG))
		{
			m_pRenderer->DrawLine(lines[XPFRAMESTAT_IAPP_MSG], XPFRAMESTAT_COUNT, pCfm->GetFrameStateColor(XPFRAMESTAT_IAPP_MSG));
		}
	}

	void Engine::DrawPerformance()
	{
		const int fheight = 20;
		RECT trect;
		trect.left = 0;
		trect.top =180;
		trect.right =  500;
		trect.bottom = 180 + fheight;

		RenderPerformance rp = m_pRenderer->GetPerformance();
		wchar_t buffer[64];

		wsprintf(buffer, L"ManagedTextureCount   : %d", rp.m_iManagedTextureCount);
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff00);

		wsprintf(buffer, L"ManagedTextureMemory  : %d M", rp.m_iManagedTextureMemory/(1024*1024));
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffeebb00);

		float fTmp = rp.m_iTotalPixelRenderedPerFrame/(float)(m_iWidth*m_iHeight);
		int iTmp1 = (int)fTmp;
		int iTmp2 = (int)(10*(fTmp-iTmp1));
		wsprintf(buffer, L"AverageTextureLayer   : %d.%d", iTmp1, iTmp2);
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffeebb00);

		fTmp = m_pFileIOMan ? m_pFileIOMan->GetAverageReadSpeed() :0.f;
		iTmp1 = (int)fTmp;
		iTmp2 = (int)(10*(fTmp-iTmp1));
		wsprintf(buffer, L"AverageReadSpeed      : %d.%d M/s", iTmp1, iTmp2);
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffeebb00);

		wsprintf(buffer, L"DrawPrimitiveUp/Frame : %d", rp.m_iDrawPrimitiveUPCountPerFrame);
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff00);

		wsprintf(buffer, L"DrawPrimitive  /Frame : %d", rp.m_iDrawPrimitiveCountPerFrame);
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff00);

		wsprintf(buffer, L"DrawPicture    /Frame : %d", rp.m_iDrawPictureCountPerFrame);
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff00);

		wsprintf(buffer, L"DrawText       /Frame : %d", rp.m_iDrawTextCountPerFrame);
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff00);

		wsprintf(buffer, L"DrawLine       /Frame : %d", rp.m_iDrawLineCountPerFrame);
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff00);

		/*
		wsprintf(buffer, L"DrawTriangle   /Frame : %d", rp.m_iDrawTriangleCountPerFrame);
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff00);
		*/

		wsprintf(buffer, L"DrawBox        /Frame : %d", rp.m_iDrawBoxCountPerFrame);
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff00);


		wsprintf(buffer, L"TotalActiveParticles  : %d", m_pRenderer->GetParticleManager()->GetTotalNumActiveParticles());
		trect.top += fheight;
		trect.bottom += fheight;
		m_pRenderer->DrawText(buffer, _tcslen(buffer), &trect, 0xffffff00);

	}

	void Engine::DrawFPS()
	{
		//
		RECT trect;
		TCHAR szbuffer[256];
		int f1 = (int)(m_pRenderer->GetLatestFPS()*10);
		wsprintf(szbuffer, L"FPS: %3d.%1d", f1/10, f1%10);	
		trect.left = 0;
		trect.top =0;
		trect.right =  500;
		trect.bottom = 300;
		m_pRenderer->DrawText(szbuffer, 11, &trect, 0xffff00ff);

	}

	void Engine::DrawCameraPos()
	{
		// 视窗坐标
		RECT trect;
		TCHAR szbuffer[256];
		int f1 = (int)( (GetViewport().left + GetViewport().Width()/2) * 10 );
		wsprintf(szbuffer, L"X: %3d.%1d", f1/10, f1%10);	
		trect.left = GetViewport().Width()-100;
		trect.top =0;
		trect.right =  GetViewport().Width();
		trect.bottom = 120;
		m_pRenderer->DrawText(szbuffer, 8, &trect, 0xffff0000);
		f1 = (int)((GetViewport().top+GetViewport().Height()/2)*10);
		wsprintf(szbuffer, L"Y: %3d.%1d", f1/10, f1%10);
		trect.top += 20;
		trect.bottom += 20;
		m_pRenderer->DrawText(szbuffer, 8, &trect, 0xffff0000);
	}

	void Engine::RunUserTask()
	{
		__int64 tc = rdtsc();
		__int64 endtc = tc + ((__int64)m_userTasksTimePF[m_windowState]) * 1000 * m_pSystemResourceMan->GetCPUFrequency();
		RannableVect::iterator vIt, vIe;
		m_lckTask.Lock();
		vIt = m_userTasks.begin(), vIe = m_userTasks.end();
		for (;vIt!=vIe;++vIt)
		{
			m_userTasksList.push_back(*vIt);
		}
		m_userTasks.clear();
		m_lckTask.Unlock();
		while (endtc > rdtsc() && m_userTasksList.size() > 0)
		{
			m_userTasksList.front()->Run();
			m_userTasksList.pop_front();
		}
		frame_task += rdtsc() - tc;
	}

	void Engine::PutTask(IRunnable *task)
	{
		if( task == NULL )
			return;
		m_lckTask.Lock();
		m_userTasks.push_back(XHardRef<IRunnable>(task));
		m_lckTask.Unlock();
//		PostMessage(m_hWnd, XPMESSAGE_TASKWAKEUP, 0, 0);
	}

	void CALLBACK TimerProc(HWND , UINT , UINT_PTR idEvent, DWORD /*dwTime*/ )
	{
		((ITimer*)idEvent)->OnTimer();
	}

	bool Engine::ScheduleTimer(ITimer * timer, int period/* ms */)
	{
		if( timer == NULL || period < 50 || period > 3600*1000) 
			return false;
		return NULL != ::SetTimer(m_hWnd, (UINT_PTR)timer, period, TimerProc);
	}

	bool Engine::CancelTimer(ITimer * timer)
	{
		if( timer == NULL ) return false;
		return NULL != ::KillTimer(m_hWnd, (UINT_PTR)timer);
	}

	void Engine::CheckSpritePictureHandle()
	{
		if (m_SpriteHandle == INVALID_PICTURE_HANDLE)
		{			
			XPTEXTURE_FORMAT fmt = m_pRenderer->GetDisplayMode().bitperpix==32?XPTEXFMT_A8R8G8B8:XPTEXFMT_A4R4G4B4;
			m_SpriteHandle = m_pRenderer->CreateRenderTarget((int)TEMP_SPRITE_PICTURE_WIDTH, (int)TEMP_SPRITE_PICTURE_HEIGHT, fmt);
		}
		if (m_AlphaSpriteHandle == INVALID_PICTURE_HANDLE)
		{
			if (m_pConfigMan->IsRenderAlphaSprite())
			{
				const XDisplayMode& mode = m_pRenderer->GetDisplayMode();
				XPTEXTURE_FORMAT fmt = mode.bitperpix==32?XPTEXFMT_A8R8G8B8:XPTEXFMT_A4R4G4B4;
				m_AlphaSpriteHandle = m_pRenderer->CreateRenderTarget(mode.width,
					mode.height, fmt);
			}
		}

	}

	EngineSpriteHandle Engine::CreateEngineSprite(const std::wstring &modelname)
	{
		Sprite *pSprite = new ComponentSprite(this, modelname);
		if (pSprite)
		{
			pSprite->SetScreenCoord(true);
			pSprite->EnableShadow(false);
			m_engineSprites[++m_eshSeed] = pSprite;
			return m_eshSeed;
		}
		return INVALID_ENGINE_SPRITE_HANDLE;
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
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
		{
			iter->second->Render(NULL);
		}
	}

	void Engine::SetEngineSpriteLoc(EngineSpriteHandle handle, const Location& loc)
	{
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
		{
			iter->second->SetLocation(loc);
		}
	}

	void Engine::SetEngineSpriteDirection(EngineSpriteHandle handle, XPDIRECTION dir)
	{
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
		{
			iter->second->SetDirection(dir);
		}
	}

	void Engine::SetEngineSpriteDefaultAction(EngineSpriteHandle handle, const std::wstring &actname)
	{
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
		{
			iter->second->ReleaseAction(iter->second->GetDefaultAction());
			iter->second->HoldAction(actname);
			iter->second->SetDefaultAction(actname, XPSALT_ASYNC, 1.0f, false);
		}
	}

	void Engine::SetEngineSpriteAction(EngineSpriteHandle handle, const std::wstring &actname)
	{
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
		{
			iter->second->PlayAction(actname, XPSALT_SYNC, 1.0f);
		}
	}

	void Engine::SetEngineSpriteComponent(EngineSpriteHandle handle, int scid, const std::wstring& resource, Nuclear::XPCOLOR color)
	{
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
		{
			iter->second->SetComponent(scid, resource, color);
		}
	}

	void Engine::EnableEngineSpriteShadow(EngineSpriteHandle handle, bool b)
	{
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
		{
			iter->second->EnableShadow(b);
		}
	}

	void Engine::SetEngineSpriteScale(EngineSpriteHandle handle, float scale)
	{
		EngineSpriteMap::iterator iter = m_engineSprites.find(handle);
		if (iter != m_engineSprites.end())
		{
			iter->second->SetScale(scale);
		}
	}

	IEffect* Engine::CreateFontEffect(TextBlockHandle textHandle, const XPFontEffectParam& structFontParam)
	{
		FontEffect* pFontEffect = GetEffectManager()->CreateFontEffect(textHandle, structFontParam);
		
		if( pFontEffect != NULL )
		{
			m_effects.insert(pFontEffect);
		}

		return pFontEffect;
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
		tpEffect->Render(NULL);
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
		Effect *tpEffect = GetEffectManager()->CreateEffect(name, true);
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

	//声音相关
	void Engine::SetBGSoundMode(XPBGSoundMode mode) 
	{
		if (m_bgsoundMode == mode)
			return;
		if (!m_pSystemResourceMan->IsSoundInited())
			return;
		m_bgsoundMode = mode;
		m_pWorld->OnChangeBGSoundMode(mode);
	}

	CHECK_SOUND_STATE Engine::CheckBGSound(const std::wstring &fn, bool async)
	{
		if (!m_pSystemResourceMan->IsSoundInited())
			return CSS_FAILD;
		if (m_bgsoundMode == XPBGSM_AUTO)	//只要不是XPBGSM_AUTO就能播放
			return CSS_FAILD;
		XPISoundType type = XPIST_STREAM_SOUND;
		return m_pSoundManager->CheckSound(fn, async, this, type);
	}

	SoundHandle Engine::PlayBGSound(const std::wstring &fn, unsigned short fadeInTime, unsigned char type, unsigned char volume, bool looping, bool bGlobalFocus)
	{
		if (!m_pSystemResourceMan->IsSoundInited())
			return INVALID_SOUND_HANDLE;
		if (m_bgsoundMode == XPBGSM_AUTO)	//只要不是XPBGSM_AUTO就能播放
			return INVALID_SOUND_HANDLE;
		SoundHandle handle = m_pSoundManager->CreateStreamSound(fn, this);
		if (handle != INVALID_SOUND_HANDLE)
		{
			m_pSoundManager->SetSoundVolume(handle, volume);
			if (m_pSoundManager->PlayAndFadeIn(handle, fadeInTime, 0, type, looping))
				return handle;
		}
		return INVALID_SOUND_HANDLE;
	}

	bool Engine::FadeOutToStop(SoundHandle handle, unsigned short time)
	{
		if (!m_pSystemResourceMan->IsSoundInited())
			return false;
		if (m_bgsoundMode == XPBGSM_AUTO)	//只要不是XPBGSM_AUTO就能播放
			return false;
		return m_pSoundManager->FadeOutToStop(handle, time);
	}

	bool Engine::PushNextStreamSound(SoundHandle handle, const std::wstring &fn)
	{
		if (!m_pSystemResourceMan->IsSoundInited())
			return false;
		if (m_bgsoundMode == XPBGSM_AUTO)	//只要不是XPBGSM_AUTO就能播放
			return false;
		return m_pSoundManager->PushNextStreamSound(handle, fn);
	}

	bool Engine::PlayEffectSound(const std::wstring &fn, short priority, unsigned char type, unsigned char volume, unsigned char freq) 
	{ 
		if (!m_pSystemResourceMan->IsSoundInited())
			return false;
		return (m_pSoundManager->XPPlaySound(fn, priority, type, volume, freq, false) != INVALID_SOUND_HANDLE);
	}
	
	void Engine::SetSoundVolume(unsigned char type, float fVolume) 
	{ 
		if (!m_pSystemResourceMan->IsSoundInited())
			return;
		m_pSoundManager->SetSoundTypeVolume(type, fVolume);
	}

	float Engine::GetSoundVolume(unsigned char type) const
	{ 
		if (!m_pSystemResourceMan->IsSoundInited())
			return 0.0f;
		return m_pSoundManager->GetSoundTypeVolume(type); 
	}
	
	void Engine::StopTypeSound(unsigned char type)
	{ 
		if (!m_pSystemResourceMan->IsSoundInited())
			return ;
		if (m_pWorld && type == GetConfigManager()->GetEnvSoundType())
			m_pWorld->ClearActiveEnvSoundArea();
		m_pSoundManager->StopTypeSound(type); 
	}

	void Engine::EnableSound(unsigned char type, bool b)
	{
		if (!m_pSystemResourceMan->IsSoundInited())
			return;
		m_pSoundManager->EnableSoundType(type, b);
		if ((!b) && (type == GetConfigManager()->GetBGMType()))
		{
			m_pWorld->ResetBGSound();
		}
	}

	bool Engine::IsSoundEnable(unsigned char type) const
	{
		if (!m_pSystemResourceMan->IsSoundInited())
			return false;
		return m_pSoundManager->IsSoundTypeEnable(type);
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

	int Engine::GetAllEffectCount() const
	{
		int result = (int)m_effects.size();
		if (m_pWorld)
			result += m_pWorld->GetAllEffectCount();
		return result;
	}

	void Engine::GetAllEffectDetail(std::map<std::wstring, int> &detail) const
	{
		detail.clear();
		EffectSet::const_iterator it = m_effects.begin(), ie = m_effects.end();
		for (;it!=ie;++it)
		{
			detail[(*it)->GetName()] += 1;
		}
		if (m_pWorld)
			m_pWorld->GetAllEffectDetail(detail);
	}

	int Engine::GetAllSoundCount() const
	{
		if (m_pSoundManager)
			return m_pSoundManager->GetAllSoundCount();
		return 0;
	}

	void Engine::GetAllSoundDetail(std::vector<std::wstring> &detail) const
	{
		detail.clear();
		if (m_pSoundManager)
			m_pSoundManager->GetAllSoundDetail(detail);
	}

	PictureHandle Engine::CaptureWorld()
	{
		if( m_pRenderer == NULL ) return INVALID_PICTURE_HANDLE;
		if( m_pWorld == NULL ) return INVALID_PICTURE_HANDLE;
		if( m_pRenderer->TestDevice() != XPDS_OK) return INVALID_PICTURE_HANDLE;
		XDisplayMode mode = m_pRenderer->GetDisplayMode();
		PictureHandle result = m_pRenderer->CreateRenderTarget(mode.width, mode.height, mode.bitperpix==32?XPTEXFMT_A8R8G8B8:XPTEXFMT_A4R4G4B4);
		if (result == INVALID_PICTURE_HANDLE) return result;
		CheckSpritePictureHandle();
		m_pRenderer->JustBegin();
		if ( GetConfigManager()->IsRenderAlphaSprite() && m_AlphaSpriteHandle != INVALID_PICTURE_HANDLE)
		{
			m_pRenderer->PushRenderTarget(m_AlphaSpriteHandle);
			m_pRenderer->Clear(0x00000000);
			m_pRenderer->PopRenderTarget();
		}
		m_pRenderer->PushRenderTarget(result);
		m_pWorld->Render(m_tick, false);
		m_pRenderer->PopRenderTarget();
		m_pRenderer->JustEnd();
		return result;
	}

}