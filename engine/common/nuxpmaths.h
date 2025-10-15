#ifndef __Nuclear_XPMATHS_H__
#define __Nuclear_XPMATHS_H__
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <windows.h>
#endif
#include <math.h>
#include <vector>
#include <algorithm>
#include "nuport.h"
#include "nugeom.h"

#define XPNORMALIZE(a, b, v) (static_cast<float>((v)-(a)) / ((b)-(a)))

namespace Nuclear
{
	extern const float FIRE_SIN[181];
	
	const float FIRE_EPSILON = 0.00001f;
	const float FIRE_PI	= 3.14159265f;
	const float FIRE_HALFPI = 1.570796325f;

	inline void XPSinCos(float radian, float *sin, float *cos)
	{
        *sin = sinf(radian);
        *cos = cosf(radian);
//		__asm
//		{
//			FLD		radian
//				FSINCOS
//				MOV		ECX,sin
//				MOV		EDX,cos
//				FSTP	DWORD PTR [EDX]
//			FSTP	DWORD PTR [ECX]
//		}
	}

	//根据角度获得弧度PI/180.0f = 0.01745 //误差积累很严重..
	inline float TORADIANS(float p) { return p * 0.017453292519943295769236907684886f; }
	//根据弧度获得角度   180.0/PI = 57.29583 //精度..
	inline float TOANGLE(float r) { return r * 57.295779513082320876798154814105f; }

	inline float XPSIN(int angle) //角度
	{
		//先把角度转化到 0---360 之间
		angle -= (angle/360) *360;
		if(angle < 0) 
			angle += 360;
		
		//查表获得结果
		return angle<181?FIRE_SIN[angle]:-FIRE_SIN[angle-180];
	}

	inline float XPCOS(int angle) //角度
	{
		return XPSIN(90+angle);
	}

	inline int GetGreatestDenominator(int a1, int a2)
	{
		int tmp, p, r;
		if (a1 < a2)
		{
			tmp = a1; a1 = a2; a2 = tmp;
		}
		p = a1 * a2;
		while (a2 != 0)
		{
			r = a1 % a2;
			a1 = a2;
			a2 = r;
		}
		return a1;
	}
	inline int GetLowestComMultiple(int a1, int a2)
	{
		return a1 * a2 / GetGreatestDenominator(a1, a2);
	}

	template <class T> 
	inline T Lerpf( const T& v1, const T& v2, float s)
	{
		return (T)(v1 + ( v2 - v1) * s);
	}

	template<class T>
	bool IsRectCross(const T &r1, const T &r2)
	{
		if (r1.bottom <= r2.top)
			return false;
		if (r1.top >= r2.bottom)
			return false;
		if (r1.left >= r2.right)
			return false;
		if (r1.right <= r2.left)
			return false;
		return true;
	}

	template<class T>
	void NormalizeRect(T &r)
	{
		if (r.top > r.bottom)
			std::swap(r.top, r.bottom);
		if (r.left > r.right)
			std::swap(r.left, r.right);
	}

	struct NuclearVector3
	{
		float x;
		float y;
		float z;
		NuclearVector3 (float _x=0.0f, float _y=0.0f, float _z=0.0f) : x(_x), y(_y), z(_z) {};
		NuclearVector3& operator += (const NuclearVector3 &v) { x+=v.x;y+=v.y;z+=v.z;return *this;}
		NuclearVector3& operator -= (const NuclearVector3 &v) { x-=v.x;y-=v.y;z-=v.z;return *this;}
		NuclearVector3& operator *= (float s) { x*=s;y*=s;z*=s;return *this;}
		NuclearVector3& operator /= (float s) { x/=s;y/=s;z/=s;return *this;}
		NuclearVector3 operator + (const NuclearVector3 &v) const { return NuclearVector3(x+v.x, y+v.y, z+v.z);}
		NuclearVector3 operator - (const NuclearVector3 &v) const { return NuclearVector3(x-v.x, y-v.y, z-v.z);}
		NuclearVector3 operator * (float s) const { return NuclearVector3(x*s, y*s, z*s);}
		NuclearVector3 operator / (float s) const { return NuclearVector3(x/s, y/s, z/s);}
		NuclearVector3& Reset(float _x,float _y,float _z)  { x = _x; y = _y; z = _z; return *this; }
		NuclearVector3& Add(float _x,float _y,float _z)  { x += _x; y += _y; z += _z; return *this; }
		float Length() const { return sqrtf(x*x+y*y+z*z); }
	};

	struct NuclearVector4
	{
		float x;
		float y;
		float z;
		float w;
		NuclearVector4 (float _x=0.0f, float _y=0.0f, float _z=0.0f, float _w=0.0f) : x(_x), y(_y), z(_z), w(_w) {};
		NuclearVector4 &operator += (const NuclearVector4 &v) { x+=v.x;y+=v.y;z+=v.z;w+=v.w;return *this;}
		NuclearVector4 &operator -= (const NuclearVector4 &v) { x-=v.x;y-=v.y;z-=v.z;w-=v.w;return *this;}
		NuclearVector4 &operator *= (const float s) { x*=s;y*=s;z*=s;w*=s;return *this;}
		NuclearVector4 &operator /= (const float s) { x/=s;y/=s;z/=s;w/=s;return *this;}
		NuclearVector4 operator + (const NuclearVector4 &v) const { return NuclearVector4(x+v.x, y+v.y, z+v.z, w+v.w);}
		NuclearVector4 operator - (const NuclearVector4 &v) const { return NuclearVector4(x-v.x, y-v.y, z-v.z, w-v.w);}
		NuclearVector4 operator * (const float s) const { return NuclearVector4(x*s, y*s, z*s, w*s);}
		NuclearVector4 operator / (const float s) const { return NuclearVector4(x/s, y/s, z/s, w/s);}
	};

