// CheckProgressDlg.cpp : implementation file
//

#include "stdafx.h"
#include "install.h"

#include "CheckState.h"
#include "ProgressState.h"
#include "CheckProgressDlg.h"
#include "const.h"
#include "Config.h"
#include "FinishState.h"
#include "GlobalError.h"

// CCheckProgressDlg dialog
IMPLEMENT_DYNAMIC(CCheckProgressDlg, CDialog)

CCheckProgressDlg::CCheckProgressDlg(CWnd* pParent /*=NULL*/)
: CDialog(CCheckProgressDlg::IDD, pParent)
{

}

CCheckProgressDlg::~CCheckProgressDlg()
{
}

void CCheckProgressDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_PROGRESS_TOTAL, m_progress);
}


BEGIN_MESSAGE_MAP(CCheckProgressDlg, CDialog)
	ON_MESSAGE( WM_PROGRESS, OnCheckProgress)
END_MESSAGE_MAP()


// CCheckProgressDlg message handlers

BOOL CCheckProgressDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	// TODO:  Add extra initialization here
	SetWindowText( CConfig::GetStr( CHECKPROGRESS).c_str());
	GetDlgItem( IDCANCEL)->SetWindowText( CConfig::GetStr( CANCEL).c_str());

	m_listener.SetHWnd( GetSafeHwnd());
	std::vector<std::wstring> zipfiles;
	for( CConfig::CPackageVector::iterator itor = CConfig::GetInstance().m_packages.begin(); 
		itor != CConfig::GetInstance().m_packages.end(); ++itor)
	{
		zipfiles.push_back( CInstallState::GetAppPathStr() + L"/" + itor->m_name);
	}
	m_checkthread.SetFileName( zipfiles);
	m_checkthread.SetListener( &m_listener);
	m_checkthread.Start();

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

HRESULT CCheckProgressDlg::OnCheckProgress( WPARAM wParam, LPARAM lParam)
{
	switch( lParam)
	{
	case FILELEN:
		m_scale = 1.0f;
		if( wParam)
			m_scale = 100.0f / wParam;
		m_progress.SetRange( 0, 100);
		m_filewritten = 0;
		m_progress.SetPos( 0);
		break;
	case FILEPROGRESS:
		m_filewritten += wParam;
		m_progress.SetPos( static_cast<int>( m_filewritten*m_scale));
		break;
	case FILEEND:
		OnCheckEnd();
		break;
	}
	return 0;
}

void CCheckProgressDlg::OnCheckEnd( )
{
	CDialog::OnCancel();
	std::vector<std::string>& out = m_checkthread.GetMD5();
	CConfig::CPackageVector& packages = CConfig::GetInstance().m_packages;
	bool bRes = true;
	if( out.size() == packages.size())
	{
		int i = 0;
		for (CConfig::CPackageVector::iterator itor = packages.begin(); itor != packages.end(); ++itor)
		{
			if( itor->m_md5 != out[i])
			{
				bRes = false;
				break;
			}
			i++;
		}
	}
	else
	{
		bRes = false;
	}
	if( bRes)
	{
		CCheckState::GetInstance().ChangeState( CProgressState::GetInstance());
	}
	else
	{
		CGlobalError::GetInstance().SetError( CInstallState::ERROR_CHECK, L"");
		CCheckState::GetInstance().ChangeState( CFinishState::GetInstance());
	}
}

void CCheckProgressDlg::OnCancel()
{
	if( m_checkthread.IsRunning())
	{
		// 我们这里不能suspend线程，否则很容易导致死锁
		// 因为CRT库与MFC库共享了部分CRT全局锁，如果被挂起的线程持有了这些锁，将导致
		// 主线程竞争这些锁时无法获取，导致主线程挂起。
		// 参见 yvals.h 中定义的锁(搜索LOCK MACROS即可)
		// 相关在线文档: 
		//
		// Why you should never call Suspend/TerminateThread: 
		// http://blog.kalmbachnet.de/?postid=6 
		// and
		// http://blog.csdn.net/hafent/archive/2005/10/27/517778.aspx
		//
		// Worker Threads:
		// http://www.flounder.com/workerthreads.htm#Pausing%20a%20Thread
		//  [7/2/2009 yanghaibo]

		//m_checkthread.Suspend();
		if( IDNO == AfxMessageBox( CConfig::GetStr( USERSTOPCHECK).c_str(), MB_YESNO|MB_DEFBUTTON2))
		{
		//	m_checkthread.Resume();
			return;
		}

		// 设置退出状态
		SetWindowText( CConfig::GetStr( CANCELVERIFY_INPROGRESS).c_str());
		GetDlgItem( IDCANCEL)->EnableWindow( FALSE );
		UpdateWindow();	

		AfxGetApp()->BeginWaitCursor();
		//m_checkthread.Resume();
		m_checkthread.Stop();
		AfxGetApp()->EndWaitCursor();
	}
	CDialog::OnCancel();
	CCheckState::GetInstance().ChangeState( CProgressState::GetInstance());
}

void CCheckProgressDlg::OnOK()
{
}
