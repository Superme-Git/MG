// MainFrm.cpp : implementation of the CMainFrame class
//

#include "stdafx.h"
#include "PackZip.h"
#include "MainFrm.h"
#include "LeftView.h"
#include "RightView.h"
#include "BrowsePath.h"
#include "FolderDialog.h"
#include "shlwapi.h"
#include "RecentProject.h"
#include "SaveProgressDlg.h"
#include "AddNewPathDlg.h"
#include "KHLogger.h"
#include "ProjectPathDlg.h"
#include "EditVersionDlg.h"
#include <sstream>
#include "UpdateSetupPfsDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif
// CMainFrame

#define ID_MENU_RECENTPROJECT		20000

IMPLEMENT_DYNAMIC(CMainFrame, CFrameWnd)

BEGIN_MESSAGE_MAP(CMainFrame, CFrameWnd)
	ON_WM_CREATE()
	ON_WM_SETFOCUS()
	ON_WM_CLOSE()

	ON_MESSAGE( WM_OPERATOREND, OnOperatorEnd)

	ON_COMMAND(ID_FILE_ADDPATH, &CMainFrame::OnFileAddPath)
	ON_COMMAND(ID_FILE_SAVESETUP, &CMainFrame::OnFileSaveSetup)
	ON_COMMAND(ID_VIEW_EDITFILEZIPLEVEL, &CMainFrame::OnViewEditfileziplevel)
	ON_COMMAND(ID_RESOURCETYPE, &CMainFrame::OnResourcetype)
	ON_UPDATE_COMMAND_UI(ID_RESOURCETYPE, &CMainFrame::OnUpdateResourcetype)
	ON_COMMAND(ID_SETUPTYPE, &CMainFrame::OnSetuptype)
	ON_UPDATE_COMMAND_UI(ID_SETUPTYPE, &CMainFrame::OnUpdateSetuptype)
	ON_COMMAND(ID_EDITIGNORE, &CMainFrame::OnEditignore)
	ON_COMMAND(ID_FILE_SAVERESOURCE, &CMainFrame::OnFileSaveResource)
	ON_COMMAND(ID_FILE_NEW, &CMainFrame::OnFileNew)
	ON_COMMAND(ID_FILE_OPEN, &CMainFrame::OnFileOpen)
	ON_COMMAND(ID_FILE_SAVE, &CMainFrame::OnFileSave)
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVE, &CMainFrame::OnUpdateFileSave)
	ON_COMMAND(ID_EDIT_ADDPATHCONTENT, &CMainFrame::OnEditAddpathcontent)
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVERESOURCE, &CMainFrame::OnUpdateFileSaveresource)
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVESETUP, &CMainFrame::OnUpdateFileSavesetup)
	ON_UPDATE_COMMAND_UI(ID_VIEW_EDITFILEZIPLEVEL, &CMainFrame::OnUpdateViewEditfileziplevel)
	ON_UPDATE_COMMAND_UI(ID_EDIT_ADDPATHCONTENT, &CMainFrame::OnUpdateEditAddpathcontent)
	ON_UPDATE_COMMAND_UI(ID_EDITIGNORE, &CMainFrame::OnUpdateEditignore)
	ON_UPDATE_COMMAND_UI(ID_FILE_ADDPATH, &CMainFrame::OnUpdateFileAddpath)
	ON_COMMAND(ID_VIEW_SHOWUNCHECKED, &CMainFrame::OnViewShowunchecked)
	ON_UPDATE_COMMAND_UI(ID_VIEW_SHOWUNCHECKED, &CMainFrame::OnUpdateViewShowunchecked)
	ON_COMMAND(ID_FILE_CLOSE, &CMainFrame::OnFileClose)
	ON_UPDATE_COMMAND_UI(ID_FILE_CLOSE, &CMainFrame::OnUpdateFileClose)
	ON_COMMAND_RANGE( ID_MENU_RECENTPROJECT, ID_MENU_RECENTPROJECT+CRecentProject::MAXCOUNT, OnRecentProject)
	ON_COMMAND(ID_EDIT_ADDNEWPATH, &CMainFrame::OnEditAddnewpath)
	ON_UPDATE_COMMAND_UI(ID_EDIT_ADDNEWPATH, &CMainFrame::OnUpdateEditAddnewpath)
	ON_COMMAND(ID_EDIT_DELETE, &CMainFrame::OnEditDelete)
	ON_UPDATE_COMMAND_UI(ID_EDIT_DELETE, &CMainFrame::OnUpdateEditDelete)
	ON_UPDATE_COMMAND_UI(ID_MESSAGE_PANE, OnUpdateControlBarMenu)
	ON_COMMAND_EX(ID_MESSAGE_PANE, OnBarCheck)
	ON_COMMAND(ID_EDIT_PROJECTPATH, &CMainFrame::OnEditProjectPath)
	ON_UPDATE_COMMAND_UI(ID_EDIT_PROJECTPATH, &CMainFrame::OnUpdateProjectPath)
	ON_COMMAND(ID_FILE_REFRESH, &CMainFrame::OnFileRefresh)
	ON_UPDATE_COMMAND_UI(ID_FILE_REFRESH, &CMainFrame::OnUpdateFileRefresh)
	ON_WM_DROPFILES()
	ON_COMMAND(ID_EDIT_VERSIONMETA, &CMainFrame::OnEditVersionmeta)
	ON_UPDATE_COMMAND_UI(ID_EDIT_VERSIONMETA, &CMainFrame::OnUpdateEditVersionmeta)
	ON_COMMAND( ID_EDIT_SETISMAKEMD5, OnSetIsMakeMD5)
	ON_UPDATE_COMMAND_UI( ID_EDIT_SETISMAKEMD5, OnUpdateSetIsMakeMD5)

	ON_COMMAND( ID_FILE_SAVETEMPUPDATE, OnFileSaveTempUpdate)
	ON_UPDATE_COMMAND_UI( ID_FILE_SAVETEMPUPDATE, OnUpdateFileSaveTempUpdate)

	ON_COMMAND( ID_VIEW_UPDATESETUPPFS, OnViewUpdateSetupPfs)
	ON_COMMAND(ID_EDIT_SORT_OPERATORS, &CMainFrame::OnEditSortOperators)
	ON_UPDATE_COMMAND_UI(ID_EDIT_SORT_OPERATORS, &CMainFrame::OnUpdateEditSortOperators)
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVEZIP, &CMainFrame::OnUpdateFileSavezip)
	ON_COMMAND(ID_FILE_SAVEZIP, &CMainFrame::OnFileSavezip)
