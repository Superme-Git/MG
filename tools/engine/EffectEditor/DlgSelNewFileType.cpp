

#include "stdafx.h"
#include "EffectEditor.h"
#include "DlgSelNewFileType.h"




IMPLEMENT_DYNAMIC(CDlgSelNewFileType, CDialog)

void CDlgSelNewFileType::OnOK()
{
	switch(static_cast<CListBox*>(GetDlgItem(IDC_NEW_FILE_TYPE_LIST))->GetCurSel())
	{
	case 0:
		type = EFT_EFFECT;
		break;
	case 1:
		type = EFT_SEBIND;
		break;
	}
	CDialog::OnOK();
}

BOOL CDlgSelNewFileType::OnInitDialog()
{
	CDialog::OnInitDialog();

	CListBox* listBox = static_cast<CListBox*>(GetDlgItem(IDC_NEW_FILE_TYPE_LIST));
	listBox->InsertString(0, L"特效文件");
	listBox->InsertString(1, L"绑定特效");
	listBox->SetCurSel(0);

	return TRUE;
}

BEGIN_MESSAGE_MAP(CDlgSelNewFileType, CDialog)
END_MESSAGE_MAP()




void CDlgSelNewFileType::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}


CDlgSelNewFileType::~CDlgSelNewFileType()
{
}

CDlgSelNewFileType::CDlgSelNewFileType(CWnd* pParent /*=NULL*/)
: CDialog(CDlgSelNewFileType::IDD, pParent)
{

}

