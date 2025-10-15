///=============================================================================
/// Product   : MHZX
/// Project   : Launcher
/// Module    : UpdateManager.cpp
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	更新管理器
///=============================================================================
/// Revision History :
/// 	V1.0 - 2008-12-11 Created.
///=============================================================================
#include "StdAfx.h"
#include "UpdateManager.h"
#include "ErrorInfo.h"
#include "GlobalNotification.h"
#include "UpdateManagerPatch.h"
#include "UpdateManagerRepair.h"
#include "../../pfs/setupmetafile.h"
#include "../../pfs/stringhelper.h"
#include "UpdateManagerAutoPatch.h"
#include "LauncherConfig.h"
#include "KHLogger.h"
#include "TextManager.h"
#include "TextID.h"

inline void GlobalNotify_TextID( int textId )
{
	GlobalNotify( GlobalGetText( textId ).c_str() );
}

bool EvalUpdateItemsForRoot(
					  const std::wstring& url,			// 保证非空
					  const std::wstring& rundir,		// 运行目录
					  PFS::CMetaInfoMap& rMimServer,	// 服务器根目录文件列表
					  PFS::CMetaInfoMap& rMimLocal,		// 本地根目录文件列表
					  PFS::CMetaInfoMap& rMimDifference	// 服务器根目录和本地根目录的差异列表
					  )

{
	rMimServer.clear();
	rMimLocal.clear();
	rMimDifference.clear();

	// 读本地 mount 目录的 .files.meta 文件
	if( !ReadMetaInfoFile( RUN_ROOT, rMimLocal))
	{
		// .file.meta 有可能不存在
		if( PFS::CEnv::GetLastError() != PFS::EC_FILE_NOT_FOUND)
		{
			return false;
		}
	}

	// 读服务器 mount 目录的 .files2.meta 文件
	GlobalNotify_TextID( TIP_DOWNLOAD_META_FILE );
	if( !DownloadMetaInfoFile2( url, rMimServer))
	{
		return false;
	}

	// 计算2个MetaInfoMap的差集(本次需更新的文件列表 - 只针对根)
	SubtractMetaInfoMap( rMimServer, rMimLocal, rMimDifference);

	// always true!
	return true;
}

bool CUpdateManager::IsNeedUpdateLauncher( const PFS::CMetaInfoMap& mim)
{
	// TODO: 考虑将这个值用成员变量保存下来（通过外部传入）
	std::wstring strLauncherFolder = L"/";
	strLauncherFolder += GetLauncherFolder();
	strLauncherFolder += L"/";
	std::wstring strLauncherFolderEnd = strLauncherFolder;
	strLauncherFolderEnd[strLauncherFolder.size()-2]++;

	// 计算是否需要更新Launcher和Patcher相关的文件,以确定是否需要重启patcher
	PFS::CMetaInfoMap::const_iterator rBegin = mim.lower_bound( strLauncherFolder );
	PFS::CMetaInfoMap::const_iterator rEnd = mim.lower_bound( strLauncherFolderEnd );
	if( std::distance( rBegin, rEnd) != 0)
	{
		return true;
	}

	return false;
}

