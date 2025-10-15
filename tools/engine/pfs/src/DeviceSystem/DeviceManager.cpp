// 
// 2010-11-15
//

#include "..\common.h"
#include "DeviceManager.h"
#include "DeviceFactory.h"
#include "AbstractDevice.h"

namespace PFS
{
	CDeviceManager::CDeviceManager(void)
	{
	}

	CDeviceManager::~CDeviceManager(void)
	{
	}

	IAbstractDevice* CDeviceManager::CreateDevice(const std::wstring& device, MOUNT_TYPE mt)
	{
		std::wstring strNativeDeviceName = device;
		IAbstractDevice* pRealDevice = CDeviceFactory::CreateDeviceObject( strNativeDeviceName, mt );
		if( pRealDevice == NULL )
		{
			return NULL;
		}

		if( pRealDevice->Initialize( strNativeDeviceName, mt ) )
		{
			return pRealDevice;
		}
		else
		{
			delete pRealDevice;
			return NULL;
		}
	}
}