// PictureViewer.cpp : 定义应用程序的类行为。
//

#include "stdafx.h"
#include "PictureViewer.h"
#include "MainFrm.h"
#include <atlbase.h>
#include <statreg.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CPictureViewerApp

BEGIN_MESSAGE_MAP(CPictureViewerApp, CWinApp)
	ON_COMMAND(ID_APP_ABOUT, &CPictureViewerApp::OnAppAbout)
END_MESSAGE_MAP()


// CPictureViewerApp 构造

CPictureViewerApp::CPictureViewerApp()
{
	// TODO: 在此处添加构造代码，
	// 将所有重要的初始化放置在 InitInstance 中
}


// 唯一的一个 CPictureViewerApp 对象

CPictureViewerApp theApp;


// CPictureViewerApp 初始化

#ifdef _AFXDLL

// $(VCInstallDir)VC\atlmfc\src\mfc\filecore.cpp
void AFXAPI AfxGetModuleShortFileName(HINSTANCE hInst, CString& strShortName)
{
	TCHAR szLongPathName[_MAX_PATH];
	::GetModuleFileName(hInst, szLongPathName, _MAX_PATH);
	if (::GetShortPathName(szLongPathName,
		strShortName.GetBuffer(_MAX_PATH), _MAX_PATH) == 0)
	{
		// rare failure case (especially on not-so-modern file systems)
		strShortName = szLongPathName;
	}
	strShortName.ReleaseBuffer();
}
#else
extern void AFXAPI AfxGetModuleShortFileName(HINSTANCE hInst, CString& strShortName);
#endif

BOOL CPictureViewerApp::InitInstance()
{
	CWinApp::InitInstance();

	// 下面的方法用于得到ShortPath
	CString strPathName;
	AfxGetModuleShortFileName(AfxGetInstanceHandle(), strPathName);
	

	//下面的方法可以得到FullPath
	//DWORD dwLen = GetModuleFileName( m_hInstance, strPathName.GetBufferSetLength(MAX_PATH), MAX_PATH );
	//strPathName.ReleaseBuffer(dwLen);

	// 使用ATL的对象完成REGISTRY资源文件的注册
	ATL::CRegObject ro;
	if( SUCCEEDED( ro.FinalConstruct() ) )
	{
		ro.AddReplacement( L"MODULE", strPathName);

		//
		// replacement for different operating systems:
		//
		//%DEFAULT_ICON%
		//val DefaultIcon = 
		//	s '%systemroot%\system32\imageres.dll,-71'
		//	s 'shimgvw.dll,4'
		//
		//%MUI_VERB%
		// val MuiVerb = 
		//	s '@%ProgramFiles%\Windows Photo Viewer\photoviewer.dll,-3043'
		//	s '@shimgvw.dll,-550'
		//

		OSVERSIONINFO vi = {0};
		vi.dwOSVersionInfoSize = sizeof(OSVERSIONINFO);
		::GetVersionEx( &vi );
		CString strIconResModuleName;
		CString strMuiVerb;
		if( vi.dwMajorVersion >= 6 )
		{
			strIconResModuleName = _T("%systemroot%\\system32\\imageres.dll,-71");
			strMuiVerb = _T("@%ProgramFiles%\\Windows Photo Viewer\\photoviewer.dll,-3043");
		}
		else
		{
			strIconResModuleName = _T("%systemroot%\\system32\\shimgvw.dll,4");
			strMuiVerb = _T("@shimgvw.dll,-550");
		}

		ro.AddReplacement( L"DEFAULT_ICON", strIconResModuleName);
		ro.AddReplacement( L"MUI_VERB", strMuiVerb);
	}

	CCommandLineInfo cmdInfo;
	ParseCommandLine( cmdInfo );

	if( cmdInfo.m_nShellCommand == CCommandLineInfo::AppUnregister )
	{
		if( !SUCCEEDED( ro.ResourceUnregister( strPathName, IDR_MAINFRAME, L"REGISTRY") ) )
		{
			return FALSE;
		}
		return TRUE;
	}
	else
	{
		if( !SUCCEEDED( ro.ResourceRegister( strPathName, IDR_MAINFRAME, L"REGISTRY") ) )
		{
			return FALSE;
		}

		if( cmdInfo.m_nShellCommand == CCommandLineInfo::AppRegister )
		{
			return TRUE;
		}
	}

	SetRegistryKey( _T("LocoJoy"));
	LoadStdProfileSettings(10);

	// 标准初始化
	// 如果未使用这些功能并希望减小
	// 最终可执行文件的大小，则应移除下列
	// 不需要的特定初始化例程
	// 更改用于存储设置的注册表项
	// TODO: 应适当修改该字符串，
	// 例如修改为公司或组织名
	// SetRegistryKey(_T("应用程序向导生成的本地应用程序"));
	// 若要创建主窗口，此代码将创建新的框架窗口
	// 对象，然后将其设置为应用程序的主窗口对象
	CMainFrame* pFrame = new CMainFrame;
	if (!pFrame)
		return FALSE;
	m_pMainWnd = pFrame;
	// 创建并加载框架及其资源
	pFrame->LoadFrame(IDR_MAINFRAME,
		WS_OVERLAPPEDWINDOW | FWS_ADDTOTITLE, NULL,
		NULL);

	// 唯一的一个窗口已初始化，因此显示它并对其进行更新
	pFrame->ShowWindow(SW_SHOW);
	pFrame->UpdateWindow();

	// 仅当具有后缀时才调用 DragAcceptFiles
	//  在 SDI 应用程序中，这应在 ProcessShellCommand  之后发生

	return TRUE;
}

// CPictureViewerApp 消息处理程序

// 用于应用程序“关于”菜单项的 CAboutDlg 对话框

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// 对话框数据
	enum { IDD = IDD_ABOUTBOX };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

// 实现
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()

// 用于运行对话框的应用程序命令
void CPictureViewerApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}

// CPictureViewerApp 消息处理程序
