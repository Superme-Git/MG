#include "stdafx.h"
#include "UpdateManagerAutoPatch.h"
#include "GlobalNotification.h"
#include "ResumeBrokenDownload.h"
#include "PatchDownloader.h"
#include "KHLogger.h"
#include "TextManager.h"
#include "TextID.h"

inline void GlobalNotify_TextID( int textId )
{
	GlobalNotify( GlobalGetText( textId ).c_str() );
}

static bool CheckPatchFile(const std::wstring& file, PFS::FILE_DATA_HASH hash)
{
	PFSX::CNativeFile nf;
	if( nf.Open( file, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		PFS::FILE_DATA_HASH fileHash;
		PFS::helper::CMD5FileHashMethod md5Hash;
		md5Hash.MakeFileHash( nf, fileHash);

		return hash == fileHash;
	}

	return false;
}

static bool MovePatchFile(const std::wstring& srcFile, const std::wstring& dstFile)
{
	if(!PFS::CFileIO::IsFileExisting( srcFile))
		return false;

	if(MoveFileEx(srcFile.c_str(), dstFile.c_str(), MOVEFILE_REPLACE_EXISTING | MOVEFILE_WRITE_THROUGH))
		return true;

	return false;
}

bool CUpdateManagerAutoPatch::HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch, bool& rMergeCache )
{
	// 使用CUpdateManager而不使用CUpdateManagerAutoPatch进行新版本检查
	// 以保证即使使用UPDATE_MODE_AUTOPATCH模式进行新版本检查，也能获取正确结果
	//m_UpdateMode = UPDATE_MODE_DEFAULT;
	//bool bRet = CUpdateManager::HasNewVersion( szRunDir, rHas, rLocalValid, rAutoPatch, rMergeCache );
	//m_UpdateMode = UPDATE_MODE_AUTOPATCH;
	GlobalNotify_TextID( TIP_CHECK_LOCAL_VERSION );

	m_runDir = szRunDir;
	m_cacheDir = m_runDir + L".cache";
	//m_p2spDir = m_runDir + L".p2sp";
	m_newVersionInfo.Close();
	m_cacheVersion.Close();
	m_currentPatchIndex = 0;
	rAutoPatch = false;
	rMergeCache = false;

	//
	// 检查新版本
	//
	m_localVersion.Close();
	if( !PFSX::GetLocalVersion( m_runDir, m_localVersion) )
	{
		// .version.meta 不存在的情况下也允许更新
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_READ_LOCAL_VERSION));
		rLocalValid = false;
		if( !rLocalValid )
		{
			KHMsgLog_DebugW(L"读取本地版本信息失败。标记版本无效.\n");
		}

		//return false;
	}
	else
	{
		rLocalValid = m_localVersion.IsValid();
		if( !rLocalValid )
		{
			KHMsgLog_DebugW(L"本地版本无效.\n");
		}
	}

	std::wstring url = GetVersionUrl( m_localVersion, true, false );
	if( url.empty() )
	{
		PFS::CEnv::SetLastError(PFS::EC_INVALID_VERSION_INFO );
		upd::SetLastError( PFS::EC_INVALID_VERSION_INFO, GlobalGetText( ERR_LOCAL_VERSION_NOT_INTACT));
		rLocalValid = false;
		return false;
	}

	GlobalNotify_TextID( TIP_CHECK_SERVER_VERSION );
	KHMsgLog_InfoW( L"using server url:%s\n", url.c_str() );

	// 在无法获取服务器版本信息的情况下，不执行任何更新操作
	if( !PFSX::GetRemoteVersion( url, m_newVersionInfo) )
	{
		if( PFS::EC_INVALID_VERSION_INFO == PFS::CEnv::GetLastError())
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_GET_SERVER_VERSION) );
		}
		else
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_CONNECT_TO_SERVER) );
		}
		return false;
	}

	// 获取新版本url
	m_url = GetVersionUrl( m_newVersionInfo, true, false );
	if( m_url.empty() )
	{
		ASSERT( !m_url.empty() );
		PFS::CEnv::SetLastError(PFS::EC_INVALID_VERSION_INFO );
		upd::SetLastError( PFS::EC_INVALID_VERSION_INFO, GlobalGetText( ERR_SERVER_VERSION_NOT_INTACT));
		return false;
	}

	KHMsgLog_InfoW( L"using server url:%s\n", m_url.c_str() );

	BOOL bCacheFullyReady = false;	// 后台缓存完全达到最新版本。
	BOOL bCachePartlyReady = false; // 后台缓存达到某一中间完整版本。

	rHas = !PFSX::CompareVersion( m_localVersion, m_newVersionInfo) &&
		m_localVersion.m_Version <= m_newVersionInfo.m_Version &&  // 这里增加版本大小判断(允许相同是因为有可能仅仅变更了发布日期)
		m_localVersion.m_ReleaseDate <= m_newVersionInfo.m_ReleaseDate; // 一般情况检查新版本时，增加发布时间的判断（如果时间倒置，认为没有新版本），其他模式，不对日期进行判断。

	if ( rHas )
	{
		// 无论后台更新还是前台更新，都判断后台更新是否已经准备就绪.
		// 这个处理可以做到，当前台需要AUTOPATCH时，但后台散文件自动更新已经准备就绪，就没有必要启用AUTOPATH更新。
		//
		// 缓存目录没有版本文件,视为有版本需要更新

		// 缓存目录有版本文件,但可能已过期,视为有版本需要更新
		//
		if( PFSX::GetLocalVersion( m_cacheDir, m_cacheVersion) && m_cacheVersion.IsValid() )
		{
			bCacheFullyReady = PFSX::CompareVersion( m_cacheVersion, m_newVersionInfo );
			if( !bCacheFullyReady )
			{
				bCachePartlyReady = !PFSX::CompareVersion( m_localVersion, m_cacheVersion) &&
					m_localVersion.m_Version < m_cacheVersion.m_Version &&  // 这里增加版本大小判断(允许相同是因为有可能仅仅变更了发布日期)
					m_localVersion.m_ReleaseDate <= m_cacheVersion.m_ReleaseDate; 

				std::wstring cacheVersionString;
				PFSX::Version2String( m_cacheVersion.m_Version, cacheVersionString );
				KHMsgLog_InfoW( L"cache version: %s\n", cacheVersionString.c_str() );
			}
			else
			{
				bCachePartlyReady = true;
			}
		}

		if( !bCacheFullyReady)
		{
			std::wstring localVersionString;
			std::wstring newVersionString;
			PFSX::Version2String( m_localVersion.m_Version, localVersionString );
			PFSX::Version2String( m_newVersionInfo.m_Version, newVersionString );
			KHMsgLog_InfoW( L"local version: %s, new version: %s\n",localVersionString.c_str(), newVersionString.c_str() );

			GlobalNotify_TextID( TIP_HAS_NEW_VERSION );
		}
	}
	else
	{
		if( m_localVersion.m_Version == m_newVersionInfo.m_Version )
		{
			GlobalNotify_TextID( TIP_ALREADY_NEW_VERSION );
		}
		else
		{
			std::wstring localVersionString;
			std::wstring newVersionString;
			PFSX::Version2String( m_localVersion.m_Version, localVersionString );
			PFSX::Version2String( m_newVersionInfo.m_Version, newVersionString );
			KHMsgLog_InfoW( L"local version: %s, new version: %s\n",localVersionString.c_str(), newVersionString.c_str() );

			GlobalNotify_TextID( TIP_HAS_NOT_UPDATE_VERSION );
		}
	}

	if( rHas &&	m_localVersion.m_Version < m_newVersionInfo.m_luv )
	{
		do
		{
			int defaultType = m_updateServerType * PFSX::VERSION_COUNT + UPDATE_MODE_DEFAULT;
			int type = m_updateServerType * PFSX::VERSION_COUNT + UPDATE_MODE_AUTOPATCH;
			const PFSX::VersionUpdateInfo * pUpdateInfo = m_newVersionInfo.GetUpdateInfoByType(type);
			if ( pUpdateInfo == NULL )
			{
				type = (!m_updateServerType)*PFSX::VERSION_COUNT + UPDATE_MODE_AUTOPATCH;
				pUpdateInfo = m_newVersionInfo.GetUpdateInfoByType( type);
				if( NULL == pUpdateInfo )
				{
					GlobalNotify_TextID( TIP_NOT_FOUND_PATCH_URL );
					break;
				}
			}

			std::wstring url = pUpdateInfo->Url;
			if( url.empty() )
			{
				GlobalNotify_TextID( TIP_NOT_FOUND_PATCH_URL );
				break;
			}

			// AUTOPATCH配的url和DEFAULT模式配的url一样。
			const PFSX::VersionUpdateInfo *pUpdateInfoDefault = m_newVersionInfo.GetUpdateInfoByType(defaultType);
			if ( pUpdateInfoDefault != NULL )
			{
				if ( url == pUpdateInfoDefault->Url )
				{
					GlobalNotify_TextID( TIP_NOT_FOUND_PATCH_URL );
					break;
				}
			}

			PFSX::CPatchMetaFile pmf;
			//if(bCacheFullyReady || bCachePartlyReady)
			//{
			//	if( !PFSX::CPatchMetaFile::DoRead( m_cacheDir + PFSX::PATCH_META_NAME, pmf))
			//	{
			//		GlobalNotify_TextID( TIP_NOT_FOUND_PATCH_METAFILE );
			//		break;
			//	}
			//}
			//else
			{
				if( !PFSX::GetRemotePatchMetaFile( url, pmf ) )
				{
					GlobalNotify_TextID( TIP_NOT_FOUND_PATCH_METAFILE );
					break;
				}
			}

			PFSX::CPatchInfoVector piVector;
			if( !pmf.GetPatchInfosFromVersionEX( piVector, m_localVersion.m_Version, m_newVersionInfo.m_Version ) )
			{
				GlobalNotify_TextID( TIP_NOT_FOUND_PATCH );
				break;
			}

			// 为补丁包自动更新模式准备数据
			CUpdateManager& rUpdater = GetUpdateManager( UPDATE_MODE_AUTOPATCH );
			rUpdater.SetServerVersion( m_newVersionInfo );
			rUpdater.SetUpdateServerType( m_updateServerType );
			rUpdater.SetPatchInfoVector( piVector );

			// 启用补丁包自动更新
			rAutoPatch = true;
		} while( 0);
	}

	return true;
}