	struct NuclearFColor
	{
		float r,g,b,a;
		NuclearFColor(float _r=0.f,float _g=0.f,float _b=0.f,float _a=0.f) : r(_r), g(_g), b(_b), a(_a) { }
		void Reset(float _r,float _g,float _b,float _a)  { r = _r; g = _g; b = _b; a = _a; }
		DWORD ToDWORD() const
		{
			DWORD d = (DWORD)(b*255);
			d |= ((DWORD)(g*255)<<8);
			d |= ((DWORD)(r*255)<<16);
			d |= ((DWORD)(a*255)<<24);
			return d;
		}
	};

	typedef struct NuclearFPoint
	{
		float x;
		float y;
		NuclearFPoint(float _x = 0.f, float _y = 0.f) : x(_x), y(_y) { }
		NuclearFPoint(const POINT& p) : x(static_cast<float>(p.x)), y(static_cast<float>(p.y)) { }

		NuclearFPoint& operator=(const POINT &p) { x=static_cast<float>(p.x); y=static_cast<float>(p.y); return *this; }
		NuclearFPoint operator+(const NuclearFPoint &fp) const { return NuclearFPoint(x+fp.x, y+fp.y); }
		NuclearFPoint& operator+=(const NuclearFPoint &fp) { x+=fp.x; y+=fp.y; return *this; }
		NuclearFPoint operator-(const NuclearFPoint &fp) const { return NuclearFPoint(x-fp.x, y-fp.y); }
		NuclearFPoint& operator-=(const NuclearFPoint &fp) { x-=fp.x; y-=fp.y; return *this; }
		NuclearFPoint operator * ( float k) const
		{
			NuclearFPoint result = *this;
			result *= k;
			return result;
		}
		NuclearFPoint operator / ( float k) const
		{
			NuclearFPoint result = *this;
			result /= k;
			return result;
		}
		NuclearFPoint& operator *= ( float k)
		{
			x *= k;
			y *= k;
			return *this;
		}

		NuclearFPoint& operator /= ( float k)
		{
			return (*this) *= (1/k);
		}

		bool operator==(const NuclearFPoint &fp) const
		{
			return ( fabs(x-fp.x)<FIRE_EPSILON ) && ( fabs(y-fp.y)<FIRE_EPSILON );
		}

		bool operator != (const NuclearFPoint& oth) const
		{
			return (fabs(x - oth.x) >= FIRE_EPSILON) || (fabs(y - oth.y) >= FIRE_EPSILON);
		}

		float Dis2(const NuclearFPoint &cp) const { return (x-cp.x)*(x-cp.x)+(y-cp.y)*(y-cp.y); }

		float angleBetween(const NuclearFPoint& vec)
		{
			return atan2(vec.y, vec.x) - atan2(y, x);
		}

		//判断点是否在凸多边形内
		bool isInPolygon(const std::vector<NuclearFPoint>& polygon) const
		{
			//polygon中存的是多边形的所有顶点
			//只能是凸多边形
			//基本思路是把凸多边形分解成三角形然后再判断点在三角形内...
			bool bres = false;
			int cnt = (int)polygon.size();
			for(int i=2; i<cnt; i++)
			{
				bres = isInTriangle(polygon[0], polygon[i-1], polygon[i]);
				if(bres)
					break;
			}
			return bres;
		}

		bool isInTriangle(NuclearFPoint p0, NuclearFPoint p1, NuclearFPoint p2) const;

		void normalize()
		{
			float length = sqrtf(x*x + y*y);
			x /= length;
			y /= length;
		}

	} NuclearVector2;

	NuclearFPoint inline operator * ( float k, const NuclearFPoint& v)
	{
		NuclearFPoint result = v;
		result *= k;
		return result;
	}

	typedef struct NuclearPoint : public POINT
	{
		NuclearPoint(int _x=0, int _y=0) { x = _x; y = _y; }
		NuclearPoint(const POINT &pt) { x = pt.x; y = pt.y; }
		NuclearPoint(const NuclearFPoint &pt) { x = (int)pt.x; y=(int)pt.y;  }
		NuclearPoint operator-(const NuclearPoint &cp) const { return NuclearPoint(x-cp.x, y-cp.y); }
		NuclearPoint operator-() const { return NuclearPoint(-x, -y); }
		NuclearPoint operator+(const NuclearPoint &cp) const { return NuclearPoint(x+cp.x, y+cp.y); }

		NuclearPoint& operator-=(const NuclearPoint &cp) { x-=cp.x; y-=cp.y; return *this; }
		NuclearPoint& operator+=(const NuclearPoint &cp) { x+=cp.x; y+=cp.y; return *this; }
		NuclearPoint& operator *= ( float k) { x = (int)(x*k+0.5f); y = (int)(y*k+0.5f); return *this; }
		NuclearPoint& operator /= ( float k) { return (*this) *= (1/k); }

		bool operator==(const NuclearPoint &l) const { return x == l.x && y == l.y; }
		bool operator!=(const NuclearPoint &l) const { return !operator==(l); }
		int Dis2(const NuclearPoint &cp) const { return (x-cp.x)*(x-cp.x)+(y-cp.y)*(y-cp.y); }
		NuclearFPoint ToFPOINT() const { return NuclearFPoint((float)x,(float)y); }
	} CSIZE, COFFSET, NuclearLocation;

