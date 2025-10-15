//
// DeviceSystem - CDeviceManager �豸������
// 2010-11-15
//
#pragma once

namespace PFS
{
	//
	// DeviceManager������Ҫ������ͬ���͵���ͬdeviceName��device����
	// IAbstractDevice ������Ҫʹ�����ü������ƹ��������ڡ�
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