///=============================================================================
/// Product   : Nuclear
/// Project   : Voronoi
/// Module    : voronoi.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	����voronoi�����
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-6-1 Created.
///=============================================================================
#ifndef VORONOI_H_INCLUDED
#define VORONOI_H_INCLUDED

#if _MSC_VER > 1000
#pragma once
#endif

#include <math.h>
#include <vector>
#include <string>
#include <sstream>
#include <algorithm>
#include <ctime>
#include <assert.h>

// �������Եĺ�
//#define TEST_BISECOTER
//#define TEST_DIRECTION

typedef DWORD	Color;

class Graphics
{
public:
	virtual void setColor( Color color ) = 0;
	virtual void fillRect( int x, int y, int w, int h ) = 0;
	virtual void fillOval( int x, int y, int w, int h ) = 0;
	virtual void drawLine( int x1, int y1, int x2, int y2 ) = 0;
	virtual void drawDashLine( int x1, int y1, int x2, int y2 ) = 0;
	virtual void drawString( const std::wstring& text, int x, int y ) = 0;
};

namespace voronoi
{

template<typename T>
struct PointT
{
	typedef T	coord_type;
	typedef int int_type;	// TODO: ����ʹ����ȡtraits����

	coord_type x, y;
	PointT() : x(0), y(0) { }

	PointT( coord_type x, coord_type y )
	{
		this->x = x;
		this->y = y;
	}

	//friend PointT operator - (const PointT& rLeft, const PointT& rRight);

	bool operator == (const PointT& rOther) const
	{
		if ((int_type)x == (int_type) rOther.x  && 
			(int_type)y == (int_type)rOther.y )
		{
			return true;
		}
		return false;
	}

	bool operator != (const PointT& rOther) const
	{
		return !operator==(rOther);
	}
};

typedef PointT<int>		PointI;
typedef PointT<float>	PointF;
typedef PointT<double>	PointD;
typedef PointD			PointR;
typedef PointI			Point;

template<typename T>
PointT<T> minus( const PointT<T>& rLeft, const PointT<T>& rRight )
{
	PointT<T> result; 
	result.x = rLeft.x - rRight.x;
	result.y = rLeft.y - rRight.y;
	return result;
}

inline PointI operator - (const PointI& rLeft, const PointI& rRight)
{
	PointI result = minus(rLeft, rRight); 
	return result;
}

inline PointD operator - (const PointD& rLeft, const PointD& rRight)
{
	PointD result = minus(rLeft, rRight); 
	return result;
}

inline PointF operator - (const PointF& rLeft, const PointF& rRight)
{
	PointF result = minus(rLeft, rRight); 
	return result;
}

// �����
struct Edge
{
	Point from, to;

	Point center()
	{
		Point p;
		p.x = (Point::coord_type)(( from.x + to.x ) / 2.0);
		p.y = (Point::coord_type)(( from.y + to.y ) / 2.0);
		return p;
	}
};

//
// ����չ�������ޱ�ʶ,������������ı��:
//
//              |
//              |
//              6
//        5     |     7
//              |
// -----4-------0-------0-------> x
//              |
//        3     |     1
//              2
//              |
//              |
//              V y
//              
inline int quadrant_num( const Point& point )
{
	if ( point.x > 0 )
	{
		if( point.y == 0 )
		{
			return 0;
		}
		else if( point.y > 0 )
		{
			return 1;
		}
		else
		{
			return 7;
		}
	}
	else if ( point.x == 0 )
	{
		if( point.y == 0 )
		{
			return 0;
		}
		else if( point.y > 0 )
		{
			return 2;
		}
		else
		{
			return 6;
		}
	}
	else // x < 0
	{
		if( point.y > 0 )
		{
			return 3;
		}
		else if( point.y == 0 )
		{
			return 4;
		}
		else
		{
			return 5;
		}
	}
}

//
// �Ƚϱ�׼�����ĽǶ�, A < B ����true
//
// 1. ���޲�ͬ,�������޺��жϴ�С
// 2. ������ͬ,��������tangentֵ�жϴ�С(tangent������ͬһ�����е�������)
//
inline bool vector_angle_compare( const Point& A, const Point& B )
{
	int qA = quadrant_num(A);
	int qB = quadrant_num(B);
	if ( qA != qB ) return qA < qB;
	else return A.y * B.x < A.x * B.y;
}

// ��Ƕ - ��Ƕ����,��Ƕ��,��Ƕ����
struct Tessellation
{
	Point					center;	  // ��Ƕ����
	std::vector< Edge >		polyedge; // ��Ƕ��(��ģ��)
	std::vector< Point >	polygon;  // ��Ƕ����(����ģ��)
	union 
	{
		int flags;					  // ��Ƕ����λ
		struct 
		{
			bool bMouthSite	: 1; // ����� - ���ٲ�����1���߶�,ռ����0�����򶨵�
			bool bEarSite	: 1; // ������ - ���ٲ�����2���߶�,ռ����1�����򶨵�
			bool bHeadSite	: 1; // ͷβ�� - ���ٲ�����3���߶�,ռ����2�����򶨵�
			bool bBodySite	: 1; // ������ - ���ٲ�����4���߶�,ռ����3�����򶨵�
		};
	};

