#include "stdafx.h"
#include "WavRecorder.h"
#include "utils/StringUtil.h"
#include "utils/FileUtil.h"

#include "SimpleAudioEngine.h"
#include "ConfigManager.h"

#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
#include "../../../../common/platform/android/SDJniHelper.h"
#include <jni.h>
#else
#include "SoundCapturer/SoundCapturer.h"
#endif

static std::string s_WavTempFilePath;
static std::string s_WavTempFileName; //wav临时文件名字.
static bool s_bRecording = false;

static WavRecorder s_WavRecorder;

#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
static CSoundCapturer moSoundCapturer;
static GUID gdDevGuid;
BOOL CALLBACK DSCaptureEnumCallBack(LPGUID lpGUID,
	LPCTSTR lpszDesc,
	LPCTSTR lpszDrvName,
	LPVOID lpContext)
{
	if (NULL != lpGUID) {
		gdDevGuid = *lpGUID;
	}
	return(TRUE);
}
#endif

WavRecorder* gGetWavRecorder()
{
	return &s_WavRecorder;
}

const char* WavRecorder::getTempFilePath()
{
	return s_WavTempFilePath.c_str();
}

const char* WavRecorder::getTempFileName()
{
	return s_WavTempFileName.c_str();
}

bool WavRecorder::isRecording()
{
	return s_bRecording;
}

bool WavRecorder::initialize(const char* tempFilePath)
{
	s_WavTempFilePath = tempFilePath;
	s_WavTempFileName = tempFilePath;
	s_WavTempFileName += "default.wav";
	return true;
}

void WavRecorder::release()
{
	stop();
}

bool WavRecorder::canRecord()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
		"com/locojoy/mini/mt3/WavRecorder", "getInstance", "()Lcom/locojoy/mini/mt3/WavRecorder;"))
	{
		jobject jobj = methodInfo.env->CallStaticObjectMethod(methodInfo.classID, methodInfo.methodID);
		if (jobj)
		{
			methodInfo.env->DeleteLocalRef(jobj);
			methodInfo.env->DeleteLocalRef(methodInfo.classID);

			if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
				"com/locojoy/mini/mt3/WavRecorder", "canRecord", "()Z"))
			{
				jboolean ret = methodInfo.env->CallStaticBooleanMethod(methodInfo.classID, methodInfo.methodID);
				methodInfo.env->DeleteLocalRef(methodInfo.classID);
				return ret;
			}
		}
	}
	return false;
#else
	MMRESULT lhMMResult = moSoundCapturer.SetEnumCallBackProc(DSCaptureEnumCallBack, NULL);
	if (MMSYSERR_NOERROR != lhMMResult) {
		return false;
	}
	else {
		return true;
	}
#endif
}

bool WavRecorder::start()
{
	int nError = remove(s_WavTempFileName.c_str());

#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	gGetVoiceManager()->setForbiddenVoicePlay(true);
	CocosDenshion::SimpleAudioEngine::sharedEngine()->stopAllEffects();
	CocosDenshion::SimpleAudioEngine::sharedEngine()->setEffectEnable(false);
	CocosDenshion::SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();

	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
		"com/locojoy/mini/mt3/WavRecorder", "getInstance", "()Lcom/locojoy/mini/mt3/WavRecorder;"))
	{
		jobject jobj = methodInfo.env->CallStaticObjectMethod(methodInfo.classID, methodInfo.methodID);
		if (jobj)
		{
			methodInfo.env->DeleteLocalRef(jobj);
			methodInfo.env->DeleteLocalRef(methodInfo.classID);

			if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
				"com/locojoy/mini/mt3/WavRecorder", "recordChat", "(Ljava/lang/String;)V"))
			{
				jstring stringArg = methodInfo.env->NewStringUTF(s_WavTempFileName.c_str());
				methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID, stringArg);
				s_bRecording = true;
				methodInfo.env->DeleteLocalRef(stringArg);
				methodInfo.env->DeleteLocalRef(methodInfo.classID);
			}
		}
	}
	if (!s_bRecording) {
		gGetVoiceManager()->setForbiddenVoicePlay(false);
		CocosDenshion::SimpleAudioEngine::sharedEngine()->setEffectEnable(true);
		int BackMusic = gGetGameConfigManager()->GetConfigValue(L"sound");
		if (BackMusic != 0) {
			CocosDenshion::SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
		}
	}
	return s_bRecording;

#else
	gGetVoiceManager()->setForbiddenVoicePlay(true);
	CocosDenshion::SimpleAudioEngine::sharedEngine()->stopAllEffects();
	CocosDenshion::SimpleAudioEngine::sharedEngine()->setEffectEnable(false);
	CocosDenshion::SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();
	MMRESULT lhMMResult = moSoundCapturer.StartRecord(&gdDevGuid);
	if(MMSYSERR_NOERROR != lhMMResult) {
		s_bRecording = false;
		gGetVoiceManager()->setForbiddenVoicePlay(false);
		CocosDenshion::SimpleAudioEngine::sharedEngine()->setEffectEnable(true);
		int BackMusic = gGetGameConfigManager()->GetConfigValue(L"sound");
		if (BackMusic != 0) {
			CocosDenshion::SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
		}
	}
	else {
		s_bRecording = true;
	}
	return s_bRecording;
#endif
}

void WavRecorder::stop()
{
	s_bRecording = false;
	gGetVoiceManager()->setForbiddenVoicePlay(false);

#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
		"com/locojoy/mini/mt3/WavRecorder", "getInstance", "()Lcom/locojoy/mini/mt3/WavRecorder;"))
	{
		jobject jobj = methodInfo.env->CallStaticObjectMethod(methodInfo.classID, methodInfo.methodID);
		if (jobj)
		{
			methodInfo.env->DeleteLocalRef(jobj);
			methodInfo.env->DeleteLocalRef(methodInfo.classID);

			if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
				"com/locojoy/mini/mt3/WavRecorder", "stopRecord", "()V"))
			{
				methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID);
				methodInfo.env->DeleteLocalRef(methodInfo.classID);
			}
		}
	}
	return;
#else
	MMRESULT lhMMResult = moSoundCapturer.StopRecord();
	if (MMSYSERR_NOERROR != lhMMResult) {
		return;
	}
	std::wstring strFileName = s2ws(s_WavTempFileName);
	wchar_t* lswFileName = const_cast<wchar_t*>(strFileName.c_str());
	lhMMResult = moSoundCapturer.SaveRecordToFile(lswFileName);
	if (MMSYSERR_NOERROR != lhMMResult) {
		return;
	}
#endif
}

