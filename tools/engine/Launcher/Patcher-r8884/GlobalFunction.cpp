#include "StdAfx.h"
#include "GlobalFunction.h"
#include "ErrorInfo.h"
#include "GlobalNotification.h"
#include "../../pfs/src/common.h"
#include "../../pfs/versionmetafile.h"
#include "../../pfs/setupmetafile.h"
#include "KHLogger.h"
#include "LauncherConfig.h"
#include <wininet.h>
#include "ResumeBrokenDownload.h"
#include "TextManager.h"
#include "TextID.h"
#include "../../pfs/stringhelper.h"

CUpdateMountManager::~CUpdateMountManager()
{
}

// 提交修改的数据
bool CUpdateMountManager::CommitPFS( HANDLE hStopEvent)
{
	if( m_bReadOnlyMount )
	{
		// 对于只读方式的Mount，这里不执行Commit操作
		return true;
	}

	bool bFailed = false;

	GlobalNotifyStart( (int) m_setupMetas.size() );
	
	for( PFSX::SetupMetaVector::iterator i=m_setupMetas.begin(); i != m_setupMetas.end(); ++i )
	{
		std::wstring pfsdir = RUN_ROOT + i->GetName();

		GlobalNotifyStep( pfsdir.c_str() );
		
		PFS::FILE_DATA_HASH hash = 0;

		bool bRet = false;
		bool bCommit = !DetectBreakEvent( hStopEvent);
		if( bCommit )
		{
			bRet = PFS::CEnv::Commit( pfsdir );
			if( !bRet )
			{
				upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_COMMIT, pfsdir.c_str()) );
				GlobalNotifyErrorInfo( upd::GetLastError() );
			}
			else
			{
				// 提交修改之后，获取MetaFile的hash值
				if( !PFS::CEnv::GetMetaFileHash( pfsdir, hash))
				{
					hash = GetMetaFileHash( pfsdir);
				}
			}
		}

		if ( bRet )
		{
			i->SetHash( hash );
		}
		else
		{
			i->SetHash( 0 );
			bFailed = true;
		}
	}

	GlobalNotifyEnd();

	// Commit运行根系统
	bool bRetRoot = PFS::CEnv::Commit( RUN_ROOT );
	if ( !bRetRoot )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_COMMIT, RUN_ROOT) );
		GlobalNotifyErrorInfo(  upd::GetLastError() );
	}

	return !bFailed && bRetRoot;
}

bool CUpdateMountManager::UnmountPFS()
{
	if( !m_bNeedUnmount )
	{
		return true;
	}

	// 按相反顺序unmount文件子系统
	bool bRet = true;
	GlobalNotifyStart( (int) m_setupMetas.size() );
	for( PFSX::SetupMetaVector::iterator i=m_setupMetas.begin(); i != m_setupMetas.end(); ++i )
	{
		std::wstring pfsdir = RUN_ROOT + i->GetName();

		GlobalNotifyStep( pfsdir.c_str() );
		
		// 都是以commit = false方式unmount，因为commit过程已经单独实现
		bRet = PFS::CEnv::Unmount( pfsdir, false );
		if ( !bRet )
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_UNMOUNT, pfsdir.c_str()) );
			GlobalNotifyErrorInfo( upd::GetLastError() );
			bRet = false;
		}
	}

	GlobalNotifyEnd();

	// Unmount运行根系统
	// 以commit = false方式unmount，因为commit过程已经单独实现
	bRet = PFS::CEnv::Unmount( RUN_ROOT, false );
	if ( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_UNMOUNT, RUN_ROOT) );
		GlobalNotifyErrorInfo(  upd::GetLastError() );
	}

	m_bNeedUnmount = !bRet;
	return !m_bNeedUnmount;
}

static bool MoveFiles(const std::wstring& strFromDir,
					  const std::wstring& strToDir,
					  PFS::CMetaInfoMap& rMimNeedToMove,
					  PFS::CMetaInfoMap& rMimMoved,// 统计转移成功的内容
					  bool bOverwrite,			// 是否覆盖已存在的文件
					  HANDLE hStopEvent )
{
	bool bRet = true;

	PFS::CMetaInfoMap::const_iterator rEnd = rMimNeedToMove.end();
	for(PFS::CMetaInfoMap::const_iterator itr = rMimNeedToMove.begin(); itr != rEnd; ++ itr )
	{
		if( DetectBreakEvent( hStopEvent ) ) 
		{
			bRet = false;
			break;
		}

		const std::wstring& path =  (*itr).first;

		if( PFS::CEnv::FileCopy(strFromDir + path, strToDir + path, !bOverwrite) )
		{
			if(  PFS::CEnv::RemoveFile( strFromDir + path ) )
			{
				rMimMoved.insert(*itr);
				GlobalNotifyStep( path.c_str() );
			}
			else
			{
				int nLastError = PFS::CEnv::GetLastError();
				bRet = false;
				GlobalNotifyStep( path.c_str(), nLastError );
			}
		}
		else
		{
			int nLastError = PFS::CEnv::GetLastError();
			if( PFS::EC_FILE_NOT_FOUND == nLastError ||
				ERROR_FILE_NOT_FOUND == nLastError ||
				ERROR_PATH_NOT_FOUND  == nLastError )
			{
				// 源文件已经不存在，这里会执行删除MetaInfo动作。
				PFS::CEnv::RemoveFile( strFromDir + path );
				GlobalNotifyStep( path.c_str(), nLastError );
			}
			else if( !bOverwrite && PFS::CEnv::IsFileExisting( strToDir + path ) )
			{
				// 不允许覆盖的情况，如果目标文件已存在导致Copy失败，此时，删除源，视为成功。
				PFS::CEnv::RemoveFile( strFromDir + path );
				rMimMoved.insert(*itr);
				GlobalNotifyStep( path.c_str() );
			}
			else
			{	
				GlobalNotifyStep( path.c_str(), nLastError );
				bRet = false;
			}
		}
	}

	return bRet;
}

static bool IsDirectoryExisting( const wchar_t * lpszDirName )
{
	DWORD	dwAttribute = GetFileAttributesW( lpszDirName );
	if( INVALID_FILE_ATTRIBUTES == dwAttribute)
		return false;
	return FILE_ATTRIBUTE_DIRECTORY == ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
}

