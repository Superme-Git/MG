//////////////////////////////////////////////////////////////////////////
//
// native.cpp
//
// native file system 的实现
//

#include "../common.h"
#include "native.h"
#include "../base/packetfilesystem.h"

namespace PFS
{

class CNativeAbstractFileSystem
{
private:
	CPFSBase*			m_nfs;

public:
	CNativeAbstractFileSystem( CPFSBase* nfs) : m_nfs( nfs) { assert( m_nfs); }
	virtual ~CNativeAbstractFileSystem() {}
protected:
	void OnFileClose( CBaseFile* file);
	CPFSBase* GetNativeFS() { return m_nfs; }

public:
	virtual void Delete() { assert( this); delete this; };
	virtual int OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file) = 0;

	virtual void InitMetaInfoFromMetaFile( CMetaInfoMap& mim) = 0;

	virtual void CheckMetaInfo( CMetaInfoMap& mim) = 0;
	virtual int RemoveFile( const std::wstring& filename) = 0;
	virtual bool IsFileExisting( const std::wstring& filename) const = 0;

	virtual int CreateDirectory( const std::wstring& path, bool bFailIfExisting/* = false*/ ) = 0;
	virtual int RemoveDirectory( const std::wstring& path, bool bFailIfNotEmpty/* = false*/ ) = 0;
	virtual bool IsDirectoryExisting( const std::wstring& path ) = 0;

	virtual void MakeMetaInfo( CMetaInfoMap& mim) = 0;
	virtual std::wstring GetBasePath() const = 0;
	virtual CPFSBase* GetBasePFS(){ return NULL; }
};

inline void CNativeAbstractFileSystem::OnFileClose( CBaseFile* file)
{
	assert( m_nfs);
	m_nfs->OnFileClose( file);
}

class CNativeMountFileSystem : public CNativeAbstractFileSystem
{
private:
	class CMountFile : public CBaseFile
	{
	private:
		CNativeMountFileSystem*	m_nmfs;
		CBaseFile*				m_file;
	protected:
		CBaseFile* GetBaseFile() const { return m_file; }
	public:
		CMountFile( CBaseFile* bf, CNativeMountFileSystem* nmfs) : m_file( bf), m_nmfs( nmfs) {}
		virtual ~CMountFile() 
		{ 
			CEnv::CloseFile( m_file);
			m_nmfs->OnFileClose( this); 
		}
	public:
		virtual size_t Read( void* data, size_t size)
		{
			return m_file->Read( data, size);
		}
		virtual size_t Write( const void* data, size_t size)
		{
			return m_file->Write( data, size);
		}
		virtual fsize_t GetSize()
		{
			return m_file->GetSize();
		}
		virtual fsize_t Seek( foff_t pos, FILE_SEEK_METHOD fsm)
		{
			return m_file->Seek( pos, fsm);
		}
		virtual fsize_t GetPos()
		{
			return m_file->GetPos();
		}
		virtual bool SetEnd()
		{
			return m_file->SetEnd();
		}
		virtual CImageData GetImage()
		{
			return m_file->GetImage();
		}
		virtual bool ReadAll(void* data, fsize_t size )
		{
			return m_file->ReadAll(data, size );
		}
	};
	class CNativeMountFile : public CMountFile, public PFSX::IGetFileIO
	{
	public:
		CNativeMountFile( CBaseFile* bf, CNativeMountFileSystem* nmfs) : CMountFile( bf, nmfs) {}
		virtual ~CNativeMountFile() {}
	public:
		virtual PFS::CFileIO& GetRawFileIO() const
		{
			PFSX::IGetFileIO* iget = dynamic_cast<PFSX::IGetFileIO*>( GetBaseFile());
			assert( iget);
			return iget->GetRawFileIO();
		}
	public:
		static int CreateFileObject( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa,
			CNativeMountFileSystem* nmfs, CBaseFile*& pNewFile)
		{
			CBaseFile*	bf = NULL;
			int nError = nmfs->m_BasePFS->OpenFile( filename, fm, fa, bf);
			if( EC_SUCCESS != nError)
				return nError;
			if( bf == NULL )
			{
				// 特别说明:
				// CPacketFileSystem::GetLastError()/CPacketFileSystem::SetLastError()
				// 存在线程不安全问题。
				// 当前线程的错误码可能被其它线程覆盖，因此，这里再获取一次Win32API的错误码。
				// 如果当前线程的LastError已经被覆盖，这里也没有必要覆盖人家的。所以，这里也只好作罢。
				// —— 非妥善解决方案！只能暂时如此。
				// yanghaibo
				return ::GetLastError();
			}

			if( PFSX::IGetFileIO* iget = dynamic_cast<PFSX::IGetFileIO*>( bf))
				pNewFile = new CNativeMountFile( bf, nmfs);
			else
				pNewFile = new CMountFile( bf, nmfs);

			return pNewFile ? EC_SUCCESS : ERROR_OUTOFMEMORY;
		}
	};

