// LauncherDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "Launcher.h"
#include "LauncherDlg.h"
#include "LauncherUtils.h"
#include "UpdateThread.h"
#include "LauncherConfig.h"
#include "ErrorInfo.h"
#include "GlobalNotification.h"
#include "wininet.h"
#include "KHLogger.h"
#include "SelectServerDialog.h"
#include "Profile.h"
#include "TextManager.h"
#include "TextID.h"
#include "SetDownloadSvrDlg.h"

// ���ڽ��̱����Ľ��̴�������(PP)
#include "../ParentCheck/Cross_MHZX.h"
#include "../ParentCheck/VMProtectSDK.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#define WM_TRAY_NOTIFY		WM_USER+600
#define WM_KICKIDLE         0x036A 

bool StartEngine();

static BOOL _PumpMessage()
{
	_AFX_THREAD_STATE *pState = AfxGetThreadState();

	// ע������������AfxPumpMessage()ʵ�֣����˴�����PeekMessage������ʹ��GetMessage()
	if (!::PeekMessage(&(pState->m_msgCur), NULL, NULL, NULL, PM_REMOVE))
	{
		return FALSE;
	}

	// process this message
	if (pState->m_msgCur.message != WM_KICKIDLE && !AfxPreTranslateMessage(&(pState->m_msgCur)))
	{
		::TranslateMessage(&(pState->m_msgCur));
		::DispatchMessage(&(pState->m_msgCur));
	}

	return TRUE;
}


void WaitThreadUntilQuit( HANDLE& rhThread )
{
	if ( rhThread != INVALID_HANDLE_VALUE )
	{
		DWORD dwRet = 0;
		DWORD dwExitCode = 0;
		BOOL bRet = TRUE;
		bRet = GetExitCodeThread( rhThread, &dwExitCode );
		if ( bRet && dwExitCode == STILL_ACTIVE )
		{
			int iTryCount = 120;
			do 
			{
				dwRet = WaitForSingleObject(rhThread,1000);
				_PumpMessage();

			} while ( dwRet == WAIT_TIMEOUT && iTryCount-- > 0 );
			
			if( dwRet == WAIT_TIMEOUT )
			{
				// �ȴ���ʱ��ֻ��ǿ����ֹ�̣߳�һ��ᵼ���ڴ�й¶
				TerminateThread( rhThread, -1 );
			}
			else if( dwRet == WAIT_OBJECT_0 )
			{
				// �߳�������ֹ��
			}
			else// if ( dwRet == WAIT_FAILED )
			{
				// ��������
			}
		}

		CloseHandle( rhThread );
		rhThread = INVALID_HANDLE_VALUE;
	}
}

// �������·��
// �����launcher����,��ʱModuleDir�غ���patcher.1��patcher
bool CheckStartupPath( )
{
	CString strInstallDir = GetInstallDir();
	CString strModuleDir = GetModuleDir();
	strModuleDir.MakeLower();
	int iPos = strModuleDir.Find( _T("patcher"), strInstallDir.GetLength() );
	if ( iPos != strInstallDir.GetLength() )
	{
		return false;
	}
	return true;
}

// CLauncherDlg �Ի���

CLauncherDlg::CLauncherDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CLauncherDlg::IDD, pParent)
	, m_trayIcon( IDR_MAINFRAME )
{
	m_hIcon = NULL;
	m_hIconLarge = NULL;
	m_hIconSmall = NULL;
	m_pThisWatcher = NULL;
	m_pUpdateThread = NULL;
	m_hBKBrush = NULL;
	m_globalStatusControlColor = 0;
	m_stepMessageControlColor = 0;
}

CLauncherDlg::~CLauncherDlg()
{
	// Delete Thread Object
	if ( m_pUpdateThread != NULL )
	{
		if( !m_pUpdateThread->m_bAutoDelete )
		{
			m_pUpdateThread->m_bAutoDelete = TRUE;
			m_pUpdateThread->Delete();
		}

		m_pUpdateThread = NULL;
	}

	if( m_hBKBrush != NULL )
	{
		DeleteObject( m_hBKBrush );
		m_hBKBrush = NULL;
	}
}

void CLauncherDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CLauncherDlg, CDialog)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_BN_CLICKED(IDC_CHECK_UPDATE, &CLauncherDlg::OnBnClickedCheckUpdate)
	ON_BN_CLICKED(IDC_LAUNCH_GAME, &CLauncherDlg::OnBnClickedLaunchGame)
	ON_BN_CLICKED(IDMINIMIZE, &CLauncherDlg::OnBnClickedMinimize)
	ON_NOTIFY_EX( TTN_NEEDTEXT, 0, &CLauncherDlg::OnToolTipNotify)
	ON_MESSAGE( WM_TRAY_NOTIFY, &CLauncherDlg::OnTrayNotify)
	ON_WM_NCHITTEST()
	ON_WM_CREATE()
	ON_WM_ERASEBKGND()
	ON_MESSAGE( WM_GLOBALSTATUS, OnGlobalStatus )
	ON_MESSAGE( WM_CHECK_START, OnCheckStart )
	ON_MESSAGE( WM_CHECK_STEP, OnCheckStep )
	ON_MESSAGE( WM_CHECK_END, OnCheckEnd )
	ON_MESSAGE( WM_PROGRESS_START, OnProgressStart )
	ON_MESSAGE( WM_PROGRESS_STEP, OnProgressStep )
	ON_MESSAGE( WM_PROGRESS_END, OnProgressEnd )
	ON_MESSAGE( WM_UPDATE_START, OnUpdateStart )
	ON_MESSAGE( WM_UPDATE_STEP, OnUpdateStep )
	ON_MESSAGE( WM_UPDATE_END, OnUpdateEnd )
	ON_MESSAGE( WM_LAUNCHER_UPDATE_RESULT, OnLauncherUpdateResult)
		
	ON_WM_DESTROY()
	ON_WM_CLOSE()
	ON_COMMAND(ID_SHOW_LAUNCHER, &CLauncherDlg::OnShowLauncher)
	ON_COMMAND(ID_QUIT_APP, &CLauncherDlg::OnQuitApp)
	ON_COMMAND(IDC_MANUAL_UPDATE, &CLauncherDlg::OnManualUpdate)
	ON_COMMAND(IDC_REPAIR_SETUP, &CLauncherDlg::OnRepairSetup)
	ON_MESSAGE( WM_DEPLOYWEBPAGE_END, OnDownloadWebPageEnd)
	ON_WM_CTLCOLOR()
END_MESSAGE_MAP()

LPCTSTR GetExtension()
{
	static wchar_t buffer[MAX_PATH] = {0};

	CString strTemp = GetBootstrapPath();
	CString strPath = strTemp.Left( strTemp.ReverseFind( L'\\')+1);
	strPath += L"launcher.ini";

	GetPrivateProfileString( L"setting", L"extension", L"mzpch", buffer, MAX_PATH, strPath.GetString());
	return buffer;
}

// ֧�ֵ�2��Extension
LPCTSTR GetExtension2()
{
	static wchar_t buffer[MAX_PATH] = {0};

	CString strTemp = GetBootstrapPath();
	CString strPath = strTemp.Left( strTemp.ReverseFind( L'\\')+1);
	strPath += L"launcher.ini";

	GetPrivateProfileString( L"setting", L"extension2", L"", buffer, MAX_PATH, strPath.GetString());
	return buffer;
}

