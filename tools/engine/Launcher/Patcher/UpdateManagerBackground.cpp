#include "StdAfx.h"
#include "UpdateManagerBackground.h"
//#include "ResumeBrokenDownload.h"
#include "PatchDownloader.h"
#include "ErrorInfo.h"
#include "GlobalNotification.h"
#include "KHLogger.h"
#include "TextManager.h"
#include "TextID.h"

inline void GlobalNotify_TextID( int textId )
{
	GlobalNotify( GlobalGetText( textId ).c_str() );
}

bool CUpdateManagerBackground::Destroy( bool bSucceeded )
{
	m_mimCached.clear();

	bool bRes = true;
	if( !CUpdateMountManager::GetInstance().UnmountPFS() )
	{
		KHMsgLog_ErrorW(L"��̨����: �����ڲ���Դʧ�ܣ�");
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
					// �����°汾��Ϣд��.cacheĿ¼
					if( !PFSX::WriteVersionMetaFile( m_cacheDir + PFSX::VERSION_META_NAME,
						m_newVersionInfo, PFSX::VERSION_ALL_TYPES ) )
					{
						KHMsgLog_ErrorW( L"��̨���£�д�ļ�ʧ�� - %s(%d)",
							(m_cacheDir + PFSX::VERSION_META_NAME).c_str(), PFS::CEnv::GetLastError() );
					}
				}

				//if( !PFSX::CPatchMetaFile::DoWrite( m_cacheDir + PFSX::PATCH_META_NAME, m_patchMetas))
				//{
				//	KHMsgLog_ErrorW( L"��̨���£�д�ļ�ʧ�� - %s(%d)",
				//		(m_cacheDir + PFSX::PATCH_META_NAME).c_str(), PFS::CEnv::GetLastError() );
				//}

				GlobalNotify( GlobalGetText( TIP_BACKGROUND_UPDATE_COMPLETE ).c_str() );
			}
			else
			{
				KHMsgLog_InfoW(L"��̨��ɲ����ļ����أ�" );
			}
		}
		else
		{
			KHMsgLog_ErrorW(L"��̨����û�гɹ���ɣ�" );
		}
	}

	CloseHandle( s_hBreakEvent );
	s_hBreakEvent = INVALID_HANDLE_VALUE;

	return bRes;
}

bool CUpdateManagerBackground::HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch, bool& rMergeCache )
{
	GlobalNotify_TextID( TIP_CHECK_LOCAL_VERSION );

	m_runDir = szRunDir;
	m_cacheDir = m_runDir + L".cache";
	//m_p2spDir = m_runDir + L".p2sp";

	m_newVersionInfo.Close();
	m_currentPatchIndex = 0;

	rHas = false;
	rAutoPatch = false;
	rMergeCache = false;

	//
	// ����°汾
	//
	m_localVersion.Close();
	if( !PFSX::GetLocalVersion( m_cacheDir, m_localVersion) || !m_localVersion.IsValid() )
	{
		if( !PFSX::GetLocalVersion( m_runDir, m_localVersion) )
			rLocalValid = false;
		else
			rLocalValid = m_localVersion.IsValid();

		if( !rLocalValid)
		{
			KHMsgLog_DebugW(L"���ذ汾��Ч.\n");
			return false;
		}
	}

	std::wstring url = GetVersionUrl( m_localVersion, true, false );
	if( url.empty() )
	{
		PFS::CEnv::SetLastError(PFS::EC_INVALID_VERSION_INFO );
		//upd::SetLastError( PFS::EC_INVALID_VERSION_INFO, GlobalGetText( ERR_LOCAL_VERSION_NOT_INTACT));
		//rLocalValid = false;
		return false;
	}

	GlobalNotify_TextID( TIP_CHECK_SERVER_VERSION );
	KHMsgLog_InfoW( L"using server url:%s\n", url.c_str() );

	// ���޷���ȡ�������汾��Ϣ������£���ִ���κθ��²���
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

	// ��ȡ�°汾url
	m_url = GetVersionUrl( m_newVersionInfo, true, false );
	if( m_url.empty() )
	{
		PFS::CEnv::SetLastError(PFS::EC_INVALID_VERSION_INFO );
		upd::SetLastError( PFS::EC_INVALID_VERSION_INFO, GlobalGetText( ERR_SERVER_VERSION_NOT_INTACT));
		return false;
	}

	KHMsgLog_InfoW( L"using server url:%s\n", m_url.c_str() );

	// �Ƚ�
	rHas = !PFSX::CompareVersion( m_localVersion, m_newVersionInfo) &&
		m_localVersion.m_Version <= m_newVersionInfo.m_Version &&  // �������Ӱ汾��С�ж�(������ͬ����Ϊ�п��ܽ�������˷�������)
		m_localVersion.m_ReleaseDate <= m_newVersionInfo.m_ReleaseDate; // һ���������°汾ʱ�����ӷ���ʱ����жϣ����ʱ�䵹�ã���Ϊû���°汾��������ģʽ���������ڽ����жϡ�

	if( !rHas)
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

		return true;
	}

	if( m_localVersion.m_Version < m_newVersionInfo.m_luv)
	{
		do 
		{
			if( !PFSX::GetRemotePatchMetaFile( m_url, m_patchMetas ) )
			{
				GlobalNotify_TextID( TIP_NOT_FOUND_PATCH_METAFILE );
				break;
			}

			PFSX::CPatchInfoVector piVector;
			if(!m_patchMetas.GetPatchInfosFromVersionEX(piVector, m_localVersion.m_Version, m_newVersionInfo.m_Version))
				return false;

			CUpdateManager& rUpdater = GetUpdateManager( UPDATE_MODE_BACKGROUND );
			rUpdater.SetServerVersion( m_newVersionInfo );
			rUpdater.SetUpdateServerType( m_updateServerType );
			rUpdater.SetPatchInfoVector( piVector );

		} while (0);
	}

	return true;
}