bool EvalUpdateItems2(
					 const std::wstring& url,				// 保证非空
					 const std::wstring& rundir,			// 运行目录
					 const std::wstring& cacheDir,			// 缓存目录
					 const PFSX::SetupMetaVector& smvServer,// 服务器最新的 SetupMeta 信息
					 const PFSX::SetupMetaVector& smvDiff,	// 需要更新的 SetupMeta 信息
					 PFS::CMetaInfoMap& rMimDownload,		// 需要下载的文件列表
					 PFS::CMetaInfoMap& rMimCached,			// 缓存中已有的需要直接合并的文件列表,不含将下载的文件
					 PFS::CMetaInfoMap& rDelete,			// 本地需要删除的文件列表
					 bool& rbNeedUpdateLauncher,			// 是否需要更新launcher/patcher
					 bool bIsBackground,						// 是否是后台更新
					 HANDLE hBreakEvent
					 )
{
	rMimDownload.clear();
	rMimCached.clear();
	rDelete.clear();
	rbNeedUpdateLauncher = false;

	KHMsgLog_DebugW( L"正在计算 %s 中的更新列表.\n", rundir.c_str() );
	PFS::CMetaInfoMap mimRootServer, mimRootLocal;
	if( !EvalUpdateItemsForRoot( url, rundir, mimRootServer, mimRootLocal, rMimDownload) )
	{
		return false;
	}

	if( !rMimDownload.empty() )
	{
		KHMsgLog_DebugW( L"%s 中需要更新 %lu 项。\n", rundir.c_str(), rMimDownload.size() );
	}

	rbNeedUpdateLauncher = false;
	if( !bIsBackground)
	{
		rbNeedUpdateLauncher = CUpdateManager::IsNeedUpdateLauncher( rMimDownload );
	}

	// 统计根上需要删除的文件
	SubtractMetaInfoMapEx( mimRootLocal, mimRootServer, rDelete);

	// 移除不能删除的文件
	DealUnDeleteFiles( rDelete, smvServer );
	if( !rDelete.empty() )
	{
		KHMsgLog_DebugW( L"%s 中需要删除 %lu 项。\n", rundir.c_str(), rDelete.size() );
	}

	if( !rbNeedUpdateLauncher)
	{
		// 为了避免重复下载子包的meta信息,我们根据是否需要重启launcher来决定是否对子包进行处理.
		// 当launcher需要自我更新时,launcher会重启,重启前不处理子包的更新,以避免重启后重复下载子包meta.

		// 使用断点续传机制下载子包的.files2.meta文件，下载的文件放到缓存对应的目录下
		CSetupMetasDownloader downloader( smvDiff, url, cacheDir );
		if( !downloader.Download( hBreakEvent ) )
		{
			GlobalNotify_TextID( ERR_DOWNLOAD_CHILD_META_FILE );
			return false;
		}

		for( PFSX::SetupMetaVector::const_iterator iV=smvDiff.begin(); iV!=smvDiff.end(); ++iV)
		{
			const std::wstring& mount = iV->GetName();

			// 读本地 mount 目录的 .files.meta 文件
			GlobalNotify( GlobalGetText( TIP_ANALYZE_CHILD_META_FILE).c_str(), RUN_ROOT, mount.c_str());

			PFS::CMetaInfoMap mimLocal;
			if( !ReadMetaInfoFile( RUN_ROOT + mount, mimLocal))
			{
				// .file.meta 有可能不存在
				int nErrorCode = PFS::CEnv::GetLastError();
				if( nErrorCode != ERROR_PATH_NOT_FOUND &&
					nErrorCode != ERROR_FILE_NOT_FOUND &&
					nErrorCode != PFS::EC_FILE_NOT_FOUND )
				{
					return false;
				}
			}

			// 读cache目录mount子目录的 .files2.meta 文件
			GlobalNotify( GlobalGetText( TIP_ANALYZE_CHILD_META_FILE).c_str(), RES_ROOT, mount.c_str());

			PFS::CMetaInfoMap mimCache;

			// 注意，这里使用cacheDir，而不使用RES_ROOT，因为缓存中的文件是以CNativeFile方式创建的。
			if( !ReadMetaInfoFile2( cacheDir + mount, mimCache ) )
			{
				return false;
			}

			if( mimLocal.size() != mimCache.size() )
			{
				KHMsgLog_DebugW( L"%s 中本地有 %lu 项，服务器有 %lu 项。\n", mount.c_str(),
					mimLocal.size(), mimCache.size() );
			}

			PFS::CMetaInfoMap mimDelete;
			// 统计需要删除的文件
			SubtractMetaInfoMapEx( mimLocal, mimCache, mimDelete );
			mimDelete.erase( PFSX::FILES_META_NAME);

			if( !mimDelete.empty() )
			{
				KHMsgLog_DebugW( L"%s 中需要删除 %lu 项。\n", mount.c_str(), mimDelete.size() );
				if( !bIsBackground )
				{
					std::wstringstream woss;
					woss << L"\n";
					for( PFS::CMetaInfoMap::iterator i=mimDelete.begin(), e=mimDelete.end(); i!=e; ++i)
					{
						rDelete.insert( std::make_pair( mount+i->first, i->second));
						woss << i->first <<std::endl;
					}
					KHMsgLog_DebugW( woss.str().c_str() );
				}
			}

			PFS::CMetaInfoMap mimDownload;
			// 计算2个MetaInfoMap的差集
			SubtractMetaInfoMap( mimCache, mimLocal, mimDownload);
			if( !mimDownload.empty() )
			{
				KHMsgLog_DebugW( L"%s 中需要下载 %lu 项。\n", mount.c_str(), mimDownload.size() );
				for( PFS::CMetaInfoMap::iterator i=mimDownload.begin(), e=mimDownload.end(); i!=e; ++i)
				{
					rMimDownload.insert( std::make_pair( mount+i->first, i->second));
				}
			}
		}
	}

	rMimDownload.erase( PFSX::VERSION_META_NAME );	// .version.meta 先不下载最后处理
	rMimDownload.erase( PFSX::SETUP_META_NAME);		// .setup.meta 同上

	if( rMimDownload.empty() )
	{
		// 没有文件需要更新
		return true;
	}

	//
	// 进一步考虑已经缓存的文件
	//

	GlobalNotify_TextID( TIP_READ_CACHE_META_FILE );

	PFS::CMetaInfoMap mimCache;
	bool bCached = ReadMetaInfoFile( cacheDir, mimCache );
	if( !bCached )
	{
		// 读Cache中的MetaInfo文件失败，不放弃更新
	}

	if( !mimCache.empty() )
	{
		PFS::CMetaInfoMap mimTemp;
		mimTemp.swap( rMimDownload );
		SubtractMetaInfoMap( mimTemp, mimCache, rMimDownload );

		if( !rMimDownload.empty() )
		{
			SubtractMetaInfoMap( mimTemp, rMimDownload, rMimCached );
		}
		else
		{
			rMimCached.swap( mimTemp );
		}
	}

	// always true!
	return true;
}

// static member initialization:
volatile HANDLE CUpdateManager::s_hBreakEvent = INVALID_HANDLE_VALUE;
bool CUpdateManager::s_bNeedReboot = false;

