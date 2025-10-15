#ifndef __Nuclear_IENV_H
#define __Nuclear_IENV_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "common\xptypes.h"

namespace Nuclear
{

// �����������Ϸ�����޹صĶ���
class IEnv
{
	// ��������
public:
	IEnv() {}
	virtual ~IEnv() {  }

	// �ӿ�
public:
	virtual HWND GetWindow() = 0;						// �õ�����
	virtual bool SetDisplayMode(const XDisplayMode &dmode) = 0;	// ������ʾģʽ
	virtual XDisplayMode GetDisplayMode() const = 0;
	virtual bool IsSoundInited() const = 0;

	// ���
	// ע��Ӳ����꣬��������
	// @resource��ͼƬ·���� offset�ǹ����ͼƬ�ڵ�ƫ�ƣ���С�̶�Ϊ32*32, center�ǹ������
	virtual CursorHandle RegisterCursor(const std::wstring& resource, const CPOINT &offset, const CPOINT &center) = 0;
	// �°�Ĺ�꣬����ϵͳAPI������֧��cur ani��ʽ����Դ·��Ϊwindows���·�����߾���·������Դ���ܴ�����
	virtual XPCursorHandle RegisterCursor(const std::wstring& resource) = 0;
	// �������
	virtual bool ChangeCursor(CursorHandle hCursor) = 0;
	virtual bool ChangeCursor(XPCursorHandle hCursor) = 0;
	// �����Ƿ���ʾ���
	virtual bool ShowCursor(bool b) = 0;
	// ���ù������
	virtual bool SetCursorPosition(const CPOINT &point) = 0;
	// �õ��ر�����
	virtual void GetCursorPosition(CPOINT &point) const = 0;

	// ͬ�����ص�ͼ��ʱ���Ƿ����
	virtual bool EnableRenderDuringLoading(bool b) = 0;
	virtual bool IsRenderDuringLoading() const = 0;

	// ������Ļ������Ϣ����
	virtual void SetConsoleInfo(XPCONSOLE_INFO eInfo, bool bOn) { }
	virtual bool TestConsoleInfo(XPCONSOLE_INFO eInfo) const { return false; }

	// ֡״̬������Ϣ����
	virtual void SetFrameStateInfo(XPFRAMESTAT_TYPE type, bool bOn) { }//Ĭ�ϴ�Frame Time��Camera Dis��FileIO
	virtual bool TestFrameStateInfo(XPFRAMESTAT_TYPE type) const { return false;}

	// ֡״̬������Ϣ��ɫ
	virtual void SetFrameStateColor(XPFRAMESTAT_TYPE type, XPCOLOR color) {}//Ĭ��Ϊ�� �� �̣������޶���
	virtual XPCOLOR GetFrameStateColor(XPFRAMESTAT_TYPE type) const { return 0; }

	virtual void SetDynamicMapLoading(bool b) = 0; // �Ƿ�̬���ص�ͼ��Դ��Ĭ����true
	virtual bool IsDynamicMapLoading() const = 0;

	virtual void SetControlFPS(bool b) = 0; // �Ƿ����FPS��Ĭ����true�����ֻ���Ƽ���״̬���ں�̨����С��״̬�£����������趼��֡
	virtual bool IsControlFPS() const = 0;

	virtual void SetControlFPS(XPWINDOW_STATE type, int fps) = 0;//���Ƶ�FPS֡�ʣ�Ĭ����60 60 30
	virtual int GetControlFPS(XPWINDOW_STATE type) const = 0;

	virtual void SetTaskExecuteTime(XPWINDOW_STATE type, int time) = 0;	//ÿ֡Task��ʱ�䣬��λ�Ǻ��룬Ĭ��ֵ30
	virtual int GetTaskExecuteTime(XPWINDOW_STATE type) const = 0;

	virtual void SetIOUpdateExecuteTime(int time) = 0;	//ÿ֡�����̴߳����첽��������Դ��ʱ�䣬Ĭ��30
	virtual int GetIOUpdateExecuteTime() const = 0;

