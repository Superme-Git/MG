// viewxmlView.h : interface of the CviewxmlView class
//
#pragma once
#include <sstream>

class CviewxmlDoc;
class CviewxmlView : public CTreeView
{
protected: // create from serialization only
	CviewxmlView();
	DECLARE_DYNCREATE(CviewxmlView)

// Attributes
public:
	CviewxmlDoc* GetDocument() const
   { return reinterpret_cast<CviewxmlDoc*>(m_pDocument); }

// Operations
public:

// Overrides
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual void OnInitialUpdate(); // called first time after construct

// Implementation
public:
	virtual ~CviewxmlView();

protected:

	std::wostringstream		m_wos;
	std::wstring			m_name;
	std::wstring			m_value;

	HTREEITEM				m_curItem;
	CEdit					m_editTip;
	int						m_fontHeight;

	void InsertToTree( XMLIO::CINode& node, HTREEITEM hParent);

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg HBRUSH OnCtlColor(CDC* pDC, CWnd* pWnd, UINT nCtlColor);
	afx_msg void OnTimer(UINT_PTR nIDEvent);
	afx_msg BOOL OnMouseWheel(UINT nFlags, short zDelta, CPoint pt);
};
