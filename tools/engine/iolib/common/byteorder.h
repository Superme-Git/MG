#ifndef __BYTEORDER_H
#define __BYTEORDER_H

namespace GNET
{

	#if defined BYTE_ORDER_BIG_ENDIAN
		#define byteorder_16(x)	(x)
		#define byteorder_32(x)	(x)
		#define byteorder_64(x)	(x)
	#elif defined __GNUC__
		#define byteorder_16 htons
		#define byteorder_32 htonl
		
		inline unsigned short byteorder_16(unsigned short x)
		{
			register unsigned short v;
			__asm__ ("xchg %b0, %h0" : "=q"(v) : "0"(x));
			return v;
		}
		inline unsigned long byteorder_32(unsigned long x)
		{
			register unsigned long v;
			__asm__ ("bswap %0" : "=r"(v) : "0"(x));
			return v;
		}
		inline unsigned long long byteorder_64(unsigned long long x)
		{
			union
			{
				unsigned long long __ll;
				unsigned long __l[2];
			} i, o;
			i.__ll = x;
			o.__l[0] = byteorder_32(i.__l[1]);
			o.__l[1] = byteorder_32(i.__l[0]);
			return o.__ll;
		}

		union DoubleInt64
		{
			double	_d;
			unsigned long long _ui;
			long long _si;
			DoubleInt64( double x ) : _d(x) { }
			DoubleInt64( unsigned long long x ) : _ui(x) { }
			DoubleInt64( long long x ) : _si(x) { }

			unsigned long long toUInt64() const { return _ui; }
			long long toSInt64() const { return _si; }
			double toDouble() const { return _d; }

			operator unsigned long long() const { return _ui; }
			operator long long() const { return _si; }
			operator double() const { return _d; }
		};

	#elif defined WIN32
		inline unsigned __int16 byteorder_16(unsigned __int16 x)
		{
			__asm ror x, 8
			return x;
		}
		inline unsigned __int32 byteorder_32(unsigned __int32 x)
		{
			__asm mov eax, x
			__asm bswap eax
			__asm mov x, eax
			return x;
		}
		inline unsigned __int64 byteorder_64(unsigned __int64 x)
		{
			union
			{
				unsigned __int64 __ll;
				unsigned __int32 __l[2];
			} i, o;
			i.__ll = x;
			o.__l[0] = byteorder_32(i.__l[1]);
			o.__l[1] = byteorder_32(i.__l[0]);
			return o.__ll;
		}

		union DoubleInt64
		{
			double	_d;
			unsigned __int64 _ui;
			__int64 _si;
			DoubleInt64( double x ) : _d(x) { }
			DoubleInt64( unsigned __int64 x ) : _ui(x) { }
			DoubleInt64( __int64 x ) : _si(x) { }

			unsigned __int64 toUInt64() const { return _ui; }
			__int64 toSInt64() const { return _si; }
			double toDouble() const { return _d; }

			operator unsigned __int64() const { return _ui; }
			operator __int64() const { return _si; }
			operator double() const { return _d; }
		};
	#endif

	union FloatInt32
	{
		float	_f;
		unsigned long _ui;
		long _si;

		FloatInt32( float x ) : _f(x) { }
		FloatInt32( unsigned long x ) : _ui(x) { }
		FloatInt32( long x ) : _si(x) { }

		unsigned int toUInt32() const { return _ui; }
		long toSInt32() const { return _si; }
		float toFloat() const { return _f; }

		operator unsigned long() const { return _ui; }
		operator long() const { return _si; }
		operator float() const { return _f; }
	};

};

#endif
