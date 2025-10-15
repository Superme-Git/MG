///=============================================================================
/// Product   : MHZX
/// Project   : pfslib
/// Module    : zip.cpp
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : wangzao, yanghaibo
///=============================================================================
/// Description :
/// 	Zip File System
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-25 Created.
///=============================================================================
#include "../common.h"
#include "../base/packetfilesystem.h"
#include "zip.h"
#include "arrangezip.h"

namespace PFS
{
	class CZipBaseFile : public CBaseFile
	{
		CBaseFile*						m_basefile;

		FILE_MODE						m_fm;
		FILE_ACCESS						m_fa;
		CZipFS*							m_zfs;
		const ZIPFILE::CFileCentral*	m_central;

	public:
		CZipBaseFile( const std::wstring& name, CZipFS* zfs, FILE_MODE fm, FILE_ACCESS fa, const ZIPFILE::CFileCentral* fc) 
			: m_zfs( zfs), m_fm( fm), m_fa( fa), m_central( fc), m_basefile(NULL)
		{
			InnerCreateFile( name);
		}

		virtual ~CZipBaseFile() 
		{
			OnFileClosed(); 
		}

		FILE_ACCESS GetAccess() { return m_fa; }

	private:
		void OnFileClosed()
		{ 
			assert( m_zfs);

			CEnv::CloseFile( m_basefile);
			m_central = NULL;

			m_zfs->OnFileClose( this);
		}

		void InnerCreateFile( const std::wstring& name)
		{
			if( m_central)
			{
				if( m_central->IsFileDeleted() || (FM_TRUNC & m_fm) == FM_TRUNC)
				{
					m_central->SetFileModified();
					m_basefile = m_zfs->m_tempfilemanager.CreateTempFile( name, NULL);
				}	
				else
				{
					m_basefile = m_zfs->m_tempfilemanager.GetTempFile( name);
					if( m_basefile)
					{
						// 文件可写才需要重新从 m_tempfilemanager 创建
						if( IsFileWritable( m_fa))
						{
							// 说明以前打开过这个文件
							ZIPFILE::CFileCentralRead reader( m_zfs->m_zipfile, *m_central);
							m_basefile = m_zfs->m_tempfilemanager.CreateTempFile( name, &reader);
						}
						else
						{
							// Share reading
							// Reset the file pointer
							((CZipTempFile*)m_basefile)->ReOpen();
						}
					}
					else
					{
						if( IsFileWritable(m_fa) && (FM_CREAT & m_fm ) == FM_CREAT )
						{
							// 如果是以创建方式且可写方式打开，直接创建临时文件。
							ZIPFILE::CFileCentralRead reader( m_zfs->m_zipfile, *m_central);
							m_basefile = m_zfs->m_tempfilemanager.CreateTempFile( name, &reader);
						}
						else
						{
							m_basefile = new ZIPFILE::CZipSystemReadFile( m_zfs->m_zipfile, 
								*m_central, m_zfs->m_ImageHandle);
						}
					}
				}
			}
			else
			{
				m_central = m_zfs->m_zipfile.AddFile( name);
				m_basefile = m_zfs->m_tempfilemanager.CreateTempFile( name, NULL);
			}
		}

		void CheckIsTempFile()
		{
			try
			{
				ZIPFILE::CZipSystemReadFile* readfile = dynamic_cast<ZIPFILE::CZipSystemReadFile*>( m_basefile);
				if( readfile)
				{
					fsize_t pos = readfile->GetPos();
					m_basefile = NULL;
					m_basefile = m_zfs->m_tempfilemanager.CreateTempFile( m_central->GetFileName(),
						&readfile->GetFileCentralRead());

					delete readfile;
					if( m_basefile)
					{
						m_basefile->Seek(pos, FSM_SET);
					}
				}
			}
			catch( ZIPFILE::CZipFileBadFormat&)
			{
				throw CFileIOException( EC_FILE_OPEN_MODE);
			}
		}

