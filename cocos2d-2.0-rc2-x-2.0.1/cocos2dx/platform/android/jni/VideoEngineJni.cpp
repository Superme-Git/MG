#include "VideoEngineJni.h"

#include "JniHelper.h"
#include "cocoa/CCString.h"

#include <android/log.h>
#include <jni.h>

#if 0
#define  LOG_TAG    "SystemInfoJni"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define  LOGD(...)
#endif

using namespace cocos2d;

static const char* CLASS_NAME = "org/cocos2dx/lib/Cocos2dxVideoHelper";

extern "C"
{
	void createVideoActivityJni()
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "createVideoActivity", "()V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	void destroyVideoActivityJni()
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "destroyVideoActivity", "()V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	int createVideoWidgetJNI()
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "createVideoWidget", "()I"))
		{
			jint index = t.env->CallStaticIntMethod(t.classID, t.methodID);

			t.env->DeleteLocalRef(t.classID);

			return index;
		}

		return -1;
	}

	void removeVideoWidgetJNI(int index)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "removeVideoWidget", "(I)V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID, index);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	void setVideoURLJNI(int index, int sourceType, const char* szUrl)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "setVideoUrl", "(IILjava/lang/String;)V"))
		{
			jstring jsUrl = t.env->NewStringUTF(szUrl);

			t.env->CallStaticVoidMethod(t.classID, t.methodID, index, sourceType, jsUrl);

			t.env->DeleteLocalRef(jsUrl);
			t.env->DeleteLocalRef(t.classID);
		}
	}

	void startVideoJNI(int index)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "startVideo", "(I)V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID, index);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	void pauseVideoJNI(int index)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "pauseVideo", "(I)V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID, index);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	void resumeVideoJNI(int index)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "resumeVideo", "(I)V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID, index);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	void stopVideoJNI(int index)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "stopVideo", "(I)V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID, index);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	void seekVideoToJNI(int index, int pos)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "seekVideoTo", "(II)V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID, index, pos);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	void setVideoRectJNI(int index, int x, int y, int width, int height)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "setVideoRect", "(IIIII)V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID, index, x, y, width, height);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	void setVideoVisibleJni(int index, bool bVisible)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "setVideoVisible", "(IZ)V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID, index, bVisible);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	void setVideoKeepRatioEnabledJni(int index, bool bEnable)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "setVideoKeepRatioEnabled", "(IZ)V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID, index, bEnable);

			t.env->DeleteLocalRef(t.classID);
		}
	}

	void setFullScreenEnabledJni(int index, bool bEnable, int width, int height)
	{
		JniMethodInfo t;

		if (JniHelper::getStaticMethodInfo(t, CLASS_NAME, "setFullScreenEnabled", "(IZII)V"))
		{
			t.env->CallStaticVoidMethod(t.classID, t.methodID, index, bEnable, width, height);

			t.env->DeleteLocalRef(t.classID);
		}
	}
}