	// �պ϶����(��ģ��)
	void close_polyedge( int width, int height )
	{
		flags = 0;
		size_t N = polyedge.size();
		if ( N == 0 )
		{
			// ������������ı߿�(4����)
			Edge rightBorder, upBorder, leftBorder, bottomBorder;
			rightBorder.from = Point( width, height );
			rightBorder.to = Point( width, 0);
			upBorder.from = rightBorder.to;
			upBorder.to    = Point( 0, 0 );
			leftBorder.from = upBorder.to;
			leftBorder.to = Point( 0, height );
			bottomBorder.from = leftBorder.to;
			bottomBorder.to = rightBorder.from;
			polyedge.push_back( upBorder );
			polyedge.push_back( leftBorder );
			polyedge.push_back( bottomBorder );
			polyedge.push_back( rightBorder );
			return ;
		}

		std::vector< Edge  > polyedge_r;
		std::vector< Point > polyedge_r_center;
		polyedge_r.resize(N);	
		polyedge_r_center.resize(N);
		for ( size_t i = 0; i < N; i ++ )
		{
			regular_edge(polyedge[i], polyedge_r[i]);
			polyedge_r_center[i] = polyedge_r[i].center();
		}

		// ִ�м򵥵�ѡ������(�ȽϽǶ�,ʹ����Щ�߰���ʱ������)
		for ( size_t i = 0; i < N-1; i ++ )
		{
			size_t l = i;
			for ( size_t j = i + 1; j < N; j ++ )
			{
				if( vector_angle_compare(polyedge_r_center[l], polyedge_r_center[j]) )
				{
					l = j;
				}
			}
			if ( l != i )
			{
				std::swap( polyedge_r_center[i],polyedge_r_center[l] );
				std::swap( polyedge[i],polyedge[l] );
				std::swap( polyedge_r[i],polyedge_r[l] );
			}
		}

		// ����δ�νӵı�
		for ( size_t i = 0; i < N; i ++ )
		{
			size_t n = (i+1)%N;
			const Point& from = polyedge[i].to;
			const Point& to   = polyedge[n].from;

			if( from == to )
			{
				// connected already!
				continue;
			}

			if ( from.x > 0 && from.x < width  && to.x > 0 && to.x < width )
			{
				if( from.y == height && to.y == 0 )
				{
					//           to
					// +---------*<------+
					// |          \      ^
					// |           \     |
					// |            \    |
					// |             \   | 
					// +--------------*->+
					//                 from

					Edge simpleEdgeV, simpleEdgeH1, simpleEdgeH2; // ��V,H1,H2
					simpleEdgeH1.from = from;
					simpleEdgeH1.to.x = width;
					simpleEdgeH1.to.y = from.y;
					simpleEdgeV.from = simpleEdgeH1.to;
					simpleEdgeV.to.x = width;
					simpleEdgeV.to.y = to.y;
					simpleEdgeH2.from = simpleEdgeV.to;
					simpleEdgeH2.to = to;

					polyedge.insert( polyedge.begin() + i + 1, simpleEdgeH1 );
					polyedge.insert( polyedge.begin() + i + 2, simpleEdgeV );
					polyedge.insert( polyedge.begin() + i + 3, simpleEdgeH2 );

					i += 3;
					N += 3;
					bHeadSite = true;
					continue;
				}
				else if( from.y == 0 && to.y == height  )
				{
					//      from
					// +<----*-----------+
					// |      \          |
					// |       \         |
					// |        \        |
					// v         \       | 
					// +--------->*------+
					//            to

					Edge simpleEdgeV, simpleEdgeH1, simpleEdgeH2; // ��V,H1,H2

					simpleEdgeH1.from = from;
					simpleEdgeH1.to.x = 0;
					simpleEdgeH1.to.y = from.y;
					simpleEdgeV.from = simpleEdgeH1.to;
					simpleEdgeV.to.x = 0;
					simpleEdgeV.to.y = to.y;
					simpleEdgeH2.from = simpleEdgeV.to;
					simpleEdgeH2.to = to;

					polyedge.insert( polyedge.begin() + i + 1, simpleEdgeH1 );
					polyedge.insert( polyedge.begin() + i + 2, simpleEdgeV );
					polyedge.insert( polyedge.begin() + i + 3, simpleEdgeH2 );

					i += 3;
					N += 3;
					bHeadSite = true;
					continue;
				}
			}
			else if( from.y > 0 && from.y < height && to.y > 0 && to.y < height )
			{
				if( from.x == 0 && to.x == width )
				{
					//           
					//       +-----------------+
					//       |                 |
					//       |                 |
					//  from *<----------------* to
					//   V1  |                 ^ V2
					//       v                 | 
					//       +---------------->+
					//                H

					Edge simpleEdgeV1, simpleEdgeH, simpleEdgeV2; // ��V1,H,V2
					simpleEdgeV1.from = from;
					simpleEdgeV1.to.x = from.x;
					simpleEdgeV1.to.y = height;
					simpleEdgeH.from = simpleEdgeV1.to;
					simpleEdgeH.to.x = to.x;
					simpleEdgeH.to.y = height;
					simpleEdgeV2.from = simpleEdgeH.to;
					simpleEdgeV2.to = to;

					polyedge.insert( polyedge.begin() + i + 1, simpleEdgeV1 );
					polyedge.insert( polyedge.begin() + i + 2, simpleEdgeH );
					polyedge.insert( polyedge.begin() + i + 3, simpleEdgeV2 );

					i += 3;
					N += 3;
					bHeadSite = true;
					continue;
				}
				else if ( from.x == width && to.x == 0 )
				{
					//            H
					//       +<----------------+
					//    V2 |                 ^ V1
					//       v                 | 
					//    to *---------------->* from
					//       |                 |
					//       |                 |
					//       +-----------------+

					Edge simpleEdgeV1, simpleEdgeH, simpleEdgeV2; // ��V1,H,V2
					simpleEdgeV1.from = from;
					simpleEdgeV1.to.x = from.x;
					simpleEdgeV1.to.y = 0;
					simpleEdgeH.from = simpleEdgeV1.to;
					simpleEdgeH.to.x = to.x;
					simpleEdgeH.to.y = simpleEdgeV1.to.y;
					simpleEdgeV2.from = simpleEdgeH.to;
					simpleEdgeV2.to = to;
					polyedge.insert( polyedge.begin() + i + 1, simpleEdgeV1 );
					polyedge.insert( polyedge.begin() + i + 2, simpleEdgeH );
					polyedge.insert( polyedge.begin() + i + 3, simpleEdgeV2 );

					i += 3;
					N += 3;
					bHeadSite = true;
					continue;
				}
			}

			if( from.x == to.x || from.y == to.y )
			{
				if( from.x == to.x )
				{
					assert(
						from.y >= 0 && from.y <= height && to.y >= 0 && to.y <= height && 
						( from.x == 0 || from.x == width )
						);
				}
				else if( from.y == to.y )
				{
					assert(
						from.x >= 0 && from.x <= width && to.x >= 0 && to.x <= width &&
						( from.y == 0 || from.y == height ) 
						);
				}

				// ��Ҫ����1����
				Edge simpleEdge; // �򵥱�
				simpleEdge.from = from;
				simpleEdge.to = to;
				polyedge.insert( polyedge.begin() + i + 1, simpleEdge);
				bMouthSite = true;
				i ++;
				N ++;
			}
			else
			{
				if( from.y == height && to.x == 0 )
				{
					// left - bottom ��Ҫ����4����
					
					Edge simpleEdgeH1, simpleEdgeV1, simpleEdgeH2, simpleEdgeV2; // ��H1,V1,H2,V2

					simpleEdgeH1.from = from;
					simpleEdgeH1.to = Point( width, height );
					simpleEdgeV1.from = simpleEdgeH1.to;
					simpleEdgeV1.to = Point( width, 0 );
					simpleEdgeH2.from = simpleEdgeV1.to;
					simpleEdgeH2.to = Point(0,0);
					simpleEdgeV2.from = simpleEdgeH2.to;
					simpleEdgeV2.to = to;
					polyedge.insert( polyedge.begin() + i + 1, simpleEdgeH1);
					polyedge.insert( polyedge.begin() + i + 2, simpleEdgeV1);
					polyedge.insert( polyedge.begin() + i + 3, simpleEdgeH2);
					polyedge.insert( polyedge.begin() + i + 4, simpleEdgeV2);

					i += 4;
					N += 4;

					bBodySite = true;

				}
				else if ( from.y == 0 && to.x == width )
				{
					// right - upper ��Ҫ����4����

					Edge simpleEdgeH1, simpleEdgeV1, simpleEdgeH2, simpleEdgeV2; // ��H1,V1,H2,V2
					simpleEdgeH1.from = from;
					simpleEdgeH1.to = Point(0,0);
					simpleEdgeV1.from = simpleEdgeH1.to;
					simpleEdgeV1.to = Point( 0, height );
					simpleEdgeH2.from = simpleEdgeV1.to;
					simpleEdgeH2.to = Point( width, height );
					simpleEdgeV2.from = simpleEdgeH2.to;
					simpleEdgeV2.to = to;

					polyedge.insert( polyedge.begin() + i + 1, simpleEdgeH1);
					polyedge.insert( polyedge.begin() + i + 2, simpleEdgeV1);
					polyedge.insert( polyedge.begin() + i + 3, simpleEdgeH2);
					polyedge.insert( polyedge.begin() + i + 4, simpleEdgeV2);

					i += 4;
					N += 4;

					bBodySite = true;
				}
				else if ( from.x == 0 && to.y == 0 )
				{
					// left- upper ��Ҫ����4����

					Edge simpleEdgeH1, simpleEdgeV1, simpleEdgeH2, simpleEdgeV2; // ��H1,V1,H2,V2

					simpleEdgeV1.from = from;
					simpleEdgeV1.to = Point(0,height);
					simpleEdgeH1.from = simpleEdgeV1.to;
					simpleEdgeH1.to = Point( width, height );
					simpleEdgeV2.from = simpleEdgeH1.to;
					simpleEdgeV2.to = Point( width, 0);
					simpleEdgeH2.from = simpleEdgeV2.to;
					simpleEdgeH2.to = to;
					polyedge.insert( polyedge.begin() + i + 1, simpleEdgeV1);
					polyedge.insert( polyedge.begin() + i + 2, simpleEdgeH1);
					polyedge.insert( polyedge.begin() + i + 3, simpleEdgeV2);
					polyedge.insert( polyedge.begin() + i + 4, simpleEdgeH2);

					i += 4;
					N += 4;

					bBodySite = true;
				}
				else if( from.x == width && to.y == height )
				{
					// right - bottom ��Ҫ����4����

					Edge simpleEdgeH1, simpleEdgeV1, simpleEdgeH2, simpleEdgeV2; // ��H1,V1,H2,V2
					simpleEdgeV1.from = from;
					simpleEdgeV1.to = Point(width,0);
					simpleEdgeH1.from = simpleEdgeV1.to;
					simpleEdgeH1.to = Point( 0, 0);
					simpleEdgeV2.from = simpleEdgeH1.to;
					simpleEdgeV2.to = Point( 0, height);
					simpleEdgeH2.from = simpleEdgeV2.to;
					simpleEdgeH2.to = to;
					polyedge.insert( polyedge.begin() + i + 1, simpleEdgeV1);
					polyedge.insert( polyedge.begin() + i + 2, simpleEdgeH1);
					polyedge.insert( polyedge.begin() + i + 3, simpleEdgeV2);
					polyedge.insert( polyedge.begin() + i + 4, simpleEdgeH2);

					i += 4;
					N += 4;

					bBodySite = true;
				}
				else
				{
					// ��Ҫ����������
					if( from.x > to.x && from.y > to.y || from.x < to.x && from.y < to.y )
					{
						// right-upper / left - bottom corner
						Edge simpleEdgeV, simpleEdgeH; // ��V,H
						simpleEdgeV.from = from;
						simpleEdgeH.to = to;
						simpleEdgeH.from.x = simpleEdgeV.to.x = simpleEdgeV.from.x; 
						simpleEdgeH.from.y = simpleEdgeV.to.y = simpleEdgeH.to.y;
						polyedge.insert( polyedge.begin() + i + 1, simpleEdgeV);
						polyedge.insert( polyedge.begin() + i + 2, simpleEdgeH);
					}
					else
					{
						// left - upper / right-bottom  corner 
						Edge simpleEdgeH, simpleEdgeV; // ��H,V
						simpleEdgeH.from = from;
						simpleEdgeV.to = to;
						simpleEdgeH.to.x = simpleEdgeV.from.x = simpleEdgeV.to.x; 
						simpleEdgeH.to.y = simpleEdgeV.from.y = simpleEdgeH.from.y;
						polyedge.insert( polyedge.begin() + i + 1, simpleEdgeH);
						polyedge.insert( polyedge.begin() + i + 2, simpleEdgeV);
					}
					bEarSite = true;
					i += 2;
					N += 2;
				}
			}
		}
	}