	struct NuclearFRectt
	{
		float left;
		float top;
		float right;
		float bottom;

		NuclearFRectt(float l = 0.f, float t = 0.f, float r = 0.f, float b = 0.f) : left(l), top(t), right(r), bottom(b) { }
		NuclearFRectt(const NuclearFPoint &ltpoint, float width, float height) { left = ltpoint.x; top = ltpoint.y; right = left + width; bottom = top + height; }
		NuclearFRectt(const NuclearFPoint & lt, float size)        { left = lt.x; top = lt.y; right = left + size; bottom = top + size; }
		NuclearFRectt(const NuclearFPoint & lt, const NuclearFPoint & rb) { left = lt.x; top = lt.y; right = rb.x; bottom = rb.y; }

		NuclearFRectt& operator+=(const NuclearFPoint &fp)     { left+=fp.x;right+=fp.x;top+=fp.y;bottom+=fp.y; return *this; }
		NuclearFRectt& operator+=(const NuclearPoint &cp)     { left+=cp.x;right+=cp.x;top+=cp.y;bottom+=cp.y; return *this; }
		NuclearFRectt& operator-=(const NuclearFPoint &fp)     { left-=fp.x;right-=fp.x;top-=fp.y;bottom-=fp.y; return *this; }
		NuclearFRectt& operator-=(const NuclearPoint &cp)     { left-=cp.x;right-=cp.x;top-=cp.y;bottom-=cp.y; return *this; }
		NuclearFRectt& operator/=(float s)              { left/=s;right/=s;top/=s;bottom/=s; return *this; }
		NuclearFRectt& operator*=(float s)              { left*=s;right*=s;top*=s;bottom*=s; return *this; }

		NuclearFRectt operator+(const NuclearFPoint &fp) const { return NuclearFRectt(left+fp.x, top+fp.y, right+fp.x, bottom+fp.y); }
		NuclearFRectt operator-(const NuclearFPoint &fp) const { return NuclearFRectt(left-fp.x, top-fp.y, right-fp.x, bottom-fp.y); }
		NuclearFRectt operator/(float s) const          { return NuclearFRectt(left/s, top/s, right/s, bottom/s); }
		NuclearFRectt operator*(float s) const          { return NuclearFRectt(left*s, top*s, right*s, bottom*s); }

		bool operator==(const NuclearFRectt& f) const
		{ 
			return ( fabs(left-f.left)<FIRE_EPSILON ) && ( fabs(top-f.top)<FIRE_EPSILON ) && ( fabs(right-f.right)<FIRE_EPSILON ) && ( fabs(bottom-f.bottom)<FIRE_EPSILON );
		}

		NuclearFRectt& Assign(float l, float t, float r, float b) { left = l; top = t; right = r; bottom = b; return *this; }

		float Width() const { return right - left; }
		float Height() const { return bottom - top; }
		bool PtInRect(const NuclearFPoint &pt) const { return pt.x >= left && pt.x < right && pt.y >= top && pt.y < bottom; }

	};

	inline int Cross(const POINT& pt1, const POINT& pt2)
	{
		return pt1.x * pt2.y - pt1.y * pt2.x;
	}

	inline bool IsCross(const NuclearPoint& p1, const NuclearPoint& p2, const NuclearPoint& q1, const NuclearPoint& q2)
	{
		//快速排斥实验
		RECT r1, r2;
		r1.left = p1.x;	r1.top = p1.y; r1.right = p2.x; r1.bottom = p2.y;
		r2.left = q1.x;	r2.top = q1.y; r2.right = q2.x; r2.bottom = q2.y;
		NormalizeRect(r1);NormalizeRect(r2);
		if (!IsRectCross(r1, r2))
			return false;
		//跨立实验
		NuclearPoint tmp = p2-p1;
		if (Cross(q1-p1, tmp) * Cross(tmp, q2-p1) < 0)
			return false;
		tmp = q2 - q1;
		if (Cross(p2-q1, tmp) * Cross(tmp, p1-q1) < 0)
			return false;
		return true;
	}

	struct NuclearRect : public RECT
	{
		NuclearRect(int l=0, int t=0, int r=0, int b=0) { left=l; top=t; right=r; bottom=b; }
		NuclearRect(const NuclearPoint &ltpoint, int width, int height) { left = ltpoint.x; top = ltpoint.y; right = left + width; bottom = top + height; }
		NuclearRect(const RECT &r) { left =r.left; top =r.top; right =r.right; bottom =r.bottom; }

		bool operator==(const NuclearRect &r) const { return (left == r.left && right == r.right && top == r.top && bottom == r.bottom);}

		NuclearRect operator+(const NuclearPoint &fp) const { return NuclearRect(left+fp.x, top+fp.y, right+fp.x, bottom+fp.y); }
		NuclearRect& operator+=(const NuclearPoint &fp) { left+=fp.x;right+=fp.x;top+=fp.y;bottom+=fp.y; return *this; }
		NuclearRect operator-(const NuclearPoint &fp) const { return NuclearRect(left-fp.x, top-fp.y, right-fp.x, bottom-fp.y); }
		NuclearRect& operator-=(const NuclearPoint &fp) { left-=fp.x;right-=fp.x;top-=fp.y;bottom-=fp.y; return *this; }

