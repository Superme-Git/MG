


#include "stdafx.h"
#include "SpriteEditor.h"
#include "PackPropertyDlg.h"




IMPLEMENT_DYNAMIC(CPackPropertyDlg, CDialog)

void CPackPropertyDlg::OnCancel()
{
	if( m_state == 1 )
	{
		SetCancel();
	}
	Clear();
	CDialog::OnCancel();
}

void CPackPropertyDlg::OnBnClickedBtnSkippack()
{
	if( m_state == 1 )
	{
		SetSkip();
	}
}

void CPackPropertyDlg::OnBnClickedBtnPackpro()
{
	if( m_state == 1 )
	{
		SetCancel();
	}
	else
	{
		OnOK();
	}
}

BOOL CPackPropertyDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	return TRUE;  

}

void CPackPropertyDlg::OnOK()
{
	Clear();

	CDialog::OnOK();
}

BEGIN_MESSAGE_MAP(CPackPropertyDlg, CDialog)
	ON_BN_CLICKED(IDC_BTN_PACKPRO, &CPackPropertyDlg::OnBnClickedBtnPackpro)
	ON_BN_CLICKED(IDC_BTN_SKIPPACK, &CPackPropertyDlg::OnBnClickedBtnSkippack)
END_MESSAGE_MAP()




void CPackPropertyDlg::Finish()
{
	SetDlgItemText(IDC_EDT_PACKINGDIR, L"");
	m_state = 2;
	SetDlgItemText(IDC_BTN_PACKPRO, L"OK");
	m_bSkip = false;
	m_bCancel = false;
	UpdateWindow();
}


void CPackPropertyDlg::Clear()
{
	SetDlgItemText(IDC_EDT_PACKINGDIR, L"");
	m_state = 0;
	SetDlgItemText(IDC_BTN_PACKPRO, L"OK");
	m_bSkip = false;
	m_bCancel = false;
	m_lstPackMsg.ResetContent();
	UpdateWindow();
}

void CPackPropertyDlg::AddMsg(const wchar_t *msg)
{
	m_lstPackMsg.AddString(msg);
	m_lstPackMsg.SendMessage(WM_VSCROLL,   SB_BOTTOM,   NULL);   
	UpdateData(FALSE);
	UpdateWindow();
}

void CPackPropertyDlg::SetCurDir(const std::wstring &path)
{
	SetDlgItemText(IDC_EDT_PACKINGDIR, path.c_str());
	UpdateWindow();
}

void CPackPropertyDlg::Init(const std::wstring &path)
{
	m_state = 1;
	m_bSkip = false;
	m_bCancel = false;
	wchar_t buf[MAX_PATH+32];
	wsprintf(buf, L"打包: %s", path.c_str());
	SetWindowText(buf);
	SetDlgItemText(IDC_BTN_PACKPRO, L"取消");
	UpdateWindow();
}

void CPackPropertyDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LST_PACKMSG, m_lstPackMsg);
}

CPackPropertyDlg::~CPackPropertyDlg()
{
}

CPackPropertyDlg::CPackPropertyDlg(CWnd* pParent /*=NULL*/)
: CDialog(CPackPropertyDlg::IDD, pParent)
{
	m_state = 0;
	m_bSkip = false;
	m_bCancel = false;
}

