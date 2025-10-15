// FileZipLevelDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "FileZipLevelDlg.h"
#include "GlobalInfo.h"
#include "ComboListCtrl/InPlaceCombo.h"
#include "AddFileZipLevelDlg.h"
#include "MainFrm.h"
// CFileZipLevelDlg dialog

IMPLEMENT_DYNAMIC(CFileZipLevelDlg, CDialog)

CFileZipLevelDlg::CFileZipLevelDlg( CWnd* pParent /*=NULL*/)
: CDialog( CFileZipLevelDlg::IDD, pParent)
, m_project( NULL)
{

}

CFileZipLevelDlg::~CFileZipLevelDlg()
{
}

void CFileZipLevelDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST_FILEZIPLEVEL, m_lcFileZipLevel);
	
}

BEGIN_MESSAGE_MAP(CFileZipLevelDlg, CDialog)
	ON_MESSAGE(WM_SET_ITEMS, PopulateComboList)
	ON_MESSAGE(WM_VALIDATE, OnEndLabelEditVariableCriteria)
	ON_BN_CLICKED(IDC_ADDFILEZIP, &CFileZipLevelDlg::OnBnClickedAddfilezip)
	ON_BN_CLICKED(IDC_DELFILEZIP, &CFileZipLevelDlg::OnBnClickedDelfilezip)
END_MESSAGE_MAP()


// CFileZipLevelDlg message handlers

BOOL CFileZipLevelDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  Add extra initialization here
	m_lcFileZipLevel.InsertColumn(0, L"文件类型", LVCFMT_LEFT, 70);
	m_lcFileZipLevel.InsertColumn(1, L"资源包压缩方式", LVCFMT_LEFT, 110);
	m_lcFileZipLevel.InsertColumn(2, L"安装包压缩方式", LVCFMT_LEFT, 130);

	m_lcFileZipLevel.SetExtendedStyle(LVS_EX_FULLROWSELECT);
	m_lcFileZipLevel.SetReadOnlyColumns(0);
	m_lcFileZipLevel.SetComboColumns(1, TRUE);
	m_lcFileZipLevel.SetComboColumns(2, TRUE);

	m_lcFileZipLevel.SetImageList( &CFileIcon::GetInstance().GetImageList(), LVSIL_SMALL);

	GetDlgItem( IDOK)->EnableWindow( FALSE);
	
	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CFileZipLevelDlg::OpenProject( CProject* project)
{
	m_project = project;
	m_lcFileZipLevel.DeleteAllItems();

	CFileAttribute::CAttributeMap& map = m_project->GetAttribute().GetAttributeMap();
	for( CFileAttribute::CAttributeMap::iterator itor = map.begin(); itor != map.end(); ++itor)
	{
		AddListItem(itor->first, itor->second.nZipLevel, itor->second.nSetupLevel);
	}

	// TODO:
	// 添加其他的文件的压缩方式
	// AddListItem(L"*",  CGlobalInfo::RADIO8, CGlobalInfo::ZipID);
}

LRESULT CFileZipLevelDlg::PopulateComboList(WPARAM wParam, LPARAM lParam)
{
	CStringList* pComboList = reinterpret_cast<CStringList*>(lParam);
	if (pComboList)
	{
		CAttributeDesc::CAttributeDescMap& map = (1 == wParam) ? CGlobalInfo::GetInstance().GetZipDesc().GetMap()
			: CGlobalInfo::GetInstance().GetSetupDesc().GetMap();
		for( CAttributeDesc::CAttributeDescMap::iterator itor = map.begin(); itor != map.end(); ++itor)
		{
			pComboList->AddTail( itor->second.sDescript.c_str());
		}
	}
	return 0;
}


LRESULT CFileZipLevelDlg::OnEndLabelEditVariableCriteria(WPARAM wParam, LPARAM lParam) 
{
	LV_DISPINFO* pDispInfo = (LV_DISPINFO*)lParam;
	if( pDispInfo->item.pszText)
	{
		std::wstring value = pDispInfo->item.pszText;
		CFileAttribute::CAttributeMap& map = m_project->GetAttribute().GetAttributeMap();
		std::wstring file = m_lcFileZipLevel.GetItemText( pDispInfo->item.iItem, 0).GetString();
		CFileAttribute::CAttributeMap::iterator itor = map.find( file);
		if( itor != map.end())
		{
			if( pDispInfo->item.iSubItem == 1)
			{
				itor->second.nZipLevel = CGlobalInfo::GetInstance().GetZipDesc().DescToLevel( value, CGlobalInfo::RADIO1);
				if( itor->first == L"*" )
				{
					m_project->GetAttribute().SetDefaultZipLevel( itor->second.nZipLevel );
				}
			}
			else if( pDispInfo->item.iSubItem == 2)
				itor->second.nSetupLevel = CGlobalInfo::GetInstance().GetSetupDesc().DescToLevel( value, CGlobalInfo::ZipID);
		}
		SetChanged();
	}
	return 1;
}

void CFileZipLevelDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	if( IDNO == AfxMessageBox( L"此操作将会把所有文件压缩方式设置为默认压缩方式，要继续么？", MB_YESNO))
		return;
	CMainFrame* pFrame = dynamic_cast<CMainFrame*>( AfxGetMainWnd());
	if( pFrame)
		pFrame->RefreshZipLevel();

	GetDlgItem( IDOK)->EnableWindow( FALSE);
}

void CFileZipLevelDlg::OnBnClickedAddfilezip()
{
	// TODO: Add your control notification handler code here
	CAddFileZipLevelDlg dlgAdd;
	if (IDCANCEL == dlgAdd.DoModal())
		return;
	std::wstring temp = dlgAdd.m_strExt.GetString();
	std::transform( temp.begin(), temp.end(), temp.begin(), ::towlower);
	for (int i=0; i<m_lcFileZipLevel.GetItemCount(); i++)
	{
		CString str = m_lcFileZipLevel.GetItemText(i, 0);
		if( str.CompareNoCase( temp.c_str()) == 0)
		{
			str.Format(L"\"%s\" has existed!", temp.c_str());
			AfxMessageBox(str);
			return;
		}
	}

	std::wstring zipdesc = dlgAdd.m_zipdesc.GetString();
	std::wstring setupdesc = dlgAdd.m_setupdesc.GetString();
	AddListItem( temp, zipdesc, setupdesc);

	CFileAttribute::CAttributeItem item;
	item.nZipLevel = CGlobalInfo::GetInstance().GetZipDesc().DescToLevel( zipdesc, CGlobalInfo::RADIO1);
	item.nSetupLevel = CGlobalInfo::GetInstance().GetSetupDesc().DescToLevel( setupdesc, CGlobalInfo::ZipID);
	CFileAttribute::CAttributeMap& map = m_project->GetAttribute().GetAttributeMap();
	map.insert( std::make_pair( temp, item));

	SetChanged();
}

void CFileZipLevelDlg::OnBnClickedDelfilezip()
{
	// TODO: Add your control notification handler code here
	POSITION pos = m_lcFileZipLevel.GetFirstSelectedItemPosition();
	if (pos == NULL)
	   AfxMessageBox(L"Please select item to delete");
	else
	{
		CFileAttribute::CAttributeMap& map = m_project->GetAttribute().GetAttributeMap();
		for( int i=m_lcFileZipLevel.GetItemCount(); i>=0; i--)
		{
			if( m_lcFileZipLevel.GetItemState( i, LVIS_SELECTED) == LVIS_SELECTED)
			{
				std::wstring file = m_lcFileZipLevel.GetItemText( i, 0).GetString();
				map.erase( file);

				m_lcFileZipLevel.DeleteItem( i);
			}
		}
		SetChanged();
	}
}

bool CFileZipLevelDlg::AddListItem(const std::wstring& sExt, int nZipLevel, int nSetupLevel)
{
	return AddListItem( sExt, CGlobalInfo::GetInstance().GetZipDesc().LevelToDesc( nZipLevel),
		CGlobalInfo::GetInstance().GetSetupDesc().LevelToDesc( nSetupLevel));
}

bool CFileZipLevelDlg::AddListItem( const std::wstring& sExt, const std::wstring& sZipDesc, const std::wstring& sSetupDesc)
{
	std::wstring temp = L"*." + sExt;
	int nIndex = m_lcFileZipLevel.GetItemCount();
	m_lcFileZipLevel.InsertItem( nIndex, sExt.c_str(), CFileIcon::GetInstance().GetFileIconIndex( temp));
	m_lcFileZipLevel.SetItemText( nIndex, 1, sZipDesc.c_str());
	m_lcFileZipLevel.SetItemText( nIndex, 2, sSetupDesc.c_str());
	return true;
}

BOOL CFileZipLevelDlg::PreTranslateMessage(MSG* pMsg)
{
	// TODO: Add your specialized code here and/or call the base class
	if ((pMsg->hwnd == m_lcFileZipLevel.m_hWnd) && (pMsg->message == WM_KEYDOWN) && (pMsg->wParam == VK_DELETE))
		OnBnClickedDelfilezip();
	return CDialog::PreTranslateMessage(pMsg);
}

void CFileZipLevelDlg::SetChanged()
{
	CMainFrame* pFrame = dynamic_cast<CMainFrame*>( AfxGetMainWnd());
	if( pFrame)
		pFrame->SetChanged();
	GetDlgItem(IDOK)->EnableWindow( );
}
