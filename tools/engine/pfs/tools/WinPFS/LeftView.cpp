// LeftView.cpp : CLeftView 类的实现
//

#include "stdafx.h"
#include "WinPFS.h"

#include "WinPFSDoc.h"
#include "LeftView.h"
#include "SystemFileIconList.h"
#include "WinPFSView.h"
#include "FileCentralCache.h"
#include "OperatorThread.h"
#include "../packzip/BrowsePath.h"
#include "WriteConfirmDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

// CLeftView

IMPLEMENT_DYNCREATE(CLeftView, CTreeView)

BEGIN_MESSAGE_MAP(CLeftView, CTreeView)
	ON_WM_CREATE()	
	ON_COMMAND(ID_MENU_INFLATE, &CLeftView::OnMenuInflate)
	ON_NOTIFY_REFLECT(TVN_BEGINDRAG, &CLeftView::OnTvnBegindrag)
	ON_NOTIFY_REFLECT(TVN_ITEMEXPANDING, &CLeftView::OnTvnItemexpanding)
	ON_NOTIFY_REFLECT(TVN_SELCHANGED, &CLeftView::OnTvnSelchanged)	
	ON_NOTIFY_REFLECT(NM_RCLICK, &CLeftView::OnNMRclick)
	ON_WM_CONTEXTMENU()
END_MESSAGE_MAP()

// CLeftView 构造/析构

CLeftView::CLeftView()
: m_pPFSView( NULL)
{
}

CLeftView::~CLeftView()
{
}

BOOL CLeftView::PreCreateWindow(CREATESTRUCT& cs)
{
	return CTreeView::PreCreateWindow(cs);
}

void CLeftView::OnInitialUpdate()
{
	CTreeView::OnInitialUpdate();

	GetTreeCtrl().DeleteAllItems();
	CWinPFSDoc* doc = GetDocument();
	if( doc)
	{
		CString str = doc->GetPathName();
		if( !str.IsEmpty())
		{
			HTREEITEM root = InsertOneItem( str.GetString(), TVI_ROOT);
			//int image = SFIL::CSystemFileIconList::GetInstance().GetIndex( str.GetString());
			//HTREEITEM root = GetTreeCtrl().InsertItem( str.GetString(), image, image, TVI_ROOT);
			//InsertFolder( L"", root);
			GetTreeCtrl().Expand( root, TVE_EXPAND);
			GetTreeCtrl().SelectItem( root);

			//if( m_pPFSView)
			//{
			//	m_pPFSView->FillCache( L"");
			//}
		}
	}
	
}

// CLeftView 消息处理程序

int CLeftView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CTreeView::OnCreate(lpCreateStruct) == -1)
		return -1;

	GetTreeCtrl().ModifyStyle( 0, TVS_HASBUTTONS|TVS_HASLINES|TVS_LINESATROOT|TVS_SHOWSELALWAYS);
	SFIL::CSystemFileIconList& sfil = SFIL::CSystemFileIconList::GetInstance();
	TreeView_SetImageList( GetTreeCtrl().GetSafeHwnd(), sfil.GetSmallIconList(), TVSIL_NORMAL);

	m_dlgProgress.Create( IDD_PROGRESS, this);

	return 0;
}

void CLeftView::InsertFolder( LPCTSTR parent, HTREEITEM hParent)
{
	CWinPFSDoc* doc = GetDocument();
	if( doc)
	{
		std::set<std::wstring> childs;
		doc->GetFolderList( parent, childs);
		for( std::set<std::wstring>::iterator i=childs.begin(), e=childs.end(); i!=e; ++i)
		{
			InsertOneItem( (*i).c_str(), hParent);
		}
	}
}

