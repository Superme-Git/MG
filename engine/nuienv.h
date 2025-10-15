#ifndef __Nuclear_IENV_H__
#define __Nuclear_IENV_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "common/nuxptypes.h"

namespace Nuclear
{

	// 这个类管理和游戏世界无关的东西
	class IEnv
	{
	public:
		IEnv() {}
		virtual ~IEnv() {  }

		virtual NuclearDisplayMode GetDisplayMode() const = 0;
		virtual NuclearMultiSampleType GetCurrentMultiSampleType() const = 0;//获得当前的抗锯齿类型
		virtual bool IsMultiSampleTypeEnable() const = 0;

		virtual bool IsSoundInited() const = 0;
		virtual const std::wstring &GetCurrentDirectory() const = 0;

		// 设置/得到光标坐标
		virtual bool SetCursorPosition(const NuclearPoint &point) = 0;
		virtual void GetCursorPosition(NuclearPoint &point) const = 0;

		// 同步加载地图的时候是否绘制
		virtual bool EnableRenderDuringLoading(bool b) = 0;
		virtual bool IsRenderDuringLoading() const = 0;

		// 设置屏幕调试信息开关
		virtual void SetConsoleInfo(NuclearConsoleInfo eInfo, bool bOn) { }
		virtual bool TestConsoleInfo(NuclearConsoleInfo eInfo) const { return false; }

		// 帧状态调试信息开关
		virtual void SetFrameStateInfo(NuclearFrameStatType type, bool bOn) { }//默认打开Frame Time、Camera Dis、FileIO
		virtual bool TestFrameStateInfo(NuclearFrameStatType type) const { return false;}

		// 帧状态调试信息颜色
		virtual void SetFrameStateColor(NuclearFrameStatType type, NuclearColor color) {}//默认为黄 红 绿，后面无定义
		virtual NuclearColor GetFrameStateColor(NuclearFrameStatType type) const { return 0; }

		virtual void SetDynamicMapLoading(bool b) = 0; // 是否动态加载地图资源，默认是true
		virtual bool IsDynamicMapLoading() const = 0;

		virtual void SetControlFPS(bool b) = 0; // 是否控制FPS，默认是true，这个只控制激活状态，在后台和最小化状态下，不管怎样设都控帧
		virtual bool IsControlFPS() const = 0;

		virtual void SetControlFPS(NuclearWindowState type, int fps) = 0;//控制的FPS帧率，默认是60 60 30
		virtual int GetControlFPS(NuclearWindowState type) const = 0;

		virtual void SetTaskExecuteTime(NuclearWindowState type, int time) = 0;	//每帧Task的时间，单位是毫秒，默认值30
		virtual int GetTaskExecuteTime(NuclearWindowState type) const = 0;

		virtual void SetIOUpdateExecuteTime(int time) = 0;	//每帧在主线程创建异步回来的资源的时间，默认30
		virtual int GetIOUpdateExecuteTime() const = 0;

		virtual void SetRenderNightEffectByShader(bool b) = 0; // 是否使用shader渲染夜晚效果
		virtual bool IsRenderNightEffectByShader() const = 0;

		virtual bool IsRenderNightEffectWithRenderTarget() const = 0; // 使用rendertarget渲染夜晚效果
		virtual void SetRenderNightEffectWithRenderTarget(bool b) = 0;

		virtual bool IsEnableMaskPic() const = 0;	//使用地图上的叠图，默认关
		virtual void SetEnableMaskPic(bool b) = 0;

		virtual bool IsRenderSpriteShadow() const = 0;	// 是否绘制sprite阴影，默认true
		virtual void SetRenderSpriteShadow(bool b) = 0;

		virtual bool IsBlurForTeleport() const = 0;	// 在击退（瞬间移动）的时候是否带残影，默认true
		virtual void SetBlurForTeleport(bool b) = 0;

		virtual void SetEnableSurfaceCache(bool b) = 0; // 是否使用地表Cache
		virtual bool IsEnableSurfaceCache() const = 0;

		virtual bool IsSyncBeforeWater() const = 0;	// 是否同步加载水层以下的图
		virtual void SetSyncBeforeWater(bool b) = 0;

		virtual void SetEnableSortMapObjects(bool b) = 0; // 是否排序精灵和地图中层物件
		virtual bool IsEnableSortMapObjects() const = 0;

		virtual void SetEnableLinkedObjs(bool b) = 0;	// 动态物件（lko）是否会动
		virtual bool IsEnableLinkedObjs() const = 0;

		virtual NuclearComputerConfigType GetSystemLevel() const = 0;	//当前的计算机特效配置，默认是XPCOMPUTERCONFIG_H高配

		virtual void ShowSpritePath(bool b) = 0;  // 显示精灵路径开关。
		virtual void ShowSpriteTrail(bool b) = 0; // 显示精灵尾迹开关。
		virtual void ShowMapGrid(bool b) = 0;     // 显示迷宫格子开关。

		virtual bool IsShowSpritePath() const = 0;  // 显示精灵路径开关。
		virtual bool IsShowSpriteTrail() const = 0; // 显示精灵尾迹开关。
		virtual bool IsShowMapGrid() const = 0;     // 显示迷宫格子开关。

