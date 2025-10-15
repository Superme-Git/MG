#include "stdafx.h"
#include "DeviceInfo.h"
#include "../ProtoDef/fire/pb/CNotifyDeviceInfo.hpp"
#include <map>
#include "SystemInfo.h"
#include "LoginManager.h"

#ifdef WIN7_32
#include <IPHlpApi.h>
#pragma comment(lib, "IPHlpApi.Lib")
#include "log/CoreLog.h"
#endif

#ifdef ANDROID
#include "../../../common/platform/android/SDJniHelper.h"
#include <jni.h>
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "ICSharpCallback.h"

#endif

#if (defined WIN7_32) || (defined ANDROID)
float MT3SystemInfo::sGetIOSVersionFloatValue()
{
	return 0.0f;
}

std::string MT3SystemInfo::sGetMacAddr()
{
	return "";
}
#endif

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

std::string DeviceInfo::s_strDevDesc = "";

int DeviceInfo::s_nCpuFrequency = 0;
int DeviceInfo::s_nMaxCpuFreq = 0;
int DeviceInfo::s_nCpuCount = 0;
std::string DeviceInfo::s_strCpuName = "";

int DeviceInfo::s_nTotalMemSize = 0;
int DeviceInfo::s_nFreeMemSize = 0;

float DeviceInfo::s_fAppCpuRate = 0.f;
std::string DeviceInfo::s_strAppSrc = "";
std::string DeviceInfo::s_strOSVersion = "";
int			DeviceInfo::s_iOSVersionCode = 10;
std::string DeviceInfo::s_strProductModel = "";
std::string DeviceInfo::s_strNetEnvir = "";
std::string DeviceInfo::s_strMacAddress = "00:00:00:00:00";
std::string DeviceInfo::s_strNoColonMacAddress = "00:00:00:00:00";
std::string DeviceInfo::s_strScreenWidthInfo = "";
std::string DeviceInfo::s_strScreenHeightInfo = "";
std::string DeviceInfo::s_strVersionName = "";
std::string DeviceInfo::s_strPhoneNumber = "";
std::string DeviceInfo::s_strDeviceID = "";
std::string DeviceInfo::s_strProvider = "";
std::string DeviceInfo::s_strIPAddress = "";


// 0:DEBUG, 1:ios, 2:android, 3:wp8, 4:Windows
int DeviceInfo::sGetDeviceType()
{
#ifdef WIN7_32	//by lg
	return 4;
#elif defined( DEBUG )
	return 0;
#elif defined( ANDROID )
	return 2;
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	return 3;
#else
	return 1;
#endif
}

const char* DeviceInfo::sGetCurDeviceName()
{
	int deviceType = DeviceInfo::sGetDeviceType();
	switch (deviceType) {

	case 0: //DEBUG
	{
		return "PC";
	}
	break;

	case 1: //ios
	{
		return "IOS";
	}
	break;

	case 2: //android
	{
		return "ANDROID";
	}
	break;

	default: //wp8
	{
		return "WP8";
	}
	break;

	}
}

#ifdef ANDROID
void DeviceInfo::sInitDeviceInfo()
{
	DeviceInfo::sGetProvider();
	DeviceInfo::sGetMacAddress();
	DeviceInfo::sGetDeviceID();
	DeviceInfo::sGetNetEnvir();
	DeviceInfo::sGetOSVersion();
	DeviceInfo::sGetProductModel();
}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
void DeviceInfo::sInitDeviceInfo()
{
}
#elif (defined WIN7_32)
void DeviceInfo::sInitDeviceInfo()
{
	DeviceInfo::sGetMacAddress();
	DeviceInfo::sGetIPAddress();
	SDLOG_WARN(L"macAddress = %s, ipAddress = %s", DeviceInfo::sGetMacAddress(), DeviceInfo::sGetIPAddress());
	//DeviceInfo::sGetOSVersion();
}
#endif