bool CUpdateManagerAutoPatch::Initialize( LPCTSTR szRunDir)
{
	GlobalNotify( L"%1!s!,%2!s!", szRunDir, _UpdateModeName( m_UpdateMode) );
	GlobalNotify( GlobalGetText( TIP_INITIALIZING).c_str() );

	m_runDir = szRunDir; // 游戏安装目录
	m_cacheDir = m_runDir + L".cache"; // UPDATE_MODE_REPAIR 模式没有执行HasNewVersion，所以这里需要初始化m_cacheDir
	//m_p2spDir = m_runDir + L".p2sp";

	//
	// 初始化本地版本信息
	//
	m_currentPatchIndex = 0;
	m_localVersion.Close();
	if( !PFSX::GetLocalVersion( szRunDir, m_localVersion) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_READ_LOCAL_VERSION).c_str());
		return false;
	}

	//
	// m_newVersionInfo 已经通过SetServerVersion() 初始化
	// 这里从中取得url
	// 
	m_url = GetVersionUrl( m_newVersionInfo, true, false );

	if( s_hBreakEvent == INVALID_HANDLE_VALUE)
		s_hBreakEvent = CreateEvent( NULL, TRUE, FALSE, NULL );
	else
	{
		ResetEvent( s_hBreakEvent );
	}

	if( !IsBackground())
	{
		// 检查游戏客户端是否在运行
		LPCWSTR lpszInstallationName = GetInstallationMutexName();
		if( NamedMutexExists( lpszInstallationName ) )
		{
			upd::SetLastError( SC_GAMECLIENT_RUNNING, GlobalGetText( ERR_CLIENT_IS_RUNNING) );
			return false;
		}
	}

	// 读本地 .setup.meta 文件
	m_smvLocal.clear();
	if( !PFSX::CSetupMetaFile::DoRead( m_runDir + PFSX::SETUP_META_NAME, m_smvLocal))
	{
		// .setup.meta 不存在的情况下也允许更新
		if( PFS::CEnv::GetLastError() != ERROR_FILE_NOT_FOUND)
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_READ_LOCAL_SETUPMETA) );
			return false;
		}
	}

	// 检查本地 pfs 文件是否存在
	for( PFSX::SetupMetaVector::iterator i= m_smvLocal.begin(), e=m_smvLocal.end(); i!=e; ++i)
	{
		if( !PFS::CFileIO::IsFileExisting( m_runDir+i->GetName() + L".pfs"))
		{
			i->SetHash( 0);
		}
	}
	
	// Mount运行系统
	GlobalNotify( GlobalGetText( TIP_LOAD_RUN_SYSTEM).c_str() );
	
	bool bOK = CUpdateMountManager::GetInstance().MountPFS( m_runDir, false, 
		s_hBreakEvent, m_smvLocal );

	if( bOK )
	{
		GlobalNotify( GlobalGetText( TIP_PREPARE_COMPLETE).c_str() );

		// 如果用户选择提前终止更新过程，那么此时执行Unmount过程并返回false
		if ( DetectBreakEvent() )
		{
			CUpdateMountManager::GetInstance().UnmountPFS();
			return false;
		}
	}

	return true;
}