void CLauncherDlg::OnManualUpdate()
{
	if( !CheckStartupPath() )
	{
		//AfxMessageBox( L"���launcher����,Ȼ�����ִ���ֶ����£�",  MB_OK|MB_ICONERROR );
		//m_trayIcon.ShowBalloonTipInfo( L"���launcher����,Ȼ�����ִ���ֶ����£�" );
		const std::wstring& str = GlobalGetText( TIP_START_FROM_LAUNCHER);
		AfxMessageBox( str.c_str(), MB_OK|MB_ICONERROR );
		m_trayIcon.ShowBalloonTipInfo( str.c_str());
		return;
	}
	
	LPCTSTR pszExtension = GetExtension();
	LPCTSTR pszExtension2 = GetExtension2();
	//wchar_t szFilter[MAX_PATH] = {0};
	//swprintf_s( szFilter, 
	//			MAX_PATH, 
	//			GlobalGetText( TIP_SELECT_UPDATE_PATCH).c_str(), // L"���ɸ��°�(*.%s)|*.%s;*.zip|�����ļ�(*.*)|*.*||", 
	//			pszExtension, 
	//			pszExtension);
	std::wstring strExtension;
	strExtension.assign(pszExtension);

	// ����ǰ�ڹ滮���������ﲹ��ڶ�����չ����֧���Ե÷ǳ����š��������ɡ�
	std::wstring strExtension2;
	strExtension2.assign(pszExtension2);
	if( !strExtension2.empty() )
	{
		strExtension += L";*.";
		strExtension += strExtension2;
	}

	const std::wstring& filter = GlobalFormatText( TIP_SELECT_UPDATE_PATCH, strExtension.c_str(), strExtension.c_str());
	CFileDialog openPatchDlg( TRUE, NULL, 0, 
			OFN_READONLY|OFN_NOCHANGEDIR|OFN_PATHMUSTEXIST|OFN_FILEMUSTEXIST|OFN_EXPLORER|OFN_ENABLESIZING,
			filter.c_str(), this, 0);
	if( openPatchDlg.DoModal() != IDOK )
	{
		return;
	}

	// ����߳���δ��������������
	_TryStartUpdateThread();

	if ( m_pUpdateThread != NULL )
	{
		// �����Ϸ����
		m_btnCheckUpdate.EnableWindow(FALSE);
		m_btnRepairSetup.EnableWindow(FALSE);
		m_btnManualUpdate.EnableWindow(FALSE);
		m_btnLaunchGame.EnableWindow(FALSE);

		// ����������ѡ�����ģʽ
		std::wstring patchpath = openPatchDlg.GetPathName().GetString();
		m_pUpdateThread->SetPatchPath( patchpath );
		m_pUpdateThread->CheckVersion( UPDATE_MODE_PATCH );
	}
	else
	{
		//AfxMessageBox(L"����ʧ�ܣ������߳�û�гɹ������������³���.", MB_OK|MB_ICONERROR );
		AfxMessageBox( GlobalGetText( ERR_START_UPDATE_THREAD).c_str(), MB_OK|MB_ICONERROR);
	}
}

void CLauncherDlg::OnRepairSetup()
{
	CString strApplicationName = GetRepairPath();
	// DWORD dwError = 0;

	STARTUPINFO startupInfo = {0};
	startupInfo.cb = sizeof(STARTUPINFO);
	startupInfo.wShowWindow = SW_SHOW;
	PROCESS_INFORMATION processInfo = {0}; 
	BOOL bRet =  CreateProcess(	strApplicationName,
		NULL, NULL, NULL, FALSE, 0, 0, 0, &startupInfo, &processInfo );
	if( bRet )
	{
		CloseHandle( processInfo.hThread );
		CloseHandle( processInfo.hProcess );
		PostMessage( WM_COMMAND, IDCANCEL, 0 );
	}
	else
	{
		AfxMessageBox(
			//L"�汾��֤ʧ�ܣ�û�гɹ������汾��֤���ߣ������³���.", 
			GlobalGetText( ERR_START_REPAIR).c_str(),
			MB_OK|MB_ICONERROR );
	}
}

LRESULT CLauncherDlg::OnLauncherUpdateResult(WPARAM wParam, LPARAM lParam)
{
	if ( lParam != 0 )
	{
		// ��������Bootstrap����
		CString strApplicationName = GetBootstrapPath();
		STARTUPINFO startupInfo = {0};
		startupInfo.cb = sizeof(STARTUPINFO);

		PROCESS_INFORMATION processInfo = {0}; 

		TCHAR szPath[MAX_PATH * 2 + 1] = {0};
		strApplicationName.Replace( L'/', L'\\');

		PathCanonicalize( szPath, strApplicationName );
		PathQuoteSpaces ( szPath );

		// ָ�������в���
		DWORD dwProcessId = GetCurrentProcessId();
		
		strApplicationName.Format(L" -reboot-launcher -pid %u", dwProcessId );

		LPTSTR lpcmdline = theApp.m_lpCmdLine;	
		// ����launcherʱ,׷�Ӳ�����·��
		// 1.�������û�˫�������������ϷŲ�������patcher���������ַ�ʽ�ṩ��
		// 2.�����û����"�ֶ�����"��ť��ʽ�ṩ�ĸ��°�,������û��-patch�Ͳ�����·������.
		LPTSTR pStr = lpcmdline ? _tcsstr( lpcmdline, _T("-patch ")) : NULL;
		if( pStr )
		{
			// 1.�û�˫�������������ϷŲ�������patcher���������ַ�ʽ�ṩ�Ĳ�����·������
			wcscat_s( szPath, MAX_PATH, strApplicationName);
			wcscat_s( szPath, MAX_PATH, L" ");
			wcscat_s( szPath, MAX_PATH, lpcmdline);
		}
		else
		{
			// 2.����launcherʱ,׷��"�ֶ�����"�ṩ�Ĳ�����·��
			if( wParam == UPDATE_MODE_PATCH )
			{
				if( m_pUpdateThread != NULL )
				{
					const std::wstring& strPath = m_pUpdateThread->GetPatchPath();
					if(!strPath.empty())
					{
						ptrdiff_t offset = (ptrdiff_t)wcslen(szPath);
						swprintf_s(&szPath[offset], MAX_PATH * 2 + 1 - offset, _T("%s -patch \"%s\""),
							strApplicationName.GetString(),strPath.c_str());
					}
				}
			}
			else
			{
				// ���������,����ģʽ����
				BOOL bRebooted = FALSE;
				int server = UPDATE_SERVER_NETCOM;
				int mode = UPDATE_MODE_DEFAULT;
				if( m_pUpdateThread != NULL )
				{
					bRebooted = m_pUpdateThread->GetRebooted();
					if( !bRebooted )
					{
						server = m_pUpdateThread->GetUpdateServerType();
						mode = m_pUpdateThread->GetUpdateMode();
					}
				}

				if( bRebooted )
				{
					strApplicationName.Format(L" -reboot-launcher -pid %u %s", dwProcessId, lpcmdline );
				}
				else
				{
					strApplicationName.Format(L" -reboot-launcher -pid %u -r -server %d -mode %d %s", dwProcessId, server, mode, lpcmdline );
				}

				wcscat_s( szPath, MAX_PATH, strApplicationName);
			}
		}
		
		BOOL bRet =  CreateProcess(NULL, szPath,
			NULL, NULL, FALSE, 0, 0, 0, &startupInfo, &processInfo );
		if ( bRet )
		{
			CloseHandle( processInfo.hThread );
			CloseHandle( processInfo.hProcess );
			PostQuitMessage( 0 );
			return 1;
		}
		else
		{
			DWORD dwError = GetLastError();
			CString strError;
			FormatErrorMessage( dwError, &strError );
			//ReportMessage( _T("��������ʧ��%s:\n%s"), szPath, strError.GetString() );
			ReportMessage( GlobalFormatText( ERR_CREATE_PROCESS, szPath, strError.GetString()).c_str() );

			return 1;
		}
	}

	// �����°汾������
	PostMessage(WM_COMMAND, IDC_CHECK_UPDATE, 0 );

	return 1;
}

LRESULT CLauncherDlg::OnTrayNotify(WPARAM wParam, LPARAM lParam)
{
	// �����¼�֪ͨ
	if ( lParam == WM_MOUSEMOVE )
	{
		if( m_pUpdateThread && m_pUpdateThread->IsBKUpdatingInProgress() &&
			m_pUpdateThread->GetUpdateMode() == UPDATE_MODE_BACKGROUND)
		{
			//m_trayIcon.ShowNormalTipInfo( L"���ڽ��к�̨���¡�����ģ�����Ӱ������ǰ����Ϸ���̣�" );	
			m_trayIcon.ShowNormalTipInfo( GlobalGetText( TIP_BACKGROUND_UPDATING).c_str() );
		}
		else
		{
			m_trayIcon.ShowNormalTipInfo( NULL);	
		}
		return 0;
	}

	return m_trayIcon.OnTrayNotification( wParam, lParam );
}

