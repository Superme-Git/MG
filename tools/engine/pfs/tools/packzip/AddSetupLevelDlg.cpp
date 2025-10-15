// AddSetupLevelDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "AddSetupLevelDlg.h"


// CAddSetupLevelDlg dialog

IMPLEMENT_DYNAMIC(CAddSetupLevelDlg, CDialog)

CAddSetupLevelDlg::CAddSetupLevelDlg(CWnd* pParent /*=NULL*/)
: CDialog(CAddSetupLevelDlg::IDD, pParent)
, m_strExt( _T("txt"))
{

}

CAddSetupLevelDlg::~CAddSetupLevelDlg()
{
}

void CAddSetupLevelDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_FILEEXT, m_strExt);
	DDX_Control(pDX, IDC_EDIT_FILEEXT, m_editFileExt);
	DDX_Control(pDX, IDC_COMBO1, m_cbIsPath);
	DDX_Control(pDX, IDC_COMBO2, m_cbApplyChild);
	DDX_CBString(pDX, IDC_COMBO1, m_IsPath);
	DDX_CBString(pDX, IDC_COMBO2, m_ApplyChild);
}


BEGIN_MESSAGE_MAP(CAddSetupLevelDlg, CDialog)
END_MESSAGE_MAP()


// CAddSetupLevelDlg message handlers

BOOL CAddSetupLevelDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_editFileExt.SetSel(0, -1);
	m_editFileExt.SetFocus();

	m_cbIsPath.AddString( L"ÊÇ");
	m_cbIsPath.AddString( L"·ñ");
	m_cbIsPath.SetCurSel( 0);

	m_cbApplyChild.AddString( L"ÊÇ");
	m_cbApplyChild.AddString( L"·ñ");
	m_cbApplyChild.SetCurSel( 0);

	return FALSE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CAddSetupLevelDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	UpdateData();
	if( m_strExt.IsEmpty())
	{
		AfxMessageBox( L"Please input file ext!");
		m_editFileExt.SetFocus();
		return;
	}
	if( m_strExt.FindOneOf( L"|?:<>\"") != -1)
	{
		AfxMessageBox( L"Input has error char!");
		m_editFileExt.SetFocus();
		return;
	}
	CDialog::OnOK();
}
