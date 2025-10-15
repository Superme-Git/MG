#ifndef __ATOMIC__
#define __ATOMIC__

namespace atomic_helper
{

template <typename T>
class Atomic
{
	T val;
public:
	Atomic(T _val = 0) : val(_val) { }

	Atomic(const Atomic& rhs) : val(rhs.val) { }

	Atomic GetAndAdd(T delta)
	{
		T prev;
		__asm__ __volatile__
		(
			"	lock		\n"
			"	xadd	%2, %0	\n"
			: "=m"(val), "=&r"(prev) 
			: "1"(delta), "m"(val) 
			: "cc"
		);
		return prev;
	}

	Atomic& AddAndGet(T delta)
	{
		__asm__ __volatile__
		(
			"	lock		\n"
			"	add	%1, %0	\n"
			: "=m"(val) 
			: "r"(delta), "m"(val) 
			: "cc"
		);
		return *this;
	}

	Atomic GetAndSet(T update)
	{
		T prev;
		__asm__ __volatile__
		(
			"	xchg	%2, %0	\n"
			: "=m"(val), "=&r"(prev) 
			: "1"(update), "m"(val)
		);
		return prev;
	}

	bool TestAndSet(T expect, T update)
	{
		T prev;
		__asm__ __volatile__
		(
			"	lock		\n"
			"	cmpxchg	%3, %0	\n"
			: "=m"(val), "=&a"(prev) 
			: "1"(expect), "r"(update), "m"(val)
			: "cc"
		);
		return prev == expect;
	}

	operator T() const { return val; }
	Atomic& operator = (T _val) { val = _val; return *this; }
	Atomic& operator = (const Atomic& rhs) { val = rhs.val; return *this; }
	Atomic& operator ++() { return AddAndGet(1); }
	Atomic& operator --() { return AddAndGet(-1); }
	Atomic& operator +=(T delta) { return AddAndGet(delta); }
	Atomic& operator -=(T delta) { return AddAndGet(-delta); }
	Atomic operator ++(int) { return GetAndAdd(1); }
	Atomic operator --(int) { return GetAndAdd(-1); }

};

};

typedef atomic_helper::Atomic<int> AtomicInteger;
typedef atomic_helper::Atomic<long> AtomicLong;

#endif

