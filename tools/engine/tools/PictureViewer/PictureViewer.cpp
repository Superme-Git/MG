// PictureViewer.cpp : ����Ӧ�ó��������Ϊ��
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


// CPictureViewerApp ����

CPictureViewerApp::CPictureViewerApp()
{
	// TODO: �ڴ˴���ӹ�����룬
	// ��������Ҫ�ĳ�ʼ�������� InitInstance ��
}


// Ψһ��һ�� CPictureViewerApp ����

CPictureViewerApp theApp;


// CPictureViewerApp ��ʼ��

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

	// ����ķ������ڵõ�ShortPath
	CString strPathName;
	AfxGetModuleShortFileName(AfxGetInstanceHandle(), strPathName);
	

	//����ķ������Եõ�FullPath
	//DWORD dwLen = GetModuleFileName( m_hInstance, strPathName.GetBufferSetLength(MAX_PATH), MAX_PATH );
	//strPathName.ReleaseBuffer(dwLen);

	// ʹ��ATL�Ķ������REGISTRY��Դ�ļ���ע��
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

	// ��׼��ʼ��
	// ���δʹ����Щ���ܲ�ϣ����С
	// ���տ�ִ���ļ��Ĵ�С����Ӧ�Ƴ�����
	// ����Ҫ���ض���ʼ������
	// �������ڴ洢���õ�ע�����
	// TODO: Ӧ�ʵ��޸ĸ��ַ�����
	// �����޸�Ϊ��˾����֯��
	// SetRegistryKey(_T("Ӧ�ó��������ɵı���Ӧ�ó���"));
	// ��Ҫ���������ڣ��˴��뽫�����µĿ�ܴ���
	// ����Ȼ��������ΪӦ�ó���������ڶ���
	CMainFrame* pFrame = new CMainFrame;
	if (!pFrame)
		return FALSE;
	m_pMainWnd = pFrame;
	// ���������ؿ�ܼ�����Դ
	pFrame->LoadFrame(IDR_MAINFRAME,
		WS_OVERLAPPEDWINDOW | FWS_ADDTOTITLE, NULL,
		NULL);

	// Ψһ��һ�������ѳ�ʼ���������ʾ����������и���
	pFrame->ShowWindow(SW_SHOW);
	pFrame->UpdateWindow();

	// �������к�׺ʱ�ŵ��� DragAcceptFiles
	//  �� SDI Ӧ�ó����У���Ӧ�� ProcessShellCommand  ֮����

	return TRUE;
}

// CPictureViewerApp ��Ϣ�������

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
void CPictureViewerApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}

// CPictureViewerApp ��Ϣ�������
