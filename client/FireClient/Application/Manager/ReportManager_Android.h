/***********************************************************************
filename: 	ReportManager.h
purpose:	数据上报管理器
*************************************************************************/
#pragma once
#include "Singleton.hpp"
#include "LoginManager.h"
#include "ConnectGetServerInfo.h"
#include "../../../../common/platform/utils/FileUtil.h"

#include "../../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/android/jni/JniHelper.h"
#include <jni.h>

class ReportManager : public CSingleton < ReportManager >
{
public:
	ReportManager()
	{

	}
	~ReportManager()
	{

	}

	std::string GetRandomParamByTime()
	{
		std::string sResult;;
		std::wstring wsResult;
		wsResult = gGetLoginManager()->GetDeviceid() + L"_";
		struct timeval now;
		if (gettimeofday(&now, NULL) != 0)
		{
			std::string sResult = StringCover::to_string(wsResult);
			return sResult;
		}
		long CurSec = now.tv_sec + now.tv_usec / 1000000;
		if (CurSec < 0)
		{
			CurSec = CurSec * -1;
		}
		while (CurSec > 4294967296 - 1)
		{
			CurSec = CurSec / 2;
		}
		unsigned int uiSec = CurSec;
		wsResult += StringCover::NumTowstring(uiSec);
		sResult = StringCover::to_string(wsResult);
		sResult += ".zip";
		return sResult;
	}

	int UpdateFileByFTP(std::string sURL, std::string sUserName, std::string sPassWord, std::string sSrcFileNameFull, std::string sDstFileNameRemote)
	{
		return 0;
	}
	std::string CompressLog(std::string sCustomZipFN)
	{
		std::string sZipFN;
		std::string sZipFNFull;
		std::string sRemoteFN;

		if (sCustomZipFN != "")
		{
			sZipFN = sCustomZipFN;
		}
		else
		{
			sZipFN = GetRandomParamByTime();
		}
		sZipFNFull = CFileUtil::MakePath(CFileUtil::GetLogDir().c_str(), sZipFN.c_str());
		sRemoteFN = sZipFN;

		cocos2d::JniMethodInfo methodInfo;
		if (cocos2d::JniHelper::getStaticMethodInfo(methodInfo,
			"com/locojoy/mini/mt3/GameApp",
			"PackAndUploadZipFile",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"))
		{
			jstring stringLocolPath = methodInfo.env->NewStringUTF(CFileUtil::GetLogDir().c_str());
			jstring stringFileNameExt = methodInfo.env->NewStringUTF(".log");
			jstring stringZipFileName = methodInfo.env->NewStringUTF(sZipFN.c_str());

			methodInfo.env->CallStaticIntMethod(methodInfo.classID, methodInfo.methodID, stringLocolPath, stringFileNameExt, stringZipFileName);

			methodInfo.env->DeleteLocalRef(methodInfo.classID);
			methodInfo.env->DeleteLocalRef(stringLocolPath);
			methodInfo.env->DeleteLocalRef(stringZipFileName);
			methodInfo.env->DeleteLocalRef(stringFileNameExt);
		}
		return sZipFN;
	}
	int ReportLogByFTP(std::string sCustomZipFN)
	{
		CompressLog(sCustomZipFN);
		return 0;
	}

#ifdef WIN7_32

#elif defined ANDROID

#else//IOS

#endif

};
inline ReportManager* gGetReportManager()
{
	return ReportManager::GetInstance();
}
