#ifndef __Nuclear_IENGINE_H__
#define __Nuclear_IENGINE_H__

#if _MSC_VER > 1000
#pragma once
#endif



#include "nuiquery.h"
#include "nuiworld.h"
#include "nuienv.h"
#include "nuiapp.h"
#include "nuientitativetitlemanager.h"
#include "renderer/nurenderer.h"

#include <map>

#include <cocos2d.h>
#include "engine/nuiadapter.h"

namespace Nuclear
{
	struct EngineParameter
	{
		wchar_t* szWindowTitle;
		wchar_t* szClassName;
		NuclearDisplayMode dmode;
		bool bAsyncRead;
		bool bApplictionInBuild;	//exe文件在build下面是true，在build\bin下面是false
		bool bHasMaximizbox;		//是否有最大化按钮
		bool bSizeBox;				//是否能调整大小
		bool bEnableMipMap;			//是否用MipMap
		DWORD dwRenderFlags;
		NuclearMultiSampleType multiSampleType;	//抗锯齿参数
		int nAppInitStepCount;		//调用IApp::OnInit的次数
		IApp *pApp; // 禁止为空，且需要保证此指针在Engine::Run()函数期间有效
		EngineParameter() 
			: szWindowTitle(NULL), szClassName(NULL), dmode(1024, 768, 32), bAsyncRead(false), bApplictionInBuild(false), 
			bHasMaximizbox(true), bSizeBox(true), bEnableMipMap(true), dwRenderFlags(XPCRF_VERTICALSYNC), multiSampleType(XPMULTISAMPLE_NONE), 
			nAppInitStepCount(1), pApp(NULL) { }
	};

	class StepSoundType;
	class INuclearFileIO;
	class Sprite;

	// 图形引擎
	class IEngine
	{
	public:
		IEngine() {}
		virtual ~IEngine() {  }

		iadapter* m_adapter;
	public:

		virtual int GetScreenWidth() = 0;
		virtual int GetScreenHeight() = 0;
		virtual void SetEngineLayer(cocos2d::CCLayer* aPLayer) = 0;
		virtual cocos2d::CCLayer* GetEngineLayer() = 0;
		virtual void Draw() = 0;
		// 下面两个设路径的方法，要在调用Run()之前调用
		virtual bool SetInfoLogPath(const std::wstring &fn) = 0;
		virtual bool SetErrorLogPath(const std::wstring &fn) = 0;
		virtual bool SetSegmpakLogPath(const std::wstring &fn) = 0;

		virtual bool Run(const EngineParameter &ep) = 0;						// 运行
		virtual bool Exit() = 0;												// 退出
		// 如果是用户自己创建的窗口，那么需要在程序空闲(OnIdle)时中调用此函数
		virtual void OnIdle() { }

		virtual void SetGameTimeSpeedScale(float fScale) = 0;
		virtual float GetGameTimeSpeedScale() const = 0;

		// 如果是用户自己创建的窗口，那么需要在窗口函数中调用此函数，要注意的是，如果需要播放声音，
		// 并且声音不想全局播放（窗口处于非激活状态也播放），那么WM_SHOWWINDOW、WM_ACTIVATE、WM_SIZE消息必须转到这里来
		virtual bool OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam, void* pAdditionalParam = NULL) { return false; }

		// 用户丢到引擎线程执行的任务，下帧之前执行一部分，具体要看IEnv::SetTaskExecuteCount的值
		virtual void PutTask(INuclearRunnable *task) = 0;

		// 引擎提供的 timer
		virtual bool ScheduleTimer(INuclearTimer * timer, int period/* ms */) = 0;
		virtual bool CancelTimer(INuclearTimer * timer) = 0;

		// 注意：下面这些指针在 Run 之前和 Exit之后 均无效
		virtual IWorld* GetWorld() = 0;				// 得到世界
		virtual const IWorld* GetWorld() const = 0;	// 得到世界
		virtual IEnv* GetEnv() = 0;					// 得到环境
		virtual IApp* GetApp() = 0;					//
		virtual Renderer* GetRenderer(  ) = 0; // 得到Renderer, 此接口为GXWindows暴露，其他外部应用不应使用
		virtual INuclearFileIO* GetFileIO() = 0;			//读取文件的接口
    
		virtual NuclearFileIOManager* GetFileIOManager() =0 ;
	