LRESULT CLauncherDlg::OnGlobalStatus(WPARAM wParam, LPARAM lParam)
{
	Message theMessage;
	if( GlobalGetMessage( (MessageId)lParam, &theMessage ) )
	{
		CString strMessage;
		if( theMessage.dwStatusCode == 0 )
		{
			strMessage.Format( _T("%s"), theMessage.strDetail.c_str()); 
		}
		else
		{
			strMessage.Format( _T("%s(%ld)"), theMessage.strDetail.c_str(), theMessage.dwStatusCode ); 
		}
		
		SetDlgItemText( IDC_GLOBAL_STATUS, strMessage );
	}

	return 0;
}

LRESULT CLauncherDlg::OnCheckStart(WPARAM wParam, LPARAM lParam)
{
	int nStep = wParam > 0 ? (int)wParam : 1;
	int nLower = 0, nUpper = (int)lParam;
	m_downloadPgrsCtrl.SetRange32(nLower, nUpper);
	m_downloadPgrsCtrl.SetStep( nStep );
	m_downloadPgrsCtrl.SetPos(nLower);

	GetDlgItem( IDC_GLOBAL_STATUS)->ShowWindow( SW_SHOWNOACTIVATE );
	return 0;
}

LRESULT CLauncherDlg::OnCheckStep(WPARAM wParam, LPARAM lParam)
{
	m_downloadPgrsCtrl.StepIt();
	return 0;
}