#ifdef ANDROID
void MT3SystemInfo::sSendSystemInfo()
{
	DeviceInfo::sInitDeviceInfo();

	std::map<std::wstring, std::wstring> mapSystemInfo;
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"TelcoOper", SHARE_String2Wstring(DeviceInfo::s_strProvider)));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"ChId", gGetLoginManager()->GetChannelId()));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"PlatType", SHARE_String2Wstring("3")));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"DeviId", SHARE_String2Wstring(DeviceInfo::s_strMacAddress)));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"IMEI", SHARE_String2Wstring(DeviceInfo::s_strDeviceID)));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"NetEnvir", SHARE_String2Wstring(DeviceInfo::s_strNetEnvir)));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"OsVer", SHARE_String2Wstring(DeviceInfo::s_strOSVersion)));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"BrType", SHARE_String2Wstring(DeviceInfo::s_strProductModel)));

	fire::pb::CNotifyDeviceInfo nt;
	for (std::map<std::wstring, std::wstring>::iterator iter = mapSystemInfo.begin(); iter != mapSystemInfo.end(); iter++)
	{
		LOGD("CDeviceInfo::SendSystemInfo() key:%s value:%s", SHARE_Wstring2String(iter->first).c_str(), SHARE_Wstring2String(iter->second).c_str());
		nt.info.push_back(fire::pb::UserInfoUnit(iter->first, iter->second));
	}
	gGetNetConnection()->send(nt);
}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
void MT3SystemInfo::sSendSystemInfo()
{
}
#elif (defined WIN7_32)
void MT3SystemInfo::sSendSystemInfo()
{
	DeviceInfo::sInitDeviceInfo();

	std::map<std::wstring, std::wstring> mapSystemInfo;
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"TelcoOper", SHARE_String2Wstring(DeviceInfo::s_strProvider)));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"ChId", gGetLoginManager()->GetCurChannelId()));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"PlatType", SHARE_String2Wstring("6")));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"DeviId", SHARE_String2Wstring(DeviceInfo::s_strMacAddress)));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"IMEI", SHARE_String2Wstring(DeviceInfo::s_strDeviceID)));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"NetEnvir", SHARE_String2Wstring(DeviceInfo::s_strNetEnvir)));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"OsVer", SHARE_String2Wstring(DeviceInfo::s_strOSVersion)));
	mapSystemInfo.insert(std::map<std::wstring, std::wstring>::value_type(L"BrType", SHARE_String2Wstring(DeviceInfo::s_strProductModel)));

	fire::pb::CNotifyDeviceInfo nt;
	for (std::map<std::wstring, std::wstring>::iterator iter = mapSystemInfo.begin(); iter != mapSystemInfo.end(); iter++)
	{
		LOGD("CDeviceInfo::SendSystemInfo() key:%s value:%s", SHARE_Wstring2String(iter->first).c_str(), SHARE_Wstring2String(iter->second).c_str());
		nt.info.push_back(fire::pb::UserInfoUnit(iter->first, iter->second));
	}
	gGetNetConnection()->send(nt);
}
#endif