		NuclearRect operator/(int d) const { return NuclearRect(left/d, top/d, right/d, bottom/d); }
		NuclearRect operator/(float d) const { return NuclearRect(static_cast<int>(left/d), static_cast<int>(top/d), static_cast<int>(right/d), static_cast<int>(bottom/d)); }
		NuclearRect operator*(int d) const { return NuclearRect(left*d, top*d, right*d, bottom*d); }
		NuclearRect operator*(float d) const { return NuclearRect(static_cast<int>(left*d), static_cast<int>(top*d), static_cast<int>(right*d), static_cast<int>(bottom*d)); }

		NuclearRect Widen(int d) const { return NuclearRect(left-d,top-d,right+d,bottom+d); }
		NuclearRect Union(const NuclearRect &rhs) { return NuclearRect(left<rhs.left?left:rhs.left,top<rhs.top?top:rhs.top,right>rhs.right?right:rhs.right,bottom>rhs.bottom?bottom:rhs.bottom); }

		NuclearPoint GetPos() const { return NuclearPoint(left,top); }
		NuclearPoint Center() const { return NuclearPoint((left+right)/2,(top+bottom)/2); }
		NuclearFRectt ToFRECT() const { return NuclearFRectt((float)left,(float)top,(float)right,(float)bottom); }

		NuclearRect& operator=(const RECT &r) { left = r.left; right = r.right; top = r.top; bottom = r.bottom; return *this; }

		NuclearRect& Assign(int l, int t, int r, int b) { left = l; top = t; right = r; bottom = b; return *this; }

		int Width() const { return right - left; }
		int Height() const { return bottom - top; }
		bool PtInRect(const NuclearPoint &pt) const { return pt.x >= left && pt.x < right && pt.y >= top && pt.y < bottom; }

		// 计算相交情况
		bool IsCross(const NuclearRect& rhs, NuclearRect &res) const
		{
			if( left >= rhs.right || right <= rhs.left || top >= rhs.bottom || bottom <= rhs.top )
				return false;
			res.left = left > rhs.left ? left : rhs.left;
			res.right = right < rhs.right ? right : rhs.right;
			res.top = top > rhs.top ? top : rhs.top;
			res.bottom = bottom < rhs.bottom ? bottom : rhs.bottom;
			return true;
		}
		// 根据视窗切割出需要显示的部分
		bool Cut(const NuclearRect& vrect, NuclearRect &drect, NuclearFRectt& srect) const
		{
			if( vrect.left >= right || vrect.right <= left || vrect.top >= bottom || vrect.bottom <= top )
				return false;

			if( vrect.left < left ) { drect.left = left - vrect.left; srect.left = 0.f; }
			else { drect.left = 0; srect.left = (vrect.left - left)/(float)Width(); }

			if( vrect.right < right ) { drect.right = vrect.Width(); srect.right = (vrect.right - left)/(float)Width(); }
			else { drect.right = right - vrect.left; srect.right = 1.f; }

			if( vrect.top < top ) { drect.top = top - vrect.top; srect.top = 0.f; }
			else { drect.top = 0; srect.top = (vrect.top - top)/(float)Height(); }

			if( vrect.bottom < bottom ) { drect.bottom = vrect.Height(); srect.bottom = (vrect.bottom - top)/(float)Height(); }
			else { drect.bottom = bottom - vrect.top; srect.bottom = 1.f; }

			return true;
		}
	};

	//矩形裁剪任意多边形
	class NuclearRectClipPolygon
	{
	public:
		~NuclearRectClipPolygon() {}

	public:
		static bool ClipPolygon(NuclearRect clipWindow, const std::vector<NuclearFPoint>& polygon, std::vector<NuclearFPoint>& polygonResult);

	private:
		enum RectBorderType
		{
			LEFTLINE = 1,
			TOPLINE = 2,
			RIGHTLINE = 3,
			BOTTOMLINE = 4,
		};
		static bool LineClipPolygon(NuclearRect clipWindow, RectBorderType lineType, std::vector<NuclearFPoint>& polygon, std::vector<NuclearFPoint>& polygonResult);
		static NuclearFPoint ComputeCrossPoint(NuclearRect clipWindow, RectBorderType lineType, NuclearFPoint polygonPt0, NuclearFPoint polygonPt1); //线段与直线的交点..

	private:
		NuclearRectClipPolygon();
		NuclearRectClipPolygon(const NuclearRectClipPolygon&);
		NuclearRectClipPolygon& operator=(const NuclearRectClipPolygon&);
	};

	struct NuclearTriangle
	{
		NuclearFPoint pt0;
		NuclearFPoint pt1;
		NuclearFPoint pt2;
		NuclearTriangle() : pt0(), pt1(), pt2() { }
		bool PtInTriangle(const NuclearFPoint &pt) const { return pt.isInTriangle(pt0, pt1, pt2); }

		NuclearTriangle& operator+=(const NuclearFPoint &fp) { pt0+=fp; pt1+=fp; pt2+=fp; return *this; }

		NuclearTriangle& operator *= ( float k)
		{
			pt0 *= k;
			pt1 *= k;
			pt2 *= k;
			return *this;
		}
	};

	struct NuclearTCVertex;
	union NuclearColor;

	//任意多边形分割成三角形序列
	class NuclearPolygonToTriangles
	{
	public:
		~NuclearPolygonToTriangles() {}

