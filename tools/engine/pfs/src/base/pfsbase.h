#pragma once

namespace PFS {

class CBaseFile;

class CPFSBase
{
private:
	typedef std::set<std::wstring>	CRefMountFSSet;
	CRefMountFSSet		m_rmfs;
	CMetaInfoMap		m_MetaInfos;
	bool				m_bMetaChanged;	
	std::wstring		m_mountpoint;		// 由SetMountPoint()初始化

protected:
	MOUNT_TYPE			m_MountType;	// 保存initialize()的参数mt
	std::wstring		m_deviceName;	// 保存initialize()的参数device
	std::wstring		m_nativeDevicePath;	// 由CalcNativeDevicePath()初始化
	CPFSBase*			m_BasePFS;		// 本PFS的父PFS，如果本PFS为根PFS，那么此值为NULL
	unsigned short		m_usFilesMetaVersion; // 0x0100 by default.

public:
	CPFSBase() : m_BasePFS(NULL), m_bMetaChanged( false), m_usFilesMetaVersion(0x0100) {}
	CPFSBase( FS_TYPE type);

	virtual ~CPFSBase() {}

public:
	virtual void DumpInfos( COutLog& outlog) const;

	virtual CPFSBase* Clone() const = 0;
	virtual void Delete() {	delete this; }
	virtual int Initialize( const std::wstring& device, MOUNT_TYPE mt) = 0;
	virtual FS_TYPE GetFSType() const = 0;
	virtual int OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file) = 0;
	virtual int RemoveFile( const std::wstring& filename) = 0;

#ifdef CreateDirectory
#pragma push_macro( "CreateDirectory" )
#undef CreateDirectory
#endif // CreateDirectory

#ifdef RemoveDirectory
#pragma push_macro( "RemoveDirectory" )
#undef RemoveDirectory
#endif // RemoveDirectory

	virtual int CreateDirectory( const std::wstring& path, bool bFailIfExisting = false ) = 0;
	virtual int RemoveDirectory( const std::wstring& path, bool bFailIfNotEmpty = false ) = 0;
	virtual bool IsDirectoryExisting( const std::wstring& path ) = 0;

	virtual bool IsFSBusy() const = 0;
	virtual bool IsFileExisting( const std::wstring& filename) = 0;
	virtual void OnFileClose(CBaseFile* pFile) {};

	virtual void AddRefMountFSDevive( const std::wstring& str) { m_rmfs.insert( str); }
	virtual void RemoveRefMountFSDevive( const std::wstring& str) { m_rmfs.erase( str); }
	virtual bool IsFSMountReference() const { return !m_rmfs.empty(); }

	virtual void CheckMetaInfo() = 0;
	virtual bool Commit( );
	virtual bool Destroy( bool commit );

	virtual bool SetFilesMetaVersion( unsigned short usVersion )
	{
		if( m_usFilesMetaVersion != usVersion )
		{
			m_bMetaChanged = true; // 版本变化，同时标记MetaChanged。
			m_usFilesMetaVersion = usVersion;
		}
		return true;
	}

	virtual unsigned short GetFilesMetaVersion() const
	{
		return m_usFilesMetaVersion;
	}

	// 刷新 files.meta 文件
	virtual bool RefreshMetaFile();
	// 得到 files.meta 文件的hash值
	virtual bool GetMetaFileHash( FILE_DATA_HASH& hash );

	virtual bool RemoveFileMetaInfo( const std::wstring& filename )
	{
		if( m_MetaInfos.erase( filename) )
		{
			m_bMetaChanged = true;
			return true;
		}
		else
		{
			return false;
		}
	}

	virtual int GetFileMetaInfo( const std::wstring& filename, CMetaInfo& info)
	{
		const CMetaInfoMap& mim = m_MetaInfos;
		CMetaInfoMap::const_iterator it = mim.find( filename);
		if( it == mim.end())
			return EC_FILE_NOT_FOUND;
		info = it->second;
		return EC_SUCCESS;
	}

	virtual int SetFileMetaInfo( const std::wstring& filename, const CMetaInfo& info)
	{
		CMetaInfoMap& mim = m_MetaInfos;
		CMetaInfoMap::iterator it = mim.find( filename);
		if( it == mim.end())
			return EC_FILE_NOT_FOUND;
		CMetaInfo& oldinfo = it->second;
		if( oldinfo != info)
		{
			oldinfo = info;
			m_bMetaChanged = true;
		}
		return EC_SUCCESS;
	}

	virtual int AddFileMetaInfo( const std::wstring& filename, const CMetaInfo& info)
	{
		CMetaInfoMap& mim = m_MetaInfos;
		CMetaInfoMap::iterator it = mim.find( filename);
		if( it == mim.end())
		{
			CMetaInfoMap::_Pairib ib = m_MetaInfos.insert( std::make_pair( filename, info));
			if( ib.second )
				m_bMetaChanged = true;
		}
		else
		{
			CMetaInfo& oldinfo = it->second;
			if( oldinfo != info)
			{
				oldinfo = info;
				m_bMetaChanged = true;
			}
		}

		return EC_SUCCESS ;
	}

	virtual CMetaInfoMap& GetMetaInfoMap() { return m_MetaInfos; }
	MOUNT_TYPE GetMountType() const { return m_MountType; }	

	void SetMountPoint( const std::wstring& mp) { m_mountpoint = mp; }
	const std::wstring& GetMountPoint() const { return m_mountpoint; }
	const std::wstring& GetDeviceName() const { return m_deviceName; };

	// 得到该PFS的磁盘设备全路径(路径分隔符全部为 '/' )
	const std::wstring& GetNativeDevicePath() const { return m_nativeDevicePath; }

	// 为指定的文件计算它在本PFS中的NativeFilePath
	virtual std::wstring GetNativeFilePath( const std::wstring& rFileName ) const;

protected:
	void SwapMetaInfoMap( CMetaInfoMap& mim) { m_MetaInfos.swap( mim); }

	// 反向递归计算该PFS的磁盘设备全路径（在Initialize中调用）
	void CalcNativeDevicePath();

private:
	// 为子设备计算磁盘设备全路径（被CalcNativeDevicePath调用）
	// subDevice - 子设备的device名称
	std::wstring _CalcNativeDevicePathForChildPFS(const std::wstring& subDevice);

	void DumpBaseInfos( COutLog& outlog) const;

public:
	static CPFSBase* Create( FS_TYPE type);

	static const std::wstring& GetMetaFileName()
	{
		static std::wstring name = L"/.files.meta";
		return name;
	}
};

} //namespace PFS {