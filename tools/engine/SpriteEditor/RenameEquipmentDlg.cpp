


#include "stdafx.h"
#include "SpriteEditor.h"
#include "RenameEquipmentDlg.h"

#include <set>
#include <algorithm>



IMPLEMENT_DYNAMIC(CRenameEquipmentDlg, CDialog)

BOOL CRenameEquipmentDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	int nec = m_pSprMan->GetLayerCount(m_modelname);
	std::set<std::wstring> s;
	m_enames.resize(nec);
	for(int i=0; i< nec; ++i)
	{
		m_pSprMan->DumpComponent(m_modelname, i, m_enames[i]);
		for(int j=0; j<(int)m_enames[i].size();++j)
			s.insert(m_enames[i][j]);
	}
	for(std::set<std::wstring>::const_iterator it = s.begin(), ie = s.end(); it != ie; ++it)
		m_cmbOldEName.AddString((*it).c_str());

	if( !s.empty())
		m_cmbOldEName.SetCurSel(0);

	return TRUE;  

}
void CRenameEquipmentDlg::OnOK()
{
	UpdateData();

	if( m_newEName.IsEmpty() )
	{
		AfxMessageBox(L"新名称不可以为空");
		return;
	}

	int cursel = m_cmbOldEName.GetCurSel();
	if( cursel < 0 )
	{
		AfxMessageBox(L"请选择一个旧名称");
		return;
	}

	CString buf;
	m_cmbOldEName.GetLBText(cursel, buf);
	oldname = buf;
	newname = m_newEName;

	for(int i=0; i<(int)m_enames.size();++i)
	{
		std::vector<std::wstring> &v = m_enames[i];
		if( v.end() == std::find(v.begin(), v.end(), oldname) ) continue; 
		if( v.end() != std::find(v.begin(), v.end(), newname) )
		{
			AfxMessageBox(L"名字冲突，请重新选择新名称");
			return;
		}
	}

	CDialog::OnOK();
}

BEGIN_MESSAGE_MAP(CRenameEquipmentDlg, CDialog)
END_MESSAGE_MAP()




void CRenameEquipmentDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_CMB_OLDENAME, m_cmbOldEName);
	DDX_Text(pDX, IDC_EDT_NEWENAME, m_newEName);
	DDV_MaxChars(pDX, m_newEName, 64);
}


CRenameEquipmentDlg::~CRenameEquipmentDlg()
{
}

CRenameEquipmentDlg::CRenameEquipmentDlg(Nuclear::SpriteManager* pSprMan, const std::wstring &modelname, CWnd* pParent /*=NULL*/)
: CDialog(CRenameEquipmentDlg::IDD, pParent)
, m_pSprMan(pSprMan)
, m_modelname(modelname)
, m_newEName(_T(""))
{

}

