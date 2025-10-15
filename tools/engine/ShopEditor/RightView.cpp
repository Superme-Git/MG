/// RightView.cpp : implementation file
///
/// yanghaibo 2010-12-15
///
/// yanghaibo 2010-12-16 ʵ��ѡ�ж������������ƶ��߼���
///
/// TODO: �����ظ��Լ����ơ�
/// TODO: ����Copy/Cut/Paste���ơ�

#include "stdafx.h"
#include "ShopEditor.h"
#include "RightView.h"
#include <sstream>
#include "SelectCategoryDlg.h"
#include "ShopEditorDoc.h"

#define _Trace(x)

// CRightView

IMPLEMENT_DYNCREATE(CRightView, CFormView)

CRightView::CRightView()
	: CFormView(CRightView::IDD)
	, m_first( true)
	, m_curBcp( NULL)
	, m_curScp( NULL)
{
}

CRightView::~CRightView()
{
}

void CRightView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	DDX_Control( pDX, IDC_LIST1, m_list);
}

BEGIN_MESSAGE_MAP(CRightView, CFormView)
	ON_WM_CREATE()
	ON_WM_SIZE()

	ON_MESSAGE(WM_SET_ITEMS, PopulateComboList)
	ON_MESSAGE(WM_VALIDATE, OnEndLabelEditVariableCriteria)

	ON_NOTIFY(NM_RCLICK, IDC_LIST1, &CRightView::OnNMRclickList1)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_LIST1, &OnLvnItemchangedList1)

	ON_COMMAND( ID_SHOPITEM_ADD, OnAddShopItem)
	ON_COMMAND( ID_SHOPITEM_DELETE, OnDeleteShopItem)

	ON_COMMAND( ID_ITEM_ADD, OnAddShopItem)
	ON_COMMAND( ID_ITEM_DELETE, OnDeleteShopItem)
	ON_COMMAND( ID_ITEM_MOVEUP, OnMoveUpShopItem)
	ON_COMMAND( ID_ITEM_MOVEDOWN, OnMoveDownShopItem)
	ON_COMMAND( ID_ITEM_MOVETO, OnMoveToShopItem)

	ON_COMMAND( ID_SHOPITEM_MOVEUP, OnMoveUpShopItem)
	ON_COMMAND( ID_SHOPITEM_MOVEDOWN, OnMoveDownShopItem)
	ON_COMMAND( ID_SHOPITEM_MOVETO, OnMoveToShopItem)
	ON_COMMAND( ID_SHOPITEM_COPYTO, OnCopyToShopItem)

	ON_COMMAND( ID_EDIT_CUT, OnCutShopItems)
	ON_COMMAND( ID_EDIT_PASTE, OnPasteShopItems)
	ON_COMMAND( ID_EDIT_COPY, OnCopyShopItems)
	
	ON_UPDATE_COMMAND_UI( ID_SHOPITEM_DELETE, OnUpdateNeedItemSelected)
	ON_UPDATE_COMMAND_UI( ID_EDIT_CUT, OnUpdateNeedItemSelected)
	ON_UPDATE_COMMAND_UI( ID_EDIT_PASTE, OnUpdateNeedItemSelected)
	ON_UPDATE_COMMAND_UI( ID_EDIT_COPY, OnUpdateNeedItemSelected)

	ON_UPDATE_COMMAND_UI(ID_ITEM_MOVEUP, OnUpdateItemMoveup)
	ON_UPDATE_COMMAND_UI(ID_ITEM_MOVEDOWN, OnUpdateItemMovedown)

	ON_UPDATE_COMMAND_UI(ID_SHOPITEM_MOVEUP, OnUpdateItemMoveup)
	ON_UPDATE_COMMAND_UI(ID_SHOPITEM_MOVEDOWN, OnUpdateItemMovedown)

	ON_UPDATE_COMMAND_UI(ID_ITEM_RENAME, &CRightView::OnUpdateItemRename)
	ON_COMMAND(ID_ITEM_RENAME, &CRightView::OnItemRename)
END_MESSAGE_MAP()

