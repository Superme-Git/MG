// SetEffectGroupNameDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "SetEffectGroupNameDlg.h"


// CSetEffectGroupNameDlg �Ի���

IMPLEMENT_DYNAMIC(CSetEffectGroupNameDlg, CDialog)

CSetEffectGroupNameDlg::CSetEffectGroupNameDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CSetEffectGroupNameDlg::IDD, pParent)
	, m_strEffectGroupName(L"")
{

}

CSetEffectGroupNameDlg::~CSetEffectGroupNameDlg()
{
}

void CSetEffectGroupNameDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_EFFECTGROUPNAME, m_strEffectGroupName);
	DDV_MaxChars(pDX, m_strEffectGroupName, 20);
}


BEGIN_MESSAGE_MAP(CSetEffectGroupNameDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CSetEffectGroupNameDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CSetEffectGroupNameDlg::OnBnClickedCancel)
END_MESSAGE_MAP()


// CSetEffectGroupNameDlg ��Ϣ�������

void CSetEffectGroupNameDlg::OnBnClickedOk()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnOK();
}

void CSetEffectGroupNameDlg::OnBnClickedCancel()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnCancel();
}
