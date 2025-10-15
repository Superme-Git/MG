// AddNewPathDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "AddNewPathDlg.h"


// CAddNewPathDlg dialog

IMPLEMENT_DYNAMIC(CAddNewPathDlg, CDialog)

CAddNewPathDlg::CAddNewPathDlg( CWnd* pParent, bool bEditUrl)
: CDialog(CAddNewPathDlg::IDD, pParent)
, m_path(_T("新建文件夹"))
, m_bEditUrl( bEditUrl)
{

}

CAddNewPathDlg::~CAddNewPathDlg()
{
}

void CAddNewPathDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT1, m_path);
	DDX_Control(pDX, IDC_EDIT1, m_editPath);
}


BEGIN_MESSAGE_MAP(CAddNewPathDlg, CDialog)
END_MESSAGE_MAP()


// CAddNewPathDlg message handlers

BOOL CAddNewPathDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	if( m_bEditUrl)
	{
		SetWindowText( L"设置Url");
		GetDlgItem( IDC_STATIC_PROMPT)->SetWindowTextW( L" Url：");
	}
	m_editPath.SetSel(0, -1);
	m_editPath.SetFocus();

	return FALSE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CAddNewPathDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	UpdateData();
	if( m_path.IsEmpty())
	{
		AfxMessageBox( L"Please input file ext!");
		m_editPath.SetFocus();
		return;
	}
	if( !m_bEditUrl)
	{
		if( m_path.FindOneOf( L"|?:<>\"\\/") != -1)
		{
			AfxMessageBox( L"Input has error char!");
			m_editPath.SetFocus();
			return;
		}
	}
	CDialog::OnOK();
}