//
// 打包方式变化时，客户端文件的预处理。
// 其处理方案大致如下：
// 新建一个pfsFile，从原目录下转移文件到pfsFile中。
//
static bool MoveFilesForNewPackage(std::wstring strPfsRunRoot, 
								   std::wstring strRunDir, 
								   std::wstring pakName,
								   HANDLE hStopEvent)
{
	// 如果原本pfsdir是一个目录而pfsFile尚未创建，那么，此时是一个打包方式变化的更新。
	// 其处理方案大致如下：
	// 新建一个pfsFile，从pfsdir下转移原有的文件到pfsFile中。
	// 但，转移失败，这里并不放弃后续的更新，仅仅算是一种完善措施。
	std::wstring pfsdir = strPfsRunRoot + pakName;
	std::wstring pfsFile = pfsdir + L".pfs";
	std::wstring nativeDir = strRunDir + pakName;

	bool bOverwrite = !PFS::CFile::IsFileExisting(pfsFile);
	bool bSrcExisting = IsDirectoryExisting(nativeDir.c_str());
	
	std::time_t current = std::time( NULL );
	std::wstringstream wss;
	wss << pfsdir;
	wss << L".";
	wss << (unsigned int) (current & 0x0FFFFFFFF);
	std::wstring pfsdirtemp = wss.str(); // 用时间生成一个临时的字符串。

	bool bRet = PFS::CEnv::Mount( pfsdirtemp, pfsFile, PFS::FST_ZIP, PFS::MT_ZIPUPDATE );
	if( !bRet )
	{
		return false;
	}

	PFS::CMetaInfoMap mimNeedToMove, mimMoved;
	
	// 获取该目录下的所有文件清单pfsdir
	bRet = PFS::CEnv::GetMetaInfosEx(pfsdir, mimNeedToMove );

	if( !mimNeedToMove.empty() )
	{
		bRet = MoveFiles( pfsdir, pfsdirtemp, 
			mimNeedToMove, mimMoved, bOverwrite, hStopEvent );

		if( !mimMoved.empty() )
		{
			KHMsgLog_Warning(L"Info - %d files under %s were moved to package %s.\n",
				mimMoved.size(), nativeDir.c_str(), pfsFile.c_str() );

			std::wofstream fmoved;
			fmoved.open( (strRunDir + L"\\log\\moved.log").c_str(), std::ios_base::out | std::ios_base::app );
			DumpMetaInfoMap( mimMoved, fmoved);
			fmoved << L" --- from " << pfsdir << L" to " << pfsFile << L" ---- "  << std::endl;
		}

		if( mimNeedToMove.size() != mimMoved.size() )
		{
			PFS::CMetaInfoMap mimMoveFailed; 
			SubtractMetaInfoMap( mimNeedToMove, mimMoved, mimMoveFailed );
			KHMsgLog_Warning(L"Warning - %d files under %s were moved to %s failed.\n",
				mimMoveFailed.size(), nativeDir.c_str(), pfsFile.c_str() );

			std::wofstream fmovefailed;
			fmovefailed.open( (strRunDir + L"\\log\\movefailed.log").c_str(), 
				std::ios_base::out | std::ios_base::app );
			DumpMetaInfoMap( mimMoveFailed, fmovefailed);
			fmovefailed << L" --- from " << pfsdir << L" to " << pfsFile << L" ---- "  << std::endl;
		}
	}
	
	// 可以删除目录了。
	if( bRet && bSrcExisting )
	{
		BOOL bRet2 = DeleteCacheFolder(nativeDir);
		if( bRet2 )
		{
			KHMsgLog_Info(L"Info - DeleteFolder(%s).\n", nativeDir.c_str() );
		}
		else
		{
			DWORD dwError = ::GetLastError();
			KHMsgLog_Warning(L"Warning - DeleteFolder(%s) failed(%d)\n", nativeDir.c_str(), dwError );
		}
	}

	PFS::CEnv::Unmount( pfsdirtemp );

	return bRet;
}

bool CUpdateMountManager::MountPFS(const std::wstring& strRunDir, bool bReadOnly, 
								   HANDLE hStopEvent, const PFSX::SetupMetaVector& setupMetas)
{
	m_setupMetas.clear();

	std::wstring strRunRoot = RUN_ROOT;
	m_bReadOnlyMount = bReadOnly;

	GlobalNotify( GlobalGetText( TIP_MOUNT_ROOT).c_str());
	GlobalNotifyStart( 1 );

	// Mount运行系统
	bool bOK = PFS::CEnv::Mount( strRunRoot, strRunDir, PFS::FST_NATIVE, bReadOnly ? PFS::MT_READONLY : PFS::MT_WRITABLE );
	GlobalNotifyEnd();
	if ( !bOK )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_MOUNT, strRunRoot.c_str(), strRunDir.c_str()) );

		GlobalNotifyErrorInfo( upd::GetLastError() );
		return false;
	}

	m_bNeedUnmount = true;

	if( DetectBreakEvent( hStopEvent))
	{
		PFS::CEnv::Unmount( RUN_ROOT );
		return false;
	}
	
	bool bRet = true;

	//TCHAR buffer[256] = {0};
	//DWORD timeTotalElapse = timeGetTime();
	//DWORD timeElapse = timeTotalElapse;

	GlobalNotify( GlobalGetText( TIP_MOUNT_RESOURCE).c_str());
	GlobalNotifyStart( (int) setupMetas.size() );

	for( PFSX::SetupMetaVector::const_iterator i=setupMetas.begin(); i != setupMetas.end(); ++i )
	{
		std::wstring pfsdir = strRunRoot + i->GetName();

		std::wstring pfsFile = pfsdir + L".pfs";

		GlobalNotifyStep( pfsFile.c_str() );

		// 这里处理一些特殊情况:
		// 如果原本pfsdir是一个目录而pfsFile尚未创建，那么，此时是一个打包方式变化的更新。
		// 其处理方案大致如下：
		// 新建一个pfsFile，从pfsdir下转移原有的文件到pfsFile中。
		// 但，转移失败，这里并不放弃后续的更新，仅仅算是一种完善措施。
		if( !bReadOnly )
		{
			MoveFilesForNewPackage( strRunRoot, strRunDir,  i->GetName(), hStopEvent );

			if( DetectBreakEvent( hStopEvent) )
			{
				PFS::CEnv::Unmount( RUN_ROOT );

				return false;
			}
		}

		//timeElapse = timeGetTime();
		do 
		{
			bRet = PFS::CEnv::Mount( pfsdir, pfsFile, PFS::FST_ZIP, bReadOnly ? PFS::MT_READONLY : PFS::MT_ZIPUPDATE );
			//timeElapse = timeGetTime() - timeElapse;
			//StringCchPrintf( buffer, _countof(buffer), _T("mount %s -- %lu ms\n"), pfsFile.c_str(), timeElapse );
			//OutputDebugString( buffer );
			if ( !bRet )
			{
				int nLastError = PFS::CEnv::GetLastError();
				std::wostringstream oss;
				oss << GlobalFormatText( ERR_MOUNT, pfsdir.c_str(), pfsFile.c_str());

				if ( !bReadOnly &&
					(nLastError == PFS::EC_INVALID_MOUNT_DEVICE ||
					nLastError == PFS::EC_FILE_BAD_ZIPDATA)
					)
				{
					oss << GlobalGetText( TIP_REBUILD_FILE);
				}
				upd::SetLastError( nLastError, oss.str() );
				GlobalNotifyErrorInfo( upd::GetLastError() );
				if( !bReadOnly && 
					(nLastError == PFS::EC_INVALID_MOUNT_DEVICE ||
					nLastError == PFS::EC_FILE_BAD_ZIPDATA)
					)
				{
					PFS::CFile file;
					if( !file.Open(pfsFile, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_RDWR ) )
					{
						upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_REBUILD, pfsFile.c_str()));
						GlobalNotifyErrorInfo( upd::GetLastError() );
					}
					else
					{
						// 重建成功后继续尝试
						continue;
					}
				}
			}
			else
			{
				m_setupMetas.push_back( *i);
				m_setupMetas.back().SetHash( 0);
			}
			break; // 不再尝试
		} while( 1 );

		if ( !bRet )
		{
			break; // 跳出for循环
		}

		//if( CUpdateManager::DetectBreakEvent() )
		if( DetectBreakEvent( hStopEvent))
		{
			bRet = false;
			break;
		}

		//timeTotalElapse = timeGetTime() - timeTotalElapse;
		//StringCchPrintf( buffer, _countof(buffer), _T("-- 共花费时间 %lu 秒 --\n"), timeTotalElapse/1000 );
		//OutputDebugString( buffer );
	}
	
	GlobalNotifyEnd( );

	if( !bRet )
		UnmountPFS();

	return bRet;
}

