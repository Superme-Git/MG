#include "../common/nuworldlogiccoord.h"
#include "../renderer/nurenderer.h"
//#include "../particlesystem/iparticlemanager.h"
#include "nusystemresourcemanager.h"
#include "nuenginebase.h"
#include "nuconfigmanager.h"


namespace Nuclear
{
	float NuclearWorldLogicCoord::N = 1.5f;

	ConfigManager::ConfigManager(EngineBase *pEB)
		: m_pEB(pEB)
	{
//		m_pSystemResourceMan = m_pEB->GetSystemResourceManager();
	}

	ConfigManager::~ConfigManager()
	{
	}

	// ��ʼ����֮��ſ���ʹ��������Ա�ӿ�
	void ConfigManager::Init()
	{
		// ��ʼ������
		m_eCreateTexturePoolType = XPPOOL_MANAGED;

		m_iSpriteMoveSmoothLimit	= 0;
		m_fLogicToWorldScaleX		= 1.5f;
		m_bRenderNightEffectByShader = false;
		m_bRenderNightEffectWidthRenderTarget = false;

		m_maskConsoleInfo			= 0;
		m_maskFramestat				= 0;
		for (int i=0;i<XPFRAMESTAT_TYPE_COUNT;++i)
			m_framestatColor[i]		= 0;
		m_bDynamicMapLoading		= true;
		m_bControlFPS				= true;
		m_bSortMapObjects			= true;
		m_bUseSurfaceCache			= true;
		m_bShowSpritePath			= false;
		m_bShowSpriteTrail			= true;
		m_bShowMazeGrid				= false;
		m_bRenderSpriteShadow		= true;
		m_bIsRenderSolidMask		= true;
		m_bBlurForTeleport			= true;
		m_maskMaze					= 0;//(unsigned)(-1);
		m_bSmoothMove				= false;
		m_bEnableLinkedObjs			= true;
		m_bMaskPic					= false;
		m_bSyncBeforeWater			= false;
		m_nMinDelta					= 2;
		m_nMaxDiffDelta				= 10;
		m_nMaxDeffFromAvg			= 20;
		m_sysLevel					= XPCOMPUTERCONFIG_H;
		m_fMaskAlpha				= 0.6f;
		m_spriteTranslucentType		= XPSTT_ELEMENT_TRANSLUCENT;

		m_nUnloadMapBGSoundFadeOutTime = 0;
		m_nBGMType					= 0;
		m_nStepSoundType			= 1;
		m_nEnvSoundType				= 1;
		m_nStepSoundPriority		= 40;
		m_nControlFPS[0]			= 60;
		m_nControlFPS[1]			= 60;
		m_nControlFPS[2]			= 30;
		NuclearWorldLogicCoord::SetN(m_fLogicToWorldScaleX);
		//
		Update();
	}

	// ���¶�̬�Զ�����
	void ConfigManager::Update()
	{
		// ��������ĳ�����
		if( m_pSystemResourceMan != NULL )
		{
//			m_eCreateTexturePoolType = m_pSystemResourceMan->GetAvailTextureMemory() < 4 ? XPPOOL_MANAGED : XPPOOL_VIDEOMEM;
		}
	}

	// ��ʱ�����Զ�����
	void ConfigManager::OnTimer(int iPeriod/*ms*/)
	{
		Update();
	}

	// ��������ĳ�����
	// @250ms����
	NuclearPoolType ConfigManager::GetCreateTexturePoolType() const
	{
		return m_eCreateTexturePoolType;
	}

	// ÿ��ƽ���������ơ�Ϊ1��ʱ��ƽ�����൱��ֱ�Ӱ�apath�ߡ�
	int ConfigManager::GetSpriteMoveSmoothLimit() const
	{
		return m_iSpriteMoveSmoothLimit;
	}
	void ConfigManager::SetSpriteMoveSmoothLimit(int iLimit)
	{
		m_iSpriteMoveSmoothLimit = iLimit;
	}

