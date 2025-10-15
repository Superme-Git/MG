#ifndef __Nuclear_ENGINE_H
#define __Nuclear_ENGINE_H

#if _MSC_VER > 1000
#pragma once
#endif


#include "..\common\xptypes.h"
#include "..\common\xlock.h"
#include "..\common\xref.h"
#include "..\common\fileiomanager.h"
#include "..\world\world.h"
#include "..\sound\directsoundmanager.h"
#include "..\iapp.h"
#include "..\iengine.h"
#include "enginebase.h"
#include "env.h"
#include "query.h"

#include "systemresourcemanager.h"

namespace Nuclear
{

class CursorManager;
class Sprite;
class StepSoundType;
class Effect;
class FontEffect;
class PFSLog;
class Engine : public IEngine/*对外接口*/, public EngineBase, 
	public ISoundNotify, public ISoundLoadNotify
{
	class RestoreTask : public IRunnable
	{
	public:
		virtual ~RestoreTask() { }

		// 不要在此函数中进行阻塞操作或者抛出自定义异常
		virtual void Run();
	};

	//enum
	//{
	//	XPMESSAGE_TASKWAKEUP = WM_USER+1
	//};

	// 构造析构
	Engine() ;
	virtual ~Engine();

	// 操作
public:

	static Engine& GetInstance() { return s_instance; }

	virtual IEnv* GetEnv() { return m_pEnv; }
	virtual const IEnv* GetEnv() const { return m_pEnv; }
	virtual IQuery* GetQuery() { return m_pQuery; }

	virtual bool Run(const EngineParameter &ep);
	virtual bool Exit();
	void Render(bool controlFPS);

	virtual void PutTask(IRunnable *task);
	virtual bool ScheduleTimer(ITimer * timer, int period/* ms */);
	virtual bool CancelTimer(ITimer * timer);

	//如果子窗口失去或者获得Active状态，那么马上调用这个接口：
	virtual void OnActive(bool active);

	// 如果是用户自己创建的窗口，那么需要在窗口函数中调用此函数
	virtual bool OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam );
	// 如果是用户自己创建的窗口，那么需要在程序空闲(OnIdle)时中调用此函数
	virtual void OnIdle();

	virtual void SetEntitativeTitleManager(IEntitativeTitleManager* pETM) { m_pETM = pETM; }
	virtual IEntitativeTitleManager *GetEntitativeTitleManager() { return m_pETM; }
	virtual IFileIO* GetFileIO() { return m_pFileIOMan; }
	virtual IApp* GetApp() { return m_pApp; }
	virtual IWorld* GetWorld() { return m_pWorld; }
	virtual const IWorld* GetWorld() const { return m_pWorld; }
	virtual World* GetXPWorld() { return m_pWorld; }
	virtual float GetWorldScale() const { return m_pWorld->GetScale(); }
	virtual Renderer* GetRenderer( HWND hwnd=NULL );
	virtual ConfigManager* GetConfigManager() { return m_pConfigMan; }
	virtual const SpriteManager* GetSpriteManager() const { return m_pSprMan; }
	virtual const AniManager* GetAniManager() const { return m_pAniMan; }
	virtual CursorManager* GetCursorManager() { return m_pCursorMan; }
	virtual DirectSoundManager* GetSoundManager() { return m_pSoundManager; }
	virtual SystemResourceManager* GetSystemResourceManager() { return m_pSystemResourceMan; }
	virtual PictureHandle GetSpritePictureHandle() const { return m_SpriteHandle; }
	virtual PictureHandle GetAlphaSpritePictureHandle() const { return m_AlphaSpriteHandle; }
	virtual CFileIOManager* GetFileIOManager() { return m_pFileIOMan; }
	virtual SpriteManager* GetSpriteManager() { return m_pSprMan; }
	virtual AniManager* GetAniManager() { return m_pAniMan; }
	virtual EffectManager* GetEffectManager() { return m_pEffectMan; }
	virtual astar::PathFinder * GetPathFinder() { return m_pPathFinder; }
	virtual int GetTick() const { return m_tick; }
	virtual StepSoundType* GetStepSoundType() const { return m_pStepSoundType; }
	virtual bool IsAsyncRead() const { return m_bAsyncRead; }
	virtual unsigned int GetWaterDepth(const Location& pt) const
	{
		if ((!m_pWorld) || (!m_bEnableWade))
			return 0;
		return m_pWorld->GetWaterDepth(pt);
	}
	virtual bool GetLightPos(CPOINT &pos) 
	{
		if (!m_pWorld)
			return false;
		return m_pWorld->GetLightPos(pos);
	}

	virtual CRECT GetViewport() const { return m_pWorld->GetViewport(); }

	//精灵的影子的参数
	virtual void GetSpriteShadowParam(float &shearX, float &scalingY) const 
	{
		if (!m_pWorld)
			return;
		m_pWorld->GetSpriteShadowParam(shearX, scalingY);
	}
	HWND GetWindow() const { return m_hWnd; }

	bool SetDisplayMode(const XDisplayMode &dmode);

