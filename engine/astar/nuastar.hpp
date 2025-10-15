#ifndef __Nuclear_astar_H__
#define __Nuclear_astar_H__

#include <vector>
#include <list>
#include <algorithm>
#include <functional>
#include <assert.h>
#include <stdio.h>
#include "nuapath.hpp"

namespace astar
{

	// 用来保存格子坐标。
	struct Grid
	{
		int x;
		int y;

		Grid() { }
		Grid(int x, int y) throw()     { this->x = x; this->y = y; }
		Grid(float x, float y) throw() { this->x = (int)x; this->y = (int)y; }
		bool operator==(const Grid & o) const throw() { return this->x == o.x && this->y == o.y; }
	};

	typedef std::list<Grid> APath; //A*寻路返回路径。路径点是格子。

	///////////////////////////////////////////////////////////////////////
	// 地图可达数据管理以及接口。用来向 PathFinder 传递参数。
	class AMap
	{
		int    _width;
		int    _height;

		// 1d array of maze data. index = x + y * _width
		// 可以同时支持最多8种可达图。使用一维数组存储，便于保存和装载
		const unsigned char * _maze;
		unsigned int   _mask; // 根据掩码选择可达图。walkable: 0 == (_maze[index] & _mask)

		int   _left;
		int   _top;
		int   _right;
		int   _bottom;

	public:
		inline AMap(int width = 0, int height = 0, unsigned int mask = 0, const unsigned char * maze = NULL)
		{
			_width  = width > 0  ? width  : 0;
			_height = height > 0 ? height : 0;
			_mask   = mask;
			_maze   = maze; // 直接指向内存结构，迷宫的装载管理需要注意这点。

			setRectAll();
		}

		///////////////////////////////////////////////////////////
		// 搜索参数设置。每次寻路前初始化。
		// 设置可达逻辑判断掩码
		inline void setMask(unsigned int mask) { _mask = mask; }

		// 设置搜索范围，范围之外被看成不可达。
		// c 中心点坐标，w, h 为宽度。单位是格子。
		inline void setRect(const Grid & c, const Grid & s) { this->setRect(c, s.x, s.y); }
		inline void setRect(const Grid & c, int w, int h)   { this->setRect(c.x - w/2, c.y - h/2, c.x + w/2, c.y + h/2); }

		// 设置搜索范围，范围之外被看成不可达。
		// (left, top)为Rect左上角坐标，(right, bottom)为Rect右下角坐标。单位是格子
		inline void setRect(int left, int top, int right, int bottom)
		{
			_left   = left   > 0       ? left   : 0;
			_top    = top    > 0       ? top    : 0;
			_right  = right  < _width  ? right  : _width;
			_bottom = bottom < _height ? bottom : _height;
		}

		// 搜索范围为整个地图。
		inline void setRectAll()
		{
			_left = 0; _top = 0; _right = _width; _bottom = _height;
		}

		inline int left() const   { return  _left; }
		inline int top() const    { return  _top; }
		inline int right() const  { return _right; }
		inline int bottom() const { return  _bottom; }

		///////////////////////////////////////////////////////////
		// PathFinder 需要的接口。实现自己的AMap类时必须提供。
		inline int  width() const  { return _width; }
		inline int  height() const { return _height; }

		inline bool walkable(int x, int y) const
		{
			// in rect. 搜索范围限制
			if (x < _left || x >= _right || y < _top || y >= _bottom)
				return false;
			return 0 == (_maze[x + y * _width] & _mask);
		}
	};

	enum eFindResult
	{
		eFindSuccess  = 0, // 寻路路径成功。
		eFindFail     = 1, // 寻路失败。
		eFindInrange  = 2, // 寻路成功。已经到达目标范围内。返回空路经。
		eFindInrange0 = 3, // 寻路成功。目标点就是当前点。返回空路经。eFindInrange的特例。

		// 尽量区分不同的寻路结果。由使用者来决定怎么处理。
	};

	template <typename AMap, int GridSize>
	class Finder
	{
	protected:
		AMap amap;

	public:
		typedef AMap amap_t;
		typedef Finder<AMap, GridSize> mytype;

		enum { eGridSize = GridSize };

		void setAMap(const AMap & amap) { this->amap = amap; }
		AMap & getAMap()                { return amap; }

		/*
		长路经多数通常没有走完，玩家就会更改移动方向。所以没必要全部平滑，边走边平滑。
		这个函数会根据指定的范围平滑路径点，未平滑的剩余路径会被留在src中。

		@参数
		locationDirection 当前坐标和方向，世界坐标。
		src    输入格子路径。已平滑的点会被删除。剩下未被平滑的点。[必须不为空]。
		limit  平滑路径点数量限制。 0 表示不限制，平滑全部路径点。
		dst    平滑结果，为世界坐标路径点。把start点以及dst中的点用直线连接起来，就是移动路径。新的平滑点总是添加到后面。

		不管如何，平滑处理总是尽可能执行下去，直到limit。
		重复的路径点会被跳过，可能造成不产生任何平滑点。
		*/
		void smoothness(const Point & locationDirection, APath & src, int limit, Path & dst) const;

