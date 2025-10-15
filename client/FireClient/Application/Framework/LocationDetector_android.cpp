//////////////////////////////////////////////////////////////////////////
// File:		LocationDetector_android.cpp
// Author:	杨春雷
// Date:		2016/05/13
//////////////////////////////////////////////////////////////////////////

#include "LocationDetector.h"

#include "JniHelper.h"

#include <android/log.h>
#include <jni.h>

#include <android/log.h>
#define  LOG_TAG    "location"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

static const char* CLASS_NAME = "com/locojoy/mini/mt3/GameApp";

static void startDetectLocationJNI()
{
	cocos2d::JniMethodInfo t;

	if (cocos2d::JniHelper::getStaticMethodInfo(t, CLASS_NAME, "startDetectLocation", "()V"))
	{
		t.env->CallStaticVoidMethod(t.classID, t.methodID);

		t.env->DeleteLocalRef(t.classID);
	}
}

extern "C"
{
	JNIEXPORT void Java_com_locojoy_mini_mt3_GameApp_nativeClearLocations(JNIEnv* env, jobject thiz)
	{
		LocationDetector* pLocDetector = LocationDetector::GetInstance();
		if (pLocDetector)
		{
			pLocDetector->clear();
		}
	}

	JNIEXPORT void Java_com_locojoy_mini_mt3_GameApp_nativeAddLocation(JNIEnv* env, jobject thiz, jdouble longitude, jdouble latitude, jstring name)
	{
		LocationDetector* pLocDetector = LocationDetector::GetInstance();
		if (pLocDetector)
		{
			const char* utf = env->GetStringUTFChars(name, NULL);

			LocationDetector::Location loc = { longitude, latitude, utf };
			pLocDetector->addLocation(loc);

			LOGD("addLocation: %f, %f, %s", longitude, latitude, utf);

			env->ReleaseStringUTFChars(name, utf);
		}
	}

	JNIEXPORT void Java_com_locojoy_mini_mt3_GameApp_nativeFinishLocationDetect(JNIEnv* env, jobject thiz, jboolean bSuccess)
	{
		LocationDetector* pLocDetector = LocationDetector::GetInstance();
		if (pLocDetector)
		{
			pLocDetector->onFinishDetect(bSuccess);
		}
	}
}

LocationDetector::LocationDetector()
{}

LocationDetector::~LocationDetector()
{}

bool LocationDetector::isAvailable() const
{
	return true;
}

bool LocationDetector::isAllowed() const
{
	return true;
}

void LocationDetector::startDetect()
{
	clear();
	startDetectLocationJNI();
}
