#ifndef __Nuclear_XLOCK_H__
#define __Nuclear_XLOCK_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <platform/mutex.h>

namespace Nuclear
{

	class NuclearLock
	{
		core::CMutex    m_mutex;

	public:
		NuclearLock();
		~NuclearLock();

	public:
		void Lock();
		void Unlock();
		void SetSpinCount(int count);

	private:
		NuclearLock(const NuclearLock&);
		NuclearLock& operator=(const NuclearLock&);

	};

};

#endif