END_MESSAGE_MAP()

static UINT indicators[] =
{
	ID_SEPARATOR,           // status line indicator
	ID_INDICATOR_CAPS,
	ID_INDICATOR_NUM,
	ID_INDICATOR_SCRL,
};


// CMainFrame construction/destruction

CMainFrame::CMainFrame()
: m_bChanged( false)
, m_bInit( true)
, m_bSortOperators (false)
{
}

CMainFrame::~CMainFrame()
{
	Clear();
}

void ToolbarEnableShowButtonText( CToolBar& rToolBar )
{
	// Button数目，含Seperator
	int nCount = rToolBar.GetCount();
	if ( nCount == 0 )
	{
		return ;
	}

	for ( int nIndex = 0; nIndex < nCount ; nIndex ++ )
	{
		UINT nID;
		UINT nStyle;
		int iImage;
		rToolBar.GetButtonInfo(nIndex, nID, nStyle, iImage);
		if ( nID == (UINT)0 ) // 跳过Seperator
		{
			continue;
		}

		rToolBar.SetButtonStyle(nIndex, nStyle | BTNS_SHOWTEXT/* | BTNS_AUTOSIZE*/  );
		CString strText;
		strText.LoadString( nID );
		int nPos = strText.Find( _T('\n') );
		if ( nPos > 0 )
		{
			strText = strText.Mid( nPos + 1 );
		}

		rToolBar.SetButtonText(nIndex, strText);
	}

	// 重新计算Toolbar的高度
	rToolBar.GetToolBarCtrl().SetButtonWidth(40,100);

	CRect rcButton;
	rToolBar.GetItemRect( 0, &rcButton);	
	SIZE szButton = { rcButton.Width(), rcButton.Height()};
	SIZE szImage = { 16, 22 }; // Toolbar bitmap资源中每个button的大小
	rToolBar.SetSizes( szButton, szImage );
}