bool CUpdateManagerAutoPatch::BeginLoop( )
{
	if( m_currentPatchIndex >= (int)m_patchInfos.size() )
	{
		upd::SetLastError(  PFS::EC_INVALID_META_FILE, GlobalGetText( ERR_PATCH_LIST_EMPTY).c_str());
		return false;
	}

	m_patchpath = m_patchInfos[m_currentPatchIndex].filename;
	m_patchhash = m_patchInfos[m_currentPatchIndex].hash;
	m_patchmd5  = m_patchInfos[m_currentPatchIndex].md5;
	PFS::fsize_t patchsize = m_patchInfos[m_currentPatchIndex].size;

	// 浮点数需要单独格式化，因为FormatMessage不支持。
	wchar_t buffer[256];
	swprintf_s( buffer, 256, L"%.2lf", patchsize/1048576.0f );

	GlobalNotify( GlobalGetText( TIP_DOWNLOAD_PROGRESS2).c_str(), m_patchpath.c_str(), buffer );
	//CResumeBrokenDownload downloader( m_patchpath , m_url, m_cacheDir, m_patchhash);
	PatchDownloader downloader( m_patchInfos[m_currentPatchIndex]);
	if( !downloader.Download( s_hBreakEvent, m_url, m_cacheDir) )
	{
		// 将patch文件本身作为下载失败的文件统计出来，避免更新版本信息
		m_mimDownloadFailed.insert( std::make_pair( m_patchpath, PFS::CMetaInfo() ) );
		return false;
	}

	// 修改hash path
	m_patchpath = m_cacheDir + m_patchpath;

	GlobalNotify( GlobalGetText( TIP_CHECK_PATCH_FILE).c_str(), m_patchpath.c_str());

	if( !PFS::CEnv::Mount( RES_ROOT, m_patchpath, PFS::FST_RUNZIP, PFS::MT_READONLY))
	{
		m_mimDownloadFailed.insert( std::make_pair( m_patchpath, PFS::CMetaInfo() ) );
		upd::SetLastError( PFS::EC_INVALID_PATCH, GlobalGetText( ERR_PATCH_FORMAT));
		return false;
	}

	// 读取cache中的版本信息
	m_newVersionInfo.Close();
	if( !PFSX::GetLocalVersion( RES_ROOT, m_newVersionInfo))
	{
		m_newVersionInfo.m_bValid = false;
		upd::SetLastError( PFS::EC_INVALID_PATCH, GlobalGetText( ERR_READ_PATCH_VERSION));
		PFS::CEnv::Unmount( RES_ROOT);
		return false;
	}

	m_smvServer.clear();
	if( !PFSX::CSetupMetaFile::DoRead( RES_ROOT+PFSX::SETUP_META_NAME, m_smvServer))
	{
		// 失败只记日志，因为补丁包中可能会没有 .setup.meta 文件
		int nError = PFS::CEnv::GetLastError();
		if( nError != PFS::EC_FILE_NOT_FOUND )
		{
			KHMsgLog_ErrorW( L"读补丁包 .setup.meta 文件失败!(%d)\n", nError );
		}
	}

	// 比较，本地版本号要不小于补丁包的“旧版本号”但小于其“新版本号”
	bool rHas = ( !PFSX::CompareVersion( m_localVersion, m_newVersionInfo)
		&& m_localVersion.m_Version < m_newVersionInfo.m_Version
		&& m_localVersion.m_Version >= m_newVersionInfo.m_lastVersion);
	if ( !rHas )
	{
		// 只判断版本号，不判断发布日期和hash值，方便必要时刷版本
		if( m_localVersion.m_Version == m_newVersionInfo.m_Version )
		{
			GlobalNotify( GlobalGetText( TIP_ALREADY_NEW_VERSION).c_str() );
			return true; // 当前版本与补丁包版本一样,此时返回true允许强制更新
		}
		else
		{
			PFS::CEnv::Unmount( RES_ROOT);

			PFS::CDataBlock dataVersionInfo, dataVersionInfoNew;
			m_localVersion.WriteTo(dataVersionInfo);
			const wchar_t ends = 0;
			dataVersionInfo.Append( &ends, 2 );
			m_newVersionInfo.WriteTo(dataVersionInfoNew);
			dataVersionInfoNew.Append(&ends, 2 );

			KHMsgLog_DebugW( L"local version:\n%s", dataVersionInfo.GetData() );
			KHMsgLog_DebugW( L"new version:\n%s", dataVersionInfoNew.GetData() );

			upd::SetLastError( PFS::EC_PATCH_NOT_MATCH, 
				GlobalGetText( ERR_PATCH_NOT_MATCH));
			GlobalNotify( GlobalGetText( ERR_PATCH_NOT_MATCH2).c_str() );

			m_newVersionInfo.m_bValid = false;
			return false;
		}
	}

	m_newVersionInfo.m_lastVersion = 0;		// 本地版本信息没有旧版本号

	if( !m_smvServer.empty() )
	{
		PFSX::SetupMetaVector smvNewAdded, smvDifference, smvUseless;
		AnalyzeSetupMetaUpdateInfo(m_smvServer, m_smvLocal, smvDifference, smvNewAdded, smvUseless );

		// 这里补充Mount新增的pfs子包。
		// 对于废弃的子包，暂时不处理。
		if( !smvNewAdded.empty() )
		{
			KHMsgLog_InfoW( L"INFO: Mount new pfs packages - Begin:" );
			bool bOK = CUpdateMountManager::GetInstance().MountPFSForNewAdded(
				m_runDir, s_hBreakEvent, smvNewAdded );
			if( !bOK )
			{
				KHMsgLog_ErrorW( L"ERROR: Mount new pfs packages - Failed." );
				return false;
			}
			KHMsgLog_InfoW( L"INFO: Mount new pfs packages - Done." );
		}
	}

	std::wstring localVersionString;
	std::wstring newVersionString;
	PFSX::Version2String( m_localVersion.m_Version, localVersionString );
	PFSX::Version2String( m_newVersionInfo.m_Version, newVersionString );
	KHMsgLog_InfoW( L"local version: %s, patch version: %s\n",localVersionString.c_str(), newVersionString.c_str() );

	GlobalNotify( GlobalGetText( TIP_AUTO_PATH_UPDATING).c_str() );

	return true;
}

