

#include "stdafx.h"
#include "SpriteEditor.h"
#include "EquipmentInfoDlg.h"

#include "..\engine\sprite\spritemanager.h"




IMPLEMENT_DYNAMIC(CEquipmentInfoDlg, CDialog)

void CEquipmentInfoDlg::OnOK()
{
	UpdateData();
	if( m_Name.IsEmpty() )
	{
		AfxMessageBox(L"名称不能为空");
		return;
	}

	CDialog::OnOK();
}

BOOL CEquipmentInfoDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	for(int i=0;i<(int)m_Actions.size();++i)
		m_lstActions.AddString(m_Actions[i].c_str());

	return TRUE;  

}

BEGIN_MESSAGE_MAP(CEquipmentInfoDlg, CDialog)
END_MESSAGE_MAP()

void CEquipmentInfoDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LST_ACTION, m_lstActions);
	DDX_Text(pDX, IDC_EDT_NAME, m_Name);
	DDX_Text(pDX, IDC_EDT_HEIGHT, m_Height);
	DDV_MinMaxInt(pDX, m_Height, 1, 1024);
	DDX_Text(pDX, IDC_EDT_WIDTH, m_Width);
	DDV_MinMaxInt(pDX, m_Width, 1, 1024);
	DDX_Text(pDX, IDC_EDT_ECTYPE, m_ecType);
}

CEquipmentInfoDlg::~CEquipmentInfoDlg()
{
}

CEquipmentInfoDlg::CEquipmentInfoDlg(CWnd* pParent /*=NULL*/)
: CDialog(CEquipmentInfoDlg::IDD, pParent)
, m_Name(_T(""))
, m_Height(48)
, m_Width(112)
, m_ecType(_T(""))
{

}