// 生成 mount 目录的 .file.meta 文件的 hash 值
PFS::FILE_DATA_HASH CUpdateMountManager::GetMetaFileHash( const std::wstring& mount)
{
	PFS::CMetaInfoMap mim;

	//此时.files.meta文件内容尚未更新，不能从文件读取
	//if( !ReadMetaInfoFile( mount, mim))
	//{
	//	return 0;
	//}	
	if( !PFS::CEnv::GetMetaInfos( mount, mim) )
	{
		// TODO:
		return 0;
	}
	mim.erase( PFSX::FILES_META_NAME);	// 删除 .file.meta 项，因为服务器端的列表文件中没计算 .files.meta 这项

	PFS::CDataBlock buffer;
	if( !PFS::CMetaDataWrite::DoWrite( buffer, mim))
	{
		return 0;
	}
	PFS::CImageFile tempfile( buffer);
	PFS::FILE_DATA_HASH md5 = 0;
	PFS::helper::CMD5FileHashMethod md5Method;
	md5Method.MakeFileHash( tempfile, md5);

	return md5;
}

bool CDownloadFiles::DownloadOneFile( const std::wstring& fileName )
{
	// 直接Copy文件实现下载，这里会同时Copy MetaInfo
	std::wstring strSrcPath = BRF_ROOT + fileName;
	std::wstring strDstPath = RES_ROOT + fileName;
	bool bRet = PFS::CEnv::FileCopy( strSrcPath, strDstPath, false );
	if( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_DOWNLOAD_FILE, strSrcPath.c_str()));
	}
	return bRet;
}

bool CDownloadFiles::Download( HANDLE hBreakEvent )
{
	if( !PFS::CEnv::Mount( BRF_ROOT, m_strDir + L".brief", PFS::FST_BRIEF, PFS::MT_READONLY) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_MOUNT_BRIEF, BRF_ROOT, m_strDir.c_str()));
		return false;
	}

	if( !PFS::CEnv::GetMetaInfos( BRF_ROOT, m_mimDownload ) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_GETMETAINFO, BRF_ROOT));

		PFS::CEnv::Unmount( BRF_ROOT );
		return false;
	}

	std::wstring cachaPath = m_strDir + L".cache";
	CreateHiddenDirectory( cachaPath);
	if( !PFS::CEnv::Mount( RES_ROOT, cachaPath, PFS::FST_NATIVE, PFS::MT_WRITABLE ) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_MOUNT_CACHE, RES_ROOT, m_strDir.c_str()));

		PFS::CEnv::Unmount( BRF_ROOT );
		return false;
	}

	// 移除空项
	m_mimDownload.erase(L"/.");

	size_t total = m_mimDownload.size();
	GlobalNotify( GlobalGetText( TIP_DOWNLOAD_FILE_COUNT).c_str(), total );
	GlobalNotifyStart( (int)total );

	size_t errorCounter = 0;	// 统计错误次数
	int retryTimes = 0;			// 重试次数
	while( true)
	{
		size_t maxFailedCount = m_mimDownload.size() / 32;
		if( maxFailedCount == 0 && m_mimDownload.size() > 1)
		{
			// 如果m_mimDownload个数大于1，则连续失败个数最小为10
			maxFailedCount = min( 10, m_mimDownload.size());
		}
		errorCounter = 0;

		for( PFS::CMetaInfoMap::const_iterator i=m_mimDownload.begin(), e=m_mimDownload.end(); i != e; ++i)
		{
			if( DetectBreakEvent( hBreakEvent) )
			{
				// 用户要退出此过程（此时不视为下载失败）
				break;
			}

			const std::wstring& filename = i->first;
			if( !DownloadOneFile( filename ) )
			{
				m_mimFailed.insert( *i );
				GlobalNotifyStep( filename, upd::GetLastError().ErrorCode );
				if( ++errorCounter > maxFailedCount )
				{
					GlobalNotify( GlobalGetText( TIP_DOWNLOAD_FILE_ERROR).c_str(), errorCounter );
					break;
				}
			}
			else
			{
				// 成功将errorCount清0，如果连续失败个数超过 maxFailedCount 才退出
				errorCounter = 0;
				GlobalNotifyStep( filename );
				m_mimDownloaded.insert( *i );
			}
		}
		if( errorCounter > maxFailedCount)
		{
			break;
		}
		if( m_mimFailed.empty())
		{
			break;
		}
		if( ++retryTimes >= 10)
		{
			break;	// 重试次数不超过10次
		}
		m_mimDownload.swap( m_mimFailed);
		m_mimFailed.clear();
	}

	if ( errorCounter > 0 )
	{
		GlobalNotify( GlobalGetText( TIP_DOWNLOAD_COMPLETE).c_str(), errorCounter );
		// 写下载失败的文件列表
		std::wofstream logFile;
		logFile.open( ( m_strDir + L".cache\\failed.log").c_str(), std::ios_base::out | std::ios_base::app );
		DumpMetaInfoMap( m_mimFailed, logFile);
	}

	if( m_mimDownloaded.size() > 0 )
	{
		// 写下载文件列表到缓存目录中供查阅
		GlobalNotify( GlobalGetText( TIP_DOWNLOAD_COMPLETE2).c_str(), m_mimDownloaded.size() );
		std::wofstream logFile;
		logFile.open( ( m_strDir + L".cache\\downloaded.log").c_str(), std::ios_base::out | std::ios_base::app );
		DumpMetaInfoMap( m_mimDownloaded, logFile );
	}

	PFS::CEnv::Unmount( BRF_ROOT );
	PFS::CEnv::Unmount( RES_ROOT );

	GlobalNotifyEnd();

	return m_mimDownloaded.size() > 0 && errorCounter == 0;
}

