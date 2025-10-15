#include "stdafx.h"
#include "xlock.h"

namespace Nuclear
{
	
	XCSLock::XCSLock() 
	{ 
		InitializeCriticalSection(&m_cs); 
	}

	XCSLock::~XCSLock() 
	{ 
		DeleteCriticalSection(&m_cs); 
	}

	void XCSLock::SetSpinCount(int count)
	{
		SetCriticalSectionSpinCount(&m_cs, count);
	}

	void XCSLock::Lock() 
	{ 
		EnterCriticalSection(&m_cs); 
	}

	void XCSLock::Unlock() 
	{ 
		LeaveCriticalSection(&m_cs); 
	}

};
