


#include "stdafx.h"
#include "MapEditor.h"
#include "WaterAreaInfoDlg.h"




IMPLEMENT_DYNAMIC(CWaterAreaInfoDlg, CDialog)

void CWaterAreaInfoDlg::OnBnClickedOk()
{

	OnOK();
}

BEGIN_MESSAGE_MAP(CWaterAreaInfoDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CWaterAreaInfoDlg::OnBnClickedOk)
END_MESSAGE_MAP()

void CWaterAreaInfoDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT1, m_Depth);
	DDX_Text(pDX, IDC_EDIT2, m_Border);
}

CWaterAreaInfoDlg::~CWaterAreaInfoDlg()
{
}

CWaterAreaInfoDlg::CWaterAreaInfoDlg(CWnd* pParent /*=NULL*/)
: CDialog(CWaterAreaInfoDlg::IDD, pParent)
, m_Depth(0)
, m_Border(0)
{

}

