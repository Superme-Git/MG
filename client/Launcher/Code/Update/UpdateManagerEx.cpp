#include "UpdateManagerEx.h"
#include "UpdateManagerEx_Helper.h"
#include "UpdateJson.h"
#include "../Download/DownloadManager.h"
#include "../LJFP/LJFP_ZipFile.h"
#include "../Utils/SystemUtil.h"
#include "UpdateBin.h"

const std::wstring g_VerFileName = L"ver.ljvi";
const std::wstring g_PackInfoFileName = L"fl.ljpi";
const std::wstring g_PackInfoZipFileName = L"fl.ljzip";
const std::wstring g_VerFileNameRoot = L"verroot.ljvi";
const std::wstring g_FileListBin = L"FileList.bin";
const std::wstring g_BinRelease = L"bin/release/";

//const std::wstring g_JsonSite = L"http://59.151.73.76:50000/88/";
const std::wstring g_JsonSite = L"http://mt3.cn.serverlist.locojoy.com:50000/88/";

std::vector<char> ms_data;

void UpdateManagerEx::SetDownloadSite(std::wstring DownloadSite, std::wstring AppSite)
{
#ifdef DEBUG
	DownloadSite = L"http://127.0.0.1/";
#endif
	AppSite = DownloadSite;
	GetUpdateManagerEx()->m_DownloadSite = DownloadSite;
	GetUpdateManagerEx()->m_AppSite = AppSite;
	GetUpdateManagerEx()->m_DownloadSiteIsBack = 1;
}

std::wstring UpdateManagerEx::GetDownloadSite()
{
	return m_DownloadSite;
}