	// �Ӷ���εı�ģ�͹������εĶ���ģ��
	inline void make_polygon( )
	{
		polygon.clear();
		size_t N = polyedge.size();
		polygon.reserve( N );
		for ( size_t i = 0; i < N; i ++ )
		{
			polygon.push_back( polyedge[i].from );
		}
	}

	// ���򻯵�
	inline void regular_point( const Point& pt,  Point& pt_r )
	{
		pt_r = pt - center;
	}

	// ���򻯱� - ���edge�Ĺ����(��centerΪ�������ı�)
	inline void regular_edge(const Edge& edge, Edge& edge_r)
	{
		regular_point( edge.from, edge_r.from );
		regular_point( edge.to, edge_r.to );
	}

	// ��һ����
	static void unitary_point(int width, int height, const Point& pt,  PointR& pt_u )
	{
		pt_u.x = (PointR::coord_type)pt.x / width;
		pt_u.y = (PointR::coord_type)pt.y / height;		
	}

	// ��һ�������(����ģ��)
	// [in] width - ��һ������ԭʼ���
	// [in] height - ��һ������ԭʼ�߶�
	// [out] center_u - ��һ��������ĵ�����
	// [out] polygon_ru - ��һ����Ķ�����������
	void unitary_polygon( int width, int height, PointR& center_u,  std::vector<PointR>& polygon_u)
	{
		Point pt_r;
		size_t N = polygon.size();
		polygon_u.resize(N);

		// ��ÿ����������ٹ�һ��
		for ( size_t i = 0; i < N; i ++ )
		{
			regular_point(polygon[i], pt_r);
			unitary_point(width, height, pt_r, polygon_u[i]);
		}

		// ��һ����Ƕ����
		unitary_point( width, height, center, center_u);
	}
};

typedef std::vector<double>			VectorD;
typedef std::vector<Point>			VectorPoint;
typedef std::vector<Tessellation>	VectorTessellation;

inline double pow2( double value )
{
	return value * value;
}

inline int uround( double value )
{
	return (int)(value+0.5f);
}

inline int uround( int value )
{
	return value;
}

// ������
static void heapv(VectorD& xArray,
				  VectorD& yArray,
				  int size)
{
	// ���󶥶�
	for (int i = size / 2; i > 0; --i)
	{
		double d1 = xArray[i - 1];
		double d2 = yArray[i - 1]; 

		int j = i;
		while (2 * j <= size)
		{
			int k = 2 * j;

			if ((k + 1 <= size) && (xArray[k - 1] < xArray[k]))
			{
				++k;
			}

			if (xArray[k - 1] <= d1)
				break;

			xArray[j - 1] = xArray[k - 1];
			yArray[j - 1] = yArray[k - 1];

			j = k;
		}

		xArray[j - 1] = d1;
		yArray[j - 1] = d2;

	}

	// ������(�����С����)
	for (int i = size - 1; i > 0 ; --i)
	{
		double d1 = xArray[i];
		double d2 = yArray[i]; 

		xArray[i] = xArray[0];
		yArray[i] = yArray[0]; 

		int j = 1;

		while (2 * j <= i)
		{
			int k = 2 * j;
			if ((k + 1 <= i) && (xArray[k - 1] <= xArray[k]))
			{
				++k;
			}

			if (xArray[k - 1] <= d1)
				break;

			xArray[j - 1] = xArray[k - 1];
			yArray[j - 1] = yArray[k - 1];

			j = k;
		}

		xArray[j - 1] = d1;
		yArray[j - 1] = d2;
	}
}

// ���㼸��(compute_geometry)���㸨������
namespace cg
{
	// �����߶�2���˵�����,�����д���ֱ�߷��̵Ĳ���(б�ʺͽؾ�)
	// di - �д���б��
	// ys - �д�����y�ύ��(�ؾ�)
	inline void perpendicular_bisector_params(
		double x1, double y1, double x2, double y2,
		double& di, double& ys )
	{
		if( y1 != y2 )
		{
			di = (x1-x2)/(y2-y1);
			// cpx - �д��ߴ���x����
			// cpy - �д��ߴ���y����
			double cpx = (x1+x2) / 2.0;
			double cpy = (y1+y2) / 2.0;
			ys = cpy - (cpx * di);
		}
		else
		{
			// di == ?
			ys = y1;			
		}
	}

