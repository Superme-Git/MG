#ifndef __FireClient__SystemInfo__
#define __FireClient__SystemInfo__

#include <map>
#include <vector>

class MT3SystemInfo
{
public:
	static float sGetIOSVersionFloatValue();
	static std::string sGetMacAddr();
	static void sSendSystemInfo();

public:
#if (defined ANDROID) || (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
    #include "utils/StringCover.h"
	static std::wstring sIDFAValue(){ return StringCover::to_wstring(""); }
	static std::wstring sOpenUDIDValue(){ return StringCover::to_wstring(""); }
	static std::wstring sMacValue(){ return StringCover::to_wstring(""); }
#else
	static std::wstring sIDFAValue();
	static std::wstring sOpenUDIDValue();
	static std::wstring sMacValue();
#endif
private:
	static void sGetOSVersion();		// IOS手机系统版本号
    static void sGetResolution();		// IOS手机屏幕的宽和高
	static void sGetDeviceID();		// IOS手机MAC地址（取不到则取IDFA）
   
	static void sGetPhoneNum();		// IOS用户手机号
    
    static void sGetUDID();			// IOS设备UDID
    static void sGetIOSDeviceType();	// IOS手机型号：iPad、iPhone 5等
	static void sGetProvider();		// IOS用户网络运营商
	static void sGetNetEnvir();		// IOS手机网络类型
	static void sGetIMEI();			// IOS手机IMEI

private:
    static std::map<std::wstring, std::wstring> m_MapSystemInfo;
};

#endif
