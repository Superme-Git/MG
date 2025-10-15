// CheckClientDlg.cpp : implementation file
//

#include "stdafx.h"
#include "CheckClient.h"
#include "CheckClientDlg.h"
#include "../Patcher/GlobalFunction.h"
#include "../../pfs/setupmetafile.h"
#include "../Patcher/Profile.h"
#include "../Patcher/TextManager.h"
#include "../Patcher/TextID.h"
#include "../Patcher/LauncherConfig.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#define WM_KICKIDLE         0x036A

BOOL _PumpMessage()
{
	_AFX_THREAD_STATE *pState = AfxGetThreadState();

	// ע������������AfxPumpMessage()ʵ�֣����˴�����PeekMessage������ʹ��GetMessage()
	if (!::PeekMessage(&(pState->m_msgCur), NULL, NULL, NULL, PM_REMOVE))
	{
		return FALSE;
	}

	// process this message
	if (pState->m_msgCur.message != WM_KICKIDLE && !AfxPreTranslateMessage(&(pState->m_msgCur)))
	{
		::TranslateMessage(&(pState->m_msgCur));
		::DispatchMessage(&(pState->m_msgCur));
	}

	return TRUE;
}

void WaitThreadUntilQuit( HANDLE& rhThread )
{
	if ( rhThread != INVALID_HANDLE_VALUE )
	{
		DWORD dwRet = 0;
		DWORD dwExitCode = 0;
		BOOL bRet = TRUE;
		bRet = GetExitCodeThread( rhThread, &dwExitCode );
		if ( bRet && dwExitCode == STILL_ACTIVE )
		{
			int iTryCount = 120;
			do 
			{
				dwRet = WaitForSingleObject(rhThread,1000);
				_PumpMessage();

			} while ( dwRet == WAIT_TIMEOUT && iTryCount-- > 0 );

			if( dwRet == WAIT_TIMEOUT )
			{
				// �ȴ���ʱ��ֻ��ǿ����ֹ�̣߳�һ��ᵼ���ڴ�й¶
				TerminateThread( rhThread, -1 );
			}
			else if( dwRet == WAIT_OBJECT_0 )
			{
				// �߳�������ֹ��
			}
			else// if ( dwRet == WAIT_FAILED )
			{
				// ��������
			}
		}

		CloseHandle( rhThread );
		rhThread = INVALID_HANDLE_VALUE;
	}
}

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


// CCheckClientDlg dialog

CCheckClientDlg::CCheckClientDlg(CWnd* pParent /*=NULL*/)
: CDialog(CCheckClientDlg::IDD, pParent)
, m_bResetConfig(FALSE)
, m_bCheckExeFile( FALSE)
, m_checkthread( NULL)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

CCheckClientDlg::~CCheckClientDlg()
{
}

void CCheckClientDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_STATIC_GLOBAL, m_staticGlobal);
	DDX_Control(pDX, IDC_STATIC_FILE, m_staticFile);
	DDX_Control(pDX, IDC_PROGRESS_CHECK, m_progressCheck);
	DDX_Check( pDX, IDC_CHECK_CHANGEFILE, m_bResetConfig);
	DDX_Control(pDX, IDC_COMBO1, m_cbServer);
	DDX_Control(pDX, IDC_EDIT_DOWNLOAD, m_editDownload);
	DDX_Control(pDX, IDC_LIST_CHECK, m_lcCheck);
}