//#define ID_COLUMN_FIRST					32790
//#define ID_COLUMN_LAST					32791
//#define ID_COLUMN_NEXT					32792
//#define ID_COLUMN_PREV					32793


// CRightView message handlers
enum
{
	COL_NAME = 0,
	COL_ID,
	COL_COUNT,
	COL_PRICE,
	COL_OVERTIME,
	COL_RETURNSCALE,
	COL_LIMITCOUNT,
	COL_DISCOUNT,
	COL_PACKPATH,
	COL_ICONINDEX,
	COL_DESCRIPT,
	COL_NEW,
	COL_COMMEND,
	COL_PROMOTION,
	COL_SHOPID,
};

LPCTSTR g_flag[]={
	_T("��"),
	_T("��"),
};

// CRightView message handlers

int CRightView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CFormView::OnCreate(lpCreateStruct) == -1)
		return -1;

	return 0;
}

// ����List��flag�ı�
void CRightView::SetListFlagText( int index, int col, int flag, int flagType)
{
	if( flag & flagType)
	{
		m_list.SetItemText( index, col, g_flag[0]);
	}
	else
	{
		m_list.SetItemText( index, col, g_flag[1]);
	}
}

int CRightView::AddShopItemToList( const CShopItem& si, int index)
{
	if( index == -1)
	{
		index = m_list.GetItemCount();
	}
	index = m_list.InsertItem( index, si.m_name.c_str());
	std::wostringstream wos;
	// id
	wos << si.m_ID;
	m_list.SetItemText( index, COL_ID, wos.str().c_str());
	// ����
	wos.str( L"");
	wos << si.m_count;
	m_list.SetItemText( index, COL_COUNT, wos.str().c_str());
	// �۸�
	wos.str( L"");
	wos << si.m_price;
	m_list.SetItemText( index, COL_PRICE, wos.str().c_str());
	// ʱ������
	wos.str( L"");
	wos << si.m_overTime;
	m_list.SetItemText( index, COL_OVERTIME, wos.str().c_str());
	// �������
	wos.str( L"");
	wos << si.m_returnScale;
	m_list.SetItemText( index, COL_RETURNSCALE, wos.str().c_str());
	// ���Ƹ���
	wos.str( L"");
	wos << si.m_limitCount;
	m_list.SetItemText( index, COL_LIMITCOUNT, wos.str().c_str());
	// �ۿ�
	wos.str( L"");
	wos << si.m_discount;
	m_list.SetItemText( index, COL_DISCOUNT, wos.str().c_str());
	// ͼ���
	m_list.SetItemText( index, COL_PACKPATH, si.m_packPath.c_str());
	// ͼ���
	wos.str( L"");
	wos << si.m_iconIndex;
	m_list.SetItemText( index, COL_ICONINDEX, wos.str().c_str());
	// ����
	m_list.SetItemText( index, COL_DESCRIPT, si.m_descript.c_str());
	// �̳�id
	wos.str( L"");
	wos << si.m_shopID;
	m_list.SetItemText( index, COL_SHOPID, wos.str().c_str());
	// ��Ʒ
	SetListFlagText( index, COL_NEW, si.m_flag, FLAG_NEW );
	// �Ƽ�
	SetListFlagText( index, COL_COMMEND, si.m_flag, FLAG_COMMEND );
	// ����
	SetListFlagText( index, COL_PROMOTION, si.m_flag, FLAG_PROMOTION );
	// �Ƿ��ϼ�
	m_list.SetCheck( index, si.m_isSaling);

	m_list.SetItemData( index, (DWORD)&si);

	return index;
}

bool CRightView::AddAllShopItemsToList( CSmallCategory* scp, bool clear)
{
	if(clear)
		m_list.DeleteAllItems();

	if( scp)
	{
		for( CShopItemList::const_iterator i=scp->m_items.begin(), e=scp->m_items.end(); i != e; ++i)
		{
			AddShopItemToList( *i, -1);
		}
	}
	m_curScp = scp;
	return true;
}

void CRightView::OnSize(UINT nType, int cx, int cy)
{
	CFormView::OnSize(nType, cx, cy);

	if( ::IsWindow( m_list.GetSafeHwnd()))
	{
		CRect rc;
		GetClientRect( &rc);
		m_list.MoveWindow( rc);
	}
}