		virtual void SetMapMazeMask(unsigned int) = 0;   // 显示迷宫掩码。0不显示。
		virtual unsigned int GetMapMazeMask() const = 0; // 当前显示迷宫掩码。

		virtual void SetAniPicGCTime(int t) = 0;		//ani动画图片（包括角色和ani特效）多长时间不渲染就回收，默认20000毫秒（20秒）
		virtual int GetAniPicGCTime() const = 0;

		virtual void SetAniXapGCTime(int t) = 0;		//ani动画结构（包括角色和ani特效）多长时间不渲染就回收，默认1200000毫秒（20分）
		virtual int GetAniXapGCTime() const = 0;

		virtual void SetMax3DEffectComponentCount(int count) = 0;	//3D特效组件数，默认15个

		// 精灵半透明的方案，默认是XPSTT_ELEMENT_TRANSLUCENT
		virtual NuclearSpriteTranslucentType GetSpriteTranslucentType() const = 0;
		virtual void SetSpriteTranslucentType(NuclearSpriteTranslucentType t) = 0;
		// 默认0.6
		// 如果是XPSTT_ELEMENT_TRANSLUCENT，就是遮挡物Alpha；
		// 如果是XPSTT_SPRITE_TRANSLUCENT，就是精灵Alpha
		virtual float GetMaskAlpha() const = 0;
		virtual void SetMaskAlpha(float f) = 0;
	
		// 是否绘制遮挡物底图（如果是整图其实就不需要绘制了），默认绘制, 只在XPSTT_ELEMENT_TRANSLUCENT半透明方案生效
		virtual bool IsRenderSolidMask() const = 0;
		virtual void SetRenderSolidMask(bool b) = 0;

		//测试显卡支持的抗锯齿模式
		//window: 是否窗口模式； isMode16: 是否16位色，结果输出到types里面
		virtual void GetAvailableMultiSampleType(bool window, bool isMode16, std::vector<NuclearMultiSampleType> &types) = 0;

		virtual bool IsSmoothMove() const = 0;  // 是否平滑移动
		virtual void SetSmoothMove(bool b) = 0;

		virtual short GetMinDelta() const = 0;		// 最小的Delta
		virtual void SetMinDelta(short d) = 0;

		virtual short GetMaxDiffDelta() const = 0;	// 相邻两帧之间的最大差值的绝对值
		virtual void SetMaxDiffDelta(short d) = 0;

		virtual short GetMaxDiffFromAvg() const = 0;	// 和平均值之间的最大差值的绝对值
		virtual void SetMaxDiffFromAvg(short d) = 0;

		virtual bool IsEnableWade() const = 0;		//涉水半透明效果
		virtual void SetEnableWade(bool b) = 0;

		virtual int GetUnloadMapBGSoundFadeOutTime() const = 0; //当卸载地图的时候，声音淡出的时间（毫秒），默认0
		virtual void SetUnloadMapBGSoundFadeOutTime(int t) = 0; //当卸载地图的时候，声音淡出的时间（毫秒）

		virtual unsigned char GetBGMType() const = 0;	//背景音的音量类型，默认0
		virtual void SetBGMType(unsigned char t) = 0;

		virtual unsigned char GetEnvSoundType() const = 0;	// 环境音的音量类型，默认1
		virtual void SetEnvSoundType(unsigned char t) = 0;

		virtual unsigned char GetStepSoundType() const = 0;	// 脚步声的音量类型，默认1
		virtual void SetStepSoundType(unsigned char t) = 0;

		virtual unsigned char GetActionSoundType(const std::wstring &action_name) const = 0;	//动作的声音类型，默认0
		virtual void SetActionSoundType(const std::wstring &action_name, unsigned char t) = 0;

		virtual unsigned char Get3DSpriteActionSoundType() const = 0;	//3D角色里面的声音类型，默认0
		virtual void Set3DSpriteActionSoundType(unsigned char t) = 0;

		virtual short GetStepSoundPriority() const = 0; // 脚步声的优先级，默认40
		virtual void SetStepSoundPriority(short p) = 0;

		////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 设置精灵寻路移动时，平滑处理配置。
		//
		// =  1  不平滑处理，精灵沿格子中心的连线移动，按 8 方向进行移动。
		// <= 0  进行平滑处理，查找路径时，一次性完成所有格子的平滑处理。
		// >  1  进行平滑处理，边走边处理，当上一次平滑处理结果走完以后再处理剩下的格子，每次处理格子数量不超过限制。
		//       考虑效率上的优化，80~100应该是一个合适的值。默认为 80。因为 80 > 1024/16
		virtual void SetSpriteMoveSmoothLimit(int iLimit) = 0;
		virtual int  GetSpriteMoveSmoothLimit() const = 0;

		// 逻辑坐标到世界坐标变换(X方向)的转换因子
		virtual float GetLogicToWorldScaleX() const = 0;
		virtual void SetLogicToWorldScaleX(float fScale) =0;

	private:
		IEnv(const IEnv&);
		IEnv& operator=(const IEnv&);

	};

}


#endif