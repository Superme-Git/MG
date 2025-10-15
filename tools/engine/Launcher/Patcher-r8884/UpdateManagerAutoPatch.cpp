#include "stdafx.h"
#include "UpdateManagerAutoPatch.h"
#include "GlobalNotification.h"
#include "ResumeBrokenDownload.h"
#include "KHLogger.h"
#include "TextManager.h"
#include "TextID.h"

bool CUpdateManagerAutoPatch::HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch )
{
	// ʹ��CUpdateManager����ʹ��CUpdateManagerAutoPatch�����°汾���
	// �Ա�֤��ʹʹ��UPDATE_MODE_AUTOPATCHģʽ�����°汾��飬Ҳ�ܻ�ȡ��ȷ���
	m_UpdateMode = UPDATE_MODE_DEFAULT;
	bool bRet = CUpdateManager::HasNewVersion( szRunDir, rHas, rLocalValid, rAutoPatch );
	m_UpdateMode = UPDATE_MODE_AUTOPATCH;
	m_currentPatchIndex = 0;
	return bRet;
}

bool CUpdateManagerAutoPatch::Initialize( LPCTSTR szRunDir)
{
	//
	// ��ʼ�����ذ汾��Ϣ
	//
	m_currentPatchIndex = 0;
	m_localVersion.Close();
	if( !PFSX::GetLocalVersion( szRunDir, m_localVersion) )
	{
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_READ_LOCAL_VERSION).c_str());
		return false;
	}

	//
	// m_newVersionInfo �Ѿ�ͨ��SetServerVersion() ��ʼ��
	// �������ȡ��url
	// 
	m_url = GetVersionUrl( m_newVersionInfo, true, false );

	return CUpdateManagerPatch::Initialize( szRunDir);
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
	PFS::fsize_t patchsize = m_patchInfos[m_currentPatchIndex].size;

	// ��������Ҫ������ʽ������ΪFormatMessage��֧�֡�
	wchar_t buffer[256];
	swprintf_s( buffer, 256, L"%.2lf", patchsize/1048576.0f );

	GlobalNotify( GlobalGetText( TIP_DOWNLOAD_PROGRESS2).c_str(), m_patchpath.c_str(), buffer );
	CResumeBrokenDownload downloader( m_patchpath , m_url, m_cacheDir, m_patchhash);
	if( !downloader.Download( s_hBreakEvent ) )
	{
		// ��patch�ļ�������Ϊ����ʧ�ܵ��ļ�ͳ�Ƴ�����������°汾��Ϣ
		m_mimDownloadFailed.insert( std::make_pair( m_patchpath, PFS::CMetaInfo() ) );
		return false;
	}

	// �޸�hash path
	m_patchpath = m_cacheDir + m_patchpath;

	GlobalNotify( GlobalGetText( TIP_CHECK_PATCH_FILE).c_str(), m_patchpath.c_str());

	if( !PFS::CEnv::Mount( RES_ROOT, m_patchpath, PFS::FST_RUNZIP, PFS::MT_READONLY))
	{
		m_mimDownloadFailed.insert( std::make_pair( m_patchpath, PFS::CMetaInfo() ) );
		upd::SetLastError( PFS::EC_INVALID_PATCH, GlobalGetText( ERR_PATCH_FORMAT));
		return false;
	}

	// ��ȡcache�еİ汾��Ϣ
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
		// ʧ��ֻ����־����Ϊ�������п��ܻ�û�� .setup.meta �ļ�
		int nError = PFS::CEnv::GetLastError();
		if( nError != PFS::EC_FILE_NOT_FOUND )
		{
			KHMsgLog_ErrorW( L"�������� .setup.meta �ļ�ʧ��!(%d)\n", nError );
		}
	}

	// �Ƚϣ����ذ汾��Ҫ��С�ڲ������ġ��ɰ汾�š���С���䡰�°汾�š�
	bool rHas = ( !PFSX::CompareVersion( m_localVersion, m_newVersionInfo)
		&& m_localVersion.m_Version < m_newVersionInfo.m_Version
		&& m_localVersion.m_Version >= m_newVersionInfo.m_lastVersion);
	if ( !rHas )
	{
		// ֻ�жϰ汾�ţ����жϷ������ں�hashֵ�������Ҫʱˢ�汾
		if( m_localVersion.m_Version == m_newVersionInfo.m_Version )
		{
			GlobalNotify( GlobalGetText( TIP_ALREADY_NEW_VERSION).c_str() );
			return true; // ��ǰ�汾�벹�����汾һ��,��ʱ����true����ǿ�Ƹ���
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

	m_newVersionInfo.m_lastVersion = 0;		// ���ذ汾��Ϣû�оɰ汾��

	if( !m_smvServer.empty())
	{
		PFSX::SetupMetaVector smvNewAdded, smvDifference, smvUseless;

		AnalyzeSetupMetaUpdateInfo(m_smvServer, m_smvLocal, smvDifference, smvNewAdded, smvUseless );

		// TODO: ͨ�̿��������ĺ�ɾ�������ƵĴ����߼���		
	}

	GlobalNotify( GlobalGetText( TIP_AUTO_PATH_UPDATING).c_str() );

	return true;
}

void CUpdateManagerAutoPatch::EndLoop( bool bLoopSucceeded )
{
	PFS::CEnv::Unmount( RES_ROOT );

	if( bLoopSucceeded )
	{
		// �ύ�ɹ������ذ汾����Ϊ�°汾
		m_localVersion = m_newVersionInfo;

		PFS::CDataBlock dataVersionInfo;
		m_localVersion.WriteTo(dataVersionInfo);
		const wchar_t ends = 0;
		dataVersionInfo.Append( &ends, 2 );
		KHMsgLog_DebugW( L"local version change to new version:\n%s", dataVersionInfo.GetData() );
	}
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

bool CUpdateManagerAutoPatch::Destroy(bool bSucceeded)
{
	return CUpdateManager::Destroy(bSucceeded);
}
