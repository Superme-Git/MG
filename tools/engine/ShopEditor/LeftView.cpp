// LeftView.cpp : implementation file
//

#include "stdafx.h"
#include "ShopEditor.h"
#include "LeftView.h"
#include "MainFrm.h"
#include "ShopEditorDoc.h"
#include "RightView.h"
#include "SelectCategoryDlg.h"
// CLeftView

IMPLEMENT_DYNCREATE(CLeftView, CTreeView)

CLeftView::CLeftView()
: m_pRightView( NULL)
{

}

CLeftView::~CLeftView()
{
}

BEGIN_MESSAGE_MAP(CLeftView, CTreeView)
	ON_COMMAND( ID_CATEGORY_ADD, OnAddBigCategory)
	ON_COMMAND( ID_SMALLCATEGORY_ADD, OnAddSmallCategory)
	ON_COMMAND( ID_ITEM_ADD, OnAddBigCategory)
	ON_COMMAND( ID_ITEM_DELETE, OnDeleteBigCategory)
	ON_COMMAND( ID_ITEM_RENAME, OnRenameCategory)
	ON_COMMAND( ID_ITEM_MOVEUP, OnMoveUpBigCategory)
	ON_COMMAND( ID_ITEM_MOVEDOWN, OnMoveDownBigCategory)
	ON_COMMAND( ID_CATEGORY_RENAME, OnRenameCategory)
	ON_COMMAND( ID_CATEGORY_DELETE, OnDeleteBigCategory)
	ON_COMMAND( ID_CATEGORY_MOVEUP, OnMoveUpBigCategory)
	ON_COMMAND( ID_CATEGORY_MOVEDOWN, OnMoveDownBigCategory)
	
	ON_NOTIFY_REFLECT(NM_RCLICK, &CLeftView::OnNMRclick)
	ON_WM_CREATE()
	ON_NOTIFY_REFLECT(TVN_ENDLABELEDIT, &CLeftView::OnTvnEndlabeledit)
	ON_NOTIFY_REFLECT(TVN_SELCHANGED, &CLeftView::OnTvnSelchanged)
	
	ON_UPDATE_COMMAND_UI(ID_CATEGORY_MOVEUP, &CLeftView::OnUpdateItemMoveup)
	ON_UPDATE_COMMAND_UI(ID_CATEGORY_MOVEDOWN, &CLeftView::OnUpdateItemMovedown)
	ON_UPDATE_COMMAND_UI(ID_ITEM_MOVEUP, &CLeftView::OnUpdateItemMoveup)
	ON_UPDATE_COMMAND_UI(ID_ITEM_MOVEDOWN, &CLeftView::OnUpdateItemMovedown)
	ON_UPDATE_COMMAND_UI(ID_CATEGORY_ADD, &CLeftView::OnUpdateCategoryAdd)
	ON_UPDATE_COMMAND_UI(ID_CATEGORY_DELETE, &CLeftView::OnUpdateCategoryDelete)
	ON_UPDATE_COMMAND_UI(ID_CATEGORY_RENAME, &CLeftView::OnUpdateCategoryRename)
END_MESSAGE_MAP()

// CLeftView message handlers

void CLeftView::OnNMRclick(NMHDR *pNMHDR, LRESULT *pResult)
{
	// right click to select item
	CTreeCtrl& rTree = GetTreeCtrl();
	CPoint pt;
	GetCursorPos(&pt);
	CPoint ptClient(pt);
	ScreenToClient(&ptClient);
	UINT uFlags;
	HTREEITEM hItem = rTree.HitTest( ptClient, &uFlags);
	if(hItem)
	{
		rTree.SelectItem(hItem);
	}
	CMenu popupMenu;
	popupMenu.LoadMenu( IDR_MENU_BIGCATEGORY );
	CMenu *pSubMenu = popupMenu.GetSubMenu( 0);
	if( pSubMenu )
	{
		HTREEITEM hItem = rTree.GetSelectedItem();
		if( NULL == hItem )
		{
			pSubMenu->EnableMenuItem( ID_CATEGORY_DELETE, MF_BYCOMMAND|MF_GRAYED);
			pSubMenu->EnableMenuItem( ID_CATEGORY_RENAME, MF_BYCOMMAND|MF_GRAYED);
			pSubMenu->EnableMenuItem( ID_CATEGORY_MOVEUP, MF_BYCOMMAND|MF_GRAYED);
			pSubMenu->EnableMenuItem( ID_CATEGORY_MOVEDOWN, MF_BYCOMMAND|MF_GRAYED);
		}

		CPoint point;
		::GetCursorPos( &point);
		CPoint pt( point);
		ScreenToClient( &pt);

		pSubMenu->TrackPopupMenu( 0, point.x, point.y, this );
	}
	*pResult = 0;
}

