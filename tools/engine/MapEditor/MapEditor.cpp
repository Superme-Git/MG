

#include "stdafx.h"
#include "MapEditor.h"
#include "MainFrm.h"

#include "ChildFrm.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "AllObjectResourceDlg.h"
#include "ReplaceObjs.h"
#include "CrashDump.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\common\fileiomanager.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


void CMapEditorApp::OnViewReplaceLinkedObject()
{
	CFileDialog dlg(TRUE, NULL, NULL, OFN_FILEMUSTEXIST, L"XML文件 (*.xml)|*.xml||");
	if (dlg.DoModal() == IDOK)
	{
		CReplaceObjs obj;
		obj.Do(dlg.GetPathName().GetBuffer());
	}
}
void CMapEditorApp::OnShowAllObjectResource()
{
	CAllObjectResourceDlg dlg;
	dlg.DoModal();
}

const wchar_t* XL_MD::fn()
{
	static const CString str = theApp.GetResPath() + L"\\mapeditor.log"; 
	return str;
} 
void CMapEditorApp::OnMergeImport()
{

	m_bIsMergeImport = true;
	if (m_pDocManager != NULL)
		m_pDocManager->OnFileNew();
	m_bIsMergeImport = false;
}

bool CMapEditorApp::SelectExportFolder(CString &fullPath, CString &folderName, HWND hWnd, TCHAR* title, UINT flag)
{
	BROWSEINFO bi;
	ZeroMemory(&bi,sizeof(BROWSEINFO));
	bi.hwndOwner = hWnd;
	bi.pszDisplayName = folderName.GetBuffer(MAX_PATH);
	if (title == NULL)
	{
		bi.lpszTitle = L"请选择导出路径";
	} else {
		bi.lpszTitle = title;
	}
	bi.ulFlags = flag;
	bi.pidlRoot = GetIDListFromPath(theApp.GetResPath());
	LPITEMIDLIST idl = SHBrowseForFolder(&bi);
	folderName.ReleaseBuffer();
	if(idl == NULL)
		return false;
	SHGetPathFromIDList(idl, fullPath.GetBuffer(MAX_PATH));
	fullPath.ReleaseBuffer();
	if(fullPath.GetAt(fullPath.GetLength()-1) != L'\\')
		fullPath += L"\\";
	GlobalFree(idl);
	return true;
}
void CMapEditorApp::OnImport()
{


	m_bIsImport = true;
	if (m_pDocManager != NULL)
		m_pDocManager->OnFileNew();
	m_bIsImport = false;
}