	// �߼����굽��������任(X����)��ת������
	float ConfigManager::GetLogicToWorldScaleX() const
	{
		return m_fLogicToWorldScaleX;
	}
	void ConfigManager::SetLogicToWorldScaleX(float fScale)
	{
		m_fLogicToWorldScaleX = fScale;
		NuclearWorldLogicCoord::SetN(fScale);
	}

	// ʹ��shader��Ⱦҹ��Ч��
	bool ConfigManager::IsRenderNightEffectByShader() const
	{
		return m_bRenderNightEffectByShader;
	}
	void ConfigManager::SetRenderNightEffectByShader(bool b)
	{
		m_bRenderNightEffectByShader = b;
	}

	// ʹ��rendertarget��Ⱦҹ��Ч��
	bool ConfigManager::IsRenderNightEffectWithRenderTarget() const
	{
		return m_bRenderNightEffectWidthRenderTarget;
	}
	void ConfigManager::SetRenderNightEffectWithRenderTarget(bool b)
	{
		m_bRenderNightEffectWidthRenderTarget = b;
	}

	//ʹ�õ�ͼ�ϵĵ�ͼ��Ĭ�Ϲ�
	bool ConfigManager::IsEnableMaskPic() const
	{
		return m_bMaskPic;
	}

	void ConfigManager::SetEnableMaskPic(bool b)
	{
		m_bMaskPic = b;
	}

	// �Ƿ����sprite��Ӱ
	bool ConfigManager::IsRenderSpriteShadow() const
	{
		return m_bRenderSpriteShadow;
	}

	void ConfigManager::SetRenderSpriteShadow(bool b)
	{
		m_bRenderSpriteShadow = b;
	}

	// �ڻ��ˣ�˲���ƶ�����ʱ���Ƿ����Ӱ��Ĭ��true
	bool ConfigManager::IsBlurForTeleport() const
	{
		return m_bBlurForTeleport;
	}

	void ConfigManager::SetBlurForTeleport(bool b)
	{
		m_bBlurForTeleport = b;
	}

	void ConfigManager::SetSystemLevel(NuclearComputerConfigType l)
	{
		m_sysLevel = l;
//		m_pEB->GetRenderer()->GetParticleManager()->SetSystemConfig(l);
	}

	bool ConfigManager::IsSmoothMove() const
	{
		return m_bSmoothMove;
	}

	void ConfigManager::SetSmoothMove(bool b)
	{
		m_bSmoothMove = b;
	}

	// ��С��Delta
	short ConfigManager::GetMinDelta() const
	{
		return m_nMinDelta;
	}
	void ConfigManager::SetMinDelta(short d)
	{
		m_nMinDelta = d;
	}

	// ������֮֡�������ֵ�ľ���ֵ
	short ConfigManager::GetMaxDiffDelta() const
	{
		return m_nMaxDiffDelta;
	}
	void ConfigManager::SetMaxDiffDelta(short d)
	{
		m_nMaxDiffDelta = d;
	}

	// ��ƽ��ֵ֮�������ֵ�ľ���ֵ
	short ConfigManager::GetMaxDiffFromAvg() const
	{
		return m_nMaxDeffFromAvg;
	}
	void ConfigManager::SetMaxDiffFromAvg(short d)
	{
		m_nMaxDeffFromAvg = d;
	}

	// ��Ļ������Ϣ����
	void ConfigManager::SetConsoleInfo(NuclearConsoleInfo eInfo, bool bOn)
	{
		if( bOn )
			m_maskConsoleInfo |= eInfo;
		else
			m_maskConsoleInfo &= (~eInfo);
	}
	bool ConfigManager::TestConsoleInfo(NuclearConsoleInfo eInfo) const
	{
		return 0 != (eInfo & m_maskConsoleInfo);
	}

