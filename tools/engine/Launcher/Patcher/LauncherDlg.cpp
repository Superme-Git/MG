// LauncherDlg.cpp : 实现文件
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

// 用于进程保护的进程创建方案(PP)
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

	// 注：本函数仿照AfxPumpMessage()实现，但此处需用PeekMessage而不能使用GetMessage()
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
				// 等待超时，只好强行终止线程，一般会导致内存泄露
				TerminateThread( rhThread, -1 );
			}
			else if( dwRet == WAIT_OBJECT_0 )
			{
				// 线程正常终止！
			}
			else// if ( dwRet == WAIT_FAILED )
			{
				// 其它错误
			}
		}

		CloseHandle( rhThread );
		rhThread = INVALID_HANDLE_VALUE;
	}
}

// 检测启动路径
// 必须从launcher启动,此时ModuleDir必含有patcher.1或patcher
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

// CLauncherDlg 对话框

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

// 支持第2个Extension
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
		//AfxMessageBox( L"请从launcher启动,然后才能执行手动更新！",  MB_OK|MB_ICONERROR );
		//m_trayIcon.ShowBalloonTipInfo( L"请从launcher启动,然后才能执行手动更新！" );
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
	//			GlobalGetText( TIP_SELECT_UPDATE_PATCH).c_str(), // L"修仙更新包(*.%s)|*.%s;*.zip|所有文件(*.*)|*.*||", 
	//			pszExtension, 
	//			pszExtension);
	std::wstring strExtension;
	strExtension.assign(pszExtension);

	// 由于前期规划不当，这里补充第二个扩展名的支持显得非常蹩脚。先这样吧。
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

	// 如果线程尚未启动，尝试启动
	_TryStartUpdateThread();

	if ( m_pUpdateThread != NULL )
	{
		// 检查游戏更新
		m_btnCheckUpdate.EnableWindow(FALSE);
		m_btnRepairSetup.EnableWindow(FALSE);
		m_btnManualUpdate.EnableWindow(FALSE);
		m_btnLaunchGame.EnableWindow(FALSE);

		// 根据命令行选择更新模式
		std::wstring patchpath = openPatchDlg.GetPathName().GetString();
		m_pUpdateThread->SetPatchPath( patchpath );
		m_pUpdateThread->CheckVersion( UPDATE_MODE_PATCH );
	}
	else
	{
		//AfxMessageBox(L"更新失败！更新线程没有成功启动，请重新尝试.", MB_OK|MB_ICONERROR );
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
			//L"版本验证失败！没有成功启动版本验证工具，请重新尝试.", 
			GlobalGetText( ERR_START_REPAIR).c_str(),
			MB_OK|MB_ICONERROR );
	}
}

LRESULT CLauncherDlg::OnLauncherUpdateResult(WPARAM wParam, LPARAM lParam)
{
	if ( lParam != 0 )
	{
		// 重新启动Bootstrap程序
		CString strApplicationName = GetBootstrapPath();
		STARTUPINFO startupInfo = {0};
		startupInfo.cb = sizeof(STARTUPINFO);

		PROCESS_INFORMATION processInfo = {0}; 

		TCHAR szPath[MAX_PATH * 2 + 1] = {0};
		strApplicationName.Replace( L'/', L'\\');

		PathCanonicalize( szPath, strApplicationName );
		PathQuoteSpaces ( szPath );

		// 指定命令行参数
		DWORD dwProcessId = GetCurrentProcessId();
		
		strApplicationName.Format(L" -reboot-launcher -pid %u", dwProcessId );

		LPTSTR lpcmdline = theApp.m_lpCmdLine;	
		// 重启launcher时,追加补丁包路径
		// 1.可能是用户双击补丁包或者拖放补丁包到patcher窗口上两种方式提供的
		// 2.对于用户点击"手动更新"按钮方式提供的更新包,命令行没有-patch和补丁包路径参数.
		LPTSTR pStr = lpcmdline ? _tcsstr( lpcmdline, _T("-patch ")) : NULL;
		if( pStr )
		{
			// 1.用户双击补丁包或者拖放补丁包到patcher窗口上两种方式提供的补丁包路径参数
			wcscat_s( szPath, MAX_PATH, strApplicationName);
			wcscat_s( szPath, MAX_PATH, L" ");
			wcscat_s( szPath, MAX_PATH, lpcmdline);
		}
		else
		{
			// 2.重启launcher时,追加"手动更新"提供的补丁包路径
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
				// 传入服务器,更新模式参数
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
			//ReportMessage( _T("创建进程失败%s:\n%s"), szPath, strError.GetString() );
			ReportMessage( GlobalFormatText( ERR_CREATE_PROCESS, szPath, strError.GetString()).c_str() );

			return 1;
		}
	}

	// 启动新版本检查过程
	PostMessage(WM_COMMAND, IDC_CHECK_UPDATE, 0 );

	return 1;
}

