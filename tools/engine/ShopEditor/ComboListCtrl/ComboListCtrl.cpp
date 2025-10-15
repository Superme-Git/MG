/*******************************************************************************
	Author						: Aravindan Premkumar
	Unregistered Copyright 2003	: Aravindan Premkumar
	All Rights Reserved
	
	This piece of code does not have any registered copyright and is free to be 
	used as necessary. The user is free to modify as per the requirements. As a
	fellow developer, all that I expect and request for is to be given the 
	credit for intially developing this reusable code by not removing my name as 
	the author.
*******************************************************************************/
#include "stdafx.h"
#include "ComboListCtrl.h"
#include "InPlaceCombo.h"
#include "InPlaceEdit.h"
#include "InPlaceCheckBox.h"

#define _Trace(x)

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

//#defines
#define FIRST_COLUMN				0
#define MIN_COLUMN_WIDTH			10
#define MAX_DROP_DOWN_ITEM_COUNT	40

/////////////////////////////////////////////////////////////////////////////
// CComboListCtrl
IMPLEMENT_DYNAMIC(CComboListCtrl, CListCtrl)

CComboListCtrl::CComboListCtrl()
{
	m_iColumnCounts = 0;
	m_ComboSupportColumnsList.RemoveAll();
	m_ReadOnlyColumnsList.RemoveAll();
	m_strValidEditCtrlChars.Empty();
	m_dwEditCtrlStyle = ES_AUTOHSCROLL | ES_AUTOVSCROLL | ES_LEFT | ES_NOHIDESEL;
	m_dwDropDownCtrlStyle = WS_BORDER | WS_CHILD | WS_VISIBLE | ES_AUTOHSCROLL | ES_AUTOVSCROLL | 
							CBS_DROPDOWNLIST | CBS_DISABLENOSCROLL;
	m_dwCheckBoxCtrlStyle = WS_CHILD | WS_VISIBLE | BS_AUTOCHECKBOX;
}

CComboListCtrl::~CComboListCtrl()
{
	CInPlaceCombo::DeleteInstance();
	CInPlaceEdit::DeleteInstance();  
	CInPlaceCheckBox::DeleteInstance();
}

BEGIN_MESSAGE_MAP(CComboListCtrl, CListCtrl)
	//{{AFX_MSG_MAP(CComboListCtrl)
	ON_WM_HSCROLL()
	ON_WM_VSCROLL()
	ON_WM_LBUTTONDOWN()
	ON_NOTIFY_REFLECT(LVN_BEGINLABELEDIT, OnBeginLabelEdit)
	ON_NOTIFY_REFLECT(LVN_ENDLABELEDIT, OnEndLabelEdit)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CComboListCtrl message handlers
//

