// 
// 2010-11-17
//
#include "..\common.h"
#include "DeviceFactory.h"
//#include "PackageDirectoryDevice.h"
//#include "WindowsDirectoryDevice.h"
//#include "PackageFileDevice.h"
#include "PFSBaseDevice.h"

namespace PFS
{
	IAbstractDevice* CDeviceFactory::CreateDeviceObject(std::wstring& device, MOUNT_TYPE mt)
	{
		// TODO : supper device?
		if( device.empty() )
		{
			CEnv::SetLastError(EC_DEVICE_INVALID_DEVNAME);
			return NULL;
		}

		bool IsPFSPath = (device[0] == L'/');
		if( IsPFSPath ) 
		{
			// 对于需要写操作的Device，即使能够映射为NativeFileName，我们也不必这么做，
			// 因为我们的PFS不支持共享写相同的PFS包文件。
			if( IsMountReadOnly( mt ) ) 
			{
				// 对于只读系统，对PFSPath进行必要的转换，以简化PFS系统的关联性。
				IsPFSPath = !CEnv::PFSFileNameToNativeFileName( std::wstring(device), device );
			}
		}

		if( IsMountSetFSType(mt) )
		{
			// 在判断FSType之前，PFSPath已经进行了必要的转换，这种转换，对于指定类型的FSSystem也是有意义的。

			FS_TYPE fst = FSTypeFromMountType( mt ); // 注意，0 - FST_NATIVE，是有效值。
			return new CPFSBaseDevice(fst);
		}

		if( IsPFSPath )
		{
			// device 不能简单的转换成Windows路径，
			// 此时，device所代表的设备可能是一个混合Device，只能使用MountDirectoryDevice。
			return new CMountDirectoryDevice();
		}

		// device 已经从PFS路径转换成了OS路径，
		// 这样，用该路径创建的Device将与PFS无关，可以简化对象关联性。

		// TODO: 实现一个HttpInetFS系统，待支持
		// if ( 0 == _wcsicmp(L"http://", device.substr( 0, wcslen(L"http://")-1 ) )
		// {
		//	 return new CInetDirectoryDevice();
		// }

		if( PFS::CFile::IsFileExisting( device ) )
		{
			return new CPackageFileDevice();
		}

		if( PFS::CFileIO::IsDirectoryExisting( device ) )
		{
			return new CWindowsDirectoryDevice();
		}

		if( PFS::IsMountReadOnly(mt) )
		{
			CEnv::SetLastError(EC_DEVICE_MISSING_STORAGE);
			return NULL;
		}

		// 是否优先创建目录系统，如果不是，方考虑根据文件名是否带有后缀选择创建目录还是创建包。
		if( !IsMountPreferDir( mt ) )
		{
			// 如果文件名带有后缀，那么选择创建包
			std::wstring::size_type dotPos = device.find_last_of(L'.');
			if( dotPos != std::wstring::npos )
			{
				std::wstring::size_type slashPos = device.find_last_of(L'/');
				std::wstring::size_type backslashPos = device.find_last_of(L'\\');
				std::wstring::size_type sepPos = max( (int)slashPos, (int)backslashPos );
				if( sepPos == std::wstring::npos || dotPos > sepPos )
				{
					// like a/b/c.d or a\b\c.d
					return new CPackageFileDevice();
				}
			}
		}

		// TODO:
		return new CWindowsDirectoryDevice();
	}

}