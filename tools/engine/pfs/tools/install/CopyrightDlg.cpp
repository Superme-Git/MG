// CopyrightDlg.cpp : implementation file
//

#include "stdafx.h"
#include "install.h"
#include "CopyrightDlg.h"
#include "CopyrightState.h"
#include "FolderState.h"
#include "const.h"
#include "Config.h"
// CCopyrightDlg dialog

IMPLEMENT_DYNAMIC(CCopyrightDlg, CDialog)

CCopyrightDlg::CCopyrightDlg( CWnd* pParent /*=NULL*/)
: CDialog(CCopyrightDlg::IDD, pParent)
{
}

CCopyrightDlg::~CCopyrightDlg()
{
}

void CCopyrightDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_EDIT_COPYRIGHT, m_copyright);
}


BEGIN_MESSAGE_MAP(CCopyrightDlg, CDialog)
	ON_MESSAGE( WM_SCROLLNOTIFY, OnScrollNotify)
	ON_BN_CLICKED( IDC_AGREE, &CCopyrightDlg::OnBnClickedAgree)
END_MESSAGE_MAP()


// CCopyrightDlg message handlers

BOOL CCopyrightDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	SetWindowText( CConfig::GetStr( COPYRIGHT).c_str());
	GetDlgItem( IDC_STATIC_COPYRIGHT)->SetWindowText( CConfig::GetStr( READCOPYRIGHT).c_str());
	GetDlgItem( IDC_AGREE)->SetWindowText( CConfig::GetStr( AGREE).c_str());
	GetDlgItem( IDCANCEL)->SetWindowText( CConfig::GetStr( DISAGREE).c_str());

	// TODO:  Add extra initialization here
	GetDlgItem( IDC_AGREE)->EnableWindow( FALSE);

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

HRESULT	CCopyrightDlg::OnScrollNotify( WPARAM wParam, LPARAM lParam)
{
	GetDlgItem( IDC_AGREE)->EnableWindow( TRUE);
	return 0;
}
void CCopyrightDlg::OnCancel()
{
	// TODO: Add your specialized code here and/or call the base class
	CDialog::OnCancel();
	CCopyrightState::GetInstance().Exit();
}

void CCopyrightDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	//CDialog::OnOK();
}

void CCopyrightDlg::OnBnClickedAgree()
{
	CDialog::OnOK();
	CCopyrightState::GetInstance().ChangeState( CFolderState::GetInstance());
}
