
#include "StdAfx.h"
#include "LauncherUtils.h"
#include "../InetUtils/InetUtils.h"
#include "../../pfs/pfs2.h"
#include "LauncherConfig.h"
#include "UpdateManager.h"
#include "ErrorInfo.h"
#include "GlobalFunction.h"
#include "KHLogger.h"
#include "resource.h"

#include "UpdateThread.h"


IMPLEMENT_DYNCREATE(CUpdateThread, CWinThread)

CUpdateThread::CUpdateThread()
: m_mode( UPDATE_MODE_DEFAULT)
{
	m_bUpdatingInProgress = FALSE;
	m_bMerging = FALSE;
	m_hStopEvent = CreateEvent( NULL, TRUE, FALSE, NULL);
	m_hCheckEvent = CreateEvent( NULL, FALSE, FALSE, NULL);
	m_hUpdateEvent = CreateEvent( NULL, FALSE, FALSE, NULL);
	m_hBKCheckEvent = CreateEvent( NULL, FALSE, FALSE, NULL);
	m_hBKUpdateEvent = CreateEvent( NULL, FALSE, FALSE, NULL);
	m_bUpdatingInProgress = FALSE;
	m_bRebootedFlag = FALSE;
	m_hMergeEvent = CreateEvent( NULL, TRUE, TRUE, NULL);
}

CUpdateThread::~CUpdateThread()
{
	CloseHandle( m_hStopEvent );
	CloseHandle( m_hCheckEvent );
	CloseHandle( m_hUpdateEvent );
	CloseHandle( m_hBKCheckEvent );
	CloseHandle( m_hBKUpdateEvent );
	CloseHandle( m_hMergeEvent);
}

BOOL CUpdateThread::InitInstance()
{
	return TRUE;
}

BOOL CUpdateThread::_DoCheckVersion( UPDATE_MODE mode, BOOL& bHasNewVersion, BOOL& bLocalValid, BOOL& bAutoPatch )
{
	m_bMerging = FALSE;

	if( m_pMainWnd != NULL )
		m_pMainWnd->PostMessage( WM_CHECK_START, 10, 30 );

	CUpdateManager& rUpdater = GetUpdateManager( mode);
	rUpdater.SetUpdateServerType( m_updateServerType);
	rUpdater.SetPatchPath( m_patchpath);
	
	bool bRet = rUpdater.HasNewVersion( GetGameClientDir(), (bool&)bHasNewVersion, (bool&)bLocalValid, (bool&)bAutoPatch );
	DWORD wParam = 0;
	if( bHasNewVersion)
	{
		wParam |= HASNEWVERSION;
	}
	if( bLocalValid)
	{
		wParam |= LOCALVALID;
	}

	if( bAutoPatch)
	{
		wParam |= AUTOPATCH;
	}

	if( bRet )
	{
		// 更新版本号
		if( mode == UPDATE_MODE_DEFAULT ||
			mode == UPDATE_MODE_AUTOPATCH )
		{
			m_serverVersion = rUpdater.GetServerVersion();
		}

		if( m_pMainWnd != NULL )
			m_pMainWnd->PostMessage( WM_CHECK_END, wParam, MAKELPARAM( mode, TRUE));
	}
	else
	{
		if( m_pMainWnd != NULL )
			m_pMainWnd->PostMessage( WM_CHECK_END, wParam, MAKELPARAM( mode, FALSE) );
	}

	return bRet;
}

