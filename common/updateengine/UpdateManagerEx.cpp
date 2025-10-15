#include "UpdateManagerEx.h"
#include "UpdateCommon.h"
#include "UpdateManagerEx_Helper.h"

#include "UpdateEngine.h"
#include "FileDownloader.h"

#include "../ljfm/code/include/ljfsfile.h"
#include "../ljfm/code/include/ljfszipfile.h"

#include "unistd.h"

#include "../platform/utils/UpdateUtil.h"

const std::wstring g_VerFileName = L"ver.ljvi";
const std::wstring g_PackInfoFileName = L"fl.ljpi";
const std::wstring g_PackInfoZipFileName = L"fl.ljzip";
const std::wstring g_VerFileNameRoot = L"verroot.ljvi";

//const std::wstring g_JsonSite = L"http://59.151.73.76:50000/88/";
const std::wstring g_JsonSite = L"http://mt3.cn.serverlist.locojoy.com:50000/88/";

void UpdateManagerEx::SetDownloadSite(std::wstring DownloadSite, std::wstring AppSite, std::wstring WGSite)
{
	UpdateManagerEx::GetInstance()->m_DownloadSite = DownloadSite;
	UpdateManagerEx::GetInstance()->m_AppSite = AppSite;
	
	LOGD("SetDownloadSite:%s", ws2s(WGSite).c_str());
	UpdateEngine::g_WGAdressStr = WGSite;
	UpdateManagerEx::GetInstance()->m_DownloadSiteIsBack = 1;
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
#ifdef WIN7_32
	m_SystemType = L"ios";
	m_NetworkType = L"lan";
#elif defined ANDROID
	m_SystemType = L"android";
	m_NetworkType = L"lan";
#else//IOS
	m_SystemType = L"ios";
	m_NetworkType = L"lan";
#endif

	StepInitPath();

	return 0;
}
int UpdateManagerEx::StepInitPath()
{
#ifdef WIN7_32
	m_RootPath = SHARE_String2Wstring(CFileUtil::GetRootDir() + "/");
	m_RootResPath = SHARE_String2Wstring(CFileUtil::GetRootDir() + "../../res/");
	m_CacheResPath = SHARE_String2Wstring(CFileUtil::GetRootDir() + "../../cacheres/");
	m_CacheUpdatePath = SHARE_String2Wstring(CFileUtil::GetRootDir() + "../../cacheupdate/");
#else
	m_RootPath = SHARE_String2Wstring(CFileUtil::GetRootDir() + "/");
	m_RootResPath = SHARE_String2Wstring(CFileUtil::GetRootDir() + "/res/");
	m_CacheResPath = SHARE_String2Wstring(CFileUtil::GetCacheDir() + "/res/");
	m_CacheUpdatePath = SHARE_String2Wstring(CFileUtil::GetCacheDir() + "/update/");
#endif
	UpdateUtil::CreateDir(m_RootResPath);
	UpdateUtil::CreateDir(m_CacheResPath);
	UpdateUtil::CreateDir(m_CacheUpdatePath);
#if defined ANDROID
	UpdateUtil::CreateDir(SHARE_String2Wstring(CFileUtil::GetTempDir() + "/"));
#endif

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
		//m_Channel = ((LJFP_Version*)m_pVerRoot)->GetChannel();
		//m_ChannelCaption = ((LJFP_Version*)m_pVerRoot)->GetChannelCaption();
		m_Channel = UpdateJson::GetChannelNameInt();
		m_ChannelCaption = UpdateJson::GetChannelNameWStr();
		((LJFP_Version*)m_pVerRoot)->CloneExtendMap(m_ExtendMap);
        m_ExtendMap[L"VersionDonotCheck"] = L"1";
		return 1;
	}
    //UpdateUtil::DelFile(m_CacheResPath + g_VerFileNameRoot);//Temp
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
	//UpdateUtil::DelFile(m_CacheResPath + g_VerFileName);//Temp
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
	std::wstring LocalVersionCaption = ((LJFP_Version*)m_pVerCache)->GetVersionCaption();
	GlobalNotifyLocalVersion(LocalVersionCaption);
	m_VersionOld = ((LJFP_Version*)m_pVerCache)->GetVersion();
	m_VersionOldCaption = ((LJFP_Version*)m_pVerCache)->GetVersionCaption();
	
	//m_Channel = ((LJFP_Version*)m_pVerCache)->GetChannel();
	//m_ChannelCaption = ((LJFP_Version*)m_pVerCache)->GetChannelCaption();

	m_VersionBase = ((LJFP_Version*)m_pVerCache)->GetVersionBase();
	m_VersionBaseCaption = ((LJFP_Version*)m_pVerCache)->GetVersionCaptionBase();
	m_Channel = UpdateJson::GetChannelNameInt();
	m_ChannelCaption = UpdateJson::GetChannelNameWStr();

	m_DownloadSiteIsBack = 0;
    UpdateJson::NewInstance()->SetCB_SetDownloadSite_Func(UpdateManagerEx::SetDownloadSite);
	UpdateJson::NewInstance()->RequestUpdateJson(g_JsonSite, m_VersionBaseCaption, m_ChannelCaption);
	cocos2d::extension::CCHttpClient* HTTPClient = cocos2d::extension::CCHttpClient::getInstance();
	int iDispatchDelayTime = 0;
	while (m_DownloadSiteIsBack == 0)
	{
		if (iDispatchDelayTime >= 90000)
		{
			iDispatchDelayTime = 0;
			HTTPClient->dispatchResponseCallbacks(0);
		}
		iDispatchDelayTime++;
	}
	if (m_DownloadSite.compare(L"") == 0){ return -1; }
	UpdateUtil::DelFile(m_CacheUpdatePath + g_VerFileName);
	URLInfoArr UIArr = ((LJFP_Version*)m_pVerCache)->GetURLInfoArr();
	std::wstring CacheVersionCaptionBase = ((LJFP_Version*)m_pVerCache)->GetVersionCaptionBase() + L"/";
	//bResult = UpdateUtil::DownloadFileEx(UIArr, m_SystemType, m_NetworkType, CacheVersionCaptionBase, g_VerFileName + UpdateUtil::GetRandomDownloadParam(), m_CacheUpdatePath, L"", g_VerFileName, false);
	bResult = UpdateUtil::DownloadFile(m_DownloadSite, CacheVersionCaptionBase, g_VerFileName + UpdateUtil::GetRandomDownloadParam(), m_CacheUpdatePath, L"", g_VerFileName, false);
	if (bResult != true){ return -1; }
	bResult = UpdateUtil::ExistFile(m_CacheUpdatePath + g_VerFileName);
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
	if (((LJFP_Version*)m_pVerCache)->GetVersion() < ((LJFP_Version*)m_pVerUpdate)->GetVersion())//当前版本小于最新版�?
	{
		if (((LJFP_Version*)m_pVerCache)->GetVersion() < ((LJFP_Version*)m_pVerUpdate)->GetVersionMinimum())//当前版本小于最低需求版�?不能更新,重新下载
		{
			return -1;
		}
		else//当前版本大于等于最低需求版�?需要更�?
		{
			return 1;
		}
	}
	else//当前版本就是最新版�?
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
	bResult = UpdateUtil::ExistFile(m_CacheResPath + g_PackInfoFileName);
	if (!bResult)
	{
		UpdateUtil::CopyFile(m_RootResPath + g_PackInfoFileName, m_CacheResPath + g_PackInfoFileName);
	}
	m_pPICache = LoadPackInfo(m_CacheResPath + g_PackInfoFileName, 1, m_CacheResPath);
	if (!m_pPICache){ return -1; }
	UpdateUtil::DelFile(m_CacheUpdatePath + g_PackInfoZipFileName);
	URLInfoArr UIArr = ((LJFP_Version*)m_pVerCache)->GetURLInfoArr();
	std::wstring NewVersionCaptionBase = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaptionBase() + L"/";
	std::wstring NewVersionCaption = ((LJFP_Version*)m_pVerUpdate)->GetVersionCaption() + L"/";
	//bResult = UpdateUtil::DownloadFileEx(UIArr, m_SystemType, m_NetworkType, NewVersionCaptionBase + NewVersionCaption, g_PackInfoZipFileName, m_CacheUpdatePath, L"", g_PackInfoZipFileName, false);
	bResult = UpdateUtil::DownloadFile(m_DownloadSite, NewVersionCaptionBase + NewVersionCaption, g_PackInfoZipFileName, m_CacheUpdatePath, L"", g_PackInfoZipFileName, false);
	if (bResult != true){ return -1; }
	bResult = UpdateUtil::ExistFile(m_CacheUpdatePath + g_PackInfoZipFileName);
	if (bResult != true){ return -1; }
	UpdateUtil::DelFile(m_CacheUpdatePath + g_PackInfoFileName);
	LJFP_ZipFile ZF(9999, LJCRC32Func, LJZipFunc, LJUnZipFunc, LJSMS4Func, LJDeSMS4Func, "locojoy123456789");
	int iResult = ZF.UnZipFile(m_CacheUpdatePath + g_PackInfoZipFileName, m_CacheUpdatePath + g_PackInfoFileName);
	if (iResult != 0){ return -1; }
	bResult = UpdateUtil::ExistFile(m_CacheUpdatePath + g_PackInfoFileName);
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
		std::wstring CurPathName = UpdateUtil::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateUtil::GetFileNameFull(CurPathFileNameFull);
		unsigned int CurSize = ((LJFP_PackInfo*)m_pPIAdd)->GetFileInfo(i)->m_Size;
		unsigned int CurCRC32 = ((LJFP_PackInfo*)m_pPIAdd)->GetFileInfo(i)->m_CRC32;
		if (!UpdateUtil::ExistFileEx(m_CacheUpdatePath + CurPathName + CurFileNameFull, CurSize, CurCRC32))//如果本地文件不存在或者存在但校验不通过才下�?
		{
			bool bDownloadOK = false;
			for (unsigned int i2 = 0; i2 < 1; i2++)
			{
				//bResult = UpdateUtil::DownloadFileEx(UIArr, m_SystemType, m_NetworkType, 
				//	NewVersionCaptionBase + NewVersionCaption + CurPathName, CurFileNameFull,
				//	m_CacheUpdatePath, CurPathName, CurFileNameFull, true);
				bResult = UpdateUtil::DownloadFile(m_DownloadSite, NewVersionCaptionBase + NewVersionCaption + CurPathName, CurFileNameFull,
					m_CacheUpdatePath, CurPathName, CurFileNameFull, true);
				if (bResult != true){ continue; }
				bResult = UpdateUtil::ExistFileEx(m_CacheUpdatePath + CurPathName + CurFileNameFull, CurSize, CurCRC32);
				if (bResult != true){ continue; }
				bDownloadOK = true;
				break;
			}
			if (bDownloadOK != true){ return -1; }
		}
        GlobalNotifyStep(((i + 1) * 1.0 / FileCount) * 80);
	}
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIMod)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateUtil::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateUtil::GetFileNameFull(CurPathFileNameFull);
		unsigned int CurSize = ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_Size;
		unsigned int CurCRC32 = ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_CRC32;
		if (!UpdateUtil::ExistFileEx(m_CacheUpdatePath + CurPathName + CurFileNameFull, CurSize, CurCRC32))//如果本地文件不存在或者存在但校验不通过才下�?
		{
			bool bDownloadOK = false;
			for (unsigned int i2 = 0; i2 < 1; i2++)
			{
				//bResult = UpdateUtil::DownloadFileEx(UIArr, m_SystemType, m_NetworkType, 
				//	NewVersionCaptionBase + NewVersionCaption + CurPathName, CurFileNameFull,
				//	m_CacheUpdatePath, CurPathName, CurFileNameFull, true);
				bResult = UpdateUtil::DownloadFile(m_DownloadSite, NewVersionCaptionBase + NewVersionCaption + CurPathName, CurFileNameFull,
					m_CacheUpdatePath, CurPathName, CurFileNameFull, true);
				if (bResult != true){ continue; }
				bResult = UpdateUtil::ExistFileEx(m_CacheUpdatePath + CurPathName + CurFileNameFull, CurSize, CurCRC32);
				if (bResult != true){ continue; }
				bDownloadOK = true;
				break;
			}
			if (bDownloadOK != true){ return -1; }
		}
        GlobalNotifyStep(((i + 1 + FileCountAdd) * 1.0 / FileCount) * 80);
	}
    GlobalNotifyStep(80);
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
		std::wstring CurPathName = UpdateUtil::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateUtil::GetFileNameFull(CurPathFileNameFull);
		iResult = UpdateUtil::CopyFileEx(m_CacheUpdatePath, CurPathName, CurFileNameFull, m_CacheResPath, CurPathName, CurFileNameFull);
		if (iResult != 0){ return -1; }
        GlobalNotifyStep(80 + ((i + 1) * 1.0 / FileCount) * 10);
	}
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIMod)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateUtil::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateUtil::GetFileNameFull(CurPathFileNameFull);
		iResult = UpdateUtil::CopyFileEx(m_CacheUpdatePath, CurPathName, CurFileNameFull, m_CacheResPath, CurPathName, CurFileNameFull);
		if (iResult != 0){ return -2; }
        GlobalNotifyStep(80 + ((i + 1 + FileCountAdd) * 1.0 / FileCount) * 10);
	}
	iResult = UpdateUtil::CopyFileEx(m_CacheUpdatePath, L"", g_PackInfoFileName, m_CacheResPath, L"", g_PackInfoFileName);
	if (iResult != 0){ return -3; }
	iResult = UpdateUtil::CopyFileEx(m_CacheUpdatePath, L"", g_VerFileName, m_CacheResPath, L"", g_VerFileName);
	if (iResult != 0){ return -4; }
    GlobalNotifyStep(90);
	return 0;
}
int UpdateManagerEx::StepClearPackInfo()
{
	bool bResult = true;
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIAdd)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIAdd)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateUtil::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateUtil::GetFileNameFull(CurPathFileNameFull);
		bResult = UpdateUtil::DelFile(m_CacheUpdatePath + CurPathName + CurFileNameFull);
	}
    GlobalNotifyStep(93);
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIMod)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateUtil::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateUtil::GetFileNameFull(CurPathFileNameFull);
		bResult = UpdateUtil::DelFile(m_CacheUpdatePath + CurPathName + CurFileNameFull);
	}
    GlobalNotifyStep(96);
	for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIDel)->GetFileCount(); i++)
	{
		std::wstring CurPathFileNameFull = ((LJFP_PackInfo*)m_pPIDel)->GetFileInfo(i)->m_PathFileName;
		std::wstring CurPathName = UpdateUtil::GetFilePath(CurPathFileNameFull, L"");
		std::wstring CurFileNameFull = UpdateUtil::GetFileNameFull(CurPathFileNameFull);
		bResult = UpdateUtil::DelFile(m_CacheResPath + CurPathName + CurFileNameFull);
	}
    GlobalNotifyStep(99);
	bResult = UpdateUtil::DelFile(m_CacheUpdatePath + g_PackInfoFileName);
	bResult = UpdateUtil::DelFile(m_CacheUpdatePath + g_VerFileName);
	if (bResult != true){ return -1; }
    GlobalNotifyStep(100);
	return 0;
}
bool UpdateManagerEx::Run()
{
#ifdef WIN7_32
	return true;
#endif
    int iState = 1;
    int iResult = 0;
    
    while (iState >= 0)
    {
        if (iState == 0)
        {
            if (m_FormResult < 0)
            {
				UpdateJson::ExitApp();
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
            GlobalNotifyMsgByKey(L"upmgrstr11");//正在连接服务�?..
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
                iState = 3;
            }
            else if (iResult < 0)
            {
                m_FormResult = 0;
                iState = 0;
                GlobalNotifyShowForm(1, 0, L"");
            }
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
                GlobalNotifyShowForm(2, 0, AppURL);
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
                GlobalNotifyShowForm(3, 0, L"");
            }
        }
        else if (iState == 5)
        {
            iResult = StepCheckPackInfo();
            if (iResult == 0)
            {
                int iDownloadSize = 0;
                
                for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIAdd)->GetFileCount(); i++)
                {
					iDownloadSize = iDownloadSize + ((LJFP_PackInfo*)m_pPIAdd)->GetFileInfo(i)->m_Size;
                }
                for (unsigned int i = 0; i < ((LJFP_PackInfo*)m_pPIMod)->GetFileCount(); i++)
                {
					iDownloadSize = iDownloadSize + ((LJFP_PackInfo*)m_pPIMod)->GetFileInfo(i)->m_Size;
                }
                m_FormResult = 0;
                iState = 0;
                iDownloadSize = iDownloadSize / 1024;
                //iDownloadSize = iDownloadSize / 1024;
                GlobalNotifyShowForm(4, iDownloadSize, L"");
            }
            else if (iResult < 0)
            {
                return false;
            }
        }
        else if (iState == 6)
        {
            GlobalNotifyMsgByKey(L"upmgrstr61");//自动更新�?..
            iResult = StepDownloadPackInfo();
            if (iResult == 0)
            {
                iState = 7;
                GlobalNotifyStep(80);
            }
            else if (iResult < 0)
            {
                m_FormResult = 0;
                iState = 0;
                GlobalNotifyShowForm(5, 0, L"");
            }
        }
        else if (iState == 7)
        {
            iResult = StepUpdatePackInfo(); GlobalNotifyStep(90);
            if (iResult != 0){ return false; }
            iResult = StepClearPackInfo(); GlobalNotifyStep(100);
            //if (iResult != 0){ return false; }//不管是旧资源否全部删除成�?只要新增和修改的文件都拷贝完成就算更新成�?
            GlobalNotifyMsgByKey(L"upmgrstr71");//更新完成
            return true;
        }
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
        //std::this_thread::sleep_for(std::chrono::seconds(100));
#else
        //sleep(100);
#endif
    }
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