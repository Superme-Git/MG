#pragma once


// CBootDlg dialog

class CBootDlg : public CDialog
{
	DECLARE_DYNAMIC(CBootDlg)

public:
	CBootDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CBootDlg();

// Dialog Data
	enum { IDD = IDD_DIALOG_BOOT };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnMakeDifference();
	afx_msg void OnMergePatch();
};
