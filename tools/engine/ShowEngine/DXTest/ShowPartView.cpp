// ShowPartView.cpp : implementation file
//

#include "stdafx.h"
#include "DXTest.h"
#include "ShowPartView.h"

LPCTSTR g_szTypeName[] = {
	_T("上衣"),
	_T("裤子"),
	_T("未知2"),
	_T("未知3"),
	_T("帽子"),
	_T("鞋"),
	_T("未知6"),
	_T("头发"),
	_T("未知8"),
	_T("头饰"),
	_T("背景"),
	_T("未知11"),
	_T("未知12"),
	_T("小装饰"),
	_T("像框"),
	_T("手镯"),
	_T("未知16"),
	_T("未知17"),
	_T("未知18"),
	_T("脸部"),
	_T("项链"),
	_T("胡须/耳环"),
	_T("眼镜"),
	_T("包"),
	_T("翅膀"),
	_T("未知25"),
	_T("腰带"),
	_T("未知27"),
	_T("宠物"),
};
// CShowPartView

IMPLEMENT_DYNCREATE(CShowPartView, CTreeView)

CShowPartView::CShowPartView()
: m_pDXTestView( NULL)
{

}

CShowPartView::~CShowPartView()
{
}

BEGIN_MESSAGE_MAP(CShowPartView, CTreeView)
	ON_WM_CREATE()
	ON_NOTIFY_REFLECT(TVN_ITEMEXPANDING, &CShowPartView::OnTvnItemexpanding)
	ON_NOTIFY_REFLECT(TVN_SELCHANGED, &CShowPartView::OnTvnSelchanged)
	ON_NOTIFY_REFLECT(NM_CLICK, &CShowPartView::OnNMClick)
END_MESSAGE_MAP()


// CShowPartView diagnostics

#ifdef _DEBUG
void CShowPartView::AssertValid() const
{
	CTreeView::AssertValid();
}

#ifndef _WIN32_WCE
void CShowPartView::Dump(CDumpContext& dc) const
{
	CTreeView::Dump(dc);
}
#endif
#endif //_DEBUG


// CShowPartView message handlers

int CShowPartView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CTreeView::OnCreate(lpCreateStruct) == -1)
		return -1;

	// TODO:  Add your specialized creation code here
	if( m_loader.Load() < 0)
	{
		return -1;
	}
	GetTreeCtrl().ModifyStyle( 0, TVS_HASBUTTONS|TVS_HASLINES|TVS_LINESATROOT|TVS_SHOWSELALWAYS|TVS_CHECKBOXES);
	GetTreeCtrl().SetImageList( &m_loader.GetImageList(), TVSIL_NORMAL);

	m_hManRoot = InsertOneItem( L"男", true, 0, 0, NULL);
	m_hWomenRoot = InsertOneItem( L"女", true, 0, 0, NULL);

	

	return 0;
}

void CShowPartView::InsertTypeItem( HTREEITEM hParent, long sex)
{
	int count = sizeof( g_szTypeName) / sizeof( g_szTypeName[0]);
	for( int i=0; i<count; i++)
	{
		InsertOneItem( g_szTypeName[i], TypeHasChild( i, sex), MAKELONG(i, sex), 0, hParent);
	}
}

// 插入一项
HTREEITEM CShowPartView::InsertOneItem( LPCTSTR name, bool hasChild, LPARAM lParam, int image, HTREEITEM hParent)
{
	TV_INSERTSTRUCT tvins;                // TreeView Insert Struct.

	tvins.item.mask = TVIF_TEXT | TVIF_PARAM ;
	tvins.item.lParam = lParam;

	wchar_t szName[MAX_PATH] = {0};
	wcscpy_s( szName, name);

	tvins.item.pszText = szName;
	tvins.item.cchTextMax = MAX_PATH;
	if( image >= 0)
	{
		tvins.item.mask |= TVIF_IMAGE | TVIF_SELECTEDIMAGE;
		tvins.item.iImage = image;
		tvins.item.iSelectedImage = tvins.item.iImage;
	}

	if( hasChild)
	{
		tvins.item.cChildren = 1;
		tvins.item.mask |= TVIF_CHILDREN;
	}

	tvins.hInsertAfter = NULL;
	tvins.hParent = hParent;

	return GetTreeCtrl().InsertItem(&tvins);
}

bool CShowPartView::TypeHasChild( long typeID, long sex)
{
	TShowPartContainer& parts = m_loader.GetShowPartContainer();
	for( TShowPartContainer::iterator i=parts.begin(), e=parts.end(); i != e; ++i)
	{
		ShowPartItem& part = *i;
		if( sex == part.bSexMan && typeID == part.lType)
		{
			return true;
		}
	}
	return false;
}

