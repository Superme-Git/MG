// SoundEditor.cpp : 定义应用程序的类行为。
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "MainFrm.h"

#include "SoundEditorDoc.h"
#include "SoundEditorView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CSoundEditorApp

BEGIN_MESSAGE_MAP(CSoundEditorApp, CWinApp)
	ON_COMMAND(ID_APP_ABOUT, &CSoundEditorApp::OnAppAbout)
	// 基于文件的标准文档命令
	ON_COMMAND(ID_FILE_NEW, &CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CWinApp::OnFileOpen)
	// 标准打印设置命令
	ON_COMMAND(ID_FILE_PRINT_SETUP, &CWinApp::OnFilePrintSetup)
END_MESSAGE_MAP()


// CSoundEditorApp 构造

CSoundEditorApp::CSoundEditorApp()
: m_pFileIOMan(NULL), 
  m_pPathMap(NULL)
{
	srand(GetTickCount());
	/*TCHAR exeFullName[MAX_PATH];
	GetModuleFileName(NULL, exeFullName, MAX_PATH);
	m_stringPath = exeFullName;

	int mPos=m_stringPath.ReverseFind('\\'); 

	setlocale(LC_ALL, "chinese_china");

	if(mPos==-1) 
	{
		m_stringPath = "";
		return;	//没有发现目录。。（不太可能吧。。。。- -b）
	}

	m_stringPath=m_stringPath.Left(mPos);
	if(m_stringPath.Right(1)=="\\")
	{
		m_stringPath=m_stringPath.Left(mPos-1);
	}*/

	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	
	std::wstring wdir = workdir;
	int npos = (int)wdir.rfind('\\');
	wdir = std::wstring(wdir, 0, npos);
	std::wstring sysFilePath = _T("\\res");
	std::wstring strTemp = wdir + sysFilePath;

	m_stringPath = strTemp.c_str();
}


// 唯一的一个 CSoundEditorApp 对象

CSoundEditorApp theApp;


// CSoundEditorApp 初始化

BOOL CSoundEditorApp::InitInstance()
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

	// 初始化 OLE 库
	if (!AfxOleInit())
	{
		AfxMessageBox(IDP_OLE_INIT_FAILED);
		return FALSE;
	}
	AfxEnableControlContainer();
	// 标准初始化
	// 如果未使用这些功能并希望减小
	// 最终可执行文件的大小，则应移除下列
	// 不需要的特定初始化例程
	// 更改用于存储设置的注册表项
	// TODO: 应适当修改该字符串，
	// 例如修改为公司或组织名
	SetRegistryKey(_T("应用程序向导生成的本地应用程序"));
	LoadStdProfileSettings(4);  // 加载标准 INI 文件选项(包括 MRU)
	// 注册应用程序的文档模板。文档模板
	// 将用作文档、框架窗口和视图之间的连接
	CSingleDocTemplate* pDocTemplate;
	pDocTemplate = new CSingleDocTemplate(
		IDR_MAINFRAME,
		RUNTIME_CLASS(CSoundEditorDoc),
		RUNTIME_CLASS(CMainFrame),       // 主 SDI 框架窗口
		RUNTIME_CLASS(CSoundEditorView));
	if (!pDocTemplate)
		return FALSE;
	AddDocTemplate(pDocTemplate);


	// 启用“DDE 执行”
	EnableShellOpen();
	RegisterShellFileTypes(TRUE);

	// 分析标准外壳命令、DDE、打开文件操作的命令行
	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);


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
	//m_pMainWnd->DragAcceptFiles();

	PFS::CEnv::SetOutLogInstance(&m_pfsLog);

	return TRUE;
}

void CSoundEditorApp::EnableSecToolBar(bool b)
{
	dynamic_cast<CMainFrame*>(m_pMainWnd)->EnableSecToolBar(b);
}

int CSoundEditorApp::ExitInstance()
{
	if (m_pPathMap)
	{
		m_pPathMap->Save();
		delete m_pPathMap;
	}

	if( m_pFileIOMan )
	{
		m_pFileIOMan->Destroy();
		delete m_pFileIOMan;
	}
	return CWinApp::ExitInstance();
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
public:
	afx_msg void OnBnClickedNpcOk();
	afx_msg void OnBnClickedNpcSave();
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	ON_BN_CLICKED(ID_NPC_OK, &CAboutDlg::OnBnClickedNpcOk)
	ON_BN_CLICKED(ID_NPC_SAVE, &CAboutDlg::OnBnClickedNpcSave)
END_MESSAGE_MAP()

// 用于运行对话框的应用程序命令
void CSoundEditorApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}


// CSoundEditorApp 消息处理程序


void CAboutDlg::OnBnClickedNpcOk()
{
	// TODO: Add your control notification handler code here
}

void CAboutDlg::OnBnClickedNpcSave()
{
	// TODO: Add your control notification handler code here
}