CUpdateManager::CUpdateManager(UPDATE_MODE updateMode)
	: m_UpdateMode(updateMode)
	, m_updateServerType( UPDATE_SERVER_NETCOM)
	, m_patchhash( 0 )
{}

class CUpdateManagerDummy : public CUpdateManager
{
public:
	CUpdateManagerDummy ()
		: CUpdateManager( UPDATE_MODE_DEFAULT )
	{
	}

	virtual bool HasNewVersion( LPCTSTR, bool& rHas, bool&, bool& )
	{
		rHas = false;
		return true;
	}
};

class CUpdateManagerDifferential : public CUpdateManager
{
public:
	CUpdateManagerDifferential()
		: CUpdateManager( UPDATE_MODE_DEFAULT )
	{
	}
};

class CUpdateManagerWhole : public CUpdateManager
{
public:
	CUpdateManagerWhole()
		: CUpdateManager( UPDATE_MODE_WHOLE)
	{
	}

	virtual bool Initialize( LPCTSTR szRunDir);
	virtual bool CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete );
	virtual bool Merge();
};

class CUpdateManagerBackground : public CUpdateManager
{
public:
	CUpdateManagerBackground()
		: CUpdateManager( UPDATE_MODE_BACKGROUND )
	{
	}

	virtual bool Purge() { return true; }	// Do nothing!
	virtual bool Merge();
	virtual bool Commit() { return true; } // Do nothing!
	virtual bool Destroy( bool bSucceeded );
};

CUpdateManager& GetUpdateManager(UPDATE_MODE eUpdateMode)
{
	class CUpdateManagerHolder
	{
	public:
		CUpdateManager* GetUpdateManager( int mode )
		{
			CUpdateManager*& ret = updaters[mode];
			if ( ret == NULL )
			{
				ret = _CreateUpdater(mode);
			}

			return ret;
		}

		~CUpdateManagerHolder()
		{
			std::map< int, CUpdateManager* >::iterator itr = updaters.begin();
			for ( ; itr != updaters.end(); ++ itr )
			{
				delete itr->second;
			}
		}

	private:
		CUpdateManager* _CreateUpdater( int mode )
		{
			switch ( mode )
			{
			case UPDATE_MODE_DEFAULT:
				return new CUpdateManagerDifferential();
			case UPDATE_MODE_WHOLE:
				return new CUpdateManagerWhole();
			case UPDATE_MODE_BACKGROUND:
				return new CUpdateManagerBackground();
			case UPDATE_MODE_PATCH:
				return new CUpdateManagerPatch();
			case UPDATE_MODE_REPAIR:
				return new CUpdateManagerRepair();
			case UPDATE_MODE_AUTOPATCH:
				return new CUpdateManagerAutoPatch();
			default:
				return new CUpdateManagerDummy();
			}
		}

	private:
		std::map< int, CUpdateManager* > updaters;
	};

	static CUpdateManagerHolder _Instance;
	return *_Instance.GetUpdateManager(eUpdateMode);
}

LPCWSTR _UpdateModeName( int mode )
{
	switch ( mode )
	{
	case UPDATE_MODE_DEFAULT:
		return L"DEFAULT";
	case UPDATE_MODE_WHOLE:
		return L"WHOLE";
	case UPDATE_MODE_BACKGROUND:
		return L"BACKGROUND";
	case UPDATE_MODE_PATCH:
		return L"PATCH";
	case UPDATE_MODE_REPAIR:
		return L"REPAIR";
	case UPDATE_MODE_AUTOPATCH:
		return L"AUTOPATCH";
	default:
		return L"<unknown>";
	}
}

// 获取指定版本信息中的url
std::wstring CUpdateManager::GetVersionUrl(
	const PFSX::CVersionInfo& rVersionInfo, bool bUseConfigIfNeeded, bool bUseDefault ) const
{
	int type = m_updateServerType*PFSX::VERSION_COUNT + m_UpdateMode;
	const PFSX::VersionUpdateInfo *pUpdateInfo = rVersionInfo.GetUpdateInfoByType(type);
	if( NULL == pUpdateInfo )
	{
		GlobalNotify_TextID( WRN_MISSING_DEFAULT_URL );
		// 尝试取另一种服务器(备用服务器)的类型获取更新URL
		type = (!m_updateServerType)*PFSX::VERSION_COUNT + m_UpdateMode;
		if( NULL == ( pUpdateInfo = rVersionInfo.GetUpdateInfoByType( type ) ) )
		{
			GlobalNotify_TextID( WRN_MISSING_BACKUP_URL );
			// 如果指定type不存在，则使用默认type保证能继续更新
			pUpdateInfo = rVersionInfo.GetUpdateInfoByType( PFSX::VERSION_DEFAULT );
			if( pUpdateInfo != NULL )
			{
				GlobalNotify( GlobalGetText( WRN_MISSING_DEFAULT_URL2).c_str(), pUpdateInfo->Url.c_str() );
			}
		}
	}

	if ( pUpdateInfo != NULL )
	{
		return pUpdateInfo->Url;
	}

	std::wstring url;
	if( bUseConfigIfNeeded )
	{
		// 通过配置文件获取url
		CProfile::GetInstance().GetServerUrl( url, m_UpdateMode );
		if( !url.empty() )
		{
			GlobalNotify( GlobalGetText( WRN_USE_SETTING_URL).c_str(), url.c_str() );
			return url;
		}
	}

	if( bUseDefault )
	{
		// 以下为当前版本缺省提供的url
		CProfile::GetInstance().GetDefaultUrl( url, m_updateServerType, m_UpdateMode);
		GlobalNotify( GlobalGetText( WRN_USE_DEFAULT_VALUE).c_str(), url.c_str() );
	}

	return url;
}

