//  FileUtil.cpp
//  share

#include "FileUtil.h"
#include "StringUtil.h"
//#include <sys/param.h>

#ifndef ISEDITOR
#include "cocos2d.h"
#endif

#if (defined WIN7_32)
#include <windows.h>
#include <psapi.h>
#pragma comment(lib,"psapi.lib")
#endif


std::string CFileUtil::GetRootDir()
{
#ifdef ANDROID
    std::string ret = std::string(cocos2d::CCFileUtils::sharedFileUtils()->getResDir());
	CCLOG("[Res Path:]%s", ret);
    return ret;
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	std::wstring root(Windows::ApplicationModel::Package::Current->InstalledLocation->Path->Data());
	root += L"\\resource";
	return SHARE_Wstring2String(root);
#elif (defined ISEDITOR)
	return "";
#else
    std::string ret = cocos2d::CCFileUtils::sharedFileUtils()->fullPathFromRelativePath("");
    ret = ret.substr(0, ret.rfind("/"));
    return ret;
#endif
}

std::string CFileUtil::GetDocDir()
{
#ifdef ANDROID
    std::string ret = std::string(cocos2d::CCFileUtils::sharedFileUtils()->getResDir());
    return ret;
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	std::wstring root(Windows::Storage::ApplicationData::Current->LocalFolder->Path->Data());
	return SHARE_Wstring2String(root);
#elif (defined ISEDITOR)
	return "";
#elif (defined WIN7_32)
	return GetRootDir();
#else
	std::string ret = std::string(cocos2d::CCFileUtils::sharedFileUtils()->getWriteablePath());
	ret = ret.substr(0, ret.rfind("/"));
	return ret;
#endif
}

std::string CFileUtil::MakePath(const char* strPre, const char* strSub)
{
    std::string ret = strPre;
    if (ret.empty())
	{
#ifdef WIN7_32	//by lg
		return strSub;
#else
		return ret;
#endif
    }
    char* sub = (char*)strSub;
    if (strSub[0] == '/') {
        sub++;
    }
    
    if (ret[ret.length() - 1] == '/') {
        ret += sub;
    }else
    {
        ret = ret + '/' + sub;
    }
    
    return ret;
}

std::string CFileUtil::GetCacheDir()
{
#ifdef ANDROID
    std::string ret = std::string(cocos2d::CCFileUtils::sharedFileUtils()->getResDir())+"/cache";
    return ret;
#elif (defined WIN7_32) || (defined ISEDITOR)
	return GetRootDir() + "cache";
#else
    std::string ret = std::string(cocos2d::CCFileUtils::sharedFileUtils()->getCachePath());
    ret = ret.substr(0, ret.rfind("/"));
    return ret;
#endif
}

std::string CFileUtil::GetTempDir()
{
#ifdef ANDROID
	return std::string(cocos2d::CCFileUtils::sharedFileUtils()->getResDir()) + "/temp";
#elif (defined WIN7_32)
	return GetRootDir() + "temp";
#else
    std::string ret = std::string(cocos2d::CCFileUtils::sharedFileUtils()->getTempPath());
    ret = ret.substr(0, ret.rfind("/"));
    return ret;
#endif
}

std::string CFileUtil::GetLogDir()
{
	return GetDocDir();
}

bool CFileUtil::IsCurLanguageSimpleCh()
{
	return true;
}

double CFileUtil::GetTotalMemory()
{
#if (defined WIN32)
	MEMORYSTATUSEX memstatus = { sizeof(memstatus) };
	::GlobalMemoryStatusEx(&memstatus);
	return memstatus.ullTotalPhys / 1024.0 / 1024.0;
#else
	return 0.0;
#endif
}

double CFileUtil::GetAvailableMemory()
{
#if (defined WIN32)
	MEMORYSTATUSEX memstatus = { sizeof(memstatus) };
	::GlobalMemoryStatusEx(&memstatus);
	return memstatus.ullAvailPhys / 1024.0 / 1024.0;
#else
	return 0.0;
#endif
}

double CFileUtil::GetUsedMemory()
{
#if (defined WIN32)
	HANDLE handle = GetCurrentProcess();
	PROCESS_MEMORY_COUNTERS pmc;
	::GetProcessMemoryInfo(handle, &pmc, sizeof(pmc));
	return pmc.PagefileUsage / 1024.0 / 1024.0;
#else
	return 0.0;
#endif
}

int CFileUtil::GetFileArrOfPath(std::wstring wsBasePath, std::wstring wsFileType, bool bLoop, std::vector<std::wstring>& FileArr)
{
	return 0;
}

int CFileUtil::DelFileArrOfPath(std::wstring wsBasePath, std::wstring wsFileType, bool bLoop)
{
	return 0;
}