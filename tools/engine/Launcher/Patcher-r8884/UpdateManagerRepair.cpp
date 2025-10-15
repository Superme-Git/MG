///=============================================================================
/// Product   : MHZX
/// Project   : Patcher
/// Module    : UpdateManagerRepair.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	ǿ��ʹ�ò��������¿ͻ���
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

	// TODO: ���������������,��������Ĵ�����ʱ��Ч

	// ����Ҫ��ʼ��һ�� m_newVersionInfo
	// ������³ɹ��� Destroy ��ʱ��Ὣδ��ʼ���� m_newVersionInfo ��Ϣд�� .version.meta �ļ�
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

	// ��������������ļ��б�
	GlobalNotify( GlobalGetText( TIP_ARRANGE_UPDATE_LIST).c_str() );

	if( !PFS::CEnv::Mount( RES_ROOT, m_patchpath, PFS::FST_RUNZIP, PFS::MT_READONLY))
	{
		upd::SetLastError( PFS::EC_INVALID_PATCH, GlobalGetText( ERR_PATCH_FORMAT));
		return false;
	}

	// ����Ҫ��ʼ��һ�� m_newVersionInfo
	// ������³ɹ��� Destroy ��ʱ��Ὣδ��ʼ���� m_newVersionInfo ��Ϣд�� .version.meta �ļ�
	if( !PFSX::GetLocalVersion( RES_ROOT, m_newVersionInfo))
	{
		upd::SetLastError( PFS::EC_INVALID_PATCH, GlobalGetText( ERR_READ_PATCH_VERSION));
		return false;
	}
	m_newVersionInfo.m_lastVersion = 0;

	if( !PFSX::CSetupMetaFile::DoRead( RES_ROOT+PFSX::SETUP_META_NAME, m_smvServer))
	{
		// ʧ��ֻ����־����Ϊ�������п��ܻ�û�� .setup.meta �ļ�
		GlobalNotify( GlobalGetText( ERR_READ_PATCH_SETUPMETA).c_str() );
	}

	if( !m_smvServer.empty() )
	{
		PFSX::SetupMetaVector smvNewAdded, smvDifference, smvUseless;

		AnalyzeSetupMetaUpdateInfo(m_smvServer, m_smvLocal, smvDifference, smvNewAdded, smvUseless );

		// TODO: ͨ�̿��������ĺ�ɾ�������ƵĴ����߼���	
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

	m_mimCached.erase( PFSX::FILES_META_NAME);		// Unmount ʱ�����
	m_mimCached.erase( PFSX::VERSION_META_NAME );	// ���ž������� .version.meta �ļ�
	m_mimCached.erase( PFSX::SETUP_META_NAME );		// ���ž������� .setup.meta �ļ�

	// ����Ҫ���µĳ����ļ� Copy ����ʱĿ¼��
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
