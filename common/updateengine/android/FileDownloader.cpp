#include "FileDownloader.h"
#include "utils/StringUtil.h"
#include "SDJniHelper.h"

#include <android/log.h>

#define  LOG_TAG    "updateenginejni"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)


bool FileDownloader::SynDownloadOneFile(const std::wstring& urlstring, const std::wstring& destfile, bool notify)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/FileDownloader",
				"DownloadOneFile",
				"(Ljava/lang/String;Ljava/lang/String;Z)Z"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(urlstring).c_str());
		jstring stringArg2 = t.env->NewStringUTF(SHARE_Wstring2String(destfile).c_str());
		jboolean jnotify = notify;
		bool ret = t.env->CallStaticBooleanMethod(t.classID, t.methodID, stringArg1, stringArg2, jnotify);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
		t.env->DeleteLocalRef(stringArg2);
		return ret;
	}
	return false;
}
