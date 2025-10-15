// EditIgnoreDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "EditIgnoreDlg.h"
#include "GlobalInfo.h"
#include "AddSetupLevelDlg.h"
#include "MainFrm.h"
// CEditIgnoreDlg dialog

IMPLEMENT_DYNAMIC(CEditIgnoreDlg, CDialog)

CEditIgnoreDlg::CEditIgnoreDlg( CWnd* pParent /*=NULL*/)
: CDialog(CEditIgnoreDlg::IDD, pParent)
{

}

CEditIgnoreDlg::~CEditIgnoreDlg()
{
}

void CEditIgnoreDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST1, m_lcIgnore);
}


BEGIN_MESSAGE_MAP(CEditIgnoreDlg, CDialog)
	ON_MESSAGE(WM_SET_ITEMS, PopulateComboList)
	ON_MESSAGE(WM_VALIDATE, OnEndLabelEditVariableCriteria)
	ON_BN_CLICKED(IDC_BUTTON1, &CEditIgnoreDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON2, &CEditIgnoreDlg::OnBnClickedButton2)
END_MESSAGE_MAP()


// CEditIgnoreDlg message handlers

BOOL CEditIgnoreDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  Add extra initialization here
	m_lcIgnore.SetImageList( &CFileIcon::GetInstance().GetImageList(), LVSIL_SMALL);

	m_lcIgnore.InsertColumn( 0, L"忽略的文件和目录", LVCFMT_LEFT, 140);
	m_lcIgnore.InsertColumn( 1, L"是否是目录", LVCFMT_LEFT, 80);
	m_lcIgnore.InsertColumn( 2, L"是否应用子目录", LVCFMT_LEFT, 120);

	m_lcIgnore.SetExtendedStyle(LVS_EX_FULLROWSELECT);
	m_lcIgnore.SetReadOnlyColumns(0);
	m_lcIgnore.SetComboColumns(1, TRUE);
	m_lcIgnore.SetComboColumns(2, TRUE);

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CEditIgnoreDlg::OpenProject( CProject* project)
{
	m_project = project;
	m_lcIgnore.DeleteAllItems();

	CFileIgnore::CFileIgnoreMap& map = m_project->GetIgnore().GetIgnoresMap();
	for( CFileIgnore::CFileIgnoreMap::iterator itor = map.begin(); itor != map.end(); ++itor)
	{
		AddFileIgnore( itor->first, &(itor->second));
	}
}

static const std::wstring YES = L"是";
static const std::wstring NO = L"否";

LRESULT CEditIgnoreDlg::PopulateComboList(WPARAM wParam, LPARAM lParam)
{
	CStringList* pComboList = reinterpret_cast<CStringList*>(lParam);
	if (pComboList)
	{
		pComboList->AddTail( YES.c_str());
		pComboList->AddTail( NO.c_str());
	}
	return 0;
}

void CEditIgnoreDlg::AddFileIgnore( const std::wstring& file, CFileIgnore::CIgnoreItem* pItem)
{
	int nIndex = m_lcIgnore.InsertItem( m_lcIgnore.GetItemCount(), file.c_str(), GetImageIndex( file, pItem->bIsPath));
	m_lcIgnore.SetItemText( nIndex, 1, pItem->bIsPath ? YES.c_str() : NO.c_str());
	m_lcIgnore.SetItemText( nIndex, 2, pItem->bApplyChild ? YES.c_str() : NO.c_str());
	m_lcIgnore.SetItemData( nIndex, (DWORD)pItem);
}

int CEditIgnoreDlg::GetImageIndex( const std::wstring& file, bool bPath)
{
	if( bPath)
		return CFileIcon::GetInstance().GetFileIconIndex( L"/");
	std::wstring temp = L"*." + file;
	return CFileIcon::GetInstance().GetFileIconIndex( temp);
}

