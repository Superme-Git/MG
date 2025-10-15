// BootDlg.cpp : implementation file
//

#include "stdafx.h"
#include "makedifference.h"
#include "BootDlg.h"
#include "makedifferenceDlg.h"

// CBootDlg dialog

IMPLEMENT_DYNAMIC(CBootDlg, CDialog)

CBootDlg::CBootDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CBootDlg::IDD, pParent)
{

}

CBootDlg::~CBootDlg()
{
}

void CBootDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}


BEGIN_MESSAGE_MAP(CBootDlg, CDialog)
	ON_BN_CLICKED(IDC_MAKEDIFFERENCE, &CBootDlg::OnMakeDifference)
	ON_BN_CLICKED(IDC_MERGEPATCH, &CBootDlg::OnMergePatch)
END_MESSAGE_MAP()


// CBootDlg message handlers

void CBootDlg::OnMakeDifference()
{
	// TODO: Add your control notification handler code here
	//OnOK();
	CMakeDifferenceDlg dlg;
	dlg.DoModal();
}

void CBootDlg::OnMergePatch()
{
	// TODO: Add your control notification handler code here
	//OnCancel();
	CMakeDifferenceDlg dlg;
	dlg.m_bMergePatch = true;
	dlg.DoModal();
}
