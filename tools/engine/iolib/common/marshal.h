#ifndef __MARSHAL_H
#define __MARSHAL_H

#ifdef _MSC_VER
#pragma warning( push )
#pragma warning( disable : 4146)
#pragma warning( disable : 4800)
#endif

#include <algorithm>
#include <string>
#include <vector>
#include <list>
#include <deque>
#include <map>
#include "byteorder.h"
#include "octets.h"
#include "cauthc.h"
#include "boo.h"

namespace GNET
{

template<typename T>
inline T& remove_const(const T &t) { return const_cast<T&>(t); }

class Marshal
{
public:
	class Exception { };
	enum Transaction { Begin, Commit, Rollback };

	class OctetsStream
	{
		enum { MAXSPARE = 16384};
		Octets  data;
		mutable unsigned int pos;
		mutable unsigned int tranpos;
		template<typename T> OctetsStream& push_byte(T t)
		{
			data.insert(data.end(), &t, sizeof(t));
			return *this;
		}
		template<typename T> void pop_byte(T &t) const
		{
			if (pos + sizeof(t) > data.size()) throw Marshal::Exception();
			t = *(T *)( (char*)data.begin() + pos );
			pos += sizeof(t);
		}
		unsigned char pop_byte_8() const;
		unsigned short pop_byte_16() const;
		unsigned long pop_byte_32() const;
		unsigned long long pop_byte_64() const;

		friend class CompactUINT;
		friend class CompactSINT;

		OctetsStream& compact_uint32(unsigned int x);
		OctetsStream& compact_sint32(int x);
		const OctetsStream& uncompact_uint32(const unsigned int &x) const;
		const OctetsStream& uncompact_sint32(const int &x) const;
	public:
		OctetsStream() : pos(0) {}
		OctetsStream(const Octets &o) : data(o), pos(0) {}
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
		operator Octets& () { return data; }

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
		OctetsStream& operator << (short x)              { return push_byte(byteorder_16(x)); }
		OctetsStream& operator << (unsigned short x)     { return push_byte(byteorder_16(x)); }
		OctetsStream& operator << (int x)                { return push_byte(byteorder_32(x)); }
		OctetsStream& operator << (unsigned int x)       { return push_byte(byteorder_32(x)); }
		OctetsStream& operator << (long x)               { return push_byte(byteorder_32(x)); }
		OctetsStream& operator << (unsigned long x)      { return push_byte(byteorder_32(x)); }
		OctetsStream& operator << (long long x)          { return push_byte(byteorder_64(x)); }
		OctetsStream& operator << (unsigned long long x) { return push_byte(byteorder_64(x)); }
		OctetsStream& operator << (float x)              { return push_byte(byteorder_32(*(int*)&x)); }
		OctetsStream& operator << (double x)             { return push_byte(byteorder_64(*(unsigned long long*)&x)); }
		OctetsStream& operator << (const Marshal &x)     { return x.marshal(*this); }
		OctetsStream& operator << (const Octets &x)  
		{
			compact_uint32( (unsigned int)x.size());
			data.insert(data.end(), x.begin(), x.end());
			return *this;
		}
		template<typename T>
		OctetsStream& operator << (const std::basic_string<T> &x)
		{
			/*
				sizeof(T)
				1             std::string  和share/common/marshal.h(unix版本)兼容
				2             std::wstring 只用于 windows，和 java 兼容。编码必须是utf-16le
			*/
			STATIC_ASSERT(sizeof(T) == 1 || sizeof(T) == 2);
			unsigned int bytes = x.length()*sizeof(T); // count of bytes
			compact_uint32(bytes);
			insert(end(), (void*)x.c_str(), bytes );
			return *this;
		}
		OctetsStream& push_byte(const char *x, size_t len)
		{
			data.insert(data.end(), x, len);
			return *this;
		}
		template<typename T1, typename T2>
		OctetsStream& operator << (const std::pair<T1, T2> &x)
		{
			return *this << x.first << x.second;
		}
		template<typename T>
        OctetsStream& operator << (const std::vector<T> &x) 
        {
            return *this <<( MarshalContainer(x));
        }
		template<typename T>
        OctetsStream& operator << (const std::list<T> &x) 
        {
            return *this <<( MarshalContainer(x));
        }
		template<typename T>
        OctetsStream& operator << (const std::deque<T> &x) 
        {
            return *this <<( MarshalContainer(x));
        }
		template<typename T1, typename T2>
        OctetsStream& operator << (const std::map<T1, T2> &x) 
        {
            return *this <<( MarshalContainer(x));
        }

