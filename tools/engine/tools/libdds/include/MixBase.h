#ifndef __MIX_BASE_H
#define __MIX_BASE_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <assert.h> // for assert()

#pragma warning( push)
#pragma warning( disable : 4244)

namespace MIX
{

typedef unsigned int	uint_t;
typedef unsigned short	ushort_t;
typedef unsigned char	uchar_t;
typedef __int64			int64_t;
typedef unsigned __int64	uint64_t;

enum PixelFormat
{
	PIX_FMT_UNKNOWN = -1,
	PIX_FMT_R5G6B5,
	PIX_FMT_A4R4G4B4,
	PIX_FMT_R8G8B8,
	PIX_FMT_A8R8G8B8,
	PIX_FMT_X8R8G8B8,
	PIX_FMT_A8,
	PIX_FMT_A1R5G5B5,
	PIX_FMT_X1R5G5B5,

	PIX_FMT_DXT1,
	PIX_FMT_DXT1a,
	PIX_FMT_DXT3,
	PIX_FMT_DXT5,
	PIX_FMT_DXT5n,
};

struct PixelData8
{
	unsigned char		data;

	PixelData8() {}
	PixelData8( unsigned char c)
		: data( c)
	{}
};

struct PixelData565
{
	union
	{
		unsigned short		data;
		struct  
		{
			unsigned char	b : 5;
			unsigned char	g : 6;
			unsigned char	r : 5;
		};
	};

	PixelData565() {}
	PixelData565( unsigned short c)
		: data( c)
	{}

	enum
	{
		BLUE_MASK	= 0x001F,
		GREEN_MASK	= 0x07E0,
		RED_MASK	= 0xF800,
	};

	//! 注：取出的值都是放在byte的低位的
	uchar_t GetB() const { return (data & BLUE_MASK);}
	uchar_t GetG() const { return (data & GREEN_MASK) >> 5;}
	uchar_t GetR() const { return (data & RED_MASK) >> 11;}

	//! 注：取出的值放到高位
	uchar_t GetBH() const { return GetB() << 3;}
	uchar_t GetGH() const { return GetG() << 2;}
	uchar_t GetRH() const { return GetR() << 3;}

	//! 注：假设传入的值放在byte的高位
	void SetB( uchar_t _b) { data = ( data & ~BLUE_MASK)  | ( _b >> 3);}
	void SetG( uchar_t _g) { data = ( data & ~GREEN_MASK) | ( _g >> 2);}
	void SetR( uchar_t _r) { data = ( data & ~RED_MASK)	  | ( _r >> 3);}
};

struct PixelData4444
{
	union
	{
		unsigned short		data;
		struct
		{
			unsigned char	b : 4;
			unsigned char	g : 4;
			unsigned char	r : 4;
			unsigned char	a : 4;
		};
	};

	PixelData4444() {}
	PixelData4444( unsigned short c)
		: data( c)
	{}

	enum
	{
		BLUE_MASK	= 0x000F,
		GREEN_MASK	= 0x00F0,
		RED_MASK	= 0x0F00,
		ALPHA_MASK	= 0xF000,
	};	

	//! 注：取出的值都是放在byte的低位的
	uchar_t GetB() const { return (data & BLUE_MASK);}
	uchar_t GetG() const { return (data & GREEN_MASK) >> 4;}
	uchar_t GetR() const { return (data & RED_MASK)	>> 8;}
	uchar_t GetA() const { return (data & ALPHA_MASK) >> 12;}

	//! 注：取出的值放到高位
	uchar_t GetBH() const { return GetB() << 4;}
	uchar_t GetGH() const { return GetG() << 4;}
	uchar_t GetRH() const { return GetR() << 4;}
	uchar_t GetAH() const { return GetA() << 4;}

