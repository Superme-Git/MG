#pragma once
#include <afxcmn.h>

#include "../packzip/ComboListCtrl/ComboListCtrl.h"

// CEditMetaFileView form view

class CEditMetaFileView : public CFormView
{
	struct CMetaInfoPtr
	{
		LPCWSTR				m_name;
		PFS::CMetaInfo*		m_meta;
		CMetaInfoPtr() : m_name( NULL), m_meta( NULL) {}
	};

	DECLARE_DYNCREATE(CEditMetaFileView)

protected:
	CEditMetaFileView();           // protected constructor used by dynamic creation
	virtual ~CEditMetaFileView();

	std::vector<CMetaInfoPtr>	m_cache;	// 提高顺序查找效率，将CMetaInfoMap内容的指针放在vector里面

public:
	enum { IDD = IDD_DIALOG1 };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

public:
	CComboListCtrl		m_list;
	bool				m_first;

protected:
	// 编辑命令
	void SelectAll();
	void Delete();
	void Copy();

public:
	afx_msg void OnSize(UINT nType, int cx, int cy);
	virtual void OnInitialUpdate();
	afx_msg void OnLvnGetdispinfoList1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg LRESULT OnEndLabelEditVariableCriteria(WPARAM wParam, LPARAM lParam);
	virtual BOOL PreTranslateMessage(MSG* pMsg);

	afx_msg void OnUpdateSelectAll(CCmdUI *pCmdUI);
	afx_msg void OnUpdateDelete(CCmdUI *pCmdUI);
	afx_msg void OnUpdateCopy(CCmdUI *pCmdUI);
};


