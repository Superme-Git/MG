//#include "stdafx.h"
//#include "cursormanager.h"
#include "nuconfigmanager.h"
#include "../sprite/nuspritemanager.h"
#include "nuengine.h"
#include "nuenv.h"

namespace Nuclear
{
	bool Env::IsSoundInited() const
	{
		return m_pEngine->GetSystemResourceManager()->IsSoundInited();
	}

	// 获得当前程序安装路径，不带最后面的"\"
	const std::wstring& Env::GetCurrentDirectory() const { return m_pEngine->GetCurrentDirectory(); }

	// 同步加载地图的时候是否绘制，默认true
	bool Env::EnableRenderDuringLoading(bool b)
	{
		if (!m_pEngine->GetXPWorld())
			return false;
		m_pEngine->GetXPWorld()->EnableRenderDuringLoading(b);
		return true;
	}

	bool Env::IsRenderDuringLoading() const
	{
		if (!m_pEngine->GetXPWorld())
			return true;
		return m_pEngine->GetXPWorld()->IsRenderDuringLoading();
	}


	bool Env::SetCursorPosition(const NuclearPoint &point)
	{
        assert(false);
        return false;
	}


	NuclearDisplayMode Env::GetDisplayMode() const
	{
		return m_pEngine->GetRenderer()->GetDisplayMode();
	}

	NuclearMultiSampleType Env::GetCurrentMultiSampleType() const
	{
		return m_pEngine->GetRenderer()->GetCurrentMultiSampleType();
	}

	bool Env::IsMultiSampleTypeEnable() const
	{
		return m_pEngine->IsMultiSampleTypeEnable();
	}

	void Env::GetCursorPosition(NuclearPoint &point) const
	{
        assert(false);
	}

	void Env::SetConsoleInfo(NuclearConsoleInfo eInfo, bool bOn) { m_pEngine->GetConfigManager()->SetConsoleInfo(eInfo, bOn); }
	bool Env::TestConsoleInfo(NuclearConsoleInfo eInfo) const { return m_pEngine->GetConfigManager()->TestConsoleInfo(eInfo); }

	// 帧状态调试信息开关
	void Env::SetFrameStateInfo(NuclearFrameStatType type, bool bOn) { m_pEngine->GetConfigManager()->SetFrameStateInfo(type, bOn); }
	bool Env::TestFrameStateInfo(NuclearFrameStatType type) const { return m_pEngine->GetConfigManager()->TestFrameStateInfo(type); }
	// 帧状态调试信息颜色
	void Env::SetFrameStateColor(NuclearFrameStatType type, NuclearColor color) {m_pEngine->GetConfigManager()->SetFrameStateColor(type, color); }
	NuclearColor Env::GetFrameStateColor(NuclearFrameStatType type) const { return m_pEngine->GetConfigManager()->GetFrameStateColor(type); }

	void Env::SetDynamicMapLoading(bool b) { m_pEngine->GetConfigManager()->SetDynamicMapLoading(b); } // 是否动态加载地图资源，默认是true
	bool Env::IsDynamicMapLoading() const { return m_pEngine->GetConfigManager()->IsDynamicMapLoading(); }

	void Env::SetControlFPS(bool b) { m_pEngine->GetConfigManager()->SetControlFPS(b); } // 是否控制FPS，默认是true
	bool Env::IsControlFPS() const { return m_pEngine->GetConfigManager()->IsControlFPS(); }

	void Env::SetControlFPS(NuclearWindowState type, int fps) { m_pEngine->GetConfigManager()->SetControlFPS(type, fps); }//控制的FPS帧率，默认是60 60 30
	int Env::GetControlFPS(NuclearWindowState type) const { return m_pEngine->GetConfigManager()->GetControlFPS(type); }

	void Env::SetTaskExecuteTime(NuclearWindowState type, int time) { m_pEngine->SetTaskExecuteTime(type, time); }
	int Env::GetTaskExecuteTime(NuclearWindowState type) const { return m_pEngine->GetTaskExecuteTime(type); }

	void Env::SetIOUpdateExecuteTime(int time) { m_pEngine->SetIOUpdateExecuteTime(time); }
	int Env::GetIOUpdateExecuteTime() const { return m_pEngine->GetIOUpdateExecuteTime(); }

	void Env::SetRenderNightEffectByShader(bool b) { m_pEngine->GetConfigManager()->SetRenderNightEffectByShader(b); } // 是否使用测试shader
	bool Env::IsRenderNightEffectByShader() const { return m_pEngine->GetConfigManager()->IsRenderNightEffectByShader(); }

