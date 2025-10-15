#ifndef __FIRENET_OCTETS_H__
#define __FIRENET_OCTETS_H__

namespace FireNet
{
	void *fast_memmove(void *dest, const void *src, size_t n);

	class Mutex;
	struct Octets_Rep
	{
		size_t mCap;
		size_t mLen;
		volatile size_t mRef;

		void addref();
		void release();

		void* data();

		void* clone();
		void* unique();
		void* reserve(size_t size);

		static size_t frob_size(size_t size);
		static Octets_Rep* create(size_t cap);

		static void* operator new (size_t size, size_t extra);
		static void  operator delete (void *p);
		static Octets_Rep mNnull;
        static FireNet::Mutex mLocker;
	};

	class Octets
	{
		void *base;
		Octets_Rep *rep() const;
		void unique();
	public:
		Octets& reserve(size_t size);
		Octets& replace(const void *data, size_t size);

		virtual ~Octets();

		Octets();
		Octets(size_t size);
		Octets(const void *x, size_t size);
		Octets(const void *x, const void *y);
		Octets(const Octets &x);

		Octets& operator = (const Octets&x);
		bool operator == (const Octets &x) const;
		bool operator != (const Octets &x) const;

		Octets& swap(Octets &x);

		void *begin();
		void *end();
		const void *begin() const;
		const void *end()   const;

		size_t size()     const;
		size_t capacity() const;
		Octets& clear();
		Octets& erase(size_t pos, size_t len);
		Octets& erase(void *x, void *y);

		Octets& insert(void *pos, const void *x, size_t len);
		Octets& insert(void *pos, const void *x, const void *y);
		Octets& resize(size_t size);

		void dump() const;
	};

} // namespace FireNet {

#endif
