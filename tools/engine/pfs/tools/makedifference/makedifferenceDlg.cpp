// makedifferenceDlg.cpp : implementation file
//

#include "stdafx.h"
#include "makedifference.h"
#include "makedifferenceDlg.h"
#include <algorithm>
#include "../packzip/BrowsePath.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


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


// CMakeDifferenceDlg dialog


CMakeDifferenceDlg::CMakeDifferenceDlg(CWnd* pParent /*=NULL*/)
: CDialog(CMakeDifferenceDlg::IDD, pParent)
, m_scale( 1.0f)
, m_allcount( 0)
, m_savecount( 0)
, m_bMergePatch( false)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CMakeDifferenceDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_EDIT1, m_editLastPfs);
	DDX_Control(pDX, IDC_BUTTON1, m_btnLastPfs);
	DDX_Control(pDX, IDC_EDIT2, m_editCurPfs);
	DDX_Control(pDX, IDC_BUTTON2, m_btnCurPfs);
	DDX_Control(pDX, IDC_PROGRESS1, m_pcProgress);
	DDX_Control(pDX, IDC_EDIT3, m_editInfo);
	DDX_Control(pDX, IDOK, m_btnOk);
	DDX_Control(pDX, IDC_BUTTON3, m_btnCancel);
	DDX_Control(pDX, IDC_EDIT4, m_editProgress);
	DDX_Control(pDX, IDC_LIST1, m_lcLastPfs);
	DDX_Control(pDX, IDC_EDIT5, m_editSavePath);
	DDX_Control(pDX, IDC_BUTTON4, m_btnSavePath);
	DDX_Control(pDX, IDC_BUTTON5, m_btnDeleteLastPfs);
	DDX_Control(pDX, IDC_CHECK_MAKESERVER, m_cbMakeServer);
	DDX_Control(pDX, IDC_CHECK_ISZIP, m_cbIsZip);
	DDX_Control(pDX, IDC_COMBO1, m_cbbExtension);
}

BEGIN_MESSAGE_MAP(CMakeDifferenceDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
	ON_BN_CLICKED(IDC_BUTTON1, &CMakeDifferenceDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON2, &CMakeDifferenceDlg::OnBnClickedButton2)
	ON_BN_CLICKED(IDOK, &CMakeDifferenceDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDC_BUTTON3, &CMakeDifferenceDlg::OnBnClickedButton3)

	ON_MESSAGE( WM_MAKEPROGRESS, OnMakeProgress)
	ON_MESSAGE( WM_BROWSELASTPFSPATH, OnBrowseLastPfsPath)
	ON_BN_CLICKED(IDC_BUTTON4, &CMakeDifferenceDlg::OnBnClickedButton4)
	ON_BN_CLICKED(IDC_BUTTON5, &CMakeDifferenceDlg::OnBnClickedButton5)
END_MESSAGE_MAP()


// CMakeDifferenceDlg message handlers
LPCTSTR g_section = L"setting";
LPCTSTR g_lastpfspath = L"lastpfspath";
LPCTSTR g_savepath = L"savepath";

BOOL CMakeDifferenceDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

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

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon
	
	m_lcLastPfs.SetExtendedStyle( LVS_EX_FULLROWSELECT);
	m_lcLastPfs.InsertColumn( 0, L"文件名", LVCFMT_LEFT, 280);
	m_lcLastPfs.InsertColumn( 1, L"版本号", LVCFMT_LEFT, 80);
	if( m_bMergePatch)
	{
		m_lcLastPfs.InsertColumn( 2, L"旧版本号", LVCFMT_LEFT, 80);
		m_cbMakeServer.ShowWindow( SW_HIDE);

		SetWindowText( L"合并差异包");
		GetDlgItem( IDC_STATIC_OLDVERSION)->SetWindowText( L"选择差异包路径：");
	}
	else
	{
		//m_cbMakeServer.SetCheck( BST_CHECKED);
	}

	// TODO: Add extra initialization here
	m_makedifferencelistener.SetMessageId( WM_MAKEPROGRESS);
	m_makedifferencethread.SetListener( &m_makedifferencelistener);
	m_mergePatchThread.SetListener( &m_makedifferencelistener);

	m_browselastpfspathlistener.SetMessageId( WM_BROWSELASTPFSPATH);
	m_browselastpfspaththread.SetListener( &m_browselastpfspathlistener);
	EnableButton( TRUE);

	m_lastpfspath = AfxGetApp()->GetProfileString( g_section, g_lastpfspath).GetString();
	m_editLastPfs.SetWindowText( m_lastpfspath.c_str());
	m_savepath = AfxGetApp()->GetProfileString( g_section, g_savepath).GetString();
	m_editSavePath.SetWindowText( m_savepath.c_str());
	//if( !m_lastpfspath.empty())
	//{
	//	BrowseLastPfsPath();
	//}
	m_cbbExtension.SetCurSel( 0);

	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CMakeDifferenceDlg::OnSysCommand(UINT nID, LPARAM lParam)
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

void CMakeDifferenceDlg::OnPaint()
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
HCURSOR CMakeDifferenceDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}


