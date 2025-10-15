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

	// ͬ�����ص�ͼ��ʱ���Ƿ���ƣ�Ĭ��true
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

	void Env::SetIOUpdateFrame(int frame)//����֡����һ������
	{
		m_pEngine->GetFileIOManager()->SetFrame(frame);
	}

	// ���
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

	bool Env::SetDisplayMode(const XDisplayMode &dmode)	// ������ʾģʽ
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

	// ֡״̬������Ϣ����
	void Env::SetFrameStateInfo(XPFRAMESTAT_TYPE type, bool bOn) { m_pEngine->GetConfigManager()->SetFrameStateInfo(type, bOn); }
	bool Env::TestFrameStateInfo(XPFRAMESTAT_TYPE type) const { return m_pEngine->GetConfigManager()->TestFrameStateInfo(type); }
	// ֡״̬������Ϣ��ɫ
	void Env::SetFrameStateColor(XPFRAMESTAT_TYPE type, XPCOLOR color) {m_pEngine->GetConfigManager()->SetFrameStateColor(type, color); }
	XPCOLOR Env::GetFrameStateColor(XPFRAMESTAT_TYPE type) const { return m_pEngine->GetConfigManager()->GetFrameStateColor(type); }

	void Env::SetDynamicMapLoading(bool b) { m_pEngine->GetConfigManager()->SetDynamicMapLoading(b); } // �Ƿ�̬���ص�ͼ��Դ��Ĭ����true
	bool Env::IsDynamicMapLoading() const { return m_pEngine->GetConfigManager()->IsDynamicMapLoading(); }

	void Env::SetControlFPS(bool b) { m_pEngine->GetConfigManager()->SetControlFPS(b); } // �Ƿ����FPS��Ĭ����true
	bool Env::IsControlFPS() const { return m_pEngine->GetConfigManager()->IsControlFPS(); }

	void Env::SetControlFPS(XPWINDOW_STATE type, int fps) { m_pEngine->GetConfigManager()->SetControlFPS(type, fps); }//���Ƶ�FPS֡�ʣ�Ĭ����60 60 30
	int Env::GetControlFPS(XPWINDOW_STATE type) const { return m_pEngine->GetConfigManager()->GetControlFPS(type); }

	void Env::SetTaskExecuteTime(XPWINDOW_STATE type, int time) { m_pEngine->SetTaskExecuteTime(type, time); }
	int Env::GetTaskExecuteTime(XPWINDOW_STATE type) const { return m_pEngine->GetTaskExecuteTime(type); }

	void Env::SetIOUpdateExecuteTime(int time) { m_pEngine->SetIOUpdateExecuteTime(time); }
	int Env::GetIOUpdateExecuteTime() const { return m_pEngine->GetIOUpdateExecuteTime(); }

	void Env::SetTaskExecuteCount(XPWINDOW_STATE type, int count) { m_pEngine->SetTaskExecuteCount(type, count); }//ÿִ֡�е�Task�ĸ�����-1Ϊ������
	int Env::GetTaskExecuteCount(XPWINDOW_STATE type) const { return m_pEngine->GetTaskExecuteCount(type); }

	void Env::SetTaskListMaxRemainCount(XPWINDOW_STATE type, int count) { m_pEngine->SetTaskListMaxRemainCount(type, count); }//ÿ֡ʣ�µ�Task���ĸ�����-1Ϊ������
	int Env::GetTaskListMaxRemainCount(XPWINDOW_STATE type) const { return m_pEngine->GetTaskListMaxRemainCount(type); }

	void Env::SetRenderNightEffectByShader(bool b) { m_pEngine->GetConfigManager()->SetRenderNightEffectByShader(b); } // �Ƿ�ʹ�ò���shader
	bool Env::IsRenderNightEffectByShader() const { return m_pEngine->GetConfigManager()->IsRenderNightEffectByShader(); }

	bool Env::IsRenderNightEffectWithRenderTarget() const { return m_pEngine->GetConfigManager()->IsRenderNightEffectWithRenderTarget(); } // ʹ��rendertarget��Ⱦҹ��Ч��
	void Env::SetRenderNightEffectWithRenderTarget(bool b) { m_pEngine->GetConfigManager()->SetRenderNightEffectWithRenderTarget(b); }

	bool Env::IsEnableMaskPic() const {  return m_pEngine->GetConfigManager()->IsEnableMaskPic(); }
	void Env::SetEnableMaskPic(bool b) {  m_pEngine->GetConfigManager()->SetEnableMaskPic(b); }

	bool Env::IsRenderSpriteShadow() const { return m_pEngine->GetConfigManager()->IsRenderSpriteShadow(); }	// �Ƿ����sprite��Ӱ��Ĭ��true
	void Env::SetRenderSpriteShadow(bool b) { m_pEngine->GetConfigManager()->SetRenderSpriteShadow(b); }

	bool Env::IsRenderAlphaSprite() const { return m_pEngine->GetConfigManager()->IsRenderAlphaSprite(); }	 // �Ƿ���ư�͸�����Ĭ��true
	void Env::SetRenderAlphaSprite(bool b) { m_pEngine->GetConfigManager()->SetRenderAlphaSprite(b); }

	bool Env::IsBlurForTeleport() const	{ return m_pEngine->GetConfigManager()->IsBlurForTeleport(); } // �ڻ��ˣ�˲���ƶ�����ʱ���Ƿ����Ӱ��Ĭ��true
	void Env::SetBlurForTeleport(bool b) { m_pEngine->GetConfigManager()->SetBlurForTeleport(b); }

	void Env::SetEnableSurfaceCache(bool b) { m_pEngine->GetConfigManager()->SetUseSurfaceCache(b); } // �Ƿ�ʹ�õر�Cache
	bool Env::IsEnableSurfaceCache() const { return m_pEngine->GetConfigManager()->IsUseSurfaceCache(); }

	void Env::SetSyncBeforeWater(bool b) { m_pEngine->GetConfigManager()->SetSyncBeforeWater(b); } // �Ƿ�ͬ������ˮ�����µ�ͼ
	bool Env::IsSyncBeforeWater() const { return m_pEngine->GetConfigManager()->IsSyncBeforeWater(); }

	void Env::SetEnableSortMapObjects(bool b) { m_pEngine->GetConfigManager()->SetSortMapObjects(b); } // �Ƿ�������͵�ͼ�в����
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

	//��ǰ�ļ������Ч���ã�Ĭ����XPCOMPUTERCONFIG_H����
	XPCOMPUTERCONFIGTYPE Env::GetSystemLevel() const   { return m_pEngine->GetConfigManager()->GetSystemLevel(); }
	void Env::SetSystemLevel(XPCOMPUTERCONFIGTYPE l)   { m_pEngine->GetConfigManager()->SetSystemLevel(l); }
	
	//ani����ͼƬ��������ɫ��ani��Ч���೤ʱ�䲻��Ⱦ�ͻ��գ�Ĭ��20��
	void Env::SetAniPicGCTime(int t) { m_pEngine->SetAniPicGCTime(t); }
	int Env::GetAniPicGCTime() const { return m_pEngine->GetAniPicGCTime(); }

	//ani�����ṹ��������ɫ��ani��Ч���೤ʱ�䲻��Ⱦ�ͻ��գ�Ĭ��1200��
	void Env::SetAniXapGCTime(int t) { m_pEngine->SetAniXapGCTime(t); }
	int Env::GetAniXapGCTime() const { return m_pEngine->GetAniXapGCTime(); }

	//�Ƿ�ƽ���ƶ�
	bool Env::IsSmoothMove() const { return m_pEngine->GetConfigManager()->IsSmoothMove(); }
	void Env::SetSmoothMove(bool b) { m_pEngine->GetConfigManager()->SetSmoothMove(b); }

	// ��С��Delta
	short Env::GetMinDelta() const { return m_pEngine->GetConfigManager()->GetMinDelta(); }
	void Env::SetMinDelta(short d) { m_pEngine->GetConfigManager()->SetMinDelta(d); }

	// ������֮֡�������ֵ�ľ���ֵ
	short Env::GetMaxDiffDelta() const { return m_pEngine->GetConfigManager()->GetMaxDiffDelta(); }
	void Env::SetMaxDiffDelta(short d) { m_pEngine->GetConfigManager()->SetMaxDiffDelta(d); }

	// ��ƽ��ֵ֮�������ֵ�ľ���ֵ
	short Env::GetMaxDiffFromAvg() const { return m_pEngine->GetConfigManager()->GetMaxDiffFromAvg(); }
	void Env::SetMaxDiffFromAvg(short d) { m_pEngine->GetConfigManager()->SetMaxDiffFromAvg(d); }

	void Env::SetSpriteMoveSmoothLimit(int iLimit) { m_pEngine->GetConfigManager()->SetSpriteMoveSmoothLimit(iLimit); }
	int  Env::GetSpriteMoveSmoothLimit() const	   { return m_pEngine->GetConfigManager()->GetSpriteMoveSmoothLimit(); }

	float Env::GetLogicToWorldScaleX() const { return m_pEngine->GetConfigManager()->GetLogicToWorldScaleX(); }
	void Env::SetLogicToWorldScaleX(float fScale) { m_pEngine->GetConfigManager()->SetLogicToWorldScaleX(fScale); }

	//������ʧȥ����ʱ������������ʱ�䣨���룬-1����ֹͣ���ţ�
	int Env::GetInactiveSoundFadeOutTime() const { return m_pEngine->GetSoundManager()->GetInactiveSoundFadeOutTime(); }
	void Env::SetInactiveSoundFadeOutTime(int t) { m_pEngine->GetSoundManager()->SetInactiveSoundFadeOutTime(t); }

	//���������¼���ʱ�����������ʱ�䣨���룬<0�����壩
	int Env::GetActiveSoundFadeInTime() const { return m_pEngine->GetSoundManager()->GetActiveSoundFadeInTime(); }
	void Env::SetActiveSoundFadeInTime(int t) { m_pEngine->GetSoundManager()->SetActiveSoundFadeInTime(t); }

	//��ж�ص�ͼ��ʱ������������ʱ�䣨���룩
	int Env::GetUnloadMapBGSoundFadeOutTime() const { return m_pEngine->GetConfigManager()->GetUnloadMapBGSoundFadeOutTime(); }
	void Env::SetUnloadMapBGSoundFadeOutTime(int t) { m_pEngine->GetConfigManager()->SetUnloadMapBGSoundFadeOutTime(t); }
	
	//���������������ͣ�Ĭ��0
	unsigned char Env::GetBGMType() const { return m_pEngine->GetConfigManager()->GetBGMType(); }
	void Env::SetBGMType(unsigned char t) { m_pEngine->GetConfigManager()->SetBGMType(t); }

	// ����������������
	unsigned char Env::GetEnvSoundType() const { return m_pEngine->GetConfigManager()->GetEnvSoundType(); }
	void Env::SetEnvSoundType(unsigned char t) { m_pEngine->GetConfigManager()->SetEnvSoundType(t); }

	// �Ų�������������
	unsigned char Env::GetStepSoundType() const { return m_pEngine->GetConfigManager()->GetStepSoundType(); }
	void Env::SetStepSoundType(unsigned char t) { m_pEngine->GetConfigManager()->SetStepSoundType(t); }

	//��������������
	unsigned char Env::GetActionSoundType(const std::wstring &action_name) const { return m_pEngine->GetSpriteManager()->GetActionSoundType(action_name); }
	void Env::SetActionSoundType(const std::wstring &action_name, unsigned char t) { return m_pEngine->GetSpriteManager()->SetActionSoundType(action_name, t); }

	// �Ų��������ȼ�
	short Env::GetStepSoundPriority() const { return m_pEngine->GetConfigManager()->GetStepSoundPriority(); }
	void Env::SetStepSoundPriority(short p) { m_pEngine->GetConfigManager()->SetStepSoundPriority(p); }

	//Ŀǰ����Ҵ����õ�...�Կ�ժҪ��Ϣ
	std::wstring Env::GetDisplayDeviceInfo()
	{
		return m_pEngine->GetSystemResourceManager()->GetCurDisplayDevice();
	}
}