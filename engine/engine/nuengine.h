#ifndef _Nuclear_ENGINE_H__
#define _Nuclear_ENGINE_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../common/nuxptypes.h"
#include "../common/nuxlock.h"
#include "../common/nuxref.h"
#include "../common/nufileiomanager.h"
#include "../world/nuworld.h"
#include "../nuiapp.h"
#include "../nuiengine.h"
#include "nuenginebase.h"
#include "nuenv.h"
#include "nuquery.h"
#include "nusystemresourcemanager.h"
#include "nuenginetimer.h"
#include "nuspinemanager.h"

namespace Nuclear
{
	class Sprite;
	class StepSoundType;
	class FontEffect;
	class LJFMLog;
	class MA_Impl;
    
	class Engine : public IEngine, public EngineBase
	{
		class RestoreTask : public INuclearRunnable
		{
		public:
			virtual ~RestoreTask();

			virtual void Run();// 不要在此函数中进行阻塞操作或者抛出自定义异常
		};

		Engine() ;
		virtual ~Engine();

		cocos2d::CCLayer* m_engineLayer;
	public:
		int GetScreenWidth() { assert(m_iScreenWidth != 0 && "not inited"); return m_iScreenWidth; }
		int GetScreenHeight() { assert(m_iScreenHeight != 0 && "not inited"); return m_iScreenHeight; }
		//yanji 20150822 lua 获得逻辑分辨率
		int GetLogicWidth();
		int GetLogicHeight();
		virtual void SetEngineLayer(cocos2d::CCLayer* aPLayer){m_engineLayer = aPLayer;}
		virtual cocos2d::CCLayer* GetEngineLayer(){return m_engineLayer;}
		void Draw(){ Render(false); }

		static Engine& GetInstance() { return s_instance; }

		// 下面两个设路径的方法，要在调用Run()之前调用
		virtual bool SetInfoLogPath(const std::wstring &fn);
		virtual bool SetErrorLogPath(const std::wstring &fn);
		virtual bool SetSegmpakLogPath(const std::wstring &fn);

		const std::wstring &GetCurrentDirectory() const { return m_pFileIOMan->GetCurrentDirectory(); }

		template<class T>
		bool SetLogPath(const std::wstring &fn)	{ size_t length = fn.length(); if (length >= MAX_PATH) return false; return true; }

		virtual IEnv* GetEnv() { return m_pEnv; }
		virtual const IEnv* GetEnv() const { return m_pEnv; }

		virtual bool Run(const EngineParameter &ep);
		virtual bool Exit();
		virtual void SetGameTimeSpeedScale(float fScale) { m_fGameTimeSpeedScale = fScale; }
		virtual float GetGameTimeSpeedScale() const { return m_fGameTimeSpeedScale; }
		bool Tick() { return true; }
		void Render(bool controlFPS);
		bool IsMultiSampleTypeEnable() const { return m_bIsMultiSampleTypeEnable; }

		virtual void PutTask(INuclearRunnable *task);
		virtual bool ScheduleTimer(INuclearTimer * timer, int period/* ms */);
		virtual bool CancelTimer(INuclearTimer * timer);

		// 如果是用户自己创建的窗口，那么需要在窗口函数中调用此函数
		virtual bool OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam, void* pAdditionalParam = NULL);
		// 如果是用户自己创建的窗口，那么需要在程序空闲(OnIdle)时中调用此函数
		virtual void OnIdle();

