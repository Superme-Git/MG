#ifndef __Nuclear_CONFIGMANAGER_H
#define __Nuclear_CONFIGMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"

namespace Nuclear
{

	class EngineBase;
	class Renderer;
	class SystemResourceManager;

	// �����û����á���������

	class ConfigManager
	{
	public:
		ConfigManager(EngineBase *pEB);
		~ConfigManager();

	public:
		// ��ʼ����֮��ſ���ʹ��������Ա�ӿ�
		void Init();

		// ���¶�̬�Զ�����
		void Update();

		// ��ʱ�����Զ�����
		void OnTimer(int iPeriod/*ms*/);

		/* ========================  /
		   �Զ�������Ϣ��ؽӿ�
		/  ======================== */
	public:
		// ��������ĳ�����
		// @���棬OnTimer����
		XPPOOL_TYPE GetCreateTexturePoolType() const;

		
		/* ========================  /
		   �û�������Ϣ��ؽӿ�
		/  ======================== */
	public:
		// �����ƶ�ѡ��
		// ÿ��ƽ���������ơ�Ϊ1��ʱ��ƽ�����൱��ֱ�Ӱ�apath�ߡ�
		int GetSpriteMoveSmoothLimit() const; // see sprite.cpp::MoveTo
		void SetSpriteMoveSmoothLimit(int iLimit);

		// �߼����굽��������任(X����)��ת������
		float GetLogicToWorldScaleX() const;
		void SetLogicToWorldScaleX(float fScale);

		// ʹ��shader��Ⱦҹ��Ч��
		bool IsRenderNightEffectByShader() const;
		void SetRenderNightEffectByShader(bool b);

		// ʹ��rendertarget��Ⱦҹ��Ч��
		bool IsRenderNightEffectWithRenderTarget() const;
		void SetRenderNightEffectWithRenderTarget(bool b);

		//ʹ�õ�ͼ�ϵĵ�ͼ��Ĭ�Ϲ�
		bool IsEnableMaskPic() const;
		void SetEnableMaskPic(bool b);

		// �Ƿ����sprite��Ӱ
		bool IsRenderSpriteShadow() const;
		void SetRenderSpriteShadow(bool b);

		// �Ƿ���ư�͸������
		bool IsRenderAlphaSprite() const;
		void SetRenderAlphaSprite(bool b);

		// �ڻ��ˣ�˲���ƶ�����ʱ���Ƿ����Ӱ��Ĭ��true
		bool IsBlurForTeleport() const;	
		void SetBlurForTeleport(bool b);

		XPCOMPUTERCONFIGTYPE GetSystemLevel() const { return m_sysLevel; }
		void SetSystemLevel(XPCOMPUTERCONFIGTYPE l);

		// �Ƿ�ƽ���ƶ�
		bool IsSmoothMove() const;
		void SetSmoothMove(bool b);

		// ��С��Delta
		short GetMinDelta() const;
		void SetMinDelta(short d);

		// ������֮֡�������ֵ�ľ���ֵ
		short GetMaxDiffDelta() const;
		void SetMaxDiffDelta(short d);

		// ��ƽ��ֵ֮�������ֵ�ľ���ֵ
		short GetMaxDiffFromAvg() const;
		void SetMaxDiffFromAvg(short d);

		//��ж�ص�ͼ��ʱ������������ʱ�䣨���룩
		int GetUnloadMapBGSoundFadeOutTime() const { return m_nUnloadMapBGSoundFadeOutTime; }
		void SetUnloadMapBGSoundFadeOutTime(int t) { m_nUnloadMapBGSoundFadeOutTime = t<0?0:t; }

		// ����������������
		unsigned char GetBGMType() const {  return m_nBGMType; }
		void SetBGMType(unsigned char t) { m_nBGMType = t;  }

		// ����������������
		unsigned char GetEnvSoundType() const { return m_nEnvSoundType; }
		void SetEnvSoundType(unsigned char t) { m_nEnvSoundType = t; }

		// �Ų�������������
		unsigned char GetStepSoundType() const { return m_nStepSoundType; }
		void SetStepSoundType(unsigned char t) { m_nStepSoundType = t; }

		// �Ų��������ȼ�
		short GetStepSoundPriority() const { return m_nStepSoundPriority; }
		void SetStepSoundPriority(short p) { m_nStepSoundPriority = p; }

		/* ========================  /
		   ����������Ϣ��ؽӿ�
		/  ======================== */
	public:
		// ��Ļ������Ϣ����
		void SetConsoleInfo(XPCONSOLE_INFO eInfo, bool bOn);
		bool TestConsoleInfo(XPCONSOLE_INFO eInfo) const;