	// ƽ������
	inline double square_distance( double dx, double dy )
	{
		return dx*dx + dy*dy;
	}

	// ƽ������
	inline double square_distance( double x1, double y1, double x2, double y2 )
	{
		return square_distance( x1-x2, y1-y2 );
	}

	// �жϵ�(x,y)�Ƿ��� target ���������voronoi��Ƕͼ��
	// ������neighbor���������Ƕͼ - 
	// ��Ϊ��(x,y)��target��neighbor���д�����,û�б�Ҫ������
	inline bool in_target_site(const VectorD& x1, const VectorD& y1, int N, 
		double x, double y, int target, int neighbor )
	{
		double ds = square_distance(x, y, x1[target], y1[target]);
		double us;
		for (int u = 0; u < N; u ++ )
		{
			if ((u != target) && (u != neighbor)) 
			{
				us = square_distance(x, y, x1[u], y1[u] );
				if ( us < ds )
				{
					return false;
				}
			}
		}

		return true;
	}

	// �жϵ�(x,y) ���ڵ�voronoi��Ƕͼ, ��������
	// sites_center - voronoi��Ƕͼ����Ƕ����
	inline int site_hit_test( const VectorPoint& sites_center, int size, double x, double y )
	{
		int N = size ;
		if ( N == 0 )
		{
			return -1;
		}

		// ���Ҿ�������ĵ�ı��
		int hit = 0;
		double md = square_distance( x, y, sites_center[0].x, sites_center[0].y );
		for ( int i = 1; i < N; i ++ )
		{
			double us = square_distance( x, y, sites_center[i].x, sites_center[i].y );
			if ( us < md )
			{
				hit = i;
				md = us;
			}
		}
		
		return hit;
	}