bool CUpdateManagerWhole::Initialize( LPCTSTR szRunDir)
{
	m_runDir = szRunDir; // 游戏安装目录
	if( s_hBreakEvent == INVALID_HANDLE_VALUE)
		s_hBreakEvent = CreateEvent( NULL, TRUE, FALSE, NULL );
	else
	{
		ResetEvent( s_hBreakEvent );
	}

	return true;
}

bool CUpdateManager::Initialize( LPCTSTR szRunDir)
{
	GlobalNotify( L"%1!s!,%2!s!", szRunDir, _UpdateModeName( m_UpdateMode) );
	GlobalNotify_TextID( TIP_INITIALIZING );

	m_runDir = szRunDir; // 游戏安装目录
	m_cacheDir = m_runDir + L".cache"; // UPDATE_MODE_REPAIR 模式没有执行HasNewVersion，所以这里需要初始化m_cacheDir

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
		if( !PFS::CFileIO::IsFileExisting( m_runDir+i->GetName()+L".pfs"))
		{
			i->SetHash( 0);
		}
	}

	// 生成 SetupMeta 的差异列表
	PFSX::SetupMetaVector smvDifference;
	if( m_smvServer.empty() )
	{
		smvDifference = m_smvLocal;
	}
	else
	{
		if ( m_smvLocal.IsOriginal() || // 跨版本处理.setup.meta的问题
			!m_localVersion.IsValid() )	// 本地版本无效时，处理所有pfs包
		{
			smvDifference = m_smvServer; // 使用服务器.setup.meta
			smvDifference.SetOriginal(false);

			std::wstringstream sstream;
			sstream	<< L"\nlocal setup.meta is original or version is not valid, using setup.meta from server: \n";
			for( PFSX::SetupMetaVector::const_iterator i = m_smvServer.begin(); i!=m_smvServer.end(); ++i)
			{
				sstream	<< i->GetName() << L": " << PFSX::CStringHelper::Hash2String(i->GetHash()) << L", " 
					<< PFSX::CStringHelper::Hash2String(i->GetHash2() ) << std::endl;
			}

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
	// 对于后台更新，这里以只读模式Mount运行系统
	bool bOK = CUpdateMountManager::GetInstance().MountPFS( m_runDir, IsBackground(),
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

// 注意：检查更新过程不要求Initialize，因此需要指定runDir
bool CUpdateManager::HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch )
{
	GlobalNotify_TextID( TIP_CHECK_LOCAL_VERSION );

	m_runDir = szRunDir;
	m_cacheDir = m_runDir + L".cache";
	m_newVersionInfo.Close();
	rAutoPatch = false;

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

	BOOL bCacheReady = false;

	// 比较
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
		PFSX::CVersionInfo cacheVersion;
		if( PFSX::GetLocalVersion( m_cacheDir, cacheVersion) )
		{
			bCacheReady = PFSX::CompareVersion( cacheVersion, m_newVersionInfo );
		}

		if( bCacheReady && IsBackground() )
		{
			// 后台更新已完成的话，对于后台更新模式来说，就是没有新版本需要更新。
			rHas = false;
			GlobalNotify_TextID( TIP_BACKGROUND_UPDATE_COMPLETE );
		}
		else
		{
			std::wstring localVersionString;
			std::wstring newVersionString;
			PFSX::Version2String( m_localVersion.m_Version, localVersionString );
			PFSX::Version2String( m_newVersionInfo.m_Version, newVersionString );
			KHMsgLog_InfoW( L"local version: %s, new version: %s\n",localVersionString.c_str(), newVersionString.c_str() );

			if ( IsBackground() )
			{
				GlobalNotify_TextID( TIP_BACKGROUND_HAS_NEW_VERSION );
			}
			else
			{
				GlobalNotify_TextID( TIP_HAS_NEW_VERSION );
			}
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

	// 无论什么更新模式，缓存就绪情况下，都没有必要处理LUV。
	// 后台更新暂时不处理LUV(当后台更新也处理LUV时，修改此处逻辑)
	if( rHas &&
		!bCacheReady &&
		!IsBackground() &&
		m_localVersion.m_Version < m_newVersionInfo.m_luv )
	{
		do
		{
			int defaultType = m_updateServerType*PFSX::VERSION_COUNT + UPDATE_MODE_DEFAULT;
			int type = m_updateServerType*PFSX::VERSION_COUNT + UPDATE_MODE_AUTOPATCH;
			const PFSX::VersionUpdateInfo *pUpdateInfo = m_newVersionInfo.GetUpdateInfoByType(type);
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
			if( !PFSX::GetRemotePatchMetaFile( url, pmf ) )
			{
				GlobalNotify_TextID( TIP_NOT_FOUND_PATCH_METAFILE );
				break;
			}

			PFSX::CPatchInfoVector piVector;
			if( !pmf.GetPatchInfosFromVersion( piVector, m_localVersion.m_Version, m_newVersionInfo.m_Version))
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

		} while( 0 );
	}

	if( ( rHas || !rLocalValid ) && !rAutoPatch )
	{
		m_smvServer.clear();
		bool bSetupMetaReady = false;
		if( bCacheReady )
		{
			// 缓存就绪的情况下，从缓存读 .setup.meta 文件(有可能不存在)
			bSetupMetaReady = PFSX::CSetupMetaFile::DoRead( m_cacheDir + PFSX::SETUP_META_NAME, m_smvServer);
			if( !bSetupMetaReady )
			{
				// bCacheReady = false; // 此变量不再使用，暂时没有必要修改为false。
				KHMsgLog_WarningW( L"Read SetupMetaFile failed: %s(%d)\n",
					(m_cacheDir + PFSX::SETUP_META_NAME).c_str(), PFS::CEnv::GetLastError() );
			}
		}

		// 缓存没有就绪或者从缓存读取失败，该从服务器下载 .setup.meta 文件
		if( !bSetupMetaReady && !ReadRemoteSetupMeta( m_url, m_smvServer))
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_GET_SERVER_SETUPMETA) );
			return false;
		}
	}

	return true;
}