LRESULT CLauncherDlg::OnCheckEnd(WPARAM wParam, LPARAM lParam)
{
	// �򵥵����������
	// Launcher����Patcherʱ,���Patcher�Ѿ���ǰ����,��ʱ���Patcher������°汾����Ϣ
	// ��ʱ�п���CSelectServerDialog::DoModal()������δ����,��͵��±���Ϣ��������.
	// ����� s_bLocked �� ScopedLockey Ϊ�˱�����������������ʵ�ֵ�.
	static bool s_bLocked = false;
	if( s_bLocked )
	{
		return 1;
	}
	
	struct ScopedLockey
	{
		ScopedLockey()
		{
			s_bLocked = true;
		}
		~ScopedLockey()
		{
			s_bLocked = false;
		}
	};
	ScopedLockey s_Lock;

	if( m_pUpdateThread == NULL )
	{
		return 0;
	}

	int nLower, nUpper;
	m_downloadPgrsCtrl.GetRange(nLower, nUpper);
	m_downloadPgrsCtrl.SetPos( nUpper );

	int mode = LOWORD( lParam);
	BOOL bCheckOK = (BOOL)HIWORD( lParam);

	BOOL bNewVersion = (BOOL)( wParam & HASNEWVERSION);
	BOOL bLocalValid = (BOOL)( wParam & LOCALVALID);
	BOOL bAutoPatch = (BOOL)( wParam & AUTOPATCH);
	BOOL bMergeCache = (BOOL)( wParam & MERGECACHE );

	if( bCheckOK ) // ���汾�ɹ�
	{
		if( mode != UPDATE_MODE_BACKGROUND )
		{
			//m_trayIcon.ShowBalloonTipInfo( bNewVersion ? L"���°汾��Ҫ���£�" : 
			//	( bLocalValid ? L"������Ϸ�ͻ����Ѿ������°汾��" : L"���Ŀͻ��˰汾������,ǿ�ҽ�����ִ�а汾��֤��" ) );
			m_trayIcon.ShowBalloonTipInfo( bNewVersion ? GlobalGetText( TIP_HAS_NEW_VERSION).c_str(): 
				( bLocalValid ? GlobalGetText( TIP_ALREADY_NEW_VERSION).c_str() : GlobalGetText( TIP_CLIENT_NOT_INTACT).c_str()) );
		}

		ReNavigageWebPage();

		if( bNewVersion ) // ���°汾
		{
			if( mode == UPDATE_MODE_DEFAULT || mode == UPDATE_MODE_AUTOPATCH )
			{
				if( !CheckStartupPath() )
				{
					//AfxMessageBox( L"���°汾��Ҫ���£�\n���launcher����,Ȼ�����ִ�а汾���£�",  MB_OK|MB_ICONERROR );
					//m_trayIcon.ShowBalloonTipInfo( L"���°汾��Ҫ���£�\n���launcher����,Ȼ�����ִ�а汾���£�" );
					const std::wstring& str = GlobalGetText( TIP_HAS_NEW_VERSION2);
					AfxMessageBox( str.c_str(), MB_OK|MB_ICONERROR);
					m_trayIcon.ShowBalloonTipInfo( str.c_str());
					m_btnCheckUpdate.EnableWindow();
					m_btnManualUpdate.EnableWindow();
					m_btnRepairSetup.EnableWindow();
					return 0;
				}

				FlashWindowEx( FLASHW_ALL|FLASHW_TIMERNOFG, 3, 0);

				
				// ������θ���Ϊ��������֮��ĸ��£����·������͸���ģʽ�Ѿ���������ָ����
				// ���������ٴ�Ϊ�û����Ի���ָ����
				if( !m_pUpdateThread->GetRebooted() )
				{
					INT_PTR res = IDCANCEL;
					//if( bAutoPatch )
					//{
					//	// ���ò������Զ����µı�־����
					//	res = AfxMessageBox(
					//		//L"��ǰ�ͻ��˰汾��Ҫ���ش�����������ɸ��¡�\n"
					//		//L"\n"
					//		//L"���\"��\"�����ǵĸ��³��򽫳����Զ���ɴ˴θ��¡�\n"
					//		//L"���\"��\"�����³�����Ϊ���Զ����ֶ�������ҳ�档\n"
					//		//L"���\"ȡ��\"���������Լ�ѡ�񲹶������и��£�\n"
					//		//L"\n"
					//		//L"�Զ�������ɺ�����Ծɲ������°汾��������ʾ��������¡�\n"
					//		//L"�Զ�������Ҫʱ��ϳ��������ĵȴ���",
					//		GlobalGetText( QST_AUTO_PATH_UPDATE).c_str(),
					//		MB_YESNOCANCEL|MB_ICONQUESTION);
					//}
					//else
					{
						const PFSX::CVersionInfo& rVersionInfo = m_pUpdateThread->GetServerVersion();

						//CSelectServerDialog dlg(rVersionInfo);
						SetDownloadSvrDlg dlg(rVersionInfo);
						// ��ʾ�ϴ�ѡ��ķ�����
						dlg.SetSelectServerType( CProfile::GetInstance().GetIniFileServerType());

						int dt = CProfile::GetInstance().GetFileDownloadType();
						dlg.SetP2P(dt != 0);
						if(dt != 0)
						{
							dlg.SetLowerLimit(CProfile::GetInstance().GetDownloadSpeed());
							dlg.SetUpperLimit(CProfile::GetInstance().GetUploadSpeed());
						}

						res = dlg.DoModal();
						if( IDOK == res )
						{
							CProfile::GetInstance().SetServerType( dlg.GetSelectServerType());
							CProfile::GetInstance().SetFileDownloadType( dlg.UseP2P() ? 1 : 0);
							CProfile::GetInstance().SetDownloadSpeed( dlg.GetLowerLimit());
							CProfile::GetInstance().SetUploadSpeed( dlg.GetUpperLimit());
							m_pUpdateThread->SetUpdateServerType( (UPDATE_SERVER_TYPE)dlg.GetSelectServerType() );
						}
					}
					
					if( IDYES == res || IDOK == res )
					{
						//if( bAutoPatch )
						{
							// ��ȱʡģʽ�л����������Զ�����ģʽ
							mode = UPDATE_MODE_AUTOPATCH;
							//m_trayIcon.ShowBalloonTipInfo( L"����ʹ�ò������Զ����¿ͻ���..." );
							m_trayIcon.ShowBalloonTipInfo( GlobalGetText( TIP_PATCH_UPDATING).c_str());
						}
						//else
						//{
						//	// ȷ����ʹʹ��UPDATE_MODE_AUTOPATCHģʽ�ĸ��¹�����CheckNewVersion,
						//	// ��ʱҲҪ�л���ȱʡ���¹�����
						//	mode = UPDATE_MODE_DEFAULT;
						//	//m_trayIcon.ShowBalloonTipInfo( L"�����Զ����¿ͻ���..." );
						//	m_trayIcon.ShowBalloonTipInfo( GlobalGetText( TIP_DEFAULT_UPDATING).c_str());
						//}

						m_pUpdateThread->Update( (UPDATE_MODE)mode );
					}
					else
					{
						// �û�ȡ����ѡ����������ݲ�����
						m_btnCheckUpdate.EnableWindow();
						m_btnManualUpdate.EnableWindow();
						m_btnRepairSetup.EnableWindow();
						
						// �����Ѿ���ȷ�Ѿ������°汾Ҫ����,���Բ�����"��ʼ��Ϸ��ť"
						// m_btnLaunchGame.EnableWindow();
						if( IDNO == res && bAutoPatch )
						{
							// �Զ�Ϊ�û��򿪲���������ҳ��
							CString strDownloadUrl = GetOnlineDownloadPageUrl();
							if( !strDownloadUrl.IsEmpty() )
							{
								SHELLEXECUTEINFO seInfo = {0};
								seInfo.cbSize = sizeof(SHELLEXECUTEINFO);
								seInfo.lpVerb = _T("open");
								seInfo.lpFile = strDownloadUrl.GetString();
								ShellExecuteExW(&seInfo);
							}
						}
					}
				}
			}
		}
		else // û���°汾
		{
			if( mode == UPDATE_MODE_BACKGROUND ) 
			{
				upd::SetLastError(0);// Clear the last error.
				SetDlgItemText( IDC_GLOBAL_STATUS, L"");
				GetDlgItem( IDC_GLOBAL_STATUS)->ShowWindow( SW_HIDE );
			}
			else
			{
				// ����û���°汾, ���ͻ��˰汾��Ч, ��Ҫ�޸��ͻ���
				if( !bLocalValid )
				{
					if( m_pUpdateThread->GetRebooted() )
					{
						return 0;
					}
					int nChoice = AfxMessageBox(
						//L"��ǰ�ͻ��˴������⣡�Ƿ��޸��ͻ��ˣ�", 
						GlobalGetText( QST_REPAIR).c_str(),
						MB_YESNO|MB_ICONQUESTION);
					if( nChoice == IDYES )
					{
						//m_trayIcon.ShowBalloonTipInfo( L"�����޸��ͻ���..." );
						m_trayIcon.ShowBalloonTipInfo( GlobalGetText( TIP_REPAIR_UPDATING).c_str());
						
						if( mode == UPDATE_MODE_PATCH )
						{
							// �޸�һ��patchģʽΪrepairģʽ
							mode = UPDATE_MODE_REPAIR;
						}

						// �Ե�ǰģʽ�޸��ͻ���
						m_pUpdateThread->Update((UPDATE_MODE) mode );
						return 0;
					}
				}
				else
				{
					if( mode == UPDATE_MODE_PATCH || mode == UPDATE_MODE_REPAIR )
					{
						int nChoice = AfxMessageBox(
							//L"��ǰ�ͻ����Ѿ������°汾���Ƿ�ʹ�øò�����ǿ���޸��ͻ��ˣ�", 
							GlobalGetText( QST_ENFORCE_REPAIR).c_str(), 
							MB_YESNO|MB_ICONQUESTION);
						if( nChoice == IDYES )
						{
							//m_trayIcon.ShowBalloonTipInfo( L"����ʹ�ò�����ǿ���޸��ͻ���..." );
							m_trayIcon.ShowBalloonTipInfo( GlobalGetText( TIP_ENFORCE_REPAIRING).c_str());
							// ǿ���޸��ͻ���
							m_pUpdateThread->Update( UPDATE_MODE_REPAIR );
							return 0;
						}
					}
				}
			}

			m_btnCheckUpdate.EnableWindow();
			m_btnManualUpdate.EnableWindow();
			m_btnRepairSetup.EnableWindow();
			if( mode != UPDATE_MODE_BACKGROUND ) 
			{
				m_btnLaunchGame.EnableWindow();
			}
		}
	}
	else // ���汾ʧ��
	{
		const upd::ErrorInfo& rErrorInfo = upd::GetLastError();
		CString strMessage;
		if( mode == UPDATE_MODE_BACKGROUND )
		{
			strMessage = GlobalFormatText( ERR_BACKGROUND_UPDATE, rErrorInfo.ErrorDetail.c_str(), 
				rErrorInfo.ErrorCode).c_str();
			// strMessage.Format( L"��̨����: %s(%ld)",  );
		}
		else if ( mode == UPDATE_MODE_PATCH )
		{
			//strMessage.Format( L"ʹ�ò���������ʧ�ܣ�\n%s(%ld)", 
			strMessage = GlobalFormatText( ERR_PATCH_UPDATE, rErrorInfo.ErrorDetail.c_str(), 
				rErrorInfo.ErrorCode).c_str();
		}
		else
		{
			//strMessage.Format( L"�޷���֪�Ƿ����°汾��Ҫ���£�\n%s(%ld)", 
			strMessage = GlobalFormatText( ERR_CANNOT_GET_NEW_VERSION, rErrorInfo.ErrorDetail.c_str(), 
				rErrorInfo.ErrorCode ).c_str();
		}

		if( mode == UPDATE_MODE_BACKGROUND )
		{
			KHMsgLog_Error( _T("%s\n"), strMessage.GetString() );

			// ���ڷ�����û���°汾��Ҫ���µ���������������
			if( rErrorInfo.ErrorCode != ERROR_INTERNET_ITEM_NOT_FOUND && 
				rErrorInfo.ErrorCode != ERROR_INTERNET_CANNOT_CONNECT )
			{
				// m_trayIcon.ShowBalloonTipInfo( strMessage );
				SetDlgItemText( IDC_GLOBAL_STATUS, strMessage );
			}
			else
			{
				upd::SetLastError(0);// Clear the last error.
				SetDlgItemText( IDC_GLOBAL_STATUS, L"");
				GetDlgItem( IDC_GLOBAL_STATUS)->ShowWindow( SW_HIDE );
			}
		}
		else
		{
			upd::SetLastError(0);// Clear the last error.
			SetDlgItemText( IDC_GLOBAL_STATUS, L"");
			GetDlgItem( IDC_GLOBAL_STATUS)->ShowWindow( SW_HIDE );
			ReportMessage( strMessage ); 
		}

		m_btnCheckUpdate.EnableWindow();
		m_btnManualUpdate.EnableWindow();
		m_btnRepairSetup.EnableWindow();

		// ��ʹ���汾ʧ�ܣ�Ҳ�����û�������Ϸ�ͻ��ˡ�
		if ( lParam != UPDATE_MODE_BACKGROUND )
		{
			m_btnLaunchGame.EnableWindow();
		}

		// ����ǲ��������·�ʽ����ɺ����������
		if( lParam == UPDATE_MODE_PATCH)
		{
			if( theApp.m_lpCmdLine)
			{
				// ɾ��-patch �������, ��ʽ����:
				// -patch "......"
				LPTSTR pStrStart = _tcsstr( theApp.m_lpCmdLine, _T("-patch \""));
				if( pStrStart ) 
				{
					LPTSTR pStrEnd = _tcsstr( pStrStart + wcslen(_T("-patch \"")), _T("\""));
					if ( pStrEnd )
					{
						wcscpy_s( pStrStart, -1, pStrEnd+1 );
					}
				}
			}
		}
	}
	
	return 0;
}

LRESULT CLauncherDlg::OnProgressStart(WPARAM wParam, LPARAM lParam)
{
	int nStep = wParam > 0 ? (int)wParam : 1;
	int nLower = 0, nUpper = (int)lParam;
	m_downloadPgrsCtrl.SetRange32(nLower, nUpper);
	m_downloadPgrsCtrl.SetStep( nStep );
	m_downloadPgrsCtrl.SetPos(nLower);

	GetDlgItem( IDC_STEP_MESSAGE)->ShowWindow( SW_SHOWNOACTIVATE );
	return 0;
}

LRESULT CLauncherDlg::OnProgressStep(WPARAM wParam, LPARAM lParam)
{
	if( wParam)
	{
		m_downloadPgrsCtrl.SetPos( (int)wParam);
	}
	else
	{
		m_downloadPgrsCtrl.StepIt();
	}

	Message theMessage;

	if( GlobalGetMessage( (MessageId)lParam, &theMessage ) )
	{
		if( theMessage.dwStatusCode == 0 )
		{
			SetDlgItemText( IDC_STEP_MESSAGE, theMessage.strDetail.c_str());
		}
		else
		{
			CString strMessage;
			strMessage.Format( _T("%s(%ld)"), theMessage.strDetail.c_str(), 
				theMessage.dwStatusCode ); 
			SetDlgItemText( IDC_STEP_MESSAGE, strMessage );
		}
	}

	return 0;
}

