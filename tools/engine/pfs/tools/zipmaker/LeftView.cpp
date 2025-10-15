// LeftView.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "LeftView.h"
#include "LeftViewListener.h"
#include "GlobalInfo.h"
#include "SaveProgressDlg.h"
#include "MainFrm.h"
#include "AddNewPathDlg.h"
#include <sstream>
#include "AddOperator.h"
#include "DeleteOperator.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif
// CLeftView

#define	IDR_FILECOMPRESSION		10000
#define IDR_SETISPACK			11000
#define IDR_SETURL				11001
#define IDR_SETCHILDPACKAGE		11002

IMPLEMENT_DYNCREATE(CLeftView, CTreeView)

CLeftView::CLeftView()
: m_bIsResource( true) // 缺省为resource. 用于生成zip格式
, m_bShowUnChecked( true)
, m_project( NULL)
, m_brush( NULL)
, m_brushChildPackage( NULL)
{
	m_root.SetIsPath( true);
}

CLeftView::~CLeftView()
{
	if( m_brush)
		::DeleteObject( m_brush);
	if( m_brushChildPackage)
		::DeleteObject( m_brushChildPackage);
}

BEGIN_MESSAGE_MAP(CLeftView, CTreeView)
	ON_WM_CREATE()
	ON_WM_DESTROY()
	ON_NOTIFY_REFLECT(TVN_ITEMEXPANDING, &CLeftView::OnTvnItemexpanding)
	ON_NOTIFY_REFLECT(NM_RCLICK, &CLeftView::OnNMRclick)
	ON_NOTIFY_REFLECT(NM_CUSTOMDRAW, &CLeftView::OnNMCustomdraw)
	ON_NOTIFY_REFLECT(TVN_SELCHANGED, &CLeftView::OnTvnSelchanged)
	ON_NOTIFY_REFLECT(NM_CLICK, &CLeftView::OnNMClick)
	ON_WM_KEYDOWN()
END_MESSAGE_MAP()

int CLeftView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CTreeView::OnCreate(lpCreateStruct) == -1)
		return -1;

	GetTreeCtrl().ModifyStyle( 0, TVS_HASBUTTONS|TVS_HASLINES|TVS_LINESATROOT|TVS_SHOWSELALWAYS);
	//GetTreeCtrl().SetImageList( &CGlobalInfo::GetInstance().GetSetupIconList(), TVSIL_NORMAL);
	GetTreeCtrl().SetImageList( &CFileIcon::GetInstance().GetImageList(), TVSIL_NORMAL);
	GetTreeCtrl().SetImageList( &CGlobalInfo::GetInstance().GetCheckIconList(), TVSIL_STATE);

	m_brush = CreateSolidBrush( RGB( 0,200,200));				// “是否pack” 背景色
	m_brushChildPackage = CreateSolidBrush( RGB( 200,0,0));		// “是否子包”	背景色

	return 0;
}

void CLeftView::OnDestroy()
{
	CloseProject();
	CTreeView::OnDestroy();
}

