//  CEGUILoadingTaskManager.c
//  CEGUI

#include "CEGUILoadingTaskManager.h"
#include "CEGUIResLoadThread.h"
#include <assert.h>

namespace CEGUI {
	CCEGUITaskManager* CCEGUITaskManager::s_Instance = NULL;
	CCEGUITaskManager* CCEGUITaskManager::GetInstancePtr()
	{
		if (s_Instance == NULL)
		{
			s_Instance = new CCEGUITaskManager;
		}

		return s_Instance;
	}

	// yeqing 2015-10-19
	void CCEGUITaskManager::destroy()
	{
		if (s_Instance != NULL)
		{
			delete s_Instance;
			s_Instance = NULL;
		}
	}

	bool CCEGUITaskManager::QueueTask(ITask* aPTask)
	{
		if(!aPTask) return false;
		if (core::Thread::m_iFireCounter < core::Thread::m_iLimitFires + 10)
		{
			return queueTask(aPTask);
		}
		else
		{
			m_vCacheTasks.push(aPTask);
		}
		return true;
	}

	bool CCEGUITaskManager::queueTask(ITask* aPTask)
	{
		m_mutexQueue.Lock();
		if (aPTask->GetPri() == ITask::eTPFile) {
			m_mapFileTasks[aPTask->GetPriority()].push_back(aPTask);
		}
		else if (aPTask->GetPri() == ITask::eTPParse){
			m_vParseTasks.push_back(aPTask);
		}
		else if (aPTask->GetPri() == ITask::eTPFont){
			m_vFontTasks.push_back(aPTask);
		}
		else{
			return false;
		}
		m_mutexQueue.UNLock();
		g_LoadResSem.fire();
		core::Thread::m_iFireCounter++;
		return true;
	}

	ITask* CCEGUITaskManager::GetTask(ITask::enumThread eThread)
	{
		ITask* pRet = NULL;
		m_mutexQueue.Lock();

		if (!m_vFontTasks.empty() && eThread != ITask::eMainThread) {
			pRet = m_vFontTasks.front();
			m_vFontTasks.pop_front();
		}
		else{
			if (!m_vParseTasks.empty())
			{
				pRet = m_vParseTasks.front();
				m_vParseTasks.pop_front();
			}
			else if (!m_mapFileTasks.empty()) {
				//pRet = m_vFileTasks.front();
				//m_vFileTasks.pop_front();
				std::map<float, std::list<ITask*> >::iterator ret = m_mapFileTasks.begin();
				pRet = ret->second.front();
				ret->second.pop_front();
				if (ret->second.empty()) {
					m_mapFileTasks.erase(ret);
				}
			}
		}

		m_mutexQueue.UNLock();
		return pRet;
	}

	bool    CCEGUITaskManager::LoadingFontEmpty()
	{
		m_mutexQueue.Lock();
		bool bRet = m_vFontTasks.empty();
		m_mutexQueue.UNLock();

		return bRet;
	}

	void CCEGUITaskManager::Update()
	{
		while (!m_vCacheTasks.empty())
		{
			if (core::Thread::m_iFireCounter < core::Thread::m_iLimitFires)
			{
				ITask* pTask = m_vCacheTasks.front();
				queueTask(pTask);
				m_vCacheTasks.pop();
			}
			else
			{
				break;
			}
		}
	}
}