void CRightView::OnInitialUpdate()
{
	CFormView::OnInitialUpdate();
	
	if( m_first)
	{
		m_first = false;

		m_list.SetExtendedStyle( LVS_EX_CHECKBOXES|LVS_EX_FULLROWSELECT );
		m_list.InsertColumn( COL_NAME,			L"����", 0, 100);
		m_list.InsertColumn( COL_ID,			L"id", 0, 50);
		m_list.InsertColumn( COL_COUNT,			L"����", 0, 40);
		m_list.InsertColumn( COL_PRICE,			L"�۸�", 0, 40);
		m_list.InsertColumn( COL_OVERTIME,		L"ʱ������", 0, 70);
		m_list.InsertColumn( COL_RETURNSCALE,	L"�������", 0, 70);
		m_list.InsertColumn( COL_LIMITCOUNT,	L"���Ƹ���", 0, 70);
		m_list.InsertColumn( COL_DISCOUNT,		L"�ۿ�", 0, 50);
		m_list.InsertColumn( COL_PACKPATH,		L"ͼ���", 0, 100);
		m_list.InsertColumn( COL_ICONINDEX,		L"ͼ���", 0, 60);
		m_list.InsertColumn( COL_DESCRIPT,		L"����", 0, 100);
		m_list.InsertColumn( COL_NEW,			L"��Ʒ", 0, 50);
		m_list.InsertColumn( COL_COMMEND,		L"�Ƽ�", 0, 50);
		m_list.InsertColumn( COL_PROMOTION,		L"����", 0, 50);
		m_list.InsertColumn( COL_SHOPID,		L"�̳�id", 0, 60);

		m_list.SetComboColumns( COL_NEW);
		m_list.SetComboColumns( COL_COMMEND);
		m_list.SetComboColumns( COL_PROMOTION);
		m_list.SetReadOnlyColumns( COL_SHOPID);
	}
	m_list.DeleteAllItems();
	m_curBcp = NULL;
}

void SetFlagFromText( unsigned long& flag, int col, const wchar_t* text)
{
	bool res = (wcscmp( text, g_flag[0]) == 0);
	int flagType = 1 << (col - COL_NEW);
	if( res)
	{
		flag |= flagType;
	}
	else
	{
		flag &= ~flagType;
	}
}

LRESULT CRightView::OnEndLabelEditVariableCriteria(WPARAM wParam, LPARAM lParam)
{
	LV_DISPINFO* pDispInfo = (LV_DISPINFO*)lParam;
	LPCTSTR pszText = pDispInfo->item.pszText;
	if( NULL == pszText)
	{
		return -1;
	}
	int index = pDispInfo->item.iItem;
	CShopItem* sip = (CShopItem*)m_list.GetItemData( index);
	if( NULL == sip)
	{
		return -1;
	}
	std::wistringstream iss;
	iss.str( pszText);
	switch( pDispInfo->item.iSubItem)
	{
	case COL_NAME:
		sip->m_name = pszText;
		_Trace(pszText);
		_Trace(L"\n");
		break;
	case COL_ID:
		iss >> sip->m_ID;
		break;
	case COL_COUNT:
		iss >> sip->m_count;
		break;
	case COL_PRICE:
		iss >> sip->m_price;
		break;
	case COL_OVERTIME:
		iss >> sip->m_overTime;
		break;
	case COL_RETURNSCALE:
		iss >> sip->m_returnScale;
		break;
	case COL_LIMITCOUNT:
		iss >> sip->m_limitCount;
		break;
	case COL_DISCOUNT:
		iss >> sip->m_discount;
		break;
	case COL_PACKPATH:
		sip->m_packPath = pszText;
		break;
	case COL_ICONINDEX:
		iss >> sip->m_iconIndex;
		break;
	case COL_DESCRIPT:
		sip->m_descript = pszText;
		break;
	case COL_NEW:
	case COL_COMMEND:
	case COL_PROMOTION:
		SetFlagFromText( sip->m_flag, pDispInfo->item.iSubItem, pszText);
		break;
	}
	SetModifyFlag();
	return 0;
}

