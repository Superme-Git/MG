#pragma once

#if defined _OS_IOS || defined _MSC_VER
#include <string>
#include <map>
#include "ljfmimage.h"
#endif

namespace LJFM
{
	typedef unsigned long long	fsize_t;
	typedef long long			foff_t;

enum FS_TYPE
{
	FST_NATIVE = 0,
	FST_ZIP,
	FST_BRIEF,
	FST_RUNZIP,
	FST_INETHTTP,
};


enum 
{
	FM_EXCL         = 0x00000001, 
	FM_CREAT        = 0x00000002, 
	FM_TRUNC        = 0x00000004, 
};
typedef unsigned int FILE_MODE;

enum FILE_ACCESS
{
	FA_RDONLY       = 0x00000001,	
	FA_WRONLY       = 0x00000002,	
	FA_RDWR         = 0x00000003,	
};

inline bool IsFileReadOnly( FILE_ACCESS fa ) 
{
	return (fa & FA_WRONLY) == 0;
}

inline bool IsFileWritable( FILE_ACCESS fa )
{
	return (fa & FA_WRONLY) != 0;
}

enum 
{
	LJFM_OPTION_GLOBAL = 0,		
	LJFM_OPTION_LOG = 1,			
	LJFM_OPTION_INTERNET = 2,	
	LJFM_OPTION_BASE_FILTER = 10,
	LJFM_OPTION_BASE_FILESMETAVERSION = 11,
};
typedef int LJFM_OPTION_GROUP;

enum
{
	EC_SUCCESS		= 0,
	EC_DIR_HAS_ALREADY_MOUNT = -1000,
	EC_INVALID_MOUNT_DEVICE,	
	EC_INVALID_DEVICE_NAME,		
	EC_INVALID_MOUNT_DIR_NAME,	
	EC_FSTYPE_UNEXISTING,		
	EC_MOUNT_DIR_UNFOUND,		
	EC_MOUNT_FAILED,			
	EC_FS_BUSY_NOW,				
	EC_FILE_NOT_MOUNT,			
	EC_FILE_NOT_FOUND,			
	EC_FS_READONLY,				
	EC_FILE_OPEN_MODE,			
	EC_NATIVE_DEVICE_LOST,		
	EC_COPY_DST_EXISTING,		
	EC_MAP_UNFOUND,				
	EC_FILE_BAD_ZIPDATA,		
	EC_FILE_READONLY,			
	EC_FILE_URLOPENERROR,		
	EC_INVALID_VERSION_INFO,	
	EC_INVALID_FILENAME,		
	EC_INVALID_DIRNAME,			
	EC_DIR_NOT_FOUND,			
	EC_DIR_ALREADY_EXISTING,	
	EC_DIR_NOT_EMPTY,			
	EC_ACCESS_DENIED,			
	EC_SHARING_VIOLATION,		
	EC_UNMOUNT_FAILED,			
	EC_INVALID_PATCH,			
	EC_PATCH_NOT_MATCH,			
	EC_FILE_NOTOPEN,			
	EC_FILE_WRITEONLY,			
	EC_INSUFFICIENT_BUFFER,		
	EC_FILESIZE_TOO_LARGE,		
	EC_INVALID_META_FILE,		
	EC_UNSUPPORT_META_FILE_VERSION,

	EC_FILTER_ERROR			= -1100,
	EC_FILTER_INVALID_ORI_DIR,		
	EC_FILTER_INVALID_NEW_DIR,		
	EC_FILTER_ITEM_NOT_EXISTS,		
	
	EC_OPTION_GENERAL_ERROR = -1200,
	EC_OPTION_INVALID_GROUP,		
	EC_OPTION_INVALID_NAME,			
	EC_OPTION_INVALID_VALUE,		
	EC_OPTION_OUT_OF_RANGE,			
	EC_OPTION_READ_ONLY,			
	EC_OPTION_SYSTEM_ERROR,			

	EC_DEVICE_INVALID_DEVNAME = -1300,
	EC_DEVICE_INVALID_MOUNTTYPE,	
	EC_DEVICE_INVALID_FSTYPE,		
	EC_DEVICE_MISSING_STORAGE,		
	EC_DEVICE_NOT_INIT,				
	EC_DEVICE_ALREADY_MOUNT,		
	EC_DEVICE_ALREADY_UNMOUNT,		
	EC_DEVICE_FILE_INVALID_PARAMETER,
	
	ECX_ENCODER_UNFOUND		= -2000,
	ECX_DECODER_UNFOUND,			
	ECX_BADFORMAT,					
 
#ifdef _OS_IOS
    ERROR_OUTOFMEMORY       = -3000,
    ERROR_INVALID_PARAMETER,        
#endif
};

#define MAX_MEMORY_DATA_SIZE  ((size_t)(-1))

enum { FILE_SIZE_ERROR = -1, };
enum FILE_SEEK_METHOD
{
	FSM_SET = 0,
	FSM_CUR,
	FSM_END,
};
  
class LJFMBF : public LJFMUO
{
	friend class LJFMOpen;
public:
	LJFMBF() {}
	virtual ~LJFMBF() {}
public:
	virtual size_t Read( void* data, size_t size) = 0;
	virtual size_t Write( const void* data, size_t size) = 0;
	virtual fsize_t GetSize() = 0;
	virtual fsize_t Seek( foff_t pos, FILE_SEEK_METHOD fsm) = 0;
	virtual fsize_t GetPos() = 0;
	virtual bool SetEnd() = 0;	
	virtual LJFMID GetImage() = 0;

	virtual bool ReadAll( void* data, fsize_t size ) = 0; 
protected:
	virtual void Delete() { delete this; }
};

LJFM::LJFMID BaseFileToImageData( LJFM::LJFMBF& file);
LJFM::LJFMID BaseFileToImageDataEx(LJFM::LJFMBF& file, fsize_t UnitOffset, fsize_t UnitSize);

typedef unsigned long long	FILE_DATA_HASH;
typedef unsigned long long	FILE_NAME_HASH;
typedef unsigned long long	FILELJFMXATTRIB;
typedef unsigned int		TSID;
typedef unsigned int		CRC32;

class LJFMOpen
{
	LJFMOpen() {}
	~LJFMOpen() {}
public:
	static int m_nLastError;
	static bool m_bLoadFromPak;
	static bool m_bVersionDonotCheck;
	
	static bool GetLoadFromPak()
	{
		return m_bLoadFromPak;
	}
	static void SetLoadFromPak(bool flag)
	{
		m_bLoadFromPak = flag;
	}

	static bool GetVersionDonotCheck()
	{
		return m_bVersionDonotCheck;
	}
	static void SetVersionDonotCheck(bool flag)
	{
		m_bVersionDonotCheck = flag;
	}

	static int InitFileList();
	static int UnInitFileList();
	static void* GetFileInfo(std::wstring wstrFileName);

	static std::wstring GetFullPathFileName(const std::wstring& filename);
	static LJFMBF* OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa);
	static bool CloseFile( LJFMBF* file);

	static bool IsFileExisting(const std::wstring& filename);

	static int GetLastError();
	static void SetLastError(int nErrorCode);

	static void SetOutLogInstance( COutLog* log);
	static COutLog* GetOutLogInstance();

};

}

namespace LJFMX {
	using LJFM::foff_t;
	using LJFM::fsize_t;
	using LJFM::TSID;
	using LJFM::CRC32;
}
