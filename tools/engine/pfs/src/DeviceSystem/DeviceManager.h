//
// DeviceSystem - CDeviceManager 设备管理器
// 2010-11-15
//
#pragma once

namespace PFS
{
	//
	// DeviceManager可能需要缓存相同类型的相同deviceName的device对象。
	// IAbstractDevice 可能需要使用引用计数机制管理生存期。
	//
	class IAbstractDevice;

	class CDeviceManager
	{
	public:
		CDeviceManager(void);
		~CDeviceManager(void);

		IAbstractDevice* CreateDevice(const std::wstring& deviceName, MOUNT_TYPE mt);
	};

}