		bool eos() const { return pos == data.size(); }
		const OctetsStream& operator >> (Marshal::Transaction trans) const
		{
			switch (trans)
			{
			case Marshal::Begin:
				tranpos = pos;
				break;
			case Marshal::Rollback:
				pos = tranpos;
				break;
			case Marshal::Commit:
				if (pos >= MAXSPARE)
				{
					remove_const(*this).data.erase((char*)data.begin(), (char*)data.begin()+pos);	
					pos = 0;
				}
			}
			return *this;
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
			remove_const(x) = pop_byte_8();
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
		const OctetsStream& operator >> (const Marshal &x) const
		{
			return remove_const(x).unmarshal(*this);
		}
		const OctetsStream& operator >> (const Octets &x) const
		{
			size_t len;
			uncompact_uint32(len);
			if (len > data.size() - pos) throw Marshal::Exception();
			remove_const(x).replace((char*)data.begin()+pos, len);
			pos += (unsigned int)len;
			return *this;
		}
		template<typename T>
		const OctetsStream& operator >> (const std::basic_string<T> &x) const
		{
			/*
				sizeof(T)
				1             std::string  和share/common/marshal.h(unix版本)兼容
				2             std::wstring 只用于 windows，和 java 兼容。编码必须是utf-16le
			*/
			STATIC_ASSERT(sizeof(T) == 1 || sizeof(T) == 2);
			unsigned int bytes;
			uncompact_uint32(bytes);
			if (bytes % sizeof(T)) throw Marshal::Exception(); // invalid length
			if (bytes > data.size() - pos) throw Marshal::Exception();
			remove_const(x).assign((T*)((char*)data.begin()+pos), bytes/sizeof(T));
			pos += bytes;
			return *this;
		}
		void pop_byte(char *x, size_t len) const
		{
			if (pos + len > data.size()) throw Marshal::Exception();
			memcpy(x, (char*)data.begin()+pos, len);
			pos += (unsigned int)len;
		}
		template<typename T1, typename T2>
		const OctetsStream& operator >> (const std::pair<T1, T2> &x) const
		{
			return *this >> remove_const(x.first) >> remove_const(x.second);
		}
		template<typename T>
		const OctetsStream& operator >> (const std::vector<T> &x) const
		{
			return *this >>( MarshalContainer(x));			
		}
		template<typename T>
		const OctetsStream& operator >> (const std::deque<T> &x) const
		{
			return *this >>( MarshalContainer(x));			
		}
		template<typename T>
		const OctetsStream& operator >> (const std::list<T> &x) const
		{
			return *this >>( MarshalContainer(x));			
		}
		template<typename T1, typename T2>
		const OctetsStream& operator >> (const std::map<T1, T2> &x) const
		{
			return *this >>( MarshalContainer(x));			
		}
		OctetsStream& unmarshal_UTF_16LE(const std::wstring & x)
		{
			STATIC_ASSERT(sizeof(std::wstring::value_type) == 2);
			unsigned int len;
			uncompact_uint32(len);
			if (len > data.size() - pos) throw Marshal::Exception();
			remove_const(x).assign((std::wstring::value_type*)((char*)data.begin()+pos), len/2);
			pos += len;
			return *this;
		}
	};

	virtual OctetsStream& marshal(OctetsStream &) const = 0;
	virtual const OctetsStream& unmarshal(const OctetsStream &) = 0;
	virtual std::ostream & trace(std::ostream & os) const { return os; }
	virtual ~Marshal() { }
};

class CompactUINT : public Marshal
{
	unsigned int *pi;
public:
	explicit CompactUINT(const unsigned int &i): pi(&remove_const(i)) { }

	OctetsStream& marshal(OctetsStream &os) const
	{
		return os.compact_uint32(*pi);
	}
	const OctetsStream& unmarshal(const OctetsStream &os)
	{
		return os.uncompact_uint32(*pi);
	}
};

class CompactSINT : public Marshal
{
	int *pi;
public:
	explicit CompactSINT(const int &i): pi(&remove_const(i)) { }

	OctetsStream& marshal(OctetsStream &os) const
	{
		return os.compact_sint32(*pi);
	}
	const OctetsStream& unmarshal(const OctetsStream &os)
	{
		return os.uncompact_sint32(*pi);
	}
};



template<typename Container>
class STLContainer : public Marshal
{
	Container *pc;
public:
	explicit STLContainer(Container &c) : pc(&c) { }
	OctetsStream& marshal(OctetsStream &os) const
	{
		os << CompactUINT( (unsigned int)pc->size());
		for (typename Container::const_iterator i = pc->begin(), e = pc->end(); i != e; ++i)
			os << *i;
		return os;
	}
	const OctetsStream& unmarshal(const OctetsStream &os)
	{
		pc->clear();
		size_t size;
		for (os >> CompactUINT(size); size > 0; --size)
		{
			typename Container::value_type tmp;
			os >> tmp;
			pc->insert(pc->end(), tmp);
		}
		return os;
	}
};

template<typename Container>
inline STLContainer<Container> MarshalContainer(const Container &c)
{
	return STLContainer<Container> (remove_const(c));
}

/////////////////////////////////////////////////////////////////////////////
// trace
inline std::ostream & operator << (std::ostream & os, const Marshal & s) { return s.trace(os); }

template <class _T1, class _T2>
inline std::ostream & operator<<(std::ostream & os, const std::pair<_T1, _T2>& __x)
{
	return os << __x.first << '=' << __x.second;
}

template < typename _CType >
std::ostream & TraceContainer(std::ostream & os, const _CType & c)
{
	os << '[';
	typename _CType::const_iterator i = c.begin(), e = c.end();
	if ( i != e ) { os << *i; for ( ++i; i != e; ++i) os << ',' << *i; }
	os << ']';
	return os;
}

template < typename T >
std::ostream & operator << (std::ostream & os, const std::vector<T> &x)
{
	return TraceContainer(os, x);
}

template < typename T >
std::ostream & operator << (std::ostream & os, const std::list<T> &x)
{
	return TraceContainer(os, x);
}

template < typename T >
std::ostream & operator << (std::ostream & os, const std::deque<T> &x)
{
	return TraceContainer(os, x);
}

template < typename T1, typename T2>
std::ostream & operator << (std::ostream & os, const std::map<T1, T2> &x)
{
	return TraceContainer(os, x);
}

};

#ifdef _MSC_VER
#pragma warning( pop )
#endif

#endif
