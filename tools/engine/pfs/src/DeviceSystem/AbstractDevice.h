// 
// DeviceSystem - IAbstractDevice 抽象设备接口
// 2010-11-15
//
#pragma once

namespace PFS
{
	class IAbstractDevice
	{
	public:
		//
		// Initializes the real storage device object - attach itself to the 'device'.
		//
		virtual bool Initialize( const std::wstring& device, MOUNT_TYPE mt ) = 0;

		//
		// Opens a file from the storage device.
		//
		virtual CBaseFile* OpenFile( const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa ) = 0;
		
		//
		// Closes the file opened by OpenFile() and deletes the file object.
		// After closed, the file object pointed by pFile should not be used any more.
		//
		virtual void CloseFile( CBaseFile* pFile )  = 0;

		//
		// Is the file existing in the storage device?
		//
		virtual bool IsFileExisting( const std::wstring& filename ) const = 0;

		//
		// Is the directory existing in the storage device?
		//
		virtual bool IsDirectoryExisting( const std::wstring& dirname ) const = 0;

		//
		// Deletes a file in the storage device.
		//
		virtual bool RemoveFile( const std::wstring& filename ) = 0;

		//
		// Destorys the device object itself.
		//
		virtual void Release() = 0;
	};
}