int CMapEditorApp::ExitInstance()
{
	if (m_pPathMap)
	{
		m_pPathMap->Save();
		delete m_pPathMap;
	}

	if( m_pFileIOManager )
	{
		m_pFileIOManager->Destroy();
		delete m_pFileIOManager;
	}
	return CWinApp::ExitInstance();
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

void CMapEditorApp::OnAppAbout()
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

BOOL CMapEditorApp::InitInstance()
{

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

	SetRegistryKey(_T("Local AppWizard-Generated Applications"));
	LoadStdProfileSettings(8);  
	CMultiDocTemplate* pDocTemplate;
	pDocTemplate = new CMultiDocTemplate(IDR_MapEditorTYPE,
		RUNTIME_CLASS(CMapEditorDoc),
		RUNTIME_CLASS(CChildFrame), 
		RUNTIME_CLASS(CMapEditorView));
	if (!pDocTemplate)
		return FALSE;
	AddDocTemplate(pDocTemplate);



	CMainFrame* pMainFrame = new CMainFrame;
	if (!pMainFrame || !pMainFrame->LoadFrame(IDR_MAINFRAME))
	{
		delete pMainFrame;
		return FALSE;
	}
	m_pMainWnd = pMainFrame;




	CCommandLineInfo cmdInfo;
	ParseCommandLine(cmdInfo);



	cmdInfo.m_nShellCommand   = CCommandLineInfo::FileNothing; 

	if (!ProcessShellCommand(cmdInfo))
		return FALSE;

	pMainFrame->ShowWindow(SW_SHOWMAXIMIZED);
	pMainFrame->UpdateWindow();

	PFS::CEnv::SetOutLogInstance(&m_pLog);

	m_pFileIOManager = new Nuclear::CFileIOManager();
	if( !m_pFileIOManager->Init(true, true) )
	{
		AfxMessageBox(L"PFS路径初始化失败");
	}
	m_pPathMap = new Nuclear::SubMap::PPathMap(m_pFileIOManager);
	m_pPathMap->Load();
	return TRUE;
}

void CMapEditorApp::CheckTileType(Nuclear::TILE_SIZE_TYPE tileSize)
{
	CString FileName;
	CString extName = L"\\1";
	FileName=theApp.GetResPath() + L"\\map\\tiles\\";
	TILT_TYPE_ID tileTypeID;
	switch(tileSize)
	{
	case Nuclear::TST_SMALL:
		FileName += L"smalltile";
		tileTypeID.type = 0;
		extName += L"-*";
		break;
	case Nuclear::TST_MIDDLE:
		FileName += L"midtile";
		tileTypeID.type = 1;
		break;
	case Nuclear::TST_BIG:
		FileName += L"bigtile";
		tileTypeID.type = 2;
		break;
	}
	extName += TILE_TYPE;
	FileName += L"\\*.*";

	CFileFind myFinder;
	CFileFind myTileFinder;

	BOOL working;
	BiggerTileType tileData;

	working = myFinder.FindFile(LPCTSTR(FileName));
	while (working)
	{  
		working = myFinder.FindNextFile();
		if (myFinder.IsDots())
			continue;
		if (myFinder.IsDirectory() && !(myFinder.IsHidden())) 
		{
			tileTypeID.tileType = _ttoi(myFinder.GetFileTitle().GetString());
			std::wstring strValue = myFinder.GetFileTitle();
			m_pPathMap->Insert(tileSize, tileTypeID.tileType, strValue);
			if (myTileFinder.FindFile(myFinder.GetFilePath() + extName))
			{
				m_TileTypeMap[tileTypeID.id] = true;
			} else {
				m_TileTypeMap[tileTypeID.id] = false;
			}
		}  
	}
	myFinder.Close();
	myTileFinder.Close();

}

void CMapEditorApp::CheckTileType()
{
	if (m_bIsInitTileType)
		return;
	m_bIsInitTileType = true;
	CheckTileType(Nuclear::TST_SMALL);
	CheckTileType(Nuclear::TST_MIDDLE);
	CheckTileType(Nuclear::TST_BIG);
}

#ifdef _DEBUG
CMemoryState msOld;
#endif

CMapEditorApp theApp;

CMapEditorApp::~CMapEditorApp()
{
	CrashDump::CrashDump_Clean();
}

CMapEditorApp::CMapEditorApp() : m_bIsImport(false), m_pFileIOManager(NULL), m_bIsMergeImport(false), m_pPathMap(NULL)
{


#ifdef _DEBUG
	msOld.Checkpoint();
#endif
	XL_MD::s_nProcessId = GetCurrentProcessId();
	CrashDump::CrashDump_Init( L"MapEditor.dmp", L"", static_cast<CrashDump::eDumpLevel>(3));
	srand(GetTickCount());

	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	std::wstring wdir = workdir;
	int tmppos = (int)wdir.rfind('\\');
	wdir = std::wstring(wdir, 0, tmppos);
	std::wstring sysFilePath = _T("\\res");
	m_ResPath = wdir.c_str();
	m_ResPath += sysFilePath.c_str();

	setlocale(LC_ALL, "chinese_china");

	m_bIsInitTileType = false;
}

BEGIN_MESSAGE_MAP(CMapEditorApp, CWinApp)
	ON_COMMAND(ID_APP_ABOUT, &CMapEditorApp::OnAppAbout)

	ON_COMMAND(ID_FILE_NEW, &CWinApp::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CWinApp::OnFileOpen)

	ON_COMMAND(ID_FILE_PRINT_SETUP, &CWinApp::OnFilePrintSetup)
	ON_COMMAND(ID_IMPORT, &CMapEditorApp::OnImport)
	ON_COMMAND(ID_MERGE_IMPORT, &CMapEditorApp::OnMergeImport)
	ON_COMMAND(ID_SHOW_ALL_OBJ_RES, &CMapEditorApp::OnShowAllObjectResource)
	ON_COMMAND(ID_VIEW_REPLACE_LKO, &CMapEditorApp::OnViewReplaceLinkedObject)
END_MESSAGE_MAP()

