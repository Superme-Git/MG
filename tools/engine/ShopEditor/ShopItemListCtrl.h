#pragma once

#include "ComboListCtrl/ComboListCtrl.h"

// CShopItemListCtrl

class CShopItemListCtrl : public CComboListCtrl
{
	DECLARE_DYNAMIC(CShopItemListCtrl)

public:
	CShopItemListCtrl();
	virtual ~CShopItemListCtrl();

	// 得到用户当前最感兴趣的列表项目。
	int GetIntrestedListItem();

protected:

	// Status variables for tab-navigation.
	int m_iCurrentRow;
	int m_iCurrentColumn;

protected:
	DECLARE_MESSAGE_MAP()
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnNMDblclk(NMHDR *pNMHDR, LRESULT *pResult);
	// Navigate to next column and edit it.
	afx_msg void EditColumnFirst();
	afx_msg void EditColumnLast();
	afx_msg void EditColumnNext();
	afx_msg void EditColumnPrev();
};


