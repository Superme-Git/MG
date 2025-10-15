///============================================================================
///
/// UpdateManagerMergeCache.cpp - 合并后台缓存更新管理器
///
/// 此合并后台缓存管理器，用于后台更新已经部分或全部就绪，只需合并的情况。
/// 如果后台更新已经就绪至最新版本，只需合并。
/// 如果后台更新已经部分就绪（只到非最新版本），先合并，然后继续选择其他模式更新。
/// 
/// yanghaibo - 2011-10-21
///
///============================================================================
#include "StdAfx.h"
#include "UpdateManagerMergeCache.h"
#include "GlobalNotification.h"
#include "GlobalFunction.h"
#include "KHLogger.h"
#include "TextManager.h"
#include "TextID.h"
#include "LauncherConfig.h"

inline void GlobalNotify_TextID( int textId )
{
	GlobalNotify( GlobalGetText( textId ).c_str() );
}

bool CUpdateManagerMergeCache::HasNewVersion( 
	LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch, bool& rMergeCache )
{
	// 使用UPDATE_MODE_DEFAULT而不使用UPDATE_MODE_MERGECACHE进行新版本检查
	// 以保证即使使用UPDATE_MODE_MERGECACHE模式进行新版本检查，也能获取正确结果
	m_UpdateMode = UPDATE_MODE_DEFAULT;
	
	bool bRet = CUpdateManager::HasNewVersion( szRunDir, rHas, rLocalValid, rAutoPatch, rMergeCache );
	
	m_UpdateMode = UPDATE_MODE_MERGECACHE;
	
	return bRet;
}

bool CUpdateManagerMergeCache::Initialize( LPCTSTR szRunDir)
{
	GlobalNotify( L"%1!s!,%2!s!", szRunDir, _UpdateModeName( m_UpdateMode) );
	GlobalNotify_TextID( TIP_INITIALIZING );

	m_runDir = szRunDir; // 游戏安装目录
	m_cacheDir = m_runDir + L".cache"; 
	//m_p2spDir = m_runDir + L".p2sp";
	// UPDATE_MODE_MERGECACHE模式可能没有执行HasNewVersion，所以这里需要初始化m_cacheDir

	if( s_hBreakEvent == INVALID_HANDLE_VALUE)
		s_hBreakEvent = CreateEvent( NULL, TRUE, FALSE, NULL );
	else
	{
		ResetEvent( s_hBreakEvent );
	}

	// 检查游戏客户端是否在运行
	LPCWSTR lpszInstallationName = GetInstallationMutexName();
	if( NamedMutexExists( lpszInstallationName ) )
	{
		upd::SetLastError( SC_GAMECLIENT_RUNNING, GlobalGetText( ERR_CLIENT_IS_RUNNING) );
		return false;
	}

	// 缓存中的版本信息就是当前更新的目标版本，待缓存合并完毕之后，
	// 本地安装版本更新到此版本，而不是服务器最新版本。
	m_newVersionInfo.Close();
	if( !PFSX::GetLocalVersion( m_cacheDir, m_newVersionInfo) ||
		!m_newVersionInfo.IsValid() )
	{
		if( m_newVersionInfo.IsValid() )
		{
			KHMsgLog_WarningW(L"读取缓存版本信息失败，标记版本无效，并放弃更新。\n");

			// 缓存版本无效，为了防止下次更新启用合并缓存模式(MERGECACHE)，将缓存版本文件设置为valid=false;
			m_newVersionInfo.m_bValid = false;
			if( !PFSX::WriteVersionMetaFile( m_cacheDir + PFSX::VERSION_META_NAME,
				m_newVersionInfo, PFSX::VERSION_ALL_TYPES ) )
			{
				PFS::CFile::RemoveFile(m_cacheDir + PFSX::VERSION_META_NAME);
				KHMsgLog_ErrorW( L"标记版本文件无效失败，删除之 - %s(%d)",
					(m_cacheDir + PFSX::VERSION_META_NAME).c_str(), PFS::CEnv::GetLastError() );
			}
		}
		else
		{
			KHMsgLog_WarningW(L"缓存版本信息无效，放弃更新。\n");
		}

		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_MERGECACHE_FAILED));
		
		return false;
	}

	std::wstring localVersionString;
	std::wstring newVersionString;
	PFSX::Version2String( m_localVersion.m_Version, localVersionString );
	PFSX::Version2String( m_newVersionInfo.m_Version, newVersionString );
	KHMsgLog_InfoW( L"local version: %s, cache version: %s\n",localVersionString.c_str(), newVersionString.c_str() );

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
	for( PFSX::SetupMetaVector::iterator i= m_smvLocal.begin(), e = m_smvLocal.end(); i != e; ++ i )
	{
		if( !PFS::CFileIO::IsFileExisting( m_runDir + i->GetName() + L".pfs"))
		{
			i->SetHash( 0);
		}
	}

	// 读缓存SetupMeta
	m_smvServer.clear();
	PFSX::SetupMetaVector smvDifference;
	
	if( !PFSX::CSetupMetaFile::DoRead( m_cacheDir + PFSX::SETUP_META_NAME, m_smvServer))
	{
		// 失败只记日志，因为缓存中可能没有 .setup.meta 文件
		KHMsgLog_WarningW( L"WARN: read cache setup meta failed: %d\n" , PFS::CEnv::GetLastError());
		smvDifference = m_smvLocal;
	}
	else
	{
		// 生成 SetupMeta 的差异列表
		if ( m_smvLocal.IsOriginal() )
		{
			smvDifference = m_smvServer; // 使用服务器.setup.meta
			smvDifference.SetOriginal(false);

			std::wstringstream sstream;	
			DumpSetupMeta(L"\nlocal setup.meta is original, using setup.meta from cache: \n", sstream, smvDifference);
			KHMsgLog_DebugW( sstream.str().c_str() );
		}
		else
		{
			PFSX::SetupMetaVector smvNewAdded, smvUseless;
			AnalyzeSetupMetaUpdateInfo(m_smvServer, m_smvLocal, smvDifference, smvNewAdded, smvUseless );
		}
	}

	// Mount运行系统
	GlobalNotify_TextID( TIP_LOAD_RUN_SYSTEM );

	bool bOK = CUpdateMountManager::GetInstance().MountPFS( m_runDir, false,
		s_hBreakEvent, smvDifference);
	if( bOK )
	{
		GlobalNotify_TextID( TIP_PREPARE_COMPLETE );

		// 如果用户选择提前终止更新过程，那么此时执行Unmount过程并返回false
		if ( DetectBreakEvent() )
		{
			CUpdateMountManager::GetInstance().UnmountPFS();
			return false;
		}
	}

	return bOK;
}