	CPFSBase*		m_BasePFS;
	std::wstring	m_strBasePath;

private:
	// Created by CreateObject()
	CNativeMountFileSystem( CPFSBase* nfs, CPFSBase* basePFS, std::wstring strBaseDevice) 
		: CNativeAbstractFileSystem( nfs)
		, m_BasePFS(basePFS)
		, m_strBasePath(strBaseDevice)
	{
	}

public:
	virtual ~CNativeMountFileSystem() 
	{
		if( m_BasePFS )
		{
			m_BasePFS->RemoveRefMountFSDevive( m_strBasePath);
			m_BasePFS = NULL;
			m_strBasePath.clear();
		}
	}

	static CNativeAbstractFileSystem* CreateObject( const std::wstring& device, MOUNT_TYPE mt, CPFSBase* nfs)
	{
		if( !CheckDirFileStringFormat( device) )
		{
			CEnv::SetLastError( EC_INVALID_DEVICE_NAME );
			return NULL;
		}

		std::wstring newname = device;
		CPFSBase* base = CPacketFileSystem::GetInstance().FindPFSBase( newname);
		if( NULL == base)
		{
			CEnv::SetLastError(EC_NATIVE_DEVICE_LOST);
			return NULL;
		}

		bool bBaseFSReadOnly = IsMountReadOnly( base->GetMountType() );
		bool bFSReadOnly = IsMountReadOnly( mt );
		if( bBaseFSReadOnly && !bFSReadOnly )
		{
			CEnv::SetLastError(EC_FS_READONLY);
			return NULL;
		}
	
		// BUGFIX:
		// newname.empty() 的判断，目的是解决如下的类型的Mount失败的情形：
		// <mount dir="/run" device="%current-dir%" mt="2"/>
		// <mount dir="/a/cfg" device="/run/cfg" mt="2"/>
		// <mount dir="/cfg" device="/a/cfg" mt="2"/>  <--- 这条mount将会导致newname为empty().
		// 对于 IsMountTruncate( mt ) 的情形，参见下面的详细注释。
		if( !newname.empty() && !base->IsDirectoryExisting( newname ) )
		{
			if( bBaseFSReadOnly || bFSReadOnly)
			{	
				CEnv::SetLastError(EC_NATIVE_DEVICE_LOST);
				return NULL;
			}
			else
			{
				// 创建目录
				int ErrorCode = base->CreateDirectory( newname, false );
				if( ErrorCode != EC_SUCCESS )
				{
					CEnv::SetLastError( ErrorCode );
					return NULL;
				}
			}
		}
		else
		{
			// 删除原有的目录下的所有文件，有风险！ yanghaibo 2010-11-18
			if( IsMountTruncate( mt ) ) 
			{
				// 如果 newname 为空，对于 IsMountTruncate( mt ) 的情形，由RemoveDirectory()自己处理。
		
				// 删除原有目录，重新创建
				int ErrorCode = base->RemoveDirectory( newname, false );
				if( ErrorCode != EC_SUCCESS )
				{
					CEnv::SetLastError( ErrorCode );
					return NULL;
				}

				// 重新创建目录
				ErrorCode = base->CreateDirectory( newname, false );
				if( ErrorCode != EC_SUCCESS )
				{
					CEnv::SetLastError( ErrorCode );
					return NULL;
				}
			}
		}

		base->AddRefMountFSDevive( newname);

		return new CNativeMountFileSystem( nfs, base, newname );
	}

public:
	void InitMetaInfoFromMetaFile( CMetaInfoMap& mim)
	{
		//MakeMetaInfo(mim); xuhui disable it; if m_BasePFS == native, m_BasePFS->GetMetaInfoMap() , seach -_-
	}

