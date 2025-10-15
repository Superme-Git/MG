#ifndef __FIRENET_LINUX_MUTEX_HPP__
#define __FIRENET_LINUX_MUTEX_HPP__
#pragma once

#include <pthread.h>

namespace FireNet
{

	class Mutex
	{
		pthread_mutex_t mutex;

		Mutex(const Mutex&);
		void operator=(const Mutex&);
	public:
		Mutex()
		{
         pthread_mutex_init(&mutex, NULL);
		}
		~Mutex()
		{
			pthread_mutex_destroy(&mutex);
		}

		void Lock() { pthread_mutex_lock(&mutex); }
		void UNLock() { pthread_mutex_unlock(&mutex); }

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
} // namespace aio

#endif