bool DetectBreakEvent( HANDLE hEvent)
{
	if ( WaitForSingleObject( hEvent, 1 ) == WAIT_OBJECT_0 )
	{
		return true;
	}
	return false;
}

int HandleCheckFileError( bool bPatch, std::wstring path )
{
	int nLastError = PFS::CEnv::GetLastError();

	bool bErrorProcessed = false;

	// 如果格式无效，删除缓存中保持的下载的文件，下次更新重新尝试下载
	if( PFS::ECX_BADFORMAT == nLastError )
	{
		if( !bPatch )
		{
			bool bRemoved = PFS::CEnv::RemoveFile( RES_ROOT + path);
			if ( !bRemoved )
			{
				upd::SetLastError( PFS::CEnv::GetLastError(), 
					GlobalFormatText( ERR_REMOVE_FILE, RES_ROOT, path.c_str()) );
				GlobalNotifyErrorInfo( upd::GetLastError(), __FUNCTIONW__ );
			}
			else
			{
				upd::SetLastError( nLastError, GlobalFormatText( ERR_FILE_NOT_INTACT, RES_ROOT, path.c_str()) );
				bErrorProcessed = true;
			}
		}
	}

	if( !bErrorProcessed )
	{
		upd::SetLastError( nLastError, GlobalFormatText( ERR_CHECK_FILE, path.c_str()) );
	}

	return nLastError;
}

int HandleDecodeFileError( bool bPatch, std::wstring path )
{
	int nLastError = PFS::CEnv::GetLastError();

	bool bErrorProcessed = false;

	// 如果格式无效，删除缓存中保持的下载的文件，下次更新重新尝试下载
	if( PFS::ECX_BADFORMAT == nLastError )
	{
		if( !bPatch )
		{
			bool bRemoved = PFS::CEnv::RemoveFile( RES_ROOT + path);
			if ( !bRemoved )
			{
				upd::SetLastError( PFS::CEnv::GetLastError(), 
					GlobalFormatText( ERR_REMOVE_FILE, RES_ROOT, path.c_str()) );
				GlobalNotifyErrorInfo( upd::GetLastError(), __FUNCTIONW__ );
			}
			else
			{
				upd::SetLastError( nLastError, GlobalFormatText( ERR_FILE_NOT_INTACT, RES_ROOT, path.c_str()) );
				bErrorProcessed = true;
			}
		}
		// 将运行目录中的metainfo信息清0
		if( !PFS::CEnv::SetFileMetaInfo( RUN_ROOT + path, PFS::CMetaInfo()))
		{
			std::wostringstream oss;
			oss<<L"错误: SetFileMetaInfo("<< RUN_ROOT + path << L") 失败!" ;
			//upd::SetLastError( PFS::CEnv::GetLastError(), oss.str() );
		}
		else
		{
			bErrorProcessed = true;
		}
	}

	if( !bErrorProcessed )
	{
		upd::SetLastError( nLastError, GlobalFormatText( ERR_DECODE_FILE, path.c_str()) );
	}

	return nLastError;
}


bool CheckFilesPartly(PFS::CMetaInfoMap::const_iterator rBegin, 
					  PFS::CMetaInfoMap::const_iterator rEnd,
					  HANDLE hStopEvent,
					  bool bPatch )
{
	PFS::CMetaInfoMap::const_iterator::difference_type count = std::distance( rBegin, rEnd );
	if( count == 0 )
	{
		return true;
	}

	bool bRet = true;

	for(PFS::CMetaInfoMap::const_iterator itr = rBegin; itr != rEnd; ++ itr )
	{
		if( DetectBreakEvent( hStopEvent ) ) 
		{
			bRet = false;
			break;
		}

		const std::wstring& path =  (*itr).first;

		//
		// 这里暂时放弃使用下面的函数对文件进行校验，
		// 因为，基于文件名从.cache中取得的metainfo是1.0版本的
		// 没有size2和hash2，无法对文件进行充分的校验。
		// 而且，的确没有必要再根据文件名获取一次metainfo。
		// 至于此函数的BUG问题，等考虑好了RefreshMetaFile()函数的修改方案之后再定。
		//
		//  PFSX::CheckFileEx(RES_ROOT + path)
		//
		PFS::CFile theFile;
		if( !theFile.Open( RES_ROOT + path, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			return false;
		}

		if( PFSX::CheckFileEx( theFile, (*itr).second ) )
		{	
			GlobalNotifyStep( path.c_str() );
		}
		else
		{
			// 注：这里必须首先关闭此文件，否则HandleCheckFileError()无法删除此文件。
			theFile.Close();

			int nLastError = HandleCheckFileError(bPatch, path);
			bRet = false;
			GlobalNotifyStep( path.c_str(), nLastError );
		}
	}

	return bRet;
}

bool DecodeFilesPartly( PFS::CMetaInfoMap::const_iterator rBegin, 
					   PFS::CMetaInfoMap::const_iterator rEnd,
					   PFS::CMetaInfoMap&	rMimUpdate,// 统计成功合并的内容
					   HANDLE hStopEvent,
					   bool bPatch )
{
	PFS::CMetaInfoMap::const_iterator::difference_type count = std::distance( rBegin, rEnd );
	if( count == 0 )
	{
		return true;
	}

	bool bRet = true;
	for(PFS::CMetaInfoMap::const_iterator itr = rBegin; itr != rEnd; ++ itr )
	{
		if( DetectBreakEvent( hStopEvent ) ) 
		{
			bRet = false;
			break;
		}

		const std::wstring& path =  (*itr).first;
		if( PFSX::DecodeFile(RES_ROOT + path,RUN_ROOT + path) )
		{
			rMimUpdate.insert(*itr);
			GlobalNotifyStep( path.c_str() );
		}
		else
		{
			int nLastError = HandleDecodeFileError(bPatch, path);
			bRet = false;
			GlobalNotifyStep( path.c_str(), nLastError );
		}
	}

	return bRet;
}

// 用CheckFileEx方式校验strDir目录中的文件
bool CheckFilesInFolder(const std::wstring& strDir,
						PFS::CMetaInfoMap& rMimCache,// 需要校验的所有内容
						HANDLE hStopEvent ) 
{
	bool bRet = false;
	PFS::CMetaInfoMap::const_iterator itr;

	// 如果下载的文件数据不完整,我们需要删除它,所以这里以可写的方式Mount
	bRet = PFS::CEnv::Mount( RES_ROOT, strDir, PFS::FST_NATIVE, PFS::MT_WRITABLE );
	if( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_MOUNT, RES_ROOT, strDir.c_str()) );
		return false;
	}

	// 使用传入的参数，而不用GetMetaInfos中得到的,其实他们应该一样

	GlobalNotify( GlobalGetText( TIP_CHECK_FILE_COUNT).c_str(), rMimCache.size() );

	GlobalNotifyStart( (int) rMimCache.size() );

	bRet = CheckFilesPartly( rMimCache.begin(), rMimCache.end(), hStopEvent, false );

	GlobalNotifyEnd();

	PFS::CEnv::Unmount( RES_ROOT );
	
	return bRet;
}