#ifdef ANDROID
	int DeviceInfo::sGetCpuFrequency()
	{
		DeviceInfo::s_nCpuFrequency = 800; //MHZ
		return DeviceInfo::s_nCpuFrequency;
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	int DeviceInfo::sGetCpuFrequency()
	{
		DeviceInfo::s_nCpuFrequency = 800; //MHZ
		return DeviceInfo::s_nCpuFrequency;
	}
#elif (defined WIN7_32)
	int DeviceInfo::sGetCpuFrequency()
	{
		DeviceInfo::s_nCpuFrequency = 800; //MHZ
		return DeviceInfo::s_nCpuFrequency;
	}
#endif

#ifdef ANDROID
	int DeviceInfo::sGetMaxCpuFreq()
	{
		// for android, MHZ
		//MT3::JniMethodInfo t;
		//if (MT3::JniHelper::getStaticMethodInfo(t,
		//                                         "com/locojoy/mini/mt3/AndroidSystemInfo",
		//                                         "getMaxCpuFreq",
		//                                         "()I"))
		//{
		//    DeviceInfo::s_nMaxCpuFreq = t.env->CallStaticObjectMethod(t.classID, t.methodID);
		//    t.env->DeleteLocalRef(t.classID);
		//}
		//else
		//{
		//    DeviceInfo::s_nMaxCpuFreq = 0;
		//}

		return DeviceInfo::s_nMaxCpuFreq;
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	int DeviceInfo::sGetMaxCpuFreq()
	{
		DeviceInfo::s_nMaxCpuFreq = 0;
		return DeviceInfo::s_nMaxCpuFreq;
	}
#elif (defined WIN7_32)
	int DeviceInfo::sGetMaxCpuFreq()
	{
		DeviceInfo::s_nMaxCpuFreq = 0;
		return DeviceInfo::s_nMaxCpuFreq;
	}
#endif

#ifdef ANDROID
	int DeviceInfo::sGetCpuCount()
	{
		// for android
		//MT3::JniMethodInfo t;
		//if (MT3::JniHelper::getStaticMethodInfo(t,
		//                                        "com/locojoy/mini/mt3/AndroidSystemInfo",
		//                                        "getCpuCount",
		//                                        "()I"))
		//{
		//    DeviceInfo::s_nCpuCount = t.env->CallStaticObjectMethod(t.classID, t.methodID);
		//    t.env->DeleteLocalRef(t.classID);
		//}
		//else
		//{
		//    DeviceInfo::s_nCpuCount = 0;
		//}

		return DeviceInfo::s_nCpuCount;
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	int DeviceInfo::sGetCpuCount()
	{
		DeviceInfo::s_nCpuCount = 0;
		return DeviceInfo::s_nCpuCount;
	}
#elif (defined WIN7_32)
	int DeviceInfo::sGetCpuCount()
	{
		DeviceInfo::s_nCpuCount = 0;
		return DeviceInfo::s_nCpuCount;
	}
#endif

#ifdef ANDROID
	const char* DeviceInfo::sGetCpuName()
	{
		// for android, MHZ
		//MT3::JniMethodInfo t;
		//if (MT3::JniHelper::getStaticMethodInfo(t,
		//                                         "com/locojoy/mini/mt3/AndroidSystemInfo",
		//                                         "getCpuName",
		//                                         "()Ljava/lang/String;"))
		//{
		//    jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
		//    t.env->DeleteLocalRef(t.classID);
		//    DeviceInfo::s_strCpuName = std::string(MT3::JniHelper::jstring2string(str).c_str());
		//    t.env->DeleteLocalRef(str);
		//}
		//else
		//{
		//    DeviceInfo::s_strCpuName = "";
		//}

		return DeviceInfo::s_strCpuName.c_str();
}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	const char* DeviceInfo::sGetCpuName()
	{
		DeviceInfo::s_strCpuName = "ARM";
		return DeviceInfo::s_strCpuName.c_str();
	}
#elif (defined WIN7_32)
	const char* DeviceInfo::sGetCpuName()
	{
		DeviceInfo::s_strCpuName = "WIN CPU";
		return DeviceInfo::s_strCpuName.c_str();
	}
#endif

#ifdef ANDROID
	int DeviceInfo::sGetTotalMemSize()
	{
		// for android, MBytes
		//MT3::JniMethodInfo t;
		//if (MT3::JniHelper::getStaticMethodInfo(t,
		//                                         "com/locojoy/mini/mt3/AndroidSystemInfo",
		//                                         "getTotalMemSize",
		//                                         "()I"))
		//{
		//    DeviceInfo::s_nTotalMemSize = t.env->CallStaticObjectMethod(t.classID, t.methodID);
		//    t.env->DeleteLocalRef(t.classID);
		//}
		//else
		//{
		//    DeviceInfo::s_nTotalMemSize = 0;
		//}

		return DeviceInfo::s_nTotalMemSize;
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	int DeviceInfo::sGetTotalMemSize()
	{
		DeviceInfo::s_nTotalMemSize = 0;
		return DeviceInfo::s_nTotalMemSize;
	}
#elif (defined WIN7_32)
	int DeviceInfo::sGetTotalMemSize()
	{
		DeviceInfo::s_nTotalMemSize = 0;
		return DeviceInfo::s_nTotalMemSize;
	}
#endif

#ifdef ANDROID
	int DeviceInfo::sGetFreeMemSize()
	{
		// for android, MBytes
		//MT3::JniMethodInfo t;
		//if (MT3::JniHelper::getStaticMethodInfo(t,
		//	"com/locojoy/mini/mt3/AndroidSystemInfo",
		//	"getFreeMemSize",
		//	"()I"))
		//{
		//	DeviceInfo::s_nFreeMemSize = t.env->CallStaticObjectMethod(t.classID, t.methodID);
		//	t.env->DeleteLocalRef(t.classID);
		//}
		//else
		//{
		//	DeviceInfo::s_nFreeMemSize = 0;
		//}

		return DeviceInfo::s_nFreeMemSize;
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	int DeviceInfo::sGetFreeMemSize()
	{
		DeviceInfo::s_nFreeMemSize = 0;
		return DeviceInfo::s_nFreeMemSize;
	}
#elif (defined WIN7_32)
	int DeviceInfo::sGetFreeMemSize()
	{
		DeviceInfo::s_nFreeMemSize = 0;
		return DeviceInfo::s_nFreeMemSize;
	}
#endif

#ifdef ANDROID
	float DeviceInfo::sGetAppCpuRate()
	{
		DeviceInfo::s_fAppCpuRate = 0.f;

		MT3::JniMethodInfo methodInfo;
		if (MT3::JniHelper::getStaticMethodInfo(methodInfo,
			"com/locojoy/mini/mt3/AndroidSystemInfo",
			"getProcessCpuRate",
			"()F"))
		{
			DeviceInfo::s_fAppCpuRate = methodInfo.env->CallStaticFloatMethod(methodInfo.classID, methodInfo.methodID);
			methodInfo.env->DeleteLocalRef(methodInfo.classID);
		}

		return DeviceInfo::s_fAppCpuRate;
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	float DeviceInfo::sGetAppCpuRate()
	{
		DeviceInfo::s_fAppCpuRate = 0.f;
		return DeviceInfo::s_fAppCpuRate;
	}
#elif (defined WIN7_32)
	float DeviceInfo::sGetAppCpuRate()
	{
		DeviceInfo::s_fAppCpuRate = 0.f;
		return DeviceInfo::s_fAppCpuRate;
	}
#endif

#ifdef ANDROID
	const char* DeviceInfo::sGetOSVersion()
	{
		DeviceInfo::s_strOSVersion = "";

		MT3::JniMethodInfo t;
		if (MT3::JniHelper::getStaticMethodInfo(t,
			"com/locojoy/mini/mt3/AndroidSystemInfo",
			"getOSVersion",
			"()Ljava/lang/String;"))
		{
			jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
			t.env->DeleteLocalRef(t.classID);
			DeviceInfo::s_strOSVersion = std::string(MT3::JniHelper::jstring2string(str).c_str());
			t.env->DeleteLocalRef(str);
		}

		return DeviceInfo::s_strOSVersion.c_str();
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	const char* DeviceInfo::sGetOSVersion()
	{
		DeviceInfo::s_strOSVersion = "";
		return DeviceInfo::s_strOSVersion.c_str();
	}
#elif (defined WIN7_32)
	const char* DeviceInfo::sGetOSVersion()
	{
		DeviceInfo::s_strOSVersion = "";
		return DeviceInfo::s_strOSVersion.c_str();
	}
#endif

#ifdef ANDROID
	int DeviceInfo::sGetOSVersionCode()
	{
		DeviceInfo::s_iOSVersionCode = 10;

		MT3::JniMethodInfo t;
		if (MT3::JniHelper::getStaticMethodInfo(t,
			"com/locojoy/mini/mt3/AndroidSystemInfo",
			"getOSVersionCode",
			"()I"))
		{
			DeviceInfo::s_iOSVersionCode = t.env->CallStaticIntMethod(t.classID, t.methodID);
			t.env->DeleteLocalRef(t.classID);
		}

		return DeviceInfo::s_iOSVersionCode;
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	int DeviceInfo::sGetOSVersionCode()
	{
		DeviceInfo::s_iOSVersionCode = 0;
		return DeviceInfo::s_iOSVersionCode;
	}
#elif (defined WIN7_32)
	int DeviceInfo::sGetOSVersionCode()
	{
		DeviceInfo::s_iOSVersionCode = 0;
		return DeviceInfo::s_iOSVersionCode;
	}
#endif

#ifdef ANDROID
	const char* DeviceInfo::sGetProductModel()
	{
		DeviceInfo::s_strProductModel = "";

		MT3::JniMethodInfo t;
		if (MT3::JniHelper::getStaticMethodInfo(t,
			"com/locojoy/mini/mt3/AndroidSystemInfo",
			"getProductModel",
			"()Ljava/lang/String;"))
		{
			jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
			t.env->DeleteLocalRef(t.classID);
			DeviceInfo::s_strProductModel = std::string(MT3::JniHelper::jstring2string(str).c_str());
			t.env->DeleteLocalRef(str);
		}

		return DeviceInfo::s_strProductModel.c_str();
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	const char* DeviceInfo::sGetProductModel()
	{
		DeviceInfo::s_strProductModel = "";
		return DeviceInfo::s_strProductModel.c_str();
	}
#elif (defined WIN7_32)
	const char* DeviceInfo::sGetProductModel()
	{
		DeviceInfo::s_strProductModel = "";
		return DeviceInfo::s_strProductModel.c_str();
	}
#endif

#ifdef ANDROID
	const char* DeviceInfo::sGetNetEnvir()
	{
		DeviceInfo::s_strNetEnvir = "";

		MT3::JniMethodInfo t;
		if (MT3::JniHelper::getStaticMethodInfo(t,
			"com/locojoy/mini/mt3/AndroidSystemInfo",
			"getNetEnvir",
			"()Ljava/lang/String;"))
		{
			jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
			t.env->DeleteLocalRef(t.classID);
			DeviceInfo::s_strNetEnvir = std::string(MT3::JniHelper::jstring2string(str).c_str());
			t.env->DeleteLocalRef(str);
		}

		return DeviceInfo::s_strNetEnvir.c_str();
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	const char* DeviceInfo::sGetNetEnvir()
	{
		DeviceInfo::s_strNetEnvir = "";
		return DeviceInfo::s_strNetEnvir.c_str();
	}
#elif (defined WIN7_32)
	const char* DeviceInfo::sGetNetEnvir()
	{
		DeviceInfo::s_strNetEnvir = "";
		return DeviceInfo::s_strNetEnvir.c_str();
	}
#endif

#ifdef ANDROID
	const char* DeviceInfo::sGetMacAddress()
	{
		DeviceInfo::s_strMacAddress = "";

		MT3::JniMethodInfo t;
		if (MT3::JniHelper::getStaticMethodInfo(t,
			"com/locojoy/mini/mt3/AndroidSystemInfo",
			"getMacAddress",
			"()Ljava/lang/String;"))
		{
			jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
			t.env->DeleteLocalRef(t.classID);

			std::string macstr = std::string(MT3::JniHelper::jstring2string(str).c_str());
			std::string::size_type pos = 0;
			while ((pos = macstr.find(":")) != std::string::npos) {
				macstr.replace(pos, 1, "");
			}
			DeviceInfo::s_strMacAddress = macstr;
			t.env->DeleteLocalRef(str);
		}

		return DeviceInfo::s_strMacAddress.c_str();
	}
	const char* DeviceInfo::sGetIPAddress()
	{
		DeviceInfo::s_strIPAddress = "";
		return DeviceInfo::s_strIPAddress.c_str();
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	const char* DeviceInfo::sGetMacAddress()
	{
		DeviceInfo::s_strMacAddress = "";
		return DeviceInfo::s_strMacAddress.c_str();
	}
	const char* DeviceInfo::sGetIPAddress()
	{
		DeviceInfo::s_strIPAddress = "";
		return DeviceInfo::s_strIPAddress.c_str();
	}
#elif (defined WIN7_32)

	void DeviceInfo::byte2Hex(unsigned char bData, unsigned char hex[])
	{
		int high = bData / 16, low = bData % 16;
		hex[0] = (high < 10) ? ('0' + high) : ('A' + high - 10);
		hex[1] = (low < 10) ? ('0' + low) : ('A' + low - 10);
	}

	const char* DeviceInfo::sGetMacAddress()
	{
		DeviceInfo::s_strMacAddress = "e3052346";
		ULONG ulSize = 0;
		unsigned char address[1024];
		PIP_ADAPTER_INFO pInfo = NULL;
		int temp = 0;
		temp = ::GetAdaptersInfo(pInfo, &ulSize);//第一处调用，获取缓冲区大小
		pInfo = (PIP_ADAPTER_INFO)malloc(ulSize);
		temp = ::GetAdaptersInfo(pInfo, &ulSize);

		int iCount = 0;
		//while (pInfo)//遍历每一张网卡
		{
			//  pInfo->Address MAC址
			for (int i = 0; i < (int)pInfo->AddressLength; i++)
			{
				byte2Hex(pInfo->Address[i], &address[iCount]);
				iCount += 2;
				if (i < (int)pInfo->AddressLength - 1)
				{
					address[iCount++] = ':';
				}
				else
				{
					address[iCount++] = '#';
				}
			}
			//pInfo = pInfo->Next;
		}

		if (iCount > 0)
		{
			address[--iCount] = '\0';
		}
		DeviceInfo::s_strMacAddress = (char *)address;
		return DeviceInfo::s_strMacAddress.c_str();
	}

	const char* DeviceInfo::sGetIPAddress()
	{
		WSADATA wsaData;
		char name[255];//定义用于存放获得的主机名的变量
		char *ip;//定义IP地址变量
		PHOSTENT hostinfo;
		//调用MAKEWORD（）获得Winsock版本的正确值，用于加载Winsock库
		if (WSAStartup(MAKEWORD(2,0), &wsaData) == 0) {
			//现在是加载Winsock库，如果WSAStartup（）函数返回值为0，说明加载成功，程序可以继续
			if (gethostname(name, sizeof(name)) == 0) {
				//如果成功地将本地主机名存放入由name参数指定的缓冲区中
				if ((hostinfo = gethostbyname(name)) != NULL) {
					//这是获取主机名，如果获得主机名成功的话，将返回一个指针，指向hostinfo，hostinfo
					//为PHOSTENT型的变量，下面即将用到这个结构体
					ip = inet_ntoa(*(struct in_addr *)*hostinfo->h_addr_list);
				}
			}
			WSACleanup(); //卸载Winsock库，并释放所有资源
		}
		DeviceInfo::s_strIPAddress = ip;
		return DeviceInfo::s_strIPAddress.c_str();
	}
#endif

#ifdef ANDROID
	const char* DeviceInfo::sGetScreenWidthInfo()
	{
		// for android
		//MT3::JniMethodInfo t;
		//if (MT3::JniHelper::getStaticMethodInfo(t,
		//                                         "com/locojoy/mini/mt3/AndroidSystemInfo",
		//                                         "getScreenWidthInfo",
		//                                         "()Ljava/lang/String;"))
		//{
		//    jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
		//    t.env->DeleteLocalRef(t.classID);
		//    DeviceInfo::s_strScreenWidthInfo = std::string(MT3::JniHelper::jstring2string(str).c_str());
		//    t.env->DeleteLocalRef(str);
		//}
		//else
		//{
		//    DeviceInfo::s_strScreenWidthInfo = "";
		//}

		return DeviceInfo::s_strScreenWidthInfo.c_str();
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	const char* DeviceInfo::sGetScreenWidthInfo()
	{
		DeviceInfo::s_strScreenWidthInfo = "";
		return DeviceInfo::s_strScreenWidthInfo.c_str();
	}
#elif (defined WIN7_32)
	const char* DeviceInfo::sGetScreenWidthInfo()
	{
		DeviceInfo::s_strScreenWidthInfo = "";
		return DeviceInfo::s_strScreenWidthInfo.c_str();
	}
#endif

#ifdef ANDROID
	const char* DeviceInfo::sGetScreenHeightInfo()
	{
		// for android
		//MT3::JniMethodInfo t;
		//if (MT3::JniHelper::getStaticMethodInfo(t,
		//                                         "com/locojoy/mini/mt3/AndroidSystemInfo",
		//                                         "getScreenHeightInfo",
		//                                         "()Ljava/lang/String;"))
		//{
		//    jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
		//    t.env->DeleteLocalRef(t.classID);
		//    DeviceInfo::s_strScreenHeightInfo = std::string(MT3::JniHelper::jstring2string(str).c_str());
		//    t.env->DeleteLocalRef(str);
		//}
		//else
		//{
		//    DeviceInfo::s_strScreenHeightInfo = "";
		//}

		return DeviceInfo::s_strScreenHeightInfo.c_str();
	}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	const char* DeviceInfo::sGetScreenHeightInfo()
	{
		DeviceInfo::s_strScreenHeightInfo = "";
		return DeviceInfo::s_strScreenHeightInfo.c_str();
	}
#elif (defined WIN7_32)
	const char* DeviceInfo::sGetScreenHeightInfo()
	{
		DeviceInfo::s_strScreenHeightInfo = "";
		return DeviceInfo::s_strScreenHeightInfo.c_str();
	}
#endif


const char* DeviceInfo::sGetVersionName()
{
#ifdef ANDROID
    // for android
    //MT3::JniMethodInfo t;
    //if (MT3::JniHelper::getStaticMethodInfo(t,
    //                                         "com/locojoy/mini/mt3/AndroidSystemInfo",
    //                                         "getVersionName",
    //                                         "()Ljava/lang/String;"))
    //{
    //    jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
    //    t.env->DeleteLocalRef(t.classID);
    //    DeviceInfo::s_strVersionName = std::string(MT3::JniHelper::jstring2string(str).c_str());
    //    t.env->DeleteLocalRef(str);
    //}
    //else
    //{
    //    DeviceInfo::s_strVersionName = "";
    //}
    
#endif
    return DeviceInfo::s_strVersionName.c_str();
}

const char* DeviceInfo::sGetPhoneNumber()
{
#ifdef ANDROID
    //std::wstring msg = GameTable::message::GetCStringResTableInstance().getRecorder(3013).msg;//switch for get phonenumber
    //if(msg == L"1")
    //{
    //    MT3::JniMethodInfo t;
    //    if (MT3::JniHelper::getStaticMethodInfo(t,
    //                                             "com/locojoy/mini/mt3/AndroidSystemInfo",
    //                                             "getPhoneNumber",
    //                                             "()Ljava/lang/String;"))
    //    {
    //        jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
    //        t.env->DeleteLocalRef(t.classID);
    //        DeviceInfo::s_strPhoneNumber = std::string(MT3::JniHelper::jstring2string(str).c_str());
    //        t.env->DeleteLocalRef(str);
    //    }
    //    else
    //    {
    //        DeviceInfo::s_strPhoneNumber = "";
    //    }
    //}
    //else
    //{
    //    DeviceInfo::s_strPhoneNumber = "";
    //}
    
#endif
    return DeviceInfo::s_strPhoneNumber.c_str();
}


#ifdef ANDROID
const char* DeviceInfo::sGetDeviceID()
{
	DeviceInfo::s_strDeviceID = "";

	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t,
		"com/locojoy/mini/mt3/AndroidSystemInfo",
		"getDeviceID",
		"()Ljava/lang/String;"))
	{
		jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);

		DeviceInfo::s_strDeviceID = std::string(MT3::JniHelper::jstring2string(str).c_str());
		t.env->DeleteLocalRef(str);
	}

	return DeviceInfo::s_strDeviceID.c_str();
}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
const char* DeviceInfo::sGetDeviceID()
{
	return DeviceInfo::s_strDeviceID.c_str();
}
#elif (defined WIN7_32)
const char* DeviceInfo::sGetDeviceID()
{
	return DeviceInfo::s_strDeviceID.c_str();
}
#endif

#ifdef ANDROID
const char* DeviceInfo::sGetProvider()
{
	DeviceInfo::s_strProvider = "";

	MT3::JniMethodInfo t;
	if (MT3::JniHelper::getStaticMethodInfo(t,
		"com/locojoy/mini/mt3/AndroidSystemInfo",
		"getProvider",
		"()Ljava/lang/String;"))
	{
		jstring str = (jstring)t.env->CallStaticObjectMethod(t.classID, t.methodID);
		t.env->DeleteLocalRef(t.classID);

		DeviceInfo::s_strProvider = std::string(MT3::JniHelper::jstring2string(str).c_str());
		t.env->DeleteLocalRef(str);
	}

	return DeviceInfo::s_strProvider.c_str();
}
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
const char* DeviceInfo::sGetProvider()
{
	return DeviceInfo::s_strProvider.c_str();
}
#elif (defined WIN7_32)
const char* DeviceInfo::sGetProvider()
{
	return DeviceInfo::s_strProvider.c_str();
}
#endif


const char* DeviceInfo::sGetNoColonMacAddress()
{
	DeviceInfo::sGetMacAddress();
#ifdef WIN7_32
	s_strNoColonMacAddress = DeviceInfo::s_strMacAddress;
	std::string::size_type pos = 0;
	while ((pos = s_strNoColonMacAddress.find(":")) != std::string::npos) {
		s_strNoColonMacAddress.replace(pos, 1, "");
	}
	return s_strNoColonMacAddress.c_str();
#else
	return DeviceInfo::s_strMacAddress.c_str();
#endif
}
