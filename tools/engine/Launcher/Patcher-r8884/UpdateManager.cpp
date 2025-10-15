///=============================================================================
/// Product   : MHZX
/// Project   : Launcher
/// Module    : UpdateManager.cpp
/// Copyright : (c) 2008 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	���¹�����
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
					  const std::wstring& url,			// ��֤�ǿ�
					  const std::wstring& rundir,		// ����Ŀ¼
					  PFS::CMetaInfoMap& rMimServer,	// ��������Ŀ¼�ļ��б�
					  PFS::CMetaInfoMap& rMimLocal,		// ���ظ�Ŀ¼�ļ��б�
					  PFS::CMetaInfoMap& rMimDifference	// ��������Ŀ¼�ͱ��ظ�Ŀ¼�Ĳ����б�
					  )

{
	rMimServer.clear();
	rMimLocal.clear();
	rMimDifference.clear();

	// ������ mount Ŀ¼�� .files.meta �ļ�
	if( !ReadMetaInfoFile( RUN_ROOT, rMimLocal))
	{
		// .file.meta �п��ܲ�����
		if( PFS::CEnv::GetLastError() != PFS::EC_FILE_NOT_FOUND)
		{
			return false;
		}
	}

	// �������� mount Ŀ¼�� .files2.meta �ļ�
	GlobalNotify_TextID( TIP_DOWNLOAD_META_FILE );
	if( !DownloadMetaInfoFile2( url, rMimServer))
	{
		return false;
	}

	// ����2��MetaInfoMap�Ĳ(��������µ��ļ��б� - ֻ��Ը�)
	SubtractMetaInfoMap( rMimServer, rMimLocal, rMimDifference);

	// always true!
	return true;
}

bool CUpdateManager::IsNeedUpdateLauncher( const PFS::CMetaInfoMap& mim)
{
	// TODO: ���ǽ����ֵ�ó�Ա��������������ͨ���ⲿ���룩
	std::wstring strLauncherFolder = L"/";
	strLauncherFolder += GetLauncherFolder();
	strLauncherFolder += L"/";
	std::wstring strLauncherFolderEnd = strLauncherFolder;
	strLauncherFolderEnd[strLauncherFolder.size()-2]++;

	// �����Ƿ���Ҫ����Launcher��Patcher��ص��ļ�,��ȷ���Ƿ���Ҫ����patcher
	PFS::CMetaInfoMap::const_iterator rBegin = mim.lower_bound( strLauncherFolder );
	PFS::CMetaInfoMap::const_iterator rEnd = mim.lower_bound( strLauncherFolderEnd );
	if( std::distance( rBegin, rEnd) != 0)
	{
		return true;
	}

	return false;
}

