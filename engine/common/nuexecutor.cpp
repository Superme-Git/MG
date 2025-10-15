#include <algorithm>
#include "nulog.h"
#include "nuexecutor.h"
#include "../engine/nuengine.h"

#include <platform/thread.h>

namespace Nuclear
{
	core::CMutex    ExecThread::m_QueueMutex;
	core::CSemaphore* ExecThread::m_QueueSem = new core::CSemaphore("Nuclear::Excutor");

	ExecThread::ExecThread()
		: Thread(), m_uThreadID(0), m_maskTask(0)
	{
	}

	bool ExecThread::Init()
	{
		if (IsRunningNow()) return false;

		Start();

		return true;
	}

	bool ExecThread::Destroy()
	{
		m_cacheQueTask = std::queue<Task*>();  // clear tasks which have not been executed

		{
			core::CMutex::Scoped autoMutex(ExecThread::m_QueueMutex);

			for (int i = 0; i < ePriorityCount; ++i)
			{
				m_queTask[i] = std::queue<Task*>();  // clear tasks which have not been executed
			}
		}

		{
			while (!m_cacheQueTask.empty())
			{
				Task* pTask = m_cacheQueTask.front();
				pTask->UnexecuteAndDestroy();
				m_cacheQueTask.pop();
			}
		}

		Close();

		return true;
	}

	// yeqing 2015-10-20
	void ExecThread::Close()
	{
		StopRunning();

		if (m_QueueSem)
		{
			m_QueueSem->fire();

			Join();

			if (m_QueueSem)
			{
				delete m_QueueSem;
				m_QueueSem = NULL;
			}
		}
	}

	bool ExecThread::AddTask(Task *pTask)
	{
		if (!IsRunningNow())
			return false;
		if (pTask == NULL)
			return false;

		pTask->m_pExecutor = this;
		if (core::Thread::m_iFireCounter < core::Thread::m_iLimitFires - 1)
		{
			return addTask(pTask);
		}
		else
		{
			m_cacheQueTask.push(pTask);
		}

		return true;
	}

	bool ExecThread::addTask(Task *pTask)
	{
		if (m_QueueSem)
		{
			core::CMutex::Scoped autoMutex(ExecThread::m_QueueMutex);
			this->m_queTask[pTask->GetPriority()].push(pTask);
			this->m_maskTask |= (1 << pTask->GetPriority());

			m_QueueSem->fire();
			core::Thread::m_iFireCounter++;
			return true;
		}
		return false;
	}

	void ExecThread::Run()
	{
		while (true)
		{
			if (m_QueueSem)
			{
				m_QueueSem->wait();
			}

			if (this->m_maskTask != 0)
			{
				for (int iPrior = ePriorityCount - 1; iPrior >= 0; --iPrior)
				{
					if (((1 << iPrior)&this->m_maskTask) != 0)
					{
						Task *pTask = NULL;
						{
							core::CMutex::Scoped autoMutex(ExecThread::m_QueueMutex);
							std::queue<Task*> &que = this->m_queTask[iPrior];
							pTask = que.front();
							que.pop();
						}

						if (pTask)
						{
							if (!pTask->m_bCanceled)
								pTask->Execute();
							pTask->ExecuteEndAndDestroy();
						}

						{
							core::CMutex::Scoped autoMutex(ExecThread::m_QueueMutex);
							std::queue<Task*> &que = this->m_queTask[iPrior];
							if (que.empty())
								this->m_maskTask &= (~(1 << iPrior));
						}
					}
				}
			}

			if (!IsRunningNow())
			{
				core::CMutex::Scoped autoMutex(ExecThread::m_QueueMutex);
				for (int iPrior = 0; iPrior < ePriorityCount; ++iPrior)
				{
					std::queue<Task*> &que = this->m_queTask[iPrior];
					while (!que.empty())
					{
						que.front()->UnexecuteAndDestroy();
						que.pop();
					}
				}
				break;
			}
		}
	}

	void ExecThread::Update()
	{
		if (m_cacheQueTask.empty() || !m_QueueSem) return;

		while (!m_cacheQueTask.empty())
		{
			if (core::Thread::m_iFireCounter < core::Thread::m_iLimitFires)
			{
				Task* pTask = m_cacheQueTask.front();
				if (!addTask(pTask))
				{
					pTask->UnexecuteAndDestroy();
				}
				m_cacheQueTask.pop();
			}
			else
			{
				break;
			}
		}
	}

	void ExecThread::SimpleTask::ExecuteEndAndDestroy()
	{
		Destroy();	// yeqing 2015-11-12
	}

	void ExecThread::SimpleTask::UnexecuteAndDestroy()
	{
		Destroy();	// yeqing 2015-11-12
	}

	void  ExecThread::CallbackTask::CallbackFunc(void *pParam)
	{
		CallbackTask *pTask = (CallbackTask*)pParam;
		pTask->Callback();
	}

	void ExecThread::CallbackTask::ExecuteEndAndDestroy()
	{
		CallbackFunc(this);
	}

	void ExecThread::CallbackTask::UnexecuteAndDestroy()
	{
		XPTRACE(L"Task被丢弃\n");
	}

};
