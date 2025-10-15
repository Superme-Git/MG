// SoundEditor.cpp : ����Ӧ�ó��������Ϊ��
//
#include "stdafx.h"
#include <string>
#include "SoundEditor.h"
#include "MainFrm.h"
#include "CrashDump.h"
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
{
	CrashDump::CrashDump_Init( L"SoundEditor.dmp", L"", static_cast<CrashDump::eDumpLevel>(3));
	srand(GetTickCount());
	
	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	std::wstring wdir = workdir;
	int tmppos = (int)wdir.rfind('\\');
	wdir = std::wstring(wdir, 0, tmppos);
	std::wstring sysFilePath = _T("\\res");
	std::wstring strTemp = wdir + sysFilePath;
	m_stringPath = strTemp.c_str();

	int mPos=m_stringPath.ReverseFind('\\'); 

	setlocale(LC_ALL, "chinese_china");

	if(mPos==-1) 
	{
		m_stringPath = "";
		return;	//û�з���Ŀ¼��������̫���ܰɡ�������- -b��
	}


	CString stepPath = m_stringPath + L"\\sound\\step\\";

	DWORD dwAttr = GetFileAttributes(stepPath.GetBuffer());
	if (dwAttr == -1 || (dwAttr & FILE_ATTRIBUTE_DIRECTORY) == 0 )	//�ļ��в�����
	{
		CreateDirectory(stepPath.GetBuffer(), NULL);
	}
	
	ResetMazeColors();
}

CSoundEditorApp::~CSoundEditorApp()
{
	CrashDump::CrashDump_Clean();
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
	return TRUE;
}

void CSoundEditorApp::EnableSecToolBar(bool b)
{
	dynamic_cast<CMainFrame*>(m_pMainWnd)->EnableSecToolBar(b);
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

void CSoundEditorApp::SaveMazeColors(const Nuclear::XPMazeColors &mcs, size_t oldcolorcount)
{
	Nuclear::XPMazeColors::const_iterator it = mcs.begin(), ie = mcs.end();
	size_t i = 0;
	CString key, value;
	for (;it!=ie;++it,++i)
	{
		key.Format(L"maze%d", i);
		value.Format(L"0x%X,%X", it->mask, it->color.data);
		WritePrivateProfileString(L"MazeColors", key.GetBuffer(), value.GetBuffer(), theApp.GetCfgFilePath());
		key.ReleaseBuffer();
		value.ReleaseBuffer();
	}
	while (i<oldcolorcount)//ɾ�������
	{
		key.Format(L"maze%d", i);
		WritePrivateProfileString(L"MazeColors", key.GetBuffer(), NULL, theApp.GetCfgFilePath());
		key.ReleaseBuffer();
		++i;
	}
}

void CSoundEditorApp::LoadMazeColors(Nuclear::XPMazeColors &mcs)
{
	mcs.clear();
	wchar_t NameBuffer[5000];
	GetPrivateProfileSection(L"MazeColors", NameBuffer, 5000, theApp.GetCfgFilePath());
	wchar_t *p = NameBuffer, *endP;
	Nuclear::XPMazeColor mc;
	while (*p != L'\0')
	{
		while (*(p++) != L'=');
		mc.mask = wcstoul(p, &endP, 16);
		p = endP + 1;
		mc.color.data = wcstoul(p, &endP, 16);
		p = endP + 1;
		mcs.push_back(mc);
	}
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()

// �������жԻ����Ӧ�ó�������
void CSoundEditorApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}


// CSoundEditorApp ��Ϣ�������

