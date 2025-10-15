// CheckDlg.cpp : implementation file
//

#include "stdafx.h"
#include "install.h"
#include "CheckState.h"
#include "ProgressState.h"
#include "FolderState.h"
#include "CheckDlg.h"
#include "const.h"
#include "Config.h"
#include "GlobalError.h"
#include "FinishState.h"
#include "LayoutManager.h"

// CCheckDlg dialog

IMPLEMENT_DYNAMIC(CCheckDlg, CDialog)

CCheckDlg::CCheckDlg(CWnd* pParent /*=NULL*/)
: CDialog(CCheckDlg::IDD, pParent)
{
}

CCheckDlg::~CCheckDlg()
{
}

void CCheckDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}


BEGIN_MESSAGE_MAP(CCheckDlg, CDialog)
	ON_BN_CLICKED(IDC_PREVSTEP, &CCheckDlg::OnBnClickedPrevstep)
	ON_WM_PAINT()
END_MESSAGE_MAP()


// CCheckDlg message handlers

void CCheckDlg::OnCancel()
{
	// TODO: Add your specialized code here and/or call the base class
	CDialog::OnCancel();
	CCheckState::GetInstance().ChangeState( CProgressState::GetInstance());
}

void CCheckDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	CDialog::OnOK();
	CConfig::CPackageVector& pv = CConfig::GetInstance().m_packages;
	for( CConfig::CPackageVector::iterator i=pv.begin(), e=pv.end(); i!=e; ++i)
	{
		CConfig::CPackageInfo pi = *i;
		if( pi.m_md5.empty())
		{
			CGlobalError::GetInstance().SetError( CInstallState::ERROR_MD5FILE, pi.m_name);
			CCheckState::GetInstance().ChangeState( CFinishState::GetInstance());
			return;
		}
	}
	
	m_progressdialog.Create( this);
	m_progressdialog.CenterWindow();
	m_progressdialog.ShowWindow( SW_SHOW);
}

BOOL CCheckDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	// TODO:  Add extra initialization here
	SetWindowText( CConfig::GetStr( ASK).c_str());
	GetDlgItem( IDOK)->SetWindowText( CConfig::GetStr( CHECK).c_str());
	GetDlgItem( IDCANCEL)->SetWindowText( CConfig::GetStr( JUMP).c_str());
	GetDlgItem( IDC_PREVSTEP)->SetWindowText( CConfig::GetStr( PREVSTEP).c_str());

	// 安装程序从网络下载的过程中可能会导致部分数据丢失，你需要对安装程序数据包做完整性校验吗？
	GetDlgItem( IDC_STATIC_CHECK)->SetWindowText( CConfig::GetStr( CHECK_QUESTION).c_str());

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CCheckDlg::OnBnClickedPrevstep()
{
	// TODO: Add your control notification handler code here
	CDialog::OnCancel();
	CCheckState::GetInstance().ChangeState( CFolderState::GetInstance());
}

void CCheckDlg::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: Add your message handler code here
	// Do not call CDialog::OnPaint() for painting messages

	//CLayoutManager::GetInstance().Draw( dc.GetSafeHdc(), 0);
}
