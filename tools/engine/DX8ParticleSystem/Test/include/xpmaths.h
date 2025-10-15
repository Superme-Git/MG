#ifndef __Nuclear_XPMATHS_H
#define __Nuclear_XPMATHS_H

#include <math.h>
#include <vector>
#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	extern const float XP_SIN[91];

	const float XP_EPSILON = 0.00001f;
	const float XP_PI	= 3.14159265f;


	//根据角度获得弧度PI/180.0f = 0.01745
	inline float TORADIANS(float p) { return p * 0.01745f; }
	//根据弧度获得角度   180.0/PI = 57.29583
	inline float TOANGLE(float r) { return r * 57.29583f; }

	inline float XPSIN(int angle) //角度
	{
		//先把角度转化到 0---360 之间
		int cnt = angle/360;
		if(cnt < 0)
			angle += (-cnt+1)*360;
		else if (cnt == 0)
		{

			if(angle < 0)
				angle += 360;
		}
		else
			angle -= cnt*360;

		if(angle < 91)
		{ //1
			return XP_SIN[angle];
		}
		else if (angle < 181)
		{ //2
			return XP_SIN[180-angle];
		}
		else if (angle < 271)
		{ //3
			return -XP_SIN[angle-180];
		}
		else
		{ //4
			return -XP_SIN[360-angle];
		}

	}

	inline float XPCOS(int angle) //角度
	{
		int cnt = angle/360;
		if(cnt < 0)
			angle += (-cnt+1)*360;
		else if (cnt == 0)
		{
			if(angle < 0)
				angle += 360;
		}
		else
			angle -= cnt*360;

		if(angle < 91)
		{ //1
			return XP_SIN[90-angle];
		}
		else if (angle < 181)
		{ //2
			return -XP_SIN[angle-90];
		}
		else if (angle < 271)
		{ //3
			return -XP_SIN[270-angle];
		}
		else
		{ //4
			return XP_SIN[angle-270];
		}

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
		return v1 + ( v2 - v1) * s;
	}

	struct UTCT1VERTEX
	{
		float x,y,z;
		DWORD c;
		float u,v;
		UTCT1VERTEX()
		{
			x = 0.0f;
			y = 0.0f;
			z = 0.0f;
			c = 0xffffffff;
			u = 0.0f;
			v = 0.0f;
		}
	};

	struct TCT1VERTEX
	{
		float x,y,z;
		float rhw;
		DWORD c;
		float u,v;
		TCT1VERTEX()
		{
			x = 0.0f;
			y = 0.0f;
			z = 0.0f;
			rhw = 1.0f;
			c = 0xffffffff;
			u = 0.0f;
			v = 0.0f;
		}
	};

	struct TCVERTEX
	{
		float x,y,z;
		float rhw;
		DWORD c;
		TCVERTEX()
		{
			x = 0.0f;
			y = 0.0f;
			z = 0.0f;
			rhw = 1.0f;
			c = 0xffffffff;
		}
	};

	struct XPVECTOR3
	{
		float x;
		float y;
		float z;
		XPVECTOR3 (float _x=0.0f, float _y=0.0f, float _z=0.0f) : x(_x), y(_y), z(_z) {};
		XPVECTOR3& operator += (const XPVECTOR3 &v) { x+=v.x;y+=v.y;z+=v.z;return *this;}
		XPVECTOR3& operator -= (const XPVECTOR3 &v) { x-=v.x;y-=v.y;z-=v.z;return *this;}
		XPVECTOR3& operator *= (float s) { x*=s;y*=s;z*=s;return *this;}
		XPVECTOR3& operator /= (float s) { x/=s;y/=s;z/=s;return *this;}
		XPVECTOR3 operator + (const XPVECTOR3 &v) const { return XPVECTOR3(x+v.x, y+v.y, z+v.z);}
		XPVECTOR3 operator - (const XPVECTOR3 &v) const { return XPVECTOR3(x-v.x, y-v.y, z-v.z);}
		XPVECTOR3 operator * (float s) const { return XPVECTOR3(x*s, y*s, z*s);}
		XPVECTOR3 operator / (float s) const { return XPVECTOR3(x/s, y/s, z/s);}
		XPVECTOR3& Reset(float _x,float _y,float _z)  { x = _x; y = _y; z = _z; return *this; }
		XPVECTOR3& Add(float _x,float _y,float _z)  { x += _x; y += _y; z += _z; return *this; }
		float Length() const { return sqrtf(x*x+y*y+z*z); }
	};

	struct XPVECTOR4
	{
		float x;
		float y;
		float z;
		float w;
		XPVECTOR4 (float _x=0.0f, float _y=0.0f, float _z=0.0f, float _w=0.0f) : x(_x), y(_y), z(_z), w(_w) {};
		XPVECTOR4 &operator += (const XPVECTOR4 &v) { x+=v.x;y+=v.y;z+=v.z;w+=v.w;return *this;}
		XPVECTOR4 &operator -= (const XPVECTOR4 &v) { x-=v.x;y-=v.y;z-=v.z;w-=v.w;return *this;}
		XPVECTOR4 &operator *= (const float s) { x*=s;y*=s;z*=s;w*=s;return *this;}
		XPVECTOR4 &operator /= (const float s) { x/=s;y/=s;z/=s;w/=s;return *this;}
		XPVECTOR4 operator + (const XPVECTOR4 &v) const { return XPVECTOR4(x+v.x, y+v.y, z+v.z, w+v.w);}
		XPVECTOR4 operator - (const XPVECTOR4 &v) const { return XPVECTOR4(x-v.x, y-v.y, z-v.z, w-v.w);}
		XPVECTOR4 operator * (const float s) const { return XPVECTOR4(x*s, y*s, z*s, w*s);}
		XPVECTOR4 operator / (const float s) const { return XPVECTOR4(x/s, y/s, z/s, w/s);}
	};

	struct XPFCOLOR
	{
		float r,g,b,a;
		XPFCOLOR(float _r=0.f,float _g=0.f,float _b=0.f,float _a=0.f) : r(_r), g(_g), b(_b), a(_a) { }
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

	typedef struct FPOINT
	{
		float x;
		float y;
		FPOINT(float _x = 0.f, float _y = 0.f) : x(_x), y(_y) { }
		FPOINT(const POINT& p) : x(static_cast<float>(p.x)), y(static_cast<float>(p.y)) { }

		FPOINT& operator=(const POINT &p) { x=static_cast<float>(p.x); y=static_cast<float>(p.y); return *this; }
		FPOINT operator+(const FPOINT &fp) const { return FPOINT(x+fp.x, y+fp.y); }
		FPOINT& operator+=(const FPOINT &fp) { x+=fp.x; y+=fp.y; return *this; }
		FPOINT operator-(const FPOINT &fp) const { return FPOINT(x-fp.x, y-fp.y); }
		FPOINT& operator-=(const FPOINT &fp) { x-=fp.x; y-=fp.y; return *this; }
		FPOINT operator * ( float k) const
		{
			FPOINT result = *this;
			result *= k;
			return result;
		}
		FPOINT operator / ( float k) const
		{
			FPOINT result = *this;
			result /= k;
			return result;
		}
		FPOINT& operator *= ( float k)
		{
			x *= k;
			y *= k;
			return *this;
		}

		FPOINT& operator /= ( float k)
		{
			return (*this) *= (1/k);
		}

		bool operator==(const FPOINT &fp) const
		{
			return ( fabs(x-fp.x)<XP_EPSILON ) && ( fabs(y-fp.y)<XP_EPSILON );
		}

		float Dis2(const FPOINT &cp) const { return (x-cp.x)*(x-cp.x)+(y-cp.y)*(y-cp.y); }


		//判断点是否在凸多边形内
		bool isInPolygon(const std::vector<FPOINT>& polygon) const
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

	private:
		bool isInTriangle(FPOINT p0, FPOINT p1, FPOINT p2) const;


	} XPVECTOR2;

	FPOINT inline operator * ( float k, const FPOINT& v)
	{
		FPOINT result = v;
		result *= k;
		return result;
	}

	struct FRECT
	{
		float left;
		float top;
		float right;
		float bottom;

		FRECT(float l = 0.f, float t = 0.f, float r = 0.f, float b = 0.f) : left(l), top(t), right(r), bottom(b) { }
		FRECT(const FPOINT &ltpoint, float width, float height) { left = ltpoint.x; top = ltpoint.y; right = left + width; bottom = top + height; }
		FRECT(const FPOINT & lt, float size)        { left = lt.x; top = lt.y; right = left + size; bottom = top + size; }
		FRECT(const FPOINT & lt, const FPOINT & rb) { left = lt.x; top = lt.y; right = rb.x; bottom = rb.y; }

		FRECT& operator+=(const FPOINT &fp)     { left+=fp.x;right+=fp.x;top+=fp.y;bottom+=fp.y; return *this; }
		FRECT& operator-=(const FPOINT &fp)     { left-=fp.x;right-=fp.x;top-=fp.y;bottom-=fp.y; return *this; }
		FRECT& operator/=(float s)              { left/=s;right/=s;top/=s;bottom/=s; return *this; }
		FRECT& operator*=(float s)              { left*=s;right*=s;top*=s;bottom*=s; return *this; }

		FRECT operator+(const FPOINT &fp) const { return FRECT(left+fp.x, top+fp.y, right+fp.x, bottom+fp.y); }
		FRECT operator-(const FPOINT &fp) const { return FRECT(left-fp.x, top-fp.y, right-fp.x, bottom-fp.y); }
		FRECT operator/(float s) const          { return FRECT(left/s, top/s, right/s, bottom/s); }
		FRECT operator*(float s) const          { return FRECT(left*s, top*s, right*s, bottom*s); }

		bool operator==(const FRECT& f) const
		{ 
			return ( fabs(left-f.left)<XP_EPSILON ) && ( fabs(top-f.top)<XP_EPSILON ) && ( fabs(right-f.right)<XP_EPSILON ) && ( fabs(bottom-f.bottom)<XP_EPSILON );
		}

		FRECT& Assign(float l, float t, float r, float b) { left = l; top = t; right = r; bottom = b; return *this; }

		float Width() const { return right - left; }
		float Height() const { return bottom - top; }
		bool PtInRect(const FPOINT &pt) const { return pt.x >= left && pt.x < right && pt.y >= top && pt.y < bottom; }

	};

	typedef struct CPOINT : public POINT
	{
		CPOINT(int _x=0, int _y=0) { x = _x; y = _y; }
		CPOINT(const POINT &pt) { x = pt.x; y = pt.y; }
		CPOINT(const FPOINT &pt) { x = (int)pt.x; y=(int)pt.y;  }
		CPOINT operator-(const CPOINT &cp) const { return CPOINT(x-cp.x, y-cp.y); }
		CPOINT& operator-=(const CPOINT &cp) { x-=cp.x; y-=cp.y; return *this; }
		CPOINT operator+(const CPOINT &cp) const { return CPOINT(x+cp.x, y+cp.y); }
		CPOINT& operator+=(const CPOINT &cp) { x+=cp.x; y+=cp.y; return *this; }
		bool operator==(const CPOINT &l) const { return x == l.x && y == l.y; }
		bool operator!=(const CPOINT &l) const { return !operator==(l); }
		int Dis2(const CPOINT &cp) const { return (x-cp.x)*(x-cp.x)+(y-cp.y)*(y-cp.y); }
		FPOINT ToFPOINT() const { return FPOINT((float)x,(float)y); }
	} CSIZE, COFFSET, Location;

	struct CRECT : public RECT
	{
		CRECT(int l=0, int t=0, int r=0, int b=0) { left=l; top=t; right=r; bottom=b; }
		CRECT(const CPOINT &ltpoint, int width, int height) { left = ltpoint.x; top = ltpoint.y; right = left + width; bottom = top + height; }
		CRECT(const RECT &r) { left =r.left; top =r.top; right =r.right; bottom =r.bottom; }

		bool operator==(const CRECT &r) const { return (left == r.left && right == r.right && top == r.top && bottom == r.bottom);}

		CRECT operator+(const CPOINT &fp) const { return CRECT(left+fp.x, top+fp.y, right+fp.x, bottom+fp.y); }
		CRECT& operator+=(const CPOINT &fp) { left+=fp.x;right+=fp.x;top+=fp.y;bottom+=fp.y; return *this; }
		CRECT operator-(const CPOINT &fp) const { return CRECT(left-fp.x, top-fp.y, right-fp.x, bottom-fp.y); }
		CRECT& operator-=(const CPOINT &fp) { left-=fp.x;right-=fp.x;top-=fp.y;bottom-=fp.y; return *this; }

		CRECT operator/(int d) const { return CRECT(left/d, top/d, right/d, bottom/d); }

		CRECT Widen(int d) const { return CRECT(left-d,top-d,right+d,bottom+d); }
		CRECT Union(const CRECT &rhs) { return CRECT(left<rhs.left?left:rhs.left,top<rhs.top?top:rhs.top,right>rhs.right?right:rhs.right,bottom>rhs.bottom?bottom:rhs.bottom); }

		CPOINT GetPos() const { return CPOINT(left,top); }
		CPOINT Center() const { return CPOINT((left+right)/2,(top+bottom)/2); }
		FRECT ToFRECT() const { return FRECT((float)left,(float)top,(float)right,(float)bottom); }

		//CRECT& operator/=(float s) { left/=s;right/=s;top/=s;bottom/=s; return *this; }
		CRECT& operator=(const RECT &r) { left = r.left; right = r.right; top = r.top; bottom = r.bottom; return *this; }

		CRECT& Assign(int l, int t, int r, int b) { left = l; top = t; right = r; bottom = b; return *this; }


		int Width() const { return right - left; }
		int Height() const { return bottom - top; }
		bool PtInRect(const CPOINT &pt) const { return pt.x >= left && pt.x < right && pt.y >= top && pt.y < bottom; }

		// 计算相交情况
		bool IsCross(const CRECT& rhs, CRECT &res) const
		{
			if( left >= rhs.right || right <= rhs.left || top >= rhs.bottom || bottom <= rhs.top )
				return false;
			res.left = left > rhs.left ? left : rhs.left;
			res.right = right < rhs.right ? right : rhs.right;
			res.top = top > rhs.top ? top : rhs.top;
			res.bottom = bottom < rhs.bottom ? bottom : rhs.bottom;
			return true;
		}
		// TODO 根据视窗切割出需要显示的部分
		bool Cut(const CRECT& vrect, CRECT &drect, FRECT& srect) const
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

	struct ELLIPSE
	{
		int x0;
		int y0;
		int rx;
		int ry;
		ELLIPSE() : x0(0), y0(0), rx(0), ry(0) { }
		bool PtInEllipse(const CPOINT &pt) const { return ry*ry*(pt.x-x0)*(pt.x-x0) + rx*rx*(pt.y-y0)*(pt.y-y0) < rx*rx*ry*ry; }
	};

	typedef struct XPMATRIX3
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
		XPMATRIX3() {
			int i, j;
			for (i=0;i<3;++i)
			{
				for (j=0;j<3;++j)
				{
					m[i][j] = 0;
				}
			}
		};
		//D3DXMATRIX( const float * );
		XPMATRIX3( float _11, float _12, float _13,
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


		//// access grants, 从(1,1)到(3,3)
		//float& operator () ( UINT Row, UINT Col ) {	return m[Row-1][Col-1]; }
		//float operator () ( UINT Row, UINT Col ) const { return m[Row-1][Col-1]; }

		// casting operators
		operator float* () { return m[0]; }
		operator const float* () const { return m[0]; }

		// assignment operators
		XPMATRIX3& operator *= ( const XPMATRIX3& b) { (*this) = (*this) * b; return (*this); }
		XPMATRIX3& operator += ( const XPMATRIX3& b)
		{
			int i, j;
			for (i=0;i<3;++i)
				for (j=0;j<3;++j)
					m[i][j] += b.m[i][j];
			return *this;
		}
		XPMATRIX3& operator -= ( const XPMATRIX3& b)
		{
			int i, j;
			for (i=0;i<3;++i)
				for (j=0;j<3;++j)
					m[i][j] -= b.m[i][j];
			return *this;
		}
		XPMATRIX3& operator *= ( float k)
		{
			int i, j;
			for (i=0;i<3;++i)
				for (j=0;j<3;++j)
					m[i][j] *= k;
			return *this;
		}
		XPMATRIX3& operator /= ( float k)
		{
			(*this) *= (1/k);
			return *this;
		}

		// unary operators
		XPMATRIX3 operator + () const { return *this; }
		XPMATRIX3 operator - () const {
			XPMATRIX3 result = (*this);
			result *= -1.0f;
			return result;
		}

		// binary operators
		XPMATRIX3 operator * ( const XPMATRIX3& b) const
		{
			XPMATRIX3 result;
			int i, j, k;
			for (i=0;i<3;++i)
				for (j=0;j<3;++j)
					for (k=0;k<3;++k)
						result.m[i][j] += m[i][k] * b.m[k][j];
			return result;
		}
		XPMATRIX3 operator + ( const XPMATRIX3& b) const
		{
			XPMATRIX3 result = (*this);
			result += b;
			return result;
		}
		XPMATRIX3 operator - ( const XPMATRIX3& b) const
		{
			XPMATRIX3 result = (*this);
			result -= b;
			return result;
		}
		XPMATRIX3 operator * ( float k) const
		{
			XPMATRIX3 result = (*this);
			result *= k;
			return result;
		}
		XPMATRIX3 operator / ( float k) const
		{
			XPMATRIX3 result = (*this);
			result /= k;
			return result;
		}

		friend XPMATRIX3 operator * ( float, const XPMATRIX3& );

		BOOL operator == ( const XPMATRIX3& b) const { return !((*this) != b); }

		BOOL operator != ( const XPMATRIX3& b) const
		{
			int i, j;
			for (i=0;i<3;++i)
				for (j=0;j<3;++j)
					if (fabs(m[i][j] - b.m[i][j]) > XP_EPSILON)
						return TRUE;
			return FALSE;
		}

		//逆矩阵
		XPMATRIX3 *inverse (XPMATRIX3 *pOut) const
		{
#if _DEBUG
			if (pOut == NULL)
				return NULL;
#endif
			float det = _11 * (_22 * _33 - _23 * _32)
				+ _12 * (_23 * _31 - _21 * _33)
				+ _13 * (_21 * _32 - _22 * _31);//行列式
			if (fabs(det) < XP_EPSILON)
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

	} XPMATRIX3, *LPXPMATRIX3;
	XPMATRIX3 inline operator * ( float k , const XPMATRIX3& m)
	{
		XPMATRIX3 result = m;
		result *= k;
		return result;
	}

	//pOut和pV不能是同一个向量
	XPVECTOR2 *XPVec3TransformCoord(XPVECTOR2 *pOut, const XPVECTOR2 *pV, const XPMATRIX3 *pM);
	//pOut和pV不能是同一个向量
	XPVECTOR2 *XPVec3TransformNormal(XPVECTOR2 *pOut, const XPVECTOR2 *pV, const XPMATRIX3 *pM);
	
	//====================================矩阵的初始化
	//零矩阵
	XPMATRIX3 inline *XPMatrixZero(XPMATRIX3 *pOut)
	{
#if _DEBUG
		if (pOut == NULL)
			return NULL;
#endif
		ZeroMemory(pOut, sizeof(float) * 9);
		return pOut;
	}

	//单位矩阵
	XPMATRIX3 inline *XPMatrixIdentity(XPMATRIX3 *pOut)
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
	XPMATRIX3 *XPMatrixRotationPoint(XPMATRIX3 *pOut, const FPOINT *pP, float radian);
	XPMATRIX3 inline *XPMatrixRotationOrigin(XPMATRIX3 *pOut, float radian)
	{
		return XPMatrixRotationPoint(pOut, NULL, radian);
	}

	//平移
	XPMATRIX3 *XPMatrixTranslation(XPMATRIX3 *pOut, float x, float y);
	XPMATRIX3 inline *XPMatrixTranslation(XPMATRIX3 *pOut, const FPOINT *pP)
	{
#if _DEBUG
		if (pP == NULL)
			return NULL;
#endif
		return XPMatrixTranslation(pOut, pP->x, pP->y);
	}

	//错切（切变）
	XPMATRIX3 *XPMatrixShearX(XPMATRIX3 *pOut, float s);
	XPMATRIX3 *XPMatrixShearY(XPMATRIX3 *pOut, float s);

	//缩放
	XPMATRIX3 *XPMatrixScaling(XPMATRIX3 *pOut, const FPOINT *pP, float sx, float sy);
	XPMATRIX3 inline *XPMatrixScaling(XPMATRIX3 *pOut, float sx, float sy)
	{
		return XPMatrixScaling(pOut, NULL, sx, sy);
	}

	XPMATRIX3 inline *XPMatrixMirrorY(XPMATRIX3 *pOut)
	{
		return XPMatrixScaling(pOut, NULL, -1.0f, 1.0f);
	}
	
}



#endif