void CRightView::OnNMRclickList1(NMHDR *pNMHDR, LRESULT *pResult)
{
	CMenu popupMenu;
	popupMenu.LoadMenu( IDR_MENU_SHOPITEM );
	CMenu* pSubMenu = popupMenu.GetSubMenu( 0);
	if( NULL == pSubMenu)
	{
		return;
	}
	CPoint point;
	::GetCursorPos( &point);
	CPoint pt( point);
	m_list.ScreenToClient( &pt);
	int iItem = m_list.HitTest( pt);
	if( iItem >= 0 && m_curScp )
	{
		m_list.SetItemState( iItem, LVIS_SELECTED, LVIS_SELECTED);
	}
	else
	{
		pSubMenu->EnableMenuItem( ID_SHOPITEM_DELETE, MF_BYCOMMAND|MF_GRAYED);
		pSubMenu->EnableMenuItem( ID_SHOPITEM_MOVEUP, MF_BYCOMMAND|MF_GRAYED);
		pSubMenu->EnableMenuItem( ID_SHOPITEM_MOVEDOWN, MF_BYCOMMAND|MF_GRAYED);
		pSubMenu->EnableMenuItem( ID_SHOPITEM_MOVETO, MF_BYCOMMAND|MF_GRAYED);
		pSubMenu->EnableMenuItem( ID_SHOPITEM_COPYTO, MF_BYCOMMAND|MF_GRAYED);
	}
	if( !m_curScp )
		pSubMenu->EnableMenuItem( ID_SHOPITEM_ADD, MF_BYCOMMAND|MF_DISABLED|MF_GRAYED);
	pSubMenu->TrackPopupMenu( 0, point.x, point.y, this );
	*pResult = 0;
}

void CRightView::OnAddShopItem()
{
	if( m_curScp )
	{
		CShopItem* sip = m_curScp->AddItem( L"�½���Ʒ");
		if( sip )
		{
			sip->m_shopID = CShopItem::m_maxShopID;
			CShopItem::m_maxShopID++;

			int iItem = AddShopItemToList( *sip, -1);
			m_list.SetItemState( -1, 0, LVIS_SELECTED);
			m_list.SetItemState( iItem, LVIS_SELECTED | LVIS_FOCUSED, LVIS_SELECTED| LVIS_FOCUSED);
			m_list.EnsureVisible( iItem, FALSE);

			SetModifyFlag();
		}
	}
	else
	{
		AfxMessageBox( L"���������ѡ����Ʒ������С�࣡");
	}
}

void CRightView::OnDeleteShopItem()
{
	if( m_curScp )
	{
		UINT uiSelected = m_list.GetSelectedCount();
		if(  uiSelected == 0 )
		{
			return;
		}

		CString str;
		str.Format( L"ȷ��ɾ��ѡ�е�%u����Ʒ��", uiSelected);
		if( IDYES == AfxMessageBox( str, MB_YESNO|MB_DEFBUTTON2))
		{
			// ��Ϊ���Ǳ��뵹��ɾ�������ԣ�������ʹ��GetNextSelectedItemPostion()�ӿڡ�
			for (int i=m_list.GetItemCount(); i>=0; i--)
			{
				if( m_list.GetItemState( i, LVIS_SELECTED) == LVIS_SELECTED)
				{
					CShopItem* item = (CShopItem*)m_list.GetItemData( i);
					if( m_curScp->DeleteItem( item ))
					{
						m_list.DeleteItem( i);
						
					}
				}
			}

			SetModifyFlag();
		}
	}
}

