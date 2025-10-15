// ShopItemListCtrl.cpp : implementation file
//

#include "stdafx.h"
#include "ShopEditor.h"
#include "ShopItemListCtrl.h"

// CShopItemListCtrl

IMPLEMENT_DYNAMIC(CShopItemListCtrl, CComboListCtrl)

CShopItemListCtrl::CShopItemListCtrl()
{
	m_iCurrentRow = -1;
	m_iCurrentColumn = -1;
}

CShopItemListCtrl::~CShopItemListCtrl()
{
}

BEGIN_MESSAGE_MAP(CShopItemListCtrl, CComboListCtrl)
	ON_COMMAND( ID_COLUMN_FIRST, EditColumnFirst)
	ON_COMMAND( ID_COLUMN_LAST, EditColumnLast)
	ON_COMMAND( ID_COLUMN_NEXT, EditColumnNext)
	ON_COMMAND( ID_COLUMN_PREV, EditColumnPrev)
	ON_WM_LBUTTONDOWN()
	ON_NOTIFY_REFLECT(NM_DBLCLK, &CShopItemListCtrl::OnNMDblclk)
END_MESSAGE_MAP()

// CShopItemListCtrl message handlers

int CShopItemListCtrl::GetIntrestedListItem( )
{
	CListCtrl& list = *this;
	int iRowIndex = list.GetNextItem( -1, LVNI_FOCUSED|LVNI_SELECTED );
	if( iRowIndex == -1 )
	{
		int iIndex = list.GetTopIndex();
		iIndex --;
		iRowIndex = list.GetNextItem( iIndex, LVNI_SELECTED );
		if( iRowIndex == -1 )
		{
			iRowIndex = list.GetNextItem( -1, LVNI_SELECTED );
			if( iRowIndex == -1 )
				iRowIndex = list.GetNextItem( -1, LVNI_FOCUSED );
		}
	}

	return iRowIndex;
}

// Navigate to first column and edit it.
void CShopItemListCtrl::EditColumnFirst()
{
	if( GetColumnCounts() == 0 || GetItemCount() == 0 )
	{
		return;
	}

	m_iCurrentColumn = 0;
	m_iCurrentRow = GetIntrestedListItem();
	if( m_iCurrentRow == -1 )
	{
		m_iCurrentRow = 0;
	}

	SetItemState(m_iCurrentRow, LVIS_FOCUSED|LVIS_SELECTED, 
		LVIS_FOCUSED|LVIS_SELECTED);

	BeginEditLabel( m_iCurrentRow, m_iCurrentColumn );
}

// Navigate to last column and edit it.
void CShopItemListCtrl::EditColumnLast()
{
	if( GetColumnCounts() == 0 || GetItemCount() == 0 )
	{
		return;
	}

	m_iCurrentColumn = GetColumnCounts() -1;
	m_iCurrentRow = GetIntrestedListItem();
	if( m_iCurrentRow == -1 )
	{
		m_iCurrentRow = 0;
	}

	SetItemState(m_iCurrentRow, LVIS_FOCUSED|LVIS_SELECTED, 
		LVIS_FOCUSED|LVIS_SELECTED);

	BeginEditLabel( m_iCurrentRow, m_iCurrentColumn );
}

// Navigate to previous column and edit it.
void CShopItemListCtrl::EditColumnPrev()
{
	if( GetColumnCounts() == 0 || GetItemCount() == 0 )
	{
		return;
	}

	int iRow = GetIntrestedListItem();
	if( m_iCurrentRow == iRow )
	{
		if( m_iCurrentColumn > 0 )
		{
			m_iCurrentColumn --;
		}
		else
		{
			m_iCurrentColumn = GetColumnCounts() -1;
		}
	}
	else
	{
		m_iCurrentRow = iRow;
		if( m_iCurrentColumn == - 1 )
			m_iCurrentColumn = 0;
	}
	
	if( m_iCurrentRow == -1 )
	{
		m_iCurrentRow = 0;
	}
	
	SetItemState(m_iCurrentRow, LVIS_FOCUSED|LVIS_SELECTED, 
		LVIS_FOCUSED|LVIS_SELECTED);

	BeginEditLabel( m_iCurrentRow, m_iCurrentColumn );
}

// Navigate to next column and edit it.
void CShopItemListCtrl::EditColumnNext()
{
	if( GetColumnCounts() == 0 || GetItemCount() == 0 )
	{
		return;
	}
	
	int iRow = GetIntrestedListItem();
	if( m_iCurrentRow == iRow )
	{
		if( m_iCurrentColumn < GetColumnCounts() - 1 )
		{
			m_iCurrentColumn ++;
		}
		else
		{
			m_iCurrentColumn = 0;
		}
	}
	else
	{
		m_iCurrentRow = iRow;
		if( m_iCurrentColumn == - 1 )
			m_iCurrentColumn = 0;
	}	
	
	if( m_iCurrentRow == -1 )
	{
		m_iCurrentRow = 0;
	}

	SetItemState(m_iCurrentRow, LVIS_FOCUSED|LVIS_SELECTED, 
		LVIS_FOCUSED|LVIS_SELECTED);

	BeginEditLabel( m_iCurrentRow, m_iCurrentColumn );
}


void CShopItemListCtrl::OnLButtonDown(UINT iFlags, CPoint obPoint) 
{
	// 这里重新实现该函数，只是为了屏蔽基类实现的EditLabel()行为。
	CListCtrl::OnLButtonDown(iFlags, obPoint);
}

void CShopItemListCtrl::OnNMDblclk(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 1;
	
	// If column is not read only then
	// If the SHIFT or CTRL key is down call the base class
	// Check the high bit of GetKeyState to determine whether SHIFT or CTRL key is down
	if ((GetKeyState(VK_SHIFT) & 0x80) || (GetKeyState(VK_CONTROL) & 0x80))
	{
		return;
	}

	CPoint obPoint(::GetMessagePos());
	ScreenToClient(&obPoint);

	LVHITTESTINFO hti = {0};
	hti.pt = obPoint;
	hti.flags = 0;

	// Get the current column and row
	// 使用SubItemHitTest检测子项是否被点击，并返回子项的列编号。
	int iRet = SubItemHitTest(&hti );
	if( iRet == -1 )
	{
		return;
	}

	if( !(hti.flags & LVHT_ONITEMLABEL ) )
	{
		return;
	}

	BeginEditLabel( hti.iItem, hti.iSubItem);
}