void KHMessageHookT_Proxy(LPVOID lpUserParam, LPCTSTR lpszMessage)
{
	((CRichEditCtrl*)lpUserParam)->SetSel(-1,-1);
	((CRichEditCtrl*)lpUserParam)->ReplaceSel( lpszMessage, FALSE );
}

LPCTSTR GetModuleDir()
{
	static TCHAR szModuleDir[MAX_PATH];
	if ( szModuleDir[0] == 0 )
	{
		DWORD dwRet = GetModuleFileName( NULL, szModuleDir, MAX_PATH);
		LPTSTR lpSep = &szModuleDir[dwRet];
		while( *lpSep != '/' && *lpSep != '\\' && *lpSep != ':' )
		{
			lpSep --;
		}
		lpSep[0] = _T('\0');
	}

	return szModuleDir;
}

int CMainFrame::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CFrameWnd::OnCreate(lpCreateStruct) == -1)
		return -1;

	if (!m_wndToolBar.CreateEx(this, TBSTYLE_FLAT|TBSTYLE_AUTOSIZE, WS_CHILD | WS_VISIBLE | CBRS_TOP
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC) ||
		!m_wndToolBar.LoadToolBar(IDR_MAINFRAME))
	{
		TRACE0("Failed to create toolbar\n");
		return -1;      // fail to create
	}

	if (!m_wndStatusBar.Create(this) ||
		!m_wndStatusBar.SetIndicators(indicators,
		  sizeof(indicators)/sizeof(UINT)))
	{
		TRACE0("Failed to create status bar\n");
		return -1;      // fail to create
	}

	ToolbarEnableShowButtonText( m_wndToolBar );

	// TODO: Delete these three lines if you don't want the toolbar to be dockable
	m_wndToolBar.EnableDocking(CBRS_ALIGN_ANY);
	EnableDocking(CBRS_ALIGN_ANY);
	DockControlBar(&m_wndToolBar);

	SetTitleText();


	// 消息窗口的位图列表
	VERIFY( m_ilMessagePane.Create(IDB_MESSAGE_PANE, 13, 1, RGB(0,255,0)) );

	// 消息窗口
	{
		// 在创建窗口前，必须提前设置ImageList
		m_wndMessagePane.SetBtnImageList (&m_ilMessagePane);

		// 保证消息窗口面板ID和显示消息窗口菜单ID一致
		ASSERT(ID_MESSAGE_PANE == ID_VIEW_MESSAGE_PANE);

		// Initialize dialog bar m_wndMessagePane
		// strInfo.LoadString( IDS_GUISTRING_OUTPUTPANE );
		CString strInfo = _T("Message");

		if (!m_wndMessagePane.Create(this, ID_MESSAGE_PANE,
			strInfo, CSize(600,120)))
		{
			TRACE0("Failed to create dialog bar m_wndMessagePane\n");
			return -1;		// fail to create
		}

		// allow bar to be resized when floating
		m_wndMessagePane.SetBarStyle(m_wndMessagePane.GetBarStyle() |
			CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC);

		m_wndMessagePane.EnableDocking(CBRS_ALIGN_BOTTOM);
		EnableDocking(CBRS_ALIGN_ANY);
		DockControlBar(&m_wndMessagePane, AFX_IDW_DOCKBAR_BOTTOM);
	}

	// m_wndMessagePane.m_wndRichEdit.SetTargetDevice(NULL, 0);

	///=============================================================================
	///	安装消息钩子
	///=============================================================================
#ifdef UNICODE
	KHInstallMessageHookW( &KHMessageHookT_Proxy, (LPVOID)&m_wndMessagePane.m_wndRichEdit );
#else
	KHInstallMessageHookA( &KHMessageHookT_Proxy, (LPVOID)&m_wndMessagePane.m_wndRichEdit );
