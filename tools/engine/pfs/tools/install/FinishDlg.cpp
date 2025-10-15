// FinishDlg.cpp : implementation file
//

#include "stdafx.h"
#include "install.h"
#include "FinishDlg.h"
#include "FinishState.h"
#include "const.h"
#include "Config.h"
#include "GlobalError.h"
#include "shlwapi.h"

// CFinishDlg dialog

#define REG_PATH_APPPATH3   _T("Software\\Microsoft\\Windows\\CurrentVersion\\App Paths\\360se 3.exe")
#define REG_PATH_APPPATH2   _T("Software\\Microsoft\\Windows\\CurrentVersion\\App Paths\\360se.exe")
#define REG_PATH_APPPATH5   _T("Software\\Microsoft\\Windows\\CurrentVersion\\App Paths\\360se 5.exe")

IMPLEMENT_DYNAMIC(CFinishDlg, CDialog)

CFinishDlg::CFinishDlg(CWnd* pParent /*=NULL*/)
: CDialog(CFinishDlg::IDD, pParent)
{
}

CFinishDlg::~CFinishDlg()
{
}

void CFinishDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control( pDX, IDC_CHECK_CREATELINK, m_CreateLink);
	DDX_Control( pDX, IDC_CHECK_STARTGAME, m_StartGame);
	DDX_Control(pDX, IDC_STATIC_FINISH, m_finishstate);
	DDX_Control( pDX, IDC_AUTOINSTALL_360, m_AutoInstall);
	DDX_Control( pDX, IDC_INSTALL_UNITY, m_AutoInstallTouch);
}


BEGIN_MESSAGE_MAP(CFinishDlg, CDialog)
END_MESSAGE_MAP()


// CFinishDlg message handlers

void CFinishDlg::OnCancel()
{
}

//Show error message of GetLastError()
bool GetSystemError( std::wstring& res, DWORD dwError)
{
	LPTSTR lpMsgBuf = NULL;
	FormatMessage( FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
		NULL, dwError, MAKELANGID( LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR)&lpMsgBuf, 0, NULL);
	if( lpMsgBuf)
	{
		res = lpMsgBuf;
		LocalFree( lpMsgBuf);
		return true;
	}
	return false;
}

bool GetPFSError( std::wstring& res, int error)
{
	CString str;
	str.Format( CConfig::GetStr( error).c_str(), CGlobalError::GetInstance().GetErrorFile().c_str());
	res = str.GetBuffer();
	return true;
}

