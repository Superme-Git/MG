// 
// DeviceSystem
//		CPFSBaseDevice - PFS系统设备实现类
//
// 2010-11-15
//
#pragma once
#include "AbstractDevice.h"

namespace PFS
{
	class CPFSBase;
	class CPFSBaseDevice : public IAbstractDevice
	{
	protected:
		friend class CDeviceFactory;
		CPFSBaseDevice(void);
		CPFSBaseDevice(FS_TYPE fst);
		virtual ~CPFSBaseDevice(void);
		virtual bool _InnerInitialize(CPFSBase* pPfsBase, const std::wstring& device, MOUNT_TYPE mt);

	public:
		virtual bool Initialize(const std::wstring& device, MOUNT_TYPE mt);
		virtual CBaseFile* OpenFile(const std::wstring& filename,PFS::FILE_MODE fm, PFS::FILE_ACCESS fa);
		virtual void CloseFile(CBaseFile* pFile);
		virtual bool IsFileExisting( const std::wstring& filename ) const;
		virtual bool IsDirectoryExisting( const std::wstring& dirname ) const;
		virtual bool RemoveFile( const std::wstring& filename );
		virtual void Release();

	protected:
		CPFSBase* m_pPfsBase;
		FS_TYPE m_fst;
	};

	class CWindowsDirectoryDevice : public CPFSBaseDevice
	{
	protected:
		friend class CDeviceFactory;
		CWindowsDirectoryDevice(void);
	};

	class CPackageFileDevice : public CPFSBaseDevice
	{
	protected:
		friend class CDeviceFactory;
		CPackageFileDevice(void);
	};

	class CRuntimePackageFileDevice : public CPFSBaseDevice
	{
	protected:
		friend class CDeviceFactory;
		CRuntimePackageFileDevice(void);
	};

	class CBriefDirectoryDevice : public CPFSBaseDevice
	{
	protected:
		friend class CDeviceFactory;
		CBriefDirectoryDevice(void);
	};

	class CInetHttpDirectoryDevice : public CPFSBaseDevice
	{
	protected:
		friend class CDeviceFactory;
		CInetHttpDirectoryDevice(void);
	};

	class CMountDirectoryDevice : public CPFSBaseDevice
	{
	protected:
		friend class CDeviceFactory;
		CMountDirectoryDevice(void);
	public:
		virtual bool Initialize(const std::wstring& device, MOUNT_TYPE mt);
	};
}