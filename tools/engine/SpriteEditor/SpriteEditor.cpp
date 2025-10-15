

#include "stdafx.h"
#include <string>
#include "SpriteEditor.h"
#include "MainFrm.h"
#include "CrashDump.h"
#include "SpriteEditorDoc.h"
#include "SpriteEditorView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif




const wchar_t* sXlXapInfo::fn()
{
	static const CString str = theApp.GetExePath() + L"\\xapinfo.log"; 
	return str;
}
BOOL CSpriteEditorApp::OnIdle(LONG lCount)
{
	if( m_pView ) m_pView->Render();
	return CWinApp::OnIdle(lCount);
}

bool CSpriteEditorApp::OnFrameClose()
{
	if( m_pView )
	{
		CSpriteEditorDoc *pDoc = (CSpriteEditorDoc*)m_pView->GetDocument();
		if( pDoc )
			if( !pDoc->Save(true, true, -1))
				return false;
	}
	return true;
}

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();


	enum { IDD = IDD_ABOUTBOX };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    


protected:
	DECLARE_MESSAGE_MAP()
};

void CSpriteEditorApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}


BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()


void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

BOOL CSpriteEditorApp::InitInstance()
{

	INITCOMMONCONTROLSEX InitCtrls;
	InitCtrls.dwSize = sizeof(InitCtrls);

	InitCtrls.dwICC = ICC_WIN95_CLASSES;
	InitCommonControlsEx(&InitCtrls);

	CWinApp::InitInstance();


	SetRegistryKey(_T("应用程序向导生成的本地应用程序"));
	LoadStdProfileSettings(0);  

	CSingleDocTemplate* pDocTemplate;
	pDocTemplate = new CSingleDocTemplate(
		IDR_MAINFRAME,
		RUNTIME_CLASS(CSpriteEditorDoc),
		RUNTIME_CLASS(CMainFrame),       
		RUNTIME_CLASS(CSpriteEditorView));
	if (!pDocTemplate)
		return FALSE;
	AddDocTemplate(pDocTemplate);



	EnableShellOpen();
	RegisterShellFileTypes(TRUE);


	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);



	if (!ProcessShellCommand(cmdInfo))
		return FALSE;


	m_pMainWnd->ShowWindow(SW_SHOWMAXIMIZED);
	m_pMainWnd->UpdateWindow();

	m_pMainWnd->DragAcceptFiles();
	return TRUE;
}





CSpriteEditorApp theApp;




CSpriteEditorApp::~CSpriteEditorApp()
{
	CrashDump::CrashDump_Clean();
}




CSpriteEditorApp::CSpriteEditorApp()
{
	sXlXapInfo::s_nProcessId = GetCurrentProcessId();
	m_pView = NULL;
	CrashDump::CrashDump_Init( L"SpriteEditor.dmp", L"", static_cast<CrashDump::eDumpLevel>(3));

	setlocale(LC_ALL, "chinese_china");

	TCHAR exeFullName[MAX_PATH];
	GetModuleFileName(NULL,exeFullName,MAX_PATH);
	m_ExePath = exeFullName;
	int mPos=m_ExePath.ReverseFind('\\'); 
	if(mPos==-1) 
	{
		m_ExePath = "";
		return;	
	}
	m_ExePath=m_ExePath.Left(mPos);
	if(m_ExePath.Right(1)=="\\")
	{
		m_ExePath=m_ExePath.Left(mPos-1);
	}
}

BEGIN_MESSAGE_MAP(CSpriteEditorApp, CWinApp)
	ON_COMMAND(ID_APP_ABOUT, &CSpriteEditorApp::OnAppAbout)

	ON_COMMAND(ID_FILE_NEW, &CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CWinApp::OnFileOpen)
END_MESSAGE_MAP()




