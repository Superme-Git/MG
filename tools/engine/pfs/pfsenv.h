#pragma once

namespace PFS
{

	typedef unsigned long long	fsize_t; // 64bit���ļ���С���ļ�ָ��λ��
	typedef long long			foff_t;	 // 64bit���ļ�ָ�����ƫ����

enum FS_TYPE
{
	FST_NATIVE = 0,
	FST_ZIP,
	FST_BRIEF,
	FST_RUNZIP,
	FST_INETHTTP,  // ��ʵ�֣�ֱ�ӹҽ�http url��device��
};

enum
{
	MT_READONLY		= 0,			// ֻ��
	MT_WRITABLE		= 0x00000001,	// ��д
	MT_RUNTIME		= 0x00000002,	// ����ʱϵͳ��ֻ����������metainfo
	MT_CACHEDATA	= 0x00000004,	// �����ļ����ݣ���Ҫ����RUNZIP+RUNTIME����µ��������ͣ�
	MT_ZIPUPDATE	= 0x00000009,	// update������Ϊzip���ģ���Ҫ����pfs�������ã���MT_RUNTIME���⣩������MT_WRITABLE��
	MT_OPTIONAL		= 0x00000010,	// ���豸���п��ޣ�û��ʱ���Դ��豸��(��ǰֻ����MountManager)
	MT_TRUNCATE		= 0x00001001,	// ���ײ���ļ�ϵͳ��mountʱ��գ�����zip��ϵͳ�����zip���ļ�������Ŀ¼�����Ŀ¼�������ļ�������MT_WRITABLE��
	MT_PREFERDIR	= 0x00002001,	// ����ײ��ļ�ϵͳ�����ڣ�CDeviceǿ�ƴ���Directoryϵͳ�����Ǵ���PFS��ϵͳ������MT_WRITABLE��
	MT_FSTYPEFLAG	= 0x80000000,   // ���λΪ1,��ʽ�趨��FS_TYPE���ͣ����Ϊ0�����Զ�ʶ��
	MT_FSTYPEMASK	= 0x7F000000,   // ��MT_FSTYPEFLAG��1ʱ���θ�7λΪ��ʽ�趨��FS_TYPE���ͣ�MT_FSTYPEFLAGΪ0ʱ��������FS_TYPE���͡�
};
typedef unsigned int MOUNT_TYPE;

inline bool IsMountReadOnly( MOUNT_TYPE mt )
{
	return (mt & MT_WRITABLE) == 0;
}

inline bool IsMountCacheData( MOUNT_TYPE mt )
{
	return (mt & MT_CACHEDATA) != 0;
}

// �Ƿ���.files.meta(CMetaInfoMap)��Ϣ
inline bool IsMountRuntime( MOUNT_TYPE mt )
{
	return (mt & MT_RUNTIME ) != 0;
}

inline bool IsMountZipUpdate( MOUNT_TYPE mt )
{
	return (mt & MT_ZIPUPDATE) == MT_ZIPUPDATE;
}

inline bool IsMountOptional( MOUNT_TYPE mt )
{
	return (mt & MT_OPTIONAL) != 0;
}

inline bool IsMountTruncate( MOUNT_TYPE mt )
{
	return ( mt & MT_TRUNCATE ) == MT_TRUNCATE;
}

inline bool IsMountPreferDir( MOUNT_TYPE mt )
{
	return (mt & MT_PREFERDIR ) == MT_PREFERDIR;
}

// Mount�Ƿ�ָ����FS_TYPE
inline bool IsMountSetFSType( MOUNT_TYPE mt )
{
	return (mt & MT_FSTYPEFLAG ) == MT_FSTYPEFLAG;
}

// ���IsMountSetFSType(mt)Ϊtrue, ��Mount�����л�ȡFS_TYPE
inline FS_TYPE FSTypeFromMountType( MOUNT_TYPE mt )
{
	return (FS_TYPE)((mt & MT_FSTYPEMASK ) >> 24);
}

// �������FSType��MountType
inline MOUNT_TYPE MakeMountTypeWithFSType( MOUNT_TYPE mt, FS_TYPE fst )
{
	return mt | MT_FSTYPEFLAG | (MOUNT_TYPE)fst << 24 ;
}

enum 
{
	FM_EXCL         = 0x00000001,  /*! �ļ�������� */
	FM_CREAT        = 0x00000002,  /*! �����ļ� */
	FM_TRUNC        = 0x00000004,  /*! ����ļ����� */
};
typedef unsigned int FILE_MODE;

enum FILE_ACCESS
{
	FA_RDONLY       = 0x00000001,	/*! open for reading only */
	FA_WRONLY       = 0x00000002,	/*! open for writing only */
	FA_RDWR         = 0x00000003,	/*! open for reading and writing */
};

// �ļ��Ƿ���ֻ����ʽ��
inline bool IsFileReadOnly( FILE_ACCESS fa ) 
{
	return (fa & FA_WRONLY) == 0;
}

// �ļ��Ƿ��Կ�д��ʽ��
inline bool IsFileWritable( FILE_ACCESS fa )
{
	return (fa & FA_WRONLY) != 0;
}

// 
// enumeration of pfs option groups
//
enum 
{
	PFS_OPTION_GLOBAL = 0,		// global properties that can be identified by property name.
	PFS_OPTION_LOG = 1,			// log properties(such as log level, log file... reserved)
	PFS_OPTION_INTERNET = 2,	// internet properties( such as connect timeout, receive timeout, send timeout. reserved)
	PFS_OPTION_BASE_FILTER = 10,// ����ͨ��global���� base-filter �������߹ر�
								// pfs base path filter( name is the base to be replaced, value is the base to replace)
	PFS_OPTION_BASE_FILESMETAVERSION = 11,// Set the version of a pfs base .files.meta file. 
	// The name must be a base root path, and the value is the version string like 1.0, 1.1, 2.0...
	// ע�⣺���ܵݹ�mount native�豸ʱ��Ҳ�������ô�ѡ����ǣ���ǰPFS��֧��Ϊ����ϵͳ�������ɲ�ͬ�汾�ĵ�filesmetaversion��
};
typedef int PFS_OPTION_GROUP;

enum
{
	EC_SUCCESS		= 0,
	EC_DIR_HAS_ALREADY_MOUNT = -1000,	// Ŀ¼�Ѿ�ӳ�䵽��һ���豸
	EC_INVALID_MOUNT_DEVICE,	// ��Ч��Mount�豸
	EC_INVALID_DEVICE_NAME,		// ��Ч��Mount�豸��
	EC_INVALID_MOUNT_DIR_NAME,	// ��Ч��MountĿ��Ŀ¼��
	EC_FSTYPE_UNEXISTING,		// �ļ�ϵͳ���Ͳ�����
	EC_MOUNT_DIR_UNFOUND,		// �޷��ҵ��ñ�ӳ���Ŀ¼
	EC_MOUNT_FAILED,			// Mount �ļ�ϵͳʧ��
	EC_FS_BUSY_NOW,				// �ļ�ϵͳ���ڱ�ʹ��
	EC_FILE_NOT_MOUNT,			// û���ҵ���Ӧ��ӳ��Ŀ¼
	EC_FILE_NOT_FOUND,			// û���ҵ��ļ�
	EC_FS_READONLY,				// �ļ�ϵͳĿǰֻ��
	EC_FILE_OPEN_MODE,			// �ļ��򿪷�ʽ��֧��
	EC_NATIVE_DEVICE_LOST,		// �޷��ҵ�ԭʼ�ļ��豸
	EC_COPY_DST_EXISTING,		// �ļ�������Ŀ���Ѿ�����
	EC_MAP_UNFOUND,				// �ļ����Ҳ���ӳ��
	EC_FILE_BAD_ZIPDATA,		// zipѹ�������ݽ�ѹ��ʧ��
	EC_FILE_READONLY,			// ��ǰ�ļ�ֻ��
	EC_FILE_URLOPENERROR,		// url�򿪴���
	EC_INVALID_VERSION_INFO,	// ��Ч�İ汾��Ϣ
	EC_INVALID_FILENAME,		// ��Ч���ļ���
	EC_INVALID_DIRNAME,			// ��Ч��Ŀ¼��
	EC_DIR_NOT_FOUND,			// Ŀ¼������(�޷�ɾ��)
	EC_DIR_ALREADY_EXISTING,	// Ŀ¼�Ѵ���(�����ٴδ���)
	EC_DIR_NOT_EMPTY,			// (������ɾ��)�ǿ�Ŀ¼
	EC_ACCESS_DENIED,			// ���ʱ��ܾ�(Ȩ�޲���)
	EC_SHARING_VIOLATION,		// ����Υ��(����ɾ��һ���Ѵ򿪵��ļ�)
	EC_UNMOUNT_FAILED,			// UnMountʧ��
	EC_INVALID_PATCH,			// ��Ч�Ĳ�����
	EC_PATCH_NOT_MATCH,			// �������汾��ƥ�䱾�ؿͻ���
	EC_FILE_NOTOPEN,			// �ļ���δ��
	EC_FILE_WRITEONLY,			// �ļ�ֻ֧��д����
	EC_INSUFFICIENT_BUFFER,		// ���������������������������
	EC_FILESIZE_TOO_LARGE,		// �ļ���С�����ڴ�����������ֵ,�޷���ɴ˲���.
	EC_INVALID_META_FILE,		// ���Ϸ���MetaFile�ļ���ʽ
	EC_UNSUPPORT_META_FILE_VERSION,	// ��Ч��MetaFile�汾��MetaFile�汾̫��

