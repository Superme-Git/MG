#ifndef __FIRENET_LINUX_THREAD_HPP__
#define __FIRENET_LINUX_THREAD_HPP__
#pragma once

#include <set>
#include <pthread.h>

namespace aio
{
	class Thread
	{
	private:
		pthread_t thread;
		volatile bool running;

		static void * ThreadStart(void *arg)
		{
			Thread * thread = (Thread*)arg;
			thread->Run();
			return 0;
		}

		Thread(const Thread&) {}
		void operator=(const Thread&) {}

	public:
		Thread() : thread( 0), running( false)
		{}

		virtual ~Thread() {}

		virtual bool IsRunningNow() { return running; }
		virtual void StopRunning() { running = false; }

		virtual void Start()
		{
			running = true;
			pthread_create( &thread, NULL, ThreadStart, this);
		}

		virtual void Join()
		{
			void*	result;
			pthread_join( thread, &result);
		}

		virtual void Run() = 0;
	};

} // namespace aio

#endif