		// ֡״̬������Ϣ����
		void SetFrameStateInfo(XPFRAMESTAT_TYPE type, bool bOn);
		bool TestFrameStateInfo(XPFRAMESTAT_TYPE type) const;
		// ֡״̬������Ϣ��ɫ
		void SetFrameStateColor(XPFRAMESTAT_TYPE type, XPCOLOR color);
		XPCOLOR GetFrameStateColor(XPFRAMESTAT_TYPE type) const;

		// �Ƿ�̬���ص�ͼ
		bool IsDynamicMapLoading() const;
		void SetDynamicMapLoading(bool b);

		// �Ƿ����֡��
		// ��ʱ�ǵ������ã�����Ӧ�����Զ�����
		bool IsControlFPS() const;
		void SetControlFPS(bool b);

		void SetControlFPS(XPWINDOW_STATE type, int fps);//���Ƶ�FPS֡�ʣ�Ĭ����60 60 30
		int GetControlFPS(XPWINDOW_STATE type) const;

		// �Ƿ���������к;���
		bool IsSortMapObjects() const;
		void SetSortMapObjects(bool b);

		// �Ƿ�ʹ�õر�̬Cache
		// ��ʱ�ǵ������ã�����Ӧ�����Զ�����
		bool IsUseSurfaceCache() const;
		void SetUseSurfaceCache(bool b);

		// �Ƿ�ͬ������ˮ�����µ�ͼ
		bool IsSyncBeforeWater() const;
		void SetSyncBeforeWater(bool b);
		
		// ��̬�����lko���Ƿ�ᶯ
		void SetEnableLinkedObjs(bool b);
		bool IsEnableLinkedObjs() const;

		// �Ƿ���ʾ����·��
		bool IsShowSpritePath() const;
		void SetShowSpritePath(bool b);

		// �Ƿ���ʾ����β��
		bool IsShowSpriteTrail() const;
		void SetShowSpriteTrail(bool b);

		// �Ƿ���ʾ�Թ�����
		bool IsShowMazeGrid() const;
		void SetShowMazeGrid(bool b);

		// �Թ�����
		unsigned int GetMazeMask() const;
		void SetMazeMask(unsigned int mask);

	private:
		EngineBase *m_pEB;					
		SystemResourceManager *m_pSystemResourceMan;

	private:
		// auto setting
		XPPOOL_TYPE m_eCreateTexturePoolType;

		// user setting
		int m_iSpriteMoveSmoothLimit;
		float m_fLogicToWorldScaleX;
		bool m_bRenderNightEffectByShader;
		bool m_bRenderNightEffectWidthRenderTarget;
		bool m_bRenderSpriteShadow;
		bool m_bRenderAlphaSprite;

		// debug setting
		unsigned int m_maskConsoleInfo;
		unsigned int m_maskFramestat;
		XPCOLOR m_framestatColor[XPFRAMESTAT_TYPE_COUNT];
		bool m_bDynamicMapLoading;
		bool m_bControlFPS;
		bool m_bSortMapObjects;
		bool m_bUseSurfaceCache;
		int m_nControlFPS[3];

		XPCOMPUTERCONFIGTYPE m_sysLevel;	//�����������

		//������һЩ����
		int m_nUnloadMapBGSoundFadeOutTime;
		short m_nStepSoundPriority;
		unsigned char m_nStepSoundType;
		unsigned char m_nEnvSoundType;
		unsigned char m_nBGMType;

		//ƽ��֡������
		bool m_bSmoothMove;
		short m_nMinDelta;			// ��С��Delta
		short m_nMaxDiffDelta;		// ������֮֡�������ֵ�ľ���ֵ
		short m_nMaxDeffFromAvg;	// ��ƽ��ֵ֮�������ֵ�ľ���ֵ
		
		//ƽ��֡�����ý���

		bool m_bEnableLinkedObjs;	//��̬�����lko���Ƿ�����������
		bool m_bMaskPic;			//��ͼ
		bool m_bSyncBeforeWater;	//�Ƿ�ͬ������ˮ�����µ�ͼ

		
		bool m_bShowSpritePath;  // ��ʾ����·����
		bool m_bShowSpriteTrail; // ��ʾ����β����
		bool m_bShowMazeGrid;     // ��ʾ�Թ����ӡ�
		bool m_bBlurForTeleport;	//// �ڻ��ˣ�˲���ƶ�����ʱ���Ƿ����Ӱ
		unsigned int m_maskMaze; // ��ʾ�Թ����롣0����ʾ��
		

		
				

		// ռ��
	private:
		ConfigManager(const ConfigManager&);
		ConfigManager& operator=(const ConfigManager&);
	};

};

#endif
