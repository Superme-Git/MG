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

	// 管理用户配置、智能配置

	class ConfigManager
	{
	public:
		ConfigManager(EngineBase *pEB);
		~ConfigManager();

	public:
		// 初始化，之后才可用使用其他成员接口
		void Init();

		// 更新动态自动配置
		void Update();

		// 定时更新自动配置
		void OnTimer(int iPeriod/*ms*/);

		/* ========================  /
		   自动配置信息相关接口
		/  ======================== */
	public:
		// 创建纹理的池类型
		// @缓存，OnTimer更新
		XPPOOL_TYPE GetCreateTexturePoolType() const;

		
		/* ========================  /
		   用户配置信息相关接口
		/  ======================== */
	public:
		// 精灵移动选项
		// 每次平滑数量限制。为1的时候不平滑，相当于直接按apath走。
		int GetSpriteMoveSmoothLimit() const; // see sprite.cpp::MoveTo
		void SetSpriteMoveSmoothLimit(int iLimit);

		// 逻辑坐标到世界坐标变换(X方向)的转换因子
		float GetLogicToWorldScaleX() const;
		void SetLogicToWorldScaleX(float fScale);

		// 使用shader渲染夜晚效果
		bool IsRenderNightEffectByShader() const;
		void SetRenderNightEffectByShader(bool b);

		// 使用rendertarget渲染夜晚效果
		bool IsRenderNightEffectWithRenderTarget() const;
		void SetRenderNightEffectWithRenderTarget(bool b);

		//使用地图上的叠图，默认关
		bool IsEnableMaskPic() const;
		void SetEnableMaskPic(bool b);

		// 是否绘制sprite阴影
		bool IsRenderSpriteShadow() const;
		void SetRenderSpriteShadow(bool b);

		// 是否绘制半透明人物
		bool IsRenderAlphaSprite() const;
		void SetRenderAlphaSprite(bool b);

		// 在击退（瞬间移动）的时候是否带残影，默认true
		bool IsBlurForTeleport() const;	
		void SetBlurForTeleport(bool b);

		XPCOMPUTERCONFIGTYPE GetSystemLevel() const { return m_sysLevel; }
		void SetSystemLevel(XPCOMPUTERCONFIGTYPE l);

		// 是否平滑移动
		bool IsSmoothMove() const;
		void SetSmoothMove(bool b);

		// 最小的Delta
		short GetMinDelta() const;
		void SetMinDelta(short d);

		// 相邻两帧之间的最大差值的绝对值
		short GetMaxDiffDelta() const;
		void SetMaxDiffDelta(short d);

		// 和平均值之间的最大差值的绝对值
		short GetMaxDiffFromAvg() const;
		void SetMaxDiffFromAvg(short d);

		//当卸载地图的时候，声音淡出的时间（毫秒）
		int GetUnloadMapBGSoundFadeOutTime() const { return m_nUnloadMapBGSoundFadeOutTime; }
		void SetUnloadMapBGSoundFadeOutTime(int t) { m_nUnloadMapBGSoundFadeOutTime = t<0?0:t; }

		// 背景音的音量类型
		unsigned char GetBGMType() const {  return m_nBGMType; }
		void SetBGMType(unsigned char t) { m_nBGMType = t;  }

		// 环境音的音量类型
		unsigned char GetEnvSoundType() const { return m_nEnvSoundType; }
		void SetEnvSoundType(unsigned char t) { m_nEnvSoundType = t; }

		// 脚步声的音量类型
		unsigned char GetStepSoundType() const { return m_nStepSoundType; }
		void SetStepSoundType(unsigned char t) { m_nStepSoundType = t; }

		// 脚步声的优先级
		short GetStepSoundPriority() const { return m_nStepSoundPriority; }
		void SetStepSoundPriority(short p) { m_nStepSoundPriority = p; }

		/* ========================  /
		   调试配置信息相关接口
		/  ======================== */
	public:
		// 屏幕调试信息开关
		void SetConsoleInfo(XPCONSOLE_INFO eInfo, bool bOn);
		bool TestConsoleInfo(XPCONSOLE_INFO eInfo) const;

		// 帧状态调试信息开关
		void SetFrameStateInfo(XPFRAMESTAT_TYPE type, bool bOn);
		bool TestFrameStateInfo(XPFRAMESTAT_TYPE type) const;
		// 帧状态调试信息颜色
		void SetFrameStateColor(XPFRAMESTAT_TYPE type, XPCOLOR color);
		XPCOLOR GetFrameStateColor(XPFRAMESTAT_TYPE type) const;

		// 是否动态加载地图
		bool IsDynamicMapLoading() const;
		void SetDynamicMapLoading(bool b);

		// 是否控制帧率
		// 暂时是调试配置，最终应该是自动配置
		bool IsControlFPS() const;
		void SetControlFPS(bool b);

		void SetControlFPS(XPWINDOW_STATE type, int fps);//控制的FPS帧率，默认是60 60 30
		int GetControlFPS(XPWINDOW_STATE type) const;

		// 是否排序物件中和精灵
		bool IsSortMapObjects() const;
		void SetSortMapObjects(bool b);

		// 是否使用地表静态Cache
		// 暂时是调试配置，最终应该是自动配置
		bool IsUseSurfaceCache() const;
		void SetUseSurfaceCache(bool b);

		// 是否同步加载水层以下的图
		bool IsSyncBeforeWater() const;
		void SetSyncBeforeWater(bool b);
		
		// 动态物件（lko）是否会动
		void SetEnableLinkedObjs(bool b);
		bool IsEnableLinkedObjs() const;

		// 是否显示精灵路径
		bool IsShowSpritePath() const;
		void SetShowSpritePath(bool b);

		// 是否显示精灵尾迹
		bool IsShowSpriteTrail() const;
		void SetShowSpriteTrail(bool b);

		// 是否显示迷宫格子
		bool IsShowMazeGrid() const;
		void SetShowMazeGrid(bool b);

		// 迷宫掩码
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

		XPCOMPUTERCONFIGTYPE m_sysLevel;	//计算机的配置

		//声音的一些设置
		int m_nUnloadMapBGSoundFadeOutTime;
		short m_nStepSoundPriority;
		unsigned char m_nStepSoundType;
		unsigned char m_nEnvSoundType;
		unsigned char m_nBGMType;

		//平滑帧的设置
		bool m_bSmoothMove;
		short m_nMinDelta;			// 最小的Delta
		short m_nMaxDiffDelta;		// 相邻两帧之间的最大差值的绝对值
		short m_nMaxDeffFromAvg;	// 和平均值之间的最大差值的绝对值
		
		//平滑帧的设置结束

		bool m_bEnableLinkedObjs;	//动态物件（lko）是否动起来。。。
		bool m_bMaskPic;			//叠图
		bool m_bSyncBeforeWater;	//是否同步加载水层以下的图

		
		bool m_bShowSpritePath;  // 显示精灵路径。
		bool m_bShowSpriteTrail; // 显示精灵尾迹。
		bool m_bShowMazeGrid;     // 显示迷宫格子。
		bool m_bBlurForTeleport;	//// 在击退（瞬间移动）的时候是否带残影
		unsigned int m_maskMaze; // 显示迷宫掩码。0不显示。
		

		
				

		// 占坑
	private:
		ConfigManager(const ConfigManager&);
		ConfigManager& operator=(const ConfigManager&);
	};

};

#endif
