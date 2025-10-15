//
//  FileUtil.cpp
//  share
//
//  Created by MagicStudio on 12-9-19.
//  Copyright 2012年 __MyCompanyName__. All rights reserved.
//

#include "FileUtil.h"
#include "SDJniHelper.h"

std::string getAssetsOutDirJNI()
{
        MT3::JniMethodInfo t;

        if (MT3::JniHelper::getStaticMethodInfo(t, 
                        "com/locojoy/mini/mt3/GameApp",
                        "getAssetsOutDir",
                        "()Ljava/lang/String;"))
        {
            jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
            t.env->DeleteLocalRef(t.classID);
			std::string ret = std::string(MT3::JniHelper::jstring2string(str).c_str());
            t.env->DeleteLocalRef(str);

            return ret;
        }

	return "";
}

std::string getCacheOutDirJNI()
{
        MT3::JniMethodInfo t;

        if (MT3::JniHelper::getStaticMethodInfo(t,
                        "com/locojoy/mini/mt3/GameApp",
                        "getCacheOutDir",
                        "()Ljava/lang/String;"))
        {
            jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
            t.env->DeleteLocalRef(t.classID);
			std::string ret = std::string(MT3::JniHelper::jstring2string(str).c_str());
            t.env->DeleteLocalRef(str);

            return ret;
        }

	return "";
}

std::string CFileUtil::GetRootDir()
{
    return getAssetsOutDirJNI();
}

std::string CFileUtil::GetDocDir()
{
	return GetRootDir();
}

std::string CFileUtil::MakePath(const char* strPre, const char* strSub)
{
    std::string ret = strPre;
    if (ret.empty()) {
        return ret;
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
    return getCacheOutDirJNI();
}

std::string CFileUtil::GetTempDir()
{
    std::string ret = GetRootDir() + "/temp";
    return ret;
}

std::string CFileUtil::GetLogDir()
{
	MT3::JniMethodInfo t;

	if (MT3::JniHelper::getStaticMethodInfo(t,
		"com/locojoy/mini/mt3/GameApp",
		"getLogOutDir",
		"()Ljava/lang/String;"))
	{
		jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
		std::string ret = std::string(MT3::JniHelper::jstring2string(str).c_str());
		t.env->DeleteLocalRef(str);

		return ret;
	}

	return "";
}

bool CFileUtil::IsCurLanguageSimpleCh()
{
	return true;
}

double CFileUtil::GetTotalMemory()
{
	int nResult = 0;
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t,
		"com/locojoy/mini/mt3/AndroidSystemInfo",
		"getTotalMemSize",
		"()I"))
	{
		nResult = t.env->CallStaticIntMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}

	return nResult;
}

double CFileUtil::GetAvailableMemory()
{
	int nResult = 0;
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t,
		"com/locojoy/mini/mt3/AndroidSystemInfo",
		"GetAvailableMemory",
		"()I"))
	{
		nResult = t.env->CallStaticIntMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}

	return nResult;
}

double CFileUtil::GetUsedMemory()
{
	int nResult = 0;
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t,
		"com/locojoy/mini/mt3/AndroidSystemInfo",
		"GetUsedMemory",
		"()I"))
	{
		nResult = t.env->CallStaticIntMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}

	return nResult;
}

int CFileUtil::GetFileArrOfPath(std::wstring wsBasePath, std::wstring wsFileType, bool bLoop, std::vector<std::wstring>& FileArr)
{
	return 0;
}

int CFileUtil::DelFileArrOfPath(std::wstring wsBasePath, std::wstring wsFileType, bool bLoop)
{
	int nResult = 0;

	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t,
		"com/locojoy/mini/mt3/GameApp",
		"DelFileArrOfPath",
		"(Ljava/lang/String;Ljava/lang/String;Z)I"))
	{
		jstring strBasePath = t.env->NewStringUTF(SHARE_Wstring2String(wsBasePath).c_str());
		jstring strFileType = t.env->NewStringUTF(SHARE_Wstring2String(wsFileType).c_str());

		nResult = t.env->CallStaticIntMethod(t.classID, t.methodID, strBasePath, strFileType, bLoop);

		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(strBasePath);
		t.env->DeleteLocalRef(strFileType);
	}

	return nResult;
}