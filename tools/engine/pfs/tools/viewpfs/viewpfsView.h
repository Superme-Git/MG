// viewpfsView.h : interface of the CviewpfsView class
//
#pragma once
#include <afxcview.h>

class CviewpfsView : public CListView
{
protected: // create from serialization only
	CviewpfsView();
	DECLARE_DYNCREATE(CviewpfsView)

// Attributes
public:
	CviewpfsDoc* GetDocument() const
	{ return reinterpret_cast<CviewpfsDoc*>(m_pDocument); }

// Operations
public:

// Overrides
public:
	virtual void OnDraw(CDC* pDC);  // overridden to draw this view
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:

// Implementation
public:
	virtual ~CviewpfsView();

protected:
	void InsertColumn();
	void FillCache();
	void SetItemCount();

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
	bool					m_first;
	std::vector<DWORD>		m_cache;		// ±£´æ ZIPFILE::CFileCentral µÄÖ¸Õë
	bool					m_bHex;
	bool					m_bRunZip;

public:
	virtual void OnInitialUpdate();
	afx_msg void OnLvnGetdispinfo(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnViewHexshow();
	afx_msg void OnUpdateViewHexshow(CCmdUI *pCmdUI);
	afx_msg void OnViewRunzipshow();
	afx_msg void OnUpdateViewRunzipshow(CCmdUI *pCmdUI);
};
