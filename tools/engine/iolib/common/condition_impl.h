#pragma once

#include "winiolibdef.h"

class  condition_impl 
{
public:
    condition_impl();
    ~condition_impl();
	
    void notify_one();
    void notify_all();
	
    void enter_wait();
    void do_wait();
    bool do_timed_wait( int nseconds);
	
    HANDLE m_gate;
    HANDLE m_queue;
    HANDLE m_mutex;
    unsigned m_gone;  // # threads that timed out and never made it to m_queue
    unsigned long m_blocked; // # threads blocked on the condition
    unsigned m_waiting; // # threads no longer waiting for the condition but
	// still waiting to be removed from m_queue
};
