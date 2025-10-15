// 
// 2010-11-15
//
#include "..\common.h"
#include "..\..\Device.h"
#include "AbstractDevice.h"
#include "DeviceManager.h"

namespace PFS
{
	CDevice::CDevice(void)
	{
		m_pAbstractDevice = NULL;
	}

	CDevice::~CDevice(void)
	{
		// Auto unmount when the device object destroies.
		Unmount();
	}

	bool CDevice::Mount( const std::wstring& device, MOUNT_TYPE mt)
	{
		if( m_pAbstractDevice != NULL )
		{
			// Duplicate mount! Please Unmount first.
			CEnv::SetLastError( EC_DEVICE_ALREADY_MOUNT );
			return false;
		}

		{
			static CDeviceManager deviceManager;
			IAbstractDevice* pDevice = deviceManager.CreateDevice( device, mt );
			if( pDevice == NULL )
			{
				// Last error code has been set.
				return false;
			}

			m_pAbstractDevice = pDevice;
		}

		return true;
	}

	void CDevice::Unmount()
	{
		if( m_pAbstractDevice != NULL )
		{
			m_pAbstractDevice->Release();
			m_pAbstractDevice = NULL;
		}
	}

	//
	// Is the file existing in the storage device?
	//
	bool CDevice::IsFileExisting( const std::wstring& filename ) const
	{
		if( m_pAbstractDevice == NULL )
		{
			// Please Mount first.			
			CEnv::SetLastError( EC_DEVICE_NOT_INIT );
			return false;
		}

		return m_pAbstractDevice->IsFileExisting( filename );
	}

	//
	// Is the directory existing in the storage device?
	//
	bool CDevice::IsDirectoryExisting( const std::wstring& dirname ) const
	{
		if( m_pAbstractDevice == NULL )
		{
			// Please Mount first.
			CEnv::SetLastError( EC_DEVICE_NOT_INIT );
			return false;
		}
		return m_pAbstractDevice->IsDirectoryExisting(dirname);
	}

	//
	// Deletes a file in the storage device.
	//
	bool CDevice::RemoveFile( const std::wstring& filename )
	{
		if( m_pAbstractDevice == NULL )
		{
			// Please Mount first.
			CEnv::SetLastError( EC_DEVICE_NOT_INIT );
			return false;
		}
		
		return m_pAbstractDevice->RemoveFile( filename );
	}

	CBaseFile* CDevice::OpenFile( const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa )
	{
		if( m_pAbstractDevice == NULL )
		{
			// Please Mount first.
			CEnv::SetLastError( EC_DEVICE_NOT_INIT );
			return NULL;
		}

		return m_pAbstractDevice->OpenFile( filename, fm, fa );
	}
	
	void CDevice::CloseFile(CBaseFile* pFile)
	{
		if( pFile != NULL )
		{
			if( m_pAbstractDevice == NULL )
			{
				// Please Mount first.
				CEnv::SetLastError( EC_DEVICE_NOT_INIT );
				return ;
			}

			m_pAbstractDevice->CloseFile( pFile );
		}
	}

	CDeviceFile::CDeviceFile()
	{
		m_pDevice = NULL;
	}

	CDeviceFile::CDeviceFile( CDevice* pDevice )
		: m_pDevice ( pDevice )
	{
	}

	CDeviceFile::~CDeviceFile()
	{
		Close();
	}

	void CDeviceFile::Close()
	{
		if( m_pDevice != NULL )
		{
			CBaseFile* pFile = CFile::DetackBaseFile();
			if( pFile != NULL )
			{
				m_pDevice->CloseFile( pFile );
				pFile = NULL;
			}

			m_pDevice = NULL;
		}
	}
	
	bool CDeviceFile::Open( CDevice* pDevice, const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa )
	{
		if( pDevice == NULL )
		{
			CEnv::SetLastError( EC_DEVICE_FILE_INVALID_PARAMETER );
			return false;
		}

		CFile::Close();
		m_pDevice = pDevice;
		CBaseFile* pFile = m_pDevice->OpenFile( filename, fm, fa );
		if( pFile != NULL )
		{
			AttackBaseFile( pFile );
			return true;
		}

		// Last error code has been set.
		return false;
	}

	bool CDeviceFile::Open( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa )
	{
		if( m_pDevice == NULL )
		{
			return CFile::Open( filename, fm, fa );
		}

		return Open( m_pDevice, filename, fm, fa );
	}

}