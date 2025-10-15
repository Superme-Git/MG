#ifndef __Nuclear_IENGINE_H
#define __Nuclear_IENGINE_H

#if _MSC_VER > 1000
#pragma once
#endif



#include "iquery.h"
#include "iworld.h"
#include "ienv.h"
#include "iapp.h"
#include "ientitativetitlemanager.h"
#include "renderer\renderer.h"

namespace Nuclear
{

struct EngineParameter
{
	wchar_t* szWindowTitle;
	wchar_t* szClassName;
	HWND hWnd;	// 非空表示用户自己创建的窗口
	XDisplayMode dmode;
	bool bAsyncRead;
	bool bApplictionInBuild;	//exe文件在build下面是true，在build\bin下面是false
	bool bHasMaximizbox;		//是否有最大化按钮
	bool bSizeBox;				//是否能调整大小
	DWORD dwRenderFlags;
	int nAppInitStepCount;		//调用IApp::OnInit的次数
	IApp *pApp; // 禁止为空，且需要保证此指针在Engine::Run()函数期间有效
	EngineParameter() 
		: szWindowTitle(NULL), szClassName(NULL), hWnd(NULL), dmode(1024, 768, 32), bAsyncRead(false), bApplictionInBuild(false), 
		bHasMaximizbox(true), bSizeBox(true), dwRenderFlags(XPCRF_VERTICALSYNC), nAppInitStepCount(1), pApp(NULL) { }
};

class StepSoundType;
class IFileIO;

/* 
	引擎保留使用的系统资源
	WM_TIMER、WM_SIZING、WM_PAINT、WM_SIZE、WM_DESTROY、WM_ACTIVATE、WM_KILLFOCUS、WM_SETFOCUS、(WM_SYSCOMMAND里面wParam==SC_KEYMENU的消息)消息
	用户自定义消息的ID，引擎保留使用WM_USER+1 ~ WM_USER+10

	在编译engine的时候如果添加OVERRIDE_NEW编译宏，
	那么引擎会重载new，改用Nuclear::XAlloc()和Nuclear::XFree()
	如果添加SELF_MALLOC编译宏，在
	Nuclear::XAlloc()和Nuclear::XFree()里面，会用NedMalloc内存池

	所以在客户端如果需要用malloc和free，应该用Nuclear::XAlloc()和Nuclear::XFree()
*/

// 图形引擎
class IEngine
{
	// 构造析构
public:
	IEngine() {}
	virtual ~IEngine() {  }

	// 接口
public:

	virtual bool Run(const EngineParameter &ep) = 0;						// 运行
	virtual bool Exit() = 0;												// 退出

	// 如果是用户自己创建的窗口，那么需要在窗口函数中调用此函数，要注意的是，如果需要播放声音，
	// 并且声音不想全局播放（窗口处于非激活状态也播放），那么WM_SHOWWINDOW、WM_ACTIVATE、WM_SIZE消息必须转到这里来
	virtual bool OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam ) { return false; }
	// 如果是用户自己创建的窗口，那么需要在程序空闲(OnIdle)时中调用此函数
	virtual void OnIdle() { }

	// 用户丢到引擎线程执行的任务，下帧之前执行一部分，具体要看IEnv::SetTaskExecuteCount的值
	virtual void PutTask(IRunnable *task) = 0; 
	virtual size_t GetTaskListCount() const = 0; //获得Task的数量。必须在主线程调用。PutTask之后不会马上增长。

	virtual size_t GetQueueOfIOTaskSize() const = 0; //获得IO队列的长度。这是异步IO完成之后，扔回主线程待处理的队列，必须在主线程调用。

	// 引擎提供的 timer
	virtual bool ScheduleTimer(ITimer * timer, int period/* ms */) = 0;
	virtual bool CancelTimer(ITimer * timer) = 0;

	// 注册、获得实体的TitleManager
	virtual void SetEntitativeTitleManager(IEntitativeTitleManager* pETM) = 0;
	virtual IEntitativeTitleManager *GetEntitativeTitleManager() = 0;