	// ����ֱ���ھ��������б����ó������߶�
	// di - ֱ��б��
	// ys - ֱ�߽ؾ�
	// width - ������
	// height - ����߶�
	// x1,y1,x2,y2 - ���ó����߶ζ˵�����
	inline void clipped_segment(
		double di, double ys, double width, double height, 
		double& x1, double& y1, double& x2, double& y2 )
	{
		if ((ys > 0.0) && (ys < height))
		{ // λ���������
			x1 = 0.0;
			y1 = ys;
		}
		else if (di > 0.0)
		{	// �ױ���
			x1 = -ys / di;
			y1 = 0.0;
		}
		else
		{	// λ���ϱ�����
			x1 = (height - ys) / di;
			y1 = height;
		}

		double yy = di * width + ys;
		if ((yy > 0.0) && (yy < height))
		{	// �ұ���
			x2 = width;
			y2 = yy;
		}
		else if (di > 0.0) 
		{	// �ϱ��߽���
			x2 = (height - ys) / di;
			y2 = height;
		}
		else 
		{	// �±��߽���
			x2 = -ys / di;
			y2 = 0.0;
		}
	}

	// ����2��ֱ�߷���,���㽻������
	inline void calc_cross_point(
		double di1, double ys1,
		double di2, double ys2,
		double& x, double& y)
	{
		if( di1 != di2 )
		{
			x = (ys2 - ys1) / (di1 - di2);
			y = di1 * x + ys1;
		}
	}

}// namespace cg

class Voronoi
{
protected:
	VectorPoint m_p;
	unsigned int m_n;
	int m_height;
	int m_width;

	Color m_bkColor;
	Color m_lineColor;
	Color m_col2;
	Color m_col3;
	Color m_col4;
	bool m_showbg;

	VectorTessellation m_tessellations;

public:
	Voronoi(unsigned int maxPoints, int width, int height)
	{
		init( maxPoints, width, height );
	}

	bool init(unsigned int maxPoints, int width, int height)
	{
		assert( maxPoints > 0 );
		//m_bkColor = RGB( 0,0, 255);   // blue
		m_bkColor = RGB( 192,192,192 );// gray
		m_col2 = RGB( 255,255, 0); // yellow;
		m_col3 = RGB(255,255,255); // white;
		m_col4 = RGB(255,0,255);   // 
		m_lineColor = RGB(255,0,0);   // red
		m_width = width;
		m_height = height;
		m_n = 0;
		m_p.resize(maxPoints);
		m_showbg = true;
		return true;
	}
	
	void reset( )
	{
		m_tessellations.clear();
		m_n = 0;
	}

	void showBackground( bool show)
	{
		m_showbg = show;
	}

	void setBackColor( Color bkColor )
	{
		m_bkColor = m_bkColor;
	}

	void setLineColor( Color lineColor )
	{
		m_lineColor = lineColor ;
	}

	unsigned int getPoints() const
	{
		return m_n;
	}

	unsigned int getMaxPoints() const
	{
		return (unsigned int)m_p.size();
	}

	bool addPoint(int x, int y)
	{
		if ( x < 0 || x > m_width  || y < 0 || y > m_height || m_n == m_p.size() )
		{
			return false;
		}

		m_p[m_n] = Point(x, y); 
		m_n ++;

		return true;
	}

	// return -1 if no tessellations, else returns the index
	int hitTest(int x, int y) const
	{
		return cg::site_hit_test( m_p, m_n, x, y );
	}

	// ɾ��index������ĵ�
	bool erasePoint( int index )
	{
		if( index < 0 || index >= (int)m_n )
		{
			return false;
		}

		// ������߼�ִ��erase��ͬ���߼�,����û�н�m_p�ĳ�����С
		std::copy(m_p.begin() + index + 1, m_p.begin() + m_n, m_p.begin() + index);

		m_n --;

		return true;
	}

	// �ƶ�index������ĵ�
	// absΪtrueʱ��ʾ��(x,y)����Ϊ����Ŀ��λ��
	// absΪfalseʱ��ʾ���㵱ǰ��λ�������ƫ��
	bool movePoint(int index, int x, int y, bool abs = true)
	{
		if( index < 0 || index >= (int)m_n)
		{
			return false;
		}
			
		int tempX = x;
		int tempY = y;
		if( !abs)
		{
			tempX += m_p[index].x;
			tempY += m_p[index].y;
		}
		if ( tempX < 0 || tempX > m_width  || tempY < 0 || tempY > m_height )
		{
			return false;
		}
		m_p[index].x = tempX;
		m_p[index].y = tempY;

		return true;
	}

	bool randomPoints(unsigned int n, int cx, int cy )
	{
		if ( cx < 0 || cx > m_width  || cy < 0 || cy > m_height || n > m_p.size() )
		{
			return false;
		}

		srand( (unsigned)std::time( 0 ) );

		int x = 0, y = 0;
		int signx = 1, signy = 1;
		for (unsigned int c = 0; c < n; c ++ )
		{
			int rx = rand();
			int ry = rand();
			signx = !!(rx&0x400) * 2 - 1;
			signy = !!(ry&0x400) * 2 - 1;
			if( signx > 0 )
			{
				x = rx % (m_width-cx);
			}
			else
			{
				x = rx % cx;
			}
			if ( signy > 0 )
			{
				y = ry % (m_height-cy);
			}
			else
			{
				y = ry % cy;
			}			

			m_p[c] = Point(cx+signx*x, cy+signy*y); 
		}

		m_n = n;

		return true;
	}