bool GetErrorStr( std::wstring& res, int nErrorID)
{
	switch( nErrorID)
	{
	case CInstallState::ERROR_USERCANCEL:
		res = CConfig::GetStr( ERROR_USERSTOP);
		break;
	case CInstallState::ERROR_CHECK:
		res = CConfig::GetStr( ERROR_CHECK);
		break;
	case CInstallState::ERROR_MD5FILE:
		{
			CString str;
			LPCTSTR errorFile = CGlobalError::GetInstance().GetErrorFile().c_str();
			str.Format( CConfig::GetStr( ERROR_MD5FILE_UNFOUND).c_str(), errorFile, errorFile);
			res = str.GetString();
		}
		break;
	case CInstallState::ERROR_NOTSUSPENDOS64:		// %s 暂不支持64位操作系统
		GetPFSError( res, ERROR_NOT_SUSPEND_OS64);
		break;
	case PFS::EC_DIR_HAS_ALREADY_MOUNT:			//	%s 目录已经映射
	case PFS::EC_INVALID_MOUNT_DEVICE:			//	%s 无效的Mount设备
	case PFS::EC_INVALID_DEVICE_NAME:			//	%s 无效的Mount设备名
	case PFS::EC_INVALID_MOUNT_DIR_NAME:		//	%s 无效的Mount目标目录名
	case PFS::EC_FSTYPE_UNEXISTING:				//	%s 文件系统类型不存在
	case PFS::EC_MOUNT_FAILED:					//	%s Mount 文件系统失败
	case PFS::EC_MOUNT_DIR_UNFOUND:				//	%s 映射目录无法找到
	case PFS::EC_FS_BUSY_NOW:					//	%s 文件系统正在被使用
	case PFS::EC_FILE_NOT_MOUNT:				//	%s 映射文件无法找到
	case PFS::EC_FILE_NOT_FOUND:				//	%s 文件无法找到
	case PFS::EC_FS_READONLY:					//	%s 文件系统目前只读
	case PFS::EC_FILE_OPEN_MODE:				//	%s 文件打开方式不支持
	case PFS::EC_NATIVE_DEVICE_LOST:			//	%s 原始文件设备无法找到
	case PFS::EC_COPY_DST_EXISTING:				//	%s 文件拷贝的目标已经存在
	case PFS::EC_MAP_UNFOUND:					//	%s 文件名找不到映射
	case PFS::EC_FILE_BAD_ZIPDATA:				//	%s zip压缩的数据解压缩失败
	case PFS::EC_FILE_READONLY:					//	%s 文件只读
	case PFS::EC_FILE_URLOPENERROR:				//	%s url打开错误
	case PFS::EC_INVALID_VERSION_INFO:			//	%s 无效的版本信息
	case PFS::EC_INVALID_FILENAME:				//	%s 无效的文件名
	case PFS::EC_INVALID_DIRNAME:				//	%s 无效的目录名
	case PFS::EC_DIR_NOT_FOUND:					//	%s 目录不存在(无法删除)
	case PFS::EC_DIR_ALREADY_EXISTING:			//	%s 目录已存在(不能再次创建)
	case PFS::EC_DIR_NOT_EMPTY:					//	%s (不允许删除)非空目录
	case PFS::EC_ACCESS_DENIED:					//	%s 访问被拒绝(权限不足)
	case PFS::EC_SHARING_VIOLATION:				//	%s 访问违例(比如删除一个已打开的文件)
	case PFS::EC_UNMOUNT_FAILED:				//	%s UnMount失败
	case PFS::EC_INVALID_PATCH:					//	%s 无效的补丁包
	case PFS::EC_PATCH_NOT_MATCH:				//	%s 补丁包版本不匹配本地客户端
		GetPFSError( res, ERROR_EC_DIR_HAS_ALREADY_MOUNT + nErrorID - PFS::EC_DIR_HAS_ALREADY_MOUNT);
		break;
	default:
		{
			std::wstring temp;
			if( GetSystemError( temp, nErrorID))
			{
				CString str;
				str.Format( CConfig::GetStr( ERROR_SYSTEMERROR).c_str(), 
					CGlobalError::GetInstance().GetErrorFile().c_str(), temp.c_str());
				res = str.GetBuffer();
			}
		}
	}
	if( res.empty())
	{
		res = CConfig::GetStr( ERROR_UNKNOWN);
	}
	return true;
}

BOOL CFinishDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	SetWindowText( CConfig::GetStr( FINISH).c_str());
	GetDlgItem( IDOK)->SetWindowText( CConfig::GetStr( OK).c_str());

	m_CreateLink.SetWindowTextW(  CConfig::GetStr( CHECKBOX_CREATELINK ).c_str());
	m_CreateLink.SetCheck( BST_CHECKED);
	m_StartGame.SetWindowTextW(  CConfig::GetStr( CHECKBOX_STARTGAME).c_str());
	m_AutoInstall.SetWindowTextW(  CConfig::GetStr( AUTO_INSTALL_360).c_str());
	m_AutoInstall.SetCheck(TRUE);
	if(HaveInstalled360SE(NULL, 0))
		m_AutoInstall.ShowWindow(FALSE);
	m_AutoInstallTouch.SetWindowTextW(  CConfig::GetStr( AUTO_INSTALL_UNITY3D).c_str());
	m_AutoInstallTouch.SetCheck(TRUE);
	char unity_version[32] = {0};
	wcstombs(unity_version, CConfig::GetStr( UNITY3D_VERSION).c_str(), sizeof(unity_version));
	char version[16] = {0};
	if(CheckTouchInstallation(version, sizeof(version)) && !CompareVersion(version, unity_version))
	{
		m_AutoInstallTouch.ShowWindow(FALSE);
	}

	CString str;
	int nErrorID = CGlobalError::GetInstance().GetError();
	if( nErrorID != 0)
	{
		m_CreateLink.EnableWindow( FALSE);
		m_StartGame.EnableWindow( FALSE);
		m_AutoInstallTouch.EnableWindow( FALSE);

		std::wstring temp;
		if( GetErrorStr( temp, nErrorID))
		{
			if( nErrorID != CInstallState::ERROR_USERCANCEL )
				str.Format( CConfig::GetStr( ERRORREASON).c_str(), temp.c_str());
			else
				str = temp.c_str();
		}
		
		if( str.IsEmpty() )
			str = L"安装错误，install.xml文件不存在！";
	}
	else
	{
		str = CConfig::GetStr( INSTALLSUCCESS).c_str();
	}
	m_finishstate.SetWindowText( str);
	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}