	//! 注：假设传入的值放在byte的高位
	void SetB( uchar_t _b) { data = ( data & ~BLUE_MASK)  | (_b >> 4);}
	void SetG( uchar_t _g) { data = ( data & ~GREEN_MASK) | (_g >> 4);}
	void SetR( uchar_t _r) { data = ( data & ~RED_MASK)   | (_r >> 4);}
	void SetA( uchar_t _a) { data = ( data & ~ALPHA_MASK) | (_a >> 4);}
};

struct PixelData5551
{
	union
	{
		unsigned short		data;
		struct
		{
			unsigned char	b : 5;
			unsigned char	g : 5;
			unsigned char	r : 5;
			unsigned char	a : 1;
		};
	};

	PixelData5551() {}
	PixelData5551( unsigned short c)
		: data( c)
	{}

	enum
	{
		BLUE_MASK	= 0x001F,
		GREEN_MASK	= 0x03E0,
		RED_MASK	= 0x7C00,
		ALPHA_MASK	= 0x8000,
	};	

	//! 注：取出的值都是放在byte的低位的
	uchar_t GetB() const { return (data & BLUE_MASK);}
	uchar_t GetG() const { return (data & GREEN_MASK) >> 4;}
	uchar_t GetR() const { return (data & RED_MASK)   >> 8;}
	uchar_t GetA() const { return (data & ALPHA_MASK) >> 12;}

	//! 注：取出的值放到高位
	uchar_t GetBH() const { return GetB() << 3;}
	uchar_t GetGH() const { return GetG() << 3;}
	uchar_t GetRH() const { return GetR() << 3;}
	uchar_t GetAH() const { return GetA() << 7;}


	//! 注：假设传入的值放在byte的高位
	void SetB( uchar_t _b) { data = ( data & ~BLUE_MASK)  | (_b >> 3);}
	void SetG( uchar_t _g) { data = ( data & ~GREEN_MASK) | (_g >> 3);}
	void SetR( uchar_t _r) { data = ( data & ~RED_MASK)   | (_r >> 3);}
	void SetA( uchar_t _a) { data = ( data & ~ALPHA_MASK) | (_a >> 7);}

};

#pragma pack(1)
struct PixelData24 
{
	union
	{
		unsigned int		data : 24;
		struct  
		{
			unsigned char	b;
			unsigned char	g;
			unsigned char	r;
		};
	};

	PixelData24() {}
	PixelData24( unsigned char param_b, unsigned char param_g, unsigned char param_r)
		: b( param_b), g( param_g), r(param_r)
	{}
};
#pragma pack()

struct PixelData32
{
	union
	{
		unsigned int		data;
		struct 
		{
			unsigned char	b;
			unsigned char	g;
			unsigned char	r;
			unsigned char	a;
		};
	};

	PixelData32() {}
	PixelData32( unsigned int c)
		: data( c)
	{}
	PixelData32( unsigned char param_b, unsigned char param_g, unsigned char param_r, unsigned char param_a)
		: b( param_b), g( param_g), r( param_r), a( param_a)
	{}

};

template<typename T>
struct tPoint
{
	T x;
	T y;

	tPoint() {}
	tPoint( T tx, T ty) : x(tx), y(ty) {}

	tPoint<T> operator +( const tPoint<T>& rhs) const
	{
		tPoint<T> result;
		result.x = x + rhs.x;
		result.y = y + rhs.y;
		return result;
	}

	tPoint<T> operator -( const tPoint<T>& rhs) const
	{
		tPoint<T> result;
		result.x = x - rhs.x;
		result.y = y - rhs.y;
		return result;
	}

	tPoint<T> operator *( T delta) const
	{
		tPoint<T> result;
		result.x = x * delta;
		result.y = y * delta;
		return result;
	}

	const tPoint<T>& operator += ( const tPoint<T>& rhs)
	{
		x += rhs.x;
		y += rhs.y;
		return *this;
	}

	const tPoint<T>& operator *= ( T delta)
	{
		x *= delta;
		y *= delta;
		return *this;
	}