	bool Env::IsRenderNightEffectWithRenderTarget() const { return m_pEngine->GetConfigManager()->IsRenderNightEffectWithRenderTarget(); } // 使用rendertarget渲染夜晚效果
	void Env::SetRenderNightEffectWithRenderTarget(bool b) { m_pEngine->GetConfigManager()->SetRenderNightEffectWithRenderTarget(b); }

	bool Env::IsEnableMaskPic() const {  return m_pEngine->GetConfigManager()->IsEnableMaskPic(); }
	void Env::SetEnableMaskPic(bool b) {  m_pEngine->GetConfigManager()->SetEnableMaskPic(b); }

	bool Env::IsRenderSpriteShadow() const { return m_pEngine->GetConfigManager()->IsRenderSpriteShadow(); }	// 是否绘制sprite阴影，默认true
	void Env::SetRenderSpriteShadow(bool b) { m_pEngine->GetConfigManager()->SetRenderSpriteShadow(b); }

	bool Env::IsBlurForTeleport() const	{ return m_pEngine->GetConfigManager()->IsBlurForTeleport(); } // 在击退（瞬间移动）的时候是否带残影，默认true
	void Env::SetBlurForTeleport(bool b) { m_pEngine->GetConfigManager()->SetBlurForTeleport(b); }

	void Env::SetEnableSurfaceCache(bool b) { m_pEngine->GetConfigManager()->SetUseSurfaceCache(b); } // 是否使用地表Cache
	bool Env::IsEnableSurfaceCache() const { return m_pEngine->GetConfigManager()->IsUseSurfaceCache(); }

	void Env::SetSyncBeforeWater(bool b) { m_pEngine->GetConfigManager()->SetSyncBeforeWater(b); } // 是否同步加载水层以下的图
	bool Env::IsSyncBeforeWater() const { return m_pEngine->GetConfigManager()->IsSyncBeforeWater(); }

	void Env::SetEnableSortMapObjects(bool b) { m_pEngine->GetConfigManager()->SetSortMapObjects(b); } // 是否排序精灵和地图中层物件
	bool Env::IsEnableSortMapObjects() const { return m_pEngine->GetConfigManager()->IsSortMapObjects(); }

	void Env::SetEnableLinkedObjs(bool b) { m_pEngine->GetConfigManager()->SetEnableLinkedObjs(b); }
	bool Env::IsEnableLinkedObjs() const { return m_pEngine->GetConfigManager()->IsEnableLinkedObjs(); }

	bool Env::IsEnableWade() const { return m_pEngine->IsEnableWade(); }
	void Env::SetEnableWade(bool b) { m_pEngine->SetEnableWade(b); }

	void Env::ShowSpritePath(bool b)    { m_pEngine->GetConfigManager()->SetShowSpritePath(b); }
	void Env::ShowSpriteTrail(bool b)   { m_pEngine->GetConfigManager()->SetShowSpriteTrail(b); }
	void Env::ShowMapGrid(bool b)       { m_pEngine->GetConfigManager()->SetShowMazeGrid(b); }
	bool Env::IsShowSpritePath() const  { return m_pEngine->GetConfigManager()->IsShowSpritePath(); }
	bool Env::IsShowSpriteTrail() const { return m_pEngine->GetConfigManager()->IsShowSpriteTrail(); }
	bool Env::IsShowMapGrid() const     { return m_pEngine->GetConfigManager()->IsShowMazeGrid(); }

	void Env::SetMapMazeMask(unsigned int mask)   { m_pEngine->GetConfigManager()->SetMazeMask(mask); }
	unsigned int Env::GetMapMazeMask() const      { return m_pEngine->GetConfigManager()->GetMazeMask(); }
	
	//ani动画图片（包括角色和ani特效）多长时间不渲染就回收，默认20秒
	void Env::SetAniPicGCTime(int t) { m_pEngine->SetAniPicGCTime(t); }
	int Env::GetAniPicGCTime() const { return m_pEngine->GetAniPicGCTime(); }

	//ani动画结构（包括角色和ani特效）多长时间不渲染就回收，默认1200秒
	void Env::SetAniXapGCTime(int t) { m_pEngine->SetAniXapGCTime(t); }
	int Env::GetAniXapGCTime() const { return m_pEngine->GetAniXapGCTime(); }

	// 精灵半透明的方案，默认是XPSTT_ELEMENT_TRANSLUCENT
	NuclearSpriteTranslucentType Env::GetSpriteTranslucentType() const { return m_pEngine->GetConfigManager()->GetSpriteTranslucentType(); }
	void Env::SetSpriteTranslucentType(NuclearSpriteTranslucentType t) { m_pEngine->GetConfigManager()->SetSpriteTranslucentType(t); }

