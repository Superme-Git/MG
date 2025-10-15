#ifndef __Nuclear_apath_H__
#define __Nuclear_apath_H__

#include <list>
#include <math.h>
#include <float.h>

#ifndef D3DVECTOR_DEFINED
typedef struct _D3DVECTOR {
    float x;
    float y;
    float z;
} D3DVECTOR;
#define D3DVECTOR_DEFINED
#endif

namespace astar
{

	// 向量方向。不是单位向量。比较操作时需要注意。
	// 尾迹验证算法采用严格相等的比较方式，比如 (0, 1) (0, 2)的方向是一样的，但尾迹验证会认为不一样。
	struct Point;

	struct Direction : public D3DVECTOR
	{
		inline Direction() throw ()                        { /* 不初始化 */ }
		inline Direction(float x, float y, float z = 0.0f) throw () { this->x = x; this->y = y; this->z = z; }

		inline Direction(const Point & from, const Point & to) throw();
		inline Direction(const Point & from, float toX, float toY, float toZ = 0.f) throw();

		// 通过内积比较方向是否一样。浮点精度。
		inline bool near_equals(const Direction & o) const throw ()
		{
			return (1.0f - FLT_EPSILON) < (x * o.x + y * o.y + z * o.z) / M() / o.M();
		}

		inline bool strict_equals(const Direction & o) const throw ()
		{
			return this->x == o.x && this->y == o.y && this->z == o.z;
		}

		inline float M() const throw()
		{
			return ::sqrt(x * x + y * y + z * z);
		}
	};

	struct Point : public D3DVECTOR
	{
		Direction direction;

		Point() throw () { /* 不初始化 */ }
		Point(float x, float y, float z = 0.f) throw () { this->x = x; this->y = y; this->z = z; }
		Point(float x, float y, const Direction & d) throw () : direction(d) { this->x = x; this->y = y; this->z = 0.f; }
		Point(float x, float y, float z, const Direction & d) throw () :direction(d) { this->x = x; this->y = y; this->z = z; }

		inline float distance(const Point & to) const throw()
		{
			return ::sqrt( (this->x - to.x)*(this->x - to.x) + (this->y - to.y)*(this->y - to.y) + (this->z - to.z)*(this->z - to.z) );
		}

		// 严格比较
		inline bool strict_equals(const Point & p2) const throw()
		{
			return this->x == p2.x && this->y == p2.y && this->z == p2.z && this->direction.strict_equals(p2.direction);
		}

		// 尾迹比较方法：用于尾迹的生成和验证,暂使用严格比较。忽略方向也不会有大问题。
		inline bool trail_equals(const Point & p2) const throw()
		{
			return this->strict_equals(p2);
		}

		// 寻路比较方法：只比较 x,y。不考虑方向。当前寻路只能在2D下工作。
		inline bool finder_equals(const Point & p2) const throw()
		{
			return this->x == p2.x && this->y == p2.y;
		}
	};

	typedef std::list<Point> Path;

	inline Direction::Direction(const Point & from, float toX, float toY, float toZ) throw()
	{
		this->x = toX - from.x;
		this->y = toY - from.y;
		this->z = toZ - from.z;
	}

	inline Direction::Direction(const Point & from, const Point & to) throw()
	{
		this->x = to.x - from.x;
		this->y = to.y - from.y;
		this->z = to.z - from.z;
	}

} // namespace astar

#endif