bool CLeftView::OpenProject( CProject* project)
{
	CloseProject();
	std::wstring rootName;
	if( project)
	{
		rootName = project->GetName();
	}
	if( rootName.empty())
	{
		rootName = L"/ProjectRoot";
	}
	HTREEITEM hRoot = GetTreeCtrl().InsertItem( rootName.c_str(), CFileIcon::GetInstance().m_projectIconIndex, 
		CFileIcon::GetInstance().m_projectIconIndex, TVI_ROOT);

	GetTreeCtrl().SetItemData( hRoot, (WPARAM)&m_root);
	GetTreeCtrl().SetItemState( hRoot, INDEXTOSTATEIMAGEMASK( 2), TVIS_STATEIMAGEMASK);
	GetTreeCtrl().SelectItem( hRoot);
	m_project = project;
	if( m_project)
	{
		COperator::COperatorVector ov;
		COperatorManager::COperatorItemVector& ops = m_project->GetOperators();
		for( COperatorManager::COperatorItemVector::iterator itor = ops.begin(); itor != ops.end(); ++itor)
		{
			COperator* ope = NULL;
			switch( itor->m_type)
			{
			case COperatorManager::OT_ADD:
				ope = new CAddOperator_Project( m_root, *m_project, itor->m_windowpath, itor->m_ispath, itor->m_path);
				break;
			case COperatorManager::OT_ADDNEW:
				ope = new CAddNewPathOperator_Project( m_root, *m_project, itor->m_windowpath, itor->m_path);
				break;
			case COperatorManager::OT_DELETE:
				ope = new CDeleteOperator_Project( m_root, *m_project, itor->m_path);
				break;
			case COperatorManager::OT_SETCHECKED:
				ope = new CSetCheckedOperator_Project( m_root, *m_project, itor->m_ispath, itor->m_path);
				break;
			case COperatorManager::OT_SETZIPLEVEL:
				ope = new CSetZipLevelOperator_Project( m_root, *m_project, itor->m_path, itor->m_ispath, _wtoi( itor->m_windowpath.c_str()));
				break;
			case COperatorManager::OT_ADDPATH:
				ope = new CAddPathOperator_Project( m_root, *m_project, itor->m_windowpath, itor->m_path);
				break;
			case COperatorManager::OT_SETURL:
				ope = new CSetUrlOperator_Project( m_root, *m_project, itor->m_windowpath, itor->m_path);
				break;
			case COperatorManager::OT_SETPACK:
				ope = new CSetPackOperator_Project( m_root, *m_project, itor->m_ispath, itor->m_path);
				break;
			case COperatorManager::OT_SETCHILDPACKAGE:
				ope = new CSetChildPackageOperator_Project( m_root, *m_project, itor->m_ispath, itor->m_path);
				break;
			}
			if( ope)
				ov.push_back( ope);
		}
		if( !ov.empty())
		{
			CSaveProgressDlg dlgProgress( true);
			dlgProgress.SetOperators( ov);
			dlgProgress.DoModal( );
			GetTreeCtrl().Expand( hRoot, TVE_EXPAND);
		}
		for( COperator::COperatorVector::iterator itor = ov.begin(); itor != ov.end(); ++itor)
			delete *itor;
	}
	return true;
}

void CLeftView::CloseProject()
{
	GetTreeCtrl().DeleteAllItems();
	m_root.GetChild().clear();
}


// "Add directory ..."
bool CLeftView::AddPath( const std::wstring& path)
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	CZipTreeItem* item = reinterpret_cast<CZipTreeItem*>( GetTreeCtrl().GetItemData( hItem));
	if( !item)
		return false;
	if( !item->GetIsPath())
		return false;

	COperator::COperatorVector ov;
	COperator* adder = new CAddPathOperator( *item, *m_project, path);
	ov.push_back( adder);

	CSaveProgressDlg dlgProgress( true);
	dlgProgress.SetOperators( ov);
	dlgProgress.DoModal( );
	GetTreeCtrl().Expand( hItem, TVE_EXPAND);

	delete adder;

	return true;
}

// "Add items ..."
bool CLeftView::AddPathAndFile( std::map<std::wstring, bool>& files)
{
	if( files.empty())
	{
		return false;
	}
	
	HTREEITEM hSelectedItem = GetTreeCtrl().GetSelectedItem();
	HTREEITEM hParentItem = hSelectedItem;
	CZipTreeItem* item = reinterpret_cast<CZipTreeItem*>( GetTreeCtrl().GetItemData( hParentItem ));
	if( !item)
		return false;
	if( !item->GetIsPath())
	{
		// 更换父节点
		hParentItem = GetTreeCtrl().GetParentItem(hParentItem);
		if( hParentItem == NULL )
		{
			return false;
		}

		item = item->GetParent();
		if( item == NULL )
		{
			return false;
		}
		else
		{
			// 注意，这里必须重新选择父项目，否则，无法刷新树结点。
			GetTreeCtrl().SelectItem( hParentItem );
			KHMsgLog_Warning( L"您当前在文件树中选中的目标是一个文件结点，我们将你指定的文件加到了它所在的父目录%s下。这些文件是：\n", item->GetName().c_str() );
			for( std::map<std::wstring, bool>::iterator itor = files.begin(); itor != files.end(); ++itor)
			{
				KHMsgLog_Info( L"    %s\n", itor->first.c_str() );
			}
		}
	}

	COperator::COperatorVector ov;
	for( std::map<std::wstring, bool>::iterator itor = files.begin(); itor != files.end(); ++itor)
	{
		// *itor 可能是文件名，可能是目录名。
		CAddOperator* adder = new CAddOperator( *item, *m_project, itor->first, itor->second );
		ov.push_back( adder);
	}

	if( !ov.empty())
	{
		CSaveProgressDlg dlgProgress( true);
		dlgProgress.SetOperators( ov);
		dlgProgress.DoModal( );
		GetTreeCtrl().Expand( hParentItem, TVE_EXPAND);
	}
	for( COperator::COperatorVector::iterator itor = ov.begin(); itor != ov.end(); ++itor)
		delete *itor;

	// 还原选中的项目
	GetTreeCtrl().SelectItem( hSelectedItem );

	return true;
}