bool CUpdateManagerAutoPatch::HasNextLoop()
{
	if( s_bNeedReboot )
	{
		return false;
	}

	if( ++m_currentPatchIndex < (int)m_patchInfos.size() )
		return true;
	else
		return false;
}

bool CUpdateManagerAutoPatch::Destroy( bool bSucceeded )
{
	if( !s_bNeedReboot )
	{
		GlobalNotify_TextID( TIP_START_DESTROY );
	}
	else
	{
		GlobalNotify_TextID( TIP_START_DESTROY2 );
	}

	if( !bSucceeded )
	{
		// 只有没有成功，这里才有必要记录日志，否则，Commit()时已经记录，这里没有必要重复记录了。

		// 如果以下3个文件隐藏或只读,此时强制设为非隐藏,且可写状态(暂不检查返回值)
		ShowHiddenFile( m_runDir + PFSX::FILES_META_NAME );
		ShowHiddenFile( m_runDir + PFSX::SETUP_META_NAME );
		ShowHiddenFile( m_runDir + PFSX::VERSION_META_NAME );

		bool bDownloadSucceeded = m_mimDownloadFailed.empty();
		if( !bDownloadSucceeded )
		{
			// 部分文件下载失败,记录这些下载失败的文件到日志中
			// Truncate the large file that more than 1M
			PurgeLogFile(( m_runDir + L"\\log\\downloadfailed.log").c_str(), 1024*1024 );

			std::wofstream ffailed;
			ffailed.open( (m_runDir + L"\\log\\downloadfailed.log").c_str(), std::ios_base::out | std::ios_base::app );
			if ( ffailed.is_open() )
			{
				DumpMetaInfoMap( m_mimDownloadFailed, ffailed );
			}
			else
			{
				KHMsgLog_ErrorW( L"Open file (%s,%d) failed.",
					(m_runDir + L"\\log\\downloadfailed.log").c_str(),
					(int)(std::ios_base::out | std::ios_base::app) );
			}

			m_mimDownloadFailed.clear();
		}
	}

	// unmount运行系统
	bool bRes = CUpdateMountManager::GetInstance().UnmountPFS();
	if( !bRes )
	{
		KHMsgLog_ErrorW( L"错误: UnmountPFS 失败(%d)!\n", PFS::CEnv::GetLastError() );
	}

	if( !s_bNeedReboot )
	{
		if( bSucceeded )
		{
			if( bRes )
			{
				if( PFSX::CompareVersion(m_cacheVersion, m_newVersionInfo) ||
					(m_newVersionInfo.m_Version >= m_cacheVersion.m_Version && m_newVersionInfo.m_ReleaseDate >= m_cacheVersion.m_ReleaseDate))
				{
					if( PFS::CFileIO::IsDirectoryExisting( m_cacheDir) )
					{
						// 删除临时目录
						GlobalNotify_TextID( TIP_DELETE_CACHE_FOLDER );
						if ( !DeleteCacheFolder( m_cacheDir ) )
						{
							DWORD dwLastError = GetLastError();
							KHMsgLog_ErrorW( L"错误: DeleteCacheFolder(%s)失败(%d)!\n", m_cacheDir.c_str(), dwLastError );
						}
					}
				}
			}
		}
		else
		{
			bool bUpdateSucceeded = (m_mimCached.size() == m_mimUpdate.size());
			if( !bUpdateSucceeded )
			{
				// 需要重起时不计算failed(因为当前的更新不是完全更新)
				PFS::CMetaInfoMap mimFailed;
				SubtractMetaInfoMap( m_mimCached, m_mimUpdate, mimFailed );

				// Truncate the large file that more than 1M
				PurgeLogFile(( m_runDir + L"\\log\\updatefailed.log").c_str(), 1024*1024 );

				// 写更新失败的文件列表
				std::wofstream ffailed;
				ffailed.open( (m_runDir + L"\\log\\updatefailed.log").c_str(), std::ios_base::out | std::ios_base::app );
				if ( ffailed.is_open() )
				{
					DumpMetaInfoMap( mimFailed, ffailed );
				}
				else
				{
					KHMsgLog_ErrorW( L"Open file (%s,%d) failed.",
						(m_runDir + L"\\log\\updatefailed.log").c_str(),
						(int)(std::ios_base::out | std::ios_base::app) );
				}

				m_mimCached.clear();
				m_mimCached.swap( mimFailed );
			}
		}
	}

	if( bSucceeded && bRes )
	{
		if( !s_bNeedReboot )
		{
			GlobalNotify_TextID( TIP_UPDATE_SUCCESS );
		}
		else
		{
			GlobalNotify_TextID( TIP_UPDATE_PART );
		}
	}
	else
	{
		GlobalNotify_TextID( ERR_UPDATE2 );
	}

	CloseHandle( s_hBreakEvent );
	s_hBreakEvent = INVALID_HANDLE_VALUE;
	return bRes;
}
