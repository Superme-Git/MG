///=============================================================================
/// Product   : MHZX
/// Project   : Patcher
/// Module    : UpdateManagerRepair.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	强制使用补丁包更新客户端
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-9-21 Created.
///=============================================================================
#include "StdAfx.h"
#include "UpdateManagerRepair.h"
#include "ErrorInfo.h"
#include "GlobalNotification.h"
#include "GlobalFunction.h"
#include "TextManager.h"
#include "TextID.h"

bool CUpdateManagerRepair::HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch )
{
	ASSERT(FALSE);
	rHas = true;
	rAutoPatch = false;

	// TODO: 这个函数不被调用,所以下面的代码暂时无效

	// 这里要初始化一下 m_newVersionInfo
	// 否则更新成功后 Destroy 的时候会将未初始化的 m_newVersionInfo 信息写入 .version.meta 文件
	if( !PFSX::GetLocalVersion( RES_ROOT, m_newVersionInfo))
	{
		upd::SetLastError( PFS::EC_INVALID_PATCH, GlobalGetText( ERR_READ_PATCH_VERSION));
		rLocalValid = false;
		return false;
	}

	m_newVersionInfo.m_lastVersion = 0;
	rLocalValid = m_newVersionInfo.IsValid();

	return true;
}

bool CUpdateManagerRepair::CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete )
{
	rDownload = 0;
	rMerge = 0;
	m_mimDownload.clear();
	m_mimDownloadFailed.clear();
	m_mimCached.clear();
	m_mimDelete.clear();
	m_smvServer.clear();
	rDelete = 0;

	// 分析差异包所有文件列表
	GlobalNotify( GlobalGetText( TIP_ARRANGE_UPDATE_LIST).c_str() );

	if( !PFS::CEnv::Mount( RES_ROOT, m_patchpath, PFS::FST_RUNZIP, PFS::MT_READONLY))
	{
		upd::SetLastError( PFS::EC_INVALID_PATCH, GlobalGetText( ERR_PATCH_FORMAT));
		return false;
	}

	// 这里要初始化一下 m_newVersionInfo
	// 否则更新成功后 Destroy 的时候会将未初始化的 m_newVersionInfo 信息写入 .version.meta 文件
	if( !PFSX::GetLocalVersion( RES_ROOT, m_newVersionInfo))
	{
		upd::SetLastError( PFS::EC_INVALID_PATCH, GlobalGetText( ERR_READ_PATCH_VERSION));
		return false;
	}
	m_newVersionInfo.m_lastVersion = 0;

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

	if( !PFS::CEnv::GetMetaInfos( RES_ROOT, m_mimCached) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_LOAD_LOCAL_META_FILE));
		return false;
	}

	rMerge = m_mimCached.size();
	if( rMerge > 0 )
	{
		GlobalNotify( GlobalGetText( TIP_UPDATE_FILE_COUNT).c_str(), rMerge );
	}

	if( DetectBreakEvent() )
	{
		return false;
	}

	return true;
}

bool CUpdateManagerRepair::Download( )
{
	return true;
}

bool CUpdateManagerRepair::Verify( )
{
	return true;
}

bool CUpdateManagerRepair::Merge( )
{
	if( DetectBreakEvent() )
	{
		return false;
	}

	m_mimCached.erase( PFSX::FILES_META_NAME);		// Unmount 时会更新
	m_mimCached.erase( PFSX::VERSION_META_NAME );	// 最后才决定处理 .version.meta 文件
	m_mimCached.erase( PFSX::SETUP_META_NAME );		// 最后才决定处理 .setup.meta 文件

	// 将需要更新的程序文件 Copy 到临时目录中
	GlobalNotify( GlobalGetText( TIP_START_MERGE_FILE).c_str()  );

	s_bNeedReboot = false;

	if( !TranslateFilesInFolder( m_cacheDir, m_runDir, m_mimCached, m_mimUpdate, 
		s_hBreakEvent, true) )
	{
		const std::wstring& str = GlobalFormatText( ERR_MERGE_FILE_COUNT, 
			m_mimCached.size(), m_mimCached.size() - m_mimUpdate.size());
		GlobalNotifyErrorInfo( upd::GetLastError(), str.c_str() );
		return false;
	}
	else
	{
		GlobalNotify( GlobalGetText( TIP_MERGE_FILE_COUNT).c_str(), m_mimUpdate.size() );
		return true;
	}
}

bool CUpdateManagerRepair::Destroy(bool bSucceeded )
{
	PFS::CEnv::Unmount( RES_ROOT );
	return CUpdateManager::Destroy(bSucceeded);
}
