// ErrorFileDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "ErrorFileDlg.h"

// CErrorFileDlg dialog

IMPLEMENT_DYNAMIC(CErrorFileDlg, CDialog)

CErrorFileDlg::CErrorFileDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CErrorFileDlg::IDD, pParent)
{

}

CErrorFileDlg::~CErrorFileDlg()
{
}

void CErrorFileDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST1, m_files);
}


BEGIN_MESSAGE_MAP(CErrorFileDlg, CDialog)
END_MESSAGE_MAP()

BOOL CErrorFileDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  Add extra initialization here
	m_files.InsertColumn( 0, L"³ö´íÎÄ¼þ", LVCFMT_LEFT, 360);

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

