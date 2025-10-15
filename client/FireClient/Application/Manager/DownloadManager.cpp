#include "DownloadManager.h"
#include "ljfmopen.h"
using namespace LJFM;

DownloadOne::DownloadOne() : CCHttpResponse(new cocos2d::extension::CCHttpRequest())
{
	m_State = 0;
	m_DMMemberFunctionSlot = NULL;
}
DownloadOne::~DownloadOne()
{
	if (m_DMMemberFunctionSlot)
	{
		delete m_DMMemberFunctionSlot;
	}
}
void DownloadOne::Start()
{
	LJFMOpen::GetOutLogInstance()->Print(L"DownloadOne::Start\n");
	std::string sSrcURL = ws2s(m_pDownloadInfo.m_wsSrcURL);
	getHttpRequest()->setUrl(sSrcURL.c_str());
	getHttpRequest()->setTag("get data");
	getHttpRequest()->setRequestType(cocos2d::extension::CCHttpRequest::kHttpGet);
	getHttpRequest()->setResponseCallback((cocos2d::CCObject*)this, httpresponse_selector(DownloadOne::onGetData));
	cocos2d::extension::CCHttpClient* HC = cocos2d::extension::CCHttpClient::getInstance();
	HC->setTimeoutForConnect(10);
	HC->send(getHttpRequest());
	m_State = 1;
}
void DownloadOne::Stop()
{
	getHttpRequest()->setResponseCallback((cocos2d::CCObject*)this, httpresponse_selector(DownloadOne::onGetDataDiscard));
	if (m_State == 0)
	{
		m_State = 2;
	}
}
void DownloadOne::onGetData(cocos2d::extension::CCHttpClient* HC, cocos2d::extension::CCHttpResponse* HR)
{
	LJFMOpen::GetOutLogInstance()->Print(L"DownloadOne::onGetData\n");
	int ResultCode = HR->getResponseCode();
	if (ResultCode == 200)
	{
		std::vector<char>* RD = HR->getResponseData();
		std::ofstream FS(ws2s(m_pDownloadInfo.m_wsDstFileName).c_str(), std::ios_base::binary);
		FS.write(RD->data(), RD->size());
		FS.close();
		m_pDownloadInfo.m_uiSize = RD->size();

	}
	m_pDownloadInfo.m_ResultCode = ResultCode;
	(*m_DMMemberFunctionSlot)(m_pDownloadInfo);
	m_State = 2;
}
void DownloadOne::onGetDataDiscard(cocos2d::extension::CCHttpClient* HC, cocos2d::extension::CCHttpResponse* HR)
{
	m_State = 2;
}
bool DownloadOne::IsMe(std::wstring wsKey, int iID, void* pData, std::wstring wsSrcURL, std::wstring wsDstFileName)
{
	if (m_pDownloadInfo.m_wsKey != wsKey)
	{
		return false;
	}
	if (m_pDownloadInfo.m_iID != iID)
	{
		return false;
	}
	if (m_pDownloadInfo.m_pData != pData)
	{
		return false;
	}
	if (m_pDownloadInfo.m_wsSrcURL != wsSrcURL)
	{
		return false;
	}
	if (m_pDownloadInfo.m_wsDstFileName != wsDstFileName)
	{
		return false;
	}
	return true;
}

std::map<std::wstring, DownloadOne*> DownloadManager::m_TaskWait;
std::map<std::wstring, DownloadOne*> DownloadManager::m_TaskRun;
int DownloadManager::m_SyncTaskCount = 1;

DownloadManager::DownloadManager()
{
	m_SyncTaskCount = 1;
}
DownloadManager::~DownloadManager()
{
	//cocos2d::extension::CCHttpClient* HC = cocos2d::extension::CCHttpClient::getInstance();
	//HC->destroyInstance();//ios exit is crash
	if (m_TaskWait.size() > 0)
	{
		std::map<std::wstring, DownloadOne*>::iterator it = m_TaskWait.begin();
		while (it != m_TaskWait.end())
		{
			delete it->second;
			m_TaskWait.erase(it++);
		}
	}
	if (m_TaskRun.size() > 0)
	{
		std::map<std::wstring, DownloadOne*>::iterator it = m_TaskRun.begin();
		while (it != m_TaskRun.end())
		{
			delete it->second;
			m_TaskRun.erase(it++);
		}
	}
}
void DownloadManager::ClearOverTask()
{
	{
		std::map<std::wstring, DownloadOne*>::iterator it = m_TaskWait.begin();
		while (it != m_TaskWait.end())
		{
			if (it->second->m_State == 2)
			{
				delete it->second;
				m_TaskWait.erase(it++);
			}
			else
			{
				it++;
			}
		}
	}
	{
		std::map<std::wstring, DownloadOne*>::iterator it = m_TaskRun.begin();
		while (it != m_TaskRun.end())
		{
			if (it->second->m_State == 2)
			{
				delete it->second;
				m_TaskRun.erase(it++);
			}
			else
			{
				it++;
			}
		}
	}
}
void DownloadManager::SetSyncTaskCount(int SyncTaskCount)
{
	m_SyncTaskCount = SyncTaskCount;
}
int DownloadManager::GetSyncTaskCount()
{
	return m_SyncTaskCount;
}
int DownloadManager::Run()
{
	ClearOverTask();
	if ((int)m_TaskRun.size() < m_SyncTaskCount)
	{
		if (m_TaskWait.size() > 0)
		{
			std::wstring wsKey = m_TaskWait.begin()->first;
			DownloadOne* pDO = m_TaskWait.begin()->second;
			m_TaskWait.erase(m_TaskWait.begin());
			m_TaskRun[wsKey] = pDO;
			pDO->Start();
		}
	}
	return 0;
}
void DownloadManager::StopTask(std::wstring wsDstFileName)
{
	DownloadOne* pDO = FindTask(wsDstFileName);
	if (pDO)
	{
		pDO->Stop();
	}
}
DownloadOne* DownloadManager::FindTask(std::wstring wsDstFileName)
{
	DownloadOne* pResult = NULL;
	pResult = FindTaskWait(wsDstFileName);
	if (pResult != NULL)
	{
		return pResult;
	}
	pResult = FindTaskRun(wsDstFileName);
	return pResult;
}
DownloadOne* DownloadManager::FindTaskWait(std::wstring wsDstFileName)
{
	std::map<std::wstring, DownloadOne*>::iterator it = m_TaskWait.find(wsDstFileName);
	if (it != m_TaskWait.end())
	{
		LJFMOpen::GetOutLogInstance()->Print(L"DownloadOne::FindTaskWait\n");
		return it->second;
	}
	return NULL;
}
DownloadOne* DownloadManager::FindTaskRun(std::wstring wsDstFileName)
{
	std::map<std::wstring, DownloadOne*>::iterator it = m_TaskRun.find(wsDstFileName);
	if (it != m_TaskRun.end())
	{
		LJFMOpen::GetOutLogInstance()->Print(L"DownloadOne::FindTaskRun\n");
		return it->second;
	}
	return NULL;
}