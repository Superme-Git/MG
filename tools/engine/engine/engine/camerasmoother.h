///=============================================================================
/// Product   : MHZX
/// Project   : Renderer
/// Module    : PathLubricator.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	相机(Camera)路径平滑器
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-6-6 Created.
///=============================================================================
#pragma once

namespace Nuclear
{
	// 平滑路径算法计算器
	// 算法来自于
	// <Game Programming Gems 4>
	// Edit by Andrew Kirmse 
	// chapter 1.10 - p95~p101
	class Smooth
	{
	public:
		Smooth();

		void SetSmoothTime( float fSeconds );

		float GetSmoothTime( ) const
		{
			return m_smoothTime;
		}

		void SetTarget( float from, float to, float velocity, bool bResetVolocity );
		float GetPostion( float elapsed );

	protected:
		float SmoothCD(float &vel, float elapse);

	protected:
		float m_from;
		float m_to;
		float m_velocity; // 暂时没有使用
		float m_lastVelocity;
		float m_smoothTime;
	};

	// 相机(Camera)路径平滑器
	class CCameraSmoother
	{
	public:
		CCameraSmoother();
		typedef float coord_type; // 坐标类型

	protected:
		coord_type m_focusX;		// 镜头尝试定位的焦点(不一定能够达到)
		coord_type m_focusY;		// 
		coord_type m_worldWidth;	// 世界坐标最大尺寸
		coord_type m_worldHeight;	//
		coord_type m_viewportWidth;	// 视口宽度
		coord_type m_viewportHeight;// 视口高度
		coord_type m_viewWidth;		// 视图宽度 - 从视口看到的世界宽度
		coord_type m_viewHeight;	// 视图高度 - 从视口看到的世界高度
		float m_fViewScale;			// 视图相对于视口的缩放比例 1 - 不缩放, 2 - 放大2倍, <=0 无效值
		float m_playerSpeed;		// 玩家速度

		coord_type m_vx;			// 镜头当前坐标
		coord_type m_vy;			//
		coord_type m_vxFrom;		// 镜头运动起点 
		coord_type m_vyFrom;		// 
		coord_type m_vxTarget;		// 镜头运动终点
		coord_type m_vyTarget;		//
		coord_type m_vdx;			// 镜头运动距离 target - from
		coord_type m_vdy;			// 

		float m_fElapsedX;			// 镜头平滑过程时间
		float m_fElapsedY;			//

		Smooth m_smoothX;			// 镜头平滑算法计算器
		Smooth m_smoothY;			//

		bool m_bArrival;			// 镜头是否达到目标

		// 立即定位相机焦点(不更改m_focusX/m_focusY)
		void _SetFocus(coord_type x, coord_type y);
		// 设置相机移动的目标焦点(不更改m_focusX/m_focusY)
		void _FocusTo(coord_type x, coord_type y);

	public:
		// 设置世界尺寸
		void SetWorldExt( coord_type ww, coord_type wh )
		{
			m_worldWidth = ww;
			m_worldHeight = wh;

			// 重置View尺寸
			SetViewScale( m_fViewScale );
		}

		// 设置视口尺寸
		void SetViewportExt( coord_type vw, coord_type vh )
		{
			m_viewportWidth = vw;
			m_viewportHeight = vh;

			// 重置View尺寸
			SetViewScale( m_fViewScale );
		}

		// 设置视图缩放比例
		void SetViewScale( float fScale );

		// 获取视图缩放比例
		float GetViewScale() const
		{
			return m_fViewScale;
		}

		// 设置玩家移动速度
		void SetPlayerSpeed( float playerSpeed )
		{
			m_playerSpeed = playerSpeed;
		}

		// 设置镜头平滑时间参数
		void SetSmoothTime( float fSeconds )
		{
			m_smoothX.SetSmoothTime( fSeconds );
			m_smoothY.SetSmoothTime( fSeconds );
		}

		// 获取镜头平滑时间参数
		float GetSmoothTime() const
		{
			return m_smoothX.GetSmoothTime();
		}

		// 立即定位相机焦点(更改m_focusX/m_focusY)
		void SetFocus(coord_type x, coord_type y);

		// 设置相机移动的目标焦点(更改m_focusX/m_focusY)
		void FocusTo(coord_type x, coord_type y);

		// 设置相机移动的目标焦点(通过给定的角色位置和角色的行进位置确定)
		void FocusTo(coord_type xRole, coord_type yRole, coord_type xRoleTo, coord_type yRoleTo);

		// if position changed, returns true, else returns false
		bool Update(float fCurrent);

		// 相机当前是否到达目标(即相机是否处于稳定状态)
		bool IsArrival() const
		{
			return m_bArrival;
		}

		// 相机移动目标焦点被重设(相机处于移动状态)
		void ClearArrival()
		{
			m_bArrival = false;
		}

		// 屏幕坐标点换算到世界坐标
		void ScreenToWorld( coord_type& x, coord_type &y ) const;
		
		// 世界坐标换算到屏幕坐标点
		void WorldToScreen( coord_type& x, coord_type &y ) const;

		// 相机当前的移动路径(世界坐标)
		void GetCurrentPath(
			coord_type& xfrom, coord_type& yfrom,
			coord_type& xto, coord_type& yto ) const
		{
			xfrom = m_vxFrom;
			yfrom = m_vyFrom;
			xto = m_vxTarget;
			yto = m_vyTarget;
		}

		// 相机当前的焦点位置(屏幕中央点的世界坐标)
		void GetCurrentFocus( coord_type& x, coord_type& y) const
		{
			x = m_vx + m_viewWidth / 2;
			y = m_vy + m_viewHeight / 2;
		}

		// 绘制相机的移动路径(调试使用)
		void DrawPath() const;
	};

} // namespace KNIGHT