	public:
		static bool DecomPolygon(const std::vector<NuclearFPoint>& vextexs, const NuclearColor& cr, std::vector<NuclearTCVertex>& triangles);

		static bool DecomPolygon(const std::vector<NuclearFPoint>& vextexs, std::vector<NuclearTriangle>& triangles);

	private:
		enum ORIENTATIONTYPE
		{
			COUNTER_CLOCKWISE = 0,
			CLOCKWISE = 1,
		};
		static ORIENTATIONTYPE orientation(const std::vector<NuclearFPoint>& vextexs);
		static ORIENTATIONTYPE determinant(int p1, int p2, int p3, const std::vector<NuclearFPoint>& vextexs);
		static float distance2(float x1, float y1, float x2, float y2 );
		static int interior(int p1, int p2, int p3, const std::vector<NuclearFPoint>& vextexs, int vp[], int cnt, int poly_or);

		static const int V_MAX = 300;
		static const long long BIG = 10000000;

	private:
		NuclearPolygonToTriangles();
		NuclearPolygonToTriangles(const NuclearPolygonToTriangles&);
		NuclearPolygonToTriangles& operator=(const NuclearPolygonToTriangles&);
	};

	//快速排斥实验
	inline bool IsCross(const NuclearPoint& p1, const NuclearPoint& p2, const NuclearRect& rc)
	{
		NuclearRect tmpr(p1.x, p1.y, p2.x, p2.y);
		NormalizeRect(tmpr);
		if (!IsRectCross(tmpr, rc))
			return false;
		if (rc.PtInRect(p1))
			return true;
		if (rc.PtInRect(p2))
			return true;
		NuclearPoint p3(rc.left, rc.top);
		NuclearPoint p4(rc.right, rc.bottom);
		if (IsCross(p1, p2, p3, p4))
			return true;
		std::swap(p3.x, p4.x);
		if (IsCross(p1, p2, p3, p4))
			return true;
		return true;
	}

	struct NuclearEllipse
	{
		int x0;
		int y0;
		int rx;
		int ry;
		NuclearEllipse() : x0(0), y0(0), rx(0), ry(0) { }
		bool PtInEllipse(const NuclearPoint &pt) const { return ry*ry*(pt.x-x0)*(pt.x-x0) + rx*rx*(pt.y-y0)*(pt.y-y0) < rx*rx*ry*ry; }
	};