// "Add empty directory ..."
bool CLeftView::AddNewPath( const std::wstring& name)
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	CZipTreeItem* item = reinterpret_cast<CZipTreeItem*>( GetTreeCtrl().GetItemData( hItem));
	if( !item)
		return false;
	if( !item->GetIsPath())
		return false;

	CAddNewPathOperator adder( *item, *m_project, name);
	CZipTreeItem* newitem = adder.Do();
	if( !newitem)
		return false;
	if( HTREEITEM hNewItem = RefreshAddItem( *newitem, hItem))
		GetTreeCtrl().SelectItem( hNewItem);
	return true;
}

// "Delete"
bool CLeftView::DeletePath( )
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	if( !hItem)
		return false;
	if( hItem == GetTreeCtrl().GetRootItem())
		return false;
	CZipTreeItem* item = reinterpret_cast<CZipTreeItem*>( GetTreeCtrl().GetItemData( hItem));
	CDeleteOperator deleter( *item, *m_project);
	deleter.Do();
	RefreshDeleteItem( hItem);
	return true;
}

void CLeftView::InsertChildrenOfItem( const CZipTreeItem& item, HTREEITEM hParent)
{
	const CZipTreeItem::CZipTreeItemList& map = item.GetChild();
	for (CZipTreeItem::CZipTreeItemList::const_iterator itor = map.begin(); itor != map.end(); ++itor)
	{
		if( itor->GetIsCheck() || m_bShowUnChecked)
			InsertItem( *itor, hParent);
	}
}

// 插入一项
HTREEITEM CLeftView::InsertItem( const CZipTreeItem& child, HTREEITEM hParent)
{
	if( child.GetName().empty())
		return NULL;
	TV_INSERTSTRUCT tvins;                // TreeView Insert Struct.
	tvins.item.mask = TVIF_TEXT | TVIF_IMAGE | TVIF_SELECTEDIMAGE | TVIF_STATE | TVIF_PARAM;
	tvins.item.lParam = (LPARAM)&child;

	wchar_t szName[MAX_PATH] = {0};
	std::wstring name(child.GetName());
	name.erase( name.find_last_not_of(L'/')+1);
	std::wstring::size_type pos = name.find_last_of(L'/');
	name = name.substr(pos+1, name.size());
	wcscpy_s( szName, name.c_str());
	
	tvins.item.pszText = szName;
	tvins.item.cchTextMax = MAX_PATH;

	if( child.GetIsPath())
	{
		if( IsHasChild( child))
		{
			tvins.item.cChildren = 1;
			tvins.item.mask |= TVIF_CHILDREN;
		}
		tvins.item.iImage = CFileIcon::GetInstance().m_directoryIconIndex;
	}
	else
	{
		tvins.item.iImage = CFileIcon::GetInstance().GetFileIconIndex( child.GetName());
		
	}
	//if( m_bIsResource)
	//	tvins.item.iImage = CGlobalInfo::GetInstance().GetZipDesc().LevelToIconIndex( child.GetZipLevel(), false);
	//else
	//	tvins.item.iImage = CGlobalInfo::GetInstance().GetSetupDesc().LevelToIconIndex( child.GetAttributes(), child.GetIsPack());

	tvins.item.iSelectedImage = tvins.item.iImage;
	tvins.hInsertAfter = NULL;
	tvins.hParent = hParent;
	tvins.item.state = INDEXTOSTATEIMAGEMASK( child.GetIsCheck() + 1);
	tvins.item.stateMask = TVIS_STATEIMAGEMASK;

	return GetTreeCtrl().InsertItem(&tvins);
}

bool CLeftView::IsHasChild( const CZipTreeItem& parent) const
{
	if( parent.GetIsNoInit())
		return parent.GetIsHasChild();
	const CZipTreeItem::CZipTreeItemList& map = parent.GetChild();
	if( m_bShowUnChecked)
		return !map.empty();
	for( CZipTreeItem::CZipTreeItemList::const_iterator itor = map.begin(); itor != map.end(); ++itor)
	{
		if( itor->GetIsCheck())
			return true;
	}
	return false;
}

// 得到TreeItem的绝对路径
//void CLeftView::GetNameFromTreeItem(wchar_t* szName, HTREEITEM hItem)
//{
//	wchar_t szTemp[MAX_PATH] = {0};
//	while (hItem)
//	{
//		CString str = GetTreeCtrl().GetItemText(hItem);
//		if (0 == str.Compare(L"/root"))
//		{
//			break;
//		}
//		wcscpy_s(szTemp, szName);
//		wsprintf(szName, L"%s/%s", str, szTemp);
//		hItem = GetTreeCtrl().GetParentItem(hItem);
//	}
//}

