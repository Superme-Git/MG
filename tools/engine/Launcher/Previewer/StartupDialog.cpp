// StartupDialog.cpp : implementation file
//

#include "stdafx.h"
#include "Previewer.h"
#include "StartupDialog.h"
#include "PreviewerDlg.h"
#include "PatcherMainDialog.h"


// CStartupDialog dialog

IMPLEMENT_DYNAMIC(CStartupDialog, CDialog)

CStartupDialog::CStartupDialog(CWnd* pParent /*=NULL*/)
	: CDialog(CStartupDialog::IDD, pParent)
{

}

CStartupDialog::~CStartupDialog()
{
}

void CStartupDialog::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}


BEGIN_MESSAGE_MAP(CStartupDialog, CDialog)
	ON_BN_CLICKED(IDC_PATCHER_MAIN_DIALOG, &CStartupDialog::OnBnClickedPatcherMainDialog)
	ON_BN_CLICKED(IDC_WEBPAGE_CONTROL, &CStartupDialog::OnBnClickedWebpageControl)
	ON_BN_CLICKED(IDC_USAGE, &CStartupDialog::OnBnClickedUsage)
	ON_BN_CLICKED(IDC_SELECT_SERVER_DIALOG, &CStartupDialog::OnBnClickedSelectServerDialog)
END_MESSAGE_MAP()


// CStartupDialog message handlers

void CStartupDialog::OnBnClickedPatcherMainDialog()
{
	CPatcherMainDialog dlg;	
	dlg.DoModal();
}

void CStartupDialog::OnBnClickedWebpageControl()
{
	CPreviewerDlg dlg;
	dlg.DoModal();
}

#include "UsageDialog.h"

void CStartupDialog::OnBnClickedUsage()
{
	CUsageDialog dlg;
	dlg.DoModal();
}

#include "SelectServerDialog.h"

#include "LauncherConfig.h"
void CStartupDialog::OnBnClickedSelectServerDialog()
{
	PFSX::CVersionInfo versionInfo;

	PFSX::GetLocalVersion( GetGameClientDir().GetString(), versionInfo);

	CSelectServerDialog dlg(versionInfo, this);
	dlg.DoModal();
}