bool CUpdateManagerWhole::CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete )
{
	m_mimDownloadFailed.clear();
	m_mimDownload.clear();
	m_mimCached.clear();
	m_mimDelete.clear();

	rDelete = 0;

	// 从服务器下载所有文件列表
	// 疑问：打包方式下载是不是更快更好？
	GlobalNotify_TextID( TIP_DOWNLOAD_META_FILE );
	if( !DownloadMetaInfoFile( m_url, m_mimDownload) )
	{
		m_mimDownloadFailed.insert( std::make_pair( PFSX::FILES_META_NAME, PFS::CMetaInfo() ) );
		return false;
	}

	if( DetectBreakEvent() )
	{
		return false;
	}

	rDownload = m_mimDownload.size();
	size_t nCached = m_mimCached.size();
	if( rDownload > 0 )
	{
		if( rMerge > 0 )
			GlobalNotify( GlobalGetText( TIP_UPDATE_FILE_COUNT).c_str(), rMerge );
		else if( nCached > 0 )
			GlobalNotify( GlobalGetText( TIP_DOWNLOAD_MERGE_FILE_COUNT).c_str(), rDownload, nCached );
		else
			GlobalNotify( GlobalGetText( TIP_DOWNLOAD_FILE_COUNT2).c_str(), rDownload );
	}
	else if( nCached > 0 )
	{
		GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT2).c_str(), nCached );
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

bool CUpdateManager::CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete )
{
	m_mimDownload.clear();
	m_mimDownloadFailed.clear();
	m_mimCached.clear();
	m_mimDelete.clear();
	rDelete = 0;

	// 获取新版本url
	m_url = GetVersionUrl( m_newVersionInfo, true, false );
	if( m_url.empty() )
	{
		ASSERT( !m_url.empty() );
		PFS::CEnv::SetLastError(PFS::EC_INVALID_VERSION_INFO );
		upd::SetLastError( PFS::EC_INVALID_VERSION_INFO, GlobalGetText( ERR_SERVER_VERSION_NOT_INTACT));
		return false;
	}

	// 保存到配置文件
	if( IsBackground())
	{
		CProfile::GetInstance().SetServerUrl( m_url, UPDATE_MODE_BACKGROUND );
	}
	else
	{
		CProfile::GetInstance().SetServerUrl( m_url, UPDATE_MODE_DEFAULT );
	}

	// 统计更新项目
	GlobalNotify_TextID( TIP_ARRANGE_UPDATE_LIST );
	if( !EvalUpdateItems2( m_url, m_runDir, m_cacheDir,
		m_smvServer, CUpdateMountManager::GetInstance().GetSetupMetaVector(),
		m_mimDownload, m_mimCached, m_mimDelete, s_bNeedReboot, IsBackground(), s_hBreakEvent ) )
	{
		m_mimDownloadFailed.insert( std::make_pair( PFSX::FILES_META_NAME, PFS::CMetaInfo() ) );
		return false;
	}

	// 对于后台更新,不执行合并动作
	if( IsBackground())
	{
		rDelete = 0;
		rMerge = 0;
	}
	else
	{
		rDelete = m_mimDelete.size();
		// 下面的加法运算可能不准确,但是无关紧要
		rMerge = m_mimCached.size() + m_mimDownload.size();
	}

	if( DetectBreakEvent() )
	{
		return false;
	}

	rDownload = m_mimDownload.size();
	size_t nCached = m_mimCached.size();
	if( rDownload > 0 )
	{
		if( rMerge > 0 )
			GlobalNotify( GlobalGetText( TIP_UPDATE_FILE_COUNT).c_str(), rMerge );
		else if( nCached > 0 )
			GlobalNotify( GlobalGetText( TIP_DOWNLOAD_MERGE_FILE_COUNT).c_str(), rDownload, nCached );
		else
			GlobalNotify( GlobalGetText( TIP_DOWNLOAD_FILE_COUNT2).c_str(), rDownload );
	}
	else if( nCached > 0 )
	{
		GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT2).c_str(), nCached );
	}
	else
	{
		// 这里不用写版本信息，因为最后 Destroy 会统一处理
		if( rDelete > 0 )
		{
			GlobalNotify( GlobalGetText( TIP_DELETE_FILE_COUNT).c_str(), rDelete );
		}
		else
		{
			GlobalNotify_TextID( TIP_NEED_NOT_UPDATE );
		}
	}

	if( DetectBreakEvent() )
	{
		return false;
	}

	return true;
}

