///=============================================================================
/// Product   : MHZX
/// Project   : Renderer
/// Module    : CameraSmoother.cpp
///=============================================================================
/// Description :
/// 	Camera路径平滑器
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-6-6 Created.
///=============================================================================
#include "nucamerasmoother.h"
#include <math.h>

#include "../renderer/nurenderer.h"
#include "../nuiengine.h"
// 平滑时间参数
#define XPSMOOTH_TIME 50.0f
#define XPSMOOTH_DISTANCE 100.0f
namespace Nuclear
{
	template< typename coord_type >
	static void LimitCameraPos( coord_type& x, coord_type v, coord_type w )
	{
		if ( v >= w )
		{
			x = (w-v)/2;
		}
		else if ( x < 0 )
		{
			x = 0;
		}
		else if ( x > w - v)
		{
			x = w - v;
		}
		else
		{
			// x = x;
		}
	}

	// 判断角色坐标是否在陆地区
	// 陆地定义为: 角色活动时,镜头可以始终跟踪角色的区域
	template< typename coord_type >
	static bool IsRoleInLand( coord_type& x, coord_type v, coord_type w )
	{
		if( w >= v )
			return x >= v/2 && x <= w - v/2;
		else
			return false;
	}

	template< typename coord_type >
	static void LimitCameraFocus( coord_type& x, coord_type r, coord_type v, coord_type w )
	{
		if( !IsRoleInLand( r, v, w ) )
		{
			if( v <= w )
			{
				if( r <= v/2 )
				{
					x = v/2;
				}
				else
				{
					x = w-v/2;
				}
			}
			else
			{
				x = (w-v)/2;
			}
		}
	}

    template< typename T >
	inline int signof( T x )
	{
		return x >= 0 ? 1 : -1;
	}
    
	// dx - sign of from x to tx
	// when dx < 0, if arrival, x is equal or less than tx
	// when dx > 0, if arrival, x is equal or larger than tx
	template< typename coord_type >
	bool ArrivalTarget( coord_type x, coord_type dx, coord_type tx )
	{
		if( dx == 0 && x == tx )
		{
			return true;
		}
		else if ( signof(dx) > 0 )
		{
			if( x >= tx - 1 )
			{
				return true;
			}
		}
		else 
		{
			if ( x <= tx + 1 )
			{
				return true;
			}
		}

		return false;
	}

	template< typename T>
	inline int iround( T x )
	{
		return (int)(x == 0 ? 0 : (x > 0 ? x + 0.5 : x - 0.5));
	}

	// 平方距离
	template< typename coord_type >
	inline coord_type square_distance( coord_type dx, coord_type dy )
	{
		return dx*dx + dy*dy;
	}

	// 平方距离
	template< typename coord_type >
	inline coord_type square_distance( coord_type x1, coord_type y1, coord_type x2, coord_type y2 )
	{
		return square_distance( x1-x2, y1-y2 );
	}

	// 距离
	template< typename coord_type >
	inline coord_type distance(coord_type x1, coord_type y1, coord_type x2, coord_type y2  )
	{
		return sqrtf( square_distance(x1,y1, x2,y2));
	}

	template< typename coord_type >
	inline coord_type distance(coord_type x1, coord_type x2 )
	{
		return fabs( x1 - x2 );
	}

	// 根据2个直线方程,计算交点坐标
	template< typename coord_type >
	inline void calc_cross_point(
		coord_type k1, coord_type b1,
		coord_type k2, coord_type b2,
		coord_type& x, coord_type& y)
	{
		if( k1 != k2 )
		{
			x = (b2 - b1) / (k1 - k2);
			y = k1 * x + b1;
		}
	}

	template< typename coord_type >
	inline void calc_project_point(coord_type x1, coord_type y1, 
		coord_type x2, coord_type y2, 
		coord_type xx, coord_type yy, 
		coord_type& xd, coord_type& yd )
	{
		coord_type k = 0, kk = 0;
		if( x2 != x1 )
		{	
			if( y2 != y1 )
			{
				k = (y2-y1)/(x2-x1);
				kk = (x1-x2)/(y2-y1);
			}
			else
			{
				xd = xx;
				yd = y1;
				return ;
			}
		}
		else
		{
			xd = x1;
			yd = yy;
			return ;
		}


		coord_type b = y1- k*x1;
		coord_type bb = yy-kk*xx;

		calc_cross_point( k, b, kk, bb, xd, yd );
	}

	// 目标选择器
	class CTargetChooser
	{
	public:
		typedef float coord_type;
		static void ChooseTarget( 
			coord_type rx, coord_type ry, coord_type rtx, coord_type rty, 
			coord_type vx, coord_type vy, coord_type& vtx, coord_type& vty );
	};