void CLeftView::OnTvnItemexpanding(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	// TODO: Add your control notification handler code here
	if ((pNMTreeView->itemNew.state & TVIS_EXPANDEDONCE))
		return;

	HTREEITEM hItem = pNMTreeView->itemNew.hItem;
	if (hItem)
	{
		CZipTreeItem* pItem = (CZipTreeItem*)GetTreeCtrl().GetItemData( hItem);
		if( pItem)
		{
			if( pItem->GetIsNoInit())
			{
				CAddNoInitOperator adder( *pItem, *m_project);
				adder.Do();
			}
			InsertChildrenOfItem( *pItem, hItem);
			GetTreeCtrl().Invalidate( 0);
		}
	}

	*pResult = 0;
}

void CLeftView::OnNMRclick(NMHDR *pNMHDR, LRESULT *pResult)
{
	CPoint pt;
	GetCursorPos(&pt);
	CPoint ptClient(pt);
	ScreenToClient(&ptClient);
	UINT uFlags;
	HTREEITEM hItem = GetTreeCtrl().HitTest( ptClient, &uFlags);
	
	if( hItem)
	{
		GetTreeCtrl().SelectItem( hItem);
		CMenu menu;
		menu.CreatePopupMenu();
		CZipTreeItem* pItem = (CZipTreeItem*)GetTreeCtrl().GetItemData( hItem);

		int pos = 0;
		CAttributeDesc::CAttributeDescMap& map = m_bIsResource ? CGlobalInfo::GetInstance().GetZipDesc().GetMap()
			: CGlobalInfo::GetInstance().GetSetupDesc().GetMap();
		int value = m_bIsResource ? pItem->GetZipLevel() : pItem->GetAttributes() ;
		for ( CAttributeDesc::CAttributeDescMap::iterator itor = map.begin(); itor != map.end(); ++itor, pos++)
		{
			menu.AppendMenuW( MF_STRING, IDR_FILECOMPRESSION+pos, itor->second.sDescript.c_str());
			if( value == itor->first)
				menu.CheckMenuRadioItem( 0, map.size(), pos, MF_BYPOSITION|MF_CHECKED);
		}
		
		menu.AppendMenuW( MF_SEPARATOR);
		bool bSplit = false;
		if( !m_bIsResource)
		{
			if( IsItemCanPack( *pItem))
			{
				bSplit = true;
				menu.AppendMenuW( MF_STRING, IDR_SETISPACK, L"Set Pack");
				menu.CheckMenuItem( IDR_SETISPACK, pItem->GetIsPack() ? MF_CHECKED : MF_UNCHECKED);
			}
			//menu.AppendMenuW( MF_STRING, IDR_SETURL, L"Set Url");
			if( !pItem->GetIsPath())
			{
				bSplit = true;
				menu.AppendMenuW( MF_STRING, IDR_SETCHILDPACKAGE, L"Set Child Package");
				menu.CheckMenuItem( IDR_SETCHILDPACKAGE, pItem->GetIsChildPackage() ? MF_CHECKED : MF_UNCHECKED);
			}
		}
		if( bSplit)
			menu.AppendMenu( MF_SEPARATOR);
		
		CMenu* pMainMenu = AfxGetMainWnd()->GetMenu()->GetSubMenu( 1);
		if( pMainMenu)
		{
			MENUITEMINFO info;
			info.cbSize = sizeof( info);
			info.fMask = MIIM_ID | MIIM_STRING;
			
			for( int i = 0; i< 5; i++)
			{
				wchar_t buffer[MAX_PATH] = {0};
				info.dwTypeData = buffer;
				info.cch = MAX_PATH;
				pMainMenu->GetMenuItemInfo( i, &info, TRUE);

				if( info.wID == 0)
					continue;
				if( i < 4)
				{
					if( pItem->GetIsPath())
						menu.AppendMenu( MF_STRING, info.wID, info.dwTypeData);
				}
				else
				{
					if( pItem != &m_root)
						menu.AppendMenu( MF_STRING, info.wID, info.dwTypeData);
				}
			}
		}
		menu.TrackPopupMenu( TPM_LEFTALIGN, pt.x, pt.y, this);
	}
	*pResult = 0;
}

