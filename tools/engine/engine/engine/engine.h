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
class Engine : public IEngine/*����ӿ�*/, public EngineBase, 
	public ISoundNotify, public ISoundLoadNotify
{
	class RestoreTask : public IRunnable
	{
	public:
		virtual ~RestoreTask() { }

		// ��Ҫ�ڴ˺����н����������������׳��Զ����쳣
		virtual void Run();
	};

	//enum
	//{
	//	XPMESSAGE_TASKWAKEUP = WM_USER+1
	//};

	// ��������
	Engine() ;
	virtual ~Engine();

	// ����
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

	//����Ӵ���ʧȥ���߻��Active״̬����ô���ϵ�������ӿڣ�
	virtual void OnActive(bool active);

	// ������û��Լ������Ĵ��ڣ���ô��Ҫ�ڴ��ں����е��ô˺���
	virtual bool OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam );
	// ������û��Լ������Ĵ��ڣ���ô��Ҫ�ڳ������(OnIdle)ʱ�е��ô˺���
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

	//�����Ӱ�ӵĲ���
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

	void SetTaskExecuteTime(XPWINDOW_STATE type, int time) { m_userTasksTimePF[type] = time; }	//ÿ֡Task��ʱ�䣬��λ�Ǻ��룬Ĭ��ֵ30
	int GetTaskExecuteTime(XPWINDOW_STATE type) const { return m_userTasksTimePF[type]; }

	void SetIOUpdateExecuteTime(int time) { m_pFileIOMan->SetUpdateTimePerFrame(time); }//ÿ֡�����̴߳����첽��������Դ��ʱ��
	int GetIOUpdateExecuteTime() const { return m_pFileIOMan->GetUpdateTimePerFrame(); }

	void SetTaskExecuteCount(XPWINDOW_STATE type, int count) { m_userTaskListECPF[type] = count; }//ÿִ֡�е�Task�ĸ�����-1Ϊ������
	int GetTaskExecuteCount(XPWINDOW_STATE type) const { return m_userTaskListECPF[type]; }

	void SetTaskListMaxRemainCount(XPWINDOW_STATE type, int count) { m_userTaskListRCPF[type] = count; }//ÿ֡ʣ�µ�Task���ĸ�����-1Ϊ������
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

	//��ˮ��͸��Ч��
	bool IsEnableWade() const { return m_bEnableWade; }
	void SetEnableWade(bool b) { m_bEnableWade = b; }

	// ��windows��������ת���������������-��Ե�ǰviewport��
	void WindowToClient( Nuclear::CPOINT& point);	

	//�������
	//�����������//////////////////////
	virtual void SetBGSoundMode(XPBGSoundMode mode);
	virtual XPBGSoundMode GetBGSoundMode() const { return m_bgsoundMode; }
	virtual CHECK_SOUND_STATE CheckBGSound(const std::wstring &fn, bool async);
	virtual SoundHandle PlayBGSound(const std::wstring &fn, unsigned short fadeInTime, unsigned char type, unsigned char volume, bool looping, bool bGlobalFocus);
	virtual bool FadeOutToStop(SoundHandle handle, unsigned short time);
	virtual bool PushNextStreamSound(SoundHandle handle, const std::wstring &fn);
	
	//Ч�������//////////////////////
	virtual bool PlayEffectSound(const std::wstring &fn, short priority, unsigned char type, unsigned char volume = 255, unsigned char freq = 128);
	
	//�������////////////////////////
	virtual void SetSoundVolume(unsigned char type, float fVolume);
	virtual float GetSoundVolume(unsigned char type) const;
	virtual void StopTypeSound(unsigned char type);
	virtual void EnableSound(unsigned char type, bool b);
	virtual bool IsSoundEnable(unsigned char type) const;

	//�ص�///////////////////////////
	virtual void OnSoundLoaded(const std::wstring &fn, bool succ) override { m_pApp->OnBGMLoaded(fn, succ); }
	virtual void OnSoundGoingToEnd(SoundHandle handle) override { m_pApp->OnBGMGoingToEnd(handle); }
	virtual void OnSoundPlayEnd(SoundHandle handle) override { m_pApp->OnBGMPlayEnd(handle); }

	// ���澫�����
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

	//������Ч
	virtual IEffect* CreateFontEffect(TextBlockHandle textHandle, const XPFontEffectParam& structFontParam);

	// ������Ч���
	virtual IEffect* CreateEffect(const std::wstring &effectname, bool async = false);
	virtual void DrawEffect(IEffect* pEffect);
	virtual void ReleaseEffect(IEffect* pEffect);

	//CacheEffect��ʲô��Ч�����ԣ���һ���ǽ����
	virtual void HoldEffect(const std::wstring &name);
	virtual void ReleaseEffect(const std::wstring &name);

	//��ͼ
	virtual PictureHandle CaptureWorld();

	virtual void SetCleanColor(XPCOLOR color) { m_cleanColor = color; }
	virtual XPCOLOR GetCleanColor() const { return m_cleanColor; }

	// ��������
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
	// ˽������
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
	StepSoundType *m_pStepSoundType;	//�Ų���������Ϣ
	EngineSpriteHandle	m_eshSeed;
	EngineSpriteMap m_engineSprites;
	EffectSet m_effects;
	EffectSet m_willBeDeleteEffects;
	std::map<std::wstring, Effect*> m_holdEffects;
	HHOOK m_keyboardHook;	//���̹���
	XPBGSoundMode m_bgsoundMode;
	XPWINDOW_STATE m_windowState;
	XPCOLOR m_cleanColor;

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

	HWND m_hWnd;	// ������
	int m_iHeight;
	int m_iWidth;
	int m_iOldColorMode; // 16 or 32
	int m_iCurColorMode; // 16 or 32

	

	float m_fBgSoundVolume;
	float m_fEffectSoundVolume;

	RestoreTask m_restoreTask;
	int m_tick;
	PictureHandle m_SpriteHandle;		//�еľ�����Ȼ������Handle�ϣ�Ȼ����������ͼ��
	PictureHandle m_AlphaSpriteHandle;	//�������İ�͸��ͼƬ

	// user tasks
	XCSLock m_lckTask;
	RannableVect m_userTasks;
	RannableList m_userTasksList;
	int m_userTasksTimePF[XPWS_MINIMIZE + 1];	//ÿִ֡�е�Task��ʱ��
	int m_userTaskListECPF[XPWS_MINIMIZE + 1];	//ÿִ֡�е�Task��
	int m_userTaskListRCPF[XPWS_MINIMIZE + 1];	//ÿ֡�����ʣ�µ�Task��

	// global pathfinder for sprite. setup in World::LoadMap.
	astar::PathFinder *m_pPathFinder; // default pathfinder can not find any path

	// ������
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
	// ռ��
private:
	Engine(const Engine&);
	Engine& operator=(const Engine&);
};

}

#endif