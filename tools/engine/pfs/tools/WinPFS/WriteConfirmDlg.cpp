// WriteConfirmDlg.cpp : implementation file
//

#include "stdafx.h"
#include "WinPFS.h"
#include "WriteConfirmDlg.h"


// CWriteConfirmDlg dialog

IMPLEMENT_DYNAMIC(CWriteConfirmDlg, CDialog)

CWriteConfirmDlg::CWriteConfirmDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CWriteConfirmDlg::IDD, pParent)
	, m_tip(_T(""))
{

}

CWriteConfirmDlg::~CWriteConfirmDlg()
{
}

void CWriteConfirmDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_STATIC_TIP, m_tip);
}


BEGIN_MESSAGE_MAP(CWriteConfirmDlg, CDialog)
	ON_BN_CLICKED(IDOK2, &CWriteConfirmDlg::OnBnClickedOk2)
	ON_BN_CLICKED(IDCANCEL2, &CWriteConfirmDlg::OnBnClickedCancel2)
END_MESSAGE_MAP()


// CWriteConfirmDlg message handlers

void CWriteConfirmDlg::OnBnClickedOk2()
{
	// TODO: Add your control notification handler code here
	EndDialog( IDOK2);
}

void CWriteConfirmDlg::OnBnClickedCancel2()
{
	// TODO: Add your control notification handler code here
	EndDialog( IDCANCEL2);
}

int WriteConfirmBox( const CString& tip)
{
	CWriteConfirmDlg dlg;
	dlg.m_tip = tip;
	return dlg.DoModal();
}