//
// Tiggers OnBeginLabelEdit to edit the cell of the list.
//
// Parameters:
// iRowIndex		- The row index of the cell in the list.
// iColumnIndex		- The column index of the cell in the list.
//
// Returns the window handle of the edit control. NULL if fail to edit.
//
HWND CComboListCtrl::BeginEditLabel( int iRowIndex, int iColumnIndex )
{
	if (-1 == iRowIndex)
	{
		return NULL;
	}

	UINT flag = LVIS_FOCUSED;
	
	if ((GetItemState(iRowIndex, flag ) & flag) != flag)
	{
		return NULL;
	}

	// Add check for LVS_EDITLABELS
	if ( !( GetWindowLong(m_hWnd, GWL_STYLE) & LVS_EDITLABELS) )
	{
		return NULL;
	}
	
	bool bEditLabel = FALSE;
	CInPlaceCombo* pInPlaceCombo = CInPlaceCombo::GetInstance();
	if( pInPlaceCombo && ::IsWindow( pInPlaceCombo->GetSafeHwnd()))
	{
		bEditLabel = TRUE;
	}
	if( !bEditLabel )
	{
		CInPlaceEdit* pInPlaceEdit = CInPlaceEdit::GetInstance();
		if( pInPlaceEdit && ::IsWindow( pInPlaceEdit->GetSafeHwnd()))
		{
			bEditLabel = TRUE;
		}
	}
	if( !bEditLabel )
	{
		CInPlaceCheckBox* pInPlaceCheckBox = CInPlaceCheckBox::GetInstance();
		if( pInPlaceCheckBox && ::IsWindow( pInPlaceCheckBox->GetSafeHwnd()))
		{
			bEditLabel = TRUE;
		}
	}
	if( bEditLabel )
	{
		CancelEditLabel();
		_Trace(L"CancelEditLabel called.\n");
	}

	// Get the current selection before creating the in place combo box
	CString strCurSelection = GetItemText(iRowIndex, iColumnIndex);

	// If combo box is supported
	// Create and show the in place combo box
	if (IsCombo(iColumnIndex))
	{
		CStringList obComboItemsList;

		GetParent()->SendMessage(WM_SET_ITEMS, (WPARAM)iColumnIndex, (LPARAM)&obComboItemsList);  

		CInPlaceCombo* pInPlaceComboBox = 
			ShowInPlaceList(iRowIndex, iColumnIndex, obComboItemsList, strCurSelection);
		ASSERT(pInPlaceComboBox); 

		_Trace(strCurSelection);
		_Trace(L" -- pInPlaceComboBox\n");
		// Set the selection to previous selection
		pInPlaceComboBox->SelectString(-1, strCurSelection);
		return pInPlaceComboBox->GetSafeHwnd();
	}
	else if( IsCheckBox(iColumnIndex))
	{
		_Trace(strCurSelection);
		_Trace(L" -- CInPlaceCheckBox\n");
		CInPlaceCheckBox* pInPlaceCheckBox = 
			ShowInPlaceCheckBox(iRowIndex, iColumnIndex, strCurSelection);
		return pInPlaceCheckBox->GetSafeHwnd();
	}
	// If combo box is not read only
	// Create and show the in place edit control
	else if (!IsReadOnly(iColumnIndex))
	{
		_Trace(strCurSelection);
		_Trace(L" -- CInPlaceEdit\n");

		CInPlaceEdit* pInPlaceEdit = ShowInPlaceEdit(iRowIndex, iColumnIndex, strCurSelection);
		return pInPlaceEdit->GetSafeHwnd();
	}
	else
	{
		// Ensure Column is visible. 
		CRect obCellRect(0, 0, 0, 0);
		CalculateCellRect(iColumnIndex, iRowIndex, obCellRect);
		
		_Trace(strCurSelection);
		_Trace(L" -- ReadOnly\n");
		return NULL;
	}
}

CInPlaceCombo* CComboListCtrl::ShowInPlaceList(int iRowIndex, int iColumnIndex, CStringList& rComboItemsList, 
											   CString strCurSelecetion /*= ""*/, int iSel /*= -1*/)
{
	// The returned obPointer should not be saved
	
	// Make sure that the item is visible
	if (!EnsureVisible(iRowIndex, TRUE))
	{
		return NULL;
	}

	// Make sure that iColumnIndex is valid 
	CHeaderCtrl* pHeader = static_cast<CHeaderCtrl*> (GetDlgItem(FIRST_COLUMN));

	int iColumnCount = pHeader->GetItemCount();

	if (iColumnIndex >= iColumnCount || GetColumnWidth(iColumnIndex) < MIN_COLUMN_WIDTH) 
	{
		return NULL;
	}

	// Calculate the rectangle specifications for the combo box
	CRect obCellRect(0, 0, 0, 0);
	CalculateCellRect(iColumnIndex, iRowIndex, obCellRect);

	int iHeight = obCellRect.Height();  
	int iCount = (int )rComboItemsList.GetCount();

	iCount = (iCount < MAX_DROP_DOWN_ITEM_COUNT) ? 
		iCount + MAX_DROP_DOWN_ITEM_COUNT : (MAX_DROP_DOWN_ITEM_COUNT + 1); 

	obCellRect.bottom += iHeight * iCount; 

	// Create the in place combobox
	CInPlaceCombo* pInPlaceCombo = CInPlaceCombo::GetInstance();
	pInPlaceCombo->ShowComboCtrl(m_dwDropDownCtrlStyle, obCellRect, this, 0, iRowIndex, iColumnIndex, &rComboItemsList, 
								 strCurSelecetion, iSel);
	
	return pInPlaceCombo;
}

CInPlaceEdit* CComboListCtrl::ShowInPlaceEdit(int iRowIndex, int iColumnIndex, CString& rstrCurSelection)
{
	// Create an in-place edit control
	CInPlaceEdit* pInPlaceEdit = CInPlaceEdit::GetInstance();
		
	CRect obCellRect(0, 0, 0, 0);
	CalculateCellRect(iColumnIndex, iRowIndex, obCellRect);
	
	pInPlaceEdit->ShowEditCtrl(m_dwEditCtrlStyle | WS_BORDER, obCellRect, this, 0, 
							   iRowIndex, iColumnIndex,
							   m_strValidChars[iColumnIndex], rstrCurSelection);

	return pInPlaceEdit;
}

// Creates and displays the in place checkbox control
CInPlaceCheckBox* CComboListCtrl::ShowInPlaceCheckBox(int iRowIndex, int iColumnIndex, CString& strCurSelecetion)
{
	CInPlaceCheckBox* pInPlaceCheckBox = CInPlaceCheckBox::GetInstance();

	CRect obCellRect(0, 0, 0, 0);
	CalculateCellRect(iColumnIndex, iRowIndex, obCellRect);

	pInPlaceCheckBox->ShowCheckBox( m_dwCheckBoxCtrlStyle, obCellRect, this, 0, 
		iRowIndex, iColumnIndex, strCurSelecetion);

	return pInPlaceCheckBox;
}

void CComboListCtrl::OnHScroll(UINT iSBCode, UINT iPos, CScrollBar* pScrollBar) 
{
	if (GetFocus() != this)
	{
		SetFocus();
	}

	CListCtrl::OnHScroll(iSBCode, iPos, pScrollBar);
}

void CComboListCtrl::OnVScroll(UINT iSBCode, UINT iPos, CScrollBar* pScrollBar) 
{
	if (GetFocus() != this)
	{
		SetFocus();
	}

	CListCtrl::OnVScroll(iSBCode, iPos, pScrollBar);
}

void CComboListCtrl::OnLButtonDown(UINT iFlags, CPoint obPoint) 
{
	CListCtrl::OnLButtonDown(iFlags, obPoint);

	int iColumnIndex = -1;
	int iRowIndex = -1;

	// Get the current column and row
	if (!SubItemHitTestEx(obPoint, &iRowIndex, &iColumnIndex))
	{
		CancelEditLabel();
		return;
	}

	// If column is not read only then
	// If the SHIFT or CTRL key is down call the base class
	// Check the high bit of GetKeyState to determine whether SHIFT or CTRL key is down
	if ((GetKeyState(VK_SHIFT) & 0x80) || (GetKeyState(VK_CONTROL) & 0x80))
	{
		CancelEditLabel();
		return;
	}

	BeginEditLabel( iRowIndex, iColumnIndex );
}

bool CComboListCtrl::SubItemHitTestEx(CPoint &obPoint, int* pRowIndex, int* pColumnIndex)
{
	if (!pRowIndex || !pColumnIndex)
	{
		return false;
	}

	// Make sure that the ListView is in LVS_REPORT
	if ((GetWindowLong(m_hWnd, GWL_STYLE) & LVS_TYPEMASK) != LVS_REPORT)
	{
		return false;
	}

	// Get the row index
	LVHITTESTINFO hti = {0};
	hti.pt = obPoint;
	hti.flags = 0;

	// 使用SubItemHitTest检测子项是否被点击，并返回子项的列编号。
	int iRet = SubItemHitTest(&hti );
	if( iRet == -1 )
	{
		return false;
	}

	if( hti.flags & LVHT_ONITEMLABEL )
	{
		*pRowIndex = hti.iItem;
		*pColumnIndex = hti.iSubItem;
		return true;
	}

	return false;
}

void CComboListCtrl::SetComboColumns(int iColumnIndex, bool bSet /*= true*/)
{
	// If the Column Index is not present && Set flag is false
	// Then do nothing 
	// If the Column Index is present && Set flag is true
	// Then do nothing
	POSITION Pos = m_ComboSupportColumnsList.Find(iColumnIndex);

	// If the Column Index is not present && Set flag is true
	// Then Add to list
	if ((NULL == Pos) && bSet) 
	{
		m_ComboSupportColumnsList.AddTail(iColumnIndex); 
	}

	// If the Column Index is present && Set flag is false
	// Then Remove from list
	if ((NULL != Pos) && !bSet) 
	{
		m_ComboSupportColumnsList.RemoveAt(Pos); 
	}
}