// 将strInputDir指定的目录中的文件，使用DecodeFile方式打开并保存到strOutputDir指定的目录中
bool TranslateFilesInFolder(
							const std::wstring& strInputDir,
							const std::wstring& strOutputDir,
							PFS::CMetaInfoMap& rMimMerge, // 需要合并的所有内容
							PFS::CMetaInfoMap& rMimUpdate,// 统计成功合并的内容
							HANDLE hStopEvent,
							bool bPatch) 
{
	rMimUpdate.clear();

	bool bRet = false;
	PFS::CMetaInfoMap::const_iterator itr;

	if( !bPatch)
	{
		// 如果下载的文件数据不完整,我们需要删除它,所以这里以可写的方式Mount
		bRet = PFS::CEnv::Mount( RES_ROOT, strInputDir, PFS::FST_NATIVE, PFS::MT_WRITABLE );
		if ( !bRet )
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_MOUNT, RES_ROOT, strInputDir.c_str()) );
			return false;
		}
	}

	// 使用传入的参数，而不用GetMetaInfos中得到的,其实他们应该一样
	PFS::CMetaInfoMap& paths = rMimMerge;

	bRet = true;

	GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT).c_str(), paths.size() );
	GlobalNotifyStart( (int) paths.size() );

	bRet = DecodeFilesPartly( paths.begin(), paths.end(), rMimUpdate, hStopEvent, bPatch );
		
	if( !bPatch)
	{
		PFS::CEnv::Unmount( RES_ROOT );
	}

	GlobalNotifyEnd( );

	return bRet;
}

// 将strInputDir指定的目录中的文件，使用DecodeFileEn方式打开并保存到strOutputDir指定的目录中
bool TranslateFilesInFolderEn(const std::wstring& strInputDir,
							  const std::wstring& strOutputDir,
							  PFS::CMetaInfoMap& rMimMerge, // 需要合并的所有内容
							  PFS::CMetaInfoMap& rMimUpdate,// 统计成功合并的内容
							  HANDLE hStopEvent ) 
{
	rMimUpdate.clear();

	bool bRet = false;
	PFS::CMetaInfoMap::const_iterator itr;

	// 如果下载的文件数据不完整,我们需要删除它,所以这里以可写的方式Mount
	bRet = PFS::CEnv::Mount( RES_ROOT, strInputDir, PFS::FST_NATIVE, PFS::MT_WRITABLE );
	if ( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_MOUNT, RES_ROOT, strInputDir.c_str()) );
		return false;
	}

	// 使用传入的参数，而不用GetMetaInfos中得到的,其实他们应该一样
	PFS::CMetaInfoMap& paths = rMimMerge;

	bRet = true;

	GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT).c_str(), paths.size() );
	GlobalNotifyStart( (int) paths.size() );

	for( itr = paths.begin(); itr != paths.end(); ++ itr )
	{
		if( DetectBreakEvent( hStopEvent ) ) 
		{
			bRet = false;
			break;
		}

		const std::wstring& path =  (*itr).first;
		if( PFSX::DecodeFileEn( RES_ROOT + path, RUN_ROOT + path ) )
		{
			rMimUpdate.insert( *itr );
			GlobalNotifyStep( path.c_str() );
		}
		else
		{
			int nLastError = HandleDecodeFileError(false, path);
			bRet = false;
			GlobalNotifyStep( path.c_str(), nLastError );
		}
	}

	PFS::CEnv::Unmount( RES_ROOT );
	GlobalNotifyEnd( );

	return bRet;
}

bool TranslateFilesForLauncher(
							   const std::wstring&	strInputDir,
							   const std::wstring&	strOutputDir,
							   PFS::CMetaInfoMap&	rMimMerge, // 需要合并的所有内容
							   PFS::CMetaInfoMap&	rMimUpdate,// 统计成功合并的内容
							   HANDLE hStopEvent,
							   bool bPatch) 
{
	rMimUpdate.clear();

	std::wstring strLauncherFolder = L"/";
	strLauncherFolder += GetLauncherFolder();
	strLauncherFolder += L"/";
	std::wstring strLauncherFolderEnd = strLauncherFolder;
	strLauncherFolderEnd[strLauncherFolder.size()-1]++;

	PFS::CMetaInfoMap::const_iterator rBegin = rMimMerge.lower_bound( strLauncherFolder);
	if( rBegin == rMimMerge.end() )
	{
		return true;
	}

	PFS::CMetaInfoMap::const_iterator rEnd = rMimMerge.lower_bound( strLauncherFolderEnd );
	// rEnd may be at the end.

	bool bRet = false;
	PFS::CMetaInfoMap::const_iterator itr;

	if( !bPatch)
	{
		// 如果下载的文件数据不完整,我们需要删除它,所以这里以可写的方式Mount
		bRet = PFS::CEnv::Mount( RES_ROOT, strInputDir, PFS::FST_NATIVE, PFS::MT_WRITABLE );
		if ( !bRet )
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_MOUNT, RES_ROOT, strInputDir.c_str()) );
			return false;
		}
	}

	PFS::CMetaInfoMap::const_iterator::difference_type count = std::distance( rBegin, rEnd );
	bRet = true;

	GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT).c_str(), (unsigned int) (count) );
	GlobalNotifyStart( (int) (count) );

	if( !DecodeFilesPartly(rBegin, rEnd, rMimUpdate, hStopEvent, bPatch ) )
	{
		bRet = false;
	}

	// 因为每次更新都从服务器下载最新的 .setup.meta 所以这里再不需要单独处理/.setup.meta

	if( !bPatch)
	{
		PFS::CEnv::Unmount( RES_ROOT );
	}

	GlobalNotifyEnd( );

	return bRet;
}

//
// summary:
//		比较两个MetaInfo是否不一样
//		这里只比较文件大小和Hash值
//
bool IsEqualMetaInfo(const PFS::CMetaInfo& rLeft,const PFS::CMetaInfo& rRight)
{
	return rLeft.GetFileSize() == rRight.GetFileSize() &&
		rLeft.GetFileHash() == rRight.GetFileHash() && 
		rLeft.GetFilePfsxAttributes() == rRight.GetFilePfsxAttributes() &&
		rLeft.GetMethod() == rRight.GetMethod();
}