	void calcTessellations()
	{
		int N = m_n;
		if( N == 0 )
		{
			m_tessellations.clear();
			return;
		}

		VectorD _x1(N);
		VectorD _y1(N);

		m_tessellations.clear();
		m_tessellations.resize(N);
		for (int k = 0; k < N; k ++ )
		{
			_x1[k] = (m_p[k].x + cos(k * 1.1f) + k * 0.0001f);
			_y1[k] = (m_p[k].y + sin(k * 1.1f) + k * 0.001f);
			m_tessellations[k].center.x = (Point::coord_type)(_x1[k]);
			m_tessellations[k].center.y = (Point::coord_type)(_y1[k]);
		}

		if (N > 1) 
		{
			VectorD _kx(N);
			VectorD _ky(N);

			for (int i = 0; i < N - 1; i ++)
			{
				for (int j = i + 1; j < N; j ++)
				{
					double di;	// p(i), p(j) ���д��ߵ�б��
					double ys;	// p(i), p(j) ���д��ߵĽؾ�
					cg::perpendicular_bisector_params( _x1[i], _y1[i], _x1[j], _y1[j], di, ys );

					// �����д��ߵı߽������(x0,y0), (xa1,ya1)
					double x0 = 0.0;
					double y0 = 0.0;
					double xa1 = 0.0;
					double ya1 = 0.0;
					cg::clipped_segment( di, ys, m_width, m_height, x0, y0, xa1, ya1 );
					
					int l = 1;
					double _sa2 = _x1[j] - _x1[i];
					double _sa4 = _y1[j] - _y1[i];

					double d_ij = cg::square_distance(_sa2, _sa4);

					bool _break = false;
					for (int k = 0; k < N; k ++ )
					{
						if ((k != i) && (k != j))
						{
							double di2;	// p(i), p(k) ���д��ߵ�б��
							double ys2;	// p(i), p(k) ���д��ߵĽؾ�
							cg::perpendicular_bisector_params( _x1[i], _y1[i], _x1[k], _y1[k], di2, ys2);

							double y20 = di2 * x0 + ys2;	// p(i), p(k) ���д�����p(i),p(j)�д��ߵĽ���߽�(x0,y20),(xa1,y21)
							double y21 = di2 * xa1 + ys2;	//

							double _sa0 = y0 - y20;			// 
							double _sa1 = ya1 - y21;
							double _sa3 = _x1[k] - _x1[i];
							double _sa5 = _y1[k] - _y1[i];

							double d_ik = cg::square_distance(_sa3,_sa5);

							bool sa = false;
							if ((_sa2 * _sa3 > 0.0) && (_sa4 * _sa5 > 0.0))
							{
								// ���� (i,j) (i,k) �Ƿ���ͬһ����(��_x1[i],_y1[i])Ϊ����ԭ�������ϵ)
								sa = d_ij > d_ik;
							}

							if ((_sa0 * _sa1 > 0.0) && sa)
							{
								_break = true;
								break;
							}
							
							//if (((_sa0 * _sa1 < 0.0) || (d_ij < d_ik) || sa) &&
							//	((_sa0 * _sa1 < 0.0) || (d_ij > d_ik)) )

							// ������
							if ( (_sa0 * _sa1 < 0.0) || sa )
							{
								// ������߶�(i,j), (i,k)���д��ߵĽ�������
								cg::calc_cross_point( di, ys, di2, ys2, _kx[l], _ky[l]);
								l ++;
							}
						}
					}

					if (!_break)
					{
						_kx[0] = x0;
						_ky[0] = y0;
						_kx[l] = xa1;
						_ky[l] = ya1;
						l ++;

						// ���������� (kx, ky) ��xֵ��С��������
						// ��Щ��������н���(i,j)���д��߻��ֳ�l-1��,
						// ��l-1������������һ�ο��ܱ���������Ϊsite(i),site(j)�ķָ���
						// Ҳ�п�����Щ�߶�ȫ��������,��(i,j)֮��û������voronoi������д��߶�
						heapv(_kx, _ky, l);
						
						for (int k = 0; k < l-1; k ++ )
						{
							// �����߶�(k,k+1)���е�
							double _xx = (_kx[k] + _kx[k+1]) / 2.0;
							double _yy = di * _xx + ys;

							// �����߶�(k,k+1)���е��Ƿ��Ǿ����i����ĵ�,�Դ����ж��߶�(k,k+1)
							// �Ƿ������Ϊsite(i),site(j)��voronoi�ָ���
							bool _in = cg::in_target_site( _x1, _y1, N, _xx, _yy, i, j );
							if ( _in )
							{
								// �ҵ��������������д��߶�(��Ϊ�����,�����һ��,����,������Ϳ���������ѭ����)
								Edge edgeLR, edgeRL;
								edgeLR.from = Point((Point::coord_type)(_kx[k]),(Point::coord_type)(_ky[k]));
								edgeLR.to = Point((Point::coord_type)(_kx[k+1]),(Point::coord_type)(_ky[k+1]));
								edgeRL.from = edgeLR.to;
								edgeRL.to = edgeLR.from;
								if ( _y1[i] > _y1[j] )
								{
									m_tessellations[i].polyedge.push_back( edgeRL );
									m_tessellations[j].polyedge.push_back( edgeLR );
								}
								else
								{
									m_tessellations[i].polyedge.push_back( edgeLR );
									m_tessellations[j].polyedge.push_back( edgeRL );
								}
								break;
							}
						}
					}
				}
			}
		}

		for (int n = 0; n < N; n ++)
		{
			m_tessellations[n].close_polyedge(m_width, m_height);
			m_tessellations[n].make_polygon();
		}
	}

