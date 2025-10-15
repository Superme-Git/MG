// ParticleSystemEditor.cpp : ����Ӧ�ó��������Ϊ��
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "MainFrm.h"

#include "ChildFrm.h"
#include "ParticleSystemEditorView.h"
#include "ParticleSystemEditorDoc.h"

#include <MMSystem.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CParticleSystemEditorApp

BEGIN_MESSAGE_MAP(CParticleSystemEditorApp, CWinApp)
	ON_COMMAND(ID_APP_ABOUT, &CParticleSystemEditorApp::OnAppAbout)
	// �����ļ��ı�׼�ĵ�����
	ON_COMMAND(ID_FILE_NEW, &CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CWinApp::OnFileOpen)
END_MESSAGE_MAP()


// CParticleSystemEditorApp ����

CParticleSystemEditorApp::CParticleSystemEditorApp()
{
	// TODO: �ڴ˴���ӹ�����룬

	m_pFileIOManager = NULL;
	m_bUpdateParticleSys = false;

	// ��������Ҫ�ĳ�ʼ�������� InitInstance ��
}


// Ψһ��һ�� CParticleSystemEditorApp ����

CParticleSystemEditorApp theApp;


// CParticleSystemEditorApp ��ʼ��

BOOL CParticleSystemEditorApp::InitInstance()
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

	// ��׼��ʼ��
	// ���δʹ����Щ���ܲ�ϣ����С
	// ���տ�ִ���ļ��Ĵ�С����Ӧ�Ƴ�����
	// ����Ҫ���ض���ʼ������
	// �������ڴ洢���õ�ע�����
	// TODO: Ӧ�ʵ��޸ĸ��ַ�����
	// �����޸�Ϊ��˾����֯��
	SetRegistryKey(L"Ӧ�ó��������ɵı���Ӧ�ó���");
	LoadStdProfileSettings(4);  // ���ر�׼ INI �ļ�ѡ��(���� MRU)
	// ע��Ӧ�ó�����ĵ�ģ�塣�ĵ�ģ��
	// �������ĵ�����ܴ��ں���ͼ֮�������
	CMultiDocTemplate* pDocTemplate;
	pDocTemplate = new CMultiDocTemplate(IDR_ParticleSystemETYPE,
		RUNTIME_CLASS(CParticleSystemEditorDoc),
		RUNTIME_CLASS(CChildFrame), // �Զ��� MDI �ӿ��
		RUNTIME_CLASS(CParticleSystemEditorView));
	if (!pDocTemplate)
		return FALSE;
	AddDocTemplate(pDocTemplate);

	// ������ MDI ��ܴ���
	CMainFrame* pMainFrame = new CMainFrame;
	if (!pMainFrame || !pMainFrame->LoadFrame(IDR_MAINFRAME))
	{
		delete pMainFrame;
		return FALSE;
	}
	m_pMainWnd = pMainFrame;
	// �������к�׺ʱ�ŵ��� DragAcceptFiles
	//  �� MDI Ӧ�ó����У���Ӧ������ m_pMainWnd ֮����������

//	HICON hIcon = LoadIcon(IDI_ICON_MYPLUS);
//	pMainFrame->SetIcon(hIcon,TRUE);   

	if(m_pFileIOManager == NULL)
		m_pFileIOManager = new Nuclear::CFileIOManager();
	if( !m_pFileIOManager->Init(true, true) )
	{
		XPALARM(L"PFS·����ʼ��ʧ��");
		return false;
	}

	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	std::wstring wdir = workdir;
	int tmppos = (int)wdir.rfind('\\');
	wdir = std::wstring(wdir, 0, tmppos);
	std::wstring sysFilePath = _T("\\res");
	m_strResPath = wdir.c_str();
	m_strResPath += sysFilePath.c_str();

	// ������׼������DDE�����ļ�������������
	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);

	// ��������������ָ����������
	// �� /RegServer��/Register��/Unregserver �� /Unregister ����Ӧ�ó����򷵻� FALSE��
	if (!ProcessShellCommand(cmdInfo))
		return FALSE;
	// �������ѳ�ʼ���������ʾ����������и���
	pMainFrame->ShowWindow(m_nCmdShow);
	pMainFrame->UpdateWindow();


	return TRUE;
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

// �������жԻ����Ӧ�ó�������
void CParticleSystemEditorApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}

// CParticleSystemEditorApp ��Ϣ�������

BOOL CParticleSystemEditorApp::OnIdle(LONG lCount)
{
	// TODO: �ڴ����ר�ô����/����û���
	CMainFrame* pwnd = (CMainFrame*)AfxGetMainWnd();
	CFrameWnd* pchildWnd = pwnd->GetActiveFrame(); 
	CDocument* pCurrentDoc = pchildWnd->GetActiveDocument();

	if( (pCurrentDoc != NULL) ) //&&(lCount>5)
	{
		((CParticleSystemEditorDoc*)pCurrentDoc)->GetParticleSystemEditorView()->Render();
	}
	
	return CWinApp::OnIdle(lCount);
}

int CParticleSystemEditorApp::ExitInstance()
{
	// TODO: �ڴ����ר�ô����/����û���

	if(m_pFileIOManager != NULL)
	{
		m_pFileIOManager->Destroy();
		delete m_pFileIOManager;
		m_pFileIOManager = NULL;
	}

	return CWinApp::ExitInstance();
}


const wchar_t* XL_PSLINFO::fn()
{
	static const CString str = theApp.GetResPath() + L"\\pslinfo.log"; 
	return str;
}