// 
// summary:
//		对Server端的MetaInfo信息和用户本机版本的MetaInfo进行差异化判断
//		用于确定需要更新的列表内容
//
// parameters:
//		[in] rMinuend - 含全部信息的MetaInfo信息，Server端或Server与Local的差，用作被减数
//		[in] rSubtrahend - 用户本机的MetaInfo信息，Local或Cache中的信息，用作减数
//		[out] rDifference - 计算结果，包含要更新的MetaInfo信息
//
void SubtractMetaInfoMap(
						 const PFS::CMetaInfoMap& rMinuend,
						 const PFS::CMetaInfoMap& rSubtrahend,
						 PFS::CMetaInfoMap&		rDifference )
{
	PFS::CMetaInfoMap::const_iterator itrS = rMinuend.begin();
	PFS::CMetaInfoMap::const_iterator itrL = rSubtrahend.begin();
	PFS::CMetaInfoMap::key_compare comparator;
	for ( ; itrS != rMinuend.end() &&  itrL != rSubtrahend.end();  ++ itrL )
	{
		const std::wstring& rName2 = itrL->first;

		for( ; itrS != rMinuend.end() && comparator( itrS->first, rName2 ); ++ itrS )
		{
			rDifference.insert( *itrS );
		}

		if ( itrS == rMinuend.end() )
		{
			break;
		}

		// 如果名字相同，判断MetaInfo是否一样，将不一样的加到更新列表中
		if (  itrS->first.compare( rName2 ) == 0 ) 
		{
			if ( !IsEqualMetaInfo( itrS->second, itrL->second) )
			{
				rDifference.insert( *itrS );
			}

			++ itrS;
		}
	}

	for( ; itrS != rMinuend.end() ; ++ itrS )
	{
		rDifference.insert( *itrS );
	}

	//std::wofstream frun;
	//frun.open( L"D:\\rrun.txt" );
	//DumpMetaInfoMap( rSubtrahend, frun );
	//std::wcout<< L"D:\\rrun.txt !" <<std::endl;

	//std::wofstream fserver;
	//fserver.open( L"D:\\rserver.txt" );
	//DumpMetaInfoMap( rMinuend, fserver );
	//std::wcout<< L"D:\\rserver.txt !" <<std::endl;

	//std::wofstream fupdate;
	//fupdate.open( L"D:\\rupdate.txt" );
	//DumpMetaInfoMap( rDifference, fupdate );
	//std::wcout<< L"D:\\rupdate.txt !" <<std::endl;

	rDifference.erase( L"/." );
}

// 
// summary:
//		对上一版本的MetaInfo信息和当前版本的MetaInfo进行比较
//		用于确定当前版本需要删除的列表
//
// parameters:
//		[in] rLast - 上一版本的MetaInfo信息
//		[in] rCur  - 当前版本的MetaInfo信息
//		[out] rDelete - 计算结果，包含要删除的MetaInfo信息
//
void SubtractMetaInfoMapEx( const PFS::CMetaInfoMap& rLast, 
						   const PFS::CMetaInfoMap& rCur,
						   PFS::CMetaInfoMap&	rDelete )
{
	PFS::CMetaInfoMap::const_iterator iLast = rLast.begin();
	PFS::CMetaInfoMap::const_iterator iCur = rCur.begin();
	PFS::CMetaInfoMap::const_iterator lastEnd = rLast.end();
	PFS::CMetaInfoMap::const_iterator curEnd = rCur.end();
	PFS::CMetaInfoMap::key_compare comparator;

	for ( ; iLast != lastEnd &&  iCur != curEnd; )
	{
		const std::wstring& rName1 = iLast->first;
		const std::wstring& rName2 = iCur->first;

		if( comparator( rName1, rName2 ))
		{
			rDelete.insert( *iLast );
			++iLast;
		}
		else if( comparator( rName2, rName1))
		{
			++iCur;
		}
		else	// rName1 == rName2
		{
			++iLast;
			++iCur;
		}
	}

	for( ; iLast != lastEnd ; ++ iLast)
	{
		rDelete.insert( *iLast );
	}
};

//
// 输出MetaInfoMap中的文件名列表到流中
//
void DumpMetaInfoMap( const PFS::CMetaInfoMap& mim, std::wostream& oss )
{
	try
	{
		oss.imbue(std::locale(".936"));
	}
	catch (std::runtime_error e)
	{
		KHMsgLog_Error(_T("oss.imbue(std::locale(\".936\")) throws an runtime error(%hs)\r\n"), e.what() );
	}
	
	// 输出时间信息
	std::time_t current = std::time( NULL );
	wchar_t buffer[256] = {0};
	_wctime_s(buffer, _countof(buffer), &current);
	oss << L"------------------------------------------\n";
	oss <<  buffer ; // buffer 中包含换行符
	oss << L"------------------------------------------\n";

	for( PFS::CMetaInfoMap::const_iterator it = mim.begin(); it != mim.end(); ++ it)
	{
		oss << it->first << std::endl;
	}

	oss << std::endl;
}

// 读指定目录下的MetaInfo文件
bool ReadMetaInfoFile( const std::wstring& dir, PFS::CMetaInfoMap& mimFile )
{
	bool bRet = PFSX::ReadMetaInfoFile( dir + PFSX::FILES_META_NAME, mimFile );
	if( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_READ_FILE, dir.c_str(), PFSX::FILES_META_NAME.c_str()) );
	}
	return bRet;
}

// 读指定目录下的MetaInfo2文件
bool ReadMetaInfoFile2( const std::wstring& dir, PFS::CMetaInfoMap& mimFile )
{
	bool bRet = PFSX::ReadMetaInfoFile( dir + PFSX::FILES2_META_NAME, mimFile );
	if( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_READ_FILE, dir.c_str(), PFSX::FILES2_META_NAME.c_str()) );
	}
	return bRet;
}

// 将MetaInfo文件写到指定目录下
bool WriteMetaInfoFile( const std::wstring& dir, const PFS::CMetaInfoMap& mimFile )
{
	bool bRet = PFSX::WriteMetaInfoFile( dir + PFSX::FILES_META_NAME, mimFile );
	if( !bRet )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), 
			GlobalFormatText( ERR_WRITE_FILE, dir.c_str(), PFSX::FILES_META_NAME.c_str()) );
	}

	return bRet;
}

//
// 从url指定的位置下载metainfo文件，并读取内容到rMetaInfoMap对象中
//
bool DownloadMetaInfoFile( const std::wstring& url, PFS::CMetaInfoMap& rMetaInfoMap )
{
	return ReadRemoteFilesMetaFile( url + PFSX::FILES_META_NAME, rMetaInfoMap);
}

// 从url指定的位置下载 .files2.meta 文件，并读取内容到rMetaInfoMap对象中
bool DownloadMetaInfoFile2( const std::wstring& url, PFS::CMetaInfoMap& rMetaInfoMap )
{
	return ReadRemoteFilesMetaFile( url + PFSX::FILES2_META_NAME, rMetaInfoMap);
}