		// 非世界精灵相关
		virtual EngineSpriteHandle CreateEngineSprite(const std::wstring &modelname, bool async, bool isUISprite=false) = 0;
		virtual bool SetEngineSpriteModel(EngineSpriteHandle handle, const std::wstring &modelname, bool async) = 0;
		virtual void ReleaseEngineSprite(EngineSpriteHandle handle) = 0;
		virtual void RendererEngineSprite(EngineSpriteHandle handle) = 0;
		virtual void SetEngineSpriteLoc(EngineSpriteHandle handle, const NuclearLocation& loc) = 0;
		virtual void SetEngineSpriteDirection(EngineSpriteHandle handle, NuclearDirection dir) = 0;
		virtual void SetEngineSpriteDirection(EngineSpriteHandle handle, const NuclearPoint &target) = 0;
		virtual void SetEngineSpriteDefaultAction(EngineSpriteHandle handle, const std::wstring &actname) = 0;
		virtual void SetEngineSpriteAction(EngineSpriteHandle handle, const std::wstring &actname) = 0;
		virtual void SetEngineSpriteComponent(EngineSpriteHandle handle, int scid, const std::wstring& resource, Nuclear::NuclearColor color = 0xffffffff/*XRGB*/) = 0;
		virtual void EnableEngineSpriteShadow(EngineSpriteHandle handle, bool b) = 0;
		virtual void SetEngineSpriteScale(EngineSpriteHandle handle, float scale) = 0;
		virtual void SetEngineSpriteAlpha(EngineSpriteHandle handle, unsigned char alpha) = 0;
		virtual bool GetEngineSpriteActionTimeByName(EngineSpriteHandle handle, const std::wstring& action_name, float &time) = 0;	// yeqing 2015-11-07

		virtual Sprite* FindAndAddEngineSprite(EngineSpriteHandle handle) = 0;

		// 设置和取消持续效果，需要手工取消，个数无上限
		virtual IEffect* SetEngineSpriteDurativeEffect(EngineSpriteHandle handle, const std::wstring &effect_name, const NuclearPoint& relpos, unsigned int flag) = 0;
		// 临时效果，播放完毕自动取消，个数无上限, times是播放次数, 如果有声音，soundtype就是其声音类型
		virtual IEffect* PlayEngineSpriteEffect(EngineSpriteHandle handle, const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag, unsigned char soundtype) = 0;
		virtual void RemoveEngineSpriteDurativeEffect(EngineSpriteHandle handle, IEffect* pEffect) = 0;
		// 获得冒泡物件（例如伤害文字）的初始Y偏移，向上为正
		virtual int GetEngineSpriteBubbleItemOffset(EngineSpriteHandle handle) const = 0;

		virtual void EnableParticle(bool flag) = 0;

		// 界面特效相关
		virtual IEffect* CreateEffect(const std::wstring &effectname, bool async = true) = 0;
		virtual void DrawEffect(IEffect* pEffect) = 0;
		virtual void ReleaseEffect(IEffect* pEffect) = 0;

		//CacheEffect，什么特效都可以，不一定是界面的
		virtual void HoldEffect(const std::wstring &name) = 0;
		virtual void ReleaseEffect(const std::wstring &name) = 0;

		//截图
		virtual PictureHandle CaptureWorld() = 0;

		virtual void SetCleanColor(NuclearColor color) = 0;
		virtual NuclearColor GetCleanColor() const = 0;

		//如果子窗口失去或者获得Active状态，那么马上调用这个接口：
		virtual NuclearWindowState GetWindowState() const = 0;

		virtual void GCNow() = 0;
		virtual void setGCCooldown(int time) = 0;

		virtual void  ResetFPSCounter() = 0;
		virtual float GetFPS() const = 0;
		virtual float GetLatestFPS() const = 0;

		virtual int GetLogicWidth() = 0;
		virtual int GetLogicHeight() = 0;

		virtual void SetSmoothDeltaLimit(unsigned int uiSmoothDeltaLimit) = 0;
		virtual unsigned int GetSmoothDeltaLimit() = 0;

		virtual int GetWidth() const = 0;
		virtual int GetHeight() const = 0;

		virtual unsigned int GetTimeCount() = 0;

		virtual float GetCurMemSize() = 0;//获得当前进程所使用的内存量
		virtual float GeCurAvailableMemSize() = 0;//获得当前进程可用的内存量

		virtual void SetStepLoadTexture(bool b) = 0;
		virtual bool GetStepLoadTexture() const = 0;

		virtual void  SetLimitFireThreadSecond(int limitFireThreadCount) = 0;
		virtual int   GetLimitFireThreadSecond() = 0;

	private:
		IEngine(const IEngine&);
		IEngine& operator=(const IEngine&);
	};

	IEngine* GetEngine();	// 得到引擎实例，放个裸函数

}

#endif