void CMakeDifferenceDlg::OnBnClickedButton1()
{
	// TODO: Add your control notification handler code here
	wchar_t szFilter[MAX_PATH] = {0};
	if( m_bMergePatch)
	{
		_tcscpy_s( szFilter, 
			L"cpch file|*.cpch|all files|*.*|"
			);
	}
	else
	{
		_tcscpy_s( szFilter, L"pfs file|*.pfs|all files|*.*|");
	}
	CFileDialog dlgFile( TRUE, NULL, NULL, OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, szFilter);
	if( IDCANCEL == dlgFile.DoModal())
		return;
	{
		CString str = dlgFile.GetPathName();
		m_editLastPfs.SetWindowText( str);

		int index = m_lcLastPfs.InsertItem( m_lcLastPfs.GetItemCount(), str);
		m_lcLastPfs.SetItemText( index, 1, L"正在分析...");

		m_lastpfspath = str.GetString();
		BrowseLastPfsPath();

		AfxGetApp()->WriteProfileString( g_section, g_lastpfspath, m_lastpfspath.c_str());
	}
}

void CMakeDifferenceDlg::OnBnClickedButton2()
{
	// TODO: Add your control notification handler code here
	wchar_t szFilter[] = L"pfs file|*.pfs|all files|*.*|";
	CFileDialog dlgFile( TRUE, L"pfs", NULL, OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, szFilter);
	if( IDCANCEL == dlgFile.DoModal())
		return;
	m_editCurPfs.SetWindowText( dlgFile.GetPathName());
}

void CMakeDifferenceDlg::OnBnClickedOk()
{
	// TODO: Add your control notification handler code here
	//OnOK();
	if( m_bMergePatch)
	{
		if( !CheckAllPatch())
		{
			return;
		}
	}
	else
	{
		if( !CheckAllSetupPfs())
		{
			return;
		}
	}

	CString strCur;
	m_editCurPfs.GetWindowText( strCur);
	if( strCur.IsEmpty())
	{
		AfxMessageBox( L"请先选择当前版本安装包的路径！");
		m_btnCurPfs.SetFocus();
		return;
	}

	CString strSave;
	m_editSavePath.GetWindowText( strSave);
	if( strSave.IsEmpty())
	{
		AfxMessageBox( L"请先选择保存差异包的路径！");
		m_btnSavePath.SetFocus();
		return;
	}

	CString strExtension;
	m_cbbExtension.GetWindowText( strExtension);
	if( strExtension.IsEmpty())
	{
		AfxMessageBox( L"请先选择差异包的文件后缀名！");
		m_cbbExtension.SetFocus();
		return;
	}
	
	if( m_browselastpfspaththread.IsRunning())
	{
		AfxMessageBox( L"正在分析包，请稍后再进行生成操作！");
		return;
	}
	m_makedifferencelistener.SetHWnd( GetSafeHwnd());
	CErrorStringArray::GetInstance().Clear();

	if( m_bMergePatch)
	{
		m_mergePatchThread.Stop();
		m_mergePatchThread.SetFileName( m_browselastpfspaththread.GetFileInfoMap(), 
			strCur.GetString(), strSave.GetString());
		m_mergePatchThread.SetIsZip( BST_CHECKED == m_cbIsZip.GetCheck());
		m_mergePatchThread.SetExtension( strExtension.GetString());

		m_mergePatchThread.Start();
	}
	else
	{
		m_makedifferencethread.Stop();
		m_makedifferencethread.SetFileName( m_browselastpfspaththread.GetFileInfoMap(), 
			strCur.GetString(), strSave.GetString());
		m_makedifferencethread.SetIsMakeServer( BST_CHECKED == m_cbMakeServer.GetCheck());
		m_makedifferencethread.SetIsZip( BST_CHECKED == m_cbIsZip.GetCheck());
		m_makedifferencethread.SetExtension( strExtension.GetString());

		m_makedifferencethread.Start();
	}
	
	m_editInfo.SetWindowText( L"");
	m_editProgress.SetWindowText( L"");
	m_pcProgress.SetPos( 0);

	EnableButton( FALSE);
}


