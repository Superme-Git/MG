#ifndef __Nuclear_IAPP_H__
#define __Nuclear_IAPP_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nuisprite.h"
#include "common/nuport.h"

namespace Nuclear
{

	typedef unsigned int NuclearWinMSGFuncResult;

	enum NuclearWinMSGFuncSign
	{
		XPWINMSG_OK = 0,
		XPWINMSG_HANDLED = 1,	//这条消息已经经过处理了，引擎目前不检查这一位的状态，预留。
		XPWINMSG_INTERCEPT = 2,	//需要拦截这个消息不往下处理
	};

	class NuclearP3DAssert {};

	class IApp
	{
	public:
		IApp() {}
		virtual ~IApp() {  }

	public:
		virtual void InitIni() = 0;
		virtual bool OnInit(int step) = 0;
		virtual bool OnExit() = 0;

		//注意，下面四个方法的now在开机第24天到第49天会是负的，delta一定能保证大于0并且是正确的
		virtual void OnTick(unsigned int now, unsigned int delta, unsigned int realTime) = 0;	// 主线程主循环中每帧调用，可处理客户端中与渲染无关的日常工作，如处理协议等
		virtual bool OnBeforeRender(int now/*ms*/) = 0; // 引擎每帧渲染之前调用，用于客户端更新渲染相关数据，返回false则不渲染，可用此函数内wait来控制帧率
		virtual void OnRenderUI(int now, bool realRender) = 0; // 引擎主渲染之后调用，用于GXWindows渲染，之后引擎可能还有额外渲染

		//绘制最开始的那个窗口……step是第几步，totalstep是一共有几步（注意前两步这个是-1，因为还不知道精灵一共有多少个……，这个totalsetp包括ep的nAppInitStepCount）
		//step从1到totalstep, 不过不是每一步都有可能调到，因为我会控制帧率，不让它调用得太频繁，但是，step==1和step==totalstep肯定会被调到
		virtual void OnRenderInit(int now, int step, int totalstep) = 0;
        virtual void applicationEnterForeground() = 0;
        virtual void applicationDidEnterBackground() = 0;
        virtual void OnReloadAllTexture() = 0;
		
		//在这个接口里面，当3D精灵的Type是动作结束（XPUSAT_PLAYACTIONFINISH或者XPUSAT_DEFAULTACTIONFINISH）的时候，
		//不能Attach、Detach，不能播放带有hostSocket的3D特效
		//当精灵因主动播放动作而打断原有动作的时候，不会触发这个回调
		virtual void OnUpdateSpriteAction(ISprite * sprite, XPUSA_TYPE type) = 0; // 当精灵状态发生改变时，回调这个函数更新动作。

		//当非界面精灵在地图之外的时候……
		virtual void OnSpriteLocationError(ISprite *pSprite) {}

		// windows 消息，不包括WM_TIMER、WM_SIZING、WM_PAINT、WM_SIZE、WM_DESTROY、WM_ACTIVATE、WM_KILLFOCUS、WM_SETFOCUS、(WM_SYSCOMMAND里面wParam==SC_KEYMENU的消息)消息
		// 返回值应该是NuclearWinMSGFuncSign枚举的按位或
		virtual NuclearWinMSGFuncResult OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam, void* pAdditionalParam = NULL) { return false; }

		// 加载地图的回调，异步加载会调得很频繁，所以在异步加载的时候，这个只适合设个状态
		virtual bool LoadingMapNotify(int pro) { return true; }
		virtual void LoadingMapFaildNotify() {}

		//如果处于Custom模式下，背景音乐播放完了（有可能是手动终止如调用IEngine::FadeOutToStop或退出程序）会调用此回调
		virtual void OnBGMPlayEnd(SoundHandle handle) {}
		//如果处于Custom模式下，背景音乐播放快播完了（这个时候可以去异步加载下一首音乐了）
		virtual void OnBGMGoingToEnd(SoundHandle handle) {}
		//异步加载一首音乐之后的回调，succ为真代表加载成功
		virtual void OnBGMLoaded(const std::wstring &fn, bool succ) {}

		//3D引擎的Assert被catch住了（不一定是在主线程调用）
		virtual void On3DAssertErr() { throw NuclearP3DAssert(); }

		virtual bool IsShowFPS() { return false; }
#if (defined ANDROID) || (defined WIN32 || defined _WIN32)
        bool m_bIsInterrupt;
#endif

	private:
		IApp(const IApp&);
		IApp& operator=(const IApp&);

	};

}


#endif