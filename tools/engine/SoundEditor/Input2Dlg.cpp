// Input2Dlg.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "Input2Dlg.h"


// CInput2Dlg dialog

IMPLEMENT_DYNAMIC(CInput2Dlg, CDialog)

CInput2Dlg::CInput2Dlg(CWnd* pParent /*=NULL*/)
	: CDialog(CInput2Dlg::IDD, pParent)
	, strInfo(_T(""))
	, strInfo1(_T(""))
	, strInfo2(_T(""))
	, strEdit1(_T(""))
	, strEdit2(_T(""))
	, strCaption(_T(""))
	, oneEdit(false)
{

}

CInput2Dlg::~CInput2Dlg()
{
}

void CInput2Dlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_STATIC_INFO, strInfo);
	DDX_Text(pDX, IDC_STATIC_TEXT1, strInfo1);
	DDX_Text(pDX, IDC_STATIC_TEXT2, strInfo2);
	DDX_Text(pDX, IDC_EDIT1, strEdit1);
	DDX_Text(pDX, IDC_EDIT2, strEdit2);
}


BEGIN_MESSAGE_MAP(CInput2Dlg, CDialog)
END_MESSAGE_MAP()


// CInput2Dlg message handlers

BOOL CInput2Dlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	SetWindowText(strCaption);
	if (oneEdit)
	{
		GetDlgItem(IDC_STATIC_TEXT2)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_EDIT2)->ShowWindow(SW_HIDE);
	}

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}
