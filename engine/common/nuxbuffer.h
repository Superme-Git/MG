#ifndef __Nuclear_XBuffer_H__
#define __Nuclear_XBuffer_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <stdlib.h>
#include "nuutil.h"

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

	class NuclearOctets
	{
	public:
		virtual NuclearOctets& reserve(size_t size) = 0;
		virtual NuclearOctets& replace(const void *data, size_t size) = 0;
		virtual ~NuclearOctets() {}
		bool operator == (const NuclearOctets &x) const { return size() == x.size() && !memcmp( constbegin(), x.constbegin(), size() ); }
		bool operator != (const NuclearOctets &x) const { return ! operator == (x); }
		virtual void *begin() = 0;
		virtual void *end() = 0;
		virtual const void *constbegin() const = 0;
		virtual const void *constend() const = 0;
		virtual size_t size() const = 0;
		virtual size_t capacity() const = 0;
		virtual NuclearOctets& clear() = 0;
		virtual NuclearOctets& erase(void *x, void *y) = 0;

		virtual NuclearOctets& insert(void *pos, const void *x, size_t len) = 0;
		virtual NuclearOctets& insert(void *pos, const void *x, const void *y) = 0;
		virtual NuclearOctets& resize(size_t size) = 0;
		virtual NuclearOctets *clone() const = 0;
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

	class NuclearBufferOctets : public NuclearOctets
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
		virtual NuclearOctets& reserve(size_t size)
		{
			base = rep()->reserve(size);
			return *this;
		}
		virtual NuclearOctets& replace(const void *data, size_t size)
		{
			reserve(size);
			memcpy(base, data, size);
			rep()->len = size;
			return *this;
		}
		virtual ~NuclearBufferOctets() { rep()->release(); }
		NuclearBufferOctets () : base(Rep::null.data()) { rep()->addref(); }
		NuclearBufferOctets (size_t size) : base(Rep::create(size)->data()) { }
		NuclearBufferOctets (const void *x, size_t size) : base(Rep::create(size)->data())
		{
			memcpy(base, x, size);
			rep()->len = size;
		}
		NuclearBufferOctets (const void *x, const void *y) : base(Rep::create((char*)y-(char*)x)->data())
		{
			size_t size = (char*)y-(char*)x;
			memcpy(base, x, size);
			rep()->len = size;
		} 
		NuclearBufferOctets (const NuclearBufferOctets &x) : base(x.base) { rep()->addref(); }
		NuclearBufferOctets& operator = (const NuclearBufferOctets&x)
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
		virtual NuclearOctets& clear() { unique(); rep()->len = 0; return *this;  }
		virtual NuclearOctets& erase(void *x, void *y)
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

		virtual NuclearOctets& insert(void *pos, const void *x, size_t len)
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
		virtual NuclearOctets& insert(void *pos, const void *x, const void *y) { insert(pos, x, (char*)y - (char*)x); return *this; }
		virtual NuclearOctets& resize(size_t size) { reserve(size); rep()->len = size; return *this; }
		virtual NuclearOctets *clone() const { return new NuclearBufferOctets(*this); }
		virtual int GetRef() const { return (int)rep()->ref; }
		virtual void SetTick(int tick) { rep()->tick = tick; }
		virtual int GetTick() const { return (int)rep()->tick; }
	};

	class NuclearBuffer
	{
	private:
		NuclearOctets *m_pXOctets;

	public:
		NuclearBuffer () : m_pXOctets(new NuclearBufferOctets()) { }
		NuclearBuffer (size_t size) : m_pXOctets(new NuclearBufferOctets(size)) { }
		NuclearBuffer (const void *x, size_t size) : m_pXOctets(new NuclearBufferOctets(x, size)) { }
		NuclearBuffer (const void *x, const void *y) : m_pXOctets(new NuclearBufferOctets(x, y)) { }
		NuclearBuffer (const NuclearBuffer &x) : m_pXOctets( x.m_pXOctets->clone()) { }
		virtual ~NuclearBuffer() { delete m_pXOctets; }

		NuclearBuffer& operator = (const NuclearBuffer&x)
		{
			if ( &x != this )
			{
				delete m_pXOctets;
				m_pXOctets = x.m_pXOctets->clone();
			}
			return *this;
		}

		NuclearBuffer& reserve(size_t size)
		{
			m_pXOctets->reserve(size);
			return *this;
		}
		NuclearBuffer& replace(const void *data, size_t size)
		{
			m_pXOctets->replace(data, size);
			return *this;
		}
		void SetOctets(const NuclearOctets &oct)
		{
			delete m_pXOctets;
			m_pXOctets = oct.clone();
		}
		bool operator == (const NuclearBuffer &x) const { return (*m_pXOctets) == (*x.m_pXOctets);  }
		bool operator != (const NuclearBuffer &x) const { return ! operator == (x); }

		NuclearBuffer& insert(void *pos, const void *x, size_t len) { m_pXOctets->insert(pos, x, len); return *this;}
		NuclearBuffer& insert(void *pos, const void *x, const void *y) { insert(pos, x, (char*)y - (char*)x); return *this; }
		NuclearBuffer& resize(size_t size) { m_pXOctets->resize(size); return *this; }

		NuclearBuffer& swap(NuclearBuffer &x) { NuclearOctets *tmp = m_pXOctets; m_pXOctets = x.m_pXOctets; x.m_pXOctets = tmp; return *this; }
		void *normalbegin() { return m_pXOctets->begin(); }
		void *normalend()   { return m_pXOctets->end(); }
		const void *constbegin() const { return m_pXOctets->constbegin(); }
		const void *constend()   const { return m_pXOctets->constend(); }
		size_t size()     const { return m_pXOctets->size(); }
		size_t capacity() const { return m_pXOctets->capacity(); }
		NuclearBuffer& clear() { m_pXOctets->clear(); return *this;  }
		NuclearBuffer& erase(void *x, void *y) { m_pXOctets->erase(x, y); return *this; }

		void dump() const { m_pXOctets->dump(); }
		int GetRefCount() const { return m_pXOctets->GetRef(); }
		void SetTick(int tick) { m_pXOctets->SetTick(tick); }
		int GetTick() const { return m_pXOctets->GetTick(); }
	};

};

#endif