	EC_FILTER_ERROR			= -1100,// һ�����������
	EC_FILTER_INVALID_ORI_DIR,		// ��Ч�Ĺ�����ԭĿ¼
	EC_FILTER_INVALID_NEW_DIR,		// ��Ч�Ĺ�������Ŀ¼
	EC_FILTER_ITEM_NOT_EXISTS,		// �����ڴ˹�����������Ŀ
	
	EC_OPTION_GENERAL_ERROR = -1200,// һ�����Դ���
	EC_OPTION_INVALID_GROUP,		// ��Ч��������
	EC_OPTION_INVALID_NAME,			// ��Ч��������
	EC_OPTION_INVALID_VALUE,		// ��Ч������ֵ
	EC_OPTION_OUT_OF_RANGE,			// ����ֵ����������ķ�Χ
	EC_OPTION_READ_ONLY,			// ����ֻ������֧���޸Ĵ����ԣ�
	EC_OPTION_SYSTEM_ERROR,			// �����޸�/��ȡʱ����ϵͳ����

	EC_DEVICE_INVALID_DEVNAME = -1300, // ��ЧDevice����
	EC_DEVICE_INVALID_MOUNTTYPE,	// ��Ч��MOUNT����
	EC_DEVICE_INVALID_FSTYPE,		// ��Ч���ļ�ϵͳ
	EC_DEVICE_MISSING_STORAGE,		// ֻ��MOUNTʱ��û�д洢�豸
	EC_DEVICE_NOT_INIT,				// �豸��δ��ʼ��
	EC_DEVICE_ALREADY_MOUNT,		// �豸�Ѿ����ҽ�
	EC_DEVICE_ALREADY_UNMOUNT,		// �豸�Ѿ���ж��
	EC_DEVICE_FILE_INVALID_PARAMETER, // ��Ч��DEVICE FILE������
	
