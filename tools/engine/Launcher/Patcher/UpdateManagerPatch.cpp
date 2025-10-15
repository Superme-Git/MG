#include "stdafx.h"
#include "UpdateManagerPatch.h"
#include "ErrorInfo.h"
#include "GlobalNotification.h"
#include "GlobalFunction.h"
#include "KHLogger.h"
#include "TextManager.h"
#include "TextID.h"

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

bool CUpdateManagerPatch::HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch, bool& rMergeCache )
{
	GlobalNotify( GlobalGetText( TIP_CHECKING_PATCH).c_str() );

	m_runDir = szRunDir;
	m_cacheDir = m_runDir + L".cache";
	//m_p2spDir = m_runDir + L".p2sp";
	m_newVersionInfo.Close();
	m_smvServer.clear();
	rAutoPatch = false;
	rMergeCache = false;

	//
	// ����°汾
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

	// �Ƚϣ����ذ汾��Ҫ��С�ڲ������ġ��ɰ汾�š���С���䡰�°汾�š�
	rHas = ( !PFSX::CompareVersion( m_localVersion, m_newVersionInfo)
		&& m_localVersion.m_Version < m_newVersionInfo.m_Version
		&& m_localVersion.m_Version >= m_newVersionInfo.m_lastVersion);
	if ( !rHas )
	{
		PFS::CEnv::Unmount( RES_ROOT);

		// ֻ�жϰ汾�ţ����жϷ������ں�hashֵ�������Ҫʱˢ�汾
		if( m_localVersion.m_Version == m_newVersionInfo.m_Version )
		{
			GlobalNotify( GlobalGetText( TIP_ALREADY_NEW_VERSION).c_str() );
			return true; // ��ǰ�汾�벹�����汾һ��,��ʱ����true����ǿ�Ƹ���
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

	std::wstring localVersionString;
	std::wstring newVersionString;
	PFSX::Version2String( m_localVersion.m_Version, localVersionString );
	PFSX::Version2String( m_newVersionInfo.m_Version, newVersionString );
	KHMsgLog_InfoW( L"local version: %s, patch version: %s\n",localVersionString.c_str(), newVersionString.c_str() );

	m_newVersionInfo.m_lastVersion = 0;		// ���ذ汾��Ϣû�оɰ汾��

	GlobalNotify( GlobalGetText( TIP_AUTO_PATH_UPDATING).c_str() );

	// ��Ϊ Destroy ������Ϊֱ�ӽ� m_newVersionInfo ��Ϣд�� .version.meta �ļ�
	// �������ﲻ�ش��� .cache Ŀ¼
	
	return true;
}

bool CUpdateManagerPatch::Initialize( LPCTSTR szRunDir)
{
	GlobalNotify( L"%1!s!,%2!s!", szRunDir, _UpdateModeName( m_UpdateMode) );
	GlobalNotify( GlobalGetText( TIP_INITIALIZING).c_str() );

	m_runDir = szRunDir; // ��Ϸ��װĿ¼
	m_cacheDir = m_runDir + L".cache"; // UPDATE_MODE_REPAIR ģʽû��ִ��HasNewVersion������������Ҫ��ʼ��m_cacheDir
	//m_p2spDir = m_runDir + L".p2sp";
	if( s_hBreakEvent == INVALID_HANDLE_VALUE)
		s_hBreakEvent = CreateEvent( NULL, TRUE, FALSE, NULL );
	else
	{
		ResetEvent( s_hBreakEvent );
	}

	if( !IsBackground())
	{
		// �����Ϸ�ͻ����Ƿ�������
		LPCWSTR lpszInstallationName = GetInstallationMutexName();
		if( NamedMutexExists( lpszInstallationName ) )
		{
			upd::SetLastError( SC_GAMECLIENT_RUNNING, GlobalGetText( ERR_CLIENT_IS_RUNNING) );
			return false;
		}
	}

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
	for( PFSX::SetupMetaVector::iterator i= m_smvLocal.begin(), e=m_smvLocal.end(); i!=e; ++i)
	{
		if( !PFS::CFileIO::IsFileExisting( m_runDir+i->GetName() + L".pfs"))
		{
			i->SetHash( 0);
		}
	}

	PFSX::SetupMetaVector smvAll, smvNewAdded;

	// �����е�.setup.meta�ļ�������û�С�
	m_smvServer.clear();
	if( !PFSX::CSetupMetaFile::DoRead( RES_ROOT+PFSX::SETUP_META_NAME, m_smvServer))
	{
		// ʧ��ֻ����־����Ϊ�������п��ܻ�û�� .setup.meta �ļ�
		GlobalNotify( GlobalGetText( ERR_READ_PATCH_SETUPMETA).c_str() );
		smvAll = m_smvLocal;
	}
	else
	{
		PFSX::SetupMetaVector smvUseless, smvDifference;

		AnalyzeSetupMetaUpdateInfo(m_smvServer, m_smvLocal, smvDifference, smvNewAdded, smvUseless );
		
		smvAll = smvNewAdded;
		
		PFSX::MergeSetupMeta( m_smvLocal, smvAll );
	}
	
	// Mount����ϵͳ
	GlobalNotify( GlobalGetText( TIP_LOAD_RUN_SYSTEM).c_str() );
	
	// �ر�˵����������smvAll Mount����ϵͳ����������smvDifference��
	// ԭ�����£�
	// 1. smvAll����m_smvLocal������ʶ��ɾ����Ŀ.
	// 2. smvAll����smvNewAdded����������������pfs��Ŀ.
	// 3. smvDifference �в���patch��local�й��е���Ŀ������ЩҲ�Ǳ�Ҫ�ġ�
	// ��Ϊ�������������Щ���е���Ŀ����patch�򱾵ذ汾�ϲ�ʱ�����patch�к��б��ذ汾����ͬ����Ŀ��
	// �ᵼ����Щ��Ŀ�ϲ���RUN_DIR��.files.meta�ļ��У��Ӷ�����.files.meta�ļ����Ӱ��ļ���meta�غϡ�
	// Ҫ�����������ֻ�轫һ�����������°汾�Ŀͻ��ˣ��ֹ��޸�.version.meta��ĳ���Ͱ汾��Ȼ��ʹ��patch�ļ�
	// �򲹶����ɡ������������������ʵ�ڲ���̫��Ҫ��
	bool bOK = CUpdateMountManager::GetInstance().MountPFS( m_runDir, false, 
		s_hBreakEvent, smvAll );
	if( bOK )
	{
		GlobalNotify( GlobalGetText( TIP_PREPARE_COMPLETE).c_str() );

		// ����û�ѡ����ǰ��ֹ���¹��̣���ô��ʱִ��Unmount���̲�����false
		if ( DetectBreakEvent() )
		{
			CUpdateMountManager::GetInstance().UnmountPFS();
			return false;
		}
	}

	return bOK;
}

bool CUpdateManagerPatch::BeginLoop( )
{
	return true;
}

bool CUpdateManagerPatch::CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete )
{
	m_mimDownload.clear();
	m_mimDownloadFailed.clear();
	m_mimCached.clear();
	m_mimDelete.clear();
	rDelete = 0;

	// �������Ҫɾ�����ļ��� .deletes.meta �ļ���
	if( !ReadDeleteFiles( m_mimDelete) )
	{
		int nError = PFS::CEnv::GetLastError();
		if( nError != PFS::EC_FILE_NOT_FOUND )
			upd::SetLastError( nError, GlobalGetText( ERR_LOAD_PATCH_DELETE_LIST));
	}
	else
	{
		// ��ȥm_mimDelete�в���ɾ�����ļ�
		DealUnDeleteFiles(m_mimDelete, m_smvServer);
	}

	PFS::CMetaInfoMap mimLocal;
	GlobalNotify( GlobalGetText( TIP_LOAD_LOCAL_META_FILE).c_str() );
	if ( !PFS::CEnv::GetMetaInfosEx( RUN_ROOT, mimLocal))
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_LOAD_LOCAL_META_FILE));
		return false;
	}

	// �� m_mimDelete �в��� mimLocal ����ֵ��ļ�ɾ��
	for( PFS::CMetaInfoMap::iterator i = m_mimDelete.begin(), e = m_mimDelete.end(); i != e; )
	{
		const std::wstring& filename = i->first;
		if( mimLocal.find( filename ) == mimLocal.end() )
		{
			i = m_mimDelete.erase( i );
		}
		else
		{
			++ i;
		}
	}

	rDelete = m_mimDelete.size();

	// ��������������ļ��б�
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
		// û���ļ���Ҫ����
		GlobalNotify( GlobalGetText( TIP_NO_FILE_UPDATE).c_str() );
		return true;
	}

	m_mimCached.erase( PFSX::VERSION_META_NAME );	// ���ž������� .version.meta �ļ�
	m_mimCached.erase( PFSX::SETUP_META_NAME );		// ���ž������� .setup.meta �ļ�

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

void CUpdateManagerPatch::EndLoop( bool bLoopSucceeded )
{
	PFS::CEnv::Unmount( RES_ROOT );
}
