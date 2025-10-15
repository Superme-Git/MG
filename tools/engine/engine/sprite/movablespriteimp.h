#ifndef __Nuclear_SPRITEIMP_H
#define __Nuclear_SPRITEIMP_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "astar/astar.hpp"
#include "astar/apath.hpp"
#include "..\common\xptypes.h"
#include "..\common\worldlogiccoord.h"

namespace Nuclear
{
	class Sprite;
	class EngineBase;

	// 这里得到的方向为世界坐标方向
	inline astar::Direction toAstarDirection(int direction)
	{
		switch (direction)
		{
		case XPDIR_TOP:         return astar::Direction(0, -1);
		case XPDIR_TOPRIGHT:    return astar::Direction(1, -1);
		case XPDIR_RIGHT:       return astar::Direction(1, 0);
		case XPDIR_BOTTOMRIGHT: return astar::Direction(1, 1);
		case XPDIR_BOTTOM:      return astar::Direction(0, 1);
		case XPDIR_BOTTOMLEFT:  return astar::Direction(-1, 1);
		case XPDIR_LEFT:        return astar::Direction(-1, 0);
		case XPDIR_TOPLEFT:     return astar::Direction(-1, -1);
		default:                return astar::Direction(0, 0); // no direction
		}
	}

	class MovableSpriteImp 
	{
		Sprite *m_pSprite;
		EngineBase *m_pEB;
	public:
		MovableSpriteImp(Sprite *pSprite, EngineBase *pEB);
		~MovableSpriteImp() { }

		///////////////////////////////////////////////////
		// 内部使用
		typedef astar::Direction Direction; // 先凑合使用这个类型。

		// 精灵支持4方向还是8方向，其中，4方向是左上 右上 右下 左下
		void SetOnly4Dir(bool b) { bOnly4Dir = b; }
		bool IsOnly4Dir() const { return bOnly4Dir; }

		// 世界坐标
		Location getLocation() const                { return Location(int(this->location.x), int(this->location.y)); }
		void setLocation(const Location & location) { this->setLocation((float)location.x, (float)location.y); }
		void setLocation(float x, float y);         // 设置精灵位置，会清除当前的路径和尾迹。

		// 逻辑坐标
		Location getLogicLocation() const				 { return WorldLogicCoord::world2logic( int(location.x), int(location.y)); }
		void setLogicLocation(const Location & location) { this->setLocation( WorldLogicCoord::logic2world( location)); }
		void setLogicLocation(float x, float y)			 { this->setLocation( WorldLogicCoord::logic2world(int(x), int(y))); }
		astar::Point getLocationDirection() const   { return WorldLogicCoord::world2logic( astar::Point(this->location.x, this->location.y, this->direction)); } // 结果为逻辑坐标

		// 世界坐标方向
		XPDIRECTION getDirection() const; // align 8 direction
		void setDirection(XPDIRECTION dir8)
		{
			// setDirection(const Direction &)为世界坐标系接口, 所以这里直接用toAstarDirection(int)回来的结果就好
			this->setDirection(toAstarDirection(dir8));
		}
		void setDirection(const Direction & direction)
		{
			if (false == this->direction.near_equals(direction))
			{
				this->__addtrail(this->getLocationDirection()); // get和add都是逻辑坐标系的, 无需转换
				this->direction = direction;
			}
		}
		void SetDirection(int tx, int ty)          
		{ this->setDirection(Direction(WorldLogicCoord::logic2world( this->getLocationDirection()), (float)tx, (float)ty)); }

		// 逻辑坐标系方向
		void setLogicDirection(int tx, int ty)
		{
			this->setDirection(WorldLogicCoord::logic2world( Direction(this->getLocationDirection(), (float)tx, (float)ty)));
		}
		void SetLocationDirection(const astar::Point & point)
		{
			// 设置位置方向会清除所有的路径和尾迹。所以直接修改方向，和 setDirection 的处理有所不同。
			astar::Point world = WorldLogicCoord::logic2world( point);
			this->direction = world.direction;
			this->setLocation(world.x, world.y);
		}