void CLeftView::OnAddBigCategory( )
{
	CTreeCtrl& rTree = GetTreeCtrl();
	HTREEITEM hSelected = rTree.GetSelectedItem();
	HTREEITEM hRes;
	CBigCategoryManager& manager = GetShopItemManager();
	// 若当前选择的是大类，则添加在该类后面，若当前选择为小类，则添加在该类的父类后面，若未选择任何类，则添加在最后
	if( hSelected )
	{
		HTREEITEM hParent = rTree.GetParentItem(hSelected);
		hParent = hParent ? hParent : hSelected;
		CBigCategory* bcp = manager.InsertItem((CBigCategory*)rTree.GetItemData(hParent), L"新建类");
		if(bcp)
			hRes = AddBigCategoryToTree( *bcp, hParent);
		else
		{
			AfxMessageBox(L"创建失败！");
			return;
		}
	}
	else
	{
		CBigCategory* bcp = manager.AddItem( L"新建类");
		if(bcp)
			hRes = AddBigCategoryToTree( *bcp, TVI_LAST);
		else
		{
			AfxMessageBox(L"创建失败！");
			return;
		}
	}
	if( hRes)
	{
		GetTreeCtrl().SelectItem( hRes );
		GetTreeCtrl().EditLabel( hRes );
	}
	SetModifyFlag();
}

void CLeftView::OnAddSmallCategory( )
{
	CTreeCtrl& rTree = GetTreeCtrl();
	HTREEITEM hSelected = rTree.GetSelectedItem();
	HTREEITEM hRes = NULL;
	// 若当前选择的是大类，则添加在该类的子类里面，若当前选择为小类，则平行添加
	if(hSelected)
	{
		HTREEITEM hParent = rTree.GetParentItem(hSelected);
		HTREEITEM hPrev = hParent ? hSelected : TVI_LAST;
		hParent = hParent ? hParent : hSelected;
		CBigCategory* bcp = (CBigCategory*)rTree.GetItemData(hParent);
		CSmallCategory *scp = NULL;
		if( hPrev == hSelected )
			scp	= bcp->InsertItem( (CSmallCategory*)rTree.GetItemData(hSelected), L"新建类" );
		else
			scp	= bcp->AddItem(L"新建类");
		if(scp)
			hRes = AddSmallCategoryToTree( *scp, hParent, hPrev );
		else
		{
			AfxMessageBox(L"创建失败！");
			return;
		}
	}
	else
	{
		AfxMessageBox(L"请选择所属大类！");
		return;
	}
	if( hRes)
	{
		rTree.SelectItem( hRes );
		rTree.EditLabel( hRes );
	}
	SetModifyFlag();
}

// 添加一个大类
HTREEITEM CLeftView::AddBigCategoryToTree( const CBigCategory& bc, HTREEITEM hInsertAfter )
{
	if( hInsertAfter == NULL )
		hInsertAfter = TVI_FIRST;
	HTREEITEM hRes = GetTreeCtrl().InsertItem( bc.m_name.c_str(), DIR_CLOSE_ICON, DIR_OPEN_ICON, TVI_ROOT, hInsertAfter);
	GetTreeCtrl().SetItemData( hRes, (DWORD)&bc);
	
	return hRes;
}

// 添加一个小类
HTREEITEM CLeftView::AddSmallCategoryToTree( const CSmallCategory& sc, HTREEITEM hParent, HTREEITEM hInsertAfter )
{
	if( hInsertAfter == NULL )
		hInsertAfter = TVI_FIRST;
	CTreeCtrl& rTree = GetTreeCtrl();
	HTREEITEM hRes = NULL;
	if(hParent)
	{
		hRes = rTree.InsertItem( sc.m_name.c_str(), DIR_CLOSE_ICON, DIR_OPEN_ICON, hParent, hInsertAfter);
		rTree.SetItemData( hRes, (DWORD)&sc);
	}
	
	return hRes;
}

