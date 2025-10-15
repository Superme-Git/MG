//#include "stdafx.h"
#include "nuxlock.h"

namespace Nuclear
{
	
	NuclearLock::NuclearLock() 
	{ 
		//InitializeCriticalSection(&m_cs); 
	}

	NuclearLock::~NuclearLock() 
	{ 
		//DeleteCriticalSection(&m_cs); 
	}

	void NuclearLock::SetSpinCount(int count)
	{
		//SetCriticalSectionSpinCount(&m_cs, count);
	}

	void NuclearLock::Lock() 
	{
        m_mutex.Lock();
		//EnterCriticalSection(&m_cs); 
	}

	void NuclearLock::Unlock() 
	{
        m_mutex.UNLock();
		//LeaveCriticalSection(&m_cs); 
	}

};
