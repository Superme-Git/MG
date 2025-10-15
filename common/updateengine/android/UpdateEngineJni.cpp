#include "SDJniHelper.h"

#include <android/log.h>
#include <jni.h>
#define  LOG_TAG    "updateenginejni"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

#include "UpdateEngine.h"
#include "../../common/platform/utils/StringCover.h"

extern "C"
{

	void Java_com_locojoy_mini_mt3_GameApp_nativeStartResourceUpdate(JNIEnv* env)
	{
		UpdateEngine::Run();
	}

	void Java_com_locojoy_mini_mt3_GameApp_nativeContinueResourceUpdate(JNIEnv* env, jobject thiz, jint result)
	{
		UpdateEngine::ContinueEx(result);
	}

	jstring Java_com_locojoy_mini_mt3_GameApp_nativeGetVoiceAddress(JNIEnv* env)
	{
		std::wstring ret = UpdateEngine::GetWGAdress();

		std::string str = ws2s(ret);

		return env->NewStringUTF(str.c_str());
	}
}