	void paintTessellations(Graphics& hdc)
	{
		if( m_showbg )
		{
			hdc.setColor(m_bkColor);
			hdc.fillRect(1, 1, m_width, m_height);
		}
	
		int N = (int)m_tessellations.size();
		assert( N == m_n );

		hdc.setColor(m_lineColor);
		for (int k = 0; k < N; k ++ )
		{
			int _x = (int)m_tessellations[k].center.x;
			int _y = (int)m_tessellations[k].center.y;
			hdc.fillOval(_x - 2, _y - 2, 4, 4);
			std::wostringstream woss;
			woss << k;
			hdc.drawString(woss.str(), _x-5, _y-20); 

			const Tessellation& tess = m_tessellations[k];

#ifndef TEST_DIRECTION
			int edge = (int)tess.polygon.size();
			for ( int l = 0; l < edge; l ++ )
			{
				int n = (l+1)%edge;
				int x1 = uround(tess.polygon[l].x);
				int y1 = uround(tess.polygon[l].y);
				int x2 = uround(tess.polygon[n].x);
				int y2 = uround(tess.polygon[n].y);
				hdc.drawLine( x1, y1, x2, y2 );
			}

#else
			int edge = (int)tess.polyedge.size();
			for ( int l = 0; l < edge; l ++ )
			{
				int x1 = uround(tess.polyedge[l].from.x);
				int y1 = uround(tess.polyedge[l].from.y);
				int x2 = uround(tess.polyedge[l].to.x);
				int y2 = uround(tess.polyedge[l].to.y);
	
				hdc.drawLine( x1, y1, x2, y2 );
				
				if( tess.bHeadSite )
				{
					hdc.setColor(RGB(220,155,0));
					hdc.fillOval(x1-5, y1-5, 10, 10);
					hdc.setColor(m_lineColor);
				}
				else if( tess.bEarSite )
				{
					hdc.setColor(RGB(155,0,255));
					hdc.fillOval(x1-5, y1-5, 10, 10);
					hdc.setColor(m_lineColor);
				}
				else if ( tess.bMouthSite )
				{
					hdc.setColor(RGB(155,255,0));
					hdc.fillOval(x1-5, y1-5, 10, 10);
					hdc.setColor(m_lineColor);
				}
				else if ( tess.bBodySite )
				{
					hdc.setColor(RGB(0,155,255));
					hdc.fillOval(x1-5, y1-5, 10, 10);
					hdc.setColor(m_lineColor);
				}
				
				if( tess.flags != 0 )
				{
					woss.str(L"");
					woss << L"f:" << k << L"," << l;
					hdc.drawString(woss.str(), x1, y1+k*20); 

					woss.str(L"");
					woss<< L"t:" << k << L"," << l;
					hdc.drawString(woss.str(), x2+40, y2+(k)*20); 
				}
			}
#endif
		}

		std::wostringstream woss;
		woss << L"Sites:" << N;
		hdc.setColor(m_lineColor);
		hdc.drawString(woss.str(), 5, m_height+5); 
	}

