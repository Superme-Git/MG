


#include "stdafx.h"
#include "SpriteEditor.h"
#include "RenameDlg.h"




IMPLEMENT_DYNAMIC(CRenameDlg, CDialog)

void CRenameDlg::OnOK()
{
	m_Pos = m_cmbPos.GetCurSel();
	wchar_t buf[64];
	int sel = m_cmbSrc.GetCurSel();
	m_cmbSrc.GetLBText(sel, buf);
	if( sel == 10 )
		m_Pattern = L'*';
	else
		m_Pattern = buf[0];
	m_cmbDst.GetLBText(m_cmbDst.GetCurSel(),buf);
	m_Replace = buf[0];

	CDialog::OnOK();
}
BOOL CRenameDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_cmbPos.AddString(L"1");
	m_cmbPos.AddString(L"2");
	m_cmbPos.AddString(L"3");
	m_cmbPos.AddString(L"4");
	m_cmbPos.AddString(L"5");
	m_cmbPos.SetCurSel(1);

	m_cmbSrc.AddString(L"0");
	m_cmbSrc.AddString(L"1");
	m_cmbSrc.AddString(L"2");
	m_cmbSrc.AddString(L"3");
	m_cmbSrc.AddString(L"4");
	m_cmbSrc.AddString(L"5");
	m_cmbSrc.AddString(L"6");
	m_cmbSrc.AddString(L"7");
	m_cmbSrc.AddString(L"8");
	m_cmbSrc.AddString(L"9");
	m_cmbSrc.AddString(L"All");
	m_cmbSrc.SetCurSel(0);

	m_cmbDst.AddString(L"0");
	m_cmbDst.AddString(L"1");
	m_cmbDst.AddString(L"2");
	m_cmbDst.AddString(L"3");
	m_cmbDst.AddString(L"4");
	m_cmbDst.AddString(L"5");
	m_cmbDst.AddString(L"6");
	m_cmbDst.AddString(L"7");
	m_cmbDst.AddString(L"8");
	m_cmbDst.AddString(L"9");
	m_cmbDst.SetCurSel(1);

	return TRUE;  

}

BEGIN_MESSAGE_MAP(CRenameDlg, CDialog)
END_MESSAGE_MAP()




void CRenameDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_CMB_POS, m_cmbPos);
	DDX_Control(pDX, IDC_CMB_SRC, m_cmbSrc);
	DDX_Control(pDX, IDC_CMB_DST, m_cmbDst);
}


CRenameDlg::~CRenameDlg()
{
}

CRenameDlg::CRenameDlg(CWnd* pParent /*=NULL*/)
: CDialog(CRenameDlg::IDD, pParent)
{

}