void CMakeDifferenceDlg::OnBnClickedButton3()
{
	// TODO: Add your control notification handler code here
	if( StopMakeDifferenceThread())
	{
		EnableButton( TRUE);
	}
}

HRESULT CMakeDifferenceDlg::OnMakeProgress( WPARAM wParam, LPARAM lParam)
{
	switch( wParam)
	{
	case INFO:
		{
			std::wstring info;
			CErrorStringArray::GetInstance().Pop( info);
			if( !info.empty())
			{
				m_editInfo.SetWindowText( info.c_str());
			}
		}
		break;
	case FILECOUNT:
		{
			m_allcount = lParam;
			m_savecount = 0;
			SetProgressEditText();
			m_pcProgress.SetRange( 0, 100);

			if( lParam)
				m_scale = 100.0f / lParam;
			else
				m_scale = 1.0f;
		}
		break;
	case FILEPROGRESS:
		{
			m_savecount++;
			SetProgressEditText();
			int pos = int( m_savecount * m_scale);
			if( pos != m_pcProgress.GetPos())
			{
				m_pcProgress.SetPos( pos);
			}
		}
		break;
	case END:
		{
			EnableButton( TRUE);
			if( lParam)
			{
				m_editInfo.SetWindowText( L"生成成功");
			}
		}
		break;
	}
	return 0;
}

void CMakeDifferenceDlg::EnableBrowseButton( BOOL enable)
{
	m_btnLastPfs.EnableWindow( enable);
	m_btnDeleteLastPfs.EnableWindow( enable);
}

void CMakeDifferenceDlg::EnableButton( BOOL enable)
{
	EnableBrowseButton( enable);
	m_btnCurPfs.EnableWindow( enable);
	m_btnOk.EnableWindow( enable);
	m_btnSavePath.EnableWindow( enable);

	m_cbMakeServer.EnableWindow( enable);
	m_cbIsZip.EnableWindow( enable);
	m_cbbExtension.EnableWindow( enable);

	m_btnCancel.EnableWindow( !enable);
}

bool CMakeDifferenceDlg::StopMakeDifferenceThread()
{
	if( m_makedifferencethread.IsRunning())
	{
		m_makedifferencethread.Suspend();
		if( IDNO == AfxMessageBox( L"正在生成差异包，强行取消可能会丢失数据，要继续么？", MB_YESNO|MB_DEFBUTTON2))
		{
			m_makedifferencethread.Resume();
			return false;
		}
		m_makedifferencelistener.SetHWnd( NULL);
		m_makedifferencethread.Resume();
		m_makedifferencethread.Stop( 30000);
		//CErrorStringArray::GetInstance().Clear();
		m_editInfo.SetWindowText( L"失败，用户终止");
	}
	return true;
}

void CMakeDifferenceDlg::SetProgressEditText()
{
	CString str;
	str.Format( L"%d / %d", m_savecount, m_allcount);
	m_editProgress.SetWindowText( str);
}

void CMakeDifferenceDlg::OnCancel()
{
	// TODO: Add your specialized code here and/or call the base class
	if( StopMakeDifferenceThread())
	{
		CDialog::OnCancel();
		m_browselastpfspaththread.Stop();
		m_browselastpfspaththread.Clear();
	}
}

void CMakeDifferenceDlg::BrowseLastPfsPath()
{
	//m_lcLastPfs.DeleteAllItems();
	//CErrorStringArray::GetInstance().Clear();

	m_browselastpfspaththread.Stop();
	m_browselastpfspathlistener.SetHWnd( GetSafeHwnd());
	m_browselastpfspaththread.SetLastPfsPath( m_lastpfspath);
	m_browselastpfspaththread.Start();

	EnableBrowseButton( FALSE);
}

HRESULT CMakeDifferenceDlg::OnBrowseLastPfsPath( WPARAM wParam, LPARAM lParam)
{
	std::wstring str;
	CErrorStringArray::GetInstance().Pop( str);
	std::wstring version, lastVersion;
	CErrorStringArray::GetInstance().Pop( version);
	CErrorStringArray::GetInstance().Pop( lastVersion);

	for( int i=0; i<m_lcLastPfs.GetItemCount(); i++)
	{
		CString filename = m_lcLastPfs.GetItemText( i, 0);
		if( filename.CompareNoCase( str.c_str()) == 0)
		{
			m_lcLastPfs.SetItemText( i, 1, version.c_str());
			if( m_bMergePatch)
			{
				m_lcLastPfs.SetItemText( i, 2, lastVersion.c_str());
			}
			break;
		}
	}

	EnableBrowseButton( TRUE);
	return 0;
}

