#include "libshare.h"
#include "win32/share.hpp" 
namespace FireNet {

	void *fast_memmove(void *dest, const void *src, size_t n)
	{
		unsigned char *d = (unsigned char *)dest;
		unsigned char *s = (unsigned char *)src;
		if ( s < d && s + n >= d )
			return memmove(dest, src, n);
		else
			return memcpy(dest, src, n);
	}

#if defined _REENTRANT_

	void Octets_Rep::addref()
	{
#	ifdef OS_WINDOWS
		InterlockedIncrement((LONG*)&mRef);
#else
        FireNet::Mutex::Scoped sp(mLocker);
        mRef++;
#endif
	}
    
	void Octets_Rep::release()
	{
#ifdef OS_WINDOWS
		LONG lResult = InterlockedDecrement( &mRef );
		if( 0 == lResult )
			delete this;
#else
        FireNet::Mutex::Scoped sp(mLocker);
        if ( --mRef == 0 )
            delete this;
#endif
	}

#else

	void Octets_Rep::addref()
	{
		mRef++;
	}
	void Octets_Rep::release()
	{
		if ( --mRef == 0 )
			delete this;
	}

#endif

	void* Octets_Rep::data()
	{
		return reinterpret_cast<void *>(this + 1);
	}

	void* Octets_Rep::clone()
	{
		Octets_Rep *rep = create(mCap);
		memcpy(rep->data(), data(), rep->mLen = mLen);
		return rep->data();
	}

	void* Octets_Rep::unique()
	{
		if ( mRef > 1 )
		{
			void* r = clone();
			release();
			return r;
		}
		return data();
	}

	void* Octets_Rep::reserve(size_t size)
	{
		size = frob_size(size);
		if ( size > mCap )
		{
			Octets_Rep* rep = create(size);
			memcpy(rep->data(), data(), rep->mLen = mLen);
			release();
			return rep->data();
		}
		return unique();
	}

	size_t Octets_Rep::frob_size(size_t size)
	{
		size_t tmp = 16;
		while ( size > tmp ) tmp <<= 1;
		return tmp;
	}

	Octets_Rep* Octets_Rep::create(size_t cap)
	{
		Octets_Rep *rep = new (cap)Octets_Rep;
		rep->mCap = cap;
		rep->mLen = 0;
		rep->mRef = 1;
		return rep;
	}

	void* Octets_Rep::operator new (size_t size, size_t extra)
	{
		return malloc(size + extra);
	}
	void  Octets_Rep::operator delete (void *p)
	{
		free(p);
	}

	Octets_Rep Octets_Rep::mNnull = { 0, 0, 1 };
    FireNet::Mutex Octets_Rep::mLocker;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	Octets_Rep* Octets::rep () const
	{
		return reinterpret_cast<Octets_Rep *>(base) - 1;
	}
	void Octets::unique()
	{
		base = rep()->unique();
	}

	Octets& Octets::reserve(size_t size)
	{
		base = rep()->reserve(size);
		return *this;
	}

	Octets& Octets::replace(const void *data, size_t size)
	{
		reserve(size);
		memcpy(base, data, size);
		rep()->mLen = size;
		return *this;
	}

	Octets::~Octets()
	{
		rep()->release();
	}

	Octets::Octets()
		: base( Octets_Rep::mNnull.data())
	{
		rep()->addref();
	}

	Octets::Octets( size_t size)
		: base(Octets_Rep::create(size)->data())
	{
	}

	Octets::Octets( const void *x, size_t size)
		: base(Octets_Rep::create(size)->data())
	{
		memcpy(base, x, size);
		rep()->mLen = size;
	}

	Octets::Octets( const void *x, const void *y)
		: base( Octets_Rep::create((char*)y-(char*)x)->data())
	{
		size_t size = (char*)y-(char*)x;
		memcpy(base, x, size);
		rep()->mLen = size;
	}

	Octets::Octets( const Octets &x)
		: base(x.base)
	{
		rep()->addref();
	}

	Octets& Octets::operator = (const Octets&x)
	{
		if ( &x != this )
		{
			rep()->release();
			base = x.base;
			rep()->addref();
		}
		return *this;
	}

	bool Octets::operator == (const Octets &x) const
	{
		return size() == x.size() && !memcmp( base, x.base, size() );
	}

	bool Octets::operator != (const Octets &x) const
	{
		return ! operator == (x);
	}

	Octets& Octets::swap(Octets &x)
	{
		void *tmp = base;
		base = x.base;
		x.base = tmp;
		return *this;
	}

	void* Octets::begin()
	{
		unique();
		return base;
	}

	void* Octets::end()
	{
		unique();
		return (char*)base + rep()->mLen;
	}

	const void *Octets::begin() const
	{
		return base;
	}

	const void* Octets::end() const
	{
		return (char*)base + rep()->mLen;
	}

	size_t Octets::size() const
	{
		return rep()->mLen;
	}

	size_t Octets::capacity() const
	{
		return rep()->mCap;
	}

	Octets& Octets::clear()
	{
		unique();
		rep()->mLen = 0;
		return *this;
	}

	Octets& Octets::erase(size_t pos, size_t len)
	{
		char * x = (char*)begin();
		return erase(x + pos, x + pos + len);
	}

	Octets& Octets::erase(void *x, void *y)
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
			fast_memmove((char*)x, (char*)y, ((char*)base + rep()->mLen) - (char*)y);
			rep()->mLen -= (char *)y - (char *)x;
		}
		return *this;
	}

	Octets& Octets::insert(void *pos, const void *x, size_t len)
	{
		ptrdiff_t off = (char *)pos - (char *)base;
		reserve(size() + len);
		pos = (char *)base + off;
		size_t adjust = size() - off;
		if ( adjust ) fast_memmove((char*)pos + len, pos, adjust);
		fast_memmove(pos, x, len);
		rep()->mLen += len;
		return *this;
	}

	Octets& Octets::insert(void *pos, const void *x, const void *y)
	{
		insert(pos, x, (char*)y - (char*)x);
		return *this;
	}

	Octets& Octets::resize(size_t size)
	{
		reserve(size);
		rep()->mLen = size;
		return *this;
	}

	void Octets::dump() const
	{
		const unsigned char *p = (const unsigned char *)begin();
		for(size_t i = 0; i < size(); i++)
			fprintf(stderr, "%02x ", *p++);
		fprintf(stderr, "\n");
	}

};
