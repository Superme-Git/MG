#ifndef __Nuclear_XMARSHAL_H__
#define __Nuclear_XMARSHAL_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <algorithm>
#include "nuxbuffer.h"
#include "nuxptypes.h"


namespace Nuclear
{
	class INuclearStream;
	class NuclearOStream;
	class NuclearMarshal
	{
	public:
		class Exception { };
		virtual NuclearOStream& marshal(NuclearOStream &) const = 0;
		virtual const INuclearStream& unmarshal(const INuclearStream &) = 0;
		virtual ~NuclearMarshal() { }
	};

	class NuclearOStream
	{
		friend class INuclearStream;
		NuclearBuffer  data;
		mutable unsigned int pos;
		template<typename T> NuclearOStream& push_byte(T t)
		{
			data.insert(data.normalend(), &t, sizeof(t));
			return *this;
		}

	public:
		NuclearOStream() : pos(0) {}
		NuclearOStream(const NuclearBuffer &o) : data(o), pos(0) {}

		NuclearOStream& operator = (const NuclearOStream &os) 
		{
			if (&os != this)
			{
				pos  = os.pos;
				data = os.data;
			}
			return *this;
		}

		bool operator == (const NuclearOStream &os) const { return data == os.data; }
		bool operator != (const NuclearOStream &os) const { return data != os.data; }

		size_t size() const { return data.size(); }
		void swap (NuclearOStream &os) { data.swap(os.data); }
		operator NuclearBuffer& () { return data; }

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

		NuclearOStream& operator << (char x)               { return push_byte(x); }
		NuclearOStream& operator << (unsigned char x)      { return push_byte(x); }
		NuclearOStream& operator << (bool x)               { return push_byte(x); }

		NuclearOStream& operator << (short x)              { return push_byte(x); }
		NuclearOStream& operator << (unsigned short x)     { return push_byte(x); }
		NuclearOStream& operator << (int x)                { return push_byte(x); }
		NuclearOStream& operator << (unsigned int x)       { return push_byte(x); }
		NuclearOStream& operator << (long x)               { return push_byte(x); }
		NuclearOStream& operator << (unsigned long x)      { return push_byte(x); }
		NuclearOStream& operator << (long long x)          { return push_byte(x); }
		NuclearOStream& operator << (unsigned long long x) { return push_byte(x); }
		NuclearOStream& operator << (float x)              { return push_byte(*(int*)&x); }
		NuclearOStream& operator << (double x)             { return push_byte(*(unsigned long long*)&x); }

		NuclearOStream& operator << (const POINT &x)		{ return push_byte(x.x).push_byte(x.y); }
		NuclearOStream& operator << (const NuclearPoint &x)		{ return push_byte(x.x).push_byte(x.y); }
		NuclearOStream& operator << (const NuclearFPoint &x)		{ return push_byte(x.x).push_byte(x.y); }
		NuclearOStream& operator << (const RECT &x)		{ return push_byte(x.left).push_byte(x.top).push_byte(x.right).push_byte(x.bottom); }
		NuclearOStream& operator << (const NuclearRect &x)		{ return push_byte(x.left).push_byte(x.top).push_byte(x.right).push_byte(x.bottom); }
		NuclearOStream& operator << (const NuclearFRectt &x)		{ return push_byte(x.left).push_byte(x.top).push_byte(x.right).push_byte(x.bottom); }

		template<class T>
		NuclearOStream& operator << (const std::vector<T> & v)
		{
			int n = (int)v.size();
			push_byte(n);
			for(int i=0; i<n; ++i) 
				this->operator<<(v[i]);
			return *this;
		}

		NuclearOStream& operator << (const NuclearMarshal &x)     { return x.marshal(*this); }
		NuclearOStream& operator << (const NuclearBuffer &x)  
		{
			push_byte((unsigned int)x.size());
			data.insert(data.normalend(), x.constbegin(), x.constend());
			return *this;
		}

		NuclearOStream& operator << (const std::wstring &x)
		{
			push_byte((unsigned int)x.size());
			data.insert(data.normalend(), x.c_str(), x.size()*2);
			return *this;
		}

		NuclearOStream& push_byte(const void *x, size_t len)
		{
			data.insert(data.normalend(), x, len);
			return *this;
		}

	};

	class INuclearStream
	{
		friend class NuclearOStream;
		enum { MAXSPARE = 16384};
		NuclearBuffer  data;
		mutable unsigned int pos;
		template<typename T> void pop_byte(T &t) const
		{
			if (pos + sizeof(t) > data.size()) {
				throw NuclearMarshal::Exception();
			}
			t = *(T *)( (char*)data.constbegin() + pos );
			pos += sizeof(t);
		}
		unsigned char pop_byte_8() const;
		unsigned short pop_byte_16() const;
		unsigned long pop_byte_32() const;
		unsigned long long pop_byte_64() const;

	public:
		INuclearStream() : pos(0) {}
		INuclearStream(const NuclearBuffer &o) : data(o), pos(0) {}
		INuclearStream(const NuclearOStream &os) : data(os.data), pos(0) {}

