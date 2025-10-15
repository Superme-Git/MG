// ProjectPathDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "ProjectPathDlg.h"
#include "BrowsePath.h"

// CProjectPathDlg dialog

IMPLEMENT_DYNAMIC(CProjectPathDlg, CDialog)

CProjectPathDlg::CProjectPathDlg(CWnd* pParent /*=NULL*/)
: CDialog(CProjectPathDlg::IDD, pParent)
, m_project( NULL)
, m_bChanged( false)
{

}

CProjectPathDlg::~CProjectPathDlg()
{
}

void CProjectPathDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST1, m_paths);
}


BEGIN_MESSAGE_MAP(CProjectPathDlg, CDialog)
	ON_BN_CLICKED(IDC_BUTTON1, &CProjectPathDlg::OnBnClickedButton1)
END_MESSAGE_MAP()


// CProjectPathDlg message handlers

BOOL CProjectPathDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_paths.SetImageList( &CFileIcon::GetInstance().GetImageList(), LVSIL_SMALL);

	m_paths.InsertColumn( 0, L"Ŀ¼", LVCFMT_LEFT, 280);
	CMountManager::CMountMap& map = m_project->GetMountManager().GetMountMap();
	for( CMountManager::CMountMap::iterator itor = map.begin(); itor != map.end(); ++itor)
	{
		int index = m_paths.InsertItem( m_paths.GetItemCount(), itor->second.c_str(), 
			CFileIcon::GetInstance().m_directoryIconIndex);
		m_paths.SetItemData( index, reinterpret_cast<WPARAM>( itor->first.c_str()));
	}
	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CProjectPathDlg::OnBnClickedButton1()
{
	POSITION pos = m_paths.GetFirstSelectedItemPosition();
	if (pos == NULL)
	{
		AfxMessageBox(L"Please select item to edit");
		return;
	}
	int index = m_paths.GetNextSelectedItem( pos);
	CString str = m_paths.GetItemText( index, 0);
	CBrowsePath browser( this->GetSafeHwnd());
	browser.SetIniPath( str.GetString());
	if( browser.DoModal())
		m_paths.SetItemText( index, 0, browser.GetSelectPath().c_str());
}


void CProjectPathDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	m_bChanged = false;
	for( int i=0; i<m_paths.GetItemCount(); i++)
	{
		std::wstring mount = reinterpret_cast< LPCTSTR>( m_paths.GetItemData( i));
		std::wstring path = m_paths.GetItemText( i, 0);
		if( m_project->GetMountManager().ChangeMountPath( mount, path))
			m_bChanged = true;
	}
	CDialog::OnOK();
}