void CMakeDifferenceDlg::OnBnClickedButton4()
{
	// TODO: Add your control notification handler code here
	CBrowsePath browser( GetSafeHwnd());
	browser.SetIniPath( m_savepath);
	if( browser.DoModal())
	{
		m_savepath = browser.GetSelectPath();
		m_editSavePath.SetWindowText( m_savepath.c_str());

		AfxGetApp()->WriteProfileString( g_section, g_savepath, m_savepath.c_str());
	}
}

void CMakeDifferenceDlg::OnBnClickedButton5()
{
	// TODO: Add your control notification handler code here
	POSITION pos = m_lcLastPfs.GetFirstSelectedItemPosition();
	if (pos == NULL)
		AfxMessageBox(L"Please select item to delete");
	else
	{
		for (int i=m_lcLastPfs.GetItemCount(); i>=0; i--)
		{
			if( m_lcLastPfs.GetItemState(i, LVIS_SELECTED) == LVIS_SELECTED)
			{
				CString strFile, strVersion;
				strFile = m_lcLastPfs.GetItemText( i, 0);
				//strVersion = m_lcLastPfs.GetItemText( i, 1);
				if( m_browselastpfspaththread.DeleteFileItem( strFile.GetString()))
				{
					
				}
				m_lcLastPfs.DeleteItem( i);
			}
		}
	}
}

// 生成差异包时检查旧版本安装包是否符合要求
bool CMakeDifferenceDlg::CheckAllSetupPfs()
{
	int count = m_lcLastPfs.GetItemCount();
	if( count <= 0)
	{
		AfxMessageBox( L"请先选择旧版本安装包的路径！");
		m_btnLastPfs.SetFocus();
		return false;
	}

	CString str;
	for( int i=0; i<count-1; i++)
	{
		str = m_lcLastPfs.GetItemText( i, 1);
		for( int j=i+1; j<count; j++)
		{
			if( str == m_lcLastPfs.GetItemText( j, 1))
			{
				CString strTemp;
				strTemp.Format( L"第 %d 行和第 %d 行的版本号一样，输出的差异包会覆盖，请删除其中一行后重试", i, j);
				AfxMessageBox( strTemp);
				return false;
			}
		}
	}
	return true;
}

// 合并差异包时检查所有选择的差异包是否符合要求
bool CMakeDifferenceDlg::CheckAllPatch()
{
	int count = m_lcLastPfs.GetItemCount();
	if( count <= 1)
	{
		AfxMessageBox( L"至少需要两个差异包才能进行合并，请添加差异包的路径！");
		m_btnLastPfs.SetFocus();
		return false;
	}

	CString str;
	CString maxVersion;
	int maxIndex = -1;
	for( int i=0; i<count; i++)
	{
		str = m_lcLastPfs.GetItemText( i, 1);
		if( maxVersion.IsEmpty())
		{
			maxVersion = str;
			maxIndex = i;
		}
		else if( str > maxVersion)
		{
			maxVersion = str;
			maxIndex = i;
		}
		for( int j=i+1; j<count; j++)
		{
			if( str == m_lcLastPfs.GetItemText( j, 1))
			{
				CString strTemp;
				strTemp.Format( L"第 %d 行和第 %d 行的版本号一样，请替换相应文件后重试", i, j);
				AfxMessageBox( strTemp);
				return false;
			}
		}
	}

	CString minVersion;
	int minIndex = -1;
	for( int i=0; i<count; i++)
	{
		str = m_lcLastPfs.GetItemText( i, 2);
		if( minVersion.IsEmpty())
		{
			minVersion = str;
			minIndex = i;
		}
		else if( str < minVersion)
		{
			minVersion = str;
			minIndex = i;
		}
		for( int j=i+1; j<count; j++)
		{
			if( str == m_lcLastPfs.GetItemText( j, 2))
			{
				CString strTemp;
				strTemp.Format( L"第 %d 行和第 %d 行的旧版本号一样，请替换相应文件后重试", i, j);
				AfxMessageBox( strTemp);
				return false;
			}
		}
	}

	while( minIndex != maxIndex)
	{
		CString strVersion = m_lcLastPfs.GetItemText( minIndex, 1);
		int curIndex = -1;
		for( int i=0; i<count; i++)
		{
			if( i == minIndex)
			{
				continue;
			}
			if( strVersion == m_lcLastPfs.GetItemText( i, 2))
			{
				curIndex = i;
				break;
			}
		}
		if( curIndex == -1)
		{
			CString strTemp;
			strTemp.Format( L"差异包版本不连续，旧版本号为 %s 的差异包没找到，请添加相应文件后重试", strVersion);
			AfxMessageBox( strTemp);
			return false;
		}
		minIndex = curIndex;
	}
	return true;
}