#pragma once

#include <Shlwapi.h>

namespace PFS {

class CTempFile : public PFSX::CNativeFile
{
	std::wstring		m_strfilename;

private:
	typedef BOOL (WINAPI *PGETDISKFREESPACEEX)(
		LPCWSTR, PULARGE_INTEGER, PULARGE_INTEGER, PULARGE_INTEGER);

	static BOOL SafeGetDiskFreeSpace(LPCWSTR pszDrive, unsigned __int64& i64FreeBytes )
	{
		PGETDISKFREESPACEEX pGetDiskFreeSpaceEx;

		BOOL fResult;
		pGetDiskFreeSpaceEx = (PGETDISKFREESPACEEX) GetProcAddress( 
			GetModuleHandleW(L"kernel32.dll"),
			"GetDiskFreeSpaceExW");

		if (pGetDiskFreeSpaceEx)
		{
			__int64 i64FreeBytesToCaller, i64TotalBytes;

			fResult = pGetDiskFreeSpaceEx (pszDrive,
				(PULARGE_INTEGER)&i64FreeBytesToCaller,
				(PULARGE_INTEGER)&i64TotalBytes,
				(PULARGE_INTEGER)&i64FreeBytes);

			// returns the results.
			return fResult;
		}
		else 
		{
			DWORD dwSectPerClust, dwBytesPerSect, 
				dwFreeClusters, dwTotalClusters;
			fResult = GetDiskFreeSpaceW (pszDrive, 
				&dwSectPerClust, 
				&dwBytesPerSect,
				&dwFreeClusters, 
				&dwTotalClusters);

			// returns the results.
			if(fResult) 
			{
				i64FreeBytes = (unsigned __int64)dwFreeClusters*dwSectPerClust*dwBytesPerSect;
			}
			return fResult;
		}
	}

	static std::wstring SelectMaxSpaceDisk( unsigned __int64* pMaxSpace )
	{
		// 选择最大磁盘空间目录
		DWORD mask = ::GetLogicalDrives();
		std::wstring drive = L"C:\\";
		DWORD bit = 4;
		unsigned __int64 space = 0;
		unsigned __int64 maxSpace = 0;
		std::wstring driveWithMaxSpace = drive;
		mask >>= 2;
		mask <<= 2;
		while( mask)
		{
			if( mask & bit)
			{
				if( DRIVE_FIXED == ::GetDriveTypeW( drive.c_str()))
				{
					space = 0;
					SafeGetDiskFreeSpace( drive.c_str(), space);
					if ( maxSpace < space )
					{
						maxSpace = space;
						driveWithMaxSpace = drive;
					}
				}
			}
			drive[0]++;
			mask &= ~bit;
			bit <<= 1;
		}

		if( pMaxSpace != NULL )
			*pMaxSpace = maxSpace;
		return driveWithMaxSpace;
	}

	static std::wstring SelectProperPath()
	{
		unsigned __int64 space = 0;
		WCHAR szTempPath[MAX_PATH+1] ={0};

		{// 选择系统缺省的临时目录
			GetTempPathW(MAX_PATH, szTempPath );

			unsigned __int64 space = 0;

			// MAXLONG is 2G
			// if free space is larger than 2G, we can use it!
			if ( SafeGetDiskFreeSpace( szTempPath, space ) && space > MAXLONG )
			{
				PathAddBackslashW( szTempPath );
				return szTempPath;
			}
		}

		{// 选择当前目录
			WCHAR szDirectory[MAX_PATH+1] ={0};
			GetCurrentDirectoryW( MAX_PATH, szDirectory );
			if ( !PathIsSameRootW( szTempPath, szDirectory) && 
				SafeGetDiskFreeSpace( szDirectory, space ) && space > MAXLONG )
			{
				PathAddBackslashW( szDirectory );
				return szDirectory;
			}
		}

		// 选择最大磁盘空间目录
		return SelectMaxSpaceDisk(NULL);
	}

	static int CreateValidTempPath( std::wstring& path)
	{
		std::wstring temppath = SelectProperPath();
		wchar_t suffix[] = L"pwrd";

		DWORD dwError = 0;
		int retryTimes = 0;
		while( retryTimes++ < 3)
		{
			path = temppath;
			path += suffix;
			suffix[0]++;

			if( !CreateDirectoryW( path.c_str(), NULL ) )
			{
				dwError = ::GetLastError();
				if( dwError == ERROR_ALREADY_EXISTS)
				{
					// 这里有可能是一个文件
					if( CFileIO::IsFileExisting( path))
					{
						continue;
					}
					return 0;
				}
				PFS::CEnv::GetOutLogInstance()->Print(
					L"ERROR: CTempFile::CreateValidTempPath(path:%s)(%d)\n", path.c_str(), dwError  );

				continue;
			}
			return 0;
		}
		return dwError;
	}

	static int MakeTempFileName( std::wstring& name, bool bChooseDisk)
	{
		static UINT	UniqueID = 0;
		static wchar_t prefix[4] = { L'p', L'f', L's', L'\0'};

		static std::wstring path;
		if ( !bChooseDisk && !path.empty() )
		{
			unsigned __int64 space = 0;
			SafeGetDiskFreeSpace( path.c_str(), space );
			if ( space < MAXLONG ) // Disk space is less than 2G, we should choose another one
			{
				bChooseDisk = true;
				path.clear();
			}
		}

		if( bChooseDisk || path.empty() )
		{
			// 强制要求重新选择磁盘或者第一次使用临时文件时才做
			DWORD dwError = CreateValidTempPath( path);
			if( dwError != 0)
				return dwError;
		}
		
		wchar_t	buffer[MAX_PATH] = {0};
		do 
		{
			UniqueID ++;
			if( UniqueID > 0xFFFF)		// GetTempFileName的UniqueID大于0xFFFF时生成的文件名会重复
			{
				UniqueID = 1;
				prefix[0]++;
			}

			if( GetTempFileNameW( path.c_str(), prefix, UniqueID, buffer) == 0)
				return ::GetLastError();
		} while ( PathFileExistsW( buffer ) ); // 保证临时文件为新文件

		name = buffer;
		return 0;
	}

public:
	CTempFile() {}
	virtual ~CTempFile()
	{
		if( !m_strfilename.empty() )
		{
			Close();
		}
	}
public:
	const std::wstring& GetFileName()
	{
		return m_strfilename;
	}

	virtual bool Open()
	{
		bool bNeedRetry;
		bool bChooseDisk = false;

		do
		{
			bNeedRetry = false;
			int error = MakeTempFileName( m_strfilename, bChooseDisk);
			if( error )
			{
				PFS::CEnv::SetLastError( error );
				return false;
			}

			bool bRet = PFSX::CNativeFile::Open( m_strfilename, FM_CREAT | FM_TRUNC, FA_RDWR);
			if( !bRet )
			{
				int lastError = CEnv::GetLastError();
				if( lastError == ERROR_DISK_FULL )
				{
					bChooseDisk = true;
					bNeedRetry = true;
				}
				else if( lastError == ERROR_SHARING_VIOLATION )
					bNeedRetry = true;
				else
					return false;
			}
		}while( bNeedRetry );

		return true;
	}

	virtual void Close()
	{
		PFSX::CNativeFile::Close();

#ifdef DeleteFile
#pragma push_macro( "DeleteFile" )
#undef DeleteFile
#endif // DeleteFile

		int error = CFileIO::DeleteFile( m_strfilename);
		if( error)
			PFS::CEnv::SetLastError( error);

#pragma pop_macro( "DeleteFile" )	

	}
};

} // namespace PFS {