bool EvalUpdateItems2(
					 const std::wstring& url,				// ��֤�ǿ�
					 const std::wstring& rundir,			// ����Ŀ¼
					 const std::wstring& cacheDir,			// ����Ŀ¼
					 const PFSX::SetupMetaVector& smvServer,// ���������µ� SetupMeta ��Ϣ
					 const PFSX::SetupMetaVector& smvDiff,	// ��Ҫ���µ� SetupMeta ��Ϣ
					 PFS::CMetaInfoMap& rMimDownload,		// ��Ҫ���ص��ļ��б�
					 PFS::CMetaInfoMap& rMimCached,			// ���������е���Ҫֱ�Ӻϲ����ļ��б�,���������ص��ļ�
					 PFS::CMetaInfoMap& rDelete,			// ������Ҫɾ�����ļ��б�
					 bool& rbNeedUpdateLauncher,			// �Ƿ���Ҫ����launcher/patcher
					 bool bIsBackground,						// �Ƿ��Ǻ�̨����
					 HANDLE hBreakEvent
					 )
{
	rMimDownload.clear();
	rMimCached.clear();
	rDelete.clear();
	rbNeedUpdateLauncher = false;

	KHMsgLog_DebugW( L"���ڼ��� %s �еĸ����б�.\n", rundir.c_str() );
	PFS::CMetaInfoMap mimRootServer, mimRootLocal;
	if( !EvalUpdateItemsForRoot( url, rundir, mimRootServer, mimRootLocal, rMimDownload) )
	{
		return false;
	}

	if( !rMimDownload.empty() )
	{
		KHMsgLog_DebugW( L"%s ����Ҫ���� %lu �\n", rundir.c_str(), rMimDownload.size() );
	}

	rbNeedUpdateLauncher = false;
	if( !bIsBackground)
	{
		rbNeedUpdateLauncher = CUpdateManager::IsNeedUpdateLauncher( rMimDownload );
	}

	// ͳ�Ƹ�����Ҫɾ�����ļ�
	SubtractMetaInfoMapEx( mimRootLocal, mimRootServer, rDelete);

	// �Ƴ�����ɾ�����ļ�
	DealUnDeleteFiles( rDelete, smvServer );
	if( !rDelete.empty() )
	{
		KHMsgLog_DebugW( L"%s ����Ҫɾ�� %lu �\n", rundir.c_str(), rDelete.size() );
	}

	if( !rbNeedUpdateLauncher)
	{
		// Ϊ�˱����ظ������Ӱ���meta��Ϣ,���Ǹ����Ƿ���Ҫ����launcher�������Ƿ���Ӱ����д���.
		// ��launcher��Ҫ���Ҹ���ʱ,launcher������,����ǰ�������Ӱ��ĸ���,�Ա����������ظ������Ӱ�meta.

		// ʹ�öϵ��������������Ӱ���.files2.meta�ļ������ص��ļ��ŵ������Ӧ��Ŀ¼��
		CSetupMetasDownloader downloader( smvDiff, url, cacheDir );
		if( !downloader.Download( hBreakEvent ) )
		{
			GlobalNotify_TextID( ERR_DOWNLOAD_CHILD_META_FILE );
			return false;
		}

		for( PFSX::SetupMetaVector::const_iterator iV=smvDiff.begin(); iV!=smvDiff.end(); ++iV)
		{
			const std::wstring& mount = iV->GetName();

			// ������ mount Ŀ¼�� .files.meta �ļ�
			GlobalNotify( GlobalGetText( TIP_ANALYZE_CHILD_META_FILE).c_str(), RUN_ROOT, mount.c_str());

			PFS::CMetaInfoMap mimLocal;
			if( !ReadMetaInfoFile( RUN_ROOT + mount, mimLocal))
			{
				// .file.meta �п��ܲ�����
				int nErrorCode = PFS::CEnv::GetLastError();
				if( nErrorCode != ERROR_PATH_NOT_FOUND &&
					nErrorCode != ERROR_FILE_NOT_FOUND &&
					nErrorCode != PFS::EC_FILE_NOT_FOUND )
				{
					return false;
				}
			}

			// ��cacheĿ¼mount��Ŀ¼�� .files2.meta �ļ�
			GlobalNotify( GlobalGetText( TIP_ANALYZE_CHILD_META_FILE).c_str(), RES_ROOT, mount.c_str());

			PFS::CMetaInfoMap mimCache;

			// ע�⣬����ʹ��cacheDir������ʹ��RES_ROOT����Ϊ�����е��ļ�����CNativeFile��ʽ�����ġ�
			if( !ReadMetaInfoFile2( cacheDir + mount, mimCache ) )
			{
				return false;
			}

			if( mimLocal.size() != mimCache.size() )
			{
				KHMsgLog_DebugW( L"%s �б����� %lu ��������� %lu �\n", mount.c_str(),
					mimLocal.size(), mimCache.size() );
			}

			PFS::CMetaInfoMap mimDelete;
			// ͳ����Ҫɾ�����ļ�
			SubtractMetaInfoMapEx( mimLocal, mimCache, mimDelete );
			mimDelete.erase( PFSX::FILES_META_NAME);

			if( !mimDelete.empty() )
			{
				KHMsgLog_DebugW( L"%s ����Ҫɾ�� %lu �\n", mount.c_str(), mimDelete.size() );
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
			// ����2��MetaInfoMap�Ĳ
			SubtractMetaInfoMap( mimCache, mimLocal, mimDownload);
			if( !mimDownload.empty() )
			{
				KHMsgLog_DebugW( L"%s ����Ҫ���� %lu �\n", mount.c_str(), mimDownload.size() );
				for( PFS::CMetaInfoMap::iterator i=mimDownload.begin(), e=mimDownload.end(); i!=e; ++i)
				{
					rMimDownload.insert( std::make_pair( mount+i->first, i->second));
				}
			}
		}
	}

	rMimDownload.erase( PFSX::VERSION_META_NAME );	// .version.meta �Ȳ����������
	rMimDownload.erase( PFSX::SETUP_META_NAME);		// .setup.meta ͬ��

	if( rMimDownload.empty() )
	{
		// û���ļ���Ҫ����
		return true;
	}

	//
	// ��һ�������Ѿ�������ļ�
	//

	GlobalNotify_TextID( TIP_READ_CACHE_META_FILE );

	PFS::CMetaInfoMap mimCache;
	bool bCached = ReadMetaInfoFile( cacheDir, mimCache );
	if( !bCached )
	{
		// ��Cache�е�MetaInfo�ļ�ʧ�ܣ�����������
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

// ��ȡָ���汾��Ϣ�е�url
std::wstring CUpdateManager::GetVersionUrl(
	const PFSX::CVersionInfo& rVersionInfo, bool bUseConfigIfNeeded, bool bUseDefault ) const
{
	int type = m_updateServerType*PFSX::VERSION_COUNT + m_UpdateMode;
	const PFSX::VersionUpdateInfo *pUpdateInfo = rVersionInfo.GetUpdateInfoByType(type);
	if( NULL == pUpdateInfo )
	{
		GlobalNotify_TextID( WRN_MISSING_DEFAULT_URL );
		// ����ȡ��һ�ַ�����(���÷�����)�����ͻ�ȡ����URL
		type = (!m_updateServerType)*PFSX::VERSION_COUNT + m_UpdateMode;
		if( NULL == ( pUpdateInfo = rVersionInfo.GetUpdateInfoByType( type ) ) )
		{
			GlobalNotify_TextID( WRN_MISSING_BACKUP_URL );
			// ���ָ��type�����ڣ���ʹ��Ĭ��type��֤�ܼ�������
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
		// ͨ�������ļ���ȡurl
		CProfile::GetInstance().GetServerUrl( url, m_UpdateMode );
		if( !url.empty() )
		{
			GlobalNotify( GlobalGetText( WRN_USE_SETTING_URL).c_str(), url.c_str() );
			return url;
		}
	}

	if( bUseDefault )
	{
		// ����Ϊ��ǰ�汾ȱʡ�ṩ��url
		CProfile::GetInstance().GetDefaultUrl( url, m_updateServerType, m_UpdateMode);
		GlobalNotify( GlobalGetText( WRN_USE_DEFAULT_VALUE).c_str(), url.c_str() );
	}

	return url;
}

bool CUpdateManagerWhole::Initialize( LPCTSTR szRunDir)
{
	m_runDir = szRunDir; // ��Ϸ��װĿ¼
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

	m_runDir = szRunDir; // ��Ϸ��װĿ¼
	m_cacheDir = m_runDir + L".cache"; // UPDATE_MODE_REPAIR ģʽû��ִ��HasNewVersion������������Ҫ��ʼ��m_cacheDir

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
		if( !PFS::CFileIO::IsFileExisting( m_runDir+i->GetName()+L".pfs"))
		{
			i->SetHash( 0);
		}
	}

	// ���� SetupMeta �Ĳ����б�
	PFSX::SetupMetaVector smvDifference;
	if( m_smvServer.empty() )
	{
		smvDifference = m_smvLocal;
	}
	else
	{
		if ( m_smvLocal.IsOriginal() || // ��汾����.setup.meta������
			!m_localVersion.IsValid() )	// ���ذ汾��Чʱ����������pfs��
		{
			smvDifference = m_smvServer; // ʹ�÷�����.setup.meta
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

	// Mount����ϵͳ
	GlobalNotify_TextID( TIP_LOAD_RUN_SYSTEM );
	// ���ں�̨���£�������ֻ��ģʽMount����ϵͳ
	bool bOK = CUpdateMountManager::GetInstance().MountPFS( m_runDir, IsBackground(),
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

// ע�⣺�����¹��̲�Ҫ��Initialize�������Ҫָ��runDir
bool CUpdateManager::HasNewVersion( LPCTSTR szRunDir, bool& rHas, bool& rLocalValid, bool& rAutoPatch )
{
	GlobalNotify_TextID( TIP_CHECK_LOCAL_VERSION );

	m_runDir = szRunDir;
	m_cacheDir = m_runDir + L".cache";
	m_newVersionInfo.Close();
	rAutoPatch = false;

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

	BOOL bCacheReady = false;

	// �Ƚ�
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
		PFSX::CVersionInfo cacheVersion;
		if( PFSX::GetLocalVersion( m_cacheDir, cacheVersion) )
		{
			bCacheReady = PFSX::CompareVersion( cacheVersion, m_newVersionInfo );
		}

		if( bCacheReady && IsBackground() )
		{
			// ��̨��������ɵĻ������ں�̨����ģʽ��˵������û���°汾��Ҫ���¡�
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

	// ����ʲô����ģʽ�������������£���û�б�Ҫ����LUV��
	// ��̨������ʱ������LUV(����̨����Ҳ����LUVʱ���޸Ĵ˴��߼�)
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

			// Ϊ�������Զ�����ģʽ׼������
			CUpdateManager& rUpdater = GetUpdateManager( UPDATE_MODE_AUTOPATCH );
			rUpdater.SetServerVersion( m_newVersionInfo );
			rUpdater.SetUpdateServerType( m_updateServerType );
			rUpdater.SetPatchInfoVector( piVector );

			// ���ò������Զ�����
			rAutoPatch = true;

		} while( 0 );
	}

	if( ( rHas || !rLocalValid ) && !rAutoPatch )
	{
		m_smvServer.clear();
		bool bSetupMetaReady = false;
		if( bCacheReady )
		{
			// �������������£��ӻ���� .setup.meta �ļ�(�п��ܲ�����)
			bSetupMetaReady = PFSX::CSetupMetaFile::DoRead( m_cacheDir + PFSX::SETUP_META_NAME, m_smvServer);
			if( !bSetupMetaReady )
			{
				// bCacheReady = false; // �˱�������ʹ�ã���ʱû�б�Ҫ�޸�Ϊfalse��
				KHMsgLog_WarningW( L"Read SetupMetaFile failed: %s(%d)\n",
					(m_cacheDir + PFSX::SETUP_META_NAME).c_str(), PFS::CEnv::GetLastError() );
			}
		}

		// ����û�о������ߴӻ����ȡʧ�ܣ��ôӷ��������� .setup.meta �ļ�
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

	// �ӷ��������������ļ��б�
	// ���ʣ������ʽ�����ǲ��Ǹ�����ã�
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

	// ��ȡ�°汾url
	m_url = GetVersionUrl( m_newVersionInfo, true, false );
	if( m_url.empty() )
	{
		ASSERT( !m_url.empty() );
		PFS::CEnv::SetLastError(PFS::EC_INVALID_VERSION_INFO );
		upd::SetLastError( PFS::EC_INVALID_VERSION_INFO, GlobalGetText( ERR_SERVER_VERSION_NOT_INTACT));
		return false;
	}

	// ���浽�����ļ�
	if( IsBackground())
	{
		CProfile::GetInstance().SetServerUrl( m_url, UPDATE_MODE_BACKGROUND );
	}
	else
	{
		CProfile::GetInstance().SetServerUrl( m_url, UPDATE_MODE_DEFAULT );
	}

	// ͳ�Ƹ�����Ŀ
	GlobalNotify_TextID( TIP_ARRANGE_UPDATE_LIST );
	if( !EvalUpdateItems2( m_url, m_runDir, m_cacheDir,
		m_smvServer, CUpdateMountManager::GetInstance().GetSetupMetaVector(),
		m_mimDownload, m_mimCached, m_mimDelete, s_bNeedReboot, IsBackground(), s_hBreakEvent ) )
	{
		m_mimDownloadFailed.insert( std::make_pair( PFSX::FILES_META_NAME, PFS::CMetaInfo() ) );
		return false;
	}

	// ���ں�̨����,��ִ�кϲ�����
	if( IsBackground())
	{
		rDelete = 0;
		rMerge = 0;
	}
	else
	{
		rDelete = m_mimDelete.size();
		// ����ļӷ�������ܲ�׼ȷ,�����޹ؽ�Ҫ
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
		// ���ﲻ��д�汾��Ϣ����Ϊ��� Destroy ��ͳһ����
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
// �����°汾�ļ�����ʱĿ¼��
//
bool CUpdateManager::Download( )
{
	GlobalNotify_TextID( TIP_SAVE_NEW_VERSION );

	if( DetectBreakEvent() )
	{
		return false;
	}

	std::wstring briefDir;	// BriefĿ¼
	briefDir = m_runDir + L".brief";
	CreateHiddenDirectory( briefDir);

	// ������֮ǰ�����ȸ��ݸ���ģʽ����汾��Ϣ�ļ���
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
	// ����Ҫ���ص��ļ��б���Ϣ���浽BRIEFĿ¼�У�����ִ������
	if( !WriteMetaInfoFile( briefDir, m_mimDownload ) )
	{
		GlobalNotifyErrorInfo( upd::GetLastError(), GlobalGetText( ERR_SAVE_UPDATE_META_FILE).c_str() );
		return false;
	}
	else
	{
		GlobalNotify_TextID( TIP_SAVE_UPDATE_META_FILE );
	}

	// ������Ҫ��Щ��Ϣ
	m_mimDownload.clear();

	if( DetectBreakEvent() )
	{
		return false;
	}

	m_mimDownloadFailed.clear();

	// �����ļ�
	GlobalNotify_TextID( TIP_START_DOWNLOAD );
	CDownloadFiles downloader( m_runDir );
	bool res = downloader.Download(s_hBreakEvent);

	// ������Ҫ briefDir
	if( !DeleteCacheFolder( briefDir ) )
	{
		upd::SetLastError( ::GetLastError(), GlobalFormatText( ERR_DELETE_BRIEF_FOLDER, briefDir.c_str()) );
	}

	if( !res)
	{
		// ����ʧ�ܵ���Ŀ
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
		// �����û�Ҫ�˳����£�����ҲҪ����һ�������Ѿ����ص��ļ��б�
		// ����͵��°װ����ش����ļ�
		// �һᵼ�°汾��Ϣ�ļ����޹ʸ���
	}

	GlobalNotify_TextID( TIP_SAVE_CACHE_META_FILE );

	// ���سɹ�����Ŀ
	PFS::CMetaInfoMap& rmimDownloaded = downloader.GetDownloaded();

	//////////////////////////////////////////////////////////////////////////
	// ��ʱCacheĿ¼�е�.files.metaӦ���Ѿ�����
	// ����ֱ�Ӻϲ�2��MetaInfo����.
	// �ϲ������صĺͻ��������е�.
	// СС�Ż�����С���Ϻϲ����󼯺��У�Ȼ���󲢼�
	if( m_mimCached.size() >= rmimDownloaded.size() )
	{
		m_mimCached.insert( rmimDownloaded.begin(), rmimDownloaded.end() );
	}
	else
	{
		rmimDownloaded.insert( m_mimCached.begin(), m_mimCached.end() );
		m_mimCached.swap( rmimDownloaded );
	}

	// ����û��˳����ź�
	if( DetectBreakEvent() )
	{
		return false;
	}

	return true;
}


//
//	����ͻ����з������ļ�
//
bool CUpdateManager::Purge( )
{
	if( DetectBreakEvent() )
	{
		return false;
	}

	if( !s_bNeedReboot )
	{
		// ɾ�������ļ�
		if( !DeleteLocalFiles())
		{
			// Ҫ�����������ô��
		}
	}

	return true;
}

// ɾ�������ļ�
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
				// �����÷���
				upd::SetLastError( PFS::CEnv::GetLastError(),
					GlobalFormatText( ERR_DELETE_FILE, itor->first.c_str()) );
			}
		}
	}
	GlobalNotifyEnd();

	// Truncate the large file that more than 1M
	PurgeLogFile(( m_runDir + L"\\log\\deleted.log").c_str(), 1024*1024 );

	// дɾ�����ļ��б���־�У��������
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

// У���ļ�
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
	// �汾��Ϣ�ļ���MD5����Ϊ0, ������Ҫ�ų�У��,�����ܻ�ʧ��
	// ���Ҹ��ļ�Ҳ����ϲ�

	// ��CheckFileEx��ʽУ��strDirĿ¼�е��ļ�
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

	// ����Ҫ���µĳ����ļ� Copy����ʱĿ¼������Ŀ¼��
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

	// Mount����ϵͳ
	GlobalNotify_TextID(  TIP_MOUNT_RESOURCE );

	// Mount��Դ��ϵͳ
	bool bOK = CUpdateMountManager::GetInstance().MountPFS( m_runDir, false, s_hBreakEvent, m_smvServer);
	if ( !bOK )
	{
		//const upd::ErrorInfo& rErr = upd::GetLastError();
		//GlobalNotifyErrorInfo( rErr, L"���°汾ʧ�ܣ�" );
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

				GlobalNotify_TextID( TIP_BACKGROUND_UPDATE_COMPLETE );
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

// �ύ�ɹ����µ�����
bool CUpdateManager::Commit()
{
	bool bSucceeded = true;

	GlobalNotify_TextID( TIP_COMMITING );

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
			KHMsgLog_ErrorW( L"Open file (%s,%d) failed.", (m_runDir + L"\\log\\downloadfailed.log").c_str(),
				(int)(std::ios_base::out | std::ios_base::app) );
		}

		m_mimDownloadFailed.clear();
	}

	bool bUpdateSucceeded = true;

	// Commit����ϵͳ�ĸ���
	bool bRes = CUpdateMountManager::GetInstance().CommitPFS( s_hBreakEvent);

	if( !s_bNeedReboot )
	{
		// ��д .setup.meta �ļ�
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
				bDownloadSucceeded &&	// ������ļ�����ʧ��,�����°汾�ļ�
				!DetectBreakEvent()	&&	// ����û��Ѿ���������,���ٸ��°汾�ļ�
				m_newVersionInfo.IsValid()
				)
			{
				// �����°汾��Ϣд��runĿ¼
				if( !PFSX::WriteVersionMetaFile( m_runDir + PFSX::VERSION_META_NAME,
					m_newVersionInfo, PFSX::VERSION_ALL_TYPES ) )
				{
					// �ܲ��ң�ֻ�а汾��Ϣ�ļ�����ʧ�ܣ�
					upd::SetLastError( ::GetLastError(),
						GlobalFormatText( ERR_SAVE_VERSION_FILE, PFSX::VERSION_META_NAME.c_str()) );
					GlobalNotifyErrorInfo( upd::GetLastError() );
					bRes = false;
				}
			}
		}
		else
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

	if( bRes )
	{
		// д���³ɹ����ļ��б��п��ܽ����ǲ��ֳɹ���
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