	ECX_ENCODER_UNFOUND		= -2000,// �ļ�������û���ҵ�
	ECX_DECODER_UNFOUND,			// �ļ�������û���ҵ�
	ECX_BADFORMAT,					// ������ļ���ʽ
};

#define MAX_MEMORY_DATA_SIZE  ((size_t)(-1)) // �ڴ��е���������

enum { FILE_SIZE_ERROR = -1, };
enum FILE_SEEK_METHOD
{
	FSM_SET = 0,
	FSM_CUR,
	FSM_END,
};

class CBaseFile : public CUncopyableObject
{
	friend class CEnv;
public:
	CBaseFile() {}
	virtual ~CBaseFile() {}
public:
	virtual size_t Read( void* data, size_t size) = 0;
	virtual size_t Write( const void* data, size_t size) = 0;
	virtual fsize_t GetSize() = 0;
	virtual fsize_t Seek( foff_t pos, FILE_SEEK_METHOD fsm) = 0;
	virtual fsize_t GetPos() = 0;
	virtual bool SetEnd() = 0;	
	virtual CImageData GetImage() = 0;
	//
	// ������data���������ܹ�����ȫ���ļ�����,���򷵻�false, CEnv::GetLastError()����EC_INSUFFICIENT_BUFFER.
	// ���ܸú����ĵڶ�����������Ϊfsize_t, �����ڳ���size_t(-1)���ļ�(4G),�ú������Ƿ���false, 
	// ��CEnv::GetLastError()����EC_FILESIZE_TOO_LARGE.
	//
	virtual bool ReadAll( void* data, fsize_t size ) = 0; 
protected:
	virtual void Delete() { delete this; }
};

PFS::CImageData BaseFileToImageData( PFS::CBaseFile& file); //	��ȡ�ļ����ݵ�CDataBlock������ת����CImageData

typedef unsigned long long	FILE_DATA_HASH;	// �ļ�����Hashֵ
typedef unsigned long long	FILE_NAME_HASH;	// �ļ�·��Hashֵ
typedef unsigned long long	FILEPFSXATTRIB;
typedef unsigned int		TSID;
typedef unsigned int		CRC32;

class CMetaInfo
{
	friend class CMarshalMetaInfo;
	friend class CMarshalMetaInfo_1_1;

