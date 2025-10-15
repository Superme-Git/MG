#pragma once
#include "afxwin.h"

// CRecentProjectDlg dialog

class CRecentProjectDlg : public CDialog
{
	DECLARE_DYNAMIC(CRecentProjectDlg)

public:
	CRecentProjectDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CRecentProjectDlg();

	BOOL Create( CWnd* parent) { return CDialog::Create( CRecentProjectDlg::IDD, parent); }

	void OpenProject();
// Dialog Data
	enum { IDD = IDD_RECENTPROJECT };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
	
	CListBox m_recents;

public:
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedButtonOpen();
	afx_msg void OnBnClickedButtonNew();
public:
	afx_msg void OnLbnDblclkList1();
};
