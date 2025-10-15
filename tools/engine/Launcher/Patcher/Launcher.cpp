// Launcher.cpp : 定义应用程序的类行为。
//

#include "stdafx.h"
#include "Launcher.h"
#include "LauncherDlg.h"
#include "LauncherConfig.h"
#include <afxmt.h>
#include "../../pfs/src/common.h"
#include "KHLogger.h"
#include "TextManager.h"
#include <Psapi.h> // for GetModuleFileNameEx
#pragma comment( lib, "psapi.lib" )

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CLauncherApp

BEGIN_MESSAGE_MAP(CLauncherApp, CWinApp)
	ON_COMMAND(ID_HELP, &CWinApp::OnHelp)
END_MESSAGE_MAP()


// CLauncherApp 构造

CLauncherApp::CLauncherApp()
{
	// TODO: 在此处添加构造代码，
	// 将所有重要的初始化放置在 InitInstance 中
}


// 唯一的一个 CLauncherApp 对象

CLauncherApp theApp;

//
// 判断进程是否与Launcher为同一份安装版
//
BOOL IsSameInstallationProcess( HANDLE hProcess )
{
	TCHAR PathBuffer[MAX_PATH] = {0};
	GetModuleFileNameEx(hProcess, NULL, PathBuffer, MAX_PATH );

	LPCTSTR lpszCurrentInstallDir = GetInstallDir();
	static int nDirLen = lstrlen( lpszCurrentInstallDir );

	PathBuffer[nDirLen] = TEXT('\0');
	if( 0 == lstrcmpi( lpszCurrentInstallDir, PathBuffer ) )
	{
		return TRUE;
	}

	// TODO: 是否有必要使用 PathCommonPrefix() 确定路径？
	return FALSE;
}

//
// 判断窗口是否与Launcher为同一份安装版
//
BOOL IsSameInstallationWindow( HWND hWnd )
{
	// 获取窗口的进程ID
	DWORD dwProcessId = 0;
	::GetWindowThreadProcessId( hWnd, &dwProcessId );

	// 打开进程
	HANDLE hProcess = OpenProcess( PROCESS_QUERY_INFORMATION|PROCESS_VM_READ, FALSE, dwProcessId );
	if ( hProcess == NULL )
	{
		return FALSE;
	}

	// 判断进程是否与launcher.exe在同一个安装目录下
	BOOL bIsSameInstallation = IsSameInstallationProcess( hProcess );
	CloseHandle( hProcess );
	return bIsSameInstallation;
}


BOOL CLauncherApp::InitApplication()
{
	//////////////////////////////////////////////////////////////////////////
	//
	// 这里通过窗口名确定单实例启动Launcher程序(不区分Debug/Release版本)
	//
	// 如果下面的代码需要修改，请注意保持与Bootstrap.cpp保持同步。
	//
	//////////////////////////////////////////////////////////////////////////
	HWND hWndPrev, hWndChild = NULL;

	CString strAppName(MAKEINTRESOURCE(IDR_MAINFRAME));

	// Determine if a window with the class name exists...
	while (hWndPrev = ::FindWindowEx(NULL, hWndChild, _T("#32770"), strAppName))
	{
		// Determine if the window same as my installation
		if( !IsSameInstallationWindow( hWndPrev ) )
		{
			hWndChild = hWndPrev;
			continue;
		}

		// If so, does it have any popups?
		hWndChild = ::GetLastActivePopup(hWndPrev);

		// If iconic, restore the main window
		if (::IsIconic(hWndPrev))
			::ShowWindow(hWndPrev, SW_RESTORE);

		// If hidden, show the main window
		if ( !::IsWindowVisible(hWndPrev) )
			::ShowWindow(hWndPrev, SW_SHOW );

		// Bring the main window or its popup to the foreground
		::SetForegroundWindow(hWndChild);

		// and you are done activating the other application
		return FALSE;
	}

	return CWinApp::InitApplication();
}