	FILE_DATA_HASH			m_dataHash;		// �ļ�����Hashֵ
	FILE_NAME_HASH			m_pathHash;		// δ���л����ļ�
	std::wstring			m_uri;			// δ���л����ļ�
	fsize_t					m_sizeFile;		// �Ա䳤�������л����ļ�
	int						m_nMethod;		// zip level(��unsigned char��ʽ���л����ļ�)
	union 
	{
		FILEPFSXATTRIB		m_attributes;	// for pfsx use
		struct 
		{
			TSID			m_tsid;
			CRC32			m_crc32;
		};
	};

	// 
	// 1.1 �汾����
	// 
	// ���� tsidΪ X2O �� 0 ���ļ���˵�������2���ֶ�ֵ��ͬ��m_dataHash��m_sizeFile
	// ��������£������л����ļ���,�Խ�ʡ�ļ��ռ�.
	//
	fsize_t					m_sizeFile2;	// �任��ʽ����ļ���С���Ա䳤�������л����ļ�
	FILE_DATA_HASH			m_dataHash2;	// �任��ʽ����ļ�����Hashֵ(ѹ��)

public:
	CMetaInfo() 
		: m_dataHash(0)
		, m_pathHash(0)
		, m_sizeFile(0)
		, m_nMethod(0)
		, m_attributes(0)
		, m_sizeFile2(0)
		, m_dataHash2(0)
	{}

	CMetaInfo( const CMetaInfo& src) 
		: m_dataHash( src.m_dataHash)
		, m_pathHash( src.m_pathHash)
		, m_uri( src.m_uri)
		, m_sizeFile( src.m_sizeFile)
		, m_nMethod( src.m_nMethod)
		, m_attributes( src.m_attributes)
		, m_sizeFile2( src.m_sizeFile2)
		, m_dataHash2( src.m_dataHash2)
	{}

	~CMetaInfo() 
	{}
public:
	CMetaInfo& operator=( const CMetaInfo& src)
	{
		if( this == &src) 
			return *this;
		m_dataHash = src.m_dataHash;
		m_dataHash2 = src.m_dataHash2;
		m_pathHash = src.m_pathHash;
		m_uri = src.m_uri;
		m_sizeFile = src.m_sizeFile;
		m_sizeFile2 = src.m_sizeFile2;
		m_nMethod = src.m_nMethod;
		m_attributes = src.m_attributes;
		return *this;
	}
	bool operator != ( const CMetaInfo& right)
	{
		return (m_dataHash != right.m_dataHash) 
			|| (m_pathHash != right.m_pathHash) 
			|| (m_sizeFile != right.m_sizeFile) 
			|| (m_attributes != right.m_attributes)
			|| (m_nMethod != right.m_nMethod) 
			|| (m_uri != right.m_uri)
			|| (m_dataHash2 != right.m_dataHash2) 
			|| (m_sizeFile2 != right.m_sizeFile2) 
			;
	}

	const FILE_DATA_HASH& GetFileHash() const { return m_dataHash; }
	void SetFileHash( const FILE_DATA_HASH& dataHash) { m_dataHash = dataHash; }