	void SetWindowState(XPWINDOW_STATE state);
	virtual XPWINDOW_STATE GetWindowState() const { return m_windowState; }

	void SetTaskExecuteTime(XPWINDOW_STATE type, int time) { m_userTasksTimePF[type] = time; }	//每帧Task的时间，单位是毫秒，默认值30
	int GetTaskExecuteTime(XPWINDOW_STATE type) const { return m_userTasksTimePF[type]; }

	void SetIOUpdateExecuteTime(int time) { m_pFileIOMan->SetUpdateTimePerFrame(time); }//每帧在主线程创建异步回来的资源的时间
	int GetIOUpdateExecuteTime() const { return m_pFileIOMan->GetUpdateTimePerFrame(); }

	void SetTaskExecuteCount(XPWINDOW_STATE type, int count) { m_userTaskListECPF[type] = count; }//每帧执行的Task的个数，-1为不限制
	int GetTaskExecuteCount(XPWINDOW_STATE type) const { return m_userTaskListECPF[type]; }

	void SetTaskListMaxRemainCount(XPWINDOW_STATE type, int count) { m_userTaskListRCPF[type] = count; }//每帧剩下的Task最多的个数，-1为不限制
	int GetTaskListMaxRemainCount(XPWINDOW_STATE type) const { return m_userTaskListRCPF[type]; }

	virtual int GetAllSpriteCount() const;
	virtual void GetAllSpriteDetail(std::map<std::wstring, int> &detail) const;

	virtual int GetAllEffectCount() const;
	virtual void GetAllEffectDetail(std::map<std::wstring, int> &detail) const;

	virtual int GetAllSoundCount() const;
	virtual void GetAllSoundDetail(std::vector<std::wstring> &detail) const;

	virtual size_t GetTaskListCount() const { return m_userTasksList.size(); }
	virtual size_t GetQueueOfIOTaskSize() const { return m_pFileIOMan->GetQueueOfIOTaskSize(); }

	void SetAniPicGCTime(int t);
	int GetAniPicGCTime() const;

	void SetAniXapGCTime(int t);
	int GetAniXapGCTime() const;


	void OnSetCursor();
	void OnRendererRestore();

	void RunUserTask();
	void OnTimer(int iPeriod);

	virtual int GetGameTime() { if (m_pWorld) return m_pWorld->GetGameTime(); else return 0;}

	//涉水半透明效果
	bool IsEnableWade() const { return m_bEnableWade; }
	void SetEnableWade(bool b) { m_bEnableWade = b; }

	// 把windows窗口坐标转换成世界相对坐标-相对当前viewport。
	void WindowToClient( Nuclear::CPOINT& point);	

	//声音相关
	//背景音乐相关//////////////////////
	virtual void SetBGSoundMode(XPBGSoundMode mode);
	virtual XPBGSoundMode GetBGSoundMode() const { return m_bgsoundMode; }
	virtual CHECK_SOUND_STATE CheckBGSound(const std::wstring &fn, bool async);
	virtual SoundHandle PlayBGSound(const std::wstring &fn, unsigned short fadeInTime, unsigned char type, unsigned char volume, bool looping, bool bGlobalFocus);
	virtual bool FadeOutToStop(SoundHandle handle, unsigned short time);
	virtual bool PushNextStreamSound(SoundHandle handle, const std::wstring &fn);
	
	//效果音相关//////////////////////
	virtual bool PlayEffectSound(const std::wstring &fn, short priority, unsigned char type, unsigned char volume = 255, unsigned char freq = 128);
	
	//总体控制////////////////////////
	virtual void SetSoundVolume(unsigned char type, float fVolume);
	virtual float GetSoundVolume(unsigned char type) const;
	virtual void StopTypeSound(unsigned char type);
	virtual void EnableSound(unsigned char type, bool b);
	virtual bool IsSoundEnable(unsigned char type) const;

	//回调///////////////////////////
	virtual void OnSoundLoaded(const std::wstring &fn, bool succ) override { m_pApp->OnBGMLoaded(fn, succ); }
	virtual void OnSoundGoingToEnd(SoundHandle handle) override { m_pApp->OnBGMGoingToEnd(handle); }
	virtual void OnSoundPlayEnd(SoundHandle handle) override { m_pApp->OnBGMPlayEnd(handle); }

	// 界面精灵相关
	virtual EngineSpriteHandle CreateEngineSprite(const std::wstring &modelname);
	virtual void ReleaseEngineSprite(EngineSpriteHandle handle);
	virtual void RendererEngineSprite(EngineSpriteHandle handle);
	virtual void SetEngineSpriteLoc(EngineSpriteHandle handle, const Location& loc);
	virtual void SetEngineSpriteDirection(EngineSpriteHandle handle, XPDIRECTION dir);
	virtual void SetEngineSpriteDefaultAction(EngineSpriteHandle handle, const std::wstring &actname);
	virtual void SetEngineSpriteAction(EngineSpriteHandle handle, const std::wstring &actname);
	virtual void SetEngineSpriteComponent(EngineSpriteHandle handle, int scid, const std::wstring& resource, Nuclear::XPCOLOR color=0xffffffff/*XRGB*/);
	virtual void EnableEngineSpriteShadow(EngineSpriteHandle handle, bool b);
	virtual void SetEngineSpriteScale(EngineSpriteHandle handle, float scale);