LRESULT CLauncherDlg::OnProgressEnd(WPARAM wParam, LPARAM lParam)
{
	int nLower, nUpper;
	m_downloadPgrsCtrl.GetRange(nLower, nUpper);
	m_downloadPgrsCtrl.SetPos( nUpper );

	GetDlgItem( IDC_STEP_MESSAGE)->ShowWindow( SW_HIDE );
	SetDlgItemText( IDC_STEP_MESSAGE, L"" );

	return 0;
}

LRESULT CLauncherDlg::OnUpdateStart(WPARAM wParam, LPARAM lParam)
{
	int nStep = wParam > 0 ? (int)wParam : 1;
	int nLower = 0, nUpper = (int)lParam;
	m_updatePgrsCtrl.SetRange32(nLower, nUpper);
	m_updatePgrsCtrl.SetStep( nStep );
	m_updatePgrsCtrl.SetPos( nLower );

	GetDlgItem( IDC_GLOBAL_STATUS)->ShowWindow( SW_SHOWNOACTIVATE );

	return 0;
}

LRESULT CLauncherDlg::OnUpdateStep(WPARAM wParam, LPARAM lParam)
{
	do
	{
		m_updatePgrsCtrl.StepIt();
	} while( -- lParam > 0 );

	return 0;
}

LRESULT CLauncherDlg::OnUpdateEnd(WPARAM wParam, LPARAM lParam)
{
	int nLower, nUpper;
	m_updatePgrsCtrl.GetRange(nLower, nUpper);
	m_updatePgrsCtrl.SetPos( nUpper );

	m_btnCheckUpdate.EnableWindow();
	m_btnManualUpdate.EnableWindow();
	m_btnRepairSetup.EnableWindow();

	if ( lParam != UPDATE_MODE_BACKGROUND )
	{
		// ��̨������ϣ����ı䡰������Ϸ����ť��״̬
		m_btnLaunchGame.EnableWindow();
	}

	if ( wParam )
	{
		if ( lParam == UPDATE_MODE_BACKGROUND )
		{
			KHMsgLog_Info( L"��̨������ϡ�\n" );
			//SetDlgItemText( IDC_GLOBAL_STATUS,  L"��̨������ϡ�" );
			SetDlgItemText( IDC_GLOBAL_STATUS, GlobalGetText( TIP_BACKGROUND_UPDATE_COMPLETE).c_str());
		}
		else
		{
			m_trayIcon.ShowBalloonTipInfo( GlobalGetText( TIP_UPDATE_SUCCESS).c_str() );
			//SetDlgItemText( IDC_GLOBAL_STATUS,  L"���³ɹ�!" );
			SetDlgItemText( IDC_GLOBAL_STATUS, GlobalGetText( TIP_UPDATE_SUCCESS).c_str());
		}

		// ˢ��Webҳ
		ReNavigageWebPage();
	}
	else
	{
		const upd::ErrorInfo& rErrorInfo = upd::GetLastError();
		if( rErrorInfo.ErrorCode != SC_USER_CANCELED )
		{
			if ( lParam == UPDATE_MODE_BACKGROUND )
			{
				CString strMessage = GlobalFormatText( ERR_BACKGROUND_UPDATE,	// L"��̨����ʧ�ܣ�%s(%ld)", 
					rErrorInfo.ErrorDetail.c_str(), rErrorInfo.ErrorCode ).c_str();
				// ��̨���²������Ի���
				// m_trayIcon.ShowBalloonTipError(strMessage);

				// ��¼��־����ʾ�ڶԻ������Ϣ����
				KHMsgLog_Error(  L"%s\n", strMessage.GetString() );
				SetDlgItemText( IDC_GLOBAL_STATUS, strMessage );
			}
			else
			{
				// L"����ʧ�ܣ�\n%s(%ld)", 

				ReportMessage( GlobalFormatText( ERR_UPDATE, rErrorInfo.ErrorDetail.c_str(), 
					rErrorInfo.ErrorCode).c_str() );
			}
		}
		else
		{
			KHMsgLog_Info( L"�û���;ȡ���˸��¡�\n" );
		}
	}

	// ����ǲ��������·�ʽ����ɺ����������
	if( lParam == UPDATE_MODE_PATCH && !CUpdateThread::IsNeedReboot())
	{
		if( theApp.m_lpCmdLine)
		{
			theApp.m_lpCmdLine[0] = 0;
		}
	}

	return 0;
}

// CLauncherDlg ��Ϣ�������
int CLauncherDlg::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CDialog::OnCreate(lpCreateStruct) == -1)
		return -1;

	EnableToolTips();

	//////////////////////////////////////////////////////////////////////////
	//
	// ����������ؼ�
	//
	CRect rcBrowser(0,0,1,1);
	m_htmlView = (CWebBrowserCtrl*)RUNTIME_CLASS(CWebBrowserCtrl)->CreateObject();
	if( !m_htmlView->Create( NULL, NULL, WS_CHILD | WS_VISIBLE, rcBrowser, this, NULL ) )
	{
		//ReportMessage(L"����HtmlView�ؼ�ʧ��!");
		ReportMessage( GlobalGetText( ERR_CREATE_HTMLVIEW).c_str());
		return -1;
	}

	//////////////////////////////////////////////////////////////////////////
	// ��Ϊ���ڱ���û�б�������������Ҫ�ֶ����ó�����⣬
	// ������ʾ���������С�_T("���ɸ��³���")
	//
	// Ϊ��֧�ֹ��ʻ���֧�ֶ����Ʒ���������ȴ������ļ��ж�ȡ���ڱ���.
	// ע�⣬������ֱ��뱣֤��launcher.xml�е���ͬID���ַ�����ͬ��
	const std::wstring& strCaptionPatcher = GlobalGetText(TOK_PRODUCT_PATCHER);
	if( !strCaptionPatcher.empty() )
	{
		SetWindowText(strCaptionPatcher.c_str());
	}
	else
	{
		CString strCaption(MAKEINTRESOURCE(IDR_MAINFRAME));
		SetWindowText(strCaption);
	}

	if( !m_layoutManager.Load( GetLauncherUIResourceDir(), GetLauncherUILayoutFile(), L"Launcher" ) )
	{
		//ReportMessage(L"����Launcher��Դ��ʧ��!");
		ReportMessage( GlobalGetText( ERR_LOAD_UIRESOURCE).c_str());
		return -1;
	}

	//
	// ���ÿؼ�����崰�ڣ�ͬʱָ��UI��Դ����Ŀ¼�Ͳ��������ļ���
	//
	if( !m_layoutManager.SetPanel( this ) )
	{
		//ReportMessage(L"����Launcher��Դ��ʧ��!");
		ReportMessage( GlobalGetText( ERR_LOAD_UIRESOURCE).c_str());
		return -1;
	}
	
	// ��ȡ�����ļ������õ�GlobalStatus��StepMessage 2����̬�ı��ؼ���ǰ���ı�ɫ
	if( !m_layoutManager.GetControlColor(_T("GlobalStatus"), m_globalStatusControlColor ) )
	{
		m_globalStatusControlColor = RGB(245,10,40); // ȱʡ��ɫֵ 
	}
	if( !m_layoutManager.GetControlColor( _T("StepMessage"), m_stepMessageControlColor ) )
	{
		m_stepMessageControlColor = RGB(10,40,245); // ȱʡ��ɫֵ 
	}
	
	// ����GlobalStatus��StepMessage 2����̬�ı��ؼ��ı���ˢ
	COLORREF clrBKBrush;
	if ( !m_layoutManager.GetControlBkColor(_T("GlobalStatus"), clrBKBrush) )
	{
		clrBKBrush = RGB(208,202,224);// ȱʡ������ɫ 
	}
	m_hBKBrush = CreateSolidBrush(clrBKBrush);

	return 0;
}