	const FILE_DATA_HASH& GetFileHash2() const { return m_dataHash2; }
	void SetFileHash2( const FILE_DATA_HASH& dataHash2) { m_dataHash2 = dataHash2; }

	const FILE_NAME_HASH& GetFilePathHash() const { return m_pathHash; }
	void SetFilePathHash( const FILE_NAME_HASH& pathHash) { m_pathHash = pathHash; }

	const std::wstring& GetFileDownloadURI() const { return m_uri; }
	void SetFileDownloadURI( const std::wstring& strURI) { m_uri = strURI; }

	FILEPFSXATTRIB GetFilePfsxAttributes() const { return m_attributes; }
	void SetFilePfsxAttributes( FILEPFSXATTRIB attr) { m_attributes = attr; }

	TSID GetTSID() const { return m_tsid; }
	void SetTSID( TSID tsid) { m_tsid = tsid; }

	CRC32 GetCRC32() const { return m_crc32; }
	void SetCRC32( CRC32 _crc32) { m_crc32 = _crc32; }

	fsize_t GetFileSize() const { return m_sizeFile; }
	void SetFileSize( fsize_t size) { m_sizeFile = size; }

	fsize_t GetFileSize2() const { return m_sizeFile2; }
	void SetFileSize2( fsize_t size2) { m_sizeFile2 = size2; }

	int GetMethod() const { return m_nMethod; }
	void SetMethod( int nMethod) { m_nMethod = nMethod; }

	// �ж��Ƿ���V10�汾��MetaInfo
	bool IsV10() const { return m_sizeFile2 == 0 && m_dataHash2 == 0; }

	void DumpInfo( COutLog& outlog) const;
};

typedef std::map<std::wstring,CMetaInfo> CMetaInfoMap;

class CFileHashMethod  : public CUncopyableObject
{
public:
	CFileHashMethod() {}
	virtual ~CFileHashMethod() {}
public:
	virtual CFileHashMethod* Clone() const = 0;
	virtual void Delete() { delete this; }
public:
	virtual bool MakeFileHash( CBaseFile& file, FILE_DATA_HASH& digest) const = 0;
};

class CEnv
{
	CEnv() {}
	~CEnv() {}
public:
	static int GetLastError();

	// ˵��: ���������ṩ��SetLastError����������Ӧ�ý�����PFS/PFSX���ƿռ�
	// �����ӿռ��ʵ�ִ����У�����Ҫ����ʹ��PFS��Ӧ�ó�����
	static void SetLastError(int nErrorCode);

	//
	// PFS ϵͳ���Խӿ�
	//
	// SetOption: ����PFSϵͳ����
	// GetOption: ��ȡPFSϵͳ����
	// ����:
	//	group - ������ID���μ�PFS_OPTION_GROUP����
	//	name - ָ�����µ����Ա�־
	//  value - ָ�����¶�Ӧ�����Ե�����ֵ
	//
	// ����ֵ:
	//	����SetOption,���óɹ�ʱ����true������ֻ�����ԣ�ִ��SetOption���������ǻ᷵��false��PFS LastError������EC_OPTION_READ_ONLY.
	//  ����GetOption,��ȡ�ɹ�ʱ����true. ���ڲ����ڵ����ԣ����Ƿ���false��PFS LastError������EC_OPTION_INVALID_GROUP��EC_OPTION_INVALID_NAME.
	static bool SetOption( PFS_OPTION_GROUP group, const std::wstring& name, const std::wstring& value );
	static bool GetOption( PFS_OPTION_GROUP group, const std::wstring& name, std::wstring& value );

	// ������ر�BaseFilter��
	// Ϊ�˲�Ӱ��Ч�ʣ�ȱʡ����²�����BaseFilter��������ʽ����.
	// Ҳ����ͨ��global���� base-filter �������߹ر�
	// ������رճɹ�����true��������ر�ʧ�ܷ���false��
	static bool EnableBaseFilter(bool bEnable);

	// ��ȡBaseFilter��������״̬
	// BaseFilter��������true��û�п�������false��
	static bool IsBaseFilterEnabled();