void CRightView::OnMoveUpShopItem()
{
	if( !m_curScp )
	{
		return;
	}

	UINT uiSelected = m_list.GetSelectedCount();
	if(  uiSelected == 0 )
	{
		return;
	}

	if( uiSelected == 1 )
	{
		int iFirst = m_list.GetNextItem( -1, LVNI_SELECTED );
		int iAbove = m_list.GetNextItem( iFirst, LVNI_ABOVE );
		if( iAbove == -1 )
		{
			return;
		}

		int iItem = iFirst;
		CShopItem* item = (CShopItem*)m_list.GetItemData( iItem );
		CShopItem* sip = m_curScp->MoveUp( item );
		if( sip )
		{
			m_list.DeleteItem( iItem);
			int index = AddShopItemToList( *sip, iAbove);
			m_list.SetItemState( index, LVIS_SELECTED|LVIS_FOCUSED, LVIS_SELECTED|LVIS_FOCUSED);
			SetModifyFlag();
		}
	}
	else
	{	
		int nCount = m_list.GetItemCount();
		for( int g = -1; ; ) // ��ѭ�����ڴ����û�ѡ�ж��������
		{
			int iFirst = m_list.GetNextItem( g, LVNI_SELECTED );
			if( iFirst == -1 )
			{
				return;
			}

			int iAbove = m_list.GetNextItem( iFirst, LVNI_ABOVE );
			if( iAbove == -1 )
			{
				return;
			}

			int iLast = iFirst;	
			for( int i = iFirst+1; i < nCount; i ++ )
			{
				if( m_list.GetItemState( i, LVIS_SELECTED) != LVIS_SELECTED )
				{
					// It's not selected.
					g = i;
					break;
				}

				iLast = i;
			}

			// ˵��������ͨ����λ�ڵ�һ��֮ǰ���б����Ƶ��б�����RangeMoveUp�Ĳ�����
			CShopItem* itemBeforeFirst = (CShopItem*)m_list.GetItemData( iAbove );
			CShopItem* itemLast = (CShopItem*)m_list.GetItemData( iLast );
			CShopItem* itemNew = m_curScp->MoveRangeUp( itemBeforeFirst, itemLast );
			if( NULL != itemNew )
			{
				iLast ++;
				if( iLast > nCount )
				{
					iLast = nCount;
				}

				// �����µ�λ�ò��룬�п�����β��
				g = AddShopItemToList( *itemNew, iLast );

				// �ٴ�ԭ����λ��ɾ��
				m_list.DeleteItem( iAbove );

				SetModifyFlag();
			}
			else
			{
				AfxMessageBox( L"�ƶ�ѡ����Ŀʧ�ܣ�" );
				return;
			}
		}
	}
}

void CRightView::OnMoveDownShopItem()
{
	if( !m_curBcp)
	{
		return;
	}
	
	UINT uiSelected = m_list.GetSelectedCount();
	if(  uiSelected == 0 )
	{
		return;
	}

	if( uiSelected == 1 )
	{
		int iFirst = m_list.GetNextItem( -1, LVNI_SELECTED );
		if( iFirst == -1 )
		{
			return;
		}

		int iBelow = m_list.GetNextItem( iFirst, LVNI_BELOW );
		if( iBelow == -1 )
		{
			return;
		}

		CShopItem* item = (CShopItem*)m_list.GetItemData( iFirst );
		CShopItem* sip = m_curScp->MoveDown( item);
		if( sip )
		{
			// �ȴ�ԭ����λ��ɾ��
			m_list.DeleteItem( iFirst );

			// Ȼ�����µ�λ�ò���
			int index = AddShopItemToList( *sip, iBelow);

			m_list.SetItemState( index, LVIS_SELECTED|LVIS_FOCUSED, LVIS_SELECTED|LVIS_FOCUSED);

			SetModifyFlag();
		}
	}
	else
	{
		int nCount = m_list.GetItemCount();
		for( int g = -1; ; )
		{
			int iFirst = m_list.GetNextItem( g, LVNI_SELECTED );
			if( iFirst == -1 )
			{
				return;
			}

			int iAfterLast = -1;
			for( int i = iFirst+1; i <= nCount; i ++ )
			{
				if( m_list.GetItemState( i, LVIS_SELECTED) != LVIS_SELECTED )
				{
					// It's not selected.
					iAfterLast = i;
					break;
				}
			}

			if( iAfterLast == -1 )
			{
				AfxMessageBox( L"�ƶ�ѡ����Ŀʧ�ܣ�" );
				return;
			}

			g = iAfterLast;

			// ˵��������ͨ����λ�����һ��ѡ�е��б���֮�����Ŀ�Ƶ�ѡ����Ŀ֮ǰ�����RangeMoveDown�Ĳ�����
			CShopItem* itemFirst = (CShopItem*)m_list.GetItemData( iFirst );
			CShopItem* itemAfterLast = (CShopItem*)m_list.GetItemData( iAfterLast );
			CShopItem* itemNew = m_curScp->MoveRangeDown( itemFirst, itemAfterLast );
			if( NULL != itemNew )
			{
				// �ȴ�ԭ����λ��ɾ��
				m_list.DeleteItem( iAfterLast );

				// Ȼ�����µ�λ�ò���
				AddShopItemToList( *itemNew, iFirst );

				SetModifyFlag();
			}
			else
			{
				AfxMessageBox( L"�ƶ�ѡ����Ŀʧ�ܣ�" );
			}
		}
	}
}

