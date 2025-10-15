#pragma once


// CCheckReportView view

class CCheckReportView : public CEditView
{
	DECLARE_DYNCREATE(CCheckReportView)

protected:
	CCheckReportView();           // protected constructor used by dynamic creation
	virtual ~CCheckReportView();

public:
protected:
	DECLARE_MESSAGE_MAP()

	// Handler of WM_GLOBALSTATUS
	afx_msg LRESULT OnGlobalStatus( WPARAM wParam, LPARAM lParam );
public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
};