// 注册PFS的log
class CInstallPFSLog : public  PFS::COutLog
{
public:
	virtual void Print( const wchar_t* lpszFormat, ...)
	{
		WCHAR szBuffer[16*1024] = {0};
		va_list args;
		va_start(args, lpszFormat);
		HRESULT hr = StringCchVPrintfW(szBuffer, _countof(szBuffer), lpszFormat, args);
		va_end(args);
		if(SUCCEEDED(hr))
		{
			KHMsgLogW(szBuffer);
		}
		else
		{
			ASSERT(FALSE);
		}
	}
	virtual void VPrint( const wchar_t* info, va_list vl)
	{
	}
} g_CInstallPFSLog;


// 获取Patcher自己所在的文件夹名字
LPCTSTR GetPatcherFolder()
{
	static TCHAR szPatcherFolder[MAX_PATH];
	if( szPatcherFolder[0] == 0 )
	{
		TCHAR szModuleDir[MAX_PATH] = {0};
		DWORD dwRet = GetModuleFileName( NULL, szModuleDir, MAX_PATH);
		LPTSTR lpSep = &szModuleDir[dwRet];
		while( *lpSep != _T('/') && *lpSep != _T('\\') && *lpSep != _T(':') )
		{
			lpSep --;
		}

		LPTSTR lpSepEnd = lpSep;

		do
		{
			lpSep --;
		}while( *lpSep != _T('/') && *lpSep != _T('\\') && *lpSep != _T(':') && *lpSep != _T('\0') );

		LPTSTR lpSepStart = ++ lpSep;

		_tcsncpy_s( szPatcherFolder, MAX_PATH, lpSepStart, lpSepEnd-lpSepStart );
	}

	return szPatcherFolder;
}

// CLauncherApp 初始化

HANDLE g_hP2spStartEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
//HANDLE g_hP2spPauseEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
HANDLE g_hHttpStartEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
//HANDLE g_hHttpPauseEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
//HANDLE g_hP2spCompleteEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
//HANDLE g_hHttpCompleteEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
//HANDLE g_hP2spErrorEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
//HANDLE g_hHttpErrorEvent = CreateEvent(NULL, FALSE, FALSE, NULL);