	void MakeMetaInfo( CMetaInfoMap& mim)
	{
		const CMetaInfoMap&  sim = m_BasePFS->GetMetaInfoMap();
		for( CMetaInfoMap::const_iterator it = sim.begin(); it != sim.end(); ++ it)
		{
			const std::wstring&	filename = it->first;

			if( filename.size() <= m_strBasePath.size())
				continue;
			if( 0 != wcsncmp( filename.c_str(), m_strBasePath.c_str(), m_strBasePath.size()))
				continue;
			if( '/' != filename[m_strBasePath.size()])
				continue;
			const CMetaInfo&	metainfo = it->second;
			mim.insert( std::make_pair( filename.substr( m_strBasePath.size()), metainfo));
		}
	}

	std::wstring GetRealFileName( const std::wstring& filename) const
	{
		if( !filename.empty() && filename[0] != L'\\' && filename[0] != L'/' )
			return m_strBasePath + L'/' + filename;
		else
			return m_strBasePath + filename;
	}

	virtual int OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file)
	{
		// 文件是否允许写打开已经在CNativeFS中检测
		assert( m_BasePFS);
		int nError = CNativeMountFile::CreateFileObject(GetRealFileName( filename), fm, fa, this, file);
		return nError;
	}
	virtual void CheckMetaInfo( CMetaInfoMap& mim)
	{
		assert( m_BasePFS);
		MakeMetaInfo( mim);
	}
	virtual int RemoveFile( const std::wstring& filename)
	{
		assert( m_BasePFS);
		return m_BasePFS->RemoveFile( GetRealFileName( filename));
	}
	virtual bool IsFileExisting( const std::wstring& filename) const
	{
		assert( m_BasePFS);
		return m_BasePFS->IsFileExisting( GetRealFileName( filename));
	}

	virtual int CreateDirectory( const std::wstring& path, bool bFailIfExisting )
	{
		return m_BasePFS->CreateDirectory(GetRealFileName( path), bFailIfExisting );
	}

	virtual int RemoveDirectory( const std::wstring& path, bool bFailIfNotEmpty )
	{
		return m_BasePFS->RemoveDirectory(GetRealFileName( path), bFailIfNotEmpty );
	}

	virtual bool IsDirectoryExisting( const std::wstring& path )
	{
		return m_BasePFS->IsDirectoryExisting(GetRealFileName( path) );
	}

	virtual std::wstring GetBasePath() const { return m_BasePFS->GetMountPoint() + m_strBasePath; }

	virtual CPFSBase* GetBasePFS(){ return m_BasePFS; }
};

class CNativeDirectoryFileSystem : public CNativeAbstractFileSystem
{
	class CNativeBaseFile : public PFSX::CNativeFile
	{
	private:
		CNativeDirectoryFileSystem*	m_pFS;
		CNativeBaseFile( CNativeDirectoryFileSystem* fs) : m_pFS( fs) { assert( m_pFS);}
	public:
		virtual ~CNativeBaseFile() {  assert( m_pFS); m_pFS->OnFileClose( this); }
	public:
		static int CreateFileObject( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa,
			CNativeDirectoryFileSystem* fs, CBaseFile*& pNewFile )
		{
			CNativeBaseFile* file = new CNativeBaseFile( fs);
			if( file == NULL )
			{
				return ERROR_OUTOFMEMORY;
			}

			if( !file->Open( filename, fm, fa) )
			{
				// 异常和日志都已经处理
				delete file;
				file = NULL;
				
				if( CEnv::GetLastError() == EC_SUCCESS )
				{
					CEnv::SetLastError( ::GetLastError() );
				}
				return CEnv::GetLastError();
			}
		
			pNewFile = file;
			return EC_SUCCESS;
		}
	};
	class CCheckMetaInfoFindFiles : public CFindFiles
	{
		CNativeDirectoryFileSystem&		m_ndfs;
	protected:
		CMetaInfoMap&		m_map;
	public:
		CCheckMetaInfoFindFiles( const std::wstring& strBasePath, CMetaInfoMap& mim, CNativeDirectoryFileSystem& ndfs) : CFindFiles( strBasePath), m_map( mim), m_ndfs( ndfs) {}
		virtual ~CCheckMetaInfoFindFiles() {};
	public:
		void OnOneFoundFile( const std::wstring& filename)
		{
			//assert( CheckDirFileStringFormat( filename));
			CMetaInfo fmi;

			std::wstring realfile = m_ndfs.GetRealFileName( filename);
			
			PFSX::CNativeFile file;
			if( file.Open( realfile, FM_EXCL, FA_RDONLY) )
			{
				CFileMetaInfo::MakeFileMetaInfo( file, fmi);
			}

			m_map.insert( std::make_pair( filename, fmi));
		}
	};
	class CInitializeFindFiles : public CCheckMetaInfoFindFiles
	{
	public:
		CInitializeFindFiles( const std::wstring& strBasePath, CMetaInfoMap& mim, CNativeDirectoryFileSystem& ndfs) : CCheckMetaInfoFindFiles( strBasePath, mim, ndfs) {}
		virtual ~CInitializeFindFiles() {}
	protected:
		void OnOneFoundFile( const std::wstring& filename)
		{
			//assert( CheckDirFileStringFormat( filename));
			CMetaInfoMap::iterator it = m_map.find( filename);
			if( it == m_map.end())
				CCheckMetaInfoFindFiles::OnOneFoundFile( filename);
		}
	};

	std::wstring m_strBasePath;

private:
	// Created by CreateObject()
	CNativeDirectoryFileSystem( CPFSBase* nfs, std::wstring device) 
		: CNativeAbstractFileSystem( nfs)
		, m_strBasePath( device )
	{}

public:
	virtual ~CNativeDirectoryFileSystem() {}

private:
	inline bool IsFSWritable()
	{
		return (MT_WRITABLE & GetNativeFS()->GetMountType()) == MT_WRITABLE;
	}

public:
	void InitMetaInfoFromMetaFile( CMetaInfoMap& mim)
	{
		// 从MetaInfo文件初始化MetaInfoMap
		std::wstring strMetaFilename = GetRealFileName( CPFSBase::GetMetaFileName() );
		PFSX::CNativeFile file;
		if( file.Open( strMetaFilename, FM_EXCL, FA_RDONLY ) )
		{
			CMetaFileRead::DoRead( file, mim);
		}
	}

	void MakeMetaInfo( CMetaInfoMap& mim)
	{
		// 先从MetaFile中读取
		InitMetaInfoFromMetaFile(mim);

		// 填补遗漏
		CInitializeFindFiles iff( m_strBasePath, mim, *this);
		iff.DoFind();
	}

	std::wstring GetRealFileName( const std::wstring& filename) const 
	{
		if( !filename.empty() && filename[0] != L'\\' && filename[0] != L'/' )
			return m_strBasePath + L'/' + filename;
		else
			return m_strBasePath + filename;
	}

