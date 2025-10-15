#pragma once

namespace PFS {

class CPFSBase;

class CPacketFileSystem
{
protected:
	struct stringlengthcasecmp
	{
		bool operator()( const std::wstring &x, const std::wstring &y) const
		{ 
			if( x.size() == y.size())
				return wmemcmp( x.c_str(), y.c_str(), x.size()) < 0;
			else
				return x.size() > y.size();
		}
	};

	typedef std::map<std::wstring,CPFSBase*,stringlengthcasecmp> CMountPoints;

	typedef std::map<std::wstring, std::wstring, stringlengthcasecmp> CBaseFilter;

	CMountPoints	m_MountPoints;
	CBaseFilter		m_BaseFilter;
	bool			m_BaseFilterFlag;
	mutable int		m_nLastError;// mutable������const�����б��޸�

	unsigned short	m_usFilesMetaDefaultVersion; // 1.0 for local device. 0x0100

	CPacketFileSystem() 
		: m_nLastError(0)
		, m_BaseFilterFlag(false)
		, m_usFilesMetaDefaultVersion(0x0100)
	{
	}

	~CPacketFileSystem()
	{
	}

public:
	static CPacketFileSystem& GetInstance();	
	CPFSBase* FindPFSBase( std::wstring& filename);

	int Mount( const std::wstring& dir, const std::wstring& device, FS_TYPE fst, MOUNT_TYPE mt);
	int Commit( const std::wstring& dir );
	int Unmount( const std::wstring& dir, bool commit);
	int Open( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file);
	int GetMetaInfos( const std::wstring& dir, CMetaInfoMap& mim);
	int SetMetaInfos( const std::wstring& dir, CMetaInfoMap& mim);
	bool GetMetaInfosEx( const std::wstring& dir, CMetaInfoMap& mim);
	int CheckMetaInfo( const std::wstring& dir);
	int GetFileMetaInfo( const std::wstring& filename, CMetaInfo& info);
	int SetFileMetaInfo( const std::wstring& filename, const CMetaInfo& info);
	int AddFileMetaInfo( const std::wstring& filename, const CMetaInfo& info);
	int RemoveFile( const std::wstring& filename);
	bool IsFileExisting( const std::wstring& filename);

	// ˢ��Ŀ¼ dir �� files.meta �ļ�
	bool RefreshMetaFile( const std::wstring& dir);
	// �õ�Ŀ¼ dir �� files.meta �ļ���hashֵ
	bool GetMetaFileHash( const std::wstring& dir, FILE_DATA_HASH& res);
	
	// ������ر�BaseFilter��Ϊ�˲�Ӱ��Ч�ʣ�ȱʡ����²�����BaseFilter��������ʽ����.
	// ���ؿ����ɹ���ʧ�ܡ�
	bool EnableBaseFilter(bool bEnable)
	{
		m_BaseFilterFlag = bEnable;
		return true;
	}

	bool IsBaseFilterEnabled() const
	{
		return m_BaseFilterFlag;
	}

	// ����/��ȡbaseĿ¼�������滻Ŀ¼(��������ʽ)
	bool SetBaseFilter( const std::wstring& baseDirOriginal, const std::wstring& baseDirNew );
	bool GetBaseFilter( const std::wstring& baseDirOriginal, std::wstring& baseDirNew ) const;

	// if baseDir is root "/" or empty, change the default filesMetaVersion.
	bool SetBaseFilesMetaVersion( const std::wstring& baseDir, const std::wstring& versionString );

	// if baseDir is root "/" or empty, get the default filesMetaVersion.
	bool GetBaseFilesMetaVersion( const std::wstring& baseDir, std::wstring& rVersionString ) const;

	void DumpInfos( COutLog& outlog);
public:
	int GetLastError() const
	{
		return m_nLastError;
	}

	// ������ const ������ʹ��
	void SetLastError( int nError) const
	{
		m_nLastError = nError;
	}

	void NativeFileNameToPFSFileName( const std::wstring& in, std::wstring& out);

	void PFSFileNameToNativeFileName( const std::wstring& in, std::wstring& out);

	// ����PackageDirectoryDevice����
	bool IsRepeatMountZipFile( const std::wstring& device, MOUNT_TYPE mt);

private:
	void TryNativeFileNameToPFSFileName( const std::wstring& filename, std::wstring& NewFileName);
	void TryPFSFileNameToNativeFileName( const std::wstring& filename, std::wstring& NewFileName);
	void GetMetaInfosEx( const std::wstring& dir, const std::wstring& point, CPFSBase& pfs, CMetaInfoMap& mim);
	CPFSBase* FindPFSBaseInOriginal( std::wstring& filename);
	CPFSBase* FindPFSBaseUseFilter( std::wstring& filename, bool& bOldBaseUsed );
	CMountPoints::const_iterator FindDeepestBasePoint( std::wstring& path );
};

} // namespace PFS {