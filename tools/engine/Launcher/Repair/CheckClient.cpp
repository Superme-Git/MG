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

// checkclient ����λ��install directory �� launcher ��Ŀ¼��
// ��ˣ�InstallDirΪModuleDir�ĸ�Ŀ¼
// pszApp ���� checkclient �Լ������ install directory ��·��
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

// ע��PFS��log
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


// ��ȡRepair�Լ����ڵ��ļ�������
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

	// ����launcher���ڵ�Ŀ¼��
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
	
	// ��ȡ�û�ȱʡUI����
	LANGID langId = GetUserDefaultUILanguage();
	WORD prilangId = PRIMARYLANGID(langId); // ������
	// ������������ID
	LANGID s_chineseLangId = MAKELANGID(LANG_CHINESE, SUBLANG_CHINESE_SIMPLIFIED);

	int iChoice;
	do 
	{
		iChoice = IDOK;
		// װ���ı���Դ
		// ��Ϊpatcher��repair���úܶ��ı���Դ�����Խ�patcher��repair�ı���Դ����ͬһ���ļ�
		if( !CTextManager::GetInstance().Init( CTextManager::FILE_XML, GetPatcherTextResourceFile().GetString()))
		{
			CString strErrorMessage;
			strErrorMessage = GetApplicationFileName();
			
			if( langId == s_chineseLangId )// �������İ��û�UI
			{
				strErrorMessage += L"�����쳣��\n�ı���Դ�ļ���ʧ���ܱ�������\n�����ָ�����ļ�·��ȷ�ϸ��ļ������ڵ����⣺\n\n    ";
			}
			else if ( prilangId == LANG_CHINESE ) // �������İ��û�UI
			{
				strErrorMessage += L"���Ӯ�����\n�ı��YԴ�ļ��Gʧ���ܱ�������\nՈ����ָ�����ļ�·���_�Jԓ�ļ������ڵĆ��}��\n\n    ";
			}
			else
			{
				strErrorMessage += L"Exception occurred while startuping!\nThe text resource file is missing or could not be resolved!\nPlease check it manually upon the following path:\n\n    ";
			}

			strErrorMessage += GetPatcherTextResourceFile();

			KHMsgLog_ErrorW( strErrorMessage + L"\n" );
			iChoice = AfxMessageBox( strErrorMessage, MB_ABORTRETRYIGNORE | MB_ICONWARNING );	
			// ����repair������˵��ȱ���ı���Դ���������û�����ִ�У�
			// �������ܸ��û��������û��޸��������Ŀͻ��ˡ�
			if( iChoice == IDABORT )
			{
				return FALSE;
			}
			// else continue to run;
		}
		
		// �����û�����
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