	bool operator == ( const tPoint<T>& rhs)
	{
		return x == rhs.x && y == rhs.y;
	}
	bool operator != ( const tPoint<T>& rhs)
	{
		return x != rhs.x || y != rhs.y;
	}
};

typedef tPoint<int>		Point;
typedef tPoint<float>	Pointf;


template<typename T>
struct tSize
{
	T cx;
	T cy;

	tSize() {}
	tSize( T tcx, T tcy) : cx(tcx), cy(tcy) {}
};

typedef tSize<int>		Size;
typedef tSize<float>	Sizef;

template<typename T>
struct tRect
{
	T x;
	T y;
	T w;
	T h;

	tRect() {}
	tRect( T tx, T ty, T tw, T th) : x(tx), y(ty), w(tw), h(th){}

	T GetRight() const
	{
		return x + w; 
	}
	void SetRight( T right)
	{
		w = right - x;
	}
	T GetBottom() const
	{
		return y + h;
	}
	void SetBottom( T bottom)
	{
		h = bottom - y;
	}

	bool Intersect( T _x, T _y) const 
	{
		if ( _x >= x && _x <= GetRight() && _y >= y && _y <= GetBottom()) return true;
		return false;
	}

	template<typename Y>
	bool Intersect( const tRect<Y>& rhs) const 
	{
		if ( abs( x + GetRight() - rhs.x - rhs.GetRight()) 	< ( GetRight()- x + rhs.GetRight() - rhs.x)) 
		{
			if ( abs( y + GetBottom() - rhs.y - rhs.GetBottom()) < ( GetBottom() - y + rhs.GetBottom()- rhs.y))
				return true;
		}

		return false;

	}

	//! 矩形是否完全在自己内部
	template<typename Y>
	bool Inner( const tRect<Y>& rhs) const
	{
		if ( rhs.x >= x && rhs.GetRight() <= GetRight() && rhs.y >= y && rhs.GetBottom() <= GetBottom())
			return true;
		else
			return false;
	}

	bool Inner( T _x, T _y) const
	{
		if ( _x >= x && _x <= GetRight() && _y >= y && _y <= GetBottom())
			return true;
		else
			return false;
	}
};

typedef tRect<int>		Rect;
typedef tRect<float>	Rectf;

template< typename T>
struct tVector3D
{
	T x;
	T y;
	T z;

	tVector3D() {}
	tVector3D( T _x, T _y, T _z) : x( _x), y( _y), z( _z) {}
};

typedef tVector3D<int>		Vector3D;
typedef tVector3D<float>	Vector3Df;


//! 图片原始像素定义
//struct Bitmap 
//{
//	int			width;
//	int			height;
//	PixelFormat format;
//	uchar_t*	pixels;
//
//	Bitmap()
//		: pixels( NULL)
//	{}
//};



class CUncopyable
{
protected:
	CUncopyable() {}
	~CUncopyable() {}

private:
	CUncopyable( const CUncopyable& ) {	}
	const CUncopyable& operator=( const CUncopyable& src)
	{
		return *this;
	}
};

#define MIX_DISALLOW_COPY_ASSIGN(C) private: C( const C& ); const C& operator=( const C& src);


//! 实现资源管理的弱引用管理类
template<typename T>	// T必须实现IsValid()/WeakRef()/WeakRelease()
class CResWeakRefence
{
public:
	CResWeakRefence( T& res)
		: m_hardRef( &res)
	{
		if ( m_hardRef)
			m_hardRef->WeakRef();
	}
	CResWeakRefence( const T& res)
		: m_hardRef( &res)
	{
		if ( m_hardRef)
			m_hardRef->WeakRef();
	}
	CResWeakRefence()
		: m_hardRef( NULL)
	{}
	CResWeakRefence( const CResWeakRefence<T>& rhs)
		: m_hardRef( rhs.m_hardRef)
	{
		if ( m_hardRef)
			m_hardRef->WeakRef();
	}

