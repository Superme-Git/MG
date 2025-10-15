#include "stdafx.h"
#include "cursormanager.h"
#include "configmanager.h"
#include "..\sprite\spritemanager.h"
#include "engine.h"
#include "env.h"

namespace Nuclear
{

	HWND Env::GetWindow()
	{
		return m_pEngine->GetWindow();
	}

	bool Env::IsSoundInited() const
	{
		return m_pEngine->GetSystemResourceManager()->IsSoundInited();
	}

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

	void Env::SetIOUpdateFrame(int frame)//多少帧创建一张纹理
	{
		m_pEngine->GetFileIOManager()->SetFrame(frame);
	}

	// 光标
	bool Env::ShowCursor(bool b)
	{
		return m_pEngine->GetCursorManager()->ShowCursor(b);
	}

	bool Env::SetCursorPosition(const CPOINT &point)
	{
		return m_pEngine->GetCursorManager()->SetCursorPosition(point);
	}

	CursorHandle Env::RegisterCursor(const std::wstring& resource, const CPOINT &offset, const CPOINT &center)
	{
		return m_pEngine->GetCursorManager()->RegisterCursor(resource, offset, center);
	}

	XPCursorHandle Env::RegisterCursor(const std::wstring& resource)
	{
		return m_pEngine->GetCursorManager()->RegisterCursor(resource);
	}

	bool Env::ChangeCursor(CursorHandle hCursor) // 
	{
		return m_pEngine->GetCursorManager()->ChangeCursor(hCursor);
	}

	bool Env::ChangeCursor(XPCursorHandle hCursor) // 
	{
		return m_pEngine->GetCursorManager()->ChangeCursor(hCursor);
	}

	bool Env::SetDisplayMode(const XDisplayMode &dmode)	// 设置显示模式
	{
		return m_pEngine->SetDisplayMode(dmode);
	}

	XDisplayMode Env::GetDisplayMode() const
	{
		return m_pEngine->GetRenderer()->GetDisplayMode();
	}

	void Env::GetCursorPosition(CPOINT &point) const
	{
		m_pEngine->GetCursorManager()->GetCursorPosition(point);
	}

	void Env::SetConsoleInfo(XPCONSOLE_INFO eInfo, bool bOn) { m_pEngine->GetConfigManager()->SetConsoleInfo(eInfo, bOn); }
	bool Env::TestConsoleInfo(XPCONSOLE_INFO eInfo) const { return m_pEngine->GetConfigManager()->TestConsoleInfo(eInfo); }

	// 帧状态调试信息开关
	void Env::SetFrameStateInfo(XPFRAMESTAT_TYPE type, bool bOn) { m_pEngine->GetConfigManager()->SetFrameStateInfo(type, bOn); }
	bool Env::TestFrameStateInfo(XPFRAMESTAT_TYPE type) const { return m_pEngine->GetConfigManager()->TestFrameStateInfo(type); }
	// 帧状态调试信息颜色
	void Env::SetFrameStateColor(XPFRAMESTAT_TYPE type, XPCOLOR color) {m_pEngine->GetConfigManager()->SetFrameStateColor(type, color); }
	XPCOLOR Env::GetFrameStateColor(XPFRAMESTAT_TYPE type) const { return m_pEngine->GetConfigManager()->GetFrameStateColor(type); }

	void Env::SetDynamicMapLoading(bool b) { m_pEngine->GetConfigManager()->SetDynamicMapLoading(b); } // 是否动态加载地图资源，默认是true
	bool Env::IsDynamicMapLoading() const { return m_pEngine->GetConfigManager()->IsDynamicMapLoading(); }

	void Env::SetControlFPS(bool b) { m_pEngine->GetConfigManager()->SetControlFPS(b); } // 是否控制FPS，默认是true
	bool Env::IsControlFPS() const { return m_pEngine->GetConfigManager()->IsControlFPS(); }

	void Env::SetControlFPS(XPWINDOW_STATE type, int fps) { m_pEngine->GetConfigManager()->SetControlFPS(type, fps); }//控制的FPS帧率，默认是60 60 30
	int Env::GetControlFPS(XPWINDOW_STATE type) const { return m_pEngine->GetConfigManager()->GetControlFPS(type); }

	void Env::SetTaskExecuteTime(XPWINDOW_STATE type, int time) { m_pEngine->SetTaskExecuteTime(type, time); }
	int Env::GetTaskExecuteTime(XPWINDOW_STATE type) const { return m_pEngine->GetTaskExecuteTime(type); }