//添加一个大类及该大类下的所有小类
HTREEITEM CLeftView::AddBigCategoryAndChildrenToTree( const CBigCategory& bc, HTREEITEM hInsertAfter )
{
	if( hInsertAfter == NULL )
		hInsertAfter = TVI_FIRST;
	CTreeCtrl& rTree = GetTreeCtrl();
	HTREEITEM hRes = rTree.InsertItem( bc.m_name.c_str(), DIR_CLOSE_ICON, DIR_OPEN_ICON, TVI_ROOT, hInsertAfter);
	//遍历添加大类下的所有小类
	for(CSmallCategoryList::const_iterator itor = bc.m_items.begin(), end = bc.m_items.end(); itor != end; ++itor)
	{
		AddSmallCategoryToTree(*itor, hRes, TVI_LAST);
	}
	rTree.SetItemData( hRes, (DWORD)&bc);
	
	return hRes;
}

int CLeftView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CTreeView::OnCreate(lpCreateStruct) == -1)
		return -1;

	GetTreeCtrl().ModifyStyle( 0, TVS_HASBUTTONS|TVS_HASLINES|TVS_LINESATROOT|
		TVS_SHOWSELALWAYS|TVS_EDITLABELS);//|TVS_CHECKBOXES);

	m_imageList.Create( 16, 16, ILC_COLOR32 | ILC_MASK, 0, 4);
	SHFILEINFO sfi;
	SHGetFileInfo(L"", FILE_ATTRIBUTE_DIRECTORY, &sfi, sizeof(sfi), SHGFI_ICON|SHGFI_SMALLICON);
	if (sfi.hIcon)
	{
		// 文件夹图标
		m_imageList.Add( sfi.hIcon);
	}
	SHGetFileInfo(L"", FILE_ATTRIBUTE_DIRECTORY, &sfi, sizeof(sfi),	SHGFI_ICON|SHGFI_SMALLICON|SHGFI_OPENICON);
	if (sfi.hIcon)
	{
		// 文件夹打开图标
		m_imageList.Add( sfi.hIcon);
	}
	GetTreeCtrl().SetImageList( &m_imageList, TVSIL_NORMAL);

	return 0;
}

CBigCategoryManager& CLeftView::GetShopItemManager()
{
	CShopEditorDoc* pDoc = (CShopEditorDoc*)GetDocument();
	return pDoc->m_manager;
}

void CLeftView::OnTvnEndlabeledit(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTVDISPINFO pTVDispInfo = reinterpret_cast<LPNMTVDISPINFO>(pNMHDR);
	
	LPCTSTR pszText = pTVDispInfo->item.pszText;
	if( pszText && pszText[0])
	{
		HTREEITEM hRes = pTVDispInfo->item.hItem;
		CBaseItem* bip = ( CBaseItem* )GetTreeCtrl().GetItemData( hRes );
		if( bip)
		{		
			bip->m_name = pszText;
			GetTreeCtrl().SetItemText( hRes, pszText);
			SetModifyFlag();
		}
	}
	*pResult = 0;
}

void CLeftView::OnInitialUpdate()
{
	// 清除文档内容发生在WM_INITIALUPDATE消息之前，这会导致
	// CDocument::DeleteContents()函数的调用早于下面的DeleteAllItems()的调用。
	// 而且，由于DeleteAllItems()过程可能会触发TVN_SELCHANGED通告，
	// 此通告会访问TREEITEM所绑定的category对象，而此时，category对象都已经释放完毕，
	// 因而，逻辑上存在矛盾。
	// 为了解决这个问题，必须保证CDocument::DeleteContents()函数执行之后，
	// 不能存在任何访问TREEITEM的ItemData相关的代码, 最好不要有任何TVN_XXXXX通告。
	// 详见 HINT_BEFORE_DELETE_CONTENTS 的使用方式。
	//   --- GetTreeCtrl().DeleteAllItems();

	// 保证此时TREECTRL没有任何结点。
	ASSERT( GetTreeCtrl().GetRootItem() == NULL );

	CBigCategoryManager& manager = GetShopItemManager();
	const CBigCategoryList& bcl = manager.m_items;
	//遍历添加大类
	for( CBigCategoryList::const_iterator itor = bcl.begin(), end = bcl.end(); itor != end; ++itor)
	{	
		HTREEITEM hParent = AddBigCategoryToTree( *itor, TVI_LAST);
		const CSmallCategoryList& scl = itor->m_items;
		//遍历添加小类
		for(CSmallCategoryList::const_iterator s_itor = scl.begin(), s_end = scl.end(); s_itor != s_end; ++s_itor)
		{
			AddSmallCategoryToTree(*s_itor,hParent, TVI_LAST);
		}
	}
}

