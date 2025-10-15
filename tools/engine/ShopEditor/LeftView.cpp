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
	// ����ǰѡ����Ǵ��࣬������ڸ�����棬����ǰѡ��ΪС�࣬������ڸ���ĸ�����棬��δѡ���κ��࣬����������
	if( hSelected )
	{
		HTREEITEM hParent = rTree.GetParentItem(hSelected);
		hParent = hParent ? hParent : hSelected;
		CBigCategory* bcp = manager.InsertItem((CBigCategory*)rTree.GetItemData(hParent), L"�½���");
		if(bcp)
			hRes = AddBigCategoryToTree( *bcp, hParent);
		else
		{
			AfxMessageBox(L"����ʧ�ܣ�");
			return;
		}
	}
	else
	{
		CBigCategory* bcp = manager.AddItem( L"�½���");
		if(bcp)
			hRes = AddBigCategoryToTree( *bcp, TVI_LAST);
		else
		{
			AfxMessageBox(L"����ʧ�ܣ�");
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
	// ����ǰѡ����Ǵ��࣬������ڸ�����������棬����ǰѡ��ΪС�࣬��ƽ�����
	if(hSelected)
	{
		HTREEITEM hParent = rTree.GetParentItem(hSelected);
		HTREEITEM hPrev = hParent ? hSelected : TVI_LAST;
		hParent = hParent ? hParent : hSelected;
		CBigCategory* bcp = (CBigCategory*)rTree.GetItemData(hParent);
		CSmallCategory *scp = NULL;
		if( hPrev == hSelected )
			scp	= bcp->InsertItem( (CSmallCategory*)rTree.GetItemData(hSelected), L"�½���" );
		else
			scp	= bcp->AddItem(L"�½���");
		if(scp)
			hRes = AddSmallCategoryToTree( *scp, hParent, hPrev );
		else
		{
			AfxMessageBox(L"����ʧ�ܣ�");
			return;
		}
	}
	else
	{
		AfxMessageBox(L"��ѡ���������࣡");
		return;
	}
	if( hRes)
	{
		rTree.SelectItem( hRes );
		rTree.EditLabel( hRes );
	}
	SetModifyFlag();
}

// ���һ������
HTREEITEM CLeftView::AddBigCategoryToTree( const CBigCategory& bc, HTREEITEM hInsertAfter )
{
	if( hInsertAfter == NULL )
		hInsertAfter = TVI_FIRST;
	HTREEITEM hRes = GetTreeCtrl().InsertItem( bc.m_name.c_str(), DIR_CLOSE_ICON, DIR_OPEN_ICON, TVI_ROOT, hInsertAfter);
	GetTreeCtrl().SetItemData( hRes, (DWORD)&bc);
	
	return hRes;
}

// ���һ��С��
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

//���һ�����༰�ô����µ�����С��
HTREEITEM CLeftView::AddBigCategoryAndChildrenToTree( const CBigCategory& bc, HTREEITEM hInsertAfter )
{
	if( hInsertAfter == NULL )
		hInsertAfter = TVI_FIRST;
	CTreeCtrl& rTree = GetTreeCtrl();
	HTREEITEM hRes = rTree.InsertItem( bc.m_name.c_str(), DIR_CLOSE_ICON, DIR_OPEN_ICON, TVI_ROOT, hInsertAfter);
	//������Ӵ����µ�����С��
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
		// �ļ���ͼ��
		m_imageList.Add( sfi.hIcon);
	}
	SHGetFileInfo(L"", FILE_ATTRIBUTE_DIRECTORY, &sfi, sizeof(sfi),	SHGFI_ICON|SHGFI_SMALLICON|SHGFI_OPENICON);
	if (sfi.hIcon)
	{
		// �ļ��д�ͼ��
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
	// ����ĵ����ݷ�����WM_INITIALUPDATE��Ϣ֮ǰ����ᵼ��
	// CDocument::DeleteContents()�����ĵ������������DeleteAllItems()�ĵ��á�
	// ���ң�����DeleteAllItems()���̿��ܻᴥ��TVN_SELCHANGEDͨ�棬
	// ��ͨ������TREEITEM���󶨵�category���󣬶���ʱ��category�����Ѿ��ͷ���ϣ�
	// ������߼��ϴ���ì�ܡ�
	// Ϊ�˽��������⣬���뱣֤CDocument::DeleteContents()����ִ��֮��
	// ���ܴ����κη���TREEITEM��ItemData��صĴ���, ��ò�Ҫ���κ�TVN_XXXXXͨ�档
	// ��� HINT_BEFORE_DELETE_CONTENTS ��ʹ�÷�ʽ��
	//   --- GetTreeCtrl().DeleteAllItems();

	// ��֤��ʱTREECTRLû���κν�㡣
	ASSERT( GetTreeCtrl().GetRootItem() == NULL );

	CBigCategoryManager& manager = GetShopItemManager();
	const CBigCategoryList& bcl = manager.m_items;
	//������Ӵ���
	for( CBigCategoryList::const_iterator itor = bcl.begin(), end = bcl.end(); itor != end; ++itor)
	{	
		HTREEITEM hParent = AddBigCategoryToTree( *itor, TVI_LAST);
		const CSmallCategoryList& scl = itor->m_items;
		//�������С��
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
			//ɾ��С��
			CSmallCategory* scp = (CSmallCategory*)GetTreeCtrl().GetItemData( hItem);
			if( scp)
			{
				CString str;
				str.Format( L"ȷ��ɾ�� \"%s\" �ࣿ��������%u����Ʒ��",
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
			//ɾ������
			CBigCategory* bcp = (CBigCategory*)GetTreeCtrl().GetItemData( hItem);
			if( bcp)
			{
				CString str;
				str.Format( L"ȷ��ɾ�� \"%s\" �ࣿ��������%u�����ཫ��һ��ɾ����",
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
		// ѡ��һ������ʱ���������С�������item��RightView����ʾ
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
		// ѡ��һ��С��ֻ��ʾ��С���µ�item
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
		// ͨ����ѡ����Ŀǰһ��������ʵ�ֱ������ǰ�Ƶ�Ч����
		// �ô����ڣ�����Ա���ҳ����ˢ��
		if(hParent)
		{
			// ����һ��С�ֻ࣬�ڸ��෶Χ���ƶ������������ƶ�
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
			//����һ������
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
			//����һ��С��
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
			// ͨ����ѡ����Ŀ��һ����ǰ��ʵ�ֱ���������Ƶ�Ч����
			// �ô����ڣ�����Ա���ҳ����ˢ��
			//����һ������
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
	// ����ĵ�����֮ǰ��ͨ��
	if( lHint == HINT_BEFORE_DELETE_CONTENTS )
	{
		// ע�������˳������ɾ��TREE Items��Ȼ����ListItems.
		GetTreeCtrl().DeleteAllItems();
		if( m_pRightView != NULL )
		{
			m_pRightView->SetBigCategory( NULL );
			m_pRightView->SetSmallCategory( NULL );
			m_pRightView->ClearShopItemsList();
		}
	}	
}