		INuclearStream& operator = (const INuclearStream &os) 
		{
			if (&os != this)
			{
				pos  = os.pos;
				data = os.data;
			}
			return *this;
		}

		bool operator == (const INuclearStream &os) const { return data == os.data; }
		bool operator != (const INuclearStream &os) const { return data != os.data; }
		size_t size() const { return data.size(); }
		void swap (INuclearStream &os) { data.swap(os.data); }
		operator NuclearBuffer& () { return data; }

		void *begin() { return data.normalbegin(); }
		void *end()   { return data.normalend(); }
		void reserve(size_t size) { data.reserve(size); }
		const void *begin() const { return data.constbegin(); }
		const void *end()   const { return data.constend();   }
		bool eos() const { return pos == data.size(); }

		const INuclearStream& operator >> (char &x) const
		{
			x = pop_byte_8();
			return *this;
		}
		const INuclearStream& operator >> (unsigned char &x) const
		{
			x = pop_byte_8();
			return *this;
		}
		const INuclearStream& operator >> (bool &x) const
		{
			x = (0!=pop_byte_8());
			return *this;
		}

		const INuclearStream& operator >> (short &x) const
		{
			x = pop_byte_16();
			return *this;
		}

		const INuclearStream& operator >> (unsigned short &x) const
		{
			x = pop_byte_16();
			return *this;
		}

		const INuclearStream& operator >> (int &x) const
		{
			x = pop_byte_32();
			return *this;
		}

		const INuclearStream& operator >> (unsigned int &x) const
		{
			x = pop_byte_32();
			return *this;
		}

		const INuclearStream& operator >> (long &x) const
		{
			x = pop_byte_32();
			return *this;
		}

		const INuclearStream& operator >> (unsigned long &x) const
		{
			x = pop_byte_32();
			return *this;
		}

		const INuclearStream& operator >> (long long &x) const
		{
			x = pop_byte_64();
			return *this;
		}

		const INuclearStream& operator >> (unsigned long long &x) const
		{
			x = pop_byte_64();
			return *this;
		}

		const INuclearStream& operator >> (float &x) const
		{
			unsigned long l = pop_byte_32();
			x = *(float*)&l;
			return *this;
		}

		const INuclearStream& operator >> (double &x) const
		{
			unsigned long long ll = pop_byte_64();
			x = *(double*)&ll;
			return *this;
		}

		const INuclearStream& operator >> (POINT &x) const
		{
			this->operator >>(x.x);
			this->operator >>(x.y);
			return *this;
		}

		const INuclearStream& operator >> (NuclearPoint &x) const
		{
			this->operator >>(x.x);
			this->operator >>(x.y);
			return *this;
		}

		const INuclearStream& operator >> (NuclearFPoint &x) const
		{
			this->operator >>(x.x);
			this->operator >>(x.y);
			return *this;
		}

		const INuclearStream& operator >> (RECT &x) const
		{
			this->operator >>(x.left);
			this->operator >>(x.top);
			this->operator >>(x.right);
			this->operator >>(x.bottom);
			return *this;
		}

		const INuclearStream& operator >> (NuclearRect &x) const
		{
			this->operator >>(x.left);
			this->operator >>(x.top);
			this->operator >>(x.right);
			this->operator >>(x.bottom);
			return *this;
		}

		const INuclearStream& operator >> (NuclearFRectt &x) const
		{
			this->operator >>(x.left);
			this->operator >>(x.top);
			this->operator >>(x.right);
			this->operator >>(x.bottom);
			return *this;
		}

		template<class T>
		const INuclearStream& operator >> (std::vector<T> & v)
		{
			int n;
			this->operator >>(n);
			v.resize(n);
			for(int i=0; i<n; ++i) 
				this->operator>>(v[i]);
			return *this;
		}

		const INuclearStream& operator >> (NuclearMarshal &x) const
		{
			return x.unmarshal(*this);
		}

		const INuclearStream& operator >> (NuclearBuffer &x) const
		{
			unsigned int len = pop_byte_32();
			if (len > data.size() - pos) throw NuclearMarshal::Exception();
			x.replace((char*)data.constbegin()+pos, len);
			pos += len;
			return *this;
		}

		const INuclearStream& operator >>(std::wstring &x) const
		{
			unsigned int len = pop_byte_32() * 2;
			if( len > data.size() - pos ) throw NuclearMarshal::Exception();
            wchar_t* buff = new wchar_t[(len>>1) + 1];
            memset(buff, 0, ((len>>1) + 1) * sizeof(wchar_t));
            short* pData = (short*)((char*)data.constbegin()+pos);
            for( unsigned int i = 0; i < (len>>1) ; ++i)
            {
                buff[i] = static_cast<wchar_t>(pData[i]);
            }
			x.assign(buff, len/2);
            delete []buff;
			pos += len;
			return *this;
		}

		void pop_byte(void *x, size_t len) const
		{
			if (pos + len > data.size()) throw NuclearMarshal::Exception();
			memcpy(x, (char*)data.constbegin()+pos, len);
			pos += (unsigned int)len;
		}

	};

};

#endif
