#include "stdafx.h"
#include "UpdateManagerPatch.h"
#include "ErrorInfo.h"
#include "GlobalNotification.h"
#include "GlobalFunction.h"
#include "KHLogger.h"
#include "TextManager.h"
#include "TextID.h"

bool CUpdateManagerPatch::HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch )
{
	GlobalNotify( GlobalGetText( TIP_CHECKING_PATCH).c_str() );

	m_runDir = szRunDir;
	m_cacheDir = m_runDir + L".cache";
	m_newVersionInfo.Close();
	m_smvServer.clear();
	rAutoPatch = false;

	//
	// 检查新版本
	//
	m_localVersion.Close();
	if( !PFSX::GetLocalVersion( m_runDir, m_localVersion) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_READ_LOCAL_VERSION));
		rLocalValid = false;
		return false;
	}

	rLocalValid = m_localVersion.IsValid();

	if( !PFS::CEnv::Mount( RES_ROOT, m_patchpath, PFS::FST_RUNZIP, PFS::MT_READONLY))
	{
		upd::SetLastError( PFS::EC_INVALID_PATCH, GlobalGetText( ERR_PATCH_FORMAT));
		return false;
	}

	if( !PFSX::GetLocalVersion( RES_ROOT, m_newVersionInfo))
	{
		upd::SetLastError( PFS::EC_INVALID_PATCH, GlobalGetText( ERR_READ_PATCH_VERSION));
		PFS::CEnv::Unmount( RES_ROOT);
		return false;
	}

	// 比较，本地版本号要不小于补丁包的“旧版本号”但小于其“新版本号”
	rHas = ( !PFSX::CompareVersion( m_localVersion, m_newVersionInfo)
		&& m_localVersion.m_Version < m_newVersionInfo.m_Version
		&& m_localVersion.m_Version >= m_newVersionInfo.m_lastVersion);
	if ( !rHas )
	{
		PFS::CEnv::Unmount( RES_ROOT);

		// 只判断版本号，不判断发布日期和hash值，方便必要时刷版本
		if( m_localVersion.m_Version == m_newVersionInfo.m_Version )
		{
			GlobalNotify( GlobalGetText( TIP_ALREADY_NEW_VERSION).c_str() );
			return true; // 当前版本与补丁包版本一样,此时返回true允许强制更新
		}
		else
		{
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
			return false;
		}
	}

	m_newVersionInfo.m_lastVersion = 0;		// 本地版本信息没有旧版本号

	GlobalNotify( GlobalGetText( TIP_AUTO_PATH_UPDATING).c_str() );

	// 因为 Destroy 函数改为直接将 m_newVersionInfo 信息写到 .version.meta 文件
	// 所以这里不必创建 .cache 目录
	
	return true;
}

bool CUpdateManagerPatch::Initialize( LPCTSTR szRunDir)
{
	GlobalNotify( L"%1!s!,%2!s!", szRunDir, _UpdateModeName( m_UpdateMode) );
	GlobalNotify( GlobalGetText( TIP_INITIALIZING).c_str() );

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

	// 读包中的.setup.meta文件，可能没有。
	m_smvServer.clear();
	if( !PFSX::CSetupMetaFile::DoRead( RES_ROOT+PFSX::SETUP_META_NAME, m_smvServer))
	{
		// 失败只记日志，因为补丁包中可能会没有 .setup.meta 文件
		GlobalNotify( GlobalGetText( ERR_READ_PATCH_SETUPMETA).c_str() );
	}

	if( !m_smvServer.empty() )
	{
		PFSX::SetupMetaVector smvNewAdded, smvDifference, smvUseless;

		AnalyzeSetupMetaUpdateInfo(m_smvServer, m_smvLocal, smvDifference, smvNewAdded, smvUseless );

		// TODO: 通盘考虑新增的和删除的完善的处理逻辑。	
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
		s_hBreakEvent, m_smvLocal);

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

	return bOK;
}

bool ReadDeleteFiles( PFS::CMetaInfoMap& mimDelete)
{
	PFS::CFile file;
	std::wstring name = RES_ROOT;
	name += L"/.deletes.meta";
	if( !file.Open( name, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		return false;
	}
	return PFS::CMetaFileRead::DoRead( file, mimDelete);
}

bool CUpdateManagerPatch::CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete )
{
	m_mimDownload.clear();
	m_mimDownloadFailed.clear();
	m_mimCached.clear();
	m_mimDelete.clear();
	rDelete = 0;

	// 差异包需要删除的文件在 .deletes.meta 文件中
	if( !ReadDeleteFiles( m_mimDelete) )
	{
		int nError = PFS::CEnv::GetLastError();
		if( nError != PFS::EC_FILE_NOT_FOUND )
			upd::SetLastError( nError, GlobalGetText( ERR_LOAD_PATCH_DELETE_LIST));
	}
	else
	{
		// 除去m_mimDelete中不能删除的文件
		DealUnDeleteFiles(m_mimDelete, m_smvServer);
	}

	PFS::CMetaInfoMap mimLocal;
	GlobalNotify( GlobalGetText( TIP_LOAD_LOCAL_META_FILE).c_str() );
	if ( !PFS::CEnv::GetMetaInfosEx( RUN_ROOT, mimLocal))
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_LOAD_LOCAL_META_FILE));
		return false;
	}

	// 将 m_mimDelete 中不在 mimLocal 里出现的文件删除
	for( PFS::CMetaInfoMap::iterator i=m_mimDelete.begin(), e=m_mimDelete.end(); i!=e; )
	{
		const std::wstring& filename = i->first;
		if( mimLocal.find( filename) == mimLocal.end())
		{
			i = m_mimDelete.erase( i );
		}
		else
		{
			++i;
		}
	}

	rDelete = m_mimDelete.size();

	// 分析差异包所有文件列表
	PFS::CMetaInfoMap mimPatch;
	if( !PFS::CEnv::GetMetaInfos( RES_ROOT, mimPatch) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_LOAD_PATCH_META_FILE));
		return false;
	}

	GlobalNotify( GlobalGetText( TIP_ARRANGE_UPDATE_LIST).c_str() );
	SubtractMetaInfoMap( mimPatch, mimLocal, m_mimCached);
	if( m_mimCached.empty() )
	{
		// 没有文件需要更新
		GlobalNotify( GlobalGetText( TIP_NO_FILE_UPDATE).c_str() );
		return true;
	}

	m_mimCached.erase( PFSX::VERSION_META_NAME );	// 最后才决定处理 .version.meta 文件
	m_mimCached.erase( PFSX::SETUP_META_NAME );		// 最后才决定处理 .setup.meta 文件

	rDownload = 0;
	rMerge = m_mimCached.size();
	if( rMerge > 0 )
	{
		if( rDelete > 0 )
			GlobalNotify( GlobalGetText( TIP_MERGE_DELETE_FILE_COUNT).c_str(), rMerge, rDelete  );
		else
			GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT2).c_str(), rMerge);
	}
	else if ( rDelete > 0 )
	{
		GlobalNotify( GlobalGetText( TIP_DELETE_FILE_COUNT).c_str(), rDelete );
	}

	s_bNeedReboot = CUpdateManager::IsNeedUpdateLauncher( m_mimCached);

	if( DetectBreakEvent() )
	{
		return false;
	}

	return true;
}

bool CUpdateManagerPatch::Download( )
{
	return true;
}

bool CUpdateManagerPatch::Verify( )
{
	return true;
}

bool CUpdateManagerPatch::Destroy( bool bSucceeded )
{
	PFS::CEnv::Unmount( RES_ROOT);
	return CUpdateManager::Destroy(bSucceeded);
}