	// 默认0.6
	// 如果是XPSTT_ELEMENT_TRANSLUCENT，就是遮挡物Alpha；
	// 如果是XPSTT_SPRITE_TRANSLUCENT，就是精灵Alpha
	float Env::GetMaskAlpha() const { return m_pEngine->GetConfigManager()->GetMaskAlpha(); }
	void Env::SetMaskAlpha(float f) { m_pEngine->GetConfigManager()->SetMaskAlpha(f); }

	// 是否绘制遮挡物底图（如果是整图其实就不需要绘制了）
	bool Env::IsRenderSolidMask() const { return m_pEngine->GetConfigManager()->IsRenderSolidMask(); }
	void Env::SetRenderSolidMask(bool b) { m_pEngine->GetConfigManager()->SetRenderSolidMask(b); }

	//是否支持抗锯齿
	void Env::GetAvailableMultiSampleType(bool window, bool isMode16, std::vector<NuclearMultiSampleType> &types) { m_pEngine->GetRenderer()->GetAvailableMultiSampleType(window, isMode16, types); }

	//是否平滑移动
	bool Env::IsSmoothMove() const { return m_pEngine->GetConfigManager()->IsSmoothMove(); }
	void Env::SetSmoothMove(bool b) { m_pEngine->GetConfigManager()->SetSmoothMove(b); }

	// 最小的Delta
	short Env::GetMinDelta() const { return m_pEngine->GetConfigManager()->GetMinDelta(); }
	void Env::SetMinDelta(short d) { m_pEngine->GetConfigManager()->SetMinDelta(d); }

	// 相邻两帧之间的最大差值的绝对值
	short Env::GetMaxDiffDelta() const { return m_pEngine->GetConfigManager()->GetMaxDiffDelta(); }
	void Env::SetMaxDiffDelta(short d) { m_pEngine->GetConfigManager()->SetMaxDiffDelta(d); }

	// 和平均值之间的最大差值的绝对值
	short Env::GetMaxDiffFromAvg() const { return m_pEngine->GetConfigManager()->GetMaxDiffFromAvg(); }
	void Env::SetMaxDiffFromAvg(short d) { m_pEngine->GetConfigManager()->SetMaxDiffFromAvg(d); }

	void Env::SetSpriteMoveSmoothLimit(int iLimit) { m_pEngine->GetConfigManager()->SetSpriteMoveSmoothLimit(iLimit); }
	int  Env::GetSpriteMoveSmoothLimit() const	   { return m_pEngine->GetConfigManager()->GetSpriteMoveSmoothLimit(); }

	float Env::GetLogicToWorldScaleX() const { return m_pEngine->GetConfigManager()->GetLogicToWorldScaleX(); }
	void Env::SetLogicToWorldScaleX(float fScale) { m_pEngine->GetConfigManager()->SetLogicToWorldScaleX(fScale); }

	//当卸载地图的时候，声音淡出的时间（毫秒）
	int Env::GetUnloadMapBGSoundFadeOutTime() const { return m_pEngine->GetConfigManager()->GetUnloadMapBGSoundFadeOutTime(); }
	void Env::SetUnloadMapBGSoundFadeOutTime(int t) { m_pEngine->GetConfigManager()->SetUnloadMapBGSoundFadeOutTime(t); }
	
	//背景音的音量类型，默认0
	unsigned char Env::GetBGMType() const { return m_pEngine->GetConfigManager()->GetBGMType(); }
	void Env::SetBGMType(unsigned char t) { m_pEngine->GetConfigManager()->SetBGMType(t); }

	// 环境音的音量类型
	unsigned char Env::GetEnvSoundType() const { return m_pEngine->GetConfigManager()->GetEnvSoundType(); }
	void Env::SetEnvSoundType(unsigned char t) { m_pEngine->GetConfigManager()->SetEnvSoundType(t); }

	// 脚步声的音量类型
	unsigned char Env::GetStepSoundType() const { return m_pEngine->GetConfigManager()->GetStepSoundType(); }
	void Env::SetStepSoundType(unsigned char t) { m_pEngine->GetConfigManager()->SetStepSoundType(t); }

	//动作的声音类型
	unsigned char Env::GetActionSoundType(const std::wstring &action_name) const { return m_pEngine->GetSpriteManager()->GetActionSoundType(action_name); }
	void Env::SetActionSoundType(const std::wstring &action_name, unsigned char t) { return m_pEngine->GetSpriteManager()->SetActionSoundType(action_name, t); }

	// 脚步声的优先级
	short Env::GetStepSoundPriority() const { return m_pEngine->GetConfigManager()->GetStepSoundPriority(); }
	void Env::SetStepSoundPriority(short p) { m_pEngine->GetConfigManager()->SetStepSoundPriority(p); }

}