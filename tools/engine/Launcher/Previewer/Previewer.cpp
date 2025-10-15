// Previewer.cpp : Defines the class behaviors for the application.
//

#include "stdafx.h"
#include "Previewer.h"
#include "StartupDialog.h"

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


// CPreviewerApp

BEGIN_MESSAGE_MAP(CPreviewerApp, CWinApp)
	ON_COMMAND(ID_HELP, &CWinApp::OnHelp)
END_MESSAGE_MAP()


// CPreviewerApp construction

CPreviewerApp::CPreviewerApp()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance
}


// The one and only CPreviewerApp object

CPreviewerApp theApp;


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

// CPreviewerApp initialization

BOOL CPreviewerApp::InitInstance()
{
	// InitCommonControlsEx() is required on Windows XP if an application
	// manifest specifies use of ComCtl32.dll version 6 or later to enable
	// visual styles.  Otherwise, any window creation will fail.
	INITCOMMONCONTROLSEX InitCtrls;
	InitCtrls.dwSize = sizeof(InitCtrls);
	// Set this to include all the common control classes you want to use
	// in your application.
	InitCtrls.dwICC = ICC_WIN95_CLASSES;
	InitCommonControlsEx(&InitCtrls);

	CWinApp::InitInstance();

	AfxEnableControlContainer();

	// Standard initialization
	// If you are not using these features and wish to reduce the size
	// of your final executable, you should remove from the following
	// the specific initialization routines you do not need
	// Change the registry key under which our settings are stored
	// TODO: You should modify this string to be something appropriate
	// such as the name of your company or organization
	SetRegistryKey(_T("Locojoy"));


	
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

			strErrorMessage += L"\n\nUsage:\n\n"
			L"Put this program on the same directory as the inetutils.dll.\n\n"
			L"In this directory, it should contain a \"launcher\" sub-folder.\n\n"
			L"In the \"launcher\" folder, there should contain a file named \"config.xml\",\n"
			L"and other folders like \"ui\" and \"text\".";

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


	CStartupDialog dlg;
	m_pMainWnd = &dlg;
	INT_PTR nResponse = dlg.DoModal();
	if (nResponse == IDOK)
	{
		// TODO: Place code here to handle when the dialog is
		//  dismissed with OK
	}
	else if (nResponse == IDCANCEL)
	{
		// TODO: Place code here to handle when the dialog is
		//  dismissed with Cancel
	}

	// Since the dialog has been closed, return FALSE so that we exit the
	//  application, rather than start the application's message pump.
	return FALSE;
}
