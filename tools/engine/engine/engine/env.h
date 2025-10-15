
#ifndef __Nuclear_ENV_H
#define __Nuclear_ENV_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\ienv.h"

namespace Nuclear
{

class Engine;
// ���ת������
class Env : public IEnv
{

public:
	Env(Engine *pEngine) : m_pEngine(pEngine) { }
	virtual ~Env() { }
	virtual HWND GetWindow();					// �õ�����
	virtual bool SetDisplayMode(const XDisplayMode &dmode);	// ������ʾģʽ
	virtual XDisplayMode GetDisplayMode() const;
	virtual bool IsSoundInited() const;

	// ���
	// ע��Ӳ����꣬��������
	// @resource��ͼƬ·���� offset�ǹ����ͼƬ�ڵ�ƫ�ƣ���С�̶�Ϊ32*32, center�ǹ������
	virtual CursorHandle RegisterCursor(const std::wstring& resource, const CPOINT &offset, const CPOINT &center);
	// �°�Ĺ�꣬����ϵͳAPI������֧��cur ani��ʽ����Դ·��Ϊwindows���·�����߾���·������Դ���ܴ�����
	virtual XPCursorHandle RegisterCursor(const std::wstring& resource);
	// �������
	virtual bool ChangeCursor(CursorHandle hCursor);
	virtual bool ChangeCursor(XPCursorHandle hCursor);
	// �����Ƿ���ʾ���
	virtual bool ShowCursor(bool b);
	// ���ù������
	virtual bool SetCursorPosition(const CPOINT &point);
	// �õ��ر�����
	virtual void GetCursorPosition(CPOINT &point) const;

	// ������Ļ������Ϣ����
	virtual void SetConsoleInfo(XPCONSOLE_INFO eInfo, bool bOn);
	virtual bool TestConsoleInfo(XPCONSOLE_INFO eInfo) const;

	// ֡״̬������Ϣ����
	virtual void SetFrameStateInfo(XPFRAMESTAT_TYPE type, bool bOn);//Ĭ�ϴ�Frame Time��Camera Dis��FileIO
	virtual bool TestFrameStateInfo(XPFRAMESTAT_TYPE type) const;
	// ֡״̬������Ϣ��ɫ
	virtual void SetFrameStateColor(XPFRAMESTAT_TYPE type, XPCOLOR color);//Ĭ��Ϊ�� �� �̣������޶���
	virtual XPCOLOR GetFrameStateColor(XPFRAMESTAT_TYPE type) const;

	virtual void SetDynamicMapLoading(bool x); // �Ƿ�̬���ص�ͼ��Դ��Ĭ����true
	virtual bool IsDynamicMapLoading() const;

	virtual void SetControlFPS(bool x); // �Ƿ����FPS��Ĭ����true
	virtual bool IsControlFPS() const;

	virtual void SetControlFPS(XPWINDOW_STATE type, int fps);//���Ƶ�FPS֡�ʣ�Ĭ����60 60 30
	virtual int GetControlFPS(XPWINDOW_STATE type) const;

	virtual void SetTaskExecuteTime(XPWINDOW_STATE type, int time);	//ÿ֡Task��ʱ�䣬��λ�Ǻ��룬Ĭ��ֵ30
	virtual int GetTaskExecuteTime(XPWINDOW_STATE type) const;

	virtual void SetIOUpdateExecuteTime(int time);	//ÿ֡�����̴߳����첽��������Դ��ʱ��
	virtual int GetIOUpdateExecuteTime() const;

	virtual void SetTaskExecuteCount(XPWINDOW_STATE type, int count);//ÿִ֡�е�Task�ĸ�����-1Ϊ������
	virtual int GetTaskExecuteCount(XPWINDOW_STATE type) const;

	virtual void SetTaskListMaxRemainCount(XPWINDOW_STATE type, int count);//ÿ֡ʣ�µ�Task���ĸ�����-1Ϊ������
	virtual int GetTaskListMaxRemainCount(XPWINDOW_STATE type) const;

	virtual void SetRenderNightEffectByShader(bool b); // �Ƿ�ʹ��shader��Ⱦҹ��Ч��
	virtual bool IsRenderNightEffectByShader() const;

	virtual bool IsRenderNightEffectWithRenderTarget() const; // ʹ��rendertarget��Ⱦҹ��Ч��
	virtual void SetRenderNightEffectWithRenderTarget(bool b);

	virtual bool EnableRenderDuringLoading(bool b); // ͬ�����ص�ͼ��ʱ���Ƿ���ƣ�Ĭ��true
	virtual bool IsRenderDuringLoading() const;

	virtual bool IsEnableMaskPic() const;	//ʹ�õ�ͼ�ϵĵ�ͼ��Ĭ�Ϲ�
	virtual void SetEnableMaskPic(bool b);

	virtual bool IsRenderSpriteShadow() const;	// �Ƿ����sprite��Ӱ��Ĭ��true
	virtual void SetRenderSpriteShadow(bool b);

	virtual bool IsRenderAlphaSprite() const; // �Ƿ���ư�͸�����Ĭ��true
	virtual void SetRenderAlphaSprite(bool b);