HTREEITEM CLeftView::InsertOneItem( LPCTSTR item, HTREEITEM hParent)
{
	TV_INSERTSTRUCT tvins; // TreeView Insert Struct.
	tvins.item.mask = TVIF_TEXT | TVIF_IMAGE | TVIF_SELECTEDIMAGE;

	tvins.item.pszText = CWinPFSDoc::GetLastFileName( item);
	tvins.item.cchTextMax = MAX_PATH;

	CWinPFSDoc* doc = GetDocument();
	if( doc)
	{
		bool bHasChildren = false;
		if( hParent == TVI_ROOT)
		{
			bHasChildren = doc->HasChildren( L"" );
			tvins.item.iImage = SFIL::CSystemFileIconList::GetInstance().GetIndex( item);
		}
		else
		{
			bHasChildren = doc->HasChildren( item );
			tvins.item.iImage = SFIL::CSystemFileIconList::GetInstance().GetFolderIndex( item);
		}

		if( bHasChildren )
		{
			tvins.item.cChildren = 1;
			tvins.item.mask |= TVIF_CHILDREN;
		}
	}
	
	tvins.item.iSelectedImage = tvins.item.iImage;
	tvins.hInsertAfter = NULL;
	tvins.hParent = hParent;

	return GetTreeCtrl().InsertItem(&tvins);
}


void CLeftView::OnTvnItemexpanding(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	
	if ((pNMTreeView->itemNew.state & TVIS_EXPANDEDONCE))
		return;

	HTREEITEM hItem = pNMTreeView->itemNew.hItem;
	if( hItem)
	{
		InsertFolder( GetPathNameFromTreeItem( hItem), hItem);
	}

	*pResult = 0;
}

void CLeftView::OnTvnSelchanged(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);

	HTREEITEM hItem = pNMTreeView->itemNew.hItem;
	if( hItem && m_pPFSView)
	{
		CString strItemText = this->GetTreeCtrl().GetItemText( hItem );
		((CFrameWnd*)AfxGetMainWnd())->SetMessageText(L"当前结点："+ strItemText );
		
		m_pPFSView->FillCache( GetPathNameFromTreeItem( hItem));
	}

	*pResult = 0;
}

LPCTSTR CLeftView::GetPathNameFromTreeItem( HTREEITEM item)
{
	assert( item);

	static wchar_t s_buffer[MAX_PATH] = {0};
	s_buffer[0] = 0;

	wchar_t temp[MAX_PATH] = {0};
	CTreeCtrl& tc = GetTreeCtrl();
	while( item != tc.GetRootItem())
	{
		CString str = tc.GetItemText( item);
		_stprintf_s( s_buffer, L"%s/%s", str.GetString(), temp);
		_tcscpy_s( temp, s_buffer);

		item = tc.GetParentItem( item);
	}

	return s_buffer;
}

void CLeftView::SelectChildItem( LPCTSTR childName)
{
	HTREEITEM parent = GetTreeCtrl().GetSelectedItem();
	if( parent)
	{
		GetTreeCtrl().Expand( parent, TVE_EXPAND);
		parent = GetTreeCtrl().GetChildItem( parent);
		while( parent)
		{
			CString str = GetTreeCtrl().GetItemText( parent);
			if( str.Compare( childName) == 0)
			{
				GetTreeCtrl().SelectItem( parent);
				break;	
			}
			parent = GetTreeCtrl().GetNextSiblingItem( parent);
		}
	}
}

void CLeftView::OnNMRclick(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;

	HTREEITEM hItem = this->GetTreeCtrl().GetDropHilightItem();
	if( hItem == NULL )
		hItem = this->GetTreeCtrl().GetSelectedItem();
	else
	{	// Select it
		this->GetTreeCtrl().SetItemState( hItem, TVIS_SELECTED, TVIS_SELECTED);
	}

	if( hItem == NULL )
	{
		return;
	}

	CMenu popupMenu;
	if( !popupMenu.LoadMenu( IDR_MENU1 ) )
	{
		return;
	}

	// submenu 0 for listview
	// submenu 1 for treeview
	CMenu *pSubMenu = popupMenu.GetSubMenu( 1 );
	if ( pSubMenu != NULL )
	{
		CPoint point;
		GetCursorPos(&point);

		pSubMenu->TrackPopupMenu( 0, point.x, point.y, this );

		*pResult = 1;
	}
}

