///============================================================================
///
/// UpdateManagerMergeCache.cpp - �ϲ���̨������¹�����
///
/// �˺ϲ���̨��������������ں�̨�����Ѿ����ֻ�ȫ��������ֻ��ϲ��������
/// �����̨�����Ѿ����������°汾��ֻ��ϲ���
/// �����̨�����Ѿ����־�����ֻ�������°汾�����Ⱥϲ���Ȼ�����ѡ������ģʽ���¡�
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
	// ʹ��UPDATE_MODE_DEFAULT����ʹ��UPDATE_MODE_MERGECACHE�����°汾���
	// �Ա�֤��ʹʹ��UPDATE_MODE_MERGECACHEģʽ�����°汾��飬Ҳ�ܻ�ȡ��ȷ���
	m_UpdateMode = UPDATE_MODE_DEFAULT;
	
	bool bRet = CUpdateManager::HasNewVersion( szRunDir, rHas, rLocalValid, rAutoPatch, rMergeCache );
	
	m_UpdateMode = UPDATE_MODE_MERGECACHE;
	
	return bRet;
}

bool CUpdateManagerMergeCache::Initialize( LPCTSTR szRunDir)
{
	GlobalNotify( L"%1!s!,%2!s!", szRunDir, _UpdateModeName( m_UpdateMode) );
	GlobalNotify_TextID( TIP_INITIALIZING );

	m_runDir = szRunDir; // ��Ϸ��װĿ¼
	m_cacheDir = m_runDir + L".cache"; 
	//m_p2spDir = m_runDir + L".p2sp";
	// UPDATE_MODE_MERGECACHEģʽ����û��ִ��HasNewVersion������������Ҫ��ʼ��m_cacheDir

	if( s_hBreakEvent == INVALID_HANDLE_VALUE)
		s_hBreakEvent = CreateEvent( NULL, TRUE, FALSE, NULL );
	else
	{
		ResetEvent( s_hBreakEvent );
	}

	// �����Ϸ�ͻ����Ƿ�������
	LPCWSTR lpszInstallationName = GetInstallationMutexName();
	if( NamedMutexExists( lpszInstallationName ) )
	{
		upd::SetLastError( SC_GAMECLIENT_RUNNING, GlobalGetText( ERR_CLIENT_IS_RUNNING) );
		return false;
	}

	// �����еİ汾��Ϣ���ǵ�ǰ���µ�Ŀ��汾��������ϲ����֮��
	// ���ذ�װ�汾���µ��˰汾�������Ƿ��������°汾��
	m_newVersionInfo.Close();
	if( !PFSX::GetLocalVersion( m_cacheDir, m_newVersionInfo) ||
		!m_newVersionInfo.IsValid() )
	{
		if( m_newVersionInfo.IsValid() )
		{
			KHMsgLog_WarningW(L"��ȡ����汾��Ϣʧ�ܣ���ǰ汾��Ч�����������¡�\n");

			// ����汾��Ч��Ϊ�˷�ֹ�´θ������úϲ�����ģʽ(MERGECACHE)��������汾�ļ�����Ϊvalid=false;
			m_newVersionInfo.m_bValid = false;
			if( !PFSX::WriteVersionMetaFile( m_cacheDir + PFSX::VERSION_META_NAME,
				m_newVersionInfo, PFSX::VERSION_ALL_TYPES ) )
			{
				PFS::CFile::RemoveFile(m_cacheDir + PFSX::VERSION_META_NAME);
				KHMsgLog_ErrorW( L"��ǰ汾�ļ���Чʧ�ܣ�ɾ��֮ - %s(%d)",
					(m_cacheDir + PFSX::VERSION_META_NAME).c_str(), PFS::CEnv::GetLastError() );
			}
		}
		else
		{
			KHMsgLog_WarningW(L"����汾��Ϣ��Ч���������¡�\n");
		}

		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_MERGECACHE_FAILED));
		
		return false;
	}

	std::wstring localVersionString;
	std::wstring newVersionString;
	PFSX::Version2String( m_localVersion.m_Version, localVersionString );
	PFSX::Version2String( m_newVersionInfo.m_Version, newVersionString );
	KHMsgLog_InfoW( L"local version: %s, cache version: %s\n",localVersionString.c_str(), newVersionString.c_str() );

	// ������ .setup.meta �ļ�
	m_smvLocal.clear();
	if( !PFSX::CSetupMetaFile::DoRead( m_runDir + PFSX::SETUP_META_NAME, m_smvLocal))
	{
		// .setup.meta �����ڵ������Ҳ�������
		if( PFS::CEnv::GetLastError() != ERROR_FILE_NOT_FOUND)
		{
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_READ_LOCAL_SETUPMETA) );
			return false;
		}
	}

	// ��鱾�� pfs �ļ��Ƿ����
	for( PFSX::SetupMetaVector::iterator i= m_smvLocal.begin(), e = m_smvLocal.end(); i != e; ++ i )
	{
		if( !PFS::CFileIO::IsFileExisting( m_runDir + i->GetName() + L".pfs"))
		{
			i->SetHash( 0);
		}
	}

	// ������SetupMeta
	m_smvServer.clear();
	PFSX::SetupMetaVector smvDifference;
	
	if( !PFSX::CSetupMetaFile::DoRead( m_cacheDir + PFSX::SETUP_META_NAME, m_smvServer))
	{
		// ʧ��ֻ����־����Ϊ�����п���û�� .setup.meta �ļ�
		KHMsgLog_WarningW( L"WARN: read cache setup meta failed: %d\n" , PFS::CEnv::GetLastError());
		smvDifference = m_smvLocal;
	}
	else
	{
		// ���� SetupMeta �Ĳ����б�
		if ( m_smvLocal.IsOriginal() )
		{
			smvDifference = m_smvServer; // ʹ�÷�����.setup.meta
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

	// Mount����ϵͳ
	GlobalNotify_TextID( TIP_LOAD_RUN_SYSTEM );

	bool bOK = CUpdateMountManager::GetInstance().MountPFS( m_runDir, false,
		s_hBreakEvent, smvDifference);
	if( bOK )
	{
		GlobalNotify_TextID( TIP_PREPARE_COMPLETE );

		// ����û�ѡ����ǰ��ֹ���¹��̣���ô��ʱִ��Unmount���̲�����false
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
	// �򻯴˹��̣������أ���ɾ����ֻ�ϲ���
	m_mimDownload.clear();
	m_mimDownloadFailed.clear();
	m_mimCached.clear();
	m_mimDelete.clear();
	rDelete = 0;
	rDownload = 0;

	// ͳ�Ƹ�����Ŀ
	GlobalNotify_TextID( TIP_ARRANGE_UPDATE_LIST );

	// ��ȡ�Ѿ�������ļ�
	GlobalNotify_TextID( TIP_READ_CACHE_META_FILE );

	bool bCached = ReadMetaInfoFile( m_cacheDir, m_mimCached );
	if( !bCached || m_mimCached.empty() )
	{
		// ��Cache�е�MetaInfo�ļ�ʧ�ܣ���cacheΪ�ա�
		return false;
	}

	// �����RUN_ROOT��ȡmetaInfoMap��Ȼ���뻺���е�metaInfoMap���бȽϵĹ����Ǳز����ٵġ�
	// ԭ������������һ��Launcher�����Ĺ��̡�
	// �ڵ�һ��ִ��MergeCacheʱ��Launcher����֮ǰ������������̵�ȷû�б�Ҫ��
	// �������ڴ˲�̫���׵õ�Launcher�Ƿ����������״̬������Ҳ���跨ȡ�á�
	// Launcher����֮��Launcher����Ӧ�ò�����Ҫ�ӻ���ϲ�һ�Σ�����ᵼ������������
	// ����֮�����ڻ���MetaInfoMap������������û�и��£���RUN_ROOT�е�MetaInfoMap����£�
	// ��˵����ӻ����л�ȡ��MetaInfoMap�ж�Launcher�Ƿ���Ҫ�����ǲ����ġ�
	PFS::CMetaInfoMap mimLocalRoot, mimNeedMerge;
	// ������ mount Ŀ¼�� .files.meta �ļ�
	if( !ReadMetaInfoFile( RUN_ROOT, mimLocalRoot))
	{
		// .file.meta �п��ܲ�����
		if( PFS::CEnv::GetLastError() != PFS::EC_FILE_NOT_FOUND)
		{
			return false;
		}
	}

	SubtractMetaInfoMap( m_mimCached, mimLocalRoot, mimNeedMerge );

	// ��config.xml�ж�ȡ���Ը����ļ��б�
	// �ر�˵����Ϊ�˲��Ը����߼����Ƿ���Ҫ����patcher�͹�������������Դ���
	// �����������������̷����ж��Ƿ���Ҫ����patcher֮����У���������ʹ�������Ѿ����˵�patcher�е�������Ҫ���µ��ļ���
	// ����������������������
	// �����������뷨�򵥴�������һ��ì�ܲ��ɽ����
	// ��������ǰ�Ĵ������ַ�ʽ��ȫû�����⣬����������������մ�˳�򣬻ᵼ��s_bNeedReboot��ȻΪtrue��
	// �������ǣ����³���Ƶ���ظ��������޷�����������¡�
	// ��Ȼ�����������򵥴���ʽ����������ʵ��Merge()������һ���Ժϲ����и�����Ŀ��������UpdateManagerRepair::Merge()�Ĵ���
	// ���У����Ƿ��Ѿ���������״̬���CUpdateThread::GetRebooted()�õ��������жϲ�����Ҳ���Խ�������⣬
	// ������ȥ��һ����Ϊ������ֱ���õ�Rebooted�����ǣ�������Ϊ����߼�����Ϊ�˲��ԣ���Ҫ�Բ��Ǻܴ���ʱ���ա�
	// ʵ���б�Ҫ��ʱ�򣬿��Կ������·�����
	// 1. UpdateThread��rebooted״̬����UpdateManager��
	// 2. ��_tcsstr( theApp.m_lpCmdLine, L"-r ")������õķ���ֵ�жϡ�
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