		// 保证dst非空，除非src空了。
		void smoothness2(const Point & locationDirection, APath & src, int limit, Path & dst) const
		{
			// 如果路径中有重复的点。可能造成apath还有路径，
			// 但是由于limit限制，smoothness没有在path中添加任何结果。
			while (!src.empty() && dst.empty())
				this->smoothness(locationDirection, src, limit, dst);
		}

		// 服务器用来验证客户端发送的尾迹是合法的。 location 当前坐标 trail 新来的尾迹。
		// -1 trail.size 不合法 -2 trail和当前位置无法拼接 -3 trail 经过了不可到达区域
		// 0 验证成功。
		// 成功时length返回总长度。失败时不变为部分长度。总是会被修改。
		int scantrail(const Point & location, const astar::Path & trail, float & length /*out*/) const
		{
			return scantrailGrid(location, trail, length);
		}
		int scantrailGrid(const Point & location, const astar::Path & trail, float & length /*out*/) const;
		int scantrailPixel(const Point & location, const astar::Path & trail, float & length /*out*/) const;

		// 测试是否可以通过直线到达目标。按格子判断。参数为可达图格子坐标。
		// 0 开始点==目标点；1 可达；-1 不可达
		eFindResult line(int x1, int y1, int x2, int y2) const;
		eFindResult line(const Grid & from, const Grid & to) const   { return this->line(from.x, from.y, to.x, to.y); }

		// 测试是否可以通过直线到达目标。按像素判断。参数为世界像素坐标。
		eFindResult linePixel(int x1, int y1, int x2, int y2) const;
		eFindResult linePixel(const Point & from, const Point & to) const
		{
			// 像素测试如果不四舍五入可能导致靠近格子边缘的点的验证不通过
			// 而其它地方(line函数和Grid浮点构造)如果改成四舍五入,则格子测试的失败率反而会提高.	(张路)
			return this->linePixel((int)(from.x + 0.5f), (int)(from.y + 0.5f), (int)(to.x + 0.5f), (int)(to.y + 0.5f));
		}
		eFindResult linePixel(const Point & from, int toX, int toY) const
		{
			return this->linePixel((int)(from.x + 0.5f), (int)(from.y + 0.5f), toX, toY);
		}

		// 格子坐标转换到世界坐标。世界坐标取格子中心点。
		// 移动时，如果需要坐标转换，也使用这两个函数，保证换算一致。否则可能发生不可预料结果。
		int   grid2world(int i) const              { return i * eGridSize + eGridSize / 2; }
		Point grid2world(int x, int y) const       { return Point((float)this->grid2world(x), (float)this->grid2world(y)); }
		Point grid2world(const Grid & grid) const  { return this->grid2world(grid.x, grid.y); }

		Grid  world2grid(int x, int y) const        { return Grid(x / eGridSize, y / eGridSize); }
		Grid  world2grid(float x, float y) const    { return Grid(x / eGridSize, y / eGridSize); }
		Grid  world2grid(const Point & world) const { return this->world2grid(world.x, world.y); }
	};

} // namespace astar

#include "nuastarImplement.hpp"

namespace astar
{

template <typename __Finder>
class TFinder : public __Finder
{
public:
	typedef __Finder Finder;
	/**
	 * 综合寻路sample。
	 *
	 * 先尝试直线寻路，然后才使用astar寻路。并进行平滑处理。
	 *
	 * locationDirection  当前位置和方向，开始点。世界坐标。像素单位。
	 * targetX, targetY   目标点。世界坐标。像素单位。
	 *
	 * apath 寻路结果，平滑剩余的路径点。可达图坐标，格子单位。
	 * path  寻路结果，平滑后的路径点。世界坐标。像素单位。
	 *
	 * limit 最大平滑数量. see Finder::smoothness。limit 为1时不进行平滑，也不按直线寻路。
	 * 
	 * range 修订目标坐标，使得它满足如下条件：
	 *       1) 在查找出来的路径上，新目标到原目标的距离约等于range
	 *       2) 新目标直线可达原目标。
	 *       3) 新目标到原目标的方向严格对应8方向。这个条件部分满足。详见 outofrange。
	 *
	 * 返回
	*/

