
#include "GlobalNotification.h"
#include "utils/StringUtil.h"
#include "SDJniHelper.h"

#include <android/log.h>
#define  LOG_TAG    "updateenginejni"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)


extern "C"
{

void onNotifyMsg(const std::wstring& msg)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyMsg",
				"(Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(msg).c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
	}
}

void onNotifyStart(const std::wstring& msg)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyStart",
				"(Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(msg).c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
	}
}
    
void onNotifyEnd()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyEnd",
				"()V"))
	{   
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}

void onNotifyFail()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyFail",
				"()V"))
	{   
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}
    
void onNotifyStep(int step)    
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyStep",
				"(I)V"))
	{   
		t.env->CallStaticVoidMethod(t.classID, t.methodID, step);
		t.env->DeleteLocalRef(t.classID);
	}
}
    
void onNotifyStep2(int spro, int epro, int sec)    
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyStep",
				"(III)V"))
	{   
		t.env->CallStaticVoidMethod(t.classID, t.methodID, spro, epro, sec);
		t.env->DeleteLocalRef(t.classID);
	}
}
    
void onNotifyLocalVersion(const std::wstring& localVersion )
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyLocalVersion",
				"(Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(localVersion).c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
	}
}
    
void onNotifyLatestVersion(const std::wstring& newVersion)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyLatestVersion",
				"(Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(newVersion).c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
	}
}
    
void onNotifyDownLoadSize(const std::wstring& sizeString)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyDownLoadSize",
				"(Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(sizeString).c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
	}
}
    
void onNotifyDownLoadEnd()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyDownloadEnd",
				"()V"))
	{   
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}
    
void onNotifyDownLoadSizeTooLarge(unsigned long long total)
{
	int total_in_mb = (int)(total/(1024.0f*1024.0f));
	std::string total_string = intToString(total_in_mb);

	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyDownLoadSizeTooLarge",
				"(Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(total_string.c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
	}
}
    
void onNotifyShowForm(int FormType, int DownloadSize, const std::wstring AppURL)
{
    MT3::JniMethodInfo t;
    if (MT3::JniHelper::getStaticMethodInfo(t,
                "com/locojoy/mini/mt3/GameApp",
                "NotifyShowForm",
                "(IILjava/lang/String;)V"))
    {
    	jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(AppURL).c_str());
        t.env->CallStaticVoidMethod(t.classID, t.methodID, FormType, DownloadSize, stringArg1);
        t.env->DeleteLocalRef(t.classID);
        t.env->DeleteLocalRef(stringArg1);
    }
}
    
void onNotifyAppUpdate(int ret)
{
}

}