#endif

	// 分析命令行参数
	CCommandLineInfo cmdInfo;
	AfxGetApp()->ParseCommandLine( cmdInfo );
	if( cmdInfo.m_nShellCommand == CCommandLineInfo::FileOpen )
	{
		if( !OpenProjectFile( cmdInfo.m_strFileName.GetString() ) )
		{
			AfxMessageBox( L"Invalid zip project file!");
		}
	}

	DragAcceptFiles( TRUE );

	// Launcher的运行时目录为其父目录，即GameClient.exe所在的目录
	// 设置该目录的重要性在于，pfsx-tanslator.dll插件需要根据CurrentDirectory
	// 和ModuleFileDirectory确定相对位置。
	// 正确的CurrentDirectory可以帮助找到pfsx-tanslator.dll。
	SetCurrentDirectory( GetModuleDir() );
	// 为了调用CTranslateManager的构造函数，避免找不到pfsx-tanslator.dll的情况
	PFSX::CTranslateManager::GetInstance().GetDecoder( 0);

	return 0;
}

BOOL CMainFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	if( !CFrameWnd::PreCreateWindow(cs) )
		return FALSE;
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	cs.dwExStyle &= ~WS_EX_CLIENTEDGE;
	cs.lpszClass = AfxRegisterWndClass(0);
	return TRUE;
}

BOOL CMainFrame::OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext)
{
	CRecentProject::GetInstance().Open();

	m_wndSplitter.CreateStatic(this, 1, 2);
	m_wndSplitter.CreateView(0, 0, RUNTIME_CLASS(CLeftView), CSize(300,0), pContext);
	m_wndSplitter.CreateView(0, 1, RUNTIME_CLASS(CRightView), CSize(300,0), pContext);
	CRightView* pRightView = (CRightView*)m_wndSplitter.GetPane(0,1);

	CLeftView* pLeftView = (CLeftView*)m_wndSplitter.GetPane( 0, 0);
	pLeftView->AddFileViewer( pRightView);

	m_dlgZipLevel.Create( IDD_FILEZIPLEVEL, this);
	CRect rc;
	m_dlgZipLevel.GetWindowRect( &rc);
	int nWidth = rc.Width();
	int nHeight = rc.Height();
	rc.left = GetSystemMetrics( SM_CXSCREEN) / 2;
	rc.right = rc.left + nWidth;
	rc.top = 100;
	rc.bottom = rc.top + nHeight;
	m_dlgZipLevel.MoveWindow( rc);

	m_dlgIgnore.Create( IDD_EDITIGNORE, this);
	m_dlgIgnore.GetWindowRect( &rc);
	nWidth = rc.Width();
	nHeight = rc.Height();
	rc.left = GetSystemMetrics( SM_CXSCREEN) / 2;
	rc.right = rc.left + nWidth;
	rc.bottom = GetSystemMetrics( SM_CYSCREEN) - 50;
	rc.top = rc.bottom - nHeight;
	m_dlgIgnore.MoveWindow( rc);

	AddRecentProjectToMenu();

	return CFrameWnd::OnCreateClient(lpcs, pContext);
}

void CMainFrame::AddRecentProjectToMenu()
{
	CMenu* pMenu = GetMenu();
	if( pMenu)
	{
		pMenu = pMenu->GetSubMenu( 0);
		UINT count = pMenu->GetMenuItemCount() - 2;

		std::vector<std::wstring>& projects = CRecentProject::GetInstance().GetProjects();
		if( projects.empty())
		{
			// 没有zipmaker.rup.xml删掉分割符
			pMenu->RemoveMenu( count, MF_BYPOSITION);
			return;
		}
		
		int i = 0;
		for( std::vector<std::wstring>::iterator itor = projects.begin(); itor != projects.end(); ++itor, i++)
		{
			std::wostringstream os;
			os << projects.size()-i << L" " << *itor;
			pMenu->InsertMenu( count, MF_BYPOSITION|MF_STRING, ID_MENU_RECENTPROJECT+i, os.str().c_str());
		}
	}
}

void CMainFrame::DelRecentProjectFromMenu()
{
	CMenu* pMenu = GetMenu();
	if( pMenu)
	{
		pMenu = pMenu->GetSubMenu( 0);
		for( int i = pMenu->GetMenuItemCount()-1; i >= 0; i--)
		{
			UINT id = pMenu->GetMenuItemID( i);
			if( id >= ID_MENU_RECENTPROJECT && id < ID_MENU_RECENTPROJECT+CRecentProject::MAXCOUNT)
			{
				pMenu->RemoveMenu( i, MF_BYPOSITION);
			}
		}
	}
}