void CComboListCtrl::SetCheckBoxColumns(int iColumnIndex, bool bSet /*= true*/)
{
	// If the Column Index is not present && Set flag is false
	// Then do nothing 
	// If the Column Index is present && Set flag is true
	// Then do nothing
	POSITION Pos = m_CheckBoxColumnsList.Find( iColumnIndex);

	// If the Column Index is not present && Set flag is true
	// Then Add to list
	if ((NULL == Pos) && bSet) 
	{
		m_CheckBoxColumnsList.AddTail(iColumnIndex); 
	}

	// If the Column Index is present && Set flag is false
	// Then Remove from list
	if ((NULL != Pos) && !bSet) 
	{
		m_CheckBoxColumnsList.RemoveAt(Pos); 
	}
}

void CComboListCtrl::SetReadOnlyColumns(int iColumnIndex, bool bSet /*= true*/)
{
	// If the Column Index is not present && Set flag is false
	// Then do nothing 
	// If the Column Index is present && Set flag is true
	// Then do nothing
	POSITION Pos = m_ReadOnlyColumnsList.Find(iColumnIndex);

	// If the Column Index is not present && Set flag is true
	// Then Add to list
	if ((NULL == Pos) && bSet) 
	{
		m_ReadOnlyColumnsList.AddTail(iColumnIndex); 
	}

	// If the Column Index is present && Set flag is false
	// Then Remove from list
	if ((NULL != Pos) && !bSet) 
	{
		m_ReadOnlyColumnsList.RemoveAt(Pos); 
	}
}

bool CComboListCtrl::IsReadOnly(int iColumnIndex)
{
	if (m_ReadOnlyColumnsList.Find(iColumnIndex))
	{
		return true;
	}
	
	return false;
}

bool CComboListCtrl::IsCombo(int iColumnIndex)
{
	if (m_ComboSupportColumnsList.Find(iColumnIndex))
	{
		return true;
	}

	return false;
}

bool CComboListCtrl::IsCheckBox( int iColumnIndex)
{
	if (m_CheckBoxColumnsList.Find(iColumnIndex))
	{
		return true;
	}

	return false;
}

void CComboListCtrl::CalculateCellRect(int iColumnIndex, int iRowIndex, CRect& robCellRect)
{
	GetItemRect(iRowIndex, &robCellRect, iColumnIndex ? LVIR_BOUNDS : LVIR_LABEL);
	
	CRect rcClient;
	GetClientRect(&rcClient);

	if (robCellRect.right > rcClient.right) 
	{
		robCellRect.right = rcClient.right;
	}

	ScrollToView(iColumnIndex, robCellRect);
}


void CComboListCtrl::OnBeginLabelEdit(NMHDR* pNMHDR, LRESULT* pResult) 
{
	LV_DISPINFO* pDispInfo = (LV_DISPINFO*)pNMHDR;
	
	if (IsReadOnly(pDispInfo->item.iSubItem))
	{
		*pResult = 1;
		return;
	}

	*pResult = 0;
}

void CComboListCtrl::OnEndLabelEdit(NMHDR* pNMHDR, LRESULT* pResult) 
{
	LV_DISPINFO* pDispInfo = (LV_DISPINFO*)pNMHDR;
	
	// Update the item text with the new text
	if( pDispInfo->item.pszText != NULL )
	{
		_Trace(pDispInfo->item.pszText);
		_Trace(L" -- OnEndLabelEdit\n");

		if( (GetStyle() & LVS_OWNERDATA) == 0  )
		{
			SetItemText(pDispInfo->item.iItem, pDispInfo->item.iSubItem, pDispInfo->item.pszText);
		}

		GetParent()->SendMessage(WM_VALIDATE, GetDlgCtrlID(), (LPARAM)pDispInfo); 
	}
	else
	{
		_Trace(L" Cancel -- OnEndLabelEdit\n");
	}	

	*pResult = 1;
}

void CComboListCtrl::SetValidEditCtrlCharacters(CString &rstrValidCharacters)
{
	m_strValidEditCtrlChars = rstrValidCharacters;
}

void CComboListCtrl::SetColumnValidEditCtrlCharacters(CString &rstrValidCharacters,int column)
{
	if(column>MAX_LISTCTRL_COLUMNS-1)
		return;
	if(column == -1)
	{
		for(int i=0;i<MAX_LISTCTRL_COLUMNS;i++)
		{
			m_strValidChars[i] = rstrValidCharacters;
		}
	}
	else
		m_strValidChars[column] = rstrValidCharacters;
}

void CComboListCtrl::EnableHScroll(bool bEnable /*= true*/)
{
	if (bEnable)
	{
		m_dwDropDownCtrlStyle |= WS_HSCROLL;
	}
	else
	{
		m_dwDropDownCtrlStyle &= ~WS_HSCROLL;
	}	
}