// 从 file 指定的文件读取meta信息，并读取内容到rMetaInfoMap对象中
bool ReadRemoteFilesMetaFile( const std::wstring& metafile, PFS::CMetaInfoMap& rMetaInfoMap )
{
	PFSX::CInetFile inetFile;
	if( !inetFile.Open( metafile ) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalFormatText( ERR_DOWNLOAD_FILE, metafile.c_str()) );
		return false;
	}

	PFS::fsize_t szTotal = inetFile.GetSize( );
	if( szTotal == 0 )
	{
		upd::SetLastError( PFS::FILE_SIZE_ERROR, GlobalFormatText( ERR_INVALID_FILE, metafile.c_str()) );
		return false;
	}

	PFS::CMemFile theFile;
	//if( !theFile.Open() )
	//{
	//	std::wostringstream oss;
	//	oss<<L"错误: 无法生成临时文件用于保存" << metafile;
	//	upd::SetLastError( PFS::CEnv::GetLastError(), oss.str() );

	//	return false;
	//}
	//else
	{
		const size_t BUFSIZE = 40960;

		GlobalNotifyStart( ((int)szTotal+BUFSIZE-1)/BUFSIZE);

		PFS::fsize_t szTotalRead = 0;
		size_t szRead = 0;
		BYTE buffer[ BUFSIZE ] = {0};
		int nTriedTimes1 = 0;
		int nTriedTimes2 = 0;
		while( szTotalRead < szTotal )
		{
			szRead = inetFile.Read( buffer, BUFSIZE);
			if ( PFS::FILE_SIZE_ERROR != szRead && szRead > 0 )
			{
				nTriedTimes1 = 0; // 下载成功,将尝试次数重置为0
				nTriedTimes2 = 0; 
				theFile.Write( buffer, szRead );
				szTotalRead += szRead;

				GlobalNotifyStep( GlobalFormatText( TIP_DOWNLOAD_PROGRESS, int( szTotalRead*100.0/szTotal)));
			}
			else
			{
				if( PFS::CEnv::GetLastError() == ERROR_INTERNET_TIMEOUT || 
					PFS::CEnv::GetLastError() == ERROR_INTERNET_FORCE_RETRY )
				{
					// 超时错误,尝试30次,每次1s,共30s
					if( nTriedTimes1 ++ > 30 )
					{
						break;
					}
					Sleep( 1000 );
				}
				else
				{
					// 其它错误尝试10次,每次0.1s,共1s
					if( nTriedTimes2 ++ > 10 )
					{
						break;
					}
					Sleep( 100 );
				}
			}
		}

		GlobalNotifyEnd();

		bool bOK = false;
		// 必须重置文件指针到文件头，否则下面的读操作将失败
		if( szTotalRead == szTotal )
		{
			theFile.Seek( 0, PFS::FSM_SET );
			bOK = PFS::CMetaFileRead::DoRead( theFile, rMetaInfoMap );
		}

		if ( bOK )
		{
			return true;
		}
		else
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), 
				GlobalFormatText( ERR_DOWNLOAD_FILE, metafile.c_str()) );
			return false;
		}
	}
}

// 删除指定的目录以及目录下的所有文件
bool DeleteCacheFolder( std::wstring strFolder )
{
	// 我们必须保证字符串最后有2个0结尾
	// 字符串不能含有 "/"
	// 字符串不能以 "\" 结尾

	std::replace( strFolder.begin(), strFolder.end(), L'/', L'\\');
	std::wstring::size_type pos = strFolder.find_last_not_of( L"\\");
	if( pos != strFolder.size()-1)
	{
		strFolder.erase( pos+1);
	}
	strFolder.resize( strFolder.size() + 1, 0 );
	
	SHFILEOPSTRUCT FileOp = {0};
	FileOp.pFrom = strFolder.c_str();
	FileOp.wFunc = FO_DELETE;
	FileOp.fFlags = FOF_NOCONFIRMATION | FOF_NOERRORUI | FOF_SILENT;
	int iRet = SHFileOperation(&FileOp);
	if ( iRet != ERROR_SUCCESS )
	{
		::SetLastError( iRet );
		return false;
	}

	return true;
}

// 创建隐藏文件夹
bool CreateHiddenDirectory( const std::wstring& path)
{
	if( !PFS::CFileIO::IsDirectoryExisting( path))
	{
		if( !::CreateDirectory( path.c_str(), NULL))
		{
			return false;
		}
	}
	return !!::SetFileAttributes( path.c_str(), FILE_ATTRIBUTE_HIDDEN);
}

// 判断指定的Mutex对象是否已创建
bool NamedMutexExists( LPCWSTR szName )
{
	HANDLE hMutex = OpenMutexW( STANDARD_RIGHTS_READ, FALSE, szName);
	if( hMutex == NULL )
	{
		DWORD dwError = ::GetLastError();// normal error code
		if ( dwError == ERROR_FILE_NOT_FOUND )
		{
			return false;
		}
		else 
		{
			assert ( dwError == ERROR_ACCESS_DENIED );
			KHMsgLog_Error(L"NamedMutexExists::OpenMutexW(%s) failed (%d)\n", szName, dwError );
			return true;
		}
	}
	else
	{
		CloseHandle( hMutex );
		return true;				
	}
}

// 得到安置版本特有的Mutex名字 - 不同位置的安装版本，名字不同
LPCWSTR GetInstallationMutexName()
{
	static WCHAR szObjectName[MAX_PATH] = {0};

	if( szObjectName[0] == 0 )
	{
		wcscpy_s( szObjectName, MAX_PATH, SZ_INSTALLATION_NAME_PREFIX );
		wcscat_s( szObjectName, MAX_PATH, GetInstallDir() );

		// 将路径分隔符替换为 '!'
		for( LPWSTR lpszPtr = szObjectName; *lpszPtr; ++ lpszPtr  )
		{
			if( *lpszPtr == _T('/') || *lpszPtr == _T('\\') )
			{
				*lpszPtr = _T('!');
			}
		}
	}

	return szObjectName;
}

// 如果日志文件超过上限,删除全部日志
bool PurgeLogFile( LPCWSTR lpszFileName, ULONGLONG ullFileSizeLimit )
{
	PFSX::CNativeFile file;
	if( file.Open( lpszFileName, PFS::FM_EXCL, PFS::FA_RDWR ) )
	{
		if( file.GetSize() > ullFileSizeLimit )
		{
			file.SetEnd();
		}
		return true;
	}
	return false;
}