void CRightView::OnLvnItemchangedList1(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	
	int item = pNMLV->iItem;
	CShopItem* sip = (CShopItem*)m_list.GetItemData( item);
	if( sip)
	{
		if((pNMLV->uOldState & INDEXTOSTATEIMAGEMASK(1))		/* old state : unchecked */ 
			&& (pNMLV->uNewState & INDEXTOSTATEIMAGEMASK(2)))	/* new state : checked */ 
		{ 
			sip->m_isSaling = true;
			SetModifyFlag();
		} 
		else if((pNMLV->uOldState & INDEXTOSTATEIMAGEMASK(2))	/* old state : checked */ 
			&& (pNMLV->uNewState & INDEXTOSTATEIMAGEMASK(1))) 	/* new state : unchecked */ 
		{ 
			sip->m_isSaling = false;
			SetModifyFlag();
		} 
	}
	*pResult = 0;
}

void CRightView::OnMoveToShopItem()
{
	if( NULL == m_curScp)
	{
		return;
	}

	UINT uiSelectedCount = m_list.GetSelectedCount();
	if( uiSelectedCount > 0 )
	{
		CSelectCategoryDlg dlg(this->m_curScp);
		dlg.m_manager = &((CShopEditorDoc*)GetDocument())->m_manager;
		dlg.m_selBcp = m_curBcp;
		if( IDOK == dlg.DoModal())
		{
			if( dlg.m_selScp )
			{
				bool modified = false;

				int *rgIndexArray = new int[uiSelectedCount];
				int c = 0;
				POSITION pos = m_list.GetFirstSelectedItemPosition();
				while( pos )
				{
					int iItem = m_list.GetNextSelectedItem(pos);

					CShopItem* sip = (CShopItem*)m_list.GetItemData( iItem);
					if( sip )
					{
						CShopItem temp = *sip;

						// TODO:����Ӧ���ȼ���ظ���Ŀ��Ȼ����ִ��ɾ���ͼ��������
						// ��һ������У���Ӧ�ó����ظ���Ŀ��
						bool isDuplicated = false;
						for( CShopItemList::const_iterator itor = dlg.m_selScp->m_items.begin(), end = dlg.m_selScp->m_items.end();
							itor != end; 
							++itor)
						{
							if( itor->m_ID == sip->m_ID && !itor->m_name.compare(sip->m_name) )
							{
								CString str;
								str.Format( L"\"%s\"��Ŀ��Ŀ¼���Ѵ��ڣ�", sip->m_name.c_str());
								AfxMessageBox(str);
								isDuplicated = true;
							}
						}
						if( !isDuplicated )
						{	
							if( m_curScp->DeleteItem( sip))
							{
								dlg.m_selScp->AddItem( temp );

								rgIndexArray[c++] = iItem;
							}
						}
					}
				}

				if( c > 0 )
				{
					for( int i = c-1; i >=0; i -- )
					{
						m_list.DeleteItem( rgIndexArray[i] );
					}

					SetModifyFlag();
				}

				delete[] rgIndexArray;
			}
		}
	}
}

