


#include "stdafx.h"
#include "EffectEditor.h"
#include "InputDialog.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif



IMPLEMENT_DYNAMIC(CInputDialog, CDialog)

BEGIN_MESSAGE_MAP(CInputDialog, CDialog)
END_MESSAGE_MAP()


void CInputDialog::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_INPUT, fInput);
}


CInputDialog::~CInputDialog()
{
}

CInputDialog::CInputDialog(CWnd* pParent /*=NULL*/)
: CDialog(CInputDialog::IDD, pParent)
, fInput(0)
{

}