void CLeftView::OnRenameCategory()
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	if( hItem != NULL)
		GetTreeCtrl().EditLabel( hItem );
}

void CLeftView::OnDeleteBigCategory()
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	HTREEITEM hParent = GetTreeCtrl().GetParentItem(hItem);
	if( hItem != NULL)
	{
		if(hParent)
		{
			//删除小类
			CSmallCategory* scp = (CSmallCategory*)GetTreeCtrl().GetItemData( hItem);
			if( scp)
			{
				CString str;
				str.Format( L"确认删除 \"%s\" 类？该类中有%u个物品。",
					scp->m_name.c_str(), scp->GetItemCount() );

				if( scp->IsEmpty() ||
					IDYES == AfxMessageBox( str, MB_YESNO|MB_DEFBUTTON2))
				{
					CBigCategory* bc = (CBigCategory*)GetTreeCtrl().GetItemData( hParent);
					if( bc->DeleteItem( scp))
					{
						GetTreeCtrl().DeleteItem( hItem);
						SetModifyFlag();
					}
				}
			}
		}
		else
		{
			//删除大类
			CBigCategory* bcp = (CBigCategory*)GetTreeCtrl().GetItemData( hItem);
			if( bcp)
			{
				CString str;
				str.Format( L"确认删除 \"%s\" 类？该类下有%u个子类将会一并删除。",
					bcp->m_name.c_str(), bcp->GetItemCount() );

				if( bcp->IsEmpty() ||
					IDYES == AfxMessageBox( str, MB_YESNO|MB_DEFBUTTON2))
				{
					CBigCategoryManager& manager = GetShopItemManager();
					if( manager.DeleteItem( bcp))
					{
						GetTreeCtrl().DeleteItem( hItem);
						SetModifyFlag();
					}
				}
			}
		}
	}
}

void CLeftView::OnTvnSelchanged(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	
	CBigCategory* bcp = NULL;
	CSmallCategory* scp = NULL;
	HTREEITEM hSel = pNMTreeView->itemNew.hItem;
	HTREEITEM hParent = GetTreeCtrl().GetParentItem( hSel);
	m_pRightView->SetBigCategory( hParent?(CBigCategory*)GetTreeCtrl().GetItemData(hParent):NULL );
	if( NULL == hParent )
	{
		// 选择一个大类时，遍历添加小类的所有item在RightView中显示
		bcp = (CBigCategory*)GetTreeCtrl().GetItemData( hSel);
		m_pRightView->ClearShopItemsList();
		if(bcp->m_items.size()!=0)
			for(CSmallCategoryList::const_iterator itor=bcp->m_items.begin(), e=bcp->m_items.end(); itor!=e;++itor)
			{
				m_pRightView->AddAllShopItemsToList( (CSmallCategory*)&(*itor), false);
			}
		GetTreeCtrl().Expand(hSel,TVE_EXPAND);
		m_pRightView->SetSmallCategory( NULL );
	}
	else
	{
		// 选择一个小类只显示该小类下的item
		scp = (CSmallCategory*)GetTreeCtrl().GetItemData( hSel);
		m_pRightView->SetSmallCategory( scp );
		if( m_pRightView)
		{
			m_pRightView->AddAllShopItemsToList( scp, true);
		}
	}
	*pResult = 0;
}

void CLeftView::OnMoveUpBigCategory()
{
	CTreeCtrl& rTree = GetTreeCtrl();
	HTREEITEM hItem = rTree.GetSelectedItem();
	HTREEITEM hParent = rTree.GetParentItem(hItem);
	if( hItem != NULL)
	{
		// 通过将选中项目前一项往后移实现本结点往前移的效果。
		// 好处在于，这可以避免页面重刷。
		if(hParent)
		{
			// 上移一个小类，只在父类范围内移动，不会跨大类移动
			HTREEITEM hBefore = rTree.GetPrevSiblingItem( hItem);
			CBigCategory* bcp = (CBigCategory*)rTree.GetItemData( hParent);
			if( hBefore )
			{
				CSmallCategory* scp = (CSmallCategory*)rTree.GetItemData( hBefore);
				scp = bcp->MoveDown( scp );
				if( scp )
				{
					HTREEITEM hRes = AddSmallCategoryToTree( *scp ,hParent, hItem);
					if( hRes )
					{
						rTree.DeleteItem( hBefore);
						SetModifyFlag();
					}
				}
			}
		}
		else
		{
			//上移一个大类
			HTREEITEM hBefore = rTree.GetPrevSiblingItem( hItem );
			if( hBefore)
			{
				CBigCategory* bcp = (CBigCategory*)rTree.GetItemData( hBefore);
				CBigCategoryManager& manager = GetShopItemManager();
				bcp = manager.MoveDown( bcp);
				if( bcp )
				{
					HTREEITEM hRes = AddBigCategoryAndChildrenToTree( *bcp, hItem );
					if( hRes)
					{
						rTree.DeleteItem( hBefore );
					}

					SetModifyFlag();
				}
			}
		}
	}
}