	typedef struct NuclearMatrix3
	{
		union {
			struct {
				float        _11, _12, _13;
				float        _21, _22, _23;
				float        _31, _32, _33;
			};
			float m[3][3];
		};
	public:
		NuclearMatrix3() {
			_11 = 0.0f;
			_12 = 0.0f;
			_13 = 0.0f;
			_21 = 0.0f;
			_22 = 0.0f;
			_23 = 0.0f;
			_31 = 0.0f;
			_32 = 0.0f;
			_33 = 0.0f;
		};

		NuclearMatrix3( float _11, float _12, float _13,
			float _21, float _22, float _23,
			float _31, float _32, float _33)
		{
			this->_11 = _11;
			this->_12 = _12;
			this->_13 = _13;
			this->_21 = _21;
			this->_22 = _22;
			this->_23 = _23;
			this->_31 = _31;
			this->_32 = _32;
			this->_33 = _33;
		}

		operator float* () { return m[0]; }
		operator const float* () const { return m[0]; }

		NuclearMatrix3& operator *= ( const NuclearMatrix3& b) { (*this) = (*this) * b; return (*this); }
		NuclearMatrix3& operator += ( const NuclearMatrix3& b)
		{
			int i, j;
			for (i=0;i<3;++i)
				for (j=0;j<3;++j)
					m[i][j] += b.m[i][j];
			return *this;
		}
		NuclearMatrix3& operator -= ( const NuclearMatrix3& b)
		{
			int i, j;
			for (i=0;i<3;++i)
				for (j=0;j<3;++j)
					m[i][j] -= b.m[i][j];
			return *this;
		}
		NuclearMatrix3& operator *= ( float k)
		{
			int i, j;
			for (i=0;i<3;++i)
				for (j=0;j<3;++j)
					m[i][j] *= k;
			return *this;
		}
		NuclearMatrix3& operator /= ( float k)
		{
			(*this) *= (1/k);
			return *this;
		}

		NuclearMatrix3 operator + () const { return *this; }
		NuclearMatrix3 operator - () const 
		{
			NuclearMatrix3 result = (*this);
			result *= -1.0f;
			return result;
		}

		NuclearMatrix3 operator * ( const NuclearMatrix3& b) const
		{
			NuclearMatrix3 result;
			result.m[0][0] = m[0][0] * b.m[0][0]+m[0][1] * b.m[1][0]+m[0][2] * b.m[2][0];
			result.m[0][1] = m[0][0] * b.m[0][1]+m[0][1] * b.m[1][1]+m[0][2] * b.m[2][1];
			result.m[0][2] = m[0][0] * b.m[0][2]+m[0][1] * b.m[1][2]+m[0][2] * b.m[2][2];
			result.m[1][0] = m[1][0] * b.m[0][0]+m[1][1] * b.m[1][0]+m[1][2] * b.m[2][0];
			result.m[1][1] = m[1][0] * b.m[0][1]+m[1][1] * b.m[1][1]+m[1][2] * b.m[2][1];
			result.m[1][2] = m[1][0] * b.m[0][2]+m[1][1] * b.m[1][2]+m[1][2] * b.m[2][2];
			result.m[2][0] = m[2][0] * b.m[0][0]+m[2][1] * b.m[1][0]+m[2][2] * b.m[2][0];
			result.m[2][1] = m[2][0] * b.m[0][1]+m[2][1] * b.m[1][1]+m[2][2] * b.m[2][1];
			result.m[2][2] = m[2][0] * b.m[0][2]+m[2][1] * b.m[1][2]+m[2][2] * b.m[2][2];
			return result;
		}

		void Translation(NuclearMatrix3 &result, const NuclearFPoint &fp) const
		{
			Translation(result, fp.x, fp.y);
		}

		void Translation(NuclearMatrix3 &result, float x, float y) const
		{
			result.m[0][0] = m[0][0];
			result.m[0][1] = m[0][1];
			result.m[0][2] = m[0][2];
			result.m[1][0] = m[1][0];
			result.m[1][1] = m[1][1];
			result.m[1][2] = m[1][2];
			result.m[2][0] = x * m[0][0]+y * m[1][0]+ m[2][0];
			result.m[2][1] = x * m[0][1]+y * m[1][1]+ m[2][1];
			result.m[2][2] = x * m[0][2]+y * m[1][2]+ m[2][2];
		}

		NuclearMatrix3 operator + ( const NuclearMatrix3& b) const
		{
			NuclearMatrix3 result = (*this);
			result += b;
			return result;
		}
		NuclearMatrix3 operator - ( const NuclearMatrix3& b) const
		{
			NuclearMatrix3 result = (*this);
			result -= b;
			return result;
		}
		NuclearMatrix3 operator * ( float k) const
		{
			NuclearMatrix3 result = (*this);
			result *= k;
			return result;
		}
		NuclearMatrix3 operator / ( float k) const
		{
			NuclearMatrix3 result = (*this);
			result /= k;
			return result;
		}

		friend NuclearMatrix3 operator * ( float, const NuclearMatrix3& );

		BOOL operator == ( const NuclearMatrix3& b) const { return !((*this) != b); }

		BOOL operator != ( const NuclearMatrix3& b) const
		{
			int i, j;
			for (i=0;i<3;++i)
				for (j=0;j<3;++j)
					if (fabs(m[i][j] - b.m[i][j]) > FIRE_EPSILON)
						return TRUE;
			return FALSE;
		}

		//逆矩阵
		NuclearMatrix3 *inverse (NuclearMatrix3 *pOut) const
		{
#if _DEBUG
			if (pOut == NULL)
				return NULL;
#endif
			float det = _11 * (_22 * _33 - _23 * _32)
				+ _12 * (_23 * _31 - _21 * _33)
				+ _13 * (_21 * _32 - _22 * _31);//行列式
			if (fabs(det) < FIRE_EPSILON)
			{
				return NULL;
			}
			float oneOverDet = 1.0f / det;

			pOut->_11 = (_22 * _33 - _23 * _32) * oneOverDet;
			pOut->_12 = (_13 * _32 - _12 * _33) * oneOverDet;
			pOut->_13 = (_12 * _23 - _13 * _22) * oneOverDet;

			pOut->_21 = (_23 * _31 - _21 * _33) * oneOverDet;
			pOut->_22 = (_11 * _33 - _13 * _31) * oneOverDet;
			pOut->_23 = (_13 * _21 - _11 * _23) * oneOverDet;

			pOut->_31 = (_21 * _32 - _22 * _31) * oneOverDet;
			pOut->_32 = (_12 * _31 - _11 * _32) * oneOverDet;
			pOut->_33 = (_11 * _22 - _12 * _21) * oneOverDet;

			return pOut;
		}

	} NuclearMatrix3, *LPXPMATRIX3;
	NuclearMatrix3 inline operator * ( float k , const NuclearMatrix3& m)
	{
		NuclearMatrix3 result = m;
		result *= k;
		return result;
	}

	//pOut和pV不能是同一个向量
	NuclearVector2 inline *XPVec3TransformCoord(NuclearVector2 *pOut, const NuclearVector2 *pV, const NuclearMatrix3 *pM)
	{
#if _DEBUG
		if (pOut == NULL)
			return NULL;
#endif
		pOut->x = pV->x * pM->_11 + pV->y * pM->_21 + pM->_31; 
		pOut->y = pV->x * pM->_12 + pV->y * pM->_22 + pM->_32;
		float w = pV->x * pM->_13 + pV->y * pM->_23 + pM->_33;
		pOut->x /= w;
		pOut->y /= w;
		return pOut;
	}

	//pOut和pV不能是同一个向量
	NuclearVector2 inline *XPVec3TransformNormal(NuclearVector2 *pOut, const NuclearVector2 *pV, const NuclearMatrix3 *pM)
	{
#if _DEBUG
		if (pOut == NULL)
			return NULL;
#endif
		pOut->x = pV->x * pM->_11 + pV->y * pM->_21; 
		pOut->y = pV->x * pM->_12 + pV->y * pM->_22;
		return pOut;
	}	
	
	//矩阵的初始化,零矩阵
	NuclearMatrix3 inline *XPMatrixZero(NuclearMatrix3 *pOut)
	{
#if _DEBUG
		if (pOut == NULL)
			return NULL;
#endif
		pOut->_11 = 0.0f;
		pOut->_12 = 0.0f;
		pOut->_13 = 0.0f;
		pOut->_21 = 0.0f;
		pOut->_22 = 0.0f;
		pOut->_23 = 0.0f;
		pOut->_31 = 0.0f;
		pOut->_32 = 0.0f;
		pOut->_33 = 0.0f;
		return pOut;
	}