		void SetSystemLevel(NuclearComputerConfigType l) {}
		virtual INuclearFileIO* GetFileIO() { return m_pFileIOMan; }
		virtual IApp* GetApp() { return m_pApp; }
		virtual IWorld* GetWorld() { return m_pWorld; }
		virtual const IWorld* GetWorld() const { return m_pWorld; }
		virtual World* GetXPWorld() { return m_pWorld; }
		virtual float GetWorldScale() const { return m_pWorld->GetScale(); }
		virtual Renderer* GetRenderer();
		virtual ConfigManager* GetConfigManager() { return m_pConfigMan; }
		virtual const SpriteManager* GetSpriteManager() const { return m_pSprMan; }
		virtual const AniManager* GetAniManager() const { return m_pAniMan; }
		virtual SystemResourceManager* GetSystemResourceManager() { return m_pSystemResourceMan; }
		virtual PictureHandle GetSpritePictureHandle() const { return m_SpriteHandle; }
		virtual PictureHandle GetAlphaSpritePictureHandle() const { return m_AlphaSpriteHandle; }
		virtual NuclearFileIOManager* GetFileIOManager() { return m_pFileIOMan; }
		virtual SpriteManager* GetSpriteManager() { return m_pSprMan; }
		virtual AniManager* GetAniManager() { return m_pAniMan; }
		virtual SpineManager* GetSpineManager() { return m_pSpineMan; }
		virtual EffectManager* GetEffectManager() { return m_pEffectMan; }
		virtual astar::PathFinder * GetPathFinder() { return m_pPathFinder; }
		virtual int GetTick() const { return m_tick; }
		virtual bool IsAsyncRead() const { return m_bAsyncRead; }
		virtual bool GetWaterDepth(const NuclearLocation& pt, unsigned int &nDepth) const;
		virtual void EnableParticle(bool flag);
		virtual NuclearRect GetViewport() const { return m_pWorld->GetViewport(); }

		//精灵的影子的参数
		virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const;

		virtual NuclearWindowState GetWindowState() const { return m_windowState; }

		void SetTaskExecuteTime(NuclearWindowState type, int time) { m_userTasksTimePF[type] = time; }	//每帧Task的时间，单位是毫秒，默认值30
		int GetTaskExecuteTime(NuclearWindowState type) const { return m_userTasksTimePF[type]; }

		void SetIOUpdateExecuteTime(int time) { m_pFileIOMan->SetUpdateTimePerFrame(time); }//每帧在主线程创建异步回来的资源的时间
		int GetIOUpdateExecuteTime() const { return m_pFileIOMan->GetUpdateTimePerFrame(); }

		virtual int GetAllSpriteCount() const;
		virtual void GetAllSpriteDetail(std::map<std::wstring, int> &detail) const;

		virtual size_t GetTaskListCount() const { return m_userTasksList.size(); }
		virtual size_t GetQueueOfIOTaskSize() const { return m_pFileIOMan->GetQueueOfIOTaskSize(); }

		void SetAniPicGCTime(int t);
		int GetAniPicGCTime() const;

		void SetAniXapGCTime(int t);
		int GetAniXapGCTime() const;

		void OnRendererRestore();

		void RunUserTask();
		void StopRunUserTask();
		void OnTimer(int iPeriod);

		virtual int GetGameTime() { if (m_pWorld) return m_pWorld->GetGameTime(); else return 0;}

		//涉水半透明效果
		bool IsEnableWade() const { return m_bEnableWade; }
		void SetEnableWade(bool b) { m_bEnableWade = b; }

		// 把windows窗口坐标转换成世界相对坐标-相对当前viewport。
		void WindowToClient(Nuclear::NuclearPoint& point) {}

