
#ifndef __Nuclear_ENV_H
#define __Nuclear_ENV_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\ienv.h"

namespace Nuclear
{

class Engine;
// 变成转发器了
class Env : public IEnv
{

public:
	Env(Engine *pEngine) : m_pEngine(pEngine) { }
	virtual ~Env() { }
	virtual HWND GetWindow();					// 得到窗口
	virtual bool SetDisplayMode(const XDisplayMode &dmode);	// 设置显示模式
	virtual XDisplayMode GetDisplayMode() const;
	virtual bool IsSoundInited() const;

	// 光标
	// 注册硬件光标，返回其句柄
	// @resource是图片路径， offset是光标在图片内的偏移，大小固定为32*32, center是光标中心
	virtual CursorHandle RegisterCursor(const std::wstring& resource, const CPOINT &offset, const CPOINT &center);
	// 新版的光标，调用系统API，仅仅支持cur ani格式，资源路径为windows相对路径或者绝对路径，资源不能打进包里。
	virtual XPCursorHandle RegisterCursor(const std::wstring& resource);
	// 更换光标
	virtual bool ChangeCursor(CursorHandle hCursor);
	virtual bool ChangeCursor(XPCursorHandle hCursor);
	// 设置是否显示光标
	virtual bool ShowCursor(bool b);
	// 设置光标坐标
	virtual bool SetCursorPosition(const CPOINT &point);
	// 得到关标坐标
	virtual void GetCursorPosition(CPOINT &point) const;

	// 设置屏幕调试信息开关
	virtual void SetConsoleInfo(XPCONSOLE_INFO eInfo, bool bOn);
	virtual bool TestConsoleInfo(XPCONSOLE_INFO eInfo) const;

	// 帧状态调试信息开关
	virtual void SetFrameStateInfo(XPFRAMESTAT_TYPE type, bool bOn);//默认打开Frame Time、Camera Dis、FileIO
	virtual bool TestFrameStateInfo(XPFRAMESTAT_TYPE type) const;
	// 帧状态调试信息颜色
	virtual void SetFrameStateColor(XPFRAMESTAT_TYPE type, XPCOLOR color);//默认为黄 红 绿，后面无定义
	virtual XPCOLOR GetFrameStateColor(XPFRAMESTAT_TYPE type) const;

	virtual void SetDynamicMapLoading(bool x); // 是否动态加载地图资源，默认是true
	virtual bool IsDynamicMapLoading() const;

	virtual void SetControlFPS(bool x); // 是否控制FPS，默认是true
	virtual bool IsControlFPS() const;

	virtual void SetControlFPS(XPWINDOW_STATE type, int fps);//控制的FPS帧率，默认是60 60 30
	virtual int GetControlFPS(XPWINDOW_STATE type) const;

	virtual void SetTaskExecuteTime(XPWINDOW_STATE type, int time);	//每帧Task的时间，单位是毫秒，默认值30
	virtual int GetTaskExecuteTime(XPWINDOW_STATE type) const;

	virtual void SetIOUpdateExecuteTime(int time);	//每帧在主线程创建异步回来的资源的时间
	virtual int GetIOUpdateExecuteTime() const;

	virtual void SetTaskExecuteCount(XPWINDOW_STATE type, int count);//每帧执行的Task的个数，-1为不限制
	virtual int GetTaskExecuteCount(XPWINDOW_STATE type) const;

	virtual void SetTaskListMaxRemainCount(XPWINDOW_STATE type, int count);//每帧剩下的Task最多的个数，-1为不限制
	virtual int GetTaskListMaxRemainCount(XPWINDOW_STATE type) const;

	virtual void SetRenderNightEffectByShader(bool b); // 是否使用shader渲染夜晚效果
	virtual bool IsRenderNightEffectByShader() const;

	virtual bool IsRenderNightEffectWithRenderTarget() const; // 使用rendertarget渲染夜晚效果
	virtual void SetRenderNightEffectWithRenderTarget(bool b);

	virtual bool EnableRenderDuringLoading(bool b); // 同步加载地图的时候是否绘制，默认true
	virtual bool IsRenderDuringLoading() const;

	virtual bool IsEnableMaskPic() const;	//使用地图上的叠图，默认关
	virtual void SetEnableMaskPic(bool b);

	virtual bool IsRenderSpriteShadow() const;	// 是否绘制sprite阴影，默认true
	virtual void SetRenderSpriteShadow(bool b);

	virtual bool IsRenderAlphaSprite() const; // 是否绘制半透明人物，默认true
	virtual void SetRenderAlphaSprite(bool b);

	virtual bool IsBlurForTeleport() const;	// 在击退（瞬间移动）的时候是否带残影，默认true
	virtual void SetBlurForTeleport(bool b);

