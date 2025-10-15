#ifndef __Nuclear_XMARSHAL_H
#define __Nuclear_XMARSHAL_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <algorithm>
#include "xbuffer.h"
#include "xptypes.h"

namespace Nuclear
{
	class XIStream;
	class XOStream;
	class XMarshal
	{
	public:
		class Exception { };
		virtual XOStream& marshal(XOStream &) const = 0;
		virtual const XIStream& unmarshal(const XIStream &) = 0;
		virtual ~XMarshal() { }
	};

	class XOStream
	{
		friend class XIStream;
		XBuffer  data;
		mutable unsigned int pos;
		template<typename T> XOStream& push_byte(T t)
		{
			data.insert(data.normalend(), &t, sizeof(t));
			return *this;
		}

	public:
		XOStream() : pos(0) {}
		XOStream(const XBuffer &o) : data(o), pos(0) {}

		XOStream& operator = (const XOStream &os) 
		{
			if (&os != this)
			{
				pos  = os.pos;
				data = os.data;
			}
			return *this;
		}

		bool operator == (const XOStream &os) const { return data == os.data; }
		bool operator != (const XOStream &os) const { return data != os.data; }

		size_t size() const { return data.size(); }
		void swap (XOStream &os) { data.swap(os.data); }
		operator XBuffer& () { return data; }

		void *begin() { return data.normalbegin(); }
		void *end()   { return data.normalend(); }
		void reserve(size_t size) { data.reserve(size); }
		const void *begin() const { return data.constbegin(); }
		const void *end()   const { return data.constend();   }
		void insert(void *pos, const void *x, size_t len) { data.insert(pos, x, len); }
		void insert(void *pos, const void *x, const void *y) { data.insert(pos, x, y); }
		void erase(void *x, void *y) { data.erase(x, y); }
		void clear() { data.clear(); pos = 0; }

		bool eos() const { return pos == data.size(); }

		XOStream& operator << (char x)               { return push_byte(x); }
		XOStream& operator << (unsigned char x)      { return push_byte(x); }
		XOStream& operator << (bool x)               { return push_byte(x); }

		XOStream& operator << (short x)              { return push_byte(x); }
		XOStream& operator << (unsigned short x)     { return push_byte(x); }
		XOStream& operator << (int x)                { return push_byte(x); }
		XOStream& operator << (unsigned int x)       { return push_byte(x); }
		XOStream& operator << (long x)               { return push_byte(x); }
		XOStream& operator << (unsigned long x)      { return push_byte(x); }
		XOStream& operator << (long long x)          { return push_byte(x); }
		XOStream& operator << (unsigned long long x) { return push_byte(x); }
		XOStream& operator << (float x)              { return push_byte(*(int*)&x); }
		XOStream& operator << (double x)             { return push_byte(*(unsigned long long*)&x); }

		XOStream& operator << (const POINT &x)		{ return push_byte(x.x).push_byte(x.y); }
		XOStream& operator << (const CPOINT &x)		{ return push_byte(x.x).push_byte(x.y); }
		XOStream& operator << (const FPOINT &x)		{ return push_byte(x.x).push_byte(x.y); }
		XOStream& operator << (const RECT &x)		{ return push_byte(x.left).push_byte(x.top).push_byte(x.right).push_byte(x.bottom); }
		XOStream& operator << (const CRECT &x)		{ return push_byte(x.left).push_byte(x.top).push_byte(x.right).push_byte(x.bottom); }
		XOStream& operator << (const FRECT &x)		{ return push_byte(x.left).push_byte(x.top).push_byte(x.right).push_byte(x.bottom); }

		template<class T>
		XOStream& operator << (const std::vector<T> & v)
		{
			int n = (int)v.size();
			push_byte(n);
			for(int i=0; i<n; ++i) 
				this->operator<<(v[i]);
			return this;
		}

		XOStream& operator << (const XMarshal &x)     { return x.marshal(*this); }
		XOStream& operator << (const XBuffer &x)  
		{
			push_byte((unsigned int)x.size());
			data.insert(data.normalend(), x.constbegin(), x.constend());
			return *this;
		}

		XOStream& operator << (const std::wstring &x)
		{
			push_byte((unsigned int)x.size());
			data.insert(data.normalend(), x.c_str(), x.size()*2);
			return *this;
		}

		XOStream& push_byte(const void *x, size_t len)
		{
			data.insert(data.normalend(), x, len);
			return *this;
		}

	};

	class XIStream
	{
		friend class XOStream;
		enum { MAXSPARE = 16384};
		XBuffer  data;
		mutable unsigned int pos;
		template<typename T> void pop_byte(T &t) const
		{
			if (pos + sizeof(t) > data.size()) throw XMarshal::Exception();
			t = *(T *)( (char*)data.constbegin() + pos );
			pos += sizeof(t);
		}
		unsigned char pop_byte_8() const;
		unsigned short pop_byte_16() const;
		unsigned long pop_byte_32() const;
		unsigned long long pop_byte_64() const;

