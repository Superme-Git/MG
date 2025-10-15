// Launcher.cpp : ����Ӧ�ó��������Ϊ��
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


// CLauncherApp ����

CLauncherApp::CLauncherApp()
{
	// TODO: �ڴ˴���ӹ�����룬
	// ��������Ҫ�ĳ�ʼ�������� InitInstance ��
}


// Ψһ��һ�� CLauncherApp ����

CLauncherApp theApp;

//
// �жϽ����Ƿ���LauncherΪͬһ�ݰ�װ��
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

	// TODO: �Ƿ��б�Ҫʹ�� PathCommonPrefix() ȷ��·����
	return FALSE;
}

//
// �жϴ����Ƿ���LauncherΪͬһ�ݰ�װ��
//
BOOL IsSameInstallationWindow( HWND hWnd )
{
	// ��ȡ���ڵĽ���ID
	DWORD dwProcessId = 0;
	::GetWindowThreadProcessId( hWnd, &dwProcessId );

	// �򿪽���
	HANDLE hProcess = OpenProcess( PROCESS_QUERY_INFORMATION|PROCESS_VM_READ, FALSE, dwProcessId );
	if ( hProcess == NULL )
	{
		return FALSE;
	}

	// �жϽ����Ƿ���launcher.exe��ͬһ����װĿ¼��
	BOOL bIsSameInstallation = IsSameInstallationProcess( hProcess );
	CloseHandle( hProcess );
	return bIsSameInstallation;
}


BOOL CLauncherApp::InitApplication()
{
	//////////////////////////////////////////////////////////////////////////
	//
	// ����ͨ��������ȷ����ʵ������Launcher����(������Debug/Release�汾)
	//
	// �������Ĵ�����Ҫ�޸ģ���ע�Ᵽ����Bootstrap.cpp����ͬ����
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


// ��ȡPatcher�Լ����ڵ��ļ�������
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

// CLauncherApp ��ʼ��

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
	// ���һ�������� Windows XP �ϵ�Ӧ�ó����嵥ָ��Ҫ
	// ʹ�� ComCtl32.dll �汾 6 ����߰汾�����ÿ��ӻ���ʽ��
	//����Ҫ InitCommonControlsEx()�����򣬽��޷��������ڡ�
	INITCOMMONCONTROLSEX InitCtrls;
	InitCtrls.dwSize = sizeof(InitCtrls);
	// ��������Ϊ��������Ҫ��Ӧ�ó�����ʹ�õ�
	// �����ؼ��ࡣ
	InitCtrls.dwICC = ICC_WIN95_CLASSES;
	InitCommonControlsEx(&InitCtrls);

	CWinApp::InitInstance();

	AfxEnableControlContainer();

	// Launcher������ʱĿ¼Ϊ�丸Ŀ¼����GameClient.exe���ڵ�Ŀ¼
	// ���ø�Ŀ¼����Ҫ�����ڣ�pfsx-tanslator.dll�����Ҫ����CurrentDirectory
	// ��ModuleFileDirectoryȷ�����λ�á�
	// ��ȷ��CurrentDirectory���԰����ҵ�pfsx-tanslator.dll��
	CString strModuleDir = GetModuleDir();
	SetCurrentDirectory( strModuleDir );

	strModuleDir = GetInstallDir();
	strModuleDir += L"log\\";
	KHMsgLog_SetFilePathW( strModuleDir);
	PFS::CEnv::SetOutLogInstance( &g_CInstallPFSLog);

	KHMsgLogW( L"App start\n");

	
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

	// ע�⣬�������������launcher-folder֮����ܳ�ʼ������ΪGetPatcherTextResourceFile()�����������

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
			// ����patcher������˵��ȱ���ı���Դ���������û�����ִ�У�
			// �������ܸ��û��������û��޸��������Ŀͻ��ˡ�
			if( iChoice == IDABORT )
			{
				return FALSE;
			}
			// else continue to run;
		}		
		// �����û�����
	} while( iChoice == IDRETRY );
	

	// ��׼��ʼ��
	// ���δʹ����Щ���ܲ�ϣ����С
	// ���տ�ִ���ļ��Ĵ�С����Ӧ�Ƴ�����
	// ����Ҫ���ض���ʼ������
	// �������ڴ洢���õ�ע�����
	// TODO: Ӧ�ʵ��޸ĸ��ַ�����
	// �����޸�Ϊ��˾����֯��
	// SetRegistryKey(_T("Ӧ�ó��������ɵı���Ӧ�ó���"));

	CLauncherDlg dlg;
	m_pMainWnd = &dlg;
	INT_PTR nResponse = dlg.DoModal();
	if (nResponse == IDOK)
	{
		// TODO: �ڴ˴����ô����ʱ�á�ȷ�������ر�
		//  �Ի���Ĵ���
	}
	else if (nResponse == IDCANCEL)
	{
		// TODO: �ڴ˷��ô����ʱ�á�ȡ�������ر�
		//  �Ի���Ĵ���
	}

	// ���ڶԻ����ѹرգ����Խ����� FALSE �Ա��˳�Ӧ�ó���
	//  ����������Ӧ�ó������Ϣ�á�

	KHMsgLogW( L"App end\n");
	return FALSE;
}