	virtual bool IsBlurForTeleport() const;	// �ڻ��ˣ�˲���ƶ�����ʱ���Ƿ����Ӱ��Ĭ��true
	virtual void SetBlurForTeleport(bool b);

	virtual void SetEnableSurfaceCache(bool b); // �Ƿ�ʹ�õر�Cache
	virtual bool IsEnableSurfaceCache() const;

	virtual bool IsSyncBeforeWater() const;	// �Ƿ�ͬ������ˮ�����µ�ͼ
	virtual void SetSyncBeforeWater(bool b);

	virtual void SetEnableSortMapObjects(bool b); // �Ƿ�������͵�ͼ�в����
	virtual bool IsEnableSortMapObjects() const;

	virtual void SetEnableLinkedObjs(bool b);	// ��̬�����lko���Ƿ�ᶯ
	virtual bool IsEnableLinkedObjs() const;

	virtual bool IsEnableWade() const;		//��ˮ��͸��Ч��
	virtual void SetEnableWade(bool b);

	virtual XPCOMPUTERCONFIGTYPE GetSystemLevel() const;	//��ǰ�ļ������Ч���ã�Ĭ����XPCOMPUTERCONFIG_H����
	virtual void SetSystemLevel(XPCOMPUTERCONFIGTYPE l);

	virtual void SetAniPicGCTime(int t);		//ani����ͼƬ��������ɫ��ani��Ч���೤ʱ�䲻��Ⱦ�ͻ��գ�Ĭ��20000���루20�룩
	virtual int GetAniPicGCTime() const;

	virtual void SetAniXapGCTime(int t);		//ani�����ṹ��������ɫ��ani��Ч���೤ʱ�䲻��Ⱦ�ͻ��գ�Ĭ��1200000���루20�֣�
	virtual int GetAniXapGCTime() const;

	virtual void ShowSpritePath(bool b);  // ��ʾ����·�����ء�
	virtual void ShowSpriteTrail(bool b); // ��ʾ����β�����ء�
	virtual void ShowMapGrid(bool b);     // ��ʾ�Թ����ӿ��ء�

	virtual bool IsShowSpritePath() const;  // ��ʾ����·�����ء�
	virtual bool IsShowSpriteTrail() const; // ��ʾ����β�����ء�
	virtual bool IsShowMapGrid() const;     // ��ʾ�Թ����ӿ��ء�

	virtual void SetMapMazeMask(unsigned int mask);   // ��ʾ�Թ����롣0����ʾ��
	virtual unsigned int GetMapMazeMask() const; // ��ǰ��ʾ�Թ����롣

	virtual bool IsSmoothMove() const;  // �Ƿ�ƽ���ƶ�
	virtual void SetSmoothMove(bool b);

	virtual short GetMinDelta() const;		// ��С��Delta
	virtual void SetMinDelta(short d);

	virtual short GetMaxDiffDelta() const;	// ������֮֡�������ֵ�ľ���ֵ
	virtual void SetMaxDiffDelta(short d);

	virtual short GetMaxDiffFromAvg() const;	// ��ƽ��ֵ֮�������ֵ�ľ���ֵ
	virtual void SetMaxDiffFromAvg(short d);

	virtual void SetSpriteMoveSmoothLimit(int iLimit);
	virtual int  GetSpriteMoveSmoothLimit() const;

	//������ʧȥ����ʱ������������ʱ�䣨���룬-1����ֹͣ���ţ�
	virtual int GetInactiveSoundFadeOutTime() const;
	virtual void SetInactiveSoundFadeOutTime(int t);

	//���������¼���ʱ�����������ʱ�䣨���룬<0�����壩
	virtual int GetActiveSoundFadeInTime() const; 
	virtual void SetActiveSoundFadeInTime(int t);

	//��ж�ص�ͼ��ʱ������������ʱ�䣨���룩
	virtual int GetUnloadMapBGSoundFadeOutTime() const; 
	virtual void SetUnloadMapBGSoundFadeOutTime(int t);

	// ����������������
	virtual unsigned char GetBGMType() const;
	virtual void SetBGMType(unsigned char t);

	// ����������������
	virtual unsigned char GetEnvSoundType() const;
	virtual void SetEnvSoundType(unsigned char t);

	// �Ų�������������
	virtual unsigned char GetStepSoundType() const;
	virtual void SetStepSoundType(unsigned char t);

	//��������������
	virtual unsigned char GetActionSoundType(const std::wstring &action_name) const;
	virtual void SetActionSoundType(const std::wstring &action_name, unsigned char t);

	// �Ų��������ȼ�
	virtual short GetStepSoundPriority() const;
	virtual void SetStepSoundPriority(short p);

	// �߼����굽��������任(X����)��ת������
	virtual float GetLogicToWorldScaleX() const;
	virtual void SetLogicToWorldScaleX(float fScale);

	//Ŀǰ����Ҵ����õ�...�Կ�ժҪ��Ϣ
	virtual std::wstring GetDisplayDeviceInfo();

	virtual void SetIOUpdateFrame(int frame);//����֡����һ������

private:
	Engine* m_pEngine;

};

}

#endif