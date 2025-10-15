#include "Utils.h"
#include "FileUtil.h"
#include "StringCover.h"

#include "../log/CoreLog.h"

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <direct.h>
#include <io.h>
#include <stdio.h>
#define MY_MKDIR(a) _mkdir((a))
#define MY_ACCESS _access
#define MY_RMDIR(a) _rmdir((a))
#define MY_RMFILE(a) remove((a))
#else
#include <sys/stat.h>
#include <unistd.h>
#include <stdio.h>
#define MY_MKDIR(a) mkdir((a), 0777)
#define MY_ACCESS access
#define MY_RMDIR(a) rmdir((a))
#define MY_RMFILE(a) remove((a))
#endif

#ifdef ANDROID
#include "../android/SDJniHelper.h"
#include <jni.h>
#endif

namespace MHSD_UTILS
{
	const std::wstring& GetRootDir()
    {
        static std::wstring strDir = StringCover::to_wstring(CFileUtil::GetRootDir());
        return strDir;
    }
    
    const std::wstring& GetDocDir()
    {
        static std::wstring strDir = StringCover::to_wstring(CFileUtil::GetDocDir());
        return strDir;
    }
    
    const std::wstring& GetRunDir()
	{
#ifdef WIN7_32
	#ifdef NoPack
		static std::wstring strDir = StringCover::to_wstring(CFileUtil::GetCacheDir())+L"/res";
	#else
		static std::wstring strDir = StringCover::to_wstring(CFileUtil::GetCacheDir()) + L"/res1";
	#endif
#else
		static std::wstring strDir = StringCover::to_wstring(CFileUtil::GetCacheDir()) + L"/res";
#endif
        return strDir;
	}

	bool ExistFile(const std::wstring& strFileName)
	{
		std::string strPath = StringCover::to_string(strFileName);
		int nRet = MY_ACCESS(strPath.c_str(), 0);
		return nRet == 0;
	}

	bool ExistDir(const std::wstring& strDir)
	{
		return ExistFile(strDir);
	}

	std::wstring CreateDir(const std::wstring& strDir)
	{
		std::string strPath = StringCover::to_string(strDir);

		int nRet = MY_ACCESS(strPath.c_str(), 0);
		if (nRet != 0)
		{
			nRet = MY_MKDIR(strPath.c_str());
			if (nRet != 0)
			{
				return L"";
			}
		}
		return StringCover::to_wstring(strPath);
	}

	bool RemoveFile(const std::wstring& strFileName)
	{
		std::string strPath = StringCover::to_string(strFileName);

		if (ExistFile(strFileName))
		{
			int nRet = MY_RMFILE(strPath.c_str());
			if (nRet == 0)
			{
				return true;
			}
		}
		return false;
	}

	bool RemoveDir(const std::wstring& strDir)
	{
		std::string strPath = StringCover::to_string(strDir);

		if (ExistDir(strDir))
		{
			int nRet = MY_RMDIR(strPath.c_str());
			if (nRet == 0)
			{
				return true;
			}
		}
		return false;
	}

    void flurryEvent(std::wstring s , bool once)
    {
        core::Logger::flurryEvent(s,once);
    }

}//namepace MHSD_UTILS

namespace IOS_MHSD_UTILS {
#if (defined ANDROID)

void OpenURL(const std::string& urlstring)
{
	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
		"com/locojoy/mini/mt3/AndroidSystemInfo", "openURL", "(Ljava/lang/String;)V"))
	{
		jstring stringArg1 = methodInfo.env->NewStringUTF(urlstring.c_str());
		methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID, stringArg1);
		methodInfo.env->DeleteLocalRef(methodInfo.classID);
	}	
}

float GetCurBrightness()
{
	float brightness = 1.f;
	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
		"com/locojoy/mini/mt3/AndroidSystemInfo", "getCurBrightness", "()F"))
	{
		brightness = methodInfo.env->CallStaticFloatMethod(methodInfo.classID, methodInfo.methodID);
		methodInfo.env->DeleteLocalRef(methodInfo.classID);
	}
	return brightness;
}

void  SetCurBrightness(float brightness)
{
	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
		"com/locojoy/mini/mt3/AndroidSystemInfo", "setCurBrightness", "(F)V"))
	{
		methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID, brightness);
		methodInfo.env->DeleteLocalRef(methodInfo.classID);
	}
}

#endif

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
void OpenURL(const std::string& urlstring)
{
	size_t size = urlstring.length();
	wchar_t* buffer = new wchar_t[size + 1];
	MultiByteToWideChar(CP_ACP, 0, urlstring.c_str(), size, buffer, size*sizeof(wchar_t));
	buffer[size] = 0;
	ShellExecuteW(NULL, L"open", buffer, NULL, NULL, SW_SHOWNORMAL);
	delete[] buffer;
}
void GameResLoadError()
{}
float GetCurBrightness()
{
	return 1.0f;
}
void  SetCurBrightness(float brightness)
{}
#endif
}//namepace IOS_MHSD_UTILS

