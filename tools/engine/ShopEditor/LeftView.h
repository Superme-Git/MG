#pragma once
#include "ShopItemManager.h"

// CLeftView view
class CRightView;
class CLeftView : public CTreeView
{
	DECLARE_DYNCREATE(CLeftView)

protected:
	CLeftView();           // protected constructor used by dynamic creation
	virtual ~CLeftView();

	CBigCategoryManager& GetShopItemManager();

	HTREEITEM AddBigCategoryToTree( const CBigCategory& bc, HTREEITEM hInsertAfter );
	HTREEITEM AddBigCategoryAndChildrenToTree( const CBigCategory& bc, HTREEITEM hInsertAfter );
	HTREEITEM AddSmallCategoryToTree( const CSmallCategory& sc, HTREEITEM hParent, HTREEITEM hInsertAfter );

	inline void SetModifyFlag()
	{
		GetDocument()->SetModifiedFlag( TRUE);
	}

public:
	CRightView*		m_pRightView;
	CImageList		m_imageList;

protected:
	DECLARE_MESSAGE_MAP()

public:
	afx_msg void OnAddBigCategory( );
	afx_msg void OnAddSmallCategory( );
	afx_msg void OnRenameCategory();
	afx_msg void OnDeleteBigCategory();
	afx_msg void OnMoveUpBigCategory();
	afx_msg void OnMoveDownBigCategory();

	afx_msg void OnNMRclick(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnTvnEndlabeledit(NMHDR *pNMHDR, LRESULT *pResult);

	virtual void OnInitialUpdate();
	afx_msg void OnTvnSelchanged(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnUpdateItemMoveup(CCmdUI *pCmdUI);
	afx_msg void OnUpdateItemMovedown(CCmdUI *pCmdUI);
	afx_msg void OnUpdateCategoryAdd(CCmdUI *pCmdUI);
	afx_msg void OnUpdateCategoryDelete(CCmdUI *pCmdUI);
	afx_msg void OnUpdateCategoryRename(CCmdUI *pCmdUI);
protected:
	virtual void OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/);
};