void CComboListCtrl::EnableVScroll(bool bEnable /*= true*/)
{
	if (bEnable)
	{
		m_dwDropDownCtrlStyle |= WS_VSCROLL;
	}
	else
	{
		m_dwDropDownCtrlStyle &= ~WS_VSCROLL;
	}
}

void CComboListCtrl::ScrollToView(int iColumnIndex, /*int iOffSet, */CRect& robCellRect)
{
	// Now scroll if we need to expose the column
	CRect rcClient;
	GetClientRect(&rcClient);

	int iColumnWidth = GetColumnWidth(iColumnIndex);

	// Get the column iOffset
	int iOffSet = 0;
	for (int iIndex_ = 0; iIndex_ < iColumnIndex; iIndex_++)
	{
		iOffSet += GetColumnWidth(iIndex_);
	}

	// If x1 of cell rect is < x1 of ctrl rect or
	// If x1 of cell rect is > x1 of ctrl rect or **Should not ideally happen**
	// If the width of the cell extends beyond x2 of ctrl rect then
	// Scroll

	CSize obScrollSize(0, 0);

	if (((iOffSet + robCellRect.left) < rcClient.left) || 
		((iOffSet + robCellRect.left) > rcClient.right))
	{
		obScrollSize.cx = iOffSet + robCellRect.left;
	}
	else if ((iOffSet + robCellRect.left + iColumnWidth) > rcClient.right)
	{
		obScrollSize.cx = iOffSet + robCellRect.left + iColumnWidth - rcClient.right;
	}

	Scroll(obScrollSize);
	robCellRect.left -= obScrollSize.cx;
	
	// Set the width to the column width
	robCellRect.left += iOffSet;
	robCellRect.right = robCellRect.left + iColumnWidth;
}



int CComboListCtrl::InsertColumn(int nCol,LPCTSTR lpszColumnHeading,int nFormat ,int nWidth,int nSubItem)
{
	m_iColumnCounts++;
	return CListCtrl::InsertColumn( nCol, lpszColumnHeading, nFormat, nWidth, nSubItem);
}

int CComboListCtrl::GetColumnCounts()
{
	return m_iColumnCounts;
}

void CComboListCtrl::DeleteAllColumn()
{
	for(int i=0;i<m_iColumnCounts;i++)
	{
		DeleteColumn(0);
	}
}

void CComboListCtrl::HideAllInPlaceCtrl()
{
	CInPlaceCombo* pInPlaceCombo = CInPlaceCombo::GetInstance();
	if( pInPlaceCombo && ::IsWindow( pInPlaceCombo->GetSafeHwnd()))
	{
		pInPlaceCombo->SendMessage( WM_CLOSE);
		_Trace(L"pInPlaceCombo closed.\n");
	}
	CInPlaceEdit* pInPlaceEdit = CInPlaceEdit::GetInstance();
	if( pInPlaceEdit && ::IsWindow( pInPlaceEdit->GetSafeHwnd()))
	{
		pInPlaceEdit->SendMessage( WM_CLOSE);
		_Trace(L"pInPlaceEdit closed.\n");
	}
	CInPlaceCheckBox* pInPlaceCheckBox = CInPlaceCheckBox::GetInstance();
	if( pInPlaceCheckBox && ::IsWindow( pInPlaceCheckBox->GetSafeHwnd()))
	{
		pInPlaceCheckBox->SendMessage( WM_CLOSE);
		_Trace(L"pInPlaceCheckBox closed.\n");
	}	
}

void CComboListCtrl::DrawItem(LPDRAWITEMSTRUCT lpDrawItemStruct)
{
}

BOOL CComboListCtrl::OnWndMsg(UINT message, WPARAM wParam, LPARAM lParam, LRESULT* pResult)
{
	if( message == LVM_EDITLABEL )
	{
		if( (int)wParam == -1 )
		{
			HideAllInPlaceCtrl();
			*pResult = 0;
		}
		else
		{
			*pResult = (LRESULT)BeginEditLabel(wParam, 0 );
		}
		
		return TRUE;
	}
	else if( message == LVM_CANCELEDITLABEL)
	{
		HideAllInPlaceCtrl();
	}
	return CListCtrl::OnWndMsg(message, wParam, lParam, pResult);
}