void CShowPartView::OnTvnItemexpanding(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	// TODO: Add your control notification handler code here
	if ((pNMTreeView->itemNew.state & TVIS_EXPANDEDONCE))
		return;
	HTREEITEM hItem = pNMTreeView->itemNew.hItem;
	if( hItem == m_hWomenRoot)
	{
		InsertTypeItem( hItem, 1);
	}
	else if( hItem == m_hManRoot)
	{
		InsertTypeItem( hItem, 0);
	}
	else
	{
		InsertItemsToType( hItem, GetTreeCtrl().GetItemData( hItem));
	}
	*pResult = 0;
}

void CShowPartView::InsertItemsToType( HTREEITEM hParent, LPARAM lParam)
{
	long typeID = LOWORD( lParam);
	long sex = HIWORD( lParam);
	TShowPartContainer& parts = m_loader.GetShowPartContainer();
	for( TShowPartContainer::iterator i=parts.begin(), e=parts.end(); i != e; ++i)
	{
		ShowPartItem& part = *i;
		if( sex == part.bSexMan && typeID == part.lType)
		{
			InsertOneItem( part.strSvg.c_str(), false, (LPARAM)&part, 
				m_loader.GetImageIndex(part.strTga.c_str()), hParent);
		}
	}
}

void CShowPartView::OnTvnSelchanged(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	// TODO: Add your control notification handler code here
	HTREEITEM hItem = pNMTreeView->itemNew.hItem;
	if( IsPartItem( hItem))
	{
		ShowPartItem* part = (ShowPartItem*)GetTreeCtrl().GetItemData( hItem);
		if( part)
		{
			TShowPartContainer tspc;
			tspc.push_back( *part);
			//ChangePart( *part, lot);
			if( m_pDXTestView)
			{
				m_pDXTestView->ChangePart( tspc, true, true);
			}

			GetTreeCtrl().SetCheck( hItem);
			//HTREEITEM hOld = pNMTreeView->itemOld.hItem;
			//if( GetTreeCtrl().GetParentItem( hItem) == GetTreeCtrl().GetParentItem( hOld))
			//{
			//	GetTreeCtrl().SetCheck( hOld, 0);
			//}
		}
	}
	*pResult = 0;
}

// 是否是身体部件item
bool CShowPartView::IsPartItem( HTREEITEM hItem)
{
	int level = 0;
	while( hItem)
	{
		hItem = GetTreeCtrl().GetParentItem( hItem);
		level++;
	}
	return level == 3;
}

void CShowPartView::ChangePart( const ShowPartItem& part, CLayerOperatorType& lot)
{
	lot.insert( std::make_pair( part.ulLayer, (LPARAM)part.strSvg.c_str()));

	if( part.ulLinkNum > 0)
	{
		TShowPartContainer& parts = m_loader.GetShowPartContainer();

		int count = min( 4, part.ulLinkNum);
		for( int i=0; i<count; i++)
		{
			int index = m_loader.GetIndexFromID( part.ulLinkID[i]);
			if( index >=0 && index < (int)parts.size())
			{
				ChangePart( parts[index], lot);
			}
		}
	}
}

void CShowPartView::ShowPart( const ShowPartItem& part, bool bShow, CLayerOperatorType& lot)
{
	lot.insert( std::make_pair( part.ulLayer, (LPARAM)bShow));
	if( part.ulLinkNum > 0)
	{
		TShowPartContainer& parts = m_loader.GetShowPartContainer();

		int count = min( 4, part.ulLinkNum);
		for( int i=0; i<count; i++)
		{
			int index = m_loader.GetIndexFromID( part.ulLinkID[i]);
			if( index >=0 && index < (int)parts.size())
			{
				ShowPart( parts[index], bShow, lot);
			}
		}
	}
}

void CShowPartView::OnNMClick(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: Add your control notification handler code here
	DWORD dwPos = GetMessagePos();

	POINT point;
	point.x = LOWORD( dwPos );
	point.y = HIWORD( dwPos );

	UINT flag;

	GetTreeCtrl().ScreenToClient( &point);
	HTREEITEM hItem = GetTreeCtrl().HitTest(point, &flag); 

	if( flag & TVHT_ONITEMSTATEICON	)	//是点击CHECK框
	{
		if( IsPartItem( hItem))
		{
			ShowPartItem* part = (ShowPartItem*)GetTreeCtrl().GetItemData( hItem);
			if( part)
			{
				bool bShow = !GetTreeCtrl().GetCheck( hItem);
				TShowPartContainer tspc;
				tspc.push_back( *part);
				//ShowPart( *part, bShow, lot);
				if( m_pDXTestView)
				{
					m_pDXTestView->ShowPart( tspc, bShow);
				}
			}
		}
	}
	*pResult = 0;
}

void CShowPartView::OnInitialUpdate()
{
	CTreeView::OnInitialUpdate();

	// TODO: Add your specialized code here and/or call the base class
}