bool CUpdateManagerMergeCache::CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete )
{
	// 简化此过程，不下载，不删除，只合并。
	m_mimDownload.clear();
	m_mimDownloadFailed.clear();
	m_mimCached.clear();
	m_mimDelete.clear();
	rDelete = 0;
	rDownload = 0;

	// 统计更新项目
	GlobalNotify_TextID( TIP_ARRANGE_UPDATE_LIST );

	// 读取已经缓存的文件
	GlobalNotify_TextID( TIP_READ_CACHE_META_FILE );

	bool bCached = ReadMetaInfoFile( m_cacheDir, m_mimCached );
	if( !bCached || m_mimCached.empty() )
	{
		// 读Cache中的MetaInfo文件失败，或cache为空。
		return false;
	}

	// 下面从RUN_ROOT读取metaInfoMap，然后与缓存中的metaInfoMap进行比较的过程是必不可少的。
	// 原因在于这里有一个Launcher重启的过程。
	// 在第一次执行MergeCache时，Launcher重启之前，下面这个过程的确没有必要。
	// 但由于在此不太容易得到Launcher是否重启过这个状态，这里也不设法取得。
	// Launcher重启之后，Launcher本身应该不再需要从缓存合并一次，否则会导致无限重启。
	// 重启之后，由于缓存MetaInfoMap在重启过程中没有更新，而RUN_ROOT中的MetaInfoMap会更新，
	// 因此单纯从缓存中获取的MetaInfoMap判断Launcher是否需要重启是不够的。
	PFS::CMetaInfoMap mimLocalRoot, mimNeedMerge;
	// 读本地 mount 目录的 .files.meta 文件
	if( !ReadMetaInfoFile( RUN_ROOT, mimLocalRoot))
	{
		// .file.meta 有可能不存在
		if( PFS::CEnv::GetLastError() != PFS::EC_FILE_NOT_FOUND)
		{
			return false;
		}
	}

	SubtractMetaInfoMap( m_mimCached, mimLocalRoot, mimNeedMerge );

	// 从config.xml中读取忽略更新文件列表。
	// 特别说明，为了测试更新逻辑，是否需要重启patcher和过滤器处理区别对待。
	// 如果将过滤器处理过程放在判断是否需要重启patcher之后进行，这样，即使过滤器已经过滤掉patcher中的所有需要更新的文件，
	// 可以做到该重启就重启。
	// 但依据上述想法简单处理会存在一个矛盾不可解决：
	// 对于重启前的处理，这种方式完全没有问题，而重启后，如果依旧照此顺序，会导致s_bNeedReboot依然为true，
	// 其结果就是，更新程序频繁重复重启，无法完成其他更新。
	// 当然，还有其他简单处理方式，比如重新实现Merge()方法，一次性合并所有更新项目，类似于UpdateManagerRepair::Merge()的处理。
	// 还有，将是否已经重启过的状态标记CUpdateThread::GetRebooted()拿到这里来判断并处理也可以解决此问题，
	// 分析来去，一者因为不容易直接拿到Rebooted这个标记，二者因为这个逻辑仅仅为了测试，必要性不是很大，暂时作罢。
	// 实在有必要的时候，可以考虑如下方案：
	// 1. UpdateThread将rebooted状态传给UpdateManager。
	// 2. 从_tcsstr( theApp.m_lpCmdLine, L"-r ")这个调用的返回值判断。
	// 2011-10-25
	EraseMetaInfosByIgnoringConfig( mimNeedMerge );

	m_mimCached.swap( mimNeedMerge );
	s_bNeedReboot = CUpdateManager::IsNeedUpdateLauncher( m_mimCached );
	rMerge = m_mimCached.size();

	if( rMerge > 0 )
	{
		GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT2).c_str(), rMerge );
	}
	else
	{
		GlobalNotify_TextID( TIP_NEED_NOT_UPDATE );
	}

	if( DetectBreakEvent() )
	{
		return false;
	}

	return true;
}