	// ����/��ȡbaseĿ¼�������滻Ŀ¼(��������ʽ)
	// ��2���ӿ�Ҳ����ͨ��SetOption/GetOption(PFS_OPTION_BASE_FILTER,baseDirOriginal,baseDirNew)�ӿڽ��в�����
	// ���ǣ�������ͨ��EnableBaseFilter(true)������ͨ��EnableBaseFilter(false)�رա�
	// filter������ʱ��GetBaseFilter()����false��GetLastError()���� EC_FILTER_ITEM_NOT_EXISTS��
	static bool SetBaseFilter( const std::wstring& baseDirOriginal, const std::wstring& baseDirNew );
	static bool GetBaseFilter( const std::wstring& baseDirOriginal, std::wstring& baseDirNew );

	//
	// ����PFS��ϵͳ
	//
	static bool Mount( const std::wstring& dir, const std::wstring& device, FS_TYPE fst, MOUNT_TYPE mt = MT_READONLY);

	// �ύ�����ļ�ϵͳ���޸Ĳ���������unmount�豸.
	// ��Կ�д��device��ˢ��Mount�豸��
	//	1. ����native fs��˵��ˢ��metafile
	//	2. ����zip fs ��˵�������豸����ɾ����ʱ�ļ���ˢ��metafile
	// ����ֻ�����ļ�ϵͳ���ò��������壬���Ƿ���true��
	static bool Commit( const std::wstring& dir );

	// ж��PFS��ϵͳ
	// commit = true����Unmount�����У�ִ��Commit�������ύ�ļ�ϵͳ������޸�.
	// commit = false, ����� FST_ZIP ���ͽ������������������Rollback���ع������޸ģ�.������FST����������.
	static bool Unmount( const std::wstring& dir, bool commit = true);

	// ˢ��Ŀ¼ dir �� files.meta �ļ�
	static bool RefreshMetaFile( const std::wstring& dir);
	// �õ�Ŀ¼ dir �� files.meta �ļ���hashֵ
	static bool GetMetaFileHash( const std::wstring& dir, FILE_DATA_HASH& res);

	static bool GetMetaInfos( const std::wstring& dir, CMetaInfoMap& mim);

	// ˵��: ���������ṩ��SetMetaInfos����������Ӧ�ý���������Ӧ�ó����Խ��UI����������⡣
	// ���⣬Ϊ�����Ч�ʣ������mim����Ϊ��const���ã��ڲ�����swap��ʽ�滻mim�����ݡ�
	// ��ˣ��˺������óɹ�֮��mim��ֵ���ᷢ���仯��
	// �μ�BriefFS��RUNTIMEģʽ��PFS���׸���ϵͳ��ʵ�ַ�����
	static bool SetMetaInfos( const std::wstring& dir, CMetaInfoMap& mim);

	static bool GetMetaInfosEx( const std::wstring& dir, CMetaInfoMap& mim);
	static bool CheckMetaInfo( const std::wstring& dir);
	static bool GetFileMetaInfo( const std::wstring& filename, CMetaInfo& info);
	static bool SetFileMetaInfo( const std::wstring& filename, const CMetaInfo& info);
	static bool AddFileMetaInfo( const std::wstring& filename, const CMetaInfo& info);
	static bool IsFileExisting( const std::wstring& filename);

	static void SetFileHashMethod( const CFileHashMethod& fhm);
	static const CFileHashMethod& GetFileHashMethod();
	static void SetDefaultFileHashMethod();

	static CBaseFile* OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa);
	static bool CloseFile( CBaseFile* file);
	static bool RemoveFile( const std::wstring& filename);
	static bool FileCopy( const std::wstring& src, const std::wstring& dst, bool failifexsting = true);

	static bool NativeFileNameToPFSFileName( const std::wstring& in, std::wstring& out); // if failed,GetLastError = EC_MAP_UNFOUND
	static bool PFSFileNameToNativeFileName( const std::wstring& in, std::wstring& out); // if failed,GetLastError = EC_MAP_UNFOUND

	static void SetOutLogInstance( COutLog* log);
	static COutLog* GetOutLogInstance();

	// ����ָ���ַ�����hash
	static FILE_DATA_HASH MakeStringHash( const std::wstring& str);

	static void DumpInfos( COutLog& outlog);
};

} // namespace PFS{

namespace PFSX {
	using PFS::foff_t;
	using PFS::fsize_t;
	using PFS::TSID;
	using PFS::CRC32;
} // namespace PFSX {