	virtual void SetEnableSurfaceCache(bool b); // 是否使用地表Cache
	virtual bool IsEnableSurfaceCache() const;

	virtual bool IsSyncBeforeWater() const;	// 是否同步加载水层以下的图
	virtual void SetSyncBeforeWater(bool b);

	virtual void SetEnableSortMapObjects(bool b); // 是否排序精灵和地图中层物件
	virtual bool IsEnableSortMapObjects() const;

	virtual void SetEnableLinkedObjs(bool b);	// 动态物件（lko）是否会动
	virtual bool IsEnableLinkedObjs() const;

	virtual bool IsEnableWade() const;		//涉水半透明效果
	virtual void SetEnableWade(bool b);

	virtual XPCOMPUTERCONFIGTYPE GetSystemLevel() const;	//当前的计算机特效配置，默认是XPCOMPUTERCONFIG_H高配
	virtual void SetSystemLevel(XPCOMPUTERCONFIGTYPE l);

	virtual void SetAniPicGCTime(int t);		//ani动画图片（包括角色和ani特效）多长时间不渲染就回收，默认20000毫秒（20秒）
	virtual int GetAniPicGCTime() const;

	virtual void SetAniXapGCTime(int t);		//ani动画结构（包括角色和ani特效）多长时间不渲染就回收，默认1200000毫秒（20分）
	virtual int GetAniXapGCTime() const;

	virtual void ShowSpritePath(bool b);  // 显示精灵路径开关。
	virtual void ShowSpriteTrail(bool b); // 显示精灵尾迹开关。
	virtual void ShowMapGrid(bool b);     // 显示迷宫格子开关。

	virtual bool IsShowSpritePath() const;  // 显示精灵路径开关。
	virtual bool IsShowSpriteTrail() const; // 显示精灵尾迹开关。
	virtual bool IsShowMapGrid() const;     // 显示迷宫格子开关。

	virtual void SetMapMazeMask(unsigned int mask);   // 显示迷宫掩码。0不显示。
	virtual unsigned int GetMapMazeMask() const; // 当前显示迷宫掩码。

	virtual bool IsSmoothMove() const;  // 是否平滑移动
	virtual void SetSmoothMove(bool b);

	virtual short GetMinDelta() const;		// 最小的Delta
	virtual void SetMinDelta(short d);

	virtual short GetMaxDiffDelta() const;	// 相邻两帧之间的最大差值的绝对值
	virtual void SetMaxDiffDelta(short d);

	virtual short GetMaxDiffFromAvg() const;	// 和平均值之间的最大差值的绝对值
	virtual void SetMaxDiffFromAvg(short d);

	virtual void SetSpriteMoveSmoothLimit(int iLimit);
	virtual int  GetSpriteMoveSmoothLimit() const;

	//当窗口失去激活时，声音淡出的时间（毫秒，-1代表不停止播放）
	virtual int GetInactiveSoundFadeOutTime() const;
	virtual void SetInactiveSoundFadeOutTime(int t);

	//当窗口重新激活时，声音淡入的时间（毫秒，<0无意义）
	virtual int GetActiveSoundFadeInTime() const; 
	virtual void SetActiveSoundFadeInTime(int t);

	//当卸载地图的时候，声音淡出的时间（毫秒）
	virtual int GetUnloadMapBGSoundFadeOutTime() const; 
	virtual void SetUnloadMapBGSoundFadeOutTime(int t);

	// 背景音的音量类型
	virtual unsigned char GetBGMType() const;
	virtual void SetBGMType(unsigned char t);

	// 环境音的音量类型
	virtual unsigned char GetEnvSoundType() const;
	virtual void SetEnvSoundType(unsigned char t);

	// 脚步声的音量类型
	virtual unsigned char GetStepSoundType() const;
	virtual void SetStepSoundType(unsigned char t);

	//动作的声音类型
	virtual unsigned char GetActionSoundType(const std::wstring &action_name) const;
	virtual void SetActionSoundType(const std::wstring &action_name, unsigned char t);

	// 脚步声的优先级
	virtual short GetStepSoundPriority() const;
	virtual void SetStepSoundPriority(short p);

	// 逻辑坐标到世界坐标变换(X方向)的转换因子
	virtual float GetLogicToWorldScaleX() const;
	virtual void SetLogicToWorldScaleX(float fScale);

	//目前反外挂答题用到...显卡摘要信息
	virtual std::wstring GetDisplayDeviceInfo();

	virtual void SetIOUpdateFrame(int frame);//多少帧创建一张纹理

private:
	Engine* m_pEngine;

};

}

#endif