BOOL CUpdateThread::_DoUpdate( UPDATE_MODE mode )
{
	m_bMerging = FALSE;

	// 为了使百分数显示效果比较理想,这里取10步
	if( m_pMainWnd != NULL )
		m_pMainWnd->PostMessage( WM_UPDATE_START, 10, 100 ); 
	
	CUpdateManager& rUpdater = GetUpdateManager(mode);

	if( !rUpdater.Initialize( GetGameClientDir()) )
	{
		if( m_pMainWnd != NULL )
			m_pMainWnd->PostMessage( WM_UPDATE_END, FALSE, mode );

		return FALSE;
	}

	if( m_pMainWnd != NULL )
		m_pMainWnd->PostMessage( WM_UPDATE_STEP, 0, 2 );

	// TODO: 这里需要确定更新步长
	bool bRet = true;
	do 
	{
		if( !rUpdater.BeginLoop() )
		{
			bRet = false;
			break;
		}

		size_t nDownload = 0;
		size_t nMerge = 0;
		size_t nDelete = 0;
		bRet = rUpdater.CheckDifference(nDownload, nMerge, nDelete);
		if ( bRet )
		{
			if( m_pMainWnd != NULL )
				m_pMainWnd->PostMessage( WM_UPDATE_STEP );

			if( nDownload > 0 )
			{
				bRet = rUpdater.Download();
			}
			
			if( m_pMainWnd != NULL )
				m_pMainWnd->PostMessage( WM_UPDATE_STEP, 0, 3 );

			if( bRet && (nDownload > 0 || nMerge > 0) )
			{
				bRet = rUpdater.Verify();
			}
			
			if( m_pMainWnd != NULL )
				m_pMainWnd->PostMessage( WM_UPDATE_STEP );

			if( bRet && (nMerge > 0 || nDelete > 0 ) )
			{
				::WaitForSingleObject( m_hMergeEvent, INFINITE);
				m_bMerging = TRUE;
			}

			if( bRet && nDelete > 0 )
			{
				bRet = rUpdater.Purge();
			}

			if( m_pMainWnd != NULL )
				m_pMainWnd->PostMessage( WM_UPDATE_STEP, 0, 1 );

			if( bRet && nMerge > 0 )
			{
				bRet = rUpdater.Merge();
			}

			if( m_pMainWnd != NULL )
				m_pMainWnd->PostMessage( WM_UPDATE_STEP, 0, 1 );
		}
		else
		{
			if( m_pMainWnd != NULL )
			{
				m_pMainWnd->PostMessage( WM_UPDATE_STEP, 0, 7 );
			}
		}

		if( bRet )
		{
			bRet = rUpdater.Commit();
		}

		rUpdater.EndLoop( bRet );

		if( !bRet )
		{
			break;
		}

	} while( rUpdater.HasNextLoop() );

	if( !rUpdater.Destroy( bRet ) )
	{
		bRet = false;
	}

	m_bMerging = FALSE;

	if( m_pMainWnd != NULL )
	{
		m_pMainWnd->PostMessage( WM_UPDATE_END, bRet, (LPARAM) mode );

		// 如果更新失败，无需重启客户端。
		// 避免更新程序无限自动重启的现象。		
		if( bRet )
		{
			if( CUpdateManager::IsNeedReboot() )
			{
				// Reboot launcher
				m_pMainWnd->PostMessage( WM_LAUNCHER_UPDATE_RESULT, (WPARAM)mode, (LPARAM) 1 );
			}
			else
			{
				if( mode == UPDATE_MODE_AUTOPATCH )
				{
					// 补丁包自动更新完毕之后，启动新版本检查过程
					m_pMainWnd->PostMessage(WM_COMMAND, IDC_CHECK_UPDATE, 0 );
				}
			}
		}
	}

	return TRUE;
}

void CUpdateThread::CheckVersion(UPDATE_MODE mode)
{
	m_mode = mode;
	switch ( mode )
	{
	case UPDATE_MODE_DEFAULT:
	case UPDATE_MODE_PATCH:
	case UPDATE_MODE_REPAIR:
	case UPDATE_MODE_AUTOPATCH:
		SetEvent( m_hCheckEvent );
		break;

	case UPDATE_MODE_BACKGROUND:
		SetEvent( m_hBKCheckEvent );
		break;
	default:
		break;
	}
}

void CUpdateThread::Update(UPDATE_MODE mode)
{
	CUpdateManager& rUpdater = GetUpdateManager( mode );
	rUpdater.SetUpdateServerType( m_updateServerType);

	if( mode == UPDATE_MODE_REPAIR )
		rUpdater.SetPatchPath( m_patchpath ); // 对于repair模式来说,patch path需要重置一次

	m_mode = mode;

	switch ( mode )
	{
	case UPDATE_MODE_DEFAULT:
	case UPDATE_MODE_PATCH:
	case UPDATE_MODE_REPAIR:
	case UPDATE_MODE_AUTOPATCH:
		SetEvent( m_hUpdateEvent );
		break;

	case UPDATE_MODE_BACKGROUND:
		SetEvent( m_hBKUpdateEvent );
		break;
	default:
		break;
	}
}

void CUpdateThread::Stop()
{
	upd::SetLastError( SC_USER_CANCELED );
	CUpdateManager::TriggerBreakEvent();
	SetEvent( m_hStopEvent );
}