	Smoother::Smoother()
	{
		m_from = 0.0f;
		m_to = 0.0f;
		m_velocity = 10.0f;
		m_lastVelocity = 0.0f;
		m_smoothTime = XPSMOOTH_TIME;
	}

	void Smoother::SetSmoothTime(float fSeconds)
	{
		assert(fSeconds > 0);
		m_smoothTime = fSeconds;
	}

	float Smoother::smoothCD(float &vel, float elapse)
	{
		float to = m_to;
		float omega = 2.f/m_smoothTime;
		float x = omega*elapse;
		float exp = 1.f/(1.f+x+0.48f*x*x+0.235f*x*x*x);
		float change = ( m_from - to );
		float temp = (vel+omega*change)*elapse;
		vel = (vel - omega*temp)*exp;  // Equation 5
		return to + (change+temp)*exp; // Equation 4
	}

	void Smoother::SetTarget(float from, float to, float velocity, bool bResetVolocity)
	{
		if( bResetVolocity )
			m_lastVelocity = 0;
		else
		{
			if( signof( to - from ) != signof( m_to - m_from ) )
			{
				m_lastVelocity = -m_lastVelocity;
			}
		}

		m_from = from;
		m_to = to;
	}

	float Smoother::GetPostion(float elapsed)
	{
		m_from = smoothCD (m_lastVelocity, elapsed );
		return m_from;  
	}

	CameraSmoother::CameraSmoother()
	{
		m_vx = 0;
		m_vy = 0;
		m_vdx = 0;
		m_vdy = 0;
		m_vxTarget = 0;
		m_vyTarget = 0;
		m_vxFrom = 0;
		m_vyFrom = 0;
		m_focusX = 0;
		m_focusY = 0;
		m_worldWidth = 1000;
		m_worldHeight = 1000;
		m_viewportWidth = 800;
		m_viewportHeight = 600;
		m_fViewScale = 1.0f;
		m_viewWidth = m_viewportWidth;
		m_viewHeight = m_viewportHeight;
		m_bArrival = true;

		m_fElapsedX = 0;
		m_fElapsedY = 0;
	}

	void CameraSmoother::SetViewScale( float fScale )
	{
		if( fScale > 0 )
		{
			m_fViewScale = fScale;

			// 缩放级别(Scale)变化前后，一个原则是屏幕中心保持不变
			// 而我们这里的m_vx,m_vy是屏幕的左上角，所以，这里需要重新计算m_vx,m_vy
			coord_type vx = m_vx + m_viewWidth / 2;
			coord_type vy = m_vy + m_viewHeight / 2;
			m_viewWidth = m_viewportWidth * fScale;
			m_viewHeight = m_viewportHeight * fScale;
			m_vx = vx - m_viewWidth / 2;
			m_vy = vy - m_viewHeight / 2;

			// 需要尽量保持Focus不变. 这里隐含2层处理:
			// 1. 如果原始的Focus与镜头中心点重合,则Scale变化前后,镜头中心点的世界坐标不变
			// 2. 如果原始的Focus与镜头中心点不重合,则Scale变化前后,镜头中心点将尝试向Focus靠近
			if( m_bArrival )
				setFocus(m_focusX, m_focusY);
			else
			{
				// 如果处于运动过程中, 通过FocusTo完成缓冲式调整
				focusTo(m_focusX, m_focusY);
			}
		}
	}

	void CameraSmoother::setFocus(coord_type x, coord_type y)
	{
		m_vxTarget = x - m_viewWidth / 2;
		m_vyTarget = y - m_viewHeight / 2;
		LimitCameraPos( m_vxTarget, m_viewWidth, m_worldWidth );
		LimitCameraPos( m_vyTarget, m_viewHeight, m_worldHeight );
		m_vxFrom = m_vxTarget;
		m_vyFrom = m_vyTarget;
		m_vx = m_vxFrom;
		m_vy = m_vyFrom;
		m_vdx = 0;
		m_vdy = 0;
		m_fElapsedX = 0;
		m_fElapsedY = 0;
		m_bArrival = true;
	}
	
	void CameraSmoother::SetFocus(coord_type x, coord_type y)
	{
		m_focusX = x;
		m_focusY = y;
		setFocus(x, y);
	}

	void CameraSmoother::focusTo(coord_type x, coord_type y)
	{
		// 相机系统
		m_vxTarget = x - m_viewWidth / 2;
		m_vyTarget = y - m_viewHeight / 2;

		LimitCameraPos( m_vxTarget, m_viewWidth, m_worldWidth );
		LimitCameraPos( m_vyTarget, m_viewHeight, m_worldHeight );

		m_vxFrom = m_vx;
		m_vyFrom = m_vy;
		m_vdx = m_vxTarget-m_vxFrom;
		m_vdy = m_vyTarget-m_vyFrom;
		float vfdx = 0; // 暂时没有使用
		float vfdy = 0; // 暂时没有使用

		m_smoothX.SetTarget( (float)m_vxFrom, (float)m_vxTarget, (float)(m_playerSpeed * vfdx), false );
		m_smoothY.SetTarget( (float)m_vyFrom, (float)m_vyTarget, (float)(m_playerSpeed * vfdy), false );

		ClearArrival();
	}

