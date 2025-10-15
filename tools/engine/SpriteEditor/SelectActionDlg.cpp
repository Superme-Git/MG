


#include "stdafx.h"
#include "SpriteEditor.h"
#include "SelectActionDlg.h"




IMPLEMENT_DYNAMIC(CSelectActionDlg, CDialog)

BOOL CSelectActionDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_SelectedActions.clear();
	std::vector<std::wstring>::iterator it = m_Actions.begin(), ie = m_Actions.end();
	for (;it!=ie;++it)
	{
		m_ActList.AddString(it->c_str());
	}
	return TRUE;
}
void CSelectActionDlg::OnOK()
{
	size_t count = m_Actions.size();
	size_t i;
	for (i=0;i<count;++i)
	{
		if (m_ActList.GetSel((int)i))
		{
			m_SelectedActions.push_back(i);
		}
	}
	CDialog::OnOK();
}

BEGIN_MESSAGE_MAP(CSelectActionDlg, CDialog)
END_MESSAGE_MAP()




void CSelectActionDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST1, m_ActList);
}


CSelectActionDlg::~CSelectActionDlg()
{
}

CSelectActionDlg::CSelectActionDlg(CWnd* pParent /*=NULL*/)
: CDialog(CSelectActionDlg::IDD, pParent)
{

}