	///////////////////////////����5����ʱ��Ч
	virtual void SetTaskExecuteCount(XPWINDOW_STATE type, int count) = 0;//ÿִ֡�е�Task�ĸ�����-1Ϊ�����ƣ�ÿ֡��ִ���ꡣĬ�϶���-1
	virtual int GetTaskExecuteCount(XPWINDOW_STATE type) const = 0;

	virtual void SetTaskListMaxRemainCount(XPWINDOW_STATE type, int count) = 0;//ÿ֡ʣ�µ�Task���ĸ�����-1Ϊ�����ƣ�ÿ֡ʣ�¶���Ҳ����ν��Ĭ�϶���-1
	virtual int GetTaskListMaxRemainCount(XPWINDOW_STATE type) const = 0;

	virtual void SetIOUpdateFrame(int frame) = 0;//����֡����һ������
	//////////////////////////�������

	virtual void SetRenderNightEffectByShader(bool b) = 0; // �Ƿ�ʹ��shader��Ⱦҹ��Ч��
	virtual bool IsRenderNightEffectByShader() const = 0;

	virtual bool IsRenderNightEffectWithRenderTarget() const = 0; // ʹ��rendertarget��Ⱦҹ��Ч��
	virtual void SetRenderNightEffectWithRenderTarget(bool b) = 0;

	virtual bool IsEnableMaskPic() const = 0;	//ʹ�õ�ͼ�ϵĵ�ͼ��Ĭ�Ϲ�
	virtual void SetEnableMaskPic(bool b) = 0;

	virtual bool IsRenderSpriteShadow() const = 0;	// �Ƿ����sprite��Ӱ��Ĭ��true
	virtual void SetRenderSpriteShadow(bool b) = 0;

	virtual bool IsRenderAlphaSprite() const = 0; // �Ƿ���ư�͸�����Ĭ��true
	virtual void SetRenderAlphaSprite(bool b) = 0;

	virtual bool IsBlurForTeleport() const = 0;	// �ڻ��ˣ�˲���ƶ�����ʱ���Ƿ����Ӱ��Ĭ��true
	virtual void SetBlurForTeleport(bool b) = 0;

	virtual void SetEnableSurfaceCache(bool b) = 0; // �Ƿ�ʹ�õر�Cache
	virtual bool IsEnableSurfaceCache() const = 0;

	virtual bool IsSyncBeforeWater() const = 0;	// �Ƿ�ͬ������ˮ�����µ�ͼ
	virtual void SetSyncBeforeWater(bool b) = 0;

	virtual void SetEnableSortMapObjects(bool b) = 0; // �Ƿ�������͵�ͼ�в����
	virtual bool IsEnableSortMapObjects() const = 0;

	virtual void SetEnableLinkedObjs(bool b) = 0;	// ��̬�����lko���Ƿ�ᶯ
	virtual bool IsEnableLinkedObjs() const = 0;

	virtual XPCOMPUTERCONFIGTYPE GetSystemLevel() const = 0;	//��ǰ�ļ������Ч���ã�Ĭ����XPCOMPUTERCONFIG_H����
	virtual void SetSystemLevel(XPCOMPUTERCONFIGTYPE l) = 0;

	virtual void ShowSpritePath(bool b) = 0;  // ��ʾ����·�����ء�
	virtual void ShowSpriteTrail(bool b) = 0; // ��ʾ����β�����ء�
	virtual void ShowMapGrid(bool b) = 0;     // ��ʾ�Թ����ӿ��ء�

	virtual bool IsShowSpritePath() const = 0;  // ��ʾ����·�����ء�
	virtual bool IsShowSpriteTrail() const = 0; // ��ʾ����β�����ء�
	virtual bool IsShowMapGrid() const = 0;     // ��ʾ�Թ����ӿ��ء�

	virtual void SetMapMazeMask(unsigned int) = 0;   // ��ʾ�Թ����롣0����ʾ��
	virtual unsigned int GetMapMazeMask() const = 0; // ��ǰ��ʾ�Թ����롣

	virtual void SetAniPicGCTime(int t) = 0;		//ani����ͼƬ��������ɫ��ani��Ч���೤ʱ�䲻��Ⱦ�ͻ��գ�Ĭ��20000���루20�룩
	virtual int GetAniPicGCTime() const = 0;