	void CameraSmoother::FocusTo(coord_type x, coord_type y)
	{
		m_focusX = x;
		m_focusY = y;

		focusTo(x, y);
	}

	void CameraSmoother::FocusTo(coord_type xRole, coord_type yRole, coord_type xRoleTo, coord_type yRoleTo)
	{
		coord_type xTo = m_vx+m_viewWidth/2, yTo = m_vy+m_viewHeight/2;
		CTargetChooser::ChooseTarget(xRole, yRole, xRoleTo, yRoleTo, xTo, yTo, xTo, yTo );
		
		// 记录RoleFocus
		m_focusX = xTo;
		m_focusY = yTo;

		// 调整CameraFocus
		LimitCameraFocus( xTo, xRole, m_viewWidth, m_worldWidth );
		LimitCameraFocus( yTo, yRole, m_viewHeight, m_worldHeight );

		focusTo( xTo, yTo );
	}

	bool CameraSmoother::Update(float fCurrent)
	{
		coord_type vxold = m_vx;
		coord_type vyold = m_vy;

		bool bArrivalX = ArrivalTarget( m_vx, m_vdx, m_vxTarget );
		if( !bArrivalX )
		{
			m_fElapsedX += fCurrent;
			m_vx = (coord_type)m_smoothX.GetPostion( m_fElapsedX );
			LimitCameraPos( m_vx, m_viewWidth, m_worldWidth );

			// Check Again
			bArrivalX = ArrivalTarget( m_vx, m_vdx, m_vxTarget );
		}

		bool bArrivalY = ArrivalTarget( m_vy, m_vdy, m_vyTarget );
		if( !bArrivalY )
		{
			m_fElapsedY += fCurrent;			
			m_vy = (coord_type)m_smoothY.GetPostion( m_fElapsedY );
			LimitCameraPos( m_vy, m_viewHeight, m_worldHeight );

			// Check Again
			bArrivalY = ArrivalTarget( m_vy, m_vdy, m_vyTarget );
		}

		m_bArrival = bArrivalX && bArrivalY;

		if( m_bArrival )
		{
			// 相机到达目标后,重置timer
			m_fElapsedX = 0;
			m_fElapsedY = 0;
		}

		// 返回是否有变化
		return m_vx != vxold || m_vy != vyold;
	}

	void CameraSmoother::ScreenToWorld( coord_type& x, coord_type &y ) const
	{
		x += m_vx;
		y += m_vy;
	}

	void CameraSmoother::WorldToScreen( coord_type& x, coord_type &y ) const
	{
		x -= m_vx;
		y -= m_vy;
	}

	void CTargetChooser::ChooseTarget(
		coord_type rx, coord_type ry, coord_type rtx, coord_type rty, 
		coord_type vx, coord_type vy, coord_type & vtx, coord_type & vty )
	{
		coord_type x = rx;
		coord_type y = ry;
		coord_type tx = rtx;
		coord_type ty = rty;

		coord_type dx = vx, dy = vy;
		calc_project_point(rx, ry, rtx, rty, vx, vy, dx, dy);

#if 0
		// 方案1: 跟踪角色当前坐标
		vtx = x;
		vty = y;
#else
		// 方案2:
		// 按确定性比例选择角色和角色运动目标之间的点作为镜头目标
		// 确定性d按如下方式计算:
		
		coord_type xl1 = distance( x, tx );
		coord_type xl2 = distance( dx, x );
		coord_type xl3 = distance( dx, tx );
		if ( xl3 > xl1 )
		{
			xl1 = xl3;
		}

		if ( xl3 > 0.0f )
		{
			float xd = xl2 / xl1;
			vtx = (coord_type)iround(x + xd * ( tx - x ));
		}

		coord_type yl1 = distance( y, ty );
		coord_type yl2 = distance( dy, y );
		coord_type yl3 = distance( dy, ty );
		if ( yl3 > yl1 )
		{
			yl1 = yl3;
		}
		if ( yl3 > 0.0f )
		{
			float yd = yl2 / yl1;
			vty = (coord_type)iround(y + yd * ( ty - y ));
		}		
			
#endif
	}

	void CameraSmoother::DrawPath( ) const
	{
		Nuclear::Renderer* pRender = Nuclear::GetEngine()->GetRenderer();
		if( pRender != NULL )
		{
			float cx = m_viewWidth / 2;
			float cy = m_viewHeight / 2;
		}
	}

} // namespace Nuclear