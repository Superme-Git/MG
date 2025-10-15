


#include "stdafx.h"
#include "SpriteEditor.h"
#include "RenameActionDlg.h"

#include <algorithm>




IMPLEMENT_DYNAMIC(CRenameActionDlg, CDialog)

void CRenameActionDlg::OnOK()
{
	UpdateData();

	if( std::find(m_ActList.begin(), m_ActList.end(), std::wstring(m_NewActName)) != m_ActList.end())
	{
		AfxMessageBox(L"此动作名称已经存在，请换一个吧");
		return;
	}
	CDialog::OnOK();
}

BEGIN_MESSAGE_MAP(CRenameActionDlg, CDialog)
END_MESSAGE_MAP()




void CRenameActionDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDT_OLDACTNAME, m_OldActName);
	DDX_Text(pDX, IDC_EDT_NEWACTNAME, m_NewActName);
	DDV_MaxChars(pDX, m_NewActName, 32);
	DDX_Check(pDX, IDC_CHK_RENAMEXAP, m_bRename);
}


CRenameActionDlg::~CRenameActionDlg()
{
}

CRenameActionDlg::CRenameActionDlg(CWnd* pParent /*=NULL*/)
: CDialog(CRenameActionDlg::IDD, pParent)
, m_OldActName(_T(""))
, m_NewActName(_T(""))
, m_bRename(FALSE)
{

}

