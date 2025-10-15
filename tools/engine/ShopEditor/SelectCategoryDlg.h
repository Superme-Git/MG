#pragma once
#include "afxcmn.h"
#include "ShopItemManager.h"

// CSelectCategoryDlg dialog

class CSelectCategoryDlg : public CDialog
{
	DECLARE_DYNAMIC(CSelectCategoryDlg)

public:
	CSelectCategoryDlg(void* scp = NULL, CWnd* pParent = NULL);   // standard constructor
	virtual ~CSelectCategoryDlg();

	CBigCategoryManager*		m_manager;
	CBigCategory*				m_selBcp;
	CSmallCategory*				m_selScp;
	CImageList					m_imageList;
	void*						curScp;
// Dialog Data
	enum { IDD = IDD_DIALOG_SELECTCATEGORY };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

	HTREEITEM AddBigCategoryToTree( const CBigCategory& bc, HTREEITEM hInsertAfter );

public:
	CTreeCtrl m_tcCategory;
	virtual BOOL OnInitDialog();

protected:
	virtual void OnOK();

public:
	afx_msg void OnTvnSelchangedTree1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMDblclkTree1(NMHDR *pNMHDR, LRESULT *pResult);
};
