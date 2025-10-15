// ProgressDlg.cpp : implementation file
//

#include "stdafx.h"
#include "install.h"
#include "ProgressState.h"
#include "FinishState.h"
#include "ProgressDlg.h"
#include "const.h"
#include "Config.h"
#include "GlobalError.h"

// CProgressDlg dialog

IMPLEMENT_DYNAMIC(CProgressDlg, CDialog)

CProgressDlg::CProgressDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CProgressDlg::IDD, pParent)
{
}

CProgressDlg::~CProgressDlg()
{
}

void CProgressDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control( pDX, IDC_STATIC_FILE, m_filename);
	DDX_Control( pDX, IDC_PROGRESS_FILE, m_fileprogress);
	DDX_Control( pDX, IDC_STATIC_TOTAL, m_totalname);
	DDX_Control( pDX, IDC_PROGRESS_TOTAL, m_totalprogress);
}


BEGIN_MESSAGE_MAP(CProgressDlg, CDialog)
	ON_MESSAGE( WM_PROGRESS, OnInstallProgress)
END_MESSAGE_MAP()

// CProgressDlg message handlers

BOOL CProgressDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	SetWindowText( CConfig::GetStr( PROGRESS).c_str());
	GetDlgItem( IDCANCEL)->SetWindowText( CConfig::GetStr( CANCEL).c_str());
	CString str;
	str.Format( CConfig::GetStr( TOTALPROGRESS).c_str(), 0);
	m_totalname.SetWindowText( str);

	CProgressState::GetInstance().StartInstall();

	return TRUE;
}

LRESULT CProgressDlg::OnInstallProgress( WPARAM wParam, LPARAM lParam)
{
	switch( lParam)
	{
	case INIT:
		m_filename.SetWindowText( CConfig::GetStr( PROGRESSINIT).c_str());
		break;
	case TOTALLEN:
		m_totalwritten = 0;
		m_totalscale = 1.0f;
		if( wParam)
		{
			float temp = 0.0f;
			memcpy( &temp, &wParam, sizeof(float));
			m_totalscale = 100.0f / temp;
		}
		m_totalprogress.SetRange( 0, 100);
		m_totalprogress.SetPos( 0);
		break;
	case FILEEND:
		{
			float temp = 0.0f;
			memcpy( &temp, &wParam, sizeof( float));

			m_filewritten += temp;
			m_fileprogress.SetPos( static_cast<int>( m_filewritten * m_filescale));

			m_totalwritten += temp;
			int total = static_cast<int>( m_totalwritten * m_totalscale);
			if( total != m_totalprogress.GetPos())
			{
				// 如果值未改变就不修改，防止闪烁
				m_totalprogress.SetPos( total);
				CString str;
				str.Format( CConfig::GetStr( TOTALPROGRESS).c_str(), total);
				m_totalname.SetWindowText( str);
			}
		}
		break;
	case FILELEN:
		m_filewritten = 0;
		m_filescale = 1.0f;
		if( wParam )
		{
			float temp = 0.0f;
			memcpy( &temp, &wParam, sizeof( float));
			m_filescale = 100.0f / temp;
		}
		m_fileprogress.SetRange( 0, 100);
		m_fileprogress.SetPos( 0);
		break;
	case FILEPROGRESS:
		m_filewritten += wParam;
		m_fileprogress.SetPos( static_cast<int>( m_filewritten * m_filescale));
		break;
	case FILENAME:
		if( wParam)
		{
			//FIXBUG: 
			//从其它线程传进来的wParam参数，如果代表内存地址，持有时间太久，极易导致崩溃。
			//用本线程的变量strFileName复制它，从而解决此问题。
			// 2010-7-20 yanghaibo
			CString strFileName((LPCTSTR)wParam );
			CString str;
			str.Format( CConfig::GetStr(CURRENTFILE).c_str(), strFileName.GetString() );
			m_filename.SetWindowText( str);
		}
		break;
	case TOTALEND:
		OnEnd();
		break;
	case FILEERROR:
		{
			LPCTSTR filename = reinterpret_cast<LPCTSTR>( wParam);
			std::wstring temp;
			if( filename)
			{
				temp = filename;
				KHMsgLog_Error( L" %s file error %d ", filename, PFS::CEnv::GetLastError());
			}
			CGlobalError::GetInstance().SetError( CProgressState::GetInstance().GetError(), temp);
		}
		break;
	}
	return 0;
}

void CProgressDlg::OnCancel()
{
	if( CProgressState::GetInstance().GetInstallThread().IsRunning())
	{
		// 我们这里不能suspend线程，否则很容易导致死锁
		// 因为CRT库与MFC库共享了部分CRT全局锁，如果被挂起的线程持有了这些锁，将导致
		// 主线程竞争这些锁时无法获取，导致主线程挂起。
		// 参见 yvals.h 中定义的锁(搜索LOCK MACROS即可)
		// 相关在线文档: 
		// Why you should never call Suspend/TerminateThread: 
		// http://blog.kalmbachnet.de/?postid=6 
		// and
		// http://blog.csdn.net/hafent/archive/2005/10/27/517778.aspx
		//
		// Worker Threads:
		// http://www.flounder.com/workerthreads.htm#Pausing%20a%20Thread
		//  [7/2/2009 yanghaibo]
		
		//CProgressState::GetInstance().GetInstallThread().Suspend();
		if( IDNO == AfxMessageBox( CConfig::GetStr( USERSTOP).c_str(), MB_YESNO|MB_DEFBUTTON2))
		{
		//	CProgressState::GetInstance().GetInstallThread().Resume();
			return;
		}
		//CProgressState::GetInstance().GetInstallThread().Resume();
	}
	CGlobalError::GetInstance().SetError( CInstallState::ERROR_USERCANCEL, L"");
	OnEnd();
}

void CProgressDlg::OnOK()
{
}

void CProgressDlg::OnEnd()
{
	// 设置退出状态
	std::wstring strQuiting = CConfig::GetStr(CANCELINSTALL_INPROGRESS);
	SetWindowText( strQuiting.c_str());
	m_filename.SetWindowText( strQuiting.c_str());
	GetDlgItem( IDCANCEL)->EnableWindow( FALSE );
	UpdateWindow();

	AfxGetApp()->BeginWaitCursor();
	CProgressState::GetInstance().EndInstall();
	AfxGetApp()->EndWaitCursor();

	ShowWindow( SW_HIDE );
	CProgressState::GetInstance().ChangeState( CFinishState::GetInstance());
}