bool CEditIgnoreDlg::IsExist( const std::wstring& file, const std::wstring& IsPath)
{
	for (int i=0; i<m_lcIgnore.GetItemCount(); i++)
	{
		CString strFile = m_lcIgnore.GetItemText( i, 0);
		CString strIsPath = m_lcIgnore.GetItemText( i, 1);
		if( (strFile.Compare( file.c_str()) == 0) && (strIsPath.Compare( IsPath.c_str()) == 0))
		{
			strFile.Format( L"名字为 \"%s\" 的%s已经存在!", file.c_str(), ( IsPath ==  YES) ? L"目录" : L"文件扩展名");
			AfxMessageBox( strFile);
			return true;
		}
	}
	return false;
}

void CEditIgnoreDlg::OnBnClickedButton1()
{
	CAddSetupLevelDlg dlgAdd;
	if (IDCANCEL == dlgAdd.DoModal())
		return;

	std::wstring temp = GetStandardPath( dlgAdd.m_strExt.GetString());
	std::transform( temp.begin(), temp.end(), temp.begin(), ::towlower);
	std::wstring IsPath = dlgAdd.m_IsPath.GetString();

	if( IsExist( temp, IsPath))
		return;

	std::wstring ApplyChild = dlgAdd.m_ApplyChild.GetString();

	CFileIgnore::CFileIgnoreMap& map = m_project->GetIgnore().GetIgnoresMap();
	CFileIgnore::CIgnoreItem item;
	item.bIsPath = ( IsPath == YES);
	item.bApplyChild = ( ApplyChild == YES);
	CFileIgnore::CFileIgnoreMap::iterator itor = map.insert( std::make_pair( temp, item));

	AddFileIgnore( temp, &(itor->second));
	SetChanged();
}

void CEditIgnoreDlg::OnBnClickedButton2()
{
	POSITION pos = m_lcIgnore.GetFirstSelectedItemPosition();
	if (pos == NULL)
	   AfxMessageBox(L"Please select item to delete");
	else
	{
		CFileIgnore::CFileIgnoreMap& map = m_project->GetIgnore().GetIgnoresMap();
		for (int i=m_lcIgnore.GetItemCount(); i>=0; i--)
		{
			if( m_lcIgnore.GetItemState(i, LVIS_SELECTED) == LVIS_SELECTED)
			{
				CFileIgnore::CIgnoreItem* pItem = (CFileIgnore::CIgnoreItem*)m_lcIgnore.GetItemData( i);
				for( CFileIgnore::CFileIgnoreMap::iterator itor = map.begin(); itor!=map.end(); ++itor)
				{
					if( pItem == &(itor->second))
					{
						map.erase( itor);
						break;
					}
				}
				m_lcIgnore.DeleteItem(i);
			}
		}
		SetChanged();
	}
}

void CEditIgnoreDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	CDialog::OnOK();
}

LRESULT CEditIgnoreDlg::OnEndLabelEditVariableCriteria(WPARAM wParam, LPARAM lParam) 
{
	LV_DISPINFO* pDispInfo = (LV_DISPINFO*)lParam;
	if( pDispInfo->item.pszText)
	{
		std::wstring value = pDispInfo->item.pszText;
		bool bValue = ( value == YES);
		std::wstring file = m_lcIgnore.GetItemText( pDispInfo->item.iItem, 0).GetString();

		CFileIgnore::CIgnoreItem* pItem = (CFileIgnore::CIgnoreItem*)m_lcIgnore.GetItemData( pDispInfo->item.iItem);
		
		if( pDispInfo->item.iSubItem == 1)
		{
			pItem->bIsPath = bValue;
			// 改变图标SetItemState没用，只能用SetItem
			m_lcIgnore.SetItem( pDispInfo->item.iItem, 0, LVIF_IMAGE, NULL, GetImageIndex( file, pItem->bIsPath), 0, 0, 0 );			
		}
		else if( pDispInfo->item.iSubItem == 2)
		{
			pItem->bApplyChild = bValue;
		}
		SetChanged();
	}
	return 1;
}

void CEditIgnoreDlg::SetChanged()
{
	CMainFrame* pFrame = dynamic_cast<CMainFrame*>( AfxGetMainWnd());
	if( pFrame)
	{
		pFrame->SetChanged();
		pFrame->RefreshIsCheck();
	}
}