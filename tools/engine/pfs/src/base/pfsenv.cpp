#include "../common.h"
#include "../../pfsenv.h"

namespace PFS
{
	PFS_STATIC_ASSERT(sizeof(fsize_t)==8);
	PFS_STATIC_ASSERT(sizeof(fpos_t)==8);
}

#include "packetfilesystem.h"

namespace PFS
{
namespace helper
{
	static class CDummyOutLog : public COutLog
	{
	public:
		CDummyOutLog() {}
		virtual ~CDummyOutLog() {}
	public:
		virtual void VPrint( const wchar_t* info, va_list vl) {}
	} g_DummyOutLog;

	COutLog*	g_OutLog = &g_DummyOutLog;

} // namespace helper
} // namespace PFS

namespace PFS {

	void CMetaInfo::DumpInfo( COutLog& outlog) const
	{
		outlog.Print( L"m_dataHash = 0x%016llx m_pathHash = 0x%016llx m_uri = '%s' m_sizeFile = %lld m_nMethod = %d m_attributes = %lld(0x%016llx) m_tsid = %d m_crc32 = %d(0x%08X) m_sizeFile2 = %lld m_dataHash2 = 0x%016llx", 
			m_dataHash, m_pathHash, m_uri.c_str(), m_sizeFile, m_nMethod, m_attributes, m_attributes, m_tsid, m_crc32, m_crc32, m_sizeFile2, m_dataHash2);
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

bool CEnv::NativeFileNameToPFSFileName( const std::wstring& in, std::wstring& out)
{
	CPacketFileSystem::GetInstance().NativeFileNameToPFSFileName( in, out);
	return EC_SUCCESS == GetLastError();
}

bool CEnv::PFSFileNameToNativeFileName( const std::wstring& in, std::wstring& out)
{
	if( !CheckDirFileStringFormat( in ) )
	{
		SetLastError( EC_INVALID_FILENAME );
		_PFS_ERROR( (L"不合法的文件名格式!" + in).c_str() );
		return false;
	}

	CPacketFileSystem::GetInstance().PFSFileNameToNativeFileName( in, out);
	return EC_SUCCESS == GetLastError();
}

bool CEnv::SetOption( PFS_OPTION_GROUP group, const std::wstring& name, const std::wstring& value )
{
	switch (group)
	{
	case PFS_OPTION_GLOBAL:
		if( name == L"base-filter" )
		{
			if( value == L"true" || value == L"1" )
			{
				return EnableBaseFilter(true);
			}
			else if( value == L"false" || value == L"0" )
			{
				return EnableBaseFilter(false);
			}
			else
			{
				SetLastError( EC_OPTION_INVALID_VALUE );
			}
		}
		else
		{
			SetLastError( EC_OPTION_INVALID_NAME );
		}
		return false;

	case PFS_OPTION_BASE_FILTER:
		return SetBaseFilter( name, value );

	case PFS_OPTION_BASE_FILESMETAVERSION:
		if( !CheckDirFileStringFormat( name ) )
		{
			SetLastError( EC_OPTION_INVALID_NAME );
			return false;
		}

		return CPacketFileSystem::GetInstance().SetBaseFilesMetaVersion( name, value);

	case PFS_OPTION_LOG:
	case PFS_OPTION_INTERNET:
		// TODO: 必要时扩展这些
		SetLastError( EC_OPTION_INVALID_NAME );
		return false;
	default:
		SetLastError( EC_OPTION_INVALID_GROUP );
		return false;
	}
}

bool CEnv::GetOption( PFS_OPTION_GROUP group, const std::wstring& name, std::wstring& value )
{
	switch (group)
	{
	case PFS_OPTION_GLOBAL:
		if( name == L"base-filter" )
		{
			if( IsBaseFilterEnabled() )
				value = L"true";
			else 
				value = L"false";
			return true;
		}
		else
		{
			SetLastError( EC_OPTION_INVALID_NAME );
		}
		return false;

	case PFS_OPTION_BASE_FILTER:
		return GetBaseFilter( name, value );
		
	case PFS_OPTION_BASE_FILESMETAVERSION:
		if( !CheckDirFileStringFormat( name ) )
		{
			SetLastError( EC_OPTION_INVALID_NAME );
			return false;
		}

		return CPacketFileSystem::GetInstance().GetBaseFilesMetaVersion( name, value);

	case PFS_OPTION_LOG:
	case PFS_OPTION_INTERNET:
		// TODO: 必要时扩展这些
		SetLastError( EC_OPTION_INVALID_NAME );
		return false;
	default:
		SetLastError( EC_OPTION_INVALID_GROUP );
		return false;
	}
}

// 开启或关闭BaseFilter。为了不影响效率，缺省情况下不开启BaseFilter，除非显式开启.
bool CEnv::EnableBaseFilter(bool bEnable)
{
	return CPacketFileSystem::GetInstance().EnableBaseFilter( bEnable );
}

bool CEnv::IsBaseFilterEnabled()
{
	return CPacketFileSystem::GetInstance().IsBaseFilterEnabled( );
}

// 设置/获取base目录的优先替换目录(过滤器方式)
// 这2个接口也可以通过SetOption/GetOption(PFS_OPTION_BASE_FILTER,baseDirOriginal,baseDirNew)接口进行操作.
// 但是，都必须通过EnableBaseFilter(true)开启或通过EnableBaseFilter(false)关闭.
bool CEnv::SetBaseFilter( const std::wstring& baseDirOriginal, const std::wstring& baseDirNew )
{
	if( !CheckDirFileStringFormat( baseDirOriginal ) )
	{
		SetLastError( EC_FILTER_INVALID_ORI_DIR );
		return false;
	}

	if( !CheckDirFileStringFormat( baseDirNew ) )
	{
		SetLastError( EC_FILTER_INVALID_NEW_DIR );
		return false;
	}

	return CPacketFileSystem::GetInstance().SetBaseFilter( baseDirOriginal, baseDirNew );
}

bool CEnv::GetBaseFilter( const std::wstring& baseDirOriginal, std::wstring& baseDirNew )
{
	if( !CheckDirFileStringFormat( baseDirOriginal ) )
	{
		SetLastError( EC_FILTER_INVALID_ORI_DIR );
		return false;
	}

	return CPacketFileSystem::GetInstance().GetBaseFilter( baseDirOriginal, baseDirNew );
}

bool CEnv::Mount( const std::wstring& dir, const std::wstring& device, FS_TYPE fst, MOUNT_TYPE mt)
{
	if( !CheckDirFileStringFormat( dir ) )
	{
		SetLastError( EC_INVALID_MOUNT_DIR_NAME );
		_PFS_ERROR( (L"不合法的目录格式!" + dir).c_str() );
		return false;
	}
	
	if( !CheckDeviceStringFormat( device) )
	{
		SetLastError( EC_INVALID_DEVICE_NAME  );
		_PFS_ERROR( (L"不合法的设备名!" + device).c_str() );
		return false;
	}

	int nError = CPacketFileSystem::GetInstance().Mount( dir, device, fst, mt);
	SetLastError( nError);
	return EC_SUCCESS == nError;
}

bool CEnv::Commit( const std::wstring& dir )
{
	if( !CheckDirFileStringFormat( dir ) )
	{
		SetLastError( EC_INVALID_MOUNT_DIR_NAME );
		_PFS_ERROR( (L"不合法的目录格式!" + dir).c_str() );
		return false;
	}

	int nError = CPacketFileSystem::GetInstance().Commit( dir );
	SetLastError( nError);
	return EC_SUCCESS == nError;
}

bool CEnv::Unmount( const std::wstring& dir, bool commit /*= true*/)
{
	if( !CheckDirFileStringFormat( dir ) )
	{
		SetLastError( EC_INVALID_MOUNT_DIR_NAME );
		_PFS_ERROR( (L"不合法的目录格式!" + dir).c_str() );
		return false;
	}

	int nError = CPacketFileSystem::GetInstance().Unmount( dir, commit);
	SetLastError( nError);
	return EC_SUCCESS == nError;
}

void CEnv::SetFileHashMethod( const CFileHashMethod& fhm)
{
	CFileMetaInfo::SetFileHashMethod( fhm);
}

const CFileHashMethod& CEnv::GetFileHashMethod()
{
	return *CFileMetaInfo::GetFileHashMethod();
}

void CEnv::SetDefaultFileHashMethod()
{
	CFileMetaInfo::SetDefautFileHashMethod();
}

bool CEnv::GetMetaInfos( const std::wstring& dir, CMetaInfoMap& mim)
{
	if( !CheckDirFileStringFormat( dir ) )
	{
		SetLastError( EC_INVALID_MOUNT_DIR_NAME );
		_PFS_ERROR( (L"不合法的目录格式!" + dir).c_str() );
		return false;
	}
	
	mim.clear();
	int nError = CPacketFileSystem::GetInstance().GetMetaInfos( dir, mim);
	SetLastError( nError);
	return EC_SUCCESS == nError;
}

bool CEnv::SetMetaInfos( const std::wstring& dir, CMetaInfoMap& mim)
{
	if( !CheckDirFileStringFormat( dir ) )
	{
		SetLastError( EC_INVALID_MOUNT_DIR_NAME );
		_PFS_ERROR( (L"不合法的目录格式!" + dir).c_str() );
		return false;
	}

	int nError = CPacketFileSystem::GetInstance().SetMetaInfos( dir, mim);
	SetLastError( nError);
	return EC_SUCCESS == nError;
}

bool CEnv::GetMetaInfosEx( const std::wstring& dir, CMetaInfoMap& mim)
{
	if( !CheckDirFileStringFormat( dir ) )
	{
		SetLastError( EC_INVALID_MOUNT_DIR_NAME );
		_PFS_ERROR( (L"不合法的目录格式!" + dir).c_str() );
		return false;
	}

	mim.clear();
	return CPacketFileSystem::GetInstance().GetMetaInfosEx( dir, mim);
}

bool CEnv::CheckMetaInfo( const std::wstring& dir)
{
	if( !CheckDirFileStringFormat( dir ) )
	{
		SetLastError( EC_INVALID_MOUNT_DIR_NAME );
		_PFS_ERROR( (L"不合法的目录格式!" + dir).c_str() );
		return false;
	}

	int nError = CPacketFileSystem::GetInstance().CheckMetaInfo( dir);
	SetLastError( nError);
	return EC_SUCCESS == nError;
}

bool CEnv::GetFileMetaInfo( const std::wstring& filename, CMetaInfo& info)
{
	// filenameNew将变为filename的全小写
	std::wstring filenameNew;
	if( !CheckDirFileStringFormatEx( filename, filenameNew) )
	{
		SetLastError( EC_INVALID_FILENAME );
		_PFS_ERROR( (L"不合法的文件名格式!" + filename).c_str() );
		return false;
	}

	int nError = CPacketFileSystem::GetInstance().GetFileMetaInfo( filenameNew, info);
	SetLastError( nError);
	return EC_SUCCESS == nError;
}

bool CEnv::SetFileMetaInfo( const std::wstring& filename, const CMetaInfo& info)
{
	// filenameNew将变为filename的全小写
	std::wstring filenameNew;
	if( !CheckDirFileStringFormatEx( filename, filenameNew) )
	{
		SetLastError( EC_INVALID_FILENAME );
		_PFS_ERROR( (L"不合法的文件名格式!" + filename).c_str() );
		return false;
	}

	int nError = CPacketFileSystem::GetInstance().SetFileMetaInfo( filenameNew, info);
	SetLastError( nError);
	return EC_SUCCESS == nError;
}

bool CEnv::AddFileMetaInfo( const std::wstring& filename, const CMetaInfo& info)
{
	// filenameNew将变为filename的全小写
	std::wstring filenameNew;
	if( !CheckDirFileStringFormatEx( filename, filenameNew) )
	{
		SetLastError( EC_INVALID_FILENAME );
		_PFS_ERROR( (L"不合法的文件名格式!" + filename).c_str() );
		return false;
	}

	int nError = CPacketFileSystem::GetInstance().AddFileMetaInfo( filenameNew, info);
	SetLastError( nError);
	return EC_SUCCESS == nError;
}

bool CEnv::IsFileExisting( const std::wstring& filename)
{
	std::wstring filenameNew;
	if( !CheckDirFileStringFormatEx( filename, filenameNew) )
	{
		SetLastError( EC_INVALID_FILENAME );
		_PFS_ERROR( (L"不合法的文件名格式!" + filename).c_str() );
		return false;
	}

	return CPacketFileSystem::GetInstance().IsFileExisting( filenameNew);
}

CBaseFile* CEnv::OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa)
{
	// filenameNew将变为filename的全小写
	std::wstring filenameNew;
	if( !CheckDirFileStringFormatEx( filename, filenameNew) )
	{
		SetLastError( EC_INVALID_FILENAME );
		_PFS_ERROR( (L"不合法的文件名格式!" + filename).c_str() );
		return NULL;
	}

	CBaseFile*	file = NULL;
	int nError = CPacketFileSystem::GetInstance().Open( filenameNew, fm, fa, file);
	SetLastError( nError);
	if( EC_SUCCESS == nError)
		return file;
	
	return NULL;
}

bool CEnv::CloseFile( CBaseFile* file)
{
	if( file == NULL )
	{
		assert( file != NULL );
		return false;
	}

	file->Delete();
	return true;
}

bool CEnv::RemoveFile( const std::wstring& filename)
{
	// filenameNew将变为filename的全小写
	std::wstring filenameNew;
	if( !CheckDirFileStringFormatEx( filename, filenameNew) )
	{
		SetLastError( EC_INVALID_FILENAME );
		_PFS_ERROR( (L"不合法的文件名格式!" + filename).c_str() );
		return NULL;
	}

	int nError = CPacketFileSystem::GetInstance().RemoveFile( filenameNew);
	SetLastError( nError);
	return EC_SUCCESS == nError;
}

bool CEnv::FileCopy( const std::wstring& src, const std::wstring& dst, bool failifexsting)
{
	if( failifexsting && CEnv::IsFileExisting( dst) )
	{
		SetLastError( EC_COPY_DST_EXISTING);
		return false;
	}

	CBaseFile*	fsrc = NULL;
	CBaseFile*	fdst = NULL;

	const int	nBufferSize = 40960;
	char		szBuffer[nBufferSize];
	bool		bResult = true;

	class CCopyExecption {};
	try
	{
		fsrc = OpenFile( src, FM_EXCL, FA_RDONLY);
		if( NULL == fsrc)
			throw CCopyExecption();
		fdst = OpenFile( dst, FM_CREAT | FM_TRUNC, FA_WRONLY);
		if( NULL == fdst)
			throw CCopyExecption();

		// 输入文件大小
		fsize_t srcSize = fsrc->GetSize();

		while( size_t s = fsrc->Read( szBuffer, nBufferSize))
		{
			if( FILE_SIZE_ERROR == s)
				throw CCopyExecption();
			s = fdst->Write( szBuffer, s);
			if( FILE_SIZE_ERROR == s)
				throw CCopyExecption();
		}

		// 比较Copy的数据大小。
		// 对于网络文件，这可能是有必要的
		if ( srcSize != fdst->GetSize() )
		{
			// 数据不完整
			SetLastError( ECX_BADFORMAT );
			throw CCopyExecption();
		}
	}
	catch( CCopyExecption&)
	{
		bResult = false;
	}
	if( fsrc)
		CloseFile( fsrc);
	if( fdst)
		CloseFile( fdst);

	if( bResult)
	{
		CMetaInfo	info;
		bool b = GetFileMetaInfo( src, info);
		if( b )
		{
			AddFileMetaInfo( dst, info);
		}
	}
	return bResult;
}

int CEnv::GetLastError()
{
	return CPacketFileSystem::GetInstance().GetLastError();
}

void CEnv::SetLastError(int nErrorCode)
{
	return CPacketFileSystem::GetInstance().SetLastError(nErrorCode);
}

void CEnv::SetOutLogInstance( COutLog* log)
{
	helper::g_OutLog = log;
	if( NULL == helper::g_OutLog)
		helper::g_OutLog = &helper::g_DummyOutLog;
}

COutLog* CEnv::GetOutLogInstance()
{
	return helper::g_OutLog;
}

// 生成指定字符串的hash
FILE_DATA_HASH CEnv::MakeStringHash( const std::wstring& str)
{
	helper::CMD5Hash hashMaker;
	hashMaker.Update( str.c_str(), str.size()*sizeof(wchar_t));
	FILE_DATA_HASH hash;
	hashMaker.Final( hash);
	return hash;
}

// 刷新目录 dir 的 files.meta 文件
bool CEnv::RefreshMetaFile( const std::wstring& dir)
{
	if( !CheckDirFileStringFormat( dir ) )
	{
		SetLastError( EC_INVALID_MOUNT_DIR_NAME );
		_PFS_ERROR( (L"不合法的目录格式!" + dir).c_str() );
		return false;
	}

	return CPacketFileSystem::GetInstance().RefreshMetaFile( dir);
}

// 得到目录 dir 的 files.meta 文件的hash值
bool CEnv::GetMetaFileHash( const std::wstring& dir, FILE_DATA_HASH& res)
{
	if( !CheckDirFileStringFormat( dir ) )
	{
		SetLastError( EC_INVALID_MOUNT_DIR_NAME );
		_PFS_ERROR( (L"不合法的目录格式!" + dir).c_str() );
		return false;
	}

	return CPacketFileSystem::GetInstance().GetMetaFileHash( dir, res);
}

void CEnv::DumpInfos( COutLog& outlog)
{
	CPacketFileSystem::GetInstance().DumpInfos( outlog);
}

} // namespace PFS{