#pragma once

// CRightView form view

#include "ShopItemManager.h"
#include "ShopItemListCtrl.h"

class CRightView : public CFormView
{
	DECLARE_DYNCREATE(CRightView)

protected:
	CRightView();           // protected constructor used by dynamic creation
	virtual ~CRightView();

	CShopItemListCtrl	m_list;

	bool				m_first;
	CBigCategory*		m_curBcp;
	CSmallCategory*		m_curScp;
	int					m_rightHitItem;

public:
	enum { IDD = IDD_DIALOG_RIGHT };

	int AddShopItemToList( const CShopItem& si, int index);

	bool AddAllShopItemsToList( CSmallCategory* bcp, bool);

	void ClearShopItemsList();

	inline void SetModifyFlag()
	{
		GetDocument()->SetModifiedFlag( TRUE);
	}
	
	// 增加访问和设置大类小类的接口
	CBigCategory* GetBigCategory() { return m_curBcp;}

	CSmallCategory* GetSmallCategory() { return m_curScp;}

	void SetBigCategory(CBigCategory* bcp) { m_curBcp = bcp ;}

	void SetSmallCategory(CSmallCategory* scp) { m_curScp = scp ;}

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);

	// 设置List的flag文本
	void SetListFlagText( int index, int col, int flag, int flagType);

public:
	afx_msg void OnSize(UINT nType, int cx, int cy);
	virtual void OnInitialUpdate();
	afx_msg LRESULT OnEndLabelEditVariableCriteria(WPARAM wParam, LPARAM lParam);
	afx_msg void OnNMRclickList1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLvnItemchangedList1(NMHDR *pNMHDR, LRESULT *pResult);
	LRESULT PopulateComboList(WPARAM wParam, LPARAM lParam);

	afx_msg void OnAddShopItem();
	afx_msg void OnDeleteShopItem();
	afx_msg void OnMoveUpShopItem();
	afx_msg void OnMoveDownShopItem();
	afx_msg void OnMoveToShopItem();
	afx_msg void OnCopyToShopItem();

	afx_msg void OnCutShopItems();
	afx_msg void OnPasteShopItems();
	afx_msg void OnCopyShopItems();

	afx_msg void OnUpdateNeedItemSelected(CCmdUI *pCmdUI);

	afx_msg void OnUpdateItemMoveup(CCmdUI *pCmdUI);
	afx_msg void OnUpdateItemMovedown(CCmdUI *pCmdUI);
	afx_msg void OnUpdateItemRename(CCmdUI *pCmdUI);
	afx_msg void OnItemRename();
	virtual BOOL OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo);
};


