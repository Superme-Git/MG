// 
// DeviceSystem - CDeviceFactory - 设备工厂
// 2010-11-17
//
#pragma once

namespace PFS
{
	class IAbstractDevice;

	// CDeviceFactory - 
	// It is used to create the real Device Object by device name and mount type.
	// 
	class CDeviceFactory
	{
	public:
		//
		// Create the real Device Object by device name and mount type.
		// Returns NULL if it's a invalid device name or invalid mount type.
		//
		static IAbstractDevice* CreateDeviceObject(std::wstring& device, MOUNT_TYPE mt);
	};

}