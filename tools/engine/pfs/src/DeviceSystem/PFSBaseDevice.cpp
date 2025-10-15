#include "..\common.h"
#include "../base/PFSBase.h"
#include "PFSBaseDevice.h"

namespace PFS
{
	CPFSBaseDevice::CPFSBaseDevice(void)
		: m_fst(FST_NATIVE)
		, m_pPfsBase(NULL)
	{
	}

	CPFSBaseDevice::CPFSBaseDevice(FS_TYPE fst)
		: m_fst( fst )
		, m_pPfsBase(NULL)
	{
	}

	CPFSBaseDevice::~CPFSBaseDevice(void)
	{
	}

	bool CPFSBaseDevice::_InnerInitialize( CPFSBase* pPfsBase, const std::wstring& device, MOUNT_TYPE mt)
	{
		// BUGCHECKING: duplicated init!
		assert( m_pPfsBase == NULL );
		assert( pPfsBase != NULL ); 

		int ErrorCode = pPfsBase->Initialize( device, mt & ~MT_OPTIONAL );
		CEnv::SetLastError(ErrorCode);
		if( ErrorCode == EC_SUCCESS)
		{
			pPfsBase->SetMountPoint( device );
			m_pPfsBase = pPfsBase;
			return true;
		}
		else
		{
			delete pPfsBase;
			return false;
		}
	}

	bool CPFSBaseDevice::Initialize(const std::wstring& device, MOUNT_TYPE mt)
	{
		CPFSBase* pBase = CPFSBase::Create( m_fst );
		return CPFSBaseDevice::_InnerInitialize( pBase, device, mt );
	}

	CBaseFile* CPFSBaseDevice::OpenFile( const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa)
	{
		// BUGCHECKING: You should init the device firstly.
		assert( m_pPfsBase != NULL );
	
		CBaseFile* pFile = NULL;
		int ErrorCode = m_pPfsBase->OpenFile( filename, fm, fa, pFile );
		CEnv::SetLastError( ErrorCode );
		if( ErrorCode != EC_SUCCESS )
			return NULL;
		return pFile;
	}

	void CPFSBaseDevice::CloseFile(CBaseFile* pFile)
	{
		// BUGCHECKING: You should init the device firstly.
		assert( m_pPfsBase != NULL );
		CEnv::CloseFile( pFile );
	}
	
	//
	// Is the file existing in the storage device?
	//
	bool CPFSBaseDevice::IsFileExisting( const std::wstring& filename ) const
	{
		assert( m_pPfsBase != NULL );
		return m_pPfsBase->IsFileExisting(filename);
	}

	bool CPFSBaseDevice::IsDirectoryExisting( const std::wstring& dirname ) const
	{
		assert( m_pPfsBase != NULL );
		return m_pPfsBase->IsDirectoryExisting(dirname);
	}

	//
	// Deletes a file in the storage device.
	//
	bool CPFSBaseDevice::RemoveFile( const std::wstring& filename )
	{
		assert( m_pPfsBase != NULL );
		int ErrorCode = m_pPfsBase->RemoveFile(filename);
		CEnv::SetLastError( ErrorCode );
		return ( ErrorCode == EC_SUCCESS );
	}
	
	void CPFSBaseDevice::Release()
	{
		if( m_pPfsBase != NULL )
		{
			m_pPfsBase->Destroy(true);
			delete m_pPfsBase;
			m_pPfsBase = NULL;
		}

		delete this;
	}

	CWindowsDirectoryDevice::CWindowsDirectoryDevice(void)
		: CPFSBaseDevice( FST_NATIVE )
	{
	}

	CPackageFileDevice::CPackageFileDevice(void)
		: CPFSBaseDevice( FST_ZIP )
	{
	}

	CRuntimePackageFileDevice::CRuntimePackageFileDevice(void)
		: CPFSBaseDevice( FST_RUNZIP )
	{
	}

	CBriefDirectoryDevice::CBriefDirectoryDevice(void)
		: CPFSBaseDevice( FST_BRIEF )
	{
	}

	CInetHttpDirectoryDevice::CInetHttpDirectoryDevice(void)
		: CPFSBaseDevice( FST_INETHTTP )
	{
	}

	CMountDirectoryDevice::CMountDirectoryDevice(void)
		: CPFSBaseDevice( FST_NATIVE )
	{
	}

	bool CMountDirectoryDevice::Initialize(const std::wstring& device, MOUNT_TYPE mt)
	{
		// 说明：
		// 对于递归Mount类型的Device，对于混合类型的文件系统，这里的处理是有必要的。
		// 比如： D:\test--> /test, /test/pak/ --> /pak,  device open ( /pak/a.pfs )
		// 如果 D:\test\pak是目录，而 a.pfs 是该目录下的pfs包， 我们以可写方式open这个device的时候，
		// 需要根据它是文件类型来自动切换到zip类型。否则，将会导致device.open()失败。
		// yanghaibo 2011-3-4
		CPFSBase* pBase = NULL;

		if( CFile::IsFileExisting(device) )
		{
			if ( PFS::IsMountReadOnly(mt) )
			{
				m_fst = FST_RUNZIP;
				pBase = CPFSBase::Create( m_fst );
			}
			else
			{
				m_fst = FST_ZIP;
				pBase = CPFSBase::Create( m_fst );
			}
		}
		else	
		{
			m_fst = FST_NATIVE;
			pBase = CPFSBase::Create( m_fst );
		}

		return CPFSBaseDevice::_InnerInitialize( pBase, device, mt );
	}
}