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
	// ʹ��CUpdateManager����ʹ��CUpdateManagerAutoPatch�����°汾���
	// �Ա�֤��ʹʹ��UPDATE_MODE_AUTOPATCHģʽ�����°汾��飬Ҳ�ܻ�ȡ��ȷ���
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
	// ����°汾
	//
	m_localVersion.Close();
	if( !PFSX::GetLocalVersion( m_runDir, m_localVersion) )
	{
		// .version.meta �����ڵ������Ҳ�������
		upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_READ_LOCAL_VERSION));
		rLocalValid = false;
		if( !rLocalValid )
		{
			KHMsgLog_DebugW(L"��ȡ���ذ汾��Ϣʧ�ܡ���ǰ汾��Ч.\n");
		}

		//return false;
	}
	else
	{
		rLocalValid = m_localVersion.IsValid();
		if( !rLocalValid )
		{
			KHMsgLog_DebugW(L"���ذ汾��Ч.\n");
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
		ASSERT( !m_url.empty() );
		PFS::CEnv::SetLastError(PFS::EC_INVALID_VERSION_INFO );
		upd::SetLastError( PFS::EC_INVALID_VERSION_INFO, GlobalGetText( ERR_SERVER_VERSION_NOT_INTACT));
		return false;
	}

	KHMsgLog_InfoW( L"using server url:%s\n", m_url.c_str() );

	BOOL bCacheFullyReady = false;	// ��̨������ȫ�ﵽ���°汾��
	BOOL bCachePartlyReady = false; // ��̨����ﵽĳһ�м������汾��

	rHas = !PFSX::CompareVersion( m_localVersion, m_newVersionInfo) &&
		m_localVersion.m_Version <= m_newVersionInfo.m_Version &&  // �������Ӱ汾��С�ж�(������ͬ����Ϊ�п��ܽ�������˷�������)
		m_localVersion.m_ReleaseDate <= m_newVersionInfo.m_ReleaseDate; // һ���������°汾ʱ�����ӷ���ʱ����жϣ����ʱ�䵹�ã���Ϊû���°汾��������ģʽ���������ڽ����жϡ�

	if ( rHas )
	{
		// ���ۺ�̨���»���ǰ̨���£����жϺ�̨�����Ƿ��Ѿ�׼������.
		// ������������������ǰ̨��ҪAUTOPATCHʱ������̨ɢ�ļ��Զ������Ѿ�׼����������û�б�Ҫ����AUTOPATH���¡�
		//
		// ����Ŀ¼û�а汾�ļ�,��Ϊ�а汾��Ҫ����

		// ����Ŀ¼�а汾�ļ�,�������ѹ���,��Ϊ�а汾��Ҫ����
		//
		if( PFSX::GetLocalVersion( m_cacheDir, m_cacheVersion) && m_cacheVersion.IsValid() )
		{
			bCacheFullyReady = PFSX::CompareVersion( m_cacheVersion, m_newVersionInfo );
			if( !bCacheFullyReady )
			{
				bCachePartlyReady = !PFSX::CompareVersion( m_localVersion, m_cacheVersion) &&
					m_localVersion.m_Version < m_cacheVersion.m_Version &&  // �������Ӱ汾��С�ж�(������ͬ����Ϊ�п��ܽ�������˷�������)
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

			// AUTOPATCH���url��DEFAULTģʽ���urlһ����
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

			// Ϊ�������Զ�����ģʽ׼������
			CUpdateManager& rUpdater = GetUpdateManager( UPDATE_MODE_AUTOPATCH );
			rUpdater.SetServerVersion( m_newVersionInfo );
			rUpdater.SetUpdateServerType( m_updateServerType );
			rUpdater.SetPatchInfoVector( piVector );

			// ���ò������Զ�����
			rAutoPatch = true;
		} while( 0);
	}

	return true;
}

bool CUpdateManagerAutoPatch::Initialize( LPCTSTR szRunDir)
{
	GlobalNotify( L"%1!s!,%2!s!", szRunDir, _UpdateModeName( m_UpdateMode) );
	GlobalNotify( GlobalGetText( TIP_INITIALIZING).c_str() );

	m_runDir = szRunDir; // ��Ϸ��װĿ¼
	m_cacheDir = m_runDir + L".cache"; // UPDATE_MODE_REPAIR ģʽû��ִ��HasNewVersion������������Ҫ��ʼ��m_cacheDir
	//m_p2spDir = m_runDir + L".p2sp";

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
	
	// Mount����ϵͳ
	GlobalNotify( GlobalGetText( TIP_LOAD_RUN_SYSTEM).c_str() );
	
	bool bOK = CUpdateMountManager::GetInstance().MountPFS( m_runDir, false, 
		s_hBreakEvent, m_smvLocal );

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

	// ��������Ҫ������ʽ������ΪFormatMessage��֧�֡�
	wchar_t buffer[256];
	swprintf_s( buffer, 256, L"%.2lf", patchsize/1048576.0f );

	GlobalNotify( GlobalGetText( TIP_DOWNLOAD_PROGRESS2).c_str(), m_patchpath.c_str(), buffer );
	//CResumeBrokenDownload downloader( m_patchpath , m_url, m_cacheDir, m_patchhash);
	PatchDownloader downloader( m_patchInfos[m_currentPatchIndex]);
	if( !downloader.Download( s_hBreakEvent, m_url, m_cacheDir) )
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

	if( !m_smvServer.empty() )
	{
		PFSX::SetupMetaVector smvNewAdded, smvDifference, smvUseless;
		AnalyzeSetupMetaUpdateInfo(m_smvServer, m_smvLocal, smvDifference, smvNewAdded, smvUseless );

		// ���ﲹ��Mount������pfs�Ӱ���
		// ���ڷ������Ӱ�����ʱ������
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
		// ֻ��û�гɹ���������б�Ҫ��¼��־������Commit()ʱ�Ѿ���¼������û�б�Ҫ�ظ���¼�ˡ�

		// �������3���ļ����ػ�ֻ��,��ʱǿ����Ϊ������,�ҿ�д״̬(�ݲ���鷵��ֵ)
		ShowHiddenFile( m_runDir + PFSX::FILES_META_NAME );
		ShowHiddenFile( m_runDir + PFSX::SETUP_META_NAME );
		ShowHiddenFile( m_runDir + PFSX::VERSION_META_NAME );

		bool bDownloadSucceeded = m_mimDownloadFailed.empty();
		if( !bDownloadSucceeded )
		{
			// �����ļ�����ʧ��,��¼��Щ����ʧ�ܵ��ļ�����־��
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

	// unmount����ϵͳ
	bool bRes = CUpdateMountManager::GetInstance().UnmountPFS();
	if( !bRes )
	{
		KHMsgLog_ErrorW( L"����: UnmountPFS ʧ��(%d)!\n", PFS::CEnv::GetLastError() );
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
						// ɾ����ʱĿ¼
						GlobalNotify_TextID( TIP_DELETE_CACHE_FOLDER );
						if ( !DeleteCacheFolder( m_cacheDir ) )
						{
							DWORD dwLastError = GetLastError();
							KHMsgLog_ErrorW( L"����: DeleteCacheFolder(%s)ʧ��(%d)!\n", m_cacheDir.c_str(), dwLastError );
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
				// ��Ҫ����ʱ������failed(��Ϊ��ǰ�ĸ��²�����ȫ����)
				PFS::CMetaInfoMap mimFailed;
				SubtractMetaInfoMap( m_mimCached, m_mimUpdate, mimFailed );

				// Truncate the large file that more than 1M
				PurgeLogFile(( m_runDir + L"\\log\\updatefailed.log").c_str(), 1024*1024 );

				// д����ʧ�ܵ��ļ��б�
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
