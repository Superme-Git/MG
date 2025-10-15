// WinPFS.cpp : ����Ӧ�ó��������Ϊ��
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
	// �����ļ��ı�׼�ĵ�����
	ON_COMMAND(ID_FILE_NEW, &CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CWinApp::OnFileOpen)
END_MESSAGE_MAP()


// CWinPFSApp ����

CWinPFSApp::CWinPFSApp()
{
}

// Ψһ��һ�� CWinPFSApp ����
CWinPFSApp theApp;


// CWinPFSApp ��ʼ��
BOOL CWinPFSApp::InitInstance()
{
	setlocale(LC_ALL, "chs");
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
	
	SetRegistryKey(_T("�ֶ�׿Խ�Ƽ����޹�˾"));
	LoadStdProfileSettings(16);  // ���ر�׼ INI �ļ�ѡ��(���� MRU)
	// ע��Ӧ�ó�����ĵ�ģ�塣�ĵ�ģ��
	// �������ĵ�����ܴ��ں���ͼ֮�������
	CSingleDocTemplate* pDocTemplate;
	pDocTemplate = new CSingleDocTemplate(
		IDR_MAINFRAME,
		RUNTIME_CLASS(CWinPFSDoc),
		RUNTIME_CLASS(CMainFrame),       // �� SDI ��ܴ���
		RUNTIME_CLASS(CLeftView));
	if (!pDocTemplate)
		return FALSE;
	AddDocTemplate(pDocTemplate);

	// ���á�DDE ִ�С�
	EnableShellOpen();
	//RegisterShellFileTypes(TRUE);

	// ������׼������DDE�����ļ�������������
	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);

	// ����ķ������ڵõ�����·��
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

	// ʹ��ATL�Ķ������REGISTRY��Դ�ļ���ע��
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
	m_pMainWnd->DragAcceptFiles();
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
void CWinPFSApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}


// CWinPFSApp ��Ϣ�������

