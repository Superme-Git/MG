

#include "stdafx.h"
#include "MapEditor.h"
#include "TransparentObjectEditorDlg.h"




IMPLEMENT_DYNAMIC(CTransparentObjectEditorDlg, CDialog)

BOOL CTransparentObjectEditorDlg::OnInitDialog()
{
	CDialog::OnInitDialog();


	return TRUE;  
}
void CTransparentObjectEditorDlg::OnBnClickedOk()
{

	UpdateData(TRUE);
	OnOK();
}

BEGIN_MESSAGE_MAP(CTransparentObjectEditorDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CTransparentObjectEditorDlg::OnBnClickedOk)
END_MESSAGE_MAP()




void CTransparentObjectEditorDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT1, m_nX);
	DDX_Text(pDX, IDC_EDIT2, m_nY);
	DDX_Text(pDX, IDC_EDIT3, m_nWidth);
	DDX_Text(pDX, IDC_EDIT4, m_nHeight);
	DDX_Text(pDX, IDC_EDIT5, m_nTime);
}


CTransparentObjectEditorDlg::~CTransparentObjectEditorDlg()
{
}

CTransparentObjectEditorDlg::CTransparentObjectEditorDlg(CWnd* pParent /*=NULL*/)
: CDialog(CTransparentObjectEditorDlg::IDD, pParent)
, m_nX(0)
, m_nY(0)
, m_nTime(0)
, m_nWidth(0)
, m_nHeight(0)
{

}

