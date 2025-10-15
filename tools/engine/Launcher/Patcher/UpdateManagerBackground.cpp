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

				//if( !PFSX::CPatchMetaFile::DoWrite( m_cacheDir + PFSX::PATCH_META_NAME, m_patchMetas))
				//{
				//	KHMsgLog_ErrorW( L"后台更新：写文件失败 - %s(%d)",
				//		(m_cacheDir + PFSX::PATCH_META_NAME).c_str(), PFS::CEnv::GetLastError() );
				//}

				GlobalNotify( GlobalGetText( TIP_BACKGROUND_UPDATE_COMPLETE ).c_str() );
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
	// 检查新版本
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
			KHMsgLog_DebugW(L"本地版本无效.\n");
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
		PFS::CEnv::SetLastError(PFS::EC_INVALID_VERSION_INFO );
		upd::SetLastError( PFS::EC_INVALID_VERSION_INFO, GlobalGetText( ERR_SERVER_VERSION_NOT_INTACT));
		return false;
	}

	KHMsgLog_InfoW( L"using server url:%s\n", m_url.c_str() );

	// 比较
	rHas = !PFSX::CompareVersion( m_localVersion, m_newVersionInfo) &&
		m_localVersion.m_Version <= m_newVersionInfo.m_Version &&  // 这里增加版本大小判断(允许相同是因为有可能仅仅变更了发布日期)
		m_localVersion.m_ReleaseDate <= m_newVersionInfo.m_ReleaseDate; // 一般情况检查新版本时，增加发布时间的判断（如果时间倒置，认为没有新版本），其他模式，不对日期进行判断。

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

	m_runDir = szRunDir; // 游戏安装目录
	m_cacheDir = m_runDir + L".cache"; // UPDATE_MODE_REPAIR 模式没有执行HasNewVersion，所以这里需要初始化m_cacheDir
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

	// 浮点数需要单独格式化，因为FormatMessage不支持。
	wchar_t buffer[256];
	swprintf_s( buffer, 256, L"%.2lf", patchsize/1048576.0f );

	GlobalNotify( GlobalGetText( TIP_DOWNLOAD_PROGRESS2).c_str(), m_patchpath.c_str(), buffer );
	//CResumeBrokenDownload downloader( m_patchpath , m_url, m_cacheDir, m_patchhash);
	PatchDownloader downloader( m_patchInfos[m_currentPatchIndex]);
	if( !downloader.Download( s_hBreakEvent, m_url, m_cacheDir ) )
	{
		// 将patch文件本身作为下载失败的文件统计出来，避免更新版本信息
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