// "Add directory ..."
void CMainFrame::OnFileAddPath()
{
	CBrowsePath browsepath( m_hWnd);
	browsepath.SetIniPath( m_lastpath);
	if( browsepath.DoModal())
	{
		m_lastpath = browsepath.GetSelectPath();

		CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
		if( pLeftView)
		{
			if( pLeftView->AddPath( m_lastpath))
				m_bChanged = true;
			else
				AfxMessageBox( L"添加失败，不能在文件结点下面添加子结点！");
		}
	}
}

void CMainFrame::OnFileSaveSetup()
{
	SaveFile( false, true, L"");
}

void CMainFrame::OnViewEditfileziplevel()
{
	if( m_dlgZipLevel.IsWindowVisible())
		m_dlgZipLevel.ShowWindow( SW_HIDE);
	else
		m_dlgZipLevel.ShowWindow( SW_SHOW);
}

void CMainFrame::OnResourcetype()
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane( 0, 0));
	if( pLeftView)
		pLeftView->SetIsResource( true);
}

void CMainFrame::OnUpdateResourcetype(CCmdUI *pCmdUI)
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane( 0, 0));
	if( pLeftView)
		pCmdUI->SetRadio( pLeftView->GetIsResource());
}

void CMainFrame::OnSetuptype()
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane( 0, 0));
	if( pLeftView)
		pLeftView->SetIsResource( false);
}

void CMainFrame::OnUpdateSetuptype(CCmdUI *pCmdUI)
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane( 0, 0));
	if( pLeftView)
		pCmdUI->SetRadio( !pLeftView->GetIsResource());
}

void CMainFrame::OnEditignore()
{
	if( m_dlgIgnore.IsWindowVisible())
		m_dlgIgnore.ShowWindow( SW_HIDE);
	else
		m_dlgIgnore.ShowWindow( SW_SHOW);
}

void CMainFrame::OnFileSaveResource()
{
	SaveFile( true, true, L"");
}

void CMainFrame::SaveFile( bool bResource, bool bSaveFilesMeta,  const std::wstring& lastDataPfs)
{
	std::wstring pfsname;
	if( lastDataPfs.empty())
	{
		pfsname = m_projectfilename;
		
		size_t pos ;
		if ( (pos = m_projectfilename.find( L"release.zipproj" ) ) != m_projectfilename.npos )
		{
			pfsname.replace( pos, pfsname.npos, L"data.zip" );

			// 针对data.pfs, 提醒编辑版本信息
			OnEditVersionmeta();

		}
		else if ( (pos = pfsname.find_last_of(L'.')) != pfsname.npos)
		{
			pfsname.replace( pos, pfsname.npos, L".zip" );
		}
	}
	else
	{
		pfsname = L"data_update.zip";	// 临时更新包的名称
		// 临时更新包提醒编辑版本信息
		OnEditVersionmeta();
	}
	
	wchar_t szFilter[] = L"zip file|*.zip|all files|*.*|";
	CFileDialog dlgFile( FALSE, L"zip", pfsname.c_str(), OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, szFilter);
	if (IDCANCEL == dlgFile.DoModal())
	{
		return;
	}
	
	CString strFile = dlgFile.GetPathName();
	CLeftView* pLeftView = (CLeftView*)m_wndSplitter.GetPane(0, 0);
	if( !pLeftView->SaveFile( strFile.GetString(), bResource, bSaveFilesMeta, lastDataPfs))
	{
	}
}

void CMainFrame::OnFileNew()
{
	OnFileSave();
	OpenProjectFile( L"");
}

void CMainFrame::Clear()
{
	m_projectfilename.clear();
	m_project.Close();
}

void CMainFrame::OnFileOpen()
{
	OnFileSave();
	wchar_t szFilter[] = L"zipproj file|*.zipproj|all files|*.*|";
	CFileDialog dlgFile( TRUE, L"zipproj", NULL, OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, szFilter);
	if( IDCANCEL == dlgFile.DoModal())
		return;
	if( !OpenProjectFile( dlgFile.GetPathName().GetString()))
	{
		AfxMessageBox( L"Invalid zip project file!");
	}
}