	static CNativeAbstractFileSystem* CreateObject( const std::wstring& device, MOUNT_TYPE mt, CPFSBase* nfs)
	{
		bool deviceExisting = CFileIO::IsDirectoryExisting( device);
		if( !deviceExisting )
		{
			if ( IsMountReadOnly(mt) )
			{
				CEnv::SetLastError( EC_NATIVE_DEVICE_LOST );
				return NULL;
			}
			else
			{
				if( !CFileIO::CheckCreateDirectory( device ) )
				{
					CEnv::SetLastError( ::GetLastError() );
					return NULL;
				}
			}
		}
		else
		{
			// 删除原有的目录下的所有文件，有风险！ yanghaibo 2010-11-18
			if( IsMountTruncate( mt ) ) 
			{
				// 删除原有目录，重新创建
				int ErrorCode = CFileIO::RemoveDirectory( device, false );
				if( ErrorCode != EC_SUCCESS )
				{
					CEnv::SetLastError( ErrorCode );
					return NULL;
				}

				// 重新创建目录
				if( !CFileIO::CheckCreateDirectory( device ) )
				{
					CEnv::SetLastError( ::GetLastError() );
					return NULL;
				}
			}
		}

		return new CNativeDirectoryFileSystem( nfs, device );
	}

	virtual int OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file)
	{
		// 文件是否允许写打开已经在CNativeFS中检测
		std::wstring realfile = GetRealFileName( filename);
		CBaseFile* newfile = NULL;
		int nError = CNativeBaseFile::CreateFileObject( realfile, fm, fa, this, newfile );
		if( nError == EC_SUCCESS )
			file = newfile;
		return nError;
	}

	virtual void CheckMetaInfo( CMetaInfoMap& mim)
	{
		CCheckMetaInfoFindFiles cmiff( m_strBasePath, mim, *this);
		cmiff.DoFind();
		
		// 检查系统是否可写，如果不可写，只将mim放到内存中
		if ( IsFSWritable() )
		{
			std::wstring strMetaFilename = GetRealFileName( CPFSBase::GetMetaFileName());
			PFSX::CNativeFile file;
			if( file.Open( strMetaFilename, FM_CREAT | FM_TRUNC, FA_WRONLY) )
			{
				CMetaFileWrite::DoWrite( file, mim);
			}
		}
	}

	virtual int RemoveFile( const std::wstring& filename)
	{
		return CFileIO::DeleteFile( GetRealFileName( filename));
	}

	virtual bool IsFileExisting( const std::wstring& filename) const
	{
		return CFileIO::IsFileExisting( GetRealFileName( filename));
	}

	virtual int CreateDirectory( const std::wstring& path, bool bFailIfExisting/* = false*/ )
	{
		std::wstring realPath = GetRealFileName( path );
		bool bExisting = CFileIO::IsDirectoryExisting(  realPath );
		if( !bExisting )
		{
			if( CFileIO::CheckCreateDirectory( realPath ) )
			{
				return EC_SUCCESS;
			}
			else
			{
				return ::GetLastError();
			}
		}
		else
		{
			if ( bFailIfExisting )
			{
				return EC_DIR_ALREADY_EXISTING;
			}
			else
			{
				return EC_SUCCESS;
			}
		}
	}

	virtual int RemoveDirectory( const std::wstring& path, bool bFailIfNotEmpty )
	{
		return CFileIO::RemoveDirectory( GetRealFileName( path ), bFailIfNotEmpty );
	}

	virtual bool IsDirectoryExisting( const std::wstring& path )
	{
		return CFileIO::IsDirectoryExisting( GetRealFileName( path ) );
	}

	virtual std::wstring GetBasePath() const { return m_strBasePath; }
};

CNativeFS::~CNativeFS()
{
	if( m_filesystem)
	{
		m_filesystem->Delete();
	}
}

int CNativeFS::Initialize( const std::wstring& device, MOUNT_TYPE mt)
{
	assert( !device.empty());
	assert( NULL == m_filesystem);

	m_deviceName = device;
	m_MountType = mt;

	if ( device[0] == L'/' )
	{
		m_filesystem = CNativeMountFileSystem::CreateObject( device, mt, this );
	}
	else
	{
		m_filesystem = CNativeDirectoryFileSystem::CreateObject( device, mt, this );
	}

	if( m_filesystem == NULL )
	{
		return CEnv::GetLastError();
	}

	m_BasePFS = m_filesystem->GetBasePFS();
	CalcNativeDevicePath();

	if ( (mt & MT_RUNTIME) == 0 )
	{
		CMetaInfoMap& mim = CPFSBase::GetMetaInfoMap();
		m_filesystem->InitMetaInfoFromMetaFile( mim );
	}

	return EC_SUCCESS;
}