BOOL CLauncherDlg::_TryStartUpdateThread()
{
	if( m_pUpdateThread != NULL )
	{
		return TRUE;
	}

	KHMsgLogW( L"�������������߳�...\n");
	CWinThread *pThread = AfxBeginThread( RUNTIME_CLASS(CUpdateThread),
		THREAD_PRIORITY_NORMAL, 0, CREATE_SUSPENDED, NULL );
	KHMsgLog_DebugW( L"���������߳�:pThread=(%p)��\n", pThread );
	if( pThread != NULL )
	{
		m_pUpdateThread = (CUpdateThread*)pThread;
		m_pUpdateThread->m_bAutoDelete = FALSE;
		m_pUpdateThread->ResumeThread();
		KHMsgLogW( L"�����߳�������ϡ�\n");
	}
	else
	{
		DWORD dwLastError = GetLastError();
		KHMsgLog_DebugW( L"�޷���������(%lu)��\n", dwLastError );
		//ReportMessage( _T("�޷���������(%lu)!"), dwLastError );
		ReportMessage( GlobalFormatText( ERR_START_UPDATE, dwLastError).c_str());
	}
	return pThread != NULL;
}

// ��ֹ������ռ�Ĺ�����
void CLauncherDlg::ForbidWebBrowseScrollBar()
{
	IHTMLDocument2 *phtmldoc2 = NULL; 
	IHTMLElement *phtmlElement = NULL; 
	
	IDispatch *pdisp = m_htmlView->GetHtmlDocument(); 
	if (pdisp != NULL) 
	{ 
		pdisp->QueryInterface( IID_IHTMLDocument2, (void**)&phtmldoc2); 
		pdisp->Release(); 
	} 
	if (phtmldoc2 != NULL) 
	{ 
		HRESULT res = phtmldoc2->get_body( &phtmlElement); 
		phtmldoc2->Release(); 
	} 
	if (phtmlElement != NULL) 
	{ 
		IHTMLBodyElement *phtmlbody = NULL; 
		phtmlElement->QueryInterface(IID_IHTMLBodyElement, (void**)&phtmlbody); 
		if (phtmlbody != NULL) 
		{ 
			phtmlbody->put_scroll(L"no"); 
			phtmlbody->Release(); 
		} 
		phtmlElement->Release(); 
	} 
}

BOOL CLauncherDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// ����ͨ�������ļ�ȷ�����֡�
	const std::wstring& strCaptionPatcher = GlobalGetText(TOK_PRODUCT_PATCHER);
	if( !strCaptionPatcher.empty() )
	{
		m_hIconLarge = m_layoutManager.LoadIcon(strCaptionPatcher.c_str(), TRUE);
		m_hIconSmall = m_layoutManager.LoadIcon(strCaptionPatcher.c_str(), FALSE);
	}
	
	if ( m_hIconLarge == NULL || m_hIconSmall == NULL )
	{
		CString strAppName(MAKEINTRESOURCE(IDR_MAINFRAME));
		m_hIconLarge = m_layoutManager.LoadIcon(strAppName, TRUE);
		m_hIconSmall = m_layoutManager.LoadIcon(strAppName, FALSE);
	}

	// ���ô˶Ի����ͼ�ꡣ��Ӧ�ó��������ڲ��ǶԻ���ʱ����ܽ��Զ�
	//  ִ�д˲���
	if ( m_hIconLarge != NULL && m_hIconSmall != NULL )
	{
		SetIcon(m_hIconLarge, TRUE);			// ���ô�ͼ��
		SetIcon(m_hIconSmall, FALSE);		// ����Сͼ��
		m_hIcon = m_hIconLarge;
	}
	else
	{
		// Use the default one, which need not to be destroyed
		m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);

		if( m_hIconLarge != NULL )
			SetIcon(m_hIconLarge, TRUE);
		else
			SetIcon(m_hIcon, TRUE);

		if ( m_hIconSmall != NULL )
			SetIcon(m_hIconSmall, FALSE);
		else
			SetIcon(m_hIcon, FALSE);
	}
	
	// 
	// WebBrowser��λ����ʼ��
	//
	CRect rcBrowser;
	if( !m_layoutManager.GetControlRect( _T("WebControl"), rcBrowser ) )
	{
		// ��ʷԭ��������ֲ���Ҫ���룬��������ҳ��������ģ����á�WebControl����
		// ���Ƕ�����δ�޸ĵ���Դ������һ�Ρ���ҳ�򡱡�
		if( !m_layoutManager.GetControlRect( _T("��ҳ��"), rcBrowser ) )
		{
			HWND hStatic = ::GetDlgItem( GetSafeHwnd(), IDC_WEBBROWSER );
			::GetWindowRect( hStatic , &rcBrowser );
			ScreenToClient( &rcBrowser );
		}
	}
	
	m_htmlView->MoveWindow( &rcBrowser, TRUE );
	m_htmlView->Navigate2( GetLauncherWebPageUrl() );
	ForbidWebBrowseScrollBar();

	_TryStartDownloadWebPageThread();

	m_globalStatus.SubclassDlgItem(IDC_GLOBAL_STATUS, this );
	m_stepMessage.SubclassDlgItem(IDC_STEP_MESSAGE, this );

	//
	// ΪGLOBAL_STATUS��̬��λ
	//
	CRect rcGlobalStatus, rcStepMessage;
	if( m_layoutManager.GetControlRect(_T("GlobalStatus"), rcGlobalStatus) )
	{
		m_globalStatus.MoveWindow(rcGlobalStatus, FALSE);
	}

	//
	// ΪSTEP_MESSAGE��̬��λ
	//
	if( m_layoutManager.GetControlRect(_T("StepMessage"), rcStepMessage) )
	{
		m_stepMessage.MoveWindow(rcStepMessage, FALSE);
	}

	//
	// �������ؼ���ʼ��
	//

	// ��ʾ�ٷֱȵ��������ȳ�ʼ����������ApplyProgressCtrl���п���ͨ�������޸����������
	m_updatePgrsCtrl.SetShowPercent( TRUE ); 	
	VERIFY( m_layoutManager.ApplyProgressCtrl( IDC_DOWNLOAD_PROGRESS, m_downloadPgrsCtrl ) );
	VERIFY( m_layoutManager.ApplyProgressCtrl( IDC_UPDATE_PROGRESS, m_updatePgrsCtrl ) );

	m_downloadPgrsCtrl.SetRange( 0, 100 );
	m_downloadPgrsCtrl.SetStep( 1 );
	m_updatePgrsCtrl.SetRange( 0, 100 );
	m_updatePgrsCtrl.SetStep( 20 );

	//
	// ��ť�ؼ���ʼ��
	//
	m_layoutManager.ApplyButton(IDC_CHECK_UPDATE, m_btnCheckUpdate );
	m_layoutManager.ApplyButton(IDC_MANUAL_UPDATE, m_btnManualUpdate );
	m_layoutManager.ApplyButton(IDC_REPAIR_SETUP, m_btnRepairSetup );
	m_layoutManager.ApplyButton(IDC_LAUNCH_GAME, m_btnLaunchGame );
	m_layoutManager.ApplyButton(IDMINIMIZE, m_btnMinimize );
	m_layoutManager.ApplyButton(IDCANCEL, m_btnClose );

	// Button ��ʼ״̬
	m_btnLaunchGame.SetFocus();

	// ����ͼ���ʼ��
	m_trayIcon.SetNotificationWnd( GetSafeHwnd(), WM_TRAY_NOTIFY, m_hIconSmall, strCaptionPatcher.c_str() );

	CWndWatcher* _pThisWatcher = new CWndWatcher();
	_pThisWatcher->SetHwnd( GetSafeHwnd() );
	GlobalRegister( _pThisWatcher );
	m_pThisWatcher = _pThisWatcher;

	// �����Ϸ����
	m_btnCheckUpdate.EnableWindow(FALSE);
	m_btnManualUpdate.EnableWindow(FALSE);
	m_btnRepairSetup.EnableWindow(FALSE);
	m_btnLaunchGame.EnableWindow(FALSE);

	// ���������ļ�·��
	CString strTemp = GetBootstrapPath();
	CString strPath = strTemp.Left( strTemp.ReverseFind( L'\\')+1);
	strPath += L"patcher.ini";
	CProfile::GetInstance().SetFileName( strPath.GetString());

	// �����°汾������
	OnBnClickedCheckUpdate();
	
	int tryCount = 0;
	while (tryCount++ < 10)
	{
		if(StartEngine())
			break;

		Sleep(100);
	}

	return FALSE;  // ���ǽ��������õ��ؼ������򷵻� TRUE
}

