#pragma once
#include "afxcmn.h"



class CExlvItemData
{
public:
	~CExlvItemData() {}
};

class CExlvItem{
public:
	~CExlvItem()
	{
		if (m_pItem)
			delete (m_pItem);
		if (m_pData)
			delete (m_pData);
	}
	
	CExlvItem():m_pItem(new LVITEM()), m_pData(NULL) {
		ZeroMemory(m_pItem, sizeof (LVITEM));
	}
	CExlvItemData* m_pData;
	LVITEM* m_pItem;

private:
	CExlvItem& operator = (const CExlvItem& x);
};

class CDragListCtrl :
	public CListCtrl
{
private:
	void DropItemOntoList(CListCtrl* pSourceList, CListCtrl* pDestList);
	int			m_nDropIndex;	
	int			m_nDragIndex;	
	bool		m_bDragging;	
	CImageList* m_pDragImage;	
	CListCtrl*	m_pDestList;	

protected:
	virtual void ItemDataToList(const CExlvItem* pExlvItem, CListCtrl* pList) {}
	virtual void ListToItemData(CExlvItem* pExlvItem, CListCtrl* pList) {}

public:
	int GetDropIndex() const { return m_nDropIndex; }
	int GetDragIndex() const { return m_nDragIndex; }
	bool isDragging() {return m_bDragging;}
	~CDragListCtrl(void);
	CDragListCtrl(void);
	UINT static const DRAGLISTCTRL_LVN_ENDDRAGGING;

	DECLARE_MESSAGE_MAP()
	afx_msg void OnLvnEndlabeledit(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnLvnBegindrag(NMHDR *pNMHDR, LRESULT *pResult);

};
