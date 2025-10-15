#ifndef UpdateManagerEx_H
#define UpdateManagerEx_H

#include "../stdafx.h"

using namespace std;

class UpdateManagerEx;
UpdateManagerEx* GetUpdateManagerEx();

class UpdateManagerEx
{
public:

	std::wstring m_DownloadSite;
	std::wstring m_AppSite;
	int m_DownloadSiteIsBack;

	static void SetDownloadSite(std::wstring DownloadSite, std::wstring AppSite);
	std::wstring GetDownloadSite();

	std::wstring m_RootPath;
	std::wstring m_RootResPath;
	std::wstring m_CacheResPath;
	std::wstring m_CacheUpdatePath;
	std::wstring m_CacheUpdatePathBin;
	std::wstring m_RootBinReleasePath;

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

	void* m_pBFLBase;// = LoadUpdateBinFileList(m_RootBinReleasePath + g_FileListBin);
	void* m_pBFLNew;

	void* m_pBFLAdd;
	void* m_pBFLMod;
	void* m_pBFLDel;

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

	int StepInit();
	int StepInitPath();
	int StepLoadVersion();
	int StepCheckVersion();
	int StepLoadPackInfo();
	int StepCheckPackInfo();
	int StepDownloadPackInfo();
	int StepUpdatePackInfo();
	int StepClearPackInfo();
	int StepLoadBinInfo();
	int StepCheckBinInfo();
	int StepDownLoadBinInfo();
	int StepUpdateBinInfo();
	int StepClearBinInfo();
	virtual bool Run();
	
	int StepLoadVersionNoPack();
	virtual bool RunNoPack();
    
    virtual void Continue(int iResult);

	void CloneExtendMap(std::map<std::wstring, std::wstring>& ExtendMap);
};

static UpdateManagerEx* gs_pUpdateManagerEx = NULL;
inline UpdateManagerEx* GetUpdateManagerEx()
{
	if (!gs_pUpdateManagerEx)
	{
		gs_pUpdateManagerEx = new UpdateManagerEx();
	}
	return gs_pUpdateManagerEx;
}
inline void ReleaseUpdateManagerEx()
{
	if (gs_pUpdateManagerEx)
	{
		delete gs_pUpdateManagerEx;
	}
}

#endif //UpdateManagerEx_H