	//单位矩阵
	NuclearMatrix3 inline *XPMatrixIdentity(NuclearMatrix3 *pOut)
	{
#if _DEBUG
		if (pOut == NULL)
			return NULL;
#endif
		XPMatrixZero(pOut);
		pOut->_11 = 1.0f;
		pOut->_22 = 1.0f;
		pOut->_33 = 1.0f;
		return pOut;
	}

	//旋转
	NuclearMatrix3 inline *XPMatrixRotationPoint(NuclearMatrix3 *pOut, const NuclearFPoint *pP, float radian)
	{
#if _DEBUG
		if (pOut == NULL)
			return NULL;
#endif
		float cosTheta, sinTheta;
		XPSinCos(radian, &sinTheta, &cosTheta);
		pOut->_11 = cosTheta;		//cos				sin				0
		pOut->_12 = sinTheta;		//-sin				cos				0
		pOut->_13 = 0;				//-x.cos+y.sin+x.	-x.sin-y.cos+y.	1
		pOut->_21 = -sinTheta;
		pOut->_22 = cosTheta;
		pOut->_23 = 0;
		if (pP)
		{
			pOut->_31 = -pP->x * cosTheta + pP->y * sinTheta + pP->x;
			pOut->_32 = -pP->x * sinTheta - pP->y * cosTheta + pP->y;
		} else {
			pOut->_31 = 0;
			pOut->_32 = 0;
		}
		pOut->_33 = 1;
		return pOut;
	}

	NuclearMatrix3 inline *XPMatrixRotationOrigin(NuclearMatrix3 *pOut, float radian)
	{
		return XPMatrixRotationPoint(pOut, NULL, radian);
	}

	//平移
	NuclearMatrix3 inline *XPMatrixTranslation(NuclearMatrix3 *pOut, float x, float y)
	{
#if _DEBUG
		if (pOut == NULL)
			return NULL;
#endif
		XPMatrixIdentity(pOut);
		pOut->_31 = x;		//1   0	  0
		pOut->_32 = y;		//0	  1   0
		return pOut;		//dx  dy  1
	}

	NuclearMatrix3 inline *XPMatrixTranslation(NuclearMatrix3 *pOut, const NuclearFPoint *pP)
	{
#if _DEBUG
		if (pP == NULL)
			return NULL;
#endif
		return XPMatrixTranslation(pOut, pP->x, pP->y);
	}

	//错切（切变）
	NuclearMatrix3 inline *XPMatrixShearX(NuclearMatrix3 *pOut, const NuclearFPoint *pP, float s)
	{
#if _DEBUG
		if (pOut == NULL)
			return NULL;
#endif
		XPMatrixIdentity(pOut);	// 1  0 0
		pOut->_21 = s;			// s  1 0
		if (pP)					//-sy 0 1
			pOut->_31 = -s * pP->y;
		return pOut;
	}

	NuclearMatrix3 inline *XPMatrixShearX(NuclearMatrix3 *pOut, float s)
	{
		return XPMatrixShearX(pOut, NULL, s);
	}

	NuclearMatrix3 inline *XPMatrixShearY(NuclearMatrix3 *pOut, const NuclearFPoint *pP, float s)
	{
#if _DEBUG
		if (pOut == NULL)
			return NULL;
#endif
		XPMatrixIdentity(pOut);	//1  s  0
		pOut->_12 = s;			//0  1  0
		if (pP)					//0 -sx 1
			pOut->_32 = -s * pP->x;
		return pOut;
	}

	NuclearMatrix3 inline *XPMatrixShearY(NuclearMatrix3 *pOut, float s)
	{
		return XPMatrixShearY(pOut, NULL, s);
	}

	//缩放
	NuclearMatrix3 inline *XPMatrixScaling(NuclearMatrix3 *pOut, const NuclearFPoint *pP, float sx, float sy)
	{
#if _DEBUG
		if (pOut == NULL)
			return NULL;
#endif
		XPMatrixZero(pOut);	//  sx      0      0
		pOut->_11 = sx;		//   0      sy     0
		pOut->_22 = sy;		//a(1-sx) b(1-sy)  1
		if (pP)
		{
			pOut->_31 = pP->x * (1.0f - sx);
			pOut->_32 = pP->y * (1.0f - sy);
		}
		pOut->_33 = 1;
		return pOut;

	}

	NuclearMatrix3 inline *XPMatrixScaling(NuclearMatrix3 *pOut, float sx, float sy)
	{
		return XPMatrixScaling(pOut, NULL, sx, sy);
	}

	NuclearMatrix3 inline *XPMatrixMirrorY(NuclearMatrix3 *pOut)
	{
		return XPMatrixScaling(pOut, NULL, -1.0f, 1.0f);
	}

	class Renderer;
	union NuclearColor;
    enum NuclearFillMode
	{
		XPFM_WIREFRAME = 0,
		XPFM_SOLIDFILL = 1
	};
    
	enum NuclearShapeType
	{
		XPSHAPE_TYPE_CIRCLE = 0,
		XPSHAPE_TYPE_POLYGON,
	};