int CUpdateThread::Run()
{
	HANDLE hEventArray[] =
	{
		m_hStopEvent, 
		m_hCheckEvent,
		m_hUpdateEvent,
		m_hBKCheckEvent,
		m_hBKUpdateEvent,
	};

	// 根据配置值确定后台更新的频率（缺省60分钟一次）
	const DWORD dwTime = GetBackgroundUpdateFrequency() * 60000;

	BOOL bQuit = FALSE;
	do 
	{
		DWORD dwWaitResult = WaitForMultipleObjects(ARRAYSIZE(hEventArray), hEventArray, FALSE, dwTime);
		switch ( dwWaitResult )
		{
		case WAIT_OBJECT_0:
			bQuit = TRUE;
			break;
		case WAIT_OBJECT_0 + 1:
			{
				SetThreadPriority( THREAD_PRIORITY_NORMAL );
				m_bUpdatingInProgress = TRUE;
				BOOL bHasNewVersion = FALSE;
				BOOL bLocalValid = TRUE;
				BOOL bAutoPatch = FALSE;
				BOOL bRet = _DoCheckVersion( m_mode, bHasNewVersion, bLocalValid, bAutoPatch );

				m_bUpdatingInProgress = FALSE;
				if( !bRet || (!bHasNewVersion && bLocalValid) )
				{
					break;
				}
				
				if( m_mode != UPDATE_MODE_BACKGROUND &&
					!bHasNewVersion &&
					!bLocalValid &&
					!m_bRebootedFlag )
				{
					// 本地版本尽管已经最新但无效, break给用户提示是否修复客户端
					break;
				}

				if( (m_mode == UPDATE_MODE_DEFAULT || m_mode == UPDATE_MODE_AUTOPATCH) &&
					!m_bRebootedFlag )
				{
					// 如果不是缺省模式更新，或者即使是缺省模式更新，
					// 但是已经重启patcher,此时无需等待用户指定更新服务器
					// 即：
					// 当不是patch模式更新，或者缺省模式更新的第一次更新而非重启后的更新，需要
					// break给用户指定更新服务器(电信/网通)
					break;
				}

				// BUGFIX:
				// 当先前正在使用补丁包自动更新模式进行更新时，即使版本更新到了最新的补丁包自动更新版本时，再检查新版本，
				// 有可能仍然有散文件自动更新新版本。此时需要将更新模式切换到缺省模式，否则，UpdateManager类型将不正确。
				// 其结果是，用UpdateManagerAutoPath，走散文件自动更新的流程，重复下载补丁包。
				//
				// 如果此时Break给LauncherDlg处理，模式会自动切换，但，如果没有Break给LauncherDlg的话，也需要做相同的处理。
				// 即确保即使使用UPDATE_MODE_AUTOPATCH模式的更新管理器CheckNewVersion, 此时也要切换到缺省更新管理器。
				if( !bAutoPatch )
				{
					if( m_mode == UPDATE_MODE_AUTOPATCH )
					{
						m_mode = UPDATE_MODE_DEFAULT;
					}
				}
			}
		case WAIT_OBJECT_0 + 2:
			m_bUpdatingInProgress = TRUE;
			_DoUpdate( m_mode );
			m_bUpdatingInProgress = FALSE;
			break;
		case WAIT_TIMEOUT:	// 后台更新等待超时同样走下面的逻辑
		case WAIT_OBJECT_0 + 3:
			{
				SetThreadPriority( THREAD_PRIORITY_BELOW_NORMAL );
				m_bBKUpdatingInProgress = TRUE;
				BOOL bHasNewVersion = FALSE;
				BOOL bLocalValid = TRUE;
				BOOL bAutoPatch = FALSE;
				BOOL bRet = _DoCheckVersion( UPDATE_MODE_BACKGROUND, bHasNewVersion, bLocalValid, bAutoPatch );
				m_bBKUpdatingInProgress = FALSE;
				if( !bRet || !bHasNewVersion )
				{
					SetThreadPriority( THREAD_PRIORITY_NORMAL );
					break;
				}
				// else fall through
			}
		case WAIT_OBJECT_0 + 4:
			m_bBKUpdatingInProgress = TRUE;
			_DoUpdate( UPDATE_MODE_BACKGROUND );
			// 重置后台更新期间重复启动的检查后台更新版本事件
			// 以避免客户端多开时连续启动的后台更新版本检查
			ResetEvent( m_hBKCheckEvent ); 
			ResetEvent( m_hBKUpdateEvent );
			m_bBKUpdatingInProgress = FALSE;
			SetThreadPriority( THREAD_PRIORITY_NORMAL );
			break;
		default:
			ASSERT( FALSE );
			bQuit = TRUE;
			break;
		}
	} while ( !bQuit );

	return 1;
}

BOOL CUpdateThread::IsUpdatingInProgress() const
{
	return m_bUpdatingInProgress;
}

void CUpdateThread::SetUpdateServerType( UPDATE_SERVER_TYPE type)
{
	m_updateServerType = type;
}

BOOL CUpdateThread::IsMerging() const
{
	return m_bMerging;
}

BOOL CUpdateThread::SetMergeEvent()
{
	return ::SetEvent( m_hMergeEvent);
}

BOOL CUpdateThread::ResetMergeEvent()
{
	return ::ResetEvent( m_hMergeEvent);
}

// static 
bool CUpdateThread::IsNeedReboot()
{
	return CUpdateManager::IsNeedReboot();
}
