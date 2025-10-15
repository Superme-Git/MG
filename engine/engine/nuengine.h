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

			virtual void Run();// ��Ҫ�ڴ˺����н����������������׳��Զ����쳣
		};

		Engine() ;
		virtual ~Engine();

		cocos2d::CCLayer* m_engineLayer;
	public:
		int GetScreenWidth() { assert(m_iScreenWidth != 0 && "not inited"); return m_iScreenWidth; }
		int GetScreenHeight() { assert(m_iScreenHeight != 0 && "not inited"); return m_iScreenHeight; }
		//yanji 20150822 lua ����߼��ֱ���
		int GetLogicWidth();
		int GetLogicHeight();
		virtual void SetEngineLayer(cocos2d::CCLayer* aPLayer){m_engineLayer = aPLayer;}
		virtual cocos2d::CCLayer* GetEngineLayer(){return m_engineLayer;}
		void Draw(){ Render(false); }

		static Engine& GetInstance() { return s_instance; }

		// ����������·���ķ�����Ҫ�ڵ���Run()֮ǰ����
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

		// ������û��Լ������Ĵ��ڣ���ô��Ҫ�ڴ��ں����е��ô˺���
		virtual bool OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam, void* pAdditionalParam = NULL);
		// ������û��Լ������Ĵ��ڣ���ô��Ҫ�ڳ������(OnIdle)ʱ�е��ô˺���
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

		//�����Ӱ�ӵĲ���
		virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const;

		virtual NuclearWindowState GetWindowState() const { return m_windowState; }

		void SetTaskExecuteTime(NuclearWindowState type, int time) { m_userTasksTimePF[type] = time; }	//ÿ֡Task��ʱ�䣬��λ�Ǻ��룬Ĭ��ֵ30
		int GetTaskExecuteTime(NuclearWindowState type) const { return m_userTasksTimePF[type]; }

		void SetIOUpdateExecuteTime(int time) { m_pFileIOMan->SetUpdateTimePerFrame(time); }//ÿ֡�����̴߳����첽��������Դ��ʱ��
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

		//��ˮ��͸��Ч��
		bool IsEnableWade() const { return m_bEnableWade; }
		void SetEnableWade(bool b) { m_bEnableWade = b; }

		// ��windows��������ת���������������-��Ե�ǰviewport��
		void WindowToClient(Nuclear::NuclearPoint& point) {}

		// ���澫�����
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
		// ���ú�ȡ������Ч������Ҫ�ֹ�ȡ��������������
		virtual IEffect* SetEngineSpriteDurativeEffect(EngineSpriteHandle handle, const std::wstring &effect_name, const NuclearPoint& relpos, unsigned int flag);
		// ��ʱЧ������������Զ�ȡ��������������, times�ǲ��Ŵ���, �����������soundtype��������������
		virtual IEffect* PlayEngineSpriteEffect(EngineSpriteHandle handle, const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag, unsigned char soundtype);
		virtual void RemoveEngineSpriteDurativeEffect(EngineSpriteHandle handle, IEffect* pEffect);
		// ���ð������������˺����֣��ĳ�ʼYƫ�ƣ�����Ϊ��
		virtual int GetEngineSpriteBubbleItemOffset(EngineSpriteHandle handle) const;
		// ������Ч���
		virtual IEffect* CreateEffect(const std::wstring &effectname, bool async = true);
		virtual void DrawEffect(IEffect* pEffect);
		virtual void ReleaseEffect(IEffect* pEffect);

		//CacheEffect��ʲô��Ч�����ԣ���һ���ǽ����
		virtual void HoldEffect(const std::wstring &name);
		virtual void ReleaseEffect(const std::wstring &name);

		virtual PictureHandle CaptureWorld();//��ͼ

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

		virtual float GetCurMemSize() { return mCurMemSize; }//��õ�ǰ������ʹ�õ��ڴ���

		virtual float GeCurAvailableMemSize() { return mCurAvailableMemSize; }//��õ�ǰ���̿��õ��ڴ���

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
		EngineSpriteMap m_engineSprites;	//���澫��
		EngineSpriteMap m_attachedSprites;	//�����˵ľ�����
		EffectSet m_effects;
		EffectSet m_willBeDeleteEffects;
		std::map<std::wstring, Effect*> m_holdEffects;
		NuclearWindowState m_windowState;
		NuclearColor m_cleanColor;
		TimerManager   m_timerManager;

		bool m_bEnableWade;
		bool m_bAsyncRead;		//�Ƿ��첽
		bool m_bUserWnd;
		bool m_bSizeBox;		//�Ƿ����϶���С

		bool m_bHasMaximizbox;	//�Ƿ��������С����ť
		bool m_bSetInactiveTimer;//�Ƿ�����InactiveTimer

		unsigned char m_InitState;//0 �����ʼ�� 1 IApp��ʼ�� 2����ʼ�����
		int m_nInitStep;
		int m_nAppInitCount;
		int m_nSprStepCount;

		int m_iHeight;
		int m_iWidth;
		int m_iOldColorMode; // 16 or 32
		int m_iCurColorMode; // 16 or 32

		float m_fBgSoundVolume;
		float m_fEffectSoundVolume;
		float m_fGameTimeSpeedScale;		//ʱ���������ı���

		RestoreTask m_restoreTask;
		unsigned int m_tick;	//��һ�θ��µ�ʱ���
		unsigned int m_LastFrameTick;//��һ֡��ʱ���

		unsigned int m_uiSmoothDeltaLimit;
		unsigned int m_uiSmoothDeltaCount;

		PictureHandle m_SpriteHandle;		//�еľ�����Ȼ������Handle�ϣ�Ȼ����������ͼ��
		PictureHandle m_AlphaSpriteHandle;	//XPSTT_SPRITE_TRANSLUCENTģʽ�£�����ử�����Handle�ϣ�Ȼ���͸��������ͼ��

		// tasks
		NuclearLock m_lckTask;
		RannableVect m_userTasks;
		RannableList m_userTasksList;
		int m_userTasksTimePF[XPWS_MINIMIZE + 1];	//ÿִ֡�е�Task��ʱ��

		int m_n3DSpriteCount;						// ��ǰһ�����ٸ�3D����
		int m_nAlwaysUpdate3DSpriteCount;			// ÿ֡�϶����µ�3D���������
		int m_n3DSpriteBatchUpdateCount;			// ÿ֡���¶��ٸ�3D����
		int m_nCurUpdateCount;						// ��ǰ֡���ܸ��¶��ٸ�(�ǿ϶����µ�)
		int m_nPitch;								// ���¶���������ȫ��������

		astar::PathFinder *m_pPathFinder; // default pathfinder can not find any path
	
		DWORD m_renderFlag;
		bool m_bEnableMipMap;
		bool m_bIsMultiSampleTypeEnable;		//�Ƿ�򿪿����
		unsigned char m_3dActionSoundType;
		unsigned char m_3dEffectSoundType;

		// ������
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

		// FPSͳ��
		unsigned int mDeltaTime;
		unsigned int mCurDeltaTime;
		unsigned int mFrameTimeCount;
		unsigned int mFrameCount;
		unsigned int mTimeCount;
		float		 mFps;

		// �ڴ����/֡������
		float        mGCMemVolume;			//��Ҫ�����������յ��ڴ���ռ���ٽ�ֵ
		float		 mMustGCMemVolume;		//��������������յ��ڴ���ռ���ٽ�ֵ
		float		 mAvailableMemVolume;   //�����ڴ�С�ڴ�ֵ�ű�������������յ��ٽ�ֵ
		float		 mGCMemFPSValue;		//��Ҫ�����������յ�֡����ֵ
		float        mGetPicNeedFPSValue;	//��Ҫͬ������ͼƬʱ��Ҫ���֡��ֵ
		int			 mMaxTimeIntervalPreFPS;//ÿ֡������ʱ���������������ֵ������ֵ��ʱ�䣩
		int			 mLimitFpsTime;			//��������֡ʱ��
		int			 mLastFrameCount;		//��һ֡�ļ�����
		bool		 mRealFrame;			//��ǰ֡�Ƿ�Ϊ��Ҫ��ʵ��Ⱦ
		float        mCurMemSize;			//��ǰ������ʹ�õ��ڴ���
		float        mCurAvailableMemSize;	//��ǰ���̿��õ��ڴ���

		unsigned int mExcCounter;

		static Engine s_instance;

	private:
		Engine(const Engine&);
		Engine& operator=(const Engine&);
	};

}

#endif