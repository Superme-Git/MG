#ifndef __Nuclear_WORLD_LOGIC_CORRD_H
#define __Nuclear_WORLD_LOGIC_CORRD_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "xptypes.h"
#include "astar/apath.hpp"

namespace Nuclear
{
	class ConfigManager;
	class WorldLogicCoord
	{
		friend class ConfigManager;
	private:
		static float N;		//转换比例

	private:
		WorldLogicCoord(void) {}
	public:
		~WorldLogicCoord(void) {}

	private:
		static void SetN(float n) { N = n; }
		// N = [1, 2]
		// 世界坐标.x ==  逻辑坐标.x * N
		// 世界坐标.y ==  逻辑坐标.y
	public:
		static astar::Direction	world2logic( const astar::Direction & direction) { return astar::Direction( direction.x / N, direction.y, direction.z); }
		static FPOINT			world2logic( const FPOINT & point) { return FPOINT( point.x / N, point.y); }
		static astar::Point		world2logic( const astar::Point & point) { return astar::Point( point.x / N, point.y, point.z, world2logic( point.direction)); }
		static astar::Point		world2logic( float x, float y) { return astar::Point( x / N, y); }
		static Location			world2logic( int x, int y) { return Location( (int)( x / N), y); }

		static astar::Direction	logic2world( const astar::Direction & direction) { return astar::Direction( direction.x * N, direction.y, direction.z); }
		static FPOINT			logic2world( const FPOINT & point) { return FPOINT( point.x * N, point.y); }
		static astar::Point		logic2world( const astar::Point & point) { return astar::Point( point.x * N, point.y, point.z, logic2world( point.direction)); }
		static astar::Point		logic2world( float x, float y) { return astar::Point( x * N, y); }
		static Location			logic2world( int x, int y) { return Location( (int)( x * N), y); }
	};

}

#endif //__Nuclear_WORLD_LOGIC_CORRD_H