		// 界面精灵相关
		virtual EngineSpriteHandle CreateEngineSprite(const std::wstring &modelname, bool async, bool isUISprite=false);
		virtual bool SetEngineSpriteModel(EngineSpriteHandle handle, const std::wstring &modelname, bool async);
		virtual void ReleaseEngineSprite(EngineSpriteHandle handle);
		virtual void RendererEngineSprite(EngineSpriteHandle handle);
		virtual void SetEngineSpriteLoc(EngineSpriteHandle handle, const NuclearLocation& loc);
		virtual void SetEngineSpriteDirection(EngineSpriteHandle handle, NuclearDirection dir);
		virtual void SetEngineSpriteDirection(EngineSpriteHandle handle, const NuclearPoint &target);
		virtual void SetEngineSpriteDefaultAction(EngineSpriteHandle handle, const std::wstring &actname);
		virtual void SetEngineSpriteAction(EngineSpriteHandle handle, const std::wstring &actname);
		virtual void SetEngineSpriteComponent(EngineSpriteHandle handle, int scid, const std::wstring& resource, Nuclear::NuclearColor color = 0xffffffff/*XRGB*/);
		virtual void EnableEngineSpriteShadow(EngineSpriteHandle handle, bool b);
		virtual void SetEngineSpriteScale(EngineSpriteHandle handle, float scale);
		virtual void SetEngineSpriteAlpha(EngineSpriteHandle handle, unsigned char alpha);
		virtual bool GetEngineSpriteActionTimeByName(EngineSpriteHandle handle, const std::wstring& action_name, float &time);	// yeqing 2015-11-07
		// 设置和取消持续效果，需要手工取消，个数无上限
		virtual IEffect* SetEngineSpriteDurativeEffect(EngineSpriteHandle handle, const std::wstring &effect_name, const NuclearPoint& relpos, unsigned int flag);
		// 临时效果，播放完毕自动取消，个数无上限, times是播放次数, 如果有声音，soundtype就是其声音类型
		virtual IEffect* PlayEngineSpriteEffect(EngineSpriteHandle handle, const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag, unsigned char soundtype);
		virtual void RemoveEngineSpriteDurativeEffect(EngineSpriteHandle handle, IEffect* pEffect);
		// 获得冒泡物件（例如伤害文字）的初始Y偏移，向上为正
		virtual int GetEngineSpriteBubbleItemOffset(EngineSpriteHandle handle) const;
		// 界面特效相关
		virtual IEffect* CreateEffect(const std::wstring &effectname, bool async = true);
		virtual void DrawEffect(IEffect* pEffect);
		virtual void ReleaseEffect(IEffect* pEffect);

		//CacheEffect，什么特效都可以，不一定是界面的
		virtual void HoldEffect(const std::wstring &name);
		virtual void ReleaseEffect(const std::wstring &name);

		virtual PictureHandle CaptureWorld();//截图

		virtual void SetCleanColor(NuclearColor color) { m_cleanColor = color; }
		virtual NuclearColor GetCleanColor() const { return m_cleanColor; }

		virtual void GCNow();
		virtual void setGCCooldown(int time);

		virtual int GetWidth() const { return m_iWidth; }
		virtual int GetHeight() const { return m_iHeight; }

		virtual unsigned int GetTimeCount() { return mTimeCount; }

		virtual void  SetGCMemVolume(float GCMemVolume) { mGCMemVolume = GCMemVolume; }
		virtual float GetGCMemVolume() { return mGCMemVolume; }

		virtual void  SetMustGCMemVolume(float mustGCMemVolume) { mMustGCMemVolume = mustGCMemVolume; }
		virtual float GetMustGCMemVolume() { return mMustGCMemVolume; }

		virtual void  SetAvailableMemVolume(float availableMemVolume) { mAvailableMemVolume = availableMemVolume; }
		virtual float GetAvailableMemVolume() { return mAvailableMemVolume; }

		virtual void  SetGCMemFPSValue(float GCMemFPSValue) { mGCMemFPSValue = GCMemFPSValue; }
		virtual float GetGCMemFPSValue() { return mGCMemFPSValue; }

		virtual void  SetGetPicNeedFPSValue(float GetPicNeedFPSValue) { mGetPicNeedFPSValue = GetPicNeedFPSValue; }
		virtual float GetGetPicNeedFPSValue() { return mGetPicNeedFPSValue; }

		virtual void  SetMaxTimeIntervalPreFPS(int maxTimeIntervalPreFPS) { mMaxTimeIntervalPreFPS = maxTimeIntervalPreFPS; }
		virtual int	  GetMaxTimeIntervalPreFPS() { return mMaxTimeIntervalPreFPS; }

		virtual void  SetLimitFpsTime(int limitFpsTime) { mLimitFpsTime = limitFpsTime; }
		virtual int   GetLimitFpsTime() { return mLimitFpsTime; }