BEGIN_MESSAGE_MAP(CCheckClientDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_WM_DESTROY()
	ON_MESSAGE( WM_GLOBALSTATUS, OnGlobalStatus)
	ON_MESSAGE( WM_PROGRESS_START, OnProgressStart)
	ON_MESSAGE( WM_PROGRESS_STEP, OnProgressStep)
	ON_MESSAGE( WM_PROGRESS_END, OnProgressEnd)
	ON_MESSAGE( WM_REPORTRESULT, OnReportResult)
	ON_BN_CLICKED(IDC_BUTTON_CHECK, &CCheckClientDlg::OnBnClickedButtonCheck)
	ON_NOTIFY(NM_RCLICK, IDC_LIST_CHECK, &CCheckClientDlg::OnNMRclickListCheck)
	ON_COMMAND(ID_CHECKLIST_SELECT_ALL, &CCheckClientDlg::OnChecklistSelectAll)
	ON_COMMAND(ID_CHECKLIST_REVERT_SELECTION, &CCheckClientDlg::OnChecklistRevertSelection)
END_MESSAGE_MAP()

// CCheckClientDlg message handlers

BOOL CCheckClientDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Ϊ��֧�ֹ��ʻ���֧�ֶ����Ʒ���������ȴ������ļ��ж�ȡ���ڱ���.
	// ע�⣬������ֱ��뱣֤��launcher.xml�е���ͬID���ַ�����ͬ��
	const std::wstring& strCaptionRepair = GlobalGetText(TOK_PRODUCT_REPAIR);
	if( !strCaptionRepair.empty() )
	{
		SetWindowText(strCaptionRepair.c_str());
	}
	
	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	// ���ڶԻ���ֻ���������Ե�ϵͳ�г��֡�
	
	// ��ȡ�û�ȱʡUI����
	LANGID langId = GetUserDefaultUILanguage();
	WORD prilangId = PRIMARYLANGID(langId); // ������
	if( prilangId == LANG_CHINESE )// �������İ��û�UI �� �������İ��û�UI
	{		
		// ���ڶԻ���ı��ػ���Ȼ�����⡣
		CMenu* pSysMenu = GetSystemMenu(FALSE);
		if (pSysMenu != NULL)
		{
			CString strAboutMenu;
			strAboutMenu.LoadString(IDS_ABOUTBOX);
			if (!strAboutMenu.IsEmpty())
			{
				pSysMenu->AppendMenu(MF_SEPARATOR);
				pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
			}
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	{
		// ����ļ�
		const std::wstring& strText = GlobalGetText(TOK_BUTTON_CHECK_FILES);
		if( !strText.empty() )
			SetDlgItemText(IDC_BUTTON_CHECK, strText.c_str() );
	}
	{
		// �ر�
		const std::wstring& strText = GlobalGetText(TOK_BUTTON_CLOSE );
		if( !strText.empty() )
			SetDlgItemText(IDCANCEL, strText.c_str() );
	}

	{
		// ���á�%1!s!�����÷��� (��Ҫ���½�����Ϸ���ü���������)
		const std::wstring& strTextProductName = GlobalGetText(TOK_PRODUCT_NAME);
		const std::wstring& strText = GlobalFormatText(TOK_LABEL_RESET_CONFIG, strTextProductName.c_str() );
		if( !strText.empty() )
			SetDlgItemText(IDC_CHECK_CHANGEFILE, strText.c_str() );
	}
	{	// ѡ����·�������
		const std::wstring& strText = GlobalGetText(TOK_LABEL_SELECT_UPDATE_SERVER);
		if( !strText.empty() )
			SetDlgItemText(IDC_STATIC_SELECT_UPDATE_SERVER, strText.c_str() );
	}

	{
		// ��ʼ����ļ�...
		const std::wstring& strText = GlobalGetText(TOK_LABEL_START_CHECKING);
		if( !strText.empty() )
			SetDlgItemText(IDC_STATIC_GLOBAL, strText.c_str() );
	}

	{
		// ���ڼ���ļ�... // �������á�
		m_staticFile.SetWindowText( NULL);
	}

	LPCTSTR pszAppPath = NULL, pszAppFile = NULL;
	pszAppPath = GetInstallDir( pszAppFile);
	if( (!(NULL == pszAppPath) || (0 == pszAppPath[0]) || (NULL == pszAppFile) || ( 0 == pszAppFile[0])))
	{
		// ע�⣬�����Ǵӱ��صİ汾�ļ��л�ȡ�ķ������б���Ϣ��
		PFSX::CVersionInfo _localVersion;

		if( !PFSX::GetLocalVersion( pszAppPath, _localVersion) )
		{
			// .version.meta �ļ�������Ҳ������֤
			upd::SetLastError( PFS::CEnv::GetLastError(), GlobalGetText( ERR_READ_LOCAL_VERSION) );

			// ����ȱʡ�ķ�������
			m_cbServer.AddString( L"�Զ����·�����");
			m_cbServer.SetCurSel( 0);
		}
		else
		{
			std::set<std::wstring> networks; // �����ظ���

			bool bAnyNetworkNamed = false;
			// �������·������ṩ�İ汾�����б�
			const PFSX::CVersionInfo::VersionUpdateInfoMap & rMap = _localVersion.GetUpdateInfoMap();
			for( PFSX::CVersionInfo::VersionUpdateInfoMap::const_iterator itr = rMap.begin();
				itr != rMap.end(); ++ itr)
			{
				if( ( itr->second.Type % PFSX::VERSION_COUNT ) == PFSX::VERSION_DEFAULT )
				{
					if( !itr->second.Network.empty() )
					{
						if( networks.find(itr->second.Network) == networks.end() )
						{
							networks.insert(itr->second.Network);
							m_cbServer.AddString( itr->second.Network.c_str() );
							bAnyNetworkNamed = true;
						}
					}
				}
			}

			// ���û���κ������������ṩ�̣�����URL�ж��������������IP��ַ��
			if( !bAnyNetworkNamed )
			{
				for( PFSX::CVersionInfo::VersionUpdateInfoMap::const_iterator itr = rMap.begin();
					itr != rMap.end(); ++ itr)
				{
					if( ( itr->second.Type % PFSX::VERSION_COUNT ) == PFSX::VERSION_DEFAULT )
					{
						if( !itr->second.Url.empty() )
						{
							std::wstring hostname;
							if( GetHostNameFromUrl( itr->second.Url.c_str(), hostname ) )
							{
								if( networks.find(hostname) == networks.end() )
								{
									networks.insert(hostname);
									m_cbServer.AddString( hostname.c_str() );
									bAnyNetworkNamed = true;
								}
							}
						}
					}
				}
			}

			if( !bAnyNetworkNamed )
			{
				m_cbServer.AddString( L"�Զ����·�����");
			}

			m_cbServer.SetCurSel( 0);
		}	
	}

	m_progressCheck.SetStep( 1);
	m_watch.SetHwnd( GetSafeHwnd());
	GlobalRegister( &m_watch);

	GetWindowRect( &m_rcExpand);
	m_rcExpand.OffsetRect( -m_rcExpand.left, -m_rcExpand.top);

	m_editDownload.SetLimitText( -1);

	m_lcCheck.SetExtendedStyle( LVS_EX_CHECKBOXES|LVS_EX_FULLROWSELECT );
	m_lcCheck.InsertColumn( 0, GlobalGetText( TIP_SELECT_CHECK_ITEM).c_str(), LVCFMT_LEFT, 300);
	InsertCheckItems();

	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CCheckClientDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CCheckClientDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CCheckClientDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

bool CCheckClientDlg::StartCheck( )
{
	// �����Ϸ�ͻ����Ƿ�������
	LPCWSTR lpszInstallationName = GetInstallationMutexName();
	if( NamedMutexExists( lpszInstallationName ) )
	{
		//AfxMessageBox( L"��\"ħ������\"�ͻ��˳����������У��������˳��ó���Ȼ�����³��ԣ�" );
		AfxMessageBox( GlobalGetText( ERR_CLIENT_IS_RUNNING).c_str() );
		return false;
	}

	LPCTSTR pszAppPath = NULL, pszAppFile = NULL;
	pszAppPath = GetInstallDir( pszAppFile);
	if( (NULL == pszAppPath) || (0 == pszAppPath[0]) || (NULL == pszAppFile) || ( 0 == pszAppFile[0]))
	{
		return false;
	}

	// ��� .file.meta .setup.meta .version.meta �ļ��Ƿ�����Ҳ���Ϊ����
	std::wstring filename = pszAppPath;
	filename += PFSX::FILES_META_NAME;
	if( PFS::CFileIO::IsFileExisting( filename) )
	{
		if( !CheckOneNecessaryFile( filename))
		{
			return false;
		}
	}
	
	filename = pszAppPath;
	filename += PFSX::VERSION_META_NAME;
	if( PFS::CFileIO::IsFileExisting( filename) )
	{
		if( !CheckOneNecessaryFile( filename))
		{
			return false;
		}
	}

	filename = pszAppPath;
	filename += PFSX::SETUP_META_NAME;
	if( PFS::CFileIO::IsFileExisting( filename) )
	{
		if( !CheckOneNecessaryFile( filename))
		{
			return false;
		}
	}
	else
	{
		//CString str;
		//str.Format( L"%s �ļ������ڣ�����Ҫ����ִ�����ΰ汾��֤��ɿͻ����޸���\n"
		//	L"��һ��ѡ��[�����ļ�]���а汾У��,У����ɺ���ѡ��������Դ���ļ����а汾У��.", filename.c_str());
		AfxMessageBox( GlobalFormatText( TIP_CHECK_FILE_NOT_EXIST, filename.c_str()).c_str());
	}

	bool bAllUnCheck = true;
	for( int i=0; i<m_lcCheck.GetItemCount(); i++)
	{
		if( m_lcCheck.GetCheck( i))
		{
			bAllUnCheck = false;
			break;
		}
	}
	if( bAllUnCheck)
	{
		AfxMessageBox( GlobalGetText( ERR_AT_LEAST_SELECT_ONE_LINE).c_str() );
		return false;
	}

	m_checkthread = (CCheckThread*)AfxBeginThread( RUNTIME_CLASS(CCheckThread),
		THREAD_PRIORITY_BELOW_NORMAL, 0, CREATE_SUSPENDED, NULL);
	if ( NULL == m_checkthread)
	{
		return false;
	}

	filename = pszAppPath;
	filename += L"\\";
	filename += GetLauncherFolder();
	filename += L"\\patcher.ini";
	CProfile::GetInstance().SetFileName( filename.c_str());
	
	UpdateData( TRUE);
	m_editDownload.SetWindowText( L"");

	m_bCheckExeFile = m_lcCheck.GetCheck( 0);
	PFSX::SetupMetaVector smvChecks;
	PFSX::SetupMetaInfo smi;
	for( int i=1; i<m_lcCheck.GetItemCount(); i++)
	{
		if( m_lcCheck.GetCheck( i))
		{
			filename = m_lcCheck.GetItemText( i, 0).GetString();
			filename.erase( filename.rfind( L'.'));
			filename.insert( 0, L"/");
			smi.SetName( filename);

			smvChecks.push_back( smi);
		}
	}

	std::sort( smvChecks.begin(), smvChecks.end() );

	m_checkthread->SetAppPath( pszAppPath, pszAppFile, !!m_bCheckExeFile, 
		!!m_bResetConfig, m_cbServer.GetCurSel(), smvChecks);
	m_listener.SetHWnd( GetSafeHwnd());
	m_checkthread->SetListener( m_listener);

	// �����߳�����
	m_checkthread->ResumeThread();

	return true;
}

LRESULT CCheckClientDlg::OnGlobalStatus( WPARAM wParam, LPARAM lParam)
{
	Message theMessage;
	if( GlobalGetMessage( (MessageId)lParam, &theMessage ) )
	{
		std::wstring& message = theMessage.strDetail;
		if( message[0] == L'#')		// ������ʾ��Ϣ�� # �ſ�ͷ
		{
			message.append( L"\r\n");
			m_editDownload.SetSel( m_editDownload.GetWindowTextLength(), m_editDownload.GetWindowTextLength());
			m_editDownload.ReplaceSel( message.c_str() + 1);
		}
		else
		{
			m_staticGlobal.SetWindowText( message.c_str());
		}
	}

	return 0;
}

LRESULT CCheckClientDlg::OnProgressStart( WPARAM wParam, LPARAM lParam)
{
	m_progressCheck.SetRange32( 0, (int)lParam);
	m_progressCheck.SetPos( 0);
	return 0;
}

LRESULT CCheckClientDlg::OnProgressStep( WPARAM wParam, LPARAM lParam)
{
	m_progressCheck.StepIt();

	Message theMessage;
	if( GlobalGetMessage( (MessageId)lParam, &theMessage ) )
	{
		if( !CCheckThread::s_checkend)
		{
			m_staticFile.SetWindowText( theMessage.strDetail.c_str());
		}
	}
	return 0;
}

LRESULT CCheckClientDlg::OnProgressEnd( WPARAM wParam, LPARAM lParam)
{
	int nLower, nUpper;
	m_progressCheck.GetRange( nLower, nUpper);
	m_progressCheck.SetPos( nUpper );
	m_staticFile.SetWindowText( NULL);
	return 0;
}

void CCheckClientDlg::OnOK()
{
	//CDialog::OnOK();
}

void CCheckClientDlg::OnCancel()
{
	if( m_checkthread)
	{
		HANDLE hThread = m_checkthread->m_hThread;
		if( WAIT_TIMEOUT == ::WaitForSingleObject( hThread, 0))
		{
			m_checkthread->ResetMergeEvent();

			CString str;
			if( m_checkthread->IsMerging())
			{
				str = GlobalGetText( QST_STOP_CHECK).c_str();
			}
			else
			{
				str = GlobalGetText( QST_STOP_CHECK2).c_str();
			}
			if( IDNO == AfxMessageBox( str, MB_YESNO|MB_ICONWARNING|MB_DEFBUTTON2 ) )
			{
				// ����RunModalLoop()ѭ��
				m_nFlags |= WF_CONTINUEMODAL;

				m_checkthread->SetMergeEvent();
				return ;
			}	
			m_checkthread->SetMergeEvent();
		}
		//m_checkthread->Stop();
		ShowWindow( SW_HIDE);
	}
	CDialog::OnCancel();
}

void CCheckClientDlg::OnDestroy()
{
	GlobalUnRegister( &m_watch);

	if( m_checkthread)
	{
		// �ȴ��߳��˳�
		m_checkthread->Stop();
		HANDLE hThread = m_checkthread->m_hThread;
		if( WAIT_TIMEOUT == ::WaitForSingleObject( hThread, 0))
		{
			HANDLE hProcess = GetCurrentProcess();
			DuplicateHandle( hProcess, hThread, hProcess, &hThread, NULL, FALSE, DUPLICATE_SAME_ACCESS );
			WaitThreadUntilQuit( hThread );
		}
	}

	CDialog::OnDestroy();
}

LRESULT CCheckClientDlg::OnReportResult( WPARAM wParam, LPARAM lParam)
{
	if( wParam)
	{
		if( lParam)
		{
			AfxMessageBox( GlobalGetText( TIP_SUCCESS_CHECK_ALL_FILES).c_str() );
		}
		else
		{
			AfxMessageBox( GlobalGetText( TIP_SUCCESS_CHECK_SELECT_FILES).c_str() );
		}
		InsertCheckItems();
	}
	else
	{
		const upd::ErrorInfo& rErrorInfo = upd::GetLastError();
		if( SC_USER_CANCELED == rErrorInfo.ErrorCode || 0 == rErrorInfo.ErrorCode)
		{
			return 0;
		}
		AfxMessageBox( GlobalFormatText( ERR_CHECK, rErrorInfo.ErrorDetail.c_str(), rErrorInfo.ErrorCode).c_str());
	}

	m_checkthread = NULL;
	EnableButton( TRUE);

	return 0;
}

void CCheckClientDlg::OnBnClickedButtonCheck()
{
	StartCheckImpl( );
}

void CCheckClientDlg::EnableButton( BOOL bEnable)
{
	GetDlgItem( IDC_BUTTON_CHECK)->EnableWindow( bEnable);
	GetDlgItem( IDC_CHECK_CHANGEFILE)->EnableWindow( bEnable);
	m_cbServer.EnableWindow( bEnable);
	m_lcCheck.EnableWindow( bEnable);
}

bool CCheckClientDlg::StartCheckImpl( )
{
	// Disable "Check" Button
	EnableButton( FALSE );
	if( !StartCheck( ) )
	{
		// If start check failed�� enable "Check" Button.
		EnableButton( TRUE );
	}
	return true;
}

bool CCheckClientDlg::CheckOneNecessaryFile( const std::wstring& filename)
{
	if( !ShowHiddenFile( filename))
	{
		AfxMessageBox( GlobalFormatText( ERR_CHECK_FILE_NOT_EXIST2, filename.c_str()).c_str() );
		return false;
	}
	return true;
}

void CCheckClientDlg::InsertCheckItems()
{
	m_lcCheck.DeleteAllItems();
	
	const std::wstring& strProgramFiles = GlobalGetText( TIP_PROGRAME_FILES );

	int index = 0;
	if( !strProgramFiles.empty() )
		index = m_lcCheck.InsertItem( 0, strProgramFiles.c_str());
	else
		index = m_lcCheck.InsertItem( 0, L"Program Files(�����ļ�)" );

	m_lcCheck.SetCheck( index, TRUE);

	LPCTSTR pszAppPath = NULL, pszAppFile = NULL;
	pszAppPath = GetInstallDir( pszAppFile);
	std::wstring filename = pszAppPath;
	filename += PFSX::SETUP_META_NAME;
	PFSX::SetupPathVector smv;
	if( PFSX::CSetupMetaFile::DoRead( filename, smv))
	{		
		for( PFSX::SetupPathVector::const_iterator i = smv.begin(), e=smv.end(); i!=e; ++i)
		{
			const std::wstring& name = *i;
			if( !name.empty())
			{
				filename = name.c_str() + 1;
				filename += L".pfs";
				index = m_lcCheck.InsertItem( m_lcCheck.GetItemCount(), filename.c_str());
				m_lcCheck.SetCheck( index, TRUE);
			}
		}
	}
}

void CCheckClientDlg::OnNMRclickListCheck(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;

	CMenu contextMenu;
	if( !contextMenu.CreatePopupMenu() )
	{
		return;
	}

	CMenu* pSubMenu = &contextMenu;

	// ȫѡ
	const std::wstring& strMenuItem = GlobalGetText(TOK_BUTTON_SELECT_ALL);
	if( strMenuItem.empty() )
	{
		return;
	}

	// ��ѡ
	pSubMenu->AppendMenu(MF_STRING, ID_CHECKLIST_SELECT_ALL, strMenuItem.c_str());
	const std::wstring& strMenuItem2 = GlobalGetText(TOK_BUTTON_REVERT_SELECTION);
	if( !strMenuItem2.empty() )
	{
		pSubMenu->AppendMenu(MF_STRING, ID_CHECKLIST_REVERT_SELECTION, strMenuItem2.c_str());
	}

	POINT point;
	GetCursorPos(&point);	
	contextMenu.TrackPopupMenu( TPM_LEFTALIGN|TPM_LEFTBUTTON, point.x, point.y, this, NULL);
	
	*pResult = 1;
}

void CCheckClientDlg::OnChecklistSelectAll()
{
	// ȫѡ������Ŀ
	m_lcCheck.SetCheck(-1);
}

void CCheckClientDlg::OnChecklistRevertSelection()
{
	// ��������Ŀ������ѡ��
	for( int i = 0; i < m_lcCheck.GetItemCount(); i ++)
	{
		if( m_lcCheck.GetCheck(i) )
		{
			m_lcCheck.SetCheck(i, FALSE);
		}
		else
		{
			m_lcCheck.SetCheck(i, TRUE);
		}
	}
}