BOOL CLeftView::OnCommand(WPARAM wParam, LPARAM lParam)
{
	switch( wParam)
	{
	case IDR_SETISPACK:
		OnSetIsPack();
		break;
	case IDR_SETURL:
		OnSetUrl();
		break;
	case IDR_SETCHILDPACKAGE:
		OnSetChildPackage();
		break;
	case ID_FILE_ADDPATH:
	case ID_EDIT_ADDPATHCONTENT:
	case ID_EDIT_ADDNEWPATH:
	case ID_EDIT_DELETE:
		AfxGetMainWnd()->PostMessage( WM_COMMAND, wParam, lParam);
		break;
	default:
		{
			unsigned int nLevel = wParam - IDR_FILECOMPRESSION;			
			CAttributeDesc::CAttributeDescMap& map = m_bIsResource ? CGlobalInfo::GetInstance().GetZipDesc().GetMap()
				: CGlobalInfo::GetInstance().GetSetupDesc().GetMap();
			if( nLevel >= 0 && nLevel < map.size() )
			{
				OnUpdateLevel( nLevel);
			}
		}
	}
	return CTreeView::OnCommand(wParam, lParam);
}

bool CLeftView::SaveFile(const std::wstring& file, bool bResource, bool bSaveFilesMeta, const std::wstring& lastDataPfs)
{
	CSaveOperator* saver = new CSaveOperator( m_root, *m_project, file, bResource, bSaveFilesMeta, lastDataPfs);
	COperator::COperatorVector ov;
	ov.push_back( saver);
	
	CSaveProgressDlg dlgProgress( false);
	dlgProgress.SetOperators( ov);
	dlgProgress.DoModal();

	std::wstringstream wss;
	wss << std::endl;
	wss << L"类    型:  " ;
	if( bResource)
	{
		wss << L"资源包";
	}
	else
	{
		if( lastDataPfs.empty())
		{
			wss << L"安装包";
		}
		else
		{
			wss << L"临时更新包";
		}
	}
	wss << L"保存路径:  " << file << std::endl;
	wss << L"统计信息:  " << std::endl;

	CAnalyseFile& analyse = saver->GetAnalyseFile();
	wss << analyse;

	if( !bResource && m_project->GetIsMakeMD5())
	{
		wss << L"安装包md5值：--- " << saver->GetMD5Value() << std::endl;
	}
	KHMsgLog_Info( wss.str().c_str() );
	
	delete saver;
	return PFS::CEnv::GetLastError() == 0;
}

bool CLeftView::IsItemCanPack( const CZipTreeItem& item)
{
	const CZipTreeItem* parent = item.GetParent();
	if( !parent)
		return false;
	return !parent->GetIsPack() && ( item.GetIsPath() || item.GetIsChildPackage());
}

bool CLeftView::IsItemDefaultLevel( const CZipTreeItem& item, bool bIsResource) const
{
	if( !item.GetIsPath())
	{
		if( bIsResource)
			return item.GetZipLevel() == m_project->GetAttribute().GetZipLevel( item.GetName());
		else
			return item.GetAttributes() == m_project->GetAttribute().GetSetupLevel( item.GetName());
	}
	const CZipTreeItem::CZipTreeItemList& child = item.GetChild();
	for( CZipTreeItem::CZipTreeItemList::const_iterator itor = child.begin(); itor != child.end(); ++itor)
	{
		if( !IsItemDefaultLevel( *itor, bIsResource))
			return false;
	}
	return true;
}

