

#include "stdafx.h"
#include "EffectEditor.h"
#include "MainFrm.h"

#include "CrashDump.h"
#include "EffectEditorDoc.h"
#include "EffectEditorView.h"
#include "..\engine\common\pfsutil.h"
#ifdef _DEBUG
#define new DEBUG_NEW
#endif

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

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()


BEGIN_MESSAGE_MAP(CEffectEditorApp, CWinApp)
	ON_COMMAND(ID_APP_ABOUT, &CEffectEditorApp::OnAppAbout)

	ON_COMMAND(ID_FILE_NEW, &CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CWinApp::OnFileOpen)

	ON_COMMAND(ID_FILE_PRINT_SETUP, &CWinApp::OnFilePrintSetup)
	ON_COMMAND(ID_BATCH_EXPORT, &CEffectEditorApp::OnBatchExport)
END_MESSAGE_MAP()


CEffectEditorApp theApp;

#ifdef _DEBUG
CMemoryState msOld;
#endif



void CEffectEditorApp::OnBatchExport()
{
	CString resFullPath, fname, title;
	title = L"请选择要导出的文件夹";
	resFullPath = GetExePath() + L"effect\\geffect";
	if (SelectFolder(resFullPath, fname, GetMainWnd()->GetSafeHwnd(), title, BIF_RETURNFSANCESTORS | BIF_NEWDIALOGSTYLE | BIF_NONEWFOLDERBUTTON))
	{
		CString dstFullPath;
		title = L"请选择导出到哪里";
		if (SelectFolder(dstFullPath, fname, GetMainWnd()->GetSafeHwnd(), title, BIF_RETURNFSANCESTORS | BIF_NEWDIALOGSTYLE | BIF_DONTGOBELOWDOMAIN))
		{

			if (dstFullPath == L"")
			{
				AfxMessageBox(L"请选择一个文件夹！");
			} else {
				int resFullPathLength = resFullPath.GetLength();
				CToolsEffect toolsEffect(m_pEngine);
				CFileFind myFinder;
				CStringList fileList;
				fileList.AddTail(resFullPath);
				BOOL working;
				int result;
				std::vector<std::wstring> errfiles;
				while (!fileList.IsEmpty())  
				{  
					fname=fileList.GetHead() + L"\\*.*";
					fileList.RemoveHead();
					working = myFinder.FindFile(fname);
					while (working)
					{
						working = myFinder.FindNextFile();
						if (myFinder.IsDots())   
							continue;
						if (myFinder.IsHidden())
							continue;
						if (myFinder.IsDirectory()) 
						{
							fileList.AddTail(myFinder.GetFilePath());
						} else {
							fname = myFinder.GetFilePath();
							if (fname.Right(wcslen(Effect_ExtName)).MakeLower() != Effect_ExtName)
								continue;
							std::wstring newname;
							PFS::CEnv::NativeFileNameToPFSFileName(fname.GetBuffer(), newname);
							fname.ReleaseBuffer();
							XPASSERT(newname[1] == L'e' && newname[2] == L'f' && newname[3] == L'f' && newname[4] == L'e');
							newname = newname.substr(8, newname.length() - 17);
							fname = dstFullPath + fname.Right(fname.GetLength() - resFullPathLength);
							result = toolsEffect.LoadEffect(newname);
							if (result == Nuclear::GEffect::EC_S_OK)
							{
								toolsEffect.SaveEffect(fname.GetBuffer(), Nuclear::EFFECT_IO_NORMAL);
								fname.ReleaseBuffer();
							} else {
								errfiles.push_back(newname);
							}
						}
					}
				}
				if (errfiles.size() > 0)
				{
					dstFullPath = dstFullPath + L"\\explog.txt";
					FILE *f = Nuclear::FOpen(dstFullPath.GetBuffer(), L"w");
					dstFullPath.ReleaseBuffer();
					XPASSERT( f != NULL );
					std::vector<std::wstring>::iterator it = errfiles.begin(), ie = errfiles.end();
					for (;it!=ie;++it)
					{
						fwprintf(f, L"%s\n", it->c_str());
					}
					fclose(f);
					CString errMsg;
					errMsg.Format(L"部分文件打开有误，详细文件列表请打开\n%s", dstFullPath.GetBuffer());
					dstFullPath.ReleaseBuffer();
					AfxMessageBox(errMsg);
				}
			}
		}
	}
}

int CEffectEditorApp::ExitInstance()
{
	if( m_pFileIOMgr )
	{
		m_pFileIOMgr->Destroy();
		delete m_pFileIOMgr;
	}

	return CWinApp::ExitInstance();
}

void CEffectEditorApp::OnAppAbout()
{
	CAboutDlg aboutDlg;
	aboutDlg.DoModal();
}





BOOL CEffectEditorApp::InitInstance()
{

	m_pFileIOMgr = new Nuclear::CFileIOManager();
	if( !m_pFileIOMgr->Init(true, true) )
	{
		AfxMessageBox(L"PFS路径初始化失败");
	}

	INITCOMMONCONTROLSEX InitCtrls;
	InitCtrls.dwSize = sizeof(InitCtrls);

	InitCtrls.dwICC = ICC_WIN95_CLASSES;
	InitCommonControlsEx(&InitCtrls);

	CWinApp::InitInstance();


	if (!AfxOleInit())
	{
		AfxMessageBox(IDP_OLE_INIT_FAILED);
		return FALSE;
	}
	AfxEnableControlContainer();

	SetRegistryKey(_T("应用程序向导生成的本地应用程序"));
	LoadStdProfileSettings(4);  
	CSingleDocTemplate* pDocTemplate;
	pDocTemplate = new CSingleDocTemplate(
		IDR_MAINFRAME,
		RUNTIME_CLASS(CEffectEditorDoc),
		RUNTIME_CLASS(CMainFrame),       
		RUNTIME_CLASS(CEffectEditorView));
	if (!pDocTemplate)
		return FALSE;
	AddDocTemplate(pDocTemplate);



	EnableShellOpen();
	RegisterShellFileTypes(TRUE);


	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);



	if (!ProcessShellCommand(cmdInfo))
		return FALSE;


	m_pMainWnd->ShowWindow(SW_SHOW);
	m_pMainWnd->UpdateWindow();

	m_pMainWnd->DragAcceptFiles();


	return TRUE;
}


CEffectEditorApp::~CEffectEditorApp()
{
	CrashDump::CrashDump_Clean();
}

CEffectEditorApp::CEffectEditorApp() : m_pFileIOMgr(NULL)
{
#ifdef _DEBUG
	msOld.Checkpoint();
#endif
	CrashDump::CrashDump_Init( L"EffectEditor.dmp", L"", static_cast<CrashDump::eDumpLevel>(3));
	srand(GetTickCount());
	TCHAR exeFullName[MAX_PATH];
	GetModuleFileName(NULL,exeFullName,MAX_PATH);
	m_ExePath = exeFullName;

	int mPos=m_ExePath.ReverseFind('\\'); 

	setlocale(LC_ALL, "chinese_china");

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



