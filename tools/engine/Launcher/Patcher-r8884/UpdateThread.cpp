
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
		// ���°汾��
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

	// Ϊ��ʹ�ٷ�����ʾЧ���Ƚ�����,����ȡ10��
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

	// TODO: ������Ҫȷ�����²���
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

		// �������ʧ�ܣ����������ͻ��ˡ�
		// ������³��������Զ�����������		
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
					// �������Զ��������֮�������°汾������
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
		rUpdater.SetPatchPath( m_patchpath ); // ����repairģʽ��˵,patch path��Ҫ����һ��

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

	// ��������ֵȷ����̨���µ�Ƶ�ʣ�ȱʡ60����һ�Σ�
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
					// ���ذ汾�����Ѿ����µ���Ч, break���û���ʾ�Ƿ��޸��ͻ���
					break;
				}

				if( (m_mode == UPDATE_MODE_DEFAULT || m_mode == UPDATE_MODE_AUTOPATCH) &&
					!m_bRebootedFlag )
				{
					// �������ȱʡģʽ���£����߼�ʹ��ȱʡģʽ���£�
					// �����Ѿ�����patcher,��ʱ����ȴ��û�ָ�����·�����
					// ����
					// ������patchģʽ���£�����ȱʡģʽ���µĵ�һ�θ��¶���������ĸ��£���Ҫ
					// break���û�ָ�����·�����(����/��ͨ)
					break;
				}

				// BUGFIX:
				// ����ǰ����ʹ�ò������Զ�����ģʽ���и���ʱ����ʹ�汾���µ������µĲ������Զ����°汾ʱ���ټ���°汾��
				// �п�����Ȼ��ɢ�ļ��Զ������°汾����ʱ��Ҫ������ģʽ�л���ȱʡģʽ������UpdateManager���ͽ�����ȷ��
				// �����ǣ���UpdateManagerAutoPath����ɢ�ļ��Զ����µ����̣��ظ����ز�������
				//
				// �����ʱBreak��LauncherDlg����ģʽ���Զ��л����������û��Break��LauncherDlg�Ļ���Ҳ��Ҫ����ͬ�Ĵ���
				// ��ȷ����ʹʹ��UPDATE_MODE_AUTOPATCHģʽ�ĸ��¹�����CheckNewVersion, ��ʱҲҪ�л���ȱʡ���¹�������
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
		case WAIT_TIMEOUT:	// ��̨���µȴ���ʱͬ����������߼�
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
			// ���ú�̨�����ڼ��ظ������ļ���̨���°汾�¼�
			// �Ա���ͻ��˶࿪ʱ���������ĺ�̨���°汾���
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