void CLeftView::OnNMCustomdraw(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTVCUSTOMDRAW pCustomDraw = reinterpret_cast<LPNMTVCUSTOMDRAW>(pNMHDR);
	
	switch (pCustomDraw->nmcd.dwDrawStage)
	{
	case CDDS_PREPAINT:
		// Need to process this case and set pResult to CDRF_NOTIFYITEMDRAW, 
		// otherwise parent will never receive CDDS_ITEMPREPAINT notification. (GGH) 
		*pResult = CDRF_NOTIFYITEMDRAW;
		return;

	case CDDS_ITEMPREPAINT:
		{
			CZipTreeItem* pItem = (CZipTreeItem*)pCustomDraw->nmcd.lItemlParam;
			if( pItem)
			{
				if( m_bIsResource ? pItem->GetIsZipNoDef() : pItem->GetIsAttrNoDef())
					pCustomDraw->clrText = RGB( 255, 0, 0);		// 不是默认方式以红色标示出
				CRect rc( pCustomDraw->nmcd.rc);
				rc.left = pItem->GetLevel() * GetTreeCtrl().GetIndent() + 4;
				rc.right = rc.left + 16;
				rc.DeflateRect( 0, 1, 0, 1);
				if( !m_bIsResource && pItem->GetIsPack())
					::FillRect( pCustomDraw->nmcd.hdc, &rc, m_brush);
				if( pItem->GetIsChildPackage())
				{
					rc.DeflateRect( 0, 0, 0, rc.Height()/2);
					::FillRect( pCustomDraw->nmcd.hdc, &rc, m_brushChildPackage);
				}
			}
		}
		*pResult = CDRF_NOTIFYPOSTPAINT;
		return;
	case CDDS_ITEMPOSTPAINT:
		{
			CZipTreeItem* pItem = (CZipTreeItem*)pCustomDraw->nmcd.lItemlParam;
			if( !pItem)
				break;
			if( pItem->GetIsChildPackage())
				break;
			HIMAGELIST imagelist = NULL;
			int index = -1;
			if( pItem->GetIsFileNoExist())
			{
				imagelist = CGlobalInfo::GetInstance().GetSetupIconList().GetSafeHandle();
				index = CGlobalInfo::GetInstance().GetSetupIconList().GetImageCount()-1;
				TRACE( "GetIsFileNoExist\n" );
			}
			else if( !pItem->GetIsPath())
			{	
				if( m_bIsResource)
				{
					imagelist = CGlobalInfo::GetInstance().GetZipIconList().GetSafeHandle();
					index = CGlobalInfo::GetInstance().GetZipDesc().LevelToIconIndex( pItem->GetZipLevel());
				}
				else
				{
					imagelist = CGlobalInfo::GetInstance().GetSetupIconList().GetSafeHandle();
					index = CGlobalInfo::GetInstance().GetSetupDesc().LevelToIconIndex( pItem->GetAttributes());
				}
			}
			//if( pItem->GetLevel() > 0)
			{
				ImageList_Draw( imagelist, 
					index, 
					pCustomDraw->nmcd.hdc,
					pItem->GetLevel() * GetTreeCtrl().GetIndent() + 4,
					pCustomDraw->nmcd.rc.top,
					ILD_TRANSPARENT);
			}
		}
		break;
	}
	*pResult = 0;
}

void CLeftView::OnTvnSelchanged(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	
	HTREEITEM hItem = pNMTreeView->itemNew.hItem; //GetTreeCtrl().GetSelectedItem();   
	if( hItem)
	{
		CZipTreeItem* pItem = (CZipTreeItem*)GetTreeCtrl().GetItemData( hItem);
		for( CListenerVector::iterator itor = m_vtListener.begin(); itor != m_vtListener.end(); ++itor)
			(*itor)->SetZipTreeItem( pItem);
	}

	*pResult = 0;
}

void CLeftView::SetChanged()
{
	CMainFrame* pFrame = dynamic_cast<CMainFrame*>( AfxGetMainWnd());
	if( pFrame)
		pFrame->SetChanged();
}

void CLeftView::OnUpdateLevel( int nIndex)
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	if( hItem)
	{
		CAttributeDesc::CAttributeDescMap& map = m_bIsResource ? CGlobalInfo::GetInstance().GetZipDesc().GetMap() 
			: CGlobalInfo::GetInstance().GetSetupDesc().GetMap();
		if( (nIndex >= 0) && (nIndex <= (int)map.size()))
		{
			CAttributeDesc::CAttributeDescMap::iterator itor = map.begin();
			while( nIndex--)
				itor++;
			CZipTreeItem* pItem = reinterpret_cast<CZipTreeItem*>( GetTreeCtrl().GetItemData( hItem));
			//if( !m_bIsResource && CGlobalInfo::ZpdID == itor->first )
			//{
			//	if( !pItem->GetIsPack())
			//	{
			//		AfxMessageBox( L"非pack文件不能设置为 'Encode to zipped'压缩方式！");
			//		return;
			//	}
			//}

			CSetZipLevelOperator setter( m_root, *m_project, pItem, m_bIsResource, itor->first);
			setter.Do();
			SetChanged();

			//RefreshItemIcon( hItem);
			GetTreeCtrl().Invalidate( );
		}
	}
}

void CLeftView::OnSetIsPack()
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	if( hItem)
	{
		CZipTreeItem* pItem = reinterpret_cast<CZipTreeItem*>( GetTreeCtrl().GetItemData( hItem));
		if( IsItemCanPack( *pItem))
		{
			CSetPackOperator setter( *pItem, *m_project, !pItem->GetIsPack());
			setter.Do();
			SetChanged();

			GetTreeCtrl().Invalidate( );
		}
	}
}

