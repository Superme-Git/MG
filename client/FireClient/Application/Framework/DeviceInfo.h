#ifndef FireClient_DeviceInfo_h
#define FireClient_DeviceInfo_h

#include "utils/StringUtil.h"
#include "utils/StringCover.h"
#include "GameApplication.h"

class DeviceInfo
{
public:
    
    static int sGetDeviceType(); // 0:DEBUG, 1:ios, 2:android, 3:wp8
	static const char* sGetCurDeviceName();

	static void sInitDeviceInfo();

	static int sGetCpuFrequency();
	static int sGetMaxCpuFreq();
	static int sGetCpuCount();
	static const char* sGetCpuName();

    static int sGetTotalMemSize();
    static int sGetFreeMemSize();
    
	static float sGetAppCpuRate();				// ANDROID游戏CPU占用率
    static const char* sGetOSVersion();			// ANDROID手机系统版本号
	static int		   sGetOSVersionCode();		// ANDROID手机系统版本号(整型类型)
	static const char* sGetProductModel();		// ANDROID手机型号
	static const char* sGetNetEnvir();			// ANDROID手机网络类型
	static const char* sGetMacAddress();		// ANDROID手机MAC地址
	static const char* sGetNoColonMacAddress();	// 去除冒号的MAC地址
	static const char* sGetIPAddress();			// PC端IP地址
	static const char* sGetScreenWidthInfo();	// ANDROID屏幕宽度（像素）
    static const char* sGetScreenHeightInfo();	// ANDROID屏幕高度（像素）
    static const char* sGetVersionName();		// ANDROID游戏版本号
    static const char* sGetPhoneNumber();		// ANDROID用户手机号
	static const char* sGetDeviceID();			// ANDROID设备唯一标识：IMEI
	static const char* sGetProvider();			// ANDROID用户网络运营商

private:
	static void byte2Hex(unsigned char bData, unsigned char hex[]);

public:
	static std::string s_strDevDesc;

    static int s_nCpuFrequency;
	static int s_nMaxCpuFreq;
	static int s_nCpuCount;
	static std::string s_strCpuName;

    static int s_nTotalMemSize; //unit: MByte
    static int s_nFreeMemSize; //unit: MByte

	static float s_fAppCpuRate;
    static std::string s_strAppSrc;
    static std::string s_strOSVersion;
	static int		   s_iOSVersionCode;
	static std::string s_strProductModel;
	static std::string s_strNetEnvir;
    static std::string s_strMacAddress;
	static std::string s_strNoColonMacAddress;
	static std::string s_strIPAddress;
    static std::string s_strScreenWidthInfo;
    static std::string s_strScreenHeightInfo;
    static std::string s_strVersionName;
    static std::string s_strPhoneNumber;
	static std::string s_strDeviceID;
	static std::string s_strProvider;
};

#endif // FireClient_DeviceInfo_h