	void calcAndPaintTessellations(Graphics& hdc)
	{
		hdc.setColor(m_bkColor);
		hdc.fillRect(1, 1, m_width, m_height);
		
		int N = m_n;
		if( N == 0 )
		{
			return;
		}

		hdc.setColor(m_lineColor);
		VectorD _x1(N);
		VectorD _y1(N);
		for (int k = 0; k < N; k ++ )
		{
			_x1[k] = (m_p[k].x + cos(k * 1.1f) + k * 0.0001f);
			_y1[k] = (m_p[k].y + sin(k * 1.1f) + k * 0.001f);
			int _x = (int)_x1[k];
			int _y = (int)_y1[k];

			hdc.fillOval(_x - 2, _y - 2, 4, 4);

			std::wostringstream woss;
			woss << k;
			hdc.drawString(woss.str(), _x-5, _y-20); 
		}

		std::wostringstream woss;
		woss << L"Sites:" << N;
		hdc.drawString(woss.str(), 5, m_height+5); 

		if (N == 1) 
		{
			return ;
		}

		VectorD _kx(N);
		VectorD _ky(N);
		for (int i = 0; i < N - 1; i ++)
		{
			for (int j = i + 1; j < N; j ++)
			{
				double di;			// p(i), p(j) ���д��ߵ�б��
				double ys;			// p(i), p(j) ���д��ߵĽؾ�
				cg::perpendicular_bisector_params( _x1[i], _y1[i], _x1[j], _y1[j], di, ys );

				// �����д��ߵı߽������(x0,y0), (xa1,ya1)
				double x0 = 0.0;
				double y0 = 0.0;
				double xa1 = 0.0;
				double ya1 = 0.0;
				cg::clipped_segment( di, ys, m_width, m_height, x0, y0, xa1, ya1 );
				
				//hdc.setColor(m_col4);
				//hdc.drawLine(uround(x0), uround(y0), uround(xa1), uround(ya1) );
				//hdc.setColor(m_col3);

				// Ѱ���д���(i,j)���������������i����д��ߵĽ���(���ʵ��Ĳü�)
				// (_kx,_ky)��¼���г����ü���Ľ�������
				// ���Ƚ�y��Ľ�����Ϊ���,�����y��ƽ�е��ұ߽�Ľ�����Ϊ�յ�
				// ��Ϊ������������i���д��߸���������N-2,
				// ����,�����߽��,��Щ���������������N.
				_kx[0] = x0;
				_ky[0] = y0;
				int l = 1;
				double _sa2 = _x1[j] - _x1[i];
				double _sa4 = _y1[j] - _y1[i];

				double d_ij = cg::square_distance(_sa2,_sa4);

				bool _break = false;
				for (int k = 0; k < N; k ++ )
				{
					if ((k != i) && (k != j))
					{
						double di2;				// p(i), p(k) ���д��ߵ�б��
						double ys2;				// p(i), p(k) ���д��ߵĽؾ�
						cg::perpendicular_bisector_params( _x1[i],_y1[i],_x1[k],_y1[k], di2, ys2);

						double y20 = di2 * x0 + ys2;	// p(i), p(k) ���д�����p(i),p(j)�д��ߵĽ���߽�(x0,y20),(xa1,y21)
						double y21 = di2 * xa1 + ys2;	//

						double _sa0 = y0 - y20;			// 
						double _sa1 = ya1 - y21;
		
						double _sa3 = _x1[k] - _x1[i];
						double _sa5 = _y1[k] - _y1[i];

						double d_ik = cg::square_distance(_sa3,_sa5);

						bool sa = false;
						if ((_sa2 * _sa3 > 0.0) && (_sa4 * _sa5 > 0.0))
						{
							// ���� (i,j) (i,k) �Ƿ���ͬһ����(��_x1[i],_y1[i])Ϊ����ԭ�������ϵ)
							sa = d_ij > d_ik;
						}							

						if ((_sa0 * _sa1 > 0.0) && sa)
						{
							_break = true;
							break;
						}

						//if (((_sa0 * _sa1 < 0.0) || (d_ij < d_ik) || sa) &&
						//	((_sa0 * _sa1 < 0.0) || (d_ij > d_ik)) )

						// ������
						if ( _sa0 * _sa1 < 0.0 || sa )
						{
							// ������߶�(i,j), (i,k)���д��ߵĽ�������
							cg::calc_cross_point( di, ys, di2, ys2, _kx[l], _ky[l]);
							l ++;
						}
					}
				}

				if (!_break)
				{
					// ���������� (kx, ky) ��xֵ��С��������
					// ��Щ��������н���(i,j)���д��߻��ֳ�l-1��,
					// ��l-1������������һ�ο��ܱ���������Ϊsite(i),site(j)�ķָ���
					// Ҳ�п�����Щ�߶�ȫ��������,��(i,j)֮��û������voronoi������д��߶�
					heapv(_kx, _ky, l);	
					
					_kx[l] = xa1;
					_ky[l] = ya1;
					l ++;
					
					//hdc.setColor(m_col4);
					//hdc.drawLine(uround(x0), uround(y0), uround(xa1), uround(ya1) );
					

					//for (int k = 0; k < l; k ++ )
					//{
					//	int x = uround( _kx[k] );
					//	int y = uround( _ky[k] );
					//	hdc.fillOval( x - 4, y - 4, 8, 8 );
					//}

					hdc.setColor(m_col3);

					// ɸѡ�д��߽�������
					for (int k = 0; k < l-1; k ++ )
					{
						// �ָ���(_kx[k], _kx[k+1])�����ĵ�
						double _xx = (_kx[k] + _kx[k+1]) / 2.0;
						double _yy = di * _xx + ys;

						//hdc.setColor( m_lineColor );
						//int xx = uround( _xx );
						//int yy = uround( _yy );
						//hdc.fillRect( xx - 4, yy - 4, 8, 8 );
						//std::wostringstream woss;
						//woss << i << L"," << j << L"," << k ;
						//hdc.drawString(woss.str(), xx, yy-20); 

						int x1 = uround(_kx[k]);
						int x2 = uround(_kx[k+1]);
						int y1 = uround(_ky[k]);
						int y2 = uround(_ky[k+1]);
						std::wostringstream woss1, woss2;

						// �����߶�(k,k+1)���е��Ƿ��Ǿ����i����ĵ�,�Դ����ж��߶�(k,k+1)
						// �Ƿ������Ϊsite(i),site(j)��voronoi�ָ���
						bool _in = cg::in_target_site( _x1, _y1, N, _xx, _yy, i, j );
						if ( _in )
						{							
							// �������κλ�������,�����÷ָ���,��i,j�ķָ���(k,k+1)

							//hdc.setColor( m_lineColor );
							//int xx = uround( _xx );
							//int yy = uround( _yy );
							//hdc.fillRect( xx - 4, yy - 4, 8, 8 );
							
#ifndef TEST_DIRECTION
							hdc.setColor(m_lineColor);
							hdc.drawLine(x1, y1, x2, y2);
#else

							if ( _y1[i] > _y1[j] )
							{
								hdc.drawLine(x1, y1-1, x2, y2-1);
								hdc.setColor(m_lineColor);
								hdc.drawLine(x1, y1+1, x2, y2+1);
							}
							else
							{
								hdc.setColor(m_col3);
								hdc.drawLine(x1, y1-1, x2, y2-1);
								hdc.setColor(m_lineColor);
								hdc.drawLine(x1, y1+1, x2, y2+1);
							}

#endif

#ifdef TEST_BISECOTER
							hdc.setColor(m_lineColor);
							woss1 << L"[" << i << L"," << j << L"," << k << L"]";
							woss2 << L"[" << i << L"," << j << L"," << k+1 << L"]";

							if( k != 0)
							{
								hdc.drawString(woss1.str(), x1+60, y1 + (j-1+i)*20);
							}
							else
							{
								hdc.drawString(woss1.str(), x1, y1);
							}

							if( k != N-2 )
							{
								hdc.drawString(woss2.str(), x2+60, y2 + (j-1+i)*20 );
							}
							else
							{
								hdc.drawString(woss2.str(), x2, y2 );
							}
#else

							break; // Ϊ��������������߶�,����Ͳ�break
#endif

						}

#ifdef TEST_BISECOTER
						else
						{
							// ��������ĳ���ѻ�������,�������÷ָ���
							hdc.setColor( RGB(0,255,0) );
							hdc.drawDashLine(x1, y1, x2, y2);

							//int xx = uround( _xx );
							//int yy = uround( _yy );
							//hdc.fillRect( xx - 4, yy - 4, 8, 8 );
							//std::wostringstream woss;
							//woss << L"(" << i << L","<< j << L"," << k << L")";
							//hdc.drawString(woss.str(), xx-30, yy-20); 

							hdc.setColor( m_col4 );
							hdc.fillRect( x1 - 4, y1 - 4, 8, 8 );
							hdc.fillRect( x2 - 4, y2 - 4, 8, 8 );

							woss1 << L"{" << i << L"," << j << L"," << k << L"}";
							woss2 << L"{" << i << L"," << j << L"," << k+1 << L"}";

							if( k != 0 )
							{
								hdc.drawString(woss1.str(), x1, y1 + (j-1+i)*20);
							}
							else
							{
								hdc.drawString(woss1.str(), x1, y1 );
							}

							if( k != N-2 )
							{
								hdc.drawString(woss2.str(), x2, y2 + (j-1+i)*20 );
							}
							else
							{
								hdc.drawString(woss2.str(), x2, y2 );
							}
						}
#endif
					}
				}
			}
		}
	}

	VectorTessellation& getTessellations()
	{
		return m_tessellations;
	}

	const VectorTessellation& getTessellations() const 
	{
		return m_tessellations;
	}
};

} // namespace voronoi

#endif // #ifdef VORONOI_H_INCLUDED