// �����Ի��������С����ť������Ҫ����Ĵ���
//  �����Ƹ�ͼ�ꡣ����ʹ���ĵ�/��ͼģ�͵� MFC Ӧ�ó���
//  �⽫�ɿ���Զ���ɡ�
void CLauncherDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // ���ڻ��Ƶ��豸������

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// ʹͼ���ڹ��������о���
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// ����ͼ��
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CPaintDC dc(this);
		if (!m_layoutManager.OnPanelPaint(&dc) )
		{
			CDialog::OnPaint();
		}
	}
}

//���û��϶���С������ʱϵͳ���ô˺���ȡ�ù����ʾ��
//
HCURSOR CLauncherDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

void CLauncherDlg::OnBnClickedCheckUpdate()
{
	// ����߳���δ��������������
	_TryStartUpdateThread();

	if ( m_pUpdateThread != NULL )
	{
		// �����Ϸ����
		m_btnCheckUpdate.EnableWindow(FALSE);
		m_btnRepairSetup.EnableWindow(FALSE);
		m_btnManualUpdate.EnableWindow(FALSE);
		m_btnLaunchGame.EnableWindow(FALSE);

		// ����������ѡ�����ģʽ
		UPDATE_MODE mode = UPDATE_MODE_AUTOPATCH; // UPDATE_MODE_DEFAULT;
		int server = -1;
		TCHAR* lpcmdline = theApp.m_lpCmdLine;
		lpcmdline = _tcsstr( lpcmdline, L"-patch ");
		if( lpcmdline)
		{
			std::wstring patchpath = lpcmdline + 7;
			// ȥ��ͷβ�� "" ��
			patchpath.erase( 0, patchpath.find_first_not_of( L'\"'));
			patchpath.erase( patchpath.find_last_not_of( L'\"')+1);
			if( !patchpath.empty())
			{
				mode = UPDATE_MODE_PATCH;
				m_pUpdateThread->SetPatchPath( patchpath);
			}
		}
		else
		{
			// ����-r -server -mode ����
			lpcmdline = _tcsstr( theApp.m_lpCmdLine, L"-r "); // -r ��ʾ����Ϊ��������
			if ( lpcmdline )
			{
				// ����������־
				m_pUpdateThread->SetRebooted(TRUE);

				// -server : ����ǰָ����server
				// -mode : ����ǰָ����mode
				TCHAR* lpserver = _tcsstr( lpcmdline, L"-server ");
				TCHAR* lpserverEnd = lpcmdline;
				if( lpserver != NULL ) 
					server = wcstol( lpserver + wcslen(L"-server "), &lpserverEnd, 10 );
				TCHAR* lpmode = _tcsstr( lpserverEnd, L"-mode ");
				if( lpmode != NULL ) 
				{
					TCHAR* lpmodeEnd = NULL;
					mode = (UPDATE_MODE)wcstol( lpmode + wcslen(L"-mode "), &lpmodeEnd, 10 );
				}
			}
		}

		if( server == -1)
		{
			server = CProfile::GetInstance().GetServerType();
		}
		m_pUpdateThread->SetUpdateServerType( (UPDATE_SERVER_TYPE)server);
		m_pUpdateThread->CheckVersion( mode );
	}
}

#pragma optimize("",off) // �������Ż����µ��� MyCreateProcess() ���µ�����(������Win2000��)

void CLauncherDlg::OnBnClickedLaunchGame()
{
	VMProtectBeginVirtualization
		
	// ������Ϸ�ͻ��˳���
	CString strApplicationName = GetGameClientPath();
	DWORD dwError = 0;

	// �ͻ�������ʵ������У��(��ʱ��������3��)
	// У��ͬ���Ľ�������,���ɿ��ļ�ⷽ��,��ʱֻ�����
	CString strProcessFileName = strApplicationName;
	PathStripPath(strProcessFileName.GetBuffer() );
	strProcessFileName.ReleaseBuffer();
	if( strProcessFileName.GetLength() > 0 && 
		!MultiInstanceCheck( strProcessFileName.GetString() ) )
	{
		const std::wstring& strProductName = GlobalGetText(TOK_PRODUCT_CLIENT);
		if( !strProductName.empty() )
		{
			KHMsgLog_ErrorW( L"ͬʱ�򿪵�%s�Ѵﵽ����(%s)��\n", strProductName.c_str(), strApplicationName.GetString() );
		}
		else
		{
			KHMsgLog_ErrorW( L"ͬʱ�򿪵Ŀͻ��˳����Ѵﵽ����(%s)��\n", strApplicationName.GetString() );
		}

		ReportMessage( GlobalFormatText( ERR_CLIENT_MAX_COUNT, strApplicationName.GetString()).c_str());

		return;
	}

//#if defined _DEBUG && defined NO_DEBUG_PP
//	STARTUPINFO startupInfo = {0};
//	startupInfo.cb = sizeof(STARTUPINFO);
//	startupInfo.wShowWindow = SW_SHOW;
//	PROCESS_INFORMATION processInfo = {0}; 
//	BOOL bRet =  CreateProcess(	strApplicationName,
//		NULL, NULL, NULL, FALSE, 0, 0, 0, &startupInfo, &processInfo );
//	if( bRet )
//	{
//		CloseHandle( processInfo.hThread );
//		CloseHandle( processInfo.hProcess );
//#else
//	CString strCommandLine = GetGameClientPath();
//	CString strCurrentDir = GetGameClientDir();
//	CString strPPDirectory = GetPerfectProtectorDir();
//	CString strTitleName;
//	strTitleName.LoadString( IDR_MAINFRAME );
//	CString strShowText;
//	
//	//DWORD dwRet = MyCreateProcess(
//	//	strCommandLine.GetBufferSetLength(MAX_PATH),
//	//	strCurrentDir, &dwError);
//
//	DWORD dwRet = MyCreateProcess(
//		strCommandLine.GetBufferSetLength(MAX_PATH),
//		strCurrentDir, 
//		strPPDirectory,
//		strTitleName,
//		strShowText);
//	strCommandLine.ReleaseBuffer();
//
//	if ( dwRet == 0 )
//	{
//#endif
		// ����߳���δ��������������
		_TryStartUpdateThread();
		if( m_pUpdateThread != NULL )
		{
			// ��̨�����߳����ڽ��й�����
			// �������ֶ�����°汾,�������ֶ�����,������汾��֤
			m_btnCheckUpdate.EnableWindow(FALSE);
			m_btnManualUpdate.EnableWindow(FALSE);
			m_btnRepairSetup.EnableWindow(FALSE); 
			
			KHMsgLog_DebugW( L"������̨����...\n");
			m_pUpdateThread->CheckVersion( UPDATE_MODE_BACKGROUND );
		}
		else
		{
			KHMsgLog_ErrorW( L"������̨����ʧ��(��Ӧ���߳�û�д����ɹ�)��\n");
		}

		// ��С�����ڵ�ϵͳ����λ��
		//OnBnClickedMinimize();
		//KHMsgLog_DebugW( L"��С�����ڵ�ϵͳ���̡�\n");
	//}
	//else
	//{
	//	KHMsgLog_Error(_T("MyCreateProcess(%s) failed(Error:%d)��\n"), 
	//		strApplicationName.GetString(), dwRet );
	//	//ReportMessage( _T("�����ͻ���ʧ��(%u)��\n\n\t%s\n\n�볢�ԶԿͻ��˽��а汾��֤�����°�װ�ͻ��˳���"),
	//	ReportMessage( GlobalFormatText( ERR_START_CLIENT2, 
	//			dwRet, strApplicationName.GetString()).c_str() );
	//}

	VMProtectEnd
}

#pragma optimize("",on)

void CLauncherDlg::OnBnClickedMinimize()
{
	// ��С���Ի��򴰿�
	//ShowWindow(SW_MINIMIZE);
	// ��С���Ի��򴰿ڵ�������
	ShowWindow(SW_HIDE);
}

