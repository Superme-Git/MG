// ImageEditor.cpp : ����Ӧ�ó��������Ϊ��
//

#include "stdafx.h"
#include "ImageEditor.h"
#include "MainFrm.h"

#include "ChildFrm.h"
#include "ImageEditorDoc.h"
#include "ImageEditorView.h"

#include "..\engine\common\log.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CImageEditorApp

BEGIN_MESSAGE_MAP(CImageEditorApp, CWinApp)
	ON_COMMAND(ID_APP_ABOUT, &CImageEditorApp::OnAppAbout)
	// �����ļ��ı�׼�ĵ�����
	ON_COMMAND(ID_FILE_NEW, &CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CWinApp::OnFileOpen)
	// ��׼��ӡ��������
	ON_COMMAND(ID_FILE_PRINT_SETUP, &CWinApp::OnFilePrintSetup)
END_MESSAGE_MAP()


// CImageEditorApp ����

CImageEditorApp::CImageEditorApp()
{
	// TODO: �ڴ˴���ӹ�����룬
	// ��������Ҫ�ĳ�ʼ�������� InitInstance ��

	m_strResPath = L"";
	m_pFileIOManager = NULL;
}


// Ψһ��һ�� CImageEditorApp ����

CImageEditorApp theApp;


// CImageEditorApp ��ʼ��

BOOL CImageEditorApp::InitInstance()
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
	CMultiDocTemplate* pDocTemplate;
	pDocTemplate = new CMultiDocTemplate(IDR_ImageEditorTYPE,
		RUNTIME_CLASS(CImageEditorDoc),
		RUNTIME_CLASS(CChildFrame), // �Զ��� MDI �ӿ��
		RUNTIME_CLASS(CImageEditorView));
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


	if(m_pFileIOManager == NULL)
		m_pFileIOManager = new Nuclear::CFileIOManager();
	if( !m_pFileIOManager->Init(true, true) )
	{
		XPALARM(L"PFS·����ʼ��ʧ��");
		return false;
	}

	//CString filePath = L"";  
	//GetModuleFileName(NULL, filePath.GetBufferSetLength(MAX_PATH+1), MAX_PATH);  
	//filePath.ReleaseBuffer();
	//int npos = filePath.ReverseFind('\\');
	//filePath = filePath.Left(npos);  

	//if(filePath.Right(1) != L"\\")
	//	filePath += L"\\";

	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	std::wstring wdir = workdir;
	int tmppos = (int)wdir.rfind('\\');
	wdir = std::wstring(wdir, 0, tmppos);
	std::wstring sysFilePath = _T("\\res");
	std::wstring strTemp = wdir + sysFilePath;

	m_strResPath = strTemp.c_str();

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
void CImageEditorApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}


// CImageEditorApp ��Ϣ�������


int CImageEditorApp::ExitInstance()
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

BOOL CImageEditorApp::OnIdle(LONG lCount)
{
	// TODO: �ڴ����ר�ô����/����û���

	CMainFrame* pwnd = (CMainFrame*)AfxGetMainWnd();
	CFrameWnd* pchildWnd = pwnd->GetActiveFrame(); 
	CDocument* pCurrentDoc = pchildWnd->GetActiveDocument();

	if(pCurrentDoc != NULL)
		((CImageEditorDoc*)pCurrentDoc)->GetImageEditorView()->Render();

	return CWinApp::OnIdle(lCount);
}
