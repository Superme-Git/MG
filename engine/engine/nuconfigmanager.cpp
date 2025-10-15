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

	// 初始化，之后才可用使用其他成员接口
	void ConfigManager::Init()
	{
		// 初始化配置
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

	// 更新动态自动配置
	void ConfigManager::Update()
	{
		// 创建纹理的池类型
		if( m_pSystemResourceMan != NULL )
		{
//			m_eCreateTexturePoolType = m_pSystemResourceMan->GetAvailTextureMemory() < 4 ? XPPOOL_MANAGED : XPPOOL_VIDEOMEM;
		}
	}

	// 定时更新自动配置
	void ConfigManager::OnTimer(int iPeriod/*ms*/)
	{
		Update();
	}

	// 创建纹理的池类型
	// @250ms更新
	NuclearPoolType ConfigManager::GetCreateTexturePoolType() const
	{
		return m_eCreateTexturePoolType;
	}

	// 每次平滑数量限制。为1的时候不平滑，相当于直接按apath走。
	int ConfigManager::GetSpriteMoveSmoothLimit() const
	{
		return m_iSpriteMoveSmoothLimit;
	}
	void ConfigManager::SetSpriteMoveSmoothLimit(int iLimit)
	{
		m_iSpriteMoveSmoothLimit = iLimit;
	}

	// 逻辑坐标到世界坐标变换(X方向)的转换因子
	float ConfigManager::GetLogicToWorldScaleX() const
	{
		return m_fLogicToWorldScaleX;
	}
	void ConfigManager::SetLogicToWorldScaleX(float fScale)
	{
		m_fLogicToWorldScaleX = fScale;
		NuclearWorldLogicCoord::SetN(fScale);
	}

	// 使用shader渲染夜晚效果
	bool ConfigManager::IsRenderNightEffectByShader() const
	{
		return m_bRenderNightEffectByShader;
	}
	void ConfigManager::SetRenderNightEffectByShader(bool b)
	{
		m_bRenderNightEffectByShader = b;
	}

	// 使用rendertarget渲染夜晚效果
	bool ConfigManager::IsRenderNightEffectWithRenderTarget() const
	{
		return m_bRenderNightEffectWidthRenderTarget;
	}
	void ConfigManager::SetRenderNightEffectWithRenderTarget(bool b)
	{
		m_bRenderNightEffectWidthRenderTarget = b;
	}

	//使用地图上的叠图，默认关
	bool ConfigManager::IsEnableMaskPic() const
	{
		return m_bMaskPic;
	}

	void ConfigManager::SetEnableMaskPic(bool b)
	{
		m_bMaskPic = b;
	}

	// 是否绘制sprite阴影
	bool ConfigManager::IsRenderSpriteShadow() const
	{
		return m_bRenderSpriteShadow;
	}

	void ConfigManager::SetRenderSpriteShadow(bool b)
	{
		m_bRenderSpriteShadow = b;
	}

	// 在击退（瞬间移动）的时候是否带残影，默认true
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

	// 最小的Delta
	short ConfigManager::GetMinDelta() const
	{
		return m_nMinDelta;
	}
	void ConfigManager::SetMinDelta(short d)
	{
		m_nMinDelta = d;
	}

	// 相邻两帧之间的最大差值的绝对值
	short ConfigManager::GetMaxDiffDelta() const
	{
		return m_nMaxDiffDelta;
	}
	void ConfigManager::SetMaxDiffDelta(short d)
	{
		m_nMaxDiffDelta = d;
	}

	// 和平均值之间的最大差值的绝对值
	short ConfigManager::GetMaxDiffFromAvg() const
	{
		return m_nMaxDeffFromAvg;
	}
	void ConfigManager::SetMaxDiffFromAvg(short d)
	{
		m_nMaxDeffFromAvg = d;
	}

	// 屏幕调试信息开关
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

	// 帧状态调试信息开关
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
	// 帧状态调试信息颜色
	void ConfigManager::SetFrameStateColor(NuclearFrameStatType type, NuclearColor color)
	{
		m_framestatColor[type] = color;
	}
	NuclearColor ConfigManager::GetFrameStateColor(NuclearFrameStatType type) const
	{
		return m_framestatColor[type];
	}

	// 是否动态加载地图
	bool ConfigManager::IsDynamicMapLoading() const
	{
		return m_bDynamicMapLoading;
	}
	void ConfigManager::SetDynamicMapLoading(bool b)
	{
		m_bDynamicMapLoading = b;
	}

	// 是否控制帧率
	// 暂时是调试配置，最终应该是自动配置
	bool ConfigManager::IsControlFPS() const
	{
		return m_bControlFPS;
	}
	void ConfigManager::SetControlFPS(bool b)
	{
		m_bControlFPS = b;
	}

	void ConfigManager::SetControlFPS(NuclearWindowState type, int fps)//控制的FPS帧率，默认是60 60 30
	{
		m_nControlFPS[type] = fps;
	}

	int ConfigManager::GetControlFPS(NuclearWindowState type) const
	{
		return m_nControlFPS[type];
	}

	// 是否排序物件和精灵
	bool ConfigManager::IsSortMapObjects() const
	{
		return m_bSortMapObjects;
	}
	void ConfigManager::SetSortMapObjects(bool b)
	{
		m_bSortMapObjects = b;
	}

	// 是否使用地表静态Cache
	// 暂时是调试配置，最终应该是自动配置
	bool ConfigManager::IsUseSurfaceCache() const
	{
        return false;
//		return m_bUseSurfaceCache;
	}
	void ConfigManager::SetUseSurfaceCache(bool b)
	{
		m_bUseSurfaceCache = b;
	}

	// 是否同步加载水层以下的图
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

	// 是否显示精灵路径
	bool ConfigManager::IsShowSpritePath() const
	{
		return m_bShowSpritePath;
	}
	void ConfigManager::SetShowSpritePath(bool b)
	{
		m_bShowSpritePath = b;
	}

	// 是否显示精灵尾迹
	bool ConfigManager::IsShowSpriteTrail() const
	{
		return m_bShowSpriteTrail;
	}
	void ConfigManager::SetShowSpriteTrail(bool b)
	{
		m_bShowSpriteTrail = b;
	}

	// 是否显示迷宫格子
	bool ConfigManager::IsShowMazeGrid() const
	{
		return m_bShowMazeGrid;
	}
	void ConfigManager::SetShowMazeGrid(bool b)
	{
		m_bShowMazeGrid = b;
	}

	// 迷宫掩码
	unsigned int ConfigManager::GetMazeMask() const
	{
		return m_maskMaze;
	}
	void ConfigManager::SetMazeMask(unsigned int mask)
	{
		m_maskMaze = mask;
	}
};