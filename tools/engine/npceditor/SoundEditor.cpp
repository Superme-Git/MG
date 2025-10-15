// SoundEditor.cpp : ����Ӧ�ó��������Ϊ��
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
	// �����ļ��ı�׼�ĵ�����
	ON_COMMAND(ID_FILE_NEW, &CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CWinApp::OnFileOpen)
	// ��׼��ӡ��������
	ON_COMMAND(ID_FILE_PRINT_SETUP, &CWinApp::OnFilePrintSetup)
END_MESSAGE_MAP()


// CSoundEditorApp ����

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
		return;	//û�з���Ŀ¼��������̫���ܰɡ�������- -b��
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


// Ψһ��һ�� CSoundEditorApp ����

CSoundEditorApp theApp;


// CSoundEditorApp ��ʼ��

BOOL CSoundEditorApp::InitInstance()
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

	// ��ʼ�� OLE ��
	if (!AfxOleInit())
	{
		AfxMessageBox(IDP_OLE_INIT_FAILED);
		return FALSE;
	}
	AfxEnableControlContainer();
	// ��׼��ʼ��
	// ���δʹ����Щ���ܲ�ϣ����С
	// ���տ�ִ���ļ��Ĵ�С����Ӧ�Ƴ�����
	// ����Ҫ���ض���ʼ������
	// �������ڴ洢���õ�ע�����
	// TODO: Ӧ�ʵ��޸ĸ��ַ�����
	// �����޸�Ϊ��˾����֯��
	SetRegistryKey(_T("Ӧ�ó��������ɵı���Ӧ�ó���"));
	LoadStdProfileSettings(4);  // ���ر�׼ INI �ļ�ѡ��(���� MRU)
	// ע��Ӧ�ó�����ĵ�ģ�塣�ĵ�ģ��
	// �������ĵ�����ܴ��ں���ͼ֮�������
	CSingleDocTemplate* pDocTemplate;
	pDocTemplate = new CSingleDocTemplate(
		IDR_MAINFRAME,
		RUNTIME_CLASS(CSoundEditorDoc),
		RUNTIME_CLASS(CMainFrame),       // �� SDI ��ܴ���
		RUNTIME_CLASS(CSoundEditorView));
	if (!pDocTemplate)
		return FALSE;
	AddDocTemplate(pDocTemplate);


	// ���á�DDE ִ�С�
	EnableShellOpen();
	RegisterShellFileTypes(TRUE);

	// ������׼������DDE�����ļ�������������
	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);


	// ��������������ָ����������
	// �� /RegServer��/Register��/Unregserver �� /Unregister ����Ӧ�ó����򷵻� FALSE��
	if (!ProcessShellCommand(cmdInfo))
		return FALSE;

	// Ψһ��һ�������ѳ�ʼ���������ʾ����������и���
	m_pMainWnd->ShowWindow(SW_SHOWMAXIMIZED);
	m_pMainWnd->UpdateWindow();
	// �������к�׺ʱ�ŵ��� DragAcceptFiles
	//  �� SDI Ӧ�ó����У���Ӧ�� ProcessShellCommand  ֮����
	// ������/��
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


// ����Ӧ�ó��򡰹��ڡ��˵���� CAboutDlg �Ի���

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// �Ի�������
	enum { IDD = IDD_ABOUTBOX };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

// ʵ��
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

// �������жԻ����Ӧ�ó�������
void CSoundEditorApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}


// CSoundEditorApp ��Ϣ�������


void CAboutDlg::OnBnClickedNpcOk()
{
	// TODO: Add your control notification handler code here
}

void CAboutDlg::OnBnClickedNpcSave()
{
	// TODO: Add your control notification handler code here
}
