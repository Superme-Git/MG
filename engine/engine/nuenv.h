
#ifndef __Nuclear_ENV_H__
#define __Nuclear_ENV_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../nuienv.h"

namespace Nuclear
{
	class Engine;

	// 变成转发器了
	class Env : public IEnv
	{
	public:
		Env(Engine *pEngine) : m_pEngine(pEngine) { }
		virtual ~Env() { }
		virtual NuclearDisplayMode GetDisplayMode() const;
		virtual NuclearMultiSampleType GetCurrentMultiSampleType() const;
		virtual bool IsMultiSampleTypeEnable() const;
		virtual bool IsSoundInited() const;
		// 获得当前程序安装路径，不带最后面的"\"
		virtual const std::wstring &GetCurrentDirectory() const;

		// 设置/得到光标坐标
		virtual bool SetCursorPosition(const NuclearPoint &point);
		virtual void GetCursorPosition(NuclearPoint &point) const;

		// 设置屏幕调试信息开关
		virtual void SetConsoleInfo(NuclearConsoleInfo eInfo, bool bOn);
		virtual bool TestConsoleInfo(NuclearConsoleInfo eInfo) const;

		// 帧状态调试信息开关
		virtual void SetFrameStateInfo(NuclearFrameStatType type, bool bOn);//默认打开Frame Time、Camera Dis、FileIO
		virtual bool TestFrameStateInfo(NuclearFrameStatType type) const;

		// 帧状态调试信息颜色
		virtual void SetFrameStateColor(NuclearFrameStatType type, NuclearColor color);//默认为黄 红 绿，后面无定义
		virtual NuclearColor GetFrameStateColor(NuclearFrameStatType type) const;

		virtual void SetDynamicMapLoading(bool x); // 是否动态加载地图资源，默认是true
		virtual bool IsDynamicMapLoading() const;

		virtual void SetControlFPS(bool x); // 是否控制FPS，默认是true
		virtual bool IsControlFPS() const;

		virtual void SetControlFPS(NuclearWindowState type, int fps);//控制的FPS帧率，默认是60 60 30
		virtual int GetControlFPS(NuclearWindowState type) const;

		virtual void SetTaskExecuteTime(NuclearWindowState type, int time);	//每帧Task的时间，单位是毫秒，默认值30
		virtual int GetTaskExecuteTime(NuclearWindowState type) const;

		virtual void SetIOUpdateExecuteTime(int time);	//每帧在主线程创建异步回来的资源的时间
		virtual int GetIOUpdateExecuteTime() const;

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

		virtual void SetAniPicGCTime(int t);		//ani动画图片（包括角色和ani特效）多长时间不渲染就回收，默认20000毫秒（20秒）
		virtual int GetAniPicGCTime() const;

		virtual void SetAniXapGCTime(int t);		//ani动画结构（包括角色和ani特效）多长时间不渲染就回收，默认1200000毫秒（20分）
		virtual int GetAniXapGCTime() const;

		// 精灵半透明的方案，默认是XPSTT_ELEMENT_TRANSLUCENT
		virtual NuclearSpriteTranslucentType GetSpriteTranslucentType() const;
		virtual void SetSpriteTranslucentType(NuclearSpriteTranslucentType t);

		// 默认0.6
		// 如果是XPSTT_ELEMENT_TRANSLUCENT，就是遮挡物Alpha；
		// 如果是XPSTT_SPRITE_TRANSLUCENT，就是精灵Alpha
		virtual float GetMaskAlpha() const;
		virtual void SetMaskAlpha(float f);

		virtual bool IsRenderSolidMask() const;	// 是否绘制遮挡物底图（如果是整图其实就不需要绘制了），只在XPSTT_ELEMENT_TRANSLUCENT半透明方案生效
		virtual void SetRenderSolidMask(bool b);

		virtual void GetAvailableMultiSampleType(bool window, bool isMode16, std::vector<NuclearMultiSampleType> &types);

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

	private:
		Engine* m_pEngine;

	};

}

#endif