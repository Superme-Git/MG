#include "stdafx.h"
#include <process.h>
#include <algorithm>
#include "log.h"
#include "executor.h"

//todo
typedef DWORD (WINAPI *TypeQueueUserAPC)(
  void (CALLBACK* pfnAPC)(void*),
  HANDLE hThread,
  void* dwData
);

namespace
{
	TypeQueueUserAPC myQueueUserAPC = (TypeQueueUserAPC)::GetProcAddress(::GetModuleHandle(L"kernel32.dll"), "QueueUserAPC");
};

namespace Nuclear
{
	Executor::Executor()
		: m_bRunning(false), m_hOwnerThread(NULL), m_hExecutorThread(NULL), m_hExitEvent(NULL),
	      m_uThreadID(0), m_maskTask(0)
	{
	}

	bool Executor::Init()
	{
		if( myQueueUserAPC == NULL ) // todo
			return false;
		if( m_bRunning ) 
			return false;
		if( ! ::DuplicateHandle(GetCurrentProcess(), GetCurrentThread(), GetCurrentProcess(), &m_hOwnerThread, NULL, FALSE, DUPLICATE_SAME_ACCESS) )
			return false;
		m_hExitEvent = ::CreateEvent(NULL, FALSE, FALSE, NULL);
		if( m_hExitEvent == NULL )
		{
			::CloseHandle(m_hOwnerThread);
			return false;
		}
		m_hExecutorThread = (HANDLE)_beginthreadex(NULL, 0, ExecutorThreadFunc, this, 0, &m_uThreadID);
		if( m_hExecutorThread == NULL )
		{
			::CloseHandle(m_hOwnerThread);
			::CloseHandle(m_hExitEvent);
			return false;
		}
		m_bRunning = true;
		return true;
	}

	bool Executor::Destroy()
	{
		::SetEvent(m_hExitEvent);
		::WaitForSingleObject(m_hExecutorThread, INFINITE);
		::CloseHandle(m_hExecutorThread);
		::CloseHandle(m_hOwnerThread);
		return true;
	}

	bool Executor::AddTask(Task *pTask)
	{
		if( !m_bRunning ) 
			return false;
		if( pTask == NULL )
			return false;
		pTask->m_pExecutor = this;
		return 0 != myQueueUserAPC(AddTaskFunc, m_hExecutorThread, pTask);
	}

	void Executor::UpdateExecuteResult()
	{
		if( !m_bRunning )
			return;
		myQueueUserAPC(DummyFunc, ::GetCurrentThread(), NULL);
		::SleepEx(0, TRUE);
	}

	void __stdcall Executor::AddTaskFunc(void *pParam)
	{
		Task *pTask = (Task*)pParam;
		Executor *pExecutor = pTask->m_pExecutor;
		pExecutor->m_queTask[pTask->GetPriority()].push(pTask);
		pExecutor->m_maskTask |= ( 1 << pTask->GetPriority() );
	}

	unsigned int __stdcall Executor::ExecutorThreadFunc(void *pParam)
	{
		Executor *pExecutor = (Executor*)pParam;
		while( true )
		{
			if( pExecutor->m_maskTask != 0 )
			{
				for(int iPrior = ePriorityCount-1; iPrior >=0; --iPrior)
				{
					if( ((1<<iPrior)&pExecutor->m_maskTask) != 0 )
					{
						std::queue<Task*> &que = pExecutor->m_queTask[iPrior];
						Task *pTask = que.front();
						que.pop();
						if( !pTask->m_bCanceled )
							pTask->Execute();
						pTask->ExecuteEndAndDestroy();
						if( que.empty() )
							pExecutor->m_maskTask &= (~(1<<iPrior));
						break;
					}
				}
			}
			DWORD dwRet = ::WaitForSingleObjectEx(pExecutor->m_hExitEvent, 
				pExecutor->m_maskTask == 0 ? INFINITE : 0, TRUE);
			if( WAIT_OBJECT_0 == dwRet )
			{
				for(int iPrior = 0; iPrior < ePriorityCount; ++iPrior)
				{
					std::queue<Task*> &que = pExecutor->m_queTask[iPrior];
					while( !que.empty())
					{
						que.front()->UnexecuteAndDestroy();
						que.pop();
					}
				}
				break;
			}
		}
		return 0;
	}

	void Executor::SimpleTask::ExecuteEndAndDestroy()
	{
		Destroy();
	}

	void Executor::SimpleTask::UnexecuteAndDestroy()
	{
		Destroy();
	}
	void __stdcall Executor::CallbackTask::CallbackFunc(void *pParam)
	{
		CallbackTask *pTask = (CallbackTask*)pParam;
		pTask->Callback();
	}

	void Executor::CallbackTask::ExecuteEndAndDestroy()
	{
		myQueueUserAPC(CallbackFunc, m_pExecutor->m_hOwnerThread, this);
	}

	void Executor::CallbackTask::UnexecuteAndDestroy()
	{
		XPTRACE(L"Task±»¶ªÆú\n");
	}

};