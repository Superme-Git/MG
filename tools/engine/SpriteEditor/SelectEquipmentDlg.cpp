

#include "stdafx.h"
#include "SpriteEditor.h"
#include "SelectEquipmentDlg.h"




IMPLEMENT_DYNAMIC(CSelectEquipmentDlg, CDialog)

void CSelectEquipmentDlg::OnLbnDblclkLstEquip()
{
	if(  m_lstEquip.GetCount() !=0 || m_lstEquip.GetCurSel() >= 0 )
	{
		OnOK();
	}
}
BOOL CSelectEquipmentDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	for(int i=0;i<(int)m_Equipments.size();++i)
		m_lstEquip.AddString(m_Equipments[i].c_str());

	return TRUE;  

}

void CSelectEquipmentDlg::OnOK()
{
	if(  m_lstEquip.GetCount() ==0 || m_lstEquip.GetCurSel() < 0 )
	{
		AfxMessageBox(L"至少要选一个物件吧");
		return;
	}

	CString str;
	m_lstEquip.GetText(m_lstEquip.GetCurSel(), str);
	m_Selected = str;
	CDialog::OnOK();
}

BEGIN_MESSAGE_MAP(CSelectEquipmentDlg, CDialog)
	ON_LBN_DBLCLK(IDC_LST_EQUIP, &CSelectEquipmentDlg::OnLbnDblclkLstEquip)
END_MESSAGE_MAP()




void CSelectEquipmentDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDT_ECTYPE, m_ecName);
	DDX_Control(pDX, IDC_LST_EQUIP, m_lstEquip);
}


CSelectEquipmentDlg::~CSelectEquipmentDlg()
{
}

CSelectEquipmentDlg::CSelectEquipmentDlg(CWnd* pParent /*=NULL*/)
: CDialog(CSelectEquipmentDlg::IDD, pParent)
, m_ecName(_T(""))
{

}