void CRightView::OnCopyToShopItem()
{
	if( NULL == m_curScp)
	{
		return;
	}

	UINT uiSelectedCount = m_list.GetSelectedCount();
	if( uiSelectedCount > 0 )
	{
		CSelectCategoryDlg dlg(this->m_curScp);
		dlg.m_manager = &((CShopEditorDoc*)GetDocument())->m_manager;
		dlg.m_selBcp = m_curBcp;
		if( IDOK == dlg.DoModal())
		{
			if( dlg.m_selScp )
			{
				bool modified = false;

				POSITION pos = m_list.GetFirstSelectedItemPosition();
				while( pos )
				{
					int iItem = m_list.GetNextSelectedItem(pos);

					CShopItem* psi = (CShopItem*)m_list.GetItemData( iItem );
					if( psi )
					{
						// TODO: ����ظ���Ŀ��
						// ��һ������У���Ӧ�ó����ظ���Ŀ��
						bool isDuplicated = false;
						for(CShopItemList::const_iterator itor=dlg.m_selScp->m_items.begin(), end = dlg.m_selScp->m_items.end();
							itor != end;
							++itor)
						{
							if( itor->m_ID == psi->m_ID && !itor->m_name.compare(psi->m_name) )
							{
								CString str;
								str.Format( L"\"%s\"��Ŀ��Ŀ¼���Ѵ��ڣ�", psi->m_name.c_str() );
								AfxMessageBox(str);
								isDuplicated = true;
							}
						}
						if( !isDuplicated )
						{
							dlg.m_selScp->AddItem( *psi);
							modified = true;
						}
					}
				}
				
				if( modified )
				{
					SetModifyFlag();
				}
			}
		}
	}
}

LRESULT CRightView::PopulateComboList(WPARAM wParam, LPARAM lParam)
{
	CStringList* pComboList = reinterpret_cast<CStringList*>(lParam);
	if (pComboList)
	{
		if( COL_PROMOTION == wParam || COL_NEW == wParam || COL_COMMEND == wParam)
		{
			pComboList->AddTail( g_flag[0]);
			pComboList->AddTail( g_flag[1]);
		}
	}
	return 0;
}

void CRightView::OnUpdateItemMoveup(CCmdUI *pCmdUI)
{
	int Count = m_list.GetItemCount();
	if( Count > 0 && m_list.GetItemState( 0, LVIS_SELECTED) != LVIS_SELECTED )
		pCmdUI->Enable( TRUE );
	else
		pCmdUI->Enable( FALSE );
}

void CRightView::OnUpdateItemMovedown(CCmdUI *pCmdUI)
{
	int Count = m_list.GetItemCount();
	if( Count > 0 && m_list.GetItemState( Count-1, LVIS_SELECTED) != LVIS_SELECTED )
		pCmdUI->Enable( TRUE );
	else
		pCmdUI->Enable( FALSE );
}

void CRightView::OnPasteShopItems()
{
	// TODO:
}

void CRightView::OnCopyShopItems()
{
	// TODO:
}

void CRightView::OnCutShopItems()
{
	// TODO:
}

void CRightView::ClearShopItemsList()
{
	if( m_list.GetSafeHwnd() )
		m_list.DeleteAllItems();
}

void CRightView::OnUpdateItemRename(CCmdUI *pCmdUI)
{
	int iRowIndex = m_list.GetIntrestedListItem();
	pCmdUI->Enable(iRowIndex != -1);
}

void CRightView::OnItemRename()
{
	int iRowIndex = m_list.GetIntrestedListItem();
	if( iRowIndex != -1)
	{
		// Edit the first column of the item.
		m_list.EditLabel(iRowIndex);
	}
}

void CRightView::OnUpdateNeedItemSelected(CCmdUI *pCmdUI)
{
	int iItem = m_list.GetNextItem( -1, LVNI_SELECTED );
	pCmdUI->Enable( iItem != -1 );
}

BOOL CRightView::OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo)
{
	if( m_list.OnCmdMsg( nID, nCode, pExtra, pHandlerInfo) )
	{
		return TRUE;
	}

	return CFormView::OnCmdMsg(nID, nCode, pExtra, pHandlerInfo);
}