	virtual void SetAniXapGCTime(int t) = 0;		//ani�����ṹ��������ɫ��ani��Ч���೤ʱ�䲻��Ⱦ�ͻ��գ�Ĭ��1200000���루20�֣�
	virtual int GetAniXapGCTime() const = 0;


	virtual bool IsSmoothMove() const = 0;  // �Ƿ�ƽ���ƶ�
	virtual void SetSmoothMove(bool b) = 0;

	virtual short GetMinDelta() const = 0;		// ��С��Delta
	virtual void SetMinDelta(short d) = 0;

	virtual short GetMaxDiffDelta() const = 0;	// ������֮֡�������ֵ�ľ���ֵ
	virtual void SetMaxDiffDelta(short d) = 0;

	virtual short GetMaxDiffFromAvg() const = 0;	// ��ƽ��ֵ֮�������ֵ�ľ���ֵ
	virtual void SetMaxDiffFromAvg(short d) = 0;

	virtual bool IsEnableWade() const = 0;		//��ˮ��͸��Ч��
	virtual void SetEnableWade(bool b) = 0;

	virtual int GetInactiveSoundFadeOutTime() const = 0; //������ʧȥ����ʱ������������ʱ�䣨���룬-1����ֹͣ���ţ�
	virtual void SetInactiveSoundFadeOutTime(int t) = 0;

	virtual int GetActiveSoundFadeInTime() const = 0; //���������¼���ʱ�����������ʱ�䣨���룬<0�����壩
	virtual void SetActiveSoundFadeInTime(int t) = 0;

	virtual int GetUnloadMapBGSoundFadeOutTime() const = 0; //��ж�ص�ͼ��ʱ������������ʱ�䣨���룩��Ĭ��0
	virtual void SetUnloadMapBGSoundFadeOutTime(int t) = 0; //��ж�ص�ͼ��ʱ������������ʱ�䣨���룩

	virtual unsigned char GetBGMType() const = 0;	//���������������ͣ�Ĭ��0
	virtual void SetBGMType(unsigned char t) = 0;

	virtual unsigned char GetEnvSoundType() const = 0;	// ���������������ͣ�Ĭ��1
	virtual void SetEnvSoundType(unsigned char t) = 0;

	virtual unsigned char GetStepSoundType() const = 0;	// �Ų������������ͣ�Ĭ��1
	virtual void SetStepSoundType(unsigned char t) = 0;

	virtual unsigned char GetActionSoundType(const std::wstring &action_name) const = 0;	//�������������ͣ�Ĭ��0
	virtual void SetActionSoundType(const std::wstring &action_name, unsigned char t) = 0;

	virtual short GetStepSoundPriority() const = 0; // �Ų��������ȼ���Ĭ��40
	virtual void SetStepSoundPriority(short p) = 0;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ���þ���Ѱ·�ƶ�ʱ��ƽ���������á�
	//
	// =  1  ��ƽ�����������ظ������ĵ������ƶ����� 8 ��������ƶ���
	// <= 0  ����ƽ����������·��ʱ��һ����������и��ӵ�ƽ������
	// >  1  ����ƽ���������߱ߴ�������һ��ƽ�������������Ժ��ٴ���ʣ�µĸ��ӣ�ÿ�δ�������������������ơ�
	//       ����Ч���ϵ��Ż���80~100Ӧ����һ�����ʵ�ֵ��Ĭ��Ϊ 80����Ϊ 80 > 1024/16
	virtual void SetSpriteMoveSmoothLimit(int iLimit) = 0;
	virtual int  GetSpriteMoveSmoothLimit() const = 0;

	// �߼����굽��������任(X����)��ת������
	virtual float GetLogicToWorldScaleX() const = 0;
	virtual void SetLogicToWorldScaleX(float fScale) =0;

	//Ŀǰ����Ҵ����õ�...�Կ�ժҪ��Ϣ
	virtual std::wstring GetDisplayDeviceInfo() =0;

	// ռ��
private:
	IEnv(const IEnv&);
	IEnv& operator=(const IEnv&);

};


}


#endif