//
// 下载新版本文件到临时目录中
//
bool CUpdateManager::Download( )
{
	GlobalNotify_TextID( TIP_SAVE_NEW_VERSION );

	if( DetectBreakEvent() )
	{
		return false;
	}

	std::wstring briefDir;	// Brief目录
	briefDir = m_runDir + L".brief";
	CreateHiddenDirectory( briefDir);

	// 在下载之前，首先根据更新模式保存版本信息文件。
	PFSX::CVersionInfo temp;
	temp.AddUpdateInfo( PFSX::VersionUpdateInfo( PFSX::VERSION_DEFAULT, m_url));
	if( !PFSX::WriteVersionMetaFile( briefDir + PFSX::VERSION_META_NAME, temp))
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_SAVE_NEW_VERSION) );
		return false;
	}

	if( DetectBreakEvent() )
	{
		return false;
	}

	GlobalNotify_TextID( TIP_SAVE_NEW_FILE_INFO );
	// 将需要下载的文件列表信息保存到BRIEF目录中，用于执行下载
	if( !WriteMetaInfoFile( briefDir, m_mimDownload ) )
	{
		GlobalNotifyErrorInfo( upd::GetLastError(), GlobalGetText( ERR_SAVE_UPDATE_META_FILE).c_str() );
		return false;
	}
	else
	{
		GlobalNotify_TextID( TIP_SAVE_UPDATE_META_FILE );
	}

	// 不再需要这些信息
	m_mimDownload.clear();

	if( DetectBreakEvent() )
	{
		return false;
	}

	m_mimDownloadFailed.clear();

	// 下载文件
	GlobalNotify_TextID( TIP_START_DOWNLOAD );
	CDownloadFiles downloader( m_runDir );
	bool res = downloader.Download(s_hBreakEvent);

	// 不再需要 briefDir
	if( !DeleteCacheFolder( briefDir ) )
	{
		upd::SetLastError( ::GetLastError(), GlobalFormatText( ERR_DELETE_BRIEF_FOLDER, briefDir.c_str()) );
	}

	if( !res)
	{
		// 下载失败的项目
		PFS::CMetaInfoMap& rmimFailed = downloader.GetFailed();
		if ( !rmimFailed.empty() )
		{
			m_mimDownloadFailed.swap( rmimFailed );
		}
		GlobalNotifyErrorInfo( upd::GetLastError(), GlobalGetText( ERR_DOWNLOAD_UPDATE_ITEM).c_str() );

		return false;
	}

	if( !DetectBreakEvent() )
	{
		GlobalNotify_TextID( TIP_DOWNLOAD_UPDATE_ITEM );
	}
	else
	{
		// 尽管用户要退出更新，我们也要处理一下现在已经下载的文件列表
		// 否则就导致白白下载大量文件
		// 且会导致版本信息文件被无故更新
	}

	GlobalNotify_TextID( TIP_SAVE_CACHE_META_FILE );

	// 下载成功的项目
	PFS::CMetaInfoMap& rmimDownloaded = downloader.GetDownloaded();

	//////////////////////////////////////////////////////////////////////////
	// 此时Cache目录中的.files.meta应该已经更新
	// 这里直接合并2个MetaInfo即可.
	// 合并已下载的和缓存中已有的.
	// 小小优化：将小集合合并到大集合中，然后求并集
	if( m_mimCached.size() >= rmimDownloaded.size() )
	{
		m_mimCached.insert( rmimDownloaded.begin(), rmimDownloaded.end() );
	}
	else
	{
		rmimDownloaded.insert( m_mimCached.begin(), m_mimCached.end() );
		m_mimCached.swap( rmimDownloaded );
	}

	// 检测用户退出的信号
	if( DetectBreakEvent() )
	{
		return false;
	}

	return true;
}


//
//	清理客户端中废弃的文件
//
bool CUpdateManager::Purge( )
{
	if( DetectBreakEvent() )
	{
		return false;
	}

	if( !s_bNeedReboot )
	{
		// 删除本地文件
		if( !DeleteLocalFiles())
		{
			// 要处理出错的情况么？
		}
	}

	return true;
}