LRESULT CLauncherDlg::OnNcHitTest(CPoint point)
{
	LRESULT nRet = CDialog::OnNcHitTest(point);
	if(nRet == HTCLIENT)
	{
		// ���������д���ͻ������ƶ�����
		nRet = HTCAPTION;
	}
	return nRet;
}

BOOL CLauncherDlg::OnToolTipNotify( UINT id, NMHDR * pNMHDR, LRESULT * pResult )
{
	TOOLTIPTEXT *pTTT = (TOOLTIPTEXT *)pNMHDR;
	UINT_PTR nID = pNMHDR->idFrom;

	if ((pTTT->uFlags & TTF_IDISHWND) == 0 )
	{
		return FALSE;
	}

	// idFrom is actually the HWND of the tool
	int iID = ::GetDlgCtrlID((HWND)nID);
	if( iID == 0 )
	{
		return FALSE;
	}

	// ���⴦��CDIYButton�����ʾ��Ϣ�����ǵ�������ʾ������XML���塣
	CWnd *pChild = CWnd::FromHandlePermanent((HWND)nID);
	if( pChild != NULL )
	{
		if( pChild->IsKindOf(RUNTIME_CLASS(CDIYButton)))
		{
			// �Ի水ť
			LPCTSTR lpszTipInfo = ((CDIYButton*)pChild)->GetTipInfo();
			if( lpszTipInfo != NULL && lpszTipInfo[0] != _T('\0'))
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), lpszTipInfo  );
				return TRUE;
			}
		}
		else if( pChild->IsKindOf(RUNTIME_CLASS(CODProgressCtrl) ) )
		{
			// �Ի������
			LPCTSTR lpszTipInfo = ((CODProgressCtrl*)pChild)->GetTipInfo();
			if( lpszTipInfo != NULL && lpszTipInfo[0] != _T('\0'))
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), lpszTipInfo  );
				return TRUE;
			}
		}
	}

	CString strName;
	GetDlgItemText(iID, strName );
	if ( !strName.IsEmpty() )
	{
		_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strName );
		return TRUE;
	}

	// ��ʾ��Ϣ
	if( strName.LoadString( iID ) )
	{
		if( !strName.IsEmpty() )
		{
			int iPos = strName.Find( _T("\n") );
			if ( iPos > 0 )
			{
				strName = strName.Mid( iPos + 1 );
			}

			CStringW strTipInfo;
			if( m_layoutManager.GetControlTipInfo( strName, strTipInfo ) )
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strTipInfo);
			}
			else
			{
				if ( iPos > 0 )
				{
					_tcscpy_s(pTTT->szText, _countof(pTTT->szText), strName );
				}
				else
				{
					pTTT->lpszText = MAKEINTRESOURCE(iID);
					pTTT->hinst = AfxGetResourceHandle();
				}
			}
		}
		else
		{
			pTTT->lpszText = MAKEINTRESOURCE(iID);
			pTTT->hinst = AfxGetResourceHandle();
		}
	}

	return(TRUE);
}

BOOL CLauncherDlg::OnEraseBkgnd(CDC* pDC)
{
	return TRUE; 
}

void CLauncherDlg::OnDestroy()
{
	if ( m_pThisWatcher != NULL )
	{
		// GlobalUnRegister ���� Destroy ����
		GlobalUnRegister( (IWatcher*) m_pThisWatcher );
		((IWatcher*)m_pThisWatcher)->Destroy();
		m_pThisWatcher = NULL;
	}

	if( m_pUpdateThread)
	{
		m_pUpdateThread->Stop();
		if( m_pUpdateThread->m_hThread != INVALID_HANDLE_VALUE )
		{
			HANDLE hProcess = GetCurrentProcess();
			HANDLE hThread = m_pUpdateThread->m_hThread;
			DuplicateHandle( hProcess, m_pUpdateThread->m_hThread, hProcess, &hThread, NULL, FALSE, DUPLICATE_SAME_ACCESS );
			WaitThreadUntilQuit( hThread );
		}
	}

	CDialog::OnDestroy();

	if ( m_hIconLarge != NULL )
	{
		DestroyIcon( m_hIconLarge );
		m_hIconLarge = NULL;
	}

	if ( m_hIconSmall != NULL )
	{
		DestroyIcon( m_hIconSmall );
		m_hIconSmall = NULL;
	}
}

void CLauncherDlg::OnClose()
{
	CDialog::OnClose();
}

void CLauncherDlg::OnCancel()
{
	// ͬOnOK����Ϊ
	OnOK();
}

void CLauncherDlg::OnOK()
{
	if( m_pUpdateThread != NULL )
	{
		if( m_pUpdateThread->IsUpdatingInProgress() )
		{
			m_pUpdateThread->ResetMergeEvent();

			CString str;
			if( m_pUpdateThread->IsMerging())
			{
				//str = _T("���¹��������޸ı����ļ���ǿ����ֹ�ᵼ���ļ����ݱ��ƻ�������ʱ���ܻ��ᵼ�³����޷�������������ȷ��Ҫ�˳���");
				str = GlobalGetText( QST_STOP_UPDATE).c_str();
			}
			else
			{
				//str = _T("���¹������ڽ����У���ȷ��Ҫ�˳�ô��");
				str = GlobalGetText( QST_STOP_UPDATE2).c_str();
			}
			if( IDNO == AfxMessageBox( str, MB_YESNO|MB_ICONWARNING|MB_DEFBUTTON2 ) )
			{
				// ����RunModalLoop()ѭ��
				m_nFlags |= WF_CONTINUEMODAL;

				m_pUpdateThread->SetMergeEvent();
				return ;
			}	
			m_pUpdateThread->SetMergeEvent();
		}

		// ֪ͨ���¹�������ֹ����
		m_pUpdateThread->Stop();
		ShowWindow(SW_HIDE);		
	}

	CDialog::OnOK();

	AfxPostQuitMessage(0);
}

void CLauncherDlg::OnShowLauncher()
{
	if ( IsIconic() )
	{
		ShowWindow(SW_RESTORE);
	}
	else
	{
		ShowWindow(SW_SHOW);
	}

	SetForegroundWindow();
}

void CLauncherDlg::OnQuitApp()
{
	PostMessage( WM_COMMAND, IDCANCEL, 0 );
}

BOOL CLauncherDlg::_TryStartDownloadWebPageThread()
{
	CWinThread *pThread = AfxBeginThread( RUNTIME_CLASS(CDownloadWebPageThread),
		THREAD_PRIORITY_NORMAL, 0, CREATE_SUSPENDED, NULL );
	if ( pThread != NULL )
	{
		pThread->ResumeThread();
	}
	else
	{
		// ����Ļ����ô�������ʾ�ɵ�ҳ��
	}

	return pThread != NULL;
}

// ˢ��webҳ
void CLauncherDlg::ReNavigageWebPage()
{
	// �ɹ��Ļ�ˢ��һ�£���ʾ��ҳ��
	if( m_pUpdateThread != NULL )
	{
		const PFSX::CVersionInfo& versionInfo = m_pUpdateThread->GetServerVersion();
		if( versionInfo.IsValid() )
		{
			m_htmlView->SetServerVersionInfo( versionInfo );
		}
	}

	m_htmlView->Navigate2( GetLauncherWebPageUrl() );
}

LRESULT CLauncherDlg::OnDownloadWebPageEnd( WPARAM wParam, LPARAM lParam)
{
	m_htmlView->Navigate2( GetLauncherWebPageUrl() );
	return 0;
}

HBRUSH CLauncherDlg::OnCtlColor(CDC* pDC, CWnd* pWnd, UINT nCtlColor)
{
	if ( nCtlColor == CTLCOLOR_STATIC  )
	{
		if( pWnd == &m_globalStatus ) 
		{
			pDC->SetBkMode( TRANSPARENT );
			pDC->SetTextColor( m_globalStatusControlColor ); 
			return m_hBKBrush;
		}
		else if( pWnd == &m_stepMessage ) 
		{
			pDC->SetBkMode( TRANSPARENT );
			pDC->SetTextColor( m_stepMessageControlColor ); 
			return m_hBKBrush;
		}
	}

	HBRUSH hbr = CDialog::OnCtlColor(pDC, pWnd, nCtlColor);
	// You can change any attributes of the DC here
	// Return a different brush if the default is not desired
	return hbr;
}
