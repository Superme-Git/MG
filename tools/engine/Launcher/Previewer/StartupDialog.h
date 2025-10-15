#pragma once


// CStartupDialog dialog

class CStartupDialog : public CDialog
{
	DECLARE_DYNAMIC(CStartupDialog)

public:
	CStartupDialog(CWnd* pParent = NULL);   // standard constructor
	virtual ~CStartupDialog();

// Dialog Data
	enum { IDD = IDD_STARTUP_DIALOG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedPatcherMainDialog();
	afx_msg void OnBnClickedWebpageControl();
	afx_msg void OnBnClickedUsage();
	afx_msg void OnBnClickedSelectServerDialog();
};