void CLeftView::OnSetUrl( )
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	if( hItem)
	{
		CZipTreeItem* pItem = reinterpret_cast<CZipTreeItem*>( GetTreeCtrl().GetItemData( hItem));
		if( pItem)
		{
			CAddNewPathDlg dlg( this, true);
			dlg.SetUrl( pItem->GetUrl());
			if( IDOK == dlg.DoModal())
			{
				CSetUrlOperator setter( *pItem, *m_project, dlg.m_path.GetString());
				setter.Do();
				SetChanged();
			}
			GetTreeCtrl().Invalidate( );
		}
	}
}

void CLeftView::OnSetChildPackage()
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	if( hItem)
	{
		CZipTreeItem* pItem = reinterpret_cast<CZipTreeItem*>( GetTreeCtrl().GetItemData( hItem));
		if( pItem)
		{
			CSetChildPackageOperator setter( *pItem, *m_project, !pItem->GetIsChildPackage());
			setter.Do();
			SetChanged();
			GetTreeCtrl().Invalidate( );
		}
	}
}

void CLeftView::OnNMClick(NMHDR *pNMHDR, LRESULT *pResult)
{
	CPoint pt;
	GetCursorPos(&pt);
	CPoint ptClient(pt);
	ScreenToClient(&ptClient);
	UINT uFlags;
	HTREEITEM hItem = GetTreeCtrl().HitTest(ptClient, &uFlags);
	if( hItem && ( TVHT_ONITEMSTATEICON & uFlags))
	{
		CZipTreeItem* pItem = (CZipTreeItem*)GetTreeCtrl().GetItemData( hItem);

		// 可能会耗时，在线程里做
		COperator::COperatorVector ov;
		CSetCheckedOperator* setter = new CSetCheckedOperator( *pItem, *m_project, !IsItemChecked( hItem));
		ov.push_back( setter);

		CSaveProgressDlg dlgProgress( true);
		dlgProgress.SetOperators( ov);
		dlgProgress.DoModal( );

		delete setter;
		
		SetChanged();
		RefreshItemStateIcon( hItem);
	}
	*pResult = 0;
}

void CLeftView::SetIsResource( bool bIsResource)
{
	if( m_bIsResource == bIsResource)
		return;
	m_bIsResource = bIsResource;
	//if( m_bIsResource)
	//	GetTreeCtrl().SetImageList( &CGlobalInfo::GetInstance().GetZipIconList(), TVSIL_NORMAL);
	//else
	//	GetTreeCtrl().SetImageList( &CGlobalInfo::GetInstance().GetSetupIconList(), TVSIL_NORMAL);
	//RefreshItemIcon( GetTreeCtrl().GetRootItem());
	GetTreeCtrl().Invalidate( );
	
	//HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	//CZipTreeItem* pItem = NULL;
	//if( hItem)
	//	pItem = (CZipTreeItem*)GetTreeCtrl().GetItemData( hItem);
	//for( CListenerVector::iterator itor = m_vtListener.begin(); itor != m_vtListener.end(); ++itor)
	//{
	//	(*itor)->SetIsResource( m_bIsResource);
	//	(*itor)->SetZipTreeItem( pItem);
	//}
}

//void CLeftView::RefreshItemIcon( HTREEITEM hItem)
//{
//	if( !hItem)
//		return;
//	CZipTreeItem* pItem = (CZipTreeItem*)GetTreeCtrl().GetItemData( hItem);
//	if( !pItem)
//		return;
//	int nImage = m_bIsResource ? CGlobalInfo::GetInstance().GetZipDesc().LevelToIconIndex( pItem->GetZipLevel(), false)
//		: CGlobalInfo::GetInstance().GetSetupDesc().LevelToIconIndex( pItem->GetAttributes(), pItem->GetIsPack());
//	GetTreeCtrl().SetItemImage( hItem, nImage, nImage);
//
//	hItem = GetTreeCtrl().GetChildItem(hItem);
//	while (hItem)
//	{
//		RefreshItemIcon( hItem);
//		hItem = GetTreeCtrl().GetNextItem( hItem, TVGN_NEXT);
//	}
//}

// 刷新是否忽略
void CLeftView::RefreshIsCheck( )
{
	CRefreshCheckOperator operate( m_root, *m_project);
	operate.Do();
	RefreshItemStateIcon( GetTreeCtrl().GetRootItem());
}

void CLeftView::RefreshItemStateIcon( HTREEITEM hItem)
{
	if( !hItem)
		return;
	CZipTreeItem* pItem = (CZipTreeItem*)GetTreeCtrl().GetItemData( hItem);
	if( !pItem)
		return;
	GetTreeCtrl().SetItemState(hItem, INDEXTOSTATEIMAGEMASK( pItem->GetIsCheck() ? 2 : 1), TVIS_STATEIMAGEMASK);

	hItem = GetTreeCtrl().GetChildItem(hItem);
	while (hItem)
	{
		RefreshItemStateIcon( hItem);
		hItem = GetTreeCtrl().GetNextItem( hItem, TVGN_NEXT);
	}
}

