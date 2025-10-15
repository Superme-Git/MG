// CheckClient.cpp : Defines the class behaviors for the application.
//

#include "stdafx.h"
#include "CheckClient.h"
#include "CheckClientDlg.h"
#include "../../pfs/src/common.h"
#include "../Patcher/KHLogger.h"
#include "../Patcher/LauncherConfig.h"
#include <strsafe.h>
#include "../Patcher/TextManager.h"
//#include "../Patcher/TextID.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CCheckClientApp

BEGIN_MESSAGE_MAP(CCheckClientApp, CWinApp)
	ON_COMMAND(ID_HELP, &CWinApp::OnHelp)
END_MESSAGE_MAP()


// CCheckClientApp construction

CCheckClientApp::CCheckClientApp()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance
}


// The one and only CCheckClientApp object

CCheckClientApp theApp;

// checkclient 程序位于install directory 的 launcher 子目录下
// 因此，InstallDir为ModuleDir的父目录
// pszApp 返回 checkclient 自己相对于 install directory 的路径
LPCTSTR GetInstallDir( LPCTSTR& pszAppFile)
{
	static TCHAR szLauncherDir[MAX_PATH];
	//if ( szLauncherDir[0] == 0 )
	{
		DWORD dwRet = GetModuleFileName( NULL, szLauncherDir, MAX_PATH);
		LPTSTR lpSep = &szLauncherDir[dwRet-1];
		while( *lpSep != _T('/') && *lpSep != _T('\\') && *lpSep != _T(':') && *lpSep != _T('\0') )
		{
			lpSep --;
		}

		if( *lpSep != _T('\0') )
			lpSep --;

		while( *lpSep != _T('/') && *lpSep != _T('\\') && *lpSep != _T(':') && *lpSep != _T('\0') )
		{
			lpSep --;
		}

		lpSep[0] = _T('\0');
		pszAppFile = &lpSep[1];
	}
	return szLauncherDir;
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


// 获取Repair自己所在的文件夹名字
LPCTSTR GetRepairFolder()
{
	static TCHAR szRepairFolder[MAX_PATH];
	if( szRepairFolder[0] == 0 )
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

		_tcsncpy_s( szRepairFolder, MAX_PATH, lpSepStart, lpSepEnd-lpSepStart );
	}

	return szRepairFolder;
}


// CCheckClientApp initialization

BOOL CCheckClientApp::InitInstance()
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

	LPCTSTR pszAppFile = NULL;
	CString strInstallDir = GetInstallDir( pszAppFile);
	strInstallDir += L"\\log\\";
	KHMsgLog_SetFilePathW( strInstallDir );
	PFS::CEnv::SetOutLogInstance( &g_CInstallPFSLog);

	KHMsgLogW( L"App start\n");

	// Standard initialization
	// If you are not using these features and wish to reduce the size
	// of your final executable, you should remove from the following
	// the specific initialization routines you do not need
	// Change the registry key under which our settings are stored
	// TODO: You should modify this string to be something appropriate
	// such as the name of your company or organization
	//SetRegistryKey(_T("Local AppWizard-Generated Applications"));

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
	
	CString strModuleDir = GetModuleDir();
	strModuleDir += L"\\setting.ini";
	if( !bLauncherFolder )
	{
		GetPrivateProfileString( L"setting", L"launcherfolder", GetRepairFolder(), 
			szFolderBuffer, MAX_PATH, strModuleDir);
		SetLauncherFolder( szFolderBuffer);
	}
	
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
			// 对于repair程序来说，缺少文本资源可以允许用户继续执行，
			// 这样才能给用户机会让用户修复不完整的客户端。
			if( iChoice == IDABORT )
			{
				return FALSE;
			}
			// else continue to run;
		}
		
		// 允许用户重试
	} while( iChoice == IDRETRY );


	CCheckClientDlg dlg;
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
	KHMsgLogW( L"App end\n");
	return FALSE;
}