BOOL CLauncherApp::InitInstance()
{
	// 如果一个运行在 Windows XP 上的应用程序清单指定要
	// 使用 ComCtl32.dll 版本 6 或更高版本来启用可视化方式，
	//则需要 InitCommonControlsEx()。否则，将无法创建窗口。
	INITCOMMONCONTROLSEX InitCtrls;
	InitCtrls.dwSize = sizeof(InitCtrls);
	// 将它设置为包括所有要在应用程序中使用的
	// 公共控件类。
	InitCtrls.dwICC = ICC_WIN95_CLASSES;
	InitCommonControlsEx(&InitCtrls);

	CWinApp::InitInstance();

	AfxEnableControlContainer();

	// Launcher的运行时目录为其父目录，即GameClient.exe所在的目录
	// 设置该目录的重要性在于，pfsx-tanslator.dll插件需要根据CurrentDirectory
	// 和ModuleFileDirectory确定相对位置。
	// 正确的CurrentDirectory可以帮助找到pfsx-tanslator.dll。
	CString strModuleDir = GetModuleDir();
	SetCurrentDirectory( strModuleDir );

	strModuleDir = GetInstallDir();
	strModuleDir += L"log\\";
	KHMsgLog_SetFilePathW( strModuleDir);
	PFS::CEnv::SetOutLogInstance( &g_CInstallPFSLog);

	KHMsgLogW( L"App start\n");

	
	// 解析launcher所在的目录：
	// --launcher-folder "launcher"
	// --launcher-folder "launcher_tiyan"
	_TCHAR szFolderBuffer[_MAX_PATH] = {0};
	bool bLauncherFolder = false;
	const _TCHAR* pStrStart = _tcsstr( m_lpCmdLine, L"--launcher-folder \"" );
	if ( pStrStart != NULL )
	{
		pStrStart = pStrStart + wcslen(_T("--launcher-folder \"") );
		const _TCHAR* pStrEnd = _tcsstr( pStrStart, _T("\""));
		if ( pStrEnd != NULL )
		{
			wcsncpy_s( szFolderBuffer, _MAX_PATH, pStrStart, pStrEnd-pStrStart);
			SetLauncherFolder( szFolderBuffer);
			bLauncherFolder = true;
		}
	}
	
	strModuleDir = GetModuleDir();
	strModuleDir += L"\\setting.ini";
	if( bLauncherFolder)
	{
		WritePrivateProfileString( L"setting", L"launcherfolder", szFolderBuffer, strModuleDir);
	}
	else
	{
		GetPrivateProfileString( L"setting", L"launcherfolder", GetPatcherFolder(), 
			szFolderBuffer, MAX_PATH, strModuleDir);
		SetLauncherFolder( szFolderBuffer);
	}

	// 注意，这里必须在设置launcher-folder之后才能初始化，因为GetPatcherTextResourceFile()依赖这个变量

	// 获取用户缺省UI语言
	LANGID langId = GetUserDefaultUILanguage();
	WORD prilangId = PRIMARYLANGID(langId); // 主语言
	// 简体中文语言ID
	LANGID s_chineseLangId = MAKELANGID(LANG_CHINESE, SUBLANG_CHINESE_SIMPLIFIED);

	int iChoice;
	do 
	{
		iChoice = IDOK;
		// 装载文本资源
		// 因为patcher和repair公用很多文本资源，所以将patcher和repair文本资源放在同一个文件
		if( !CTextManager::GetInstance().Init( CTextManager::FILE_XML, GetPatcherTextResourceFile().GetString()))
		{
			CString strErrorMessage;
			strErrorMessage = GetApplicationFileName();
			
			if( langId == s_chineseLangId )// 简体中文版用户UI
			{
				strErrorMessage += L"启动异常！\n文本资源文件丢失或不能被解析！\n请根据指定的文件路径确认该文件所存在的问题：\n\n    ";
			}
			else if ( prilangId == LANG_CHINESE ) // 繁体中文版用户UI
			{
				strErrorMessage += L"啟動異常！\n文本資源文件丟失或不能被解析！\n請根據指定的文件路徑確認該文件所存在的問題：\n\n    ";
			}
			else
			{
				strErrorMessage += L"Exception occurred while startuping!\nThe text resource file is missing or could not be resolved!\nPlease check it manually upon the following path:\n\n    ";
			}

			strErrorMessage += GetPatcherTextResourceFile();

			KHMsgLog_ErrorW( strErrorMessage + L"\n" );
			iChoice = AfxMessageBox( strErrorMessage, MB_ABORTRETRYIGNORE | MB_ICONWARNING );
			// 对于patcher程序来说，缺少文本资源可以允许用户继续执行，
			// 这样才能给用户机会让用户修复不完整的客户端。
			if( iChoice == IDABORT )
			{
				return FALSE;
			}
			// else continue to run;
		}		
		// 允许用户重试
	} while( iChoice == IDRETRY );
	

	// 标准初始化
	// 如果未使用这些功能并希望减小
	// 最终可执行文件的大小，则应移除下列
	// 不需要的特定初始化例程
	// 更改用于存储设置的注册表项
	// TODO: 应适当修改该字符串，
	// 例如修改为公司或组织名
	// SetRegistryKey(_T("应用程序向导生成的本地应用程序"));

	CLauncherDlg dlg;
	m_pMainWnd = &dlg;
	INT_PTR nResponse = dlg.DoModal();
	if (nResponse == IDOK)
	{
		// TODO: 在此处放置处理何时用“确定”来关闭
		//  对话框的代码
	}
	else if (nResponse == IDCANCEL)
	{
		// TODO: 在此放置处理何时用“取消”来关闭
		//  对话框的代码
	}

	// 由于对话框已关闭，所以将返回 FALSE 以便退出应用程序，
	//  而不是启动应用程序的消息泵。

	KHMsgLogW( L"App end\n");
	return FALSE;
}