// 删除本地文件
bool CUpdateManager::DeleteLocalFiles()
{
	if( m_mimDelete.empty() )
	{
		return true;
	}

	GlobalNotify( GlobalGetText( TIP_DELETE_FILE_COUNT2).c_str(), m_mimDelete.size() );
	GlobalNotifyStart( (int) m_mimDelete.size());
	for( PFS::CMetaInfoMap::iterator itor = m_mimDelete.begin(); itor != m_mimDelete.end(); ++itor)
	{
		if( DetectBreakEvent() )
		{
			return false;
		}
		GlobalNotifyStep( itor->first);
		if( !PFS::CEnv::RemoveFile( RUN_ROOT + itor->first))
		{
			int nLastError = PFS::CEnv::GetLastError();
			if( PFS::EC_FILE_NOT_FOUND == nLastError ||
				ERROR_FILE_NOT_FOUND == nLastError ||
				ERROR_PATH_NOT_FOUND  == nLastError )
			{
				continue;
			}
			else
			{
				// 出错不用返回
				upd::SetLastError( PFS::CEnv::GetLastError(),
					GlobalFormatText( ERR_DELETE_FILE, itor->first.c_str()) );
			}
		}
	}
	GlobalNotifyEnd();

	// Truncate the large file that more than 1M
	PurgeLogFile(( m_runDir + L"\\log\\deleted.log").c_str(), 1024*1024 );

	// 写删除的文件列表到日志中，方便调试
	std::wofstream fDeleted;
	fDeleted.open( (m_runDir + L"\\log\\deleted.log").c_str(), std::ios_base::out | std::ios_base::app );
	if ( fDeleted.is_open() )
	{
		DumpMetaInfoMap( m_mimDelete, fDeleted );
	}
	else
	{
		KHMsgLog_ErrorW( L"Open file (%s,%d) failed.",
			(m_runDir + L"\\log\\deleted.log").c_str(),
			(int)(std::ios_base::out | std::ios_base::app) );
	}

	return true;
}

// 校验文件
bool CUpdateManager::Verify( )
{
	if( DetectBreakEvent() )
	{
		return false;
	}

	GlobalNotify_TextID( TIP_START_CHECK_FILE );

	m_mimCached.erase( L"/." );
	m_mimCached.erase( PFSX::FILES_META_NAME );
	m_mimCached.erase( PFSX::VERSION_META_NAME );
	// 版本信息文件的MD5总是为0, 所以需要排除校验,否则总会失败
	// 而且该文件也无需合并

	// 用CheckFileEx方式校验strDir目录中的文件
	bool bRet = CheckFilesInFolder( m_cacheDir, m_mimCached, s_hBreakEvent );
	if( bRet )
	{
		GlobalNotify_TextID( TIP_CHECK_FILE_COMPLETE );
	}
	else
	{
		if( upd::GetLastError().ErrorCode != SC_USER_CANCELED )
		{
			GlobalNotify_TextID( ERR_CHECK_FILE2 );
		}
	}
	return bRet;
}

bool CUpdateManager::Merge( )
{
	if( DetectBreakEvent() )
	{
		return false;
	}

	// 将需要更新的程序文件 Copy到临时目录到运行目录中
	GlobalNotify_TextID(  TIP_START_MERGE_FILE );

	if( s_bNeedReboot )
	{
		if(!TranslateFilesForLauncher( m_cacheDir, m_runDir, m_mimCached,
			m_mimUpdate, s_hBreakEvent, IsUsePatch() ) )
		{
			const std::wstring& str = GlobalFormatText( ERR_MERGE_FILE_COUNT,
				m_mimCached.size(), m_mimCached.size() - m_mimUpdate.size());
			GlobalNotifyErrorInfo( upd::GetLastError(), str.c_str() );
			return false;
		}
		else
		{
			if( m_mimUpdate.size() > 0 )
			{
				GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COMPLETE).c_str(), m_mimUpdate.size());
			}
			return true;
		}
	}
	else
	{
		if( !TranslateFilesInFolder( m_cacheDir, m_runDir, m_mimCached, m_mimUpdate, s_hBreakEvent, IsUsePatch()))
		{
			const std::wstring& str = GlobalFormatText( ERR_MERGE_FILE_COUNT,
				m_mimCached.size(), m_mimCached.size() - m_mimUpdate.size());
			GlobalNotifyErrorInfo( upd::GetLastError(), str.c_str() );
			return false;
		}
		else
		{
			GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COMPLETE).c_str(), m_mimUpdate.size());
			return true;
		}
	}
}

bool CUpdateManagerBackground::Merge( )
{
	return TRUE;
}

bool CUpdateManagerWhole::Merge( )
{
	if( DetectBreakEvent() )
	{
		return false;
	}

	// Mount运行系统
	GlobalNotify_TextID(  TIP_MOUNT_RESOURCE );

	// Mount资源子系统
	bool bOK = CUpdateMountManager::GetInstance().MountPFS( m_runDir, false, s_hBreakEvent, m_smvServer);
	if ( !bOK )
	{
		//const upd::ErrorInfo& rErr = upd::GetLastError();
		//GlobalNotifyErrorInfo( rErr, L"更新版本失败！" );
		return false;
	}

	return CUpdateManager::Merge();
}