	CResWeakRefence<T>& operator=( T& rhs)
	{
		Reset();
		m_hardRef = &rhs;
		if ( m_hardRef)
			m_hardRef->WeakRef();

		return *this;
	}
	CResWeakRefence<T>& operator=( const CResWeakRefence<T>& rhs)
	{
		Reset();
		m_hardRef = rhs.m_hardRef;
		if ( m_hardRef)
			m_hardRef->WeakRef();

		return *this;
	}
	~CResWeakRefence()
	{
		Reset();
	}

public:
	bool		IsValid() const
	{
		if ( m_hardRef && m_hardRef->IsValid())
			return true;
		else
			return false;
	}
	void		Reset()
	{
		if ( m_hardRef)
		{
			m_hardRef->WeakRelease();
			m_hardRef = NULL;
		}
	}
	T*	Lock()
	{
		if ( IsValid())
			return m_hardRef;
		else
			return NULL;
	}
	const T* Lock() const
	{
		if ( IsValid())
			return m_hardRef;
		else
			return NULL;
	}

	void		Swap( CResWeakRefence<T>& rhs)
	{
		T* temp = m_hardRef;
		m_hardRef = rhs.m_hardRef;
		rhs.m_hardRef = temp;
	}

	bool operator! ()
	{
		return !(IsValid());
	}

	const T*	GetNative() const
	{
		return m_hardRef;
	}
private:
	T*			m_hardRef;

};




class Colorf
{
	inline static void ColorClamp(float &x) { if(x<0.0f) x=0.0f; if(x>1.0f) x=1.0f; }
public:
	float		r,g,b,a;

	Colorf(float _r, float _g, float _b, float _a) { r=_r; g=_g; b=_b; a=_a; }
	Colorf(uint_t col) { SetHWColor(col); }
	Colorf() { r=g=b=a=0; }

	Colorf		operator-  (const Colorf &c) const { return Colorf(r-c.r, g-c.g, b-c.b, a-c.a); }
	Colorf		operator+  (const Colorf &c) const { return Colorf(r+c.r, g+c.g, b+c.b, a+c.a); }
	Colorf		operator*  (const Colorf &c) const { return Colorf(r*c.r, g*c.g, b*c.b, a*c.a); }
	Colorf&	operator-= (const Colorf &c)		{ r-=c.r; g-=c.g; b-=c.b; a-=c.a; return *this;   }
	Colorf&	operator+= (const Colorf &c)		{ r+=c.r; g+=c.g; b+=c.b; a+=c.a; return *this;   }
	bool			operator== (const Colorf &c) const { return (r==c.r && g==c.g && b==c.b && a==c.a);  }
	bool			operator!= (const Colorf &c) const { return (r!=c.r || g!=c.g || b!=c.b || a!=c.a);  }

	Colorf		operator/  (const float scalar) const { return Colorf(r/scalar, g/scalar, b/scalar, a/scalar); }
	Colorf		operator*  (const float scalar) const { return Colorf(r*scalar, g*scalar, b*scalar, a*scalar); }
	Colorf&	operator*= (const float scalar)		  { r*=scalar; g*=scalar; b*=scalar; a*=scalar; return *this;   }

	void			Clamp() { ColorClamp(r); ColorClamp(g); ColorClamp(b); ColorClamp(a); }
	void			SetHWColor(uint_t col) {	a = (col>>24)/255.0f; r = ((col>>16) & 0xFF)/255.0f; g = ((col>>8) & 0xFF)/255.0f; b = (col & 0xFF)/255.0f;	}
	uint_t			GetHWColor() const { return (uint_t(a*255.0f)<<24) + (uint_t(r*255.0f)<<16) + (uint_t(g*255.0f)<<8) + uint_t(b*255.0f);	}
};

inline Colorf operator* (const float sc, const Colorf &c) { return c*sc; }



#define MIX_ASSERT(x) assert(x)


}	// namesapce MIX


#pragma warning( pop)

// #include "smart_ptr.h"


#endif