	// ֡״̬������Ϣ����
	void ConfigManager::SetFrameStateInfo(NuclearFrameStatType type, bool bOn)
	{
		if ( bOn )
			m_maskFramestat |= 1 << type;
		else
			m_maskFramestat &= ~(1 << type);
	}
	bool ConfigManager::TestFrameStateInfo(NuclearFrameStatType type) const
	{
		return 0 != ((1 << type) & m_maskFramestat);
	}
	// ֡״̬������Ϣ��ɫ
	void ConfigManager::SetFrameStateColor(NuclearFrameStatType type, NuclearColor color)
	{
		m_framestatColor[type] = color;
	}
	NuclearColor ConfigManager::GetFrameStateColor(NuclearFrameStatType type) const
	{
		return m_framestatColor[type];
	}

	// �Ƿ�̬���ص�ͼ
	bool ConfigManager::IsDynamicMapLoading() const
	{
		return m_bDynamicMapLoading;
	}
	void ConfigManager::SetDynamicMapLoading(bool b)
	{
		m_bDynamicMapLoading = b;
	}

	// �Ƿ����֡��
	// ��ʱ�ǵ������ã�����Ӧ�����Զ�����
	bool ConfigManager::IsControlFPS() const
	{
		return m_bControlFPS;
	}
	void ConfigManager::SetControlFPS(bool b)
	{
		m_bControlFPS = b;
	}

	void ConfigManager::SetControlFPS(NuclearWindowState type, int fps)//���Ƶ�FPS֡�ʣ�Ĭ����60 60 30
	{
		m_nControlFPS[type] = fps;
	}

	int ConfigManager::GetControlFPS(NuclearWindowState type) const
	{
		return m_nControlFPS[type];
	}

	// �Ƿ���������;���
	bool ConfigManager::IsSortMapObjects() const
	{
		return m_bSortMapObjects;
	}
	void ConfigManager::SetSortMapObjects(bool b)
	{
		m_bSortMapObjects = b;
	}

	// �Ƿ�ʹ�õر�̬Cache
	// ��ʱ�ǵ������ã�����Ӧ�����Զ�����
	bool ConfigManager::IsUseSurfaceCache() const
	{
        return false;
//		return m_bUseSurfaceCache;
	}
	void ConfigManager::SetUseSurfaceCache(bool b)
	{
		m_bUseSurfaceCache = b;
	}

	// �Ƿ�ͬ������ˮ�����µ�ͼ
	bool ConfigManager::IsSyncBeforeWater() const
	{
		return m_bSyncBeforeWater;
	}
	void ConfigManager::SetSyncBeforeWater(bool b)
	{
		m_bSyncBeforeWater = b;
	}


	void ConfigManager::SetEnableLinkedObjs(bool b)
	{
		m_bEnableLinkedObjs = b;
	}
	bool ConfigManager::IsEnableLinkedObjs() const
	{
		return m_bEnableLinkedObjs;
	}

	// �Ƿ���ʾ����·��
	bool ConfigManager::IsShowSpritePath() const
	{
		return m_bShowSpritePath;
	}
	void ConfigManager::SetShowSpritePath(bool b)
	{
		m_bShowSpritePath = b;
	}

	// �Ƿ���ʾ����β��
	bool ConfigManager::IsShowSpriteTrail() const
	{
		return m_bShowSpriteTrail;
	}
	void ConfigManager::SetShowSpriteTrail(bool b)
	{
		m_bShowSpriteTrail = b;
	}

	// �Ƿ���ʾ�Թ�����
	bool ConfigManager::IsShowMazeGrid() const
	{
		return m_bShowMazeGrid;
	}
	void ConfigManager::SetShowMazeGrid(bool b)
	{
		m_bShowMazeGrid = b;
	}

	// �Թ�����
	unsigned int ConfigManager::GetMazeMask() const
	{
		return m_maskMaze;
	}
	void ConfigManager::SetMazeMask(unsigned int mask)
	{
		m_maskMaze = mask;
	}
};