bool CUpdateManagerBackground::Destroy( bool bSucceeded )
{
	m_mimCached.clear();

	bool bRes = true;
	if( !CUpdateMountManager::GetInstance().UnmountPFS() )
	{
		KHMsgLog_ErrorW(L"后台下载: 清理内部资源失败！");
		bRes = false;
	}
	else
	{
		if( bSucceeded )
		{
			bool bDownloadSucceeded = m_mimDownloadFailed.empty();
			m_mimDownloadFailed.clear();
			if( bDownloadSucceeded && !DetectBreakEvent() )
			{
				if( m_newVersionInfo.IsValid() )
				{
					// 将最新版本信息写到.cache目录
					if( !PFSX::WriteVersionMetaFile( m_cacheDir + PFSX::VERSION_META_NAME,
						m_newVersionInfo, PFSX::VERSION_ALL_TYPES ) )
					{
						KHMsgLog_ErrorW( L"后台更新：写文件失败 - %s(%d)",
							(m_cacheDir + PFSX::VERSION_META_NAME).c_str(), PFS::CEnv::GetLastError() );
					}
				}

				GlobalNotify_TextID( TIP_BACKGROUND_UPDATE_COMPLETE );
			}
			else
			{
				KHMsgLog_InfoW(L"后台完成部分文件下载！" );
			}
		}
		else
		{
			KHMsgLog_ErrorW(L"后台更新没有成功完成！" );
		}
	}

	CloseHandle( s_hBreakEvent );
	s_hBreakEvent = INVALID_HANDLE_VALUE;

	return bRes;
}

// 提交成功更新的数据
bool CUpdateManager::Commit()
{
	bool bSucceeded = true;

	GlobalNotify_TextID( TIP_COMMITING );

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
			KHMsgLog_ErrorW( L"Open file (%s,%d) failed.", (m_runDir + L"\\log\\downloadfailed.log").c_str(),
				(int)(std::ios_base::out | std::ios_base::app) );
		}

		m_mimDownloadFailed.clear();
	}

	bool bUpdateSucceeded = true;

	// Commit运行系统的更新
	bool bRes = CUpdateMountManager::GetInstance().CommitPFS( s_hBreakEvent);

	if( !s_bNeedReboot )
	{
		// 改写 .setup.meta 文件
		PFSX::MergeSetupMeta( CUpdateMountManager::GetInstance().GetSetupMetaVector(), m_smvLocal);
		m_smvLocal.SetOriginal(false);
		bRes = PFSX::CSetupMetaFile::DoWrite( m_runDir + PFSX::SETUP_META_NAME, m_smvLocal) && bRes;
	}

	if( bRes && !s_bNeedReboot )
	{
		bUpdateSucceeded = (m_mimCached.size() == m_mimUpdate.size());
		if( bUpdateSucceeded )
		{
			if( bSucceeded &&
				bDownloadSucceeded &&	// 如果有文件下载失败,不更新版本文件
				!DetectBreakEvent()	&&	// 如果用户已经放弃更新,不再更新版本文件
				m_newVersionInfo.IsValid()
				)
			{
				// 将最新版本信息写到run目录
				if( !PFSX::WriteVersionMetaFile( m_runDir + PFSX::VERSION_META_NAME,
					m_newVersionInfo, PFSX::VERSION_ALL_TYPES ) )
				{
					// 很不幸，只有版本信息文件更新失败！
					upd::SetLastError( ::GetLastError(),
						GlobalFormatText( ERR_SAVE_VERSION_FILE, PFSX::VERSION_META_NAME.c_str()) );
					GlobalNotifyErrorInfo( upd::GetLastError() );
					bRes = false;
				}
			}
		}
		else
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

	if( bRes )
	{
		// 写更新成功的文件列表（有可能仅仅是部分成功）
		// Truncate the large file that more than 4M
		PurgeLogFile(( m_runDir + L"\\log\\update.log").c_str(), 4096*1024 );

		std::wofstream fupdate;
		fupdate.open( (m_runDir + L"\\log\\update.log").c_str(), std::ios_base::out | std::ios_base::app );

		if ( fupdate.is_open() )
		{
			DumpMetaInfoMap( m_mimUpdate, fupdate );
		}
		else
		{
			KHMsgLog_ErrorW( L"Open file (%s,%d) failed.",
				(m_runDir + L"\\log\\update.log").c_str(),
				(int)(std::ios_base::out | std::ios_base::app) );
		}

		m_mimUpdate.clear();

		if( s_bNeedReboot )
		{
			GlobalNotify_TextID(  TIP_COMMIT_PART_AND_REBOOT );
		}
		else if( !bDownloadSucceeded )
		{
			GlobalNotify_TextID(  ERR_DOWNLOAD_PART );
		}
		else if( !bUpdateSucceeded )
		{
			GlobalNotify_TextID( ERR_UPDATE_PART );
		}
		else if( bSucceeded )
		{
			GlobalNotify_TextID( TIP_COMMIT_COMPLETE );
		}
		else
		{
			GlobalNotify_TextID( ERR_COMMIT2 );
		}
	}
	else
	{
		GlobalNotify_TextID( ERR_COMMIT2 );
	}

	return bRes;
}

bool CUpdateManager::Destroy(bool bSucceeded)
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

//////////////////////////////////////////////////////////////////////////
//
// static functions
//
void CUpdateManager::TriggerBreakEvent()
{
	if( s_hBreakEvent != INVALID_HANDLE_VALUE)
		SetEvent( s_hBreakEvent );
}

bool CUpdateManager::DetectBreakEvent()
{
	return ::DetectBreakEvent( s_hBreakEvent );
}

bool CUpdateManager::IsNeedReboot()
{
	return s_bNeedReboot;
}
