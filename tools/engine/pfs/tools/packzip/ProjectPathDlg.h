#pragma once
#include "Project.h"
#include "afxcmn.h"

// CProjectPathDlg dialog

class CProjectPathDlg : public CDialog
{
	DECLARE_DYNAMIC(CProjectPathDlg)

public:
	CProjectPathDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CProjectPathDlg();

	void OpenProject( CProject* project) { m_project = project; }

// Dialog Data
	enum { IDD = IDD_PROJECTPATH };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	CProject* m_project;

	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnInitDialog();
	CListCtrl m_paths;
	afx_msg void OnBnClickedButton1();
	bool		m_bChanged;

protected:
	virtual void OnOK();
};