	//文字特效
	virtual IEffect* CreateFontEffect(TextBlockHandle textHandle, const XPFontEffectParam& structFontParam);

	// 界面特效相关
	virtual IEffect* CreateEffect(const std::wstring &effectname, bool async = false);
	virtual void DrawEffect(IEffect* pEffect);
	virtual void ReleaseEffect(IEffect* pEffect);

	//CacheEffect，什么特效都可以，不一定是界面的
	virtual void HoldEffect(const std::wstring &name);
	virtual void ReleaseEffect(const std::wstring &name);

	//截图
	virtual PictureHandle CaptureWorld();

	virtual void SetCleanColor(XPCOLOR color) { m_cleanColor = color; }
	virtual XPCOLOR GetCleanColor() const { return m_cleanColor; }

	// 创建销毁
private:
	typedef std::set<Effect*> EffectSet;

	static LRESULT CALLBACK MsgProc( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam );
	static LRESULT CALLBACK LowLevelKeyboardHookEx(INT nCode, WPARAM wParam, LPARAM lParam);
	void MainLoop();
	void UpdateBeforeRender(int delta);
	void DrawFPS();
	void DrawCameraPos();
	void DrawSystemInfo();
	void DrawPerformance();
	void DrawFrameStat();
	void CenterWindow();

	void CheckSpritePictureHandle();

	void ChangeDisplayColor(int color);
	DWORD GetWindowStyleFromMode(XPScreenMode mode);
	bool Init(int step);
	void InitNotify();

	void ControlFPS();
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
	typedef std::vector<XHardRef<IRunnable> > RannableVect;
	typedef std::list<XHardRef<IRunnable> > RannableList;
	// 私有数据
private:
	//
	static Engine s_instance;
	//
	PFSLog *m_pPfslog;
	Env *m_pEnv;
	CursorManager *m_pCursorMan;
	Query *m_pQuery;
	IEntitativeTitleManager *m_pETM;
	IApp *m_pApp;
	SystemResourceManager *m_pSystemResourceMan;
	CFileIOManager *m_pFileIOMan;
	SpriteManager *m_pSprMan;
	AniManager *m_pAniMan;
	EffectManager *m_pEffectMan;
	World* m_pWorld;	// the world
	Renderer *m_pRenderer; // renderer
	ConfigManager *m_pConfigMan;
	DirectSoundManager *m_pSoundManager; // sound manager
	StepSoundType *m_pStepSoundType;	//脚步声类型信息
	EngineSpriteHandle	m_eshSeed;
	EngineSpriteMap m_engineSprites;
	EffectSet m_effects;
	EffectSet m_willBeDeleteEffects;
	std::map<std::wstring, Effect*> m_holdEffects;
	HHOOK m_keyboardHook;	//键盘钩子
	XPBGSoundMode m_bgsoundMode;
	XPWINDOW_STATE m_windowState;
	XPCOLOR m_cleanColor;

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

	HWND m_hWnd;	// 主窗口
	int m_iHeight;
	int m_iWidth;
	int m_iOldColorMode; // 16 or 32
	int m_iCurColorMode; // 16 or 32

	

	float m_fBgSoundVolume;
	float m_fEffectSoundVolume;

	RestoreTask m_restoreTask;
	int m_tick;
	PictureHandle m_SpriteHandle;		//有的精灵会先画到这个Handle上，然后再贴到地图上
	PictureHandle m_AlphaSpriteHandle;	//精灵最后的半透明图片

	// user tasks
	XCSLock m_lckTask;
	RannableVect m_userTasks;
	RannableList m_userTasksList;
	int m_userTasksTimePF[XPWS_MINIMIZE + 1];	//每帧执行的Task的时间
	int m_userTaskListECPF[XPWS_MINIMIZE + 1];	//每帧执行的Task数
	int m_userTaskListRCPF[XPWS_MINIMIZE + 1];	//每帧最多能剩下的Task数

	// global pathfinder for sprite. setup in World::LoadMap.
	astar::PathFinder *m_pPathFinder; // default pathfinder can not find any path

	// 调试用
	enum { XPFRAMESTAT_COUNT = 512 };
	float frame_stat[XPFRAMESTAT_COUNT];
	float camera_dis[XPFRAMESTAT_COUNT];
	float fileio_task_count[XPFRAMESTAT_COUNT];
	float frame_stat_engine[XPFRAMESTAT_COUNT];
	float frame_stat_task[XPFRAMESTAT_COUNT];
	float frame_stat_msg[XPFRAMESTAT_COUNT];
	int frame_stat_index;
	__int64 frame_task;
	static __int64 frame_msg;
	// 占坑
private:
	Engine(const Engine&);
	Engine& operator=(const Engine&);
};

}

#endif