	eFindResult find(const Point & locationDirection, int targetX, int targetY, int limit,
		APath & apath/* out */, Path & path/* out */, int range, APath * apathcopy = NULL)
	{
		// line find
		if (limit <= 0 || limit > 1) // 只有打开平滑处理时，才尝试直线寻路。
		{
			switch (this->Finder::linePixel(locationDirection, targetX, targetY))
			{
			case astar::eFindInrange0:
				apath.clear();
				path.clear();
				return astar::eFindInrange0;

			case astar::eFindSuccess:
				{
				apath.clear();
				path.clear();
				float frange = (float)range;
				Point target((float)targetX, (float)targetY, astar::Direction(locationDirection, (float)targetX, (float)targetY));
				if (this->outofrange(target, frange, locationDirection))
				{
					path.push_back(target); // 此时，移动对象和目标之间的连线角度是任意的。不是只有8方向。
					return astar::eFindSuccess;
				}
				return astar::eFindInrange; // in range. 不需要移动了。
				}

			default:
				break;
			}
		}

		// astar find
		Grid start  = this->Finder::world2grid(locationDirection);
		Grid target = this->Finder::world2grid(targetX, targetY);
		APath _apath; // 用于返回查找路径。顺序为：(起始点, 目标点]。[注意] 不包括起始点。
		if (this->Finder::find(start.x, start.y, target.x, target.y, _apath))
		{
			if (NULL != apathcopy)
				*apathcopy = _apath; // for debug

			if (_apath.size() < 25) // 避免最坏性能发生. why is 100 ?
			{
				// 当路径点较少时，先平滑处理，修正平滑后的路径。
				Path _path;
				this->Finder::smoothness2(locationDirection, _apath, -1, _path);
				this->outofrange(locationDirection, _path, range);
				apath.swap(_apath); // _apath全部被平滑处理，此时是空的
				path.swap(_path);   // 路径可能为空。
				return path.empty() ? astar::eFindInrange : astar::eFindSuccess;
			}
			else
			{
				// 当路径较长时，先修正astar路径，然后平滑处理。
				this->outofrange(locationDirection, _apath, range);
				if (_apath.empty())
				{
					path.clear();
					apath.clear();
					return astar::eFindInrange; // in range
				}
				Path _path;
				this->Finder::smoothness2(locationDirection, _apath, limit, _path);
				apath.swap(_apath);
				path.swap(_path);
				return astar::eFindSuccess;
			}
		}
		return astar::eFindFail;
	}

	// 计算从from往to的直线上，距离为frange的点。
	// @return
	//    true  此点落在 from 到 to 之间，此时它的坐标通过参数from返回。
	//    false 此点落在 from 到 to 之外。
	bool outofrange(Point & from /* in|out */, float & frange, const Point & to)
	{
		if (frange <= 0)
			return true;

		float dis = from.distance(to);
		frange -= dis;
		if (frange <= 0)
		{
			float R = ::fabsf(frange / dis);
			from.x = R * (from.x - to.x) + to.x;
			from.y = R * (from.y - to.y) + to.y;
			return true;
		}
		return false;
	}

	// 修正平滑后的路径
	// 路径的最后一段直线上回退 range 距离。把回退的点当作新的目标点。
	void outofrange(const Point & location, Path & path /* in|out */, int range)
	{
		if (path.empty() || range <= 0)
			return; // 不需要调整目标坐标

		astar::Point target = path.back(); path.pop_back();
		float frange = (float)range;
		if (false == path.empty()) // 是否只有一个路径点。
		{
			if (outofrange(target, frange, path.back()))
				path.push_back(target);
		}
		else
		{
			// 路径里面只有一个点。从当前位置开始修正。
			// 注：1 只有关闭平滑处理时才会发生这种情况。
			//     2 参数 to 没有初始化 Point.direction。判断距离不需要。
			if (outofrange(target, frange, astar::Point(location.x, location.y)))
				path.push_back(target);
		}
	}

	// 修正 aster 路径
	// 从目标点往直线回退 range 距离。如果方向发生改变，停止回退，此时回退距离小于 range。
	// * 这个方法满足 range 修订目标坐标的条件 3)。
	// * 修正效率较低。综合考虑平滑算法的时间，总体效率可能较高（未测试）。
	// * 提前停止点应用效果不如修正平滑路径。
	void outofrange(const Point & location, APath & apath /* in|out */, int range)
	{
		if (range <= 0 || apath.empty())
			return; // 不需要调整目标坐标

		float frange = (float)range;
		Point target = this->grid2world(apath.back()); apath.pop_back();
		Direction dir;
		bool initDir = false;
		while (false == apath.empty())
		{
			astar::Point prev = this->grid2world(apath.back());
			astar::Direction dir2(target, prev);
			if (false == initDir)
			{
				dir = dir2;
				initDir = true;
			}
			else if (false == dir.near_equals(dir2))
			{
				return apath.push_back(this->world2grid(target));
			}

			if (this->outofrange(target, frange, prev))
			{
				return apath.push_back(this->world2grid(target));
			}
			target = prev; apath.pop_back();
		}

		// 路径里面只有一个点。从当前位置开始修正。
		if (outofrange(target, frange, location))
		{
			return apath.push_back(this->world2grid(target));
		}
	}

	// 兼容普通移动模式
	bool find(const Point & locationDirection, int targetX, int targetY, int limit,
		APath & apath/* out */, Path & path/* out */, APath * apathcopy = NULL)
	{
		return astar::eFindFail != find(locationDirection, targetX, targetY, limit, apath, path, 0, apathcopy);
	}
};

// ycl AFinder 只支持最大 320 x 240 的地图尺寸，紫罗兰地图超出了该限制无法寻路，因此统一使用 BFinder
//#ifdef WIN32
typedef TFinder<BFinder<AMap, 16> > PathFinder;
//#else
//typedef TFinder<AFinder<AMap, 16> > PathFinder;
//#endif

} // namespace astar

#endif