
#include "condition_impl.h"
#include <LIMITS.H>
#include <assert.H>

condition_impl::condition_impl()
	: m_gone(0), m_blocked(0), m_waiting(0)
{
    m_gate = CreateSemaphore(0, 1, 1, 0);
    m_queue = CreateSemaphore(0, 0, INT_MAX, 0);
    m_mutex = CreateMutex(0, 0, 0);
}

condition_impl::~condition_impl()
{
    int res = 0;
    res = CloseHandle( m_gate);
    assert(res);
    res = CloseHandle( m_queue);
    assert(res);
    res = CloseHandle( m_mutex);
    assert(res);
}

void condition_impl::notify_one()
{
    unsigned signals = 0;
	
    int res = 0;
    res = WaitForSingleObject( m_mutex, INFINITE);
    assert(res == WAIT_OBJECT_0);
	
    if (m_waiting != 0) // the m_gate is already closed
	{
		if (m_blocked == 0)
		{
			res = ReleaseMutex( m_mutex);
			assert(res);
			return;
		}
		
		++m_waiting;
		--m_blocked;
		signals = 1;
	}
    else
	{
		res = WaitForSingleObject( m_gate, INFINITE);
		assert(res == WAIT_OBJECT_0);
		if (m_blocked > m_gone)
		{
			if (m_gone != 0)
			{
				m_blocked -= m_gone;
				m_gone = 0;
			}
			signals = m_waiting = 1;
			--m_blocked;
		}
		else
		{
			res = ReleaseSemaphore( m_gate, 1, 0);
			assert(res);
		}
	}
	
    res = ReleaseMutex( (m_mutex));
    assert(res);
	
    if (signals)
	{
		res = ReleaseSemaphore( m_queue, signals, 0);
		assert(res);
	}
}

void condition_impl::notify_all()
{
    unsigned signals = 0;
	
    int res = 0;
    res = WaitForSingleObject( (m_mutex), INFINITE);
    assert(res == WAIT_OBJECT_0);
	
    if (m_waiting != 0) // the m_gate is already closed
	{
		if (m_blocked == 0)
		{
			res = ReleaseMutex( (m_mutex));
			assert(res);
			return;
		}
		
		m_waiting += (signals = m_blocked);
		m_blocked = 0;
	}
    else
	{
		res = WaitForSingleObject( (m_gate), INFINITE);
		assert(res == WAIT_OBJECT_0);
		if (m_blocked > m_gone)
		{
			if (m_gone != 0)
			{
				m_blocked -= m_gone;
				m_gone = 0;
			}
			signals = m_waiting = m_blocked;
			m_blocked = 0;
		}
		else
		{
			res = ReleaseSemaphore( (m_gate), 1, 0);
			assert(res);
		}
	}
	
    res = ReleaseMutex( (m_mutex));
    assert(res);
	
    if (signals)
	{
		res = ReleaseSemaphore( (m_queue), signals,
			0);
		assert(res);
	}
}

void condition_impl::enter_wait()
{
    int res = 0;
    res = WaitForSingleObject( (m_gate), INFINITE);
    assert(res == WAIT_OBJECT_0);
    ++m_blocked;
    res = ReleaseSemaphore( (m_gate), 1, 0);
    assert(res);
}

void condition_impl::do_wait()
{
    int res = 0;
    res = WaitForSingleObject( (m_queue), INFINITE);
    assert(res == WAIT_OBJECT_0);
	
    unsigned was_waiting=0;
    unsigned was_gone=0;
	
    res = WaitForSingleObject( (m_mutex), INFINITE);
    assert(res == WAIT_OBJECT_0);
    was_waiting = m_waiting;
    was_gone = m_gone;
    if (was_waiting != 0)
	{
		if (--m_waiting == 0)
		{
			if (m_blocked != 0)
			{
				res = ReleaseSemaphore( (m_gate), 1, 0); // open m_gate
				assert(res);
				was_waiting = 0;
			}
			else if (m_gone != 0)
				m_gone = 0;
		}
	}
    else if (++m_gone == (UINT_MAX / 2))
	{
		res = WaitForSingleObject( (m_gate), INFINITE);
		assert(res == WAIT_OBJECT_0);
		m_blocked -= m_gone;
		res = ReleaseSemaphore( (m_gate), 1, 0);
		assert(res);
		m_gone = 0;
	}
    res = ReleaseMutex( (m_mutex));
    assert(res);
	
    if (was_waiting == 1)
	{
		for (/**/ ; was_gone; --was_gone)
		{
			// better now than spurious later
			res = WaitForSingleObject( (m_queue),
				INFINITE);
			assert(res == WAIT_OBJECT_0);
		}
		res = ReleaseSemaphore( (m_gate), 1, 0);
		assert(res);
	}
}

bool condition_impl::do_timed_wait( int nseconds)
{
    bool ret = false;
    unsigned int res = 0;
	
    for (;;)
    {
		const int milliseconds = 1000; 
		
		int nLastTime = GetTickCount();
		
		res = WaitForSingleObject( (m_queue), milliseconds);
		assert(res != WAIT_FAILED && res != WAIT_ABANDONED);
		ret = (res == WAIT_OBJECT_0);
		
		if (res == WAIT_TIMEOUT)
		{
			
			if (  (int)GetTickCount() - nLastTime < nseconds)
				continue;
		}
		
		break;
	}
	
    unsigned was_waiting=0;
    unsigned was_gone=0;
	
    res = WaitForSingleObject( (m_mutex), INFINITE);
    assert(res == WAIT_OBJECT_0);
    was_waiting = m_waiting;
    was_gone = m_gone;
    if (was_waiting != 0)
	{
		if (!ret) // timeout
		{
			if (m_blocked != 0)
				--m_blocked;
			else
				++m_gone; // count spurious wakeups
		}
		if (--m_waiting == 0)
		{
			if (m_blocked != 0)
			{
				res = ReleaseSemaphore( (m_gate), 1,
					0); // open m_gate
				assert(res);
				was_waiting = 0;
			}
			else if (m_gone != 0)
				m_gone = 0;
		}
	}
    else if (++m_gone == (ULONG_MAX / 2))
	{
		// timeout occured, normalize the m_gone count
		// this may occur if many calls to wait with a timeout are made and
		// no call to notify_* is made
		res = WaitForSingleObject( (m_gate), INFINITE);
		assert(res == WAIT_OBJECT_0);
		m_blocked -= m_gone;
		res = ReleaseSemaphore( (m_gate), 1, 0);
		assert(res);
		m_gone = 0;
	}
    res = ReleaseMutex( (m_mutex));
    assert(res);
	
    if (was_waiting == 1)
	{
		for (/**/ ; was_gone; --was_gone)
		{
			// better now than spurious later
			res = WaitForSingleObject( (m_queue),
				INFINITE);
			assert(res ==  WAIT_OBJECT_0);
		}
		res = ReleaseSemaphore( (m_gate), 1, 0);
		assert(res);
	}
	
    return ret;
}