void CFinishDlg::OnOK()
{
	OnAutoInstall();
	if( CGlobalError::GetInstance().GetError() == 0)
	{
		OnCreateLink();
		OnStartGame();
	}
	CDialog::OnOK();
	CFinishState::GetInstance().Exit();
}

void CFinishDlg::OnCreateLink()
{
	CConfig::GetInstance().CreateLink( CFinishState::GetInstance().GetInstallPath(), 
		BST_CHECKED == m_CreateLink.GetCheck());
}

void CFinishDlg::OnAutoInstall()
{
	if( m_AutoInstall.IsWindowVisible() && BST_CHECKED == m_AutoInstall.GetCheck())
	{
		std::wstring src = L"360Inst_wanmeihuodong.exe";
		STARTUPINFO startupInfo = {0};
		startupInfo.cb = sizeof(STARTUPINFO);
		startupInfo.wShowWindow = SW_SHOW;

		PROCESS_INFORMATION pi = {0}; 

		if(CreateProcess(	
			src.c_str(), NULL, NULL, NULL, FALSE, 0, 0, 0, &startupInfo, &pi ))
		{
			WaitForSingleObject(pi.hProcess, INFINITE);
			CloseHandle( pi.hThread );
			CloseHandle( pi.hProcess );
			//::DeleteFile(src.c_str());
		}
		else
			MessageBox(L"打开360浏览器安装文件失败！");
	}
	if( m_AutoInstallTouch.IsWindowVisible() && BST_CHECKED == m_AutoInstallTouch.GetCheck())
	{
		std::wstring src = L"UnityWebPlayerDevelopment.exe";
		STARTUPINFO startupInfo = {0};
		startupInfo.cb = sizeof(STARTUPINFO);
		startupInfo.wShowWindow = SW_SHOW;

		PROCESS_INFORMATION pi = {0}; 

		if(CreateProcess(	
			src.c_str(), NULL, NULL, NULL, FALSE, 0, 0, 0, &startupInfo, &pi ))
		{
			WaitForSingleObject(pi.hProcess, INFINITE);
			CloseHandle( pi.hThread );
			CloseHandle( pi.hProcess );
		}
		else
			MessageBox(L"打开Unity3D安装文件失败！");
	}
}

void CFinishDlg::OnStartGame()
{
	if( BST_CHECKED == m_StartGame.GetCheck())
	{
		std::wstring src = CFinishState::GetInstance().GetInstallPath();
		src += L"\\" + CConfig::GetInstance().m_programefile;
		STARTUPINFO startupInfo = {0};
		startupInfo.cb = sizeof(STARTUPINFO);
		startupInfo.wShowWindow = SW_SHOW;

		PROCESS_INFORMATION processInfo = {0}; 

		BOOL bRet =  CreateProcess(	src.c_str(),
			NULL, NULL, NULL, FALSE, 0, 0, 0, &startupInfo, &processInfo );
		if ( bRet )
		{
			CloseHandle( processInfo.hThread );
			CloseHandle( processInfo.hProcess );
		}
	}
}