bool CMainFrame::OpenProjectFile( const std::wstring& filename)
{
	Clear();
	if( filename.empty())
		m_project.AddDefault();
	else
	{
		if( !m_project.Open( filename))
			return false;
	}
	m_projectfilename = filename;
	AddRecentProject();
	
	CRightView* pRightView = dynamic_cast<CRightView*>( m_wndSplitter.GetPane( 0, 1));
	if( pRightView)
		pRightView->OpenProject( &m_project);
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
		pLeftView->OpenProject( &m_project);

	m_dlgZipLevel.OpenProject( &m_project);
	m_dlgIgnore.OpenProject( &m_project);
	m_bInit = false;

	SetTitleText();
	return true;
}

void CMainFrame::AddRecentProject()
{
	if( !m_projectfilename.empty())
	{
		CRecentProject::GetInstance().AddProject( m_projectfilename);
		DelRecentProjectFromMenu();
		AddRecentProjectToMenu();
	}
}

// 刷新是否忽略
void CMainFrame::RefreshIsCheck( )
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
		pLeftView->RefreshIsCheck();
}

void CMainFrame::RefreshZipLevel()
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
		pLeftView->RefreshZipLevel();
}

void CMainFrame::OnFileSave()
{
	if( !m_bChanged)
		return;
	if( m_projectfilename.empty())
	{
		wchar_t szFilter[] = L"zip project file|*.zipproj|all files|*.*|";
		CFileDialog dlgFile( FALSE, L"zipproj", NULL, OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, szFilter);
		if( IDCANCEL == dlgFile.DoModal())
			return;
		m_projectfilename = dlgFile.GetPathName().GetString();
	}
	
	m_bChanged = false;

	if( m_bSortOperators )
	{
		m_project.SortOperators();
	}

	if( m_project.Save( m_projectfilename))
		AddRecentProject();
	SetTitleText();

	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
	{
		pLeftView->RefreshProjectShowName();
	}
}

void CMainFrame::SetTitleText()
{
	CString strFile;
	if( m_bInit)
		strFile = L"开始";
	else
	{
		if( m_projectfilename.empty())
			strFile = L"新建";
		else
			strFile = m_projectfilename.c_str();
	}
	strFile += L" - ";
	strFile += AfxGetAppName();
	SetWindowText( strFile);
}

void CMainFrame::OnClose()
{
	if( !OnCloseProject())
		return;
	CFrameWnd::OnClose();
}

bool CMainFrame::OnCloseProject()
{
	if( !m_bChanged)
		return true;
	int res = AfxMessageBox( L"工程文件已经被更改，是否需要保存？", MB_YESNOCANCEL);
	if( IDCANCEL == res)
		return false;
	if( IDYES == res)
		OnFileSave();
	m_bChanged = false;
	return true;
}

void CMainFrame::OnUpdateFileSave(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bChanged);
}

//
// "Add File ..."
//
void CMainFrame::OnEditAddpathcontent()
{
	CFolderDialog dlgFile;
	if( IDCANCEL == dlgFile.DoModal())
		return;
	std::vector<std::wstring>& selects = CFolderDialog::m_files;
	if( selects.empty())
		selects.push_back( dlgFile.GetPathName().GetString());

	m_lastpath = selects[0];
	std::wstring::size_type pos = m_lastpath.find_last_of( L'\\');
	if( pos != std::wstring::npos)
	{
		m_lastpath.erase( pos);
	}

	std::map<std::wstring, bool> files;
	for( std::vector<std::wstring>::iterator itor = selects.begin(); itor != selects.end(); ++itor)
	{
		if( !itor->empty())
		{
			files.insert( std::make_pair( *itor, !!PathIsDirectory( itor->c_str())));
		}
	}

	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
	{
		if( pLeftView->AddPathAndFile( files))
			m_bChanged = true;
		else
			AfxMessageBox( L"添加失败，可能是以下原因:\n1、文件不存在\n2、不能在文件结点下面添加子结点！");
	}
}

void CMainFrame::OnUpdateFileSaveresource(CCmdUI *pCmdUI)
{
	if( m_bInit)
	{
		pCmdUI->Enable( FALSE);
	}
	else
	{
		CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane( 0, 0));
		if( pLeftView)
		{
			pCmdUI->Enable( pLeftView->GetIsResource());
		}
	}
}

