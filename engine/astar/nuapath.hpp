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

	// �������򡣲��ǵ�λ�������Ƚϲ���ʱ��Ҫע�⡣
	// β����֤�㷨�����ϸ���ȵıȽϷ�ʽ������ (0, 1) (0, 2)�ķ�����һ���ģ���β����֤����Ϊ��һ����
	struct Point;

	struct Direction : public D3DVECTOR
	{
		inline Direction() throw ()                        { /* ����ʼ�� */ }
		inline Direction(float x, float y, float z = 0.0f) throw () { this->x = x; this->y = y; this->z = z; }

		inline Direction(const Point & from, const Point & to) throw();
		inline Direction(const Point & from, float toX, float toY, float toZ = 0.f) throw();

		// ͨ���ڻ��ȽϷ����Ƿ�һ�������㾫�ȡ�
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

		Point() throw () { /* ����ʼ�� */ }
		Point(float x, float y, float z = 0.f) throw () { this->x = x; this->y = y; this->z = z; }
		Point(float x, float y, const Direction & d) throw () : direction(d) { this->x = x; this->y = y; this->z = 0.f; }
		Point(float x, float y, float z, const Direction & d) throw () :direction(d) { this->x = x; this->y = y; this->z = z; }

		inline float distance(const Point & to) const throw()
		{
			return ::sqrt( (this->x - to.x)*(this->x - to.x) + (this->y - to.y)*(this->y - to.y) + (this->z - to.z)*(this->z - to.z) );
		}

		// �ϸ�Ƚ�
		inline bool strict_equals(const Point & p2) const throw()
		{
			return this->x == p2.x && this->y == p2.y && this->z == p2.z && this->direction.strict_equals(p2.direction);
		}

		// β���ȽϷ���������β�������ɺ���֤,��ʹ���ϸ�Ƚϡ����Է���Ҳ�����д����⡣
		inline bool trail_equals(const Point & p2) const throw()
		{
			return this->strict_equals(p2);
		}

		// Ѱ·�ȽϷ�����ֻ�Ƚ� x,y�������Ƿ��򡣵�ǰѰ·ֻ����2D�¹�����
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