bool CUpdateManagerBackground::Initialize(LPCTSTR szRunDir)
{
	GlobalNotify( L"%1!s!,%2!s!", szRunDir, _UpdateModeName( m_UpdateMode) );
	GlobalNotify_TextID( TIP_INITIALIZING );

	m_runDir = szRunDir; // ��Ϸ��װĿ¼
	m_cacheDir = m_runDir + L".cache"; // UPDATE_MODE_REPAIR ģʽû��ִ��HasNewVersion������������Ҫ��ʼ��m_cacheDir
	m_currentPatchIndex = 0;

	if( s_hBreakEvent == INVALID_HANDLE_VALUE)
		s_hBreakEvent = CreateEvent( NULL, TRUE, FALSE, NULL );
	else
	{
		ResetEvent( s_hBreakEvent );
	}

	return true;
}

bool CUpdateManagerBackground::CheckDifference( size_t& rDownload, size_t& rMerge, size_t& rDelete )
{
	rDownload = 0;
	rMerge = 0;
	rDelete = 0;

	m_mimDownload.clear();
	m_mimDownloadFailed.clear();
	m_mimCached.clear();
	m_mimDelete.clear();

	CProfile::GetInstance().SetServerUrl( m_url, UPDATE_MODE_BACKGROUND );

	if( DetectBreakEvent() )
	{
		return false;
	}

	return true;
}

bool CUpdateManagerBackground::BeginLoop()
{
	if( m_currentPatchIndex >= (int)m_patchInfos.size() )
	{
		upd::SetLastError(  PFS::EC_INVALID_META_FILE, GlobalGetText( ERR_PATCH_LIST_EMPTY).c_str());
		return false;
	}

	m_patchpath = m_patchInfos[m_currentPatchIndex].filename;
	m_patchhash = m_patchInfos[m_currentPatchIndex].hash;
	m_patchmd5	= m_patchInfos[m_currentPatchIndex].md5;

	PFS::fsize_t patchsize = m_patchInfos[m_currentPatchIndex].size;

	// ��������Ҫ������ʽ������ΪFormatMessage��֧�֡�
	wchar_t buffer[256];
	swprintf_s( buffer, 256, L"%.2lf", patchsize/1048576.0f );

	GlobalNotify( GlobalGetText( TIP_DOWNLOAD_PROGRESS2).c_str(), m_patchpath.c_str(), buffer );
	//CResumeBrokenDownload downloader( m_patchpath , m_url, m_cacheDir, m_patchhash);
	PatchDownloader downloader( m_patchInfos[m_currentPatchIndex]);
	if( !downloader.Download( s_hBreakEvent, m_url, m_cacheDir ) )
	{
		// ��patch�ļ�������Ϊ����ʧ�ܵ��ļ�ͳ�Ƴ�����������°汾��Ϣ
		m_mimDownloadFailed.insert( std::make_pair( m_patchpath, PFS::CMetaInfo() ) );
		return false;
	}

	return true;
}

bool CUpdateManagerBackground::HasNextLoop()
{
	if( ++m_currentPatchIndex < (int)m_patchInfos.size() )
		return true;
	else
		return false;
}
