#ifndef UpdateManagerEx_H
#define UpdateManagerEx_H

#include <string>
#include "Singleton.hpp"
#include "ljfmext.h"

class UpdateManagerEx : public CSingleton <UpdateManagerEx>
{
public:

	std::wstring m_DownloadSite;
	std::wstring m_AppSite;
	int m_DownloadSiteIsBack;

	static void SetDownloadSite(std::wstring DownloadSite, std::wstring AppSite, std::wstring WGSite);
	std::wstring GetDownloadSite();

	std::wstring m_RootPath;
	std::wstring m_RootResPath;
	std::wstring m_CacheResPath;
	std::wstring m_CacheUpdatePath;

	std::wstring m_SystemType;
	std::wstring m_NetworkType;
    

    int m_FormResult;

	void* m_pVerRoot;
	void* m_pVerCache;
	void* m_pVerUpdate;

	void* m_pPIRoot;
	void* m_pPICache;
	void* m_pPIUpdate;

	void* m_pPIAdd;
	void* m_pPIMod;
	void* m_pPIDel;
    
    unsigned int m_VersionOld;
    std::wstring m_VersionOldCaption;
    unsigned int m_Version;
    std::wstring m_VersionCaption;
	unsigned int m_VersionBase;
	std::wstring m_VersionBaseCaption;
	unsigned int m_Channel;
	std::wstring m_ChannelCaption;

	std::map<std::wstring, std::wstring> m_ExtendMap;

	UpdateManagerEx();
	~UpdateManagerEx();

	int DelOldResFile();

	int StepInit();
	int StepInitPath();
	int StepLoadVersion();
	int StepCheckVersion();
	int StepLoadPackInfo();
	int StepCheckPackInfo();
	int StepDownloadPackInfo();
	int StepUpdatePackInfo();
	int StepClearPackInfo();
	virtual bool Run();
	
	int StepLoadVersionNoPack();
	virtual bool RunNoPack();
    
    virtual void Continue(int iResult);

	void CloneExtendMap(std::map<std::wstring, std::wstring>& ExtendMap);
};


#endif //UpdateManagerEx_H
