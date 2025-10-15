//  semaphore.cpp
//  share

#include "ksemaphore.h"
#include <assert.h>
#include <errno.h>
#include <string.h>
#include <stdio.h>
#include <fcntl.h>

namespace core
{
	CSemaphore::CSemaphore(const char* aName)
	{
		m_pSemName = aName;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		val = 0;
#else
#ifdef ANDROID 
		m_sem = new sem_t;
		sem_init(m_sem, 0, 0);
#else
		m_sem = sem_open(aName, O_CREAT, 0644, 0);
#endif
		if (m_sem == SEM_FAILED)
		{
		}

		assert(m_sem != SEM_FAILED);
#endif
	}

	CSemaphore::~CSemaphore()
	{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)

#else
		sem_unlink(m_pSemName);
		sem_close(m_sem);
#ifdef ANDROID
		delete m_sem;
#endif
#endif
	}

	int CSemaphore::wait()
	{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		std::unique_lock<std::mutex> lock(mutex);
		if (val <= 0) {
			condition.wait(lock);
		}
		val--;
		return val;
#else
		return sem_wait(m_sem);
#endif
	}

	void CSemaphore::fire()
	{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
		std::unique_lock<std::mutex> lock(mutex);
		condition.notify_one();
		val++;
#else
		sem_post(m_sem);
#endif
	}

}
