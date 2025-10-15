// WinPFS.cpp : 定义应用程序的类行为。
//

#include "stdafx.h"
#include "WinPFS.h"
#include "MainFrm.h"

#include "WinPFSDoc.h"
#include "LeftView.h"
#include <atlbase.h>
#include <statreg.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CWinPFSApp

BEGIN_MESSAGE_MAP(CWinPFSApp, CWinApp)
	ON_COMMAND(ID_APP_ABOUT, &CWinPFSApp::OnAppAbout)
	// 基于文件的标准文档命令
	ON_COMMAND(ID_FILE_NEW, &CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CWinApp::OnFileOpen)
END_MESSAGE_MAP()


// CWinPFSApp 构造

CWinPFSApp::CWinPFSApp()
{
}

// 唯一的一个 CWinPFSApp 对象
CWinPFSApp theApp;


// CWinPFSApp 初始化
BOOL CWinPFSApp::InitInstance()
{
	setlocale(LC_ALL, "chs");
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

	// 初始化 OLE 库
	if (!AfxOleInit())
	{
		AfxMessageBox(IDP_OLE_INIT_FAILED);
		return FALSE;
	}
	AfxEnableControlContainer();
	
	SetRegistryKey(_T("乐动卓越科技有限公司"));
	LoadStdProfileSettings(16);  // 加载标准 INI 文件选项(包括 MRU)
	// 注册应用程序的文档模板。文档模板
	// 将用作文档、框架窗口和视图之间的连接
	CSingleDocTemplate* pDocTemplate;
	pDocTemplate = new CSingleDocTemplate(
		IDR_MAINFRAME,
		RUNTIME_CLASS(CWinPFSDoc),
		RUNTIME_CLASS(CMainFrame),       // 主 SDI 框架窗口
		RUNTIME_CLASS(CLeftView));
	if (!pDocTemplate)
		return FALSE;
	AddDocTemplate(pDocTemplate);

	// 启用“DDE 执行”
	EnableShellOpen();
	//RegisterShellFileTypes(TRUE);

	// 分析标准外壳命令、DDE、打开文件操作的命令行
	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);

	// 下面的方法用于得到程序路径
	TCHAR szPath[ MAX_PATH] = {0};
	TCHAR szModuleFile[ MAX_PATH] = {0};
	::GetModuleFileName( NULL, szPath, MAX_PATH);

	const wchar_t * pStr = _tcsrchr( szPath, _T('\\') ) ;
	if( pStr == NULL )
	{
		pStr = _tcsrchr( szPath, _T('/') ) ;
	}
	if( pStr != NULL )
	{
		wcscpy_s( szModuleFile, MAX_PATH, pStr+1 );
	}
	else
	{
		wcscpy_s( szModuleFile, MAX_PATH, szPath );
	}

	// 使用ATL的对象完成REGISTRY资源文件的注册
	ATL::CRegObject ro;
	if( SUCCEEDED( ro.FinalConstruct() ) )
	{
		ro.AddReplacement( L"MODULE", szPath);
		ro.AddReplacement( L"MODULE_FILE", szModuleFile);
	}

	if( cmdInfo.m_nShellCommand == CCommandLineInfo::AppUnregister )
	{
		if( !SUCCEEDED( ro.ResourceUnregister(szPath, IDR_FILE_TYPE_REG, OLESTR("REGISTRY"))))
		{
			return FALSE;
		}
		return TRUE;
	}
	else
	{
		if( !SUCCEEDED( ro.ResourceRegister(szPath, IDR_FILE_TYPE_REG, OLESTR("REGISTRY"))))
		{
			return FALSE;
		}

		if(cmdInfo.m_nShellCommand == CCommandLineInfo::AppRegister )
		{
			return TRUE;
		}
	}

	// 调度在命令行中指定的命令。如果
	// 用 /RegServer、/Register、/Unregserver 或 /Unregister 启动应用程序，则返回 FALSE。
	if (!ProcessShellCommand(cmdInfo))
		return FALSE;

	// 唯一的一个窗口已初始化，因此显示它并对其进行更新
	m_pMainWnd->ShowWindow(SW_SHOWMAXIMIZED);
	m_pMainWnd->UpdateWindow();
	// 仅当具有后缀时才调用 DragAcceptFiles
	//  在 SDI 应用程序中，这应在 ProcessShellCommand  之后发生
	// 启用拖/放
	m_pMainWnd->DragAcceptFiles();
	return TRUE;
}

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
void CWinPFSApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}


// CWinPFSApp 消息处理程序

