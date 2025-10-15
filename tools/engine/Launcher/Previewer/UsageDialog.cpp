// UsageDialog.cpp : implementation file
//

#include "stdafx.h"
#include "Previewer.h"
#include "UsageDialog.h"


// CUsageDialog dialog

IMPLEMENT_DYNAMIC(CUsageDialog, CDialog)

CUsageDialog::CUsageDialog(CWnd* pParent /*=NULL*/)
	: CDialog(CUsageDialog::IDD, pParent)
{

}

CUsageDialog::~CUsageDialog()
{
}

void CUsageDialog::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}


BEGIN_MESSAGE_MAP(CUsageDialog, CDialog)
END_MESSAGE_MAP()


// CUsageDialog message handlers