int CNativeFS::OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file)
{
	assert( m_filesystem);

	bool bFSReadOnly = IsMountReadOnly(GetMountType());
	if( bFSReadOnly )
	{
		if( (FA_RDONLY != fa) || (FM_EXCL != fm)  )
			return EC_FS_READONLY;
	}

	int nCode = m_filesystem->OpenFile( filename, fm, fa, file);
	if( EC_SUCCESS == nCode && file != NULL )
	{
		if ( !IsMountRuntime(GetMountType()))
		{
			CheckOpenFileMetaInfo( filename, fm );
		}
		{
			CMutex::CScoped lock( m_cs );
			m_setFiles.insert(file);
		}
	}

	return nCode;
}

void CNativeFS::CheckMetaInfo()
{
	assert( m_filesystem);
	CMetaInfoMap mim;
	m_filesystem->CheckMetaInfo( mim);
	SwapMetaInfoMap( mim);
}

int CNativeFS::RemoveFile( const std::wstring& filename)
{
	assert( m_filesystem);
	if ( IsMountReadOnly( GetMountType() ) )
	{
		return EC_FS_READONLY;
	}

	int nError = m_filesystem->RemoveFile( filename);		
	if( EC_SUCCESS != nError )
	{
		if( EC_FILE_NOT_FOUND == nError ||
			ERROR_FILE_NOT_FOUND == nError ||
			ERROR_PATH_NOT_FOUND  == nError )
		{
			// 如果删除失败,但错误码为找不到文件,我们删除本地的MetaInfo
			RemoveFileMetaInfo(filename);
		}
		return nError;
	}

	RemoveFileMetaInfo(filename);

	return EC_SUCCESS;
}

int CNativeFS::CreateDirectory( const std::wstring& path, bool bFailIfExisting/*= false */)
{
	assert( m_filesystem);
	if ( IsMountReadOnly( GetMountType() ) )
	{
		return EC_FS_READONLY;
	}

	return m_filesystem->CreateDirectory( path, bFailIfExisting );
}

int CNativeFS::RemoveDirectory( const std::wstring& path, bool bFailIfNotEmpty )
{
	assert( m_filesystem);
	if ( IsMountReadOnly( GetMountType() ) )
	{
		return EC_FS_READONLY;
	}

	return m_filesystem->RemoveDirectory( path, bFailIfNotEmpty );
}

bool CNativeFS::IsDirectoryExisting( const std::wstring& path )
{
	assert( m_filesystem);
	return m_filesystem->IsDirectoryExisting( path );
}

std::wstring CNativeFS::GetBasePath() const 
{ 
	assert( m_filesystem);
	return m_filesystem->GetBasePath();
}

CMetaInfoMap& CNativeFS::GetMetaInfoMap() 
{ 
	CMetaInfoMap& mim = CPFSBase::GetMetaInfoMap();
	if( mim.empty())
	{
		assert( m_filesystem);
		m_filesystem->MakeMetaInfo( mim);
	}
	return mim;
}

bool CNativeFS::IsFileExisting( const std::wstring& filename)
{
	assert( m_filesystem);
	return m_filesystem->IsFileExisting( filename);
}

void CNativeFS::CheckOpenFileMetaInfo( const std::wstring& filename, FILE_MODE fm)
{
	// 注意，如果需要修改，须保证这里的逻辑与ZipFS相一致
	if( (FM_CREAT & fm) == 0 )
		return;

	CMetaInfo fmi;
	if( EC_SUCCESS != CPFSBase::GetFileMetaInfo( filename, fmi ) )
	{
		AddFileMetaInfo(filename, fmi );
	}
}

}// namespace PFS