	public:
		static int CreateFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, 
			CZipFS* zfs, CBaseFile*& file)
		{
			assert( zfs);
			try
			{
				std::wstring newname( filename);
				newname.erase( 0, newname.find_first_not_of( L'/'));
				const ZIPFILE::CFileCentral* pcentral = zfs->m_zipfile.GetFileCentral( newname);
				if( !pcentral || (pcentral && pcentral->IsFileDeleted()))
				{
					if( (FM_CREAT & fm) != FM_CREAT)
						return EC_FILE_NOT_FOUND;
				}
				file = new CZipBaseFile( newname, zfs, fm, fa, pcentral);
			}
			catch( CFileIOException& fioe)
			{
				return fioe.GetLastError();
			}
			return EC_SUCCESS;
		}

	public:
		virtual size_t Read( void* data, size_t size)
		{
			try
			{
				if( m_basefile)
					return m_basefile->Read( data, size);
			}
			catch( CFileIOException& fioe)
			{
				CEnv::SetLastError( fioe.GetLastError());
			}
			catch( ZIPFILE::CZipFileBadFormat&)
			{
			}
			return FILE_SIZE_ERROR;
		}
		virtual size_t Write( const void* data, size_t size) 
		{
			try
			{
				if( PFS::IsFileReadOnly(m_fa) )
				{
					CEnv::SetLastError( EC_FILE_READONLY);
					return FILE_SIZE_ERROR;
				}
				if( NULL == m_basefile)
				{
					return FILE_SIZE_ERROR;
				}

				size_t res = 0;
				try
				{
					res = m_basefile->Write( data, size);
				}
				catch( ZIPFILE::CZipReadError&)
				{
					CheckIsTempFile();
					if( m_basefile)
						res = m_basefile->Write( data, size);
				}
				if( res > 0 && res != FILE_SIZE_ERROR )
					m_central->SetFileModified();
				return res;
			}
			catch( CFileIOException& fioe)
			{
				CEnv::SetLastError( fioe.GetLastError());
			}
			return FILE_SIZE_ERROR;
		}

		virtual fsize_t Seek( foff_t pos, FILE_SEEK_METHOD fsm) 
		{
			try
			{
				try
				{
					if( m_basefile)
						return m_basefile->Seek(pos, fsm);
				}
				catch( ZIPFILE::CZipReadError&)
				{
					CheckIsTempFile();
					if( m_basefile)
						return m_basefile->Seek(pos, fsm);
				}
			}
			catch( CFileIOException& fioe)  
			{
				CEnv::SetLastError( fioe.GetLastError());
			}
			return FILE_SIZE_ERROR;
		}
		virtual fsize_t GetSize()
		{
			try
			{
				if( m_basefile)
					return m_basefile->GetSize();
			}
			catch( CFileIOException& fioe)  
			{
				CEnv::SetLastError( fioe.GetLastError());
			}
			return FILE_SIZE_ERROR;
		}
		virtual fsize_t GetPos() 
		{ 
			if( m_basefile)
				return m_basefile->GetPos(); 
			return FILE_SIZE_ERROR;
		}
		virtual bool SetEnd() 
		{
			try
			{
				if( m_basefile)
					return m_basefile->SetEnd(); 
			}
			catch (ZIPFILE::CZipReadError&)
			{
				PFS::CEnv::SetLastError( PFS::EC_FILE_BAD_ZIPDATA );
			}
			return false;
		}
		virtual CImageData GetImage() 
		{ 
			if( m_basefile)
				return m_basefile->GetImage(); 
			else
			{
				PFS::CEnv::SetLastError( PFS::EC_FILE_NOTOPEN );
				return CImageData();
			}
		}

