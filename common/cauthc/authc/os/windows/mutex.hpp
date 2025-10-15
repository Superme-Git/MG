#ifndef __FIRENET_MUTEX_HPP__
#define __FIRENET_MUTEX_HPP__

#pragma once

#include "../../cauthc/include/cauthc.h"

namespace FireNet
{

	class Mutex
	{
		CRITICAL_SECTION _cs;
		Mutex(const Mutex&);
		void operator=(const Mutex&);
	public:
		Mutex() 
		{ 
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
			BOOL ret = InitializeCriticalSectionEx(&_cs, 0, 0);
			if (!ret)
			{
				int i = GetLastError();
			}
#else
			InitializeCriticalSection(&_cs);
#endif
		}
		~Mutex()      { DeleteCriticalSection(&_cs); }

		bool CheckValid() { return _cs.DebugInfo != NULL/* && _cs.OwningThread != NULL*/; }
		void Lock()   { EnterCriticalSection(&_cs); }
		void UNLock() { LeaveCriticalSection(&_cs); }

		class Scoped
		{
			Mutex & mm;
			Scoped(const Scoped&);
			void operator=(const Scoped&);
		public:
			explicit Scoped(Mutex& mMember) : mm(mMember) { mm.Lock(); }
			explicit Scoped(Mutex* mMember) : mm(*mMember) { mm.Lock(); }
			~Scoped() { mm.UNLock(); }
		};
	};

	class AtomicLong
	{
	public:
		AtomicLong(long x = 0) : _value(x) { }
		long get() const { return _value; }
		long set(long x) { return this->exchange(x); }

		// 算术。仿java接口。
		long incrementAndGet() { return InterlockedIncrement(&_value); }
		long decrementAndGet() { return InterlockedDecrement(&_value); }
		long getAndAdd(long x) { return InterlockedExchangeAdd(&_value, x); }

		// 交换，比较。
		long exchange(long x)                        { return InterlockedExchange(&_value, x); }
		long exchangeIfEqual(long x, long comparand) { return InterlockedCompareExchange(&_value, x, comparand); }

	private:
		volatile long _value;
		AtomicLong & operator=(AtomicLong const &);
		AtomicLong(AtomicLong const &);
	};

} // namespace aio

#endif