	// 注意：下面这些指针在 Run 之前和 Exit之后 均无效
	virtual IWorld* GetWorld() = 0;				// 得到世界
	virtual const IWorld* GetWorld() const = 0;	// 得到世界
	virtual IEnv* GetEnv() = 0;					// 得到环境
	virtual const IEnv* GetEnv() const = 0;		// 得到环境
	virtual IApp* GetApp() = 0;					//
	virtual IQuery* GetQuery() = 0;
	virtual Renderer* GetRenderer( HWND hwnd=NULL ) = 0; // 得到Renderer, 此接口为GXWindows暴露，其他外部应用不应使用
	virtual IFileIO* GetFileIO() = 0;			//读取文件的接口

	//声音相关, 其中，volume为0~255，255最大，0最小, freq为0~255，128是正常
	//切换背景音模式，如果要从Customs切换到别的模式，必须自己STOP背景音乐，从AUTO切换到别的模式，引擎里面的背景音乐会自己停止
	//默认是XPBGSM_AUTO
	virtual void SetBGSoundMode(XPBGSoundMode mode) = 0;
	virtual XPBGSoundMode GetBGSoundMode() const = 0;
	//只有音乐模式处于Custom的时候，下面几个方法才有效
	virtual CHECK_SOUND_STATE CheckBGSound(const std::wstring &fn, bool async) = 0;
	__declspec(deprecated) SoundHandle PlayBGSound(LPCTSTR fn, bool looping = false, bool bGlobalFocus = false)
	{
		CheckBGSound(fn, false);
		return PlayBGSound(fn, 0, GetEnv()->GetBGMType(), 255, looping, bGlobalFocus);
	}
	//必须要先CheckBGSound返回OK，或者回调了加载成功之后，调用PlayBGSound, 或者PushNextStreamSound才能成功
	virtual SoundHandle PlayBGSound(const std::wstring &fn, unsigned short fadeInTime, unsigned char type, unsigned char volume, bool looping, bool bGlobalFocus) = 0;
	__declspec(deprecated) SoundHandle PlayBGSound(const std::wstring &fn, unsigned short fadeInTime, bool looping, bool bGlobalFocus)
	{
		return PlayBGSound(fn, fadeInTime, GetEnv()->GetBGMType(), 255, looping, bGlobalFocus);
	}
	__declspec(deprecated) virtual SoundHandle PlayBGSound(const std::wstring &fn, unsigned short fadeInTime, unsigned char type, bool looping, bool bGlobalFocus)
	{
		return PlayBGSound(fn, fadeInTime, type, 255, looping, bGlobalFocus);
	}
	virtual bool PushNextStreamSound(SoundHandle handle, const std::wstring &fn) = 0;
	virtual bool FadeOutToStop(SoundHandle handle, unsigned short time) = 0;

	//priority是优先级，0为最高，目前会打断低优先级的声音（如果播满了10个声音）
	//type是类型，0固定为环境音效，其余由客户端定义，用来做开关和整体音量调节，也可以全部用0
	virtual bool PlayEffectSound(const std::wstring &fn, short priority, unsigned char type, unsigned char volume = 255, unsigned char freq = 128) = 0;

	//停止所有某种类型的声音（不过接下来如果有这种类型的声音还会播放，如果要禁止播放，应该用EnableSound）
	virtual void StopTypeSound(unsigned char type) = 0;

	//全局音量，区间为[0,1]
	virtual void SetSoundVolume(unsigned char type, float fVolume) = 0;
	virtual float GetSoundVolume(unsigned char type) const = 0;
	__declspec(deprecated) void SetBGSoundVolume(float fVolume) { SetSoundVolume(GetEnv()->GetBGMType(), fVolume); }
	__declspec(deprecated) float GetBGSoundVolume() const {	return GetSoundVolume(GetEnv()->GetBGMType()); }
	__declspec(deprecated) void SetEffectSoundVolume(unsigned char type, float fVolume) { SetSoundVolume(type, fVolume); }
	__declspec(deprecated) float GetEffectSoundVolume(unsigned char type) const {	return GetSoundVolume(type); }