void CLeftView::RefreshZipLevel( )
{
	CRefreshZipLevelOperator operate( m_root, *m_project);
	operate.Do();
	//RefreshItemIcon( GetTreeCtrl().GetRootItem());
	SetChanged();
	GetTreeCtrl().Invalidate( );
}

void CLeftView::RefreshShowUnChecked( const CZipTreeItem& item, HTREEITEM parent)
{
	//if( parent ? GetTreeCtrl().GetItemState( parent, TVIS_EXPANDEDONCE) & TVIS_EXPANDEDONCE : true)
	{
		const CZipTreeItem::CZipTreeItemList& map = item.GetChild();
		for( CZipTreeItem::CZipTreeItemList::const_iterator itor = map.begin(); itor != map.end(); ++itor)
		{
			HTREEITEM hItem = FindZipItemInTree( *itor, parent);
			if( itor->GetIsCheck() || m_bShowUnChecked )
			{
				if( hItem)
					RefreshShowUnChecked( *itor, hItem);
				else
					RefreshAddItem( *itor, parent);
			}
			else
			{
				RefreshDeleteItem( hItem);
			}
		}
	}
}

HTREEITEM CLeftView::FindZipItemInTree( const CZipTreeItem& child, HTREEITEM parent)
{
	parent = GetTreeCtrl().GetChildItem( parent);
	while( parent)
	{
		if( reinterpret_cast<CZipTreeItem*>( GetTreeCtrl().GetItemData( parent)) == &child)
			return parent;
		parent = GetTreeCtrl().GetNextItem( parent, TVGN_NEXT);
	}
	return NULL;
}

void CLeftView::RefreshTreeItem( const CZipTreeItem& item, int nType)
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	if( (COperator::PT_ADDEND_PROJECT == nType) || (COperator::PT_DELETEEND_PROJECT == nType))
		hItem = FindZipItemInTree( item, GetTreeCtrl().GetRootItem());
	switch( nType)
	{
	case COperator::PT_ADDEND:
		RefreshAddItem( item, hItem );
		break;
	case COperator::PT_ADDEND_PROJECT:
		if( item.GetParent() == &m_root && !hItem)
			RefreshAddItem( item, GetTreeCtrl().GetRootItem());
		break;
	case COperator::PT_SETCHECKED:
		RefreshItemStateIcon( hItem);
		break;
	case COperator::PT_DELETEEND:
	case COperator::PT_DELETEEND_PROJECT:
		RefreshDeleteItem( hItem);
		break;
	}
}

HTREEITEM CLeftView::RefreshAddItem( const CZipTreeItem& item, HTREEITEM parent)
{
	HTREEITEM hItem = NULL;
	if( parent)
	{
		if( GetTreeCtrl().GetItemState( parent, TVIS_EXPANDEDONCE) & TVIS_EXPANDEDONCE)
			hItem = InsertItem( item, parent);
		SetHasChild( parent, true);
	}
	else
		hItem = InsertItem( item, parent);
	return hItem;
}

void CLeftView::RefreshDeleteItem( HTREEITEM deleteitem)
{
	if( deleteitem)
	{
		HTREEITEM parent = GetTreeCtrl().GetParentItem( deleteitem);
		GetTreeCtrl().DeleteItem( deleteitem);
		if( parent)
		{
			if( !GetTreeCtrl().ItemHasChildren( parent))
				SetHasChild( parent, false);
		}
	}
}

void CLeftView::SetHasChild( HTREEITEM parent, bool bHasChild)
{
	TVITEM item;
	item.hItem = parent;
	item.cChildren = bHasChild ? 1 : 0;
	item.mask = TVIF_CHILDREN;
	GetTreeCtrl().SetItem( &item);
}

void CLeftView::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	if( nChar == VK_DELETE)
		AfxGetMainWnd()->PostMessage( WM_COMMAND, ID_EDIT_DELETE, 0);
	CTreeView::OnKeyDown(nChar, nRepCnt, nFlags);
}

void CLeftView::OnSetIsMakeMD5()
{
	m_project->SetIsMakeMD5( !m_project->GetIsMakeMD5());
}

// 刷新project显示名
void CLeftView::RefreshProjectShowName()
{
	if( m_project)
	{
		CTreeCtrl& tc = GetTreeCtrl();
		tc.SetItemText( tc.GetRootItem(), m_project->GetName().c_str());
	}
}
