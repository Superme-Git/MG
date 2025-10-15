#ifndef __Nuclear_XBuffer_H
#define __Nuclear_XBuffer_H

#if _MSC_VER > 1000
#pragma once
#endif

#include <stdlib.h>
#include "util.h"

namespace Nuclear
{


inline void *fast_memmove(void *dest, const void *src, size_t n)
{
	unsigned char *d = (unsigned char *)dest;
	unsigned char *s = (unsigned char *)src;
	if ( s < d && s + n >= d )
		return memmove(dest, src, n);
	else
		return memcpy(dest, src, n);
}

class XOctets
{
public:
	virtual XOctets& reserve(size_t size) = 0;
	virtual XOctets& replace(const void *data, size_t size) = 0;
	virtual ~XOctets() {}
	bool operator == (const XOctets &x) const { return size() == x.size() && !memcmp( constbegin(), x.constbegin(), size() ); }
	bool operator != (const XOctets &x) const { return ! operator == (x); }
	virtual void *begin() = 0;
	virtual void *end() = 0;
	virtual const void *constbegin() const = 0;
	virtual const void *constend() const = 0;
	virtual size_t size() const = 0;
	virtual size_t capacity() const = 0;
	virtual XOctets& clear() = 0;
	virtual XOctets& erase(void *x, void *y) = 0;

	// x 不可以 在begin()和end()之间
	virtual XOctets& insert(void *pos, const void *x, size_t len) = 0;
	virtual XOctets& insert(void *pos, const void *x, const void *y) = 0;
	virtual XOctets& resize(size_t size) = 0;
	virtual XOctets *clone() const = 0;
	virtual int GetRef() const = 0;
	virtual void SetTick(int tick) = 0;
	virtual int GetTick() const = 0;
	void dump() const
	{
		const unsigned char *p = (const unsigned char *)constbegin();
		for(size_t i = 0; i < size(); i++)
			fprintf(stderr, "%02x ", *p++);
		fprintf(stderr, "\n");
	}
};

class XBufferOctets : public XOctets
{
	struct Rep
	{
		size_t cap;
		size_t len;
		size_t ref;
		size_t tick;

		void addref() { ref++; }
		void release() { if ( cap > 0 && --ref == 0 ) delete this; }

		void* data() { return reinterpret_cast<void *>(this + 1); }

		void* clone()
		{
			Rep *rep = create(cap);
			memcpy(rep->data(), data(), rep->len = len);
			return rep->data();
		}

		void* unique()
		{
			if ( ref > 1 )
			{
				void* r = clone();
				release();
				return r;
			}
			return data();
		}

		void* reserve(size_t size)
		{
			size = frob_size(size);
			if ( size > cap )
			{
				Rep* rep = create(size);
				memcpy(rep->data(), data(), rep->len = len);
				release();
				return rep->data();
			}
			return unique();
		}

		static size_t frob_size(size_t size)
		{
			size_t tmp = 16;
			while ( size > tmp ) tmp <<= 1;
			return tmp;
		}

		static Rep* create(size_t cap)
		{
			if (cap == 0)
			{
				Rep::null.addref();
				return &Rep::null;
			}
			Rep *rep = new (cap) Rep;
			rep->cap = cap;
			rep->len = 0;
			rep->ref = 1;
			rep->tick = 0;
			return rep;
		}

		static void* operator new (size_t size, size_t extra) { return XAlloc(static_cast<int>(size + extra)); }
		static void  operator delete (void *p) { XFree(p); }
		static Rep null;
	};
	void *base;
	Rep *rep () const { return reinterpret_cast<Rep *>(base) - 1; }
	void unique() { base = rep()->unique(); }
public:
	virtual XOctets& reserve(size_t size)
	{
		base = rep()->reserve(size);
		return *this;
	}
	virtual XOctets& replace(const void *data, size_t size)
	{
		reserve(size);
		memcpy(base, data, size);
		rep()->len = size;
		return *this;
	}
	virtual ~XBufferOctets() { rep()->release(); }
	XBufferOctets () : base(Rep::null.data()) { rep()->addref(); }
	XBufferOctets (size_t size) : base(Rep::create(size)->data()) { }
	XBufferOctets (const void *x, size_t size) : base(Rep::create(size)->data())
	{
		memcpy(base, x, size);
		rep()->len = size;
	}
	XBufferOctets (const void *x, const void *y) : base(Rep::create((char*)y-(char*)x)->data())
	{
		size_t size = (char*)y-(char*)x;
		memcpy(base, x, size);
		rep()->len = size;
	} 
	XBufferOctets (const XBufferOctets &x) : base(x.base) { rep()->addref(); }
	XBufferOctets& operator = (const XBufferOctets&x)
	{
		if ( &x != this )
		{
			rep()->release();
			base = x.base;
			rep()->addref();
		}
		return *this;
	}
	virtual void *begin() { unique(); return base; }
	virtual void *end()   { unique(); return (char*)base + rep()->len; }
	virtual const void *constbegin() const { return base; }
	virtual const void *constend()   const { return (char*)base + rep()->len; }
	virtual size_t size()     const { return rep()->len; }
	virtual size_t capacity() const { return rep()->cap; }
	virtual XOctets& clear() { unique(); rep()->len = 0; return *this;  }
	virtual XOctets& erase(void *x, void *y)
	{
		if (x != y)
		{
			void *tmp = base;
			base = rep()->unique();
			ptrdiff_t o = (char *)base - (char *)tmp;
			if (o)
			{
				x = (char *)x + o;
				y = (char *)y + o;
			}
			fast_memmove((char*)x, (char*)y, ((char*)base + rep()->len) - (char*)y);
			rep()->len -= (char *)y - (char *)x;
		}
		return *this;
	}

