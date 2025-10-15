#pragma once
#ifndef DownloadManager_Helper_H
#define DownloadManager_Helper_H
#include "DownloadOne.h"
#include "../Curl/curl.h"
class DownloadManager;
DownloadManager* GetDownloadManager();
class DownloadManager
{
public:
	list<DownloadOne*> m_Task;

	DownloadManager()
	{
		CURLcode code = curl_global_init(CURL_GLOBAL_WIN32);
		if (code != CURLE_OK)
		{
			code = code;
		}
	}
	~DownloadManager()
	{
		ClearAllTask();
		curl_global_cleanup();
	}
	int ClearAllTask()
	{
		list<DownloadOne*>::iterator it = m_Task.begin();
		while (it != m_Task.end())
		{
			DownloadOne* pCurDO = (DownloadOne*)*it;
			if (pCurDO != NULL)
			{
				pCurDO->m_CBFunc = NULL;
				if (pCurDO->m_iState == 0 || pCurDO->m_iState == 2 || pCurDO->m_iState == 3)
				{
					delete pCurDO;
					pCurDO = NULL;
				}
			}
			else
			{
			}
			it = m_Task.erase(it);
		}
		return 0;
	}
	int ClearOverTask()
	{
		list<DownloadOne*>::iterator it = m_Task.begin();
		while (it != m_Task.end())
		{
			DownloadOne* pCurDO = (DownloadOne*)*it;
			if (pCurDO != NULL)
			{
				if (pCurDO->m_iState == 2 || pCurDO->m_iState == 3)
				{
					it = m_Task.erase(it);
					pCurDO->m_CBFunc(pCurDO);
					delete pCurDO;
					pCurDO = NULL;
				}
				else
				{
					it++;
				}
			}
			else
			{
				it++;
			}
		}
		return 0;
	}
	DownloadOne* AddTask(wstring wsURL, wstring wsDst, DownloadCB_Func CBFunc)
	{
		DownloadOne* pResult = NULL;
		pResult = FindTask(wsDst);
		if (pResult)
		{
			return pResult;
		}
		pResult = new DownloadOne(wsURL, wsDst, CBFunc);
		m_Task.push_back(pResult);
		return pResult;
	}
	DownloadOne* AddTask(wstring wsURL, wstring wsDst, void* pData, DownloadCB_Func CBFunc)
	{
		DownloadOne* pResult = NULL;
		pResult = FindTask(wsDst);
		if (pResult)
		{
			return pResult;
		}
		pResult = new DownloadOne(wsURL, pData, CBFunc);
		m_Task.push_back(pResult);
		return pResult;
	}
	DownloadOne* FindTask(wstring wsDst)
	{
		list<DownloadOne*>::iterator it = m_Task.begin();
		while (it != m_Task.end())
		{
			DownloadOne* pCurDO = (DownloadOne*)*it;
			if (pCurDO)
			{
				if (pCurDO->m_wsDst == wsDst)
				{
					return pCurDO;
				}
			}
			it++;
		}
		return NULL;
	}
	int Run()
	{
		ClearOverTask();
		list<DownloadOne*>::iterator it = m_Task.begin();
		if (it != m_Task.end())
		{
			DownloadOne* pCurDO = (DownloadOne*)*it;
			if (pCurDO->m_iState == 0)
			{
				//pCurDO->m_tRun = thread([pCurDO]{pCurDO->Run(); });
				//pCurDO->m_tRun = thread(&DownloadOne::Run, pCurDO);
				//pCurDO->m_tRun = thread(std::bind(&DownloadOne::Run, pCurDO));

				pCurDO->Run();
			}
		}
		return 0;
	}
};
static DownloadManager* gs_pDownloadManager = NULL;
inline DownloadManager* GetDownloadManager()
{
	if (!gs_pDownloadManager)
	{
		gs_pDownloadManager = new DownloadManager();
	}
	return gs_pDownloadManager;
}
inline void ReleaseDownloadManager()
{
	if (gs_pDownloadManager)
	{
		delete gs_pDownloadManager;
	}
}
#endif