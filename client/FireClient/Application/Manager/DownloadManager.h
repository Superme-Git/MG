#ifndef DownloadManager_h
#define DownloadManager_h

#include "CCPlatformMacros.h"
/*#if CC_TARGET_PLATFORM==CC_PLATFORM_IOS
#include <AudioToolbox/CAFFile.h>
#include "sys/types.h"
#include "sys/sysctl.h"
#define MAKEWORD(a, b)      ((WORD)(((BYTE)(((DWORD_PTR)(a)) & 0xff)) | ((WORD)((BYTE)(((DWORD_PTR)(b)) & 0xff))) << 8))
#endif 
*/
#include "network/HttpClient.h"
#include "network/HttpRequest.h"
#include "network/HttpResponse.h"

#include "GameApplication.h"
#include "Utils/base64.h"
#include "Singleton.hpp"
#include "utils/StringCover.h"
#include "utils/FileUtil.h"
#include "ConfigManager.h"

#include <fstream>

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

class DownloadInfoBase
{
public:
protected:
private:
};

class DownloadInfo : public DownloadInfoBase
{
public:
	DownloadInfo()
	{
		m_ResultCode = 0;
		m_iID = 0;
		m_pData = NULL;
		m_uiSize = 0;
	}
	int m_ResultCode;
	std::wstring m_wsKey;
	int m_iID;
	void* m_pData;
	std::wstring m_wsSrcURL;
	std::wstring m_wsDstFileName;
	unsigned int m_uiSize;
protected:
private:
};

class DMSlotFunctorBase
{
public:
	virtual ~DMSlotFunctorBase() {};
	virtual bool operator()(DownloadInfo& pDownloadInfo) = 0;
};

template<typename T>
class DMMemberFunctionSlot : public DMSlotFunctorBase
{
public:

	typedef bool(T::*DMMemberFunctionType)(DownloadInfo& pDownloadInfo);

	DMMemberFunctionSlot(DMMemberFunctionType AFunction, T* AClass) :
		m_Function(AFunction),
		m_Class(AClass)
	{
	}

	virtual bool operator()(DownloadInfo& pDownloadInfo)
	{
		return (m_Class->*m_Function)(pDownloadInfo);
	}

private:
	DMMemberFunctionType m_Function;
	T* m_Class;

};
//static cocos2d::extension::CCHttpRequest m_Request;
class DownloadOne : public cocos2d::extension::CCHttpResponse
{
	DownloadInfo m_pDownloadInfo;
	DMSlotFunctorBase* m_DMMemberFunctionSlot;
public:
	int m_State;
public:
	DownloadOne();
	~DownloadOne();
	template<typename T>
	void Init(DownloadInfo& pDownloadInfo, bool (T::*AFunction)(DownloadInfo& pDownloadInfo), T* AClass)
	{
		m_pDownloadInfo.m_wsKey = pDownloadInfo.m_wsKey;
		m_pDownloadInfo.m_iID = pDownloadInfo.m_iID;
		m_pDownloadInfo.m_pData = pDownloadInfo.m_pData;
		m_pDownloadInfo.m_wsSrcURL = pDownloadInfo.m_wsSrcURL;
		m_pDownloadInfo.m_wsDstFileName = pDownloadInfo.m_wsDstFileName;
		m_DMMemberFunctionSlot = new DMMemberFunctionSlot<T>(AFunction, AClass);
	}
	void Start();
	void Stop();
	void onGetData(cocos2d::extension::CCHttpClient* HC, cocos2d::extension::CCHttpResponse* HR);
	void onGetDataDiscard(cocos2d::extension::CCHttpClient* HC, cocos2d::extension::CCHttpResponse* HR);
	bool IsMe(std::wstring wsKey, int iID, void* pData, std::wstring wsSrcURL, std::wstring wsDstFileName);
	
};

class DownloadManager
{
	static std::map<std::wstring, DownloadOne*> m_TaskWait;
	static std::map<std::wstring, DownloadOne*> m_TaskRun;
	static int m_SyncTaskCount;
	
public:
	DownloadManager();
	~DownloadManager();
	void ClearOverTask();
	void SetSyncTaskCount(int SyncTaskCount);
	int GetSyncTaskCount();
	int Run();
	template<typename T>
	DownloadOne* AddTask(std::wstring wsKey, int iID, void* pData, std::wstring wsSrcURL, std::wstring wsDstFileName, bool (T::*AFunction)(DownloadInfo& pDownloadInfo), T* AClass)
	{
		DownloadOne* pDO = FindTask(wsDstFileName);
		if (pDO != NULL && pDO->m_State != 2)
		{
			if (pDO->IsMe(wsKey, iID, pData, wsSrcURL, wsDstFileName) == false)
			{
				return NULL;
			}
			return pDO;
		}
		DownloadInfo pDownloadInfo;
		pDownloadInfo.m_wsKey = wsKey;
		pDownloadInfo.m_iID = iID;
		pDownloadInfo.m_wsSrcURL = wsSrcURL;
		pDownloadInfo.m_wsDstFileName = wsDstFileName;
		pDO = new DownloadOne();
		pDO->Init<T>(pDownloadInfo, AFunction, AClass);
		m_TaskWait[wsDstFileName] = pDO;
		return pDO;
	}
	void StopTask(std::wstring wsDstFileName);
	DownloadOne* FindTask(std::wstring wsDstFileName);
	DownloadOne* FindTaskWait(std::wstring wsDstFileName);
	DownloadOne* FindTaskRun(std::wstring wsDstFileName);

};

static DownloadManager* g_DownloadManager = NULL;

inline DownloadManager* GetDownloadManager()
{
	if (g_DownloadManager == NULL)
	{
		g_DownloadManager = new DownloadManager();
	}
	return g_DownloadManager;
}
inline void RemoveDownloadManager()
{
	if (g_DownloadManager != NULL)
	{
		delete g_DownloadManager;
		g_DownloadManager = NULL;
	}
}

#endif