	// x 不可以 在begin()和end()之间
	virtual XOctets& insert(void *pos, const void *x, size_t len)
	{
		ptrdiff_t off = (char *)pos - (char *)base;
		reserve(size() + len);
		pos = (char *)base + off;
		size_t adjust = size() - off;
		if ( adjust ) fast_memmove((char*)pos + len, pos, adjust);
		fast_memmove(pos, x, len);
		rep()->len += len;
		return *this;
	}
	virtual XOctets& insert(void *pos, const void *x, const void *y) { insert(pos, x, (char*)y - (char*)x); return *this; }
	virtual XOctets& resize(size_t size) { reserve(size); rep()->len = size; return *this; }
	virtual XOctets *clone() const { return new XBufferOctets(*this); }
	virtual int GetRef() const { return (int)rep()->ref; }
	virtual void SetTick(int tick) { rep()->tick = tick; }
	virtual int GetTick() const { return (int)rep()->tick; }
};



class XBuffer
{
	XOctets *m_pXOctets;
public:
	XBuffer& reserve(size_t size)
	{
		m_pXOctets->reserve(size);
		return *this;
	}
	XBuffer& replace(const void *data, size_t size)
	{
		m_pXOctets->replace(data, size);
		return *this;
	}
	virtual ~XBuffer() { delete m_pXOctets; }
	XBuffer () : m_pXOctets(new XBufferOctets()) { }
	XBuffer (size_t size) : m_pXOctets(new XBufferOctets(size)) { }
	XBuffer (const void *x, size_t size) : m_pXOctets(new XBufferOctets(x, size)) { }
	XBuffer (const void *x, const void *y) : m_pXOctets(new XBufferOctets(x, y)) { }
	XBuffer (const XBuffer &x) : m_pXOctets( x.m_pXOctets->clone()) { }
	XBuffer& operator = (const XBuffer&x)
	{
		if ( &x != this )
		{
			delete m_pXOctets;
			m_pXOctets = x.m_pXOctets->clone();
		}
		return *this;
	}
	void SetOctets(const XOctets &oct)
	{
		delete m_pXOctets;
		m_pXOctets = oct.clone();
	}
	bool operator == (const XBuffer &x) const { return (*m_pXOctets) == (*x.m_pXOctets);  }
	bool operator != (const XBuffer &x) const { return ! operator == (x); }
	XBuffer& swap(XBuffer &x) { XOctets *tmp = m_pXOctets; m_pXOctets = x.m_pXOctets; x.m_pXOctets = tmp; return *this; }
	void *normalbegin() { return m_pXOctets->begin(); }
	void *normalend()   { return m_pXOctets->end(); }
	const void *constbegin() const { return m_pXOctets->constbegin(); }
	const void *constend()   const { return m_pXOctets->constend(); }
	size_t size()     const { return m_pXOctets->size(); }
	size_t capacity() const { return m_pXOctets->capacity(); }
	XBuffer& clear() { m_pXOctets->clear(); return *this;  }
	XBuffer& erase(void *x, void *y) { m_pXOctets->erase(x, y); return *this; }

	// x 不可以 在begin()和end()之间
	XBuffer& insert(void *pos, const void *x, size_t len) { m_pXOctets->insert(pos, x, len); return *this;}
	XBuffer& insert(void *pos, const void *x, const void *y) { insert(pos, x, (char*)y - (char*)x); return *this; }
	XBuffer& resize(size_t size) { m_pXOctets->resize(size); return *this; }

	void dump() const { m_pXOctets->dump(); }
	int GetRefCount() const { return m_pXOctets->GetRef(); }
	void SetTick(int tick) { m_pXOctets->SetTick(tick); }
	int GetTick() const { return m_pXOctets->GetTick(); }
};

};

#endif
