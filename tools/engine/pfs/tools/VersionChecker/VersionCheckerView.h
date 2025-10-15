// VersionCheckerView.h : interface of the CVersionCheckerView class
//


#pragma once


class CVersionCheckerView : public CListView
{
protected: // create from serialization only
	CVersionCheckerView();
	DECLARE_DYNCREATE(CVersionCheckerView)

// Attributes
public:
	CVersionCheckerDoc* GetDocument() const;

// Operations
public:

// Overrides
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual void OnInitialUpdate(); // called first time after construct

// Implementation
public:
	virtual ~CVersionCheckerView();

protected:

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg LRESULT OnDataFetch(WPARAM wParam, LPARAM lParam ); // Handler of WM_DATAFETCH
	afx_msg LRESULT OnDataReady(WPARAM wParam, LPARAM lParam ); // Handler of WM_DATAREADY
	afx_msg LRESULT OnDataLost(WPARAM wParam, LPARAM lParam ); // Handler of WM_DATALOST
};

inline CVersionCheckerDoc* CVersionCheckerView::GetDocument() const
   { return reinterpret_cast<CVersionCheckerDoc*>(m_pDocument); }

