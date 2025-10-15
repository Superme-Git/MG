#include "stdafx.h"
#include "DeviceData.h"

// IOS ---------------------------------------------------------------------------
#ifdef OS_IOS
#include "DeviceData_iOS.h"
float DeviceData::sGetBatteryRatio()
{
	return gGetBatteryRatio_IOS();
}

int DeviceData::sGetNetworkType()
{
	return gGetNetworkType_IOS();
}

bool DeviceData::sIsNetworkConnected()
{
    return gIsNetworkConnected_IOS();
}

bool DeviceData::isBatteryCharging()
{
    return gIsBatteryCharging_IOS();
}



// Android -----------------------------------------------------------------------
#elif (defined ANDROID)
#include "../../../common/platform/android/SDJniHelper.h"
#include <jni.h>

float DeviceData::sGetBatteryRatio()
{
	float ratio = 1.f;
	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
												"com/locojoy/mini/mt3/AndroidSystemInfo",
												"getBatteryRatio",
												"()F"))
	{
		ratio = methodInfo.env->CallStaticFloatMethod(methodInfo.classID, methodInfo.methodID);
		methodInfo.env->DeleteLocalRef(methodInfo.classID);
	}
	return ratio;
}

//if is wifi return 1, else return 0
int DeviceData::sGetNetworkType()
{
	int networkType = 0;
	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
												"com/locojoy/mini/mt3/AndroidSystemInfo",
												"getCurNetWorkType",
												"()I"))
	{
		networkType = methodInfo.env->CallStaticIntMethod(methodInfo.classID, methodInfo.methodID);
		methodInfo.env->DeleteLocalRef(methodInfo.classID);
	}		
	return networkType;
}

bool DeviceData::sIsNetworkConnected()
{
	bool isNetworkConnected = false;
	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
		"com/locojoy/mini/mt3/AndroidSystemInfo",
		"IsNetworkConnected",
		"()Z"))
	{
		isNetworkConnected = methodInfo.env->CallStaticBooleanMethod(methodInfo.classID, methodInfo.methodID);
		methodInfo.env->DeleteLocalRef(methodInfo.classID);
	}
	return isNetworkConnected;
}

bool DeviceData::isBatteryCharging()
{
	bool bCharging = false;
	MT3::JniMethodInfo methodInfo;
	if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
		"com/locojoy/mini/mt3/AndroidSystemInfo",
		"isBatteryCharging",
		"()Z"))
	{
		bCharging = methodInfo.env->CallStaticBooleanMethod(methodInfo.classID, methodInfo.methodID);
		methodInfo.env->DeleteLocalRef(methodInfo.classID);
	}
	return bCharging;
}



// Windows Phone -----------------------------------------------------------------
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
float DeviceData::sGetBatteryRatio()
{
	return 1.0f;
}

int DeviceData::sGetNetworkType()
{
	return eNetworkWifi;
}

bool DeviceData::sIsNetworkConnected()
{
    return true;
}

bool DeviceData::isBatteryCharging()
{
	return false;
}



// Win32 -------------------------------------------------------------------------
#elif (defined WIN7_32)

float DeviceData::sGetBatteryRatio()
{
	return 1.0f;
}

int DeviceData::sGetNetworkType()
{
	return eNetworkWifi;
}

bool DeviceData::sIsNetworkConnected()
{
    return true;
}

bool DeviceData::isBatteryCharging()
{
	static bool sVal = false;
	return sVal;
}



// Other -------------------------------------------------------------------------
#else

float DeviceData::sGetBatteryRatio()
{
	return 1.0f;
}

int DeviceData::sGetNetworkType()
{
	return eNetworkWifi;
}

bool DeviceData::sIsNetworkConnected()
{
    return true;
}

bool DeviceData::isBatteryCharging()
{
	return false;
}

#endif



// No Device Limit -----------------------------------------------------------
char DeviceData::_buff[8] = {};
const char* DeviceData::sGetTimeStr()
{
	time_t t = time(0);
	memset(_buff, 0, 8);
	strftime(_buff, 8, "%H:%M", localtime(&t));
	return _buff;
}