void CLeftView::OnContextMenu(CWnd* /*pWnd*/, CPoint /*point*/)
{
	HTREEITEM hItem = this->GetTreeCtrl().GetSelectedItem();
	if( hItem == NULL )
	{
		return;
	}
	
	CRect rcItem;
	if( !GetTreeCtrl().GetItemRect(hItem, &rcItem, TRUE ) )
	{
		return;
	}

	CMenu popupMenu;
	if( !popupMenu.LoadMenu( IDR_MENU1 ) )
	{
		return;
	}

	// submenu 0 for listview
	// submenu 1 for treeview
	CMenu *pSubMenu = popupMenu.GetSubMenu( 1 );
	if ( pSubMenu != NULL )
	{
		CPoint point(rcItem.left, rcItem.bottom);
		ClientToScreen(&point);

		CString strItemText = this->GetTreeCtrl().GetItemText(hItem);
		((CFrameWnd*)AfxGetMainWnd())->SetMessageText(L"当前结点："+ strItemText );

		pSubMenu->TrackPopupMenu( 0, point.x, point.y, this );
	}
}

void CLeftView::OnMenuInflate()
{
	HTREEITEM hItem = this->GetTreeCtrl().GetSelectedItem();
	if( hItem == NULL )
	{
		return;
	}
	
	CBrowsePath browser( GetSafeHwnd());
	browser.SetIniPath( GetDocument()->m_lastSelPath);
	if( !browser.DoModal())
	{
		return;
	}
	COperatorList::GetInstance().Clear();
	GetDocument()->m_lastSelPath = browser.GetSelectPath();

	COperatorList::Item item;

	item.m_des = GetDocument()->m_lastSelPath;
	if( item.m_des.at(item.m_des.size()-1) != L'\\' )
		item.m_des += L"\\";

	CString str;
	HTREEITEM hParent = this->GetTreeCtrl().GetParentItem( hItem ) ;
	if( hParent != NULL )
	{
		str = this->GetTreeCtrl().GetItemText(hItem);
		
		item.m_src = GetPathNameFromTreeItem(hItem);
		item.m_des += str.GetString();
		item.m_des += L"\\";

		if( PFS::CFileIO::IsDirectoryExisting( item.m_des ) )
		{
			CString prompt;
			prompt.Format( L"%s 目录已存在，要覆盖么？", item.m_des.c_str());		
			int state = WriteConfirmBox( prompt);
			if( IDCANCEL == state || IDCANCEL2 == state)
			{
				// 如果取消了
				return;
			}
		}
	}
	else
	{
		// Empty means root.
		// item.m_src = L"";
	}

	item.m_isPath = true;	
	item.m_type = COperatorList::INFLATEEND;

	if( GetDocument()->m_thread != NULL )
		GetDocument()->m_thread->SetWnd( &m_dlgProgress);

	COperatorList::GetInstance().Push( item);

	m_dlgProgress.ShowWindow( SW_SHOW);
}

void CLeftView::OnTvnBegindrag(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMLV = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	
	// 拖放解压暂时实现为这样，如果文件较大时会阻塞
	// 还未研究出WinRar拖放时异步解压是怎么实现的

	CFileCentralCache fcc;
	
	std::wstring str;
	str = GetDocument()->m_parentPath;
	
	const ZIPFILE::CFileCentral* fc = GetDocument()->m_filereader.GetFileCentral(str);
	if( fc )
	{
		fcc.push_back( (DWORD_PTR)fc);
	}
	else 
	{
		if( str.empty() && this->GetTreeCtrl().GetParentItem( pNMLV->itemOld.hItem ) != NULL )
		{
			// 特殊处理根结点。
			*pResult = 0;
			return;
		}
	}

	GetDocument()->AddDropFilesInPath( str, fcc);
	
	GetDocument()->DoDragDrop( fcc );

	*pResult = 1;
}