		void  SetMoveSpeed(float speed) { this->speed = speed; }
		float GetMoveSpeed() const      { return this->speed; }
		void  StopMove();
		bool  IsMoving() const          { return this->moving; }
		void SetMoveSuspended(bool b)   { this->movesuspended = b; }
		bool IsMoveSuspended() const    { return this->movesuspended; }

		void  MoveTo(astar::Path & trail); // 逻辑坐标
		void  MoveTo(int targetX, int targetY, int range, const CSIZE * size); // 世界坐标

		// 获取当前path的一个拷贝, 在使用MoveTo后调用可得到即将要走的路径(逻辑坐标)
		void GetPath(astar::Path & path);

		void KeepMoveTrail(bool keep);
		bool IsKeepMoveTrail() const        { return this->keepTrail; }
		void SetMoveMask(unsigned int mask) { this->moveMask = mask; }
		bool TakeMoveTrail(astar::Path & trail /*out*/); // 逻辑坐标

		bool InAttackRange( const Nuclear::Location & target, int range, unsigned int mask); // 世界坐标

		// 移动嘀嗒。参数为上一次嘀嗒依赖的时间差。by world
		void tick(int now, int deltaTime);
		void RenderPath();
	private:
		// 控制精灵在世界坐标中水平和垂直方向的移动速度, 
		// a*中使用逻辑坐标, 两个坐标系的换算方法为:
		// 世界坐标.x ==  逻辑坐标.x * N
		// 世界坐标.y ==  逻辑坐标.y
		// N = [1, 2]
		// TakeMoveTrail(astar::Path &)和MoveTo(astar::Path &)处
		// 用于收集尾迹发给服务器和接收服务器广播尾迹进行移动
		// 所以参数中trail均为逻辑坐标

		FPOINT      location;  // 位置，世界坐标，即像素
		Direction   direction; // 方向，世界坐标下的方向。
		float       speed;     // 速度，逻辑速度，适用于逻辑坐标。

		astar::APath apath; // A* 路径。可达图坐标。逻辑坐标格子。
		astar::Path  path;  // 平滑路径。逻辑坐标。
		astar::Path  trail; // 移动尾迹。逻辑坐标。

		bool     moving;        // 是否移动
		bool     keepTrail;
		bool     movesuspended; // 是否暂停
		bool	 bOnly4Dir;		//是否只有4个方向
		unsigned int moveMask;
		

		// world . 2 logic inner
		void __addtrail(const astar::Point & point) // 逻辑坐标
		{
			if (this->keepTrail && (this->trail.empty() || false == this->trail.back().trail_equals(point)))
				this->trail.push_back(point);
		}

		void __nextWaypoint(); // 逻辑坐标

		// 移动停止动作冷却控制。防止瞬间变换。在玩家持续移动时，观看方收到的尾迹由于网络延迟抖动造成缝隙。
		// 如果动作切换是立刻执行的，站立、走路动作发生切换抖动。设置cooldown用来缓和这种切换。
		// cooldown的配置上：由于时钟误差，太小的配置没有意义，但太大的配置也会造成问题。
		// 一般来说动画动作的图像帧为100ms。选择略小的值。超过这个值太多，会造成：原地跑动或站立漂移。
		//
		// 带来的其它问题：
		//     逻辑状态IsMoving和cooldown是分离的。当IsMoving已经是false时，内部可能还在cooldown。
		//     在这种情况下，如果更换了角色动作(发生了其他状态变化，比如攻击)，
		//     那么当cooldown达到时间，会回调 OnUpdateSpriteAction。这个迟到的回调实际上是多余的。
		//     问题不算严重，以后再考虑。
		enum {
			// 0 立即更换动作。
			eMovingCooldown =  0, // 切换到移动的冷却时间。ms
			eStandCooldown  = 80, // 切换到站立的冷却时间。ms
		};

		int updateActionCooldown; // ms
		void __tickActionCooldown(int now);
		void __setMoving(bool moving);

		#ifdef _DEBUG
		astar::APath apathcopy; // 保存最近查找的的 A* 路径。用来调试的。
		astar::APath * __apathcopy() { return &apathcopy; }
		#else
		astar::APath * __apathcopy() { return NULL; }
		#endif
		
	};

};

#endif
