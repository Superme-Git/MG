// PackZip.cpp : Defines the class behaviors for the application.
//

#include "stdafx.h"
#include "PackZip.h"
#include "MainFrm.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CPackZipApp

BEGIN_MESSAGE_MAP(CPackZipApp, CWinApp)
	ON_COMMAND(ID_APP_ABOUT, &CPackZipApp::OnAppAbout)
END_MESSAGE_MAP()


// CPackZipApp construction

CPackZipApp::CPackZipApp()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance
}


// The one and only CPackZipApp object

CPackZipApp theApp;


// CPackZipApp initialization

BOOL CPackZipApp::InitInstance()
{
	CCommandLineInfo cmdInfo;
	ParseCommandLine( cmdInfo );

	if( cmdInfo.m_nShellCommand == CCommandLineInfo::AppRegister ||
		cmdInfo.m_nShellCommand == CCommandLineInfo::AppUnregister )
	{
		return ProcessShellCommand( cmdInfo );
	}

	CDocTemplate* pDocTemplate = new CSingleDocTemplate(
		IDR_PACKDOCTYPE, NULL, NULL, NULL );
	AddDocTemplate( pDocTemplate );


	// InitCommonControlsEx() is required on Windows XP if an application
	// manifest specifies use of ComCtl32.dll version 6 or later to enable
	// visual styles.  Otherwise, any window creation will fail.
	INITCOMMONCONTROLSEX InitCtrls;
	InitCtrls.dwSize = sizeof(InitCtrls);
	// Set this to include all the common control classes you want to use
	// in your application.
	InitCtrls.dwICC = ICC_WIN95_CLASSES;
	InitCommonControlsEx(&InitCtrls);

	CWinApp::InitInstance();

	// Initialize OLE libraries
	if (!AfxOleInit())
	{
		AfxMessageBox(IDP_OLE_INIT_FAILED);
		return FALSE;
	}
	AfxEnableControlContainer();

	// Standard initialization
	// If you are not using these features and wish to reduce the size
	// of your final executable, you should remove from the following
	// the specific initialization routines you do not need
	// Change the registry key under which our settings are stored
	// TODO: You should modify this string to be something appropriate
	// such as the name of your company or organization
	SetRegistryKey(_T("LocoJoy\\ZipMaker"));
	RegisterShellFileTypes();
	LoadStdProfileSettings();

	// To create the main window, this code creates a new frame window
	// object and then sets it as the application's main window object
	CMainFrame* pFrame = new CMainFrame;
	if (!pFrame)
		return FALSE;
	m_pMainWnd = pFrame;
	// create and load the frame with its resources
	pFrame->LoadFrame(IDR_MAINFRAME,
		WS_OVERLAPPEDWINDOW | FWS_ADDTOTITLE, NULL,
		NULL);

	// The one and only window has been initialized, so show and update it
	pFrame->ShowWindow( SW_MAXIMIZE);
	pFrame->UpdateWindow();
	// call DragAcceptFiles only if there's a suffix
	//  In an SDI app, this should occur after ProcessShellCommand
	return TRUE;
}


// CPackZipApp message handlers




// CAboutDlg dialog used for App About

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// Dialog Data
	enum { IDD = IDD_ABOUTBOX };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

// Implementation
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

// App command to run the dialog
void CPackZipApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}


// CPackZipApp message handlers

