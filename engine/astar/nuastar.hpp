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

	// ��������������ꡣ
	struct Grid
	{
		int x;
		int y;

		Grid() { }
		Grid(int x, int y) throw()     { this->x = x; this->y = y; }
		Grid(float x, float y) throw() { this->x = (int)x; this->y = (int)y; }
		bool operator==(const Grid & o) const throw() { return this->x == o.x && this->y == o.y; }
	};

	typedef std::list<Grid> APath; //A*Ѱ·����·����·�����Ǹ��ӡ�

	///////////////////////////////////////////////////////////////////////
	// ��ͼ�ɴ����ݹ����Լ��ӿڡ������� PathFinder ���ݲ�����
	class AMap
	{
		int    _width;
		int    _height;

		// 1d array of maze data. index = x + y * _width
		// ����ͬʱ֧�����8�ֿɴ�ͼ��ʹ��һά����洢�����ڱ����װ��
		const unsigned char * _maze;
		unsigned int   _mask; // ��������ѡ��ɴ�ͼ��walkable: 0 == (_maze[index] & _mask)

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
			_maze   = maze; // ֱ��ָ���ڴ�ṹ���Թ���װ�ع�����Ҫע����㡣

			setRectAll();
		}

		///////////////////////////////////////////////////////////
		// �����������á�ÿ��Ѱ·ǰ��ʼ����
		// ���ÿɴ��߼��ж�����
		inline void setMask(unsigned int mask) { _mask = mask; }

		// ����������Χ����Χ֮�ⱻ���ɲ��ɴ
		// c ���ĵ����꣬w, h Ϊ��ȡ���λ�Ǹ��ӡ�
		inline void setRect(const Grid & c, const Grid & s) { this->setRect(c, s.x, s.y); }
		inline void setRect(const Grid & c, int w, int h)   { this->setRect(c.x - w/2, c.y - h/2, c.x + w/2, c.y + h/2); }

		// ����������Χ����Χ֮�ⱻ���ɲ��ɴ
		// (left, top)ΪRect���Ͻ����꣬(right, bottom)ΪRect���½����ꡣ��λ�Ǹ���
		inline void setRect(int left, int top, int right, int bottom)
		{
			_left   = left   > 0       ? left   : 0;
			_top    = top    > 0       ? top    : 0;
			_right  = right  < _width  ? right  : _width;
			_bottom = bottom < _height ? bottom : _height;
		}

		// ������ΧΪ������ͼ��
		inline void setRectAll()
		{
			_left = 0; _top = 0; _right = _width; _bottom = _height;
		}

		inline int left() const   { return  _left; }
		inline int top() const    { return  _top; }
		inline int right() const  { return _right; }
		inline int bottom() const { return  _bottom; }

		///////////////////////////////////////////////////////////
		// PathFinder ��Ҫ�Ľӿڡ�ʵ���Լ���AMap��ʱ�����ṩ��
		inline int  width() const  { return _width; }
		inline int  height() const { return _height; }

		inline bool walkable(int x, int y) const
		{
			// in rect. ������Χ����
			if (x < _left || x >= _right || y < _top || y >= _bottom)
				return false;
			return 0 == (_maze[x + y * _width] & _mask);
		}
	};

	enum eFindResult
	{
		eFindSuccess  = 0, // Ѱ··���ɹ���
		eFindFail     = 1, // Ѱ·ʧ�ܡ�
		eFindInrange  = 2, // Ѱ·�ɹ����Ѿ�����Ŀ�귶Χ�ڡ����ؿ�·����
		eFindInrange0 = 3, // Ѱ·�ɹ���Ŀ�����ǵ�ǰ�㡣���ؿ�·����eFindInrange��������

		// �������ֲ�ͬ��Ѱ·�������ʹ������������ô����
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
		��·������ͨ��û�����꣬��Ҿͻ�����ƶ���������û��Ҫȫ��ƽ�������߱�ƽ����
		������������ָ���ķ�Χƽ��·���㣬δƽ����ʣ��·���ᱻ����src�С�

		@����
		locationDirection ��ǰ����ͷ����������ꡣ
		src    �������·������ƽ���ĵ�ᱻɾ����ʣ��δ��ƽ���ĵ㡣[���벻Ϊ��]��
		limit  ƽ��·�����������ơ� 0 ��ʾ�����ƣ�ƽ��ȫ��·���㡣
		dst    ƽ�������Ϊ��������·���㡣��start���Լ�dst�еĵ���ֱ�����������������ƶ�·�����µ�ƽ����������ӵ����档

		������Σ�ƽ���������Ǿ�����ִ����ȥ��ֱ��limit��
		�ظ���·����ᱻ������������ɲ������κ�ƽ���㡣
		*/
		void smoothness(const Point & locationDirection, APath & src, int limit, Path & dst) const;

		// ��֤dst�ǿգ�����src���ˡ�
		void smoothness2(const Point & locationDirection, APath & src, int limit, Path & dst) const
		{
			// ���·�������ظ��ĵ㡣�������apath����·����
			// ��������limit���ƣ�smoothnessû����path������κν����
			while (!src.empty() && dst.empty())
				this->smoothness(locationDirection, src, limit, dst);
		}

		// ������������֤�ͻ��˷��͵�β���ǺϷ��ġ� location ��ǰ���� trail ������β����
		// -1 trail.size ���Ϸ� -2 trail�͵�ǰλ���޷�ƴ�� -3 trail �����˲��ɵ�������
		// 0 ��֤�ɹ���
		// �ɹ�ʱlength�����ܳ��ȡ�ʧ��ʱ����Ϊ���ֳ��ȡ����ǻᱻ�޸ġ�
		int scantrail(const Point & location, const astar::Path & trail, float & length /*out*/) const
		{
			return scantrailGrid(location, trail, length);
		}
		int scantrailGrid(const Point & location, const astar::Path & trail, float & length /*out*/) const;
		int scantrailPixel(const Point & location, const astar::Path & trail, float & length /*out*/) const;

		// �����Ƿ����ͨ��ֱ�ߵ���Ŀ�ꡣ�������жϡ�����Ϊ�ɴ�ͼ�������ꡣ
		// 0 ��ʼ��==Ŀ��㣻1 �ɴ-1 ���ɴ�
		eFindResult line(int x1, int y1, int x2, int y2) const;
		eFindResult line(const Grid & from, const Grid & to) const   { return this->line(from.x, from.y, to.x, to.y); }

		// �����Ƿ����ͨ��ֱ�ߵ���Ŀ�ꡣ�������жϡ�����Ϊ�����������ꡣ
		eFindResult linePixel(int x1, int y1, int x2, int y2) const;
		eFindResult linePixel(const Point & from, const Point & to) const
		{
			// ���ز������������������ܵ��¿������ӱ�Ե�ĵ����֤��ͨ��
			// �������ط�(line������Grid���㹹��)����ĳ���������,����Ӳ��Ե�ʧ���ʷ��������.	(��·)
			return this->linePixel((int)(from.x + 0.5f), (int)(from.y + 0.5f), (int)(to.x + 0.5f), (int)(to.y + 0.5f));
		}
		eFindResult linePixel(const Point & from, int toX, int toY) const
		{
			return this->linePixel((int)(from.x + 0.5f), (int)(from.y + 0.5f), toX, toY);
		}

		// ��������ת�����������ꡣ��������ȡ�������ĵ㡣
		// �ƶ�ʱ�������Ҫ����ת����Ҳʹ����������������֤����һ�¡�������ܷ�������Ԥ�Ͻ����
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
	 * �ۺ�Ѱ·sample��
	 *
	 * �ȳ���ֱ��Ѱ·��Ȼ���ʹ��astarѰ·��������ƽ������
	 *
	 * locationDirection  ��ǰλ�úͷ��򣬿�ʼ�㡣�������ꡣ���ص�λ��
	 * targetX, targetY   Ŀ��㡣�������ꡣ���ص�λ��
	 *
	 * apath Ѱ·�����ƽ��ʣ���·���㡣�ɴ�ͼ���꣬���ӵ�λ��
	 * path  Ѱ·�����ƽ�����·���㡣�������ꡣ���ص�λ��
	 *
	 * limit ���ƽ������. see Finder::smoothness��limit Ϊ1ʱ������ƽ����Ҳ����ֱ��Ѱ·��
	 * 
	 * range �޶�Ŀ�����꣬ʹ������������������
	 *       1) �ڲ��ҳ�����·���ϣ���Ŀ�굽ԭĿ��ľ���Լ����range
	 *       2) ��Ŀ��ֱ�߿ɴ�ԭĿ�ꡣ
	 *       3) ��Ŀ�굽ԭĿ��ķ����ϸ��Ӧ8������������������㡣��� outofrange��
	 *
	 * ����
	*/

	eFindResult find(const Point & locationDirection, int targetX, int targetY, int limit,
		APath & apath/* out */, Path & path/* out */, int range, APath * apathcopy = NULL)
	{
		// line find
		if (limit <= 0 || limit > 1) // ֻ�д�ƽ������ʱ���ų���ֱ��Ѱ·��
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
					path.push_back(target); // ��ʱ���ƶ������Ŀ��֮������߽Ƕ�������ġ�����ֻ��8����
					return astar::eFindSuccess;
				}
				return astar::eFindInrange; // in range. ����Ҫ�ƶ��ˡ�
				}

			default:
				break;
			}
		}

		// astar find
		Grid start  = this->Finder::world2grid(locationDirection);
		Grid target = this->Finder::world2grid(targetX, targetY);
		APath _apath; // ���ڷ��ز���·����˳��Ϊ��(��ʼ��, Ŀ���]��[ע��] ��������ʼ�㡣
		if (this->Finder::find(start.x, start.y, target.x, target.y, _apath))
		{
			if (NULL != apathcopy)
				*apathcopy = _apath; // for debug

			if (_apath.size() < 25) // ��������ܷ���. why is 100 ?
			{
				// ��·�������ʱ����ƽ����������ƽ�����·����
				Path _path;
				this->Finder::smoothness2(locationDirection, _apath, -1, _path);
				this->outofrange(locationDirection, _path, range);
				apath.swap(_apath); // _apathȫ����ƽ��������ʱ�ǿյ�
				path.swap(_path);   // ·������Ϊ�ա�
				return path.empty() ? astar::eFindInrange : astar::eFindSuccess;
			}
			else
			{
				// ��·���ϳ�ʱ��������astar·����Ȼ��ƽ������
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

	// �����from��to��ֱ���ϣ�����Ϊfrange�ĵ㡣
	// @return
	//    true  �˵����� from �� to ֮�䣬��ʱ��������ͨ������from���ء�
	//    false �˵����� from �� to ֮�⡣
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

	// ����ƽ�����·��
	// ·�������һ��ֱ���ϻ��� range ���롣�ѻ��˵ĵ㵱���µ�Ŀ��㡣
	void outofrange(const Point & location, Path & path /* in|out */, int range)
	{
		if (path.empty() || range <= 0)
			return; // ����Ҫ����Ŀ������

		astar::Point target = path.back(); path.pop_back();
		float frange = (float)range;
		if (false == path.empty()) // �Ƿ�ֻ��һ��·���㡣
		{
			if (outofrange(target, frange, path.back()))
				path.push_back(target);
		}
		else
		{
			// ·������ֻ��һ���㡣�ӵ�ǰλ�ÿ�ʼ������
			// ע��1 ֻ�йر�ƽ������ʱ�Żᷢ�����������
			//     2 ���� to û�г�ʼ�� Point.direction���жϾ��벻��Ҫ��
			if (outofrange(target, frange, astar::Point(location.x, location.y)))
				path.push_back(target);
		}
	}

	// ���� aster ·��
	// ��Ŀ�����ֱ�߻��� range ���롣����������ı䣬ֹͣ���ˣ���ʱ���˾���С�� range��
	// * ����������� range �޶�Ŀ����������� 3)��
	// * ����Ч�ʽϵ͡��ۺϿ���ƽ���㷨��ʱ�䣬����Ч�ʿ��ܽϸߣ�δ���ԣ���
	// * ��ǰֹͣ��Ӧ��Ч����������ƽ��·����
	void outofrange(const Point & location, APath & apath /* in|out */, int range)
	{
		if (range <= 0 || apath.empty())
			return; // ����Ҫ����Ŀ������

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

		// ·������ֻ��һ���㡣�ӵ�ǰλ�ÿ�ʼ������
		if (outofrange(target, frange, location))
		{
			return apath.push_back(this->world2grid(target));
		}
	}

	// ������ͨ�ƶ�ģʽ
	bool find(const Point & locationDirection, int targetX, int targetY, int limit,
		APath & apath/* out */, Path & path/* out */, APath * apathcopy = NULL)
	{
		return astar::eFindFail != find(locationDirection, targetX, targetY, limit, apath, path, 0, apathcopy);
	}
};

// ycl AFinder ֻ֧����� 320 x 240 �ĵ�ͼ�ߴ磬��������ͼ�����˸������޷�Ѱ·�����ͳһʹ�� BFinder
//#ifdef WIN32
typedef TFinder<BFinder<AMap, 16> > PathFinder;
//#else
//typedef TFinder<AFinder<AMap, 16> > PathFinder;
//#endif

} // namespace astar

#endif