UpdateManagerEx::UpdateManagerEx()
{
	m_pVerRoot = NULL;
	m_pVerCache = NULL;
	m_pVerUpdate = NULL;

	m_pPIRoot = NULL;
	m_pPICache = NULL;
	m_pPIUpdate = NULL;

	m_pPIAdd = NULL;
	m_pPIMod = NULL;
	m_pPIDel = NULL;

	m_pBFLBase = NULL;
	m_pBFLNew = NULL;

	m_pBFLAdd = NULL;
	m_pBFLMod = NULL;
	m_pBFLDel = NULL;

    m_FormResult = 0;
}
UpdateManagerEx::~UpdateManagerEx()
{
	if (m_pVerRoot)
	{
		delete (LJFP_Version*)m_pVerRoot;
	}
	if (m_pVerCache)
	{
		delete (LJFP_Version*)m_pVerCache;
	}
	if (m_pVerUpdate)
	{
		delete (LJFP_Version*)m_pVerUpdate;
	}

	if (m_pPIRoot)
	{
		delete (LJFP_PackInfo*)m_pPIRoot;
	}
	if (m_pPICache)
	{
		delete (LJFP_PackInfo*)m_pPICache;
	}
	if (m_pPIUpdate)
	{
		delete (LJFP_PackInfo*)m_pPIUpdate;
	}

	if (m_pPIAdd)
	{
		delete (LJFP_PackInfo*)m_pPIAdd;
	}
	if (m_pPIMod)
	{
		delete (LJFP_PackInfo*)m_pPIMod;
	}
	if (m_pPIDel)
	{
		delete (LJFP_PackInfo*)m_pPIDel;
	}

	m_pVerRoot = NULL;
	m_pVerCache = NULL;
	m_pVerUpdate = NULL;

	m_pPIRoot = NULL;
	m_pPICache = NULL;
	m_pPIUpdate = NULL;

	m_pPIAdd = NULL;
	m_pPIMod = NULL;
	m_pPIDel = NULL;
}
int UpdateManagerEx::StepInit()
{
	StepInitPath();
	return 0;
}
int UpdateManagerEx::StepInitPath()
{
	m_RootPath = GetRootPath();
	m_RootResPath = m_RootPath + L"res1/";
	m_RootBinReleasePath = m_RootPath + g_BinRelease;
	m_CacheResPath = m_RootBinReleasePath + L"cache/res1/";
	m_CacheUpdatePath = m_RootBinReleasePath + L"cache/update/";
	m_CacheUpdatePathBin = m_CacheUpdatePath + g_BinRelease;

	UpdateHelper::CreateDirEx(L"", m_RootResPath);
	UpdateHelper::CreateDirEx(L"", m_CacheResPath);
	UpdateHelper::CreateDirEx(L"", m_CacheUpdatePath);
	UpdateHelper::CreateDirEx(L"", m_CacheUpdatePathBin);
	UpdateHelper::CreateDirEx(L"", m_RootBinReleasePath);

	return 0;
}
int UpdateManagerEx::StepLoadVersion()
{
	bool bResult = true;
	int iResult = 0;
	m_pVerRoot = new LJFP_Version();
	iResult = ((LJFP_Version*)m_pVerRoot)->LoadFromFile(m_RootResPath + g_VerFileName);
	if (iResult != 0){ return -1; }
	if (((LJFP_Version*)m_pVerRoot)->GetVersionDonotCheck() != 0)
	{
		UpdateHelper::DelFile(m_CacheResPath + g_VerFileName);
		UpdateHelper::CopyFile(m_RootResPath + g_VerFileName, m_CacheResPath + g_VerFileName);
		UpdateHelper::DelFile(m_CacheResPath + g_PackInfoFileName);
		UpdateHelper::CopyFile(m_RootResPath + g_PackInfoFileName, m_CacheResPath + g_PackInfoFileName);
		m_VersionOld = ((LJFP_Version*)m_pVerRoot)->GetVersion();
		m_VersionOldCaption = ((LJFP_Version*)m_pVerRoot)->GetVersionCaption();
		m_Version = m_VersionOld;
		m_VersionCaption = m_VersionOldCaption;
		m_VersionBase = ((LJFP_Version*)m_pVerRoot)->GetVersionBase();
		m_VersionBaseCaption = ((LJFP_Version*)m_pVerRoot)->GetVersionCaptionBase();
		m_Channel = ((LJFP_Version*)m_pVerRoot)->GetChannel();
		m_ChannelCaption = ((LJFP_Version*)m_pVerRoot)->GetChannelCaption();
		//m_Channel = UpdateJson::GetChannelNameInt();
		//m_ChannelCaption = UpdateJson::GetChannelNameWStr();
		((LJFP_Version*)m_pVerRoot)->CloneExtendMap(m_ExtendMap);
		m_ExtendMap[L"VersionDonotCheck"] = L"1";
		GlobalNotifyLocalVersion(m_VersionCaption);
		GlobalNotifyNewVersion(m_VersionCaption);
		return 1;
	}
    //UpdateUtil::DelFile(m_CacheResPath + g_VerFileNameRoot);//Temp
    bResult = UpdateHelper::ExistFile(m_CacheResPath + g_VerFileNameRoot);
    if (!bResult)
    {
        UpdateHelper::DelFile(m_CacheResPath + g_VerFileName);
        UpdateHelper::CopyFileEx(m_RootResPath, L"", g_VerFileName, m_CacheResPath, L"", g_VerFileNameRoot);
    }
    else
    {
        LJFP_Version* pVersion = new LJFP_Version();
		iResult = pVersion->LoadFromFile(m_CacheResPath + g_VerFileNameRoot);
		if (iResult != 0){ return -1; }
        if (((LJFP_Version*)m_pVerRoot)->GetVersion() != pVersion->GetVersion())
        {
            UpdateHelper::DelFile(m_CacheResPath + g_VerFileName);
            UpdateHelper::CopyFileEx(m_RootResPath, L"", g_VerFileName, m_CacheResPath, L"", g_VerFileNameRoot);
        }
        delete pVersion;
    }
	//UpdateUtil::DelFile(m_CacheResPath + g_VerFileName);//Temp
	bResult = UpdateHelper::ExistFile(m_CacheResPath + g_VerFileName);
	if (!bResult)
	{
		UpdateHelper::CopyFile(m_RootResPath + g_VerFileName, m_CacheResPath + g_VerFileName);
        UpdateHelper::DelFile(m_CacheResPath + g_PackInfoFileName);
        UpdateHelper::CopyFile(m_RootResPath + g_PackInfoFileName, m_CacheResPath + g_PackInfoFileName);
	}
	m_pVerCache = new LJFP_Version();
	iResult = ((LJFP_Version*)m_pVerCache)->LoadFromFile(m_CacheResPath + g_VerFileName);
	if (iResult != 0){ return -1; }
	std::wstring LocalVersionCaption = ((LJFP_Version*)m_pVerCache)->GetVersionCaption();
	GlobalNotifyLocalVersion(LocalVersionCaption);
	m_VersionOld = ((LJFP_Version*)m_pVerCache)->GetVersion();
	m_VersionOldCaption = ((LJFP_Version*)m_pVerCache)->GetVersionCaption();
	
	m_Channel = ((LJFP_Version*)m_pVerCache)->GetChannel();
	m_ChannelCaption = ((LJFP_Version*)m_pVerCache)->GetChannelCaption();

	m_VersionBase = ((LJFP_Version*)m_pVerCache)->GetVersionBase();
	m_VersionBaseCaption = ((LJFP_Version*)m_pVerCache)->GetVersionCaptionBase();
	//m_Channel = UpdateJson::GetChannelNameInt();
	//m_ChannelCaption = UpdateJson::GetChannelNameWStr();

	m_DownloadSiteIsBack = 0;
    UpdateJson::SetCB_SetDownloadSite_Func(UpdateManagerEx::SetDownloadSite);
	UpdateJson::RequestUpdateJson(g_JsonSite, m_VersionBaseCaption, m_ChannelCaption);
	
	int iDispatchDelayTime = 0;
	while (m_DownloadSiteIsBack == 0)
	{
		if (iDispatchDelayTime >= 90000)
		{
			GetDownloadManager()->Run();
			DoEvents();
			iDispatchDelayTime = 0;
		}
		iDispatchDelayTime++;
	}
	if (m_DownloadSite.compare(L"") == 0){ return -1; }
	UpdateHelper::DelFile(m_CacheUpdatePath + g_VerFileName);
	URLInfoArr UIArr = ((LJFP_Version*)m_pVerCache)->GetURLInfoArr();
	std::wstring CacheVersionCaptionBase = ((LJFP_Version*)m_pVerCache)->GetVersionCaptionBase() + L"/";
	//bResult = UpdateUtil::DownloadFileEx(UIArr, m_SystemType, m_NetworkType, CacheVersionCaptionBase, g_VerFileName + UpdateUtil::GetRandomDownloadParam(), m_CacheUpdatePath, L"", g_VerFileName, false);
	bResult = UpdateHelper::DownloadFile(m_DownloadSite, CacheVersionCaptionBase, g_VerFileName + UpdateHelper::GetRandomDownloadParam(), m_CacheUpdatePath, L"", g_VerFileName, false);
	if (bResult != true){ return -1; }
	bResult = UpdateHelper::ExistFile(m_CacheUpdatePath + g_VerFileName);
	if (bResult != true){ return -1; }
	m_pVerUpdate = new LJFP_Version();
	iResult = ((LJFP_Version*)m_pVerUpdate)->LoadFromFile(m_CacheUpdatePath + g_VerFileName);
	if (iResult != 0){ return -1; }
	std::wstring NewVersion = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaption();
	GlobalNotifyNewVersion(NewVersion);
	m_Version = ((LJFP_Version*)m_pVerUpdate)->GetVersion();
	m_VersionCaption = NewVersion;
	m_VersionBase = ((LJFP_Version*)m_pVerUpdate)->GetVersionBase();
	m_VersionBaseCaption = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaptionBase();
	((LJFP_Version*)m_pVerUpdate)->CloneExtendMap(m_ExtendMap);
	m_ExtendMap[L"VersionDonotCheck"] = L"0";
	return 0;
}
int UpdateManagerEx::StepCheckVersion()
{
	if (((LJFP_Version*)m_pVerCache)->GetVersion() < ((LJFP_Version*)m_pVerUpdate)->GetVersion())//当前版本小于最新版本
	{
		if (((LJFP_Version*)m_pVerCache)->GetVersion() < ((LJFP_Version*)m_pVerUpdate)->GetVersionMinimum())//当前版本小于最低需求版本,不能更新,重新下载
		{
			return -1;
		}
		else//当前版本大于等于最低需求版本,需要更新
		{
			return 1;
		}
	}
	else//当前版本就是最新版本
	{
		return 0;
	}
	return 0;
}
int UpdateManagerEx::StepLoadPackInfo()
{
	bool bResult = true;
	m_pPIRoot = LoadPackInfo(m_RootResPath + g_PackInfoFileName, 0, m_RootResPath);
	if (!m_pPIRoot){ return -1; }
	//UpdateUtil::DelFile(m_CacheResPath + g_PackInfoFileName);//Temp
	bResult = UpdateHelper::ExistFile(m_CacheResPath + g_PackInfoFileName);
	if (!bResult)
	{
		UpdateHelper::CopyFile(m_RootResPath + g_PackInfoFileName, m_CacheResPath + g_PackInfoFileName);
	}
	m_pPICache = LoadPackInfo(m_CacheResPath + g_PackInfoFileName, 1, m_CacheResPath);
	if (!m_pPICache){ return -1; }
	UpdateHelper::DelFile(m_CacheUpdatePath + g_PackInfoZipFileName);
	URLInfoArr UIArr = ((LJFP_Version*)m_pVerCache)->GetURLInfoArr();
	std::wstring NewVersionCaptionBase = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaptionBase() + L"/";
	std::wstring NewVersionCaption = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaption() + L"/";
	//bResult = UpdateUtil::DownloadFileEx(UIArr, m_SystemType, m_NetworkType, NewVersionCaptionBase + NewVersionCaption, g_PackInfoZipFileName, m_CacheUpdatePath, L"", g_PackInfoZipFileName, false);
	bResult = UpdateHelper::DownloadFile(m_DownloadSite, NewVersionCaptionBase + NewVersionCaption, g_PackInfoZipFileName, m_CacheUpdatePath, L"", g_PackInfoZipFileName, false);
	if (bResult != true){ return -1; }
	bResult = UpdateHelper::ExistFile(m_CacheUpdatePath + g_PackInfoZipFileName);
	if (bResult != true){ return -1; }
	UpdateHelper::DelFile(m_CacheUpdatePath + g_PackInfoFileName);
	LJFP_ZipFile ZF(9999, LJCRC32Func, LJZipFunc, LJUnZipFunc, LJSMS4Func, LJDeSMS4Func, "locojoy123456789");
	int iResult = ZF.UnZipFile(m_CacheUpdatePath + g_PackInfoZipFileName, m_CacheUpdatePath + g_PackInfoFileName);
	if (iResult != 0){ return -1; }
	bResult = UpdateHelper::ExistFile(m_CacheUpdatePath + g_PackInfoFileName);
	if (bResult != true){ return -1; }
	m_pPIUpdate = LoadPackInfo(m_CacheUpdatePath + g_PackInfoFileName, 1, m_CacheUpdatePath);
	if (!m_pPIUpdate){ return -1; }
	return 0;
}
int UpdateManagerEx::StepCheckPackInfo()
{
	int iResult = ParsePackInfo((LJFP_PackInfo*)m_pPICache, (LJFP_PackInfo*)m_pPIUpdate, (LJFP_PackInfo*&)m_pPIAdd, (LJFP_PackInfo*&)m_pPIMod, (LJFP_PackInfo*&)m_pPIDel);
	if (iResult != 0)
	{
		return -1;
	}
	return 0;
}
int UpdateManagerEx::StepDownloadPackInfo()
{
	bool bResult = true;
	URLInfoArr UIArr = ((LJFP_Version*)m_pVerCache)->GetURLInfoArr();
    unsigned int FileCountAdd = ((LJFP_PackInfo*)m_pPIAdd)->GetFileCount();
    unsigned int FileCountMod = ((LJFP_PackInfo*)m_pPIMod)->GetFileCount();
    unsigned int FileCount = FileCountAdd + FileCountMod;
	std::wstring NewVersionCaptionBase = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaptionBase() + L"/";
	std::wstring NewVersionCaption = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaption() + L"/";
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIAdd)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIAdd)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		unsigned int CurSize = ((LJFP_PackInfo*)m_pPIAdd)->GetFileInfo(i)->m_Size;
		unsigned int CurCRC32 = ((LJFP_PackInfo*)m_pPIAdd)->GetFileInfo(i)->m_CRC32;
		if (!UpdateHelper::ExistFileEx(m_CacheUpdatePath + CurPathName + CurFileNameFull, CurSize, CurCRC32))//如果本地文件不存在或者存在但校验不通过才下载
		{
			bool bDownloadOK = false;
			for (unsigned int i2 = 0; i2 < 1; i2++)
			{
				//bResult = UpdateUtil::DownloadFileEx(UIArr, m_SystemType, m_NetworkType, 
				//	NewVersionCaptionBase + NewVersionCaption + CurPathName, CurFileNameFull,
				//	m_CacheUpdatePath, CurPathName, CurFileNameFull, true);
				bResult = UpdateHelper::DownloadFile(m_DownloadSite, NewVersionCaptionBase + NewVersionCaption + CurPathName, CurFileNameFull,
					m_CacheUpdatePath, CurPathName, CurFileNameFull, true);
				if (bResult != true){ continue; }
				bResult = UpdateHelper::ExistFileEx(m_CacheUpdatePath + CurPathName + CurFileNameFull, CurSize, CurCRC32);
				if (bResult != true){ continue; }
				bDownloadOK = true;
				break;
			}
			if (bDownloadOK != true){ return -1; }
		}
		GlobalNotifyStep(((i + 1) * 1.0f / FileCount) * 80 / 2 + 50);
	}
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIMod)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		unsigned int CurSize = ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_Size;
		unsigned int CurCRC32 = ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_CRC32;
		if (!UpdateHelper::ExistFileEx(m_CacheUpdatePath + CurPathName + CurFileNameFull, CurSize, CurCRC32))//如果本地文件不存在或者存在但校验不通过才下载
		{
			bool bDownloadOK = false;
			for (unsigned int i2 = 0; i2 < 1; i2++)
			{
				//bResult = UpdateUtil::DownloadFileEx(UIArr, m_SystemType, m_NetworkType, 
				//	NewVersionCaptionBase + NewVersionCaption + CurPathName, CurFileNameFull,
				//	m_CacheUpdatePath, CurPathName, CurFileNameFull, true);
				bResult = UpdateHelper::DownloadFile(m_DownloadSite, NewVersionCaptionBase + NewVersionCaption + CurPathName, CurFileNameFull,
					m_CacheUpdatePath, CurPathName, CurFileNameFull, true);
				if (bResult != true){ continue; }
				bResult = UpdateHelper::ExistFileEx(m_CacheUpdatePath + CurPathName + CurFileNameFull, CurSize, CurCRC32);
				if (bResult != true){ continue; }
				bDownloadOK = true;
				break;
			}
			if (bDownloadOK != true){ return -1; }
		}
		GlobalNotifyStep(((i + 1 + FileCountAdd) * 1.0f / FileCount) * 80 / 2 + 50);
	}
	GlobalNotifyStep(80 / 2 + 50);
	return 0;
}
int UpdateManagerEx::StepUpdatePackInfo()
{
	int iResult = 0;
    unsigned int FileCountAdd = ((LJFP_PackInfo*)m_pPIAdd)->GetFileCount();
    unsigned int FileCountMod = ((LJFP_PackInfo*)m_pPIMod)->GetFileCount();
    unsigned int FileCount = FileCountAdd + FileCountMod;
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIAdd)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIAdd)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		iResult = UpdateHelper::CopyFileEx(m_CacheUpdatePath, CurPathName, CurFileNameFull, m_CacheResPath, CurPathName, CurFileNameFull);
		if (iResult != 0){ return -1; }
		GlobalNotifyStep((80 + ((i + 1) * 1.0f / FileCount) * 10) / 2 + 50);
	}
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIMod)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		iResult = UpdateHelper::CopyFileEx(m_CacheUpdatePath, CurPathName, CurFileNameFull, m_CacheResPath, CurPathName, CurFileNameFull);
		if (iResult != 0){ return -2; }
		GlobalNotifyStep((80 + ((i + 1 + FileCountAdd) * 1.0f / FileCount) * 10) / 2 + 50);
	}
	iResult = UpdateHelper::CopyFileEx(m_CacheUpdatePath, L"", g_PackInfoFileName, m_CacheResPath, L"", g_PackInfoFileName);
	if (iResult != 0){ return -3; }
	iResult = UpdateHelper::CopyFileEx(m_CacheUpdatePath, L"", g_VerFileName, m_CacheResPath, L"", g_VerFileName);
	if (iResult != 0){ return -4; }
	GlobalNotifyStep(90 / 2 + 50);
	return 0;
}
int UpdateManagerEx::StepClearPackInfo()
{
	bool bResult = true;
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIAdd)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIAdd)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		bResult = UpdateHelper::DelFile(m_CacheUpdatePath + CurPathName + CurFileNameFull);
	}
	GlobalNotifyStep(93 / 2 + 50);
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIMod)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		bResult = UpdateHelper::DelFile(m_CacheUpdatePath + CurPathName + CurFileNameFull);
	}
	GlobalNotifyStep(96 / 2 + 50);
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIDel)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIDel)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		bResult = UpdateHelper::DelFile(m_CacheResPath + CurPathName + CurFileNameFull);
	}
	GlobalNotifyStep(99 / 2 + 50);
	bResult = UpdateHelper::DelFile(m_CacheUpdatePath + g_PackInfoFileName);
	bResult = UpdateHelper::DelFile(m_CacheUpdatePath + g_VerFileName);
	if (bResult != true){ return -1; }
	GlobalNotifyStep(100);
	return 0;
}
int UpdateManagerEx::StepLoadBinInfo()
{
	bool bResult = true;
	m_pBFLBase = LoadUpdateBinFileList(m_RootBinReleasePath + g_FileListBin);
	if (!m_pBFLBase){ return -1; }
	std::wstring NewVersionCaptionBase = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaptionBase() + L"/";
	std::wstring NewVersionCaption = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaption() + L"/";
	bResult = UpdateHelper::DownloadFile(m_DownloadSite, NewVersionCaptionBase + NewVersionCaption + g_BinRelease, g_FileListBin + UpdateHelper::GetRandomDownloadParam(), m_CacheUpdatePathBin, L"", g_FileListBin, false);
	if (bResult != true){ return -1; }
	m_pBFLNew = LoadUpdateBinFileList(m_CacheUpdatePathBin + g_FileListBin);
	if (!m_pBFLNew){ return -1; }
	return 0;
}
int UpdateManagerEx::StepCheckBinInfo()
{
	int iResult = ParseUpdateBinFileList((UpdateBinFileList*)m_pBFLBase, (UpdateBinFileList*)m_pBFLNew, (UpdateBinFileList*&)m_pBFLAdd, (UpdateBinFileList*&)m_pBFLMod, (UpdateBinFileList*&)m_pBFLDel);
	if (iResult != 0){ return -1; }
	return 0;
}
int UpdateManagerEx::StepDownLoadBinInfo()
{
	bool bResult = true;
	unsigned int FileCountAdd = ((UpdateBinFileList*)m_pBFLAdd)->GetFileCount();
	unsigned int FileCountMod = ((UpdateBinFileList*)m_pBFLMod)->GetFileCount();
	unsigned int FileCount = FileCountAdd + FileCountMod;
	std::wstring NewVersionCaptionBase = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaptionBase() + L"/";
	std::wstring NewVersionCaption = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaption() + L"/";
	for (unsigned int i = 0; i < ((UpdateBinFileList*)m_pBFLAdd)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((UpdateBinFileList*)m_pBFLAdd)->GetFile(i)->m_wsFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		unsigned int CurSize = ((UpdateBinFileList*)m_pBFLAdd)->GetFile(i)->m_Size;
		unsigned int CurCRC32 = ((UpdateBinFileList*)m_pBFLAdd)->GetFile(i)->m_CRC32;
		if (!UpdateHelper::ExistFileEx(m_CacheUpdatePathBin + CurPathName + CurFileNameFull, CurSize, CurCRC32))//如果本地文件不存在或者存在但校验不通过才下载
		{
			bool bDownloadOK = false;
			for (unsigned int i2 = 0; i2 < 1; i2++)
			{
				bResult = UpdateHelper::DownloadFile(m_DownloadSite, NewVersionCaptionBase + NewVersionCaption + g_BinRelease + CurPathName, CurFileNameFull,
					m_CacheUpdatePathBin, CurPathName, CurFileNameFull + L".z", true);
				if (bResult != true){ continue; }
				bResult = UpdateHelper::ExistFileEx(m_CacheUpdatePathBin + CurPathName + CurFileNameFull + L".z", CurSize, CurCRC32);
				if (bResult != true){ continue; }
				bDownloadOK = true;
				break;
			}
			if (bDownloadOK != true){ return -1; }
		}
		GlobalNotifyStep(((i + 1) * 1.0f / FileCount) * 80 / 2);
	}
	for (unsigned int i = 0; i < ((UpdateBinFileList*)m_pBFLMod)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((UpdateBinFileList*)m_pBFLMod)->GetFile(i)->m_wsFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		unsigned int CurSize = ((UpdateBinFileList*)m_pBFLMod)->GetFile(i)->m_Size;
		unsigned int CurCRC32 = ((UpdateBinFileList*)m_pBFLMod)->GetFile(i)->m_CRC32;
		if (!UpdateHelper::ExistFileEx(m_CacheUpdatePathBin + CurPathName + CurFileNameFull, CurSize, CurCRC32))//如果本地文件不存在或者存在但校验不通过才下载
		{
			bool bDownloadOK = false;
			for (unsigned int i2 = 0; i2 < 1; i2++)
			{
				bResult = UpdateHelper::DownloadFile(m_DownloadSite, NewVersionCaptionBase + NewVersionCaption + g_BinRelease + CurPathName, CurFileNameFull,
					m_CacheUpdatePathBin, CurPathName, CurFileNameFull + L".z", true);
				if (bResult != true){ continue; }
				bResult = UpdateHelper::ExistFileEx(m_CacheUpdatePathBin + CurPathName + CurFileNameFull + L".z", CurSize, CurCRC32);
				if (bResult != true){ continue; }
				bDownloadOK = true;
				break;
			}
			if (bDownloadOK != true){ return -1; }
		}
		GlobalNotifyStep(((i + 1 + FileCountAdd) * 1.0f / FileCount) * 80 / 2);
	}
	GlobalNotifyStep(80 / 2);
	return 0;
}
int UpdateManagerEx::StepUpdateBinInfo()
{
	int iResult = 0;
	unsigned int FileCountAdd = ((UpdateBinFileList*)m_pBFLAdd)->GetFileCount();
	unsigned int FileCountMod = ((UpdateBinFileList*)m_pBFLMod)->GetFileCount();
	unsigned int FileCount = FileCountAdd + FileCountMod;
	for (unsigned int i = 0; i < ((UpdateBinFileList*)m_pBFLAdd)->GetFileCount(); i++)
	{
		UpdateBinFile* pCurBF = ((UpdateBinFileList*)m_pBFLAdd)->GetFile(i);
		std::wstring CurPathFileNameFull = pCurBF->m_wsFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		int iResult = LJUnZFileToFile(m_CacheUpdatePathBin + CurPathName + CurFileNameFull + L".z", m_CacheUpdatePathBin + CurPathName + CurFileNameFull);
		if (iResult != 0){ return -11; }
		unsigned int uiSize;
		unsigned int uiCRC32;
		UpdateHelper::GetFileSizeCRC32(m_CacheUpdatePathBin + CurPathName + CurFileNameFull, uiSize, uiCRC32);
		if (pCurBF->m_SizeOriginal != uiSize || pCurBF->m_CRC32Original != uiCRC32)
		{
			return -12;
		}
		iResult = UpdateHelper::CopyFileEx(m_CacheUpdatePathBin, CurPathName, CurFileNameFull, m_RootBinReleasePath, CurPathName, CurFileNameFull);
		if (iResult != 0){ return -13; }
		GlobalNotifyStep((80 + ((i + 1) * 1.0f / FileCount) * 10) / 2);
	}
	for (unsigned int i = 0; i < ((UpdateBinFileList*)m_pBFLMod)->GetFileCount(); i++)
	{
		UpdateBinFile* pCurBF = ((UpdateBinFileList*)m_pBFLMod)->GetFile(i);
		std::wstring CurPathFileNameFull = ((UpdateBinFileList*)m_pBFLMod)->GetFile(i)->m_wsFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		int iResult = LJUnZFileToFile(m_CacheUpdatePathBin + CurPathName + CurFileNameFull + L".z", m_CacheUpdatePathBin + CurPathName + CurFileNameFull);
		if (iResult != 0){ return -21; }
		unsigned int uiSize;
		unsigned int uiCRC32;
		UpdateHelper::GetFileSizeCRC32(m_CacheUpdatePathBin + CurPathName + CurFileNameFull, uiSize, uiCRC32);
		if (pCurBF->m_SizeOriginal != uiSize || pCurBF->m_CRC32Original != uiCRC32)
		{
			return -22;
		}
		iResult = UpdateHelper::CopyFileEx(m_CacheUpdatePathBin, CurPathName, CurFileNameFull, m_RootBinReleasePath, CurPathName, CurFileNameFull);
		if (iResult != 0){ return -23; }
		GlobalNotifyStep((80 + ((i + 1 + FileCountAdd) * 1.0f / FileCount) * 10) / 2);
	}
	iResult = UpdateHelper::CopyFileEx(m_CacheUpdatePathBin, L"", g_FileListBin, m_RootBinReleasePath, L"", g_FileListBin);
	if (iResult != 0){ return -3; }
	GlobalNotifyStep(90 / 2);
	return 0;
}
int UpdateManagerEx::StepClearBinInfo()
{
	bool bResult = true;
	for (unsigned int i = 0; i < ((UpdateBinFileList*)m_pBFLAdd)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((UpdateBinFileList*)m_pBFLAdd)->GetFile(i)->m_wsFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		bResult = UpdateHelper::DelFile(m_CacheUpdatePathBin + CurPathName + CurFileNameFull);
		bResult = UpdateHelper::DelFile(m_CacheUpdatePathBin + CurPathName + CurFileNameFull + L".z");
	}
	GlobalNotifyStep(93 / 2);
	for (unsigned int i = 0; i < ((UpdateBinFileList*)m_pBFLMod)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((UpdateBinFileList*)m_pBFLMod)->GetFile(i)->m_wsFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		bResult = UpdateHelper::DelFile(m_CacheUpdatePathBin + CurPathName + CurFileNameFull);
		bResult = UpdateHelper::DelFile(m_CacheUpdatePathBin + CurPathName + CurFileNameFull + L".z");
	}
	GlobalNotifyStep(96 / 2);
	for (unsigned int i = 0; i < ((UpdateBinFileList*)m_pBFLDel)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((UpdateBinFileList*)m_pBFLDel)->GetFile(i)->m_wsFileName;
		std::wstring CurPathName = UpdateHelper::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateHelper::GetFileNameFull(CurPathFileNameFull);
		bResult = UpdateHelper::DelFile(m_RootBinReleasePath + CurPathName + CurFileNameFull);
	}
	GlobalNotifyStep(99 / 2);
	bResult = UpdateHelper::DelFile(m_CacheUpdatePathBin + g_FileListBin);
	if (bResult != true){ return -1; }
	GlobalNotifyStep(100 / 2);
	return 0;
}
bool UpdateManagerEx::Run()
{
    int iState = 1;
    int iResult = 0;
    
    while (iState >= 0)
    {
        if (iState == 0)
        {
            if (m_FormResult < 0)
            {
				//UpdateJson::ExitApp();
				GlobalNotifyFail();
                return false;
            }
            else
            {
                iState = m_FormResult;
            }
        }
        else if (iState == 1)
        {
            GlobalNotifyMsg(L"");
            GlobalNotifyStep(0);
            GlobalNotifyMsgByKey(L"upmgrstr11");//正在连接服务器...
            iResult = StepInit();
            if (iResult != 0){ return false; }
            iState = 2;
        }
		else if (iState == 2)
		{
			iResult = StepLoadVersion();
			if (iResult > 0)
			{
				GlobalNotifyMsgByKey(L"upmgrstr21");//更新完成
				GlobalNotifyStep(100);
				return true;
			}
			else if (iResult == 0)
			{
				iState = 21;
			}
			else if (iResult < 0)
			{
				m_FormResult = 0;
				iState = 0;
				Continue(GlobalNotifyShowForm(1, 0, L""));
			}
		}
		else if (iState == 21)
		{
			iResult = StepLoadBinInfo();
			if (iResult == 0)
			{
				iState = 22;
			}
			else if (iResult < 0)
			{
				m_FormResult = 0;
				iState = 0;
				Continue(GlobalNotifyShowForm(21, 0, L""));
			}
		}
		else if (iState == 22)
		{
			iResult = StepCheckBinInfo();
			if (iResult == 0)
			{
				int iDownloadSize = 0;
				for (unsigned int i = 0; i < ((UpdateBinFileList*)m_pBFLAdd)->GetFileCount(); i++)
				{
					iDownloadSize = iDownloadSize + ((UpdateBinFileList*)m_pBFLAdd)->GetFile(i)->m_Size;
				}
				for (unsigned int i = 0; i < ((UpdateBinFileList*)m_pBFLMod)->GetFileCount(); i++)
				{
					iDownloadSize = iDownloadSize + ((UpdateBinFileList*)m_pBFLMod)->GetFile(i)->m_Size;
				}
				m_FormResult = 0;
				iState = 0;
				//iDownloadSize = iDownloadSize / 1024;
				//iDownloadSize = iDownloadSize / 1024;
				if (iDownloadSize != 0)
				{
					//Continue(GlobalNotifyShowForm(22, iDownloadSize, L""));
					Continue(23);
				}
				else
				{
					GlobalNotifyStep(100 / 2);
					GlobalNotifyMsgByKey(L"upmgrstr221");//执行文件更新完成
					iState = 3;
				}
			}
			else if (iResult < 0)
			{
				return false;
			}
		}
		else if (iState == 23)
		{
			GlobalNotifyMsgByKey(L"upmgrstr231");//执行文件自动更新中...
			iResult = StepDownLoadBinInfo();
			if (iResult == 0)
			{
				iState = 24;
				GlobalNotifyStep(80 / 2);
			}
			else if (iResult < 0)
			{
				m_FormResult = 0;
				iState = 0;
				Continue(GlobalNotifyShowForm(23, 0, L""));
			}
		}
		else if (iState == 24)
		{
			iResult = StepUpdateBinInfo(); GlobalNotifyStep(90 / 2);
			if (iResult != 0){ return false; }
			iResult = StepClearBinInfo(); GlobalNotifyStep(100 / 2);
			//if (iResult != 0){ return false; }//不管是旧资源否全部删除成功,只要新增和修改的文件都拷贝完成就算更新成功
			GlobalNotifyMsgByKey(L"upmgrstr241");//执行文件更新完成
			iState = 3;
		}
        else if (iState == 3)
        {
            GlobalNotifyMsgByKey(L"upmgrstr31");//版本检测中...
            iResult = StepCheckVersion();
            if (iResult == 0)
            {
                GlobalNotifyMsgByKey(L"upmgrstr32");//更新完成
                GlobalNotifyStep(100);
                return true;
            }
            else if (iResult > 0)
            {
                iState = 4;
            }
            else if (iResult < 0)
            {
                m_FormResult = 0;
                iState = 0;
                //std::wstring AppURL = ((LJFP_Version*)m_pVerUpdate)->GetAppURL();
				std::wstring AppURL = m_AppSite;
				Continue(GlobalNotifyShowForm(2, 0, AppURL));
            }
        }
        else if (iState == 4)
        {
            iResult = StepLoadPackInfo();
            if (iResult == 0)
            {
                iState = 5;
            }
            else if (iResult < 0)
            {
                m_FormResult = 0;
                iState = 0;
				Continue(GlobalNotifyShowForm(3, 0, L""));
            }
        }
        else if (iState == 5)
        {
            iResult = StepCheckPackInfo();
            if (iResult == 0)
            {
     //           int iDownloadSize = 0;
     //           
     //           for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIAdd)->GetFileCount(); i++)
     //           {
					//iDownloadSize = iDownloadSize + ((LJFP_PackInfo*)m_pPIAdd)->GetFileInfo(i)->m_Size;
     //           }
     //           for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIMod)->GetFileCount(); i++)
     //           {
					//iDownloadSize = iDownloadSize + ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_Size;
     //           }
                m_FormResult = 0;
                iState = 0;
                //iDownloadSize = iDownloadSize / 1024;
                //iDownloadSize = iDownloadSize / 1024;
				//Continue(GlobalNotifyShowForm(4, iDownloadSize, L""));
				Continue(6);
            }
            else if (iResult < 0)
            {
                return false;
            }
        }
        else if (iState == 6)
        {
            GlobalNotifyMsgByKey(L"upmgrstr61");//自动更新中...
            iResult = StepDownloadPackInfo();
            if (iResult == 0)
            {
                iState = 7;
				GlobalNotifyStep(80 / 2 + 50);
            }
            else if (iResult < 0)
            {
                m_FormResult = 0;
                iState = 0;
				Continue(GlobalNotifyShowForm(5, 0, L""));
            }
        }
        else if (iState == 7)
        {
			iResult = StepUpdatePackInfo(); GlobalNotifyStep(90 / 2 + 50);
            if (iResult != 0){ return false; }
            iResult = StepClearPackInfo(); GlobalNotifyStep(100);
            //if (iResult != 0){ return false; }//不管是旧资源否全部删除成功,只要新增和修改的文件都拷贝完成就算更新成功
            GlobalNotifyMsgByKey(L"upmgrstr71");//更新完成
            return true;
        }
		DoEvents();
		DoEventsUI();
    }
	return true;
}
int UpdateManagerEx::StepLoadVersionNoPack()
{
	int iResult = 0;
	m_pVerRoot = new LJFP_Version();
	iResult = ((LJFP_Version*)m_pVerRoot)->LoadFromFile(m_RootResPath + g_VerFileName);
	if (iResult != 0){ return -1; }
	UpdateHelper::DelFile(m_CacheResPath + g_VerFileName);
	UpdateHelper::CopyFile(m_RootResPath + g_VerFileName, m_CacheResPath + g_VerFileName);
	UpdateHelper::DelFile(m_CacheResPath + g_PackInfoFileName);
	UpdateHelper::CopyFile(m_RootResPath + g_PackInfoFileName, m_CacheResPath + g_PackInfoFileName);
	m_VersionOld = ((LJFP_Version*)m_pVerRoot)->GetVersion();
	m_VersionOldCaption = ((LJFP_Version*)m_pVerRoot)->GetVersionCaption();
	m_Version = m_VersionOld;
	m_VersionCaption = m_VersionOldCaption;
	m_VersionBase = ((LJFP_Version*)m_pVerRoot)->GetVersionBase();
	m_VersionBaseCaption = ((LJFP_Version*)m_pVerRoot)->GetVersionCaptionBase();
	m_Channel = ((LJFP_Version*)m_pVerRoot)->GetChannel();
	m_ChannelCaption = ((LJFP_Version*)m_pVerRoot)->GetChannelCaption();
	((LJFP_Version*)m_pVerRoot)->CloneExtendMap(m_ExtendMap);
	m_ExtendMap[L"VersionDonotCheck"] = L"1";
	return 0;
}
bool UpdateManagerEx::RunNoPack()
{
	int iResult = 0;
	iResult = StepInit();
	if (iResult != 0){ return false; }
	iResult = StepLoadVersionNoPack();
	if (iResult != 0){ return false; }
	return true;
}
void UpdateManagerEx::Continue(int iResult)
{
    m_FormResult = iResult;
}
void UpdateManagerEx::CloneExtendMap(std::map<std::wstring, std::wstring>& ExtendMap)
{
	ExtendMap.clear();
	std::map<std::wstring, std::wstring>::iterator it = m_ExtendMap.begin();
	while (it != m_ExtendMap.end())
	{
		ExtendMap[it->first] = it->second;
		it++;
	}
}