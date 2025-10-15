// AddFileZipLevelDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "AddFileZipLevelDlg.h"


// CAddFileZipLevelDlg dialog

IMPLEMENT_DYNAMIC(CAddFileZipLevelDlg, CDialog)

CAddFileZipLevelDlg::CAddFileZipLevelDlg(CWnd* pParent /*=NULL*/)
: CDialog(CAddFileZipLevelDlg::IDD, pParent)
, m_strExt( _T("txt"))
, m_zipdesc(_T(""))
, m_setupdesc(_T(""))
{

}

CAddFileZipLevelDlg::~CAddFileZipLevelDlg()
{
}

void CAddFileZipLevelDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_FILEEXT, m_strExt);
	DDX_Control(pDX, IDC_EDIT_FILEEXT, m_editFileExt);
	DDX_Control(pDX, IDC_COMBO1, m_ziplevel);
	DDX_Control(pDX, IDC_COMBO2, m_setuplevel);
	DDX_CBString(pDX, IDC_COMBO1, m_zipdesc);
	DDX_CBString(pDX, IDC_COMBO2, m_setupdesc);
}


BEGIN_MESSAGE_MAP(CAddFileZipLevelDlg, CDialog)
END_MESSAGE_MAP()


// CAddFileZipLevelDlg message handlers

void CAddFileZipLevelDlg::OnOK()
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

BOOL CAddFileZipLevelDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  Add extra initialization here
	m_editFileExt.SetSel(0, -1);
	m_editFileExt.SetFocus();

	AddLevels( CGlobalInfo::GetInstance().GetZipDesc().GetMap(), m_ziplevel);
	m_ziplevel.SetCurSel( 0);
	AddLevels( CGlobalInfo::GetInstance().GetSetupDesc().GetMap(), m_setuplevel);;
	m_setuplevel.SetCurSel( 3);	

	return FALSE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CAddFileZipLevelDlg::AddLevels( CAttributeDesc::CAttributeDescMap& map, CComboBox& box)
{
	for( CAttributeDesc::CAttributeDescMap::iterator itor = map.begin(); itor != map.end(); ++itor)
	{
		box.AddString( itor->second.sDescript.c_str());
	}
}