	//声音开关
	//如果当前背景音模式是Custom，重新打开背景音乐的类型的时候，要重新调用PlayBGSound()方法播放背景音乐
	virtual void EnableSound(unsigned char type, bool b) = 0;
	virtual bool IsSoundEnable(unsigned char type) const = 0;

	__declspec(deprecated) void EnableBGSound(bool b) { EnableSound(GetEnv()->GetBGMType(), b); }
	__declspec(deprecated) bool IsBGSoundEnable() const { return IsSoundEnable(GetEnv()->GetBGMType()); }
	__declspec(deprecated) void EnableEffectSound(unsigned char type, bool b) { EnableSound(type, b); }
	__declspec(deprecated) bool IsEffectSoundEnable(unsigned char type) const { return IsSoundEnable(type); }
	
	// 非世界精灵相关
	virtual EngineSpriteHandle CreateEngineSprite(const std::wstring &modelname) = 0;
	virtual void ReleaseEngineSprite(EngineSpriteHandle handle) = 0;
	virtual void RendererEngineSprite(EngineSpriteHandle handle) = 0;
	virtual void SetEngineSpriteLoc(EngineSpriteHandle handle, const Location& loc) = 0;
	virtual void SetEngineSpriteDirection(EngineSpriteHandle handle, XPDIRECTION dir) = 0;
	virtual void SetEngineSpriteDefaultAction(EngineSpriteHandle handle, const std::wstring &actname) = 0;
	virtual void SetEngineSpriteAction(EngineSpriteHandle handle, const std::wstring &actname) = 0;
	virtual void SetEngineSpriteComponent(EngineSpriteHandle handle, int scid, const std::wstring& resource, Nuclear::XPCOLOR color=0xffffffff/*XRGB*/) = 0;
	virtual void EnableEngineSpriteShadow(EngineSpriteHandle handle, bool b) = 0;
	virtual void SetEngineSpriteScale(EngineSpriteHandle handle, float scale) = 0;


	//一些数据。。。

	virtual int GetAllSpriteCount() const = 0;
	virtual void GetAllSpriteDetail(std::map<std::wstring, int> &detail) const = 0;

	virtual int GetAllEffectCount() const = 0;
	virtual void GetAllEffectDetail(std::map<std::wstring, int> &detail) const = 0;

	virtual int GetAllSoundCount() const = 0;
	virtual void GetAllSoundDetail(std::vector<std::wstring> &detail) const = 0;


	//文字特效
	virtual IEffect* CreateFontEffect(TextBlockHandle textHandle, const XPFontEffectParam& structFontParam) = 0;

	// 界面特效相关
	virtual IEffect* CreateEffect(const std::wstring &effectname, bool async = false) = 0;
	virtual void DrawEffect(IEffect* pEffect) = 0;
	virtual void ReleaseEffect(IEffect* pEffect) = 0;

	//CacheEffect，什么特效都可以，不一定是界面的
	virtual void HoldEffect(const std::wstring &name) = 0;
	virtual void ReleaseEffect(const std::wstring &name) = 0;

	//截图
	virtual PictureHandle CaptureWorld() = 0;

	virtual void SetCleanColor(XPCOLOR color) = 0;
	virtual XPCOLOR GetCleanColor() const = 0;

	//如果子窗口失去或者获得Active状态，那么马上调用这个接口：
	virtual void OnActive(bool active) = 0;
	virtual XPWINDOW_STATE GetWindowState() const = 0;

	// 占坑
private:
	IEngine(const IEngine&);
	IEngine& operator=(const IEngine&);
};

IEngine* GetEngine();	// 得到引擎实例，放个裸函数

}


#endif