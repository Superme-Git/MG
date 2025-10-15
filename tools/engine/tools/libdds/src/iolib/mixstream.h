#ifndef __MIX_STREAM_H
#define __MIX_STREAM_H

#include "octets.h"

namespace MIX
{

	template<typename T>
	inline T& remove_const(const T &t) { return const_cast<T&>(t); }

	class OctetsStream
	{
		enum { MAXSPARE = 16384};
		GNET::Octets  data;
		mutable unsigned int pos;
		mutable unsigned int tranpos;
		template<typename T> OctetsStream& push_byte(T t)
		{
			data.insert(data.end(), &t, sizeof(t));
			return *this;
		}

		template<typename T> void pop_byte(T &t) const
		{
			if (pos + sizeof(t) > data.size()) throw L"pos over";
			t = *(T *)( (char*)data.begin() + pos );
			pos += sizeof(t);
		}

		unsigned char pop_byte_8() const
		{
			unsigned char c;
			pop_byte(c);
			return c;
		}

		unsigned short pop_byte_16() const
		{
			unsigned short s;
			pop_byte(s);
			return (s);
		}

		unsigned long pop_byte_32() const
		{
			unsigned long l;
			pop_byte(l);
			return (l);
		}

		unsigned long long pop_byte_64() const
		{
			unsigned long long ll;
			pop_byte(ll);
			return (ll);
		}

	public:
		OctetsStream() : pos(0) {}
		OctetsStream(const GNET::Octets &o) : data(o), pos(0) {}
		OctetsStream(const OctetsStream &os) : data(os.data), pos(0) {}

		OctetsStream& operator = (const OctetsStream &os) 
		{
			if (&os != this)
			{
				pos  = os.pos;
				data = os.data;
			}
			return *this;
		}

		bool operator == (const OctetsStream &os) const { return data == os.data; }
		bool operator != (const OctetsStream &os) const { return data != os.data; }
		size_t size() const { return data.size(); }
		void swap (OctetsStream &os) { data.swap(os.data); }
		operator GNET::Octets& () { return data; }

		void *begin() { return data.begin(); }
		void *end()   { return data.end(); }
		void reserve(size_t size) { data.reserve(size); }
		const void *begin() const { return data.begin(); }
		const void *end()   const { return data.end();   }
		void insert(void *pos, const void *x, size_t len) { data.insert(pos, x, len); }
		void insert(void *pos, const void *x, const void *y) { data.insert(pos, x, y); }
		void erase(void *x, void *y) { data.erase(x, y); }
		void clear() { data.clear(); pos = 0; }

		OctetsStream& operator << (char x)               { return push_byte(x); }
		OctetsStream& operator << (unsigned char x)      { return push_byte(x); }
		OctetsStream& operator << (bool x)               { return push_byte(x); }
		OctetsStream& operator << (short x)              { return push_byte((x)); }
		OctetsStream& operator << (unsigned short x)     { return push_byte((x)); }
		OctetsStream& operator << (int x)                { return push_byte((x)); }
		OctetsStream& operator << (unsigned int x)       { return push_byte((x)); }
		OctetsStream& operator << (long x)               { return push_byte((x)); }
		OctetsStream& operator << (unsigned long x)      { return push_byte((x)); }
		OctetsStream& operator << (long long x)          { return push_byte((x)); }
		OctetsStream& operator << (unsigned long long x) { return push_byte((x)); }
		OctetsStream& operator << (float x)              { return push_byte(*(float*)&x); }
		OctetsStream& operator << (double x)             { return push_byte(*(unsigned long long*)&x); }

		OctetsStream& push_byte(const char *x, size_t len)
		{
			data.insert(data.end(), x, len);
			return *this;
		}


		bool eos() const { return pos == data.size(); }
		bool seek( unsigned int pos)
		{
			if ( pos < data.size())
			{
				this->pos = pos;
				return true;
			}
			return false;
		}

		const OctetsStream& operator >> (const char &x) const
		{
			remove_const(x) = pop_byte_8();
			return *this;
		}
		const OctetsStream& operator >> (const unsigned char &x) const
		{
			remove_const(x) = pop_byte_8();
			return *this;
		}
		const OctetsStream& operator >> (const bool &x) const
		{
			remove_const(x) = pop_byte_8() ? true : false;
			return *this;
		}
		const OctetsStream& operator >> (const short &x) const
		{
			remove_const(x) = pop_byte_16();
			return *this;
		}
		const OctetsStream& operator >> (const unsigned short &x) const
		{
			remove_const(x) = pop_byte_16();
			return *this;
		}
		const OctetsStream& operator >> (const int &x) const
		{
			remove_const(x) = pop_byte_32();
			return *this;
		}
		const OctetsStream& operator >> (const unsigned int &x) const
		{
			remove_const(x) = pop_byte_32();
			return *this;
		}
		const OctetsStream& operator >> (const long &x) const
		{
			remove_const(x) = pop_byte_32();
			return *this;
		}
		const OctetsStream& operator >> (const unsigned long &x) const
		{
			remove_const(x) = pop_byte_32();
			return *this;
		}
		const OctetsStream& operator >> (const long long &x) const
		{
			remove_const(x) = pop_byte_64();
			return *this;
		}
		const OctetsStream& operator >> (const unsigned long long &x) const
		{
			remove_const(x) = pop_byte_64();
			return *this;
		}
		const OctetsStream& operator >> (const float &x) const
		{
			unsigned long l = pop_byte_32();
			remove_const(x) = *(float*)&l;
			return *this;
		}
		const OctetsStream& operator >> (const double &x) const
		{
			unsigned long long ll = pop_byte_64();
			remove_const(x) = *(double*)&ll;
			return *this;
		}

		void pop_byte(char *x, size_t len) const
		{
			if (pos + len > data.size()) throw L"pos over";
			memcpy(x, (char*)data.begin()+pos, len);
			pos += (unsigned int)len;
		}
	};

}		// namespace MIX

#endif	//__MIX_STREAM_H
