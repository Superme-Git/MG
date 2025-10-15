#include "AsyncFileDownloader.h"
#include "utils/StringUtil.h"
#include "SDJniHelper.h"
#include <android/log.h>
#include <jni.h>
#define  LOG_TAG    "updateenginejni"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

#include "GlobalFunction.h"

void AsyncFileDownloader::InitUrlAndDestdir(const std::wstring& url, const std::wstring& destdir)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/updateengine/AsynFileDownloader",
				"InitUrlAndDestdir",
				"(Ljava/lang/String;Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(url).c_str());
		jstring stringArg2 = t.env->NewStringUTF(SHARE_Wstring2String(destdir).c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1, stringArg2);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
		t.env->DeleteLocalRef(stringArg2);
	}
}

void AsyncFileDownloader::DownloadOneFileAsyn(const std::wstring& filename)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/updateengine/AsynFileDownloader",
				"DownloadOneFileAsyn",
				"(Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(filename).c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
	}
}

void AsyncFileDownloader::Clear()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/updateengine/AsynFileDownloader",
				"Clear",
				"()V"))
	{   
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}


void AsyncFileDownloader::StartDownload()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/updateengine/AsynFileDownloader",
				"StartDownload",
				"()V"))
	{   
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}