		virtual bool ReadAll(void* data, fsize_t size )
		{
			if( m_basefile)
				return m_basefile->ReadAll(data, size); 
			else
			{
				PFS::CEnv::SetLastError( PFS::EC_FILE_NOTOPEN );
				return false;
			}
		}
	};

	CZipFS::~CZipFS() 
	{ 
		CloseFile();
		if( m_BasePFS)
			m_BasePFS->RemoveRefMountFSDevive( m_strBasePath);
	}

	void CZipFS::CloseFile()
	{
		if( m_basefile)
		{
			CEnv::CloseFile( m_basefile);
			m_basefile = NULL;
		}
		m_tempfilemanager.Destroy();
	}
	
	//////////////////////////////////////////////////////////////////////////
	//
	// private functions:
	//
	void CZipFS::MakeZipMetaInfo( CMetaInfoMap& mim, bool bMakeUnModify)
	{
		ZIPFILE::CFileReader::CFileCentralMap& centralmap = m_zipfile.GetFileCentralMap();
		for( ZIPFILE::CFileReader::CFileCentralMap::iterator itor = centralmap.begin(); itor != centralmap.end(); ++itor)
		{
			if( itor->second.IsDirectory())
				continue;
			if( itor->second.IsFileDeleted())
				continue;
			if( itor->first[0] == L'.')
				continue;
			std::wstring filename( itor->first);
			filename.insert(0, L"/");
			CMetaInfo fmi;
			if( itor->second.IsFileModified())
			{
				CZipTempFile* tempfile = m_tempfilemanager.GetTempFile( filename );
				if( tempfile)
				{
					fsize_t bak = tempfile->GetPos();
					if( bak != 0)
						tempfile->Seek( 0, FSM_SET);
					CFileMetaInfo::MakeFileMetaInfo( *tempfile, fmi);
					tempfile->Seek( bak, FSM_SET);
				}
			}
			else
			{
				if( !bMakeUnModify)
					continue;
				CBaseFile* file = NULL;
				CZipBaseFile::CreateFile( itor->first, FM_EXCL, FA_RDONLY, this, file);
				if( file)
				{
					CFileMetaInfo::MakeFileMetaInfo( *file, fmi);
					CEnv::CloseFile( file);
				}
			}
			mim.insert( std::make_pair( filename, fmi));
		}
	}
	void CZipFS::CheckOpenFileMetaInfo( const std::wstring& filename, FILE_MODE fm)
	{
		// 注意，如果需要修改，须保证这里的逻辑与NativeFS相一致
		if( (FM_CREAT & fm) == 0 )
			return;

		CMetaInfo fmi;
		if( EC_SUCCESS != CPFSBase::GetFileMetaInfo( filename, fmi ) )
		{
			AddFileMetaInfo(filename, fmi);
		}
	}

	void CZipFS::MakeImageHandle()
	{
		if( PFSX::IGetFileIO* rawfile = dynamic_cast<PFSX::IGetFileIO*>( m_basefile))
			m_ImageHandle.Initialize( rawfile->GetRawFileIO());
	}

	int CZipFS::ReOpenFS()
	{
		try
		{
			FILE_ACCESS fa = IsMountReadOnly( m_MountType) ? FA_RDONLY : FA_RDWR;
			FILE_MODE fm = IsMountReadOnly( m_MountType) ? FM_EXCL : FM_CREAT;

			// 删除原有的文件内容，有风险！ yanghaibo 2010-11-18
			if( IsMountTruncate( m_MountType ) ) fm |= FM_TRUNC;

			m_basefile = NULL;

			std::wstring newname = m_deviceName;
			CPFSBase* base = CPacketFileSystem::GetInstance().FindPFSBase( newname);
			if( base)
			{
				bool bBaseFSReadOnly = IsMountReadOnly( base->GetMountType());
				if( bBaseFSReadOnly && !IsMountReadOnly( m_MountType) )
					return EC_FS_READONLY; // 父系统只读，不允许Mount可写的子系统

				assert( CheckDirFileStringFormat( newname));
				int nRes = base->OpenFile( newname, fm, fa, m_basefile);
				if (EC_SUCCESS != nRes)
					return nRes;
				m_BasePFS = base;
				m_strBasePath = newname;
				base->AddRefMountFSDevive( newname);
			}
			else
			{
				PFSX::CNativeFile* rawfile = new PFSX::CNativeFile();
				if( !rawfile->Open( m_deviceName, fm, fa))
				{
					delete rawfile;
					return CEnv::GetLastError();
				}
				m_basefile = rawfile;
			}
			m_zipfile.SetFileIO( *m_basefile);

			if( IsMountReadOnly( m_MountType ) )
				MakeImageHandle();
		}
		catch( CFileIOException& fio)
		{
			return fio.GetLastError();
		}
		catch( ZIPFILE::CZipFileBadFormat&)
		{
			return EC_INVALID_MOUNT_DEVICE;
		}

		return EC_SUCCESS;
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// public functions:
	//

	int CZipFS::Initialize( const std::wstring& device, MOUNT_TYPE mt)
	{
		if( device.empty() )
		{
			assert( !device.empty() );
			return EC_INVALID_MOUNT_DEVICE;
		}
		
		m_deviceName = device;
		m_MountType = mt;

		int res = ReOpenFS();
		if( res != EC_SUCCESS)
		{
			return res;
		}

		// 计算设备路径
		CalcNativeDevicePath();

		// 从.files.meta初始化MetaInfoMap
		InitMetaInfoFromMetaFile();

		return EC_SUCCESS;
	}

	int CZipFS::OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file)
	{
		bool bFSReadOnly = IsMountReadOnly(GetMountType());
		if( bFSReadOnly )
		{
			if( (FA_RDONLY != fa) || (FM_EXCL != fm)  )
				return EC_FS_READONLY;
		}

		//std::wstring name( filename);
		//std::transform( name.begin(), name.end(), name.begin(), ::towlower);

		CFileAccessMap::iterator itor = m_mapFileAccess.begin();
		for( ; itor != m_mapFileAccess.end(); ++itor)
		{
			if( (itor->second == filename) 
				&& ((FA_RDONLY != ((CZipBaseFile*)itor->first)->GetAccess()) || (FA_RDONLY != fa)))
			{
				// 只能以只读方式打开同一文件
				return EC_FILE_OPEN_MODE;
			}
		}

		CBaseFile* zipbasefile = NULL;
		int nCode = CZipBaseFile::CreateFile( filename, fm, fa, this, zipbasefile);
		if( EC_SUCCESS == nCode)
		{
			file = zipbasefile;

			if( !IsMountRuntime(GetMountType()) )
			{
				CheckOpenFileMetaInfo( filename, fm);
			}

			{
				CMutex::CScoped lock( m_cs );
				m_mapFileAccess.insert( std::make_pair( zipbasefile, filename));
			}

		}

		return nCode;
	}

	int CZipFS::RemoveFile( const std::wstring& filename)
	{
		bool bFSReadOnly = IsMountReadOnly( GetMountType() );
		if( bFSReadOnly )
			return EC_FS_READONLY;
		
		{
			CMutex::CScoped lock( m_cs );
			CFileAccessMap::iterator itor = m_mapFileAccess.begin();
			for( ; itor != m_mapFileAccess.end(); ++itor)
			{
				if(itor->second == filename) 
				{
					return EC_SHARING_VIOLATION;
				}
			}
		}

		std::wstring newname( filename);
		newname.erase( 0, newname.find_first_not_of( L'/'));
		if( !m_zipfile.DeleteFile( newname) )
		{
			// 找不到文件时,从MetaInfoMap中删除该文件项
			RemoveFileMetaInfo(filename);
			return EC_FILE_NOT_FOUND;
		}

		// 从MetaInfoMap中删除该文件项
		RemoveFileMetaInfo(filename);

		return EC_SUCCESS;
	}

	void CZipFS::CheckMetaInfo() 
	{
		CMetaInfoMap mim;
		MakeZipMetaInfo( mim, true);
		CBaseFile* file = NULL;
		OpenFile( CPFSBase::GetMetaFileName(), FM_CREAT | FM_TRUNC, FA_WRONLY, file);
		if( file)
		{
			CMetaFileWrite::DoWrite( *file, mim);
			CEnv::CloseFile( file);
		}
		SwapMetaInfoMap( mim);
	}

	void CZipFS::InitMetaInfoFromMetaFile()
	{ 
		if( (GetMountType() & MT_RUNTIME) == 0 )
		{
			CBaseFile* file = NULL;
			CZipBaseFile::CreateFile( CPFSBase::GetMetaFileName(), FM_EXCL, FA_RDONLY, this, file);
			if( file)
			{
				CMetaInfoMap& mim = CPFSBase::GetMetaInfoMap();
				CMetaFileRead::DoRead( *file, mim);
				CEnv::CloseFile( file);
			}
		}
	}

	int CZipFS::CreateDirectory( const std::wstring& path, bool bFailIfExisting /*= false*/ )
	{
		if( IsMountReadOnly(GetMountType() ))
		{
			return EC_FS_READONLY;
		}

		// TODO:
		bool bExisting = IsDirectoryExisting(path);
		if( bFailIfExisting )
		{
			if ( bExisting )
			{
				return EC_DIR_ALREADY_EXISTING;
			}
			else
			{
				return EC_SUCCESS;
			}
		}

		std::wstring zipDir;
		if( path.size() > 0 )
			zipDir = &path[1];
		zipDir += L"/";
		return m_zipfile.AddFile( zipDir ) != NULL ? EC_SUCCESS : EC_DIR_ALREADY_EXISTING;
	}

	int CZipFS::RemoveDirectory( const std::wstring& path, bool bFailIfNotEmpty )
	{
		if( IsMountReadOnly(GetMountType() ))
		{
			return EC_FS_READONLY;
		}
		
		std::wstring zipDir;
		if( path.size() > 0 )
			zipDir = &path[1];
		zipDir += L"/";
		
		// 用判断文件的方式判断目录是否存在
		if( bFailIfNotEmpty )
		{
			const ZIPFILE::CFileCentral* pcentral = m_zipfile.GetFileCentral( zipDir );
			if( pcentral == NULL || pcentral->IsFileDeleted() )
			{
				return EC_DIR_NOT_FOUND;
			}

			// 判断目录是否为空
			if ( !m_zipfile.IsDirectoryEmpty( zipDir ) )
			{
				return EC_DIR_NOT_EMPTY;
			}

			if( m_zipfile.DeleteFile( zipDir ) )
			{
				return EC_SUCCESS;
			}
			else
			{
				assert( false );
				return EC_DIR_NOT_FOUND;
			}
		}
		else
		{
			if( m_zipfile.RemoveDirectory( zipDir ) )
			{
				return EC_SUCCESS;
			}
			else
			{
				// 目录不存在
				return EC_DIR_NOT_FOUND;
			}
		}
	}

	bool CZipFS::IsFileExisting( const std::wstring& filename)
	{
		if( filename.empty() )
		{
			return false;
		}

		std::wstring zipFileName = &filename[1]; // zipFileName不以 '/' 开头
		const ZIPFILE::CFileCentral* pcentral = m_zipfile.GetFileCentral( zipFileName );
		if( pcentral == NULL || pcentral->IsFileDeleted() )
		{
			return false;
		}

		return true;
	}

	bool CZipFS::IsDirectoryExisting( const std::wstring& path )
	{
		if ( path.empty() )
		{
			return false;
		}

		std::wstring zipDir = &path[1];
		zipDir += L"/";
		const ZIPFILE::CFileCentral* pcentral = m_zipfile.GetFileCentral( zipDir );
		if( pcentral == NULL || pcentral->IsFileDeleted() )
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	bool CZipFS::Commit( )
	{
		CEnv::GetOutLogInstance()->Print(L"INFO: PFS Commit BEGIN\n" );

		if( !CPFSBase::Commit() )
		{
			return false;
		}
		
		try
		{
			if( m_zipfile.IsZipFileModified())
			{
				int nIntegrityFailedCount = 0;

				ZIPFILE::CFileReader::CFileCentralMap& centralmap = m_zipfile.GetFileCentralMap();
				ZIPFILE::CFileReader::CFileCentralMap::iterator iEnd = centralmap.end();
				for( ZIPFILE::CFileReader::CFileCentralMap::iterator itor = centralmap.begin(); itor != iEnd; ++itor)
				{
					const ZIPFILE::CFileCentral& fc = itor->second;
					if( fc.IsFileDeleted())
						continue;
					
					if( fc.IsFileModified())
					{
						CZipTempFile* tempfile = m_tempfilemanager.GetTempFile( fc.GetFileName() );
						if( NULL == tempfile)
						{
							CEnv::GetOutLogInstance()->Print(L"ERROR: - No TempFile for file %s found.\n", fc.GetFileName().c_str() );
							nIntegrityFailedCount ++;
						}								
					}
				}

				if( nIntegrityFailedCount > 0 )
				{
					CEnv::GetOutLogInstance()->Print(L"ERROR: Totally there are %d files not finding its corresponding temp data file.\n", 
						nIntegrityFailedCount );
				}				

				helper::CArrangeZipManager arrangeManager( this );

				// 清理删除的文件的Central对象。
				m_zipfile.ClearDeletedFileCentrals();
				
				size_t nStillModified = m_zipfile.CountModifiedFileCentrals();
				if( nStillModified != 0 )
				{
					CEnv::GetOutLogInstance()->Print(
						L"WARN: After committing the ZipFS, there are still %d files being modified.\n",
							nStillModified);
				}

				// 提交临时文件管理器，*尽量*清理已经关闭的文件，但不保证完全清理。
				size_t nOpenAfterCommit = m_tempfilemanager.Commit();
				if( nOpenAfterCommit != 0 )
				{
					// 只写WARNING日志。
					if( nOpenAfterCommit == 1 )
						CEnv::GetOutLogInstance()->Print(L"WARN: After committing the ZipFS, there is still 1 file open.\n" );
					else
						CEnv::GetOutLogInstance()->Print(L"WARN: After committing the ZipFS, there are still %d files open.\n",
							nOpenAfterCommit);
				}

				//
				//	如果CArrangeZipManager没有更新FileCentralMap，那么，下面的两行代码是有必要的。
				//	尤其是对于CArrangeZipUnCompact这种模式的包整理方案。
				//	但，由于我们在CArrangeZipUnCompact中同时更新了FileCentralMap，所以
				//	m_zipfile.SetFileIO( *m_basefile);这行暂时没有必要。
				//	另外，由于合并文件内容时，File的MetaInfo也进行了相应的复制，所以
				//	InitMetaInfoFromMetaFile()这行暂时也是没有必要的。
				//
				//else
				//{
				// // 重新初始化centralmap和metainfomap.
				//	m_zipfile.SetFileIO( *m_basefile);
				//	InitMetaInfoFromMetaFile();
				//}
			}
		}
		catch( CFileIOException& rIOError)
		{
			CEnv::SetLastError( rIOError.GetLastError() );
			
			CEnv::GetOutLogInstance()->Print(L"ERROR: - CFileIOException occurred(%d).\n", rIOError.GetLastError() );

			return false;
		}
		catch( ZIPFILE::CZipWriteError& /*rZipError*/ )
		{
			DWORD dwNtError = ::GetLastError();
			if( dwNtError == ERROR_SUCCESS )
			{
				// TODO: 丰富CZipWriteError的内容，确定具体错误码
				CEnv::SetLastError( EC_FILE_BAD_ZIPDATA );
			}
			else
			{
				CEnv::SetLastError( dwNtError );
			}

			CEnv::GetOutLogInstance()->Print(L"ERROR: - CZipWriteError occurred(%d).\n", CEnv::GetLastError() );

			return false;
		}

		CEnv::GetOutLogInstance()->Print(L"INFO: PFS Commit END\n" );

		return true;
	}

	bool CZipFS::Destroy( bool commit )
	{
		CEnv::GetOutLogInstance()->Print(L"INFO: PFS Destroy BEGIN - commit(%d)\n", commit );
		if( !CPFSBase::Destroy( commit ) )
		{
			return false;
		}

		m_ImageHandle.Destroy();
		if( commit )
		{
			return Commit();
		}

		CEnv::GetOutLogInstance()->Print(L"INFO: PFS Destroy END\n" );

		return true;
	}

}