		virtual void SetSmoothDeltaLimit(unsigned int uiSmoothDeltaLimit);
		virtual unsigned int GetSmoothDeltaLimit();

		virtual void SetSmoothDeltaCount(unsigned int uiSmoothDeltaCount);
		virtual unsigned int GetSmoothDeltaCount();

		virtual void GetCurMemInfo();

		virtual float GetCurMemSize() { return mCurMemSize; }//获得当前进程所使用的内存量

		virtual float GeCurAvailableMemSize() { return mCurAvailableMemSize; }//获得当前进程可用的内存量

		virtual unsigned int GetThreadExcCounter() { return mExcCounter; }

		virtual unsigned int GetCurDeltaTime() const { return mCurDeltaTime; }

		virtual Sprite* FindAndAddEngineSprite(EngineSpriteHandle handle);

		virtual void SetStepLoadTexture(bool b);
		virtual bool GetStepLoadTexture() const;

		virtual void  SetLimitFireThreadSecond(int limitFireThreadCount);
		virtual int   GetLimitFireThreadSecond();

	private:
		typedef std::set<Effect*> EffectSet;

		Nuclear_ResState Check3DResExisting(const std::wstring &pathIn3D, bool isModel);
		void MainLoop();
		void UpdateBeforeRender(int delta);
		virtual void UpdateSpriteAction(DWORD delta);
		inline const Sprite* CheckEngineSpriteIncludeAttached(EngineSpriteHandle handle) const;
		inline const Sprite* CheckEngineSprite(EngineSpriteHandle handle) const;
		inline Sprite* CheckEngineSpriteIncludeAttached(EngineSpriteHandle handle);
		inline Sprite* CheckEngineSprite(EngineSpriteHandle handle);

		bool Init(int step);
		void InitNotify();

		void  ResetFPSCounter();
		float GetFPS() const;
		float GetLatestFPS() const;

	private:
		typedef std::map<EngineSpriteHandle, Sprite*> EngineSpriteMap;
		enum XPEngineInitStep
		{
			XPEIS_SOURCE_CONFIG_CURSOR_MAN = 0,
			XPEIS_ENV_SOUND_ANI_MAN,
			XPEIS_SPRITE_PRE_INIT,
			XPEIS_SPRITE_INITING,
			XPEIS_SPRITE_POST_INIT,
			XPEIS_EFFECT_QUERY_STEPSOUND,
			XPEIS_INPUT_WORLD,
		};
		typedef std::vector<NuclearHardRef<INuclearRunnable> > RannableVect;
		typedef std::list<NuclearHardRef<INuclearRunnable> > RannableList;
	
		typedef std::map<std::wstring, Nuclear_ResState> SegmpakResMap;

		LJFMLog *m_pLJFMLog;
		Env *m_pEnv;
		IApp *m_pApp;
		SystemResourceManager *m_pSystemResourceMan;
		NuclearFileIOManager *m_pFileIOMan;
		SpriteManager *m_pSprMan;
		AniManager *m_pAniMan;
		SpineManager *m_pSpineMan;
		EffectManager *m_pEffectMan;
		World* m_pWorld;	// the world
		Renderer *m_pRenderer; // renderer
		ConfigManager *m_pConfigMan;
		EngineSpriteHandle	m_eshSeed;
		EngineSpriteMap m_engineSprites;	//界面精灵
		EngineSpriteMap m_attachedSprites;	//被绑了的精灵们
		EffectSet m_effects;
		EffectSet m_willBeDeleteEffects;
		std::map<std::wstring, Effect*> m_holdEffects;
		NuclearWindowState m_windowState;
		NuclearColor m_cleanColor;
		TimerManager   m_timerManager;

		bool m_bEnableWade;
		bool m_bAsyncRead;		//是否异步
		bool m_bUserWnd;
		bool m_bSizeBox;		//是否能拖动大小

		bool m_bHasMaximizbox;	//是否有最大化最小化按钮
		bool m_bSetInactiveTimer;//是否设了InactiveTimer

