#include "FileDownloader.h"
#include "GlobalNotification.h"
#include "utils/StringUtil.h"
#include "SDJniHelper.h"
#include <jni.h>
#include "../GlobalFunction_Common.cpp"

extern "C"

{
jint Java_com_locojoy_mini_updateengine_UpdateEngineActivity_getDocResVersion(JNIEnv* env, jobject thiz, jstring path)
{
    jint ji = 0;
    return ji;
}
	
}

void CopyGameSo() 
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"copyGameSo",
				"()V"))
	{   
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}

void UploadErrorFiles(const std::wstring& fileName)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"UploadErrorFiles",
				"(Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(fileName).c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
	}
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
bool IsDirectoryExisting( const wchar_t * lpszDirName )
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"IsDirectoryExisting",
				"(Ljava/lang/String;)Z"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(lpszDirName).c_str());
		bool ret = t.env->CallStaticBooleanMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
		return ret;
	}
	return false;
}

void CreateDir(const std::wstring& dir)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"CreateDir",
				"(Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(dir).c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
	}
}

void RemoveDir(const std::wstring& dir)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"RemoveDir",
				"(Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(dir).c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
	}
}

void CopyFile(const std::string& src, const std::string& dst)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"copyLib",
				"(Ljava/lang/String;Ljava/lang/String;)V"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(src.c_str());
		jstring stringArg2 = t.env->NewStringUTF(dst.c_str());
		t.env->CallStaticVoidMethod(t.classID, t.methodID, stringArg1, stringArg2);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
		t.env->DeleteLocalRef(stringArg2);
	}
}

bool CheckMD5(std::string fileName, std::string md5FileName)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"CheckMD5",
				"(Ljava/lang/String;Ljava/lang/String;)Z"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(fileName.c_str());
		jstring stringArg2 = t.env->NewStringUTF(md5FileName.c_str());
		bool b = t.env->CallStaticObjectMethod(t.classID, t.methodID, stringArg1, stringArg2);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
		t.env->DeleteLocalRef(stringArg2);
		return b;
	}
	return false;
}

std::string jString2str(JNIEnv* env, jstring jstr)
{
	char* rtn = NULL;
	jclass clsstring = env->FindClass("java/lang/String");
	jstring strencode = env->NewStringUTF("utf-8");
	jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
	jbyteArray barr= (jbyteArray)env->CallObjectMethod(jstr, mid, strencode);
	jsize alen = env->GetArrayLength(barr);
	jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
	if (alen > 0)
	{
		rtn = (char*)malloc(alen+1);
		memcpy(rtn, ba, alen);
		rtn[alen]=0;
	}
	env->ReleaseByteArrayElements(barr, ba, 0);
	std::string stemp(rtn);
	free(rtn);
	return stemp;
}

std::wstring GetFileMD5(std::string fileName)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"GetFileMD5",
				"(Ljava/lang/String;)Ljava/lang/String;"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(fileName.c_str());

		jstring jstr = (jstring)(t.env->CallStaticObjectMethod(t.classID, t.methodID, stringArg1));
		std::string str = jString2str(t.env, jstr);
		std::wstring ret = SHARE_String2Wstring(str);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
		return ret; 
	}
	return L"";
}

bool UnzipFile(std::string zipFileName, std::string targetPath)
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"unZipFileToTargetDir",
				"(Ljava/lang/String;Ljava/lang/String;)Z"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(zipFileName.c_str());
		jstring stringArg2 = t.env->NewStringUTF(targetPath.c_str());
		bool b = t.env->CallStaticObjectMethod(t.classID, t.methodID, stringArg1, stringArg2);
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
		t.env->DeleteLocalRef(stringArg2);
		return b;
	}
	return false;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void GlobalNotify(const wchar_t* format, ...)
{
    
    va_list ap;
    va_start(ap,format);
    std::wstring instring;
    wchar_t buffer[1024] = {0};
    
    int numforwrite = 0;
    
    numforwrite = vsnwprintf_x( buffer,1024,format,ap);
    
    if ( numforwrite > 0)
    {
        std::wstring str(buffer);
        instring = str;
    }
    
    else
        instring = L"";
    va_end(ap);
    
    onNotifyMsg(instring);
}

void GlobalNotifyMsgAndStep(const std::wstring& msg)
{
    onNotifyStart(msg);
}

void GlobalNotifyEnd()
{
   onNotifyEnd();
}

void GlobalNotifyFail()
{
   onNotifyFail();
}

void GlobalNotifyStep(int step)
{
    onNotifyStep(step);
}

void GlobalNotifyStep2(int spro, int epro, int sec)
{
	onNotifyStep2(spro, epro, sec);
}

void GlobalNotifyMsg(const std::wstring& msg)
{
    onNotifyMsg(msg);
}

void GlobalNotify_TextID(int textID)
{
    //GlobalNotify(GlobalGetText(textID).c_str()) ;
}

void GlobalNotifyLocalVersion(const std::wstring& localVersion)
{
    onNotifyLocalVersion(localVersion);
}

void GlobalNotifyNewVersion(const std::wstring& newVersion)
{
    onNotifyLatestVersion(newVersion);
}

void GlobalNotifyDownLoadSize(LJFM::fsize_t downloaded , LJFM::fsize_t total)
{
	std::wstring sizeString(L""); 
	FormatString(sizeString, L"%.1fkb/%.1fkb", downloaded/1024.0f, total/1024.0f);
    onNotifyDownLoadSize(sizeString);
}

void GlobalNotifyDownLoadEnd()
{
    onNotifyDownLoadEnd();
}

void GlobalNotifyDownLoadSizeTooLarge(unsigned long long total)
{
    onNotifyDownLoadSizeTooLarge(total);
}

void GlobalNotifyAppUpdate(int ret, const std::wstring& downloadurl)
{
    onNotifyAppUpdate(ret);
}

void GlobalNotifyShowForm(int FormType, int DownloadSize, std::wstring AppURL)
{
    onNotifyShowForm(FormType, DownloadSize, AppURL);
}

void GlobalNotifyMsgByKey(const std::wstring& key)
{
	std::string result = "";
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"NotifyMsgByKey",
				"(Ljava/lang/String;)Ljava/lang/String;"))
	{   
		jstring stringArg1 = t.env->NewStringUTF(SHARE_Wstring2String(key).c_str());
		jstring ret = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID, stringArg1);
		
		result = MT3::JniHelper::jstring2string(ret);
		
		t.env->DeleteLocalRef(t.classID);
		t.env->DeleteLocalRef(stringArg1);
		t.env->DeleteLocalRef(ret);
	}
	
	onNotifyMsg(s2ws(result));
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////


bool needToSelectUpdateUrl()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"needToSelectUpdateUrl",
				"()Z"))
	{   
		bool ret = t.env->CallStaticBooleanMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
		return ret;
	}
	return false;
}

std::wstring getResourceUpdateUrl()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"getResourceUpdateUrl",
				"()Ljava/lang/String;"))
	{   
		jstring jstr = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
        return SHARE_String2Wstring(MT3::JniHelper::jstring2string(jstr).c_str());
	}
	return L"";
}

void saveHaveSelectUpdateUrl()
{
	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t, 
				"com/locojoy/mini/mt3/GameApp",
				"saveHaveSelectUpdateUrl",
				"()V"))
	{   
		t.env->CallStaticVoidMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);
	}
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