LRESULT CLauncherDlg::OnTrayNotify(WPARAM wParam, LPARAM lParam)
{
	// 托盘事件通知
	if ( lParam == WM_MOUSEMOVE )
	{
		if( m_pUpdateThread && m_pUpdateThread->IsBKUpdatingInProgress() &&
			m_pUpdateThread->GetUpdateMode() == UPDATE_MODE_BACKGROUND)
		{
			//m_trayIcon.ShowNormalTipInfo( L"正在进行后台更新。请放心，不会影响您当前的游戏进程！" );	
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
	// 简单的重入检测机制
	// Launcher启动Patcher时,如果Patcher已经提前起来,此时会给Patcher发检查新版本的消息
	// 此时有可能CSelectServerDialog::DoModal()函数还未返回,这就导致本消息函数重入.
	// 下面的 s_bLocked 和 ScopedLockey 为了避免这种情况的重入而实现的.
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

	if( bCheckOK ) // 检查版本成功
	{
		if( mode != UPDATE_MODE_BACKGROUND )
		{
			//m_trayIcon.ShowBalloonTipInfo( bNewVersion ? L"有新版本需要更新！" : 
			//	( bLocalValid ? L"您的游戏客户端已经是最新版本！" : L"您的客户端版本不完整,强烈建议你执行版本验证！" ) );
			m_trayIcon.ShowBalloonTipInfo( bNewVersion ? GlobalGetText( TIP_HAS_NEW_VERSION).c_str(): 
				( bLocalValid ? GlobalGetText( TIP_ALREADY_NEW_VERSION).c_str() : GlobalGetText( TIP_CLIENT_NOT_INTACT).c_str()) );
		}

		ReNavigageWebPage();

		if( bNewVersion ) // 有新版本
		{
			if( mode == UPDATE_MODE_DEFAULT || mode == UPDATE_MODE_AUTOPATCH )
			{
				if( !CheckStartupPath() )
				{
					//AfxMessageBox( L"有新版本需要更新！\n请从launcher启动,然后才能执行版本更新！",  MB_OK|MB_ICONERROR );
					//m_trayIcon.ShowBalloonTipInfo( L"有新版本需要更新！\n请从launcher启动,然后才能执行版本更新！" );
					const std::wstring& str = GlobalGetText( TIP_HAS_NEW_VERSION2);
					AfxMessageBox( str.c_str(), MB_OK|MB_ICONERROR);
					m_trayIcon.ShowBalloonTipInfo( str.c_str());
					m_btnCheckUpdate.EnableWindow();
					m_btnManualUpdate.EnableWindow();
					m_btnRepairSetup.EnableWindow();
					return 0;
				}

				FlashWindowEx( FLASHW_ALL|FLASHW_TIMERNOFG, 3, 0);

				
				// 如果本次更新为重新启动之后的更新，更新服务器和更新模式已经在命令行指定，
				// 这里无需再次为用户弹对话框指定。
				if( !m_pUpdateThread->GetRebooted() )
				{
					INT_PTR res = IDCANCEL;
					//if( bAutoPatch )
					//{
					//	// 启用补丁包自动更新的标志已置
					//	res = AfxMessageBox(
					//		//L"当前客户端版本需要下载大量数据以完成更新。\n"
					//		//L"\n"
					//		//L"点击\"是\"，我们的更新程序将尝试自动完成此次更新。\n"
					//		//L"点击\"否\"，更新程序尝试为你自动打开手动包下载页面。\n"
					//		//L"点击\"取消\"，您可以自己选择补丁包进行更新；\n"
					//		//L"\n"
					//		//L"自动更新完成后如果仍旧不是最新版本，将会提示你继续更新。\n"
					//		//L"自动更新需要时间较长，请耐心等待。",
					//		GlobalGetText( QST_AUTO_PATH_UPDATE).c_str(),
					//		MB_YESNOCANCEL|MB_ICONQUESTION);
					//}
					//else
					{
						const PFSX::CVersionInfo& rVersionInfo = m_pUpdateThread->GetServerVersion();

						//CSelectServerDialog dlg(rVersionInfo);
						SetDownloadSvrDlg dlg(rVersionInfo);
						// 显示上次选择的服务器
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
							// 从缺省模式切换到补丁包自动更新模式
							mode = UPDATE_MODE_AUTOPATCH;
							//m_trayIcon.ShowBalloonTipInfo( L"正在使用补丁包自动更新客户端..." );
							m_trayIcon.ShowBalloonTipInfo( GlobalGetText( TIP_PATCH_UPDATING).c_str());
						}
						//else
						//{
						//	// 确保即使使用UPDATE_MODE_AUTOPATCH模式的更新管理器CheckNewVersion,
						//	// 此时也要切换到缺省更新管理器
						//	mode = UPDATE_MODE_DEFAULT;
						//	//m_trayIcon.ShowBalloonTipInfo( L"正在自动更新客户端..." );
						//	m_trayIcon.ShowBalloonTipInfo( GlobalGetText( TIP_DEFAULT_UPDATING).c_str());
						//}

						m_pUpdateThread->Update( (UPDATE_MODE)mode );
					}
					else
					{
						// 用户取消了选择服务器，暂不更新
						m_btnCheckUpdate.EnableWindow();
						m_btnManualUpdate.EnableWindow();
						m_btnRepairSetup.EnableWindow();
						
						// 这里已经明确已经有了新版本要更新,所以不启用"开始游戏按钮"
						// m_btnLaunchGame.EnableWindow();
						if( IDNO == res && bAutoPatch )
						{
							// 自动为用户打开补丁包下载页面
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
		else // 没有新版本
		{
			if( mode == UPDATE_MODE_BACKGROUND ) 
			{
				upd::SetLastError(0);// Clear the last error.
				SetDlgItemText( IDC_GLOBAL_STATUS, L"");
				GetDlgItem( IDC_GLOBAL_STATUS)->ShowWindow( SW_HIDE );
			}
			else
			{
				// 尽管没有新版本, 但客户端版本无效, 需要修复客户端
				if( !bLocalValid )
				{
					if( m_pUpdateThread->GetRebooted() )
					{
						return 0;
					}
					int nChoice = AfxMessageBox(
						//L"当前客户端存在问题！是否修复客户端？", 
						GlobalGetText( QST_REPAIR).c_str(),
						MB_YESNO|MB_ICONQUESTION);
					if( nChoice == IDYES )
					{
						//m_trayIcon.ShowBalloonTipInfo( L"正在修复客户端..." );
						m_trayIcon.ShowBalloonTipInfo( GlobalGetText( TIP_REPAIR_UPDATING).c_str());
						
						if( mode == UPDATE_MODE_PATCH )
						{
							// 修改一下patch模式为repair模式
							mode = UPDATE_MODE_REPAIR;
						}

						// 以当前模式修复客户端
						m_pUpdateThread->Update((UPDATE_MODE) mode );
						return 0;
					}
				}
				else
				{
					if( mode == UPDATE_MODE_PATCH || mode == UPDATE_MODE_REPAIR )
					{
						int nChoice = AfxMessageBox(
							//L"当前客户端已经是最新版本！是否使用该补丁包强制修复客户端？", 
							GlobalGetText( QST_ENFORCE_REPAIR).c_str(), 
							MB_YESNO|MB_ICONQUESTION);
						if( nChoice == IDYES )
						{
							//m_trayIcon.ShowBalloonTipInfo( L"正在使用补丁包强制修复客户端..." );
							m_trayIcon.ShowBalloonTipInfo( GlobalGetText( TIP_ENFORCE_REPAIRING).c_str());
							// 强制修复客户端
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
	else // 检查版本失败
	{
		const upd::ErrorInfo& rErrorInfo = upd::GetLastError();
		CString strMessage;
		if( mode == UPDATE_MODE_BACKGROUND )
		{
			strMessage = GlobalFormatText( ERR_BACKGROUND_UPDATE, rErrorInfo.ErrorDetail.c_str(), 
				rErrorInfo.ErrorCode).c_str();
			// strMessage.Format( L"后台更新: %s(%ld)",  );
		}
		else if ( mode == UPDATE_MODE_PATCH )
		{
			//strMessage.Format( L"使用补丁包更新失败！\n%s(%ld)", 
			strMessage = GlobalFormatText( ERR_PATCH_UPDATE, rErrorInfo.ErrorDetail.c_str(), 
				rErrorInfo.ErrorCode).c_str();
		}
		else
		{
			//strMessage.Format( L"无法获知是否有新版本需要更新！\n%s(%ld)", 
			strMessage = GlobalFormatText( ERR_CANNOT_GET_NEW_VERSION, rErrorInfo.ErrorDetail.c_str(), 
				rErrorInfo.ErrorCode ).c_str();
		}

		if( mode == UPDATE_MODE_BACKGROUND )
		{
			KHMsgLog_Error( _T("%s\n"), strMessage.GetString() );

			// 对于服务器没有新版本需要更新的情况，不报告错误
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

		// 即使检查版本失败，也允许用户启动游戏客户端。
		if ( lParam != UPDATE_MODE_BACKGROUND )
		{
			m_btnLaunchGame.EnableWindow();
		}

		// 如果是补丁包更新方式，完成后将命令行清空
		if( lParam == UPDATE_MODE_PATCH)
		{
			if( theApp.m_lpCmdLine)
			{
				// 删除-patch 及其参数, 格式如下:
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
		// 后台更新完毕，不改变“启动游戏”按钮的状态
		m_btnLaunchGame.EnableWindow();
	}

	if ( wParam )
	{
		if ( lParam == UPDATE_MODE_BACKGROUND )
		{
			KHMsgLog_Info( L"后台更新完毕。\n" );
			//SetDlgItemText( IDC_GLOBAL_STATUS,  L"后台更新完毕。" );
			SetDlgItemText( IDC_GLOBAL_STATUS, GlobalGetText( TIP_BACKGROUND_UPDATE_COMPLETE).c_str());
		}
		else
		{
			m_trayIcon.ShowBalloonTipInfo( GlobalGetText( TIP_UPDATE_SUCCESS).c_str() );
			//SetDlgItemText( IDC_GLOBAL_STATUS,  L"更新成功!" );
			SetDlgItemText( IDC_GLOBAL_STATUS, GlobalGetText( TIP_UPDATE_SUCCESS).c_str());
		}

		// 刷新Web页
		ReNavigageWebPage();
	}
	else
	{
		const upd::ErrorInfo& rErrorInfo = upd::GetLastError();
		if( rErrorInfo.ErrorCode != SC_USER_CANCELED )
		{
			if ( lParam == UPDATE_MODE_BACKGROUND )
			{
				CString strMessage = GlobalFormatText( ERR_BACKGROUND_UPDATE,	// L"后台更新失败！%s(%ld)", 
					rErrorInfo.ErrorDetail.c_str(), rErrorInfo.ErrorCode ).c_str();
				// 后台更新不弹出对话框
				// m_trayIcon.ShowBalloonTipError(strMessage);

				// 记录日志并显示在对话框的消息栏上
				KHMsgLog_Error(  L"%s\n", strMessage.GetString() );
				SetDlgItemText( IDC_GLOBAL_STATUS, strMessage );
			}
			else
			{
				// L"更新失败！\n%s(%ld)", 

				ReportMessage( GlobalFormatText( ERR_UPDATE, rErrorInfo.ErrorDetail.c_str(), 
					rErrorInfo.ErrorCode).c_str() );
			}
		}
		else
		{
			KHMsgLog_Info( L"用户中途取消了更新。\n" );
		}
	}

	// 如果是补丁包更新方式，完成后将命令行清空
	if( lParam == UPDATE_MODE_PATCH && !CUpdateThread::IsNeedReboot())
	{
		if( theApp.m_lpCmdLine)
		{
			theApp.m_lpCmdLine[0] = 0;
		}
	}

	return 0;
}

// CLauncherDlg 消息处理程序
int CLauncherDlg::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CDialog::OnCreate(lpCreateStruct) == -1)
		return -1;

	EnableToolTips();

	//////////////////////////////////////////////////////////////////////////
	//
	// 创建浏览器控件
	//
	CRect rcBrowser(0,0,1,1);
	m_htmlView = (CWebBrowserCtrl*)RUNTIME_CLASS(CWebBrowserCtrl)->CreateObject();
	if( !m_htmlView->Create( NULL, NULL, WS_CHILD | WS_VISIBLE, rcBrowser, this, NULL ) )
	{
		//ReportMessage(L"创建HtmlView控件失败!");
		ReportMessage( GlobalGetText( ERR_CREATE_HTMLVIEW).c_str());
		return -1;
	}

	//////////////////////////////////////////////////////////////////////////
	// 因为窗口本身没有标题栏，所以需要手动设置程序标题，
	// 用以显示在任务栏中。_T("修仙更新程序")
	//
	// 为了支持国际化和支持多个产品，这里优先从配置文件中读取窗口标题.
	// 注意，这个名字必须保证和launcher.xml中的相同ID的字符串相同。
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
		//ReportMessage(L"加载Launcher资源包失败!");
		ReportMessage( GlobalGetText( ERR_LOAD_UIRESOURCE).c_str());
		return -1;
	}

	//
	// 设置控件的面板窗口，同时指定UI资源包的目录和布局配置文件名
	//
	if( !m_layoutManager.SetPanel( this ) )
	{
		//ReportMessage(L"加载Launcher资源包失败!");
		ReportMessage( GlobalGetText( ERR_LOAD_UIRESOURCE).c_str());
		return -1;
	}
	
	// 获取配置文件中配置的GlobalStatus和StepMessage 2个静态文本控件的前景文本色
	if( !m_layoutManager.GetControlColor(_T("GlobalStatus"), m_globalStatusControlColor ) )
	{
		m_globalStatusControlColor = RGB(245,10,40); // 缺省颜色值 
	}
	if( !m_layoutManager.GetControlColor( _T("StepMessage"), m_stepMessageControlColor ) )
	{
		m_stepMessageControlColor = RGB(10,40,245); // 缺省颜色值 
	}
	
	// 创建GlobalStatus和StepMessage 2个静态文本控件的背景刷
	COLORREF clrBKBrush;
	if ( !m_layoutManager.GetControlBkColor(_T("GlobalStatus"), clrBKBrush) )
	{
		clrBKBrush = RGB(208,202,224);// 缺省背景颜色 
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

	KHMsgLogW( L"尝试启动更新线程...\n");
	CWinThread *pThread = AfxBeginThread( RUNTIME_CLASS(CUpdateThread),
		THREAD_PRIORITY_NORMAL, 0, CREATE_SUSPENDED, NULL );
	KHMsgLog_DebugW( L"启动更新线程:pThread=(%p)。\n", pThread );
	if( pThread != NULL )
	{
		m_pUpdateThread = (CUpdateThread*)pThread;
		m_pUpdateThread->m_bAutoDelete = FALSE;
		m_pUpdateThread->ResumeThread();
		KHMsgLogW( L"更新线程启动完毕。\n");
	}
	else
	{
		DWORD dwLastError = GetLastError();
		KHMsgLog_DebugW( L"无法启动更新(%lu)！\n", dwLastError );
		//ReportMessage( _T("无法启动更新(%lu)!"), dwLastError );
		ReportMessage( GlobalFormatText( ERR_START_UPDATE, dwLastError).c_str());
	}
	return pThread != NULL;
}

// 禁止浏览器空间的滚动条
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

	// 首先通过配置文件确定名字。
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

	// 设置此对话框的图标。当应用程序主窗口不是对话框时，框架将自动
	//  执行此操作
	if ( m_hIconLarge != NULL && m_hIconSmall != NULL )
	{
		SetIcon(m_hIconLarge, TRUE);			// 设置大图标
		SetIcon(m_hIconSmall, FALSE);		// 设置小图标
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
	// WebBrowser定位并初始化
	//
	CRect rcBrowser;
	if( !m_layoutManager.GetControlRect( _T("WebControl"), rcBrowser ) )
	{
		// 历史原因，这个名字不需要翻译，废弃“网页框”这个中文，改用“WebControl”。
		// 但是对于尚未修改的资源，尝试一次“网页框”。
		if( !m_layoutManager.GetControlRect( _T("网页框"), rcBrowser ) )
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
	// 为GLOBAL_STATUS静态框定位
	//
	CRect rcGlobalStatus, rcStepMessage;
	if( m_layoutManager.GetControlRect(_T("GlobalStatus"), rcGlobalStatus) )
	{
		m_globalStatus.MoveWindow(rcGlobalStatus, FALSE);
	}

	//
	// 为STEP_MESSAGE静态框定位
	//
	if( m_layoutManager.GetControlRect(_T("StepMessage"), rcStepMessage) )
	{
		m_stepMessage.MoveWindow(rcStepMessage, FALSE);
	}

	//
	// 进度条控件初始化
	//

	// 显示百分比的特征首先初始化，这样，ApplyProgressCtrl才有可能通过配置修改这个特征。
	m_updatePgrsCtrl.SetShowPercent( TRUE ); 	
	VERIFY( m_layoutManager.ApplyProgressCtrl( IDC_DOWNLOAD_PROGRESS, m_downloadPgrsCtrl ) );
	VERIFY( m_layoutManager.ApplyProgressCtrl( IDC_UPDATE_PROGRESS, m_updatePgrsCtrl ) );

	m_downloadPgrsCtrl.SetRange( 0, 100 );
	m_downloadPgrsCtrl.SetStep( 1 );
	m_updatePgrsCtrl.SetRange( 0, 100 );
	m_updatePgrsCtrl.SetStep( 20 );

	//
	// 按钮控件初始化
	//
	m_layoutManager.ApplyButton(IDC_CHECK_UPDATE, m_btnCheckUpdate );
	m_layoutManager.ApplyButton(IDC_MANUAL_UPDATE, m_btnManualUpdate );
	m_layoutManager.ApplyButton(IDC_REPAIR_SETUP, m_btnRepairSetup );
	m_layoutManager.ApplyButton(IDC_LAUNCH_GAME, m_btnLaunchGame );
	m_layoutManager.ApplyButton(IDMINIMIZE, m_btnMinimize );
	m_layoutManager.ApplyButton(IDCANCEL, m_btnClose );

	// Button 初始状态
	m_btnLaunchGame.SetFocus();

	// 托盘图标初始化
	m_trayIcon.SetNotificationWnd( GetSafeHwnd(), WM_TRAY_NOTIFY, m_hIconSmall, strCaptionPatcher.c_str() );

	CWndWatcher* _pThisWatcher = new CWndWatcher();
	_pThisWatcher->SetHwnd( GetSafeHwnd() );
	GlobalRegister( _pThisWatcher );
	m_pThisWatcher = _pThisWatcher;

	// 检查游戏更新
	m_btnCheckUpdate.EnableWindow(FALSE);
	m_btnManualUpdate.EnableWindow(FALSE);
	m_btnRepairSetup.EnableWindow(FALSE);
	m_btnLaunchGame.EnableWindow(FALSE);

	// 设置配置文件路径
	CString strTemp = GetBootstrapPath();
	CString strPath = strTemp.Left( strTemp.ReverseFind( L'\\')+1);
	strPath += L"patcher.ini";
	CProfile::GetInstance().SetFileName( strPath.GetString());

	// 启动新版本检查过程
	OnBnClickedCheckUpdate();
	
	int tryCount = 0;
	while (tryCount++ < 10)
	{
		if(StartEngine())
			break;

		Sleep(100);
	}

	return FALSE;  // 除非将焦点设置到控件，否则返回 TRUE
}

// 如果向对话框添加最小化按钮，则需要下面的代码
//  来绘制该图标。对于使用文档/视图模型的 MFC 应用程序，
//  这将由框架自动完成。
void CLauncherDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // 用于绘制的设备上下文

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// 使图标在工作矩形中居中
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// 绘制图标
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

//当用户拖动最小化窗口时系统调用此函数取得光标显示。
//
HCURSOR CLauncherDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

void CLauncherDlg::OnBnClickedCheckUpdate()
{
	// 如果线程尚未启动，尝试启动
	_TryStartUpdateThread();

	if ( m_pUpdateThread != NULL )
	{
		// 检查游戏更新
		m_btnCheckUpdate.EnableWindow(FALSE);
		m_btnRepairSetup.EnableWindow(FALSE);
		m_btnManualUpdate.EnableWindow(FALSE);
		m_btnLaunchGame.EnableWindow(FALSE);

		// 根据命令行选择更新模式
		UPDATE_MODE mode = UPDATE_MODE_AUTOPATCH; // UPDATE_MODE_DEFAULT;
		int server = -1;
		TCHAR* lpcmdline = theApp.m_lpCmdLine;
		lpcmdline = _tcsstr( lpcmdline, L"-patch ");
		if( lpcmdline)
		{
			std::wstring patchpath = lpcmdline + 7;
			// 去掉头尾的 "" 号
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
			// 分析-r -server -mode 参数
			lpcmdline = _tcsstr( theApp.m_lpCmdLine, L"-r "); // -r 表示本次为重新启动
			if ( lpcmdline )
			{
				// 设置重启标志
				m_pUpdateThread->SetRebooted(TRUE);

				// -server : 重启前指定的server
				// -mode : 重启前指定的mode
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

#pragma optimize("",off) // 避免因优化导致调用 MyCreateProcess() 导致的问题(发生在Win2000下)

void CLauncherDlg::OnBnClickedLaunchGame()
{
	VMProtectBeginVirtualization
		
	// 启动游戏客户端程序
	CString strApplicationName = GetGameClientPath();
	DWORD dwError = 0;

	// 客户端启动实例个数校验(暂时不允许超过3开)
	// 校验同名的进程数量,不可靠的检测方法,暂时只能如此
	CString strProcessFileName = strApplicationName;
	PathStripPath(strProcessFileName.GetBuffer() );
	strProcessFileName.ReleaseBuffer();
	if( strProcessFileName.GetLength() > 0 && 
		!MultiInstanceCheck( strProcessFileName.GetString() ) )
	{
		const std::wstring& strProductName = GlobalGetText(TOK_PRODUCT_CLIENT);
		if( !strProductName.empty() )
		{
			KHMsgLog_ErrorW( L"同时打开的%s已达到上限(%s)！\n", strProductName.c_str(), strApplicationName.GetString() );
		}
		else
		{
			KHMsgLog_ErrorW( L"同时打开的客户端程序已达到上限(%s)！\n", strApplicationName.GetString() );
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
		// 如果线程尚未启动，尝试启动
		_TryStartUpdateThread();
		if( m_pUpdateThread != NULL )
		{
			// 后台更新线程正在进行过程中
			// 不允许手动检测新版本,不允许手动更新,不允许版本验证
			m_btnCheckUpdate.EnableWindow(FALSE);
			m_btnManualUpdate.EnableWindow(FALSE);
			m_btnRepairSetup.EnableWindow(FALSE); 
			
			KHMsgLog_DebugW( L"启动后台更新...\n");
			m_pUpdateThread->CheckVersion( UPDATE_MODE_BACKGROUND );
		}
		else
		{
			KHMsgLog_ErrorW( L"启动后台更新失败(相应的线程没有创建成功)！\n");
		}

		// 最小化窗口到系统托盘位置
		//OnBnClickedMinimize();
		//KHMsgLog_DebugW( L"最小化窗口到系统托盘。\n");
	//}
	//else
	//{
	//	KHMsgLog_Error(_T("MyCreateProcess(%s) failed(Error:%d)。\n"), 
	//		strApplicationName.GetString(), dwRet );
	//	//ReportMessage( _T("启动客户端失败(%u)：\n\n\t%s\n\n请尝试对客户端进行版本验证或重新安装客户端程序。"),
	//	ReportMessage( GlobalFormatText( ERR_START_CLIENT2, 
	//			dwRet, strApplicationName.GetString()).c_str() );
	//}

	VMProtectEnd
}

#pragma optimize("",on)

void CLauncherDlg::OnBnClickedMinimize()
{
	// 最小化对话框窗口
	//ShowWindow(SW_MINIMIZE);
	// 最小化对话框窗口到托盘区
	ShowWindow(SW_HIDE);
}

LRESULT CLauncherDlg::OnNcHitTest(CPoint point)
{
	LRESULT nRet = CDialog::OnNcHitTest(point);
	if(nRet == HTCLIENT)
	{
		// 允许鼠标点中窗体客户区域移动窗口
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

	// 特殊处理CDIYButton类的提示信息，它们的悬浮提示来自于XML定义。
	CWnd *pChild = CWnd::FromHandlePermanent((HWND)nID);
	if( pChild != NULL )
	{
		if( pChild->IsKindOf(RUNTIME_CLASS(CDIYButton)))
		{
			// 自绘按钮
			LPCTSTR lpszTipInfo = ((CDIYButton*)pChild)->GetTipInfo();
			if( lpszTipInfo != NULL && lpszTipInfo[0] != _T('\0'))
			{
				_tcscpy_s(pTTT->szText, _countof(pTTT->szText), lpszTipInfo  );
				return TRUE;
			}
		}
		else if( pChild->IsKindOf(RUNTIME_CLASS(CODProgressCtrl) ) )
		{
			// 自绘进度条
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

	// 提示信息
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
		// GlobalUnRegister 不会 Destroy 对象
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
	// 同OnOK的行为
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
				//str = _T("更新过程正在修改本地文件！强行终止会导致文件内容被破坏，严重时可能还会导致程序无法正常启动。您确定要退出吗？");
				str = GlobalGetText( QST_STOP_UPDATE).c_str();
			}
			else
			{
				//str = _T("更新过程正在进行中！您确定要退出么？");
				str = GlobalGetText( QST_STOP_UPDATE2).c_str();
			}
			if( IDNO == AfxMessageBox( str, MB_YESNO|MB_ICONWARNING|MB_DEFBUTTON2 ) )
			{
				// 继续RunModalLoop()循环
				m_nFlags |= WF_CONTINUEMODAL;

				m_pUpdateThread->SetMergeEvent();
				return ;
			}	
			m_pUpdateThread->SetMergeEvent();
		}

		// 通知更新管理器中止更新
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
		// 出错的话不用处理，还显示旧的页面
	}

	return pThread != NULL;
}

// 刷新web页
void CLauncherDlg::ReNavigageWebPage()
{
	// 成功的话刷新一下，显示新页面
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