	public:
		XIStream() : pos(0) {}
		XIStream(const XBuffer &o) : data(o), pos(0) {}
		XIStream(const XOStream &os) : data(os.data), pos(0) {}

		XIStream& operator = (const XIStream &os) 
		{
			if (&os != this)
			{
				pos  = os.pos;
				data = os.data;
			}
			return *this;
		}

		bool operator == (const XIStream &os) const { return data == os.data; }
		bool operator != (const XIStream &os) const { return data != os.data; }
		size_t size() const { return data.size(); }
		void swap (XIStream &os) { data.swap(os.data); }
		operator XBuffer& () { return data; }

		void *begin() { return data.normalbegin(); }
		void *end()   { return data.normalend(); }
		void reserve(size_t size) { data.reserve(size); }
		const void *begin() const { return data.constbegin(); }
		const void *end()   const { return data.constend();   }
		bool eos() const { return pos == data.size(); }

		const XIStream& operator >> (char &x) const
		{
			x = pop_byte_8();
			return *this;
		}
		const XIStream& operator >> (unsigned char &x) const
		{
			x = pop_byte_8();
			return *this;
		}
		const XIStream& operator >> (bool &x) const
		{
			x = (0!=pop_byte_8());
			return *this;
		}

		const XIStream& operator >> (short &x) const
		{
			x = pop_byte_16();
			return *this;
		}

		const XIStream& operator >> (unsigned short &x) const
		{
			x = pop_byte_16();
			return *this;
		}

		const XIStream& operator >> (int &x) const
		{
			x = pop_byte_32();
			return *this;
		}

		const XIStream& operator >> (unsigned int &x) const
		{
			x = pop_byte_32();
			return *this;
		}

		const XIStream& operator >> (long &x) const
		{
			x = pop_byte_32();
			return *this;
		}

		const XIStream& operator >> (unsigned long &x) const
		{
			x = pop_byte_32();
			return *this;
		}

		const XIStream& operator >> (long long &x) const
		{
			x = pop_byte_64();
			return *this;
		}

		const XIStream& operator >> (unsigned long long &x) const
		{
			x = pop_byte_64();
			return *this;
		}

		const XIStream& operator >> (float &x) const
		{
			unsigned long l = pop_byte_32();
			x = *(float*)&l;
			return *this;
		}

		const XIStream& operator >> (double &x) const
		{
			unsigned long long ll = pop_byte_64();
			x = *(double*)&ll;
			return *this;
		}

		const XIStream& operator >> (POINT &x) const
		{
			this->operator >>(x.x);
			this->operator >>(x.y);
			return *this;
		}

		const XIStream& operator >> (CPOINT &x) const
		{
			this->operator >>(x.x);
			this->operator >>(x.y);
			return *this;
		}

		const XIStream& operator >> (FPOINT &x) const
		{
			this->operator >>(x.x);
			this->operator >>(x.y);
			return *this;
		}

		const XIStream& operator >> (RECT &x) const
		{
			this->operator >>(x.left);
			this->operator >>(x.top);
			this->operator >>(x.right);
			this->operator >>(x.bottom);
			return *this;
		}

		const XIStream& operator >> (CRECT &x) const
		{
			this->operator >>(x.left);
			this->operator >>(x.top);
			this->operator >>(x.right);
			this->operator >>(x.bottom);
			return *this;
		}

		const XIStream& operator >> (FRECT &x) const
		{
			this->operator >>(x.left);
			this->operator >>(x.top);
			this->operator >>(x.right);
			this->operator >>(x.bottom);
			return *this;
		}

		template<class T>
		const XIStream& operator >> (std::vector<T> & v)
		{
			int n;
			this->operator >>(n);
			v.resize(n);
			for(int i=0; i<n; ++i) 
				this->operator>>(v[i]);
			return this;
		}

		const XIStream& operator >> (XMarshal &x) const
		{
			return x.unmarshal(*this);
		}

		const XIStream& operator >> (XBuffer &x) const
		{
			unsigned int len = pop_byte_32();
			if (len > data.size() - pos) throw XMarshal::Exception();
			x.replace((char*)data.constbegin()+pos, len);
			pos += len;
			return *this;
		}

		const XIStream& operator >>(std::wstring &x) const
		{
			unsigned int len = pop_byte_32() * 2;
			if( len > data.size() - pos ) throw XMarshal::Exception();
			x.assign((const wchar_t*)((char*)data.constbegin()+pos), len/2);
			pos += len;
			return *this;
		}

		void pop_byte(void *x, size_t len) const
		{
			if (pos + len > data.size()) throw XMarshal::Exception();
			memcpy(x, (char*)data.constbegin()+pos, len);
			pos += (unsigned int)len;
		}

	};

};

#endif
