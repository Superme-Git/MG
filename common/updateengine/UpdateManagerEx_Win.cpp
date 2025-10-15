#include "UpdateManagerEx_Win.h"
#include "UpdateManagerEx_Helper.h"

#include "UpdateEngine.h"
#include "platform/platform_types.h"

const std::wstring g_VerFileName = L"ver.ljvi";
const std::wstring g_PackInfoFileName = L"fl.ljpi";
const std::wstring g_PackInfoZipFileName = L"fl.ljzip";
const std::wstring g_VerFileNameRoot = L"verroot.ljvi";

UpdateManagerEx::UpdateManagerEx()
{
	m_pVerRoot = NULL;
	m_pVerCache = NULL;

}
UpdateManagerEx::~UpdateManagerEx()
{
	if (m_pVerRoot)
	{
		delete (LJFP_Version*)m_pVerRoot;
		m_pVerRoot = NULL;
	}
	if (m_pVerCache)
	{
		delete (LJFP_Version*)m_pVerCache;
		m_pVerCache = NULL;
	}
}

int UpdateManagerEx::DelOldResFile()
{
#ifdef WIN7_32
	std::string clientIniName = "cfg/clientsetting_win.ini";
#elif defined ANDROID
	std::string clientIniName = "cfg/clientsetting_android.ini";
#else//IOS
	std::string clientIniName = "cfg/clientsetting_ios.ini";
#endif
	unsigned int FNCRC32 = LJCRC32Func(0, (unsigned char*)(clientIniName.c_str()), clientIniName.size() * sizeof(char));
	std::wstring clientIniNameWS = L"";
	clientIniNameWS = GetStringUtil().UI2WS(FNCRC32);
	UpdateUtil::DelFile(m_CacheResPath + clientIniNameWS);
	return 0;
}
int UpdateManagerEx::StepInit()
{
	return StepInitPath();
}
int UpdateManagerEx::StepInitPath()
{
	m_RootPath = SHARE_String2Wstring(CFileUtil::GetRootDir());
#if defined WIN7_32 && defined NoPack
	m_RootResPath = SHARE_String2Wstring(CFileUtil::GetRootDir() + "../../res/");
	m_CacheResPath = SHARE_String2Wstring(CFileUtil::GetCacheDir() + "/res/");
#else
	m_RootResPath = SHARE_String2Wstring(CFileUtil::GetRootDir() + "../../res1/");
	m_CacheResPath = SHARE_String2Wstring(CFileUtil::GetCacheDir() + "/res1/");
#endif

	UpdateUtil::CreateDirEx(L"", m_RootResPath);
	UpdateUtil::CreateDirEx(L"", m_CacheResPath);
	UpdateUtil::CreateDirEx(L"", SHARE_String2Wstring(CFileUtil::GetTempDir() + "/"));

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
		UpdateUtil::DelFile(m_CacheResPath + g_VerFileName);
		UpdateUtil::CopyFile(m_RootResPath + g_VerFileName, m_CacheResPath + g_VerFileName);
		UpdateUtil::DelFile(m_CacheResPath + g_PackInfoFileName);
		UpdateUtil::CopyFile(m_RootResPath + g_PackInfoFileName, m_CacheResPath + g_PackInfoFileName);
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
    bResult = UpdateUtil::ExistFile(m_CacheResPath + g_VerFileNameRoot);
    if (!bResult)
    {
        UpdateUtil::DelFile(m_CacheResPath + g_VerFileName);
        UpdateUtil::CopyFileEx(m_RootResPath, L"", g_VerFileName, m_CacheResPath, L"", g_VerFileNameRoot);
		DelOldResFile();
    }
    else
    {
        LJFP_Version* pVersion = new LJFP_Version();
		iResult = pVersion->LoadFromFile(m_CacheResPath + g_VerFileNameRoot);
		if (iResult != 0)
		{
			UpdateUtil::DelFile(m_CacheResPath + g_VerFileNameRoot);
			return -1;
		}
        if (((LJFP_Version*)m_pVerRoot)->GetVersion() != pVersion->GetVersion())
        {
            UpdateUtil::DelFile(m_CacheResPath + g_VerFileName);
            UpdateUtil::CopyFileEx(m_RootResPath, L"", g_VerFileName, m_CacheResPath, L"", g_VerFileNameRoot);
			DelOldResFile();
        }
        delete pVersion;
    }
	bResult = UpdateUtil::ExistFile(m_CacheResPath + g_VerFileName);
	if (!bResult)
	{
		UpdateUtil::CopyFile(m_RootResPath + g_VerFileName, m_CacheResPath + g_VerFileName);
        UpdateUtil::DelFile(m_CacheResPath + g_PackInfoFileName);
        UpdateUtil::CopyFile(m_RootResPath + g_PackInfoFileName, m_CacheResPath + g_PackInfoFileName);
	}
	m_pVerCache = new LJFP_Version();
	iResult = ((LJFP_Version*)m_pVerCache)->LoadFromFile(m_CacheResPath + g_VerFileName);
	if (iResult != 0){ return -1; }
	m_VersionOld = ((LJFP_Version*)m_pVerCache)->GetVersion();
	m_VersionOldCaption = ((LJFP_Version*)m_pVerCache)->GetVersionCaption();
	m_VersionBase = ((LJFP_Version*)m_pVerCache)->GetVersionBase();
	m_VersionBaseCaption = ((LJFP_Version*)m_pVerCache)->GetVersionCaptionBase();
	m_Channel = ((LJFP_Version*)m_pVerCache)->GetChannel();
	m_ChannelCaption = ((LJFP_Version*)m_pVerCache)->GetChannelCaption();
	((LJFP_Version*)m_pVerCache)->CloneExtendMap(m_ExtendMap);
	m_ExtendMap[L"VersionDonotCheck"] = L"0";
	return 0;
}
bool UpdateManagerEx::Run()
{
    int iResult = 0;
	iResult = StepInit();
	if (iResult != 0){ return false; }
	iResult = StepLoadVersion();
	if (iResult != 0){ return false; }
	return true;
}
int UpdateManagerEx::StepLoadVersionNoPack()
{
	int iResult = 0;
	m_pVerRoot = new LJFP_Version();
	iResult = ((LJFP_Version*)m_pVerRoot)->LoadFromFile(m_RootResPath + g_VerFileName);
	if (iResult != 0){ return -1; }
	UpdateUtil::DelFile(m_CacheResPath + g_VerFileName);
	UpdateUtil::CopyFile(m_RootResPath + g_VerFileName, m_CacheResPath + g_VerFileName);
	UpdateUtil::DelFile(m_CacheResPath + g_PackInfoFileName);
	UpdateUtil::CopyFile(m_RootResPath + g_PackInfoFileName, m_CacheResPath + g_PackInfoFileName);
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