	class INuclearShape
	{
	public:
		INuclearShape() {}
		virtual ~INuclearShape() {}
		virtual bool IsPointIn(int x, int y) const = 0;
		bool IsPointIn(const NuclearPoint &pt) const { return IsPointIn(pt.x, pt.y); }
		virtual float DistanceTo(int x, int y) const = 0;//这个是到边界的距离，正负无定义，目前，如果是圆，那么点在内部的话则为负，如果是多边形，不管内外均为正
		float DistanceTo(const NuclearPoint &pt) const { return DistanceTo(pt.x, pt.y); }
		virtual void Render(Renderer *pRenderer, const NuclearRect &viewport, NuclearColor color, NuclearFillMode fillmode, int pointR) const = 0;
		virtual NuclearShapeType GetType() const = 0;
		virtual INuclearShape* Extension(int l) const = 0;
		virtual bool IsVaild()	const = 0;//是否合法
	};

	class NuclearCircle : public INuclearShape
	{
	public:
		NuclearPoint	m_pos;		//圆心的位置
		int		m_nRadius;	//半径
	public:
		NuclearCircle(NuclearPoint	pos = NuclearPoint(0, 0), int r = 0) : m_pos(pos), m_nRadius(r) {}
		virtual NuclearShapeType GetType() const { return XPSHAPE_TYPE_CIRCLE; }
		virtual bool IsPointIn(int x, int y) const { return DistanceTo(x, y) < 0.0f; }
		virtual float DistanceTo(int x, int y) const
		{
			float _x = static_cast<float>(x - m_pos.x);
			float _y = static_cast<float>(y - m_pos.y);
			return sqrtf(_x*_x+_y*_y) - m_nRadius;
		}
		virtual void Render(Renderer *pRenderer, const NuclearRect &viewport, NuclearColor color, NuclearFillMode fillmode, int pointR) const;
		virtual INuclearShape* Extension(int l) const { return new NuclearCircle(m_pos, m_nRadius + l); }
		virtual bool IsVaild() const { return m_nRadius > 0; } //是否合法
	};

	class NuclearPolygon : public INuclearShape
	{
	public:
		std::vector<NuclearPoint> m_points;	//连续的顶点
	public:
		NuclearPolygon() {}
		virtual NuclearShapeType GetType() const { return XPSHAPE_TYPE_POLYGON; }
		virtual bool IsPointIn(int x, int y) const 
		{
			const std::vector<NuclearPoint>::size_type n = m_points.size();
			if(n < 2)
				return false;	//如果只有一个点或者没有点，肯定是false

			bool ret = false;
			NuclearPoint a = m_points[n - 1];
			for(std::vector<NuclearPoint>::size_type i = 0; i < n; ++i)
			{
				const NuclearPoint &b = m_points[i];
				if((b.x < x) == (x <= a.x))
					ret ^= ((a.x < b.x) == ((y - a.y) * (b.x - a.x) < (x - a.x) * (b.y - a.y)));
				a = b;
			}
			return ret;
		}
		virtual float DistanceTo(int x, int y) const;
		virtual void Render(Renderer *pRenderer, const NuclearRect &viewport, NuclearColor color, NuclearFillMode fillmode, int pointR) const;
		virtual INuclearShape* Extension(int l) const;
		virtual bool IsVaild() const { return m_points.size() > 2; } //是否合法
	};

	RECT GetMaxRect(const RECT &r, float s);

	bool IsPower2(int i);
	bool PartitionRectToPower2(const NuclearRect &src, std::vector<NuclearRect> &dst);
	
	//这个函数把一个点从图片的直角坐标系（原点在左上角）中转换为在菱形的坐标系中。
	//其中，基单位向量i在直角坐标系为(1, cos(58))，基单位向量j为(-1, cos(58))
	void TransToDiamondRadix(int x, int y, NuclearPoint& pointInDiamonRadix);

	template<class T>
	bool IsRectIncisionVertically(const T &r1, const T &r2)
	{
		if (r1.top < r2.top && r1.bottom > r2.bottom && r1.left > r2.left && r1.right < r2.right)
			return true;
		return false;
	}

	template<class T>
	bool IsRectIncision(const T &r1, const T &r2)
	{
		if (!IsRectCross<T>(r1, r2))
			return false;
		if (IsRectIncisionVertically<T>(r1, r2) || IsRectIncisionVertically<T>(r2, r1))
			return true;
		return false;
	}

	// 两点距离
	inline float distance(const NuclearFPoint & from, const NuclearFPoint & to)
	{
		return ::sqrt( ( from.x - to.x ) * ( from.x - to.x ) + ( from.y - to.y ) * ( from.y - to.y ) );
	}

	// 两点距离平方
	inline float distance2(const NuclearFPoint & from, const NuclearFPoint & to)
	{
		return ( from.x - to.x ) * ( from.x - to.x ) + ( from.y - to.y ) * ( from.y - to.y );
	}

	//向量点积
	inline float DotProduct(const NuclearVector2& v0, const NuclearVector2& v1)
	{
		return v0.x * v1.x + v0.y * v1.y;
	}

	//向量长度
	inline float VectorLength(const NuclearVector2& v)
	{
		return sqrt(v.x * v.x + v.y * v.y);
	}

	//两向量的夹角
	inline float Computer2VectorAngle(const NuclearVector2& v1, const NuclearVector2& v2)
	{
		float fcosangle = DotProduct(v1, v2)/(VectorLength(v1)*VectorLength(v2));
		return acos(fcosangle);
	}
}

#endif