void CMainFrame::OnUpdateFileSavesetup(CCmdUI *pCmdUI)
{
	if( m_bInit)
	{
		pCmdUI->Enable( FALSE);
	}
	else
	{
		CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane( 0, 0));
		if( pLeftView)
			pCmdUI->Enable( !pLeftView->GetIsResource());
	}
}

void CMainFrame::OnUpdateViewEditfileziplevel(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_dlgZipLevel.IsWindowVisible());
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
}

void CMainFrame::OnUpdateEditignore(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_dlgIgnore.IsWindowVisible());
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
}

void CMainFrame::OnUpdateEditAddpathcontent(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
}

void CMainFrame::OnUpdateFileAddpath(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
}

void CMainFrame::OnViewShowunchecked()
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
		pLeftView->RefreshShowUnChecked();
}

void CMainFrame::OnUpdateViewShowunchecked(CCmdUI *pCmdUI)
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
		pCmdUI->SetCheck( pLeftView->GetIsShowUnChecked() ? TRUE : FALSE);
}

void CMainFrame::OnFileClose()
{
	if( !OnCloseProject())
		return;

	m_bInit = true;
	Clear();
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
		pLeftView->CloseProject( );
	CRightView* pRightView = dynamic_cast<CRightView*>( m_wndSplitter.GetPane( 0, 1));
	if( pRightView)
		pRightView->CloseProject( );
	m_dlgZipLevel.ShowWindow( SW_HIDE);
	m_dlgIgnore.ShowWindow( SW_HIDE);

	SetTitleText();
}

void CMainFrame::OnUpdateFileClose(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
}

void CMainFrame::OnRecentProject( UINT id)
{
	std::vector<std::wstring>& projects = CRecentProject::GetInstance().GetProjects();
	size_t index = id - ID_MENU_RECENTPROJECT;
	if( (index >= 0) && (index < projects.size()))
	{
		if( !OnCloseProject())
			return;
		if( !OpenProjectFile( projects[ index]))
			AfxMessageBox( L"Invalid zip project file!");
	}
}

LRESULT CMainFrame::OnOperatorEnd( WPARAM wParam, LPARAM lParam)
{
	if( wParam)
	{
		CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
		if( pLeftView)
		{
			CZipTreeItem* item = reinterpret_cast<CZipTreeItem*>( wParam);
			pLeftView->RefreshTreeItem( *item, lParam);
		}
	}
	return 0;
}


// "Add empty directory ..."
void CMainFrame::OnEditAddnewpath()
{
	CAddNewPathDlg dlg;
	if( IDCANCEL == dlg.DoModal())
		return;
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
	{
		if( pLeftView->AddNewPath( dlg.m_path.GetString()))
			m_bChanged = true;
		else
			AfxMessageBox( L"添加失败，可能是以下原因：\n1、当前选中项不是目录\n2、添加的目录名与现有项重名！");
	}
}

void CMainFrame::OnUpdateEditAddnewpath(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
}

void CMainFrame::OnEditDelete()
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
	{
		if( IDCANCEL == AfxMessageBox( L"确认要删除该结点么？", MB_OKCANCEL))
			return;
		if( pLeftView->DeletePath())
			m_bChanged = true;
	}
}

void CMainFrame::OnUpdateEditDelete(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
}

void CMainFrame::OnEditProjectPath()
{
	CProjectPathDlg dlg;
	dlg.OpenProject( &m_project);
	if( IDOK == dlg.DoModal())
	{
		if( dlg.m_bChanged)
		{
			OnFileRefresh();
			m_bChanged = true;
		}
	}
}

void CMainFrame::OnUpdateProjectPath(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
}

void CMainFrame::OnFileRefresh()
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
		pLeftView->OpenProject( &m_project);
}

void CMainFrame::OnUpdateFileRefresh(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
}

void CMainFrame::OnDropFiles(HDROP hDropInfo)
{
	SetActiveWindow();      // activate us first !

	UINT nFiles = ::DragQueryFile(hDropInfo, (UINT)-1, NULL, 0);
	if ( nFiles < 1 )
	{
		::DragFinish(hDropInfo);
		return;
	}

	TCHAR szFileName[MAX_PATH+1] = {0};
	DragQueryFile( hDropInfo, 0, szFileName, MAX_PATH );
	if( !OpenProjectFile( szFileName ) )
	{
		AfxMessageBox( L"Invalid zip project file!");
	}

	::DragFinish(hDropInfo);
}