void CLeftView::OnMoveDownBigCategory()
{
	CTreeCtrl& rTree = GetTreeCtrl();
	HTREEITEM hItem = rTree.GetSelectedItem();
	HTREEITEM hParent = rTree.GetParentItem(hItem);
	if(hItem )
	{
		HTREEITEM hAfter = rTree.GetNextSiblingItem( hItem);
		if(hParent)
		{
			//下移一个小类
			CBigCategory* bcp = (CBigCategory*)rTree.GetItemData( hParent);
			if( hAfter )
			{
				CSmallCategory* scp = (CSmallCategory*)rTree.GetItemData( hAfter);
				scp = bcp->MoveUp( scp );
				if( scp )
				{
					HTREEITEM hBefore = rTree.GetNextItem( hItem, TVGN_PREVIOUS );
					HTREEITEM hRes = AddSmallCategoryToTree( *scp ,hParent, hBefore);
					if( hRes )
					{
						rTree.DeleteItem( hAfter);
						SetModifyFlag();
					}
				}
			}
		}
		else
		{
			// 通过将选中项目后一项往前移实现本结点往后移的效果。
			// 好处在于，这可以避免页面重刷。
			//下移一个大类
			if( hAfter )
			{
				CBigCategory* bcp = (CBigCategory*)rTree.GetItemData( hAfter );
				CBigCategoryManager& manager = GetShopItemManager();
				bcp = manager.MoveUp( bcp );
				if( bcp )
				{
					HTREEITEM hBefore = rTree.GetNextItem( hItem, TVGN_PREVIOUS );
					HTREEITEM hRes = AddBigCategoryAndChildrenToTree( *bcp, hBefore );
					if( hRes )
					{
						rTree.DeleteItem( hAfter);
						SetModifyFlag();
					}
				}
			}
		}
	}
}

void CLeftView::OnUpdateItemMoveup(CCmdUI *pCmdUI)
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	if(hItem )
	{
		HTREEITEM hAfter = GetTreeCtrl().GetNextItem( hItem, TVGN_PREVIOUS );
		pCmdUI->Enable( hAfter != NULL );
	}
	else
	{
		pCmdUI->Enable( FALSE );
	}
}

void CLeftView::OnUpdateItemMovedown(CCmdUI *pCmdUI)
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	if(hItem )
	{
		HTREEITEM hNext = GetTreeCtrl().GetNextItem( hItem, TVGN_NEXT );
		pCmdUI->Enable( hNext != NULL );
	}
	else
	{
		pCmdUI->Enable( FALSE );
	}
}

void CLeftView::OnUpdateCategoryAdd(CCmdUI *pCmdUI)
{
	// Always true.
	pCmdUI->Enable( TRUE );
}

void CLeftView::OnUpdateCategoryDelete(CCmdUI *pCmdUI)
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	pCmdUI->Enable( hItem != NULL );
}

void CLeftView::OnUpdateCategoryRename(CCmdUI *pCmdUI)
{
	HTREEITEM hItem = GetTreeCtrl().GetSelectedItem();
	pCmdUI->Enable( hItem != NULL );
}

void CLeftView::OnUpdate(CView* /*pSender*/, LPARAM lHint, CObject* /*pHint*/)
{
	// 清除文档内容之前的通告
	if( lHint == HINT_BEFORE_DELETE_CONTENTS )
	{
		// 注意下面的顺序，首先删除TREE Items，然后处理ListItems.
		GetTreeCtrl().DeleteAllItems();
		if( m_pRightView != NULL )
		{
			m_pRightView->SetBigCategory( NULL );
			m_pRightView->SetSmallCategory( NULL );
			m_pRightView->ClearShopItemsList();
		}
	}	
}