	void Env::SetIOUpdateExecuteTime(int time) { m_pEngine->SetIOUpdateExecuteTime(time); }
	int Env::GetIOUpdateExecuteTime() const { return m_pEngine->GetIOUpdateExecuteTime(); }

	void Env::SetTaskExecuteCount(XPWINDOW_STATE type, int count) { m_pEngine->SetTaskExecuteCount(type, count); }//每帧执行的Task的个数，-1为不限制
	int Env::GetTaskExecuteCount(XPWINDOW_STATE type) const { return m_pEngine->GetTaskExecuteCount(type); }

	void Env::SetTaskListMaxRemainCount(XPWINDOW_STATE type, int count) { m_pEngine->SetTaskListMaxRemainCount(type, count); }//每帧剩下的Task最多的个数，-1为不限制
	int Env::GetTaskListMaxRemainCount(XPWINDOW_STATE type) const { return m_pEngine->GetTaskListMaxRemainCount(type); }

	void Env::SetRenderNightEffectByShader(bool b) { m_pEngine->GetConfigManager()->SetRenderNightEffectByShader(b); } // 是否使用测试shader
	bool Env::IsRenderNightEffectByShader() const { return m_pEngine->GetConfigManager()->IsRenderNightEffectByShader(); }

	bool Env::IsRenderNightEffectWithRenderTarget() const { return m_pEngine->GetConfigManager()->IsRenderNightEffectWithRenderTarget(); } // 使用rendertarget渲染夜晚效果
	void Env::SetRenderNightEffectWithRenderTarget(bool b) { m_pEngine->GetConfigManager()->SetRenderNightEffectWithRenderTarget(b); }

	bool Env::IsEnableMaskPic() const {  return m_pEngine->GetConfigManager()->IsEnableMaskPic(); }
	void Env::SetEnableMaskPic(bool b) {  m_pEngine->GetConfigManager()->SetEnableMaskPic(b); }

	bool Env::IsRenderSpriteShadow() const { return m_pEngine->GetConfigManager()->IsRenderSpriteShadow(); }	// 是否绘制sprite阴影，默认true
	void Env::SetRenderSpriteShadow(bool b) { m_pEngine->GetConfigManager()->SetRenderSpriteShadow(b); }

	bool Env::IsRenderAlphaSprite() const { return m_pEngine->GetConfigManager()->IsRenderAlphaSprite(); }	 // 是否绘制半透明人物，默认true
	void Env::SetRenderAlphaSprite(bool b) { m_pEngine->GetConfigManager()->SetRenderAlphaSprite(b); }

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

	//当前的计算机特效配置，默认是XPCOMPUTERCONFIG_H高配
	XPCOMPUTERCONFIGTYPE Env::GetSystemLevel() const   { return m_pEngine->GetConfigManager()->GetSystemLevel(); }
	void Env::SetSystemLevel(XPCOMPUTERCONFIGTYPE l)   { m_pEngine->GetConfigManager()->SetSystemLevel(l); }
	
	//ani动画图片（包括角色和ani特效）多长时间不渲染就回收，默认20秒
	void Env::SetAniPicGCTime(int t) { m_pEngine->SetAniPicGCTime(t); }
	int Env::GetAniPicGCTime() const { return m_pEngine->GetAniPicGCTime(); }

	//ani动画结构（包括角色和ani特效）多长时间不渲染就回收，默认1200秒
	void Env::SetAniXapGCTime(int t) { m_pEngine->SetAniXapGCTime(t); }
	int Env::GetAniXapGCTime() const { return m_pEngine->GetAniXapGCTime(); }

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

	//当窗口失去激活时，声音淡出的时间（毫秒，-1代表不停止播放）
	int Env::GetInactiveSoundFadeOutTime() const { return m_pEngine->GetSoundManager()->GetInactiveSoundFadeOutTime(); }
	void Env::SetInactiveSoundFadeOutTime(int t) { m_pEngine->GetSoundManager()->SetInactiveSoundFadeOutTime(t); }

	//当窗口重新激活时，声音淡入的时间（毫秒，<0无意义）
	int Env::GetActiveSoundFadeInTime() const { return m_pEngine->GetSoundManager()->GetActiveSoundFadeInTime(); }
	void Env::SetActiveSoundFadeInTime(int t) { m_pEngine->GetSoundManager()->SetActiveSoundFadeInTime(t); }

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

	//目前反外挂答题用到...显卡摘要信息
	std::wstring Env::GetDisplayDeviceInfo()
	{
		return m_pEngine->GetSystemResourceManager()->GetCurDisplayDevice();
	}
}