void CMainFrame::OnEditVersionmeta()
{
	CEditVersionDlg dlgEditVersion;
	dlgEditVersion.OpenProject( &m_project);
	if( dlgEditVersion.DoModal() == IDOK)
	{
		m_bChanged = dlgEditVersion.m_changed;
	}
}

void CMainFrame::OnUpdateEditVersionmeta(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
}

BOOL CMainFrame::OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo)
{
	// 将Command消息路由到消息窗口
	if ( m_wndMessagePane.m_wndRichEdit.GetSafeHwnd() == ::GetFocus() )
	{
		if( m_wndMessagePane.m_wndRichEdit.OnCmdMsg( nID, nCode, pExtra, pHandlerInfo ) )
		{
			return true;
		}
	}

	return CFrameWnd::OnCmdMsg(nID, nCode, pExtra, pHandlerInfo);
}

void CMainFrame::OnSetIsMakeMD5()
{
	CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane(0, 0));
	if( pLeftView)
	{
		pLeftView->OnSetIsMakeMD5();
		m_bChanged = true;
	}
}

void CMainFrame::OnUpdateSetIsMakeMD5(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_bInit ? FALSE : TRUE);
	pCmdUI->SetCheck( m_project.GetIsMakeMD5());
}

class CUnMountScope
{
	std::wstring		m_mountStr;

public:
	CUnMountScope( const std::wstring& mountStr) : m_mountStr( mountStr) {}
	~CUnMountScope() { PFS::CEnv::Unmount( m_mountStr); }
};

void CMainFrame::OnFileSaveTempUpdate()
{
	AfxMessageBox( L"请先在下面弹出的对话框中选择上一版本安装包的路径！", MB_ICONINFORMATION);
	wchar_t szFilter[] = L"pfs file|*.pfs|all files|*.*|";
	CFileDialog dlgFile( TRUE, L"pfs", L"data.pfs", OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, szFilter);
	if( IDCANCEL == dlgFile.DoModal())
	{
		return;
	}
	AfxGetApp()->BeginWaitCursor();

	const std::wstring lastVersionPfs = L"/lastversionpfs";

	std::wstring fileName = dlgFile.GetPathName().GetString();
	if( !PFS::CEnv::Mount( lastVersionPfs, fileName, PFS::FST_ZIP, PFS::MT_READONLY))
	{
		std::wostringstream woss;
		woss << fileName << L" 文件mount失败！";
		AfxMessageBox( woss.str().c_str());
		return;
	}

	AfxGetApp()->EndWaitCursor();

	CUnMountScope scope( lastVersionPfs);		// 保证最后能unmount

	PFSX::CVersionInfo lastVersionInfo;
	if( !PFSX::GetLocalVersion( lastVersionPfs, lastVersionInfo))
	{
		std::wostringstream woss;
		woss << fileName << L" 文件中获得版本信息出错！";
		return;
	}
	// 提示编辑版本
	m_project.SetVersionMeta( lastVersionInfo);
	SaveFile( false, true, lastVersionPfs);
}

void CMainFrame::OnUpdateFileSaveTempUpdate( CCmdUI *pCmdUI)
{
	if( m_bInit)
	{
		pCmdUI->Enable( FALSE);
	}
	else
	{
		CLeftView* pLeftView = dynamic_cast<CLeftView*>( m_wndSplitter.GetPane( 0, 0));
		if( pLeftView)
		{
			pCmdUI->Enable( !pLeftView->GetIsResource());
		}
	}
}

void CMainFrame::OnViewUpdateSetupPfs()
{
	CUpdateSetupPfsDlg dlg;
	dlg.DoModal();
}

void CMainFrame::OnEditSortOperators()
{
	m_bSortOperators = !m_bSortOperators;
	m_bChanged = true;
}

void CMainFrame::OnUpdateEditSortOperators(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bSortOperators);
}

void CMainFrame::OnUpdateFileSavezip(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( !m_bInit );
}

void CMainFrame::OnFileSavezip()
{
	SaveFile( true, false, L"");
}