BOOL CFinishDlg::HaveInstalled360SE(LPTSTR sePath , int maxcount)
{
	BOOL bRet = FALSE;
	TCHAR szPath[MAX_PATH]={0};
	TCHAR szFullName[MAX_PATH]={0};
	DWORD dwType = REG_SZ;
	DWORD dwSize = sizeof(szPath)-1;

	if(ERROR_SUCCESS == SHGetValue(HKEY_LOCAL_MACHINE, REG_PATH_APPPATH3, _T("Path"), &dwType, szPath, &dwSize))
	{   
		_sntprintf_s( szFullName, sizeof(szFullName) - 1, _T("%s\\360se.exe"), szPath);
		if(PathFileExists(szFullName))
		{      
			if (sePath)
				_tcscpy_s(sePath,maxcount,szFullName);
			bRet = TRUE;  
		}
	}

	if( !bRet )
	{
		memset( szPath, 0, sizeof(szPath) );
		dwSize = sizeof(szPath)-1;
		if (ERROR_SUCCESS == SHGetValue(HKEY_LOCAL_MACHINE, REG_PATH_APPPATH2, _T("Path"), &dwType, szPath, &dwSize))
		_sntprintf_s( szFullName, sizeof(szFullName) - 1, _T("%s\\360se3.exe"), szPath);
		if(PathFileExists(szFullName))
		{      
			if (sePath)
				_tcscpy_s(sePath,maxcount,szFullName);
			bRet = TRUE;  
		}
	}

	if( !bRet )
	{
		memset( szPath, 0, sizeof(szPath) );
		dwSize = sizeof(szPath)-1;
		if (ERROR_SUCCESS == SHGetValue(HKEY_LOCAL_MACHINE, REG_PATH_APPPATH5, _T("Path"), &dwType, szPath, &dwSize))
		_sntprintf_s( szFullName, sizeof(szFullName) - 1, _T("%s\\360se5.exe"), szPath);
		if(PathFileExists(szFullName))
		{      
			if (sePath)
				_tcscpy_s(sePath,maxcount,szFullName);
			bRet = TRUE;  
		}
	}

	return bRet;
}

BOOL CFinishDlg::CheckTouchInstallation(char* version, int versionLength)
{
	HKEY keyHandle		= NULL;
	const bool isOpend	= ERROR_SUCCESS == RegOpenKeyExA(HKEY_CURRENT_USER, "Software\\Unity\\WebPlayer", 0, KEY_READ, &keyHandle);
	if (!isOpend)
	{
		return FALSE;
	}

	const int bufferLength	= 256;
	DWORD usedLength;
	char pathBuffer[bufferLength];
	const int retValue	= RegQueryValueExA(keyHandle, "Directory", NULL, NULL, (LPBYTE)pathBuffer, &usedLength);
	RegCloseKey(keyHandle);
	if (retValue != 0)
	{
		return FALSE;
	}

	strcat(pathBuffer, "\\loader\\info.plist");
	FILE* fin	= fopen(pathBuffer, "r");
	if (NULL == fin)
	{
		return FALSE;
	}

	const int passCount	= 4;
	char line[bufferLength];
	for (int i= 0; i< passCount; ++i)
	{
		fgets(line, bufferLength, fin);
	}

	const char* format	= "\t<string>%[1-9a-z.]</string>\n";
	memset(version, 0, versionLength);
	fscanf(fin, format, version);
	fclose(fin);
	return TRUE;
}

bool CFinishDlg::CompareVersion(const char* src, const char* dest)
{
	char ph[8] = {0}, pm[8] = {0}, pl[8] = {0};
	sscanf(src, "%[0-9].%[0-9].%[0-9]", ph, pm, pl);
	int src_h = atoi(ph);
	int src_m = atoi(pm);
	int src_l = atoi(pl);
	memset(ph, 0, 8);
	memset(pm, 0, 8);
	memset(pl, 0, 8);
	sscanf(dest, "%[0-9].%[0-9].%[0-9]", ph, pm, pl);
	int dest_h = atoi(ph);
	int dest_m = atoi(pm);
	int dest_l = atoi(pl);
	return src_h <= dest_h && src_m <= dest_m && src_l < dest_l;
}