		unsigned char m_InitState;//0 引擎初始化 1 IApp初始化 2：初始化完毕
		int m_nInitStep;
		int m_nAppInitCount;
		int m_nSprStepCount;

		int m_iHeight;
		int m_iWidth;
		int m_iOldColorMode; // 16 or 32
		int m_iCurColorMode; // 16 or 32

		float m_fBgSoundVolume;
		float m_fEffectSoundVolume;
		float m_fGameTimeSpeedScale;		//时间快进慢播的倍数

		RestoreTask m_restoreTask;
		unsigned int m_tick;	//上一次更新的时间戳
		unsigned int m_LastFrameTick;//上一帧的时间戳

		unsigned int m_uiSmoothDeltaLimit;
		unsigned int m_uiSmoothDeltaCount;

		PictureHandle m_SpriteHandle;		//有的精灵会先画到这个Handle上，然后再贴到地图上
		PictureHandle m_AlphaSpriteHandle;	//XPSTT_SPRITE_TRANSLUCENT模式下，精灵会画到这个Handle上，然后半透明贴到地图上

		// tasks
		NuclearLock m_lckTask;
		RannableVect m_userTasks;
		RannableList m_userTasksList;
		int m_userTasksTimePF[XPWS_MINIMIZE + 1];	//每帧执行的Task的时间

		int m_n3DSpriteCount;						// 当前一共多少个3D精灵
		int m_nAlwaysUpdate3DSpriteCount;			// 每帧肯定更新的3D精灵的数量
		int m_n3DSpriteBatchUpdateCount;			// 每帧更新多少个3D精灵
		int m_nCurUpdateCount;						// 当前帧还能更新多少个(非肯定更新的)
		int m_nPitch;								// 更新多少批才能全部更新完

		astar::PathFinder *m_pPathFinder; // default pathfinder can not find any path
	
		DWORD m_renderFlag;
		bool m_bEnableMipMap;
		bool m_bIsMultiSampleTypeEnable;		//是否打开抗锯齿
		unsigned char m_3dActionSoundType;
		unsigned char m_3dEffectSoundType;

		// 调试用
		enum { XPFRAMESTAT_COUNT = 512 };
		float frame_stat[XPFRAMESTAT_COUNT];
		float camera_dis[XPFRAMESTAT_COUNT];
		float fileio_task_count[XPFRAMESTAT_COUNT];
		float frame_stat_engine[XPFRAMESTAT_COUNT];
		float frame_stat_task[XPFRAMESTAT_COUNT];
		float frame_stat_msg[XPFRAMESTAT_COUNT];
		int64 frame_stat_index;
		int64 frame_task;
		static int64 frame_msg;
		int m_iScreenWidth;
		int m_iScreenHeight;

		// FPS统计
		unsigned int mDeltaTime;
		unsigned int mCurDeltaTime;
		unsigned int mFrameTimeCount;
		unsigned int mFrameCount;
		unsigned int mTimeCount;
		float		 mFps;

		// 内存控制/帧率限制
		float        mGCMemVolume;			//需要进入垃圾回收的内存所占的临界值
		float		 mMustGCMemVolume;		//必须进入垃圾回收的内存所占的临界值
		float		 mAvailableMemVolume;   //可用内存小于此值才必须进入垃圾回收的临界值
		float		 mGCMemFPSValue;		//需要进入垃圾回收的帧率阈值
		float        mGetPicNeedFPSValue;	//需要同步加载图片时所要求的帧率值
		int			 mMaxTimeIntervalPreFPS;//每帧间最大的时间间隔（如果超过此值即按此值算时间）
		int			 mLimitFpsTime;			//设置限制帧时间
		int			 mLastFrameCount;		//上一帧的计数器
		bool		 mRealFrame;			//当前帧是否为需要真实渲染
		float        mCurMemSize;			//当前进程所使用的内存量
		float        mCurAvailableMemSize;	//当前进程可用的内存量

		unsigned int mExcCounter;

		static Engine s_instance;

	private:
		Engine(const Engine&);
		Engine& operator=(const Engine&);
	};

}

#endif