// 将隐藏文件设为可见
// 因为隐藏文件已可写方式打开时会返回错误号5
bool ShowHiddenFile( const std::wstring& filename)
{
	if( !PFS::CFileIO::IsFileExisting( filename))
	{
		return false;
	}
	DWORD attr = ::GetFileAttributes( filename.c_str());
	if( (FILE_ATTRIBUTE_HIDDEN|FILE_ATTRIBUTE_READONLY) & attr)
	{
		attr &= ~(FILE_ATTRIBUTE_HIDDEN|FILE_ATTRIBUTE_READONLY);
		return !!::SetFileAttributes( filename.c_str(), attr);
	}
	return true;
}

// 从url指定的位置读取 .setup.meta 文件，内容保存在
bool ReadRemoteSetupMeta( const std::wstring& url, PFSX::SetupMetaVector& metas)
{
	std::wstring urlFile = url + PFSX::SETUP_META_NAME;
	PFSX::CInetFile netfile;
	if( !netfile.Open( urlFile ))
		return false;

	// 因为网络文件CInetFile对Seek操作支持不是很好
	// 所以先从网络上读到内存中，然后作为CImageFile装载
	PFS::CDataBlock db;
	size_t filelen = (size_t)netfile.GetSize();
	db.Resize( filelen);
	if( filelen != netfile.Read( db.GetData(), filelen))
	{
		return false;
	}

	PFS::CImageFile imf( db);
	if( !PFSX::CSetupMetaFile::DoRead( imf, metas))
	{
		return false;
	}
	return true;
}

bool CSetupMetasDownloader::Download( HANDLE hBreakEvent )
{
	bool bRet = true;
	for( PFSX::SetupMetaVector::const_iterator iV = m_smVector.begin(); 
		iV != m_smVector.end(); ++iV )
	{
		const std::wstring& mount = iV->GetName();

		GlobalNotify( GlobalGetText( TIP_DOWNLOAD_CHILD_META_FILE).c_str(), mount.c_str() );

		CResumeBrokenDownload downloader( 
			PFSX::FILES2_META_NAME, m_strBaseUrl + mount, m_strBaseDir + mount, iV->GetHash2() );

		int iTryCounter = 0;
		do 
		{
			if( downloader.Download( hBreakEvent ) )
			{
				break;
			}

			if( DetectBreakEvent( hBreakEvent) )
			{
				bRet = false;
				break;
			}
		} while( ++iTryCounter < 3 );

		bRet &= iTryCounter < 3;
	}

	return bRet;
}

// 处理不能删除的文件
void DealUnDeleteFiles(PFS::CMetaInfoMap& rDelete, 
					   const PFSX::SetupMetaVector& smvServer )
{
	rDelete.erase( PFSX::FILES_META_NAME);	// 根目录中.files.meta
	for( PFSX::SetupMetaVector::const_iterator i=smvServer.begin(); i != smvServer.end(); ++i)
	{
		const std::wstring& file = i->GetName();
		rDelete.erase( file + L".pfs");
		rDelete.erase( file + PFSX::FILES_META_NAME);
	}
}

// 从URL中获取HostName或者IP地址。
bool GetHostNameFromUrl( LPCWSTR lpszUrl, std::wstring& hostname )
{
	DWORD dwError = 0;
	bool bRet = TRUE;
	URL_COMPONENTS UrlComponents = {0};
	UrlComponents.dwStructSize = sizeof(URL_COMPONENTS);
	UrlComponents.dwUrlPathLength = 1;
	UrlComponents.dwHostNameLength = 1;
	UrlComponents.nPort = INTERNET_DEFAULT_HTTP_PORT;
	UrlComponents.dwSchemeLength = 1;
	UrlComponents.dwExtraInfoLength = 1;
	bRet = !!InternetCrackUrl( lpszUrl, 0, 0, &UrlComponents );
	if ( bRet && UrlComponents.dwHostNameLength > 0 )
	{
		//if( UrlComponents.dwUrlPathLength > 0 ) // Maybe empty path
		//	m_baseUrl.assign( UrlComponents.lpszUrlPath, UrlComponents.dwUrlPathLength );
		hostname.assign( UrlComponents.lpszHostName, UrlComponents.dwHostNameLength );
	}
	else
	{
		if( bRet )
		{
			::SetLastError(dwError = ERROR_INTERNET_INVALID_URL);
			bRet = FALSE;
		}
	}

	return bRet;
}

void DumpSetupMeta(const wchar_t* prefix, std::wostream& wos, const PFSX::SetupMetaVector& setupmeta )
{
	wos << prefix;
	for( PFSX::SetupMetaVector::const_iterator i = setupmeta.begin(); i != setupmeta.end(); ++ i )
	{
		wos << i->GetName() << L": " << PFSX::CStringHelper::Hash2String(i->GetHash()) << L", " 
			<< PFSX::CStringHelper::Hash2String(i->GetHash2() ) << std::endl;
	}
}

void AnalyzeSetupMetaUpdateInfo(const PFSX::SetupMetaVector& _smvServer,
								PFSX::SetupMetaVector& smvLocal,
								PFSX::SetupMetaVector& smvDifference,
								PFSX::SetupMetaVector& smvNewAdded,
								PFSX::SetupMetaVector& smvUseless)
{
	if( _smvServer.empty() )
	{
		return;
	}

	PFSX::SetupMetaVector smvModified;

	// 对smvServer和smvLocal进行排序。
	PFSX::SetupMetaVector smvServer = _smvServer;
	std::sort( smvServer.begin(), smvServer.end() );
	std::sort( smvLocal.begin(), smvLocal.end() );

	// 本地和服务器均有效，计算差异。
	PFSX::SubtractSetupMeta( smvServer, smvLocal, smvDifference );

	// 服务器和本地做差，计算新增的setupmeta
	PFSX::DiffSetupMeta( smvServer, smvLocal, smvNewAdded );

	// 全部差异剔除新增的，计算修改的setupmeta
	PFSX::DiffSetupMeta( smvDifference, smvNewAdded, smvModified );

	// 本地和补丁包版本做差，计算废弃的setupmeta
	PFSX::DiffSetupMeta( smvLocal, smvServer, smvUseless );

	std::wstringstream sstream;
	
	DumpSetupMeta( L"\nserver or patch: \n", sstream, smvServer);
	DumpSetupMeta( L"\nlocal: \n", sstream, smvLocal);
	
	if( !smvNewAdded.empty() )
	{
		DumpSetupMeta( L"\nnewly added: \n", sstream, smvNewAdded);
	}

	if( !smvModified.empty())
	{
		DumpSetupMeta( L"\nmodified: \n", sstream, smvModified);
	}

	if( !smvUseless.empty() )
	{
		DumpSetupMeta( L"\nuseless: \n", sstream, smvUseless);
		
		// 从本地集合中，删除不再使用的setupmeta项目。
		PFSX::PurgeSetupMeta( smvLocal, smvUseless );

		DumpSetupMeta( L"\nafter remove useless ones, the local: \n", sstream, smvLocal);
	}

	KHMsgLog_DebugW( sstream.str().c_str() );
}