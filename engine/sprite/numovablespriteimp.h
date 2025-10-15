#ifndef __Nuclear_SPRITEIMP_H__
#define __Nuclear_SPRITEIMP_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../astar/nuastar.hpp"
#include "../astar/nuapath.hpp"
#include "../common/nuxptypes.h"
#include "../common/nuworldlogiccoord.h"

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
		typedef astar::Direction Direction; 

		// 精灵支持8、4、2、1四种方向
		void SetTurnDir(int iDir) { iTurnDir = iDir; }
		int  GetTurnDir() const { return iTurnDir; }

		// 世界坐标
		NuclearLocation getLocation() const                { return NuclearLocation(int(this->location.x), int(this->location.y)); }
		void setLocation(const NuclearLocation & location) { this->setLocation((float)location.x, (float)location.y); }
		void setLocation(float x, float y);         // 设置精灵位置，会清除当前的路径和尾迹。
		NuclearFPoint getVectorDirection() const				{ return NuclearFPoint(direction.x, direction.y); }

		// 逻辑坐标
		NuclearLocation getLogicLocation() const				 { return NuclearWorldLogicCoord::world2logic( int(location.x), int(location.y)); }
		void setLogicLocation(const NuclearLocation & location) { this->setLocation( NuclearWorldLogicCoord::logic2world( location)); }
		void setLogicLocation(float x, float y)			 { this->setLocation( NuclearWorldLogicCoord::logic2world(int(x), int(y))); }
		astar::Point getLocationDirection() const   { return NuclearWorldLogicCoord::world2logic( astar::Point(this->location.x, this->location.y, this->direction)); } // 结果为逻辑坐标

		// 世界坐标方向
		NuclearDirection getDirection() const;
		void setDirection(NuclearDirection dir8){this->setDirection(toAstarDirection(dir8));}
		void setDirection(const Direction & direction)
		{
			if (false == this->direction.near_equals(direction))
			{
				this->__addtrail(this->getLocationDirection()); // get和add都是逻辑坐标系的, 无需转换
				this->direction = direction;
			}
		}
		void SetDirection(int tx, int ty){ this->setDirection(Direction(NuclearWorldLogicCoord::logic2world( this->getLocationDirection()), (float)tx, (float)ty)); }

		// 逻辑坐标系方向
		void setLogicDirection(int tx, int ty){this->setDirection(NuclearWorldLogicCoord::logic2world( Direction(this->getLocationDirection(), (float)tx, (float)ty)));}
		void SetLocationDirection(const astar::Point & point)
		{
			// 设置位置方向会清除所有的路径和尾迹。所以直接修改方向，和 setDirection 的处理有所不同。
			astar::Point world = NuclearWorldLogicCoord::logic2world( point);
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
		void  MoveTo(int targetX, int targetY, int range, const CSIZE * size, bool straight = false); // 世界坐标

		void SetCurveMove(bool b)   { this->needCurveMove = b; }
		bool IsCurveMove() const	{ return this->needCurveMove; }

		// 获取当前path的一个拷贝, 在使用MoveTo后调用可得到即将要走的路径(逻辑坐标)
		void GetPath(astar::Path & path);

		void KeepMoveTrail(bool keep);
		bool IsKeepMoveTrail() const        { return this->keepTrail; }
		void SetMoveMask(unsigned int mask) { this->moveMask = mask; }
		bool TakeMoveTrail(astar::Path & trail /*out*/); // 逻辑坐标

		bool InAttackRange( const Nuclear::NuclearLocation & target, int range, unsigned int mask); // 世界坐标

		// 移动嘀嗒。参数为上一次嘀嗒依赖的时间差。by world
		void tick(int now, int deltaTime);
		void RenderPath();

	private:

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
		enum
		{
			// 0 立即更换动作。
			eMovingCooldown = 0, // 切换到移动的冷却时间。ms
			eStandCooldown = 80, // 切换到站立的冷却时间。ms
		};

		int updateActionCooldown; // ms
		void __tickActionCooldown(int now);
		void __setMoving(bool moving);

	private:
		// 控制精灵在世界坐标中水平和垂直方向的移动速度, 
		// a*中使用逻辑坐标, 两个坐标系的换算方法为:
		// 世界坐标.x ==  逻辑坐标.x * N
		// 世界坐标.y ==  逻辑坐标.y
		// N = [1, 2]
		// TakeMoveTrail(astar::Path &)和MoveTo(astar::Path &)处
		// 用于收集尾迹发给服务器和接收服务器广播尾迹进行移动
		// 所以参数中trail均为逻辑坐标

		NuclearFPoint      location;  // 位置，世界坐标，即像素
		Direction   direction; // 方向，世界坐标下的方向。
		float       speed;     // 速度，逻辑速度，适用于逻辑坐标。

		astar::APath apath; // A* 路径。可达图坐标。逻辑坐标格子。
		astar::Path  path;  // 平滑路径。逻辑坐标。
		astar::Path  trail; // 移动尾迹。逻辑坐标。

		bool    moving;        // 是否移动
		bool    keepTrail;
		bool    movesuspended; // 是否暂停
		int		iTurnDir;	   // 朝向(8方向，4方向，2方向，1方向)
		unsigned int moveMask;
		
		NuclearFPoint	startPos;//开始移动时刻的坐标
		bool	needCurveMove;	//是否需要进行弧线移动
		bool	straightMove;	//是否直线移动，不走自动寻路

		int     accumulatetime;//累计的时间
	};

}

#endif
