#ifndef FireClient_DeviceData_h
#define FireClient_DeviceData_h

enum eNetworkType
{
	eNetworkOther,
	eNetworkWifi
};

class DeviceData
{
public:
	static const char* sGetTimeStr();
	static float sGetBatteryRatio();
	static bool isBatteryCharging();  // 电池是否正在充电
	static int sGetNetworkType(); //0.not wifi 1.wifi
    static bool sIsNetworkConnected();

private:
	static char _buff[8];
};

#endif // FireClient_DeviceData_h
