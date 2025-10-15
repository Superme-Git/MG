// FolderDlg.cpp : implementation file
//

#include "stdafx.h"
#include "install.h"
#include "FolderDlg.h"
#include "FolderState.h"
#include "CopyrightState.h"
#include "CheckState.h"
#include "const.h"
#include "Config.h"

// CFolderDlg dialog

IMPLEMENT_DYNAMIC(CFolderDlg, CDialog)

CFolderDlg::CFolderDlg(CWnd* pParent /*=NULL*/)
: CDialog( CFolderDlg::IDD, pParent)
{
}

CFolderDlg::~CFolderDlg()
{
}

void CFolderDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_STATIC_NEEDSPACE, m_needspace);
	DDX_Control(pDX, IDC_STATIC_FREESPACE, m_freespace);
	DDX_Control(pDX, IDC_EDIT_FOLDER, m_folder);
	DDX_Control(pDX, IDC_TREE_FOLDER, m_foldertree);
}


BEGIN_MESSAGE_MAP(CFolderDlg, CDialog)
	ON_BN_CLICKED(IDC_PRESTEP, &CFolderDlg::OnBnClickedPrestep)
	ON_NOTIFY(TVN_SELCHANGEDA, IDC_TREE_FOLDER, &CFolderDlg::OnTvnSelchangedTreeFolder)
	ON_NOTIFY(TVN_SELCHANGEDW, IDC_TREE_FOLDER, &CFolderDlg::OnTvnSelchangedTreeFolder)
END_MESSAGE_MAP()


// CFolderDlg message handlers

void CFolderDlg::OnCancel()
{
	CDialog::OnCancel();
	CFolderState::GetInstance().Exit();
}

void CFolderDlg::OnOK()
{
	CString str;
	m_folder.GetWindowText( str);
	if( str.IsEmpty())
	{
		AfxMessageBox( CConfig::GetStr( SPACENOTENOUGH).c_str());
		return;
	}
	m_selectspace = m_foldertree.GetFreeSpaceFromDiskChar( str[0]);
	if( m_selectspace == 0 || CConfig::GetInstance().m_needspace >= m_selectspace)
	{
		AfxMessageBox( CConfig::GetStr( SPACENOTENOUGH).c_str());
		return;
	}
	CDialog::OnOK();
	CFolderState::GetInstance().SetInstallPath( str.GetBuffer());
	CFolderState::GetInstance().ChangeState( CCheckState::GetInstance());
}

void CFolderDlg::OnBnClickedPrestep()
{
	CDialog::OnCancel();
	CFolderState::GetInstance().ChangeState( CCopyrightState::GetInstance());
}

BOOL CFolderDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	SetWindowText( CConfig::GetStr( BROWSE).c_str());
	GetDlgItem( IDC_PREVSTEP)->SetWindowText( CConfig::GetStr( PREVSTEP).c_str());
	GetDlgItem( IDOK)->SetWindowText( CConfig::GetStr( NEXTSTEP).c_str());
	GetDlgItem( IDCANCEL)->SetWindowText( CConfig::GetStr( END).c_str());
	CString str;
	str.Format( CConfig::GetStr( NEEDSPACE).c_str(), CConfig::GetInstance().m_needspace);
	m_needspace.SetWindowText( str);

	m_foldertree.SelectItem( m_foldertree.GetChildItem( m_foldertree.GetRootItem()));

	// TODO:  Add extra initialization here
	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CFolderDlg::OnTvnSelchangedTreeFolder(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	// TODO: Add your control notification handler code here
	wchar_t szFolder[MAX_PATH] = {0};
	m_foldertree.GetFolderFromTreeItem( szFolder, pNMTreeView->itemNew.hItem);
	CString str;
	if( szFolder[0])
	{
		str.Format( L"%s%s", szFolder, CConfig::GetInstance().m_product.c_str());
		m_folder.SetWindowText( str);
	}
	DWORD dwFree = m_foldertree.GetFreeSpaceOfItem( pNMTreeView->itemNew.hItem);
	m_selectspace = dwFree;
	str.Format( CConfig::GetStr( DISKFREESPACE).c_str(), dwFree);
	m_freespace.SetWindowText( str);

	*pResult = 0;
}
